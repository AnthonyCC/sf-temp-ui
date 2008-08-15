package com.freshdirect.fdstore.content;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.AttributeEqualsPredicate;
import com.freshdirect.cms.query.RelationshipAnyPredicate;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.content.ProductAutoconfigureTest.FDFactoryProductAspect;
import com.freshdirect.fdstore.content.ProductAutoconfigureTest.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;

/**
 *  Test case for automated cross-sell functioniality, used with YMALs.
 *  This basically means looking up recipes that have a certain product (SKU)
 *  as a required ingredient.
 */
public class YmalAutomatedCrossSellTest extends FDCustomerManagerTestSupport {

	public YmalAutomatedCrossSellTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/** The content service used during the test. */
	ContentServiceI service;
	
	/** The content type service used during the test. */
	ContentTypeServiceI typeService;
	
	public void setUp() throws Exception {
		super.setUp();
		
		List list = new ArrayList();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/ConfiguredProducts.xml");

		CmsManager.setInstance(new CmsManager(service, null));
		
		Context            context            = null;
		Hashtable          env                = new Hashtable();
		MockContextFactory mockContextFactory = new MockContextFactory();

		// set the context factory to the mockejb context factory
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );
		
		MockContextFactory.setAsInitial();
		context = mockContextFactory.getInitialContext(env);

