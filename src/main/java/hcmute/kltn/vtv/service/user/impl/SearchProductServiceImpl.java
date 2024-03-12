package hcmute.kltn.vtv.service.user.impl;


import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.service.user.ISearchProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductServiceImpl implements ISearchProductService {


    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;


    @Override
    public ListProductPageResponse getProductsPageBySearchAndSort(String search, int page, int size, String sort) {
        String message = checkSortMessage(sort);
        Page<Product> productPage = getProductPage(search, page, size, sort);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    @Override
    public ListProductPageResponse getProductsPageBySearchAndPriceRangeAndSort(String search, int page, int size, String sort,
                                                                               Long minPrice, Long maxPrice) {
        String message = checkSortMessage(sort) + " Trong khoảng giá từ " + minPrice + " VNĐ đến " + maxPrice + " VNĐ!";
        Page<Product> productPage = getProductPageAndPriceRange(search, page, size, sort, minPrice, maxPrice);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    @Override
    public ListProductPageResponse getProductsPageBySearchAndSortOnShop(String search, Long shopId,
                                                                        int page, int size, String sort) {
        if (!shopRepository.existsById(shopId)) {
            throw new NotFoundException("Không tìm thấy cửa hàng nào có mã: " + shopId);
        }

        String message = checkSortMessage(sort) + " Của cửa hàng!";
        Page<Product> productPage = getProductPageOnShop(search, shopId, page, size, sort);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    @Override
    public ListProductPageResponse getProductsPageBySearchAndPriceRangeAndSortOnShop(String search, Long shopId,
                                                                                     int page, int size, String sort,
                                                                                     Long minPrice, Long maxPrice) {
        if (!shopRepository.existsById(shopId)) {
            throw new NotFoundException("Không tìm thấy cửa hàng nào có mã: " + shopId);
        }

        String message = checkSortMessage(sort) + " Trong khoảng giá từ " + minPrice + " VNĐ đến " + maxPrice + " VNĐ!";
        Page<Product> productPage = getProductPagePriceRangeOnShop(search, shopId, page, size, sort, minPrice, maxPrice);

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }


    private String checkSortMessage(String sort) {
        return switch (sort) {
            case "newest" -> "Lọc sản phẩm tìm kiếm theo thứ tự mới nhất thành công!";
            case "best-selling" -> "Lọc sản phẩm tìm kiếm theo thứ tự bán chạy nhất thành công!";
            case "price-asc" -> "Lọc sản phẩm tìm kiếm theo thứ tự giá tăng dần thành công!";
            case "price-desc" -> "Lọc sản phẩm tìm kiếm theo thứ tự giá giảm dần thành công!";
            case "random" -> "Lấy sản phẩm tìm kiếm ngẫu nhiên thành công!";
            default -> "Tìm kiếm sản phẩm thành công!";
        };
    }


    private Page<Product> getProductPageAndPriceRange(String search, int page, int size, String sort, Long minPrice, Long maxPrice) {
        return switch (sort) {
            case "newest" -> productRepository
                    .searchFullTextAndPriceRangeByStatusAndSortCreateAtAsc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productRepository
                    .searchFullTextAndPriceRangeByStatusAndSortBestSelling(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productRepository
                    .searchFullTextAndPriceRangeByStatusAndSortPriceAsc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào!" +
                            " Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productRepository
                    .searchFullTextAndPriceRangeByStatusAndSortPriceDesc(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productRepository
                    .searchFullTextAndPriceRangeByStatusAndSortRandomly(
                            search, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lấy sản phẩm ngẫu nhiên!"));
        };
    }


    private Page<Product> getProductPage(String search, int page, int size, String sort) {
        return switch (sort) {
            case "newest" -> productRepository
                    .searchFullTextByNameAndStatusAndSortByNewest(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productRepository
                    .searchFullTextByNameAndStatusAndSortByBestSelling(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productRepository
                    .searchFullTextByNameAndStatusAndSortByPriceAsc(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productRepository
                    .searchFullTextByNameAndStatusAndSortByPriceDesc(search, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productRepository
                    .searchFullTextByNameAndStatusAndSortRandomly(search, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào! Lấy sản phẩm ngẫu nhiên!"));
        };
    }


    private Page<Product> getProductPageOnShop(String search, Long shopId, int page, int size, String sort) {
        return switch (sort) {
            case "newest" -> productRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByNewest(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByBestSelling(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByPriceAsc(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productRepository
                    .searchFullTextOnShopByNameAndStatusAndSortByPriceDesc(search, shopId, Status.ACTIVE.toString(),
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productRepository
                    .searchFullTextOnShopByNameAndStatusAndSortRandomly(search, shopId, Status.ACTIVE.toString(), PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lấy sản phẩm ngẫu nhiên!"));
        };
    }


    private Page<Product> getProductPagePriceRangeOnShop(String search, Long shopId, int page, int size, String sort, Long minPrice, Long maxPrice) {
        return switch (sort) {
            case "newest" -> productRepository
                    .searchFullTextPriceRangeOnShopAndSortByNewest(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự mới nhất!"));

            case "best-selling" -> productRepository
                    .searchFullTextPriceRangeOnShopAndSortByBestSelling(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự bán chạy nhất!"));

            case "price-asc" -> productRepository
                    .searchFullTextPriceRangeOnShopAndSortByPriceAse(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá tăng dần!"));

            case "price-desc" -> productRepository
                    .searchFullTextPriceRangeOnShopAndSortByPriceDesc(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lọc sản phẩm theo thứ tự giá giảm dần!"));

            default -> productRepository
                    .searchFullTextPriceRangeOnShopAndSortByRandomly(
                            search, shopId, Status.ACTIVE.toString(), minPrice, maxPrice,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng! Lấy sản phẩm ngẫu nhiên!"));
        };
    }





}
