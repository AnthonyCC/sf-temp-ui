package com.freshdirect.framework.event;

import java.io.Serializable;

/**
 * @author knadeem Date May 3, 2005
 */
public interface EventSinkI extends Serializable {
	
	public boolean log(FDWebEvent event);

}
