package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetNewProductsTag extends AbstractGetterTag {

	private int days = 14;
	private String department = null;

	public void setDays(int days) {
		this.days = days;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	/** Orders products by glance name */
	private final static Comparator GLANCE_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			ProductModel p1 = (ProductModel) o1;
			ProductModel p2 = (ProductModel) o2;
			return p1.getGlanceName().compareTo(p2.getGlanceName());
		}

	};

	protected Object getResult() throws Exception {
		List prods = ContentFactory.getInstance().getNewProducts(this.days, this.department);
		Collections.sort(prods, prods.size() > 10 ? ProductModel.DEPTFULL_COMPARATOR : GLANCE_COMPARATOR);
		return prods;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
