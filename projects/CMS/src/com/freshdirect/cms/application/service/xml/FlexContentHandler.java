package com.freshdirect.cms.application.service.xml;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * Collect the content nodes which created during parsing the XML into a map.
 * 
 */
public class FlexContentHandler extends ContentNodeBuilder {

    final Map<ContentKey, ContentNodeI> nodes = new HashMap<ContentKey, ContentNodeI>();

    public FlexContentHandler() {
    }

    /**
     * @return Map of ContentKey -> ContentNodeI
     */
    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes() {
        return nodes;
    }

    /**
     * @param node
     */
    @Override
    protected void nodeCreated(ContentNodeI node) {
        nodes.put(node.getKey(), node);
    }
}