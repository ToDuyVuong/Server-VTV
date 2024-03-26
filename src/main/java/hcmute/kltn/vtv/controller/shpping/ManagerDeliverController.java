package hcmute.kltn.vtv.controller.shpping;

import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateDeliverWorkRequest;
import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ListDeliverResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.service.shipping.IManagerDeliverService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transport-provider/manager/deliver")
@RequiredArgsConstructor
public class ManagerDeliverController {

    private final IManagerDeliverService managerDeliverService;


    @PostMapping("/add-manager")
    public ResponseEntity<DeliverResponse> addNewDeliverManagerByProvider(@RequestBody DeliverRequest request,
                                                         HttpServletRequest servletRequest) {

        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerDeliverService.addNewDeliverManagerByProvider(request, usernameAdded));
    }

    @PostMapping("/add")
    public ResponseEntity<DeliverResponse> addNewDeliverByManager(@RequestBody DeliverRequest request,
                                                         HttpServletRequest servletRequest) {

        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerDeliverService.addNewDeliverByManager(request, usernameAdded));
    }

    @PutMapping("/update-work")
    public ResponseEntity<DeliverResponse> updateDeliverWork(@RequestBody UpdateDeliverWorkRequest request,
                                                             HttpServletRequest servletRequest) {
        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerDeliverService.updateDeliverWork(request, usernameAdded));
    }

    @PutMapping("/{deliverId}/status/{status}")
    public ResponseEntity<DeliverResponse> updateStatusDeliver(@PathVariable("deliverId") Long deliverId,
                                                               @PathVariable("status") Status status,
                                                               HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(managerDeliverService.updateStatusDeliver(deliverId, status, username));
    }

    @GetMapping("/list/status/{status}")
    public ResponseEntity<ListDeliverResponse> getListDeliverByStatus(@PathVariable("status") Status status) {

        return ResponseEntity.ok(managerDeliverService.getListDeliverByStatus(status));
    }

    @GetMapping("/list/status/{status}/type-work/{typeWork}")
    public ResponseEntity<ListDeliverResponse> getListDeliverByStatusAndTypeWork(@PathVariable("status") Status status,
                                                                                 @PathVariable("typeWork") TypeWork typeWork) {

        return ResponseEntity.ok(managerDeliverService.getListDeliverByStatusAndTypeWork(status, typeWork));
    }


}
