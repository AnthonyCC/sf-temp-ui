package com.freshdirect.framework.xml;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XMLSerializerTestCase extends TestCase {

	public XMLSerializerTestCase(String arg0) {
		super(arg0);
	}

	
	protected void setUp() throws Exception {
		super.setUp();
		// FIXME the test (and XMLSerializer) relies on default timezone.
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-5:00"));
	}
	
	public void testSerializeMap() {
		XMLSerializer ser = new XMLSerializer();
		Map map = new HashMap();
		
		map.put("simple", "foo");
		
		Object obj = new Object() {

			public String getPrimitive() {
				return "foo";	
			}

			public boolean isBool() {
				return true;	
			}
			
			public Object getNull() {
				return null;	
			}
			
			public String getProblem() {
				throw new UnsupportedOperationException();
			}

			public Object getLoopy() {
				return this;	
			}

			public List getItems() {
				return Arrays.asList( new String[] { "foo", "bar", "baz" });
			}

			public int[] getArray() {
				return new int[] { 1,2,3 };	
			}
			
			public Node getFooNode() {
				Document doc = DocumentHelper.createDocument();
				Element root = doc.addElement("shouldBeRenamedToFooNode");
				root.setText("Hello world");
				return root;
			}
			
			public Document getFooDocument() {
				return createDocument("shouldBeRenamedToFooDocument");
			}

			private Date d = new java.sql.Date(0);

			public Date getSimpleLoopy() {
				return d;	
			}
			
			public Date getSimpleLoopyRef() {
				return d;	
			}
			
			public Date getDate() {
				return new Date(0);
			}
			
			public Map getMap() {
				Map m = new HashMap();
				m.put("foo", "bar");
				m.put("blah", "blah");
				return m;
			}

		};
		
		map.put("complex", obj);
		map.put("complexAgain", obj);
		
		map.put("doco", createDocument("shouldBeRenamedToDoco"));
		
		Document doc = ser.serializeDocument("root", map);
		
		System.out.println( doc.asXML() );

		assertEquals( "foo", doc.valueOf("/root/simple") );
	
		assertEquals( "foo", doc.valueOf("/root/complex/primitive") );
		assertEquals( "true", doc.valueOf("/root/complex/bool") );
		assertNull( doc.selectSingleNode("/root/complex/null") );
		assertNull( doc.selectSingleNode("/root/complex/problem") );

		assertEquals( "", doc.valueOf("/root/complex/loopy") );
		assertEquals( doc.valueOf("/root/complex/@id"), doc.valueOf("/root/complex/loopy/@refid") );

		List items = doc.selectNodes("/root/complex/items/*");
		assertEquals( 3, items.size() );
		assertEquals( "foo", ((Node)items.get(0)).getText() );
		assertEquals( "bar", ((Node)items.get(1)).getText() );
		assertEquals( "baz", ((Node)items.get(2)).getText() );

		List array = doc.selectNodes("/root/complex/array/*");
		assertEquals( 3, array.size() );
		assertEquals( "1", ((Node)array.get(0)).getText() );
		assertEquals( "2", ((Node)array.get(1)).getText() );
		assertEquals( "3", ((Node)array.get(2)).getText() );
	
		assertEquals( "Hello world", doc.valueOf("/root/complex/fooNode") );
	
		assertEquals( "Hello", doc.valueOf("/root/complex/fooDocument/bar"));
		assertEquals( "world", doc.valueOf("/root/complex/fooDocument/baz"));	

		assertEquals( "Hello", doc.valueOf("/root/doco/bar"));
		assertEquals( "world", doc.valueOf("/root/doco/baz"));	

		assertEquals( "1969-12-31 19:00:00,000", doc.valueOf("/root/complex/date") );		
		
		assertEquals( "1969-12-31 19:00:00,000", doc.valueOf("/root/complex/simpleLoopy") );
		assertEquals( "1969-12-31 19:00:00,000", doc.valueOf("/root/complex/simpleLoopyRef") );
		
		assertEquals( 2, doc.selectNodes("/root/complex/map/*").size() );
		assertEquals( "bar", doc.valueOf("/root/complex/map/foo") );
		assertEquals( "blah", doc.valueOf("/root/complex/map/blah") );
		
		assertEquals( doc.valueOf("/root/complexAgain"), doc.valueOf("/root/complex") );
		
	}

	private Document createDocument(String rootElement) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement(rootElement);
		root.addElement("bar").setText("Hello");
		root.addElement("baz").setText("world");
		return doc;
	}
}
