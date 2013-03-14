package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;

public class BvHelperTag extends SimpleTagSupport{
	
	private ProductModel product;
	
	// Bazaarvoice department exclude list
	public static List<String> BV_FREE_DEPTS;// = Arrays.asList( "veg", "fru", "sea", "mea", "usq" );
	
	@Override
	public void doTag() throws JspException, IOException {
		
		BV_FREE_DEPTS = FDStoreProperties.getBazaarvoiceExcludedDepts();
		
		boolean showReviews = true;
		DepartmentModel dept = null;
		
		if ( product != null ) {
			// First check primary home's dept
			CategoryModel prHome = product.getPrimaryHome();
			if ( prHome != null )
				dept = prHome.getDepartment();
			
			// Fall back to product's dept if there is no primary home set
			if ( dept == null )
				dept = product.getDepartment();
		}
		
		if ( dept == null || BV_FREE_DEPTS.contains( dept.getContentKey().getId() ) ) {
			// Disable if dept is missing or is in the exclude list
			showReviews = false;
		}
		
		((PageContext)getJspContext()).setAttribute("showReviews", showReviews);
	}

	public ProductModel getProd() {
		return product;
	}

	public void setProd(ProductModel prod) {
		this.product = prod;
	}

}
