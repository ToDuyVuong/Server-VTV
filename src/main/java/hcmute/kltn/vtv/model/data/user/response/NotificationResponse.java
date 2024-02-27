package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.NotificationDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;

    private List<NotificationDTO> notificationDTOs;
}
