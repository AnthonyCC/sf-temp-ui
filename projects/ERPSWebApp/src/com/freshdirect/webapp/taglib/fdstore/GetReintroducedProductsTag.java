package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
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
		Collection prods = ContentFactory.getInstance().getReintroducedProducts(this.days, this.department);
		if (prods != null) {
			List ret = new ArrayList(prods);
			Collections.sort(ret, ProductModel.DEPTFULL_COMPARATOR);
			return ret;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
