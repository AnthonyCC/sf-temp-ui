package com.freshdirect.webapp.taglib.fdstore;


import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;


public class SideNav extends BaseSideNav {

	private final static Logger LOGGER = LoggerFactory.getInstance( SideNav.class );

	    /**
	     * @param folder
	     * @param topCategory
	     * @throws FDResourceException
	     */
	@Override
	    protected void startRecursiveWalking(CategoryModel folder, CategoryModel topCategory) throws FDResourceException {
	        // watch out, it's got a funky beat
	        if (startFromDept) {
	                this.fillFolderInfo(true, 0, folder.getDepartment(), folder.getDepartment());
	        } else {
	                this.fillFolderInfo(true, 0, topCategory, folder);
	        }
	        
	        Collections.sort(this.navList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
	        Collections.sort(this.unavailableList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
	    }
	
	@Override
        protected void fillProducts(int depth, CategoryModel currentFolder, boolean showProducts, int indent, ProductCounts p) throws FDResourceException {
            List<ProductModel> products = currentFolder.getProducts();
            p.prodCount = products.size();
    
            // since we do not show discontinued & hidden items..we will change
            // the variable to the following
            p.displayableProds = p.prodCount; // count number of displayed products
            // instead
            for (ProductModel prod : products) {
                if (prod.isDiscontinued() || prod.isInvisible()) {
                    // availProds--;
                    p.displayableProds--; // ok..decrement now
                    continue;
                }
                if (prod.isUnavailable()) {
                    if (showProducts) {
                        p.displayableProds--; // decrement if we would have
                        // displayed this prod
                        this.unavailableList.add(new ProductNavigationElement(1, prod));
                    }
                } else if (showProducts) {
                    this.navList.add(new ProductNavigationElement(depth + indent, prod));
                }
            }
        }	
	
	public int doStartTag() throws JspException {
		if (this.catId == null) {
			return SKIP_BODY;
		}

		try {
			CategoryModel folder = (CategoryModel)ContentFactory.getInstance().getContentNode(this.catId);

			if (folder == null) {
				LOGGER.info("SideNav did not find category "+this.catId);
				return SKIP_BODY;
			}

			//Get all the objects
			CategoryModel topNode = fillFolderInfo(folder);
			
			if (ContentNodeModel.TYPE_DEPARTMENT.equalsIgnoreCase(topNode.getContentType())) {
				topNode = folder;
			}
			pageContext.setAttribute(topCategoryName, topNode);
			pageContext.setAttribute(navListName, this.navList);
			pageContext.setAttribute(unavailableListName, this.unavailableList);

			return EVAL_BODY_BUFFERED;

		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException("Error accessing resources: " + ex.getMessage());
		}

	}

}
