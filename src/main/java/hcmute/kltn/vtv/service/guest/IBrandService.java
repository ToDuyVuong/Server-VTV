package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.BrandResponse;
import hcmute.kltn.vtv.model.data.guest.ListBrandResponse;

public interface IBrandService {

    BrandResponse getBrandByBrandId(Long brandId);

    ListBrandResponse getBrandsByCategoryId(Long categoryId);
}
