package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ContentNodeModel;

/**
 * Pattern Row Tag <br/>
 * 
 *  Common row tag for layouts. Iterates over elements of one table row. <br/> 
 *  
 * @author treer
 */
public class PatternRowTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	// === ATTRIBUTES ===
	private String				id					= null;
	private List				itemsToShow			= null;

	// work variables
	private int			itemsToShowIndex	= 0;
	
	public static final String currentItemVariableName = "currentItem";
	public static final String columnIndexVariableName = "columnIndex";

	public void setId( String id ) {
		this.id = id;
	}	
	
	
	public void setItemsToShow( List itemsToShow ) {
		this.itemsToShow = itemsToShow;
	}


	public int doStartTag() throws JspException {
		
		itemsToShowIndex = 0;

		if ( itemsToShow == null || itemsToShow.size() < 1 ) {
			return SKIP_BODY;
		} else {
			return EVAL_BODY_BUFFERED;
		}
	}

	public void doInitBody() throws JspException {
		try {
			setVariables( getCurrentItem() );
		} catch ( OutOfItemsException e ) {
			setVariables( null );
		}
	}

	public int doAfterBody() throws JspException {

		ContentNodeModel returnItem;
		try {
			returnItem = getCurrentItem();
		} catch ( OutOfItemsException e ) {
			return SKIP_BODY;
		}
		
		setVariables( returnItem );
		return EVAL_BODY_BUFFERED;
	}
	
	private ContentNodeModel getCurrentItem() throws OutOfItemsException {
		if ( itemsToShowIndex >= itemsToShow.size() )
			throw new OutOfItemsException();
		
		return (ContentNodeModel)itemsToShow.get( itemsToShowIndex++ );
	}

	private void setVariables( ContentNodeModel item ) {
		pageContext.setAttribute( id, this );
		pageContext.setAttribute( columnIndexVariableName, new Integer( itemsToShowIndex-1 ) );
		if ( item == null ) {
			pageContext.removeAttribute( currentItemVariableName );			
		} else {
			pageContext.setAttribute( currentItemVariableName, item );
		}
	}
	
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "id" ),
	            		"com.freshdirect.webapp.taglib.fdstore.layout.PatternRowTag",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		currentItemVariableName,
	            		"com.freshdirect.fdstore.content.ContentNodeModel",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		columnIndexVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED )
	        };
	    }
	}
	
	public static class OutOfItemsException extends IndexOutOfBoundsException {
		
		public OutOfItemsException() {
			super();
		}
		
	    public OutOfItemsException(String s) {
	    	super(s);
        }
	}
}
