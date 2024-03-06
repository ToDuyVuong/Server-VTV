package hcmute.kltn.vtv.controller.shpping;


import hcmute.kltn.vtv.model.data.shipping.response.ListShippingResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {

    @Autowired
    private final IShippingService shippingService;
    @Autowired
    private final IWardService wardService;

    @GetMapping("/calculate-shipping")
    public ResponseEntity<ShippingResponse> calculateShipping(@Param("wardCodeCustomer") String wardCodeCustomer,
                                                              @Param("wardCodeShop") String wardCodeShop,
                                                              @Param("shippingProvider") String shippingProvider) {
        wardService.checkWardCodeExist(wardCodeCustomer);
        wardService.checkWardCodeExist(wardCodeShop);
        shippingService.checkShippingProvider(shippingProvider);
        return ResponseEntity.ok(shippingService
                .getCalculateShippingByWardAndTransportProvider(wardCodeCustomer, wardCodeShop, shippingProvider));
    }

    @GetMapping("/transport-providers")
    public ResponseEntity<ListShippingResponse> getShippingProvidersByWard(@Param("wardCodeCustomer") String wardCodeCustomer,
                                                                           @Param("wardCodeShop") String wardCodeShop) {
        wardService.checkWardCodeExist(wardCodeCustomer);
        wardService.checkWardCodeExist(wardCodeShop);
        return ResponseEntity.ok(shippingService.getShippingProvidersByWard(wardCodeCustomer, wardCodeShop));
    }

}
