package th.team.stock.models.commons;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageableCommon implements Serializable{
    

    private static final long serialVersionUID = -1558377258384952556L;
	private Long rowNum;
    private Long totalRows;

	private Integer first;
    private Integer size;
    
    private String mode;
    
    @Schema(hidden = true)
    public Object[] getPageable() {
    	return new Object[] { getFirst(), getSize() };
    }
}
