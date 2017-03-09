package com.freshdirect.cms.search;

/**
 * Configuration object to instruct
 * {@link com.freshdirect.cms.search.LuceneSearchService} to index a named
 * attribute of nodes with a particular {@link com.freshdirect.cms.ContentType}.
 */
public class AttributeIndex extends ContentIndex {
	private static final long serialVersionUID = 3283380751457964164L;

	private String attributeName;

	private String relationshipAttributeName;

	private boolean text;

	private boolean spelled;

	private boolean recurseParent;

	public AttributeIndex(String contentType, String attributeName) {
		super(contentType);
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getRelationshipAttributeName() {
		return relationshipAttributeName;
	}

	public void setRelationshipAttributeName(String relationshipAttributeName) {
		this.relationshipAttributeName = relationshipAttributeName;
	}

	public boolean isText() {
		return text;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public boolean isSpelled() {
		return spelled;
	}

	public void setSpelled(boolean spelled) {
		this.spelled = spelled;
	}

	public boolean isRecurseParent() {
		return recurseParent;
	}

	public void setRecurseParent(boolean recurseParent) {
		this.recurseParent = recurseParent;
	}

	public String getCanonicalName() {
		StringBuilder buf = new StringBuilder();
		buf.append(getContentType());
		buf.append(".");
		buf.append(attributeName);
		if (relationshipAttributeName != null) {
			buf.append("#");
			buf.append(relationshipAttributeName);
		}
		return buf.toString();
	}

	@Override
	public String toString() {
		return "AttributeIndex [contentType=" + getContentType() + ", attributeName=" + attributeName
				+ (relationshipAttributeName != null ? ", relationshipAttributeName=" + relationshipAttributeName : "")
				+ ", text=" + text + ", spelled=" + spelled + ", recurseParent=" + recurseParent + "]";
	}

}