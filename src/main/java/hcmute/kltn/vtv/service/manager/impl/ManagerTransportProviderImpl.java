package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.model.data.manager.request.TransportProviderRegisterRequest;
import hcmute.kltn.vtv.model.data.manager.request.UpdateTransportProviderWithProvincesRequest;
import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.response.ListTransportProviderResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportProviderResponse;
import hcmute.kltn.vtv.model.entity.location.Province;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.TransportProviderRepository;
import hcmute.kltn.vtv.service.location.IProvinceService;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.service.manager.IManagerTransportProvider;
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
public class ManagerTransportProviderImpl implements IManagerTransportProvider {

    private final TransportProviderRepository transportProviderRepository;
    private final ICustomerService customerService;
    private final IAuthenticationService authenticationService;
    private final IManagerCustomerService managerCustomerService;
    private final IProvinceService provinceService;
    private final ITransportProviderService transportProviderService;
    private final IManagerDeliverService managerDeliverService;

    @Override
    @Transactional
    public TransportProviderResponse addNewTransportProvider(TransportProviderRegisterRequest request) {

        checkEmailExist(request.getEmail());
        checkPhoneExist(request.getPhone());
        checkUsernameExist(request.getRegisterRequest().getUsername());

        List<Province> provinces = provinceService.getPronvincesByProvinceCode(request.getProvincesCode());
        Customer customer = authenticationService.addNewCustomer(request.getRegisterRequest());
        managerCustomerService.updateRoleWithCustomer(customer.getCustomerId(), Role.PROVIDER);

        TransportProvider transportProvider = createTransportProvider(request);
        transportProvider.setCustomer(customer);
        transportProvider.setProvinces(provinces);

        Deliver deliver = createDeliverWithTransportProvider(transportProvider, customer, request.getRegisterRequest().getUsername());

        try {
            transportProviderRepository.save(transportProvider);
            deliver.setTransportProvider(transportProvider);
            managerDeliverService.addNewDeliverWithProviderRegister(deliver);

            return TransportProviderResponse.transportProviderResponse(transportProvider,
                    "Đăng ký nhà vận chuyển thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Đăng ký nhà vận chuyển thất bại.");
        }
    }


    @Override
    @Transactional
    public TransportProviderResponse updateTransportProviderWithProvinces(UpdateTransportProviderWithProvincesRequest request) {

        TransportProvider transportProvider = transportProviderService.getTransportProviderByTransportProviderId(request.getTransportProviderId());

        List<Province> provinces = provinceService.getPronvincesByProvinceCode(request.getProvincesCode());
        transportProvider.setProvinces(provinces);

        try {
            transportProviderRepository.save(transportProvider);

            return TransportProviderResponse.transportProviderResponse(transportProvider,
                    "Cập nhật nhà vận chuyển thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật nhà vận chuyển thất bại.");
        }
    }


    @Override
    public ListTransportProviderResponse getAllTransportProviders() {
        List<TransportProvider> transportProviders = transportProviderRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy nhà vận chuyển nào."));

        return ListTransportProviderResponse.listTransportProvidersResponse(transportProviders);
    }


    private void checkEmailExist(String email) {
        TransportProvider transportProvider = transportProviderRepository.findByEmail(email)
                .orElse(null);

        if (transportProvider != null) {
            throw new BadRequestException("Email nhà vận chuyển đã tồn tại.");
        }
    }


    private void checkPhoneExist(String phone) {
        TransportProvider transportProvider = transportProviderRepository.findByPhone(phone)
                .orElse(null);

        if (transportProvider != null) {
            throw new BadRequestException("Số điện thoại nhà vận chuyển đã tồn tại.");
        }
    }

    private void checkUsernameExist(String username) {
        if (customerService.checkUsernameExist(username)) {
            throw new BadRequestException("Tài khoản đã tồn tại.");
        }
    }


    private TransportProvider createTransportProvider(TransportProviderRegisterRequest request) {
        TransportProvider transportProvider = new TransportProvider();
        transportProvider.setFullName(request.getFullName());
        transportProvider.setShortName(request.getShortName());
        transportProvider.setEmail(request.getEmail());
        transportProvider.setPhone(request.getPhone());
        transportProvider.setUsernameAdded(request.getUsernameAdded());
        transportProvider.setStatus(Status.ACTIVE);
        transportProvider.setCreateAt(LocalDateTime.now());
        transportProvider.setUpdateAt(LocalDateTime.now());

        return transportProvider;
    }


    private Deliver createDeliverWithTransportProvider(TransportProvider transportProvider, Customer customer, String usernameAdded) {
        Deliver deliver = new Deliver();
        deliver.setFullAddress(null);
        deliver.setTypeWork(TypeWork.PROVIDER);
        deliver.setPhone(transportProvider.getPhone());
        deliver.setUsernameAdded(usernameAdded);
        deliver.setCustomer(customer);
        deliver.setWard(null);
        deliver.setDistrictWork(null);
        deliver.setWardsWork(null);
        deliver.setStatus(Status.ACTIVE);
        deliver.setCreateAt(LocalDateTime.now());
        deliver.setUpdateAt(LocalDateTime.now());

        return deliver;
    }


}
