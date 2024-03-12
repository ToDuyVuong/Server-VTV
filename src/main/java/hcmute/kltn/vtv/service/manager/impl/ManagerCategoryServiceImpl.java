package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vtv.request.CategoryRequest;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.service.manager.IManagerBrandService;
import hcmute.kltn.vtv.service.manager.IManagerCategoryService;
import hcmute.kltn.vtv.service.manager.IManagerProductService;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerCategoryServiceImpl implements IManagerCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final IManagerProductService managerProductService;
    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final IImageService imageService;


    @Override
    @Transactional
    public CategoryResponse addNewCategoryByManager(CategoryRequest request, String username) {
        checkExistCategoryByName(request.getName());
        if (request.isChild()) {
            validateCategoryDepth(request.getParentId());
        }
        Category category = createCategoryByCategoryRequest(request, username);
        try {
            categoryRepository.save(category);

            return CategoryResponse.categoryResponse(category, "Thêm danh mục thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm danh mục thất bại!");
        }
    }


    @Override
    @Transactional
    public CategoryResponse updateCategoryByManager(CategoryRequest request, Long categoryId, String username) {
        checkExistCategoryById(categoryId);
        checkExistCategoryByCategoryIdAndName(categoryId, request.getName());
        if (request.isChild()) {
            validateCategoryDepth(request.getParentId());
        }

        Category category = getCategoryById(categoryId);
        String oldImage = category.getImage();
        updateCategoryByCategoryRequest(category, request, username);
        try {
            categoryRepository.save(category);
            if (request.isChangeImage()) {
                imageService.deleteImageFromFirebase(oldImage);
            }

            return CategoryResponse.categoryResponse(category, "Cập nhật danh mục thành công!", "Success");
        } catch (Exception e) {
            if (request.isChangeImage()) {
                imageService.deleteImageFromFirebase(category.getImage());
            }
            throw new InternalServerErrorException("Cập nhật danh mục thất bại!");
        }
    }


    @Override
    @Transactional
    public ResponseClass deleteCategoryNoUsingByManager(Long categoryId) {
        checkExistCategoryById(categoryId);
        checkUsingCategory(categoryId);
        String oldImage = getCategoryById(categoryId).getImage();
        try {
            categoryRepository.deleteById(categoryId);
            imageService.deleteImageFromFirebase(oldImage);

            return ResponseClass.responseClass("Xóa danh mục thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa danh mục thất bại!");
        }
    }


    @Override
    public List<Category> getCategoriesByIds(List<Long> categoryIds) {
        return categoryRepository.findAllByCategoryIdInAndStatus(categoryIds, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục!"));
    }


    private void checkUsingCategory(Long categoryId) {

        if (brandRepository.existsBrandUsingCategoryIdInCategories(categoryId)) {
            throw new BadRequestException("Danh mục đang chứa thương hiệu, không thể xóa!");
        }

        if (managerProductService.checkExistProductUseCategory(categoryId)) {
            throw new BadRequestException("Danh mục đang chứa sản phẩm, không thể xóa!");
        }
    }


    private Category createCategoryByCategoryRequest(CategoryRequest request, String username) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImage(imageService.uploadImageToFirebase(request.getImage()));
        category.setChild(request.isChild());
        category.setStatus(Status.ACTIVE);
        category.setCreatedBy(username);
        category.setUpdatedBy(username);
        if (request.isChild()) {
            category.setParent(getCategoryById(request.getParentId()));
        }
        category.setCreateAt(LocalDateTime.now());
        category.setUpdateAt(LocalDateTime.now());

        return category;
    }


    private void updateCategoryByCategoryRequest(Category category, CategoryRequest request, String username) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImage(request.isChangeImage() ? imageService.uploadImageToFirebase(request.getImage()) : category.getImage());
        category.setChild(request.isChild());
        category.setUpdatedBy(username);
        category.setParent(null);
        if (request.isChild()) {
            category.setParent(getCategoryById(request.getParentId()));
        }
        category.setUpdateAt(LocalDateTime.now());
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


    private void checkExistCategoryByCategoryIdAndName(Long categoryId, String name) {
        if (!categoryRepository.existsByCategoryIdAndName(categoryId, name)
                && categoryRepository.existsByNameAndStatus(name, Status.ACTIVE)) {
            throw new BadRequestException("Tên danh mục đã tồn tại!");
        }
    }


}
