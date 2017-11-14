package com.freshdirect.mobileapi.controller.data;

import java.util.List;

import com.freshdirect.storeapi.content.CMSSectionModel;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;


public class CMSPotatoSectionModel extends CMSSectionModel {
    /**
     * Create a new instance havign core attributes copied from a CMSSectionModel object
     * 
     * @param section source model
     * 
     * @return
     */
    public static CMSPotatoSectionModel withSection(final CMSSectionModel section) {
        CMSPotatoSectionModel model = new CMSPotatoSectionModel();

        model.setName(section.getName());
        model.setComponents(section.getComponents());
        model.setSchedules(section.getSchedules());
        model.setType(section.getType());
        model.setCaptionText(section.getCaptionText());
        model.setHeadlineText(section.getHeadlineText());
        model.setBodyText(section.getBodyText());
        model.setLinkText(section.getLinkText());
        model.setLinkURL(section.getLinkURL());
        model.setLinkType(section.getLinkType());
        model.setLinkTarget(section.getLinkTarget());
        model.setDisplayType(section.getDisplayType());
        model.setDrawer(section.getDrawer());
        model.setImageBanner(section.getImageBanner());
        model.setProductList(section.getProductList());
        model.setMustHaveProdList(section.getMustHaveProdList());
        model.setCategoryList(section.getCategoryList());
        
        return model;
    }


    private List<ProductPotatoData> products;


    public List<ProductPotatoData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPotatoData> products) {
        this.products = products;
    }
}
