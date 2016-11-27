package com.freshdirect.payment;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.SapFileBuilder;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechCategory;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PaymentechFINParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PaymentechPDEParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.ACT0010DataParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PDE0017DDataParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PDE0018DDataParser;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.sap.ejb.SapException;

public class AuthBugTestCase extends TestCase {
	
	private Context ctx;
	private FDCustomerManagerHome home;
	private ReconciliationHome reconciliationHome; 
	private static final SimpleDateFormat SF = new SimpleDateFormat("MM/yy");
	private static final String PID = "944633";
	private static final String PID_NAME = "\"freshd\"";
	private static final String SUBMISSION_NUMBER = "\"51205.11DCh\"";
	private static final String MCC = "5411";
	
	private String[] orders = {"145399213", "145399230", "145399248", "145399265", "145399282", "145399300", "145399318", "145399336", "145399354", "145399372", "145399390", "145399409", 
		"145399437", "145399454", "145399470", "145399486", "145399533", "145399547", "145399574", "145399597", "145399608", "145399631", "145399652", "145399683", 
		"145399870", "145407891", "145407956", "145408036", "145408121", "145408151", "145408689", "145408726", "145408752", "145408793", "145408839", "145408858", 
		"145408888", "145408917", "145408946", "145408985", "145409001", "145409021", "145409055", "145409076", "145409095", "145409114", "145409154", "145409173", 
		"145409209", "145409248", "145409265", "145409285", "145409447"};
	
	private String[] pyr = {"145281242", "145271446", "145274570"};
	
	private String[] refunds = {"145280355", "145280756", "145279553", "145279385"};
	
	private String[] stlFailed = {"145281242", "145271446", "145274570"};
	
	private String[] cbks = {"145271324", "145272462", "145280772"};
	
	private double netAmount;
	private double visaFee;
	private double mcFee;
	private double refund;
	private double chargeback;
	private double sFail;
	
	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
		reconciliationHome = (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
		
		netAmount = 0.0;
		refund = 0.0;
		chargeback = 0.0;
		sFail = 0.0;
	}
	
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	
	public void testAuthBug() throws NamingException, CreateException, FDResourceException, BadDataException, IOException, ErpTransactionException, SapException {
		 //"145281242" 
		ACT0010DataParser p = new ACT0010DataParser();
		SapFileBuilder builder = new SapFileBuilder();
		ReconciliationSB sb = reconciliationHome.create();
		PaymentechFINParserClient client = new PaymentechFINParserClient(builder, sb);
		p.setClient(client);
		
		this.createCharges(p, builder);
		
		/*
		//this.createRefunds(p);
		
		PDE0018DDataParser p18 = new PDE0018DDataParser();
		PaymentechPDEParserClient pClient = new PaymentechPDEParserClient(builder, sb);
		p18.setClient(pClient);
		
		//this.createSettlementFail(p18);
		
		PDE0017DDataParser p17 = new PDE0017DDataParser();
		p17.setClient(pClient);
		
		//this.createChargeback(p17, false);
		
		//this.createChargeback(p17, true);
		
		//this.createPaymentRecharge(p);
		*/
		
		builder.addHeader(new Date(), String.valueOf(System.currentTimeMillis()), MathUtil.roundDecimal(netAmount - visaFee - mcFee - refund - sFail - chargeback));
		
		System.out.println("\nSAP Builder output");
		System.out.println(builder.getData());
		String fileName = "sapReconciliation_paymentech_2006_02_21.txt";
		File f = new File(DataLoaderProperties.getWorkingDir() + fileName);
		
		builder.writeTo(f);
		
		SettlementLoaderUtil.uploadFileToSap(fileName);
		
		SettlementLoaderUtil.callSettlementBapi(fileName);
	}
	
	private void createCharges(ACT0010DataParser p, SapFileBuilder builder) throws FDResourceException, RemoteException, CreateException, BadDataException {
		this.calculateFees();
		builder.addCreditFee(EnumCardType.VISA, MathUtil.roundDecimal(visaFee));
		builder.addCreditFee(EnumCardType.MC, MathUtil.roundDecimal(mcFee));
		
		for(int i = 0; i < orders.length; i++) {
			this.createSettlement(orders[i], p);
		}
	}
	
	private void createPaymentRecharge(ACT0010DataParser p) throws RemoteException, CreateException, FDResourceException, BadDataException {
		FDCustomerManagerSB sb = home.create();
		for(int i = 0; i < pyr.length; i++) {
			String orderId = pyr[i];
			FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(orderId);
			ErpPaymentMethodI pm = o.getPaymentMethod();
			ErpSaleModel sale = o.getSale();
			
			int recNumber = 1;
			int idx = 0;
			for(Iterator j = sale.getGoodCaptures().iterator(); j.hasNext(); ){
				ErpCaptureModel cap = (ErpCaptureModel) j.next();
				ErpAffiliate aff = cap.getAffiliate();
				String refNumber = "\"" + orderId + "X" + idx++ + "\"";
				String expDate = pm.getExpirationDate() != null ? SF.format(pm.getExpirationDate()) : ""; 
				String line = getSettlementLine(refNumber, recNumber++, aff.getPayementechTxDivision(), refNumber, cap.getAbaRouteNumber(), pm.getAccountNumber(), expDate, cap.getAmount(), pm.getCardType(), "D", cap.getAuthCode());
					
				System.out.print(line);
				p.parseLine(line);
			}
		}
	}
	
