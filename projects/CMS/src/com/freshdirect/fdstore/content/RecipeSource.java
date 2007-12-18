package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDStoreProperties;

public class RecipeSource extends ContentNodeModelImpl {

	public final static Comparator NAME_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			RecipeSource r1 = (RecipeSource) o1;
			RecipeSource r2 = (RecipeSource) o2;
			return r1.getName().compareTo(r2.getName());
		}
	};

	public final static Comparator NAME_COMPARATOR_NO_THE = new Comparator() {
		public int compare(Object o1, Object o2) {
			RecipeSource r1 = (RecipeSource) o1;
			RecipeSource r2 = (RecipeSource) o2;
			String n1 = r1.getName().toUpperCase().startsWith("THE ") ? r1.getName().substring(4) : r1.getName();
			String n2 = r2.getName().toUpperCase().startsWith("THE ") ? r2.getName().substring(4) : r2.getName();
			return n1.compareTo(n2);
		}
	};

	
	private final List authors = new ArrayList();

	private final List featuredRecipes = new ArrayList();

	private final List featuredProducts = new ArrayList();

	private final List bookRetailers = new ArrayList();

	public RecipeSource(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Find all available (non-orphan) RecipeSources.
	 * 
	 * @TODO check production status
	 * 
	 * @return List of RecipeSource, sorted by name
	 */
	public static List findAllAvailable() {
		return findAllAvailable(false);
	}
	
	
	public static List findAllAvailable(boolean ignoreLeadingThe) {
		Set keys = CmsManager.getInstance().getContentKeysByType(
				FDContentTypes.RECIPE_SOURCE);
		List l = new ArrayList(keys.size());
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey k = (ContentKey) i.next();
			RecipeSource rs = (RecipeSource) ContentFactory.getInstance()
					.getContentNode(k.getId());
			if (!rs.isOrphan() && rs.isAvailable()) {
				l.add(rs);
			}
		}
		if(ignoreLeadingThe) {
			Collections.sort(l, NAME_COMPARATOR_NO_THE);
		} else {
			Collections.sort(l, NAME_COMPARATOR);
		}
		return l;
	}

	public String getProductionStatus() {
		return getAttribute("productionStatus", EnumProductionStatus.PENDING);
	}

	public boolean isAvailable() {
		if (FDStoreProperties.getPreviewMode()) {
			return true;
		}
		return EnumProductionStatus.ACTIVE.equals(getProductionStatus());
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public String getIsbn() {
		return getAttribute("ISBN", "");
	}

	public String getType() {
		return getAttribute("type", "");
	}

	public String getNotes() {
		return getAttribute("notes", "");
	}

	public List getAuthors() {
		ContentNodeModelUtil.refreshModels(this, "authors", authors, false);
		return Collections.unmodifiableList(authors);
	}
	
	/**
	 * @see RecipeAuthor#authorsToString(List)
	 */
	public String getAuthorNames() {
		return RecipeAuthor.authorsToString(getAuthors());
	}

	public List getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	public Html getLeftContent() {
		return (Html) getAttribute("leftContent", (Html) null);
	}
	
	public Html getTopContent() {
		return (Html) getAttribute("topContent", (Html) null);
	}
	
	public Html getBottomContent() {
		return (Html) getAttribute("bottomContent", (Html) null);
	}
	
	public Html getEmailContent() {
		return (Html) getAttribute("emailContent", (Html) null);
	}
	
	public List getFeaturedProducts() {
		ContentNodeModelUtil.refreshModels(this, "featuredProducts", featuredProducts, false);
		return Collections.unmodifiableList(featuredProducts);
	}
	
	public List getBookRetailers() {
		ContentNodeModelUtil.refreshModels(this, "bookRetailers", bookRetailers, false);
		return Collections.unmodifiableList(bookRetailers );
	}
	
	public Image getImage() {
		return (Image) getAttribute("image", (Image) null);
	}
	
	public Image getZoomImage() {
		return (Image) getAttribute("zoomImage", (Image) null);
	}
	
}
