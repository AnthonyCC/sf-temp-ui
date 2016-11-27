package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.CMSSectionModel;

public class CMSSectionProductModel extends CMSSectionModel {

    private List<Product> products = new ArrayList<Product>();

    public CMSSectionProductModel(CMSSectionModel section) {
        setName(section.getName());
        setComponents(section.getComponents());
        setSchedules(section.getSchedules());
        setType(section.getType());
        setCaptionText(section.getCaptionText());
        setHeadlineText(section.getHeadlineText());
        setBodyText(section.getBodyText());
        setLinkText(section.getLinkText());
        setLinkURL(section.getLinkURL());
        setLinkType(section.getLinkType());
        setLinkTarget(section.getLinkTarget());
        setDisplayType(section.getDisplayType());
        setDrawer(section.getDrawer());
        setImageBanner(section.getImageBanner());
        setProductList(section.getProductList());
        setCategoryList(section.getCategoryList());
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
