package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.admin.response.AllCategoryAdminResponse;
import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.service.admin.ICategoryAdminService;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {


    @Autowired
    private final ICategoryService categoryService;

    @GetMapping("/all-parent")
    public ResponseEntity<CategoryResponse> getParentCategory() {
        return ResponseEntity.ok(categoryService.getAllCategoryParent());
    }


    @GetMapping("/all-category/by-parent/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryByParentId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAllCategoryByParentId(categoryId));
    }



}
