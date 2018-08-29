/* Generated by Together */

package com.freshdirect.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.framework.core.*;

/**
 * ErpAppliedCredit model class.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpAppliedCreditModel extends ErpCreditModel {

    private PrimaryKey customerCreditPk;
    private String sapNumber;
    
    public ErpAppliedCreditModel () {
    	super();
    }
    
    //Introduced For Storefront 2.0 Implementation
    public ErpAppliedCreditModel (@JsonProperty("affiliate") ErpAffiliate affiliate) {
    	super(affiliate);
    }
    
    public ErpAppliedCreditModel (ErpAppliedCreditModel credit) {
    	super(credit);
    	this.customerCreditPk = credit.getCustomerCreditPk();
    	this.sapNumber = credit.getSapNumber();
    }

    public PrimaryKey getCustomerCreditPk() {
        return this.customerCreditPk;
    }

    public void setCustomerCreditPk(PrimaryKey customerCreditPk) {
        this.customerCreditPk = customerCreditPk;
    }
    
    public String getSapNumber(){
    	return this.sapNumber;
    }
    
    public void setSapNumber(String sapNumber){
    	this.sapNumber = sapNumber;
    }

}
