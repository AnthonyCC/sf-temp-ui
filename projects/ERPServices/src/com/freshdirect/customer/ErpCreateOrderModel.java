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
 * ErpCreateOrder interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCreateOrderModel extends ErpAbstractOrderModel {

    public ErpCreateOrderModel() {
        super(EnumTransactionType.CREATE_ORDER);
    }

}

