package hcmute.kltn.vtv.service.vtv;

import java.util.Date;

public interface IDeliveryTimeEstimationService {
    Date estimateDeliveryTime(int distance, String shippingProvider);
}
