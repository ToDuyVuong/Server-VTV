package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.CategoriesResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;

public interface ICategoryService {
    CategoriesResponse getAllCategoryParent();

    CategoriesResponse getAllCategoryByParentId(Long categoryId);

    CategoriesResponse getAllCategoryByShopId(Long shopId);

    Category getCategoryById(Long categoryId);
}
