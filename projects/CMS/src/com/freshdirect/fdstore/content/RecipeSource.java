package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeSource extends ContentNodeModelImpl {

	public final static Comparator<RecipeSource> NAME_COMPARATOR = new Comparator<RecipeSource>() {
		public int compare(RecipeSource r1, RecipeSource r2) {
			return r1.getName().compareTo(r2.getName());
		}
	};

	public final static Comparator<RecipeSource> NAME_COMPARATOR_NO_THE = new Comparator<RecipeSource>() {
		public int compare(RecipeSource r1, RecipeSource r2) {
			String n1 = r1.getName().toUpperCase().startsWith("THE ") ? r1.getName().substring(4) : r1.getName();
			String n2 = r2.getName().toUpperCase().startsWith("THE ") ? r2.getName().substring(4) : r2.getName();
			return n1.compareTo(n2);
		}
	};

	
	private final List<RecipeAuthor> authors = new ArrayList<RecipeAuthor>();

	private final List<Recipe> featuredRecipes = new ArrayList<Recipe>();

	private final List<ProductModel> featuredProducts = new ArrayList<ProductModel>();

	private final List<BookRetailer> bookRetailers = new ArrayList<BookRetailer>();

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
	public static List<RecipeSource> findAllAvailable() {
		return findAllAvailable(false);
	}
	
	
	public static List<RecipeSource> findAllAvailable(boolean ignoreLeadingThe) {
		Set<ContentKey> keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.RECIPE_SOURCE);
		List<RecipeSource> l = new ArrayList<RecipeSource>(keys.size());
		for ( ContentKey k : keys ) {
			RecipeSource rs = (RecipeSource) ContentFactory.getInstance().getContentNode(k.getId());
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

	public List<RecipeAuthor> getAuthors() {
		ContentNodeModelUtil.refreshModels(this, "authors", authors, false);
		return Collections.unmodifiableList(authors);
	}
	
	/**
	 * @see RecipeAuthor#authorsToString(List)
	 */
	public String getAuthorNames() {
		return RecipeAuthor.authorsToString(getAuthors());
	}

	public List<Recipe> getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	public Html getLeftContent() {
		return FDAttributeFactory.constructHtml( this, "leftContent" );
	}

	public Html getTopContent() {
		return FDAttributeFactory.constructHtml( this, "topContent" );
	}

	public Html getBottomContent() {
		return FDAttributeFactory.constructHtml( this, "bottomContent" );
	}

	public Html getEmailContent() {
		return FDAttributeFactory.constructHtml( this, "emailContent" );
	}
	
	public List<ProductModel> getFeaturedProducts() {
		ContentNodeModelUtil.refreshModels(this, "featuredProducts", featuredProducts, false);
		return Collections.unmodifiableList(featuredProducts);
	}
	
	public List<BookRetailer> getBookRetailers() {
		ContentNodeModelUtil.refreshModels(this, "bookRetailers", bookRetailers, false);
		return Collections.unmodifiableList(bookRetailers );
	}
	
	public Image getImage() {
	    return FDAttributeFactory.constructImage(this, "image");
	}
	
	public Image getZoomImage() {
        return FDAttributeFactory.constructImage(this, "zoomImage");
	}
	
}
