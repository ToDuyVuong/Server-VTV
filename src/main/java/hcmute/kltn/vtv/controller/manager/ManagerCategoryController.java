package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.data.manager.request.CategoryRequest;
import hcmute.kltn.vtv.service.manager.IManagerCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager/category")
@RequiredArgsConstructor
public class ManagerCategoryController {


    @Autowired
    private final IManagerCategoryService managerCategoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addNewCategoryByManager(
            @RequestBody CategoryRequest request, HttpServletRequest servletRequest) {

        String username = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerCategoryService.addNewCategoryByManager(request, username));
    }


}
