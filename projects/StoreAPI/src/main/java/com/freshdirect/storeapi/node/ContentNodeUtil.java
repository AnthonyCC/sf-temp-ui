package com.freshdirect.storeapi.node;

import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;

@CmsLegacy
public class ContentNodeUtil {

    private ContentNodeUtil() {
    }

    public static String getStringAttribute(ContentNodeI contentNode, String attributeName) {
        Object value = contentNode != null ? contentNode.getAttributeValue(attributeName) : null;

        return (value instanceof String) ? (String) value : null;
    }

    public static boolean getBooleanAttribute(ContentNodeI node, String name) {
        Object value = node.getAttributeValue(name);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return false;
    }

    public static Integer getIntegerAttribute(ContentNodeI contentNode, String attributeName) {
        Object value = contentNode.getAttributeValue(attributeName);
        if (value instanceof Integer) {
            return ((Integer) value);
        }
        return null;
    }

}
