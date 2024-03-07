package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.CategoriesResponse;

public interface ICategoryService {
    CategoriesResponse getAllCategoryParent();

    CategoriesResponse getAllCategoryByParentId(Long categoryId);

    CategoriesResponse getAllCategoryByShopId(Long shopId);
}
