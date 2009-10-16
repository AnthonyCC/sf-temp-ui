package com.freshdirect.cms.fdstore.recipes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.conf.ResourceUtil;

/**
 * Test case for the RecipeChildNodeValidator class.
 */
public class BulkLoaderTest extends TestCase {

	ContentServiceI	service;
	CmsUser			user;
	
	public void setUp() {
		
		FDRegistry.setDefaultRegistry("classpath:/com/freshdirect/TestServiceConfig.registry");
		// The addConfiguration() call has the side-effect that FDRegistry.getInstance()
		// will be null-d out. this is requred for the services to be reset before
		// each test - otherwise they would be shared between tests.
		FDRegistry.addConfiguration("classpath:/com/freshdirect/TestServiceConfig.registry");
		service = (ContentServiceI) FDRegistry.getInstance().getService("com.freshdirect.test.CloneContentService", ContentServiceI.class);
		//SimpleContentService scs = new SimpleContentService(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		//scs.setName("foo");
		//service = new CloneProxyContentService(scs);
		//service = scs;
		user    = new CmsUser("test_user");
	}
	
	/**
	 *  Test the parsing of a very simple recipe.
	 */
	public void testParseFruitRecipe() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/fruitsRecipe.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 			loader = new RecipeBulkLoader(service,
														reader,
														"fruitRecipe",
														RecipeBulkLoader.RECIPE);
		List			 			list;
		Iterator	   	 			it;
		RecipeBulkLoader.SectionNode	sectionNode;
		ContentKey	 			key;
		
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(3, list.size());
		
