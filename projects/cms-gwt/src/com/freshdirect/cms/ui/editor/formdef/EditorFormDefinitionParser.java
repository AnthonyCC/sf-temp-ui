package com.freshdirect.cms.ui.editor.formdef;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.cms.ui.editor.formdef.data.Editor;
import com.freshdirect.cms.ui.editor.formdef.data.Field;
import com.freshdirect.cms.ui.editor.formdef.data.FieldType;
import com.freshdirect.cms.ui.editor.formdef.data.Page;
import com.freshdirect.cms.ui.editor.formdef.data.Section;
import com.google.common.base.Strings;

public class EditorFormDefinitionParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorFormDefinitionParser.class);

    private Iterator<String> idGenerator = new Iterator<String>() {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public String next() {
            return MessageFormat.format("gid{0}", index++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };

    private Map<String, Editor> editorsMap = new HashMap<String, Editor>();
    private Map<String, Page> pagesMap = new HashMap<String, Page>();
    private Map<String, Section> sectionsMap = new HashMap<String, Section>();

    public EditorFormDefinitionParser() {
    }

    public List<Editor> parse() {
        try {
            ClassPathResource res = new ClassPathResource("FDForms.xml", EditorFormDefinitionParser.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(res.getInputStream());

            Element root = doc.getDocumentElement();

            parseEditors(doc, root);

            return new ArrayList<Editor>(editorsMap.values());
        } catch (IOException e) {
            LOGGER.error("Failed to load form definition file", e);
        } catch (ParserConfigurationException e) {
            LOGGER.error("Error occurred while configuring XML parser", e);
        } catch (SAXException e) {
            LOGGER.error("Failed to process XML data", e);
        }
        return Collections.emptyList();
    }

    private Element findElement(Document doc, String tagName, String id) {
        for (Node n : new NodeListIterable(doc.getElementsByTagName("*"))) {
            if (n.getNodeType() == Node.ELEMENT_NODE && tagName.equals(((Element) n).getTagName()) && ((Element) n).hasAttribute("id")
                    && id.equals(((Element) n).getAttribute("id"))) {

                Element elem = (Element) n;

                return elem;
            }
        }
        return null;
    }

    private void parseEditors(Document doc, Element root) {
        for (Node node : new NodeListIterable(root.getElementsByTagName("CmsEditor"))) {
            Element editorNode = (Element) node;

            label: try {

                String elemId = editorNode.getAttribute("id");
                String elemRef = editorNode.getAttribute("ref");

                if (!Strings.isNullOrEmpty(elemRef)) {
                    if (editorsMap.containsKey(elemRef)) {
                        LOGGER.debug("Editor " + elemRef + "already processed");

                        break label;
                    } else {
                        editorNode = findElement(doc, "CmsEditor", elemRef);
                        elemId = editorNode.getAttribute("id");
                    }
                } else if (Strings.isNullOrEmpty(elemId)) {
                    break label;
                }

                String typeString = parseTextContent(editorNode, "contentType");
                if (typeString == null) {
                    break label;
                }

                List<Page> pages = parsePages(doc, editorNode.getElementsByTagName("pages").item(0).getChildNodes());

                final Editor editor = new Editor(withId(elemId), typeString, pages);
                editorsMap.put(editor.id, editor);

                // LOGGER.info("Parsed editor: " + editor);
            } catch (NullPointerException exc) {
                LOGGER.error("Failed to parse editor: null " + editorNode, exc);
            } catch (IllegalArgumentException exc) {
                LOGGER.error("Failed to parse editor " + editorNode.getElementsByTagName("contentType").item(0).getTextContent(), exc);
            }
        }
    }

    private List<Page> parsePages(Document doc, NodeList pageElementList) {
        List<Page> pages = new ArrayList<Page>();

        for (Node node : new NodeListIterable(pageElementList)) {
            label: try {
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element pageNode = (Element) node;

                String elemId = pageNode.getAttribute("id");
                String elemRef = pageNode.getAttribute("ref");

                if (!Strings.isNullOrEmpty(elemRef)) {
                    if (pagesMap.containsKey(elemRef)) {
                        LOGGER.debug("Page " + elemRef + "already processed");
                        pages.add(pagesMap.get(elemRef));
                        break label;
                    } else {
                        pageNode = findElement(doc, "CmsPage", elemRef);
                        elemId = pageNode.getAttribute("id");
                    }
                }

                String title = parseTextContent(pageNode, "title");
                if (title == null) {
                    break label;
                }

                List<Section> sections = parseSections(doc, pageNode.getElementsByTagName("sections").item(0).getChildNodes());

                Page page = new Page(withId(elemId), title, sections);

                pagesMap.put(elemId, page);

                LOGGER.debug("Parsed page " + page);

                pages.add(page);
            } catch (Exception exc) {
                LOGGER.error("Failed to process page", exc);
            }

        }

        return pages;
    }

    private String withId(String id) {
        return Strings.isNullOrEmpty(id) ? idGenerator.next() : id;
    }

    private String parseTextContent(Element elem, String tagName) {
        NodeList nl = elem.getElementsByTagName(tagName);

        String textContent = null;

        for (Node node : new NodeListIterable(nl)) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                textContent = node.getTextContent();
                break;
            }
        }

        if (nl.getLength() == 1) {
            textContent = nl.item(0).getTextContent();
        }

        return textContent;
    }

    private List<Section> parseSections(Document doc, NodeList childNodes) {
        List<Section> sections = new ArrayList<Section>();

        for (Node node : new NodeListIterable(childNodes)) {
            label: try {
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element sectionNode = (Element) node;

                String elemId = sectionNode.getAttribute("id");
                String elemRef = sectionNode.getAttribute("ref");

                if (!Strings.isNullOrEmpty(elemRef)) {
                    if (sectionsMap.containsKey(elemRef)) {
                        LOGGER.debug("Section " + elemRef + "already processed");
                        sections.add(sectionsMap.get(elemRef));
                        break label;
                    } else {
                        sectionNode = findElement(doc, "CmsSection", elemRef);
                        elemId = sectionNode.getAttribute("id");
                    }
                }

                String title = parseTextContent(sectionNode, "title");
                if (title == null) {
                    break label;
                }

                List<Field> fields = parseFields(sectionNode.getElementsByTagName("fields").item(0).getChildNodes());

                Section section = new Section(withId(elemId), title, fields);
                sectionsMap.put(section.id, section);

                LOGGER.debug("Parsed section " + section);
                sections.add(section);
            } catch (Exception exc) {
                LOGGER.error("Failed to process section, node type=" + node.getNodeType(), exc);
            }

        }

        return sections;
    }

    private List<Field> parseFields(NodeList childNodes) {
        List<Field> fields = new ArrayList<Field>();

        for (Node node : new NodeListIterable(childNodes)) {
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element elem = (Element) node;

            Field field = null;
            if ("CmsField".equals(elem.getTagName())) {
                String attributeName = parseTextContent(elem, "attribute");

                field = new Field(withId(null), FieldType.CmsField, attributeName, null, null);

            } else if ("CmsCustomField".equals(elem.getTagName())) {
                String attributeName = parseTextContent(elem, "attribute");
                String customComponentName = parseTextContent(elem, "component");

                field = new Field(withId(null), FieldType.CmsCustomField, attributeName, customComponentName, null);
            } else if ("CmsMultiColumnField".equals(elem.getTagName())) {
                String attributeName = parseTextContent(elem, "attribute");
                List<String> columns = new ArrayList<String>();

                for (Node columnsNode : new NodeListIterable(elem.getElementsByTagName("CmsField"))) {
                    if (columnsNode.getNodeType() == Node.ELEMENT_NODE) {
                        String columnAttributeName = parseTextContent(((Element) columnsNode), "attribute");
                        columns.add(columnAttributeName);
                    }
                }

                field = new Field(withId(null), FieldType.CmsMultiColumnField, attributeName, null, columns);
            } else if ("CmsGridField".equals(elem.getTagName())) {
                String attributeName = parseTextContent(elem, "attribute");
                List<String> columns = new ArrayList<String>();
                for (Node columnsNode : new NodeListIterable(elem.getElementsByTagName("CmsField"))) {
                    if (columnsNode.getNodeType() == Node.ELEMENT_NODE) {
                        String columnAttributeName = parseTextContent(((Element) columnsNode), "attribute");
                        columns.add(columnAttributeName);
                    }
                }

                field = new Field(withId(null), FieldType.CmsGridField, attributeName, null, columns);
            } else {
                LOGGER.warn(" !! unknown field tag " + elem.getTagName());
            }

            if (field != null) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static void main(String[] args) {
        new EditorFormDefinitionParser().parse();
    }
}
