package hcmute.kltn.vtv.service.admin.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.model.data.admin.CategoryAdminDTO;
import hcmute.kltn.vtv.model.data.admin.request.CategoryAdminRequest;
import hcmute.kltn.vtv.model.data.admin.response.AllCategoryAdminResponse;
import hcmute.kltn.vtv.model.data.admin.response.CategoryAdminResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.service.admin.ICategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements ICategoryAdminService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public CategoryAdminResponse addNewCategory(CategoryAdminRequest request) {

//        Optional<Category> existingCategory = categoryRepository.findByNameAndAdminOnly(request.getName(), true);
//        if (existingCategory.isPresent()) {
//            throw new BadRequestException("Tên danh mục đã tồn tại!");
//        }
        Category category = modelMapper.map(request, Category.class);
        category.setChild(true);
        category.setStatus(Status.ACTIVE);
        category.setCreateAt(LocalDateTime.now());
        category.setUpdateAt(LocalDateTime.now());

        try {
            Category categorySave = categoryRepository.save(category);

            CategoryAdminDTO categoryAdminDTO = modelMapper.map(categorySave, CategoryAdminDTO.class);

            CategoryAdminResponse response = new CategoryAdminResponse();
            response.setCategoryAdminDTO(categoryAdminDTO);
            response.setCode(200);
            response.setMessage("Thêm danh mục thành công từ admin thành công!");
            response.setStatus("success");

            return response;
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm danh mục từ admin thất bại!");
        }
    }

    @Override
    @Transactional
    public CategoryAdminResponse getCategoryParent(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh mục!"));

        CategoryAdminDTO categoryAdminDTO = modelMapper.map(category, CategoryAdminDTO.class);

        CategoryAdminResponse response = new CategoryAdminResponse();
        response.setCategoryAdminDTO(categoryAdminDTO);
        response.setCode(200);
        response.setMessage("Lấy chi tiết danh mục thành công.");
        response.setStatus("ok");

        return response;
    }

    @Override
    @Transactional
    public AllCategoryAdminResponse getAllCategoryParent() {
        List<Category> categories = categoryRepository.findAllByChildAndStatus(true, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh mục cha nào!"));
        if (categories.isEmpty()) {
            AllCategoryAdminResponse response = new AllCategoryAdminResponse();
            response.setCode(404);
            response.setMessage("Không tìm thấy danh mục!");
            response.setStatus("error");
            return response;
        }

        categories.sort(Comparator.comparing(Category::getName));
        List<CategoryAdminDTO> categoryAdminDTOs = CategoryAdminDTO.convertToListDTO(categories);

        AllCategoryAdminResponse response = new AllCategoryAdminResponse();
        response.setCategoryAdminDTOs(categoryAdminDTOs);
        response.setCode(200);
        response.setMessage("Lấy danh mục thành công!");
        response.setStatus("ok");

        return response;
    }

    @Override
    @Transactional
    public CategoryAdminResponse updateCategoryParent(CategoryAdminRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh mục!"));

        if (!category.getName().equals(request.getName())) {
//            checkCategoryName(request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImage(request.getImage());
        category.setUpdateAt(LocalDateTime.now());

        try {
            categoryRepository.save(category);
            CategoryAdminDTO categoryAdminDTO = modelMapper.map(category, CategoryAdminDTO.class);

            CategoryAdminResponse response = new CategoryAdminResponse();
            response.setCategoryAdminDTO(categoryAdminDTO);
            response.setCode(200);
            response.setMessage("Cập nhật danh mục thành công!");
            response.setStatus("success");

            return response;
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật danh mục thất bại!");
        }
    }

    @Override
    @Transactional
    public CategoryAdminResponse updateStatusCategoryParent(Long categoryId, Status status) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh mục!"));

        category.setStatus(status);
        category.setUpdateAt(LocalDateTime.now());

        if (status == Status.DELETED) {
            List<Category> categories = categoryRepository.findAllByParent(category);
            if (!categories.isEmpty()) {
                for (Category category1 : categories) {
                    category1.setStatus(Status.DELETED);
                    category1.setUpdateAt(LocalDateTime.now());
                    try {
                        categoryRepository.save(category1);
                    } catch (Exception e) {
                        throw new BadRequestException("Cập nhật danh mục con thất bại!");
                    }
                }
            }
        }

        try {
            Category categorySave = categoryRepository.save(category);
            CategoryAdminDTO categoryAdminDTO = modelMapper.map(categorySave, CategoryAdminDTO.class);

            CategoryAdminResponse response = new CategoryAdminResponse();
            response.setCategoryAdminDTO(categoryAdminDTO);
            response.setCode(200);
            response.setMessage("Cập nhật danh mục thành công!");
            response.setStatus("success");

            return response;
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật danh mục thất bại!");
        }
    }

//    private void checkCategoryName(String name) {
//        Optional<Category> category = categoryRepository.findByNameAndAdminOnly(name, true);
//        if (category.isPresent()) {
//            throw new BadRequestException("Tên danh mục đã tồn tại!");
//        }
//    }
}
