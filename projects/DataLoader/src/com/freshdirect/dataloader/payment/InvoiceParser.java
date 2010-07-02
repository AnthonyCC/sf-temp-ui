/*
 * InvoiceParser.java
 *
 * Created on October 2, 2001, 4:08 PM
 */

package com.freshdirect.dataloader.payment;

/**
 *
 * @author  knadeem
 * @version
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpInvoicedCreditModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.FlatFileParser;
import com.freshdirect.dataloader.sap.SAPConstants;

/**
 * Beware, this class is Evil. It's a mutant flat file parser, with three different sets of fields.
 * Overriden getString() circumvents Field validation because of this - pretends all fields all required...
 */
public class InvoiceParser extends FlatFileParser implements SAPConstants, ProducerI{ 
    
    //predefined material numbers provided by SAP for different charges/promotions
	private final static String PROMOTION		= "000000000000009999";
	 
	private List<Field> headerFields = new ArrayList<Field>();
	private List<Field> lineFields = new ArrayList<Field>();
	private List<Field> creditFields = new ArrayList<Field>();
	private ConsumerI consumer = null;
	private String pk = null;
	private String billingType = null;
	private ErpShippingInfo shippingInfo = null;
	private ErpInvoiceModel invoice = null;
	private List<ErpInvoiceLineModel> invoiceLines = new ArrayList<ErpInvoiceLineModel>();
	
	public static final String CREDIT_MEMO = "C";
	public static final String INVOICE_HEADER = "H";
	public static final String INVOICE_LINE = "D";
	
	/** Creates new InvoiceParser */
	public InvoiceParser() {
		super();
		fields.add(new Field(TYPE_INDICATOR, 1, true));
		fields.add(new Field(WEB_ORDER_NUMBER, 35, true));
		fields.add(new Field(INVOICE_NUMBER, 10, true));
		//fields.add(new Field(SALES_ORDER_NUMBER, 10, true));
		buildInvoiceFields();
		
		
	}
	
	@Override
    public void parseFile(String filename) {
		super.parseFile(filename);
		invoice.setInvoiceLines(invoiceLines);
		this.consumer.consume(invoice, pk, billingType, this.shippingInfo);
	}

	@Override
    protected BufferedReader createReader(String fileName) throws IOException {
		return new BufferedReader(
			new InputStreamReader(
				new GZIPInputStream(
					new FileInputStream(fileName)
				)
			)
		);
	}

	/**
	 * using a list of fields provided by a concrete subclass
	 * chop up a line into tokens and returns the result
	 * as a set of key-value pairs in a HashMap
	 * @param line a single line from a text file
	 * @throws BadDataException any problems encountered converting the line into tokens
	 * @return a HashMap of String tokens from a line of a text file,
	 * keyed by their field names
	 */
	@Override
    protected HashMap<String, String> tokenize(String line) throws BadDataException {
		//
		// iterate through the list of fields are read each token
		// from the line
		//
	Iterator<Field> iter = fields.iterator();
		HashMap<String, String> retval = new HashMap<String, String>();
		String type = line.substring(0, 1);
		int startPosition = 0;
		if(!CREDIT_MEMO.equals(type)){
			while (iter.hasNext()) {
				Field f = iter.next();
				String name = f.getName();
				int endPosition = startPosition + f.getLength();
				try {
					retval.put(name, line.substring(startPosition, endPosition).trim());
				} catch (StringIndexOutOfBoundsException sie) {
					throw new BadDataException(sie, "Found a line that was too short");
				}
				startPosition = endPosition;
			}
			if(INVOICE_HEADER.equals(type)){
				tokenizeInvoiceHeader(retval, line, startPosition);
			}
			if(INVOICE_LINE.equals(type)){
				tokenizeInvoiceLine(retval, line, startPosition);
			}
		}else{
			tokenizeCreditMemo(retval, line);
		}
		return retval;
	}
	
