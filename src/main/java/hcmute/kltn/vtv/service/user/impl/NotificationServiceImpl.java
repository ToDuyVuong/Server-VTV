package hcmute.kltn.vtv.service.user.impl;


import hcmute.kltn.vtv.model.data.user.request.NotificationRequest;
import hcmute.kltn.vtv.model.data.user.response.NotificationResponse;
import hcmute.kltn.vtv.model.dto.user.NotificationDTO;
import hcmute.kltn.vtv.model.entity.user.Notification;
import hcmute.kltn.vtv.repository.user.NotificationRepository;
import hcmute.kltn.vtv.service.user.IFcmService;
import hcmute.kltn.vtv.service.user.INotificationService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private final IFcmService fcmService;


    @Override
    @Transactional
    public void addNewNotification(NotificationRequest request) {
        Notification notification = createNotification(request);

        try {
            notificationRepository.save(notification);
            fcmService.sendNotification(notification);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi thêm thông báo mới");
        }
    }



    @Override
    public NotificationResponse getNotificationsByUsername(String username, int page, int size) {
        Page<Notification> notifications = getNotifications(username, page, size);
        return notificationResponse(notifications, "Lấy danh sách thông báo thành công.");
    }


    @Override
    @Transactional
    public NotificationResponse deleteNotificationById(UUID notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo nào"));

        try {
            notificationRepository.save(notification);

            return notificationResponse(getNotifications(username, 1, 20), "Xóa thông báo thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa thông báo");
        }
    }


    @Override
    @Transactional
    public NotificationResponse readNotification(UUID notificationId, String username) {
        Notification notification = getNotificationByIdAndRecipient(notificationId, username);

        try {
            notification.setRead(true);
            notificationRepository.save(notification);

            return notificationResponse(getNotifications(username, 1, 20), "Đọc thông báo thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi đọc thông báo");
        }
    }


    private Notification getNotificationByIdAndRecipient(UUID notificationId, String username) {
        return notificationRepository.findByNotificationIdAndRecipient(notificationId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo theo id và người nhận"));
    }




    private Page<Notification> getNotifications(String username, int page, int size) {
        return notificationRepository
                .findByRecipientOrderByCreateAtDesc(username, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo nào"));
    }


    public NotificationRequest createNotificationRequest(String title, String body, String recipient, String sender, String type) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setRecipient(recipient);
        notificationRequest.setSender(sender);
        notificationRequest.setType(type);
        return notificationRequest;
    }

    private Notification createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setBody(request.getBody());
        notification.setRecipient(request.getRecipient());
        notification.setSender(request.getSender());
        notification.setType(request.getType());
        notification.setRead(false);
        notification.setCreateAt(LocalDateTime.now());

        return notification;
    }


    private NotificationResponse notificationResponse(Page<Notification> notifications, String message) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationDTOs(NotificationDTO.convertEntitiesToListDTOs(notifications.getContent()));
        response.setTotalPage(notifications.getTotalPages());
        response.setCount((int) notifications.getNumberOfElements());
        response.setPage(notifications.getNumber() + 1);
        response.setSize(notifications.getSize());
        response.setStatus("OK");
        response.setCode(200);
        response.setMessage("message");

        return response;
    }


}
