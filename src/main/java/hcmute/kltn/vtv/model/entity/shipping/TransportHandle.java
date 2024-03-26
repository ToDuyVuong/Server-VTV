package hcmute.kltn.vtv.model.entity.shipping;

import hcmute.kltn.vtv.model.extra.TransportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransportHandle {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transportHandleId;

    private String username;

    // vi tri cua shipper
    private String wardCode;

    private boolean active;

    // thể hiện đã xử lý thành công hay chưa
    private boolean handled;

    @Enumerated(EnumType.STRING)
    private TransportStatus transportStatus;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;
}
