package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.product.data.ProductData;

public class ModuleData implements Serializable {

    private static final long serialVersionUID = -2548262633417228403L;

    private String openHTMLEditorial;
    private List<ProductData> products;
    private List<IconData> icons;
    private List<IconData> imageGridData;
    private List<SectionData> sectionData;

    public String getOpenHTMLEditorial() {
        return openHTMLEditorial;
    }

    public void setOpenHTMLEditorial(String openHTMLEditorial) {
        this.openHTMLEditorial = openHTMLEditorial;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }

    public List<IconData> getIcons() {
        return icons;
    }

    public void setIcons(List<IconData> icons) {
        this.icons = icons;
    }

    public List<IconData> getImageGridData() {
        return imageGridData;
    }

    public void setImageGridData(List<IconData> imageGridData) {
        this.imageGridData = imageGridData;
    }

    public List<SectionData> getSectionData() {
        return sectionData;
    }

    public void setSectionData(List<SectionData> sectionData) {
        this.sectionData = sectionData;
    }

}
