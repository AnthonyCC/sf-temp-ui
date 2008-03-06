package com.freshdirect.cms;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDException;

/**
 * Unmutable, typed key for a {@link com.freshdirect.cms.ContentNodeI} instance.
 * Keys can be represented as strings in the format <code>ContentType:id</code>.
 */
public class ContentKey implements Serializable {

	private static final long serialVersionUID = -5367719524667192683L;

	private final static char SEPARATOR = ':';

	private final ContentType type;
	private final String id;

	public final static Pattern NAME_PATTERN = Pattern.compile("([a-zA-Z]|\\d|_|-)+");

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


	/**
	 * Factory method to valid create key instance. This is useful when ID's come from
	 * user input (Excel sheet, plain text, etc).
	 * 
	 * @param type Content type
	 * @param id Content ID / Key
	 * @return ContentKey instance
	 * @throws InvalidContentKeyException if key format is invalid (ie. contains white space)
	 */
	public static ContentKey create(ContentType type, String id) throws InvalidContentKeyException {
		ContentKey key = new ContentKey(type, id);

		// validate key.id
		Matcher matcher = ContentKey.NAME_PATTERN.matcher(id);
		if(!matcher.matches()) {
			/*
			LOGGER.info("requested content id \'"+id+"\' does not match pattern "+ContentKey.NAME_PATTERN.pattern());
			delegate.record(" \""+ id + "\" must contain only letters, numbers, underscore and '-'", null);
			return;*/
			throw new InvalidContentKeyException();
		}

		return key;
	}
	

	public static class InvalidContentKeyException extends FDException {
	}

}