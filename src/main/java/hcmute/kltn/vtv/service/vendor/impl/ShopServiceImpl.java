package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.ShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.impl.CustomerServiceImpl;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
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
    @Autowired
    private final IImageService imageService;
    @Autowired
    private final IWardService wardService;

    @Override
    @Transactional
    public ShopResponse registerShop(ShopRequest request, String username) {

        Customer customer = customerService.getCustomerByUsername(username);
        checkExistShoppByUsernameAndNameAndEmailAndPhone(username, request.getName(), request.getEmail(), request.getPhone());
        Ward ward = wardService.checkWardCodeMatchWithFullLocation(
                request.getProvinceName(), request.getDistrictName(), request.getWardName(), request.getWardCode());
        Shop shop = createShopByShopRequest(request, customer, ward);
        try {
            shopRepository.save(shop);
            addRoleVendor(customer);

            return ShopResponse.shopResponse(shop, "Đăng ký cửa hàng thành công.", "Success");
        } catch (Exception e) {
            imageService.deleteImageInFirebase(shop.getAvatar());
            throw new InternalServerErrorException("Đăng ký cửa hàng thất bại!");
        }
    }

    @Override
    public ShopResponse getProfileShop(String username) {
        Shop shop = getShopByUsername(username);

        return ShopResponse.shopResponse(shop, "Lấy thông tin cửa hàng thành công.", "OK");
    }

    @Override
    @Transactional
    public ShopResponse updateShop(ShopRequest request, String username) {
        checkExistInShopByUsernameAndNameAndEmailAndPhone(
                username, request.getName(), request.getEmail(), request.getPhone());
        Shop shop = getShopByUsername(username);
        String oldAvatar = shop.getAvatar();

        Ward ward = wardService.checkWardCodeMatchWithFullLocation(request.getProvinceName(),
                request.getDistrictName(), request.getWardName(), request.getWardCode());
        updateShopByShopRequest(shop, request, ward);
        try {
            shopRepository.save(shop);
            if (request.isChangeAvatar()){
                imageService.deleteImageInFirebase(oldAvatar);
            }
            return ShopResponse.shopResponse(shop, "Cập nhật cửa hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật cửa hàng thất bại!");
        }
    }

    @Override
    @Transactional
    public ShopResponse updateStatusShop(String username, Status status) {
        Shop shop = getShopByUsername(username);
        if (shop.getStatus().equals(Status.DELETED) || status.equals(Status.LOCKED)) {
            throw new BadRequestException("Cửa hàng đã bị xóa hoặc khóa không thể cập nhật trạng thái!");
        }
        shop.setStatus(status);
        shop.setUpdateAt(LocalDateTime.now());
        try {
            shopRepository.save(shop);

            return ShopResponse.shopResponse(shop, "Cập nhật trạng thái cửa hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái cửa hàng thất bại!");
        }
    }

    @Override
    public Shop getShopByUsername(String username) {
        return shopRepository.findByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Tài khoản chưa đăng ký cửa hàng!"));
    }


    private Shop createShopByShopRequest(ShopRequest request, Customer customer, Ward ward) {
        Shop shop = new Shop();
        shop.setName(request.getName());
        shop.setAddress(request.getAddress());
        shop.setWardName(request.getWardName());
        shop.setProvinceName(request.getProvinceName());
        shop.setAvatar(imageService.uploadImageToFirebase(request.getAvatar()));
        shop.setWardName(request.getWardName());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());
        shop.setOpenTime(request.getOpenTime());
        shop.setCloseTime(request.getCloseTime());
        shop.setDescription(request.getDescription());
        shop.setCustomer(customer);
        shop.setWard(ward);
        shop.setStatus(Status.ACTIVE);
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());

        return shop;
    }

    private void updateShopByShopRequest(Shop shop, ShopRequest request, Ward ward) {
        shop.setName(request.getName());
        shop.setAvatar(request.isChangeAvatar() ? imageService.uploadImageToFirebase(request.getAvatar()) : shop.getAvatar());
        shop.setAddress(request.getAddress());
        shop.setWardName(request.getWardName());
        shop.setProvinceName(request.getProvinceName());
        shop.setWardName(request.getWardName());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());
        shop.setOpenTime(request.getOpenTime());
        shop.setCloseTime(request.getCloseTime());
        shop.setDescription(request.getDescription());
        shop.setWard(ward);
        shop.setUpdateAt(LocalDateTime.now());
    }


    private void checkExistShoppByUsernameAndNameAndEmailAndPhone(String username, String name, String email, String phone) {
        if (shopRepository.existsByName(name)) {
            throw new BadRequestException("Tên cửa hàng đã được sử dụng!");
        }

        if (shopRepository.existsByCustomerUsername(username)) {
            throw new BadRequestException("Tài khoản đã đăng ký cửa hàng!");
        }

        if (shopRepository.existsByEmail(email)) {
            throw new BadRequestException("Email đã được sử dụng!");
        }

        if (shopRepository.existsByPhone(phone)) {
            throw new BadRequestException("Số điện thoại đã được sử dụng!");
        }

    }


    private void checkExistInShopByUsernameAndNameAndEmailAndPhone(String username, String name, String email, String phone) {
        if (!shopRepository.existsByCustomerUsername(username)) {
            throw new BadRequestException("Tài khoản chưa đăng ký cửa hàng!");
        }

        if (shopRepository.existsByNameAndCustomerUsernameNot(name, username)) {
            throw new BadRequestException("Tên cửa hàng mới đã được sử dụng!");
        }

        if (shopRepository.existsByEmailAndCustomerUsernameNot(email, username)) {
            throw new BadRequestException("Email mới đã được sử dụng!");
        }

        if (shopRepository.existsByPhoneAndCustomerUsernameNot(phone, username)) {
            throw new BadRequestException("Số điện thoại mới đã được sử dụng!");
        }

    }


    private void addRoleVendor(Customer customer) {
        customer.addRole(Role.VENDOR);
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật quyền cho tài khoản thất bại!");
        }

    }


}
