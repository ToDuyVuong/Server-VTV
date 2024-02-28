package hcmute.kltn.vtv.model.data.shipping.response;


import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingResponse extends ResponseAbstract {

    private int distance;
    private Long shippingCost;
    private String currency;
    private Date estimatedDeliveryTime;
    private Date timestamp;
    private String shippingProvider;


}
