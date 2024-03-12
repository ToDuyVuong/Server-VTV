package hcmute.kltn.vtv.model.entity.vendor;

import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productVariantId;

    private String sku;

    private String image;

    private Long originalPrice;

    private Long price;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_variant_attribute", joinColumns = @JoinColumn(name = "product_variant_id", nullable = true), inverseJoinColumns = @JoinColumn(name = "attribute_id", nullable = true))
    private List<Attribute> attributes;

}
