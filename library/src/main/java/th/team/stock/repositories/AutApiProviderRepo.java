package th.team.stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import th.team.stock.dto.ApiProviderData;
import th.team.stock.models.AutApiProvider;

import java.util.List;
import java.util.Optional;

/**
 * Repository for AutApiProvider
 * 
 * @author your-name
 */
public interface AutApiProviderRepo extends JpaRepository<AutApiProvider, Long> {
    
    /**
     * Find by API name
     */
    Optional<AutApiProvider> findByApiName(String apiName);
    
    /**
     * Find by API name (ignore case)
     */
    @Query("SELECT a FROM AutApiProvider a WHERE LOWER(a.apiName) = LOWER(:apiName)")
    Optional<AutApiProvider> findByApiNameIgnoreCase(@Param("apiName") String apiName);
    
    /**
     * Find all active providers
     */
    List<AutApiProvider> findByActiveFlagTrue();
    
    /**
     * Count by API name
     */
    @Query(nativeQuery = true, value = """
            SELECT COUNT(*)
            FROM aut_api_provider
            WHERE api_name = :apiName
            """)
    int countByApiName(@Param("apiName") String apiName);
    
    /**
     * Count by API name for edit (exclude current ID)
     */
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = """
            SELECT COUNT(*)
            FROM aut_api_provider
            WHERE api_name = :apiName
            AND provider_id != :providerId
            """)
    int countEditApiName(@Param("apiName") String apiName, @Param("providerId") Long providerId);
    
    /**
     * Find provider data by ID
     */
    @Query(nativeQuery = true, value = """
            SELECT 
                provider_id as providerId,
                api_name as apiName,
                endpoint,
                active_flag as activeFlag,
                remark
            FROM aut_api_provider
            WHERE provider_id = :providerId
            """)
    Optional<ApiProviderData> findProviderDataById(@Param("providerId") Long providerId);
    
    /**
     * Find by endpoint containing
     */
    @Query(nativeQuery = true, value = """
            SELECT *
            FROM aut_api_provider
            WHERE active_flag = true
            AND endpoint ILIKE %:endpoint%
            """)
    List<AutApiProvider> findByEndpointContaining(@Param("endpoint") String endpoint);
    
    /**
     * Find active providers with custom query
     */
    @Query("""
            SELECT a FROM AutApiProvider a 
            WHERE a.activeFlag = true 
            AND (
                LOWER(a.apiName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.endpoint) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY a.apiName ASC
            """)
    List<AutApiProvider> searchActiveProviders(@Param("keyword") String keyword);
}