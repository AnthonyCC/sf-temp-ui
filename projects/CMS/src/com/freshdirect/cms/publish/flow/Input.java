package com.freshdirect.cms.publish.flow;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;


/**
 * A simple POJO capturing all required data.
 * 
 * @author segabor
 */
public class Input {
    
    /**
     * Collection of CMS nodes
     */
    private Collection<ContentNodeI> contentNodes = null;
    
    /**
     * Collection of media nodes
     */
    private Collection<ContentNodeI> mediaNodes = null;
    
    /**
     * List of URIs
     */
    private List<String> mediaUris;
    
    /**
     * Collection SKUCODE -> Material ID assignments
     */
    private Map<String, String> skuMaterialAssignment = null;
    
    /**
     * Collection of Material ID -> Characteristic options
     */
    private Map<String,Map<String,Set<String>>> materialConfiguration = null;

    /**
     * Collection of Material ID -> Sales Unit assignments
     */
    private Map<String, Set<String>> materialSalesUnits = null;
    
    public Collection<ContentNodeI> getContentNodes() {
        return contentNodes != null ? unmodifiableCollection(contentNodes) : null;
    }

    
    public void setContentNodes(Collection<ContentNodeI> contentNodes) {
        this.contentNodes = contentNodes;
    }

    
    public Collection<ContentNodeI> getMediaNodes() {
        return mediaNodes != null ? unmodifiableCollection(mediaNodes) : null;
    }

    
    public void setMediaNodes(Collection<ContentNodeI> mediaNodes) {
        this.mediaNodes = mediaNodes;
    }

    
    public List<String> getMediaUris() {
        return mediaUris != null ? unmodifiableList(mediaUris) : null;
    }

    
    public void setMediaUris(List<String> mediaUris) {
        this.mediaUris = mediaUris;
    }

    
    public Map<String, String> getSkuMaterialAssignment() {
        return skuMaterialAssignment != null ? unmodifiableMap(skuMaterialAssignment) : null;
    }

    
    public void setSkuMaterialAssignment(Map<String, String> skuMaterialAssignment) {
        this.skuMaterialAssignment = skuMaterialAssignment;
    }

    
    public Map<String,Map<String,Set<String>>> getMaterialConfiguration() {
        return materialConfiguration != null ? unmodifiableMap(materialConfiguration) : null;
    }

    
    public void setMaterialConfiguration(Map<String,Map<String,Set<String>>> materialConfiguration) {
        this.materialConfiguration = materialConfiguration;
    }

    
    public Map<String, Set<String>> getMaterialSalesUnits() {
        return materialSalesUnits != null ? unmodifiableMap(materialSalesUnits) : null;
    }

    
    public void setMaterialSalesUnits(Map<String, Set<String>> materialSalesUnits) {
        this.materialSalesUnits = materialSalesUnits;
    }
}
