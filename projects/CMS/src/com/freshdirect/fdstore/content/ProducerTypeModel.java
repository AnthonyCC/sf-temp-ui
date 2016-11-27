package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class ProducerTypeModel extends ContentNodeModelImpl {

    public ProducerTypeModel(ContentKey key) {
        super(key);
    }

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
