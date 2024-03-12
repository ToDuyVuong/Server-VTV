package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vtv.request.CategoryRequest;
import hcmute.kltn.vtv.service.manager.IManagerCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/category")
@RequiredArgsConstructor
public class ManagerCategoryController {


    @Autowired
    private final IManagerCategoryService managerCategoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addNewCategoryByManager(
            @ModelAttribute CategoryRequest request, HttpServletRequest servletRequest) {

        String username = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerCategoryService.addNewCategoryByManager(request, username));
    }


    @PostMapping("/update/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategoryByManager(
            @ModelAttribute CategoryRequest request,
            @PathVariable("categoryId") Long categoryId,
            HttpServletRequest servletRequest) {

        String username = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerCategoryService.updateCategoryByManager(request, categoryId, username));
    }


    @DeleteMapping("/delete/no-using/{categoryId}")
    public ResponseEntity<ResponseClass> deleteCategoryNoUsingByManager(
            @PathVariable("categoryId") Long categoryId) {

        return ResponseEntity.ok(managerCategoryService.deleteCategoryNoUsingByManager(categoryId));
    }


}
