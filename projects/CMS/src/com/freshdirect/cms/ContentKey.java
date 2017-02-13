package com.freshdirect.cms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.freshdirect.fdstore.FDException;

/**
 * Immutable, typed key for a {@link com.freshdirect.cms.ContentNodeI} instance.
 * Keys can be represented as strings in the format <code>ContentType:id</code>.
 */
public class ContentKey implements Serializable {

	private static final long serialVersionUID = -5367719524667192683L;

    private static final char SEPARATOR = ':';
    private static final Pattern NAME_PATTERN = Pattern.compile("([a-zA-Z]|\\d|_|-)+");
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Map<String, ContentKey> CONTENT_KEY_CACHES = new HashMap<String, ContentKey>();

    private final ContentType type;
	private final String id;

	/**
	 * @param type content type (non-null)
	 * @param id identifier (non-null)
	 * 
	 * @throws IllegalArgumentException for invalid key parameters
	 */
	private ContentKey(ContentType type, String id) {
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

	@Override
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


	@Override
    public int hashCode() {
		return type.hashCode() ^ id.hashCode();
	}

	@Override
    public String toString() {
		return "ContentKey[" + getEncoded() + "]";
	}

	/**
	 * Get the key encoded as a String in the format <code>ContentType:id</code>.
	 * 
	 * @return encoded version of key
	 */
	public String getEncoded() {
		return encodeKey(type, id);
	}

	/**
	 * Factory method to decode the String representation of the content-key.
	 * 
	 * @param key String representation of key
	 * @return the content key
	 * 
	 * @throws IllegalArgumentException for malformed keys
	 */
	private static ContentKey decode(String key) {
		if ( key == null ) {
			throw new IllegalArgumentException("Invalid null key ");
		}
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
	    validateKey(id);
	    return getContentKey(type, id);
	}

    public static ContentKey getContentKey(ContentType type, String id) {
        return getContentKey(encodeKey(type, id));
    }

    private static String encodeKey(ContentType type, String id) {
        return type.getName() + SEPARATOR + id;
    }

    public static ContentKey getContentKey(String key) {
        ContentKey contentKey = CONTENT_KEY_CACHES.get(key);
        if (contentKey == null) {
            contentKey = ContentKey.decode(key);
            try {
                LOCK.lock();
                CONTENT_KEY_CACHES.put(key, contentKey);
            } finally {
                LOCK.unlock();
            }
        }
        return contentKey;
    }

    private static void validateKey(String id) throws InvalidContentKeyException {
        // validate key.id
        Matcher matcher = ContentKey.NAME_PATTERN.matcher(id);
        if(!matcher.matches()) {
            /*
            LOGGER.info("requested content id \'"+id+"\' does not match pattern "+ContentKey.NAME_PATTERN.pattern());
            delegate.record(" \""+ id + "\" must contain only letters, numbers, underscore and '-'", null);
            return;*/
            throw new InvalidContentKeyException();
        }
    }

	/**
	 * Null safe equality.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(ContentKey a, ContentKey b) {
	    return (a==null && b==null) || (a != null && a.equals(b)); 
	}

	public static class InvalidContentKeyException extends FDException {
		private static final long serialVersionUID = -4393163976420522249L;
	}
	public final static ContentKey NULL_KEY = ContentKey.getContentKey(ContentType.NULL_TYPE, "null");
	

}