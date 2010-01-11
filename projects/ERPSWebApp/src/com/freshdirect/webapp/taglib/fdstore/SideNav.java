package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ArticleMedia;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumShowChildrenType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;


public class SideNav extends com.freshdirect.framework.webapp.BodyTagSupport {

	private final static Category LOGGER = LoggerFactory.getInstance( SideNav.class );

	private static final int COMPARE_PRIORITY = 1;
	private static final int COMPARE_NAME = 2;
	private final static Comparator NAV_NAME_COMPARATOR = new NavigationSorter(COMPARE_NAME);
	private final static Comparator NAV_PRIORITY_COMPARATOR = new NavigationSorter(COMPARE_PRIORITY);
	
	private boolean startFromDept = false;
	private String catId;
	private String navListName;
	private String unavailableListName;
	private String topCategoryName;
    private boolean returnEmptyFolders=false;
	private boolean sortByPriority = false;
	/** List of the resulting NavigationElement objects */
	private List navList = new ArrayList();
	
	/** List of ProductNavigationElement objects, that are temporarily unavailable */
	private List unavailableList = new ArrayList();

    public void setReturnEmptyFolders(boolean flag) {
       this.returnEmptyFolders=flag;
    }
        
	public void setCatId(String catId) {
		this.catId = catId;
	}

	public void setNavList(String navList) {
		this.navListName = navList;
	}

	public void setUnavailableList(String unavList) {
		this.unavailableListName = unavList;	
	}

	public void setTopCategory(String topCategory) {
		this.topCategoryName = topCategory;	
	}

	public void setStartFromDept(boolean flag){
		this.startFromDept = flag;
	}

	public void setSortByPriority(boolean flag) {
		this.sortByPriority = flag;	
	}

	private static class NavigationSorter implements Comparator {
		private int compareBy;

    	public NavigationSorter(int compareBy) {
    		this.compareBy = compareBy;
    	}

    	public int compare(Object o1, Object o2) {
    		NavigationElement n1 = (NavigationElement) o1;
    		NavigationElement n2 = (NavigationElement) o2;

    		String n1Path = n1.getSortString();
    		String n2Path = n2.getSortString();
    		int pathCompare = n1Path.compareTo(n2Path);
    		if (pathCompare != 0) return pathCompare;

			switch (compareBy) {
				case COMPARE_PRIORITY:
					int priorityDifference = n1.getPriority() - n2.getPriority();
					if (priorityDifference != 0) return priorityDifference;
					//Fall through
				case COMPARE_NAME:
					int comp = n1.getDisplayString().compareTo(n2.getDisplayString());
					if(comp == 0) {
						comp = n1.getContentName().compareTo(n2.getContentName());
					}
					return comp;
				default:
					return 0;
			}
    	}
    }

