package th.team.stock.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import th.team.stock.dto.ApiProviderData;
import th.team.stock.models.AutApiProvider;
import th.team.stock.repositories.AutApiProviderRepo;

import java.util.List;

/**
 * Service for API Provider business logic
 * 
 * @author your-name
 */
@Service
@Transactional
public class ApiProviderService {
    
    private final AutApiProviderRepo autApiProviderRepo;
    
    public ApiProviderService(AutApiProviderRepo autApiProviderRepo) {
        this.autApiProviderRepo = autApiProviderRepo;
    }
    
    /**
     * Save API Provider
     */
    public void saveApiProvider(AutApiProvider apiProvider) throws Exception {
        Assert.notNull(apiProvider, "ApiProvider data is null");
        Assert.hasText(apiProvider.getApiName(), "API name is required");
        Assert.hasText(apiProvider.getEndpoint(), "Endpoint is required");
        
        // ตรวจสอบว่า API name ซ้ำหรือไม่
        int count = autApiProviderRepo.countByApiName(apiProvider.getApiName());
        if (count > 0) {
            throw new Exception("API name already exists: " + apiProvider.getApiName());
        }
        
        // Validate endpoint
        if (!isValidEndpoint(apiProvider.getEndpoint())) {
            throw new IllegalArgumentException("Invalid endpoint format");
        }
        
        // Set default values
        if (apiProvider.getActiveFlag() == null) {
            apiProvider.setActiveFlag(true);
        }
        
        autApiProviderRepo.save(apiProvider);
    }
    
    /**
     * Update API Provider
     */
    public void updateApiProvider(AutApiProvider apiProvider) throws Exception {
        Assert.notNull(apiProvider, "ApiProvider data is null");
        Assert.notNull(apiProvider.getProviderId(), "Provider ID is required");
        Assert.hasText(apiProvider.getApiName(), "API name is required");
        Assert.hasText(apiProvider.getEndpoint(), "Endpoint is required");
        
        // ตรวจสอบว่ามีข้อมูลอยู่จริง
        if (!autApiProviderRepo.existsById(apiProvider.getProviderId())) {
            throw new Exception("API Provider not found with ID: " + apiProvider.getProviderId());
        }
        
        // ตรวจสอบว่า API name ซ้ำหรือไม่ (ยกเว้น ID ปัจจุบัน)
        int count = autApiProviderRepo.countEditApiName(
            apiProvider.getApiName(), 
            apiProvider.getProviderId()
        );
        if (count > 0) {
            throw new Exception("API name already exists: " + apiProvider.getApiName());
        }
        
        // Validate endpoint
        if (!isValidEndpoint(apiProvider.getEndpoint())) {
            throw new IllegalArgumentException("Invalid endpoint format");
        }
        
        autApiProviderRepo.save(apiProvider);
    }
    
    /**
     * Find by ID
     */
    public AutApiProvider findById(Long providerId) throws Exception {
        Assert.notNull(providerId, "Provider ID is null");
        
        return autApiProviderRepo.findById(providerId)
            .orElseThrow(() -> new Exception("API Provider not found with ID: " + providerId));
    }
    
    /**
     * Find provider data by ID
     */
    public ApiProviderData findProviderDataById(Long providerId) throws Exception {
        Assert.notNull(providerId, "Provider ID is null");
        
        return autApiProviderRepo.findProviderDataById(providerId)
            .orElseThrow(() -> new Exception("API Provider not found with ID: " + providerId));
    }
    
    /**
     * Find all providers
     */
    public List<AutApiProvider> findAllApiProviders() {
        return autApiProviderRepo.findAll();
    }
    
    /**
     * Find all active providers
     */
    public List<AutApiProvider> findActiveProviders() {
        return autApiProviderRepo.findByActiveFlagTrue();
    }
    
    /**
     * Search providers
     */
    public List<AutApiProvider> searchProviders(String keyword) {
        Assert.hasText(keyword, "Search keyword is required");
        return autApiProviderRepo.searchActiveProviders(keyword);
    }
    
    /**
     * Delete API Provider
     */
    public void deleteApiProvider(Long providerId) throws Exception {
        Assert.notNull(providerId, "Provider ID is null");
        
        if (!autApiProviderRepo.existsById(providerId)) {
            throw new Exception("API Provider not found with ID: " + providerId);
        }
        
        autApiProviderRepo.deleteById(providerId);
    }
    
    /**
     * Toggle active status
     */
    public void toggleActiveStatus(Long providerId) throws Exception {
        AutApiProvider provider = findById(providerId);
        provider.setActiveFlag(!provider.getActiveFlag());
        autApiProviderRepo.save(provider);
    }
    
    /**
     * Validate endpoint format
     */
    private boolean isValidEndpoint(String endpoint) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return false;
        }
        return endpoint.startsWith("http://") || endpoint.startsWith("https://");
    }
}