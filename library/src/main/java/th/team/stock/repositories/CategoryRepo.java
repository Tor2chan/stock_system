package th.team.stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import th.team.stock.dto.CategoryData;
import th.team.stock.models.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryRepo  extends JpaRepository<Category, Long> {
 
    @Modifying
    @Query(nativeQuery = true, value = """
        delete from category
        where id = :id
    """)
    public void deleteById(@Param("id") Integer id);
}
