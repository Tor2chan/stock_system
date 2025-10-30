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
import th.team.stock.repositories.CategoryRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import th.team.stock.services.EntityMapperService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stock-api/category")

public class CategoryController implements ApiConstant{
    
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;
    private final EntityMapperService mapperService;
    
    @PostMapping("find-category")
    public ResponseEntity<Map<String, Object>> findCategory(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CategoryData data) {
        try {

            Map<String, Object> result = categoryService.findCategory(data);

            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @PostMapping("save-category")
    public ResponseEntity<Map<String, Object>> saveCategory(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CategoryData data) {
        try {

            Category category = mapperService.convertToEntity(data, Category.class);
            category.setCode(data.getCode());
            category.setName(data.getName());
            category.setActive(data.getActive());

            categoryRepo.save(category);
            CategoryData result = mapperService.convertToEntity(category, CategoryData.class);

            return new ResponseEntity<>(CommonUtils.response(result, "SUCCESS", null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError("Invalid input data for creating category"), HttpStatus.OK);
        }
    }      
                     
    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable(name = "id", required = true) Long id) {
            
            
                  
        try {

            categoryRepo.deleteById(id);

            return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

}