package com.freshdirect.cms.application.service.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;

public class XmlTypeServiceTest extends TestCase {

	public void testBasic() {
		ContentTypeServiceI typeService = new XmlTypeService(
				"classpath:/com/freshdirect/cms/application/service/xml/TestDefinition.xml");

		ContentType fooType = ContentType.get("Foo");
		assertEquals(toSet(new ContentType[] { fooType }), typeService
				.getContentTypes());

		ContentTypeDefI def = typeService.getContentTypeDefinition(fooType);
		assertEquals(
				toSet(new String[] { "label", "date", "enum", "children" }),
				def.getAttributeNames());

		AttributeDefI attrDef = def.getAttributeDef("label");
		assertEquals(EnumAttributeType.STRING, attrDef.getAttributeType());

		attrDef = def.getAttributeDef("date");
		assertEquals(EnumAttributeType.DATE, attrDef.getAttributeType());

		attrDef = def.getAttributeDef("children");
		assertEquals(EnumAttributeType.RELATIONSHIP, attrDef.getAttributeType());
		assertTrue(attrDef instanceof RelationshipDefI);

		attrDef = def.getAttributeDef("enum");
		assertEquals(EnumAttributeType.ENUM, attrDef.getAttributeType());
		assertTrue(attrDef instanceof EnumDefI);
		EnumDefI enumDef = (EnumDefI)attrDef;
		assertEquals(EnumAttributeType.INTEGER, enumDef.getValueType());
		Map enumValues = enumDef.getValues();
		assertEquals(2, enumValues.size());
		assertEquals("ten", (String) enumValues.get(new Integer(10)));
		assertEquals("twenty", (String) enumValues.get(new Integer(20)));
	}

	private Set toSet(Object[] arr) {
		return new HashSet(Arrays.asList(arr));
	}
	
	public void testIdGeneration() {

		ContentTypeServiceI typeService;
		ContentType         type;
		ContentTypeDefI		typeDef;
		ContentKey 			key;
		
		typeService = new XmlTypeService("classpath:/com/freshdirect/cms/application/service/xml/TestIdGeneration.xml");

		type    = ContentType.get("FooDefault");
		typeDef = typeService.getContentTypeDefinition(type);
		assertNotNull(typeDef);
		assertFalse(typeDef.isIdGenerated());
		key = typeService.generateUniqueContentKey(type);
		assertNull(key);
		
		type    = ContentType.get("FooGenerateId");
		typeDef = typeService.getContentTypeDefinition(type);
		assertNotNull(typeDef);
		assertTrue(typeDef.isIdGenerated());
		key     = typeService.generateUniqueContentKey(type);
		assertNotNull(key);
		
		type    = ContentType.get("FooDontGenerateId");
		typeDef = typeService.getContentTypeDefinition(type);
		assertNotNull(typeDef);
		assertFalse(typeDef.isIdGenerated());

		key = typeService.generateUniqueContentKey(type);
		assertNull(key);
	}

}
