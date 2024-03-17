package hcmute.kltn.vtv.model.entity.user;

import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

}
