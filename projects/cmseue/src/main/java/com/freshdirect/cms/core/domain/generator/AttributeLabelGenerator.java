package com.freshdirect.cms.core.domain.generator;

import static org.joox.JOOX.$;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joox.Context;
import org.joox.Each;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class AttributeLabelGenerator {

    private static final String TAB = "    ";

    private AttributeLabelGenerator() {
    }

    private static String generateAttributeLabels(File xmlDoc) throws SAXException, IOException {
        final StringBuilder typeBuilder = new StringBuilder();

        typeBuilder.append("package com.freshdirect.cms.ui.editor;\n\n");
        typeBuilder.append("\n");

        typeBuilder.append("import static com.freshdirect.cms.ui.editor.AttributeLabelKey.keyOf;\n");
        typeBuilder.append("\n");

        typeBuilder.append("import java.util.Map;\n");
        typeBuilder.append("import java.util.HashMap;\n");
        typeBuilder.append("\n");

        typeBuilder.append("import com.freshdirect.cms.core.domain.ContentTypes;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.Attribute;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.ContentType;\n");
        typeBuilder.append("import com.freshdirect.cms.ui.editor.AttributeLabelKey;\n");
        typeBuilder.append("\n\n");

        typeBuilder.append("@SuppressWarnings(\"serial\")\n");
        typeBuilder.append("public final class AttributeLabels {\n");

        typeBuilder.append(TAB + "public static final Map<AttributeLabelKey, String> ATTRIBUTE_LABELS = new HashMap<AttributeLabelKey, String>();\n");
        typeBuilder.append("\n");
        typeBuilder.append(TAB + "public static final Map<AttributeLabelKey, Map<String, String>> ENUM_LABELS = new HashMap<AttributeLabelKey, Map<String, String>>();\n");
        typeBuilder.append("\n\n");

        typeBuilder.append(TAB + "static {\n");

        $(xmlDoc).find("ContentTypeDef").each(new Each() {

            @Override
            public void each(Context context) {
                final Element elem = context.element();

                final String typeName = elem.getAttribute("name");

                $(elem).find("AttributeDef").each(new Each() {

                    @Override
                    public void each(Context context) {
                        Element attr = context.element();

                        final String attrName = attr.getAttribute("name");
                        final String attrLabel = attr.getAttribute("label");

                        typeBuilder.append(TAB + TAB
                                + "ATTRIBUTE_LABELS.put(keyOf(ContentType." + typeName + ", ContentTypes." + typeName + "." + attrName + "), \"" + attrLabel + "\");\n");

                    }
                });

                $(elem).find("EnumDef").each(new Each() {

                    @Override
                    public void each(Context context) {
                        Element enumItem = context.element();

                        final String attrName = enumItem.getAttribute("name");
                        final String attrLabel = enumItem.getAttribute("label");

                        typeBuilder.append(TAB + TAB
                                + "ATTRIBUTE_LABELS.put(keyOf(ContentType." + typeName + ", ContentTypes." + typeName + "." + attrName + "), \"" + attrLabel + "\");\n");

                        // collect enum value labels
                        Map<String, String> valueLabelMap = new LinkedHashMap<String, String>();
                        NodeList valueList = enumItem.getElementsByTagName("EnumValue");
                        for (int k = 0; k < valueList.getLength(); k++) {
                            Node n = valueList.item(k);
                            String enumValue = n.getAttributes().getNamedItem("value").getNodeValue();
                            String enumLabel = n.getAttributes().getNamedItem("label").getNodeValue();

                            valueLabelMap.put(enumValue, enumLabel);
                        }

                        typeBuilder.append(TAB + TAB
                                + "ENUM_LABELS.put(keyOf(ContentType." + typeName + ", ContentTypes." + typeName + "." + attrName + "), new HashMap<String, String>() {{\n");
                        for (String value : valueLabelMap.keySet()) {
                            String label = valueLabelMap.get(value);
                            typeBuilder.append(TAB + TAB + TAB + "put(\"" + value + "\", \"" + label + "\");\n");
                        }
                        typeBuilder.append(TAB + TAB + "}});\n");

                    }
                });

                $(elem).find("RelationshipDef").each(new Each() {

                    @Override
                    public void each(Context context) {
                        final Element attr = context.element();

                        final String attrName = attr.getAttribute("name");
                        final String attrLabel = attr.getAttribute("label");

                        typeBuilder.append(TAB + TAB
                                + "ATTRIBUTE_LABELS.put(keyOf(ContentType." + typeName + ", ContentTypes." + typeName + "." + attrName + "), \"" + attrLabel + "\");\n");

                    }
                });
                typeBuilder.append("\n");

            }
        });

        typeBuilder.append(TAB + "}\n");

        typeBuilder.append("}\n");

        return typeBuilder.toString();
    }

    public static void main(String[] args) throws IOException, SAXException {
        File xmlDoc = new File("CMSStoreDef.xml");
        System.out.println(generateAttributeLabels(xmlDoc));
    }
}
