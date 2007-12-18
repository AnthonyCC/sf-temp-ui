package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetReintroducedProductsTag extends AbstractGetterTag {

	private int days = 14;
	private String department = null;

	public void setDays(int days) {
		this.days = days;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	protected Object getResult() throws Exception {
		List prods = ContentFactory.getInstance().getReintroducedProducts(this.days, this.department);
		Collections.sort(prods, ProductModel.DEPTFULL_COMPARATOR);
		return prods;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
