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
import th.team.stock.dto.ProductData;

import th.team.stock.models.Product;
import th.team.stock.services.EntityMapperService;
import th.team.stock.services.ProductService;
import th.team.stock.repositories.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stock-api/product")

public class ProductController implements ApiConstant{
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final EntityMapperService mapperService;
    
    @PostMapping("find-product-detail")
    public ResponseEntity<Map<String, Object>> findProductDetail(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductData data) {
        try {

            Map<String, Object> result = productService.findProductDetail(data);

            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @PostMapping("find-product")
    public ResponseEntity<Map<String, Object>> findProduct(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductData data) {
        try {

            Map<String, Object> result = productService.findProduct(data);

            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

 
    @PostMapping("save-product")
    public ResponseEntity<Map<String, Object>> saveProduct(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductData data) {
        try {

            Product product = mapperService.convertToEntity(data, Product.class);
            product.setSku(data.getSku());
            product.setName(data.getName());
            product.setBatchCode(data.getBatchCode());
            product.setAmount(data.getAmount());
            product.setPrice(data.getPrice());
            product.setReceivedDate(data.getReceivedDate());
            product.setExpireDate(data.getExpireDate());
            product.setCategory(data.getCategory());
            product.setCode(data.getCode());

            productRepo.save(product);
            ProductData result = mapperService.convertToEntity(product, ProductData.class);

            return new ResponseEntity<>(CommonUtils.response(result, "SUCCESS", null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError("Invalid input data for creating product"), HttpStatus.OK);
        }
    }
     @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Map<String, Object>> deleteproduct(HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable(name = "id", required = true) Long id) {
                
        try {

            productRepo.deleteById(id);

            return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
 
}
    

