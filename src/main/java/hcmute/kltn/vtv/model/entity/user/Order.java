package hcmute.kltn.vtv.model.entity.user;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "`order`")
public class Order {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Date orderDate;

    private String note;

    private int count;

    private Long shopId;

    private String shopName;

    private Long totalPrice;

    private Long discount;

    private Long shippingFee;

    private String paymentMethod;

    private Long paymentTotal;

    private String shippingMethod;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_code")
    private Ward wardCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VoucherOrder> voucherOrders;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

}
