package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.FollowedShopResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFollowedShopResponse;
import hcmute.kltn.vtv.model.dto.user.FollowedShopDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.repository.user.FollowedShopRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IFollowedShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowedShopServiceImpl implements IFollowedShopService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private FollowedShopRepository followedShopRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public FollowedShopResponse addNewFollowedShop(Long shopId, String username) {

        boolean isExist = followedShopRepository.existsByCustomerUsernameAndShopShopId(username, shopId);
        if (isExist) {
            throw new BadRequestException("Bạn đã theo dõi cửa hàng này!");
        }

        Customer customer = customerService.getCustomerByUsername(username);
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException("Cửa hàng không tồn tại!"));

        FollowedShop followedShop = new FollowedShop();
        followedShop.setCustomer(customer);
        followedShop.setShop(shop);
        followedShop.setCreateAt(LocalDateTime.now());

        try {
            followedShopRepository.save(followedShop);

            FollowedShopResponse response = new FollowedShopResponse();
            response.setFollowedShopDTO(modelMapper.map(followedShop, FollowedShopDTO.class));
            response.setMessage("Theo dõi cửa hàng thành công!");
            response.setStatus("success");
            response.setCode(200);

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Lỗi theo dõi cửa hàng!");
        }
    }

    @Override
    public ListFollowedShopResponse getListFollowedShopByUsername(String username) {
        List<FollowedShop> followedShops = followedShopRepository.findAllByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Không có cửa hàng nào được theo dõi!"));

        ListFollowedShopResponse response = new ListFollowedShopResponse();
        response.setFollowedShopDTOs(FollowedShopDTO.convertToListDTO(followedShops));
        response.setCount(followedShops.size());
        response.setMessage("Lấy danh sách cửa hàng theo dõi thành công.");
        response.setStatus("ok");
        response.setCode(200);
        return response;
    }

    @Override
    public FollowedShopResponse deleteFollowedShop(Long followedShopId, String username) {
        FollowedShop followedShop = followedShopRepository.findById(followedShopId)
                .orElseThrow(() -> new BadRequestException("Theo dõi cửa hàng không tồn tại!"));

        if (!followedShop.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Bạn không có quyền xóa theo dõi cửa hàng này!");
        }

        try {
            followedShopRepository.delete(followedShop);

            FollowedShopResponse response = new FollowedShopResponse();
            response.setMessage("Xóa theo dõi cửa hàng thành công!");
            response.setStatus("success");
            response.setCode(200);
            return response;
        } catch (Exception e) {
            throw new BadRequestException("Lỗi xóa theo dõi cửa hàng!");
        }
    }

}
