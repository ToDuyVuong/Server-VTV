package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.guest.BrandResponse;
import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vtv.request.BrandRequest;
import hcmute.kltn.vtv.service.manager.IManagerBrandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/brand")
@RequiredArgsConstructor
public class ManagerBrandController {



    @Autowired
    private final IManagerBrandService managerBrandService;


    @PostMapping("/add")
    public ResponseEntity<BrandResponse> addNewBrand(@ModelAttribute BrandRequest brandRequest,
                                                  HttpServletRequest request) {
        brandRequest.validate();
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(managerBrandService.addNewBrand(brandRequest, username));

    }


    @PostMapping("/update/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long brandId,
                                                     @ModelAttribute BrandRequest brandRequest,
                                                     HttpServletRequest request) {
        brandRequest.validate();
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(managerBrandService.updateBrand(brandId, brandRequest, username));
    }


    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<ResponseClass> deleteBrand(@PathVariable Long brandId, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(managerBrandService.deleteBrand(brandId, username));
    }


}
