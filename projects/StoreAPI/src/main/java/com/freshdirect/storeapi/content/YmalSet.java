package com.freshdirect.storeapi.content;

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

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.query.AttributeEqualsPredicate;
import com.freshdirect.storeapi.query.AttributeInPredicate;
import com.freshdirect.storeapi.query.RelationshipAnyPredicate;

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
	private static final Collection<ContentType> ymalProductTypes;

	/**
	 *  The YMAL items in this ymal set.
	 */
	private final List<ContentNodeModel> ymals = new ArrayList<ContentNodeModel>();
	private final List<Recommender> recommenders = new ArrayList<Recommender>();

	static {
		ymalProductTypes = new ArrayList<ContentType>(3);
		ymalProductTypes.add(ContentType.Product);
		ymalProductTypes.add(ContentType.ConfiguredProduct);
		ymalProductTypes.add(ContentType.ConfiguredProductGroup);
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
		Date a = (Date) getCmsAttributeValue("startDate");
		return a == null ? new Date(0) : (Date) a;
	}

	/**
	 *  Get the end date for the YMAL set.
	 *  The YMAL set is not considered to be valid after this date.
	 *
	 *  @return the end date of the YMAL set.
	 */
	public Date getEndDate() {
		Date a = (Date) getCmsAttributeValue("endDate");
		return a == null ? new Date(Long.MAX_VALUE) : (Date) a;
	}

	/**
	 *  Tell if auto-configurable products in this ymal set should be
	 *  auto-configured and presented as configured products.
	 *
	 *  @return true of auto-configurable products should be presented
	 *          in a transactional manner, false otherwise
	 */
	public boolean isTransactional() {
		return getAttribute("transactional", false);
	}

	/**
	 *  Tell if the YMAL set is active.
	 *  A YMAL set is only active, if its workflow status is ACTIVE, and
	 *  the current date is between the start date and the end date.
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
	public List<ContentNodeModel> getYmals() {
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
	@SuppressWarnings( "unchecked" )
	private <T extends ContentNodeModel> List<T> getYmals(ContentType type) {
		List<ContentNodeModel> values = getYmals();
		List<T> l = new ArrayList<T>( values.size() );

		for ( ContentNodeModel  node : values ) {
			ContentKey k = node.getContentKey();
			if ( type.equals( k.type ) ) {
				l.add((T)node);
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
	@SuppressWarnings( "unchecked" )
	private <T extends ContentNodeModel> List<T> getYmals(Collection<ContentType> types) {
		List<ContentNodeModel> values = getYmals();
		List<T> l = new ArrayList<T>( values.size() );

		for ( ContentNodeModel node : values ) {
			ContentKey k = node.getContentKey();
			if (types.contains(k.type)) {
				l.add((T)node);
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
	@Override
    public List<ProductModel> getYmalProducts() {
		return getYmals(ymalProductTypes);
	}

	/**
	 *  Return a list of YMAL categories.
	 *
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	@Override
    public List<CategoryModel> getYmalCategories() {
		return getYmals(ContentType.Category);
	}

	/**
	 *  Return a list of YMAL recipes.
	 *
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	@Override
    public List<Recipe> getYmalRecipes() {
		return getYmals(ContentType.Recipe);
	}

	/**
	 *  Find recipes that have a product as a required ingredient.
	 *  This is used to generate automated cross-sell (YMAL) lists.
	 *
	 *  @param product the product to get the recipes for
	 *  @return a list of Recipe objects, that have the product
	 *          (with any of its SKUs) as a required ingredient.
	 */
	public static Collection<Recipe> getAutoCrossSellRecipes(ProductModel product) {
        ContentFactory      contentFactory;
        Set<ContentKey>     skus;
        Relationship        variantsRelDef;
        Relationship        sectionsRelDef;
        Relationship        ingredientsRelDef;
        Relationship        itemsRelDef;
        Relationship        skuRelDef;
        Scalar              requiredAttrDef;
        Predicate           variantPredicate;
        Predicate           sectionPredicate;
        Predicate           configuredProductPredicate;
        Predicate           configuredProductGroupPredicate;
        Predicate           productPredicate;
        Predicate           requiredUpperCasePredicate;
        Predicate           requiredLowerCasePredicate;
        Predicate           itemsPredicate;
        Predicate           skuPredicate;
        Map<ContentKey,ContentNodeI>    results;
        List<Recipe>        recipes;

        contentFactory = ContentFactory.getInstance();

        // get the skus as ContentKey objects
        skus = new HashSet<ContentKey>();
        for ( String skuCode : product.getSkuCodes() ) {
            ContentKey key = ContentKeyFactory.get( ContentType.Sku, skuCode );
            skus.add( key );
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
        requiredAttrDef   = (Scalar) ContentTypes.ConfiguredProduct.REQUIRED;
        requiredUpperCasePredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

        // the predicate to check on configured products if they have a specific SKU
        skuRelDef       = (Relationship) ContentTypes.ConfiguredProduct.SKU;
        skuPredicate    = new AttributeInPredicate(skuRelDef, skus);

        // the predicate to combine all predicates related to configured products
        configuredProductPredicate = new AndPredicate(requiredUpperCasePredicate, skuPredicate);


        // create the configuredProductGroupPredicate

        // the predicate to check on configured product groups if they are required
        requiredAttrDef   = (Scalar) ContentTypes.ConfiguredProductGroup.required;
        requiredLowerCasePredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

        // the predicate to check on configured products if they have a specific SKU
        // (this is a duplication of the above)
        skuRelDef       = (Relationship) ContentTypes.ConfiguredProduct.SKU;
        skuPredicate    = new AttributeInPredicate(skuRelDef, skus);

        // the predicate to delegate SKU checking to objects in the items relation
        itemsRelDef     = (Relationship) ContentTypes.ConfiguredProductGroup.items;
        itemsPredicate  = new RelationshipAnyPredicate(itemsRelDef, skuPredicate);

        // the predicate to combine all predicates related to configured products
        configuredProductGroupPredicate = new AndPredicate(requiredLowerCasePredicate,
                                                           itemsPredicate);


        // aggregate the configured product and configured product group predicates
        productPredicate = new OrPredicate(configuredProductPredicate,
                                           configuredProductGroupPredicate);


        // the predicate to delegate required-attribute checking to all
        // ingredients in a recipe section
        ingredientsRelDef = (Relationship) ContentTypes.RecipeSection.ingredients;
        sectionPredicate  = new RelationshipAnyPredicate(ingredientsRelDef,
                                                         productPredicate);

        // the predicate to delegate required-attribute checking to all
        // sections in a recipe variant
        sectionsRelDef   = (Relationship) ContentTypes.RecipeVariant.sections;
        variantPredicate = new RelationshipAnyPredicate(sectionsRelDef,
                                                        sectionPredicate);

        // the predicate to delegate required-attribute checking to all
        // variants in a recipe
        variantsRelDef   = (Relationship) ContentTypes.Recipe.variants;
        variantPredicate = new RelationshipAnyPredicate(variantsRelDef,
                                                        variantPredicate);

        results = CmsManager.getInstance().queryContentNodes(ContentType.Recipe, variantPredicate);

        // present the results as a list of Recipe objects
        recipes = new ArrayList<Recipe>();
        for ( ContentKey key : results.keySet() ) {
            Recipe recipe = (Recipe) contentFactory.getContentNode(key.getId());
            recipes.add(recipe);
        }

        return recipes;
	}

	@Override
    public YmalSet getActiveYmalSet() {
		return isActive() ? this : null;
	}

	@Override
    public void resetActiveYmalSetSession() {
	}

	@Override
    public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus) {
		if (removeSkus == null || removeSkus.isEmpty())
			return getYmalProducts();

		List<ProductModel> prods = getYmalProducts();
		ListIterator<ProductModel> it = prods.listIterator();
		while (it.hasNext()) {
			ProductModel p = it.next();
			Iterator<SkuModel> it2 = p.getSkus().iterator();
			outer: while (it2.hasNext()) {
				String skuCode = it2.next().getSkuCode();
				Iterator<FDSku> it3 = removeSkus.iterator();
				while (it3.hasNext()) {
					String removeSkuCode = it3.next().getSkuCode();
					if (skuCode.equals(removeSkuCode)) {
						it.remove();
						break outer;
					}
				}
			}
		}
		return prods;
	}

	@Override
    public List<ProductModel> getRelatedProducts() {
		return Collections.<ProductModel>emptyList();
	}

	@Override
    public String getYmalHeader() {
		return getProductsHeader();
	}

	public String getTitle() {
	    return getAttribute("title", "");
	}

	public List<Recommender> getRecommenders() {
		ContentNodeModelUtil.refreshModels(this, "recommenders", recommenders, false);
		return Collections.unmodifiableList(recommenders);
	}

}
