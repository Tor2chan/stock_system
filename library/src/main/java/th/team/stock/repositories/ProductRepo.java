package th.team.stock.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import th.team.stock.dto.ProductData;
import th.team.stock.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepo  extends JpaRepository<Product, Long> {
    
    // @Query(nativeQuery = true, value = """
    //     SELECT 
    //         p.*,
    //         (p.amount - :withdraw) AS new_amount
    //     FROM product p
    //     WHERE p.id = :id
    // """)
    // Product findProductWithdraw(@Param("id") Long id, @Param("withdraw") Integer withdraw);

}
