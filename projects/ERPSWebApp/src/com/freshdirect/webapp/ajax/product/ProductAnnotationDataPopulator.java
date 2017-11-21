package com.freshdirect.webapp.ajax.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductAnnotationData;

public class ProductAnnotationDataPopulator {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance( ProductAnnotationDataPopulator.class );

	public static ProductAnnotationData createAnnotationData( FDUserI user, ProductModel product, String productId, String categoryId ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		// Create response data object
		ProductAnnotationData data = new ProductAnnotationData();
		
		// First populate product-level data
		populateData( data, user, product, productId, categoryId );
		
		return data;
	}


	public static ProductAnnotationData createAnnotationData( FDUserI user, String productId, String categoryId ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		
		return createAnnotationData( user, product, productId, categoryId );
	}


	

	private static void populateData(ProductAnnotationData data, FDUserI user, ProductModel productNode, String productId, String categoryId) throws FDSkuNotFoundException, FDResourceException {
		
		final SkuModel defaultSku = PopulatorUtil.getDefSku( productNode );
		if (defaultSku == null) {
			throw new FDSkuNotFoundException("No available SKU found for product " + productNode.getContentName());
		}
		
		final String skuCode = defaultSku.getSkuCode();
		final String salesOrg=user.getUserContext().getPricingContext().getZoneInfo().getSalesOrg();
		final String distrChannel=user.getUserContext().getPricingContext().getZoneInfo().getDistributionChanel();
		FDProductInfo productInfo	= FDCachedFactory.getProductInfo(skuCode);
		FDProduct fdprd				= null;
		
		try {
			fdprd				= FDCachedFactory.getProduct(productInfo);	
		} catch (FDResourceException exc) {
		} catch (FDSkuNotFoundException exc) {
		}

		// ---- //
		
		Map<String,String> vdata = new HashMap<String,String>();
		
		vdata.put("skuCode", skuCode);
		vdata.put("material", fdprd!=null ?fdprd.getMaterial().getMaterialNumber().substring(9) : "-");
		if (fdprd!=null) {
			//add additional links
			
			//erpsy material
			vdata.put("materialLink", FDStoreProperties.getAnnotationErpsy() + "/attribute/material/material_view.jsp?sapId=" +fdprd.getMaterial().getMaterialNumber());
			
			//info for cms link (url in soy, there's no prop like erpsy)
			if (categoryId != null && !"".equals(categoryId) && productId != null && !"".equals(productId)) {
				vdata.put("categoryId", categoryId);
				vdata.put("productId", productId);
			}
		}
		
		try {
			vdata.put("availability", productInfo.getAvailabilityStatus(salesOrg,distrChannel).getShortDescription());
		} catch (Exception exc) {
			LOG.error("Failed to populate availability info of " + productInfo.getSkuCode(), exc);

			vdata.put("availability", "(UNKNOWN)");
		}
		
		List<DomainValue> varMtx = defaultSku.getVariationMatrix();
		if (varMtx != null && varMtx.size() > 0) {
			StringBuilder vbuf = new StringBuilder();

			// vbuf.append("<b>");
			for (DomainValue v : varMtx) {
				vbuf.append(v.getContentKey().getId());
				vbuf.append(" ");
			}
			// vbuf.append("</b>");

			vdata.put("variation", vbuf.toString());
		}
		
		data.setData(vdata);
		
		//changed this to BASE, set additional url target in SOY
		data.setErpsyLink( FDStoreProperties.getAnnotationErpsy());
	}
}
