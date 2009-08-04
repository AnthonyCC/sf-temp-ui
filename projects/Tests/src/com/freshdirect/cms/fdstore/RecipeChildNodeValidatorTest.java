package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ValidatingContentService;

/**
 * Test case for the RecipeChildNodeValidator class.
 */
public class RecipeChildNodeValidatorTest extends TestCase {
	
	ContentServiceI	service;
	CmsUser			user;
	ContentKey		key;

	/**
	 *  Set up by inserting a new recipe node named recipe_test into the service.
	 *
	 */
	public void setUp() {
		List				list;
		CmsRequestI		request;
		ContentNodeI		node;
		
		// initialize the service type list, with only a store def
		list = new ArrayList();
		list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);
		
		// create a validating content service
		list = new ArrayList();
		list.add(new RecipeChildNodeValidator());
		service = new ValidatingContentService(new SimpleContentService(typeService),
											   list);
	
		user        = new CmsUser("test_user");
				
		// create a new recipe, and add and insert it
		key     = new ContentKey(FDContentTypes.RECIPE, "recipe_test");
		node    = service.createPrototypeContentNode(key);
		request = new CmsRequest(user);
		
		request.addNode(node);
		service.handle(request);
	}

	/**
	 *  Test an the creation / insertion of an empty recipe, and if the validator
	 *  puts a default variant into it and a main section into the variant.
	 */
	public void testEmptyInsert() {
		ContentNodeI		node;
		Set				set;

		// check on the inserted recipe
		node = service.getContentNode(key);
		assertEquals("test recipe", node.getLabel(), "recipe_test");
		
		set  = node.getChildKeys();
		assertEquals("recipe has only one child", set.size(), 1);
		
		node = service.getContentNode((ContentKey) set.toArray()[0]);
		assertEquals("test recipe default variant", node.getLabel(), "default");

		set  = node.getChildKeys();
		assertEquals("recipe variant has only one child", set.size(), 1);
		
		node = service.getContentNode((ContentKey) set.toArray()[0]);
		assertEquals("test recipe variant main section", node.getLabel(), "main");
	}

	/**
	 *  Test the update that already has a default variant and a main section
	 *  within the variant.
	 */
	public void testUpdate() {
		
		List				list;
		CmsRequestI		request;
		ContentNodeI		node;
		Set				set;

		
		// add another section to the default variant
		ContentNodeI		variantNode;
		ContentNodeI		sectionNode;
		AttributeI		attr;
		
		request = new CmsRequest(user);

		// get the variant node 
		node = service.getContentNode(key);
		set  = node.getChildKeys();
		variantNode = service.getContentNode((ContentKey) set.toArray()[0]);
		
		// add another section to the variant
		key = new ContentKey(FDContentTypes.RECIPE_SECTION, variantNode.getKey().getId() + "_second");
		sectionNode = service.createPrototypeContentNode(key);

		// get (or create if needed) the section relation
		attr = variantNode.getAttribute("sections");
		list = (List) attr.getValue();
		if (list == null) {
			list = new ArrayList();
		}
		// add the new section to the relation
		list.add(sectionNode.getKey());
		attr.setValue(list);

		// send through the request
		request.addNode(node);
		service.handle(request);

		
		// check on updated inserted recipe
		node = service.getContentNode(node.getKey());
		assertEquals("test recipe", node.getLabel(), "recipe_test");
		
		set  = node.getChildKeys();
		assertEquals("recipe has only one variant", set.size(), 1);
		
		node = service.getContentNode((ContentKey) set.toArray()[0]);
		assertEquals("test recipe default variant", node.getLabel(), "default");

		set  = node.getChildKeys();
		assertEquals("recipe variant has two children", set.size(), 2);
		
//		node = service.getContentNode((ContentKey) set.toArray()[0]);
//		assertEquals("test recipe variant default section", node.getLabel(), "recipe_test_default_main");
	}

	/**
	 *  Test if a default recipe section is created for new recipe variants that are
	 *  added to an aready existing recipe.
	 */
	public void testNewVariant() {
		
		List				list;
		CmsRequestI		request;
		ContentNodeI		node;
		Set				set;
		Object			array[];
		ContentKey		ckey;

		
		request = new CmsRequest(user);

		// get the recipe node 
		node = service.getContentNode(key);
		
		// add another section to the default variant
		ContentNodeI		variantNode;
		AttributeI		attr;
		
		// create a new variant node
		key = new ContentKey(FDContentTypes.RECIPE_VARIANT, "second");
		variantNode = service.createPrototypeContentNode(key);

		// get (or create if needed) the variants relation
		attr = node.getAttribute("variants");
		list = (List) attr.getValue();
		if (list == null) {
			list = new ArrayList();
		}
		// add the new section to the relation
		list.add(variantNode.getKey());
		attr.setValue(list);

		// send through the request
		request.addNode(node);
		request.addNode(variantNode);
		service.handle(request);

		
		// check on updated inserted recipe
		node = service.getContentNode(node.getKey());
		assertEquals("test recipe", node.getLabel(), "recipe_test");
		
		set  = node.getChildKeys();
		assertEquals("recipe has two variants", set.size(), 2);

		array = set.toArray();
		ckey  = (ContentKey) array[0];
		node  = service.getContentNode(ckey);
		assertEquals("test recipe default variant", node.getLabel(), "default");

		ckey  = (ContentKey) array[1];
		node  = service.getContentNode(ckey);
		assertEquals("test recipe second variant", node.getLabel(), "second");

		set  = node.getChildKeys();
		assertEquals("recipe variant has one child", set.size(), 1);
		
//		node = service.getContentNode((ContentKey) set.toArray()[0]);
//		assertEquals("test recipe variant main section", node.getLabel(), "recipe_test_default_main");
	}

}
