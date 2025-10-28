package th.team.stock.services;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import th.team.stock.models.commons.FootprintInterfaces;

@Service
@RequiredArgsConstructor
public class EntityMapperService {
    
    
    private final ModelMapper modelMapper;
    public <D, T> D convertToData(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <D, T> D convertToEntity(T data, Class<D> entityClass) {
        return modelMapper.map(data, entityClass);
    }
    
    @SuppressWarnings("unchecked")
	public <D, T> D convertDataToEntity(T dto, Class<D> entityClass) {
    	Object o = modelMapper.map(dto, entityClass);
    	FootprintInterfaces fp = (FootprintInterfaces) o;
    	fp.setCreateDate(new Date());
    	return (D) o;
    }
}
