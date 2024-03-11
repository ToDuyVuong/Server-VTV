package hcmute.kltn.vtv.model.entity.vtv;

import hcmute.kltn.vtv.model.entity.user.Customer;
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
public class Brand {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    private String name;

    private String image;

    private String description;

    private String information;

    private String origin;

    private String createdBy;

    private String updatedBy;

    // access all categories
    private boolean allCategories;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;



    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "brand_category",
            joinColumns = @JoinColumn(name = "brand_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = true))
    private List<Category> categories;



}
