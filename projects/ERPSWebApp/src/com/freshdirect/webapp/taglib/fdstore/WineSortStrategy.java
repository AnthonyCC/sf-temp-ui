package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.util.EnumWineSortType;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;
import com.freshdirect.webapp.taglib.fdstore.layout.ItemSorterTag;

/**@author ekracoff*/
public class WineSortStrategy extends TagSupport {
	private static final long serialVersionUID = -487559315956770400L;

	@SuppressWarnings("unused")
	private static Category LOGGER = LoggerFactory.getInstance(ItemSorterTag.class);
	private String sortNameAttrib;
	private boolean sortDescending;
	
	public WineSortStrategy() {
		
	}
	public WineSortStrategy(String sortNameAttrib, boolean sortDescending) {
		this.sortNameAttrib = sortNameAttrib;
		this.sortDescending = sortDescending;
	}
	public String getSortNameAttrib() {
		return sortNameAttrib;
	}
	public void setSortNameAttrib(String sortNameAttrib) {
		this.sortNameAttrib = sortNameAttrib;
	}
	public boolean isSortDescending() {
		return sortDescending;
	}
	public void setSortDescending(boolean sortDescending) {
		this.sortDescending = sortDescending;
	}
	
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
