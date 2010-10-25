package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class DepartmentModel extends ProductContainer {

	private final List<ProductModel> featuredProductModels = new ArrayList<ProductModel>();

	private final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
	
	private final List<CategoryModel> deptNav = new ArrayList<CategoryModel>();
	
	private final List<CategoryModel> featuredCategories = new ArrayList<CategoryModel> ();

        private final List<TileList> tileList = new ArrayList<TileList> ();
	
	
	public DepartmentModel(ContentKey cKey) {
		super(cKey);
	}


	public Image getTitleImage() {
        return FDAttributeFactory.constructImage(this, "DEPT_TITLE");
	}

	public Image getPhoto() {
        return FDAttributeFactory.constructImage(this, "DEPT_PHOTO");
	}

	public Image getPhotoSmall() {
        return FDAttributeFactory.constructImage(this, "DEPT_PHOTO_SMALL");
	}

	public Image getMgrPhotoNoName() {
        return FDAttributeFactory.constructImage(this, "DEPT_MGR_NONAME");
	}

	public Image getGlobalNavBar() {
        return FDAttributeFactory.constructImage(this, "DEPT_NAVBAR");
	}

	public Image getGlobalNavBarRollover() {
        return FDAttributeFactory.constructImage(this, "DEPT_NAVBAR_ROLLOVER");
	}
	
	public boolean isUseAlternateImages() {
		return getAttribute( "USE_ALTERNATE_IMAGES", false );
	}
	
	public boolean isHidddenInQuickshop() {
	    return getAttribute("HIDE_IN_QUICKSHOP", false);
	}
	
   /**
     * this is a Department level attribute, ASSOC_EDITORIAL
     */
    @SuppressWarnings("unchecked")
    public List<Html> getAssocEditorial() {
        return (List<Html>) FDAttributeFactory.constructWrapperList(this, "ASSOC_EDITORIAL");
    }

	/**
	 * @return List of Html
	 */
    @SuppressWarnings("unchecked")
	public List<Html> getDepartmentBottom() {
        return FDAttributeFactory.constructWrapperList(this, "DEPARTMENT_BOTTOM");
	}
	
	/**
	 * @return List of Html
	 */
    @SuppressWarnings("unchecked")
	public List<Html> getDepartmentMiddleMedia() {
        return FDAttributeFactory.constructWrapperList(this, "DEPARTMENT_MIDDLE_MEDIA");
	}
	

	public int getMaxRowCount() {
		return getAttribute("MAX_ROWCOUNT", 1);
	}

	public List<CategoryModel> getCategories() {
		ContentNodeModelUtil.refreshModels(this, "categories", categoryModels, true);
		return new ArrayList<CategoryModel>(categoryModels);
	}

    public List<CategoryModel> getFeaturedCategories() {
        ContentNodeModelUtil.refreshModels(this, "FEATURED_CATEGORIES", featuredCategories, false);
        return new ArrayList<CategoryModel>(featuredCategories);
    }


    public List<CategoryModel> getDeptNav() {
        ContentNodeModelUtil.refreshModels(this, "DEPT_NAV", deptNav, false);
        return new ArrayList<CategoryModel>(deptNav);
    }	

	@Override
	public List<CategoryModel> getSubcategories() {
	    return getCategories();
	}

	@Override
	public List<ProductModel> getFeaturedProducts() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(
			this,
			"FEATURED_PRODUCTS",
			featuredProductModels,
			false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(this, featuredProductModels);
		}
		return new ArrayList<ProductModel>(featuredProductModels);
	}
	
	public Html getDeptStorageGuideMedia() {
	    return FDAttributeFactory.constructHtml(this, "DEPT_STORAGE_GUIDE_MEDIA");
	}


	public static Comparator<DepartmentModel> DepartmentNameComparator = new Comparator<DepartmentModel>() {
		public int compare( DepartmentModel dept1, DepartmentModel dept2 ) {
			String name1 = dept1.getFullName().toLowerCase();
			String name2 = dept2.getFullName().toLowerCase();
			return (name1.compareTo(name2));
		}
	};

    public Set<ContentKey> getAllChildProductKeys() {
    	Set<ContentKey> keys = new HashSet<ContentKey>();
    	for (CategoryModel c : getCategories())
    		keys.addAll(c.getAllChildProductKeys());
    	return keys;
    }

	public String getTemplatePath() {
		return getAttribute("DEPARTMENT_TEMPLATE_PATH", "");
	}

	public List<TileList> getTileList() {
		ContentNodeModelUtil.refreshModels(this, "tile_list", tileList, false);
		return Collections.unmodifiableList(tileList);
	}
}
