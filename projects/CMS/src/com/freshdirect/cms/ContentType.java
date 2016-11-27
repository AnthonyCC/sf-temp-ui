package com.freshdirect.cms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Unmutable content type.  Implements flyweight pattern for efficient
 * resource usage. Use {@link #get(String)} to obtain an instance.
 */
public class ContentType implements Serializable {

	private static final long	serialVersionUID	= 5821270918113658776L;

	/** Flyweight instance pool */
	private final static Map<String, ContentType> INSTANCES = new HashMap<String, ContentType>();

	private final String name;

	protected ContentType(String name) {
		if (name == null) {
			throw new IllegalArgumentException("ContentType name cannot be null");
		}
		this.name = name;
	}

	/**
	 * Factory method to get an instance.
	 * 
	 * @param name contentype name, never null
	 * @return ContentType instance
	 */
	public static ContentType get(String name) {
		ContentType t = INSTANCES.get(name);
		if (t == null) {
			t = new ContentType(name);
			INSTANCES.put(name, t);
		}
		return t;
	}

	/**
	 * @return content type name, never null
	 */
	public String getName() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof ContentType) {
			return this.name.equals(((ContentType) o).name);
		}
		return false;
	}

	public int hashCode() {
		return this.name.hashCode();
	}

	public String toString() {
		return this.name;
	}
	
	public final static ContentType NULL_TYPE = get("Null");

}