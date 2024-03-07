package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;

public interface ICategoryService {
    CategoryResponse getAllCategoryParent();

    CategoryResponse getAllCategoryByParentId(Long categoryId);

    CategoryResponse getAllCategoryByShopId(Long shopId);
}
