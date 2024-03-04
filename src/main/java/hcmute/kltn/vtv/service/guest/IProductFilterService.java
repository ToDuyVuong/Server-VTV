package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;

public interface IProductFilterService {
    ListProductPageResponse getFilterProductPage(int page, int size, String filter);

    ListProductPageResponse getFilterProductPageByPriceRange(int page, int size, String filter,
                                                             Long minPrice, Long maxPrice);
}
