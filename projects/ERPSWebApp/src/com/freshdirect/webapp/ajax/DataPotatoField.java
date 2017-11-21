package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.EnumTemplateType;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.BrowsePopulator;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.cart.data.CartConfirmData;
import com.freshdirect.webapp.ajax.product.ProductAnnotationDataPopulator;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.ProductExtraDataPopulator;
import com.freshdirect.webapp.ajax.product.ProductImageDataPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductAnnotationData;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductImageData;
import com.freshdirect.webapp.globalnav.data.GlobalNavData;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;

public class DataPotatoField {
	
    private static final String CARTLINE_MODIFY_URL	= "/product_modify.jsp?cartLine=%s";
    private static final String PDP_MODIFY_URL = "/pdp.jsp?productId=%s&catId=%s&modify=true";
	
	private static final Logger LOG = LoggerFactory.getInstance( DataPotatoField.class );

	public static Map<String,?> digCartConfirm( FDUserI user, String cartLineId ) {	
		try {
			
			// Get cart & cartline
			FDCartI cart = user.getShoppingCart();
			FDCartLineI cartLine = null;
			for( FDCartLineI cl : cart.getOrderLines() ) {
				if ( cartLineId.equals( Integer.toString( cl.getRandomId() ) ) ) {	//FIXME: randomId vs. cartLineId ???
					cartLine = cl;
					break;
				}
			}
			if ( cartLine == null ) {
				LOG.error( "No cartline with such ID : "+cartLineId );
				return null;
			}
			
			ProductData productData = ProductDetailPopulator.createProductData( user, cartLine, true );
			
			CartConfirmData confirmData = new CartConfirmData();
			confirmData.setSubTotal( JspMethods.formatPrice( cart.getSubTotal() ) );
			confirmData.setLineTotal( JspMethods.formatPrice( cartLine.getPrice() ) );
			confirmData.setBackUrl( FDURLUtil.getCategoryURI( getBackUrl(cartLine.getCategoryName()), "confcatlink" ) );
			confirmData.setEditUrl(populateEditUrl(cartLineId, productData));
			
			confirmData.setCartLine( SoyTemplateEngine.convertToMap( productData ) );
			
			return SoyTemplateEngine.convertToMap( confirmData );
			
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		
		return null;
	}

	/** replicates logic from i_cart_confirm_bottom.jspf **/
	private static String getBackUrl(String parentCategoryId){
		if (parentCategoryId!=null){
			ContentNodeModel topCategory = ContentFactory.getInstance().getContentNode(parentCategoryId);
	
			if (topCategory != null && topCategory instanceof CategoryModel) {
				//wine should not go up to top level category
				if (! EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(((CategoryModel)topCategory).getTemplateType(1)))) {
				
					// find top category of product
					while (topCategory != null && topCategory.getParentNode() instanceof CategoryModel) {
						topCategory = topCategory.getParentNode();
					}
				}
				
				return topCategory.getContentName();
			}
		}
		return null;
	}
	
    private static String populateEditUrl(String cartLineId, ProductData productData) {
        String editUrl;
        if (productData.getHolidayMealBundleContainer().getMealIncludeDatas() == null) {
            editUrl = String.format(CARTLINE_MODIFY_URL, cartLineId);
        } else {
            editUrl = String.format(PDP_MODIFY_URL, productData.getProductId(), productData.getCatId());
        }
        return editUrl;
    }
	
	public static Map<String, ?> digProduct( FDUserI user, ProductModel product ) {
		return digProduct(user, product, null);
	}
	
	public static Map<String, ?> digProduct( FDUserI user, ProductModel product, String variantId ) {	
		try {
			
			// first get a ProductData for product level attributes
			ProductData productData = ProductDetailPopulator.createProductDataForCarousel( user, product );
			if (variantId != null) {
				productData.setVariantId(variantId);
				productData.setProductPageUrl( FDURLUtil.getNewProductURI(product, variantId) );
			}
			
			// convert and return
			return SoyTemplateEngine.convertToMap( productData );
			
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		
		return null;
	}
	
	public static Map<String, ?> digProduct( FDUserI user, String categoryId, String productId ) {	
		try {
			
			// first get a ProductData for product level attributes
			ProductData productData = ProductDetailPopulator.createProductData( user, productId, categoryId );
						
			// convert and return
			return SoyTemplateEngine.convertToMap( productData );
			
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		
		return null;
	}
	
	private static final String PRODUCT_LIST_KEY = "productList";

	public static Map<String, ?> digProductListFromKeys( FDUserI user, List<ContentKey> keys ) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>(1);
		List<Map<String,?>> result = new ArrayList<Map<String,?>>( keys != null ? keys.size() : 0 );
		resultMap.put( PRODUCT_LIST_KEY, result );
		
		if ( keys != null ) {
			for ( ContentKey key : keys ) {
				if ( key.getType().equals( ContentType.Product ) ) {
					String catId = null;
					Map<String, ?> productData = digProduct( user, catId, key.getId() );
					if ( productData != null ) {
						result.add( productData );
					}
				} else {
					// TODO : what about other product types? configuredproduct, etc?
				}
			}
		}
		
		// convert and return
		return SoyTemplateEngine.convertToMap( resultMap );
	}
	
	public static Map<String, ?> digProductListFromModels( FDUserI user, List<ProductModel> products ) {
		return digProductListFromModels(user, products, null);
	}

	public static Map<String, ?> digProductListFromModels( FDUserI user, List<ProductModel> products, String variantId ) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>(1);
		List<Map<String,?>> result = new ArrayList<Map<String,?>>( products != null ? products.size() : 0 );
		resultMap.put( PRODUCT_LIST_KEY, result );
		
		if ( products != null ) {
			for ( ProductModel product : products ) {
				Map<String, ?> productData = digProduct( user, product, variantId );
				if ( productData != null ) {
					result.add( productData );
				}
			}
		}
		
		// convert and return
		return SoyTemplateEngine.convertToMap( resultMap );
	}
	
