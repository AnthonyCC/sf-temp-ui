package com.freshdirect.webapp.ajax.filtering;

/** 
 * Used when filtering arguments are invalid and no fallback is available
 */
public class InvalidFilteringArgumentException extends Exception {

	public enum Type {
        CANNOT_DISPLAY_NODE, NODE_IS_RECIPE_DEPARTMENT, SPECIAL_LAYOUT, NODE_HAS_REDIRECT_URL, TERMINATE, SUPER_DEPARTMENT_WITHOUT_GLOBALNAV
	}
	
	private static final long serialVersionUID = -7020867935974185895L;

	private Type type;
	private String redirectUrl;

	public InvalidFilteringArgumentException(String message, Type type) {
		super(message);
		this.type=type;
	}
	
	public InvalidFilteringArgumentException(Throwable cause, Type type) {
		super(cause);
		this.type=type;
	}
	
	public InvalidFilteringArgumentException(String message, Type type, String url) {
		super(message);
		this.type=type;
		this.redirectUrl=url;
	}
	
	public InvalidFilteringArgumentException(Throwable cause, Type type, String url) {
		super(cause);
		this.type=type;
		this.redirectUrl=url;
	}

	public InvalidFilteringArgumentException(String message, Throwable cause, Type type, String url) {
		super(message, cause);
		this.type=type;
		this.redirectUrl=url;
	}

	public Type getType() {
		return type;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}
}
