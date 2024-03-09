package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);



    @Query(value =
            "SELECT CASE " +
                    "WHEN COUNT(b) > 0 THEN true ELSE false END " +
                    "FROM Brand b " +
                    "JOIN brand_category bc" +
                    "ON b.brand_id = bc.brand_id " +
                    "WHERE bc.category_id = :categoryId",
            nativeQuery = true)
    boolean existsBrandUsingCategoryIdInCategories(@Param("categoryId") Long categoryId);


}
