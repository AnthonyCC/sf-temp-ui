package com.freshdirect.cms.search;

/**
 * Builder class for AttributeIndex, to make life easier
 * 
 * @author tsoltesz
 *
 */
public class AttributeIndexBuilder {
	private AttributeIndex attributeIndex;

	public AttributeIndexBuilder(String contentType, String attributeName) {
		attributeIndex = new AttributeIndex(contentType, attributeName);
	}

	public AttributeIndexBuilder withText(boolean text) {
		attributeIndex.setText(text);
		return this;
	}

	public AttributeIndexBuilder withSpelled(boolean spelled) {
		attributeIndex.setSpelled(spelled);
		return this;
	}

	public AttributeIndexBuilder withRecurseParent(boolean recurseParent) {
		attributeIndex.setRecurseParent(recurseParent);
		return this;
	}

	public AttributeIndexBuilder withRelationshipAttributeName(String relationshipAttributeName) {
		attributeIndex.setRelationshipAttributeName(relationshipAttributeName);
		return this;
	}

	public AttributeIndex build() {
		return attributeIndex;
	}
}