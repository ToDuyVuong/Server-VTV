package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.ProductFilterRepository;
import hcmute.kltn.vtv.service.guest.IProductFilterService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductFilterServiceImpl implements IProductFilterService {


    @Autowired
    private final ProductFilterRepository productFilterRepository;


    @Override
    public ListProductPageResponse getFilterProductPage(int page, int size, String filter) {
        String message = checkSortMessage(filter);
        Page<Product> productPage = filterProductPage(filter, page, size);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    @Override
    public ListProductPageResponse getFilterProductPageByPriceRange(int page, int size, String filter,
                                                                    Long minPrice, Long maxPrice) {


        String message = checkSortMessage(filter) + " Trong khoảng giá từ " + minPrice + " VNĐ đến " + maxPrice + " VNĐ!";
        Page<Product> productPage = filterProductPagePriceRange(filter, page, size, minPrice, maxPrice);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    private String checkSortMessage(String filter) {
        return switch (filter) {
            case "newest" -> "Lọc sản phẩm theo thứ tự mới nhất thành công!";
            case "best-selling" -> "Lọc sản phẩm theo thứ tự bán chạy nhất thành công!";
            case "price-asc" -> "Lọc sản phẩm theo thứ tự giá tăng dần thành công!";
            case "price-desc" -> "Lọc sản phẩm theo thứ tự giá giảm dần thành công!";
            case "random" -> "Lấy sản phẩm ngẫu nhiên thành công!";
            default -> "Tìm kiếm sản phẩm thành công!";
        };
    }


    public Page<Product> filterProductPage(String filter, int page, int size) {

        return switch (filter) {
            case "newest" -> productFilterRepository
                    .filterByStatusCreateAtAsc(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm mới nhất!"));

            case "best-selling" -> productFilterRepository
                    .filterByStatusBestSelling(
                            Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm bán chạy nhất!"));

            case "price-asc" -> productFilterRepository
                    .filterByStatusPriceAsc(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productFilterRepository
                    .filterByStatusPriceDesc(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productFilterRepository
                    .filterByStatusRandomly(Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm ngẫu nhiên!"));

        };
    }


    public Page<Product> filterProductPagePriceRange(String filter, int page, int size, Long minPrice, Long maxPrice) {

        return switch (filter) {
            case "newest" -> productFilterRepository
                    .filterAndPriceRangeByStatusCreateAtAsc(
                            Status.ACTIVE.toString(), minPrice, maxPrice, PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm mới nhất!"));

            case "best-selling" -> productFilterRepository
                    .filterAndPriceRangeByStatusBestSelling(
                            Status.ACTIVE.toString(), minPrice, maxPrice, PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm bán chạy nhất!"));

            case "price-asc" -> productFilterRepository
                    .filterAndPriceRangeByStatusPriceAsc(
                            Status.ACTIVE.toString(), minPrice, maxPrice, PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productFilterRepository
                    .filterAndPriceRangeByStatusPriceDesc(
                            Status.ACTIVE.toString(), minPrice, maxPrice, PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productFilterRepository
                    .filterAndPriceRangeByStatusRandomly(
                            Status.ACTIVE.toString(), minPrice, maxPrice, PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào theo lọc sản phẩm ngẫu nhiên!"));

        };
    }


}
