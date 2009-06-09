/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ItemGrabberTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance( ItemGrabberTag.class );
	
	private boolean ignoreShowChildren = false;
	private String id = null;
	private ContentNodeModel rootNode = null;
	private boolean returnHiddenFolders = false;
	private boolean returnSecondaryFolders = false;
	private boolean filterDiscontinued = true;
    private boolean ignoreDuplicateProducts=false;
    private boolean returnSkus=false;
	private boolean returnInvisibleProducts = false;  // return prods that are flagged as invisible
	private int depth = 1;
		
	private HashSet noDupeProds = new HashSet();
	private List workSet = new ArrayList();
	private List skuList = new ArrayList();

	public void setReturnHiddenFolders(boolean returnHiddenFlag) {
		this.returnHiddenFolders = returnHiddenFlag;
	}

	public void setIgnoreShowChildren(boolean ignoreFlag) {
		this.ignoreShowChildren = ignoreFlag;
	}

    public void setIgnoreDuplicateProducts(boolean ignoreFlag) {
        this.ignoreDuplicateProducts = ignoreFlag;
        
    }
    public void setWorkSet(List workSet){
    	this.workSet=workSet;
    }
    
    
    public void setReturnSkus(boolean flag) {
        this.returnSkus = flag;
     }
    
    
	public void setId(String id) {
			this.id = id;
	}

	public void setCategory(ContentNodeModel category) {
			this.rootNode = category;
	}
		
	public void setDepth(int depth){
		this.depth = depth;
	}

	public void setReturnSecondaryFolders(boolean rsf) {
		this.returnSecondaryFolders = rsf;
	}
		
	public void setReturnInvisibleProducts(boolean setting) {
		this.returnInvisibleProducts = setting;
	}

	public void setFilterDiscontinued(boolean filterDiscontinued) {
		this.filterDiscontinued = filterDiscontinued;
	}
			
	public int doStartTag() throws JspException {
		if (rootNode == null || id == null) {
			throw new JspException("Attribute for ItemGrabberTag was null!");
		}
		
		//Profiler profiler = new Profiler("ItemGrabber");
		//profiler.start();	

		try {
			getProdsAndFolders(rootNode, this.depth); 

			if (this.filterDiscontinued && this.skuList.size()>0) {
				// make sure FDProductInfos are cached
				FDCachedFactory.getProductInfos( (String[])this.skuList.toArray( new String[0] ) );
	
				// remove discontinued products from workSet
				for (ListIterator i=this.workSet.listIterator(); i.hasNext(); ) {
					ContentNodeModel node = (ContentNodeModel)i.next();
					if (node instanceof ProductModel && ((ProductModel)node).isDiscontinued() ) {
						i.remove();
					}
				}			
			}

		} catch (FDSkuNotFoundException ex) {
			throw new JspException(ex);	
		} catch (FDResourceException ex) {
			throw new JspException(ex);	
		}
		
		//sort the set
		LOGGER.debug(">>>>>>>>>> workSet size before Sort = "+workSet.size());
		
		pageContext.setAttribute(id, workSet);
		
		//profiler.stop();
		
		return (EVAL_BODY_BUFFERED);
	} // method doStartTag



	private void getProdsAndFolders(ContentNodeModel currentNode, int desiredDepth) throws FDResourceException, FDSkuNotFoundException {
		this.getProdsAndFolders(currentNode, desiredDepth, 0);
	}
	
	/**
	 * @return true if at least one product/category added to work list
	 * also if a duplecate product would have been added, it returns true.
	 */
	private boolean getProdsAndFolders(ContentNodeModel currentNode, int desiredDepth, int currentDepth) throws FDResourceException, FDSkuNotFoundException {
   
		if (currentNode instanceof ProductModel) {
			return false;
		}
	
		ProductModel product = null;
		boolean rtnValue = false;  

		List subFolders = null;

		if (currentNode instanceof CategoryModel) {
			CategoryModel currentCat = (CategoryModel)currentNode;
			Collection products = currentCat.getProducts();
	        
			for (Iterator prdItr = products.iterator(); prdItr.hasNext(); ) {  // get prods from current folder
				product = (ProductModel)prdItr.next();

				// are we returning invisible products 
				if (!returnInvisibleProducts && product.isInvisible()) continue;

				if ( (!ignoreDuplicateProducts && this.noDupeProds.add(product.getContentName())==false) || product.isHidden()) {
					rtnValue = true;
					continue;  
				}
				
				if (filterDiscontinued) {
					this.skuList.addAll( product.getSkuCodes() );
				}
				if (this.returnSkus){
					for (Iterator itr = product.getSkus().iterator(); itr.hasNext();) {
						SkuModel sku = (SkuModel)itr.next();
						if (!sku.isDiscontinued()){
							this.workSet.add(sku);
						}
					}
				} else 	this.workSet.add(product );
				rtnValue=true;
			}

			subFolders = currentCat.getSubcategories();

		} else {
			// it must be a department
			subFolders = ((DepartmentModel)currentNode).getCategories();				   
		
		}

		//
		// Now get the products for the subfolders that have their Show_Folder=true and Show_children=always
		//

		CategoryModel subFolder = null;

		for (Iterator iter=subFolders.iterator(); iter.hasNext(); ) {
			subFolder = (CategoryModel)iter.next();

			if (subFolder.isHidden()) {
				// skip hidden
				continue;
			}
			if (!returnSecondaryFolders && subFolder.isSecondaryCategory()) {
				continue;
			}
			if (subFolder.getShowSelf() || returnHiddenFolders) {
				this.workSet.add(subFolder);
				rtnValue = true;
			}

			if ( (!subFolder.getAttribute("TREAT_AS_PRODUCT", false) && depth>0) && 
			     ( (EnumShowChildrenType.ALWAYS.equals(subFolder.getShowChildren()) ||
			        (ignoreShowChildren && !EnumShowChildrenType.NEVER.equals(subFolder.getShowChildren()))		//get prods if show_children, regardless of show folder setting
			       ) 
			     )
			   ) {	
				  
				int wrkSetSize1 = this.workSet.size();
					
				// recurse and process this folder
				if (desiredDepth >= (currentDepth+1)) {
					boolean keepLastCategory = this.getProdsAndFolders( subFolder, desiredDepth, currentDepth+1 );
					if (!keepLastCategory && this.workSet.size()==wrkSetSize1 && wrkSetSize1 !=0 && (subFolder.getShowSelf() || returnHiddenFolders)) {
						this.workSet.remove(wrkSetSize1-1);
				   }
				}
			}
		}
		return rtnValue;
	}

	public static class TagEI extends TagExtraInfo {

	    /**
	     * Return information about the scripting variables to be created.
	     *
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(data.getAttributeString("id"),
	                "java.util.Collection",true, VariableInfo.NESTED),
	        };

	    }

	}
}