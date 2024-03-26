package hcmute.kltn.vtv.service.guest;

public interface IPageService {
    void checkRequestProductPageParams(int page, int size);

    void checkRequestOrderPageParams(int page, int size);

    void checkRequestPriceRangeParams(Long minPrice, Long maxPrice);

    void checkRequestSortParams(String sort);
}
