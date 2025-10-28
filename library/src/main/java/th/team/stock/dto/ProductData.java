package th.team.stock.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.team.stock.models.commons.PageableCommon;

import java.util.Date;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor


public class ProductData extends PageableCommon{

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
    private Integer sumAmount;
    
}