	protected void tokenizeInvoiceHeader(HashMap<String, String> retval, String line, int startPosition) throws BadDataException {
		int endPosition;
		for (Field f : headerFields) {
			String name = f.getName();
			endPosition = startPosition + f.getLength();
			try{
				String str = line.substring(startPosition, endPosition).trim();
				if(str.endsWith("-")){
					StringBuffer buff = new StringBuffer("-");
					buff.append(str.substring(0, str.length()-1));
					str = buff.toString();
				}
				retval.put(name, str);
			}catch(StringIndexOutOfBoundsException sie){
				throw new BadDataException(sie, "Found a line that was too short while parsing invoice header");
			}
			startPosition = endPosition;
		}
	}
	
	protected void tokenizeInvoiceLine(HashMap<String, String> retval, String line, int startPosition) throws BadDataException {
		for (Field f : lineFields) {
			String name = f.getName();
			int endPosition = startPosition + f.getLength();
			try{
				String str = line.substring(startPosition, endPosition).trim();
				if(str.endsWith("-")){
					StringBuffer buff = new StringBuffer("-");
					buff.append(str.substring(0, str.length()-1));
					str = buff.toString();
				}
				retval.put(name, str);
			}catch(StringIndexOutOfBoundsException sie){
				throw new BadDataException(sie, "Found a line that was too short while parsing invoice line");
			}
			startPosition = endPosition;
		}
	}
	
	protected void tokenizeCreditMemo(HashMap<String, String> retval, String line) throws BadDataException {
		int startPosition = 0;
		for (Field f : creditFields) {
			String name = f.getName();
			int endPosition = startPosition + f.getLength();
			try{
				String str = line.substring(startPosition, endPosition).trim();
				if(str.endsWith("-")){
					StringBuffer buff = new StringBuffer("-");
					buff.append(str.substring(0, str.length()-1));
					str = buff.toString();
				}
				retval.put(name, str);
			}catch(StringIndexOutOfBoundsException sie){
				throw new BadDataException(sie, "Found a line that was too short while parsing credit");
			}
			startPosition = endPosition;
		}
	}
	
	
	/**
	 * a template method that must be defined by implementors
	 * subclasses will know how to assemble model objects
	 * from a a hash of tokens
	 * @param tokens a HashMap containing parsed tokens from a single line
	 * of a text file, keyed by their field names
	 * @throws BadDataException an problems while trying to assemble objects from the
	 * supplied tokens
	 */
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
						
