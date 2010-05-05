package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class ProducerModel extends ContentNodeModelImpl {

    public ProducerModel(ContentKey key) {
        super(key);
    }
    
    @Override
    public String getFullName(){
        return (String) getCmsAttributeValue("FULL_NAME");
    }
    
    public String getAddress() {
        return (String) getCmsAttributeValue("ADDRESS");
    }
    
    public String getLocation() {
        return (String) getCmsAttributeValue("GMAPS_LOCATION");
    }

    public Image getIconImage() {
        Image icon = FDAttributeFactory.constructImage(this, "icon");
        if (icon == null) {
            icon = getProducerType().getIconImage();
        }
        return icon;
    }
    
    public ProducerTypeModel getProducerType() {
        Object value = getCmsAttributeValue("producer_type");
        return value instanceof ContentKey ? (ProducerTypeModel) ContentFactory.getInstance().getContentNodeByKey((ContentKey) value) : null;
    }
    
    public Image getIconShadowImage() {
        Image icon = FDAttributeFactory.constructImage(this, "icon_shadow");
        if (icon == null) {
            icon = getProducerType().getIconShadowImage();
        }
        return icon;
    }
    
    public Html getBubbleContent() {
        return FDAttributeFactory.constructHtml(this, "bubble_content");
    }

    public CategoryModel getBrandCategory() {
        Object value = getCmsAttributeValue("brand_category");
        return value instanceof ContentKey ? (CategoryModel) ContentFactory.getInstance().getContentNodeByKey((ContentKey) value) : null;
    }

    public BrandModel getBrand() {
        Object value = getCmsAttributeValue("brand");
        return value instanceof ContentKey ? (BrandModel) ContentFactory.getInstance().getContentNodeByKey((ContentKey) value) : null;
    }
    
    public boolean isActive() {
        CategoryModel brandCategory = getBrandCategory();
        return brandCategory != null && brandCategory.isActive();
    }
    
}
