package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;


public class HorizontalPatternTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	// === ATTRIBUTES ===
	private String							id							= null;
	private Collection<ContentNodeModel>	itemsToShow					= null;
	private int								productCellWidth			= 0;
	private int								folderCellWidth				= 0;
	private int								tableWidth					= 0;
	private ContentNodeModel				currentFolder				= null;
	private boolean							showCategories				= false;
	private boolean							showProducts				= true;
	private boolean							useLayoutPattern			= true;
	private boolean							dynamicSize					= false;
	private int								maxColumns					= 4;
	boolean									useAlternateImage			= false;			// alternate image
	
	// === JSP VARIABLES created ===
	public static final String rowListVariableName 			= "rowList";
	public static final String tableWidthVariableName 		= "tableWidth";
	public static final String onlyOneProductVariableName 	= "onlyOneProduct";
	public static final String theOnlyProductVariableName 	= "theOnlyProduct";

	// === work variables ===
	private List<ContentNodeModel>			itemList					= null;
	private int[]							patternArray				= null;
	private int								patternIndex				= 0;
	private int								itemsToShowIndex			= 0;
	// private int tableWidth = 0;
	private boolean							onlyOneProduct				= false;
	private ProductModel					theOnlyProduct				= null;	
	
	// pattern layout for 4 columns
	public static int[][] displayPattern = { 
		{ 1, 0, 0, 0, 0 }, { 2, 0, 0, 0, 0 }, { 3, 0, 0, 0, 0 }, { 4, 0, 0, 0, 0 },
		{ 3, 2, 0, 0, 0 }, { 4, 2, 0, 0, 0 }, { 4, 3, 0, 0, 0 }, { 4, 4, 0, 0, 0 }, 
		{ 4, 2, 3, 0, 0 }, { 4, 3, 3, 0, 0 }, { 4, 3, 4, 0, 0 }, { 4, 4, 4, 0, 0 }, 
		{ 4, 3, 4, 2, 0 }, { 4, 3, 4, 3, 0 }, { 4, 4, 4, 3, 0 }, { 4, 4, 4, 4, 0 }, 
		{ 4, 3, 4, 3, 3 }, { 4, 3, 4, 3, 4 }, { 4, 4, 4, 4, 3 }, { 4, 4, 4, 4, 4 } 
	};

	
	// experimental pattern for 3 columns
	public static int[][] displayPattern3 = { 
		{ 1, 0, 0, 0, 0 }, { 2, 0, 0, 0, 0 }, { 3, 0, 0, 0, 0 }, 
		{ 2, 2, 0, 0, 0 }, { 3, 2, 0, 0, 0 }, { 3, 3, 0, 0, 0 },  
		{ 3, 2, 2, 0, 0 }, { 3, 3, 2, 0, 0 }, { 3, 3, 3, 0, 0 }, 
		{ 3, 3, 2, 2, 0 }, { 3, 3, 3, 2, 0 }, { 3, 3, 3, 3, 0 }, 
		{ 3, 3, 3, 2, 2 }, { 3, 3, 3, 3, 2 }, { 3, 3, 3, 3, 3 }
	};
	
	// experimental pattern for 5 columns
	public static int[][] displayPattern5 = { 
		{ 1, 0, 0, 0, 0 }, { 2, 0, 0, 0, 0 }, { 3, 0, 0, 0, 0 }, { 4, 0, 0, 0, 0 }, { 5, 0, 0, 0, 0 },
		{ 3, 3, 0, 0, 0 }, { 4, 3, 0, 0, 0 }, { 4, 4, 0, 0, 0 }, { 5, 4, 0, 0, 0 }, { 5, 5, 0, 0, 0 },  
		{ 4, 4, 3, 0, 0 }, { 4, 4, 4, 0, 0 }, { 5, 4, 4, 0, 0 }, { 5, 5, 4, 0, 0 }, { 5, 5, 5, 0, 0 },
		{ 4, 4, 4, 4, 0 }, { 5, 5, 4, 3, 0 }, { 5, 5, 4, 4, 0 }, { 5, 5, 5, 4, 0 }, { 5, 5, 5, 5, 0 },
		{ 5, 5, 4, 4, 3 }, { 5, 5, 4, 4, 4 }, { 5, 5, 5, 4, 4 }, { 5, 5, 5, 5, 4 }, { 5, 5, 5, 5, 5 }
	};
	
	
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
	
	public void setShowCategories( boolean showCategories ) {
		this.showCategories = showCategories;
	}
	public boolean isShowCategories() {
		return showCategories;
	}

	public void setShowProducts( boolean showProducts ) {
		this.showProducts = showProducts;
	}
	public boolean isShowProducts() {
		return showProducts;
	}
	
	public void setUseLayoutPattern( boolean useLayoutPattern ) {
		this.useLayoutPattern = useLayoutPattern;
	}
	public boolean isUseLayoutPattern() {
		return useLayoutPattern;
	}
	
	public void setDynamicSize( boolean dynamicSize ) {
		this.dynamicSize = dynamicSize;
	}
	public boolean isDynamicSize() {
		return dynamicSize;
	}
	
	public void setMaxColumns( int maxColumns ) {
		this.maxColumns = maxColumns < 1 ? 1 : maxColumns;
	}
	public int getMaxColumns() {
		return maxColumns;
	}
	
	public void setTableWidth( int tableWidth ) {
		this.tableWidth = tableWidth;
	}
	public int getTableWidth() {
		return tableWidth;
	}
	
	public void setUseAlternateImage( boolean useAlternateImage ) {
		this.useAlternateImage = useAlternateImage;
	}
	public boolean isUseAlternateImage() {
		return useAlternateImage;
	}
	
	public void setItemsToShow( Collection<ContentNodeModel> items ) {
		itemsToShow = items;
		itemList = null;
	}

	
	// ========= TAG related methods =========
	
	public int doStartTag() throws JspException {
		
		patternIndex		= 0;
		itemsToShowIndex	= 0;
		prepareItemList();

		if ( itemList == null || itemList.size() < 1 ) {
			pageContext.setAttribute( onlyOneProductVariableName, new Boolean(false) );
			return SKIP_BODY;
		} else if ( useLayoutPattern && ( patternArray == null || patternArray.length < 1 ) ) {
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

		List<ContentNodeModel> returnItems = buildReturnArray();		
		
		if ( returnItems.size() == 0 ) {
			return SKIP_BODY;
		}
		
		setVariables( returnItems );
		return EVAL_BODY_BUFFERED;
	}

	
	// ========= private stuff =========
	
	private void prepareItemList() {
		
		if ( itemsToShow == null ) {
			itemList = null;
			return;
		}
		
		itemList = new ArrayList<ContentNodeModel>( itemsToShow.size() );
		
		onlyOneProduct = false;
		theOnlyProduct = null;
		int productCounter = 0;
		
		for ( ContentNodeModel obj : itemsToShow ) {
			
			// skip if not Product or Category, or null 
			if ( obj == null || ! ( obj instanceof ProductModel || obj instanceof CategoryModel ) )
				continue;
			
			// skip categories if not requested 
			if ( !showCategories && obj instanceof CategoryModel )
				continue;
			
			// skip products if not requested 
			if ( !showProducts && obj instanceof ProductModel )
				continue;
			
			// skip unavailable products
			if ( obj instanceof ProductModel && ( (ProductModel)obj ).isUnavailable() )
				continue;
			
			// skip hidden folders
			if ( obj instanceof CategoryModel && ! ( (CategoryModel)obj ).getShowSelf() )
				continue;
		
			// otherwise add it to the list
			itemList.add( obj );
			
			// check for single products 
			if ( obj instanceof ProductModel ) {
				productCounter++;
				theOnlyProduct = (ProductModel)obj;
			}
		}
		
		if ( productCounter == 1 ) {
			onlyOneProduct = true;
		}
		
		if ( useLayoutPattern ) {
			int totalItemsToDisplay = 	
				itemList.size() > displayPattern.length ? 
				displayPattern.length - 1 : 
				itemList.size() - 1;
			if ( totalItemsToDisplay < 0 )
				totalItemsToDisplay = 0;
			patternArray = displayPattern[ totalItemsToDisplay ];
		}		
	}
	
	private List<ContentNodeModel> buildReturnArray() {
		
		ArrayList<ContentNodeModel> returnItems = new ArrayList<ContentNodeModel>();
		int numOfItemsOnRow; 
		
		if ( useLayoutPattern ) {
			
			// check to see if the pattern index has been exhausted..and reset it
			if ( patternIndex >= patternArray.length )
				patternIndex = 0;
			
			numOfItemsOnRow = patternArray[patternIndex];
			
		} else if ( dynamicSize ) {
			
			int w = 0;
			int i = itemsToShowIndex;
			
			while ( i < itemList.size() && w <= tableWidth ) {
				ContentNodeModel node = (ContentNodeModel)itemList.get( i++ );
				
				Image img = null;
				if ( node instanceof ProductModel ) {
					if ( useAlternateImage ) {
						img = ( (ProductModel)node ).getAlternateImage();
					}
					if ( img == null ) {
						img = ( (ProductModel)node ).getProdImage();
					}
					w += img == null ? productCellWidth : img.getWidth();
				} else if ( node instanceof CategoryModel ) {
					img = ( (CategoryModel)node ).getCategoryPhoto();
					w += img == null ? folderCellWidth : img.getWidth();
				}				
			}
			
			numOfItemsOnRow = i - itemsToShowIndex - 1;
			numOfItemsOnRow = Math.min( maxColumns, numOfItemsOnRow );
			
		} else {
			
			numOfItemsOnRow = Math.min( maxColumns, itemList.size() - itemsToShowIndex );
		}

		//compute the width of the table
		if ( !dynamicSize )
			tableWidth = 0;
		
		for ( int rowItemIndex = 0; rowItemIndex < numOfItemsOnRow && itemsToShowIndex < itemList.size(); rowItemIndex++ ) {
			ContentNodeModel item = (ContentNodeModel)itemList.get( itemsToShowIndex++ );
			
			if ( !dynamicSize ) {
				if ( item instanceof CategoryModel && currentFolder instanceof DepartmentModel ) {
					tableWidth += folderCellWidth;
				} else {
					tableWidth += productCellWidth;
				}
			}
			
			returnItems.add( item );			
		}
		
		if ( useLayoutPattern )
			patternIndex++;
		
		return returnItems;
	}
	
	private void setVariables( List<ContentNodeModel> returnItems ) {
		pageContext.setAttribute( id, this );
		pageContext.setAttribute( rowListVariableName, returnItems );
		pageContext.setAttribute( tableWidthVariableName, new Integer(tableWidth) );		
		pageContext.setAttribute( onlyOneProductVariableName, new Boolean(onlyOneProduct) );
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
	            		"com.freshdirect.webapp.taglib.fdstore.layout.HorizontalPatternTag",
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
