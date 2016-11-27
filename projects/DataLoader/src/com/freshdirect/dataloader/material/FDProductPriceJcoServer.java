package com.freshdirect.dataloader.material;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialPriceParameter;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialPromoPriceParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ejb.ErpMaterialEB;
import com.freshdirect.erp.ejb.ErpMaterialHome;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
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
 * Created by kkanuganti on 11/2/14.
 */
public class FDProductPriceJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDProductPriceJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDProductPriceJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDProductPriceRepository");

		final JCoRecordMetaData metaMaterialPriceList = JCo.createRecordMetaData("MAT_PRICE_LIST");

		tableMetaDataList.add(new TableMetaData("KUNNR", JCoMetaData.TYPE_CHAR, 10, "Customer Number"));
		tableMetaDataList.add(new TableMetaData("ZONEID", JCoMetaData.TYPE_CHAR, 6, "Zone ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		// tableMetaDataList.add(new TableMetaData("KUNNR",
		// JCoMetaData.TYPE_CHAR, 10, "Customer No."));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, "Material Description"));
		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("PRITY", JCoMetaData.TYPE_CHAR, 10, "Price Type"));
		// tableMetaDataList.add(new TableMetaData("QUANT",
		// JCoMetaData.TYPE_NUM, 13, 2, "Quantity"));
		tableMetaDataList.add(new TableMetaData("QUANT", JCoMetaData.TYPE_CHAR, 5, "Quantity"));
		tableMetaDataList.add(new TableMetaData("KONMS", JCoMetaData.TYPE_CHAR, 3, "Scale Unit"));
		tableMetaDataList.add(new TableMetaData("MEINS", JCoMetaData.TYPE_CHAR, 3, "Unit of Measure"));
		// tableMetaDataList.add(new TableMetaData("PRICE",
		// JCoMetaData.TYPE_NUM, 11, 2, "Price"));
		tableMetaDataList.add(new TableMetaData("PRICE", JCoMetaData.TYPE_CHAR, 15, "Price"));
		tableMetaDataList.add(new TableMetaData("CURNCY", JCoMetaData.TYPE_CHAR, 5, "Currency"));
		tableMetaDataList.add(new TableMetaData("SALEUN", JCoMetaData.TYPE_CHAR, 3, "Sales Unit"));
		tableMetaDataList.add(new TableMetaData("VLDFR", JCoMetaData.TYPE_DATE, 10, "Valid From date"));
		tableMetaDataList.add(new TableMetaData("VLDTO", JCoMetaData.TYPE_DATE, 10, "Valid To date"));
		tableMetaDataList.add(new TableMetaData("KNUMH", JCoMetaData.TYPE_CHAR, 10, "Condition Record Number"));

		createTableRecord(metaMaterialPriceList, tableMetaDataList);
		metaMaterialPriceList.lock();
		repository.addRecordMetaDataToCache(metaMaterialPriceList);

		/*
		 * final JCoListMetaData fmetaImport =
		 * JCo.createListMetaData("MATERIAL_PRICE_IMPORTS");
		 * fmetaImport.add("T_PRICE", JCoMetaData.TYPE_TABLE,
		 * metaMaterialPriceList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.lock();
		 */

		final JCoRecordMetaData metaMaterialPriceReturnList = JCo.createRecordMetaData("MAT_PRICERETURN_LIST");

		tableMetaDataList = new ArrayList<TableMetaData>();
		tableMetaDataList.add(new TableMetaData("KUNNR", JCoMetaData.TYPE_CHAR, 10, "Customer No."));
		tableMetaDataList.add(new TableMetaData("ZONEID", JCoMetaData.TYPE_CHAR, 6, "Zone ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, "Material Description"));
		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("PRITY", JCoMetaData.TYPE_CHAR, 10, "Price Type")); // condition
																									// type
		// tableMetaDataList.add(new TableMetaData("QUANT",
		// JCoMetaData.TYPE_NUM, 13, 2, "Quantity"));
		tableMetaDataList.add(new TableMetaData("QUANT", JCoMetaData.TYPE_CHAR, 5, "Quantity"));
		tableMetaDataList.add(new TableMetaData("KONMS", JCoMetaData.TYPE_CHAR, 3, "Scale Unit"));
		tableMetaDataList.add(new TableMetaData("MEINS", JCoMetaData.TYPE_CHAR, 3, "Unit of Measure"));
		// tableMetaDataList.add(new TableMetaData("PRICE",
		// JCoMetaData.TYPE_NUM, 11, 2, "Price"));
		tableMetaDataList.add(new TableMetaData("PRICE", JCoMetaData.TYPE_CHAR, 15, "Price"));
		tableMetaDataList.add(new TableMetaData("CURNCY", JCoMetaData.TYPE_CHAR, 5, "Currency"));
		tableMetaDataList.add(new TableMetaData("SALEUN", JCoMetaData.TYPE_CHAR, 3, "Sales Unit"));
		tableMetaDataList.add(new TableMetaData("VLDFR", JCoMetaData.TYPE_DATE, 10, "Valid From date"));
		tableMetaDataList.add(new TableMetaData("VLDTO", JCoMetaData.TYPE_DATE, 10, "Valid To date"));
		tableMetaDataList.add(new TableMetaData("KNUMH", JCoMetaData.TYPE_CHAR, 10, "Condition Record Number"));
		tableMetaDataList.add(new TableMetaData("ERROR", JCoMetaData.TYPE_CHAR, 220, "Error message"));

		createTableRecord(metaMaterialPriceReturnList, tableMetaDataList);
		metaMaterialPriceReturnList.lock();
		repository.addRecordMetaDataToCache(metaMaterialPriceReturnList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("MATERIAL_PRICE_IMPORTS");
		fmetaImport.add("T_PRICE", JCoMetaData.TYPE_TABLE, metaMaterialPriceList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_ERROR", JCoMetaData.TYPE_TABLE, metaMaterialPriceReturnList,
				JCoListMetaData.EXPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("MATERIAL_PRICE_EXPORTS");
		fmetaExport.add("T_ERROR", JCoMetaData.TYPE_TABLE, metaMaterialPriceReturnList,
				JCoListMetaData.EXPORT_PARAMETER);
		// fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null,
		// JCoListMetaData.EXPORT_PARAMETER, null, null);
		// fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null,
		// null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null,
				fmetaImport, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	}

	@Override
	protected FDSapFunctionHandler getHandler() {
		return new FDConnectionHandler();
	}

	protected class FDConnectionHandler extends FDSapFunctionHandler implements JCoServerFunctionHandler {
		@Override
		public String getFunctionName() {
			return functionName;
		}

		public void handleRequest(final JCoServerContext serverCtx, final JCoFunction function) {
			final JCoParameterList exportParamList = function.getExportParameterList();
			try {
				final JCoTable materialPriceTable = function.getTableParameterList().getTable("T_PRICE");
				final JCoTable materialPriceErrorTable = function.getTableParameterList().getTable("T_ERROR");
				
				if(SapProperties.isMaterialPriceExportLogEnabled()){
					LOG.info("******************* Material Price Data ************");
					LOG.info(materialPriceTable);
				}
				
				final FDJcoServerResult result = new FDJcoServerResult();
				final int successCnt = 0;

				if (materialPriceTable != null) {
					final Map<String, List<MaterialPromoPriceParameter>> promoPriceRecordMap = new HashMap<String, List<MaterialPromoPriceParameter>>();
					final Map<String, List<MaterialPriceParameter>> priceRecordMap = new HashMap<String, List<MaterialPriceParameter>>();

					for (int i = 0; i < materialPriceTable.getNumRows(); i++) {
						materialPriceTable.setRow(i);

						final String matNo = FDSapHelperUtils.getString(materialPriceTable.getString("MATNR"));
						if (FDSapHelperUtils.PROMO_PRICE_ROW_INDICATOR.equals(FDSapHelperUtils
								.getString(materialPriceTable.getString("PRITY")))) {
							if (!promoPriceRecordMap.containsKey(matNo)) {
								promoPriceRecordMap.put(matNo, new ArrayList<MaterialPromoPriceParameter>());
							}
							promoPriceRecordMap.get(matNo).add(populateMaterialPromoPriceRecord(materialPriceTable));
						} else {
							if (!priceRecordMap.containsKey(matNo)) {
								priceRecordMap.put(matNo, new ArrayList<MaterialPriceParameter>());
							}
							priceRecordMap.get(matNo).add(populateMaterialPriceRecord(materialPriceTable));
						}

						materialPriceTable.nextRow();
					}

					populateProductPricing(result, materialPriceErrorTable, promoPriceRecordMap, priceRecordMap,
							successCnt);
				}

				// exportParamList.setValue("RETURN",
				// (FDJcoServerResult.OK_STATUS.equals(result.getStatus()) &&
				// materialPriceTable
				// .getNumRows() == successCnt) ? "S" : "W");
				// exportParamList.setValue("MESSAGE",
				// String.format("%s Product price row(s) imported successfully! [ %s ]! [ %s ]",
				// successCnt, new Date()));
				exportParamList.setValue("T_ERROR", materialPriceErrorTable);

				// email failure report
				emailFailureReport(result, "Product price", null);
			} catch (final Exception e) {
				LOG.error("Error importing price row(s): ", e);
				// exportParamList.setValue("RETURN", "E");
				// exportParamList.setValue("MESSAGE",
				// "Error importing price row(s) " + e.toString().substring(0,
				// Math.min(230, e.toString().length())));
			}
		}

		/**
		 * Method to populate/save price rows for a product for all pricing
		 * zones, sales org & dist channel
		 * 
		 * @param materialPriceErrorTable
		 * @param promoPriceRecordMap
		 * @param priceRecordMap
		 * 
		 * @throws CreateException
		 * @throws RemoteException
		 * @throws EJBException
		 */
		@SuppressWarnings("unused")
		private void populateProductPricing(final FDJcoServerResult result, final JCoTable materialPriceErrorTable,
				final Map<String, List<MaterialPromoPriceParameter>> promoPriceRecordMap,
				final Map<String, List<MaterialPriceParameter>> priceRecordMap, int successCnt) throws EJBException,
				RemoteException, CreateException, LoaderException {
			int batchNumber = 0;
			Context initCtx = null;
			try {
				initCtx = com.freshdirect.ErpServicesProperties.getInitialContext();

				ErpMaterialHome materialHome = (ErpMaterialHome) initCtx
						.lookup(ErpServicesProperties.getMaterialHome());

				SAPLoaderHome home = (SAPLoaderHome) initCtx.lookup("freshdirect.dataloader.SAPLoader");
				SAPLoaderSB sapLoader = home.create();

				// create a new batch
				batchNumber = sapLoader.createBatch();

				for (final Map.Entry<String, List<MaterialPriceParameter>> priceRecordEntry : priceRecordMap.entrySet()) {
					List<ErpMaterialPriceModel> priceRows = new ArrayList<ErpMaterialPriceModel>();
					String materialNo = priceRecordEntry.getKey();
					try {
						if (priceRecordEntry.getValue().size() > 0) {
							for (final MaterialPriceParameter priceRecord : priceRecordEntry.getValue()) {
								// 1. check if base (global) material exists
								try {
									ErpMaterialEB materialEB = materialHome.findBySapId(materialNo);

									ErpMaterialModel erpMaterialModel = (ErpMaterialModel) materialEB.getModel();
								} catch (FinderException fe) {
									populateResponseRecord(result, priceRecord, materialPriceErrorTable,
											"No base product found for the material");
									break;
								}

								// 2. check if master pricing zone exists

								// 3. check if regular price is zero or negative
								if (priceRecord.getPrice() <= 0) {
									populateResponseRecord(result, priceRecord, materialPriceErrorTable, String.format(
											"Regular price cannot be zero", priceRecordEntry.getKey(),
											priceRecord.getZoneId()));
									break;
								}
								if(null==priceRecord.getSapId()||"".equalsIgnoreCase(priceRecord.getSapId())){
									populateResponseRecord(result, priceRecord, materialPriceErrorTable, String.format(
											"Condition record number is empty", priceRecordEntry.getKey(),
											priceRecord.getZoneId()));
									break;
								}

								ErpMaterialPriceModel priceRow = new ErpMaterialPriceModel();
								priceRows.add(priceRow);

								priceRow.setSalesOrg(priceRecord.getSalesOrganizationId());
								priceRow.setDistChannel(priceRecord.getDistributionChannelId());
								priceRow.setPrice(Double.valueOf(priceRecord.getPrice()));
								priceRow.setSapId(priceRecord.getSapId());
								priceRow.setPricingUnit(priceRecord.getPricingUnitCode());
								priceRow.setSapZoneId("0000"+priceRecord.getZoneId());//10 digit format for storefront.
								priceRow.setScaleUnit(priceRecord.getScaleUnitCode());
								priceRow.setScaleQuantity(Long.valueOf(priceRecord.getScaleQuanity()));

								// 4. find promo price
								final double promoPrice = findPromoPrice(priceRecord, promoPriceRecordMap);
								if (promoPrice > 0) {
									LOG.debug("Setting the promo price " + Double.valueOf(promoPrice)
											+ " for material: " + priceRecord.getMaterialID() + ", salesarea: "
											+ priceRecord.getSalesOrganizationId()
											+ priceRecord.getDistributionChannelId() + ", pricingzone: "
											+ priceRecord.getZoneId());

									priceRow.setPromoPrice(Double.valueOf(promoPrice));
								}
							}
						}

						if (priceRows.size() > 0) {
							// check for default price row per salesarea
							checkIfDefaultPriceRowExists(priceRows);

							sapLoader.loadPriceRows(batchNumber, materialNo, priceRows);

							successCnt = successCnt + priceRecordEntry.getValue().size();

							LOG.info(String.format(
									"%s Price rows updated for the material: " + priceRecordEntry.getKey(),
									priceRecordEntry.getValue().size()));
						}

						// mark the batch status
						sapLoader.updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);
					} catch (final Exception e) {
						LOG.error("Saving price row(s) for material: " + priceRecordEntry.getKey()
								+ " failed. Exception is ", e);
						for (final MaterialPriceParameter priceRecord : priceRecordEntry.getValue()) {
							populateResponseRecord(result, priceRecord, materialPriceErrorTable, e.toString());
						}
					}
				}
			} catch (NamingException ne) {
				LOG.warn("Failed to look up session bean home", ne);
			} finally {
				try {
					if (initCtx != null)
						initCtx.close();
				} catch (NamingException ne) {
					LOG.warn("Error closing naming context", ne);
				}
			}
		}

		/**
		 * This method determines whether a default price row with default
		 * pricing zone exists within the salesarea.
		 * 
		 * @param priceRows
		 * @throws LoaderException
		 */
		private void checkIfDefaultPriceRowExists(final List<ErpMaterialPriceModel> priceRows) throws LoaderException {
			Map<String, List<ErpMaterialPriceModel>> salesAreaPriceMap = new HashMap<String, List<ErpMaterialPriceModel>>();
			final StringBuffer bufferStr = new StringBuffer();
			for (ErpMaterialPriceModel priceRowModel : priceRows) {
				String salesArea = priceRowModel.getSalesOrg() + priceRowModel.getDistChannel();
				if (!salesAreaPriceMap.containsKey(salesArea)) {
					salesAreaPriceMap.put(salesArea, new ArrayList<ErpMaterialPriceModel>());
				}
				salesAreaPriceMap.get(salesArea).add(priceRowModel);
			}

			for (final Map.Entry<String, List<ErpMaterialPriceModel>> salesAreaPriceMapEntry : salesAreaPriceMap
					.entrySet()) {
				boolean isDefaultZoneExists = false;
				for (ErpMaterialPriceModel priceRowEntry : salesAreaPriceMapEntry.getValue()) {
					if (ErpServicesProperties.getMasterDefaultZoneId().equalsIgnoreCase(priceRowEntry.getSapZoneId())) {
						isDefaultZoneExists = true;
					}
				}
				if (!isDefaultZoneExists) {
					bufferStr.append(salesAreaPriceMapEntry.getKey() + ", ");
				}
			}

			/*
			 * if(!StringUtils.isEmpty(bufferStr.toString())) { throw new
			 * LoaderException
			 * ("No master default price row exported for salesarea: " +
			 * bufferStr.toString()); }
			 */
		}

		/**
		 * This method evaluates the promo price if any within the sales org,
		 * distribution channel & pricing zone. Also, promo price is NOT
		 * applicable for scaled price rows
		 * 
		 * @param priceRecord
		 * @param promoPriceRecordMap
		 * @return the promo price
		 */
		private double findPromoPrice(final MaterialPriceParameter priceRecord,
				final Map<String, List<MaterialPromoPriceParameter>> promoPriceRecordMap) {
			final double promoPrice = 0.0;
			// cannot have promo price for scaled pricing.
			if (promoPriceRecordMap != null && priceRecord != null && priceRecord.getScaleQuanity() <= 0
					&& promoPriceRecordMap.get(priceRecord.getMaterialID()) != null) {
				for (final MaterialPromoPriceParameter promoPriceRecord : promoPriceRecordMap.get(priceRecord
						.getMaterialID())) {
					if (promoPriceRecord.getSalesOrganizationId().equals(priceRecord.getSalesOrganizationId())
							&& promoPriceRecord.getDistributionChannelId().equals(
									priceRecord.getDistributionChannelId())
							&& promoPriceRecord.getZoneId().equals(priceRecord.getZoneId())) {
						return promoPriceRecord.getPrice();
					}
				}
			}
			return promoPrice;
		}

		/**
		 * method to populate error record to ERP system
		 * 
		 * @param priceRecord
		 * @param materialPriceErrorTable
		 */
		private void populateResponseRecord(final FDJcoServerResult result, final MaterialPriceParameter priceRecord,
				final JCoTable materialPriceErrorTable, final String errorMessage) {
			if (priceRecord != null && materialPriceErrorTable != null) {
				materialPriceErrorTable.appendRow();

				materialPriceErrorTable.setValue("KUNNR", priceRecord.getCustomerNumber());
				materialPriceErrorTable.setValue("MATNR", priceRecord.getMaterialID());
				materialPriceErrorTable.setValue("ZONEID", priceRecord.getZoneId());
				materialPriceErrorTable.setValue("MAKTX", priceRecord.getMaterialDescription());
				materialPriceErrorTable.setValue("VKORG", priceRecord.getSalesOrganizationId());
				materialPriceErrorTable.setValue("VTWEG", priceRecord.getDistributionChannelId());
				// materialPriceErrorTable.setValue("WERKS", "");
				materialPriceErrorTable.setValue("PRITY", priceRecord.getPriceType());
				materialPriceErrorTable.setValue("QUANT", priceRecord.getScaleQuanity());
				materialPriceErrorTable.setValue("KONMS", priceRecord.getScaleUnitCode());
				materialPriceErrorTable.setValue("MEINS", priceRecord.getPricingUnitCode());
				materialPriceErrorTable.setValue("PRICE", priceRecord.getPrice());
				materialPriceErrorTable.setValue("CURNCY", priceRecord.getCurrencyCode());
				materialPriceErrorTable.setValue("SALEUN", priceRecord.getSalesUnitCode());
				materialPriceErrorTable.setValue("VLDFR", priceRecord.getValidFromDate());
				materialPriceErrorTable.setValue("VLDTO", priceRecord.getValidToDate());
				materialPriceErrorTable.setValue("KNUMH", priceRecord.getSapId());
				materialPriceErrorTable.setValue("ERROR", errorMessage);
			}

			if (result != null) {
				result.addError(priceRecord.getMaterialID(), errorMessage);
			}
		}

		/**
		 * This method populates the promo price record from ERP system
		 * 
		 * @param materialPriceTable
		 * @return the record parameter
		 */
		private MaterialPromoPriceParameter populateMaterialPromoPriceRecord(final JCoTable materialPriceTable) {
			final MaterialPromoPriceParameter param = new MaterialPromoPriceParameter();

			param.setMaterialID(FDSapHelperUtils.getString(materialPriceTable.getString("MATNR")));
			param.setZoneId(FDSapHelperUtils.getString(materialPriceTable.getString("ZONEID")));

			param.setSalesOrganizationId(FDSapHelperUtils.getString(materialPriceTable.getString("VKORG")));
			param.setDistributionChannelId(FDSapHelperUtils.getString(materialPriceTable.getString("VTWEG")));

			param.setPrice(FDSapHelperUtils.getDouble(materialPriceTable.getString("PRICE")));
			param.setPricingUnitCode(FDSapHelperUtils.getString(materialPriceTable.getString("MEINS")));

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got promo price record for Material No:" + param.getMaterialID() + "\t SalesOrg:"
						+ param.getSalesOrganizationId() + "\t DistChannel:" + param.getDistributionChannelId()
						+ "\t ZoneId:" + param.getZoneId() + "\t UnitCode:" + param.getPricingUnitCode()
						+ "\t PromoPrice:" + param.getPrice());
			}

			return param;
		}

		/**
		 * This method populates the regular/scale price record from ERP system
		 * 
		 * @param materialPriceTable
		 * @return the record parameter
		 */
		private MaterialPriceParameter populateMaterialPriceRecord(final JCoTable materialPriceTable) {
			final MaterialPriceParameter param = new MaterialPriceParameter();

			param.setMaterialID(FDSapHelperUtils.getString(materialPriceTable.getString("MATNR")));
			param.setMaterialDescription(FDSapHelperUtils.getString(materialPriceTable.getString("MAKTX")));
			param.setZoneId(FDSapHelperUtils.getString(materialPriceTable.getString("ZONEID")));
			param.setCustomerNumber(FDSapHelperUtils.getString(materialPriceTable.getString("KUNNR")));

			param.setSalesOrganizationId(FDSapHelperUtils.getString(materialPriceTable.getString("VKORG")));
			param.setDistributionChannelId(FDSapHelperUtils.getString(materialPriceTable.getString("VTWEG")));

			param.setPriceType(FDSapHelperUtils.getString(materialPriceTable.getString("PRITY")));
			param.setPrice(FDSapHelperUtils.getDouble(materialPriceTable.getString("PRICE")));
			param.setPricingUnitCode(FDSapHelperUtils.getString(materialPriceTable.getString("MEINS")));
			param.setScaleQuanity(FDSapHelperUtils.getInt(materialPriceTable.getString("QUANT")));
			param.setScaleUnitCode(FDSapHelperUtils.getString(materialPriceTable.getString("KONMS")));
			param.setSalesUnitCode(FDSapHelperUtils.getString(materialPriceTable.getString("SALEUN")));

			param.setCurrencyCode(FDSapHelperUtils.getString(materialPriceTable.getString("CURNCY")));
			param.setValidFromDate(materialPriceTable.getDate("VLDFR"));
			param.setValidToDate(materialPriceTable.getDate("VLDTO"));
			param.setSapId(FDSapHelperUtils.getString(materialPriceTable.getString("KNUMH")));

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got regular/scale price record for Material No:" + param.getMaterialID() + "\t SalesOrg:"
						+ param.getSalesOrganizationId() + "\t DistChannel:" + param.getDistributionChannelId()
						+ "\t ZoneId:" + param.getZoneId() + "\t UnitCode:" + param.getPricingUnitCode() + "\t Price:"
						+ param.getPrice() + "\t ScaleUnitCode:" + param.getScaleUnitCode() + "\t ScaleQuantity:"
						+ param.getScaleQuanity() + "\t CustomerNo:" + param.getCustomerNumber());
			}

			return param;
		}
	}

	/**
	 * Returns a negative integer if either the promoPrice or sellingPrice is
	 * zero or if the promoPrice is greater than or equal to sellingPrice. The
	 * formula used is (sellingPrice - promoPrice) / sellingPrice x 100. The
	 * percentage is rounded down to the nearest integer divisible by 5 or 2.
	 * Example: 27.XX becomes 26, 25.XX becomes 25 etc.
	 * 
	 * @param sellingPrice
	 * @param promoPrice
	 * @return the integer (deal percentage)
	 */
	public static int getVariancePercentage(final double sellingPrice, final double promoPrice) {

		if (promoPrice <= 0.0 || sellingPrice <= 0.0 || sellingPrice <= promoPrice) {
			return -1;
		}
		final int dealResult = (int) ((sellingPrice - promoPrice) * 100.0 / sellingPrice + 0.2);
		if (((dealResult % 5) == 0) || ((dealResult % 2) == 0)) {
			return dealResult;
		}
		return dealResult - 1;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

}
