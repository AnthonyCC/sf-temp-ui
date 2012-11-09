package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class FilterListTag extends BodyTagSupportEx {
	
	private static final long serialVersionUID = 8580228895820361277L;
	private static Logger LOGGER = LoggerFactory.getInstance( FilterListTag.class );
	private static final String hasSelectedVar = "hasSelected";

	private EnumFilteringValue domainName;
	private int hideAfter=7;
	private Map<EnumFilteringValue, Map<String, FilteringMenuItem>> filters;
	
	static final Comparator<FilteringMenuItem> COUNT_ORDER_REV = new Comparator<FilteringMenuItem>() {

		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			return o2.getCounter()-o1.getCounter();
		}
			
	};
	
	@Override
	public int doStartTag() throws JspException {
		ArrayList<FilteringMenuItem> result;
		
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

	boolean hasSelected(ArrayList<FilteringMenuItem> items) {
		if(items == null || items.isEmpty()) return false;
		
		for(FilteringMenuItem item : items) {
			if(item != null && item.isSelected()) return true;
		}
		
		return false;
	}

	public Map<EnumFilteringValue, Map<String, FilteringMenuItem>> getFilters() {
		return filters;
	}

	public void setFilters(Map<EnumFilteringValue, Map<String, FilteringMenuItem>> filters) {
		this.filters = filters;
	}

	public EnumFilteringValue getDomainName() {
		return domainName;
	}

	public void setDomainName(EnumFilteringValue domainName) {
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
							ArrayList.class.getName() + "<"+FilteringMenuItem.class.getName()+">",
							true, VariableInfo.NESTED),
					new VariableInfo(
							hasSelectedVar,
							"java.lang.Boolean",
							true, VariableInfo.NESTED) };
		}
	}

	protected ArrayList<FilteringMenuItem> getResult() throws Exception {
		
		Map<EnumFilteringValue, Map<String, FilteringMenuItem>> menus = this.getFilters();
		ArrayList<FilteringMenuItem> filters = new ArrayList<FilteringMenuItem>(menus.get(domainName).values());
		
		if( filters.isEmpty() ) return null; 
		
		if(domainName.equals(EnumFilteringValue.EXPERT_RATING)) {
			ArrayList<FilteringMenuItem> expertRating = new ArrayList<FilteringMenuItem>((Collection) Arrays.asList(null,null,null,null,null));
			
			for( ListIterator<FilteringMenuItem> it=filters.listIterator(); it.hasNext() ; ) {
				FilteringMenuItem current = it.next();
				int pos = 5-Integer.parseInt(current.getName(),10);
				expertRating.set(pos, current);
			}
			
			return expertRating;
			
		} else {
			Collections.sort(filters, COUNT_ORDER_REV);
			
			if(filters.size() > hideAfter ) {
				for( ListIterator<FilteringMenuItem> it=filters.listIterator(hideAfter); it.hasNext() ; ) {
					it.next().hide();
				}			
			}
			
		}
		
		
		return filters;
	}

}
