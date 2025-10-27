package th.team.stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import th.team.stock.dto.CategoryData;
import th.team.stock.models.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryRepo  extends JpaRepository<Category, Long> {
    
}
