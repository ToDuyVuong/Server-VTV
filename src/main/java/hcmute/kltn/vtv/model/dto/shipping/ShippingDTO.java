package hcmute.kltn.vtv.model.dto.shipping;

import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import hcmute.kltn.vtv.util.DateUtils;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDTO {

    private Long transportProviderId;
    private String transportProviderFullName;
    private String transportProviderShortName;
    private Long shippingCost;
//    private String currency;
    private String estimatedDeliveryTime;
    private String timestamp;

    public static ShippingDTO createShippingDTO(TransportProvider transportProvider, Long shippingCost, Date estimatedDeliveryTime, Date timestamp) {
        ShippingDTO shippingDTO = new ShippingDTO();

        shippingDTO.setTransportProviderId(transportProvider.getTransportProviderId());
        shippingDTO.setTransportProviderFullName(transportProvider.getFullName());
        shippingDTO.setTransportProviderShortName(transportProvider.getShortName());
        shippingDTO.setShippingCost(shippingCost);
//        shippingDTO.setCurrency(currency);
        shippingDTO.setEstimatedDeliveryTime(DateUtils.formatDate(estimatedDeliveryTime, "yyyy-MM-dd"));
        shippingDTO.setTimestamp(DateUtils.formatDate(timestamp, "yyyy-MM-dd"));

        return shippingDTO;
    }


}