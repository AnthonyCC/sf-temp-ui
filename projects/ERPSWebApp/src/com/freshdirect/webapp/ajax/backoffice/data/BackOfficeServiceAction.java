package com.freshdirect.webapp.ajax.backoffice.data;


public enum BackOfficeServiceAction {
    GET_CONTENT_TYPE(Module.BACKOFFICE_CATELOG, "getContentType"), 
    GET_SUPER_DEPT(Module.BACKOFFICE_CATELOG, "getSuperDepartment"), 
    GET_DEPT(Module.BACKOFFICE_CATELOG, "getDepartment"), 
    GET_SKU(Module.BACKOFFICE_CATELOG, "getSku"), 
    GET_SMART_STORE_INFO(Module.BACKOFFICE_CATELOG, "getSmartStoreInfo"), 
    GET_DOMAIN_VALUE_RESPONSE(Module.BACKOFFICE_CATELOG, "getDomainValueResponse"), 
    GET_PRODUCT_BY_SKU(Module.BACKOFFICE_CATELOG, "getProductBySku"), 
    GET_DOMAIN_VALUES(Module.BACKOFFICE_CATELOG, "getDomainValues"), 
    GET_SKU_BY_ID(Module.BACKOFFICE_CATELOG, "getSkuById"), 
    GET_CATEGORY(Module.BACKOFFICE_CATELOG, "getCategory"), 
    GET_PRODUCT(Module.BACKOFFICE_CATELOG, "getProduct"),
    GET_LIST_SKU(Module.BACKOFFICE_CATELOG, "getListSku"),
    GET_SKU_INFO(Module.BACKOFFICE_CATELOG, "getSkuInfo"),
    GET_RESEND_INVOICE_MAIL(Module.BACKOFFICE_CATELOG, "getResendInvoiceMail"),
    GET_RESUBMIT_ORDER(Module.BACKOFFICE_CATELOG, "getResubmitOrder"),
    GET_RESUBMIT_CUSTOMER(Module.BACKOFFICE_CATELOG, "getResubmitCustomer");
    
 

    public final Module module;
    public final String actionName;

    BackOfficeServiceAction(Module module, String actionName) {
        this.module = module;
        this.actionName = actionName;
    }

    public static BackOfficeServiceAction parse(String actionName) {
        BackOfficeServiceAction result = null;
        for (BackOfficeServiceAction requestAction : values()) {
            if (requestAction.actionName.equals(actionName)) {
                result = requestAction;
                break;
            }
        }
        return result;
    }

    public enum Module {
        BACKOFFICE_CATELOG,
        BACKOFFICE_SMARTSTORE;
    }
}
