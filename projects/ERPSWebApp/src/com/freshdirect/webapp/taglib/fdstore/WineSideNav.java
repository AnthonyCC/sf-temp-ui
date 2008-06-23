package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.FDResourceException;


public class WineSideNav extends com.freshdirect.framework.webapp.BodyTagSupport {

	private final static Category LOGGER = LoggerFactory.getInstance( WineSideNav.class );

	private static final int COMPARE_PRIORITY = 1;
	private static final int COMPARE_NAME = 2;
	private final static Comparator NAV_NAME_COMPARATOR = new NavigationSorter(COMPARE_NAME);
	private final static Comparator NAV_PRIORITY_COMPARATOR = new NavigationSorter(COMPARE_PRIORITY);
	
	private boolean startFromDept = false;
	private String catId;
	private String navListName;
	private String childCatMapName;
	private String showMoreOptionsName;
	private String unavailableListName;
	private String topCategoryName;
    private boolean returnEmptyFolders=false;
    private Map childCatMap=new HashMap();
	private boolean sortByPriority = false;
	
	/** List of the resulting NavigationElement objects */
	private List navList = new ArrayList();
	
	/** List of ProductNavigationElement objects, that are temporarily unavailable */
	private List unavailableList = new ArrayList();

	private Boolean showMoreOptions=new Boolean(false);
	
	private boolean moreOptions=false;
	
	
	public void setShowMoreOptions(String name ) {
		this.showMoreOptionsName=name;
	}
	public void setMoreOptions(boolean flag) {
		this.moreOptions=flag;
	}
	
    public void setReturnEmptyFolders(boolean flag) {
       this.returnEmptyFolders=flag;
    }
        
	public void setCatId(String catId) {
		this.catId = catId;
	}

	public void setNavList(String navList) {
		this.navListName = navList;
	}

	public void setChildCatMap(String childCatMap) {
		this.childCatMapName = childCatMap;
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
			this.fillFolderInfo(true, 0, folder.getDepartment(), /*folder.getDepartment()*/folder);
		} else {
			this.fillFolderInfo(true, 0, topCategory, folder);
		}
		
		//Collections.sort(this.navList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
		//Collections.sort(this.unavailableList, (sortByPriority ? NAV_PRIORITY_COMPARATOR : NAV_NAME_COMPARATOR) );
		
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
		boolean isDept = ContentNodeI.TYPE_DEPARTMENT.equalsIgnoreCase(f.getContentType());

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
        if (f.getAttribute("ARTICLES") !=null ) {
            List articles = (List)f.getAttribute("ARTICLES").getValue();
            int articleIdx = 0;
            for (Iterator ai=articles.iterator();ai.hasNext();) {
				this.navList.add(new ArticleNavigationElement(depth + indent, (ArticleMedia)ai.next(),articleIdx,f));
                articleIdx++;
            }
        }
		int displayableProds = 0;  // count number of displayed products instead
		int prodCount=0;
		
