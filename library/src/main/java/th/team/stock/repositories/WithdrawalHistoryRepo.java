package th.team.stock.repositories;

import th.team.stock.models.WithdrawalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface WithdrawalHistoryRepo extends JpaRepository<WithdrawalHistory, Long> {

@Query(value = """
    SELECT * FROM withdrawal_history
    WHERE (CAST(:keyword AS text) IS NULL
           OR LOWER(product_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(sku) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (CAST(:category AS text) IS NULL OR category_name = :category)
      AND (CAST(:dateFrom AS timestamp) IS NULL OR withdraw_date >= CAST(:dateFrom AS timestamp))
      AND (CAST(:dateTo   AS timestamp) IS NULL OR withdraw_date <= CAST(:dateTo   AS timestamp))
    ORDER BY withdraw_date DESC
""", nativeQuery = true,
     countQuery = """
    SELECT COUNT(*) FROM withdrawal_history
    WHERE (CAST(:keyword AS text) IS NULL
           OR LOWER(product_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(sku) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (CAST(:category AS text) IS NULL OR category_name = :category)
      AND (CAST(:dateFrom AS timestamp) IS NULL OR withdraw_date >= CAST(:dateFrom AS timestamp))
      AND (CAST(:dateTo   AS timestamp) IS NULL OR withdraw_date <= CAST(:dateTo   AS timestamp))
""")
Page<WithdrawalHistory> findByFilter(
    @Param("keyword")  String keyword,
    @Param("category") String category,
    @Param("dateFrom") LocalDateTime dateFrom,
    @Param("dateTo")   LocalDateTime dateTo,
    Pageable pageable
);
}