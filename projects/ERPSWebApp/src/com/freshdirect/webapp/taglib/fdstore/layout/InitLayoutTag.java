package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;


/**
 * Common layout initializing
 * 
 * @author treer
 */
public class InitLayoutTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	// === JSP VARIABLES created ===
	public static final String categoryIdVariableName 		= "categoryId";
	public static final String departmentIdVariableName 	= "departmentId";
	public static final String isDepartmentVariableName 	= "isDepartment";
	public static final String trackingCodeVariableName 	= "trackingCode";
	public static final String currentFolderVariableName 	= "currentFolder";
	public static final String sortedCollectionVariableName = "sortedCollection";	
	
		
	public int doStartTag() throws JspException {
		setVariables();
		return EVAL_BODY_INCLUDE;
	}
	
		
	private void setVariables() {
		
		String catId = pageContext.getRequest().getParameter( "catId" );
		String deptId = pageContext.getRequest().getParameter( "deptId" );
		boolean isDepartment = false;
		ContentNodeModel currentFolder = null;
		String trackingCode = "";
		
		if ( deptId != null && !deptId.trim().equals( "" ) ) {
			isDepartment = true;
			currentFolder = ContentFactory.getInstance().getContentNode( deptId );
			trackingCode = "dpage";
		} else {
		    currentFolder = ContentFactory.getInstance().getContentNode( catId );				
			trackingCode = "cpage";
		}
		
		Collection sortedColl = (Collection)pageContext.getRequest().getAttribute( "itemGrabberResult" );
		if ( sortedColl == null )
			sortedColl = new ArrayList();

		
		if ( catId != null )
			pageContext.setAttribute( categoryIdVariableName, catId );
		
		if ( deptId != null ) 
			pageContext.setAttribute( departmentIdVariableName, deptId );
		
		pageContext.setAttribute( isDepartmentVariableName, new Boolean(isDepartment) );
		pageContext.setAttribute( trackingCodeVariableName, trackingCode );
		
		if ( currentFolder != null )
			pageContext.setAttribute( currentFolderVariableName, currentFolder );
		
		pageContext.setAttribute( sortedCollectionVariableName, sortedColl );
		
	}	
	
	// ========= TAG Extra Info class =========
	
	public static class TagEI extends TagExtraInfo {
		
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		categoryIdVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		departmentIdVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		isDepartmentVariableName,
	            		"java.lang.Boolean",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		trackingCodeVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		currentFolderVariableName,
	            		"com.freshdirect.fdstore.content.ContentNodeModel",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		sortedCollectionVariableName,
	            		"java.util.Collection",
	            		true, 
	            		VariableInfo.AT_END ),
	        };
	    }
	}
}
