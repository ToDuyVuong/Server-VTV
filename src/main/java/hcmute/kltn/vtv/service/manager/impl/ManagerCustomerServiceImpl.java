package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ListCustomerManagerResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.manager.ManagerShopRepository;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerCustomerServiceImpl implements IManagerCustomerService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    private ManagerShopRepository managerShopRepository;

    @Override
    public ListCustomerManagerResponse getListCustomerByStatus(int size, int page, Status status) {
        int totalCustomer = customerRepository.countAllByStatus(status);
        int totalPage = (int) Math.ceil((double) totalCustomer / size);

        Page<Customer> customers = customerRepository.findAllByStatus(status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));

        return listCustomerAdminResponse(customers.getContent(),
                size, page, totalPage, "Lấy danh sách khách hàng theo trạng thái thành công!");
    }

    @Override
    public ListCustomerManagerResponse getListCustomerByStatusSort(int size, int page, Status status, String sort) {
        int totalCustomer = customerRepository.countAllByStatus(status);
        int totalPage = (int) Math.ceil((double) totalCustomer / size);
        Page<Customer> customers;
        String message;

        switch (sort) {
            case "name-asc" -> {
                customers = customerRepository.findAllByStatusOrderByFullName(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo tên tăng dần và trạng thái thành công!";
            }
            case "name-desc" -> {
                customers = customerRepository
                        .findAllByStatusOrderByFullNameDesc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo tên giảm dần và trạng thái thành công!";
            }
            case "at-asc" -> {
                customers = customerRepository.findAllByStatusOrderByCreateAtAsc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo ngày tạo tăng dần và trạng thái thành công!";
            }
            case "at-desc" -> {
                customers = customerRepository
                        .findAllByStatusOrderByCreateAtDesc(status, PageRequest.of(page - 1, size))
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
                message = "Lọc danh sách khách hàng theo ngày tạo giảm dần và trạng thái thành công!";
            }
            default -> {
                return getListCustomerByStatus(size, page, status);
            }
        }

        return listCustomerAdminResponse(customers.getContent(), size, page, totalPage, message);

    }

    @Override
    public ListCustomerManagerResponse searchCustomerByStatus(int size, int page, Status status, String search) {
        int totalCustomer = customerRepository.countAllByFullNameContainingAndStatus(search, status);
        int totalPage = (int) Math.ceil((double) totalCustomer / size);

        Page<Customer> customers = customerRepository.findAllByFullNameContainingAndStatus(search, status,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách khách hàng"));
        String message = "Tìm kiếm danh sách khách hàng theo tên và trạng thái thành công!";

        return listCustomerAdminResponse(customers.getContent(), size, page, totalPage, message);
    }

    @Override
    public ProfileCustomerResponse getCustomerDetailByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new NotFoundException("Mã khách hàng không được để trống!");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng không tồn tại."));
        CustomerDTO customerDTO = CustomerDTO.convertEntityToDTO(customer);

        ProfileCustomerResponse response = new ProfileCustomerResponse();
        response.setCustomerDTO(customerDTO);
        response.setMessage("Lấy thông tin khách hàng thành công.");
        response.setStatus("ok");
        response.setCode(200);

        return response;
    }

    public ListCustomerManagerResponse listCustomerAdminResponse(List<Customer> customers,
                                                                 int size, int page,
                                                                 int totalPage, String message) {
        ListCustomerManagerResponse response = new ListCustomerManagerResponse();
        response.setCount(customers.size());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPage(totalPage);
        response.setCustomerDTOs(CustomerDTO.convertEntitiesToDTOs(customers));
        response.setMessage(message);
        response.setCode(200);
        response.setStatus("OK");

        return response;
    }

    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
        }
        if (size > 500) {
            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
        }
    }

    @Override
    public void checkRequestSortParams(String sort) {
        if (sort == null) {
            throw new NotFoundException("Tham số sắp xếp không được để trống!");
        }
        if (!sort.equals("name-asc") && !sort.equals("name-desc") && !sort.equals("at-asc")
                && !sort.equals("at-desc")) {
            throw new NotFoundException(
                    "Tham số sắp xếp không hợp lệ! Tham số sắp xếp phải là name-asc, name-desc, at-asc, at-desc");
        }
    }

    @Override
    @Transactional
    public void updateRoleWithCustomer(Long customerId, Role role) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng không tồn tại."));
        customer.addRole(role);
        customer.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật quyền cho tài khoản thất bại!");
        }
    }

}
