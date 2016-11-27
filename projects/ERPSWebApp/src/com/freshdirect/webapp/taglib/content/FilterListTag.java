package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.EnumSearchFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class FilterListTag extends BodyTagSupportEx {
	
	private static final long serialVersionUID = 8580228895820361277L;
	private static Logger LOGGER = LoggerFactory.getInstance( FilterListTag.class );
	private static final String hasSelectedVar = "hasSelected";

	private EnumSearchFilteringValue domainName;
	private int hideAfter=7;
	private Map<FilteringValue, Map<String, FilteringMenuItem>> filters;
	
		
	@Override
	public int doStartTag() throws JspException {
		List<FilteringMenuItem> result;
		
		try {
			result = getResult();
		} catch (Exception e) {
			LOGGER.warn("Exception occured in getResult", e);
			throw new JspException(e);
		}
		
		pageContext.setAttribute(id, result);
		pageContext.setAttribute(hasSelectedVar, hasSelected(result) );
		
		if( result == null ) return SKIP_BODY;	

		return EVAL_BODY_INCLUDE;		
	}

	boolean hasSelected(List<FilteringMenuItem> items) {
		if(items == null || items.isEmpty()) return false;
		
		for(FilteringMenuItem item : items) {
			if(item != null && item.isSelected()) return true;
		}
		
		return false;
	}

	public Map<FilteringValue, Map<String, FilteringMenuItem>> getFilters() {
		return filters;
	}

	public void setFilters(Map<FilteringValue, Map<String, FilteringMenuItem>> filters) {
		this.filters = filters;
	}

	public EnumSearchFilteringValue getDomainName() {
		return domainName;
	}

	public void setDomainName(EnumSearchFilteringValue domainName) {
		this.domainName = domainName;
	}
	
	public void setHideAfter(int hideAfter) {
		this.hideAfter = hideAfter;
	}
	
	public int getHideAfter() {
		return hideAfter;
	}
	
	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { 
					new VariableInfo(
							data.getAttributeString("id"),
							List.class.getName() + "<"+FilteringMenuItem.class.getName()+">",
							true, VariableInfo.NESTED),
					new VariableInfo(
							hasSelectedVar,
							"java.lang.Boolean",
							true, VariableInfo.NESTED) };
		}
	}

	protected List<FilteringMenuItem> getResult() throws Exception {
		
		Map<FilteringValue, Map<String, FilteringMenuItem>> menus = this.getFilters();
		
		// Additional safety check, return empty list if domainName not found
		Map<String, FilteringMenuItem> domainFilters = menus.get(domainName);		
		if ( domainFilters == null ) {
			return new ArrayList<FilteringMenuItem>();
		}
		
		List<FilteringMenuItem> filters = new ArrayList<FilteringMenuItem>(domainFilters.values());
		
		if( filters.isEmpty() ) return filters; 
		
		if(domainName.equals(EnumSearchFilteringValue.EXPERT_RATING)) {
			
			Collections.sort( filters, FilteringMenuItem.RATING_COMP );
			
		} else 	if(domainName.equals(EnumSearchFilteringValue.CUSTOMER_RATING)) {
			
			Collections.sort( filters, FilteringMenuItem.RATING_COMP );
			
		} else {
			
			Collections.sort( filters, FilteringMenuItem.COUNT_ORDER_REV );
			
			if( filters.size() > hideAfter ) {
				for( ListIterator<FilteringMenuItem> it = filters.listIterator(hideAfter); it.hasNext() ; ) {
					it.next().hide();
				}			
			}			
		}		
		
		return filters;
	}

}
