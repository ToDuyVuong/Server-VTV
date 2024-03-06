package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.AddressRequest;
import hcmute.kltn.vtv.model.data.user.request.AddressStatusRequest;
import hcmute.kltn.vtv.model.data.user.response.AddressResponse;
import hcmute.kltn.vtv.model.data.user.response.ListAddressResponse;
import hcmute.kltn.vtv.model.dto.user.AddressDTO;
import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.AddressRepository;
import hcmute.kltn.vtv.service.user.IAddressService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private final AddressRepository addressRepository;
    @Autowired
    private final ICustomerService customerService;


    @Override
    @Transactional
    public AddressResponse addNewAddress(AddressRequest request) {
        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        Address address = createAddressByAddressRequest(request);
        address.setCustomer(customer);

        try {
            addressRepository.save(address);
            updateStatusInActiveWithAddress(customer, address.getAddressId());
            String message = "Thêm địa chỉ mới của khách hàng: " + customer.getFullName() + " thành công.";

            return AddressResponse.addressResponse(address, customer, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Thêm địa chỉ mới thất bại.");
        }

    }

    @Override
    public AddressResponse getAddressById(Long addressId, String username) {
        Address address = checkAddress(addressId, username);

        return AddressResponse.addressResponse(address, address.getCustomer(),
                "Lấy thông tin địa chỉ thành công.", "OK");
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(AddressRequest request) {
        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        Address address = checkAddress(request.getAddressId(), request.getUsername());

        try {
            addressRepository.save(editAddressByAddressRequest(address, request));
            String message = "Cập nhật địa chỉ của khách hàng: " + customer.getFullName() + " thành công.";

            return AddressResponse.addressResponse(address, customer, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật địa chỉ mới thất bại.");
        }

    }

    @Override
    @Transactional
    public AddressResponse updateStatusAddress(AddressStatusRequest request) {
        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        Address address = checkAddress(request.getAddressId(), request.getUsername());

        address.setUpdateAt(LocalDateTime.now());
        address.setStatus(request.getStatus());
        checkStatus(request, address, customer, request.getAddressId());

        try {
            addressRepository.save(address);
            String message = setMessageUpdateStatus(request.getStatus(), customer.getFullName());

            return AddressResponse.addressResponse(address, customer, message, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái địa chỉ mới thất bại.");
        }


    }

    @Override
    public ListAddressResponse getAllAddress(String username) {
        Customer customer = customerService.getCustomerByUsername(username);
        List<Address> addresses = addressRepository.findAllByCustomerAndStatusNot(customer, Status.DELETED)
                .orElseThrow(() -> new NotFoundException("Khách hàng chưa có địa chỉ nào."));
        String message = "Lấy danh sách địa chỉ của khách hàng: " + customer.getFullName() + " thành công.";

        return ListAddressResponse.listAddressResponse(addresses, customer, message, "OK");
    }

    @Override
    public Address getAddressActiveByUsername(String username) {
        Address address = addressRepository.findFirstByCustomerUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Khách hàng chưa có địa chỉ nào."));
        if (address == null) {
            throw new NotFoundException("Khách hàng chưa có địa chỉ nào sẳn sàng.");
        }

        return address;
    }


    @Override
    public Address checkAddress(Long addressId, String username) {
        checkExistAddressByAddressIdAndUsername(addressId, username);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Địa chỉ không tồn tại."));

        if (address.getStatus().equals(Status.DELETED)) {
            throw new BadRequestException("Địa chỉ đã bị xóa.");
        }

        return address;
    }


    private void checkStatus(AddressStatusRequest request, Address address, Customer customer, Long addressId) {
        if (request.getStatus().equals(Status.DELETED)) {
            address.setStatus(Status.DELETED);
        } else if (request.getStatus().equals(Status.ACTIVE)) {
            updateStatusInActiveWithAddress(customer, addressId);
            address.setStatus(Status.ACTIVE);
        } else if (request.getStatus().equals(Status.INACTIVE)) {
            address.setStatus(Status.INACTIVE);
        } else {
            throw new BadRequestException("Trạng thái không hợp lệ.");
        }
    }


    private String setMessageUpdateStatus(Status status, String fullName) {
        if (status.equals(Status.DELETED)) {
            return "Xóa địa chỉ của khách hàng: " + fullName + " thành công.";
        }
        return "Cập nhật trạng thái địa chỉ của khách hàng: " + fullName + " thành công.";
    }

    private void updateStatusInActiveWithAddress(Customer customer, Long addressId) {
        List<Address> addresses = addressRepository
                .findAllByCustomerAndStatusAndAddressIdNot(customer, Status.ACTIVE, addressId)
                .orElseThrow(() -> new NotFoundException("Khách hàng chưa có địa chỉ nào."));

        for (Address address : addresses) {
            address.setStatus(Status.INACTIVE);
            address.setUpdateAt(LocalDateTime.now());
            try {

                addressRepository.save(address);
            } catch (Exception e) {
                throw new InternalServerErrorException("Cập nhật trạng thái địa chỉ mới thất bại.");

            }
        }
    }


    private Address createAddressByAddressRequest(AddressRequest addressRequest) {
        Address address = new Address();

        address.setProvince(addressRequest.getProvince());
        address.setDistrict(addressRequest.getDistrict());
        address.setWard(addressRequest.getWard());
        address.setFullAddress(addressRequest.getFullAddress());
        address.setFullName(addressRequest.getFullName());
        address.setPhone(addressRequest.getPhone());
        address.setStatus(addressRequest.getStatus());
        address.setCreateAt(LocalDateTime.now());
        address.setUpdateAt(LocalDateTime.now());
        address.setStatus(Status.ACTIVE);
        return address;
    }


    private Address editAddressByAddressRequest(Address address, AddressRequest addressRequest) {

        address.setProvince(addressRequest.getProvince());
        address.setDistrict(addressRequest.getDistrict());
        address.setWard(addressRequest.getWard());
        address.setFullAddress(addressRequest.getFullAddress());
        address.setFullName(addressRequest.getFullName());
        address.setPhone(addressRequest.getPhone());
        address.setStatus(addressRequest.getStatus());
        address.setUpdateAt(LocalDateTime.now());

        return address;
    }


    private void checkExistAddressByAddressId(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new BadRequestException("Mã địa chỉ không tồn tại.");
        }
    }


    private void checkExistAddressByAddressIdAndUsername(Long addressId, String username) {
        checkExistAddressByAddressId(addressId);
        if (!addressRepository.existsByAddressIdAndCustomerUsername(addressId, username)) {
            throw new BadRequestException("Mã địa chỉ không thuộc về khách hàng này.");
        }
    }

}
