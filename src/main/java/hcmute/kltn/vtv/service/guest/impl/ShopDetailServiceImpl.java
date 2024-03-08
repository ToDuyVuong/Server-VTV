package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ShopDetailResponse;
import hcmute.kltn.vtv.model.dto.vtv.CategoryDTO;
import hcmute.kltn.vtv.model.dto.vtv.ProductDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vtv.Product;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.repository.user.FollowedShopRepository;
import hcmute.kltn.vtv.repository.vtv.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.guest.IShopDetailService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopDetailServiceImpl implements IShopDetailService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private FollowedShopRepository followedShopRepository;
    // @Autowired
    // ModelMapper modelMapper;

    @Override
    public ShopDetailResponse getShopDetailByShopId(Long shopId, String username) {

//        Shop shop = shopRepository.findById(shopId)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không tồn tại!"));
//        if (shop.getStatus() != Status.ACTIVE) {
//            throw new BadRequestException("Cửa hàng đã bị khóa!");
//        }
//
//        List<Product> products = productRepository.findByShopShopIdAndStatus(shopId, Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));
//        if (products.isEmpty()) {
//            throw new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!");
//        }
//        if (products.size() > 20) {
//            products = products.subList(products.size() - 20, products.size());
//        }
//        products.sort(Comparator.comparing(Product::getCreateAt).reversed());
//
//        List<Category> categories = categoryRepository.findAllByShopShopIdAndStatus(shopId, Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục nào trong cửa hàng này!"));
//        if (categories.isEmpty()) {
//            throw new NotFoundException("Không tìm thấy danh mục nào trong cửa hàng này!!");
//        }
//        categories.sort(Comparator.comparing(Category::getName));
//
//        boolean isFollow = followedShopRepository.existsByCustomerUsernameAndShopShopId(username, shopId);
//
//        ShopDetailResponse response = new ShopDetailResponse();
//        // response.setShopDTO(modelMapper.map(shop, ShopDTO.class));
//        response.setShopDTO(ShopDTO.convertEntityToDTO(shop));
//        response.setProductDTOs(ProductDTO.convertToListDTO(products));
//        response.setTotalProduct(products.size());
//        response.setCategoryDTOs(CategoryDTO.convertToListDTO(categories));
//        response.setTotalCategory(categories.size());
//        response.setFollowed(isFollow);
//        response.setMessage("Lấy thông tin cửa hàng thành công!");
//        response.setStatus("ok");
//        response.setCode(200);

        return null;
    }
}