	private void createRefunds(ACT0010DataParser p) throws FDResourceException, RemoteException, CreateException, BadDataException {
		FDCustomerManagerSB sb = home.create();
		int recNumber = 1;
		for(int i = 0; i < refunds.length; i++) {
			int idx = 0;
			FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(refunds[i]);
			ErpPaymentMethodI pm = o.getPaymentMethod();
			ErpSaleModel sale = o.getSale();
			for(Iterator j = sale.getCashbacks().iterator(); j.hasNext(); ) {
				ErpCashbackModel c = (ErpCashbackModel) j.next();
				ErpAffiliate aff = c.getAffiliate();
				String refNumber = "\"" + refunds[i] + "X" + idx++ + "\"";
				String expDate = pm.getExpirationDate() != null ? SF.format(pm.getExpirationDate()) : "";
				String line = getSettlementLine(null, recNumber++, aff.getPayementechTxDivision(), refNumber, c.getAbaRouteNumber(), pm.getAccountNumber(), expDate, c.getAmount() * -1, pm.getCardType(), "R", c.getAuthCode());
				this.refund += c.getAmount();
				System.out.print(line);
				p.parseLine(line);
			}
			
		}
	}
	
	private void createChargeback(PDE0017DDataParser p, boolean reversal) throws RemoteException, CreateException, FDResourceException, ErpTransactionException, BadDataException {
		FDCustomerManagerSB sb = home.create();
		int recNumber = 1;
		for(int i = 0; i < cbks.length; i++){
			int idx = 0;
			FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(cbks[i]);
			ErpPaymentMethodI pm = o.getPaymentMethod();
			ErpSaleModel sale = o.getSale();
			for(Iterator j = sale.getSettlements().iterator(); j.hasNext(); ){
				ErpSettlementModel s = (ErpSettlementModel) j.next();
				ErpAffiliate aff = s.getAffiliate();
				String refNumber = "\"" + cbks[i] + "X" + idx++ + "\"";
				String line = getChargebackLine(refNumber, aff.getPayementechTxDivision(), s.getAmount(), reversal, "0002"+recNumber++, pm.getAccountNumber());
				this.chargeback += s.getAmount();
				System.out.println(line);
				
				p.parseLine(line);
			}
		}
		
	}
	
	private void calculateFees() throws RemoteException, CreateException, FDResourceException{
		for(int idx = 0; idx < orders.length; idx++){
			
			FDCustomerManagerSB sb = home.create();
			FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(orders[idx]);
			ErpPaymentMethodI pm = o.getPaymentMethod();
			ErpSaleModel sale = o.getSale();
			for(Iterator i = sale.getCaptures().iterator(); i.hasNext(); ){
				ErpCaptureModel cap = (ErpCaptureModel) i.next();
				if(EnumCardType.ECP.equals(pm.getCardType()) || EnumCardType.VISA.equals(pm.getCardType()) || EnumCardType.MC.equals(pm.getCardType())){
					netAmount += cap.getAmount();
				}
				
				if(EnumCardType.VISA.equals(pm.getCardType())){
					visaFee += MathUtil.roundDecimal(cap.getAmount() * 0.02);
				}
				
				if(EnumCardType.MC.equals(pm.getCardType())) {
					mcFee += MathUtil.roundDecimal(cap.getAmount() * 0.02);
				}
			}
		}
			
	}
	
	private void createSettlement(String orderId, ACT0010DataParser p) throws BadDataException, FDResourceException, RemoteException, CreateException {
		FDCustomerManagerSB sb = home.create();
		FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(orderId);
		ErpPaymentMethodI pm = o.getPaymentMethod();
		ErpSaleModel sale = o.getSale();
		
		//RACT0010|12/05/2005|944633|"freshd"|"51205.11DCh"|84|TD|87991|USD|"763541701X0"||5113900085600975|02/07|201.34|MC|D|12/03/2005|003563|100||||5411
		//System.out.println("RACT0010|12/05/2005|944633|\"freshd\"|\"51205.11DCh\"|84|TD|87991|USD|\"763541701X0\"||5113900085600975|02/07|201.34|MC|D|12/03/2005|003563|100||||5411");
		
		int recNumber = 1;
		int idx = 0;
		for(Iterator i = sale.getGoodCaptures().iterator(); i.hasNext(); ){
			ErpCaptureModel cap = (ErpCaptureModel) i.next();
			ErpAffiliate aff = cap.getAffiliate();
			String refNumber = "\"" + orderId + "X" + idx++ + "\"";
			String expDate = pm.getExpirationDate() != null ? SF.format(pm.getExpirationDate()) : ""; 
			String line = getSettlementLine(null, recNumber++, aff.getPayementechTxDivision(), refNumber, cap.getAbaRouteNumber(), pm.getAccountNumber(), expDate, cap.getAmount(), pm.getCardType(), "D", cap.getAuthCode());
				
			System.out.print(line);
			p.parseLine(line);
		}
	}
	
