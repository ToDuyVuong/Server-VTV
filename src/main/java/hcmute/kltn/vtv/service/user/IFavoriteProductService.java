package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.FavoriteProductResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFavoriteProductResponse;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import org.springframework.transaction.annotation.Transactional;

public interface IFavoriteProductService {
    @Transactional
    FavoriteProductResponse addNewFavoriteProduct(Long productId, String username);

    ProductResponse getProductByFavoriteProductId(Long favoriteProductId, String username);

    ListFavoriteProductResponse getListFavoriteProduct(String username);

    FavoriteProductResponse deleteFavoriteProduct(Long favoriteProductId, String username);
}
