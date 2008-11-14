package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.attributes.Attribute;

public abstract class AbstractProductModelImpl extends ContentNodeModelImpl implements ProductModel {

	protected final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	private List alsoSoldAs = new ArrayList();
	private List alsoSoldAsList = new ArrayList();
	private List alsoSoldAsRefs = new ArrayList();

	public AbstractProductModelImpl(ContentKey key) {
		super(key);
	}

	//
	// common implementation between Product and ConfiguredProduct
	// 

	/*

	 FULL_NAME
	 GLANCE_NAME
	 NAV_NAME
	 BLURB
	 PROD_DESCR
	 PROD_IMAGE
	 PROD_IMAGE_CONFIRM
	 PROD_IMAGE_DETAIL
	 PROD_IMAGE_FEATURE
	 PROD_IMAGE_ZOOM
	 PROD_DESCRIPTION_NOTE
	 ALTERNATE_IMAGE
	 PRODUCT_QUALITY_NOTE
	 RELATED_PRODUCTS
	 ALSO_SOLD_AS

	 */
	

	private Image getImage(String key) {
		return (Image) getAttribute(key, IMAGE_BLANK);
	}

	public Image getCategoryImage() {
		return getImage("PROD_IMAGE");
	}

	public Image getConfirmImage() {
		return getImage("PROD_IMAGE_CONFIRM");
	}

	public Image getDetailImage() {
		return getImage("PROD_IMAGE_DETAIL");
	}

	public Image getThumbnailImage() {
		return getImage("PROD_IMAGE_FEATURE");
	}

	public Image getZoomImage() {
		return getImage("PROD_IMAGE_ZOOM");
	}


	public ProductModel getAlsoSoldAs(int idx) {
		return (ProductModel) getAlsoSoldAs().get(idx);
	}
	
	/**
	 * Can the product be recommended.
	 * @return if the EXCLUDED_RECOMMENDATION flag is set to true (default false)
	 */
	public boolean isExcludedRecommendation() {
		return getAttribute("EXCLUDED_RECOMMENDATION", false);
	}

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	public List getAlsoSoldAs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAs, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAs);
		}

		ArrayList refs = new ArrayList();
		for (Iterator iter = alsoSoldAs.iterator(); iter.hasNext();) {
			ProductModel pm = (ProductModel) iter.next();
			ProductRef ref = new ProductRef(pm.getParentNode().getContentName(), pm.getContentName());
			refs.add(ref);
		}
		return refs;
	}

	public List getAlsoSoldAsRefs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAsList, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAsList);
			alsoSoldAsRefs.clear();
			for (Iterator tmp = alsoSoldAsList.iterator(); tmp.hasNext();) {
				ContentNodeModel m = (ContentNodeModel) tmp.next();
				alsoSoldAsRefs.add(new ProductRef(m.getParentNode().getContentName(), m.getContentName()));
			}
		}
		return alsoSoldAsRefs;
	}

	
	public Html getProductDescription() {
		Attribute a = getAttribute("PROD_DESCR");
		return a == null ? null : (Html) a.getValue();
	}

	public boolean hasTerms() {
		Attribute a = getAttribute("PRODUCT_TERMS_MEDIA");
		return a == null ? false : true;
	}
	
	public Html getProductTerms() {
		Attribute a = getAttribute("PRODUCT_TERMS_MEDIA");
		return a == null ? null : (Html) a.getValue();
	}
	
	public boolean isShowTopTenImage() {
		return getAttribute("SHOW_TOP_TEN_IMAGE", false);
	}
	
	public String getDefaultPrice() {
			try {
				SkuModel skuModel = getDefaultSku();
				if(skuModel == null)
					return "";
				FDProductInfo productInfo = skuModel.getProductInfo();
				if(productInfo == null)
					return "";
				return productInfo.getDefaultPrice() + "/" +productInfo.getDefaultPriceUnit();
			} catch (FDResourceException e) {
				throw new RuntimeException(e);
			} catch (FDSkuNotFoundException e) {
				throw new RuntimeException(e);
			}
	}
	
	public boolean isDisplayable() {	
		return !(isHidden() || isDiscontinued() || isUnavailable() || isOrphan() || isInvisible());
	}
	
        public boolean isDisplayableBasedOnCms() {        
            return !(isHidden() ||  isOrphan() || isInvisible());
        }
	
	public String getDefaultPriceOnly() {
		try {
			SkuModel skuModel = getDefaultSku();
			if(skuModel == null)
				return "";
			FDProductInfo productInfo = skuModel.getProductInfo();
			if(productInfo == null)
				return "";
			return productInfo.getDefaultPrice() + "";
		} catch (FDResourceException e) {
			throw new RuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getDefaultUnitOnly() {
		try {
			SkuModel skuModel = getDefaultSku();
			if(skuModel == null)
				return "";
			FDProductInfo productInfo = skuModel.getProductInfo();
			if(productInfo == null)
				return "";
			return productInfo.getDefaultPriceUnit();
		} catch (FDResourceException e) {
			throw new RuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new RuntimeException(e);
		}
}
	
}
