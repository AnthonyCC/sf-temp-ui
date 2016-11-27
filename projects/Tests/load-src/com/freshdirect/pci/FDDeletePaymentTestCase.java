package com.freshdirect.pci;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.TestUtil;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpPaymentMethodPersistentBean;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * The <code>ExampleTestCase</code> is an example
 * stateless <code>TestCase</code>.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FDDeletePaymentTestCase extends DbTestCaseSupport {
	private static Map paymentMethods = new HashMap();
	private static Object syncOnMe = new Object();
	private static List identities = new ArrayList();

    public FDDeletePaymentTestCase(String name) throws Exception{
        super(name);
        
		super.setUp();
		synchronized (syncOnMe) {
			if (identities.size() == 0) {
				synchronized (identities) {
					if (identities.size() == 0) {
						identities.addAll(LoadUtil.getRandomCustomerList());
					}
				}
			}
			if (paymentMethods.size() == 0) {
				synchronized (paymentMethods) {
					if (paymentMethods.size() == 0) {
						//These customers are specific to DB. Please change these values appropriately before you run the testcase.						
						paymentMethods.put("1909813891",LoadUtil.findByParent("1909813891"));
						paymentMethods.put("1909813904",LoadUtil.findByParent("1909813904"));
						paymentMethods.put("1909813938",LoadUtil.findByParent("1909813938"));
						paymentMethods.put("1909814031",LoadUtil.findByParent("1909814031"));
						paymentMethods.put("1909814036",LoadUtil.findByParent("1909814036"));
						
					}
				}
			}
			
		}
    }

	protected void setUp() throws Exception {
	}

	protected void tearDown() {
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
	private ErpPaymentMethodI getNewCCMethod(String accountNumber) throws Exception {
        String month = "09";
        String year = "2010";
        String cardType = "MC";
        
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
        
        paymentMethod.setAddress1("1320 Azalea Drive");
        paymentMethod.setAddress2("");
        paymentMethod.setApartment("");
        paymentMethod.setCity("North Burnswick");
        paymentMethod.setState("NJ");
        paymentMethod.setZipCode("07088");
        paymentMethod.setCountry("US");		
        return paymentMethod;
        
	}
	
//	private ErpPaymentMethodI getNewECheckMethod(String paymentId) throws Exception {
//		String pmType = "EC";
//        String accountNumber = "1234566790";
//        String bankAccountType = "C";
//        String abaRouteNumber = "091000022";
//        String bankName = "US Bank";
//        
//        ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(identity, paymentId);
//        assertNotNull("Payment Method Not found. "+paymentId,paymentMethod);
//		
//        paymentMethod.setName("Sairam Krishnasamy");
//        paymentMethod.setAccountNumber(accountNumber);
//        
//        //Only needed for ECHeck
//        paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
//        paymentMethod.setAbaRouteNumber(abaRouteNumber);
//        paymentMethod.setBankName(bankName);
//        paymentMethod.setAddress1("65 Chapin Road");
//        paymentMethod.setAddress2("");
//        paymentMethod.setApartment("27");
//        paymentMethod.setCity("Pine Brook");
//        paymentMethod.setState("NJ");
//        paymentMethod.setZipCode("07058");
//        paymentMethod.setCountry("US");		
//        return paymentMethod;
//	}
	
	public void testDeletePaymentMethod() throws Exception {
		FDIdentity identity = (FDIdentity) identities.get((int)(Math.random() * identities.size()));
		List paymentMList = (List)paymentMethods.get(identity.getErpCustomerPK());
		ErpPaymentMethodPersistentBean bean = (ErpPaymentMethodPersistentBean) paymentMList.get((int)(Math.random() * paymentMList.size()));
		ErpPaymentMethodPersistentBean localBean = new ErpPaymentMethodPersistentBean();
		localBean.setPK(bean.getPK());
		remove(conn, localBean);
		assertNull(localBean.getPK());
	}
	public void remove(Connection conn, ErpPaymentMethodPersistentBean bean) throws SQLException {
		
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.PAYMENTMETHOD WHERE ID=?");
		try {
			ps.setString(1, bean.getPK().getId());
			ps.executeUpdate();
		} finally {
			if (ps != null) ps.close();
			ps = null;
		}
		
		bean.setPK(null); // make it anonymous
	}
	public static Test suite() {
		return new TestSuite(FDDeletePaymentTestCase.class);
	}

	public static void main(String args[]) throws IOException{
		junit.textui.TestRunner.run(suite());
	}
}
