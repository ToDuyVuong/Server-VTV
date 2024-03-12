package hcmute.kltn.vtv.repository.vendor;

import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Page<Product>> findAllByShopShopIdAndStatus(Long shopId, Status status,
                                                                           Pageable pageable);

    boolean existsByAndCategoryCategoryId(Long categoryId);

    boolean existsByBrandBrandId(Long brandId);

    boolean existsByNameAndStatus(String name, Status status);

    boolean existsByProductIdAndShopShopId(Long productId, Long shopId);


    Optional<List<Product>> findAllByShopShopIdAndStatus(Long shopId, Status status);

    Optional<List<Product>> findAllByCategoryCategoryIdAndShopShopIdAndStatus(Long categoryId, Long shopId,
                                                                                      Status status);

    Optional<List<Product>> findAllByNameContainingAndShopShopIdAndStatus(String name, Long shopId,
                                                                                  Status status);

    Optional<List<Product>> findAllByNameContainingAndStatus(String name, Status status);


    Optional<List<Product>> findByShopShopIdAndStatusOrderBySoldDescNameAsc(Long shopId, Status status);

    Optional<List<Product>> findByStatusOrderBySoldDescNameAsc(Status status);

    Optional<List<Product>> findByShopShopIdAndStatusOrderByCreateAtDesc(Long shopId, Status status);


    @Query("SELECT p FROM Product p JOIN p.productVariants v " +
            "WHERE p.shop.shopId = :shopId " +
            "AND p.status = :status " +
            "AND v.status = :status " +
            "AND v.price >= :minPrice " +
            "AND v.price <= :maxPrice")
    Optional<List<Product>> findByPriceRange(@Param("shopId") Long shopId,
                                             @Param("status") Status status,
                                             @Param("minPrice") Long minPrice,
                                             @Param("maxPrice") Long maxPrice);


    Optional<List<Product>> findByShopShopIdAndStatus(Long shopId, Status status);

    @Query("SELECT p FROM Product p JOIN p.productVariants v " +
            "WHERE p.status = :status " +
            "AND v.status = :status " +
            "AND v.price >= :minPrice " +
            "AND v.price <= :maxPrice")
    Optional<List<Product>> findByPriceRange(@Param("status") Status status,
                                             @Param("minPrice") Long minPrice,
                                             @Param("maxPrice") Long maxPrice);

    @Query("SELECT p FROM Product p " +
            "WHERE p.status = :status " +
            "ORDER BY p.createAt DESC")
    Optional<Page<Product>> findNewestProducts(@Param("status") Status status, Pageable pageable);



    Optional<Page<Product>> findAllByCategoryCategoryIdAndStatusOrderByCreateAt(Long categoryId, Status status,
                                                                                PageRequest pageable);


    Optional<Page<Product>> findAllByCategoryInAndStatusOrderByCreateAt(List<Category> categories, Status status,
                                                                        PageRequest pageRequest);


    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderByCreateAt(Long shopId, Status status,
                                                                                Pageable pageable);

    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderBySoldDescNameAsc(Long shopId, Status status,
                                                                                       Pageable pageable);

    Optional<Page<Product>> findAllByShopShopIdAndStatusOrderByCreateAtDesc(Long shopId, Status status,
                                                                                    Pageable pageable);


    Optional<Page<Product>> findAllByStatusOrderByName(Status status, Pageable pageable);


    /////////////////////////////////////////// Suggestion ///////////////////////////////////////////

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) FROM product p WHERE  p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> suggestByRandomly(
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) FROM product p WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> suggestBySearchHistoryAndRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE p.category_id = :categoryId " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "" +
                    "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "WHERE p.category_id = :categoryId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> suggestByCategoryAndRandomly(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN  category c " +
                    "ON p.category_id = c.category_id " +
                    "WHERE c.parent_id = :categoryParentId " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "" +
                    "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN  category c " +
                    "ON p.category_id = c.category_id " +
                    "WHERE c.parent_id = :categoryParentId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> suggestByCategoryParentAndRandomly(
            @Param("categoryParentId") Long categoryParentId,
            @Param("status") String status,
            Pageable pageable);


    /////////////////////////////////////////// Search On VTV ///////////////////////////////////////////


    @Query(value =
            "SELECT * " +
                    "FROM product p " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY p.create_at DESC",
            countQuery = "SELECT COUNT(*) FROM product p WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT * " +
                    "FROM product p " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(*) FROM product p WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByPriceAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY CASE WHEN :searchText IS NULL THEN pv.price END DESC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) FROM product p WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextByNameAndStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            Pageable pageable);


    /////////////////////////////////////////// Price Range On VTV ///////////////////////////////////////////


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.create_at ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortCreateAtAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortBestSelling(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortPriceAsc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price DESC",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortPriceDesc(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN product_variant pv ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT( DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextAndPriceRangeByStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    /////////////////////////////////////////// Price Range On Shop ///////////////////////////////////////////

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.create_at ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByPriceAse(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY pv.price DESC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice " +
                    "ORDER BY RAND()",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "AND pv.price >= :minPrice " +
                    "AND pv.price <= :maxPrice",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextPriceRangeOnShopAndSortByRandomly(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);


    /////////////////////////////////////////// OnShop ///////////////////////////////////////////
    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY p.create_at DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByNewest(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY p.sold DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByBestSelling(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);

    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY pv.price ASC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByPriceAsc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY pv.price DESC",
            countQuery = "SELECT COUNT(DISTINCT p.product_id) " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "JOIN product_variant pv " +
                    "ON p.product_id = pv.product_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status ",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortByPriceDesc(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);


    @Query(value =
            "SELECT DISTINCT p.* " +
                    "FROM product p " +
                    "JOIN category c " +
                    "ON p.category_id = c.category_id " +
                    "JOIN shop s " +
                    "ON c.shop_id = s.shop_id " +
                    "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                    "AND s.shop_id = :shopId " +
                    "AND p.status = :status " +
                    "ORDER BY RAND()",
            countQuery =
                    "SELECT COUNT(DISTINCT p.product_id) " +
                            "FROM product p " +
                            "JOIN category c " +
                            "ON p.category_id = c.category_id " +
                            "JOIN shop s " +
                            "ON c.shop_id = s.shop_id " +
                            "WHERE MATCH(p.name, p.description) AGAINST (:searchText IN BOOLEAN MODE) " +
                            "AND s.shop_id = :shopId " +
                            "AND p.status = :status",
            nativeQuery = true)
    Optional<Page<Product>> searchFullTextOnShopByNameAndStatusAndSortRandomly(
            @Param("searchText") String searchText,
            @Param("shopId") Long shopId,
            @Param("status") String status,
            Pageable pageable);

















}
