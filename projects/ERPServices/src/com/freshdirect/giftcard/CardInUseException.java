/*
 * ErpSaleNotFoundException.java
 *
 * Create Date: 5/23/02 3:49:05 PM
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.giftcard;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 *
 * @author skrishnasamy
 * @version
 */ 
public class CardInUseException extends ExceptionSupport {
	
	private String cardOwner;
    
    /**
     * Default constructor.
     */    
    public CardInUseException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public CardInUseException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public CardInUseException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public CardInUseException(Exception ex, String message) {
        super(ex, message);
    }

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

}
