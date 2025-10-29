package th.team.stock.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String sku;

    private String batchCode;
    private Integer amount;
    private String category;
    private Integer price;
    private Date receivedDate;
    private Date expireDate;
    private Date createDate;
private String code;

}
