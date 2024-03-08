package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.RegisterShopRequest;
import hcmute.kltn.vtv.model.data.vendor.request.UpdateShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.impl.CustomerServiceImpl;
import hcmute.kltn.vtv.service.vendor.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerServiceImpl customerService;
    // @Autowired
    // ModelMapper modelMapper;

    @Override
    @Transactional
    public ShopResponse registerShop(RegisterShopRequest request) {

        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        checkEmailAndPhoneAndUsername(request.getEmail(), request.getPhone(), request.getUsername());

        if (shopRepository.existsByName(request.getName())) {
            throw new BadRequestException("Tên cửa hàng đã được sử dụng!");
        }

        // Shop shop = modelMapper.map(request, Shop.class);
        Shop shop = new Shop();
        shop.setName(request.getName());
        shop.setAvatar(request.getAvatar());
        shop.setAddress(request.getAddress());
        shop.setProvince(request.getProvince());
        shop.setDistrict(request.getDistrict());
        shop.setWard(request.getWard());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());
        shop.setOpenTime(request.getOpenTime());
        shop.setCloseTime(request.getCloseTime());
        shop.setDescription(request.getDescription());

        shop.setCustomer(customer);
        shop.setStatus(Status.ACTIVE);
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());

        try {
            Shop save = shopRepository.save(shop);

            // ShopDTO shopDTO = modelMapper.map(save, ShopDTO.class);
            // customer = addRoleVendor(customer);

            // shopDTO.setCustomerDTO(modelMapper.map(customer, CustomerDTO.class));

            ShopDTO shopDTO = ShopDTO.convertEntityToDTO(save);
            addRoleVendor(customer);
            ShopResponse shopResponse = new ShopResponse();
            shopResponse.setShopDTO(shopDTO);
            shopResponse.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
            shopResponse.setCode(200);
            shopResponse.setMessage("Đăng ký cửa hàng thành công.");
            shopResponse.setStatus("success");
            return shopResponse;

        } catch (Exception e) {
            throw new BadRequestException("Đăng ký cửa hàng thất bại!");
        }

    }

    @Override
    public ShopResponse getProfileShop(String username) {
        // Customer customer = customerService.getCustomerByUsername(username);
        Shop shop = getShopByUsername(username);

        // ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
        // shopDTO.setCustomerDTO(modelMapper.map(customer, CustomerDTO.class));
        ShopDTO shopDTO = ShopDTO.convertEntityToDTO(shop);

        ShopResponse shopResponse = new ShopResponse();
        shopResponse.setShopDTO(shopDTO);
        shopResponse.setCode(200);
        shopResponse.setMessage("Lấy thông tin cửa hàng thành công.");
        shopResponse.setStatus("ok");

        return shopResponse;

    }

    @Override
    @Transactional
    public ShopResponse updateShop(UpdateShopRequest request) {
        checkEmailAndPhoneAndUsernameInShop(request.getEmail(), request.getPhone(), request.getUsername());

        Shop shop = shopRepository.findByCustomer_Username(request.getUsername());
        shop.setName(request.getName());
        shop.setAddress(request.getAddress());
        shop.setProvince(request.getProvince());
        shop.setDistrict(request.getDistrict());
        shop.setWard(request.getWard());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());
        shop.setOpenTime(request.getOpenTime());
        shop.setCloseTime(request.getCloseTime());
        shop.setDescription(request.getDescription());
        shop.setUpdateAt(LocalDateTime.now());

        try {
            shopRepository.save(shop);

            // Customer customer =
            // customerService.getCustomerByUsername(request.getUsername());
            // ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
            // shopDTO.setCustomerDTO(modelMapper.map(customer, CustomerDTO.class));

            ShopDTO shopDTO = ShopDTO.convertEntityToDTO(shop);

            ShopResponse shopResponse = new ShopResponse();
            shopResponse.setShopDTO(shopDTO);
            shopResponse.setCode(200);
            shopResponse.setMessage("Cập nhật cửa hàng thành công.");
            shopResponse.setStatus("ok");
            return shopResponse;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật cửa hàng thất bại!");
        }
    }

    @Override
    @Transactional
    public ShopResponse updateStatusShop(String username, Status status) {
        Shop shop = getShopByUsername(username);
        shop.setStatus(status);
        shop.setUpdateAt(LocalDateTime.now());

        try {
            shopRepository.save(shop);

            // Customer customer = customerService.getCustomerByUsername(username);
            // ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
            // shopDTO.setCustomerDTO(modelMapper.map(customer, CustomerDTO.class));

            ShopDTO shopDTO = ShopDTO.convertEntityToDTO(shop);

            ShopResponse shopResponse = new ShopResponse();
            shopResponse.setShopDTO(shopDTO);
            shopResponse.setCode(200);
            shopResponse.setMessage("Cập nhật trạng thái cửa hàng thành công.");
            shopResponse.setStatus("ok");
            return shopResponse;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái cửa hàng thất bại!");
        }
    }

    @Override
    public Shop getShopByUsername(String username) {
        return shopRepository.findByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Tài khoản chưa đăng ký cửa hàng!"));
    }




    private void checkEmailAndPhoneAndUsernameInShop(String email, String phone, String username) {
        Shop shop = shopRepository.findByCustomer_Username(username);

        if (shop == null) {
            throw new BadRequestException("Tài khoản chưa đăng ký cửa hàng!");
        } else {
            // Check if email is used by a shop other than the current shop
            Shop shopByEmail = shopRepository.findByEmail(email);
            if (shopByEmail != null && !shopByEmail.getEmail().equals(shop.getEmail())) {
                throw new BadRequestException("Email đã được sử dụng ở một cửa hàng khác!");
            }

            // Check if phone is used by a shop other than the current shop
            Shop shopByPhone = shopRepository.findByPhone(phone);
            if (shopByPhone != null && !shopByPhone.getPhone().equals(shop.getPhone())) {
                throw new BadRequestException("Số điện thoại đã được sử dụng ở một cửa hàng khác!");
            }
        }
    }

    private Customer addRoleVendor(Customer customer) {
        customer.addRole(Role.VENDOR);
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật quyền cho tài khoản thất bại!");
        }

    }

    private void checkEmailAndPhoneAndUsername(String email, String phone, String username) {
        // Check if the email is already used by a shop
        Shop shop = shopRepository.findByEmail(email);
        if (shop != null) {
            throw new BadRequestException("Email đã được sử dụng ở một cửa hàng khác!");
        }

        // Check if the phone is already used by a shop
        shop = shopRepository.findByPhone(phone);
        if (shop != null) {
            throw new BadRequestException("Số điện thoại đã được sử dụng ở một cửa hàng khác!");
        }

        // Check if the username is already used by a customer
        shop = shopRepository.findByCustomer_Username(username);
        if (shop != null) {
            throw new BadRequestException("Tài khoản đã được sử dụng ở một cửa hàng khác!");
        }

    }

}
