package th.team.stock.models.commons;

import java.util.Date;

public interface FootprintInterfaces {

	public Date getCreateDate();
	
	public void setCreateDate(Date createDate);
	
	public Date getUpdateDate();
	
	public void setUpdateDate(Date updateDate);
	
	public Boolean getActive();
	
	public void setActive(Boolean active);
	
}