package hcmute.kltn.vtv.model.dto.user;


import hcmute.kltn.vtv.model.entity.user.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationDTO {

    private UUID notificationId;

    private String title;

    private String body;

    private String recipient;

    private String sender;

    private String type;

    private boolean seen;


//    private boolean isDeleted;

    private LocalDateTime createAt;


    public static NotificationDTO convertEntityToDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationId(notification.getNotificationId());
        notificationDTO.setTitle(notification.getTitle());
        notificationDTO.setBody(notification.getBody());
        notificationDTO.setRecipient(notification.getRecipient());
        notificationDTO.setSender(notification.getSender());
        notificationDTO.setType(notification.getType());
        notificationDTO.setSeen(notification.isSeen());
//        notificationDTO.setDeleted(notification.isDeleted());
        notificationDTO.setCreateAt(notification.getCreateAt());

        return notificationDTO;
    }


    public static List<NotificationDTO> convertEntitiesToListDTOs(List<Notification> notifications) {
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationDTOs.add(convertEntityToDTO(notification));
        }
        notificationDTOs.sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()));

        return notificationDTOs;
    }
}
