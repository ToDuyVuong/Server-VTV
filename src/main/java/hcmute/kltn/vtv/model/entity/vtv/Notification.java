package hcmute.kltn.vtv.model.entity.vtv;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private String title;

    private String body;

    private String recipient;

    private String sender;

    private String type;

    private boolean isRead;

    private boolean isDeleted;
}

