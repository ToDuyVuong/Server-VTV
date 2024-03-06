package hcmute.kltn.vtv.service.shipping;

import java.util.Date;

public interface IShippingTimeEstimationService {
    Date estimateDeliveryTime(int distance, String shippingProvider);
}
