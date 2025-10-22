package th.team.stock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for aut_api_provider table
 * 
 * @author your-name
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutApiProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    
    private String apiName;
    private String endpoint;
    private Boolean activeFlag;
    private String remark;
}