package hcmute.kltn.vtv.model.entity.vendor;

import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Voucher {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String code;

    private String name;

    private String description;

    private Long discount;

    private Long quantity;

    private Date startDate;

    private Date endDate;

    private Long quantityUsed;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private VoucherType type;

    // @ManyToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "voucher_order", joinColumns = @JoinColumn(name =
    // "voucher_id"), inverseJoinColumns = @JoinColumn(name = "order_id"))
    // private List<Order> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
