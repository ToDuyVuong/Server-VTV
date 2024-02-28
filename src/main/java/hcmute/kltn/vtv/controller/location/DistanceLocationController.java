package hcmute.kltn.vtv.controller.location;


import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location/distance")
@RequiredArgsConstructor
public class DistanceLocationController {

    @Autowired
    private final IDistanceLocationService distanceService;


    @GetMapping("/calculate")
    public ResponseEntity<Object> calculateDistance(@Param("wardCodeCustomer") String wardCodeCustomer, @Param("wardCodeShop") String wardCodeShop) {
        int distance = distanceService.calculateDistance(wardCodeCustomer, wardCodeShop);
        String message = distanceService.messageByLeverRegion(distance);
        return ResponseEntity.ok().body(distance + " " + message);
    }


}
