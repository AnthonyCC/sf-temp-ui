package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.framework.util.Reference;

public class ContentNodeModelReference<X extends ContentNodeModel> extends Reference<X, ContentKey>{

    public ContentNodeModelReference(X model) {
        super(model);
    }


    @Override
    protected ContentKey getKey(X model) {
        return model.getContentKey();
    }

    @Override
    protected X lookup(ContentKey key) {
        return (X) ContentFactory.getInstance().getContentNodeByKey(key);
    }

}
