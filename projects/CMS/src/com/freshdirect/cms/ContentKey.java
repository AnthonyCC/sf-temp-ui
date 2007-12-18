package com.freshdirect.cms;

import java.io.Serializable;

import com.freshdirect.cms.application.CmsManager;

/**
 * Unmutable, typed key for a {@link com.freshdirect.cms.ContentNodeI} instance.
 * Keys can be represented as strings in the format <code>ContentType:id</code>.
 */
public class ContentKey implements Serializable {

	private static final long serialVersionUID = -5367719524667192683L;

	private final static char SEPARATOR = ':';

	private final ContentType type;
	private final String id;

	/**
	 * @param type content type (non-null)
	 * @param id identifier (non-null)
	 * 
	 * @throws IllegalArgumentException for invalid key parameters
	 */
	public ContentKey(ContentType type, String id) {
		if (type == null) {
			throw new IllegalArgumentException("ContentType cannot be null");
		}
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		this.type = type;
		this.id = id.intern();
	}

	public String getId() {
		return id;
	}

	public ContentType getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ContentKey) {
			ContentKey d = (ContentKey) o;
			return type.equals(d.type) && id.equals(d.id);
		}
		return false;
	}

	/**
	 * @see #getContentNode()
	 */
	public ContentNodeI lookupContentNode() {
		return getContentNode();
	}

	/**
	 * Convenience method to look up the corresponding {@link ContentNodeI}
	 * via the global {@link CmsManager}.
	 * 
	 * @return the content node or null if not found
	 */
	public ContentNodeI getContentNode() {
		return CmsManager.getInstance().getContentNode(this);
	}

	public int hashCode() {
		return type.hashCode() ^ id.hashCode();
	}

	public String toString() {
		return "ContentKey[" + getEncoded() + "]";
	}

	/**
	 * Get the key encoded as a String in the format <code>ContentType:id</code>.
	 * 
	 * @return encoded version of key
	 */
	public String getEncoded() {
		return type.getName() + SEPARATOR + id;
	}

	/**
	 * Factory method to decode the String representation of the content-key.
	 * 
	 * @param key String representation of key
	 * @return the content key
	 * 
	 * @throws IllegalArgumentException for malformed keys
	 */
	public static ContentKey decode(String key) {
		int p = key.indexOf(SEPARATOR);
		if (p < 0) {
			throw new IllegalArgumentException("Invalid key " + key);
		}
		String type = key.substring(0, p);
		String id = key.substring(p + 1);
		return new ContentKey(ContentType.get(type), id);
	}

}