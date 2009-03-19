package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

public class Recipe extends ContentNodeModelImpl implements ContentStatusI, YmalSource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1726859705342564086L;

	private final List authors = new ArrayList();

	private final List variants = new ArrayList();

	private final List classifications = new ArrayList();
	
	private final ContentStatusHelper helper;

	/**
	 *  The list of YMAL products related to this recipe.
	 */
	private final List ymals = new ArrayList();
	
	/**
	 *  The list of YmalSet objects related to this recipe.
	 */
	private final List ymalSets = new ArrayList();
	
	public Recipe(ContentKey cKey) {
		super(cKey);
		helper = new ContentStatusHelper(this);
	}

	public String getFullName() {
		return this.getName();
	}
	
	public String getName() {
		return getAttribute("name", "");
	}

	public String getLabel() {
		return getAttribute("label", "");
	}

	public String getKeywords() {
		return getAttribute("keywords", "");
	}

	public String getNotes() {
		return getAttribute("notes", "");
	}

	public String getProductionStatus() {
		return getAttribute("productionStatus", EnumProductionStatus.PENDING);
	}

	public DateRange getValidDateRange() {
		return new DateRange(getStartDate(), getEndDate());
	}
	
	public Date getStartDate() {
		return helper.getStartDate();
	}

	public Date getEndDate() {
		return helper.getEndDate();
	}
	
	public String getThemeColor() {
		String themeColor = getAttribute("theme_color", "DEFAULT");
		return "DEFAULT".equals(themeColor) ? "" : themeColor;
	}
	
	public RecipeSource getSource() {
		AttributeI attrib = getCmsAttribute("source");
		if (attrib==null) return null;
		ContentKey key = (ContentKey) attrib.getValue();
		return key == null ? null : (RecipeSource) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public List getAuthors() {
		ContentNodeModelUtil.refreshModels(this, "authors", authors, false);
		return Collections.unmodifiableList(authors);
	}
	
	/**
	 * @see RecipeAuthor#authorsToString(List)
	 */
	public String getAuthorNames() {
		List authors = !this.authors.isEmpty() ? this.authors
				: (getSource() != null ? getSource().getAuthors()
						: Collections.EMPTY_LIST);			
		return RecipeAuthor.authorsToString(authors);
	}
	
	/**
	 *  Return all variants of this recipe.
	 *  
	 *  @return a list of RecipeVariant objects.
	 */
	public List getVariants() {
		ContentNodeModelUtil.refreshModels(this, "variants", variants, true);
		return Collections.unmodifiableList(variants);
	}
	
	/**
	 *  Return the available variants of this recipe.
	 *  
	 *  @return a list of RecipeVariant objects.
	 */
	public List getAvailableVariants() {
		List l = new ArrayList(getVariants());
		for (Iterator i = l.iterator(); i.hasNext();) {
			RecipeVariant v = (RecipeVariant) i.next();
			if (!v.isAvailable()) {
				i.remove();
			}
		}
		return l;
	}

	public RecipeVariant getDefaultVariant() {
		List variants = this.getAvailableVariants();
		for (Iterator i = variants.iterator(); i.hasNext();) {
			RecipeVariant v = (RecipeVariant) i.next();
			if ("default".equalsIgnoreCase(v.getName())) {
				return v;
			}
		}
		if (variants.size() >= 1) {
			return (RecipeVariant) variants.get(0);
		}
		return null;
	}

	public List getClassifications() {
		ContentNodeModelUtil.refreshModels(this, "classifications", classifications, false);
		return Collections.unmodifiableList(classifications);
	}

	/**
	 *  Return all the YMAL products, of all types, that are related to this
	 *  product. The returned list will not contain any items from YMAL sets
	 *  related to this product.
	 *  
	 *  @return a list of content nodes that are YMALs to this product.
	 *  @deprecated
	 */
	public List getYouMightAlsoLike() {
		return getYmals();
	}
	
	/**
	 *  Return all YMAL products for this recipe.
	 *  
	 *  @return a list of all related products for this recipe, of type
	 *          ContentNode Model
	 */
	private List getYmals() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_PRODUCTS", ymals, false, true);
		return Collections.unmodifiableList(ymals);
	}
	
	/**
	 *  Return a list of YMAL products, but only of a specified type.
	 *  
	 *  @param type the type of YMAL products to return.
	 *  @return a list of objects of the specified content type, which are
	 *          YMALs of this product.
	 *  @see #getYouMightAlsoLike()
	 */
	private List getYmals(ContentType type) {
		List values = getYmals();
		List l      = new ArrayList(values.size());
		
		for (Iterator i = values.iterator(); i.hasNext(); ) {
			ContentNodeModel  node = (ContentNodeModel) i.next();
			ContentKey        k    = node.getContentKey();
			if (type.equals(k.getType())) {
				l.add(node);
			}
		}
		
		return l;
	}

	/**
	 *  Return all Ymal Sets related to this recipe.
	 *  
	 *  @return the list of all YmalSet objects related to this recipe.
	 */
	private List getYmalSets() {
		ContentNodeModelUtil.refreshModels(this, "ymalSets", ymalSets, false, true);
		return Collections.unmodifiableList(ymalSets);
	}
	
	/**
	 *  Return the active YmalSet, if any.
	 *  
	 *  @return the active YmalSet, or null if there's no active Ymal Set.
	 *  @see YmalSet
	 */
	public YmalSet getActiveYmalSet() {
		List ymalSets = getYmalSets();
		for (Iterator it = ymalSets.iterator(); it.hasNext(); ) {
			YmalSet     ymalSet = (YmalSet) it.next();
			
			if (ymalSet.isActive()) {
				return ymalSet;
			}
		}
	
		return null;
	}

	/**
	 *  Remove discontinued and unavailable products from a product set.
	 *
	 *  @param products a set of ProductModel objects.
	 *         this set may be changed by this function call.
	 */
	private void removeDiscUnavProducts(Set products) {
		for (Iterator it = products.iterator(); it.hasNext(); ) {
			ProductModel product = (ProductModel) it.next();
			
			if (product.isUnavailable() || product.isDiscontinued()) {
				it.remove();
			}
		}
	}

	/**
	 *  Remove this very product of a product set, if contained within.
	 *
	 *  @param products a set of ProductModel objects.
	 *         this set may be changed by this function call.
	 */
	private void removeSelf(Set products) {
		if (products.contains(this)) {
			products.remove(this);
		}
	}

	/**
	 *  Remove unavailable recipes from a recipe set.
	 *
	 *  @param recipes a set of Recipe objects.
	 *         this set may be changed by this function call.
	 */
	private void removeUnavRecipes(Set recipes) {
		for (Iterator it = recipes.iterator(); it.hasNext(); ) {
			Recipe recipe = (Recipe) it.next();
			
			if (!recipe.isAvailable()) {
				it.remove();
			}
		}
	}

	/**
	 *  Remove products from the YMAL set that correspond to the supplied set
	 *  of SKUs.
	 *
	 *  @param products a collection of Product objects.
	 *         this set may be changed by this function call.
	 *  @param removeSkus a set of FDSku object, for which correspodning
	 *         products will be removed.
	 */
	private void removeSkus(Collection products, Set removeSkus) {
		if (removeSkus == null) return ;
		for (Iterator it = products.iterator(); it.hasNext(); ) {
			ProductModel product = (ProductModel) it.next();
			
inner:
			for (Iterator iit = product.getSkus().iterator(); iit.hasNext(); ) {
				SkuModel sku = (SkuModel) iit.next();

				for (Iterator iiit = removeSkus.iterator(); iiit.hasNext(); ) {
					FDSku fdSku = (FDSku) iiit.next();
					
					if (sku.getSkuCode().equals(fdSku.getSkuCode())) {
						it.remove();
						break inner;
					}
				}
			}
		}
	}

	/**
	 *  Return a list of YMAL products.
	 *  A helper function, for a complete description, {@link #getYmalProducts(Set)}
	 *  Calls getYmalProducts(null)
	 *  
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmalProducts(Set)
	 */
	public List getYmalProducts() {
		return getYmalProducts(null);
	}

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of the PRODUCT are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - discontinued and unavailable products are removed from the list
	 *  - the product shown on the page is removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  TODO:
	 *  - auto-configure products which can be auto-configured if the active
	 *    YMAL set has the 'transactional' property set
	 *  - generate auto cross-sell if otherwise no YMAL items would be displayed
	 *
	 *  @param removeSkus a set of FDSku objects, for which correspoding
	 *         products need to be removed from the final list of YMALs.
	 *         this might be null, in which case it has no effect. 
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 */
	public List getYmalProducts(Set removeSkus) {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.PRODUCT));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalProducts());
		}
		
		removeDiscUnavProducts(ymals);
		removeSelf(ymals);
		removeSkus(ymals, removeSkus);

		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList(size);
		// cut the list to be at most 6 items in size
		for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
			finalList.add(it.next());
		}
		
		return finalList;
	}

	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of the CATEGORY are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - the product shown on the page is removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 */
	public List getYmalCategories() {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.CATEGORY));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalCategories());
		}
		
		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList(size);
		// cut the list to be at most 6 items in size
		for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
			finalList.add(it.next());
		}
		
		return finalList;
	}
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of type RECIPE are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - unavailable recipes are removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYouMightAlsoLike()
	 */
	public List getYmalRecipes() {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.RECIPE));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalRecipes());
		}
		
		removeUnavRecipes(ymals);
		
		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList(size);
		// cut the list to be at most 6 items in size
		for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
			finalList.add(it.next());
		}
		
		return finalList;
	}

	public List getRelatedProducts() {
		return getYouMightAlsoLike();
	}

	public Html getDescription() {
		return (Html) getAttribute("description", (Html) null);
	}
	
	public Html getIngredientsMedia() {
		return (Html) getAttribute("ingredientsMedia", (Html) null);
	}

	public Html getPreparationMedia() {
		return (Html) getAttribute("preparationMedia", (Html) null);
	}
	
	public Html getCopyrightMedia() {
		return (Html) getAttribute("copyrightMedia", (Html) null);
	}
	
	public Image getPhoto() {
		return (Image) getAttribute("photo", (Image) null);
	}

	public Image getLogo() {
		return (Image) getAttribute("logo", (Image) null);
	}
	
	public boolean isAvailable() {
		// always show in preview mode
		if (FDStoreProperties.getPreviewMode()) {
			return true;
		}
		
		// check status
		if (isPending() || isCompleted()) {
			return false;
		}

		// check date
		if (!getValidDateRange().contains(new Date())) {
			return false;
		}
		
		// check if there's at least available variant
		for (Iterator i = getVariants().iterator(); i.hasNext();) {
			RecipeVariant v = (RecipeVariant) i.next();
			if (v.isAvailable()) {
				return true;
			}
		}
		
		return false;
	}
	public String getDisplayableSource() {
		return getDisplayableSource(false);
	}
	public String getDisplayableSource(boolean linkSource) {
		  RecipeSource source = this.getSource();
		  String sourceName="";
		  if (source!=null) {
			  if (linkSource){
			  		sourceName="<a href=\"javascript:popup('/recipe_source.jsp?recipeId="+ this.getContentName() +"&trk=rec','large_long')\">\""+source.getName() +"\"</a>";
			  } else {
			  		sourceName="\""+source.getName() +"\"";
			  }
		  }
		  //StringBuffer authorNames = new StringBuffer("");
		  //boolean recipeHasAuthors = true;
		  
         return createDisplayableSource(sourceName, !this.authors.isEmpty()  ? this.authors 
				  : (source!=null  ? source.getAuthors() : Collections.EMPTY_LIST),	!this.authors.isEmpty() );	
          
		  
	}
	
	private String createDisplayableSource(String sourceName,List authors,boolean isRecipeAuthor) {
		StringBuffer authorNames = new StringBuffer();
		StringBuffer displayString = new StringBuffer();
		
		for (int i = 0; i<authors.size();i++) {
	   		if (i==0) {
	   			authorNames.append("by ");
	   		}
	   		
	   		if (i == authors.size()-1 && authors.size() > 1) {
	   			authorNames.append(" and ");
	   		}  else if (i!=0){
	   			authorNames.append(", ");
	   		}
	   		authorNames.append( ((RecipeAuthor)authors.get(i)).getName());
		  }


		  if (isRecipeAuthor) {
		  	displayString.append(authorNames.toString());
			if (sourceName!=null && !"".equals(sourceName.trim())) {
				displayString.append(" from " + sourceName);
			}
		  }
		  else { 
			if (sourceName!=null && !"".equals(sourceName.trim())) {
				displayString.append("from " + sourceName);
			}
		  	displayString.append(" ").append(authorNames.toString() );
		  }
		  
		  return displayString.toString();
	  
	}
	
	
	/**
	 * Find the first {@link RecipeSubcategory} that has a classification
	 * matching this recipe.
	 * 
	 * @return null if no matching subcategories are found
	 */
	public RecipeSubcategory getPrimarySubcategory(){
		RecipeDepartment rcpDept = RecipeDepartment.getDefault();
		if (rcpDept == null) {
			return null;
		}
		List classifications = this.getClassifications();
		for (Iterator itrC = rcpDept.getCategories().iterator(); itrC.hasNext();) {
			RecipeCategory rcpCat = (RecipeCategory) itrC.next();
			for (Iterator itrS = rcpCat.getSubcategories().iterator(); itrS.hasNext();) {
				RecipeSubcategory rcpSubCat = (RecipeSubcategory) itrS.next();
				if (classifications.contains(rcpSubCat.getClassification())) {
					return rcpSubCat;
				}

			}
		}
		return null;
	}
	
	public boolean isPending() {
		return EnumProductionStatus.PENDING.equals(getProductionStatus());
	}
	
	public boolean isActive() {
		return EnumProductionStatus.ACTIVE.equals(getProductionStatus());
	}
	
	public boolean isLimited() {
		return EnumProductionStatus.LIMITED.equals(getProductionStatus());
	}
	
	public boolean isCompleted() {
		return EnumProductionStatus.COMPLETED.equals(getProductionStatus());
	}

	public String getPath() {
		return "www.freshdirect.com/rec/"
				+ (getSource() == null ? "null" : getSource().getContentName())
				+ "/" + getContentName();
	}
	
	public String getYmalHeader() {
		final YmalSet activeYmalSet = getActiveYmalSet();
		if (activeYmalSet != null)
			return activeYmalSet.getProductsHeader();
		else
			return null;
	}
}
