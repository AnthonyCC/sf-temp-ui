package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.WineUtil;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

/**
 * This tag is used to decide whether the current product id defines an alcoholic product
 */
public class USQProductBurstTag extends BodyTagSupportEx {

	private static final long serialVersionUID = -6698194047077787106L;

	private ProductModel product;
	private String suffix = "";
	private String forceLogo = "false";
	
	public int doStartTag() throws JspException {
		
		try {
			if ( "true".equals(forceLogo) || ContentNodeModelUtil.hasWineDepartment(product.getContentKey()) && (product.getSku(0).getProduct() != null && !"".equals(product.getSku(0).getProduct().getMaterial().getAlcoholicContent().getCode())) ) {
				if ((pageContext.getRequest().getParameter("catId") == null || !pageContext.getRequest().getParameter("catId").startsWith(WineUtil.getWineAssociateId().toLowerCase())) && 
					(!WineUtil.getWineAssociateId().toLowerCase().equals(pageContext.getRequest().getParameter("deptId")) || WineUtil.getWineAssociateId().toLowerCase().equals(pageContext.getRequest().getParameter("deptId")) && pageContext.getRequest().getParameter("genericFilter") != null) && 
					!((HttpServletRequest)pageContext.getRequest()).getServletPath().contains("wine")) {
					pageContext.getOut().append("<span id=\"burst-wine-" + suffix + "\" name=\"burst-wine\" class=\"burst-wine\"></span>");
				}
			}
		} catch (IOException e) {
			throw new JspException(e);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (FDSkuNotFoundException e) {
		  return SKIP_BODY;
		}

		
		return EVAL_BODY_INCLUDE;
	}
	
	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public String isForceLogo() {
		return forceLogo;
	}

	public void setForceLogo(String forceLogo) {
		this.forceLogo = forceLogo;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}