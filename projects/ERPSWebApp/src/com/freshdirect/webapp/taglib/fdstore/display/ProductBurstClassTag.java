package com.freshdirect.webapp.taglib.fdstore.display;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductBurstClassTag extends AbstractGetterTag<String> {
	private static final long serialVersionUID = 1052411080367169416L;

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return String.class.getName();
		}
	}

	private ProductModel product;

	private PriceCalculator priceCalculator;
	
	private boolean hideFave;
	
	private boolean hideDeal;
	
	private boolean hideNewAndBack;
	
	private boolean useRegularDealOnly;

	@Override
	protected String getResult() throws Exception {
		return getContent((FDUserI) pageContext.getSession().getAttribute(SessionName.USER));
	}

	public String getContent(FDUserI user) throws Exception {
		pageContext.setAttribute("burstStyleString", ""); //reset value in case last product already set it on the page
		
		if (user == null) {
			return "";
		}

		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return "";

		if (product == null) {
			if (priceCalculator == null) {
				throw new JspException("'priceCalculator' or 'product' is mandatory!");
			}
			product = priceCalculator.getProductModel();
		} else {
			priceCalculator = product.getPriceCalculator();
		}


		String className = null;

		if (!hideFave && DYFUtil.isFavorite(product, user)) {
			className = "burst-fave";
		} else {
			// calculate deal
			int deal = useRegularDealOnly ? priceCalculator.getDealPercentage() : priceCalculator.getHighestDealPercentage();
			if (!hideDeal && deal > 0) {
				className = "burst-deal-" + deal;
				pageContext.setAttribute("burstStyleString", "background-image:url(/media_stat/images/deals/brst_sm_"+deal+".png)");
			} else if (!hideNewAndBack && product.isBackInStock()) {
				className = "burst-back";
			} else if (!hideNewAndBack && product.isNew()) {
				className = "burst-new";
			}
		}

		if (className != null)
			return className;
		else
			return "";
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public PriceCalculator getPriceCalculator() {
		return priceCalculator;
	}

	public void setPriceCalculator(PriceCalculator priceCalculator) {
		this.priceCalculator = priceCalculator;
	}

	public boolean isHideFave() {
		return hideFave;
	}

	public void setHideFave(boolean hideFave) {
		this.hideFave = hideFave;
	}

	public boolean isHideDeal() {
		return hideDeal;
	}

	public void setHideDeal(boolean hideDeal) {
		this.hideDeal = hideDeal;
	}

	public boolean isHideNewAndBack() {
		return hideNewAndBack;
	}

	public void setHideNewAndBack(boolean hideNewAndBack) {
		this.hideNewAndBack = hideNewAndBack;
	}

	public boolean isUseRegularDealOnly() {
		return useRegularDealOnly;
	}

	public void setUseRegularDealOnly(boolean useRegularDealOnly) {
		this.useRegularDealOnly = useRegularDealOnly;
	}
}
