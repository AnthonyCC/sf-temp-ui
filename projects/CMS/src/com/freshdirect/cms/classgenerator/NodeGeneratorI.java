package com.freshdirect.cms.classgenerator;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

public interface NodeGeneratorI {

    ContentTypeDefI getDefinition(String name);
    
    BidirectionalReferenceHandler getReferenceHandler(ContentType type, String attributeName);
}
