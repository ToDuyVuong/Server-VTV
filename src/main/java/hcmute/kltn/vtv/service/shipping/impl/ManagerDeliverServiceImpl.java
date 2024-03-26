package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateDeliverWorkRequest;
import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ListDeliverResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.DeliverRepository;
import hcmute.kltn.vtv.service.location.IDistrictService;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.service.shipping.IManagerDeliverService;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
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
    private final ITransportProviderService transportProviderService;

    @Override
    @Transactional
    public void addNewDeliverWithProviderRegister(Deliver deliver) {
        checkPhoneExist(deliver.getPhone());
        transportProviderService.checkExistByTransportProviderId(deliver.getTransportProvider().getTransportProviderId());
        try {

            managerCustomerService.updateRoleWithCustomer(deliver.getCustomer().getCustomerId(), Role.MANAGER);
            deliverRepository.save(deliver);
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại.");
        }
    }


    @Override
    @Transactional
    public DeliverResponse addNewDeliverByManager(DeliverRequest request, String usernameAdded) {
        checkPhoneExist(request.getPhone());
        transportProviderService.checkExistByTransportProviderId(request.getTransportProviderId());
        Customer customer = authenticationService.addNewCustomer(request.getRegisterCustomerRequest());
        Deliver deliver = createDeliverByDeliverRequest(request, customer, usernameAdded);
        try {
            managerCustomerService.updateRoleWithCustomer(customer.getCustomerId(), Role.DELIVER);
            deliverRepository.save(deliver);
            String message = "Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công. Vui lòng kích hoạt tài khoản thông qua email.";

            return DeliverResponse.deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại.");
        }
    }


    @Override
    @Transactional
    public DeliverResponse addNewDeliverManagerByProvider(DeliverRequest request, String usernameAdded) {
        checkPhoneExist(request.getPhone());
        transportProviderService.checkExistByTransportProviderId(request.getTransportProviderId());

        Customer customer = authenticationService.addNewCustomer(request.getRegisterCustomerRequest());
        Deliver deliver = createDeliverByDeliverRequest(request, customer, usernameAdded);
        try {
            managerCustomerService.updateRoleWithCustomer(customer.getCustomerId(), Role.DELIVER_MANAGER);
            deliverRepository.save(deliver);
            String message = "Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công. Vui lòng kích hoạt tài khoản thông qua email.";

            return DeliverResponse.deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại.");
        }
    }


    @Override
    @Transactional
    public DeliverResponse updateDeliverWork(UpdateDeliverWorkRequest request, String usernameAdded) {
        checkByExistsByDeliverId(request.getDeliverId());
        Deliver deliver = createDeliverByUpdateDeliverWorkRequest(request, usernameAdded);
        try {
            deliverRepository.save(deliver);
            String message = "Cập nhật nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công.";

            return DeliverResponse.deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại." + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DeliverResponse updateStatusDeliver(Long deliverId, Status status, String usernameAdded) {
        Deliver deliver = deliverRepository.findByDeliverId(deliverId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên này."));

        deliver.setStatus(status);
        deliver.setUsernameAdded(usernameAdded);
        deliver.setUpdateAt(LocalDateTime.now());

        try {
            deliverRepository.save(deliver);

            String message = "Cập nhật trạng thái nhân viên " + getTypeWork(deliver.getTypeWork()) + " thành công.";

            return DeliverResponse.deliverResponse(deliver, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái nhân viên " + getTypeWork(deliver.getTypeWork()) + " thất bại." + e.getMessage());
        }
    }


    @Override
    public ListDeliverResponse getListDeliverByStatus(Status status) {

        String getStringStatus = getStringStatus(status);

        List<Deliver> delivers = deliverRepository.findAllByStatus(status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách nhân viên theo trạng thái " + status + "."));

        return ListDeliverResponse.listDeliverResponse(delivers, "Lấy danh sách nhân viên " + getStringStatus + " thành công.", "OK");
    }

    @Override
    public ListDeliverResponse getListDeliverByStatusAndTypeWork(Status status, TypeWork typeWork) {

        String getStringStatus = getStringStatus(status);

        List<Deliver> delivers = deliverRepository.findAllByStatusAndTypeWork(status, typeWork)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách nhân viên theo trạng thái " + status + " và loại công việc " + typeWork + "."));

        return ListDeliverResponse.listDeliverResponse(delivers, "Lấy danh sách nhân viên " + getStringStatus + " và loại công việc " + typeWork + " thành công.", "OK");
    }


    private Deliver createDeliverByUpdateDeliverWorkRequest(UpdateDeliverWorkRequest request, String usernameAdded) {
        Deliver deliver = deliverRepository.findByDeliverId(request.getDeliverId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên này."));
        deliver.setDistrictWork(districtService.getDistrictByCode(request.getDistrictCodeWork()));
        deliver.setWardsWork(wardService.getWardsByWardsCodeWithDistrictCode(
                request.getWardsCodeWork(), request.getDistrictCodeWork()));
        deliver.setUsernameAdded(usernameAdded);
        deliver.setUpdateAt(LocalDateTime.now());

        return deliver;
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


    private Deliver createDeliverByDeliverRequest(DeliverRequest request, Customer customer, String usernameAdded) {
        TransportProvider transportProvider = transportProviderService.getTransportProviderByTransportProviderId(request.getTransportProviderId());

        Deliver deliver = new Deliver();
        deliver.setFullAddress(request.getFullAddress());
        deliver.setTypeWork(request.getTypeWork());
        deliver.setPhone(request.getPhone());
        deliver.setUsernameAdded(usernameAdded);
        deliver.setCustomer(customer);
        deliver.setWard(wardService.getWardByWardCode(request.getWardCode()));
        deliver.setDistrictWork(districtService.getDistrictByCode(request.getDistrictCodeWork()));
        deliver.setWardsWork(wardService.getWardsByWardsCodeWithDistrictCode(
                request.getWardsCodeWork(), request.getDistrictCodeWork()));
        deliver.setTransportProvider(transportProvider);
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

    private void checkExistsByPhoneAndDeliverIdNot(String phone, Long deliverId) {
        if (deliverRepository.existsByPhoneAndDeliverIdNot(phone, deliverId)) {
            throw new BadRequestException("Số điện thoại nhân viên đã tồn tại.");
        }
    }


    private void checkByExistsByDeliverId(Long deliverId) {
        if (!deliverRepository.existsByDeliverId(deliverId)) {
            throw new NotFoundException("Không tìm thấy nhân viên này.");
        }
    }


    private String getTypeWork(TypeWork typeWork) {
        return switch (typeWork) {
            case SHIPPER -> "giao hàng";
            case PICKUP -> "lấy hàng tại cửa hàng";
            case WAREHOUSE -> "kho hàng";
            case MANAGER -> "quản lý vận chuyển";
            case PROVIDER -> "nhà vận chuyển";
            default -> "trung chuyển";
        };

    }


}
