package com.freshdirect.cms.index.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.AttributeIndexBuilder;
import com.freshdirect.cms.search.ContentIndex;

/**
 * 
 * Class to hold the index creation rules. These rules decide which node attributes should be indexed and how.
 * 
 * ! IMPORTANT ! The IndexerService builds upon the fact that these rules are not changed runtime! Keep in mind!
 *
 */
public class IndexConfiguration {

    private static final IndexConfiguration INSTANCE = new IndexConfiguration();

    public static IndexConfiguration getInstance() {
        return INSTANCE;
    }

    private static final List<ContentIndex> CONFIGURATIONS = new ArrayList<ContentIndex>();
    static {
        CONFIGURATIONS.add(new AttributeIndexBuilder("Category", "FULL_NAME").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Category", "KEYWORDS").withSpelled(true).withRecurseParent(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Product", "FULL_NAME").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Product", "AKA").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Product", "KEYWORDS").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Product", "brands").withRelationshipAttributeName("FULL_NAME").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Product", "PRIMARY_HOME").withRelationshipAttributeName("KEYWORDS").withRecurseParent(true).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Brand", "FULL_NAME").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Recipe", "name").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Recipe", "keywords").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("YmalSet", "title").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ConfiguredProductGroup", "name").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("FavoriteList", "full_name").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Recommender", "FULL_NAME").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("RecommenderStategy", "FULL_NAME").build());

        CONFIGURATIONS.add(new ContentIndex("Sku"));
        CONFIGURATIONS.add(new ContentIndex("ConfiguredProduct"));

        CONFIGURATIONS.add(new AttributeIndexBuilder("FAQ", "QUESTION").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("FAQ", "ANSWER").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("FAQ", "FULL_NAME").build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("FAQ", "KEYWORDS").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("FAQ", "PRIORITY_LIST").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ImageBanner", "Name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ImageBanner", "Description").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Section", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("WebPage", "PAGE_TITLE").withText(true).build());

        CONFIGURATIONS.add(new ContentIndex("Banner"));

        CONFIGURATIONS.add(new AttributeIndexBuilder("DarkStore", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Domain", "NAME").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Domain", "Label").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ProductFilter", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ProductFilterGroup", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ProductFilterMultiGroup", "level1Name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ProductFilterMultiGroup", "level2Name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Tag", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Section", "headlineText").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("DomainValue", "Label").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("DomainValue", "VALUE").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("Module", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ModuleGroup", "name").withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder("ModuleContainer", "name").withText(true).build());
    }

    public List<ContentIndex> getIndexConfiguration() {
        return Collections.unmodifiableList(CONFIGURATIONS);
    }

    public Map<ContentType, List<AttributeIndex>> getAllIndexConfigurationsByContentType() {
        Map<ContentType, List<AttributeIndex>> attributeIndexByContentType = new HashMap<ContentType, List<AttributeIndex>>();
        for (ContentIndex index : CONFIGURATIONS) {
            ContentType contentType = ContentType.get(index.getContentType());
            List<AttributeIndex> indexes = attributeIndexByContentType.get(contentType);
            if (indexes == null) {
                indexes = new ArrayList<AttributeIndex>();
                attributeIndexByContentType.put(contentType, indexes);
            }
            if (index instanceof AttributeIndex) {
                indexes.add((AttributeIndex) index);
            }
        }
        return Collections.unmodifiableMap(attributeIndexByContentType);
    }
}
