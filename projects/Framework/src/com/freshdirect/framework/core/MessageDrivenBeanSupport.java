/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.core;

import javax.jms.MessageListener;
import javax.ejb.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class MessageDrivenBeanSupport implements MessageDrivenBean, MessageListener {
	
	private MessageDrivenContext messageDrivenCtx;

	public void ejbRemove() {
	}

	public void ejbCreate() {
	}

	public void setMessageDrivenContext(MessageDrivenContext ctx) {
		this.messageDrivenCtx = ctx;
	}
	
	public MessageDrivenContext getMessageDrivenContext() {
		return this.messageDrivenCtx;	
	}

}