		String type = tokens.get(TYPE_INDICATOR);
		if(CREDIT_MEMO.equalsIgnoreCase(type)){
			ErpInvoicedCreditModel credit = new ErpInvoicedCreditModel();
			credit.setAmount(getDouble(tokens, CREDIT_AMOUNT));
			credit.setOriginalCreditId(getString(tokens, WEB_REFERENCE_NUMBER));
			String creditMemoNumber = tokens.get(CREDIT_MEMO_NUMBER).trim();
			
			if (invoice.getAmount() > 0 && "".equals(creditMemoNumber)) {
				throw new BadDataException("Invoice "
					+ invoice.getInvoiceNumber()
					+ ": Credit memo number required for non-zero invoices");
			}
			
			credit.setSapNumber(creditMemoNumber);
			invoice.addAppliedCredit(credit);
			
			for (Field f : creditFields) {
				String name = f.getName();
				System.out.println(name+": "+tokens.get(name));
			}
		}else{ 
			for (Field f : fields) {
				String name = f.getName();
				System.out.println(name+": "+tokens.get(name));
			}
			if(INVOICE_HEADER.equalsIgnoreCase(type)){
				if(pk == null){
					pk = getString(tokens, WEB_ORDER_NUMBER);
				}
				if(billingType == null){
					billingType = getString(tokens, BILLING_TYPE);
				}
				
				if(this.shippingInfo == null){
					this.shippingInfo = new ErpShippingInfo(getString(tokens, TRUCK_NUMBER), getString(tokens, STOP_SEQUENCE), 
												getInt(tokens, NUMBER_REGULAR_CARTONS), getInt(tokens, NUMBER_FREEZER_CARTONS), 
												getInt(tokens, NUMBER_ALCOHOL_CARTONS));
				}
				if(invoice != null){
					
					invoice.setInvoiceLines(invoiceLines);
					this.consumer.consume(invoice, pk, billingType, this.shippingInfo);
					invoiceLines = new ArrayList<ErpInvoiceLineModel>();
				}
				invoice = new ErpInvoiceModel();
				pk = getString(tokens, WEB_ORDER_NUMBER);
				billingType = getString(tokens, BILLING_TYPE);
				this.shippingInfo = new ErpShippingInfo(getString(tokens, TRUCK_NUMBER), getString(tokens, STOP_SEQUENCE), 
						getInt(tokens, NUMBER_REGULAR_CARTONS), getInt(tokens, NUMBER_FREEZER_CARTONS), 
						getInt(tokens, NUMBER_ALCOHOL_CARTONS));
				double amount = getDouble(tokens, INVOICE_TOTAL_AFTER_CREDIT);
				double tax = getDouble(tokens, INVOICE_TAX);
				invoice.setAmount(amount);
				invoice.setInvoiceNumber(getString(tokens, INVOICE_NUMBER));
				invoice.setSubTotal(getDouble(tokens, INVOICE_SUB_TOTAL));
				invoice.setTax(tax);
				invoice.setTransactionDate(new Date(System.currentTimeMillis()));
				invoice.setTransactionSource(EnumTransactionSource.SYSTEM);
								
				for (Field f : headerFields) {
					String name = f.getName();
					System.out.println(name+": "+tokens.get(name));
				}
				
				double discountAmount = getDouble(tokens, ACTUAL_DISCOUNT_AMOUNT);

				//invoice.setSubTotal(invoice.getSubTotal() +discountAmount);									
				
			}
			if(INVOICE_LINE.equalsIgnoreCase(type)){
				String materialNumber = getString(tokens, MATERIAL_NUMBER);
				double amount = getDouble(tokens, INVOICE_LINE_AMOUNT);
				if(PROMOTION.equalsIgnoreCase(materialNumber)){
					// FIXME promotion code is not know at this point, will be filled in by InvoiceLoaderSessionBean
					Discount discount = new Discount("UNKNOWN", EnumDiscountType.DOLLAR_OFF, Math.abs(amount));
					invoice.addDiscount(new ErpDiscountLineModel(discount));
					
					//'cause SAP deals with promotion as line item (yuck!!) to get subtotal conceptually correct
					//adding back promotion value to subtotal 
					invoice.setSubTotal(invoice.getSubTotal() + (Math.abs(amount)));
					
				} else {
					
					// check if it's a charge-line
				
					boolean chargeMatch = false;
					for (Iterator i = EnumChargeType.getEnumList().iterator(); i.hasNext();) {
						EnumChargeType chargeType = (EnumChargeType) i.next();
						if (materialNumber.equals(chargeType.getMaterialNumber())) {
							chargeMatch = true;
							ErpChargeLineModel charge = new ErpChargeLineModel();
							charge.setType(chargeType);
							charge.setAmount(amount);
							invoice.addCharge(charge);

							invoice.setSubTotal(invoice.getSubTotal() - amount);
						}
					}
					
					
					if (!chargeMatch) {
						// process as regular invoice line
						
						ErpInvoiceLineModel invoiceLine = new ErpInvoiceLineModel();
						double customizationPrice = getDouble(tokens, CUSTOMIZATION_PRICE);
						invoiceLine.setPrice(getDouble(tokens, INVOICE_LINE_AMOUNT)+customizationPrice);
						invoiceLine.setCustomizationPrice(customizationPrice);
						invoiceLine.setTaxValue(getDouble(tokens, INVOICE_LINE_TAX));
						invoiceLine.setDepositValue( getDouble(tokens, INVOICE_LINE_BOTTLE_DEPOSIT) );
						invoiceLine.setQuantity(getDouble(tokens, ACTUAL_SHIPPED_QUANTITY));
						invoiceLine.setMaterialNumber(getString(tokens, MATERIAL_NUMBER));
						invoiceLine.setOrderLineNumber(getString(tokens, INVOICE_LINE_NUMBER));
						invoiceLine.setWeight(getDouble(tokens, GROSS_WEIGHT));
						invoiceLine.setActualCost(getDouble(tokens, ACTUAL_COST));
						invoiceLine.setActualDiscountAmount(getDouble(tokens, INVOICE_LINE_DISCOUNT_AMOUNT));						
						//invoiceLine.setPrice(invoiceLine.getPrice()+invoiceLine.getActualDiscountAmount());
						invoiceLines.add(invoiceLine);
					}
				}
					
				for (Field f : lineFields) {
					String name = f.getName();
					System.out.println(name+": "+tokens.get(name));
				}
			}
		}
		System.out.println("\n\n");
	}
	
	public ConsumerI getConsumer() {
		return consumer;
	}
	
	public void setConsumer(ConsumerI consumer) {
		this.consumer = consumer;
	}
	
	 @Override
    protected String getString(Map<String, String> tokens, String fieldName) throws BadDataException {
        System.out.println("fieldName :"+fieldName);
        String s = tokens.get(fieldName).trim();
        //
        // check if this is a required field
        //
        if (s.equals("")) {
            throw new BadDataException("Required field \"" + fieldName + "\" was empty for order# "+tokens.get(WEB_ORDER_NUMBER));
        }
        return s;
    }
	
	protected void buildInvoiceFields(){
		// header fields
		headerFields.add(new Field(INVOICE_SUB_TOTAL, 15, true));
		headerFields.add(new Field(INVOICE_TAX, 15, true));
		headerFields.add(new Field(INVOICE_BOTTLE_DEPOSIT, 15, true));
		headerFields.add(new Field(INVOICE_TOTAL_BEFORE_CREDIT, 15, true));		
		headerFields.add(new Field(CREDIT_AMOUNT, 15, true));
		headerFields.add(new Field(INVOICE_TOTAL_AFTER_CREDIT, 15, true));
		headerFields.add(new Field(ORDER_STATUS, 1, true));
		headerFields.add(new Field(BILLING_TYPE, 4, true));
		headerFields.add(new Field(STOP_SEQUENCE, 5, true));
		headerFields.add(new Field(TRUCK_NUMBER, 6, true));
		headerFields.add(new Field(SALES_ORDER_NUMBER, 10, true));
		headerFields.add(new Field(NUMBER_REGULAR_CARTONS, 3, false));
		headerFields.add(new Field(NUMBER_FREEZER_CARTONS, 3, false));
		headerFields.add(new Field(NUMBER_ALCOHOL_CARTONS, 3, false));
		headerFields.add(new Field(ACTUAL_DISCOUNT_AMOUNT, 15, false));
		
		//line fields
		lineFields.add(new Field(INVOICE_LINE_NUMBER, 6, true));
		lineFields.add(new Field(MATERIAL_NUMBER, 18, true));
		lineFields.add(new Field(INVOICE_LINE_AMOUNT,15, true));
		lineFields.add(new Field(INVOICE_LINE_TAX, 15, true));
		lineFields.add(new Field(INVOICE_LINE_BOTTLE_DEPOSIT, 15, true));
		lineFields.add(new Field(UNIT_PRICE, 16, true));
		lineFields.add(new Field(CUSTOMIZATION_PRICE, 15, true));
		lineFields.add(new Field(SALES_UNIT_OF_MEASURE, 3, true));
		lineFields.add(new Field(ORDER_QUANTITY, 13, true));
		lineFields.add(new Field(ACTUAL_SHIPPED_QUANTITY, 13, true));
		lineFields.add(new Field(TOTAL_SHIPPED_QUANTITY, 13, true));
		lineFields.add(new Field(GROSS_WEIGHT, 15, true));
		lineFields.add(new Field(WEIGHT_UNIT, 3, true));
		lineFields.add(new Field(ORDER_LINE_STATUS, 1, true));
		lineFields.add(new Field(SALES_ORDER_NUMBER, 10, true));
		lineFields.add(new Field(ACTUAL_COST, 15, true));
		lineFields.add(new Field(INVOICE_LINE_DISCOUNT_AMOUNT, 15, true));
		
		//credit fields
		creditFields.add(new Field(TYPE_INDICATOR, 1, true));
		creditFields.add(new Field(WEB_ORDER_NUMBER, 35, true));
		creditFields.add(new Field(WEB_REFERENCE_NUMBER, 10, true));
		creditFields.add(new Field(CREDIT_MEMO_NUMBER, 10, false));
		creditFields.add(new Field(CREDIT_AMOUNT, 15, true));
		creditFields.add(new Field(SALES_ORDER_NUMBER, 10, true));
	}
	
}
