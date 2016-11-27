package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.text.StrBuilder;

import com.freshdirect.fdstore.content.util.FourMinuteMealsHelper;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class FilterWidgetColumnTag extends BodyTagSupport {
	
	private static final long	serialVersionUID	= -7712743727449769854L;
	
	protected String 										id 					= null;
	protected List<String>						 			itemIds				= null;
	protected List<String>						 			selectedIds			= null;
	protected Map<String,FourMinuteMealsHelper.FilterInfo>	filterRows			= null;
	protected String 										trackingCode		= null;
	protected String										urlParameterName	= null;
	protected String										allItemName			= null;
	protected boolean										enableAll		= false;

	private int 								itemIdIndex = 0;
	private int									currentItemIndex = 0;
	private Map<String, String[]>				urlParams=null;

	public static final String 					currentIdVariableName 			= "currentId";
	public static final String 					currentLabelVariableName 		= "currentLabel";
	public static final String 					currentUrlVariableName 			= "currentUrl";
	public static final String 					itemIndexVariableName 			= "itemIndex";
	public static final String 					currentItemCountVariableName	= "currentCount";
	public static final String 					itemDisabledVariableName		= "itemDisabled";	
	public static final String 					itemSelectedVariableName		= "itemSelected";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public List<String> getItemIds() {
		return itemIds;
	}
	public void setItemIds(List<String> itemIds) {
		this.itemIds = itemIds;
	}

	public String getAllItemName() {
		return allItemName;
	}
	public void setAllItemName(String allItemName) {
		this.allItemName = allItemName;
	}

	public List<String> getSelectedIds() {
		return selectedIds;
	}
	public void setSelectedIds(List<String> selectedIds) {
		this.selectedIds = selectedIds;
	}

	public Map<String, FourMinuteMealsHelper.FilterInfo> getFilterRows() {
		return filterRows;
	}
	public void setFilterRows(Map<String, FourMinuteMealsHelper.FilterInfo> filterRows) {
		this.filterRows = filterRows;
	}
	
	public String getTrackingCode() {
		return trackingCode;
	}	
	public void setTrackingCode( String trackingCode ) {
		this.trackingCode = trackingCode;
	}

	public String getUrlParameterName() {
		return urlParameterName;
	}
	public void setUrlParameterName(String urlParameterName) {
		this.urlParameterName = urlParameterName;
	}
	
	public boolean isEnableAll() {
		return enableAll;
	}
	public void setEnableAll(boolean enableAllItem) {
		this.enableAll = enableAllItem;
	}
	/**
	 * Override this to get the items id
	 */
	protected String getItemId(Object item) {
		return "itemId";
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public int doStartTag() throws JspException {
		itemIdIndex = 0;
		currentItemIndex = 0;
				
		if ( itemIds == null || itemIds.size() == 0 ) {
			return SKIP_BODY;
		} 
		
		urlParams=new HashMap<String, String[]>(pageContext.getRequest().getParameterMap());
		
		doAllItem();
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		
		String returnItem;
		try {
			returnItem = getCurrentItem();
		} catch ( OutOfItemsException e ) {
			return SKIP_BODY;
		}
		
		setVariables( returnItem );
				
		return EVAL_BODY_AGAIN;
	}
	
	@Override
	public int doEndTag() {
		
		return EVAL_PAGE;
	}
	
	public void doAllItem() {
		urlParams.remove(urlParameterName);
		pageContext.setAttribute( currentIdVariableName, "all" );			
		pageContext.setAttribute( currentItemCountVariableName, 0 );
		pageContext.setAttribute( itemSelectedVariableName, false );
		pageContext.setAttribute( itemDisabledVariableName, !isEnableAll() && (selectedIds == null || selectedIds.size() == 0) );
		pageContext.setAttribute( currentLabelVariableName, getAllItemName() );		
		pageContext.setAttribute( currentUrlVariableName, FourMinuteMealsHelper.allPageBaseUrl+"&"+generateUrl(urlParams) );		
	}
		
	private String getCurrentItem() throws OutOfItemsException {
		if ( itemIdIndex >= itemIds.size() )
			throw new OutOfItemsException();
		
		
		String nextItem = itemIds.get( itemIdIndex++ );
		
		if( nextItem != null) {
			return nextItem;
		}
		
		return getCurrentItem();
	}

	protected void setVariables( String item ) {
		FourMinuteMealsHelper.FilterInfo currentRow = null; 
		pageContext.setAttribute( id, this );
		pageContext.setAttribute( itemIndexVariableName, new Integer( currentItemIndex++ ) );
		if(item!=null) {
			currentRow=filterRows.get(item);
		}
		
		if ( currentRow == null ) {
			pageContext.removeAttribute( currentIdVariableName );			
			pageContext.removeAttribute( currentLabelVariableName );
			pageContext.removeAttribute( currentItemCountVariableName );
			pageContext.removeAttribute( currentUrlVariableName );
			pageContext.removeAttribute( itemSelectedVariableName );
			pageContext.removeAttribute( itemDisabledVariableName );
			
		} else {
			boolean isSelected = (selectedIds != null && selectedIds.contains(item));
			List<String> selectedParams = new ArrayList<String>(); 
			pageContext.setAttribute( currentIdVariableName, item );
			pageContext.setAttribute( currentLabelVariableName, currentRow.getLabel() );
			pageContext.setAttribute( currentItemCountVariableName, currentRow.getCount());
			pageContext.setAttribute( itemSelectedVariableName, isSelected );
			pageContext.setAttribute( itemDisabledVariableName, currentRow.getCount()==0 );

			if(selectedIds != null && selectedIds.size() > 0) {
				selectedParams.addAll(selectedIds);
				if(isSelected) {
					selectedParams.remove(item);					
				} else {
					selectedParams.add(item);
				}
			}
			else {
				selectedParams.add(item);
			}
			urlParams.remove(urlParameterName);
			if(selectedParams.size()>0) {
				urlParams.put(urlParameterName, selectedParams.toArray(new String[0]));				
			}
			pageContext.setAttribute( currentUrlVariableName, FourMinuteMealsHelper.allPageBaseUrl+"&"+generateUrl(urlParams) );
		}
	}
	
	private String generateUrl(Map<String, String[]> map) {
		List<String> buffer = new ArrayList<String>();
		StrBuilder buf = new StrBuilder();
		for( Map.Entry<String, String[]> e : map.entrySet()) {
			String key = e.getKey();
			if ( "trk".equals( key ) || "deptId".equals( key ) ) {
				continue;
			}
			for( String value : e.getValue()) {
				buffer.add(e.getKey()+"="+value);
			}
		}
		
		return buf.appendWithSeparators(buffer, "&").toString();
	}
		
	protected void println(String s) {		
		try {
			JspWriter out = pageContext.getOut();
			out.print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static class OutOfItemsException extends IndexOutOfBoundsException {
		
		private static final long	serialVersionUID	= -9044131924685412513L;

		public OutOfItemsException() {
			super();
		}
		
	    public OutOfItemsException(String s) {
	    	super(s);
        }
	}

	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "id" ),
	            		"com.freshdirect.webapp.taglib.fdstore.display.FilterWidgetColumnTag",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		currentIdVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
		        		currentItemCountVariableName,
		        		"java.lang.Integer",
		        		true, 
		        		VariableInfo.NESTED ),
	            new VariableInfo(
		        		currentUrlVariableName,
		        		"java.lang.String",
		        		true, 
		        		VariableInfo.NESTED ),
	            new VariableInfo(
	            		itemSelectedVariableName,
	            		"java.lang.Boolean",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		itemDisabledVariableName,
	            		"java.lang.Boolean",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		itemIndexVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		currentLabelVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.NESTED )
	        };
	    }
	}
}
