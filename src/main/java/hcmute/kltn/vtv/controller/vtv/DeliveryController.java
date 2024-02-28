package hcmute.kltn.vtv.controller.vtv;


import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.service.vtv.IDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    @Autowired
    private final IDeliveryService deliveryService;

    @GetMapping("/calculate-shipping")
    public ResponseEntity<ShippingResponse> calculateShipping(@Param("wardCodeCustomer") String wardCodeCustomer,
                                                              @Param("wardCodeShop") String wardCodeShop,
                                                              @Param("shippingProvider") String shippingProvider) {

        return ResponseEntity.ok(deliveryService.calculateShipping(wardCodeCustomer, wardCodeShop, shippingProvider));
    }

}
