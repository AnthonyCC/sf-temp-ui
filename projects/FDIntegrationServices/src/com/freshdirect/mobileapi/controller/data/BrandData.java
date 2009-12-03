package com.freshdirect.mobileapi.controller.data;

import com.freshdirect.mobileapi.model.Brand;

/**
 * Represent a product brand information.
 * @author fgarcia
 *
 */
public class BrandData {
    /**
     * Brand logo
     */
    private Image logo;

    /** 
     * Brand name
     */
    private String name;

    public BrandData() {
    	
    }
    public BrandData(Brand brand) {
        logo = new Image();
        this.logo.setHeight(brand.getHeight());
        this.logo.setWidth(brand.getWidth());
        this.logo.setSource(brand.getLogo());
        setName(brand.getName());

    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
