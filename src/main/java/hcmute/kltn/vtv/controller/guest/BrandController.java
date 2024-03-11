package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.guest.BrandResponse;
import hcmute.kltn.vtv.model.data.guest.ListBrandResponse;
import hcmute.kltn.vtv.service.guest.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {

    @Autowired
    private final IBrandService brandService;


    @GetMapping("/get/{brandId}")
    public ResponseEntity<BrandResponse> getBrandByBrandId(@PathVariable Long brandId) {

        return ResponseEntity.ok(brandService.getBrandByBrandId(brandId));

    }

    @GetMapping("/get/category/{categoryId}")
    public ResponseEntity<ListBrandResponse> getBrandsByCategoryId(@PathVariable Long categoryId) {

        return ResponseEntity.ok(brandService.getBrandsByCategoryId(categoryId));
    }


}
