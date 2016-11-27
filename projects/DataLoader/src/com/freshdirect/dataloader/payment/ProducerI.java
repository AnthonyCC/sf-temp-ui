/*
 * ProducerI.java
 *
 * Created on December 26, 2001, 8:36 PM
 */

package com.freshdirect.dataloader.payment;

/**
 *
 * @author  knadeem
 * @version 
 */
public interface ProducerI {
	
	public void setConsumer(ConsumerI consumer);
	
	public ConsumerI getConsumer();

}

