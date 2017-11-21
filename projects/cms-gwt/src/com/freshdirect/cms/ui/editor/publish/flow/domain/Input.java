package com.freshdirect.cms.ui.editor.publish.flow.domain;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.util.List;
import java.util.Map;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.media.domain.Media;


/**
 * A simple POJO capturing all required data.
 *
 * @author segabor
 */
public class Input {

    /**
     * Collection of CMS nodes
     */
    private Map<ContentKey, Map<Attribute, Object>> contentNodes = null;

    /**
     * Collection of media nodes
     */
    private List<Media> mediaNodes = null;

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
    private Map<String,Map<String,Map<String, String>>> materialConfiguration = null;

    /**
     * Collection of Material ID -> Sales Unit assignments
     */
    private Map<String, Map<String, String>> materialSalesUnits = null;

    public Map<ContentKey, Map<Attribute, Object>> getContentNodes() {
        return contentNodes != null ? unmodifiableMap(contentNodes) : null;
    }


    public void setContentNodes(Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        this.contentNodes = contentNodes;
    }


    public List<Media> getMediaNodes() {
        return mediaNodes != null ? unmodifiableList(mediaNodes) : null;
    }


    public void setMediaNodes(List<Media> mediaNodes) {
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


    public Map<String,Map<String,Map<String, String>>> getMaterialConfiguration() {
        return materialConfiguration != null ? unmodifiableMap(materialConfiguration) : null;
    }


    public void setMaterialConfiguration(Map<String,Map<String,Map<String, String>>> materialConfiguration) {
        this.materialConfiguration = materialConfiguration;
    }


    public Map<String, Map<String, String>> getMaterialSalesUnits() {
        return materialSalesUnits != null ? unmodifiableMap(materialSalesUnits) : null;
    }


    public void setMaterialSalesUnits(Map<String, Map<String, String>> materialSalesUnits) {
        this.materialSalesUnits = materialSalesUnits;
    }
}
