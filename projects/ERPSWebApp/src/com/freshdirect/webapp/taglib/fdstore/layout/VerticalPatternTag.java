package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;


public class VerticalPatternTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	// === ATTRIBUTES ===
	private String				id					= null;
	private Collection			itemsToShow			= null;
	private int					productCellWidth	= 0;
	private int					folderCellWidth		= 0;
	private ContentNodeModel 	currentFolder 		= null;
	private boolean				showFolders 		= false;
	private boolean				returnCategory		= false;
	private int					maxColumns			= 4;
	private boolean 			labeled 			= false;
	
	// === JSP VARIABLES created ===
	public static final String rowListVariableName 			= "rowList";
	public static final String tableWidthVariableName 		= "tableWidth";
	public static final String onlyOneProductVariableName 	= "onlyOneProduct";
	public static final String theOnlyProductVariableName 	= "theOnlyProduct";
	public static final String rowIndexVariableName 		= "rowIndex";

	// === work variables ===
	private int				rowIndex			= 0;
	private int				tableWidth			= 0;
	private boolean			onlyOneProduct 		= false;
	private ProductModel 	theOnlyProduct		= null;
	private List[] 			columns				= null; 	
	private Image[] 		headers 			= null; 	
	
	// ========= Attribute setter/getter-s ============
	
	public void setId( String id ) {
		this.id = id;
	}

	public void setProductCellWidth( int productCellWidth ) {
		this.productCellWidth = productCellWidth;
	}
	public int getProductCellWidth() {
		return productCellWidth;
	}

	
	public void setFolderCellWidth( int folderCellWidth ) {
		this.folderCellWidth = folderCellWidth;
	}
	public int getFolderCellWidth() {
		return folderCellWidth;
	}

	public void setCurrentFolder( ContentNodeModel currentFolder ) {
		this.currentFolder = currentFolder;
	}
	public ContentNodeModel getCurrentFolder() {
		return currentFolder;
	}

	public void setShowFolders( boolean showFolders ) {
		this.showFolders = showFolders;
	}	
	public boolean isShowFolders() {
		return showFolders;
	}

	public void setReturnCategory( boolean returnCategory ) {
		this.returnCategory = returnCategory;
	}
	public boolean isReturnCategory() {
		return returnCategory;
	}
	
	public void setMaxColumns( int maxColumns ) {
		this.maxColumns = maxColumns < 1 ? 1 : maxColumns;
	}
	public int getMaxColumns() {
		return maxColumns;
	}
	
	public void setLabeled( boolean labeled ) {
		this.labeled = labeled;
	}
	public boolean isLabeled() {
		return labeled;
	}	

	public Image getHeader( int colIdx ) {
		return headers[colIdx];
	}
	

	public void setItemsToShow( Collection items ) {
		itemsToShow = items;
		columns = null;
	}
	
	
	// ========= TAG related methods =========
	
	public int doStartTag() throws JspException {
		
		rowIndex = 0;
		prepareColumns();

		if ( columns == null || columns.length < 1 ) {
			pageContext.setAttribute( onlyOneProductVariableName, new Boolean(false) );
			return SKIP_BODY;
		} else {
			return EVAL_BODY_BUFFERED;
		}
	}

	public void doInitBody() throws JspException {
		setVariables( buildReturnArray() );
	}

	public int doAfterBody() throws JspException {

		List returnItems = buildReturnArray();		
		
		if ( returnItems == null || returnItems.size() == 0 ) {
			return SKIP_BODY;
		}
		
		boolean hasAnyRealItems = false;
		Iterator it = returnItems.iterator();
		while( it.hasNext() ) {
			if ( it.next() != null ) {
				hasAnyRealItems = true;
				break;
			}
		}
		if ( !hasAnyRealItems )
			return SKIP_BODY;
		
		setVariables( returnItems );
		return EVAL_BODY_BUFFERED;
	}

	
	// ========= private stuff =========
	
	private void prepareColumns() {
		
		onlyOneProduct = false;
		theOnlyProduct = null;
		int productCounter = 0;
		
		columns = new List[maxColumns];
				
		if ( itemsToShow == null )
			return;
		
		for ( int i = 0; i < maxColumns; i++ )
			columns[i] = new ArrayList( 2 * itemsToShow.size() / maxColumns );
		
		if ( isLabeled() )
			headers = new Image[maxColumns];
		
		Iterator it = itemsToShow.iterator();
		while ( it.hasNext() ) {			
			ContentNodeModel currentNode = (ContentNodeModel)it.next();
			
			// skip if not Product or Category, or null 
			if ( currentNode == null || ! ( currentNode instanceof ProductModel || currentNode instanceof CategoryModel ) )
				continue;
			
			// skip unavailable products
			if ( currentNode instanceof ProductModel && ( (ProductModel)currentNode ).isUnavailable() )
				continue;
			
			// skip hidden folders
			if ( !isLabeled() && currentNode instanceof CategoryModel && ! ((CategoryModel)currentNode).getShowSelf() )
				continue;
			
			// skip categories if ...
			if ( currentNode instanceof CategoryModel ) {
				if ( !returnCategory )
					continue;
				if ( !showFolders && ((CategoryModel)currentNode).getAttribute( "TREAT_AS_PRODUCT", false ) )
					continue;				
			}			

			// get category 
			CategoryModel category;
			if ( currentNode instanceof CategoryModel ) { // && !contentNode.getAttribute( "TREAT_AS_PRODUCT", false ) ) {					
				category = (CategoryModel)currentNode;
			} else {
				// use the parent-category
				category = (CategoryModel)( (ContentNodeModel)currentNode ).getParentNode();
			}

			
			int colIdx  = category.getAttribute( "COLUMN_NUM", 1 ) - 1 ; // -1 to have 0 based index			
			int colSpan = category.getAttribute( "COLUMN_SPAN", 1 );
			
			// skip if column index is invalid 
			if ( colIdx < 0 || colIdx >= maxColumns ) 
				continue;

			if ( isLabeled() ) {
				if ( currentNode instanceof CategoryModel && currentNode.hasAttribute("CAT_LABEL") && ((CategoryModel)currentNode).getDepartment().equals(currentNode.getParentNode()) ) {
					headers[ colIdx ] = ((CategoryModel)currentNode).getCategoryLabel();
					continue;
				}
			}
			
			// determine which column to add to
			while ( colSpan > 1 && colIdx < maxColumns-1 ) {
				if ( columns[colIdx].size() > columns[colIdx+1].size() ) {
					colIdx++;
					colSpan--;
				} else {
					break;
				}
			}			
			
			// add it to the column list
			columns[colIdx].add( currentNode );
			
								
			// check for single products 
			if ( currentNode instanceof ProductModel ) {
				productCounter++;
				theOnlyProduct = (ProductModel)currentNode;
			}
		}
		
		if ( productCounter == 1 ) {
			onlyOneProduct = true;
		}		
	}

	private List buildReturnArray() {
		
		ArrayList returnItems = new ArrayList( maxColumns );
		
		boolean hasAnyRealItems = false;
		Object item = null;
		for ( int i = 0; i < maxColumns; i++ ) {
			if ( columns[i] != null && rowIndex < columns[i].size() ) {
				item = columns[i].get( rowIndex );
				hasAnyRealItems = true;
			} else {
				item = null;
			}
			returnItems.add( item );
		}		
		rowIndex++;
		
		if ( !hasAnyRealItems ) {
			returnItems = new ArrayList();
		}
		
		return returnItems;
	}
	
	private void setVariables( List returnItems ) {
		pageContext.setAttribute( id, this );
		pageContext.setAttribute( rowListVariableName, returnItems );
		pageContext.setAttribute( tableWidthVariableName, new Integer(tableWidth) );		
		pageContext.setAttribute( onlyOneProductVariableName, new Boolean(onlyOneProduct) );
		pageContext.setAttribute( rowIndexVariableName, new Integer( rowIndex-1 ) );
		if ( theOnlyProduct != null )
			pageContext.setAttribute( theOnlyProductVariableName, theOnlyProduct );		
	}
	
	
	// ========= TAG Extra Info class =========
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "id" ),
	            		"com.freshdirect.webapp.taglib.fdstore.layout.VerticalPatternTag",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		rowListVariableName,
	            		"java.util.List",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		tableWidthVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		rowIndexVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED ),
   	            new VariableInfo(
	            		onlyOneProductVariableName,
	            		"java.lang.Boolean",
	            		true, 
	            		VariableInfo.AT_BEGIN ),
   	            new VariableInfo(
	            		theOnlyProductVariableName,
	            		"com.freshdirect.fdstore.content.ProductModel",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
