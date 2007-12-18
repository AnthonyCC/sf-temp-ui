package com.freshdirect.framework.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.BeanUtil;
import com.freshdirect.framework.util.QuickDateFormat;

public class XMLSerializer {

	private final String[] expandedClasses;
	private final Class[] primitiveClasses;
	private final String[] includedMethods;
	private final String[] excludedMethods;
	
	private final boolean formatted = false;

	public XMLSerializer() {
		Collection c;
		
		c = this.getExpandedClasses();
		this.expandedClasses = (String[]) c.toArray(new String[c.size()]);
		
		c = this.getPrimitiveClasses();
		this.primitiveClasses = (Class[]) c.toArray(new Class[c.size()]);

		c = this.getIncludedMethods();
		this.includedMethods = (String[]) c.toArray(new String[c.size()]);

		c = this.getExcludedMethods();
		this.excludedMethods = (String[]) c.toArray(new String[c.size()]);
	}

	protected Collection getExpandedClasses() {
		Collection c = new ArrayList();
		c.add( "com.freshdirect" );
		return c;
	}

	protected Collection getPrimitiveClasses() {
		Collection c = new ArrayList();
		c.add( Boolean.class );
		c.add( Character.class );
		c.add( Number.class );
		c.add( String.class );
		c.add( StringBuffer.class );
		c.add( Enum.class );
		c.add( PrimaryKey.class );
		return c;
	}
	
	protected Collection getIncludedMethods() {
		Collection c = new ArrayList();
		c.add( "get" );
		c.add( "is" );
		c.add( "has" );
		return c;
	}

 	protected Collection getExcludedMethods() {
		Collection c = new ArrayList();
 		c.add( "hashCode" );
 		c.add( "getClass" );
 		return c;
 	}

	public Document serializeDocument(String rootNode, Object object) {
		Document document = DocumentHelper.createDocument();
		this.serialize(new ArrayList(), document, rootNode, object);
		return document;	
	}

	public String serialize(String rootNode, Object object) {
		if(formatted){
			Document doc = this.serializeDocument(rootNode, object);
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			outformat.setEncoding("UTF-8");
			StringWriter sw = new StringWriter();
			XMLWriter writer = new XMLWriter(sw, outformat);
			try {
				writer.write(doc);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sw.getBuffer().toString();
		}else{
			return this.serializeDocument(rootNode, object).asXML();
		}
	}	
		
	private void serialize(List visitedObjects, Branch node, String elementName, Object obj) {

		if (obj==null) {
			if (this.isShowNulls()) {
				node.addElement(elementName).addAttribute("isNull", "true");
			}
			return;
		}
		
		final Class objClass = obj.getClass();		

		if (obj instanceof Collection) {
			Element childNode = node.addElement(elementName);
			for (Iterator ci=((Collection)obj).iterator(); ci.hasNext(); ) {
				this.serialize(visitedObjects, childNode, elementName, ci.next());
			}
			
		} else if (obj instanceof Map) {

			Element childNode = node.addElement(elementName);
			Map map = (Map)obj;		
			for (Iterator i=map.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry)i.next();
				String name = (String)e.getKey();
				this.serialize(visitedObjects, childNode, name, e.getValue());
			}
			
		} else if (objClass.isArray()) {
			int len = Array.getLength(obj);
			Element childNode = node.addElement(elementName);
			for (int i=0; i<len; i++) {
				this.serialize(visitedObjects, childNode, elementName, Array.get(obj, i));			
			}
		
		} else if (obj instanceof Node) {
			Node cloneNode;
			if (obj instanceof Document) {
				cloneNode = (Node)((Document)obj).getRootElement().clone();
			} else {
				cloneNode = (Node)((Node)obj).clone();
			}
			cloneNode.setName( elementName );
			node.add( cloneNode );
			
		} else if (obj instanceof Date) {
			String fmt = QuickDateFormat.ISO_FORMATTER.format( (Date)obj );
			node.addElement( elementName ).addText( fmt );

		} else if ( this.isPrimitiveClass( objClass ) ) {
			node.addElement( elementName ).addText( String.valueOf(obj) );
		
		} else if (this.isExpandedClass( objClass )) {

			Integer idHash = new Integer(System.identityHashCode(obj));

			if (visitedObjects.contains(idHash)) {
				node.addElement(elementName).addAttribute("refid", String.valueOf(idHash));
				return;
			}

			visitedObjects.add(idHash);

			node = node.addElement(elementName);
			if (node instanceof Element) {
				((Element)node).addAttribute("id", String.valueOf(idHash));
			}

			Method[] methods = objClass.getMethods();
			
			for (int i=0; i<methods.length; i++) {
				Method method = methods[i];
	
				if (method.getParameterTypes().length!=0) {
					continue;
				}
				
				String childName = this.mapMethod(method);
				if (childName==null) {
					continue;	
				}
	
				try {
	
					Object o = method.invoke( obj, new Object[0] );
					this.serialize(visitedObjects, node, childName, o);
	
				} catch (InvocationTargetException ex) {
					if (this.isShowExceptions()) {
						node.addElement(elementName).addAttribute("isException", ex.getTargetException().toString());
					}
	
				} catch (Exception ex) {
					//out.println("<"+elementName+" isError='"+ex+"'/>");
				}
			}

			visitedObjects.remove(visitedObjects.size()-1);	

		} else {
			node.addElement(elementName).addText( obj.toString() );
		
		}
	}

	private boolean isExpandedClass(Class klass) {
		String name = klass.getName();
		for (int x=0; x<expandedClasses.length; x++) {
			if (name.startsWith(expandedClasses[x])) {
				return true;
			}
		}
		return false;
	}

	/** @return normalized element name, or null if method doesn't need to be mapped */
	private String mapMethod(Method method) {

		String methodName = method.getName();
		for (int j=0; j<includedMethods.length; j++) {
			
			if (methodName.startsWith(includedMethods[j])) {
		
				if (this.isMethodExcluded(methodName)) {
					return null;
				}

				return BeanUtil.decapitalize(methodName.substring(includedMethods[j].length()) );

			}
		}

		return null;
	}
	
	private boolean isMethodExcluded(String methodName) {
		for (int k=0; k<excludedMethods.length; k++) {
			if (methodName.startsWith(excludedMethods[k])) {
				return true;
			}
		}
		return false;
	}

	private boolean isPrimitiveClass(Class klass) {
		for (int x=0; x<primitiveClasses.length; x++) {
			if (primitiveClasses[x].isAssignableFrom(klass)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isShowExceptions() {
		return false;
	}

	protected boolean isShowNulls() {
		return false;
	}

}
