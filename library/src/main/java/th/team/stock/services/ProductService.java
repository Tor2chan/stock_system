package th.team.stock.services;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import th.team.stock.commons.ApiConstant;
import th.team.stock.commons.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import th.team.stock.dto.ProductData;
import th.team.stock.repositories.ProductRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ApiConstant {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepo ProductRepo;

    public Map<String, Object> findProduct(ProductData criteria) {

        Map<String, Object> result = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();
        List<Object> countParams = new ArrayList<>();

        String orderBy = " order by p.sku desc ";

        // ⭐ ค้นหาจาก name field เดียว แต่ OR ทั้ง name และ sku
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            conditions.append(" and ( LOWER(p.name) LIKE LOWER(?) OR LOWER(p.sku) LIKE LOWER(?) ) ");
            String likeParam = CommonUtils.concatLikeParam(criteria.getName(), true, true);
            params.add(likeParam);
            params.add(likeParam);
            countParams.add(likeParam);
            countParams.add(likeParam);
        }

        if (criteria.getCode() != null && !criteria.getCode().isEmpty()) {
            conditions.append(" and p.code = ? ");
            params.add(criteria.getCode());
            countParams.add(criteria.getCode());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() OVER ( ");
        sb.append(orderBy);
        sb.append(" ) AS row_num, p.name, p.sku, SUM(p.amount) AS sum_amount, p.code, c.name AS category_name ");
        sb.append(" from product p JOIN category c ON c.code = p.code ");
        sb.append(WHERE);
        sb.append(conditions.toString());
        sb.append("group by p.sku, p.name, p.code, c.name");
        sb.append(LIMIT);

        List<ProductData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(ProductData.class),
                CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

        StringBuilder count = new StringBuilder();
        count.append("select count(DISTINCT sku) from product p ");
        count.append(WHERE);
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, countParams.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }

    public Map<String, Object> findProductDetail(ProductData criteria) {

        Map<String, Object> result = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();

        String orderBy = " order by p.id desc ";

        if (null != criteria.getSku()) {
            conditions.append(" and p.sku = ? ");
            params.add(criteria.getSku());
        }

        if (null != criteria.getId()) {
            conditions.append(" and p.id = ? ");
            params.add(criteria.getId());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() OVER ( ");
        sb.append(orderBy);
        sb.append(" ) AS row_num, p.*, c.name AS category_name ");
        sb.append(" from product p JOIN category c ON c.code = p.code");
        sb.append(WHERE);
        sb.append(conditions.toString());
        sb.append(orderBy);
        sb.append(LIMIT);

        List<ProductData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(ProductData.class),
                CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

        StringBuilder count = new StringBuilder();
        count.append("select count(*) from product p ");
        count.append(WHERE);
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }

    public Map<String, Object> withdrawProduct(ProductData criteria) {

        Map<String, Object> result = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();

        String orderBy = " order by p.id desc ";

        if (null != criteria.getSku()) {
            conditions.append(" and p.sku = ? ");
            params.add(criteria.getSku());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() OVER ( ");
        sb.append(orderBy);
        sb.append(" ) AS row_num, p.*, c.name AS category_name ");
        sb.append(" from product p JOIN category c ON c.code = p.code");
        sb.append(WHERE);
        sb.append(conditions.toString());
        sb.append(orderBy);
        sb.append(LIMIT);

        List<ProductData> entries = jdbcTemplate.query(sb.toString(),
                BeanPropertyRowMapper.newInstance(ProductData.class),
                CommonUtils.joinParam(params.toArray(), criteria.getPageable()));

        StringBuilder count = new StringBuilder();
        count.append("select count(*) from product p ");
        count.append(WHERE);
        count.append(conditions.toString());
        Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

        result.put(ENTRIES, entries);
        result.put(TOTAL_RECORDS, totalRecords);

        return result;
    }
}