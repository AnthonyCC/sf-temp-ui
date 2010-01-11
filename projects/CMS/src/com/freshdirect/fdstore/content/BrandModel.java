/*
 * BrandModel.java
 *
 * Created on March 18, 2002, 12:13 PM
 */

package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

/**
 *
 * @author  rgayle
 * @version 
 */
public class BrandModel extends ContentNodeModelImpl {

    List<ProductModel> featuredProducts = new ArrayList();
    
	/** Creates new BrandModel */
	public BrandModel(ContentKey cKey) {
		super(cKey);
	}
	
	public Image getLogoMedium() {
	    return FDAttributeFactory.constructImage(this, "BRAND_LOGO_MEDIUM");
	}
	
	public Image getLogoSmall() {
	    return FDAttributeFactory.constructImage(this, "BRAND_LOGO_SMALL");
	}

        public Image getLogoLarge() {
            return FDAttributeFactory.constructImage(this, "BRAND_LOGO");
        }
        public Html getPopupContent() {
            return FDAttributeFactory.constructHtml(this, "BRAND_POPUP_CONTENT");
        }
	
	
	public List<ProductModel> getFeaturedProducts() {
            ContentNodeModelUtil.refreshModels(this, "FEATURED_PRODUCTS", featuredProducts, false);

            return new ArrayList(featuredProducts);
	}
}
