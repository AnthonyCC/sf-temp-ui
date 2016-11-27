package com.freshdirect.webapp.taglib.fdstore.display;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class ProductAvailabilityTag extends AbstractGetterTag<ProductAvailabilityTag> {
	private static final long serialVersionUID = -8724903431640460713L;

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return ProductAvailabilityTag.class.getName();
		}
	}

	ProductModel product;

	boolean fullyAvailable;

	boolean discontinued;

	boolean outOfSeason;

	boolean temporaryUnavailable;

	@Override
	protected ProductAvailabilityTag getResult() throws Exception {
		fullyAvailable = product.isFullyAvailable();

		discontinued = product.isDiscontinued();

		outOfSeason = product.isOutOfSeason();

		temporaryUnavailable = product.isTempUnavailable();

		return this;
	}
	
	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public ProductModel getProduct() {
		return product;
	}

	public boolean isFullyAvailable() {
		return fullyAvailable;
	}

	public boolean isDiscontinued() {
		return discontinued;
	}

	public boolean isTemporaryUnavailable() {
		return temporaryUnavailable;
	}

	public boolean isOutOfSeason() {
		return outOfSeason;
	}
}
