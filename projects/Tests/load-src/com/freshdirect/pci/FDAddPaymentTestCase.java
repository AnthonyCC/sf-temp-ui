package com.freshdirect.pci;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ejb.ErpPaymentMethodPersistentBean;
import com.freshdirect.enum.EnumManager;
import com.freshdirect.enum.TestableEnumManager;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;

/**
 * The <code>ExampleTestCase</code> is an example
 * stateless <code>TestCase</code>.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FDAddPaymentTestCase extends DbTestCaseSupport {

	/*private Map info2 = new HashMap();
	private Map info3 = new HashMap();
	private Map info4 = new HashMap();
	private Map info5 = new HashMap();*/
	private static List identities = new ArrayList();
	private static ErpPaymentMethodI paymentMethod = null;
	private static Object syncOnMe = new Object();
	
    public FDAddPaymentTestCase(String name) throws Exception{
        super(name);

		paymentMethod = getCCMethod();
		super.setUp();
		synchronized (syncOnMe) {
			if (identities.size() == 0) {
				synchronized (identities) {
					if (identities.size() == 0) {
						identities.addAll(LoadUtil.getRandomCustomerList());
					}
				}
			}
		}
    }

	protected void setUp() throws Exception {

	}

	protected void tearDown() throws Exception{
	}

	protected String getSchema() {
		return "CUST";
	}
	
	protected String[] getAffectedTables() {
		return new String[]{};
	}
	
	protected IDataSet getActualDataSet() throws DatabaseUnitException {
		return null;
	}
	
	private ErpPaymentMethodI getCCMethod() throws Exception {
        String month = "08";
        String year = "2008";
        String cardType = "VISA";
		String accountNumber = "5555515555555551";
		
        ErpCreditCardModel paymentMethod = new ErpCreditCardModel();
        
        SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
        Date date = sf.parse(month.trim()+year.trim(), new ParsePosition(0));
        Calendar expCal = new GregorianCalendar();
        expCal.setTime(date);
        expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
		paymentMethod.setExpirationDate(expCal.getTime());
		
        paymentMethod.setName("Sairam Krishnasamy");
        paymentMethod.setAccountNumber(accountNumber);
        paymentMethod.setCardType(EnumCardType.getCardType(cardType));
        
        paymentMethod.setAddress1("65 Chapin Road");
        paymentMethod.setAddress2("");
        paymentMethod.setApartment("25");
        paymentMethod.setCity("Pinebrook");
        paymentMethod.setState("NJ");
        paymentMethod.setZipCode("07058");
        paymentMethod.setCountry("US");		
        return paymentMethod;
        
	}
	
	private ErpPaymentMethodI getECheckMethod() throws Exception {
		String pmType = "EC";
        String accountNumber = "1234566788";
        String bankAccountType = "C";
        String abaRouteNumber = "091000022";
        String bankName = "US Bank";
        
    	ErpPaymentMethodI paymentMethod = null;
		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(pmType);
		paymentMethod = PaymentManager.createInstance(paymentMethodType);
		
        paymentMethod.setName("Sairam Krishnasamy");
        paymentMethod.setAccountNumber(accountNumber);
        
        //Only needed for ECHeck
        paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
        paymentMethod.setAbaRouteNumber(abaRouteNumber);
        paymentMethod.setBankName(bankName);
        paymentMethod.setAddress1("65 Chapin Road");
        paymentMethod.setAddress2("");
        paymentMethod.setApartment("25");
        paymentMethod.setCity("Pinebrook");
        paymentMethod.setState("NJ");
        paymentMethod.setZipCode("07058");
        paymentMethod.setCountry("US");		
        return paymentMethod;
	}
	
	public void testAddPaymentMethod() throws Exception {
			//Performing add payment for five different customers.
			//Connection conn = getDataSource().getConnection();	
    		//FDCustomerManager.addPaymentMethod(getActionInfo("Added new Payment Method", identity), paymentMethod);
			ErpPaymentMethodPersistentBean bean = new ErpPaymentMethodPersistentBean((ErpPaymentMethodModel)paymentMethod);
			FDIdentity identity = (FDIdentity) identities.get((int)(Math.random() * identities.size()));
			bean.setParentPK(new PrimaryKey(identity.getErpCustomerPK()));
    		bean.create(conn);
 	}
	public static Test suite() {
		return new TestSuite(FDAddPaymentTestCase.class);
	}

	public static void main(String args[]) throws IOException{
		junit.textui.TestRunner.run(suite());
	}
}
