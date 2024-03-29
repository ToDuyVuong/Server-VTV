package hcmute.kltn.vtv.model.entity.manager;

import hcmute.kltn.vtv.model.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ManagerCustomer {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerCustomerId;

    private String note;

    @Column(name = "is_lock")
    private boolean lock;

    @Column(name = "is_delete")
    private boolean delete;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Customer manager;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
