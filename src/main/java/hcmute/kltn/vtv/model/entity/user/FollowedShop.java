package hcmute.kltn.vtv.model.entity.user;

import hcmute.kltn.vtv.model.entity.vendor.Shop;
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
public class FollowedShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followedShopId;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Shop shop;

    private LocalDateTime createAt;
}
