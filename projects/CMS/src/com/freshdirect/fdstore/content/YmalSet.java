package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.OrPredicate;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.AttributeEqualsPredicate;
import com.freshdirect.cms.query.AttributeInPredicate;
import com.freshdirect.cms.query.RelationshipAnyPredicate;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

/**
 *  A set of You Might Also Like (YMAL) content nodes.
 *  
 *  @see Recommender
 *  @see RecommenderStrategy
 */
public class YmalSet extends ContentNodeModelImpl implements YmalSource {
	private static final long serialVersionUID = 2177290860515807364L;

	/**
	 *  The list of node types to return for getYmalProducts.
	 *  @see #getYmalProducts()
	 */
	private static final Collection ymalProductTypes;
	
	/**
	 *  The YMAL items in this ymal set.
	 */
	private final List ymals = new ArrayList();
	private final List recommenders = new ArrayList();
	
	static {
		ymalProductTypes = new ArrayList(3);
		ymalProductTypes.add(FDContentTypes.PRODUCT);
		ymalProductTypes.add(FDContentTypes.CONFIGURED_PRODUCT);
		ymalProductTypes.add(FDContentTypes.CONFIGURED_PRODUCT_GROUP);
	}

	/**
	 *  Constructor based on the content key.
	 *
	 *  @param cKey the content key of the node.
	 */
	public YmalSet(ContentKey cKey) {
		super(cKey);
	}

	/**
	 *  Return the products header related to the YMAL set.
	 *  This header should be displayed on top of the YMAL list on the
	 *  storefront.
	 *  
	 *  @return the products header related to the YMAL set.
	 *          null if not set for this YMAL set.
	 */
	public String getProductsHeader() {
		return getAttribute("productsHeader", null);
	}
	
	/**
	 *  Return the workflow status of the YMAL set.
	 *  
	 *  @return the workflow status as a string, a constant from
	 *          EnumWorkflowStatus
	 *  @see EnumWorkflowStatus  
	 */
	public String getWorkflowStatus() {
		return getAttribute("workflowStatus", EnumWorkflowStatus.PENDING_APPROVAL);
	}
	
	/**
	 *  Get the start date for the YMAL set.
	 *  The YMAL set is not considered to be valid before this date.
	 *  
	 *  @return the start date of the YMAL set.
	 */
	public Date getStartDate() {
		AttributeI a = getCmsAttribute("startDate");
		return a == null ? new Date(0) : (Date) a.getValue();
	}

	/**
	 *  Get the end date for the YMAL set.
	 *  The YMAL set is not considered to be valid after this date.
	 *  
	 *  @return the end date of the YMAL set.
	 */
	public Date getEndDate() {
		AttributeI a = getCmsAttribute("endDate");
		return a == null ? new Date(Long.MAX_VALUE) : (Date) a.getValue();
	}

	/**
	 *  Tell if auto-configurable products in this ymal set should be
	 *  auto-configured and presented as configured products.
	 *  
	 *  @return true of auto-confiugrable products should be presented
	 *          in a transational manner, false otherwise
	 */
	public boolean isTransactional() {
		return getAttribute("transactional", false);
	}

	/**
	 *  Tell if the YMAL set is active.
	 *  A YMAL set is only active, if its workflow status is ACTIVE, and
	 *  the current date is betwen the start date and the end date.
	 *  If the start date or the end date is not set, only the workflow
	 *  status is checked for.
	 *  
	 *  @return true if the YMAL set is active, false otherwise.
	 *          returns true also if the store is in preview mode
	 */
	public boolean isActive() {
		if (FDStoreProperties.getPreviewMode()) {
			return true;
		}

		if (!EnumWorkflowStatus.ACTIVE.equals(getWorkflowStatus())) {
			return false;
		}
		
		// DateRange is a thoroughly tested class, so let's use it here as well
		Date      now       = new Date();
		DateRange dateRange = new DateRange(getStartDate(), getEndDate());
		if (dateRange.contains(now)) {
			return true;
		}

		return false;
	}
	
	/**
	 *  Returns all the YMAL products within this YmalSet.
	 *  
	 *  @return all YMAL products contained in this Ymal Set,
	 *          which are of ContentNodeModel type
	 */
	public List getYmals() {
		ContentNodeModelUtil.refreshModels(this, "ymals", ymals, false);
		return Collections.unmodifiableList(ymals);
	}
	
