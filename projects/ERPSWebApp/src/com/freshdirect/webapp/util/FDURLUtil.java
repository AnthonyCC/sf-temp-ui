package com.freshdirect.webapp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProxyProduct;
import com.freshdirect.smartstore.Variant;



/**
 * Utility to generate various URLs pointing to store
 * 
 * @author segabor
 *
 */
public class FDURLUtil {
	public static final String URL_PARAM_SEP = "&amp;";
	
	public static final String PRODUCT_PAGE_BASE = "/product.jsp";
	public static final String CATEGORY_PAGE_BASE = "/category.jsp";
	public static final String DEPARTMENT_PAGE_BASE = "/department.jsp";

	
	public static String safeURLEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// NOTE: this should never happen!
			return "";
		}
	}


	public static String getProductURI(ProductModel productNode, String trackingCode) {
		return FDURLUtil.getProductURI(productNode, trackingCode, null);
	}

	
	public static String getProductURI(ProductModel productNode, Variant variant) {
		// FIXME: later variant will have site feature attribute as well
		return FDURLUtil.getProductURI(productNode,
				variant.getSiteFeature().getName().toLowerCase(),
				variant.getId());
	}

	/**
	 * Get the "real" parent of the content node.
	 * 
	 * In the case of {@link ProxyProduct}s the "real" parent is that of the product wrapped.
	 * 
	 * @param model
	 * @return
	 */
	private static ContentNodeModel getRealParent(ContentNodeModel model) {
		if (model instanceof ConfiguredProductGroup) {
			ConfiguredProductGroup configuredProductGroup = (ConfiguredProductGroup)model;
			return getRealParent(configuredProductGroup.getProduct());
		} else if (model instanceof ConfiguredProduct) {
			ConfiguredProduct configuredProduct = (ConfiguredProduct)model;
			return getRealParent(configuredProduct.getProduct());
		} else { 
			return model.getParentNode();
		}
	}
	
	/**
	 * Get the "real" product.
	 * 
	 * In the case of {@link ProxyProduct}s the "real" product is the one wrapped.
	 * @param productModel
	 * @return
	 */
	private static ProductModel getRealProduct(ProductModel productModel) {
		if (productModel instanceof ConfiguredProductGroup) {
			return ((ConfiguredProductGroup)productModel).getProduct();
		} else if (productModel instanceof ConfiguredProduct) {
			return ((ConfiguredProduct)productModel).getProduct();
		} else {
			return productModel;
		}
	}


	/**
	 * Generate product page URL
	 * 
	 * @param productNode {@link ProductModel} product instance
	 * @param trackingCode {@link String} Tracking code (dyf, cpage, ...)
	 * @param variantId {@link String} variant identifier
	 * @return URI that points to the page of product
	 */
	public static String getProductURI(ProductModel productNode, String trackingCode, String variantId) {
		
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(PRODUCT_PAGE_BASE + "?catId=" + getRealParent(productNode).getContentName());
		
		// tracking code 
		if (trackingCode != null) {
			uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
		}
		
		// append product ID
		uri.append(URL_PARAM_SEP + "productId=" + getRealProduct(productNode).getContentName());
		
		// append variant ID (optional)
		if (variantId != null) {
			// variant ID may contain SPACE or other non-ASCII characters ...
			uri.append(URL_PARAM_SEP + "variant=" + safeURLEncode(variantId));
			uri.append(URL_PARAM_SEP + "fdsc.source=SS");
		}

		return uri.toString();
	}


	/**
	 * Generates URL for products in search page
	 * 
	 * @param productNode
	 * @param trackingCode	trk
	 * @param trackingCode2	trkd
	 * @param rank			rank
	 * @return
	 */
	public static String getProductURI(ProductModel productNode, String trackingCode, String trackingCodeEx, int rank) {
		
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(PRODUCT_PAGE_BASE + "?catId=" + getRealParent(productNode).getContentName());
		
		// tracking code 
		if (trackingCode != null) {
			trackingCode = "srch"; // default value
		}
		uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
		
		// append product ID
		uri.append(URL_PARAM_SEP + "productId=" + getRealProduct(productNode).getContentName());
		
		// tracking code 
		if (trackingCodeEx != null) {
			uri.append(URL_PARAM_SEP + "trkd=" + trackingCodeEx);
		}

		uri.append(URL_PARAM_SEP + "rank=" + rank);

		return uri.toString();
	}

	// get uri of configured product
	//   see in i_configured_product.jspf
	public static String getConfiguredProductURI(ConfiguredProduct productNode, String trackingCode, FDConfigurableI config) {
		ProductModel actProd = productNode.getProduct();
		Map cfgOptions = config.getOptions();
		
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(PRODUCT_PAGE_BASE + "?catId=" + actProd.getParentNode().getContentName());

		// tracking code 
		if (trackingCode != null) {
			uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
		}
		
		// append product ID
		uri.append(URL_PARAM_SEP + "productId=" + actProd.getContentName());
		
		uri.append(URL_PARAM_SEP + "skuCode="+productNode.getSkuCode());

		// append configuration
		uri.append(URL_PARAM_SEP + "quantity=").append(config.getQuantity()).append(URL_PARAM_SEP + "salesUnit=").append(config.getSalesUnit());
    	for(Iterator optItr = cfgOptions.keySet().iterator(); optItr.hasNext();) {
    		String optionName = (String)optItr.next();
    		String optionValue = (String)cfgOptions.get(optionName);
    		uri.append(URL_PARAM_SEP).append(optionName).append("=").append(optionValue);
    	}
		return uri.toString();
	}



	/**
	 * Returns URL to product image
	 * @param productNode <{@link ProductModel}> product the link points to
	 * @return link to media
	 */
	public static Image getProductImage(ProductModel productNode) {
		Image image = productNode.getSourceProduct().getAlternateImage();
		if (image == null) {
            image = productNode.getSourceProduct().getCategoryImage();
        }
        return image;
	}

	
	/**
	 * Returns URI to category page
     * 
	 * @param catId category ID
	 * @param trackingCode Tracking Code
	 * @return link to category page
	 */
	public static String getCategoryURI(String catId, String trackingCode) {
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(CATEGORY_PAGE_BASE + "?catId=" + catId);

		// tracking code 
		if (trackingCode != null) {
			uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
		}

		return uri.toString();
	}




	/**
	 * Returns URI to department page
     * 
	 * @param deptId department ID
	 * @param trackingCode Tracking Code
	 * @return link to category page
	 */
	public static String getDepartmentURI(String deptId, String trackingCode) {
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(DEPARTMENT_PAGE_BASE + "?deptId=" + deptId);

		// tracking code 
		if (trackingCode != null) {
			uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
		}

		return uri.toString();
	}
}
