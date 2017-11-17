package com.freshdirect.cms.core.domain.generator;

import static org.joox.JOOX.$;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joox.Context;
import org.joox.Each;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport;
import com.google.common.base.Joiner;

public final class ContentTypeAttributesGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentTypeAttributesGenerator.class);

    private static final String TAB = "    ";

    private ContentTypeAttributesGenerator() {
    }

    private static boolean getOptionalBooleanValue(Element elem, String attrName, boolean defaultValue) {
        return elem.hasAttribute(attrName)
                ? Boolean.valueOf(elem.getAttribute(attrName))
                : defaultValue;
    }

    private static AttributeFlags processFlags(Element elem) {
        boolean inheritable = getOptionalBooleanValue(elem, "inheritable", false);
        boolean required = getOptionalBooleanValue(elem, "required", false);

        AttributeFlags flagz = AttributeFlags.valueOf(required, inheritable);
        return flagz;
    }

    private static String generateAttributeEnum(File xmlDoc) throws SAXException, IOException {
        final StringBuilder typeBuilder = new StringBuilder();

        typeBuilder.append("package com.freshdirect.cms.core.domain;\n\n");

        typeBuilder.append("import com.freshdirect.cms.core.domain.ContentType;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.Attribute;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.Scalar;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.Relationship;\n");
        typeBuilder.append("import com.freshdirect.cms.core.domain.RelationshipCardinality;\n");
        typeBuilder.append("\n\n");

        // Iterators.tra Arrays.asList(ContentType.values()).

        for (ContentType en : ContentType.values()) {
            typeBuilder.append("import static " + ContentType.class.getCanonicalName() + "." + en.name() + ";\n");
        }
        typeBuilder.append("\n\n");

        for (AttributeFlags en :  AttributeFlags.values()) {
            typeBuilder.append("import static " + AttributeFlags.class.getCanonicalName() + "." + en.name() + ";\n");
        }
        typeBuilder.append("\n\n");

        for (RelationshipCardinality en :  RelationshipCardinality.values()) {
            typeBuilder.append("import static " + RelationshipCardinality.class.getCanonicalName() + "." + en.name() + ";\n");
        }
        typeBuilder.append("\n\n");

        for (Method meth : AttributeBuilderSupport.class.getDeclaredMethods()) {
            typeBuilder.append("import static " + AttributeBuilderSupport.class.getCanonicalName() + "." + meth.getName() + ";\n");
        }
        typeBuilder.append("\n\n");

        typeBuilder.append("public final class ContentTypes {\n");

        final Joiner stringJoiner = Joiner.on(",").skipNulls();

        // generate types
        $(xmlDoc).find("ContentTypeDef").each(new Each() {
            @Override
            public void each(Context context) {
                final Element elem = context.element();

                final String typeName = elem.getAttribute("name");
                typeBuilder.append(TAB + "public static final class " + typeName + " {\n");


                $(elem).find("AttributeDef").each(new Each() {
                    @Override
                    public void each(Context context) {
                        Element attr = context.element();

                        final String attrName = attr.getAttribute("name");

                        final String type = attr.getAttribute("type");

                        // Labels are not included
                        //final String label = attr.getAttribute("label");

                        AttributeFlags flagz = processFlags(attr);

                        typeBuilder.append(TAB + TAB + "public static final Attribute " + attrName + " = ");
                        if ("S".equals(type) || "TXT".equals(type)) {
                            typeBuilder.append("stringAttribute(\"" + attrName + "\")\n");
                        } else if ("B".equals(type)) {
                            typeBuilder.append("booleanAttribute(\"" + attrName + "\")\n");
                        } else if ("I".equals(type)) {
                            typeBuilder.append("integerAttribute(\"" + attrName + "\")\n");
                        } else if ("D".equals(type)) {
                            typeBuilder.append("doubleAttribute(\"" + attrName + "\")\n");
                        } else if ("DT".equals(type) || "TS".equals(type)) {
                            typeBuilder.append("attribute().name(\"" + attrName + "\").type(java.util.Date.class)\n");
                        } else {
                            LOGGER.error("Attribute " + attrName + " of type " + typeName + " is not mapped!");
                        }

                        if (flagz != AttributeFlags.NONE) {
                            typeBuilder.append(TAB + TAB + TAB);
                            if (flagz.isRequired()) {
                                typeBuilder.append(".required()");
                            }
                            if (flagz.isInheritable()) {
                                typeBuilder.append(".inheritable()");
                            }
                            typeBuilder.append("\n");
                        }
                        typeBuilder.append(TAB + TAB + TAB + ".build();\n");
                    }
                });

                typeBuilder.append("\n");

                $(elem).find("EnumDef").each(new Each() {
                    @Override
                    public void each(Context context) {
                        Element enumItem = context.element();

                        final String attrName = enumItem.getAttribute("name");
                        final String type = enumItem.getAttribute("type");

                        AttributeFlags flagz = processFlags(enumItem);

                        // TODO: process enum values
                        // EnumValue value="1" label="SECONDARY_FOLDERS"
                        final boolean isInteger = "I".equals(type);
                        final boolean isString = "S".equals(type);


                        final List<String> rawEnums = new ArrayList<String>();
                        $(enumItem).find("EnumValue").each(new Each() {
                            @Override
                            public void each(Context context) {
                                Element el = context.element();
                                if (isString) {
                                    rawEnums.add("\"" + el.getAttribute("value") + "\"");
                                } else {
                                    rawEnums.add(el.getAttribute("value"));
                                }
                            }
                        });

                        typeBuilder.append(TAB + TAB + "public static final Attribute " + attrName + " = ");
                        if (isString) {
                            typeBuilder.append("stringEnum(\"" + attrName + "\")\n");
                            typeBuilder.append(TAB + TAB + TAB + ".withValues(" + stringJoiner.join(rawEnums) + ")\n");
                        } else if (isInteger) {
                            typeBuilder.append("integerEnum(\"" + attrName + "\")\n");
                            typeBuilder.append(TAB + TAB + TAB + ".withValues(" + stringJoiner.join(rawEnums) + ")\n");
                        } else {
                            LOGGER.error("Enum " + attrName + " of type " + typeName + " is not mapped!");
                        }

                        if (flagz != AttributeFlags.NONE) {
                            typeBuilder.append(TAB + TAB + TAB);
                            if (flagz.isRequired()) {
                                typeBuilder.append(".required()");
                            }
                            if (flagz.isInheritable()) {
                                typeBuilder.append(".inheritable()");
                            }
                            typeBuilder.append("\n");
                        }
                        typeBuilder.append(TAB + TAB + TAB + ".build();\n");

                    }
                });

                $(elem).find("RelationshipDef").each(new Each() {
                    @Override
                    public void each(Context context) {
                        final Element attr = context.element();

                        final String attrName = attr.getAttribute("name");
                        //final String label = attr.getAttribute("label");

                        boolean navigable = getOptionalBooleanValue(attr, "navigable", false);

                        RelationshipCardinality c = attr.hasAttribute("cardinality")
                                ? RelationshipCardinality.valueOf(attr.getAttribute("cardinality").toUpperCase())
                                : RelationshipCardinality.ONE;

                        final List<ContentType> destinationTypes = new ArrayList<ContentType>();
                        $(attr).find("DestinationDef").each(new Each() {
                            @Override
                            public void each(Context context) {
                                Element da = context.element();
                                destinationTypes.add(ContentType.valueOf(da.getAttribute("contentType")));
                            }
                        });

                        Collections.sort(destinationTypes, new Comparator<ContentType>() {
                            @Override
                            public int compare(ContentType o1, ContentType o2) {
                                return o1.name().compareTo(o2.name());
                            }
                        });

                        String dt = stringJoiner.join(destinationTypes);

                        AttributeFlags flagz = processFlags(attr);

                        typeBuilder.append(TAB + TAB + "public static final Attribute " + attrName + " = ");
                        if (c == RelationshipCardinality.ONE) {
                            typeBuilder.append("linkOneOf(" + dt + ").toName(\"" + attrName + "\")\n");
                            // typeBuilder.append("singleRelationship(\""+attrName+"\", "+dt+")\n");
                        } else {
                            // typeBuilder.append("multiRelationship(\""+attrName+"\", "+dt+")\n");
                            typeBuilder.append("linkManyOf(" + dt + ").toName(\"" + attrName + "\")\n");
                        }

                        if (flagz != AttributeFlags.NONE) {
                            typeBuilder.append(TAB + TAB + TAB);
                            if (flagz.isRequired()) {
                                typeBuilder.append(".required()");
                            }
                            if (flagz.isInheritable()) {
                                typeBuilder.append(".inheritable()");
                            }
                            typeBuilder.append("\n");
                        }
                        if (navigable) {
                            typeBuilder.append(TAB + TAB + TAB + ".navigable()\n");
                        }

                        typeBuilder.append(TAB + TAB + TAB + ".build();\n");

                    }
                });
                typeBuilder.append(TAB + "}\n\n");
            }
        });

        typeBuilder.append("}\n");

        return typeBuilder.toString();
    }

    public static void main(String[] args) throws IOException, SAXException {
        File xmlDoc = new File("CMSStoreDef.xml");
        System.out.println(generateAttributeEnum(xmlDoc));
    }
}
