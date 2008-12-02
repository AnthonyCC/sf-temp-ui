package com.freshdirect.webapp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProxyProduct;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.smartstore.Variant;



/**
 * Utility to generate various URLs pointing to store
 * 
 * @author segabor
 *
 */
public class FDURLUtil {
	public static final String URL_PARAM_SEP = "&amp;";
	
	public static final String PRODUCT_PAGE_BASE		= "/product.jsp";
	public static final String CATEGORY_PAGE_BASE		= "/category.jsp";
	public static final String DEPARTMENT_PAGE_BASE		= "/department.jsp";
	public static final String CART_CONFIRM_PAGE_BASE	= "/cart_confirm.jsp";

	public static final String RECIPE_PAGE_BASE			= "/recipe.jsp";
	public static final String RECIPE_PAGE_BASE_CRM		= "/order/recipe.jsp";

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
		
		// tracking code "<%= request.getRequestURI()%>?catId=<%=catIdParam%>&recipeId=<%=recipe.getContentName()+subCatIdParam%>&variantId=<%= variant.getContentName() %>"
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


	
	public static void appendCommonParameters(StringBuffer buf, Map params ) {
		// tracking code 
		if (params.get("trk") != null) {
			buf.append("&trk=" + ((String[])params.get("trk"))[0]);
			
			// additional DYF parameter
	        if (params.get("variant") != null) {
	        	buf.append("&variant=" + safeURLEncode(((String[])params.get("variant"))[0]) );
	        }

	        // Smart Search parameters
	        if (params.get("trkd") != null) {
	        	buf.append("&trkd=" + ((String[])params.get("trkd"))[0]);
	        	buf.append("&rank=" + ((String[])params.get("rank"))[0]);
			}
	    }
	}

	/**
	 * Build URL from request. Used in product.jsp to redirect to grocery page
     *
	 * @param request
	 * @return
	 */
	public static String getCategoryURI(HttpServletRequest request, ProductModel productNode) {
		StringBuffer uri = new StringBuffer();
		
		// "/category.jsp?catId="+request.getParameter("catId")+"&prodCatId="+request.getParameter("catId")+"&productId="+productNode+"&trk="
		
		String catId = request.getParameter("catId");
		
		// product page with category ID
		uri.append(CATEGORY_PAGE_BASE + "?catId=" + catId);
		uri.append("&prodCatId=" + catId);
		uri.append("&productId=" + productNode.getContentName());

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	
	// called from grocery_product.jsp
	public static String getCartConfirmPageURI(HttpServletRequest request) {
		StringBuffer uri = new StringBuffer();
		
		// "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId")+"&trk="+ptrk;
		
		uri.append(CART_CONFIRM_PAGE_BASE + "?catId=" + request.getParameter("catId"));

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	

	// called from product.jsp
	public static String getCartConfirmPageURI(HttpServletRequest request, ProductModel productNode) {
		StringBuffer uri = new StringBuffer();
		
		// "/cart_confirm.jsp?catId="+productNode.getParentNode().getContentName()+"&productId="+productNode.getContentName()+"&trk="+ptrk;		
		
		uri.append(CART_CONFIRM_PAGE_BASE + "?catId=" + productNode.getParentNode().getContentName());
		uri.append("&productId=" + productNode.getContentName());

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	

	// called from recipe.jsp
	public static String getRecipeCartConfirmPageURI(HttpServletRequest request, String catId) {
		StringBuffer uri = new StringBuffer();
		
		// "/grocery_cart_confirm.jsp?catId="+catIdParam+"&recipeId="+recipeId;
		String recipeId	= request.getParameter("recipeId");
		/// String catId	= request.getParameter("catId");

		uri.append(CART_CONFIRM_PAGE_BASE + "?catId=" + catId);
		uri.append("&recipeId=" + recipeId);

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}

	// called from recipe.jsp > i_recipe_body.jspf
	public static String getRecipePageURI(HttpServletRequest request, Recipe recipe, RecipeVariant variant, String catId, boolean crm) {
		StringBuffer uri = new StringBuffer();
		
		// "<%= request.getRequestURI()%>?catId=<%=catIdParam%>&recipeId=<%=recipe.getContentName()+subCatIdParam%>&variantId=<%= variant.getContentName() %>"

		/// String catId = request.getParameter("catId");

		uri.append( (crm ? RECIPE_PAGE_BASE_CRM : RECIPE_PAGE_BASE ) + "?catId=" + catId + "&recipeId=" + recipe.getContentName());

		String subCatId = request.getParameter("subCatId");
		if (subCatId !=null && !"".equals(subCatId.trim()) ) {
			uri.append("&subCatId=" + subCatId.trim());
		}

		uri.append("&variantId=" + variant.getContentName());

		appendCommonParameters(uri, request.getParameterMap());

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
