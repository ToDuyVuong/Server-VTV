package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.manager.request.CategoryRequest;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerCategoryService {
    @Transactional
    CategoryResponse addNewCategoryByManager(CategoryRequest request, String username);

    @Transactional
    CategoryResponse updateCategoryByManager(CategoryRequest request, Long categoryId, String username);

    @Transactional
    ResponseClass deleteCategoryNoUsingByManager(Long categoryId);
}
