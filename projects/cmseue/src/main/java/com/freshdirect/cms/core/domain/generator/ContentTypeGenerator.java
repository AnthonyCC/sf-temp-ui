package com.freshdirect.cms.core.domain.generator;

import static org.joox.JOOX.$;

import java.io.File;
import java.io.IOException;

import org.joox.Context;
import org.joox.Each;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class ContentTypeGenerator {

    private static final String TAB = "    ";

    private ContentTypeGenerator() {
    }

    private static boolean getOptionalBooleanValue(Element elem, String attrName, boolean defaultValue) {
        return elem.hasAttribute(attrName)
                ? Boolean.valueOf(elem.getAttribute(attrName))
                : defaultValue;
    }

    private static String generateTypeEnum(File xmlDoc) throws SAXException, IOException {

        final StringBuilder typeBuilder = new StringBuilder();

        typeBuilder.append("package com.freshdirect.cms.core.domain;\n\n");
        typeBuilder.append("public enum ContentType {\n");

        // generate types
        $(xmlDoc).find("ContentTypeDef").each(new Each() {
            @Override
            public void each(Context context) {
                Element elem = context.element();
                boolean genValue = getOptionalBooleanValue(elem, "generateId", false);

                if (genValue) {
                    typeBuilder.append(TAB + elem.getAttribute("name") + "(true),\n");
                } else {
                    typeBuilder.append(TAB + elem.getAttribute("name") + "(false),\n");
                }
            }
        });

        typeBuilder
            // fields
            .append(TAB + ";\n\n")
            .append(TAB + "/** Denotes that type has generated content ID */\n")
            .append(TAB + "public final boolean generatedContentId;\n")
            .append("\n")

            // constructors
            .append(TAB + "ContentType(final boolean generatedContentId) {\n")
            .append(TAB + TAB + "this.generatedContentId = generatedContentId;\n")
            .append(TAB + "}\n");

        typeBuilder.append("}\n");

        return typeBuilder.toString();
    }

    public static void main(String[] args) throws SAXException, IOException {
        File xmlDoc = new File("CMSStoreDef.xml");
        System.out.println(generateTypeEnum(xmlDoc));
    }
}