	/**
	 *  Return a list of YMAL products, but only of a specified type.
	 *  
	 *  @param type the type of YMAL products to return.
	 *  @return a list of objects of the specified content type, which are
	 *          YMALs of this ymal set.
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
	 *  Return a list of YMAL products, but only of specified types.
	 *  
	 *  @param types a list of ContentType objects, the types of YMALs to
	 *         return
	 *  @return a list of objects of the specified content types, which are
	 *          YMALs of this ymal set.
	 */
	private List getYmals(Collection types) {
		List values = getYmals();
		List l      = new ArrayList(values.size());
		
		for (Iterator i = values.iterator(); i.hasNext(); ) {
			ContentNodeModel  node = (ContentNodeModel) i.next();
			ContentKey        k    = node.getContentKey();
			if (types.contains(k.getType())) {
				l.add(node);
			}
		}
		
		return l;
	}

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @return a list of AbstractProductModel objects, which are contained in
	 *          the YMALs for this product. The returned objects may be of
	 *          ProductModelImpl and ConfiguredProduct type.
	 *  @see #getYmals()
	 */
	public List getYmalProducts() {
		return getYmals(ymalProductTypes);
	}
	
	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List getYmalCategories() {
		return getYmals(FDContentTypes.CATEGORY);
	}
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List getYmalRecipes() {
		return getYmals(FDContentTypes.RECIPE);
	}
	
	/**
	 *  Find recipes that have a product as a required ingredient.
	 *  This is used to generate automated cross-sell (YMAL) lists.
	 *  
	 *  @param product the product to get the recipes for
	 *  @return a list of Recipe objects, that have the product
	 *          (with any of its SKUs) as a required ingredient.
	 */
	public static Collection getAutoCrossSellRecipes(ProductModel product) {		
		ContentTypeServiceI typeService;
		ContentServiceI 	contentService;
		ContentFactory      contentFactory;
		Set 				skus;
		ContentTypeDefI		recipeDef;
		ContentTypeDefI     recipeVariantDef;
		ContentTypeDefI     recipeSectionDef;
		ContentTypeDefI     configuredProductDef;
		ContentTypeDefI     configuredProductGroupDef;
		RelationshipDefI    variantsRelDef;
		RelationshipDefI    sectionsRelDef;
		RelationshipDefI    ingredientsRelDef;
		RelationshipDefI    itemsRelDef;
		RelationshipDefI    skuRelDef;
		AttributeDefI       requiredAttrDef;
		Predicate       	variantPredicate;
		Predicate       	sectionPredicate;
		Predicate           configuredProductPredicate;
		Predicate           configuredProductGroupPredicate;
		Predicate           productPredicate;
		Predicate           requiredUpperCasePredicate;
		Predicate           requiredLowerCasePredicate;
		Predicate           itemsPredicate;
		Predicate           skuPredicate;
		Map					results;
		List				recipes;

		contentService = CmsManager.getInstance();
		typeService    = contentService.getTypeService();
		contentFactory = ContentFactory.getInstance();
		
		recipeDef        = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE);
		recipeVariantDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		recipeSectionDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		configuredProductDef = typeService.getContentTypeDefinition(
												FDContentTypes.CONFIGURED_PRODUCT);
		configuredProductGroupDef = typeService.getContentTypeDefinition(
										FDContentTypes.CONFIGURED_PRODUCT_GROUP);
		
		// get the skus as ContentKey objects
		skus = new HashSet();
		for (Iterator it = product.getSkuCodes().iterator(); it.hasNext(); ) {
			String     		skuCode = (String) it.next();
			ContentKey 		key     = new ContentKey(FDContentTypes.SKU, skuCode);
			
			skus.add(key);
		}
		
		// the predicate built up here looks like this:
		// Recipe
		//   |
		//   |   variantPredicate (check all variants with a sub-predicate)
		//   |
		// RecipeVariant
		//   |
		//   |   sectionPrediacate (check all sections with a sub-predicate)
		//   |
		// RecipeSection
		//   |
		//   |   configuredProductPredicate (check configured products for
		//   |   the required attribute and SKU)
		//   |
		//   |        OR (productPredicate)
		//   |---------------------------------\
		//   |                                 |
		// configuedProductPredicate         configuredProductGroupPredicate
		//
		// where:
		//
		// configuredProductPredicate
		//   |
		//   |                 AND
		//   |-------------------------------------------\
		//   |                                           |
		//   |  requiredUpperCasePredicate               |  skuPredicate
		//   |  (check for the REQUIRED attribute)       |    (check for the SKUs)
		// REQUIRED == true                            SKU == <one of the product's skus>
		//
		//
		// configuredProductGroupPredicate
		//   |
		//   |      AND
		//   |-----------------------------------\
		//   |                                   |
		// required == true                      |  itemsPredicate
		// requiredPredicate                     |  (check all in the items relation)
		//                                    ConfiguredProduct
		//                                       |
		//                                       |  skuPredicate (check for the SKUs)
		//                                     SKU == <one of the product's skus>

		
		// create the configuredProductPredicate
		
