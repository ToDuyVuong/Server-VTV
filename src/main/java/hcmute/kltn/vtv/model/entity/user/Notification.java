package hcmute.kltn.vtv.model.entity.user;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    //    @Column( columnDefinition = "boolean default false")
    private boolean seen;

//    @Column(columnDefinition = "boolean default false")
//    private boolean deleted;

    private LocalDateTime createAt;
}

