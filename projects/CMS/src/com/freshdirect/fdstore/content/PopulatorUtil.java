package com.freshdirect.fdstore.content;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;

public class PopulatorUtil {
    private static final Logger LOGGER = Logger.getLogger(PopulatorUtil.class.getSimpleName());

    private static final String NODE_IS_NOT_FOUND_IN_CMS_ERROR_MESSAGE = "Node is not found in Content Management System";
    private static final String PRODUCT_IS_DISCONTINUED_IN_CMS_ERROR_MESSAGE = "Product is discontinued in Content Management System";
	
	public static final ProductModel getProduct( String productId, String categoryId ) {
		if ( categoryId == null ) {
			// get product in its primary home
			return (ProductModel)ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get( "Product" ), productId) );
		} else {
			// get product in specified category context
			return ContentFactory.getInstance().getProductByName( categoryId, productId );
		}		
	}

	public static final ProductModel getProduct( String skuCode ) throws FDSkuNotFoundException {
		if ( skuCode != null ) {
			return ContentFactory.getInstance().getProduct(skuCode);
		}		
		return null;
	}
	
	public static final SkuModel getDefSku( ProductModel product ) {
		if (product == null) {
			LOGGER.error("getDefSku(): No input product!");
			return null;
		}
		SkuModel sku = product.getDefaultSku();
		if ( sku == null ) {
			//LOGGER.debug("getDefSku(): ... fall back to default temporary unavailable sku");

			// temporary unav item?
			sku = product.getDefaultTemporaryUnavSku();
			
			if (sku == null) {
				//LOGGER.error("getDefSku(): No default SKU found for product with key " + (ck != null ? ck.getId() : "<null>") + " at all");
			}
		}
		return sku;
	}

	/**
	 * Check if a product is newly created in CMS
	 * and corresponding ERPS data is not assigned yet.
	 * 
	 * @return
	 * 
	 * @throws FDSkuNotFoundException 
	 * @throws FDResourceException 
	 */
	public static final boolean isProductIncomplete(ProductModel prd) throws FDResourceException, FDSkuNotFoundException {
	    if (null == prd.getSkus() || prd.getSkus().isEmpty()) {
			// No SKUs found. This is really bad.
			// Let the execution go and break somewhere else
			throw new FDSkuNotFoundException("Product " + prd.getContentName() + " contains NO SKUs!");
		}
		
		// now pick the first SKU
		// Theoretically there should be only one by now 
		
		SkuModel sku = PopulatorUtil.getDefSku(prd);
		if (sku == null) {
			return true;
		}

		FDProductInfo pInfo = null;
		try {
			pInfo = sku.getProductInfo();
		} catch (FDSkuNotFoundException ex) {
			return true;
		}

		return pInfo == null || pInfo.getVersion() == 0;
	}
	
	/** originally in GetPeakProduceTag.isProduce() **/
	public static boolean isShowRatings(String skuCode) {
		boolean matchFound = false;
		
		String _skuPrefixes=FDStoreProperties.getRatingsSkuPrefixes(); // grab sku prefixes that should show ratings
		if (_skuPrefixes!=null && !"".equals(_skuPrefixes)) { //if we have prefixes then check them

			StringTokenizer st=new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
			String curPrefix = ""; //holds prefix to check against
			
			while(st.hasMoreElements()) { //loop and check each prefix
				curPrefix=st.nextToken();
				if(skuCode.startsWith(curPrefix)) { //if prefix matches get product info
					matchFound=true;
                }
				
				if (matchFound) { //exit on matched sku prefix
					break;
				}
            }
        }
		return matchFound;
	}
	
    public static boolean isNodeArchived(final ContentNodeModel node) {
        ContentNodeModel department = getDepartmentModel(node);
        String departmentContentName = (department != null) ? department.getContentName() : null;
        return node == null || node.isOrphan() || "Archive".equalsIgnoreCase(departmentContentName);
    }

    public static boolean isNodeDiscontinued(final AvailabilityI node) {
        return node == null || node.isDiscontinued();
    }

    private static ContentNodeModel getDepartmentModel(final ContentNodeModel node) {
        ContentNodeModel departmentNode = null;
        if (node != null) {
            if (FDContentTypes.DEPARTMENT == node.getContentKey().getType()) {
                departmentNode = node;
            } else {
                departmentNode = getDepartmentModel(node.getParentNode());
            }
        }
        return departmentNode;
    }

    public static void isNodeNotFound(final ContentNodeModel node, String... ids) throws FDNotFoundException {
        String errorMessage = null;
        if (node == null) {
            errorMessage = getNodeNotFoundErrorMessage(NODE_IS_NOT_FOUND_IN_CMS_ERROR_MESSAGE, ids);
        } else if (PopulatorUtil.isNodeArchived(node)) {
            errorMessage = getNodeNotFoundErrorMessage(NODE_IS_NOT_FOUND_IN_CMS_ERROR_MESSAGE, ids);
        }

        if (errorMessage != null) {
            throw new FDNotFoundException(errorMessage);
        }
    }

    public static void isProductNodeNotFound(final ProductModel node, String... ids) throws FDNotFoundException {
        String errorMessage = null;

        isNodeNotFound(node, ids);

        if (isNodeDiscontinued(node)) {
            errorMessage = getNodeNotFoundErrorMessage(PRODUCT_IS_DISCONTINUED_IN_CMS_ERROR_MESSAGE, ids);
        }

        if (errorMessage != null) {
            throw new FDNotFoundException(errorMessage);
        }
    }

    private static String getNodeNotFoundErrorMessage(String template, String... ids) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(template);
        for (String id : ids) {
            errorMessage.append(", " + id);
        }
        return errorMessage.toString();
    }

    public static ProductModel getProductByName(String categoryId, String productId) throws FDNotFoundException {
        ProductModel productNode = ContentFactory.getInstance().getProductByName(categoryId, productId);
        PopulatorUtil.isProductNodeNotFound(productNode, "categoryId:" + categoryId, "productId:" + productId);
        return productNode;
    }

    public static ProductModel getProductByName(String skuCode) throws FDNotFoundException {
        ProductModel productNode = null;
        try {
            productNode = ContentFactory.getInstance().getProduct(skuCode);
        } catch (FDSkuNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        PopulatorUtil.isProductNodeNotFound(productNode, "skuCode:" + skuCode);
        return productNode;
    }

    public static ContentNodeModel getContentNode(String id) throws FDNotFoundException {
        ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(id);
        PopulatorUtil.isNodeNotFound(contentNode, "id:" + id);
        return contentNode;
    }

    public static ContentNodeModel getContentNode(ContentType type, String id) throws FDNotFoundException {
        return PopulatorUtil.getContentNodeByKey(ContentKey.getContentKey(type, id));
    }

    public static ContentNodeModel getContentNodeByKey(String key) throws FDNotFoundException {
        return PopulatorUtil.getContentNodeByKey(ContentKey.getContentKey(key));
    }

    public static ContentNodeModel getContentNodeByKey(ContentKey key) throws FDNotFoundException {
        ContentNodeModel contentNode = ContentFactory.getInstance().getContentNodeByKey(key);
        PopulatorUtil.isNodeNotFound(contentNode, "contentkey:" + key);
        return contentNode;
    }

}