		// the predicate to check on configured products if they are required
		requiredAttrDef   = configuredProductDef.getAttributeDef("REQUIRED");
		requiredUpperCasePredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		// the predicate to check on concifgured products if they have a specific SKU
		skuRelDef       = (RelationshipDefI) configuredProductDef.getAttributeDef("SKU");
		skuPredicate    = new AttributeInPredicate(skuRelDef, skus);
		
		// the predicate to combine all predicates related to configured products
		configuredProductPredicate = new AndPredicate(requiredUpperCasePredicate, skuPredicate);

		
		// create the configuredProductGroupPredicate
		
		// the predicate to check on configured product groups if they are required
		requiredAttrDef   = configuredProductGroupDef.getAttributeDef("required");
		requiredLowerCasePredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		// the predicate to check on concifgured products if they have a specific SKU
		// (this is a duplication of the above)
		skuRelDef       = (RelationshipDefI) configuredProductDef.getAttributeDef("SKU");
		skuPredicate    = new AttributeInPredicate(skuRelDef, skus);

		// the predicate to delegate SKU checking to objects in the items relation
		itemsRelDef     = (RelationshipDefI) configuredProductGroupDef.getAttributeDef("items");
		itemsPredicate  = new RelationshipAnyPredicate(itemsRelDef, skuPredicate);

		// the predicate to combine all predicates related to configured products
		configuredProductGroupPredicate = new AndPredicate(requiredLowerCasePredicate,
														   itemsPredicate);

		
		// aggregate the configured product and configured product group predicates
		productPredicate = new OrPredicate(configuredProductPredicate,
				                           configuredProductGroupPredicate);
		
		
		// the predicate to delegate required-attribute checking to all
		// ingredients in a recipe section
		ingredientsRelDef = (RelationshipDefI) recipeSectionDef.getAttributeDef("ingredients");
		sectionPredicate  = new RelationshipAnyPredicate(ingredientsRelDef,
				                                         productPredicate);
		
		// the predicate to delegate required-attribute checking to all
		// sections in a recipe variant
		sectionsRelDef   = (RelationshipDefI) recipeVariantDef.getAttributeDef("sections");
		variantPredicate = new RelationshipAnyPredicate(sectionsRelDef,
				                                        sectionPredicate);

		// the predicate to delegate required-attribute checking to all
		// variants in a recipe
		variantsRelDef   = (RelationshipDefI) recipeDef.getAttributeDef("variants");
		variantPredicate = new RelationshipAnyPredicate(variantsRelDef,
				                                        variantPredicate);

		results         = contentService.queryContentNodes(FDContentTypes.RECIPE,
												           variantPredicate);
		
		// present the results as a list of Recipe objects
		recipes = new ArrayList();
		for (Iterator it = results.keySet().iterator(); it.hasNext();) {
			ContentKey key = (ContentKey) it.next();
			Recipe recipe = (Recipe) contentFactory.getContentNode(key.getId());
			recipes.add(recipe);
		}

		return recipes;
	}

	public YmalSet getActiveYmalSet() {
		return isActive() ? this : null;
	}
	
	public void resetActiveYmalSetSession() {
	}

	public List getYmalProducts(Set removeSkus) {
		if (removeSkus == null || removeSkus.isEmpty())
			return getYmalProducts();
		
		List prods = getYmalProducts();
		ListIterator it = prods.listIterator();
		while (it.hasNext()) {
			ProductModel p = (ProductModel) it.next();
			Iterator it2 = p.getSkus().iterator();
			outer: while (it2.hasNext()) {
				String skuCode = ((SkuModel) it2.next()).getSkuCode();
				Iterator it3 = removeSkus.iterator();
				while (it3.hasNext()) {
					String removeSkuCode = ((FDSku) it3.next()).getSkuCode();
					if (skuCode.equals(removeSkuCode)) {
						it.remove();
						break outer;
					}
				}
			}
		}
		return prods;
	}
	
	public List getRelatedProducts() {
		return Collections.EMPTY_LIST;
	}
	
	public String getYmalHeader() {
		return getProductsHeader();
	}
	
	public List getRecommenders() {
		ContentNodeModelUtil.refreshModels(this, "recommenders", recommenders, false);
		return Collections.unmodifiableList(recommenders);
	}

}
