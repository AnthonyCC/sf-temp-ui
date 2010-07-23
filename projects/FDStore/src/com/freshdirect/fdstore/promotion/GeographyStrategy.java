package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GeographyStrategy implements PromotionStrategyI {

	private final List geographies = new ArrayList();

	private final static Comparator GEO_DATE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			PromotionGeography g1 = (PromotionGeography) o1;
			PromotionGeography g2 = (PromotionGeography) o2;
			return g1.getStartDate().compareTo(g2.getStartDate());
		}
	};

	public void addGeography(PromotionGeography geo) {
		this.geographies.add(geo);
		Collections.sort(this.geographies, GEO_DATE_COMPARATOR);
	}

	public List getGeographies() {
		return Collections.unmodifiableList(this.geographies);
	}

	protected PromotionGeography getGeography(Date date) {
		PromotionGeography match = null;
		for (Iterator i = this.geographies.iterator(); i.hasNext();) {
			PromotionGeography g = (PromotionGeography) i.next();
			if (date.before(g.getStartDate())) {
				break;
			}
			match = g;
		}
		return match;
	}

	public int evaluate(String promotionCode, PromotionContextI context) {

		PromotionGeography g = this.getGeography(new Date());
		if (g == null || !isAllowedGeography(context, g)) {
			return DENY;
		}

		return ALLOW;
	}

	
	private boolean isAllowedGeography(PromotionContextI context, PromotionGeography geo) {

		EnumOrderType orderType = context.getOrderType();
		if (EnumOrderType.HOME.equals(orderType)) {
			return geo.isAllowedZipCode(context.getZipCode());

		} else if (EnumOrderType.DEPOT.equals(orderType) || EnumOrderType.PICKUP.equals(orderType)) {
			return geo.isAllowedDepotCode(context.getDepotCode());
		}

		// not deliverable
		return false;
	}


	public int evaluate(String promotionCode, EnumOrderType orderType, String zipCode, String depotCode) {

		PromotionGeography g = this.getGeography(new Date());
		if (g == null || !isAllowedGeography(orderType, g,zipCode, depotCode)) {
			return DENY;
		}

		return ALLOW;
	}
	private boolean isAllowedGeography(EnumOrderType orderType, PromotionGeography geo, String zipCode, String depotCode) {

		
		if (EnumOrderType.HOME.equals(orderType)) {
			return geo.isAllowedZipCode(zipCode);

		} else if (EnumOrderType.DEPOT.equals(orderType) || EnumOrderType.PICKUP.equals(orderType)) {
			return geo.isAllowedDepotCode(depotCode);
		}

		// not deliverable
		return false;
	}
	public int getPrecedence() {
		return 300;
	}

	public String toString() {
		return "GeographyStrategy[" + this.geographies + "]";
	}

}
