package hcmute.kltn.vtv.service.guest.impl;


import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vtv.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.repository.vtv.ProductRepository;
import hcmute.kltn.vtv.service.guest.IProductSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductSuggestionServiceImpl implements IProductSuggestionService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public ListProductPageResponse suggestByRandomly(int page, int size) {

        Page<Product> productPage = productRepository.suggestByRandomly(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));

        return ListProductPageResponse.listProductPageResponse(productPage, size, "Lấy danh sách sản phẩm gợi ý thành công!");
    }


    @Override
    public ListProductPageResponse suggestByRandomlyAndProductId(Long productId, int page, int size, boolean inShop) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
        String content = product.getName() + " " + product.getDescription();
        Page<Product> productPage = new PageImpl<>(new ArrayList<>());
        String message;

        if (inShop) {
            productPage = productRepository.searchFullTextOnShopByNameAndStatusAndSortRandomly(
                            content, product.getShop().getShopId(), Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý trong cùng cửa hàng thành công!";
        } else {
            productPage = productRepository.searchFullTextByNameAndStatusAndSortRandomly(
                            content, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý thành công!";
        }

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    @Override
    public ListProductPageResponse suggestByCategoryAndRandomly(Long categoryId, int page, int size) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
        Page<Product> productPage = new PageImpl<>(new ArrayList<>());
        String message;

        if (category.getParent() == null) {
            productPage = productRepository.suggestByCategoryParentAndRandomly(
                            category.getCategoryId(), Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý trong cùng cửa hàng thành công!";
        } else {
            productPage = productRepository.suggestByCategoryAndRandomly(
                            categoryId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm phù hợp"));
            message = "Lấy danh sách sản phẩm gợi ý thành công!";
        }

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


}
