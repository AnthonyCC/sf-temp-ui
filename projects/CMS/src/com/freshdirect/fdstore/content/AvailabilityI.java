/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.content;

import java.util.Date;

/** 
 *
 * @version     $Revision$
 * @author      $Author$
 */ 
public interface AvailabilityI {

    /**
     * @return  */    
    public boolean isDiscontinued();

	public boolean isTempUnavailable();

    /**
     * @return  */    
    public boolean isOutOfSeason();

    /**
     * @return  */    
    public boolean isUnavailable();

    /**
     *  Tell if the product is available within the specified number
     *  of days.
     *  
     * @param days the number of days to look at, '1' means today,
     *        '2' means by tomorrow, etc.
     * @return if the product is available within the specified number
     *         of days.
     */    
    public boolean isAvailableWithin(int days);

    /**
     * @return  */    
    public Date getEarliestAvailability();
    
}
