package hcmute.kltn.vtv.service.guest.impl;


import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
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
    public CategoryResponse getAllCategoryParent() {
        List<Category> categories = categoryRepository.findAllByAdminOnlyAndStatus(true, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha!"));

        return CategoryResponse.categoryResponse(categories, "Lấy danh mục thành công!", "OK");
    }


    @Override
    public CategoryResponse getAllCategoryByParentId(Long categoryId) {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdAndStatus(categoryId, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục con nào trong danh mục cha này!"));
        return CategoryResponse.categoryResponse(categories, "Lấy danh mục con theo danh mục cha thành công!", "OK");
    }


}