	/**
	 * Fill up this.navList and this.unavailableList.
	 *
	 * @return topmost category
	 */ 
	private CategoryModel fillFolderInfo(CategoryModel folder) throws FDResourceException {
		// find the topmost category (one below dept, in path)
		ContentNodeModel topNode = null;
		ContentNodeModel tempNode = folder;
		
		while (!(tempNode instanceof DepartmentModel)) {
			topNode = tempNode;
			//LOGGER.debug(" tempNode: "+tempNode.getPK());
			tempNode = tempNode.getParentNode();
		}
		CategoryModel topCategory = (CategoryModel)topNode;
		
		// watch out, it's got a funky beat
		if (startFromDept) {
			this.fillFolderInfo(true, 0, folder.getDepartment(), folder.getDepartment());
		} else {
			this.fillFolderInfo(true, 0, topCategory, folder);
		}
		
		Collections.sort(this.navList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
		Collections.sort(this.unavailableList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
		
		if (topCategory==null) {
			return folder ;
		} else {
			return topCategory;
		}
	}

	/**
	 * Recursive method for filling up folder info.
	 * Adds elements to this.navList and this.unavailableList.
	 * 
	 * @param firstIteration must be true on first call, will be false on recursion
	 * @param depth current display depth, starting with zero
	 * @param f current category (start recursion with topmost category)
	 * @param displayedFolder the folder the 
	 */
    private void fillFolderInfo(boolean firstIteration, int depth, ContentNodeModel f, ContentNodeModel displayedFolder) throws FDResourceException {


		boolean showFolders = false;
		boolean showProducts = false;
		boolean isDept = ContentNodeModel.TYPE_DEPARTMENT.equalsIgnoreCase(f.getContentType());

		EnumShowChildrenType sc =isDept ? EnumShowChildrenType.ALWAYS_FOLDERS : ((CategoryModel)f).getSideNavShowChildren();
		if (EnumShowChildrenType.BROWSE_PATH.equals(sc)) {
			//Only show, if f is one of the parents of displayedFolder
			ContentNodeModel tempNode = displayedFolder;
			while (tempNode!=null) {
				if ( f.getContentName().equals(tempNode.getContentName()) ) {
					showFolders = true;
					showProducts = true;
					break;
				}
				tempNode = tempNode.getParentNode();
			}
		} else if (EnumShowChildrenType.ALWAYS_FOLDERS.equals(sc)) {
			showFolders = true;
		} else if (EnumShowChildrenType.ALWAYS.equals(sc)) {
			showFolders = true;
			showProducts = true;
		}

		int indent = 0;
		if (!isDept && !firstIteration && ((CategoryModel)f).getSideNavShowSelf() ) {
			indent++;
			this.navList.add( new FolderNavigationElement(depth, (CategoryModel)f, showFolders || showProducts) );
		}

        // get any articles from the article attribute
	List articles = (f instanceof CategoryModel) ? ((CategoryModel)f).getArticles() : null;
	if (articles != null) {
            int articleIdx = 0;
            for (Iterator ai=articles.iterator();ai.hasNext();) {
                this.navList.add(new ArticleNavigationElement(depth + indent, (ArticleMedia)ai.next(),articleIdx,f));
                articleIdx++;
            }
	}
		int displayableProds = 0;  // count number of displayed products instead
		int prodCount=0;
        if (!isDept) {        
			Collection products = ((CategoryModel)f).getProducts();
	        
			prodCount = products.size();
	                // since we do not show discontinued & hidden items..we will change the variable to the following
	        displayableProds = prodCount;  // count number of displayed products instead
			for (Iterator i = products.iterator(); i.hasNext();) {
				ProductModel prod = (ProductModel) i.next();
				if (prod.isDiscontinued() || prod.isInvisible()) {
					//availProds--;
	                                displayableProds--; //ok..decrement now
	                                continue;
				}
				if (prod.isUnavailable()) {
					if (showProducts) {
	                                        displayableProds--;  //decrement if we would have displayed this prod
						this.unavailableList.add(new ProductNavigationElement(1, prod));
					}
				} else if (showProducts) {
					this.navList.add(new ProductNavigationElement(depth + indent, prod));
				}
			}
        }

		boolean childrenRendered = false;
		if (showFolders) {
		//LOGGER.debug("..in logic for showFolders");
            long lastNavListSize = this.navList.size();
			Iterator catItr = isDept ? ((DepartmentModel)f).getCategories().iterator()  : ((CategoryModel)f).getSubcategories().iterator();                    
			for (;catItr.hasNext();) {
				// recursion here
				this.fillFolderInfo(
					false,
					depth + indent,
					(CategoryModel) catItr.next(),
					displayedFolder);
			}
			childrenRendered = lastNavListSize != this.navList.size();
		}

		if (!returnEmptyFolders && !childrenRendered && displayableProds == 0 && prodCount>0) {
			if (!isDept && !firstIteration && ((CategoryModel)f).getSideNavShowSelf()) {
				this.navList.remove(navList.size() - 1);
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