		// check on the five content node values
		it   = list.iterator();
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals("Main", sectionNode.section);
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals("FRU0005203", key.getId());
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals("Main", sectionNode.section);
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals("FRU0005090", key.getId());
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals("Main", sectionNode.section);
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals("MEA0063339", key.getId());
	}

	/**
	 *  Test the parsing of a configured product group.
	 */
	public void testParseConfiguredProductGroup() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/simpleInput.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 			loader = new RecipeBulkLoader(service,
														reader,
														"simpleGroup",
														RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);
		List			 			list;
		Iterator	   	 			it;
		RecipeBulkLoader.SectionNode	sectionNode;
		ContentKey	 			key;
		
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(list.size(), 5);
		
		// check on the five content node values
		it   = list.iterator();
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO058390");
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0066161");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058949");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002558");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058948");
	}

	/**
	 *  Test a simple recipe, with only a Main section
	 */
	public void testParseTwoSections() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
					"classpath:/com/freshdirect/cms/fdstore/recipes/twoSectionInput.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 loader = new RecipeBulkLoader(service,
											 reader,
											 "twoSectionRecipe",
											 RecipeBulkLoader.RECIPE);
		List			 			list;
		Iterator	   	 			it;
		RecipeBulkLoader.SectionNode	sectionNode;
		ContentKey	 			key;
		
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// check that there are two sections, called Main and Stable
		assertEquals(list.size(), 13);
		
		// check on the content node values
		it   = list.iterator();
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SEA0007114");
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058834");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "DAI0068691");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "DAI0008771");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0011235");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0058758");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0000580");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0011090");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Main");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO001138");

	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Staple");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "FRU0005131");
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Staple");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002572");
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Staple");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "[clig_pepper]");
		
	    sectionNode = (RecipeBulkLoader.SectionNode) it.next();
		assertEquals(sectionNode.section, "Staple");
		key  = (ContentKey) sectionNode.node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002521");
	}
	
	/**
	 *  Test the processing of a configured product group.
	 */
	public void testProcessConfiguredProductGroup() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/simpleInput.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 	loader = new RecipeBulkLoader(service,
												reader,
												"simpleGroup",
												RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);
		List			 	list;
		Iterator	   	 	it;
		ContentNodeI		node;
		ContentKey	 	key;
		
		// parse the input
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(list.size(), 5);

		// process the input
		try {
			list = loader.process();
		} catch (InvalidContentKeyException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// check on the six content node values
		it   = list.iterator();

		assertEquals(list.size(), 6);

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO058390");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0066161");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058949");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002558");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058948");

		// this is the configured product group itself
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "simpleGroup");
		assertEquals(key.getType(), FDContentTypes.CONFIGURED_PRODUCT_GROUP);
		// TODO: check the items in the group
	}

	/**
	 *  Test the processing of a fruit recipe
	 */
	public void testProcessFruitRecipe() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/fruitsRecipe.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 	loader = new RecipeBulkLoader(service,
												reader,
												"fruitRecipe",
												RecipeBulkLoader.RECIPE);
		List			 	list;
		Iterator	   	 	it;
		ContentNodeI		node;
		ContentKey	 	key;
		List				llist;
		ContentKey		ckey;
		
		// parse the input
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(3, list.size());

		// process the input
		try {
			list = loader.process();
		} catch (InvalidContentKeyException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// check on the six content node values
		it   = list.iterator();

		assertEquals(6, list.size());


	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "FRU0005203");
		
	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "FRU0005090");
		
	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "MEA0063339");
		
		// this is the main recipe section
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "fruitRecipe_default_main");
		assertEquals(key.getType(), FDContentTypes.RECIPE_SECTION);
		// check for the section to include 2 ingredients
		llist = (List) node.getAttributeValue("ingredients");
		assertEquals(3, llist.size());
		
		// check the ingredients one by one
		Iterator iter = llist.iterator();
		
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "fruitRecipe_1");
			
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "fruitRecipe_2");
			
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "fruitRecipe_3");
			

		// this is the default recipe variant
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "fruitRecipe_default");
		assertEquals(key.getType(), FDContentTypes.RECIPE_VARIANT);
		// check for the variant to include a main section
		llist = (List) node.getAttributeValue("sections");
		assertEquals(llist.size(), 1);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "fruitRecipe_default_main");

		// this is the recipe itself
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "fruitRecipe");
		assertEquals(key.getType(), FDContentTypes.RECIPE);
		// check for the recipe to include the single main variant
		llist = (List) node.getAttributeValue("variants");
		assertEquals(llist.size(), 1);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "fruitRecipe_default");

	}

	/**
	 *  Test the processing of a ISO-8859-1 entry names, that are outside of ASCII
	 */
	public void testProcessPate() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/rec_test_pate.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 	loader = new RecipeBulkLoader(service,
												reader,
												"pate",
												RecipeBulkLoader.RECIPE);
		List			 	list;
		Iterator	   	 	it;
		ContentNodeI		node;
		ContentKey	 	key;
		List				llist;
		ContentKey		ckey;
		
		// parse the input
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(1, list.size());

		// process the input
		try {
			list = loader.process();
		} catch (InvalidContentKeyException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// check on the six content node values
		it   = list.iterator();

		assertEquals(4, list.size());


	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "DEL0057701");
				
		// this is the main recipe section
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "pate_default_main");
		assertEquals(key.getType(), FDContentTypes.RECIPE_SECTION);
		// check for the section to include 1 ingredient
		llist = (List) node.getAttributeValue("ingredients");
		assertEquals(1, llist.size());
		
		// check the ingredients one by one
		Iterator iter = llist.iterator();
		
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "pate_1");
			
		// this is the default recipe variant
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "pate_default");
		assertEquals(key.getType(), FDContentTypes.RECIPE_VARIANT);
		// check for the variant to include a main section
		llist = (List) node.getAttributeValue("sections");
		assertEquals(llist.size(), 1);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "pate_default_main");

		// this is the recipe itself
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "pate");
		assertEquals(key.getType(), FDContentTypes.RECIPE);
		// check for the recipe to include the single main variant
		llist = (List) node.getAttributeValue("variants");
		assertEquals(llist.size(), 1);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "pate_default");
	}
	
	/**
	 *  Test the processing of a configured product group.
	 */
	public void testProcessRecipe() {
		Reader reader;
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/twoSectionInput.txt"));
		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		RecipeBulkLoader 	 	loader = new RecipeBulkLoader(service,
												reader,
												"twoSectionRecipe",
												RecipeBulkLoader.RECIPE);
		List			 	list;
		Iterator	   	 	it;
		ContentNodeI		node;
		ContentKey	 	key;
		List				llist;
		ContentKey		ckey;
		
		// parse the input
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}

		assertEquals(list.size(), 13);

		// process the input
		try {
			list = loader.process();
		} catch (InvalidContentKeyException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// check on the six content node values
		it   = list.iterator();

		assertEquals(list.size(), 16);

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SEA0007114");
		
	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0058834");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "DAI0068691");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "DAI0008771");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0011235");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0058758");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "SPE0000580");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "VEG0011090");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO001138");

	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "FRU0005131");
		
	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002572");
		
	    node = (ContentNodeI) it.next();
		key  = (ContentKey) node.getAttributeValue("SKU");
		assertEquals(key.getId(), "GRO002521");
		
		// this is the main recipe section
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "twoSectionRecipe_default_main");
		assertEquals(key.getType(), FDContentTypes.RECIPE_SECTION);
		// check for the section to include 9 ingredients
		llist = (List) node.getAttributeValue("ingredients");
		assertEquals(llist.size(), 9);
		// TODO: check the ingredients one by one

		// this is the staple recipe section
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "twoSectionRecipe_default_Staple");
		assertEquals(key.getType(), FDContentTypes.RECIPE_SECTION);
		// check for the section to include 4 ingredients
		llist = (List) node.getAttributeValue("ingredients");
		assertEquals(llist.size(), 4);
		
		// check the ingredients one by one
		Iterator iter = llist.iterator();
		
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "twoSectionRecipe_10");
			
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "twoSectionRecipe_11");
			
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT_GROUP);
		assertEquals(ckey.getId(), "clig_pepper");
			
		ckey = (ContentKey) iter.next();
		assertEquals(ckey.getType(), FDContentTypes.CONFIGURED_PRODUCT);
		assertEquals(ckey.getId(), "twoSectionRecipe_13");
			

		// this is the default recipe variant
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "twoSectionRecipe_default");
		assertEquals(key.getType(), FDContentTypes.RECIPE_VARIANT);
		// check for the variant to include a main and a Staple section
		llist = (List) node.getAttributeValue("sections");
		assertEquals(llist.size(), 2);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "twoSectionRecipe_default_main");
		key = (ContentKey) llist.get(1);
		assertEquals(key.getId(), "twoSectionRecipe_default_Staple");

		// this is the recipe itself
	    node = (ContentNodeI) it.next();
		key  = node.getKey();
		assertEquals(key.getId(), "twoSectionRecipe");
		assertEquals(key.getType(), FDContentTypes.RECIPE);
		// check for the recipe to include the single main variant
		llist = (List) node.getAttributeValue("variants");
		assertEquals(llist.size(), 1);
		key = (ContentKey) llist.get(0);
		assertEquals(key.getId(), "twoSectionRecipe_default");

	}
	
	/**
	 * Process an input stream with the bulk loader, and insert the contents into a
	 * storage.
	 * 
	 * @param reader the reader that contains the contents to load.
	 * @param recipeId the id of the recipe to process.
	 * @param type specify what to process: a ConfiguredProductGroup
	 *        or a Recipe
	 * @see RecipeBulkLoader#CONFIGURED_PRODUCT_GROUP
	 * @see RecipeBulkLoader#RECIPE
	 */
	private void load(Reader reader,
			          String recipeId,
			          int    type) {
		
		List			list;
		RecipeBulkLoader 	 	loader = new RecipeBulkLoader(service, reader, recipeId, type);
		
		// parse the input
		try {
			list = loader.parse();
		} catch (IOException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// process the input
		try {
			list = loader.process();
		} catch (InvalidContentKeyException e) {
			fail("error processing input: " + e.toString());
			return;
		}
		
		// create a new request with all nodes, and add it to the content service
		CmsRequest   request = new CmsRequest(user);
		
		for (Iterator it = list.iterator(); it.hasNext();) {
			ContentNodeI node = (ContentNodeI) it.next();
			
			request.addNode(node);
		}
		
		service.handle(request);
	}
	
	/**
	 *  A test case using recipe data provided by Neal Bayless
	 */
	public void testProcessRecipeProducts() {
		Reader reader;
		
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_brd_soup_garlic.txt"));
			load(reader, "recwk_brd_soup_garlic", RecipeBulkLoader.RECIPE);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_pchdpear_rdwn.txt"));
			load(reader, "recwk_pchdpear_rdwn", RecipeBulkLoader.RECIPE);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_pnfry_crabcake.txt"));
			load(reader, "recwk_pnfry_crabcake", RecipeBulkLoader.RECIPE);

		} catch (IOException e) {
			fail("can't open input file");
			return;
		}

		Set contentKeys = service.getContentKeys();
		
		// the total number of expected nodes is 38, including all
		// section nodes, recipe nodes, configured product nodes, variants
		assertEquals(38, contentKeys.size());
		
		// TODO: check the contents in the content service as well
	}
	
	/**
	 *  A test case using recipe data provided by Neal Bayless
	 */
	public void testProcessConfiguredProducts() {
		Reader reader;
		
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/clig_butter_unslt_2stk.txt"));
			load(reader, "clig_butter_unslt_2stk", RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/clig_butter_unslt_4stk.txt"));
			load(reader, "clig_butter_unslt_4stk", RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/clig_pepper.txt"));
			load(reader, "clig_pepper", RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/clig_rd_wn_vinegar.txt"));
			load(reader, "clig_rd_wn_vinegar", RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);
			
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/clig_salt.txt"));
			load(reader, "clig_salt", RecipeBulkLoader.CONFIGURED_PRODUCT_GROUP);

		} catch (IOException e) {
			fail("can't open input file; exc=" + e);
			return;
		}

		Set contentKeys = service.getContentKeys();
		
		// the total number of expected nodes is 24, including all
		// section nodes, recipe nodes, configured product nodes, variants
		assertEquals(24, contentKeys.size());
		
		// TODO: check the contents in the content service as well
	}

	/**
	 *  Test to see of Recipes are put into a Newborn folder, if it exists.
	 */
	public void testNewbornFolder() {
		
		// create a new FDFolder named "Newborn"
		CmsRequest      request;
		ContentKey		newbornKey;
		ContentNodeI		newbornFolder;
		List				newbornChildren;
		
		request         = new CmsRequest(user);
		newbornKey      = new ContentKey(FDContentTypes.FDFOLDER, "NewbornRecipes");
		newbornFolder   = service.createPrototypeContentNode(newbornKey);
		
		request.addNode(newbornFolder);
		service.handle(request);
		
		newbornFolder = service.getContentNode(newbornKey);
		newbornChildren = (List) newbornFolder.getAttributeValue("children");
		
		assertNull(newbornChildren);
	

		// now read in a recipe, load it, and see if it becomes the child of the
		// Newborn folder.
		Reader reader;
		
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_brd_soup_garlic.txt"));
			load(reader, "recwk_brd_soup_garlic", RecipeBulkLoader.RECIPE);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_pchdpear_rdwn.txt"));
			load(reader, "recwk_pchdpear_rdwn", RecipeBulkLoader.RECIPE);

			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/recwk_pnfry_crabcake.txt"));
			load(reader, "recwk_pnfry_crabcake", RecipeBulkLoader.RECIPE);

		} catch (IOException e) {
			fail("can't open input file");
			return;
		}
		
		newbornFolder   = service.getContentNode(newbornKey);
		newbornChildren = (List) newbornFolder.getAttributeValue("children");
		
		assertEquals(3, newbornChildren.size());

		Set contentKeys = service.getContentKeys();
		
		// the total number of expected nodes is 38, including all
		// section nodes, recipe nodes, configured product nodes, variants
		// and the Newborn folder
		assertEquals(39, contentKeys.size());
		
		// TODO: check the contents in the content service as well
	}

	/**
	 *  Test a recipe containing descriptions that is outside of ASCII
	 */
	public void testProcessRecipePate() {
		Reader reader;
		
		try { 
			reader = new InputStreamReader(ResourceUtil.openResource(
						"classpath:/com/freshdirect/cms/fdstore/recipes/rec_test_pate.txt"));
			load(reader, "rec_test_pate", RecipeBulkLoader.RECIPE);

		} catch (IOException e) {
			fail("can't open input file");
			return;
		}

		Set contentKeys = service.getContentKeys();
		
		// the total number of expected nodes is 38, including all
		// section nodes, recipe nodes, configured product nodes, variants
		assertEquals(4, contentKeys.size());
		
		// TODO: check the contents in the content service as well
	}

}