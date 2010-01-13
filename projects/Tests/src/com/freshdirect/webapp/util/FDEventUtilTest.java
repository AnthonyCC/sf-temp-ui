package com.freshdirect.webapp.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.event.EventLogger;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCustomerManagerGetNextIdAspect;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.framework.event.FDEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;

/**
 * Test case for the availability of ConfiguredProducts.
 */
public class FDEventUtilTest extends FDCustomerManagerTestSupport {

	public FDEventUtilTest(String name) {
		super(name);
	}

	/** The content service used in the test */
	ContentServiceI service;

	/** The HTTP request to use for testing. */
	MockHttpServletRequest	request;
	
	/** The HTTP session to use for testing. */
	MockHttpSession			session;
	
	public void setUp() throws Exception {
		super.setUp();

		List list = new ArrayList();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/ConfiguredProducts.xml");

		CmsManager.setInstance(new CmsManager(service, null));
		
		Hashtable          			env                		= new Hashtable();
		MockContextFactory 			mockContextFactory 		= new MockContextFactory();

		// set the context factory to the mockejb context factory
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");
		ErpServicesProperties.set("erpservices.providerURL", "");
		ErpServicesProperties.set("erpservices.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );
		
		MockContextFactory.setAsInitial();
		context = mockContextFactory.getInitialContext(env);
		
		FDUserI  user;
		
		user      = new FDUser();
		session   = new MockHttpSession();
		session.setAttribute(SessionName.USER, user);
		request   = new MockHttpServletRequest();
		request.setSession(session);
		
		aspectSystem.add(new FDFactoryProductAspect());
		aspectSystem.add(new FDFactoryProductInfoAspect());
		aspectSystem.add(new FDCustomerManagerGetNextIdAspect());
	}
	
	public void tearDown() {
		MockContextFactory.revertSetAsInitial();
		session = null;
		request = null;
	}

	/**
	 *  Just a very little smoke test.
	 */
	public void testFirst() {
		
		FDProduct			product   = findProduct("MEA0004561");
		FDSalesUnit         salesUnit = product.getSalesUnit("C01");
		FDCartLineI			cartLine  = getCartLine(product,
			                                        1.0,
			                                        "C_MT_BF_PAK=ST,C_MT_BF_TW1=N,C_MT_BF_MAR=N",
			                                        salesUnit,
			                                        null);
		cartLine.setYmalCategoryId("test_category");
		cartLine.setYmalSetId("test_ymal_set");
		cartLine.setOriginatingProductId("test_originating_product");
		
		assertNotNull(product);
		assertNotNull(salesUnit);
		assertNotNull(cartLine);
		FDEventUtil.logAddToCartEvent(cartLine, request);
		
		FDWebEvent event = (FDWebEvent)EventLogger.getInstance().getLastEvent();
		assertEquals(event.getEventValues()[0], "ok_product"); // product id
		assertEquals(event.getEventValues()[1], "MEA0004561"); // SKU code
		assertEquals(event.getEventValues()[2], "ok_category"); // category id
		assertEquals(event.getEventValues()[3], "The OK Department"); // department id
		//assertEquals(event.getEventValues()[4], ""); this is the cartline id - don't care
		assertEquals(event.getEventValues()[5], "1.0"); // this is the quantity
		assertEquals(event.getEventValues()[6], "C01"); // this is the sales unit
		assertEquals(event.getEventValues()[7], "c-oh-one, prime, ess-tee, enn, enn");  // this is the configuration descritpion
		assertEquals(event.getEventValues()[8], "test_category"); // the category the YMAL set was obtained from 
		assertEquals(event.getEventValues()[9], "test_originating_product"); // the originating product of the YMAL
		assertEquals(event.getEventValues()[10], "test_ymal_set"); // the YMAL set id from which the YMAL comes from
	}

	/**
	 *  Find a product from a specified product id.
	 *  
	 *  @param id the id of the product to find.
	 *  @return the product with the specified id, or null if no such
	 *          product exists.
	 */
	/*
	private ContentNodeModel findProduct(String id) {
		return ContentFactory.getInstance().getContentNode(id);
	}
	*/

	private FDProduct findProduct(String skuCode) { 
		FDProduct product;
		
		try {
			product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
		} catch (FDResourceException fdre) {
			return null;
		} catch (FDSkuNotFoundException fdsnfe) {
			return null;
		}
		
		return product;
	}

	
	/**
	 *  Create a cart line object, based on the product and configuration
	 *  provided.
	 *  
	 *  @param product the product to add
	 *  @param quantity the quantity to add
	 *  @param optionsString the selected options for the product as one string
	 *  @param salesUnit the sales unit to add
	 *  @param origCartLineId the id of the original cart line. if this is not
	 *         a new cartline item, but a change to an existing one, the cartline
	 *         with this id will be updated. otherwise this parameter is null.
	 *  @return a cart line object, including the specified product with
	 *          the specified configuration. null if there were some errors
	 *          generating the cart line object.
	 */
	private FDCartLineI getCartLine(FDProduct 		product,
									double 			quantity,
									String        	optionsString,
									FDSalesUnit 	salesUnit,
									String 			origCartLineId) {

		ProductModel prodNode;
		
		try {
			prodNode = ContentFactory.getInstance().getProduct(product.getSkuCode());
		} catch (FDSkuNotFoundException e) {
			return null;
		}
		
		//
		// walk through the variations to see what's been set and try to build a variation map
		//
		HashMap varMap = new HashMap();
		FDVariation[] variations = product.getVariations();
		for (int i = 0; i < variations.length; i++) {
			FDVariation variation = variations[i];
			FDVariationOption[] options = variation.getVariationOptions();

			if (options.length == 1) {
				//
				// there's only a single option, pick that
				//
				varMap.put(variation.getName(), options[0].getName());
				
			} else if (((optionsString == null) || "".equals(optionsString)) && variation.isOptional()) {
				//
				// user didn't select anything for an optional variation, pick the SELECTED option for them
				//
				String selected = null;
				for (int j = 0; j < options.length; j++) {
					if (options[j].isSelected())
						selected = options[j].getName();
				}
				varMap.put(variation.getName(), selected);
			} else if (!"".equals(optionsString)) {
				//
				// validate & add the option the user selected
				//
				boolean validOption = false;
				for (int j = 0; j < options.length; j++) {
					if (optionsString.equals(options[j].getName())) {
						validOption = true;
						break;
					}
				}
				if (validOption) {
					varMap.put(variation.getName(), optionsString);
				} else {
					return null;
				}
			} else {
				//
				// user didn't select anything for a required variation, alert them
				//
				return null;
			}
		}

		//
		// make the order line and add it to the cart
		//
		FDCartLineModel cartLine = null;
		if (origCartLineId == null || origCartLineId.length() == 0) {
			/*
			 * This condition is true whenever there is a new item
			 * added to the cart.
			 */
			cartLine =
				new FDCartLineModel(
					new FDSku(product),
					prodNode,
					new FDConfiguration(quantity, salesUnit.getName(), varMap), null);
		} else {
			/*
			 * When an existing item in the cart is modified, reuse the same
			 * cartlineId instead of generating a new one.
			 * 
			 */
			cartLine =
				new FDCartLineModel(
					new FDSku(product),
					prodNode,
					new FDConfiguration(quantity, salesUnit.getName(), varMap),
					origCartLineId, null, false, null);
		}

		try {
			OrderLineUtil.describe(cartLine);
		} catch (FDResourceException e) {
			// don't care
		} catch (FDInvalidConfigurationException e) {
			// don't care
		}
		
		return cartLine;
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
	    	
			FDVariation[]       variations = new FDVariation[3];
			FDVariationOption[]	variationOptions;

			variationOptions    = new FDVariationOption[1];
			variationOptions[0] = new FDVariationOption(new AttributeCollection(), "ST", "ess-tee");
			variations[0] = new FDVariation(new AttributeCollection(),
					                        "C_MT_BF_PAK",
					                        variationOptions);
			
			variationOptions    = new FDVariationOption[1];
			variationOptions[0] = new FDVariationOption(new AttributeCollection(), "N", "enn");
			variations[1] = new FDVariation(new AttributeCollection(),
					                        "C_MT_BF_TW1",
					                        variationOptions);
			
			variationOptions    = new FDVariationOption[1];
			variationOptions[0] = new FDVariationOption(new AttributeCollection(), "N", "enn");
			variations[2] = new FDVariation(new AttributeCollection(),
					                        "C_MT_BF_MAR",
					                        variationOptions);
			
			FDSalesUnit[] salesUnits     = new FDSalesUnit[1];
			salesUnits[0] = new FDSalesUnit(new AttributeCollection(), "C01", "c-oh-one");
			
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
			List erpEntries = new ArrayList();
			String[] materials = {"000000000123"};
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
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
					                           "", inventoryCache,"",null, 1.0,"ea",false,-1,-1);

			return productInfo;
		}
	    
	}
	protected String[] getAffectedTables() {
		return null;
	}

	protected String getSchema() {
		return null;
	}

}
