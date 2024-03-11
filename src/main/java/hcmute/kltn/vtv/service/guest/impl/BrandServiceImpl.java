package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.guest.BrandResponse;
import hcmute.kltn.vtv.model.data.guest.ListBrandResponse;
import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.service.guest.IBrandService;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final ICategoryService categoryService;


    @Override
    public BrandResponse getBrandByBrandId(Long brandId) {
        checkExistBrandByBrandId(brandId);
        return brandRepository.findById(brandId)
                .map(brand -> BrandResponse.brandResponse(brand, "Lấy thông tin thương hiệu thành công!", "OK"))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thương hiệu theo id!"));
    }


    @Override
    public ListBrandResponse getBrandsByCategoryId(Long categoryId) {
        categoryService.checkExistCategoryById(categoryId);

        return brandRepository.findAllByCategories_CategoryIdAndStatusOrCategoriesEmpty(categoryId, Status.ACTIVE)
                .map(brand -> ListBrandResponse.listBrandResponse(brand, "Lấy danh sách thương hiệu theo danh mục thành công!", "OK"))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thương hiệu theo danh mục!"));

    }


    private void checkExistBrandByBrandId(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new NotFoundException("Không tìm thấy thương hiệu!");
        }
    }


}
