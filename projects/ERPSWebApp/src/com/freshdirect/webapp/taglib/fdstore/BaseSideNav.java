package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ArticleMedia;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumShowChildrenType;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.CartesianProduct.TupleIterator;
import com.freshdirect.framework.webapp.BodyTagSupport;

public abstract class BaseSideNav extends BodyTagSupport {

    protected static final int COMPARE_PRIORITY = 1;
    protected static final int COMPARE_NAME = 2;
    protected static final Comparator<NavigationElement> NAV_NAME_COMPARATOR = new NavigationSorter(COMPARE_NAME);
    protected static final Comparator<NavigationElement> NAV_PRIORITY_COMPARATOR = new NavigationSorter(COMPARE_PRIORITY);
    protected boolean startFromDept = false;
    protected String catId;
    protected String navListName;
    protected String unavailableListName;
    protected String topCategoryName;
    protected boolean returnEmptyFolders = false;
    protected boolean sortByPriority = false;
    /** List of the resulting NavigationElement objects */
    protected List<NavigationElement> navList = new ArrayList<NavigationElement>();
    /** List of ProductNavigationElement objects, that are temporarily unavailable */
    protected List<NavigationElement> unavailableList = new ArrayList<NavigationElement>();

    protected static class NavigationSorter implements Comparator<NavigationElement> {
            private int compareBy;
    
            public NavigationSorter(int compareBy) {
                this.compareBy = compareBy;
            }
    
            public int compare(NavigationElement n1, NavigationElement n2) {
    
                String n1Path = n1.getSortString();
                String n2Path = n2.getSortString();
                int pathCompare = n1Path.compareTo(n2Path);
                if (pathCompare != 0) {
                    return pathCompare;
                }
    
                switch (compareBy) {
                case COMPARE_PRIORITY:
                    int priorityDifference = n1.getPriority() - n2.getPriority();
                    if (priorityDifference != 0) {
                        return priorityDifference;
                    }
                    // Fall through
                case COMPARE_NAME:
                    int comp = n1.getDisplayString().compareTo(n2.getDisplayString());
                    if (comp == 0) {
                        comp = n1.getContentName().compareTo(n2.getContentName());
                    }
                    return comp;
                default:
                    return 0;
                }
            }
        }

    public final void setReturnEmptyFolders(boolean flag) {
       this.returnEmptyFolders=flag;
    }

    public final void setCatId(String catId) {
    	this.catId = catId;
    }

    public final void setNavList(String navList) {
    	this.navListName = navList;
    }

    public final void setUnavailableList(String unavList) {
    	this.unavailableListName = unavList;	
    }

    public final void setTopCategory(String topCategory) {
    	this.topCategoryName = topCategory;	
    }

    public final void setStartFromDept(boolean flag) {
    	this.startFromDept = flag;
    }

    public final void setSortByPriority(boolean flag) {
    	this.sortByPriority = flag;	
    }

    public BaseSideNav() {
        super();
    }

    /**
     * Fill up this.navList and this.unavailableList.
     *
     * @return topmost category
     */
    protected final CategoryModel fillFolderInfo(CategoryModel folder) throws FDResourceException {
    	// find the topmost category (one below dept, in path)
    	ContentNodeModel topNode = null;
    	ContentNodeModel tempNode = folder;
    	
    	while (!(tempNode instanceof DepartmentModel)) {
    		topNode = tempNode;
    		//LOGGER.debug(" tempNode: "+tempNode.getPK());
    		if (tempNode == null) { break; }
    		tempNode = (tempNode.getParentNode() != null) ? tempNode.getParentNode() : null;
    	}
    	CategoryModel topCategory = (CategoryModel)topNode;
    	
    	// FIXME: quick & dirty fix for Bakery.
    	// This code is seriously broken, as it ignores all cms attributes which control side-nav behaviour.
    	// This has to be fixed, but that could break other categories too, so we have to do this hack for the bakery for now.
    	
    	// If we happen to be in the Bakery department then use the current folder as the top folder. 
    	// This cannot be achieved with cms attributes as they are ignored here. 
    	// Currently there is no other way to ensure that other categories will not be affected.  
    	if ( tempNode.getContentKey().getEncoded().equals( "Department:bak" ) ) {
    		topCategory = folder;
    	}
    	
    	if (topCategory==null) {
    		return folder;
    	} else {
    		//only startRecursiveWalking if topCat is not null
    		startRecursiveWalking(folder, topCategory);
    		return topCategory;
    	}
    }

