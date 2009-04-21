package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProxyProduct;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.taglib.smartstore.Impression;




/**
 * Utility to generate various URLs pointing to store
 * 
 * @author segabor
 *
 */
public class FDURLUtil {
	private static final String IMPRESSION_ID = "impId";

    public static final String URL_PARAM_SEP = "&amp;";
	
	public static final String PRODUCT_PAGE_BASE		= "/product.jsp";
	public static final String CATEGORY_PAGE_BASE		= "/category.jsp";
	public static final String DEPARTMENT_PAGE_BASE		= "/department.jsp";
	public static final String CART_CONFIRM_PAGE_BASE	= "/cart_confirm.jsp";
	public static final String GR_CART_CONFIRM_PAGE_BASE	= "/grocery_cart_confirm.jsp";

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
	 * Convenience method for recommended products
	 * 
	 * @param productNode
	 * @param variant
	 * @param trackingCodeEx
	 * @param rank
	 * @return
	 */
	public static String getProductURI(ProductModel productNode, Variant variant, String trackingCodeEx, int rank) {
		return getProductURI(productNode, variant.getId(), variant.getSiteFeature().getName().toLowerCase(), trackingCodeEx, rank);
	}

        /**
         * Generate product page URL
         * (called from Featured Items pages)
         * 
         * @param productNode {@link ProductModel} product instance
         * @param variantId {@link String} variant identifier
         * @param trackingCode {@link String} Tracking code (dyf, cpage, ...)
         * @param trackingCodeEx {@link String} Tracking code (fave, ...)
         * @return URI that points to the page of product
         */
        public static String getProductURI(ProductModel productNode, String variantId, String trackingCode, String trackingCodeEx, int rank) {
            return getProductURI(productNode, variantId, trackingCode,trackingCodeEx,rank, null);
        }

	/**
	 * Generate product page URL
	 * (called from Featured Items pages)
	 * 
	 * @param productNode {@link ProductModel} product instance
	 * @param variantId {@link String} variant identifier
	 * @param trackingCode {@link String} Tracking code (dyf, cpage, ...)
	 * @param trackingCodeEx {@link String} Tracking code (fave, ...)
	 * @return URI that points to the page of product
	 */
	public static String getProductURI(ProductModel productNode, String variantId, String trackingCode, String trackingCodeEx, int rank, String impressionId) {
		
		StringBuffer uri = new StringBuffer();
		
		// product page with category ID
		uri.append(PRODUCT_PAGE_BASE + "?catId=" + getRealParent(productNode).getContentName());

		// append product ID
		uri.append(URL_PARAM_SEP + "productId=" + getRealProduct(productNode).getContentName());
		
		// append variant ID (optional)
		if (variantId != null) {
			// variant ID may contain SPACE or other non-ASCII characters ...
			uri.append(URL_PARAM_SEP + "variant=" + safeURLEncode(variantId));
			// uri.append(URL_PARAM_SEP + "fdsc.source=SS");
		}

		// tracking code 
		if (trackingCode != null) {
			uri.append(URL_PARAM_SEP + "trk=" + trackingCode);
			
			
			// tracking code 
			if (trackingCodeEx != null) {
				uri.append(URL_PARAM_SEP + "trkd=" + trackingCodeEx);
			}

			uri.append(URL_PARAM_SEP + "rank=" + rank);
		}
		if (impressionId != null) {
		    uri.append(URL_PARAM_SEP).append(IMPRESSION_ID+"=").append(impressionId);
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
		if (trackingCode == null) {
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

	// convenience method
	public static String getCategoryURI(CategoryModel cat, String trackingCode) {
		return getCategoryURI(cat.getContentName(), trackingCode);
	}


	/**
	 * Extracts parameters from request and put them to a separate hash map
	 * 
	 * @param params Original request parameters
	 * @param suffix target key suffix
	 * @return Extracted parameters
	 */
	private static Map collectCommonParameters(Map params, String suffix) {
		Map collectedParams = new HashMap();

		if (suffix == null) {
			suffix = "";
		}

		// tracking code 
	    if (params.get("trk") != null) {
	    	collectedParams.put("trk"+suffix, ((String[])params.get("trk"))[0]);
			
	    	// additional DYF parameter
	    	if (params.get("variant") != null) {
	    		collectedParams.put("variant"+suffix, ((String[])params.get("variant"))[0]);
	    	}

	    	// Smart Search parameters
	    	if (params.get("trkd") != null) {
	    		collectedParams.put("trkd"+suffix, ((String[])params.get("trkd"))[0]);
	    		if (params.get("rank")!=null) {
	    			collectedParams.put("rank"+suffix, ((String[])params.get("rank"))[0]);
	    		}
	    	}
	    }
	    if (params.get(IMPRESSION_ID) != null) {
	        collectedParams.put(IMPRESSION_ID, ((String[])params.get(IMPRESSION_ID))[0]);
	    }
	    return collectedParams;
	}



	/**
	 * Extracts parameters from request and put them to string buffer
	 * 
	 * @param buf<StringBuffer> Buffer that will have parameters
	 * @param params Request parameters
	 */
	public static void appendCommonParameters(StringBuffer buf, Map params ) {
		Map _p = collectCommonParameters(params, null);

		for (Iterator it=_p.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
	        buf.append("&"+e.getKey()+"=" + e.getValue());
		}
	}

	public static String getHiddenCommonParameters(Map params, String suffix) {
		Map _p = collectCommonParameters(params, suffix);
		StringWriter out = new StringWriter();
		for (Iterator it=_p.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			appendHiddenField(out, e.getKey().toString(), e.getValue().toString());
		}
		return out.toString();
	}

	/**
	 * Put parameters to hidden input fields
	 * 
	 * @param out
	 * @param params
	 * @param suffix
	 */
	public static void appendCommonParameters(JspWriter out, Map params, String suffix) {
		Map _p = collectCommonParameters(params, suffix);
		for (Iterator it=_p.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
	        appendHiddenField(out, e.getKey().toString(), e.getValue().toString());
		}
	}
	
	private static void appendHiddenField(Writer out, String name, String value) {
		try {
			out.write("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
		} catch (IOException e) {}
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
		
		uri.append(GR_CART_CONFIRM_PAGE_BASE + "?catId=" + request.getParameter("catId"));

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

		uri.append(GR_CART_CONFIRM_PAGE_BASE + "?catId=" + catId);
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
	
	
	public static void logProductClick(HttpServletRequest req) {
            String impressionId = req.getParameter(IMPRESSION_ID);
            String trkId = req.getParameter("trk");
            String trkdId = req.getParameter("trkd");
            if (impressionId!=null) {
                Impression.productClick(impressionId, trkId, trkdId);
            }
	}
}
