package com.freshdirect.cms.publish.service;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * 
 * Content Node generator service.
 * Provides node construction for the given service type.
 * 
 * @author segabor
 */
public interface ContentNodeGeneratorService {
    
    ContentNodeI createContentNode(ContentKey key);
}
