/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

/**
 * ErpModifyOrderModel interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpModifyOrderModel extends ErpAbstractOrderModel {

    public ErpModifyOrderModel() {
        super(EnumTransactionType.MODIFY_ORDER);
    }

}

