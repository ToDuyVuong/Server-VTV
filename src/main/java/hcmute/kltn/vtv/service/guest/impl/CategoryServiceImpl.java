package hcmute.kltn.vtv.service.guest.impl;


import hcmute.kltn.vtv.model.data.guest.CategoriesResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public CategoriesResponse getAllCategoryParent() {
        List<Category> categories = categoryRepository.findAllByAdminOnlyAndStatus(true, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha!"));

        return CategoriesResponse.categoriesResponse(categories, "Lấy danh mục thành công!", "OK");
    }


    @Override
    public CategoriesResponse getAllCategoryByParentId(Long categoryId) {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdAndStatus(categoryId, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục con nào trong danh mục cha này!"));
        return CategoriesResponse.categoriesResponse(categories, "Lấy danh mục con theo danh mục cha thành công!", "OK");
    }


    @Override
    public CategoriesResponse getAllCategoryByShopId(Long shopId) {
        List<Category> categories = categoryRepository.findAllByShopShopId(shopId);
        return CategoriesResponse.categoriesResponse(categories, "Lấy danh mục theo cửa hàng thành công!", "OK");
    }


    @Override
    public Category getCategoryById(Long categoryId) {
        checkExistCategoryById(categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));
    }

    private void checkExistCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Danh mục không tồn tại!");
        }
    }


}
