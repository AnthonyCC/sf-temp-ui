package com.freshdirect.dataloader.payment.info;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.BINCache;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.BillingInfoFactory;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.PaymentMethodFactory;
import com.freshdirect.payment.gateway.impl.RequestFactory;

public class DebitCardRecognitionCron {

	// select count(*) from cust.PAYMENTMETHOD pm where pm.CUSTOMER_ID in
	// (select unique s.CUSTOMER_ID from cust.SALE s where
	// s.CROMOD_DATE>sysdate-730)

	private static PaymentMethod getAccountInfo(String profileId) {
		PaymentMethod accInfo = null;
		Request _request = RequestFactory
				.getRequest(TransactionType.GET_PROFILE);
		CreditCard cc = PaymentMethodFactory.getCreditCard();
		cc.setBillingProfileID(profileId);
		BillingInfo billinginfo = BillingInfoFactory.getBillingInfo(
				Merchant.FRESHDIRECT, cc);
		_request.setBillingInfo(billinginfo);
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		try {
			Response _response = gateway.getProfile(_request);
			accInfo = _response.getBillingInfo() != null ? _response
					.getBillingInfo().getPaymentMethod() : null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return accInfo;
	}

	public static void main(String[] s) {
		/*
		 * String paymentMethods =
		 * "select pm.ID, pm.PROFILE_ID from cust.PAYMENTMETHOD pm where pm.CUSTOMER_ID in "
		 * +
		 * "(select unique s.CUSTOMER_ID from cust.SALE s where s.CROMOD_DATE>sysdate-730) and pm.IS_DEBIT_CARD is null and pm.PROFILE_ID is not null"
		 * ;
		 */
		String startDate = "";
		String endDate = "";
		if (null != s[1]) {
			startDate = s[0];
			endDate = s[1];
		} else if (null != s[0] && null == s[1]) {
			startDate = "1/1/" + s[0];
			endDate = "12/31" + s[0];
		}

		else {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			startDate = "1/1/" + year;
			endDate = "" + calendar.get(Calendar.DAY_OF_MONTH) + ""+ calendar.get(Calendar.MONTH) + "" + year;
		}

		String paymentMethods = "select pm.ID, pm.PROFILE_ID from cust.PAYMENTMETHOD pm where pm.CUSTOMER_ID in (select unique c.ID from cust.customer c "
				+ "where exists (select 1 from cust.sale s where s.cromod_date between TO_DATE('"+ startDate+ "', 'DD/MM/YYYY') and TO_DATE('"+ endDate+ "', 'DD/MM/YYYY') and s.customer_id=c.id))"
				+ " and pm.IS_DEBIT_CARD is null and pm.CARD_TYPE in ('MC','VISA')";
		InitialContext ctx = null;
		DataSource ds = null;
		ResultSet rs = null;
		Connection conn = null;
		List<String> debitCardProfiles = new ArrayList<String>();
		List<String> otherCards = new ArrayList<String>();
		
		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			env.put(Context.PROVIDER_URL, "t3://localhost:7001");
			ctx = new InitialContext(env);
			ds = (DataSource) ctx.lookup("fddatasource");
			conn = ds.getConnection();
			rs = conn.createStatement().executeQuery(paymentMethods);

			if (null != rs) {
				while (rs.next()) {
					String profileId = rs.getString(2);

					BINCache binCache = BINCache.getInstance();
					if (null != profileId) {
						PaymentMethod pm = DebitCardRecognitionCron.getAccountInfo(profileId);
						if (null != pm) {
							boolean isDebit = binCache.isDebitCard(pm.getAccountNumber(), EnumCardType.VISA)|| binCache.isDebitCard(pm.getAccountNumber(),EnumCardType.MC);
							System.out.println("account number: "+ pm.getAccountNumber()+ "  and is it debit card?, the answer is: "+ isDebit + "\n");
							if (isDebit)
								debitCardProfiles.add(rs.getString(1));
							else
								otherCards.add(rs.getString(1));
						}
						
					}
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		updatePaymentMethod(ds, conn, debitCardProfiles, "D");
		updatePaymentMethod(ds, conn, otherCards, "O");
	}

	private static void updatePaymentMethod(DataSource ds, Connection conn,
			List<String> debitCardProfiles, String cardIdentifier) {
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement("update cust.PAYMENTMETHOD pm set pm.IS_DEBIT_CARD=? where pm.ID=?");

			for (String profileId : debitCardProfiles) {

				stmt.setString(1, cardIdentifier);
				stmt.setString(2, profileId);
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
			// conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
