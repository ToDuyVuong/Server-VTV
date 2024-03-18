package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ShopRequest;
import hcmute.kltn.vtv.model.data.vendor.request.UpdateShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

public interface IShopService {
    ShopResponse registerShop(ShopRequest request, String username);

    ShopResponse getProfileShop(String username);


    @Transactional
    ShopResponse updateShop(ShopRequest request, String username);

    ShopResponse updateStatusShop(String username, Status status);

    Shop getShopByUsername(String username);
}
