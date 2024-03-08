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









    Optional<List<Category>> findAllByParentCategoryIdAndStatus(Long categoryId, Status status);

}
