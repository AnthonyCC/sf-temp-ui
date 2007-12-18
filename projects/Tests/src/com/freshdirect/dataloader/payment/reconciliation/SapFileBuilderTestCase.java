package com.freshdirect.dataloader.payment.reconciliation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;

public class SapFileBuilderTestCase extends TestCase {

	private SapFileBuilder builder = new SapFileBuilder();
	
	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public SapFileBuilderTestCase(String arg0) {
		super(arg0);
	}

	public void testAddChargeDetail() {
		ErpAffiliate aff = new ErpAffiliate("FD","FreshDirect",null,null,null,new HashMap(),new HashSet());
		ErpSettlementInfo info = new ErpSettlementInfo("000987", aff);
		info.setSplitTransaction(true);
		info.setTransactionCount(1);
		info.setId("123");
		builder.addChargeDetail(info, true, 1234.56, EnumCardType.VISA);
		assertEquals("D\t000987\tREF\tVISA\tUSD\t1234.56\tSPL\tFD\t1\t123\n", builder.getData());
		

	}

	public void testAddHeader() throws ParseException {
		builder.addHeader(DF.parse("1969-12-31"), "12345", 1234.56);
		assertEquals("H\t12/31/1969\t12345\t1234.56\n", builder.getData() );
	}

	public void testAddCreditFee() {
		builder.addCreditFee(EnumCardType.VISA, 10.12);
		assertEquals("F\tVISA\t10.12\n", builder.getData());	
	}

	public void testAddInvoice() {
		builder.addInvoice(1234.56, "AUTHORIZATION FEE");
		assertEquals("M\t1234.56\tAUTHORIZATION FEE\n", builder.getData());
	}

	public void testAddChargeback() {
		ErpAffiliate aff = new ErpAffiliate("FD","FreshDirect",null,null,null,new HashMap(),new HashSet());
		ErpSettlementInfo info = new ErpSettlementInfo("000987", aff);
		info.setSplitTransaction(true);
		info.setTransactionCount(1);
		info.setId("123");
		info.setCardType(EnumCardType.VISA);

		builder.addChargeback(info,1234.56);
		assertEquals("D\t000987\tCBK\tVISA\tUSD\t1234.56\tSPL\tFD\t1\t123\n", builder.getData());
		
	}
	
	public void testAddChargebackReversal(){
		ErpAffiliate aff = new ErpAffiliate("FD","FreshDirect",null,null,null,new HashMap(),new HashSet());
		ErpSettlementInfo info = new ErpSettlementInfo("000987", aff);
		info.setSplitTransaction(true);
		info.setTransactionCount(1);
		info.setId("123");
		info.setCardType(EnumCardType.VISA);

		builder.addChargebackReversal(info, 1234.56);
		assertEquals("D\t000987\tCBR\tVISA\tUSD\t1234.56\tSPL\tFD\t1\t123\n", builder.getData());
	}

	
}
