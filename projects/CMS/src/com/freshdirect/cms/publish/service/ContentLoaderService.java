package com.freshdirect.cms.publish.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.repository.ERPSDataRepository;

/**
 * Service supporting loading content nodes at once
 * 
 * @author segabor
 *
 */
public interface ContentLoaderService {

    /**
     * Fetches all CMS content nodes from the CMS database
     * 
     * @return
     */
    Collection<ContentNodeI> fetchAllContentNodes();

    /**
     * Fetches all Media nodes from CMS.MEDIA table
     * 
     * @return
     */
    Collection<ContentNodeI> fetchAllMediaNodes();

    /**
     * Fetches the map of SKU to ERPS material mapping from ERPS DB
     * 
     * @return map of SKU CODE->Material ID pairs
     */
    Map<String, String> fetchSkuMaterialAssociations();

    /**
     * Assemble the mapping of the possible material configuration options
     * 
     * @return configuration options for each material ID
     *
     * @see {@link ERPSDataRepository#fetchMaterialConfigurationMap}
     */
    Map<String,Map<String,Set<String>>> fetchMaterialConfigurationMap();
    
    /**
     * 
     * @return
     */
    Map<String, Set<String>> fetchMaterialSalesUnits();
    
    /**
     * Fetch URIs of media items updated since last successful publish
     */
    List<String> fetchMediaURIs();
}
