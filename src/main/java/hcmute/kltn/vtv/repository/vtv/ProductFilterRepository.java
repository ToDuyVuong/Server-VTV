package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductFilterRepository extends JpaRepository<Product, Long> {


    /////////////////////////////////////////// Filter And Price Range On VTV ///////////////////////////////////////////

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.create_at ASC",
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN product_variant pv " +
                            "ON p.product_id = pv.product_id " +
                            "WHERE p.status = :status " +
                            "AND pv.price >= :minPrice " +
                            "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> filterAndPriceRangeByStatusCreateAtAsc(
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE  p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.sold DESC",
            countQuery =
                    "SELECT COUNT( DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN product_variant pv " +
                            "ON p.product_id = pv.product_id " +
                            "WHERE p.status = :status " +
                            "AND pv.price >= :minPrice " +
                            "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> filterAndPriceRangeByStatusBestSelling(
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> filterAndPriceRangeByStatusPriceAsc(
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price DESC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> filterAndPriceRangeByStatusPriceDesc(
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> filterAndPriceRangeByStatusRandomly(
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    /////////////////////////////////////////// Filter On VTV ///////////////////////////////////////////

    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "WHERE p.status = :status " +
                    "ORDER BY p.create_at ASC",
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "WHERE p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> filterByStatusCreateAtAsc(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "WHERE  p.status = :status " +
                    "ORDER BY p.sold DESC",
            countQuery =
                    "SELECT COUNT( DISTINCT p.product_id) " +
                            "FROM product p " +
                            "WHERE p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> filterByStatusBestSelling(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT(  p.product_id) " +
                    "FROM product p " +
                    "WHERE p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> filterByStatusPriceAsc(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT  p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE p.status = :status " +
                    "ORDER BY pv.price DESC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "WHERE p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> filterByStatusPriceDesc(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT  p.* " +
                    "FROM product p " +
                    "WHERE p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(  p.product_id) " +
                    "FROM product p " +
                    "WHERE p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> filterByStatusRandomly(
            @Param("status") String status,
            Pageable pageable);

}
