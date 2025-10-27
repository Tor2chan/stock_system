package th.team.stock.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import th.team.stock.commons.ApiConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import th.team.stock.commons.ApiConstant.LogType;
import th.team.stock.commons.CommonUtils;
import th.team.stock.dto.CategoryData;
import th.team.stock.models.Category;
import th.team.stock.services.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.team.stock.dto.CategoryData;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stock/category")

public class CategoryController implements ApiConstant{
    
    private final CategoryService categoryService;
    
    @PostMapping("find")
    public ResponseEntity<Map<String, Object>> findSysFtpExportDv(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CategoryData data) {
        try {

            Map<String, Object> result = categoryService.findSysFtpExportDvByCondition(data);

            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

 
}