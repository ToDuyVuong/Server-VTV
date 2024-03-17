package hcmute.kltn.vtv.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmute.kltn.vtv.model.entity.location.Ward;
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
public class Address {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String fullAddress;

    private String fullName;

    private String phone;


    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_code")
    private Ward ward;

}
