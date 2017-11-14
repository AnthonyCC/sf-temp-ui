package com.freshdirect.storeapi.content;

import java.math.BigDecimal;

import com.freshdirect.fdstore.FDResourceException;

public abstract class AbstractRangeFilter extends AbstractProductFilter{
	
	private BigDecimal fromValue;
	private BigDecimal toValue;
	private boolean fromValueIncluded;
	private boolean toValueIncluded;
	
	public AbstractRangeFilter(BigDecimal fromValue, BigDecimal toValue, boolean fromValueIncluded, boolean toValueIncluded) {

		this.fromValue = fromValue;
		this.toValue = toValue;
		this.fromValueIncluded = fromValueIncluded;
		this.toValueIncluded = toValueIncluded;
	}
	
	protected abstract BigDecimal getProductValue(ProductModel product);
	
	public boolean applyTest(ProductModel product) throws FDResourceException {
		
		BigDecimal prodValue = getProductValue(product);
		
		if (prodValue==null){
			return false;
		
		} else {
			if (fromValue != null){
				int result = fromValue.compareTo(prodValue);

				if (fromValueIncluded && result > 0 || !fromValueIncluded && result>=0){
					return false;
				}
			}
			if (toValue != null){
				int result = toValue.compareTo(prodValue);

				if (toValueIncluded && result < 0 || !toValueIncluded && result<=0){
					return false;
				}
			}
		}
		
		return true;
	}

}
