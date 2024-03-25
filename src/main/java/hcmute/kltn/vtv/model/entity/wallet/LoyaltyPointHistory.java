package hcmute.kltn.vtv.model.entity.wallet;


import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LoyaltyPointHistory {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loyaltyPointHistoryId;

    private Long point;

    private String type;


    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_point_id")
    private LoyaltyPoint loyaltyPoint;
}
