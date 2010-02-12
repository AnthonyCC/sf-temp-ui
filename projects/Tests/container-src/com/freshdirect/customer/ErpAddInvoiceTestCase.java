/*
 * Created on Apr 24, 2003
 */

package com.freshdirect.customer;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpInvoicedCreditModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.dataloader.payment.ejb.InvoiceLoaderHome;
import com.freshdirect.dataloader.payment.ejb.InvoiceLoaderSB;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 */
public class ErpAddInvoiceTestCase extends TestCase {
	private Context ctx;
	private ErpSaleHome saleHome;
	private InvoiceLoaderHome invoiceLoaderHome;

	private String saleId = null;
	private String invoiceNumber = null;
	private String waveNumber = null;
	private String truckNumber = null;
	private String stop = null;
	private boolean shortItem = false;
	
	public ErpAddInvoiceTestCase(String name) {
		super(name);
	}
	
	protected void setUp() throws NamingException { 
		ctx = TestUtil.getInitialContext();
		saleHome = (ErpSaleHome)ctx.lookup("freshdirect.erp.Sale");
		invoiceLoaderHome = (InvoiceLoaderHome) ctx.lookup("freshdirect.dataloader.InvoiceLoader");
	}
	
	public static void main(String[] args) {
		ErpAddInvoiceTestCase testCase = new ErpAddInvoiceTestCase("addInvoice");
		testCase.loadCommandLineArgs(args);
		junit.textui.TestRunner.run(testCase);
	}
	
	protected void tearDown() throws NamingException {
		ctx.close();
	}
	
	public void loadCommandLineArgs(String args[]) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("saleId=")) {
					saleId = args[i].substring("saleId=".length());
				} else if (args[i].startsWith("invoiceNumber=")) {
					invoiceNumber = args[i].substring("invoiceNumber=".length());
				} else if (args[i].startsWith("waveNumber=")) {
					waveNumber = args[i].substring("waveNumber=".length());
				} else if (args[i].startsWith("truckNumber=")) {
					truckNumber = args[i].substring("truckNumber=".length());
				} else if (args[i].startsWith("stop=")) {
					stop = args[i].substring("stop=".length());
				} else if (args[i].startsWith("shortItem=")) {
					String si = args[i].substring("shortItem=".length());
					shortItem = new Boolean(si).booleanValue();
				}
			}
		}
	}
	
	public void addInvoice() throws Exception {
		
		
		if (saleId == null) {
			saleId = "8790484";
		}
		ErpSaleEB saleEB = saleHome.findByPrimaryKey(new PrimaryKey(saleId));  

		ErpAbstractOrderModel order = saleEB.getCurrentOrder();
		ErpInvoiceModel invoice = new ErpInvoiceModel();
		
		invoice.setAmount(order.getAmount());
		if (invoiceNumber == null) {
			invoiceNumber = "00009001";
		}
		invoice.setInvoiceNumber(invoiceNumber);
		Discount discount = null;//order.getDiscount(EnumDiscountType.DOLLAR_OFF).getDiscount();
		Discount invDiscount = null;
		if(discount != null){
			invDiscount = new Discount("UNKNOWN", discount.getDiscountType(), discount.getAmount());
		}
		//invoice.setDiscount(new ErpDiscountLineModel(invDiscount));
		invoice.setDiscounts(order.getDiscounts());
		invoice.setSubTotal(order.getSubTotal());
		invoice.setTax(order.getTax());
		invoice.setTransactionSource(EnumTransactionSource.SYSTEM);
		invoice.setTransactionDate(new Date());
		
		List orderLines = order.getOrderLines();
		List invoiceLines = new ArrayList();
		ErpOrderLineModel orderLine = null;
		ErpInvoiceLineModel invoiceLine = null;
		for(int i = 0, size = orderLines.size(); i < size; i++){
			orderLine = (ErpOrderLineModel)orderLines.get(i);
			invoiceLine = new ErpInvoiceLineModel();
			invoiceLine.setPrice(orderLine.getPrice());
			if (i==0 && shortItem) { // short first item if requested
				invoiceLine.setQuantity(0.0);
				invoice.setAmount(order.getAmount()-(orderLine.getPrice()*orderLine.getQuantity()));
				invoice.setSubTotal(order.getSubTotal()-(orderLine.getPrice()*orderLine.getQuantity()));
			} else {
				invoiceLine.setQuantity(orderLine.getQuantity());
			}
			invoiceLine.setWeight(orderLine.getQuantity());
			invoiceLine.setDepositValue(orderLine.getDepositValue());
			invoiceLine.setMaterialNumber(orderLine.getMaterialNumber());
			invoiceLine.setOrderLineNumber(orderLine.getOrderLineNumber());
			invoiceLine.setTaxValue(orderLine.getPrice() * orderLine.getTaxRate());
			
			invoiceLines.add(invoiceLine);
		}
		
		invoice.setInvoiceLines(invoiceLines);

		List invoicedCharges = new ArrayList();
		for(Iterator i = order.getCharges().iterator(); i.hasNext(); ){
			ErpChargeLineModel charge = (ErpChargeLineModel) i.next();			
			ErpChargeLineModel invoicedCharge = new ErpChargeLineModel();			
			invoicedCharge.setAmount(charge.getAmount());
			invoicedCharge.setDiscount(charge.getDiscount());
			invoicedCharge.setReasonCode(charge.getReasonCode());
			invoicedCharge.setTaxRate(charge.getTaxRate());
			invoicedCharge.setType(charge.getType());			
			invoicedCharges.add(invoicedCharge);
		}
		invoice.setCharges(invoicedCharges);
			
		List invoicedCredits = new ArrayList();
		int sapCreditNumber = 1;
		for(Iterator i = order.getAppliedCredits().iterator(); i.hasNext(); ){
			ErpAppliedCreditModel appliedCredit = (ErpAppliedCreditModel) i.next();
			
			ErpInvoicedCreditModel invoicedCredit = new ErpInvoicedCreditModel();
			invoicedCredit.setAffiliate(ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
			invoicedCredit.setAmount(appliedCredit.getAmount());
			invoicedCredit.setDepartment(appliedCredit.getDepartment());
			invoicedCredit.setCustomerCreditPk(appliedCredit.getCustomerCreditPk());
			invoicedCredit.setOriginalCreditId(appliedCredit.getPK().getId());
			invoicedCredit.setSapNumber("0000"+sapCreditNumber++);
			
			invoicedCredits.add(invoicedCredit);
			
		}
		invoice.setAppliedCredits(invoicedCredits);
		
		InvoiceLoaderSB sb = invoiceLoaderHome.create();
		if (waveNumber == null) {
			waveNumber = "000001";
		}
		if (truckNumber == null) {
			truckNumber = "000001";
		}
		if (stop == null) {
			stop = "00001";
		}
		
		ErpShippingInfo sInfo = new ErpShippingInfo(waveNumber, truckNumber, stop, 1, 1, 1);
		sb.addAndReconcileInvoice(saleId, invoice, sInfo); 
		
	}

}
