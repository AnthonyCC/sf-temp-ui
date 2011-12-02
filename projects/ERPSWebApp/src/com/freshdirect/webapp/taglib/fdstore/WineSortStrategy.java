package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.util.EnumWineSortType;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;

/**@author ekracoff*/
@Deprecated
public class WineSortStrategy extends TagSupport {
	private static final long serialVersionUID = -487559315956770400L;

	@SuppressWarnings("unused")
	private static Category LOGGER = LoggerFactory.getInstance(WineSortStrategy.class);
	
	private String sortNameAttrib;
	private boolean sortDescending;
	
	@Deprecated
	public WineSortStrategy() {
		
	}
	@Deprecated
	public WineSortStrategy(String sortNameAttrib, boolean sortDescending) {
		this.sortNameAttrib = sortNameAttrib;
		this.sortDescending = sortDescending;
	}
	@Deprecated
	public String getSortNameAttrib() {
		return sortNameAttrib;
	}
	@Deprecated
	public void setSortNameAttrib(String sortNameAttrib) {
		this.sortNameAttrib = sortNameAttrib;
	}
	@Deprecated
	public boolean isSortDescending() {
		return sortDescending;
	}
	@Deprecated
	public void setSortDescending(boolean sortDescending) {
		this.sortDescending = sortDescending;
	}
	
	@Deprecated
	public List<SortStrategyElement> getSortStrategyElements(String sortCol) {
		List<SortStrategyElement> strategyElements = new ArrayList<SortStrategyElement>();
		if(EnumWineSortType.ABC.equals(EnumWineSortType.getWineSortType(sortCol))){
			strategyElements.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, sortDescending));
		}
		else if(EnumWineSortType.PRICE.equals(EnumWineSortType.getWineSortType(sortCol))){
			strategyElements.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE, null, sortDescending));
		} else {
			strategyElements.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_WINE_ATTRIBUTE, sortCol, sortDescending));
		}
		return strategyElements;
	}
}