    /**
     * @param folder
     * @param topCategory
     * @throws FDResourceException
     */
    protected abstract void startRecursiveWalking(CategoryModel folder, CategoryModel topCategory) throws FDResourceException ;

    public static class ProductCounts {
        int displayableProds = 0; // count number of displayed products instead
        int prodCount = 0;
    }
    /**
     * Recursive method for filling up folder info.
     * Adds elements to this.navList and this.unavailableList.
     * 
     * @param topLevel must be true on first call, will be false on recursion
     * @param depth current display depth, starting with zero
     * @param currentFolder current category (start recursion with topmost category)
     * @param displayedFolder the folder the 
     */
    protected final void fillFolderInfo(boolean topLevel, int depth, ProductContainer currentFolder, ContentNodeModel displayedFolder) throws FDResourceException {
    
        boolean showFolders = false;
        boolean showProducts = false;
        boolean isDept = ContentNodeModel.TYPE_DEPARTMENT.equalsIgnoreCase(currentFolder.getContentType());
        final CategoryModel currentCategory = !isDept ? (CategoryModel) currentFolder : null;
    
        EnumShowChildrenType sc = isDept ? EnumShowChildrenType.ALWAYS_FOLDERS : currentFolder.getSideNavShowChildren();
        if (EnumShowChildrenType.BROWSE_PATH.equals(sc)) {
            if (isParent(currentFolder, displayedFolder)) {
                showFolders = true;
                showProducts = true;
            }
        } else if (EnumShowChildrenType.ALWAYS_FOLDERS.equals(sc)) {
            showFolders = true;
        } else if (EnumShowChildrenType.ALWAYS.equals(sc)) {
            showFolders = true;
            showProducts = true;
        }
    
        int indent = 0;
        final boolean categoryNavigationAdded = (!isDept && !topLevel && currentFolder.getSideNavShowSelf()) && (currentCategory.isActive() || !currentCategory.isHideInactiveSideNav());
        if (categoryNavigationAdded) {
            indent++;
            this.navList.add(new FolderNavigationElement(depth, (CategoryModel) currentFolder, showFolders || showProducts));
        }
    
        // get any articles from the article attribute
        List<Html> articles = currentCategory != null ? ((CategoryModel) currentFolder).getArticles() : null;
        if (articles != null) {
            int articleIdx = 0;
            for (Html ai : articles) {
                this.navList.add(new ArticleNavigationElement(depth + indent, (ArticleMedia) ai, articleIdx, currentFolder));
                articleIdx++;
            }
        }
        ProductCounts p = new ProductCounts();
        if (!isDept) {
            fillProducts(depth, currentCategory, showProducts, indent, p);
        }
    
        boolean childrenRendered = false;
        if (showFolders) {
            // LOGGER.debug("..in logic for showFolders");
            long lastNavListSize = this.navList.size();
            for (CategoryModel cat : currentFolder.getSubcategories()) {
                // recursion here
                this.fillFolderInfo(false, depth + indent, cat, displayedFolder);
            }
            childrenRendered = lastNavListSize != this.navList.size();
        }
    
        if (!returnEmptyFolders && !childrenRendered && p.displayableProds == 0 && p.prodCount > 0) {
            if (categoryNavigationAdded) {
                this.navList.remove(navList.size() - 1);
            }
        }
    }

    
    /**
     * @param depth
     * @param currentFolder 
     * @param showProducts
     * @param indent
     * @param prodCount
     * @param products
     * @return
     */
    protected abstract void fillProducts(int depth, CategoryModel currentFolder, boolean showProducts, int indent, ProductCounts p) throws FDResourceException;
    

    /**
     * Only true, if currentFolder is one of the parents of displayedFolder
     * @param currentFolder
     * @param displayedFolder
     * @return true, if currentFolder is one of the parents of displayedFolder
     */
    protected final boolean isParent(ProductContainer currentFolder, ContentNodeModel displayedFolder) {
        // Only show, if f is one of the parents of displayedFolder
        ContentNodeModel tempNode = displayedFolder;
        while (tempNode != null) {
            if (currentFolder.getContentName().equals(tempNode.getContentName())) {
                return true;
            }
            tempNode = tempNode.getParentNode();
        }
        return false;
    }

}