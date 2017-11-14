package com.freshdirect.storeapi.content;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;

public class SkuReference implements ProductReference, Serializable {
	private static final long serialVersionUID = 3175504740828306803L;

	String skuCode;

	public SkuReference(String skuCode) {
		this.skuCode = skuCode;
	}

	public SkuReference(FDSku sku) {
		this.skuCode = sku.getSkuCode();
	}


	public String getSkuCode() {
		return skuCode;
	}

	@Override
	public ProductModel lookupProductModel() {
	    // FIXME : resolve reference
		ProductModel pm;
		try {
			pm = ContentFactory.getInstance().getProduct(this.skuCode);
		} catch (FDSkuNotFoundException e) {
			pm = null;
		}
		return pm;
	}


	@Override
	public String getCategoryId() {
		ProductModel prd = lookupProductModel();
		return prd != null ? prd.getCategory().getContentKey().id : null;
	}

	@Override
	public String getProductId() {
		ProductModel prd = lookupProductModel();
		return prd != null ? prd.getContentKey().id : null;
	}

	@Override
	public ContentKey getContentKey() {
		ProductModel prd = lookupProductModel();
		return prd != null ? prd.getContentKey() : null;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skuCode == null) ? 0 : skuCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkuReference other = (SkuReference) obj;
		if (skuCode == null) {
			if (other.skuCode != null)
				return false;
		} else if (!skuCode.equals(other.skuCode))
			return false;
		return true;
	}


}
