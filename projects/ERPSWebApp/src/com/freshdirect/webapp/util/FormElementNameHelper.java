/*
 * $Workfile: FormElementNameHelper.java$
 *
 * $Date: 8/29/2001 11:52:25 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.util;

import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.model.*;

/**
 *
 * @version $Revision: 1$
 * @author $Author: Mike Rose$
 */
public class FormElementNameHelper {

    public static String getFormElementName(ErpModelSupport model, String attributeName) {
        String prefix = "";
        String id = "";
        if (model instanceof ErpCharacteristicModel) {
            ErpCharacteristicModel e = (ErpCharacteristicModel) model;
            prefix = "CHR";
            id = e.getName();
        } else if (model instanceof ErpCharacteristicValueModel) {
            ErpCharacteristicValueModel e = (ErpCharacteristicValueModel) model;
            prefix = "CHV";
            id = e.getName();
        } else if (model instanceof ErpClassModel) {
            ErpClassModel e = (ErpClassModel) model;
            prefix = "CLS";
            id = e.getSapId();
        } else if (model instanceof ErpMaterialModel) {
            ErpMaterialModel e = (ErpMaterialModel) model;
            prefix = "MTL";
            id = e.getSapId();
        } else if (model instanceof ErpMaterialPriceModel) {
            ErpMaterialPriceModel e = (ErpMaterialPriceModel) model;
            prefix = "MPR";
            id = e.getSapId();
        } else if (model instanceof ErpSalesUnitModel) {
            ErpSalesUnitModel e = (ErpSalesUnitModel) model;
            prefix = "SLU";
            id = e.getAlternativeUnit();
        }
        return prefix + ":" + id + ":" + attributeName;
    }

}
