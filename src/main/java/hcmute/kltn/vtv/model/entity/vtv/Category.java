package hcmute.kltn.vtv.model.entity.vtv;

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
public class Category {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    private String image;

    private String description;

    private boolean adminOnly;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id", nullable = true)
    // @JsonIgnore
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", nullable = true)
    // @JsonIgnore
    private Category parent;

    // @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch =
    // FetchType.EAGER)
    // private List<Category> children;



}
