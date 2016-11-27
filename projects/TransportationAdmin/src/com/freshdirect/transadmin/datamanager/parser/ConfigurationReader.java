/**
 * Flatworm - A Java Flat File Importer Copyright (C) 2004 James M. Turner
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 */

package com.freshdirect.transadmin.datamanager.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * The <code>ConfigurationReader<code> class is used to initialize Flatworm with an XML configuration file which
 *  describes the format and conversion options to be applied to the input file to produce output beans.
 */

public class ConfigurationReader
{

    /**
     * <code>loadConfigurationFile</code> takes an XML configuration file, and returns a <code>FileFormat</code>
     * object, which can be used to parse an input file into beans.
     * 
     * @param xmlFile An XML file which contains a valid Flatworm configuration
     * @return A <code>FileFormat</code> object which can parse the specified format.
     * @throws FlatwormUnsetFieldValueException If a required parameter of a tag is not set.
     * @throws FlatwormConfigurationValueException If the file contains invalid syntax.
     */
    public FileFormat loadConfigurationFile(String xmlFile) throws FlatwormUnsetFieldValueException,
            FlatwormConfigurationValueException
    {
        java.io.InputStream in = null;
        try
        {        	       	
            in = ClassUtils.getDefaultClassLoader().getResourceAsStream(xmlFile);
            // Load the default properties through default classloader
            if (in == null)
            {
            	 // Load the default properties through default file path
                in = (new java.io.FileInputStream(xmlFile));
            }
            return loadConfigurationFile(in);
        } catch (IOException e)
        {
        	//   Unable to load file
            e.printStackTrace();
        }
        return null;
    }

