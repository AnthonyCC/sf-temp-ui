package com.freshdirect.dataloader.payment.reconciliation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.giftcard.ErpGCSettlementInfo;

public class SapFileBuilder implements SettlementBuilderI {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

	private static final String SETTLEMENT = "STL";
	private static final String SETTLEMENT_FAILED = "STF";
	private static final String CHARGE_BACK = "CBK";
	private static final String CHARGE_BACK_REVERSAL = "CBR";
	private static final String FD_REFUND = "REF";
	private static final String ECHECK_BOUNCE_CHECK_FEE = "ECB";
	private static final String PAYMENT_RECHARGE = "PYR";
	
	private static final String NO_SPLIT = " ";
	private static final String SPLIT = "SPL";
	
	private final StringBuffer sb = new StringBuffer(32768);

	private boolean buildOldSapFileFormat = false;
	
	public void writeTo(File file) throws IOException {
		FileOutputStream ofs = new FileOutputStream(file);
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(ofs));
		bfw.write(sb.toString());
		bfw.flush();
		bfw.close();
	}

	public String getData() {
		return sb.toString();
	}

	public void addChargeDetail(ErpSettlementInfo info, boolean refund, double amount, EnumCardType cardType) {
		sb.append("D");

		sb.append("\t");
		sb.append(info.getInvoiceNumber());

		sb.append("\t");		
		if (buildOldSapFileFormat) {
			sb.append(" ");  // chargeback flag

			sb.append("\t");
			sb.append(" "); //chargeback reversal flag
			
			sb.append("\t");
			//if transaction code is '06'/Return flag
			if(refund){
				sb.append("X");
			}else{
				sb.append(" ");
			}			
		} else {
			//if transaction code is '06'/Return flag
			if(refund){
				sb.append(FD_REFUND);
			}else{
				sb.append(SETTLEMENT);
			}
		}
		sb.append("\t");
		sb.append( cardType.getSapName() );

		sb.append("\t");
		sb.append("USD");

		sb.append("\t");
		sb.append( amount );
		
		this.appendInfo(info);
	}
	public void addGCChargeDetail(ErpGCSettlementInfo info, EnumCardType cardType) {
		sb.append("D");

		sb.append("\t");
		sb.append(info.getInvoiceNumber());

		sb.append("\t");		
		sb.append(SETTLEMENT);
		sb.append("\t");
		sb.append( cardType.getSapName() );

		sb.append("\t");
		sb.append("USD");

		sb.append("\t");
		sb.append(Math.abs(info.getAmount()) );
		
		this.appendInfo(info);
	}

	public void addFailedGCSettlement(ErpGCSettlementInfo info, EnumCardType cardType) {
		sb.append("D");

		sb.append("\t");
		sb.append(info.getInvoiceNumber());

		sb.append("\t");		
		//if post auth failed then set STF else STL
		if(info.getAmount() >=0)
			sb.append(SETTLEMENT_FAILED);
		else
			sb.append(SETTLEMENT);
		sb.append("\t");
		sb.append( cardType.getSapName() );

		sb.append("\t");
		sb.append("USD");

		sb.append("\t");
		sb.append(Math.abs(info.getAmount()) );
		
		this.appendInfo(info);
	}

	public void addFailedSettlement(ErpSettlementInfo info, double amount, EnumCardType cardType) {
		sb.append("D");

		sb.append("\t");
		sb.append(info.getInvoiceNumber());

		sb.append("\t");
		//SAP can only process positive number. STF means debit, STL means credit
		if(amount>=0)
			sb.append(SETTLEMENT_FAILED);
		else
			sb.append(SETTLEMENT);
		
		sb.append("\t");
		sb.append( cardType.getSapName() );

		sb.append("\t");
		sb.append("USD");

		sb.append("\t");
		//always return positive number
		sb.append( Math.abs(amount) );
		
		this.appendInfo(info);
	}


	public void addHeader(Date batchDate, String batchNumber, double netDeposit) {
		sb.append("H");
		sb.append("\t");
		sb.append(DATE_FORMATTER.format(batchDate));
		sb.append("\t");
		sb.append(batchNumber);
		sb.append("\t");
		sb.append(netDeposit);
		sb.append("\n");		
	}


	public void addCreditFee(EnumCardType ccType, double amount) {
		sb.append("F");
		sb.append("\t");
		sb.append(ccType.getSapName());
		sb.append("\t");
		sb.append(amount);
		sb.append("\n");
	}


	public void addInvoice(double amount, String description) {
		sb.append("M");
		sb.append("\t");
		sb.append(amount);
		sb.append("\t");
		sb.append(description);
		sb.append("\n");
	}


	public void addChargeback(ErpSettlementInfo info, double amount) {
		sb.append("D");
		sb.append("\t");
		sb.append(info.getInvoiceNumber());
		sb.append("\t");
		if (buildOldSapFileFormat) {
			sb.append("X");
			sb.append("\t");
			sb.append(" ");
			sb.append("\t");
			sb.append(" ");
		} else {
			sb.append(CHARGE_BACK);
		}
		sb.append("\t");
		sb.append(info.getCardType().getSapName());
		sb.append("\t");
		sb.append("USD");
		sb.append("\t");
		sb.append(amount);
		
		this.appendInfo(info);
	}
	
	public void addChargebackReversal(ErpSettlementInfo info, double amount){
		sb.append("D");
		sb.append("\t");
		sb.append(info.getInvoiceNumber());
		sb.append("\t");
		if (buildOldSapFileFormat) {
			sb.append(" ");
			sb.append("\t");
			sb.append("X");
			sb.append("\t");
			sb.append(" ");
		} else {
			sb.append(CHARGE_BACK_REVERSAL);
		}
		sb.append("\t");
		sb.append(info.getCardType().getSapName());
		sb.append("\t");
		sb.append("USD");
		sb.append("\t");
		sb.append(amount);
		
		this.appendInfo(info);
	}

	public void addBounceCheckCharge(ErpSettlementInfo info, EnumCardType type, double amount){
		sb.append("D");
		sb.append("\t");
		sb.append(info.getInvoiceNumber());
		sb.append("\t");
		if (buildOldSapFileFormat) {  // old sap file format doesn't recognize bounce check fee
			sb.append(" ");
			sb.append("\t");
			sb.append(" ");
			sb.append("\t");
			sb.append(" ");
		} else {
			sb.append(ECHECK_BOUNCE_CHECK_FEE);
		}
		sb.append("\t");
		sb.append(type.getSapName());
		sb.append("\t");
		sb.append("USD");
		sb.append("\t");
		sb.append(amount);
		
		this.appendInfo(info);
	}
	
	public void addPaymentRecharge(ErpSettlementInfo info, EnumCardType type, double amount){
		
		sb.append("D");
		sb.append("\t");
		sb.append(info.getInvoiceNumber());
		sb.append("\t");
		if (buildOldSapFileFormat) {  // old sap file format doesn't recognize bounce check fee
			sb.append(" ");
			sb.append("\t");
			sb.append(" ");
			sb.append("\t");
			sb.append(" ");
		} else {
			sb.append(PAYMENT_RECHARGE);
		}
		sb.append("\t");
		sb.append(type.getSapName());
		sb.append("\t");
		sb.append("USD");
		sb.append("\t");
		sb.append(amount);

		this.appendInfo(info);
	}
	
	private void appendInfo(ErpSettlementInfo info) {
		sb.append("\t");
		sb.append(info.hasSplitTransaction() ? SPLIT : NO_SPLIT);
		
		sb.append("\t");
		sb.append(info.getAffiliate().getCode());
		
		sb.append("\t");
		sb.append(info.getTransactionCount());
		
		sb.append("\t");
		sb.append(info.getId());
		
		sb.append("\n");
	}

	public void setBuildOldSapFileFormat(boolean buildOldSapFileFormat) {
		this.buildOldSapFileFormat = buildOldSapFileFormat;
	}
	
	public boolean getBuildOldSapFileFormat() { return this.buildOldSapFileFormat; }

}
