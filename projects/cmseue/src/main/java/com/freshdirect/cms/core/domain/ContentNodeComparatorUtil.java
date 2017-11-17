package com.freshdirect.cms.core.domain;

import java.util.Map;

import org.springframework.util.Assert;

public final class ContentNodeComparatorUtil {

    private ContentNodeComparatorUtil() {
    }

    public static boolean isValueChanged(Attribute attribute, Object originalValue, Object changedValue) {
        if (originalValue == null && changedValue == null) {
            return false;
        } else if (originalValue != null && changedValue != null) {
            return !originalValue.equals(changedValue);
        } else {
            return true;
        }
    }

    public static boolean isNodeChanged(Map<Attribute, Object> incoming, Map<Attribute, Object> original) {
        Assert.notNull(incoming);
        Assert.notNull(original);

        // test attribute sets are equal
        if (!original.keySet().containsAll(incoming.keySet())) {
            return true;
        }
        if (!incoming.keySet().containsAll(original.keySet())) {
            return true;
        }

        // test content
        for (Attribute attribute : incoming.keySet()) {
            if (ContentNodeComparatorUtil.isValueChanged(attribute, original.get(attribute), incoming.get(attribute))) {
                return true;
            }
        }
        return false;
    }
}
