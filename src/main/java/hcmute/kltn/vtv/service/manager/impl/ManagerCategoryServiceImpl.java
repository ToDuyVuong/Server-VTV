package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.data.manager.request.CategoryRequest;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.service.manager.IManagerCategoryService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerCategoryServiceImpl implements IManagerCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    @Transactional
    public CategoryResponse addNewCategoryByManager(CategoryRequest request, String username) {

        checkExistCategoryByName(request.getName());
        if (request.isChild()) {
            validateCategoryDepth(request.getParentId());
        }
        Category category = createCategoryByCategoryRequest(request, username);

        try {
            Category categorySave = categoryRepository.save(category);
            return CategoryResponse.categoryResponse(categorySave, "Thêm danh mục thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm danh mục thất bại!");
        }


    }


    private Category createCategoryByCategoryRequest(CategoryRequest request, String username) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImage(request.getImage());
        category.setChild(request.isChild());
        category.setStatus(Status.ACTIVE);
        category.setCreatedBy(username);
        category.setUpdatedBy(username);

        if (request.isChild()) {
            category.setParent(getCategoryById(request.getParentId()));
        }

        return category;
    }


    private void validateCategoryDepth(Long categoryId) {
        checkExistCategoryById(categoryId);
        int depth = 0;

        validateCategoryDepthRecursive(categoryId, depth);
    }

    private void validateCategoryDepthRecursive(Long categoryId, int depth) {
        if (checkExistCategoryByIdAndChild(categoryId)) {
            depth++;
            if (depth > 1) {
                throw new BadRequestException("Danh mục cha tối không được lớn hơn 3!");
            }

            Long parentId = getCategoryById(categoryId).getParent().getCategoryId();
            validateCategoryDepthRecursive(parentId, depth);
        }
    }


    private void checkExistCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new BadRequestException("Danh mục không tồn tại!");
        }
    }


    private boolean checkExistCategoryByIdAndChild(Long categoryId) {
        return categoryRepository.existsByCategoryIdAndChild(categoryId, true);
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh mục theo id!"));
    }


    private void checkExistCategoryByName(String name) {
        if (categoryRepository.existsByNameAndStatus(name, Status.ACTIVE)) {
            throw new BadRequestException("Tên danh mục đã tồn tại!");
        }
    }
//    private int checkCountParentCategory(Long categoryId) {
//
//        checkExistCategoryById(categoryId);
//        int count = 0;
//        if (checkExistCategoryByIdAndChild(categoryId)) {
//            count++;
//            Long parentId = getCategoryById(categoryId).getParent().getCategoryId();
//            checkCountParentCategory(parentId);
//        }
//        if (count > 4) {
//            throw new BadRequestException("Danh mục cha không được lớn hơn 4!");
//        }
//
//        return count;
//

//    }

}
