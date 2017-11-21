package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class ProducerTypeModel extends ContentNodeModelImpl {

    public ProducerTypeModel(ContentKey key) {
        super(key);
    }

    @Override
    public String getFullName(){
        return (String) getCmsAttributeValue("FULL_NAME");
    }


    public Image getIconImage() {
        Image icon = FDAttributeFactory.constructImage(this, "icon");
        return icon;
    }

    public Image getIconShadowImage() {
        Image icon = FDAttributeFactory.constructImage(this, "icon_shadow");
        return icon;
    }


}
