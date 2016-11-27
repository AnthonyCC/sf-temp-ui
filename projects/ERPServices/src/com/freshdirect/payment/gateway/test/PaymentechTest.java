package com.freshdirect.payment.gateway.test;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.impl.GatewayFactory;

public class PaymentechTest {
	

public static void _main(String[]a) {
	Gateway g=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
	//Response response=g.addProfile(PaymentechTestData.getAddProfileRequest(CreditCardType.VISA));
	//Response response=g.getProfile(PaymentechTestData.getGetProfileRequest("33794029"));
	//Response response=g.deleteProfile(PaymentechTestData.getDeleteProfileRequest("33789741"));
	//Response response=g.updateProfile(PaymentechTestData.getUpdateProfileRequest(CreditCardType.VISA,"33794029"));
	//System.out.println("/***************** Request ****************/");
	//System.out.println(response.getRawRequest());
	//Response response=g.verify(PaymentechTestData.getCCVerifyRequest(CreditCardType.VISA));
	//System.out.println("/***************** Response ****************/");
	/*System.out.println(response.getRawRequest());
	System.out.println(response.getRawResponse());
	System.out.println(response);*/
}
public static void main(String[] a) throws NamingException, RemoteException, CreateException {
	Context ctx=getInitialContext();
	//sb.verify(GatewayType.PAYMENTECH, PaymentechTestData.getCCVerifyRequest(CreditCardType.VISA));
	//sb.addProfile(GatewayType.PAYMENTECH, PaymentechTestData.getAddProfileRequest(CreditCardType.VISA));
}
static public Context getInitialContext() throws NamingException {
	Hashtable<String, String> h = new Hashtable<String, String>();
	h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	h.put(Context.PROVIDER_URL, "t3://127.0.0.1:7001");
	return new InitialContext(h);
}
}
