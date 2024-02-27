package hcmute.kltn.vtv.model.data.user.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String title;

    private String body;

    private String recipient;

    private String sender;

    private String type;
}
