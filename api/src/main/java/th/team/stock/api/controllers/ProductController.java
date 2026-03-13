package th.team.stock.api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.team.stock.commons.ApiConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import th.team.stock.commons.CommonUtils;
import th.team.stock.dto.ProductData;
import th.team.stock.models.Product;
import th.team.stock.models.WithdrawalHistory;
import th.team.stock.services.EntityMapperService;
import th.team.stock.services.ProductService;
import th.team.stock.repositories.ProductRepo;
import th.team.stock.repositories.WithdrawalHistoryRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stock-api/product")
public class ProductController implements ApiConstant {

    private final ProductService        productService;
    private final ProductRepo           productRepo;
    private final EntityMapperService   mapperService;
    private final WithdrawalHistoryRepo withdrawalHistoryRepo; // ⭐ เพิ่ม

    // ── find-product-detail ────────────────────────────
    @PostMapping("find-product-detail")
    public ResponseEntity<Map<String, Object>> findProductDetail(
            HttpServletRequest request, HttpServletResponse response,
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

    // ── find-product ───────────────────────────────────
    @PostMapping("find-product")
    public ResponseEntity<Map<String, Object>> findProduct(
            HttpServletRequest request, HttpServletResponse response,
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

    // ── save-product ───────────────────────────────────
    @PostMapping("save-product")
    public ResponseEntity<Map<String, Object>> saveProduct(
            HttpServletRequest request, HttpServletResponse response,
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

    // ── delete-product ─────────────────────────────────
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Map<String, Object>> deleteproduct(
            HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "id", required = true) Long id) {
        try {
            productRepo.deleteById(id);
            return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    // ── withdraw-product ───────────────────────────────
    @PutMapping("withdraw-product/{id}")
    public ResponseEntity<Map<String, Object>> withdrawProduct(
            HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "id", required = true) Long id,
            @RequestBody ProductData data) {
        try {
            Map<String, Object> addOn = new HashMap<>();
            Product product = productRepo.findById(id).orElse(null);

            if (product == null) {
                return new ResponseEntity<>(CommonUtils.responseError("Product not found"), HttpStatus.NOT_FOUND);
            }

           
Long newAmount = product.getAmount() - data.getWithdraw().longValue();

            if (newAmount < 0) {
                return new ResponseEntity<>(CommonUtils.responseError("Amount cannot be negative"), HttpStatus.BAD_REQUEST);
            }

            // ⭐ บันทึกประวัติการเบิกก่อนเสมอ
            // category ใน table product เป็น varchar ดึงมาตรงๆ ได้เลย
           WithdrawalHistory history = new WithdrawalHistory();
history.setProductId(product.getId());
history.setProductName(product.getName());
history.setSku(product.getSku());

history.setCategoryName(
    product.getCategory() != null ? product.getCategory() : "Unknown"
);

history.setWithdrawAmount(
    data.getWithdraw() != null ? data.getWithdraw().longValue() : 0L
);

history.setWithdrawPrice(
    product.getPrice() != null
        ? BigDecimal.valueOf(product.getPrice())
        : BigDecimal.ZERO
);

history.setWithdrawDate(LocalDateTime.now());

history.setWithdrawBy(
    data.getWithdrawBy() != null ? data.getWithdrawBy() : "user"
);

withdrawalHistoryRepo.save(history);

            // ลบสินค้าถ้าสต็อกหมด
            if (newAmount == 0) {
                productRepo.deleteById(id);
                return new ResponseEntity<>(CommonUtils.response(null, SUCCESS, addOn), HttpStatus.OK);
            }

            product.setAmount(newAmount);
            productRepo.save(product);

            ProductData result = mapperService.convertToEntity(product, ProductData.class);
            return new ResponseEntity<>(CommonUtils.response(result, SUCCESS, null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ── find-withdrawal-history ────────────────────────
    @PostMapping("find-withdrawal-history")
    public ResponseEntity<Map<String, Object>> findWithdrawalHistory(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductData data) {
        try {
            int page = data.getFirst() != null
                ? data.getFirst() / (data.getSize() != null ? data.getSize() : 15) : 0;
            int size = data.getSize() != null ? data.getSize() : 15;

            LocalDateTime dateFrom = data.getDateFrom() != null
                ? data.getDateFrom().atStartOfDay() : null;
            LocalDateTime dateTo = data.getDateTo() != null
                ? data.getDateTo().atTime(23, 59, 59) : null;

    

Page<WithdrawalHistory> pageResult = withdrawalHistoryRepo.findByFilter(
   data.getKeyword(),
data.getCategory(),
    dateFrom,
    dateTo,
    PageRequest.of(page, size)
);
            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, pageResult.getTotalElements());
            return new ResponseEntity<>(
                CommonUtils.response(pageResult.getContent(), SUCCESS, addOn),
                HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    // ── helper ─────────────────────────────────────────
    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}