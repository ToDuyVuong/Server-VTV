package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.AddressRequest;
import hcmute.kltn.vtv.model.data.user.request.AddressStatusRequest;
import hcmute.kltn.vtv.model.data.user.response.AddressResponse;
import hcmute.kltn.vtv.model.data.user.response.ListAddressResponse;
import hcmute.kltn.vtv.service.user.IAddressService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/address")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private final IAddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<AddressResponse> addNewAddressByUsername(@RequestBody AddressRequest addressRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        addressRequest.setUsername(username);
        addressRequest.validate();

        return ResponseEntity.ok(addressService.addNewAddress(addressRequest));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable("addressId") Long addressId,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (addressId == null) {
            throw new BadRequestException("Id địa chỉ không được để trống.");
        }

        return ResponseEntity.ok(addressService.getAddressById(addressId, username));
    }

    @PutMapping("/update")
    public ResponseEntity<AddressResponse> updateAddressByUsername(@RequestBody AddressRequest addressRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        addressRequest.setUsername(username);
        addressRequest.validate();

        return ResponseEntity.ok(addressService.updateAddress(addressRequest));
    }

    @PatchMapping("/update/status")
    public ResponseEntity<AddressResponse> updateStatusAddressByUsername(
            @RequestBody AddressStatusRequest addressStatusRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        addressStatusRequest.setUsername(username);
        addressStatusRequest.validate();

        return ResponseEntity.ok(addressService.updateStatusAddress(addressStatusRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<ListAddressResponse> getAllAddressByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(addressService.getAllAddress(username));
    }

}
