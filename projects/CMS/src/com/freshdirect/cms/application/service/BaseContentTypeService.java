package com.freshdirect.cms.application.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

public abstract class BaseContentTypeService implements ContentTypeServiceI {

    public BaseContentTypeService() {
    }

    @Override
    public ContentKey generateUniqueContentKey(ContentType type) {
        // unsupported
        return null;
    }

    @Override
    public String generateUniqueId(ContentType type) {
        // unsupported
        return null;
    }

    @Override
    public Collection<BidirectionalReferenceHandler> getAllReferenceHandler() {
        return Collections.emptySet();
    }


    @Override
    public BidirectionalReferenceHandler getReferenceHandler(ContentType type, String attribute) {
        // unsupported
        return null;
    }

}
