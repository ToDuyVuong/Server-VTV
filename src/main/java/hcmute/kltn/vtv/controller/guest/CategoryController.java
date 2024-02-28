package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.admin.response.AllCategoryAdminResponse;
import hcmute.kltn.vtv.service.admin.ICategoryAdminService;
import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {


    @Autowired
    private ICategoryAdminService categoryAdminService;

    @GetMapping("/all-parent")
    public ResponseEntity<AllCategoryAdminResponse> getParentCategory() {
        return ResponseEntity.ok(categoryAdminService.getAllCategoryParent());
    }



}
