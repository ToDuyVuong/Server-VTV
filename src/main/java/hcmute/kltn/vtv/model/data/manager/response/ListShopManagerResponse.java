package hcmute.kltn.vtv.model.data.manager.response;

import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListShopManagerResponse extends ResponseAbstract {

        private int count;
        private int size;
        private int page;
        private int totalPage;
        private List<ShopDTO> shopDTOs;
}
