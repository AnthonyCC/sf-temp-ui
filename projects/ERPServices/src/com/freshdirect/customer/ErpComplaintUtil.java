/*
 * Created on Apr 2, 2003
 *
 */
package com.freshdirect.customer;

/**
 * @author knadeem
 */
import java.util.Iterator;
import java.util.List;

public class ErpComplaintUtil {

	public static boolean isComplaintReversable(ErpComplaintModel complaint, List customerCredits){
		int complaintMethod = complaint.getComplaintMethod();
		if(ErpComplaintModel.CASH_BACK == complaintMethod || ErpComplaintModel.MIXED == complaintMethod){
			return false;
		}
		boolean reversable = true;
		for(Iterator i = customerCredits.iterator(); i.hasNext(); ){
			ErpCustomerCreditModel customerCredit = (ErpCustomerCreditModel)i.next();
			if(customerCredit.getRemainingAmount() != customerCredit.getOriginalAmount()){
				reversable = false;
				break;
			}
		}
		return reversable;
	}

}
