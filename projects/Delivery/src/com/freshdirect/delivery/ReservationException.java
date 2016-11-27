/*
 * ReservationException.java
 *
 * Created on August 30, 2001, 3:26 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
public class ReservationException extends Exception {

    /**
     * Creates new <code>ReservationException</code> without detail message.
     */
    public ReservationException() {
    }


    /**
     * Constructs an <code>ReservationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ReservationException(String msg) {
        super(msg);
    }
}


