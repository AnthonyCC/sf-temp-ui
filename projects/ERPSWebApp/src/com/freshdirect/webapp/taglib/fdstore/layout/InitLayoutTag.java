package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;


/**
 * Common layout initializing
 * 
 * @author treer
 */
public class InitLayoutTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static final int IMAGE_MAX_HEIGHT = 130;

	private static final int IMAGE_MAX_WIDTH = 160;

	private static final int IMAGE_MIN_WIDTH = 140;

	private static final long	serialVersionUID	= 336241157986179162L;
	
	// === JSP VARIABLES created ===
	public static final String categoryIdVariableName 		= "categoryId";
	public static final String departmentIdVariableName 	= "departmentId";
	public static final String isDepartmentVariableName 	= "isDepartment";
	public static final String trackingCodeVariableName 	= "trackingCode";
	public static final String currentFolderVariableName 	= "currentFolder";
	public static final String sortedCollectionVariableName = "sortedCollection";	
	public static final String useAlternateVariableName 	= "useAlternateImages";
	public static final String useWidenedWidth 				= "useWidenedWidth";
	
	private boolean useAlternateWidth=false;
	
		
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
		
		@SuppressWarnings( "unchecked" )
		List<ContentNodeModel> sortedColl = (List<ContentNodeModel>)pageContext.getRequest().getAttribute( "itemGrabberResult" );
		if ( sortedColl == null )
			sortedColl = new ArrayList<ContentNodeModel>();
		
		if ( catId != null )
			pageContext.setAttribute( categoryIdVariableName, catId );
		
		if ( deptId != null ) 
			pageContext.setAttribute( departmentIdVariableName, deptId );
		
		pageContext.setAttribute( isDepartmentVariableName, Boolean.valueOf(isDepartment) );
		pageContext.setAttribute( trackingCodeVariableName, trackingCode );
		
		if ( currentFolder != null ) {
			pageContext.setAttribute( currentFolderVariableName, currentFolder );
			boolean useAlternate = false;
			if ( currentFolder instanceof CategoryModel )
				useAlternate = ((CategoryModel)currentFolder).isUseAlternateImages();
			else if ( currentFolder instanceof DepartmentModel )
				useAlternate = ((DepartmentModel)currentFolder).isUseAlternateImages();			
			pageContext.setAttribute( useAlternateVariableName, Boolean.valueOf( useAlternate ) );			
		} else {
			pageContext.setAttribute( currentFolderVariableName, null );			
			pageContext.setAttribute( useAlternateVariableName, Boolean.FALSE );			
		}
		
		
		pageContext.setAttribute( sortedCollectionVariableName, sortedColl );
		
		boolean widenedWidth=false;
		
		//should be deleted after all department navigation images are widened
		if(useAlternateWidth){
			
			for(ContentNodeModel model: sortedColl){
			
				if(model instanceof CategoryModel){
				
					CategoryModel cat=(CategoryModel) model;
					if(cat.getCategoryPhoto()!=null && 
					   cat.getCategoryPhoto().getWidth()>IMAGE_MIN_WIDTH && 
					   cat.getCategoryPhoto().getWidth()<IMAGE_MAX_WIDTH && 
					   cat.getCategoryPhoto().getHeight()<IMAGE_MAX_HEIGHT){
						
						widenedWidth=true;
						break;
					}
				}
			}
		}
		
		pageContext.setAttribute(useWidenedWidth, widenedWidth);
		
		
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
	            		"java.util.List<ContentNodeModel>",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		useAlternateVariableName,
	            		"java.lang.Boolean",
	            		true, 
	            		VariableInfo.AT_END )
	        };
	    }
	}

	public void setUseAlternateWidth(boolean useAlternateWidth) {
		this.useAlternateWidth = useAlternateWidth;
	}
	
}
