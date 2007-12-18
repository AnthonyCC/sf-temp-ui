/*
 * VerticalPatterTag.java
 *
 * Created on October 26, 2001, 5:01 PM
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.attributes.*;

/**
 * Iterator-tag (repeats body for every row).
 *
 * @version $Revision$
 * @author $Author$
 */
public class VerticalPatternTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private String id;
	private int columns = -1;
	private int rows = -1;
	private boolean showFolder;
	private boolean returnCategory = false;
	private List nodes;

	/** Loop-variable */
	private int startIndex = 0;

	public void setId(String id) {
		this.id = id;
	}

	public void setShowFolder(boolean setting){
		this.showFolder = setting;
	}
	
	public void setReturnCategory(boolean setting){
		this.returnCategory = setting;
	}

	/**
	 * @param items Collection if ContentNodes (CategoryModel or ProductModel)
	 */
	public void setCollection(Collection items) {
		boolean folderAsProduct = false;
		if (showFolder) {
			this.nodes = new ArrayList(items);
		} else {
			// copy ProductModels and category models with TREAT_AS_Product attrib = true only
			this.nodes = new ArrayList( items.size() );
			for (Iterator i=items.iterator(); i.hasNext(); ) {
				Object o = i.next();
				if (o instanceof CategoryModel ) { 
					CategoryModel cm = (CategoryModel)o;
					folderAsProduct = (cm.getAttribute("TREAT_AS_PRODUCT")==null) 
						? false
						: ((Boolean)cm.getAttribute("TREAT_AS_PRODUCT").getValue()).booleanValue();
					if (!folderAsProduct && !returnCategory) continue;
				}
				this.nodes.add(o);
			}
		}
	}

	public void setColumns(int columnCount) {
		this.columns = columnCount;
	}

	public void setRows(int rowCount) {
		this.rows = rowCount;
	}
 
	public int doStartTag() throws JspException {
		return (this.nodes==null || this.nodes.size()<1) ? SKIP_BODY : EVAL_BODY_BUFFERED;
	}

	public void doInitBody() throws JspException {
		Object[] returnItems = this.buildReturnArray();
		pageContext.setAttribute(id, returnItems);
	}

	public int doAfterBody() throws JspException  {
		Object[] returnItems = this.buildReturnArray();
		if (returnItems.length==0) {
			return SKIP_BODY;
		}
		pageContext.setAttribute(id, returnItems);
		return EVAL_BODY_BUFFERED;
	}

	private final static ContentNodeModel[] EMPTY_NODES = new ContentNodeModel[0];

	//the dirty work method.
	private ContentNodeModel[] buildReturnArray() {
            ArrayList returnItems = new ArrayList();
		int collectionSize = this.nodes.size();
		//** if we are building unlimited columns then columns will be negative
		if (rows > 0) {
			if (startIndex<=collectionSize && startIndex<rows){
				for (int idx = startIndex;idx < collectionSize;idx+=rows){
					returnItems.add( this.nodes.get(idx) );
				}
			}
		} else if (columns>0 && startIndex<collectionSize) { 
			//unlimited row. but set number of columns
			
			//initialize the returnArrayList to contain empty items.
			for(int i=0; i<this.columns; i++) {
				returnItems.add(i,null);
			}

			int colIdx = 0;
			int colSpan = 1;  
			
			// iterate thru nodes and add it to array if the element at the specified column_num is not null (it was not set yet)
			// null out processed nodes, so they can be easily skipped
			for (int idx=0; idx<collectionSize; idx++){
				ContentNodeModel contentNode = (ContentNodeModel)this.nodes.get(idx);
				if (contentNode==null) {
					// processed, skip it
					continue;
				}

				CategoryModel cat;
				if (contentNode instanceof CategoryModel) {
					boolean folderAsProduct = (contentNode.getAttribute("TREAT_AS_PRODUCT")==null) 
							? false 
							: ((Boolean)contentNode.getAttribute("TREAT_AS_PRODUCT").getValue()).booleanValue();
					if (folderAsProduct) {
						cat = (CategoryModel)contentNode.getParentNode();
					} else {
						cat = (CategoryModel)contentNode;
					}
				} else {
					// it must be a product, use the parent-category
					cat = (CategoryModel)contentNode.getParentNode();
					
				}
                colIdx = 1;
                colSpan=1;
                Attribute colinfo = cat.getAttribute("COLUMN_NUM");
                if (colinfo!=null) {
                    colIdx = ((Integer)colinfo.getValue()).intValue();
                }
                colinfo = cat.getAttribute("COLUMN_SPAN");
                if (colinfo!=null) {
                    colSpan = ((Integer)colinfo.getValue()).intValue();
                }

				//colIdx = cat.getAttribute("COLUMN_NUM", 4);
				//colSpan = cat.getAttribute("COLUMN_SPAN", 1);
				//we got a column...if we already put an item in the array, then skip this
				colIdx = colIdx-1<0 ? 0 : colIdx-1;
				   
				//if the column is greater than the number of specified cols.. ignore
				if (colIdx >= returnItems.size()) {
					continue;
				}
				boolean foundColumn = true;
				if (returnItems.get(colIdx)!=null) {
					foundColumn = false;
					// If there is a colSpan set.. then find the next available column
					for (int j=1; (j<colSpan) && ((colIdx+j)<returnItems.size()) && (foundColumn==false); j++) {
						// locate next 
						if (returnItems.get(colIdx+j)!=null) {
							continue;
						}
						colIdx+=j;
                                                foundColumn = true;
					}
				}
				if (!foundColumn) {
					continue;
				}

				returnItems.set(colIdx, contentNode);	// add it
				this.nodes.set(idx, null);				// node processed, null it out

			}
		}

		boolean allNull = true;
		for (int i=0; i<returnItems.size(); i++){
			if (returnItems.get(i)!=null) {
				allNull=false;
				break;
			}
		}

		startIndex++;
		return allNull ? EMPTY_NODES : (ContentNodeModel[])returnItems.toArray(EMPTY_NODES);
	}

}
