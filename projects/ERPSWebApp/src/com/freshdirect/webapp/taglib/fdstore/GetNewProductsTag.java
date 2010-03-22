package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetNewProductsTag extends AbstractGetterTag {

	private static Category LOGGER = LoggerFactory.getInstance( GetNewProductsTag.class );

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
			//null check
			String p1GlanceName = "";
			String p2GlanceName = "";
			
			p1GlanceName = (p1.getGlanceName() == null) ? "" : p1.getGlanceName();
			p2GlanceName = (p2.getGlanceName() == null) ? "" : p2.getGlanceName();
			
			return p1GlanceName.compareTo(p2GlanceName);
		}

	};

	protected Object getResult() throws Exception {
		List<ProductModel> prods = new ArrayList<ProductModel>(ContentFactory.getInstance().getNewProducts(days).keySet());
		prods = ContentFactory.filterProductsByDeptartment(prods, department);
		Collections.sort(prods, prods.size() > 10 ? ProductModel.DEPTFULL_COMPARATOR : GLANCE_COMPARATOR);
		return prods;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
