package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.NotificationRequest;
import hcmute.kltn.vtv.model.data.user.response.NotificationResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface INotificationService {
    @Transactional
    void addNewNotification(NotificationRequest request);

    NotificationResponse getNotificationsByUsername(String username, int page, int size);

    NotificationResponse deleteNotificationById(UUID notificationId, String username);

    NotificationResponse readNotification(UUID notificationId, String username);
}
