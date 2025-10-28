package th.team.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.team.stock.models.commons.PageableCommon;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersData extends PageableCommon{
    
    private Long id;

    private String username;
    private String name;
    private String password;
    private String role;
}