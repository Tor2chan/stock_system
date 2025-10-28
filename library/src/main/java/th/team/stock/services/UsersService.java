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

import th.team.stock.models.Users;
import th.team.stock.dto.UsersData;
import th.team.stock.repositories.UsersRepo;


@Service
@RequiredArgsConstructor
@Slf4j


public class UsersService implements ApiConstant{
    


    private final JdbcTemplate jdbcTemplate;
  
    
 public Map<String, Object> findUsers(UsersData criteria) {
		
		Map<String, Object> result = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();

        String orderBy = " order by u.id desc ";


          if (null != criteria.getId() && null != criteria.getId()) { 
               conditions.append(" and name = ? ");
                params.add(criteria.getId());
              
            }


        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() OVER ( ");
        sb.append(orderBy);
        sb.append(" ) AS row_num,*");
        sb.append(" from users u ");
        sb.append(WHERE);
        sb.append(conditions.toString());
        sb.append(orderBy);
        sb.append(LIMIT);

        List<UsersData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(UsersData.class),
                CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

        StringBuilder count = new StringBuilder();
        count.append("select count(*) from users u ");
        count.append(WHERE);
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }

}