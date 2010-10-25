package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class WineOtherRatingsTag extends AbstractGetterTag<List<String>> {
	private static final long serialVersionUID = -111983912293730255L;

	private ProductModel product;
	
	private boolean small;

	@Override
	protected List<String> getResult() throws Exception {
		DomainValue[] values = { product.getWineRatingValue1(), product.getWineRatingValue2(), product.getWineRatingValue2() };

		String basePath = "/media/editorial/win_usq/icons/rating" + (small ? "_small" : "") + "/";
		String extension = ".gif";
		int width = small ? 42 : 25;
		int height = small ? 15 : 43;

		List<String> paths = new ArrayList<String>();
		for (DomainValue value : values) {
			if (value == null)
				continue;
			String rating = value.getValue();
			try {
				int i = Integer.parseInt(rating);
				if (i == 0)
					continue;
			} catch (NumberFormatException e) {
				continue;
			}
			String str = value.getDomain().getName();
			String token = str.substring(str.lastIndexOf("_") + 1);
			String rater = token;
			String imagePath = basePath + value.getContentKey().getId() + extension;
			String imageTag = "<img src=\"" + imagePath + "\" width=\"" + width + "\" height=\"" + height + "\" border=\"0\" alt=\"" + rater + rating
					+ "\">";
			paths.add(imageTag);
			if (paths.size() == 2)
				break;
		}

		return paths.size() > 0 ? paths : null;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public boolean isSmall() {
		return small;
	}

	public void setSmall(boolean small) {
		this.small = small;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return List.class.getName() + "<" + String.class.getName() + ">";
		}
	}
}
