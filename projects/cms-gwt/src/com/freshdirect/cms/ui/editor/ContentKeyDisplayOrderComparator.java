package com.freshdirect.cms.ui.editor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;

public class ContentKeyDisplayOrderComparator implements Comparator<ContentKey> {

    private final static Map<ContentType, Integer> DEFAULT_ORDER = new HashMap<ContentType, Integer>();
    static {
        DEFAULT_ORDER.put(ContentType.Store, new Integer(103));
        DEFAULT_ORDER.put(ContentType.Department, new Integer(102));
        DEFAULT_ORDER.put(ContentType.FDFolder, new Integer(101));
        DEFAULT_ORDER.put(ContentType.Category, new Integer(100));

        DEFAULT_ORDER.put(ContentType.GlobalNavigation, new Integer(99));
        DEFAULT_ORDER.put(ContentType.MyFD, new Integer(98));
        DEFAULT_ORDER.put(ContentType.SuperDepartment, new Integer(97));

        DEFAULT_ORDER.put(ContentType.Product, new Integer(61));
        DEFAULT_ORDER.put(ContentType.ConfiguredProduct, new Integer(60));

        DEFAULT_ORDER.put(ContentType.Sku, new Integer(50));

        DEFAULT_ORDER.put(ContentType.Recipe, new Integer(40));

        DEFAULT_ORDER.put(ContentType.Recommender, new Integer(31));
        DEFAULT_ORDER.put(ContentType.RecommenderStrategy, new Integer(30));

        DEFAULT_ORDER.put(ContentType.MediaFolder, new Integer(11));
        DEFAULT_ORDER.put(ContentType.Image, new Integer(10));
        DEFAULT_ORDER.put(ContentType.Html, new Integer(10));
    }

    private final Map<ContentKey, String> labels;

    public ContentKeyDisplayOrderComparator(Map<ContentKey, String> labels) {
        this.labels = labels;
    }

    @Override
    public int compare(ContentKey key1, ContentKey key2) {
        int node1Priority = DEFAULT_ORDER.get(key1.getType()) == null ? 0 : (DEFAULT_ORDER.get(key1.getType())).intValue();
        int node2Priority = DEFAULT_ORDER.get(key2.getType()) == null ? 0 : (DEFAULT_ORDER.get(key2.getType())).intValue();

        int i = node2Priority - node1Priority;

        // if both are 0
        if (i == 0 && node1Priority == 0) {
            i = key1.getType().compareTo(key2.getType());
        }

        if (i == 0) {
            i = labels.get(key1).toLowerCase().compareTo(labels.get(key2).toLowerCase());
        }

        if (i == 0) {
            i = key1.getId().toLowerCase().compareTo(key2.getId().toLowerCase());
        }

        return i;

    }

}
