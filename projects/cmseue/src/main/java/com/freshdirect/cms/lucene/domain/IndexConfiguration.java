package com.freshdirect.cms.lucene.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;

/**
 *
 * Class to hold the index creation rules. These rules decide which node attributes should be indexed and how.
 *
 * ! IMPORTANT ! The IndexerService builds upon the fact that these rules are not changed runtime! Keep in mind!
 *
 */
public class IndexConfiguration {

    private static final IndexConfiguration INSTANCE = new IndexConfiguration();

    private static final List<ContentIndex> CONFIGURATIONS = new ArrayList<ContentIndex>();
    static {
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Department, ContentTypes.Department.FULL_NAME).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Category, ContentTypes.Category.FULL_NAME).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Category, ContentTypes.Category.KEYWORDS).withSpelled(true).withRecurseParent(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Product, ContentTypes.Product.FULL_NAME).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Product, ContentTypes.Product.AKA).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Product, ContentTypes.Product.KEYWORDS).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Product, ContentTypes.Product.brands).withRelationshipAttributeName("FULL_NAME").withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Product, ContentTypes.Product.PRIMARY_HOME).withRelationshipAttributeName("KEYWORDS").withRecurseParent(true)
                .withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Brand, ContentTypes.Brand.FULL_NAME).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Recipe, ContentTypes.Recipe.name).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Recipe, ContentTypes.Recipe.keywords).withSpelled(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.YmalSet, ContentTypes.YmalSet.title).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.name).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FavoriteList, ContentTypes.FavoriteList.full_name).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Recommender, ContentTypes.Recommender.FULL_NAME).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.FULL_NAME).build());

        CONFIGURATIONS.add(new ContentIndex(ContentType.Sku));
        CONFIGURATIONS.add(new ContentIndex(ContentType.ConfiguredProduct));

        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FAQ, ContentTypes.FAQ.QUESTION).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FAQ, ContentTypes.FAQ.ANSWER).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FAQ, ContentTypes.FAQ.FULL_NAME).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FAQ, ContentTypes.FAQ.KEYWORDS).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FAQ, ContentTypes.FAQ.PRIORITY_LIST).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ImageBanner, ContentTypes.ImageBanner.Name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ImageBanner, ContentTypes.ImageBanner.Description).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Section, ContentTypes.Section.name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.WebPage, ContentTypes.WebPage.PAGE_TITLE).withText(true).build());

        CONFIGURATIONS.add(new ContentIndex(ContentType.Banner));

        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.DarkStore, ContentTypes.DarkStore.name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Domain, ContentTypes.Domain.NAME).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Domain, ContentTypes.Domain.Label).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ProductFilter, ContentTypes.ProductFilter.name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level1Name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level2Name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Tag, ContentTypes.Tag.name).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Section, ContentTypes.Section.headlineText).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.DomainValue, ContentTypes.DomainValue.Label).withText(true).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.DomainValue, ContentTypes.DomainValue.VALUE).withText(true).build());

        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.Module, ContentTypes.Module.name).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ModuleContainer, ContentTypes.ModuleContainer.name).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.ModuleGroup, ContentTypes.ModuleGroup.name).build());
        CONFIGURATIONS.add(new AttributeIndexBuilder(ContentType.FDFolder, ContentTypes.FDFolder.name).build());
    }

    public static IndexConfiguration getInstance() {
        return INSTANCE;
    }

    public List<ContentIndex> getIndexConfiguration() {
        return Collections.unmodifiableList(CONFIGURATIONS);
    }

    public Map<ContentType, List<AttributeIndex>> getAllIndexConfigurationsByContentType() {
        Map<ContentType, List<AttributeIndex>> attributeIndexByContentType = new HashMap<ContentType, List<AttributeIndex>>();
        for (ContentIndex index : CONFIGURATIONS) {
            ContentType contentType = index.getContentType();
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
