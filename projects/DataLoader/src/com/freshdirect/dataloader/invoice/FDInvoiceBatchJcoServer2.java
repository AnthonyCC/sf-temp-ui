package com.freshdirect.dataloader.invoice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
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
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.payment.ejb.InvoiceLoaderHome;
import com.freshdirect.dataloader.payment.ejb.InvoiceLoaderSB;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.InvoiceCreditParameter;
import com.freshdirect.dataloader.sap.jco.server.param.InvoiceEntryParameter;
import com.freshdirect.dataloader.sap.jco.server.param.InvoiceHeaderParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.sap.SapProperties;
import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoCustomRepository;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;

/**
 * This class will be used for populating the invoice for orders via Jco-server
 * (3.0) registered to the ERP system
 * 
 * @author kkanuganti
 */
public class FDInvoiceBatchJcoServer2 extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDInvoiceBatchJcoServer2.class.getName());

	private String serverName;

	private String functionName;

	private InvoiceLoaderHome invoiceLoaderHome = null;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDInvoiceBatchJcoServer2(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDInvoiceBatchRepository");

		final JCoRecordMetaData metaInvoiceHeaderList = JCo.createRecordMetaData("INVOICE_HEADER_LIST");
		createInvoiceHeaderMetadata(repository, metaInvoiceHeaderList);

		final JCoRecordMetaData metaInvoiceItemList = JCo.createRecordMetaData("INVOICE_LINE_LIST");
		createInvoiceLineMetadata(repository, metaInvoiceItemList);
		
		final JCoRecordMetaData metaInvoiceCreditList = JCo.createRecordMetaData("INVOICE_CREDIT_LIST");
		createInvoiceCreditMetadata(repository, metaInvoiceCreditList);

		/*final JCoListMetaData fmetaImport = JCo.createListMetaData("INVOICE_IMPORTS");
		fmetaImport.add("T_INVOICE_HEADER", JCoMetaData.TYPE_TABLE, metaInvoiceHeaderList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_INVOICE_ITEM", JCoMetaData.TYPE_TABLE, metaInvoiceItemList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();*/

		final JCoRecordMetaData metaInvoiceReturnRecord = JCo.createRecordMetaData("INVOICE_RETURN_LIST");
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("DEL_DATE", JCoMetaData.TYPE_CHAR, 10, "Delivery Date"));
		tableMetaDataList.add(new TableMetaData("INV_NO", JCoMetaData.TYPE_CHAR, 10, "Invoice No."));
		tableMetaDataList.add(new TableMetaData("WEB_ORD", JCoMetaData.TYPE_CHAR, 20, "Weborder No."));
		tableMetaDataList.add(new TableMetaData("ERROR", JCoMetaData.TYPE_CHAR, 220, "Error Message"));

		createTableRecord(metaInvoiceReturnRecord, tableMetaDataList);
		metaInvoiceReturnRecord.lock();
		repository.addRecordMetaDataToCache(metaInvoiceReturnRecord);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("INVOICE_IMPORTS");
		fmetaImport.add("T_INVOICE_HEADER", JCoMetaData.TYPE_TABLE, metaInvoiceHeaderList,JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_INVOICE_ITEM", JCoMetaData.TYPE_TABLE, metaInvoiceItemList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_INVOICE_CREDIT", JCoMetaData.TYPE_TABLE, metaInvoiceCreditList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_INVOICE_ERR", JCoMetaData.TYPE_TABLE, metaInvoiceReturnRecord,JCoListMetaData.EXPORT_PARAMETER);
		fmetaImport.lock();

		
		final JCoListMetaData fmetaExport = JCo.createListMetaData("INVOICE_EXPORTS");
		fmetaExport.add("T_INVOICE_ERR", JCoMetaData.TYPE_TABLE, metaInvoiceReturnRecord,JCoListMetaData.EXPORT_PARAMETER);
//		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null,null);
//		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER,null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null,
				fmetaImport, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	}

	/**
	 * @param repository
	 * @param metaInvoiceHeaderList
	 */
	private void createInvoiceHeaderMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaInvoiceHeaderList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("DEL_DATE", JCoMetaData.TYPE_CHAR, 10, "Delivery Date"));
		tableMetaDataList.add(new TableMetaData("INV_NUM", JCoMetaData.TYPE_CHAR, 10, "Billing Document"));
		tableMetaDataList.add(new TableMetaData("ORDER_NO", JCoMetaData.TYPE_CHAR, 10, "SAP Sales Order No."));
		tableMetaDataList.add(new TableMetaData("BILLING_TYPE", JCoMetaData.TYPE_CHAR, 4, "Billing Type"));
		tableMetaDataList.add(new TableMetaData("WEB_ORD", JCoMetaData.TYPE_CHAR, 20,"Customer purchase order number/Web Order "));
		tableMetaDataList.add(new TableMetaData("ORDER_STATUS", JCoMetaData.TYPE_CHAR, 1, "Order Status"));
		tableMetaDataList.add(new TableMetaData("INV_TOTAL", JCoMetaData.TYPE_CHAR, 15,"Invoice total before applying credit"));
		tableMetaDataList.add(new TableMetaData("INV_TAX", JCoMetaData.TYPE_CHAR, 15, "Invoice Tax amount"));
		tableMetaDataList.add(new TableMetaData("INV_BOT_DEP", JCoMetaData.TYPE_CHAR, 15, "Bottle Deposit"));
		tableMetaDataList.add(new TableMetaData("INV_SUB_TOTAL", JCoMetaData.TYPE_CHAR, 15, "Invoice Subtotal"));
		tableMetaDataList.add(new TableMetaData("CREDIT_AMOUNT", JCoMetaData.TYPE_CHAR, 15, "Credit Amount"));
		tableMetaDataList.add(new TableMetaData("INV_GROSS", JCoMetaData.TYPE_CHAR, 15,"Gross amount after applying credit"));
		
		// Start APPDEV-5319  As part of HRy SAP is no more going to pass below attributes 
		
		/* tableMetaDataList.add(new TableMetaData("ZZTRKNO", JCoMetaData.TYPE_CHAR, 6, "Truck Number"));
		tableMetaDataList.add(new TableMetaData("ZZSTOPSEQ", JCoMetaData.TYPE_CHAR, 5, "Stop Sequence"));
		tableMetaDataList.add(new TableMetaData("REG_CRTN", JCoMetaData.TYPE_CHAR, 3, "Regular cartons"));
		tableMetaDataList.add(new TableMetaData("FRZ_CRTN", JCoMetaData.TYPE_CHAR, 3, "Freezer cartons"));
		tableMetaDataList.add(new TableMetaData("ALC_CRTN", JCoMetaData.TYPE_CHAR, 3, "Alcohol cartons")); */
		
		//End APPDEV-5319
		
		tableMetaDataList.add(new TableMetaData("HDR_DISCOUNT", JCoMetaData.TYPE_CHAR, 15,"Actual discount at header level"));
		tableMetaDataList.add(new TableMetaData("ZZBMREF", JCoMetaData.TYPE_CHAR, 20, "Credit Memo Web Reference No."));
		tableMetaDataList.add(new TableMetaData("CREDIT_MEMO_NO", JCoMetaData.TYPE_CHAR, 10, "Credit memo No."));
		tableMetaDataList.add(new TableMetaData("CREDIT_MEMO_AMOUNT", JCoMetaData.TYPE_CHAR, 15, "Credit amount"));
		tableMetaDataList.add(new TableMetaData("CREDIT_MEMO_ORDER_NO", JCoMetaData.TYPE_CHAR, 10,"Credit memo Sales order No."));	
		tableMetaDataList.add(new TableMetaData("CREDIT_TYPE", JCoMetaData.TYPE_CHAR, 1,"Single-Character Indicator"));
		
		createTableRecord(metaInvoiceHeaderList, tableMetaDataList);
		metaInvoiceHeaderList.lock();
		repository.addRecordMetaDataToCache(metaInvoiceHeaderList);
	}

	/**
	 * @param repository
	 * @param metaInvoiceItemList
	 */
	private void createInvoiceLineMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaInvoiceItemList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("INV_NO", JCoMetaData.TYPE_CHAR, 10, "Invoice No."));
		tableMetaDataList.add(new TableMetaData("INV_LINE_NO", JCoMetaData.TYPE_CHAR, 6, "Invoice line item"));
		tableMetaDataList.add(new TableMetaData("MATERIAL_NO", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("INV_LINE_AMT", JCoMetaData.TYPE_CHAR, 15, "Invoice line item amount"));
		tableMetaDataList.add(new TableMetaData("INV_LINE_TAX", JCoMetaData.TYPE_CHAR, 15, "Invoice line tax"));
		tableMetaDataList.add(new TableMetaData("INV_LINE_BOT_DEP", JCoMetaData.TYPE_CHAR, 15, "Line Bottle Deposit"));
		tableMetaDataList.add(new TableMetaData("UNIT_PRICE", JCoMetaData.TYPE_CHAR, 15, "Unit Price"));
		tableMetaDataList.add(new TableMetaData("CUST_PRICE", JCoMetaData.TYPE_CHAR, 15, "Customization Price"));
		tableMetaDataList.add(new TableMetaData("SALES_UOM", JCoMetaData.TYPE_CHAR, 3, "Sales Unit of Measure"));
		tableMetaDataList.add(new TableMetaData("ORDER_QTY", JCoMetaData.TYPE_CHAR, 18, "Order Quantity"));
		tableMetaDataList.add(new TableMetaData("SHIPPED_QTY", JCoMetaData.TYPE_CHAR, 18, "Actual Shipped Qty"));
		tableMetaDataList.add(new TableMetaData("GROSS_WEIGHT", JCoMetaData.TYPE_CHAR, 13, "Gross Weight"));
		tableMetaDataList.add(new TableMetaData("WEIGHT_UNIT", JCoMetaData.TYPE_CHAR, 3, "Weight Unit"));
		tableMetaDataList.add(new TableMetaData("ORDER_LINE_STATUS", JCoMetaData.TYPE_CHAR, 1,"Processing Status of Order line"));
		tableMetaDataList.add(new TableMetaData("ORDER_NO", JCoMetaData.TYPE_CHAR, 10, "SAP Sales Order No."));
		tableMetaDataList
				.add(new TableMetaData("ACTUAL_COST", JCoMetaData.TYPE_CHAR, 15, "Actual Cost of the product"));
		tableMetaDataList.add(new TableMetaData("LINE_DISCOUNT", JCoMetaData.TYPE_CHAR, 15, "Order entry discount"));
		tableMetaDataList.add(new TableMetaData("ECOUP_DISCOUNT", JCoMetaData.TYPE_CHAR, 15, "E-Coupon discount"));
		tableMetaDataList.add(new TableMetaData("SHIP_STAT", JCoMetaData.TYPE_CHAR, 2, 0, "Shipping Status"));
		tableMetaDataList.add(new TableMetaData("SUB_WEBSKU", JCoMetaData.TYPE_CHAR, 18, "Substituted Material Web ID"));
	
		createTableRecord(metaInvoiceItemList, tableMetaDataList);
		metaInvoiceItemList.lock();
		repository.addRecordMetaDataToCache(metaInvoiceItemList);
	}

	/**
	 * @param repository
	 * @param metaInvoiceHeaderList
	 */
	private void createInvoiceCreditMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaInvoiceCreditList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("TYPE", JCoMetaData.TYPE_CHAR, 1,"Single-Character Indicator"));
		tableMetaDataList.add(new TableMetaData("INV_NUM", JCoMetaData.TYPE_CHAR, 10, "Billing Document"));
		tableMetaDataList.add(new TableMetaData("WEB_ORDER", JCoMetaData.TYPE_CHAR, 35,"Customer purchase order number/Web Order "));
		tableMetaDataList.add(new TableMetaData("ZZBMREF", JCoMetaData.TYPE_CHAR, 20, "Blue Martini Ref no."));
		tableMetaDataList.add(new TableMetaData("CREDIT_MEMO_NO", JCoMetaData.TYPE_CHAR, 10,"Credit memo Sales order No."));
		tableMetaDataList.add(new TableMetaData("CREDIT_AMOUNT", JCoMetaData.TYPE_CHAR, 15, "Credit amount"));
		tableMetaDataList.add(new TableMetaData("ORDER_NO", JCoMetaData.TYPE_CHAR, 10, "SAP Sales Order No."));
		
		createTableRecord(metaInvoiceCreditList, tableMetaDataList);
		metaInvoiceCreditList.lock();
		repository.addRecordMetaDataToCache(metaInvoiceCreditList);
	}
	
	@Override
	protected FDSapFunctionHandler getHandler() {
		return new FDConnectionHandler();
	}

	protected class FDConnectionHandler extends FDSapFunctionHandler implements JCoServerFunctionHandler {
		private JCoTable invoiceErrorTable;

		@Override
		public String getFunctionName() {
			return functionName;
		}

		@Override
        public void handleRequest(final JCoServerContext serverCtx, final JCoFunction function) {
			final JCoParameterList exportParamList = function.getExportParameterList();
			final FDJcoServerResult result = new FDJcoServerResult();
			try {
				final JCoTable invoiceHeaderTable = function.getTableParameterList().getTable("T_INVOICE_HEADER");
				final JCoTable invoiceLineTable = function.getTableParameterList().getTable("T_INVOICE_ITEM");
				final JCoTable invoiceCreditTable = function.getTableParameterList().getTable("T_INVOICE_CREDIT");

				invoiceErrorTable = function.getTableParameterList().getTable("T_INVOICE_ERR");
				
				if(SapProperties.isInvoiceExportLogEnabled()){
					LOG.info("******************* Invoice Export - Header ************");
					LOG.info(invoiceHeaderTable);
					LOG.info("******************* Invoice Export - Line Items************");
					LOG.info(invoiceLineTable);
					LOG.info("******************* Invoice Export - Credits************");
					LOG.info(invoiceCreditTable);
				}

				final int successCnt = 0;

				// Invoice No. -> Invoice Header
				final Map<String, InvoiceHeaderParameter> invoiceMap = new HashMap<String, InvoiceHeaderParameter>();
				
							
				if (invoiceHeaderTable != null && invoiceLineTable != null) {
					// invoice header
					for (int i = 0; i < invoiceHeaderTable.getNumRows(); i++) {
						invoiceHeaderTable.setRow(i);

						final InvoiceHeaderParameter invoiceHeaderParam = populateInvoiceHeaderRecord(invoiceHeaderTable);
						if (invoiceMap.containsKey(invoiceHeaderParam.getInvoiceNo())) {
							LOG.error("Invoice batch contains duplicate invoice header(s) with invoice No. { "
									+ invoiceHeaderParam.getInvoiceNo() + " }, " + "order No. { "
									+ invoiceHeaderParam.getWebOrderNo() + " }");

							populateResponseRecord(result, invoiceHeaderParam,
									"Duplicate invoice for the order in the batch");
						} else {
							invoiceMap.put(invoiceHeaderParam.getInvoiceNo(), invoiceHeaderParam);
						}

						invoiceHeaderTable.nextRow();
					}

					// invoice line item
					for (int i = 0; i < invoiceLineTable.getNumRows(); i++) {
						invoiceLineTable.setRow(i);

						final InvoiceEntryParameter invoiceEntryParam = populateInvoiceLineRecord(invoiceLineTable);
						if (invoiceMap.containsKey(invoiceEntryParam.getInvoiceNo())) {
							if (invoiceMap.get(invoiceEntryParam.getInvoiceNo()).getEntries() == null) {
								invoiceMap.get(invoiceEntryParam.getInvoiceNo()).setEntries(
										new ArrayList<InvoiceEntryParameter>());
							}
							invoiceMap.get(invoiceEntryParam.getInvoiceNo()).getEntries().add(invoiceEntryParam);
						} else {
							LOG.warn("Invoice batch contains invoice line(s) with no invoice header for invoice No. { "
									+ invoiceEntryParam.getInvoiceNo() + " }, " + "SAP Order No. { "
									+ invoiceEntryParam.getSalesOrderNo() + " }");
						}
						invoiceLineTable.nextRow();
					}
					
					// invoice credit lines
					for (int i = 0; i < invoiceCreditTable.getNumRows(); i++) {
						invoiceCreditTable.setRow(i);

						final InvoiceCreditParameter invoiceCreditParam = populateInvoiceCreditRecord(invoiceCreditTable);
						if (invoiceMap.containsKey(invoiceCreditParam.getInvoiceNo())) {
							if (invoiceMap.get(invoiceCreditParam.getInvoiceNo()).getCreditEntries() == null) {
								invoiceMap.get(invoiceCreditParam.getInvoiceNo()).setCreditEntries(new ArrayList<InvoiceCreditParameter>());
							}
							invoiceMap.get(invoiceCreditParam.getInvoiceNo()).getCreditEntries().add(invoiceCreditParam);
						} else {
							LOG.warn("Invoice batch contains invoice credit(s) with no invoice header for invoice No. { "
									+ invoiceCreditParam.getInvoiceNo() + " }, " + "SAP Order No. { "
									+ invoiceCreditParam.getSalesOrderNo() + " }");
						}
						invoiceCreditTable.nextRow();
					}

					processInvoiceForOrders(result, invoiceMap, successCnt);

//					exportParamList.setValue("RETURN",(FDJcoServerResult.OK_STATUS.equals(result.getStatus()) && invoiceHeaderTable
//											.getNumRows() == successCnt) ? "S" : "W");
//					exportParamList.setValue("MESSAGE",	String.format("%s Invoice(s) imported successfully!, [ %s ]", successCnt, new Date()));
					exportParamList.setValue("T_INVOICE_ERR", invoiceErrorTable);
				}
			} catch (final Exception e) {
				LOG.error("Error importing invoice details: ", e);
				/*exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE", e.toString().substring(0, Math.min(255, e.toString().length())));*/
			}
		}

		/**
		 * @param invoiceMap
		 * @param result
		 * @throws LoaderException
		 */
		@SuppressWarnings("unchecked")
		private void processInvoiceForOrders(final FDJcoServerResult result,
				final Map<String, InvoiceHeaderParameter> invoiceMap, int successCnt) throws LoaderException {
			InvoiceLoaderSB sb;
			try {
				if (invoiceLoaderHome == null) {
					lookupInvoiceLoaderHome();
				}
				sb = invoiceLoaderHome.create();
			} catch (CreateException ce) {
				throw new LoaderException(ce);
			} catch (RemoteException re) {
				throw new LoaderException(re);
			}

			if (invoiceMap != null) {
				// Invoice No. -> Invoice
				for (final Map.Entry<String, InvoiceHeaderParameter> invoiceMapEntry : invoiceMap.entrySet()) {
					final InvoiceHeaderParameter param = invoiceMapEntry.getValue();
					try {
						LOG.info("Processing invoice for the order { " + param.getWebOrderNo() + " }");

						FDOrderI order =null;
						try {
							order = FDCustomerManager.getOrder(param.getWebOrderNo());
						} catch (Exception e) {
							LOG.warn("Exception while fetching the order: { " + param.getWebOrderNo() + " }"+e);
						}

						if (order == null) {
							LOG.warn("No order found with the order {" + param.getWebOrderNo() + "} to add invoice {"
									+ param.getInvoiceNo() + "} ");
							populateResponseRecord(result, param, "No order found for the web order");
							continue;
						}

						//To fix - APPDEV-5294 - Duplicate invoice export for an order from SAP, should be ignored and return success to SAP.
						/*if (!EnumSaleStatus.INPROCESS.equals(order.getOrderStatus())) {
							populateResponseRecord(result, param, "Order is in ["+order.getOrderStatus()+"], not in correct status [PRC] to add invoice");
							continue;
						}*/

						// START APPDEV-5319 : SAP is not going to pass below attributes as part of HRY
						/* ErpShippingInfo shippingInfo =  new ErpShippingInfo(param.getTruckNumber(),
								param.getStopSequence(), param.getRegularCartonCnt(), param.getFreezerCartonCnt(),
								param.getAlcoholCartonCnt()); */
                       
						ErpShippingInfo shippingInfo = null;
						// End APPDEV-5319
						
						ErpInvoiceModel invoice = new ErpInvoiceModel();

						// 1. set HEADER info
						invoice.setAmount(param.getInvoiceTotal());
						invoice.setInvoiceNumber(param.getInvoiceNo());
						invoice.setSubTotal(param.getInvoiceSubTotal());
						invoice.setTax(param.getInvoiceTax());
						invoice.setTransactionDate(new Date(System.currentTimeMillis()));
						invoice.setTransactionSource(EnumTransactionSource.SYSTEM);

						// 2. set Applied credits
						if(null !=param.getCreditEntries())
						for(final InvoiceCreditParameter creditEnty: param.getCreditEntries()){
							if(null != creditEnty){
								ErpInvoicedCreditModel credit = new ErpInvoicedCreditModel();
	
								credit.setAmount(creditEnty.getCreditAmount());
								credit.setOriginalCreditId(creditEnty.getCreditWebReferenceNo());
	
								if (invoice.getAmount() > 0 && StringUtils.isEmpty(creditEnty.getCreditMemoNo())) {
									LOG.warn("Credit memo number required for non-zero invoices: order {"
											+ creditEnty.getWebOrderNo() + "}, invoice {" + creditEnty.getInvoiceNo() + "} ");
	
									populateResponseRecord(result, param,
											"Credit memo number required for non-zero invoices");
									continue;
								}
								credit.setSapNumber(creditEnty.getCreditMemoNo());
								invoice.addAppliedCredit(credit);
							}
						}
						/*if (param.getCreditMemoNo() != null && param.getCreditAmount() > 0) {
							ErpInvoicedCreditModel credit = new ErpInvoicedCreditModel();

							credit.setAmount(param.getCreditAmount());
							credit.setOriginalCreditId(param.getCreditWebReferenceNo());

							if (invoice.getAmount() > 0 && StringUtils.isEmpty(param.getCreditMemoNo())) {
								LOG.warn("Credit memo number required for non-zero invoices: order {"
										+ param.getWebOrderNo() + "}, invoice {" + param.getInvoiceNo() + "} ");

								populateResponseRecord(result, param,
										"Credit memo number required for non-zero invoices");
								continue;
							}
							credit.setSapNumber(param.getCreditMemoNo());
							invoice.addAppliedCredit(credit);
						}*/

						List<ErpInvoiceLineModel> invoiceLines = new ArrayList<ErpInvoiceLineModel>();
						invoice.setInvoiceLines(invoiceLines);

						// 3. add charges
						addCharges(order, invoice);

						// 4. set Invoice lines
						for (final InvoiceEntryParameter entry : param.getEntries()) {
							// 1. CHARGE lines are ignored sent by SAP. Will use
							// the one's on the order
							boolean isChargeEntry = false;
							if (entry.getMaterialNumber() != null) {
								for (Iterator<EnumChargeType> i = EnumChargeType.getEnumList().iterator(); i.hasNext();) {
									EnumChargeType chargeType = i.next();
									if (entry.getMaterialNumber().equalsIgnoreCase(chargeType.getMaterialNumber())) {
										isChargeEntry = true;
										break;
									}
								}

								if (isChargeEntry) {
									continue;
								}

								// 2. DISCOUNT lines at header level. SAP deals
								// promotion(s) as line items. To get sub-total
								// correct, adding back promotion value to order
								// sub-total
								if (FDSapHelperUtils.PROMOTION_MATERIAL_NO.equals(entry.getMaterialNumber())) {
									Discount discount = new Discount("UNKNOWN", EnumDiscountType.DOLLAR_OFF,
											Math.abs(entry.getAmount()));
									invoice.addDiscount(new ErpDiscountLineModel(discount));
									invoice.setSubTotal(invoice.getSubTotal() + (Math.abs(entry.getAmount())));
								} else {
									ErpInvoiceLineModel invoiceLine = new ErpInvoiceLineModel();

									invoiceLine.setPrice(entry.getAmount() + entry.getCustomizationPrice());
									invoiceLine.setCustomizationPrice(entry.getCustomizationPrice());
									invoiceLine.setTaxValue(entry.getTaxAmt());
									invoiceLine.setDepositValue(entry.getBottleDepositAmount());
									invoiceLine.setQuantity(entry.getShippedQuantity());
									invoiceLine.setMaterialNumber(entry.getMaterialNumber());
									invoiceLine.setOrderLineNumber(entry.getInvoiceLineNo());
									if (entry.getGrossWeight() > 0.0) {
										invoiceLine.setWeight(entry.getGrossWeight());
									}
									if (entry.getActualCost() != null) {
										invoiceLine.setActualCost(Double.parseDouble(entry.getActualCost()));
									}
									invoiceLine.setActualDiscountAmount(entry.getDiscount());
									invoiceLine.setCouponDiscountAmount(entry.getCouponDiscount());
									invoiceLine.setSubSkuStatus(entry.getSubSkuStatus());
									invoiceLine.setSubstitutedSkuCode(entry.getSubstitutedSkuCode());
									invoiceLines.add(invoiceLine);
								}
							}
						}

						sb.addAndReconcileInvoice(param.getWebOrderNo(), invoice, shippingInfo);

						successCnt = successCnt + 1;

						LOG.info(String.format(
								"%s Invoice entry(s) added for the sales order [" + param.getWebOrderNo() + "]", param
										.getEntries().size()));

					} catch (ErpTransactionException e) {
						LOG.error("Adding invoice details for the order [" + param.getWebOrderNo()
								+ "] failed. Exception is ", e);
						populateResponseRecord(result, param, e.getMessage());
					} catch (RemoteException e) {
						LOG.error("Adding invoice details for the order [" + param.getWebOrderNo()
								+ "] failed. Exception is ", e);
						populateResponseRecord(result, param, e.getMessage());
					} catch (final Exception e) {
						LOG.error("Adding invoice details for the order [" + param.getWebOrderNo()
								+ "] failed. Exception is ", e);
						populateResponseRecord(result, param, e.getMessage());
					}
				}
			}
		}



        /**
         * Method to log the failure records back to ERP system
         * 
         * @param param
         * @param invoiceErrorTable
         * @param errorMessage
         */
		private void populateResponseRecord(final FDJcoServerResult result, final InvoiceHeaderParameter param,
				final String errorMessage) {
			if (param != null && invoiceErrorTable != null) {
				invoiceErrorTable.appendRow();
				invoiceErrorTable.setValue("DEL_DATE", param.getDeliveryDate());
				invoiceErrorTable.setValue("INV_NO", param.getInvoiceNo());
				invoiceErrorTable.setValue("WEB_ORD", param.getWebOrderNo());
				invoiceErrorTable.setValue("ERROR", errorMessage);
			}

			if (result != null) {
				result.addError(param.getWebOrderNo(), errorMessage);
			}
		}
	}

	/**
	 * @param invoiceHeaderTable
	 * @return the invoice header
	 */
	private InvoiceHeaderParameter populateInvoiceHeaderRecord(final JCoTable invoiceHeaderTable) {
		final InvoiceHeaderParameter param = new InvoiceHeaderParameter();

		param.setDeliveryDate(FDSapHelperUtils.getString(invoiceHeaderTable.getString("DEL_DATE")));
		param.setWebOrderNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("WEB_ORD")));
		param.setInvoiceNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("INV_NUM")));
		param.setInvoiceSubTotal(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("INV_SUB_TOTAL")));
		param.setInvoiceTotal(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("INV_GROSS")));
		param.setInvoiceTax(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("INV_TAX")));
		param.setBottleDepositAmount(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("INV_BOT_DEP")));

		param.setBillingType(FDSapHelperUtils.getString(invoiceHeaderTable.getString("BILLING_TYPE")));

		param.setSalesOrderNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("ORDER_NO")));

		// Start APPDEV-5319  As part of HRy SAP is no more going to pass below attributes 
		
		/* param.setTruckNumber(FDSapHelperUtils.getString(invoiceHeaderTable.getString("ZZTRKNO")));
		param.setStopSequence(FDSapHelperUtils.getString(invoiceHeaderTable.getString("ZZSTOPSEQ")));
		param.setRegularCartonCnt(FDSapHelperUtils.getInt(invoiceHeaderTable.getString("REG_CRTN")));
		param.setFreezerCartonCnt(FDSapHelperUtils.getInt(invoiceHeaderTable.getString("FRZ_CRTN")));
		param.setAlcoholCartonCnt(FDSapHelperUtils.getInt(invoiceHeaderTable.getString("ALC_CRTN"))); */
        
		//End APPDEV-5319
		param.setHeaderDiscount(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("HDR_DISCOUNT")));

		/*param.setCreditWebReferenceNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("ZZBMREF")));
		param.setCreditMemoNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("CREDIT_MEMO_NO")));
		param.setCreditAmount(FDSapHelperUtils.getDouble(invoiceHeaderTable.getString("CREDIT_MEMO_AMOUNT")));
		param.setCreditMemoSalesOrderNo(FDSapHelperUtils.getString(invoiceHeaderTable.getString("CREDIT_MEMO_ORDER_NO")));*/

		return param;
	}

	/**
	 * @param invoiceLineTable
	 * @return the invoice line item record
	 */
	private InvoiceEntryParameter populateInvoiceLineRecord(final JCoTable invoiceLineTable) {
		final InvoiceEntryParameter param = new InvoiceEntryParameter();

		param.setInvoiceNo(FDSapHelperUtils.getString(invoiceLineTable.getString("INV_NO")));
		param.setInvoiceLineNo(FDSapHelperUtils.getString(invoiceLineTable.getString("INV_LINE_NO")));
		param.setMaterialNumber(FDSapHelperUtils.getString(invoiceLineTable.getString("MATERIAL_NO")));
		String invoiceAmt = invoiceLineTable.getString("INV_LINE_AMT");
		if(null != invoiceAmt){
			invoiceAmt = invoiceAmt.replace("-", "");
		}
		param.setAmount(FDSapHelperUtils.getDouble(invoiceAmt));
		param.setTaxAmt(FDSapHelperUtils.getDouble(invoiceLineTable.getString("INV_LINE_TAX")));
		param.setBottleDepositAmount(FDSapHelperUtils.getDouble(invoiceLineTable.getString("INV_LINE_BOT_DEP")));

		param.setUnitPrice(FDSapHelperUtils.getDouble(invoiceLineTable.getString("UNIT_PRICE")));
		param.setCustomizationPrice(FDSapHelperUtils.getDouble(invoiceLineTable.getString("CUST_PRICE")));

		param.setSalesUOMCode(FDSapHelperUtils.getString(invoiceLineTable.getString("SALES_UOM")));

		param.setOrderQuantity(FDSapHelperUtils.getDouble(invoiceLineTable.getString("ORDER_QTY")));
		param.setShippedQuantity(FDSapHelperUtils.getDouble(invoiceLineTable.getString("SHIPPED_QTY")));
		param.setWeightUnitCode(FDSapHelperUtils.getString(invoiceLineTable.getString("WEIGHT_UNIT")));
		param.setGrossWeight(FDSapHelperUtils.getDouble(invoiceLineTable.getString("GROSS_WEIGHT")));

		param.setStatus(FDSapHelperUtils.getString(invoiceLineTable.getString("ORDER_LINE_STATUS")));
		param.setSalesOrderNo(FDSapHelperUtils.getString(invoiceLineTable.getString("ORDER_NO")));

		param.setActualCost(FDSapHelperUtils.getString(invoiceLineTable.getString("ACTUAL_COST")));
		param.setDiscount(FDSapHelperUtils.getDouble(invoiceLineTable.getString("LINE_DISCOUNT")));
		param.setCouponDiscount(FDSapHelperUtils.getDouble(invoiceLineTable.getString("ECOUP_DISCOUNT")));
		param.setSubSkuStatus(FDSapHelperUtils.getString(invoiceLineTable.getString("SHIP_STAT")));
		param.setSubstitutedSkuCode(FDSapHelperUtils.getString(invoiceLineTable.getString("SUB_WEBSKU")));
	
		return param;
	}
	
	/**
	 * @param invoiceCreditTable
	 * @return the invoice credit item record
	 */
	private InvoiceCreditParameter populateInvoiceCreditRecord(final JCoTable invoiceCreditTable) {
		final InvoiceCreditParameter param = new InvoiceCreditParameter();

		param.setTypeIndicator(FDSapHelperUtils.getString(invoiceCreditTable.getString("TYPE")));
		param.setInvoiceNo(FDSapHelperUtils.getString(invoiceCreditTable.getString("INV_NUM")));
		param.setWebOrderNo(FDSapHelperUtils.getString(invoiceCreditTable.getString("WEB_ORDER")));
		param.setSalesOrderNo(FDSapHelperUtils.getString(invoiceCreditTable.getString("ORDER_NO")));
		param.setCreditWebReferenceNo(FDSapHelperUtils.getString(invoiceCreditTable.getString("ZZBMREF")));
		param.setCreditMemoNo(FDSapHelperUtils.getString(invoiceCreditTable.getString("CREDIT_MEMO_NO")));
		param.setCreditAmount(FDSapHelperUtils.getDouble(invoiceCreditTable.getString("CREDIT_AMOUNT")));
		
		return param;
	}

	/**
	 * @param order
	 * @param invoice
	 */
	private void addCharges(FDOrderI order, ErpInvoiceModel invoice) {
		List<ErpChargeLineModel> charges = new ArrayList<ErpChargeLineModel>();

		double chargeAmount = 0;

		for (Iterator<ErpChargeLineModel> i = order.getCharges().iterator(); i.hasNext();) {
			ErpChargeLineModel charge = new ErpChargeLineModel(i.next());
			if (invoice.getTax() == 0) {
				charge.setTaxRate(0);
			}
			chargeAmount += charge.getTotalAmount();
			charges.add(charge);
		}
		invoice.setSubTotal(invoice.getSubTotal() - chargeAmount);
		invoice.setCharges(charges);
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	private void lookupInvoiceLoaderHome() throws EJBException {
		Context ctx = null;
		try {
			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
			ctx = new InitialContext(h);

			this.invoiceLoaderHome = (InvoiceLoaderHome) ctx.lookup("freshdirect.dataloader.InvoiceLoader");
		} catch (NamingException ex) {
			LOG.debug(ex);
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				LOG.debug(ne);
			}
		}
	}

}
