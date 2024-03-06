package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.FavoriteProductDTO;
import hcmute.kltn.vtv.model.entity.user.FavoriteProduct;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductResponse extends ResponseAbstract {

    private FavoriteProductDTO favoriteProductDTO;

    public static FavoriteProductResponse favoriteProductResponse(FavoriteProduct favoriteProduct, String message, String status) {
        FavoriteProductResponse response = new FavoriteProductResponse();
        if (favoriteProduct != null) {
            response.setFavoriteProductDTO(FavoriteProductDTO.convertEntityToDTO(favoriteProduct));
        }

        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

}
