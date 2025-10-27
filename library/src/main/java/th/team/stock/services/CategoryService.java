package th.team.stock.services;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import th.team.stock.commons.ApiConstant;
import th.team.stock.commons.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import th.team.stock.dto.CategoryData;
import th.team.stock.models.Category;
import th.team.stock.repositories.CategoryRepo;

/**
 * @author sutthipongk
 */
@Service
@RequiredArgsConstructor
@Slf4j

public class CategoryService implements ApiConstant{

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepo CategoryRepo;

    
 
    

    public Map<String, Object> findCategory(CategoryData criteria) {
		
		Map<String, Object> result = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();

        String orderBy = " order by c.id desc ";


          if (null != criteria.getId() && null != criteria.getId()) { 
            conditions.append(" and id = ? ");
            params.add(criteria.getId());
              log.info("5555!!");
            }

        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() OVER ( ");
        sb.append(orderBy);
        sb.append(" ) AS row_num, c.* ");
        sb.append(" from category c  ");
        sb.append(WHERE);
        sb.append(conditions.toString());
        sb.append(orderBy);
        sb.append(LIMIT);

        List<CategoryData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(CategoryData.class),
                CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

        StringBuilder count = new StringBuilder();
        count.append("select count(*) from category c ");
        count.append(WHERE);
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }


}