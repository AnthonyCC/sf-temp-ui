package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.Attribute;

public class DepartmentModel extends ContentNodeModelImpl {

	private final List featuredProductModels = new ArrayList();

	private final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

	public DepartmentModel(ContentKey cKey) {
		super(cKey);
	}


	public Image getTitleImage() {
		return (Image) getAttribute("DEPT_TITLE", (Image) null);
	}

	public Image getPhoto() {
		return (Image) getAttribute("DEPT_PHOTO", (Image) null);
	}

	public Image getPhotoSmall() {
		return (Image) getAttribute("DEPT_PHOTO_SMALL", (Image) null);
	}

	public Image getMgrPhoto() {
		return (Image) getAttribute("DEPT_MGR", (Image) null);
	}

	public Image getMgrPhotoNoName() {
		return (Image) getAttribute("DEPT_MGR_NONAME", (Image) null);
	}

	public Image getGlobalNavBar() {
		return (Image) getAttribute("DEPT_NAVBAR", (Image) null);
	}

	public Image getGlobalNavBarRollover() {
		return (Image) getAttribute("DEPT_NAVBAR_ROLLOVER", (Image) null);
	}
	
	public List getDeptNav() {
		return (List)getAttribute( "DEPT_NAV", (Object)null );
	}
	
	public boolean isUseAlternateImages() {
		return getAttribute( "USE_ALTERNATE_IMAGES", false );
	}
	
	/**
	 * @return List of Html
	 */
	public List getAssocEditorial() {
		Attribute a = getAttribute("ASSOC_EDITORIAL");
		return (List) (a==null ? null : a.getValue());
	}
	
	/**
	 * @return List of Html
	 */
	public List getDepartmentBottom() {
		Attribute a = getAttribute("DEPARTMENT_BOTTOM");
		return (List) (a==null ? null : a.getValue());
	}
	
	/**
	 * @return List of Html
	 */
	public List getDepartmentMiddleMedia() {
		Attribute a = getAttribute("DEPARTMENT_MIDDLE_MEDIA");
		return (List) (a==null ? null : a.getValue());
	}
	

	public int getMaxRowCount() {
		return getAttribute("MAX_ROWCOUNT", 1);
	}

	public List<CategoryModel> getCategories() {
		ContentNodeModelUtil.refreshModels(this, "categories", categoryModels, true);

		return new ArrayList<CategoryModel>(categoryModels);
	}

	public List getFeaturedProducts() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(
			this,
			"FEATURED_PRODUCTS",
			featuredProductModels,
			false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(this, featuredProductModels);
		}
		return new ArrayList(featuredProductModels);
	}

	/* [APPREQ-77] */
	public Html getMediaContent() {
		return (Html)getAttribute("MEDIA_CONTENT",(Html)null);
	}


	public static Comparator DepartmentNameComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			DepartmentModel dept1 = (DepartmentModel) o1;
			DepartmentModel dept2 = (DepartmentModel) o2;
			String name1 = dept1.getFullName().toLowerCase();
			String name2 = dept2.getFullName().toLowerCase();
			return (name1.compareTo(name2));
		}
	};

}
