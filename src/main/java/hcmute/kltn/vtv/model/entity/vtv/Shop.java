package hcmute.kltn.vtv.model.entity.vtv;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shop {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    private String name;

    private String address;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String phone;

    private String email;

    private String avatar;

    private String description;

    private String openTime;

    private String closeTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_code")
    private Ward ward;

}
