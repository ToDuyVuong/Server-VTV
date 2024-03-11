package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findAllByChildAndStatus(boolean child, Status status);

    List<Category> findAllByParent(Category parent);


    Optional<Category> findByParentCategoryIdAndStatus(Long parentCategoryId, Status status);

    boolean existsByParentCategoryIdAndStatus(Long parentCategoryId, Status status);

    boolean existsByNameAndStatus(String name, Status status);

    boolean existsByCategoryIdAndChild(Long categoryId, boolean child);

    boolean existsByCategoryIdAndName(Long categoryId, String name);

    Optional<List<Category>> findAllByCategoryIdInAndStatus(List<Long> categoryIds, Status status);

    Optional<List<Category>> findAllByParentCategoryIdAndStatus(Long categoryId, Status status);

}
