package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.util.Reference;

public class ContentNodeModelReference<X extends ContentNodeModel> extends Reference<X, ContentKey>{

    public ContentNodeModelReference(X model) {
        super(model);
    }
    

    protected ContentKey getKey(X model) {
        return model.getContentKey();
    }
    
    @Override
    protected X lookup(ContentKey key) {
        return (X) ContentFactory.getInstance().getContentNodeByKey(key);
    }
    
}
