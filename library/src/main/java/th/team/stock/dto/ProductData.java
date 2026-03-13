package th.team.stock.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.team.stock.models.commons.PageableCommon;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class ProductData extends PageableCommon{

   private Long id;
    
   private String name;
   private String sku;

   private String batchCode;
   private Long amount;
   private String category;
   private Double price;
   private Date receivedDate;
   private Date expireDate;
   private Date createDate;
   private Long sumAmount;
   private String categoryName;
   private String code;

   //จำนวนที่จะเบิกของ
   private Integer withdraw;
   private Integer newAmount;    
   private String    keyword;       // ค้นหาชื่อ / SKU
// filter หมวดหมู่
private LocalDate dateFrom;      // filter วันที่เริ่ม
private LocalDate dateTo;        // filter วันที่สิ้นสุด
private String    withdrawBy;    // ผู้เบิก (ส่งมาจาก frontend)
    
}