		aspectSystem.add(new FDFactoryProductAspect());
		aspectSystem.add(new FDFactoryProductInfoAspect());		
	}
	
	public void tearDown() {
		MockContextFactory.revertSetAsInitial();
	}

	/**
	 *  Just a very little smoke test.
	 */
	public void testFirst() {
		ConfiguredProduct cp = (ConfiguredProduct) findNode("cp_required");
		assertNotNull(cp);
	}
	
	/**
	 *  Test finding required configured products.
	 */
	public void testFindRequiredConfiguredProducts() {
		
		ContentTypeDefI	cpDef;
		AttributeDefI   requiredAttrDef;
		Predicate       predicate;
		Map				results; 
		
		cpDef           = typeService.getContentTypeDefinition(
									FDContentTypes.CONFIGURED_PRODUCT);
		requiredAttrDef = cpDef.getAttributeDef("REQUIRED");
		predicate       = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);
		results         = service.queryContentNodes(FDContentTypes.CONFIGURED_PRODUCT,
												    predicate); 

		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_required")));
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_not_required")));
	}

	/**
	 *  Test finding configured products referencing specific SKUs.
	 */
	public void testFindConfiguredProductsForSkus() {
		
		ContentTypeDefI		cpDef;
		RelationshipDefI  	skuRelDef;
		Predicate       	predicate;
		Map					results; 
		
		cpDef           = typeService.getContentTypeDefinition(
									FDContentTypes.CONFIGURED_PRODUCT);
		skuRelDef       = (RelationshipDefI) cpDef.getAttributeDef("SKU");
		predicate       = new AttributeEqualsPredicate(skuRelDef,
							new ContentKey(FDContentTypes.SKU, "MEA0004568"));
		results         = service.queryContentNodes(FDContentTypes.CONFIGURED_PRODUCT,
												    predicate); 

		// this product contains the above SKU
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_after_horizon")));
		// this product does not contain the above SKU
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_not_required")));
	}

	/**
	 *  Test finding configured products referencing specific SKUs, but
	 *  only those that are required.
	 */
	public void testFindRequiredConfiguredProductsForSkus() {
		
		ContentTypeDefI		cpDef;
		RelationshipDefI  	skuRelDef;
		AttributeDefI   	requiredAttrDef;
		Predicate           skuPredicate;
		Predicate           requiredPredicate;
		Predicate       	predicate;
		Map					results; 
		
		cpDef           = typeService.getContentTypeDefinition(
									FDContentTypes.CONFIGURED_PRODUCT);
		
		skuRelDef       = (RelationshipDefI) cpDef.getAttributeDef("SKU");
		skuPredicate    = new AttributeEqualsPredicate(skuRelDef,
							new ContentKey(FDContentTypes.SKU, "MEA0004562"));

		requiredAttrDef   = cpDef.getAttributeDef("REQUIRED");
		requiredPredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		predicate = new AndPredicate(requiredPredicate, skuPredicate);
		
		results         = service.queryContentNodes(FDContentTypes.CONFIGURED_PRODUCT,
												    predicate); 

		// a required configured product with the specified sku
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_required")));
		// a not required configured product with the specified sku
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "cp_not_required")));
		// a required configured product with a different sku
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, "chckn_brsts_sb_2whl")));
	}

	/**
	 *  Test finding configured products referencing specific SKUs, but
	 *  only those that are required.
	 */
	public void testFindRequiredConfiguredProductGroupsForSkus() {
		
		ContentTypeDefI		cpDef;
		ContentTypeDefI		cpgDef;
		RelationshipDefI  	skuRelDef;
		RelationshipDefI    itemsRelDef;
		AttributeDefI   	requiredAttrDef;
		Predicate           skuPredicate;
		Predicate           itemsPredicate;
		Predicate           requiredPredicate;
		Predicate       	predicate;
		Map					results; 
		
		// the predicate used is the following:
		//
		// ConfiguredProductGroup
		//   |
		//   |      AND (predicate)
		//   |-----------------------------------\
		//   |                                   |
		// required == true                      |  itemsPredicate
		// requiredPredicate                     |  (check all in the items relation)
		//                                    ConfiguredProduct
		//                                       |
		//                                     SKU == <specified sku>
		//                                     skuPredicate
		
		cpDef           = typeService.getContentTypeDefinition(
											FDContentTypes.CONFIGURED_PRODUCT);
		cpgDef          = typeService.getContentTypeDefinition(
											FDContentTypes.CONFIGURED_PRODUCT_GROUP);
		
		
		skuRelDef       = (RelationshipDefI) cpDef.getAttributeDef("SKU");
		skuPredicate    = new AttributeEqualsPredicate(skuRelDef,
							new ContentKey(FDContentTypes.SKU, "MEA0004562"));

		itemsRelDef     = (RelationshipDefI) cpgDef.getAttributeDef("items");
		itemsPredicate  = new RelationshipAnyPredicate(itemsRelDef, skuPredicate);
		
		requiredAttrDef   = cpgDef.getAttributeDef("required");
		requiredPredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		predicate = new AndPredicate(requiredPredicate, itemsPredicate);
		
		results         = service.queryContentNodes(FDContentTypes.CONFIGURED_PRODUCT_GROUP,
												    predicate); 

		// a required configured product with the specified sku
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT_GROUP, "cpg_required")));
		// a not required configured product with the specified sku
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT_GROUP, "cpg_not_required")));
		// a required configured product with a different sku
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT_GROUP, "cpg_not_required_4569")));
	}

	/**
	 *  Test finding recipes that contain a variant of a specific name.
	 */
	public void testFindRecipeByVariant() {
		
		ContentTypeDefI		recipeDef;
		ContentTypeDefI     recipeVariantDef;
		AttributeDefI   	nameAttrDef;
		RelationshipDefI    variantsRelDef;
		Predicate           namePredicate;
		Predicate       	predicate;
		Map					results; 
		
		recipeDef        = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE);
		recipeVariantDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		
		nameAttrDef   = recipeVariantDef.getAttributeDef("name");
		namePredicate = new AttributeEqualsPredicate(nameAttrDef, "main");

		variantsRelDef = (RelationshipDefI) recipeDef.getAttributeDef("variants");

		// this will look at the recipe variants for each receipe, and
		// execute the namePredicate on each of them
		predicate = new RelationshipAnyPredicate(variantsRelDef, namePredicate);
		
		results         = service.queryContentNodes(FDContentTypes.RECIPE,
												    predicate); 

		// basically all recipes contain a variant named 'main'
		// just check for one of them as a sanity-check
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_available")));
	}

	/**
	 *  Test finding recipes that contain a required ingredients
	 */
	public void testFindRecipesWithRequiredIngredients() {
		
		ContentTypeDefI		recipeDef;
		ContentTypeDefI     recipeVariantDef;
		ContentTypeDefI     recipeSectionDef;
		ContentTypeDefI     configuredProductDef;
		RelationshipDefI    variantsRelDef;
		RelationshipDefI    sectionsRelDef;
		RelationshipDefI    ingredientsRelDef;
		AttributeDefI       requiredAttrDef;
		Predicate       	variantPredicate;
		Predicate       	sectionPredicate;
		Predicate           requiredPredicate;
		Map					results; 

		recipeDef        = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE);
		recipeVariantDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		recipeSectionDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		configuredProductDef = typeService.getContentTypeDefinition(
												FDContentTypes.CONFIGURED_PRODUCT);
		
		// the predicate to check on configured products
		requiredAttrDef   = configuredProductDef.getAttributeDef("REQUIRED");
		requiredPredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		// the predicate to delegate required-attribute checking to all
		// ingredients in a recipe section
		ingredientsRelDef = (RelationshipDefI) recipeSectionDef.getAttributeDef("ingredients");
		sectionPredicate  = new RelationshipAnyPredicate(ingredientsRelDef,
				                                         requiredPredicate);
		
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

		results         = service.queryContentNodes(FDContentTypes.RECIPE,
												    variantPredicate); 

		// check that the recipe with a required ingredient shows up
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_with_required_ingredient")));
		// check that the recipe with no required ingredients does not show up
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_with_no_required_ingredient")));
	}

	/**
	 *  Test finding recipes that contain a required ingredients and a
	 *  specific SKU
	 */
	public void testFindRecipesWithRequiredIngredientsAndSku() {
		
		ContentTypeDefI		recipeDef;
		ContentTypeDefI     recipeVariantDef;
		ContentTypeDefI     recipeSectionDef;
		ContentTypeDefI     configuredProductDef;
		RelationshipDefI    variantsRelDef;
		RelationshipDefI    sectionsRelDef;
		RelationshipDefI    ingredientsRelDef;
		RelationshipDefI    skuRelDef;
		AttributeDefI       requiredAttrDef;
		Predicate       	variantPredicate;
		Predicate       	sectionPredicate;
		Predicate           requiredPredicate;
		Predicate           skuPredicate;
		Predicate           configuredProductPredicate;
		Map					results;

		recipeDef        = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE);
		recipeVariantDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		recipeSectionDef = typeService.getContentTypeDefinition(
												FDContentTypes.RECIPE_VARIANT);
		configuredProductDef = typeService.getContentTypeDefinition(
												FDContentTypes.CONFIGURED_PRODUCT);
		
		// the predicate to check on configured products if they are required
		requiredAttrDef   = configuredProductDef.getAttributeDef("REQUIRED");
		requiredPredicate = new AttributeEqualsPredicate(requiredAttrDef, Boolean.TRUE);

		// the predicate to check on concifgured products if they have a specific SKU
		skuRelDef       = (RelationshipDefI) configuredProductDef.getAttributeDef("SKU");
		skuPredicate    = new AttributeEqualsPredicate(skuRelDef,
							new ContentKey(FDContentTypes.SKU, "MEA0004562"));
		
		// the predicate to combine all predicates related to configured products
		configuredProductPredicate = new AndPredicate(requiredPredicate, skuPredicate);
		
		// the predicate to delegate required-attribute checking to all
		// ingredients in a recipe section
		ingredientsRelDef = (RelationshipDefI) recipeSectionDef.getAttributeDef("ingredients");
		sectionPredicate  = new RelationshipAnyPredicate(ingredientsRelDef,
				                                         configuredProductPredicate);
		
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

		results         = service.queryContentNodes(FDContentTypes.RECIPE,
												    variantPredicate); 

		// check that the recipe with a required ingredient and the
		// specified SKU shows up
		assertTrue(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_with_required_ingredient")));
		// check that the recipe with no required ingredients does not show up
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_with_no_required_ingredient")));
		// check that the recipe with required ingredients but with not
		// the specified SKU does not show up
		assertFalse(results.containsKey(new ContentKey(
				FDContentTypes.RECIPE, "rec_with_required_ingredient_4569")));
	}

	/**
	 *  Test the automated cross-sell function
	 */
	public void testAutomatedCrossSell() {
		ProductModel    product;
		ContentKey		key;
		Recipe		  	recipe;
		ContentFactory	contentFactory;
		Collection		recipes;
		
		contentFactory = ContentFactory.getInstance();

		// get recipes which have the "ok_product" as a required ingredient
		product = (ProductModel) findNode("ok_product");
		recipes = YmalSet.getAutoCrossSellRecipes(product);
		
		
		// check that the recipe with a required ingredient and the
		// specified SKU shows up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_required_ingredient");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertTrue(recipes.contains(recipe));

		// check that the recipe with no required ingredients does not show up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_no_required_ingredient");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertFalse(recipes.contains(recipe));

		// check that the recipe with required ingredients but with not
		// the specified SKU does not show up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_required_ingredient_4569");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertFalse(recipes.contains(recipe));
	}
	
	/**
	 *  Test the automated cross-sell function with configured product groups
	 */
	public void testAutomatedCrossSellConfiguredProductGroups() {
		ProductModel    product;
		ContentKey		key;
		Recipe		  	recipe;
		ContentFactory	contentFactory;
		Collection		recipes;
		
		contentFactory = ContentFactory.getInstance();

		// get recipes which have the "ok_product" as a required ingredient
		product = (ProductModel) findNode("ok_product");
		recipes = YmalSet.getAutoCrossSellRecipes(product);
		
		
		// check that the recipe with a required ingredient and the
		// specified SKU shows up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_required_ingredient_cpg");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertTrue(recipes.contains(recipe));

		// check that the recipe with no required ingredients does not show up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_no_required_ingredient_cpg");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertFalse(recipes.contains(recipe));

		// check that the recipe with required ingredients but with not
		// the specified SKU does not show up
		key    = new ContentKey(FDContentTypes.RECIPE, "rec_with_required_ingredient_cpg_4569");
		recipe = (Recipe) contentFactory.getContentNode(key.getId());
		assertNotNull(recipe);
		assertFalse(recipes.contains(recipe));
	}

	/**
	 *  Find a content node in the content service.
	 *  
	 *  @param id the id of the node to find
	 *  @return the node with the specified id, or null if it does not exist
	 *          in the content service.
	 */
	private ContentNodeModel findNode(String id) {
		return ContentFactory.getInstance().getContentNode(id);
	}

	public static class FDFactoryProductInfoAspect implements Aspect {

		public Pointcut getPointcut() {
			return new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProductInfo\\(java.lang.String\\)");
		}

		public void intercept(InvocationContext ctx) throws Exception {
			String sku = (String) ctx.getParamVals()[0];
			ctx.setReturnObject(getProductInfo(sku));
		}
	    
		/**
		 * Get current product information object for sku.
		 *
		 * @param sku SKU code
		 *
		 * @return FDProductInfo object
		 *
	 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
		 * @throws FDResourceException if an error occured using remote resources
		 */
		public FDProductInfo getProductInfo(String sku) throws RemoteException, FDSkuNotFoundException, FDResourceException {			
			Date now = new Date();
			String[] materials = {"000000000123"};
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
			List erpEntries = new ArrayList();
			FDProductInfo                productInfo;

			// return all items as available
			// a 10000 units available starting now
			erpEntries.add(new ErpInventoryEntryModel(now, 10000));
			inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
			productInfo    = new FDProductInfo(sku,
					                           1,
					                           1.0,
					                           "ea",
					                           materials,
					                           EnumATPRule.MATERIAL,
					                           EnumAvailabilityStatus.AVAILABLE,
					                           now,
					                           null, inventoryCache,"");

			return productInfo;
		}
	    
	}

	public static class FDFactoryProductAspect implements Aspect {

		public Pointcut getPointcut() {
			return new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProduct\\(java.lang.String,int\\)");
		}

		public void intercept(InvocationContext ctx) throws Exception {
			String sku = (String) ctx.getParamVals()[0];
			Integer i = (Integer) ctx.getParamVals()[1];
			ctx.setReturnObject(getProduct(sku, i.intValue()));
		}
	
	    /**
		 * Get product with specified version. 
		 *
		 * @param sku SKU code
		 * @param version requested version
		 *
		 * @return FDProduct object
		 *
		 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
		 */
	    public FDProduct getProduct(String sku, int version) throws RemoteException, FDSkuNotFoundException, FDResourceException {
	    	
	    	Date          now            = new Date();
	    	FDMaterial    material       = null;
			FDVariation[] variations     = new FDVariation[1];
			variations[0] = new FDVariation(new AttributeCollection(),
					                        "variation",
					                        null);
			FDSalesUnit[] salesUnits     = null;
			Pricing       pricing        = null;
			ArrayList     nutrition      = null;
			
	    	FDProduct     product  = new FDProduct(sku,
		    			                           version,
		    			                           now,
		    			                           material,
		    			                           variations,
		    			                           salesUnits,
		    			                           pricing,
		    			                           nutrition);
	    	
	    	return product;
	    }
		
	}

	protected String[] getAffectedTables() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
