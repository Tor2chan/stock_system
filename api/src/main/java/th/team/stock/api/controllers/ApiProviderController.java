package th.team.stock.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import th.team.stock.dto.ApiProviderData;
import th.team.stock.models.AutApiProvider;
import th.team.stock.services.ApiProviderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
public class ApiProviderController {
    
    private final ApiProviderService apiProviderService;
    
    public ApiProviderController(ApiProviderService apiProviderService) {
        this.apiProviderService = apiProviderService;
    }
    
    /**
     * Create new API Provider
     */
    @PostMapping
    public ResponseEntity<?> createProvider(@RequestBody AutApiProvider apiProvider) {
        try {
            apiProviderService.saveApiProvider(apiProvider);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createSuccessResponse("API Provider created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to create API Provider: " + e.getMessage()));
        }
    }
    
    /**
     * Get provider by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProvider(@PathVariable Long id) {
        try {
            AutApiProvider provider = apiProviderService.findById(id);
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Get provider data by ID
     */
    @GetMapping("/{id}/data")
    public ResponseEntity<?> getProviderData(@PathVariable Long id) {
        try {
            ApiProviderData data = apiProviderService.findProviderDataById(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Get all providers
     */
    @GetMapping
    public ResponseEntity<List<AutApiProvider>> getAllProviders() {
        List<AutApiProvider> providers = apiProviderService.findAllApiProviders();
        return ResponseEntity.ok(providers);
    }
    
    /**
     * Get active providers
     */
    @GetMapping("/active")
    public ResponseEntity<List<AutApiProvider>> getActiveProviders() {
        List<AutApiProvider> providers = apiProviderService.findActiveProviders();
        return ResponseEntity.ok(providers);
    }
    
    /**
     * Search providers
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProviders(@RequestParam String keyword) {
        try {
            List<AutApiProvider> providers = apiProviderService.searchProviders(keyword);
            return ResponseEntity.ok(providers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Update provider
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvider(
            @PathVariable Long id,
            @RequestBody AutApiProvider apiProvider) {
        try {
            apiProvider.setProviderId(id);
            apiProviderService.updateApiProvider(apiProvider);
            return ResponseEntity.ok(createSuccessResponse("API Provider updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Toggle active status
     */
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<?> toggleActiveStatus(@PathVariable Long id) {
        try {
            apiProviderService.toggleActiveStatus(id);
            return ResponseEntity.ok(createSuccessResponse("Status toggled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Delete provider
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        try {
            apiProviderService.deleteApiProvider(id);
            return ResponseEntity.ok(createSuccessResponse("API Provider deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}