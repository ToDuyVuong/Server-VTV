package hcmute.kltn.vtv.model.entity.wallet;

import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WithdrawalHistory {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID withdrawalId;

    private Long money;

    private String method;

    private String accountNumber;

    private String accountName;

    private String bankName;

    private String transactionCode;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime withdrawalAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
