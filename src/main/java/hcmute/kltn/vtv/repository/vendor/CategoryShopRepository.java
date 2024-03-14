package hcmute.kltn.vtv.repository.vendor;


import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryShopRepository extends JpaRepository<CategoryShop, Long> {
}
