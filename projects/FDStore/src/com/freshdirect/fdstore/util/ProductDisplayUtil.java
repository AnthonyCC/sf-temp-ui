package com.freshdirect.fdstore.util;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProxyProduct;
import com.freshdirect.fdstore.customer.FDUserI;

public class ProductDisplayUtil {
	public static final String URL_PARAM_SEP = "&amp;";
	public static final String IMPRESSION_ID = "impId";

	public static final String PRODUCT_PAGE_BASE = "/product.jsp";
	public static final String CATEGORY_PAGE_BASE = "/category.jsp";
	public static final String DEPARTMENT_PAGE_BASE = "/department.jsp";
	public static final String CART_CONFIRM_PAGE_BASE = "/cart_confirm.jsp";
	public static final String GR_CART_CONFIRM_PAGE_BASE = "/grocery_cart_confirm.jsp";

	public final static ProductModel.RatingComparator RATING_COMP = new ProductModel.RatingComparator();

	public static String getProductBurstCode(FDUserI user,
			EnumSiteFeature siteFeature, ProductModel product) {
		String burst = null;
		ProductLabeling labeling = new ProductLabeling(user, product, false,
				false, false, EnumSiteFeature.DYF.equals(siteFeature));
		if (labeling.isDisplayDeal())
			burst = Integer.toString(product.getHighestDealPercentage());
		else if (labeling.isDisplayFave())
			burst = "YF";
		else if (labeling.isDisplayNew())
			burst = "NE";
		return burst;
	}

	public static String getProductRatingCode(FDUserI user, ProductModel product)
			throws FDResourceException, FDSkuNotFoundException {
		String rating = user.isProduceRatingEnabled() ? product
				.getProductRating() : null;
		if (rating.length() < 2)
			rating = null;
		return rating;
	}

	/**
	 * Get the "real" parent of the content node.
	 * 
	 * In the case of {@link ProxyProduct}s the "real" parent is that of the
	 * product wrapped.
	 * 
	 * @param model
	 * @return
	 */
	public static ContentNodeModel getRealParent(ContentNodeModel model) {
		if (model instanceof ConfiguredProductGroup) {
			ConfiguredProductGroup configuredProductGroup = (ConfiguredProductGroup) model;
			return getRealParent(configuredProductGroup.getProduct());
		} else if (model instanceof ConfiguredProduct) {
			ConfiguredProduct configuredProduct = (ConfiguredProduct) model;
			return getRealParent(configuredProduct.getProduct());
		} else {
			return model.getParentNode();
		}
	}

	/**
	 * Get the "real" product.
	 * 
	 * In the case of {@link ProxyProduct}s the "real" product is the one
	 * wrapped.
	 * 
	 * @param productModel
	 * @return
	 */
	public static ProductModel getRealProduct(ProductModel productModel) {
		if (productModel instanceof ConfiguredProductGroup) {
			return ((ConfiguredProductGroup) productModel).getProduct();
		} else if (productModel instanceof ConfiguredProduct) {
			return ((ConfiguredProduct) productModel).getProduct();
		} else {
			return productModel;
		}
	}

	public static String getProductURI(ProductModel productNode) {
		StringBuffer uri = new StringBuffer();

		uri.append(PRODUCT_PAGE_BASE);
		uri.append("?catId=" + getRealParent(productNode).getContentName());

		uri.append(URL_PARAM_SEP);
		uri.append("productId=" + getRealProduct(productNode).getContentName());

		return uri.toString();
	}
}
