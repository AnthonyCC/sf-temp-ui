package com.freshdirect.webapp.taglib.fdstore.display;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.display.FilterWidgetColumnTag.OutOfItemsException;

public class SortBarTag extends BodyTagSupport {

	private static final long serialVersionUID = 6776136251996325348L;
	
	private SearchSortType[] sortItems;
	private String sortParamName = "sort";
	private String orderParamName = "order";

	private String defaultSort = SearchSortType.BY_NAME.getLabel();
	private String defaultOrder = "asc";
	
	public static String currentUrlVariableName = "currentUrl";
	public static String currentTextVariableName = "currentText";
	public static String isSelectedVariableName = "isSelected";
	public static String currentIndexVariableName = "currentIndex";
	
	private SearchSortType currentSort;
	private String currentOrder;
	
	private int itemIdIndex;
	
	private HttpServletRequest request;
	private QueryParameterCollection qc;
	private String uri;
	
	public int doStartTag() throws JspException {
		String qs;
		itemIdIndex = 0;
				
		if ( sortItems == null || sortItems.length == 0 ) {
			return SKIP_BODY;
		} 
		
		request = (HttpServletRequest) pageContext.getRequest();
		uri = request.getRequestURI();
		
		qs = request.getQueryString();
		
		if(qs == null) {
			qs = "";
		}
		
		qc = QueryParameterCollection.decode(qs);
		
		currentSort = SearchSortType.findByLabel(qc.getParameterValue("sort", defaultSort));
		currentOrder = qc.getParameterValue("order", defaultOrder);
		
		setVariables( getCurrentItem() );
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		
		SearchSortType returnItem;
		try {
			returnItem = getCurrentItem();
		} catch ( OutOfItemsException e ) {
			return SKIP_BODY;
		}
		
		setVariables( returnItem );
				
		return EVAL_BODY_AGAIN;
	}
	
	private void setVariables(SearchSortType sortType) {
		
		boolean isSelected = currentSort == sortType;
		boolean isAscending = currentOrder.equals("asc");
		
		qc.setParameterValue(sortParamName, sortType.getLabel());
		
		if(isSelected && isAscending) {
			qc.setParameterValue(orderParamName, "desc" );			
		} else {
			qc.setParameterValue(orderParamName, "asc");
		}
		
		pageContext.setAttribute(isSelectedVariableName, isSelected);
		pageContext.setAttribute(currentIndexVariableName, itemIdIndex);
		pageContext.setAttribute(currentUrlVariableName, uri+'?'+qc.getEncoded());
		pageContext.setAttribute(currentTextVariableName, isSelected ? (isAscending ? sortType.getTextAsc() : sortType.getTextDesc()) : sortType.getText());
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	private SearchSortType getCurrentItem() throws OutOfItemsException {
		if ( itemIdIndex >= sortItems.length )
			throw new OutOfItemsException();
		
		
		SearchSortType nextItem = sortItems[itemIdIndex++];
		
		if( nextItem != null) {
			return nextItem;
		}
		
		return getCurrentItem();
	}
	
	public String getSortParamName() {
		return sortParamName;
	}
	
	public void setSortParamName(String sortParamName) {
		this.sortParamName = sortParamName;
	}
	
	public SearchSortType[] getSortItems() {
		return sortItems;
	}
	
	public void setSortItems(SearchSortType[] sortItems) {
		this.sortItems = sortItems;
	}
	
	public void setDefaultSort(String defaultSort) {
		this.defaultSort = defaultSort;
	}
	
	public String getDefaultSort() {
		return defaultSort;
	}
	
	public void setDefaultOrder(String defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	
	public String getDefaultOrder() {
		return defaultOrder;
	}
	
	
	
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
		            new VariableInfo(
		            		currentUrlVariableName,
		            		"java.lang.String",
		            		true, 
		            		VariableInfo.NESTED ),
		            new VariableInfo(
		            		isSelectedVariableName,
		            		"java.lang.Boolean",
		            		true, 
		            		VariableInfo.NESTED ),
		            new VariableInfo(
		            		currentIndexVariableName,
		            		"java.lang.Integer",
		            		true, 
		            		VariableInfo.NESTED ),
		            new VariableInfo(
		            		currentTextVariableName,
			        		"java.lang.String",
			        		true, 
			        		VariableInfo.NESTED )
	        };
	    }
	}
}
