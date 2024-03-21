package hcmute.kltn.vtv.model.entity.wallet;


import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LoyaltyPoint {


    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loyaltyPointId;

    private Long totalPoint;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String username;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "loyaltyPoint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LoyaltyPointHistory> loyaltyPointHistories;

}
