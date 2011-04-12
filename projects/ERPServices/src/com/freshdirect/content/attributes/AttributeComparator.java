/*
 * $Workfile: AttributeComparator.java$
 *
 * $Date: 8/30/2001 9:11:56 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.content.attributes;

import java.util.Comparator;

/**
 * a set of convenience classes for sorting objects by their attributes
 * all comparators sort in ascending order
 *
 * @version $Revision: 1$
 * @author $Author: Mike Rose$
 */
public class AttributeComparator {
    
//    private static String   NO_STRING_VALUE   = "ZZZZZZZZZZ";
    private static int      NO_INTEGER_VALUE  = Integer.MAX_VALUE;
//    private static boolean  NO_BOOLEAN_VALUE  = false;

    public static Comparator<AttributesI> PRIORITY = new Priority<AttributesI>();
    
    public static class Priority<T extends AttributesI> implements Comparator<T> {
        public int compare(T o1, T o2) {
            int p1 = o1.getAttributeInt(EnumAttributeName.PRIORITY.getName(), NO_INTEGER_VALUE);
            int p2 = o2.getAttributeInt(EnumAttributeName.PRIORITY.getName(), NO_INTEGER_VALUE);
            return p1 - p2;
        }
    }
//    
//    public static Comparator DESCRIPTION = new Description();
//    public static class Description implements Comparator {
//        public int compare(Object o1, Object o2) {
//            String d1 = ((AttributesI) o1).getAttribute(EnumAttributeName.DESCRIPTION.getName(), NO_STRING_VALUE);
//            String d2 = ((AttributesI) o2).getAttribute(EnumAttributeName.DESCRIPTION.getName(), NO_STRING_VALUE);
//            return d1.compareTo(d2);
//        }
//    };
//    
//    public static Comparator DISPLAY_GROUP = new DisplayGroup();
//    public static class DisplayGroup implements Comparator {
//        public int compare(Object o1, Object o2) {
//            String d1 = ((AttributesI) o1).getAttribute(EnumAttributeName.DISPLAY_GROUP.getName(), NO_STRING_VALUE);
//            String d2 = ((AttributesI) o2).getAttribute(EnumAttributeName.DISPLAY_GROUP.getName(), NO_STRING_VALUE);
//            return d1.compareTo(d2);
//        }
//    };
//    
//    public static Comparator DISPLAY_FORMAT = new DisplayFormat();
//    public static class DisplayFormat implements Comparator {
//        public int compare(Object o1, Object o2) {
//            String d1 = ((AttributesI) o1).getAttribute(EnumAttributeName.DISPLAY_FORMAT.getName(), NO_STRING_VALUE);
//            String d2 = ((AttributesI) o2).getAttribute(EnumAttributeName.DISPLAY_FORMAT.getName(), NO_STRING_VALUE);
//            return d1.compareTo(d2);
//        }
//    };
//    
//    public static Comparator UNDER_LABEL = new UnderLabel();
//    public static class UnderLabel implements Comparator {
//        public int compare(Object o1, Object o2) {
//            String d1 = ((AttributesI) o1).getAttribute(EnumAttributeName.UNDER_LABEL.getName(), NO_STRING_VALUE);
//            String d2 = ((AttributesI) o2).getAttribute(EnumAttributeName.UNDER_LABEL.getName(), NO_STRING_VALUE);
//            return d1.compareTo(d2);
//        }
//    };
//    
//    public static Comparator PRODUCT_CODE = new ProductCode();
//    public static class ProductCode implements Comparator {
//        public int compare(Object o1, Object o2) {
//            String d1 = ((AttributesI) o1).getAttribute(EnumAttributeName.PRODUCT_CODE.getName(), NO_STRING_VALUE);
//            String d2 = ((AttributesI) o2).getAttribute(EnumAttributeName.PRODUCT_CODE.getName(), NO_STRING_VALUE);
//            return d1.compareTo(d2);
//        }
//    };
//    
//    public static Comparator SELECTED = new Selected();
//    public static class Selected implements Comparator {
//        public int compare(Object o1, Object o2) {
//            boolean b1 = ((AttributesI) o1).getAttributeBoolean(EnumAttributeName.SELECTED.getName(), NO_BOOLEAN_VALUE);
//            boolean b2 = ((AttributesI) o2).getAttributeBoolean(EnumAttributeName.SELECTED.getName(), NO_BOOLEAN_VALUE);
//            if (b1 && !b2) return -1;
//            if (!b1 && b2) return 1;
//            return 0;
//        }
//    };
//    
//    public static Comparator OPTIONAL = new Optional();
//    public static class Optional implements Comparator {
//        public int compare(Object o1, Object o2) {
//            boolean b1 = ((AttributesI) o1).getAttributeBoolean(EnumAttributeName.OPTIONAL.getName(), NO_BOOLEAN_VALUE);
//            boolean b2 = ((AttributesI) o2).getAttributeBoolean(EnumAttributeName.OPTIONAL.getName(), NO_BOOLEAN_VALUE);
//            if (b1 && !b2) return -1;
//            if (!b1 && b2) return 1;
//            return 0;
//        }
//    };


}
