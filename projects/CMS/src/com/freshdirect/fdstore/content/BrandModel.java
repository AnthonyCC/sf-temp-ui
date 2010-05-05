package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class BrandModel extends ContentNodeModelImpl {

	private static final long	serialVersionUID	= 3825451393246006610L;

	List<ProductModel> featuredProducts = new ArrayList<ProductModel>();

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
	
        public ProducerModel getProducer() {
            Object value = getCmsAttributeValue("producer");
            return value instanceof ContentKey ? (ProducerModel) ContentFactory.getInstance().getContentNodeByKey((ContentKey) value) : null;
        }
        
	
	public List<ProductModel> getFeaturedProducts() {
            ContentNodeModelUtil.refreshModels(this, "FEATURED_PRODUCTS", featuredProducts, false);

            return new ArrayList(featuredProducts);
	}
}
