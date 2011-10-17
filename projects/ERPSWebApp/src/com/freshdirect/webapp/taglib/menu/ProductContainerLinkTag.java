package com.freshdirect.webapp.taglib.menu;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.webapp.util.FDURLUtil;

/**
 * Creates a link for the given product container.
 * 
 * @author tgelesz
 * 
**/
public class ProductContainerLinkTag extends SimpleTagSupport {

	private ProductContainer productContainer;
	private String cssClass;
	private String trackingCode;

	@Override
	public void doTag() throws JspException, IOException {
		String url = null;
		if (productContainer != null) {
			if (productContainer != null) {
				String id = productContainer.getContentKey().getId();

				if (productContainer instanceof DepartmentModel) {
					url = FDURLUtil.getDepartmentURI(id, trackingCode);
				} else if (productContainer instanceof CategoryModel) {
					url = FDURLUtil.getCategoryURI(id, trackingCode);
				}
			}
		}

		JspWriter out = getJspContext().getOut();
		try {
			out.print("<a href=\"" + url + "\"");
			if (cssClass != null) {
				out.print(" class=\"" + cssClass + "\"");
			}
			out.print(">");
			getJspBody().invoke(null); 			
			out.print("</a>");

		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	public void setProductContainer(ProductContainer productContainer) {
		this.productContainer = productContainer;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

}