package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);

    boolean existsByName(String name);

    boolean existsByBrandIdAndName(Long brandId, String name);

    Optional<List<Brand>> findAllByCategoriesEmptyAndStatus(Status status);


    Optional<List<Brand>> findAllByCategories_CategoryIdAndStatusOrCategoriesEmpty(Long categoryId, Status status);



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
