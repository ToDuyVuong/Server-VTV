package hcmute.kltn.vtv.model.entity.user;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VoucherOrder {

    @Id
    @Column(name = "voucher_order_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherOrderId;


    private boolean type; // true: voucher system, false: voucher shop

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  // Trường tham chiếu đến Order

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
}
