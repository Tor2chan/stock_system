package th.team.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiProviderData {
    private Long rowNum;
    private Long providerId;
    private String apiName;
    private String endpoint;
    private Boolean activeFlag;
    private String remark;
}