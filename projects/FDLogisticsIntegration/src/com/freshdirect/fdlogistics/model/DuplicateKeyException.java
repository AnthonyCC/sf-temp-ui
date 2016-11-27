package com.freshdirect.fdlogistics.model;

/**
 *
 * @author  tbalumuri
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;
public class DuplicateKeyException extends ExceptionSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = -362461153806632524L;


	/**
     * Creates new <code>FDInvalidAddressException</code> without detail message.
     */
    public DuplicateKeyException() {
    }


    /**
     * Constructs an <code>FDInvalidAddressException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DuplicateKeyException(String msg) {
        super(msg);
    }
}


