package hcmute.kltn.vtv.service.user.impl;

import com.google.firebase.messaging.*;
import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.model.entity.user.FcmToken;
import hcmute.kltn.vtv.repository.user.FcmTokenRepository;
import hcmute.kltn.vtv.service.user.IFcmService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmServiceImpl implements IFcmService {

    @Autowired
    private final FirebaseMessaging firebaseMessaging;
    @Autowired
    private final FcmTokenRepository fcmTokenRepository;


    @Override
    public void addNewFcmToken(String fcmToken, String username) {
        if (fcmTokenRepository.existsByFcmToken(fcmToken)) {
            return;
        }
        FcmToken fcmTokenEntity = new FcmToken();
        fcmTokenEntity.setFcmToken(fcmToken);
        fcmTokenEntity.setUsername(username);
        try {
            fcmTokenRepository.save(fcmTokenEntity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi thêm fcm token");
        }
    }


    @Override
    public void sendNotification(Notification notice) {
        List<String> registrationTokens = getFcmTokens(notice.getRecipient());
        MulticastMessage message = createMulticastMessage(notice, registrationTokens);
//        BatchResponse batchResponse = null;

        try {
            firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new InternalServerErrorException("Lỗi khi gửi nhiều thông báo");
        }
    }


    @Override
    public void deleteFcmToken(String fcmToken) {
        try {
            fcmTokenRepository.deleteByFcmToken(fcmToken);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa fcm token");
        }
    }


    private List<String> getFcmTokens(String username) {
        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy fcm token"));
        return fcmTokens.stream().map(FcmToken::getFcmToken).toList();
    }


    private com.google.firebase.messaging.Notification createNotification(Notification notice) {
        return com.google.firebase.messaging.Notification.builder()
                .setTitle(notice.getTitle())
                .setBody(notice.getBody())
                .build();
    }


    private MulticastMessage createMulticastMessage(Notification notice, List<String> registrationTokens) {
        com.google.firebase.messaging.Notification notification = createNotification(notice);
        return MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .putAllData(null)
                .build();
    }






}

