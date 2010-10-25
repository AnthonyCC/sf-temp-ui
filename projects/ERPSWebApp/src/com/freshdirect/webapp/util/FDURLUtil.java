package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.ProductDisplayUtil;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.taglib.smartstore.Impression;


/**
 * Utility to generate various URLs pointing to store
 * 
 * @author segabor
 *
 */
public class FDURLUtil {
	
	public static final String RECIPE_PAGE_BASE			= "/recipe.jsp";
	public static final String RECIPE_PAGE_BASE_CRM		= "/order/recipe.jsp";

	public static final String STANDING_ORDER_DETAIL_PAGE	= "/quickshop/so_details.jsp";
	public static final String STANDING_ORDER_MAIN_PAGE		= "/quickshop/standing_orders.jsp";
	
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
		return FDURLUtil.getProductURI(productNode,
				variant.getSiteFeature().getName().toLowerCase(),
				variant.getId());
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
		
		StringBuilder uri = new StringBuilder();
		
		appendProduct(uri, productNode);

		// product page with category ID
		// uri.append(ProductDisplayUtil.PRODUCT_PAGE_BASE + "?catId=" + ProductDisplayUtil.getRealParent(productNode).getContentName());
		
		// tracking code 
		if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
		}
		
		// append product ID
		// uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + ProductDisplayUtil.getRealProduct(productNode).getContentName());
		
		// append variant ID (optional)
		if (variantId != null) {
			// variant ID may contain SPACE or other non-ASCII characters ...
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "variant=" + safeURLEncode(variantId));
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "fdsc.source=SS");
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
	public static String getProductURI(ProductModel productNode, String variantId, String trackingCode, 
			String trackingCodeEx, int rank, String impressionId) {
		return getProductURI(productNode, variantId, trackingCode, trackingCodeEx, rank, impressionId, null, null);
	}
    
	/**
	 * Generate product page URL
	 * (called from Featured Items pages)
	 * 
	 * @param productNode {@link ProductModel} product instance
	 * @param variantId {@link String} variant identifier
	 * @param trackingCode {@link String} Tracking code (dyf, cpage, ...)
	 * @param trackingCodeEx {@link String} Tracking code (fave, ...)
	 * @param ymalSetId {@link String} YMAL set ID
	 * @param originatingProductId {@link String} originating product id or YMAL product ID
	 * @return URI that points to the page of product
	 */
	public static String getProductURI(ProductModel productNode, String variantId, String trackingCode, 
			String trackingCodeEx, int rank, String impressionId, String ymalSetId, String originatingProductId) {
		
		StringBuilder uri = new StringBuilder();
		
		appendProduct(uri, productNode);

		// product page with category ID
		// uri.append(ProductDisplayUtil.PRODUCT_PAGE_BASE + "?catId=" + ProductDisplayUtil.getRealParent(productNode).getContentName());

		// append product ID
		// uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + ProductDisplayUtil.getRealProduct(productNode).getContentName());
		
		// append variant ID (optional)
		if (variantId != null) {
			// variant ID may contain SPACE or other non-ASCII characters ...
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "variant=" + safeURLEncode(variantId));
			// uri.append(URL_PARAM_SEP + "fdsc.source=SS");
		}

		// append YMAL set ID (optional)
		if (ymalSetId != null) {
			// YMAL set ID may contain SPACE or other non-ASCII characters ...
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "ymalSetId=" + safeURLEncode(ymalSetId));
		}

		// append originatig product ID (optional)
		if (originatingProductId != null) {
			// originating product ID may contain SPACE or other non-ASCII characters ...
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "originatingProductId=" + safeURLEncode(originatingProductId));
		}

		// tracking code 
		if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
			
			
			// tracking code 
			if (trackingCodeEx != null) {
				uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trkd=" + trackingCodeEx);
			}

			if (rank >= 0)
				uri.append(ProductDisplayUtil.URL_PARAM_SEP + "rank=" + rank);
		}
		if (impressionId != null) {
		    uri.append(ProductDisplayUtil.URL_PARAM_SEP).append(ProductDisplayUtil.IMPRESSION_ID+"=").append(impressionId);
		}

		return uri.toString();
	}


	/**
	 * Generates URL for products in search page
	 * 
	 * @param productNode
	 * @param trackingCode	Tracking Code (trk)
	 * @param trackingCodeEx Tracking Code Detail (trkd)
	 * @param rank			Rank (if value is greater or equal to 0)
	 * @return
	 */
	public static String getProductURI(ProductModel productNode, String trackingCode, String trackingCodeEx, int rank) {
		
		StringBuilder uri = new StringBuilder();
		
		appendProduct(uri, productNode);
		
		// product page with category ID
		// uri.append(ProductDisplayUtil.PRODUCT_PAGE_BASE + "?catId=" + ProductDisplayUtil.getRealParent(productNode).getContentName());
		
		// tracking code 
		if (trackingCode == null) {
			trackingCode = "srch"; // default value
		}
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
		
		// append product ID
		// uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + ProductDisplayUtil.getRealProduct(productNode).getContentName());
		
		// tracking code "<%= request.getRequestURI()%>?catId=<%=catIdParam%>&recipeId=<%=recipe.getContentName()+subCatIdParam%>&variantId=<%= variant.getContentName() %>"
		if (trackingCodeEx != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trkd=" + trackingCodeEx);
		}

		if (rank >= 0)
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "rank=" + rank);

		return uri.toString();
	}


	/**
	 * Appends product and its parent category ID to URI
	 * 
	 * @param uri
	 * @param productNode
	 * @return
	 */
	private static StringBuilder appendProduct(StringBuilder uri, ProductModel productNode) {
		// product page with category ID
		uri.append(ProductDisplayUtil.PRODUCT_PAGE_BASE + "?catId=" + ProductDisplayUtil.getRealParent(productNode).getContentName());

		// append product ID
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + ProductDisplayUtil.getRealProduct(productNode).getContentName());

		return uri;
	}


	// get uri of configured product
	//   see in i_configured_product.jspf
	public static String getConfiguredProductURI(ConfiguredProduct productNode, String trackingCode, FDConfigurableI config) {
		final ProductModel actProd = productNode.getProduct();
		Map<String,String> cfgOptions = config.getOptions();
		
		StringBuilder uri = new StringBuilder();
		
		appendProduct(uri, actProd);

		// product page with category ID
		// uri.append(ProductDisplayUtil.PRODUCT_PAGE_BASE + "?catId=" + actProd.getParentNode().getContentName());

		// tracking code 
		if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
		}

		// append product ID
		// uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + actProd.getContentName());
		
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "skuCode="+productNode.getSkuCode());

		// append configuration
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "quantity=").append(config.getQuantity()).append(ProductDisplayUtil.URL_PARAM_SEP + "salesUnit=").append(config.getSalesUnit());
    	for( String optionName : cfgOptions.keySet() ) {
    		String optionValue = cfgOptions.get(optionName);
    		uri.append(ProductDisplayUtil.URL_PARAM_SEP).append(optionName).append("=").append(optionValue);
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
		StringBuilder uri = new StringBuilder();
		
		// product page with category ID
		uri.append(ProductDisplayUtil.CATEGORY_PAGE_BASE + "?catId=" + catId);

		// tracking code 
		if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
		}

		return uri.toString();
	}

	// convenience method
	public static String getCategoryURI(CategoryModel cat, String trackingCode) {
		
		// Use alias category if present
		ContentKey aliasKey = cat.getAliasAttributeValue();
		if ( aliasKey  != null ) {
			cat = (CategoryModel)ContentFactory.getInstance().getContentNodeByKey( aliasKey );
		}
		
		return getCategoryURI(cat.getContentName(), trackingCode);
	}


	/**
	 * Extracts parameters from request and put them to a separate hash map
	 * 
	 * @param params Original request parameters
	 * @param suffix target key suffix
	 * @return Extracted parameters
	 */
	private static Map<String, String> collectCommonParameters(Map<String,String[]> params, String suffix) {
		Map<String, String> collectedParams = new HashMap<String, String>();

		if (suffix == null) {
			suffix = "";
		}

		// tracking code 
	    if (params.get("trk") != null) {
	    	collectedParams.put("trk"+suffix, params.get("trk")[0]);
			
	    	// additional DYF parameter
	    	if (params.get("variant") != null) {
	    		collectedParams.put("variant"+suffix, params.get("variant")[0]);
	    	}

	    	if (params.get("ymalSetId") != null) {
	    		collectedParams.put("ymalSetId"+suffix, params.get("ymalSetId")[0]);
	    	}

	    	if (params.get("originatingProductId") != null) {
	    		collectedParams.put("originatingProductId"+suffix, params.get("originatingProductId")[0]);
	    	}

	    	// Smart Search parameters
	    	if (params.get("trkd") != null) {
	    		collectedParams.put("trkd"+suffix, params.get("trkd")[0]);
	    		if (params.get("rank")!=null) {
	    			collectedParams.put("rank"+suffix, params.get("rank")[0]);
	    		}
	    	}
	    }
	    if (params.get(ProductDisplayUtil.IMPRESSION_ID) != null) {
	        collectedParams.put(ProductDisplayUtil.IMPRESSION_ID, params.get(ProductDisplayUtil.IMPRESSION_ID)[0]);
	    }
	    return collectedParams;
	}



	/**
	 * Extracts parameters from request and put them to string buffer
	 * 
	 * @param buf Buffer that will have parameters
	 * @param params Request parameters
	 */
	public static void appendCommonParameters(Appendable buf, Map<String,String[]> params ) {
		Map<String, String> _p = collectCommonParameters(params, null);

		for ( Map.Entry<String,String> e : _p.entrySet() ) {
			try {
				buf.append(ProductDisplayUtil.URL_PARAM_SEP + ""+e.getKey()+"=" + e.getValue());
			} catch (IOException e1) {}
		}
	}

	public static String getHiddenCommonParameters(Map<String,String[]> params, String suffix) {
		Map<String, String> _p = collectCommonParameters(params, suffix);
		StringWriter out = new StringWriter();
		for ( Map.Entry<String,String> e : _p.entrySet() ) {
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
	public static void appendCommonParameters(Appendable out, Map<String,String[]> params, String suffix) {
		Map<String, String> _p = collectCommonParameters(params, suffix);
		for ( Map.Entry<String,String> e : _p.entrySet() ) {
	        appendHiddenField(out, e.getKey().toString(), e.getValue().toString());
		}
	}
	
	private static void appendHiddenField(Appendable out, String name, String value) {
		try {
			out.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
		} catch (IOException e) {}
	}


	/**
	 * Build URL from request. Used in product.jsp to redirect to grocery page
     *
	 * @param request
	 * @return
	 */
	@SuppressWarnings( "unchecked" )
	public static String getCategoryURI(HttpServletRequest request, ProductModel productNode) {
		StringBuilder uri = new StringBuilder();
		
		// "/category.jsp?catId="+request.getParameter("catId")+"&prodCatId="+request.getParameter("catId")+"&productId="+productNode+"&trk="
		
		String catId = request.getParameter("catId");
		
		// product page with category ID
		uri.append(ProductDisplayUtil.CATEGORY_PAGE_BASE + "?catId=" + catId);
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "prodCatId=" + catId);
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + productNode.getContentName());

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	
	// called from grocery_product.jsp
	@SuppressWarnings( "unchecked" )
	public static String getCartConfirmPageURI(HttpServletRequest request) {
		StringBuilder uri = new StringBuilder();
		
		// "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId")+"&trk="+ptrk;
		
		uri.append(ProductDisplayUtil.GR_CART_CONFIRM_PAGE_BASE + "?catId=" + request.getParameter("catId"));

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	

	// called from product.jsp
	@SuppressWarnings( "unchecked" )
	public static String getCartConfirmPageURI(HttpServletRequest request, ProductModel productNode) {
		StringBuilder uri = new StringBuilder();
		
		// "/cart_confirm.jsp?catId="+productNode.getParentNode().getContentName()+"&productId="+productNode.getContentName()+"&trk="+ptrk;		
		
		uri.append(ProductDisplayUtil.CART_CONFIRM_PAGE_BASE + "?catId=" + productNode.getParentNode().getContentName());
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=" + productNode.getContentName());

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}
	

	// called from recipe.jsp
	@SuppressWarnings( "unchecked" )
	public static String getRecipeCartConfirmPageURI(HttpServletRequest request, String catId) {
		StringBuilder uri = new StringBuilder();
		
		// "/grocery_cart_confirm.jsp?catId="+catIdParam+"&recipeId="+recipeId;
		String recipeId	= request.getParameter("recipeId");
		/// String catId	= request.getParameter("catId");

		uri.append(ProductDisplayUtil.GR_CART_CONFIRM_PAGE_BASE + "?catId=" + catId);
		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "recipeId=" + recipeId);

		appendCommonParameters(uri, request.getParameterMap());

		return uri.toString();
	}

	// called from recipe.jsp > i_recipe_body.jspf
	@SuppressWarnings( "unchecked" )
	public static String getRecipePageURI(HttpServletRequest request, Recipe recipe, RecipeVariant variant, String catId, boolean crm) {
		StringBuilder uri = new StringBuilder();
		
		// "<%= request.getRequestURI()%>?catId=<%=catIdParam%>&recipeId=<%=recipe.getContentName()+subCatIdParam%>&variantId=<%= variant.getContentName() %>"

		/// String catId = request.getParameter("catId");

		uri.append( (crm ? RECIPE_PAGE_BASE_CRM : RECIPE_PAGE_BASE ) + "?catId=" + catId + ProductDisplayUtil.URL_PARAM_SEP + "recipeId=" + recipe.getContentName());

		String subCatId = request.getParameter("subCatId");
		if (subCatId !=null && !"".equals(subCatId.trim()) ) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "subCatId=" + subCatId.trim());
		}

		uri.append(ProductDisplayUtil.URL_PARAM_SEP + "variantId=" + variant.getContentName());

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
		StringBuilder uri = new StringBuilder();
		
		// product page with category ID
		uri.append(ProductDisplayUtil.DEPARTMENT_PAGE_BASE + "?deptId=" + deptId);

		// tracking code 
		if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "trk=" + trackingCode);
		}

		return uri.toString();
	}
	
	
	public static void logProductClick(HttpServletRequest req) {
            String impressionId = req.getParameter(ProductDisplayUtil.IMPRESSION_ID);
            String trkId = req.getParameter("trk");
            String trkdId = req.getParameter("trkd");
            if (impressionId!=null) {
                Impression.productClick(impressionId, trkId, trkdId);
            }
	}


	/**
	 * Return URI to standing order page
	 * 
	 * @param so a {@link com.freshdirect.fdstore.standingorders.FDStandingOrder} instance
	 * @param action Action name (can be null)
	 * 
	 * @return
	 */
	public static String getStandingOrderLandingPage(FDStandingOrder so, String action) {
		StringBuilder uri = new StringBuilder();
		uri.append(STANDING_ORDER_DETAIL_PAGE);
		uri.append("?ccListId=" + so.getCustomerListId());
		if (action != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP + "action="+action);
		}
		return uri.toString();
	}

	public static String getStandingOrderMainPage() {
		StringBuilder uri = new StringBuilder();
		uri.append(STANDING_ORDER_MAIN_PAGE);
		return uri.toString();
	}

	/**
	 * Provides link to line item modification page.
	 * Example:
	 * 
	 * http://dev.freshdirect.com/quickshop/ccl_item_modify.jsp?skuCode=VAR0072429
	 *  &catId=hmr_fresh
	 *  &productId=var_ds_gt_risotto
	 *  &action=CCL:ItemManipulate
	 *  &ccListId=2153109849
	 *  &lineId=2197096194
	 *  &quantity=2
	 *  &salesUnit=EA
	 *  
	 * @param servletRoot	e.g. "/quickshop/"
	 * @param qc			QuickShop instance
	 * @param orderId
	 * @param orderLine
	 * @param ccListIdStr	Customer list ID
	 * @param hasDeptId
	 * @param qsDeptId
	 * @return
	 */
	public static String getQuickShopItemModifyPage(String servletRoot, QuickCart qc, String orderId, FDProductSelectionI orderLine, String ccListIdStr, boolean hasDeptId, String qsDeptId) {
	    final ProductModel productNode = orderLine.lookupProduct();
	    
	    
	    StringBuilder qsLink = new StringBuilder();

	    String cartType = qc.getProductType();
	    final boolean isCCLorSO = QuickCart.PRODUCT_TYPE_CCL.equals(cartType) || QuickCart.PRODUCT_TYPE_SO.equals(cartType);
	    
		if (isCCLorSO) {
		   qsLink.append(servletRoot);
		   qsLink.append("/ccl_item_modify.jsp");
		} else  {
		   qsLink.append(servletRoot);
		   qsLink.append("/item_modify.jsp");
		}

		qsLink.append("?skuCode=").append( orderLine.getSkuCode() );
		qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "catId=").append( productNode.getParentNode() );
		qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "productId=").append( productNode );

		// specify action - needed for FDShoppingCartController
		if (isCCLorSO) {
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "action=CCL:ItemManipulate");
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "qcType="+cartType);
		}
			
		if (orderId!=null)
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "orderId=").append( orderId );
		
		if (ccListIdStr != null)
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP).append(CclUtils.CC_LIST_ID).append('=').append(ccListIdStr);

		if (hasDeptId)
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "qsDeptId=" + qsDeptId);

		// list configuration
		for (Iterator<Map.Entry<String,String>> i=orderLine.getOptions().entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<String,String> entry = i.next();
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP).append( entry.getKey() ).append("=").append( entry.getValue() );
		}

		if (orderLine.getRecipeSourceId() != null)
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "recipeId=").append( orderLine.getRecipeSourceId() );

		if (isCCLorSO)
			qsLink.append(ProductDisplayUtil.URL_PARAM_SEP + "lineId=").append(orderLine.getCustomerListLineId());

		
		return qsLink.toString();
	}
	
	
	
	public static final String WINE_PARAMS[] = {"domainName", "domainValue",
		QueryParameter.WINE_FILTER, QueryParameter.WINE_FILTER_CLICKED,
		QueryParameter.WINE_SORT_BY, QueryParameter.WINE_VIEW, QueryParameter.WINE_PAGE_SIZE, QueryParameter.WINE_PAGE_NO};

	public static String getWineProductURI(ProductModel productNode, String trackingCode, Map<String,String[]> params) {
		
		StringBuilder uri = new StringBuilder();
		
		appendProduct(uri, productNode);

		/* append wine params */

	    if (trackingCode != null) {
			uri.append(ProductDisplayUtil.URL_PARAM_SEP).append("trk=").append(trackingCode);
	    }

		appendWineParamsToURI(uri, params);
	    
		return uri.toString();
	}


	public static CharSequence appendWineParamsToURI(CharSequence uri, Map<String,String[]> params) {
		if (uri == null || params == null)
			return uri;

		final boolean isAppendable = uri instanceof Appendable;
		
		Appendable buf = isAppendable ? (Appendable) uri : new StringBuilder(uri);
		
	    final String _trk = params.get("trk") != null ? params.get("trk")[0] : null;
	    if (_trk != null) {
	    	try {
				buf.append(ProductDisplayUtil.URL_PARAM_SEP).append("_trk=").append(_trk);
			} catch (IOException e) {
			}
	    }
	    final String _catId = params.get("catId") != null ? params.get("catId")[0] : null;
	    if (_catId != null) {
	    	try {
				buf.append(ProductDisplayUtil.URL_PARAM_SEP).append("_catId=").append(_catId);
			} catch (IOException e) {
			}
	    }
	    final String _deptId = params.get("deptId") != null ? params.get("deptId")[0] : null;
	    if (_deptId != null) {
	    	try {
				buf.append(ProductDisplayUtil.URL_PARAM_SEP).append("_deptId=").append(_deptId);
			} catch (IOException e) {
			}
	    }

	    // append wine params
	    for (String p : WINE_PARAMS) {
	    	if (params.get(p) != null && params.get(p).length > 0) {
	    		try {
					buf.append(ProductDisplayUtil.URL_PARAM_SEP).append(p).append("=").append(params.get(p)[0]);
				} catch (IOException e) {
				}
	    	}
	    }

	    return isAppendable ? uri : buf.toString();
	}


	
	/**
	 * Use &amps; in HTML links not directly like sendRedirect on server side, etc.
	 * This simple utility methods helps you by converting and-amp-semicolon entities to single amps.
	 * 
	 * For more info see http://htmlhelp.com/tools/validator/problems.html#amp
	 *  
	 * @param urlContainingEscapedAmpersands URL full of &amp; separators
	 * @return Converted string now good to use on server side.
	 */
	public static String toDirectURL(String urlContainingEscapedAmpersands) {
		if (urlContainingEscapedAmpersands != null && urlContainingEscapedAmpersands.length() > 0) {
			return urlContainingEscapedAmpersands.replace("&amp;", "&");
		}
		return urlContainingEscapedAmpersands;
	}
}
