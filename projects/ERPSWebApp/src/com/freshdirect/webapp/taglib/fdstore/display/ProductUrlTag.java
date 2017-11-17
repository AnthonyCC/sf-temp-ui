package com.freshdirect.webapp.taglib.fdstore.display;

import java.util.Map;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.FDURLUtil;

public class ProductUrlTag extends AbstractGetterTag<String> {
	private static final long serialVersionUID = -581640772480083994L;

	private ProductModel product;

	private String trackingCode;

	private boolean appendWineParams = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected String getResult() throws Exception {
		// String productURI = FDURLUtil.getProductURI(product, trackingCode);
		
		if (appendWineParams) {
			return FDURLUtil.getWineProductURI(product, trackingCode, (Map<String,String[]>) request.getParameterMap());
		} else {
			return FDURLUtil.getProductURI(product, trackingCode);
		}
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public void setAppendWineParams(boolean appendWineParams) {
		this.appendWineParams = appendWineParams;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return String.class.getName();
		}
	}
}
