package hcmute.kltn.vtv.controller.test;

import hcmute.kltn.vtv.service.user.impl.FcmServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PushNotificationController {

    @Autowired
    private FcmServiceImpl fcmService;

    @PostMapping("/notification")
    public String sendSampleNotification(@RequestBody PnsRequest pnsRequest) {

        return fcmService.pushNotification(pnsRequest);
    }
}

