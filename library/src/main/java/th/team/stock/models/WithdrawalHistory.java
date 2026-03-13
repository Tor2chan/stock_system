package th.team.stock.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "withdrawal_history")
public class WithdrawalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "withdraw_amount", nullable = false)
    private Long withdrawAmount = 0L;

    @Column(name = "withdraw_price", precision = 15, scale = 2)
    private BigDecimal withdrawPrice;

    @Column(name = "withdraw_date", nullable = false)
    private LocalDateTime withdrawDate;

    @Column(name = "withdraw_by")
    private String withdrawBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (withdrawDate == null) withdrawDate = LocalDateTime.now();
    }
}