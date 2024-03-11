package hcmute.kltn.vtv.service.vtv;

import hcmute.kltn.vtv.model.entity.user.Notification;

public interface IFcmService {
    void addNewFcmToken(String fcmToken, String username);

    void sendNotification(Notification notice);

    void deleteFcmToken(String fcmToken);
}
