package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.response.ListManagerProductResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductResponse;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerProductService {

    @Transactional
    ManagerProductResponse lockProductByProductId(Long productId, String username, String note);

    @Transactional
    ManagerProductResponse unLockProductByProductId(Long productId, String username, String note);

    ListManagerProductResponse getListManagerProduct(int page, int size);

    boolean checkExistProductUseCategory(Long categoryId);

    ListManagerProductResponse getListManagerProductByProductName(int page, int size, String productName);

    void checkRequestPageParams(int page, int size);
}
