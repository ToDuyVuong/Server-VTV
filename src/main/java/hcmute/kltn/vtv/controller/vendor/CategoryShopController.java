package hcmute.kltn.vtv.controller.vendor;


import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor/category-shop")
@RequiredArgsConstructor
public class CategoryShopController {

    @Autowired
    private final ICategoryShopService categoryShopService;


}
