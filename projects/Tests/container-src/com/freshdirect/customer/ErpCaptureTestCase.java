/*
 * ErpCaptureTestCase.java
 *
 * Created on January 11, 2002, 4:17 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;
import lcc.japi.LCC;
import lcc.japi.LCCTransaction;

import com.freshdirect.TestUtil;
import com.freshdirect.payment.PaylinxException;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.ejb.PaymentSB;

public class ErpCaptureTestCase extends TestCase {

	protected Context ctx;
	protected PaymentHome paymentHome;

	/** Creates new ErpCaptureTestCase */
	public ErpCaptureTestCase(String testName) {
		super(testName);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpCaptureTestCase("testCCDCapture"));
	}

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		paymentHome = (PaymentHome) ctx.lookup("freshdirect.payment.Payment");
	}

	/** This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}

	public void testCCDCapture()
		throws CreateException, FinderException, ErpTransactionException, PaylinxException, RemoteException {
		PaymentSB sb = this.paymentHome.create();
		sb.captureAuthorization("17823");

		// for running the capture transaction without EJB 
		//runCaptureTransaction(new LCCTransaction()); 
	}

	/**
	 * Runs a authorization and a capture transaction.Note: not to be confused with a one (auth and capture) transaction.
	 * 
	 * @param	LCCTransaction trans.  The transaction object from the calling function.
	 */
	private static void runCaptureTransaction(LCCTransaction trans) {

		// Set the necessary fields for the transaction you intend to perform
		trans.SetValue(LCC.ID_MERCHANT_ID, "demo");
		trans.SetConnectionInformation("appdevlab22", 1530, 0);
		trans.SetValue(LCC.ID_ACCOUNT_NUMBER, "4012881188888888");
		trans.SetValue(LCC.ID_CARD_TYPE, "001"); // Visa Card
		trans.SetValue(LCC.ID_EXPIRATION_DATE, "1014");
		trans.SetValue(LCC.ID_AMOUNT, "15000"); // $150.00  //Must be between 100 and 200 for a call response.
		trans.SetValue(LCC.ID_TAX_AMOUNT, "250"); // $2.50
		trans.SetValue(LCC.ID_USER_DEFINED_1, "User Defined 1");
		trans.SetValue(LCC.ID_CUSTOMER_STREET, "100 Main St.");
		trans.SetValue(LCC.ID_CUSTOMER_ZIP, "90210");

		int rCode = trans.RunTransaction(LCC.ID_AUTHORIZATION);

		System.out.println("\n\nAuthorization Returned with:" + rCode);

		// Check the return status
		// It should be 0, if the transaction was performed successfully
		// A 0 return status does NOT mean the transaction was approved
		if (rCode != 0) {
			// If the return status != 0, get the return code message
			System.out.println(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		} else {
			// If the authorization was approved...
			//String sTemp = trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE);

			if (trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE).equals("A")) {
				// Show some of the fields returned by this transaction
				System.out.println("Return Code Message = " + trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
				System.out.println("Approval Code = " + trans.GetValue(LCC.ID_APPROVAL_CODE));
				System.out.println("Authorization Response Code = " + trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
				System.out.println("Authorization Response Code Message = " + trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
				System.out.println("Sequence Number = " + trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
				System.out.println("Address Match = " + trans.GetValue(LCC.ID_ADDRESS_MATCH));
				System.out.println("Zip Match = " + trans.GetValue(LCC.ID_ZIP_MATCH));
				System.out.println("Processor Auth Response Code = " + trans.GetValue(LCC.ID_PROCESSOR_AUTH_RESPONSE_CODE));
				System.out.println("Processor AVS Result = " + trans.GetValue(LCC.ID_PROCESSOR_AVS_RESULT));

				// Show a complete list of field id/value pairs returned by the server for this transaction...
				//trans.PrintFields();
			} else if (trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE).equals("C")) //Call Response.
				{
				String sSequenceNumber = "";
				String sMerchantID = "";

				sSequenceNumber = trans.GetValue(LCC.ID_SEQUENCE_NUMBER);
				sMerchantID = trans.GetValue(LCC.ID_MERCHANT_ID);

				trans.ClearValues(); //MUST clear values between transacations.
				trans.SetValue(LCC.ID_MERCHANT_ID, "demo");
				trans.SetConnectionInformation("appdevlab22", 1530, 0);
				trans.SetValue(LCC.ID_ACCOUNT_NUMBER, "4012881188888888");
				trans.SetValue(LCC.ID_CARD_TYPE, "001"); // Visa Card
				trans.SetValue(LCC.ID_EXPIRATION_DATE, "1014");
				trans.SetValue(LCC.ID_AMOUNT, "15000"); // $150.00  //Must be between 100 and 200 for a call response.
				trans.SetValue(LCC.ID_TAX_AMOUNT, "250"); // $2.50
				trans.SetValue(LCC.ID_USER_DEFINED_1, "User Defined 1");
				trans.SetValue(LCC.ID_CUSTOMER_STREET, "100 Main St.");
				trans.SetValue(LCC.ID_CUSTOMER_ZIP, "90210");

				trans.SetValue(LCC.ID_SEQUENCE_NUMBER, sSequenceNumber);
				trans.SetValue(LCC.ID_MERCHANT_NAME, "Demonstration Store");
				//Adding merchant name to set value initiates using the configuration file.
				//trans.SetValue(LCC.ID_MERCHANT_ID, sMerchantID);
				trans.SetValue(LCC.ID_APPROVAL_CODE, "123456");
				trans.SetConnectionInformation("appdevlab22", 1530, 0);

				rCode = trans.RunTransaction(LCC.ID_CAPTURE);

				System.out.println("\n\nCapture Returned with:" + rCode);

				if (rCode != 0) {
					System.out.println(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
				} else {
					System.out.println("Return Code Message = " + trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
					System.out.println("Approval Code = " + trans.GetValue(LCC.ID_APPROVAL_CODE));
					System.out.println("Authorization Response Code = " + trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
					System.out.println("Authorization Response Code Message = " + trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
					System.out.println("Sequence Number = " + trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
					System.out.println("Address Match = " + trans.GetValue(LCC.ID_ADDRESS_MATCH));
					System.out.println("Zip Match = " + trans.GetValue(LCC.ID_ZIP_MATCH));
					System.out.println("Processor Auth Response Code = " + trans.GetValue(LCC.ID_PROCESSOR_AUTH_RESPONSE_CODE));
					System.out.println("Processor AVS Result = " + trans.GetValue(LCC.ID_PROCESSOR_AVS_RESULT));
				}

			} else {
				System.out.println("Transaction Denied - " + trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			}
		}

		//The values must be cleared after each transaction ran.
		trans.ClearValues();

	}

}
