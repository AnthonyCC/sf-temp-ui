package com.freshdirect.webapp.taglib.fdstore;


import javax.servlet.http.HttpServletRequest;

import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.fdstore.customer.QuickCart;

import com.freshdirect.framework.util.NVL;
import com.freshdirect.fdstore.lists.CclUtils;

public class BackToListGetterTag extends AbstractGetterTag implements SessionName {

	private QuickCart quickCart;
	
	
	public void setQuickCart(QuickCart quickCart) {
		this.quickCart = quickCart;
	}
	
	private String deptId;
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	private String servletRoot = null;
	public void setServletRoot(String servletRoot) {
		this.servletRoot = servletRoot;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6841289216274417198L;

	
	/** Infer the "back to list" link from the quick cart.
	 * @return back to list link as a string
	 */
	protected Object getResult() throws Exception {
		
		
		String root = servletRoot;
		if (root == null) {
			String servletPath = ((HttpServletRequest)pageContext.getRequest()).getServletPath();
			root = servletPath.substring(0,servletPath.lastIndexOf('/'));
		}
				
		StringBuffer backToList = new StringBuffer();
		
		if (QuickCart.PRODUCT_TYPE_STARTER_LIST.equals(quickCart.getProductType())) {
			backToList
				.append(root)
				.append("/starter_list.jsp?")
				.append(CclUtils.STARTER_LIST_ID)
				.append('=')
				.append(quickCart.getOrderId());
		} else if (QuickCart.PRODUCT_TYPE_PRD.equals(quickCart.getProductType())) {
			if (quickCart.isEveryItemEverOrdered()) {
				backToList
					.append(root)
					.append("/every_item.jsp");
				if (NVL.apply(deptId,"").length() > 0) backToList.append("?qsDeptId=").append(deptId);	
			} else {
				backToList
					.append(root)
					.append("/shop_from_order.jsp")
					.append("?orderId=")
					.append(quickCart.getOrderId());
			}
				
		} else if (QuickCart.PRODUCT_TYPE_CCL.equals(quickCart.getProductType())) {
			backToList
				.append(root)
				.append("/ccl_item_modify.jsp?")
				.append(CclUtils.CC_LIST_ID)
				.append(quickCart.getOrderId());
		}

		return backToList.toString();
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.lang.String";
		}
	}

}
