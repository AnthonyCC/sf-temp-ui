package com.freshdirect.mobileapi.model;

import java.io.IOException;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.content.TitledMedia;
import com.freshdirect.mobileapi.util.ProductUtil;

public class Brand {
    /**
     * Brand logo
     */
    private String logo;

    private int width;

    private int height;

    /** 
     * Brand name
     */
    private String name;

    private String id;

    /**
     * Description about the brand. HTML content
     */
    private String description = "";

    @SuppressWarnings("deprecation")
    public static Brand wrap(BrandModel brandModel) {
        Brand brand = new Brand();
        Attribute logo = brandModel.getAttribute("BRAND_LOGO_SMALL");
        Attribute content = brandModel.getAttribute("BRAND_POPUP_CONTENT");
        if (content != null) {
            TitledMedia tm = (TitledMedia) content.getValue();
            try {
                brand.description = ProductUtil.readContent(ProductUtil.resolve(tm.getPath()));
            } catch (IOException e) {
                brand.description = "";
            }

        }
        if(logo != null) {
            MediaI brandLogo = (MediaI) logo.getValue();
            brand.height = brandLogo.getHeight();
            brand.width = brandLogo.getWidth();
            brand.logo = brandLogo.getPath();
        }
        brand.name = brandModel.getFullName();
        brand.id = brandModel.getContentKey().getId();
        return brand;
    }

    public String getLogo() {
        return logo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Brand other = (Brand) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
