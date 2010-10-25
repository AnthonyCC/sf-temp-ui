package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.FDURLUtil;

public class ContentNodeIteratorTag extends BodyTagSupport {
	
	private static final long	serialVersionUID	= -7712743727449769854L;
	
	protected String 								id 				= null;
	protected List<? extends ContentNodeModel> 		itemsToShow		= null;
	protected boolean 								showProducts	= true;
	protected boolean 								showCategories	= true;
	protected boolean								noTypeCheck		= false;
	protected String 								trackingCode	= null;
	protected int									maxItems 		= 0;	// 0 means no limit
	protected boolean								filterUnav		= true;
	
	protected boolean								appendWineParams = false;

	
	private int 								itemsToShowIndex = 0;
	private int									currentItemIndex = 0;

	public static final String 					currentItemVariableName 	= "currentItem";
	public static final String 					itemIndexVariableName 		= "itemIndex";
	public static final String 					actionUrlVariableName 		= "actionUrl";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public List<? extends ContentNodeModel> getItemsToShow() {
		return itemsToShow;
	}
	public void setItemsToShow(List<? extends ContentNodeModel> itemsToShow) {
		this.itemsToShow = itemsToShow;
	}
	
	public boolean isShowProducts() {
		return showProducts;
	}
	public void setShowProducts(boolean showProducts) {
		this.showProducts = showProducts;
	}

	public boolean isShowCategories() {
		return showCategories;
	}
	public void setShowCategories(boolean showCategories) {
		this.showCategories = showCategories;
	}

	public boolean isNoTypeCheck() {
		return noTypeCheck;
	}
	
	public void setNoTypeCheck(boolean noTypeCheck) {
		this.noTypeCheck = noTypeCheck;
	}

	public String getTrackingCode() {
		return trackingCode;
	}	
	public void setTrackingCode( String trackingCode ) {
		this.trackingCode = trackingCode;
	}

	public int getMaxItems() {
		return maxItems;
	}
	
	public void setMaxItems( int maxItems ) {
		this.maxItems = maxItems;
	}
	
	public boolean isFilterUnav() {
		return filterUnav;
	}
	
	public void setFilterUnav( boolean filterUnav ) {
		this.filterUnav = filterUnav;
	}

	public void setAppendWineParams(boolean appendWineParams) {
		this.appendWineParams = appendWineParams;
	}
	
	@Override
	public int doStartTag() throws JspException {
		
		itemsToShowIndex = 0;
		currentItemIndex = 0;

		if ( itemsToShow == null || itemsToShow.size() < 1 ) {
			return SKIP_BODY;
		} 
			
		try {
			setVariables( getCurrentItem() );
		} catch ( OutOfItemsException e ) {
			return SKIP_BODY;
		}
		
		doFirst();
		doStart();

		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		
		ContentNodeModel returnItem;
		try {
			returnItem = getCurrentItem();
		} catch ( OutOfItemsException e ) {
			return SKIP_BODY;
		}
		
		setVariables( returnItem );
		
		doEnd();
		doStart();
		
		return EVAL_BODY_AGAIN;
	}
	
	@Override
	public int doEndTag() {

		if ( itemsToShow == null || itemsToShow.size() < 1 ) {
			return SKIP_BODY;
		} 
		
		doEnd();
		doLast();
		
		return EVAL_PAGE;
	}
	
	/**
	 * Override this to output content at the start of the very first iteration
	 */
	protected void doFirst() {		
	}
	/**
	 * Override this to output content at the start of every iteration
	 */
	protected void doStart() {		
	}
	/**
	 * Override this to output content at the end of every iteration
	 */
	protected void doEnd() {		
	}
	/**
	 * Override this to output content at the end of the very last iteration
	 */
	protected void doLast() {		
	}
	
	private ContentNodeModel getCurrentItem() throws OutOfItemsException {
		if ( itemsToShowIndex >= itemsToShow.size() )
			throw new OutOfItemsException();
		
		if ( maxItems != 0 && currentItemIndex >= maxItems )
			throw new OutOfItemsException();
		
		ContentNodeModel nextItem = itemsToShow.get( itemsToShowIndex++ );
		
		if( noTypeCheck && nextItem != null && isAvailable( nextItem ) ) {
			return nextItem;
		}
		
		if ( showProducts && nextItem instanceof ProductModel && isAvailable( nextItem ) ) {
			return nextItem;
		}

		if ( showCategories && nextItem instanceof CategoryModel ) {
			return nextItem;
		}
		
		return getCurrentItem();
	}
	
	private boolean isAvailable( ContentNodeModel node ) {
		if ( filterUnav == false ) 
			return true;

		if ( node instanceof ProductModel ) {
			return !((ProductModel)node).isUnavailable();
		} else if ( node instanceof SkuModel ) {
			return !((SkuModel)node).isUnavailable();			
		}
		return true;
	}

	protected void setVariables( ContentNodeModel item ) {
		pageContext.setAttribute( id, this );
		pageContext.setAttribute( itemIndexVariableName, new Integer( currentItemIndex++ ) );
		pageContext.setAttribute( actionUrlVariableName, getActionUrl( item ) );
		if ( item == null ) {
			pageContext.removeAttribute( currentItemVariableName );			
		} else {
			pageContext.setAttribute( currentItemVariableName, item );
		}
	}
	
	@SuppressWarnings("unchecked")
	protected String getActionUrl( ContentNodeModel node ) {
		if ( node instanceof CategoryModel ) {
			String uri = FDURLUtil.getCategoryURI( (CategoryModel)node, trackingCode );
			return appendWineParams ? (String) FDURLUtil.appendWineParamsToURI(uri, pageContext.getRequest().getParameterMap()) : uri;			
		} else if ( node instanceof ProductModel ) {
			String uri = FDURLUtil.getProductURI( (ProductModel)node, trackingCode );
			return appendWineParams ? (String) FDURLUtil.appendWineParamsToURI(uri, pageContext.getRequest().getParameterMap()) : uri;			
		} else {
			return "#";
		}
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
	            		"com.freshdirect.webapp.taglib.fdstore.display.ContentNodeIteratorTag",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		currentItemVariableName,
	            		"com.freshdirect.fdstore.content.ContentNodeModel",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		itemIndexVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		actionUrlVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.NESTED )
	        };
	    }
	}
}