        if (!isDept) {        
        	String _catId=((CategoryModel)f).getContentKey().getId();
        	
        	if(catId.equals(_catId)) {
        		if(childCatMap.containsKey(catId)) {
        			childCatMap.remove(catId);
        		}
        		else if (ContentNodeI.TYPE_DEPARTMENT.equalsIgnoreCase(f.getParentNode().getParentNode().getContentType())) {
        			childCatMap.remove(f.getParentNode().getContentName());
        		}
        		List sideNavDomainVals=new ArrayList();
        		List sideNavUsableDomainVals=new ArrayList();
        		List domainNavElem=new ArrayList();
        		sideNavDomainVals=getSideNavDomainValues((CategoryModel)f);
        		Collection products = ((CategoryModel)f).getProducts();
        		prodCount = products.size();
        		displayableProds = prodCount;
        		boolean hasDomainVals=false;
        		if(sideNavDomainVals!=null && sideNavDomainVals.size()>0) {
        			hasDomainVals=true;   
        		}
                for (Iterator i = products.iterator(); i.hasNext();) {
                	ProductModel prod = (ProductModel) i.next();
                	if (prod.isDiscontinued() || prod.isInvisible()) {
                		displayableProds--; //ok..decrement now
                	    continue;
                	}
                	List prodDomainValue=new ArrayList();
                	if(showProducts && hasDomainVals) {// Category has filter by (wine region and wine varietal) domain.
                		prodDomainValue=prod.getNewWineRegion();
                		prodDomainValue.addAll(prod.getWineVarietal());
                    	if(prodDomainValue!=null && prodDomainValue.size()>0) {
                    		for (int j=0;j<prodDomainValue.size();j++) {
                    			DomainValue dValue=(DomainValue)prodDomainValue.get(j);
                    			if(sideNavDomainVals.contains(dValue) && !sideNavUsableDomainVals.contains(dValue)) { 
                    				sideNavUsableDomainVals.add(dValue);
                    				domainNavElem.add(new DomainNavigationElement(depth+indent, (CategoryModel) f, isDept,dValue,moreOptions ));
                    				//this.navList.add(new DomainNavigationElement(depth+indent, (CategoryModel) f, isDept,dValue ));
                    			}
                    		}
            			}
                	}
                	if (prod.isUnavailable()) {
    					if (showProducts ) {
    	                    displayableProds--;  //decrement if we would have displayed this prod
    						this.unavailableList.add(new ProductNavigationElement(1, prod));
    					}
    				} else if (showProducts && !hasDomainVals) {
    					this.navList.add(new ProductNavigationElement(depth + indent, prod));
    				}
                }
                if(showProducts && hasDomainVals) {
                	Collections.sort(domainNavElem, NAV_NAME_COMPARATOR);
                	this.navList.addAll(domainNavElem);
                }
			}
        	else if (ContentNodeI.TYPE_DEPARTMENT.equalsIgnoreCase(f.getParentNode().getContentType())){
        		if(EnumShowChildrenType.ALWAYS.equals(((CategoryModel)f).getShowChildren())) {
            		List childCategories=((CategoryModel)f).getSubcategories();
            		if(!childCatMap.containsKey(((CategoryModel)f).getContentName())) {
            			childCatMap.put(((CategoryModel)f).getContentName(), childCategories);
            		}
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


    private List getSideNavDomainValues(CategoryModel model) {
    	
    	/*List domainValues=new ArrayList();
    	domainValues=model.getWineSideNavSections();
    	if(domainValues!=null && domainValues.size()==0) {
    		
        	domainValues=model.getWineSideNavFullList();
        	if(domainValues!=null && domainValues.size()>0) {
        		Domain dName=(Domain)domainValues.get(0);
        		domainValues=dName.getDomainValues();
        	}
    	}
		return domainValues;
		*/
    	List domainValues=new ArrayList();
    	if(moreOptions) {
        	domainValues=model.getWineSideNavFullList();
        	if(domainValues!=null && domainValues.size()>0) {
        		Domain dName=(Domain)domainValues.get(0);
        		domainValues=dName.getDomainValues();
        		showMoreOptions=new Boolean(false);
        	}
    	}
    	else {
    		domainValues=model.getWineSideNavSections();
    		if(domainValues!=null && domainValues.size()==0) {
        		
            	domainValues=model.getWineSideNavFullList();
            	if(domainValues!=null && domainValues.size()>0) {
            		Domain dName=(Domain)domainValues.get(0);
            		domainValues=dName.getDomainValues();
            		showMoreOptions=new Boolean(false);
            	}
        	}
    		else {
    			List _dVal=model.getWineSideNavFullList();
    			if(_dVal!=null && _dVal.size()>0) {
    				showMoreOptions=new Boolean(true);
    			}
    		}
    	}
    	return domainValues;
    	
	}

	public int doStartTag() throws JspException {

		if (this.catId == null) {
			return SKIP_BODY;
		}

		try {
			CategoryModel folder = (CategoryModel)ContentFactory.getInstance().getContentNodeByName( this.catId );

			if (folder == null) {
				LOGGER.info("SideNav did not find category "+this.catId);
				return SKIP_BODY;
			}

			//Get all the objects
			CategoryModel topNode = fillFolderInfo(folder);
			
			if (ContentNodeI.TYPE_DEPARTMENT.equalsIgnoreCase(topNode.getContentType())) {
				topNode = folder;
			}

			pageContext.setAttribute(topCategoryName, topNode);
			pageContext.setAttribute(navListName, this.navList);
			pageContext.setAttribute(childCatMapName, this.childCatMap);
			pageContext.setAttribute(showMoreOptionsName, this.showMoreOptions);
			pageContext.setAttribute(unavailableListName, this.unavailableList);

			return EVAL_BODY_BUFFERED;

		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException("Error accessing resources: " + ex.getMessage());
		}

	}
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     *
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {

	            new VariableInfo(data.getAttributeString("navList"),
	                "java.util.List", true, VariableInfo.NESTED),
	                
	           new VariableInfo(data.getAttributeString("childCatMap"),
	                        "java.util.Map", true, VariableInfo.NESTED), 
	                        
	         	           new VariableInfo(data.getAttributeString("showMoreOptions"),
	   	                        "java.lang.Boolean", true, VariableInfo.NESTED),	                        

	            new VariableInfo(data.getAttributeString("unavailableList"),
	                "java.util.List", true, VariableInfo.NESTED),
	            
	            new VariableInfo(data.getAttributeString("topCategory"),
	                "com.freshdirect.fdstore.content.CategoryModel", true, VariableInfo.NESTED)

	        };

	    }
	}	    

}
