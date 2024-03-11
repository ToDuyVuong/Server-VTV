package hcmute.kltn.vtv.controller.user;


import hcmute.kltn.vtv.model.data.user.response.NotificationResponse;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private final INotificationService notificationService;
    @Autowired
    private final IPageService pageService;

    @GetMapping("/get-page")
    public ResponseEntity<NotificationResponse> getNotifications(@Param("page") int page,
                                                                 @Param("size") int size,
                                                                 HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(notificationService.getNotificationsByUsername(username, page, size));
    }


    @PutMapping("/read/{notificationId}")
    public ResponseEntity<NotificationResponse> readNotification(@PathVariable("notificationId") UUID notificationId,
                                                               HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(notificationService.readNotification(notificationId, username));
    }


    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<NotificationResponse> deleteNotification(@PathVariable("notificationId") UUID notificationId,
                                                                  HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(notificationService.deleteNotificationById(notificationId, username));
    }
}
