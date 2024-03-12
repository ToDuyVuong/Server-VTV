package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.guest.IProductPageService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPageServiceImpl implements IProductPageService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ListProductPageResponse getListProductPage(int page, int size) {

        Page<Product> productPage = productRepository.findAllByStatusOrderByName(Status.ACTIVE,
                PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào!"));

        String message = "Lấy danh sách sản phẩm thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    @Override
    public ListProductPageResponse getListProductPageByCategoryId(Long categoryId, int page, int size) {
        if (isAdminOnlyInCategory(categoryId)) {
            return getProductsByCategoryParentId(categoryId, page, size);
        } else {
            return getProductsByCategoryId(categoryId, page, size);
        }
    }

    @Override
    public ListProductPageResponse getListProductsPageByShopId(Long shopId, int page, int size) {

        Page<Product> productPage = productRepository.findAllByShopShopIdAndStatusOrderByCreateAt(
                shopId, Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));

        String message = "Lấy danh sách sản phẩm theo cửa hàng thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    @Override
    public ListProductPageResponse getListBestSellingProductsPageByShopId(Long shopId, int page, int size) {

        Page<Product> productPage = productRepository.findAllByShopShopIdAndStatusOrderBySoldDescNameAsc(
                shopId, Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));

        String message = "Lấy danh sách sản phẩm theo cửa hàng theo thứ tự bán chạy nhất thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    @Override
    public ListProductPageResponse getListNewProductsPageByShopId(Long shopId, int page, int size) {

        Page<Product> productPage = productRepository.findAllByShopShopIdAndStatusOrderByCreateAtDesc(
                shopId, Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));

        String message = "Lấy danh sách sản phẩm theo cửa hàng theo thứ tự mới nhất thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }



    private ListProductPageResponse getProductsByCategoryId(Long categoryId, int page, int size) {

        Page<Product> productPage = productRepository.findAllByCategoryCategoryIdAndStatusOrderByCreateAt(
                categoryId, Status.ACTIVE,
                PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục cha này!"));

        String message = "Lấy danh sách sản phẩm theo danh mục con thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);

    }







    private ListProductPageResponse getProductsByCategoryParentId(Long categoryId, int page, int size) {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdAndStatus(categoryId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục nào!"));

        Page<Product> productPage = productRepository
                .findAllByCategoryInAndStatusOrderByCreateAt(categories, Status.ACTIVE,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục cha này!"));

        String message = "Lấy danh sách sản phẩm theo danh mục cha thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    public boolean isAdminOnlyInCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return categoryOptional.map(Category::isChild).orElse(false);
    }



}
