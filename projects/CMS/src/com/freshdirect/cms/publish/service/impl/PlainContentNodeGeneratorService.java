package com.freshdirect.cms.publish.service.impl;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.publish.service.ContentNodeGeneratorService;

/**
 * Create content node the old way
 * 
 * @author segabor
 *
 */
public class PlainContentNodeGeneratorService implements ContentNodeGeneratorService {

    private final ContentServiceI contentService;
    
    public PlainContentNodeGeneratorService(ContentServiceI contentService) {
        this.contentService = contentService;
    }
    
    @Override
    public ContentNodeI createContentNode(ContentKey key) {
        return new ContentNode(contentService, DraftContext.MAIN, key);
    }

}