	public static Map<String, ?> digProductListFromData( FDUserI user, List<ProductData> productDatas ) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>(1);
		List<Map<String,?>> result = new ArrayList<Map<String,?>>( productDatas != null ? productDatas.size() : 0 );
		resultMap.put( PRODUCT_LIST_KEY, result );
		
		if ( productDatas != null ) {
			for ( ProductData productData : productDatas ) {
				Map<String, ?> productDataMap = SoyTemplateEngine.convertToMap( productData );
				if ( productData != null ) {
					result.add( productDataMap );
				}
			}
		}
		
		// convert and return
		return SoyTemplateEngine.convertToMap( resultMap );
	}
	


	/**
	 * Collect product related extra info. Mostly used by PDP accordion widgets.
	 * @return
	 */
	public static Map<String, ?> digProductExtraData( FDUserI user, String categoryId, String productId, ServletContext context, String grpId, String grpVersion ) {
		// first get a ProductData for product level attributes
		try {
			ProductExtraData extraData = ProductExtraDataPopulator.createExtraData( user, productId, categoryId, grpId, grpVersion );
			
			// convert and return
			return SoyTemplateEngine.convertToMap( extraData );
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		return null;
	}
	
	public static Map<String,?> digBrowse(BrowseData browseData) {
		return SoyTemplateEngine.convertToMap(browseData);
	}
	
	public static Map<String,?> digBrowse(CmsFilteringFlowResult result) {
		try {
			return digBrowse(BrowsePopulator.createBrowseData(result));

		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get browse info.", e );
		}
		return null;
	}

	public static Map<String,?> digGlobalNav(GlobalNavData globalNavData) {
		return SoyTemplateEngine.convertToMap(globalNavData);

	}

	/**
	 * Collect product related extra info. Mostly used by PDP accordion widgets.
	 * @return
	 */
	public static Map<String, ?> digProductExtraData( FDUserI user, ProductModel product, ServletContext context, String grpId, String grpVersion ) {
		// first get a ProductData for product level attributes
		try {
			ProductExtraData extraData = ProductExtraDataPopulator.createExtraData( user, product, grpId, grpVersion, true );
			
			// convert and return
			return SoyTemplateEngine.convertToMap( extraData );
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		return null;
	}
	
	   /**
     * Collect light product related extra info. Mostly used by PDP accordion widgets.
     * @return
     */
    public static Map<String, ?> digProductLightExtraData(FDUserI user, ProductModel product) {
        // first get a LightProductData for product level attributes
        try {
            ProductExtraData extraData = ProductExtraDataPopulator.createLightExtraData(user, product);
            
            // convert and return
            return SoyTemplateEngine.convertToMap( extraData );
        } catch ( HttpErrorResponse e ) {
            LOG.error( "Failed to get product info.", e );
        }
        return null;
    }

	
	/**
	 * Collect product images.
	 * @return
	 */
	public static Map<String, ?> digProductImages( FDUserI user, String categoryId, String productId ) {
		// first get a ProductData for product level attributes
		try {
			ProductImageData imageData = ProductImageDataPopulator.createImageData( user, productId, categoryId );
			
			// convert and return
			return SoyTemplateEngine.convertToMap( imageData );
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		return null;
	}



	/**
	 * Collect product annotation data.
	 * @return
	 */
	public static Map<String, ?> digProductAnnotations( FDUserI user, String categoryId, String productId ) {
		// first get a ProductData for product level attributes
		if (!FDStoreProperties.isAnnotationMode())
			return null;

		try {
			ProductAnnotationData annotationData = ProductAnnotationDataPopulator.createAnnotationData( user, productId, categoryId );
			
			// convert and return
			return SoyTemplateEngine.convertToMap( annotationData );
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		return null;
	}




	
	public static Map<String, ?> digProductLight( FDUserI user, ProductModel product ) {	
		try {
			
			// first get a ProductData for product level attributes
			ProductData productData = ProductDetailPopulator.createProductDataLight( user, product );
						
			// convert and return
			return SoyTemplateEngine.convertToMap( productData );
			
		} catch ( FDResourceException e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to get product info.", e );
		} catch ( FDSkuNotFoundException e ) {
			LOG.error( "Failed to get product info.", e );
		}
		
		return null;
	}
}
