package com.freshdirect.cms.index.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.search.AttributeIndexBuilder;
import com.freshdirect.cms.search.ContentIndex;

public class IndexConfigurationBuilder {

	private static final List<ContentIndex> configuration = new ArrayList<ContentIndex>();
	static {
		configuration.add(new AttributeIndexBuilder("Category", "FULL_NAME").withSpelled(true).build());
		configuration.add(
				new AttributeIndexBuilder("Category", "KEYWORDS").withSpelled(true).withRecurseParent(true).build());
		configuration.add(new AttributeIndexBuilder("Product", "FULL_NAME").withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Product", "AKA").withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Product", "KEYWORDS").withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Product", "brands").withRelationshipAttributeName("FULL_NAME")
				.withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Product", "PRIMARY_HOME").withRelationshipAttributeName("KEYWORDS")
				.withRecurseParent(true).withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Brand", "FULL_NAME").build());
		configuration.add(new AttributeIndexBuilder("Recipe", "name").withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("Recipe", "keywords").withSpelled(true).build());
		configuration.add(new AttributeIndexBuilder("YmalSet", "title").build());
		configuration.add(new AttributeIndexBuilder("ConfiguredProductGroup", "name").build());
		configuration.add(new AttributeIndexBuilder("FavoriteList", "full_name").build());
		configuration.add(new AttributeIndexBuilder("Recommender", "FULL_NAME").build());
		configuration.add(new AttributeIndexBuilder("RecommenderStategy", "FULL_NAME").build());

		configuration.add(new ContentIndex("Sku"));
		configuration.add(new ContentIndex("ConfiguredProduct"));

		configuration.add(new AttributeIndexBuilder("FAQ", "QUESTION").withText(true).build());
		configuration.add(new AttributeIndexBuilder("FAQ", "ANSWER").withText(true).build());
		configuration.add(new AttributeIndexBuilder("FAQ", "FULL_NAME").build());
		configuration.add(new AttributeIndexBuilder("FAQ", "KEYWORDS").withText(true).build());
		configuration.add(new AttributeIndexBuilder("FAQ", "PRIORITY_LIST").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ImageBanner", "Name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ImageBanner", "Description").withText(true).build());
		configuration.add(new AttributeIndexBuilder("Section", "name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("WebPage", "PAGE_TITLE").withText(true).build());

		configuration.add(new ContentIndex("Banner"));

		configuration.add(new AttributeIndexBuilder("DarkStore", "name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("Domain", "NAME").withText(true).build());
		configuration.add(new AttributeIndexBuilder("Domain", "Label").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ProductFilter", "name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ProductFilterGroup", "name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ProductFilterMultiGroup", "level1Name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("ProductFilterMultiGroup", "level2Name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("Tag", "name").withText(true).build());
		configuration.add(new AttributeIndexBuilder("Section", "headlineText").withText(true).build());
		configuration.add(new AttributeIndexBuilder("DomainValue", "Label").withText(true).build());
		configuration.add(new AttributeIndexBuilder("DomainValue", "VALUE").withText(true).build());
	}

	public static List<ContentIndex> getIndexConfiguration() {
		return Collections.unmodifiableList(configuration);
	}

}
