package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.guest.BrandResponse;
import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.vtv.request.BrandRequest;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerBrandService {
    boolean existsBrandUsingCategoryIdInCategories(Long categoryId);

    @Transactional
    BrandResponse addNewBrandByManager(BrandRequest brandRequest, String username);

    @Transactional
    BrandResponse updateBrandByManager(Long brandId, BrandRequest brandRequest, String username);

    @Transactional
    ResponseClass deleteBrandByManager(Long brandId, String username);
}