    public FileFormat loadConfigurationFile(InputStream in) throws FlatwormUnsetFieldValueException,
            FlatwormConfigurationValueException
    {
        DocumentBuilder parser;
        Document document;
        NodeList children;

        try
        {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            parser = fact.newDocumentBuilder();
            document = parser.parse((new org.xml.sax.InputSource(in)));
            children = document.getChildNodes();
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (("file-format".equals(child.getNodeName())) && (child.getNodeType() == Node.ELEMENT_NODE))
                {
                    return (FileFormat) traverse(child);
                }
            }
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Vector getChildNodes(Node node) throws FlatwormUnsetFieldValueException,
            FlatwormConfigurationValueException
    {
        Vector v = new Vector();
        NodeList children = node.getChildNodes();
        if (children != null)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                Object o = traverse(child);
                if (o != null)
                    v.add(o);
            }
        }
        return v;
    }

    private boolean isElementNodeOfType(String type, Node node)
    {
        return type.equals(node.getNodeName()) && node.getNodeType() == 1;
    }

    private Node getChildElementNodeOfType(String type, Node node)
    {
        NodeList children = node.getChildNodes();
        if (children != null)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (type.equals(child.getNodeName()) && child.getNodeType() == 1)
                    return child;
            }
        }
        return null;
    }

    private Vector getChildElementNodesOfType(String type, Node node)
    {
        Vector v = new Vector();
        NodeList children = node.getChildNodes();
        if (children != null)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (type.equals(child.getNodeName()) && child.getNodeType() == 1)
                    v.add(child);
            }
        }
        return v;
    }

    private String getChildTextNodeValue(Node node)
    {
        NodeList children = node.getChildNodes();
        if (children != null)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (child.getNodeType() == 3)
                    return child.getNodeValue();
            }
        }
        return null;
    }

    private boolean hasAttributeValueNamed(Node node, String name)
    {
        return node.getAttributes().getNamedItem(name) != null;
    }

    private String getAttributeValueNamed(Node node, String name)
    {
        NamedNodeMap map = node.getAttributes();
        return map.getNamedItem(name).getNodeValue();
    }

    private Node getAttributeNamed(Node node, String name)
    {
        NamedNodeMap map = node.getAttributes();
        return map.getNamedItem(name);
    }

    private Object traverse(Node node) throws FlatwormUnsetFieldValueException, FlatwormConfigurationValueException
    {
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE)
        {
            NamedNodeMap map = node.getAttributes();
            String nodeName = node.getNodeName();
            if (nodeName.equals("file-format"))
            {
                FileFormat f = new FileFormat();
                Vector v = getChildNodes(node);
                for (int i = 0; i < v.size(); i++)
                {
                    if (v.elementAt(i).getClass().equals(Converter.class))
                    {
                        f.addConverter((Converter) v.elementAt(i));
                    }
                    if (v.elementAt(i).getClass().equals(Record.class))
                    {
                        f.addRecord((Record) v.elementAt(i));
                    }
                }
                return f;
            }
            if (nodeName.equals("converter"))
            {
                Converter c = new Converter();
                c.setConverterClass(getAttributeValueNamed(node, "class"));
                c.setMethod(getAttributeValueNamed(node, "method"));
                c.setReturnType(getAttributeValueNamed(node, "return-type"));
                c.setName(getAttributeValueNamed(node, "name"));
                return c;
            }
            if (nodeName.equals("record"))
            {
                Record r = new Record();
                r.setName(getAttributeValueNamed(node, "name"));
                Node identChild = getChildElementNodeOfType("record-ident", node);
                if (identChild != null)
                {
                    Node fieldChild = getChildElementNodeOfType("field-ident", identChild);
                    Node lengthChild = getChildElementNodeOfType("length-ident", identChild);
                    if (lengthChild != null)
                    {
                        r.setLengthIdentMin(Integer.parseInt(getAttributeValueNamed(lengthChild, "minlength")));
                        r.setLengthIdentMax(Integer.parseInt(getAttributeValueNamed(lengthChild, "maxlength")));
                        r.setIdentTypeFlag('L');
                    } else if (fieldChild != null)
                    {
                        r.setFieldIdentStart(Integer.parseInt(getAttributeValueNamed(fieldChild, "field-start")));
                        r.setFieldIdentLength(Integer.parseInt(getAttributeValueNamed(fieldChild, "field-length")));
                        Vector matchNodes = getChildElementNodesOfType("match-string", fieldChild);
                        for (int j = 0; j < matchNodes.size(); j++)
                        {
                            r.addFieldIdentMatchString(getChildTextNodeValue((Node) matchNodes.get(j)));
                        }
                        r.setIdentTypeFlag('F');
                    }
                }
                Node recordChild = getChildElementNodeOfType("record-definition", node);
                r.setRecordDefinition((RecordDefinition) traverse(recordChild));
                return r;
            }
            if (nodeName.equals("record-definition"))
            {
                RecordDefinition rd = new RecordDefinition();
                Vector v = getChildNodes(node);
                for (int i = 0; i < v.size(); i++)
                {
                    Object o = v.get(i);
                    if (o.getClass().equals(Bean.class))
                    {
                        rd.addBeanUsed((Bean) o);
                    }
                    if (o.getClass().equals(Line.class))
                    {
                        rd.addLine((Line) o);
                    }
                }
                return rd;
            }
            if (nodeName.equals("bean"))
            {
                Bean b = new Bean();
                b.setBeanName(getAttributeValueNamed(node, "name"));
                b.setBeanClass(getAttributeValueNamed(node, "class"));
                return b;
            }
            if (nodeName.equals("line"))
            {
                Line li = new Line();

                // JBL - Determine if this line is delimited
                // Determine value of quote character, default = "
                // These field is optional
                Node delimit = getAttributeNamed(node, "delimit");
                Node quote = getAttributeNamed(node, "quote");
                if (delimit != null)
                {
                    li.setDelimeter(getAttributeValueNamed(node, "delimit"));
                }
                if (quote != null)
                {
                    li.setQuoteChar(getAttributeValueNamed(node, "quote"));
                }
                Vector v = getChildNodes(node);
                for (int i = 0; i < v.size(); i++)
                {
                    Object o = v.get(i);
                    if (o.getClass().equals(RecordElement.class))
                    {
                        li.addRecordElement((RecordElement) o);
                    }
                }
                return li;
            }
            if (nodeName.equals("record-element"))
            {
                RecordElement re = new RecordElement();

                Node start = getAttributeNamed(node, "start");
                Node end = getAttributeNamed(node, "end");
                Node length = getAttributeNamed(node, "length");
                Node beanref = getAttributeNamed(node, "beanref");
                Node beanType = getAttributeNamed(node, "type");
                if ((end == null) && (length == null))
                {
                    FlatwormConfigurationValueException err = new FlatwormConfigurationValueException(
                            "Must set either the 'end' or 'length' properties");
                    throw err;
                }
                if ((end != null) && (length != null))
                {
                    FlatwormConfigurationValueException err = new FlatwormConfigurationValueException(
                            "Can't specify both the 'end' or 'length' properties");
                    throw err;
                }
                if (start != null)
                {
                    re.setFieldStart(Integer.parseInt(start.getNodeValue()));
                }
                if (end != null)
                {
                    re.setFieldEnd(Integer.parseInt(end.getNodeValue()));
                }
                if (length != null)
                {
                    re.setFieldLength(Integer.parseInt(length.getNodeValue()));
                }
                if (beanref != null)
                {
                    re.setBeanRef(beanref.getNodeValue());
                }
                if (beanType != null)
                {
                    re.setType(beanType.getNodeValue());
                }
                Vector v = getChildElementNodesOfType("conversion-option", node);
                for (int i = 0; i < v.size(); i++)
                {
                    Node o = (Node) v.get(i);

                    String name = getAttributeValueNamed(o, "name");
                    String value = getAttributeValueNamed(o, "value");
                    ConversionOption co = new ConversionOption(name, value);

                    re.addConversionOption(name, co);
                }
                return re;
            }

        }
        return null;
    }

}
