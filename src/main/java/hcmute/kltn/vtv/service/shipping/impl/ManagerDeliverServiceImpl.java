package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateDeliverWorkRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateStatusDeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ListDeliverResponse;
import hcmute.kltn.vtv.model.dto.shipping.DeliverDTO;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.DeliverRepository;
import hcmute.kltn.vtv.service.location.IDistrictService;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.service.shipping.IManagerDeliverService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerDeliverServiceImpl implements IManagerDeliverService {

    private final DeliverRepository deliverRepository;
    private final IDistrictService districtService;
    private final IWardService wardService;
    private final ICustomerService customerService;
    private final IManagerCustomerService managerCustomerService;
    private final IAuthenticationService authenticationService;

    @Override
    @Transactional
    public DeliverResponse addNewDeliver(DeliverRequest request) {

        checkPhoneExist(request.getPhone());

        Customer customer = authenticationService.addNewCustomer(request.getRegisterCustomerRequest());
        Deliver deliver = createDeliver(request);
        deliver.setCustomer(customer);

        try {
            managerCustomerService.updateRoleWithCustomer(customer.getCustomerId(), Role.DELIVER);
            deliverRepository.save(deliver);

            String message = "Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công.";

            return deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại.");
        }
    }

    @Override
    @Transactional
    public DeliverResponse updateDeliverWork(UpdateDeliverWorkRequest request) {
        Deliver deliver = deliverRepository.findById(request.getDeliverId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên này."));


        deliver.setDistrictWork(districtService.getDistrictByCode(request.getDistrictCodeWork()));
        deliver.setWardsWork(wardService.getWardsByWardsCodeWithDistrictCode(
                request.getWardsCodeWork(), request.getDistrictCodeWork()));
        deliver.setUpdateAt(LocalDateTime.now());

        try {
            deliverRepository.save(deliver);

            String message = "Cập nhật nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công.";

            return deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại." + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DeliverResponse updateStatusDeliver(UpdateStatusDeliverRequest request) {
        Deliver deliver = deliverRepository.findById(request.getDeliverId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên này."));
        checkStatusDeliver(request.getStatus());

        deliver.setStatus(request.getStatus());
        deliver.setUsernameAdded(request.getUsernameAdded());
        deliver.setUpdateAt(LocalDateTime.now());

        try {
            deliverRepository.save(deliver);

            String message = "Cập nhật trạng thái nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công.";

            return deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại." + e.getMessage());
        }
    }


    @Override
    public ListDeliverResponse getListDeliverByStatus(Status status){

        checkStatusDeliver(status);
        String getStringStatus = getStringStatus(status);

        List<Deliver> delivers = deliverRepository.findAllByStatus(status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách nhân viên theo trạng thái " + status + "."));

        return listDeliverResponse(delivers, "Lấy danh sách nhân viên " + getStringStatus + " thành công.", "OK");
    }

    @Override
    public ListDeliverResponse getListDeliverByStatusAndTypeWork(Status status, TypeWork typeWork) {

        checkStatusDeliver(status);
        String getStringStatus = getStringStatus(status);

        List<Deliver> delivers = deliverRepository.findAllByStatusAndTypeWork(status, typeWork)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách nhân viên theo trạng thái " + status + " và loại công việc " + typeWork + "."));

        return listDeliverResponse(delivers, "Lấy danh sách nhân viên " + getStringStatus + " và loại công việc " + typeWork + " thành công.", "OK");
    }



    private String getStringStatus(Status status) {

        if (status.equals(Status.ACTIVE)) {
            return "đang hoạt động";
        } else if (status.equals(Status.INACTIVE)) {
            return "đang nghỉ";
        } else if (status.equals(Status.DELETED)) {
            return "đã xóa";
        } else {
            return "đã khóa";
        }

    }


    private Deliver createDeliver(DeliverRequest request) {
        Deliver deliver = DeliverRequest.convertRequestToEntity(request);

        deliver.setPhone(request.getPhone());

        deliver.setFullAddress(request.getFullAddress());
        deliver.setTypeWork(request.getTypeWork());
        deliver.setDistrictWork(districtService.getDistrictByCode(request.getDistrictCodeWork()));
        deliver.setWardsWork(wardService.getWardsByWardsCodeWithDistrictCode(
                request.getWardsCodeWork(), request.getDistrictCodeWork()));
        deliver.setStatus(Status.ACTIVE);
        deliver.setCreateAt(LocalDateTime.now());
        deliver.setUpdateAt(LocalDateTime.now());

        return deliver;
    }



    private void checkPhoneExist(String phone) {
        if (deliverRepository.existsByPhone(phone)) {
            throw new BadRequestException("Số điện thoại nhân viên đã tồn tại.");
        }
    }



    private void checkStatusDeliver(Status status) {
        if (!status.equals(Status.ACTIVE) && !status.equals(Status.INACTIVE) && !status.equals(Status.DELETED) && !status.equals(Status.LOCKED)) {
            throw new BadRequestException("Trạng thái nhân viên không hợp lệ. Trạng thái nhân viên phải là ACTIVE, INACTIVE, DELETED hoặc LOCKED.");
        }
    }







    private String getTypeWork(TypeWork typeWork) {
        return switch (typeWork) {
            case SHIPPER -> "giao hàng";
            case PICKUP -> "lấy hàng tại cửa hàng";
            case WAREHOUSE -> "kho hàng";
            case MANAGER -> "quản lý vận chuyển";
            default -> "trung chuyển";
        };

    }


    public DeliverResponse deliverResponse(Deliver deliver, String message, String status) {
        DeliverResponse response = new DeliverResponse();
        response.setDeliverDTO(DeliverDTO.convertEntityToDTO(deliver));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }


    public ListDeliverResponse listDeliverResponse(List<Deliver> delivers, String message, String status) {
        ListDeliverResponse response = new ListDeliverResponse();
        response.setCount(delivers.size());
        response.setDeliverDTOs(DeliverDTO.convertEntitiesToDTOs(delivers));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }


}
