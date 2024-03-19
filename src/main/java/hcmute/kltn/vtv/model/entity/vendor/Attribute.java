package hcmute.kltn.vtv.model.entity.vendor;

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
public class Attribute {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;

    private String name;

    private String value;

    private boolean active;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.EAGER)
    private List<ProductVariant> productVariants;

    public boolean isUsedInProductVariants() {
        return productVariants != null && !productVariants.isEmpty();
    }

}
