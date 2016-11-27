package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class GenericPagerTag extends BodyTagSupportEx {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199005705248456158L;
	
	private String itemsId;
	private FilteringNavigator nav;
	List items;
	
	@Override
	public int doStartTag() throws JspException {
		
		int pageSize = nav.getPageSize();
		int pageOffset = nav.getPageOffset();

		List pageProducts = new ArrayList(pageSize <= 0 ? items.size() : pageSize);
		int noOfPagedProducts = items.size();
		int pageCount = pageSize == 0 ? 1 : noOfPagedProducts / pageSize;
		if (pageSize != 0 && noOfPagedProducts % pageSize > 0) {
			pageCount++;

		}
		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			if (i >= items.size()) {
				break;
			}
			pageProducts.add(items.get(i));
		}

		pageContext.setAttribute(itemsId, pageProducts);

		return EVAL_BODY_INCLUDE;
	}
	
	
	public String getItemsId() {
		return itemsId;
	}
	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}
	public FilteringNavigator getNav() {
		return nav;
	}
	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}
	
	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {new VariableInfo(
					data.getAttributeString("itemsId"),
					List.class.getName(),
					true,
					VariableInfo.NESTED) };
		}
	}

}
