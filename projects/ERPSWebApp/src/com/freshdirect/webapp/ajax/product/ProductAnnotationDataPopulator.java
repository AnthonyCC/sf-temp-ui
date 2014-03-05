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
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductAnnotationData;

public class ProductAnnotationDataPopulator {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance( ProductAnnotationDataPopulator.class );

	public static ProductAnnotationData createAnnotationData( FDUserI user, ProductModel product ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		// Create response data object
		ProductAnnotationData data = new ProductAnnotationData();
		
		// First populate product-level data
		populateData( data, user, product );
		
		return data;
	}


	public static ProductAnnotationData createAnnotationData( FDUserI user, String productId, String categoryId ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		
		return createAnnotationData( user, product );
	}


	

	private static void populateData(ProductAnnotationData data, FDUserI user, ProductModel productNode) throws FDSkuNotFoundException, FDResourceException {
		
		final SkuModel defaultSku = PopulatorUtil.getDefSku( productNode );
		if (defaultSku == null) {
			throw new FDSkuNotFoundException("No available SKU found for product " + productNode.getContentName());
		}
		
		final String skuCode = defaultSku.getSkuCode();
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
		vdata.put("availability", productInfo.getAvailabilityStatus().getShortDescription());
		
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
		
		data.setErpsyLink( FDStoreProperties.getAnnotationErpsy() + "/attribute/material/material_search.jsp?searchterm=" + skuCode + "&amp;searchtype=WEBID");
	}
}
