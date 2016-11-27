package com.freshdirect.fdstore.content.test;


/**
 * This exception will cause the current row to be removed from the {@link ContentBundle}.
 * 
 * It may be the case that during the constuction of the row some problem is encountered.
 * With this exception, the {@link ColumnExtractor} can signal that it cannot generate
 * a valid row.
 * @author istvan
 *
 */
public class AbortRowException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8433059669089798415L;
	
	
	public AbortRowException() {}
	
	public AbortRowException(String description) {
		super(description);
	}
	

}
