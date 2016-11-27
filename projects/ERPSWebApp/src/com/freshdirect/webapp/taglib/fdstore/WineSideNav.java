package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumShowChildrenType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author zsombor
 *
 */
public class WineSideNav extends BaseSideNav {

	private final static Logger LOGGER = LoggerFactory.getInstance( WineSideNav.class );

	
	private String childCatMapName;
	private String showMoreOptionsName;
	private Map<String, List<CategoryModel>> childCatMap=new HashMap<String, List<CategoryModel>>();
	
	private Boolean showMoreOptions=Boolean.FALSE;
	
	private boolean moreOptions=false;
	
	
	public void setShowMoreOptions(String name ) {
		this.showMoreOptionsName=name;
	}
	public void setMoreOptions(boolean flag) {
		this.moreOptions=flag;
	}

	public void setChildCatMap(String childCatMap) {
		this.childCatMapName = childCatMap;
	}
	
    /**
     * @param folder
     * @param topCategory
     * @throws FDResourceException
     */
    @Override
    protected void startRecursiveWalking(CategoryModel folder, CategoryModel topCategory) throws FDResourceException {
        // watch out, it's got a funky beat
        if (startFromDept) {
            this.fillFolderInfo(true, 0, folder.getDepartment(), /* folder.getDepartment() */folder);
        } else {
            this.fillFolderInfo(true, 0, topCategory, folder);
        }
    }	
	
    /**
     * @param depth
     * @param currentFolder
     * @param showProducts
     * @param isDept
     * @param indent
     * @param p
     * @throws FDResourceException
     */
    @Override
    protected void fillProducts(int depth, CategoryModel currentFolder, boolean showProducts, int indent, ProductCounts p) throws FDResourceException {
        String _catId = ((CategoryModel) currentFolder).getContentKey().getId();

        if (catId.equals(_catId)) {
            if (childCatMap.containsKey(catId)) {
                childCatMap.remove(catId);
            } else if (ContentNodeModel.TYPE_DEPARTMENT.equalsIgnoreCase(currentFolder.getParentNode().getParentNode().getContentType())) {
                childCatMap.remove(currentFolder.getParentNode().getContentName());
            }
            List<DomainValue> sideNavDomainVals = new ArrayList<DomainValue>();
            List<DomainValue> sideNavUsableDomainVals = new ArrayList<DomainValue>();
            List<DomainNavigationElement> domainNavElem = new ArrayList<DomainNavigationElement>();
            sideNavDomainVals = getSideNavDomainValues((CategoryModel) currentFolder);
            Collection<ProductModel> products = ((CategoryModel) currentFolder).getProducts();
            p.prodCount = products.size();
            p.displayableProds = p.prodCount;
            boolean hasDomainVals = false;
            if (sideNavDomainVals != null && sideNavDomainVals.size() > 0) {
                hasDomainVals = true;
            }
            for (Iterator<ProductModel> i = products.iterator(); i.hasNext();) {
                ProductModel prod = i.next();
                if (prod.isDiscontinued() || prod.isInvisible()) {
                    p.displayableProds--; // ok..decrement now
                    continue;
                }
                List<DomainValue> prodDomainValue = new ArrayList<DomainValue>();
                if (showProducts && hasDomainVals) {// Category has filter by
                                                    // (wine region and wine
                                                    // varietal) domain.
                    prodDomainValue = prod.getNewWineRegion();
                    prodDomainValue.addAll(prod.getWineVarietal());
                    if (prodDomainValue != null && prodDomainValue.size() > 0) {
                        for (int j = 0; j < prodDomainValue.size(); j++) {
                            DomainValue dValue = (DomainValue) prodDomainValue.get(j);
                            if (sideNavDomainVals.contains(dValue) && !sideNavUsableDomainVals.contains(dValue)) {
                                sideNavUsableDomainVals.add(dValue);
                                domainNavElem.add(new DomainNavigationElement(depth + indent, (CategoryModel) currentFolder, false, dValue, moreOptions));
                                // this.navList.add(new
                                // DomainNavigationElement(depth+indent,
                                // (CategoryModel) f, isDept,dValue ));
                            }
                        }
                    }
                }
                if (prod.isUnavailable()) {
                    if (showProducts) {
                        p.displayableProds--; // decrement if we would have
                                              // displayed this prod
                        this.unavailableList.add(new ProductNavigationElement(1, prod));
                    }
                } else if (showProducts && !hasDomainVals) {
                    this.navList.add(new ProductNavigationElement(depth + indent, prod));
                }
            }
            if (showProducts && hasDomainVals) {
                Collections.sort(domainNavElem, NAV_NAME_COMPARATOR);
                this.navList.addAll(domainNavElem);
            }
        } else if (ContentNodeModel.TYPE_DEPARTMENT.equalsIgnoreCase(currentFolder.getParentNode().getContentType())) {
            if (EnumShowChildrenType.ALWAYS.equals(((CategoryModel) currentFolder).getShowChildren())) {
                List<CategoryModel> childCategories = ((CategoryModel) currentFolder).getSubcategories();
                if (!childCatMap.containsKey(((CategoryModel) currentFolder).getContentName())) {
                    childCatMap.put(((CategoryModel) currentFolder).getContentName(), childCategories);
                }
            }
        }
    }

    private List<DomainValue> getSideNavDomainValues(CategoryModel model) {
        List<DomainValue> domainValues = new ArrayList<DomainValue>();
        if (moreOptions) {
            List<Domain> domains = model.getWineSideNavFullList();
            if (domains != null && domains.size() > 0) {
                Domain dName = domains.get(0);
                domainValues = dName.getDomainValues();
                showMoreOptions = Boolean.FALSE;
            }
        } else {
            domainValues = model.getWineSideNavSections();
            if (domainValues != null && domainValues.size() == 0) {

                List<Domain> domains = model.getWineSideNavFullList();
                if (domains != null && domains.size() > 0) {
                    Domain dName = domains.get(0);
                    domainValues = dName.getDomainValues();
                    showMoreOptions = Boolean.FALSE;
                }
            } else {
                List<Domain> _dVal = model.getWineSideNavFullList();
                if (_dVal != null && _dVal.size() > 0) {
                    showMoreOptions = Boolean.TRUE;
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
							"java.util.List<com.freshdirect.webapp.taglib.fdstore.NavigationElement>", true, VariableInfo.NESTED),
					new VariableInfo(data.getAttributeString("childCatMap"),
							"java.util.Map<String, List<com.freshdirect.fdstore.content.CategoryModel>>", true, VariableInfo.NESTED),
					new VariableInfo(
							data.getAttributeString("showMoreOptions"),
							"java.lang.Boolean", true, VariableInfo.NESTED),
					new VariableInfo(
							data.getAttributeString("unavailableList"),
							"java.util.List<com.freshdirect.webapp.taglib.fdstore.NavigationElement>", true, VariableInfo.NESTED),
					new VariableInfo(data.getAttributeString("topCategory"),
							"com.freshdirect.fdstore.content.CategoryModel",
							true, VariableInfo.NESTED)
			};

		}
	}	    

}