	private void createSettlementFail(PDE0018DDataParser p) throws ErpTransactionException, FDResourceException, RemoteException, CreateException, BadDataException {
		FDCustomerManagerSB sb = home.create();
		int recNumber = 1;
		for(int i = 0; i < this.stlFailed.length; i++) {
			FDOrderAdapter o = (FDOrderAdapter)sb.getOrder(stlFailed[i]);
			ErpPaymentMethodI pm = o.getPaymentMethod();
			ErpSaleModel sale = o.getSale();
			int idx = 0;
			for(Iterator j = sale.getSettlements().iterator(); j.hasNext(); ) {
				ErpSettlementModel s = (ErpSettlementModel) j.next();
				ErpAffiliate aff = s.getAffiliate();
				
				String refNumber = "\"" + stlFailed[i] + "X" + idx++ + "\"";
				String line = getSettlementFailLine(aff.getPayementechTxDivision(), refNumber, pm.getCardType(), "000000002"+recNumber++, pm.getAccountNumber(), s.getAmount());
				this.sFail += s.getAmount();
				System.out.print(line);
				p.parseLine(line);
			}
			
		}
	}
	
	private String getSettlementLine(String sequenceNumber, int recNumber, String division, String orderId, String routingNumber, String account, String expDate, double amount, EnumCardType card, String txCode, String authCode) {
		StringBuffer buffer = new StringBuffer();
		//buffer.append("RACT0010").append("|");
		buffer.append("12/12/2005").append("|");
		buffer.append(PID).append("|");
		buffer.append(PID_NAME).append("|");
		buffer.append(NVL.apply(sequenceNumber, SUBMISSION_NUMBER)).append("|");
		buffer.append(recNumber).append("|");
		buffer.append("TD").append("|");
		buffer.append(division).append("|");
		buffer.append("USD").append("|");
		buffer.append(orderId).append("|");
		buffer.append(NVL.apply(routingNumber, "").trim()).append("|");
		buffer.append(account).append("|");
		buffer.append(expDate).append("|");
		buffer.append(MathUtil.roundDecimal(amount)).append("|");
		buffer.append(card.getPaymentechCode()).append("|");
		buffer.append(NVL.apply(txCode, "")).append("|");
		buffer.append("12/10/2005").append("|");
		buffer.append(NVL.apply(authCode, "123456")).append("|");
		buffer.append("100").append("|");
		buffer.append("|").append("|").append("|");
		buffer.append(MCC).append("\n");
		
		return buffer.toString();
	}
	
	private String getSettlementFailLine(String division, String orderId, EnumCardType card, String sequenceNumber, String account, double amount) {
		//RPDE0018D|TD|87991||EC|USD|RTM||109510370|"752426736X0"|57315396|R01|11/30/2005|12/12/2005|12/12/2005|-139.30|2
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("TD").append("|");
		buffer.append(division).append("|");
		buffer.append("|");
		buffer.append(card.getPaymentechCode()).append("|");
		buffer.append("USD").append("|");
		buffer.append(EnumPaymentechCategory.NEW_CC_ECP_CHARGEBACKS.getName()).append("|");
		buffer.append("|");
		buffer.append(sequenceNumber).append("|");
		buffer.append(orderId).append("|");
		buffer.append(account).append("|");
		buffer.append("R01").append("|");
		buffer.append("12/10/2005").append("|");
		buffer.append("12/15/2005").append("|");
		buffer.append("12/15/2005").append("|");
		buffer.append(MathUtil.roundDecimal(amount)).append("|");
		buffer.append("2").append("\n");
		return buffer.toString();
	}
	
	private String getChargebackLine(String refNumber, String division, double amount, boolean reversal, String sequenceNumber, String account) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("TD").append("|");
		buffer.append(division).append("|");
		buffer.append(MathUtil.roundDecimal(amount)).append("|");
		buffer.append("N").append("|");
		buffer.append("USD").append("|");
		buffer.append(EnumPaymentechCategory.NEW_CC_ECP_CHARGEBACKS.getName()).append("|");
		buffer.append(reversal ? "R" : "").append("|");
		buffer.append(sequenceNumber).append("|");
		buffer.append(refNumber).append("|");
		buffer.append(account).append("|");
		buffer.append("08").append("|");
		buffer.append("12/01/2005").append("|");
		buffer.append("12/12/2005").append("|");
		buffer.append("12/12/2005").append("|");
		buffer.append(MathUtil.roundDecimal(amount)).append("|");
		buffer.append("|");
		buffer.append("1").append("\n");
		
		return buffer.toString();
		
	}
}
