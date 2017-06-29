package com.freshdirect.dataloader.material;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialUOMParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.payment.service.FDECommerceService;
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
 * This class populates the material sales units via Jco-server registered to
 * ERP system
 * 
 * Created by kkanuganti on 02/11/15.
 */
public class FDProductUOMJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDProductUOMJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDProductUOMJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDProductUOMRepository");

		final JCoRecordMetaData metadataMatUOMList = JCo.createRecordMetaData("MATUOMLIST");

		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("UMREN", JCoMetaData.TYPE_CHAR, 5,
				"Denominator for conversion to base units"));
		// tableMetaDataList.add(new TableMetaData("UMREN", JCoMetaData.TYPE_NUM
		// , 5, 0, "Denominator for conversion to base units"));
		tableMetaDataList.add(new TableMetaData("MEINH", JCoMetaData.TYPE_CHAR, 3, "Alternative Unit of Measure"));
		tableMetaDataList.add(new TableMetaData("MSEHT", JCoMetaData.TYPE_CHAR, 10, "Unit of Measurement Text "));
		tableMetaDataList.add(new TableMetaData("UMREZ", JCoMetaData.TYPE_CHAR, 5,
				"Numerator for Conversion to Base Units"));
		// tableMetaDataList.add(new TableMetaData("UMREZ",
		// JCoMetaData.TYPE_NUM, 5, 0,
		// "Numerator for Conversion to Base Units"));
		tableMetaDataList.add(new TableMetaData("MEINS", JCoMetaData.TYPE_CHAR, 3, "Base Unit of Measure"));
		tableMetaDataList.add(new TableMetaData("MEABM", JCoMetaData.TYPE_CHAR, 3,
				"Unit of Dimension (Display Indicator)")); // If the unit
															// dimension is
															// 'U1', it shows
															// the conversion
															// for unit price

		createTableRecord(metadataMatUOMList, tableMetaDataList);
		metadataMatUOMList.lock();
		repository.addRecordMetaDataToCache(metadataMatUOMList);

		/*
		 * final JCoListMetaData fmetaImport =
		 * JCo.createListMetaData("MATERIAL_UOM_IMPORTS");
		 * fmetaImport.add("T_MAT_UOM", JCoMetaData.TYPE_TABLE,
		 * metadataMatUOMList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.lock();
		 */
		final JCoRecordMetaData metaMaterialUOMReturnRecord = JCo.createRecordMetaData("MATERIAL_UOM_RETURN_LIST");

		tableMetaDataList = new ArrayList<TableMetaData>();
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("ERROR", JCoMetaData.TYPE_CHAR, 220, "Error Message"));

		createTableRecord(metaMaterialUOMReturnRecord, tableMetaDataList);
		metaMaterialUOMReturnRecord.lock();
		repository.addRecordMetaDataToCache(metaMaterialUOMReturnRecord);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("MATERIAL_UOM_IMPORTS");
		fmetaImport.add("T_MAT_UOM", JCoMetaData.TYPE_TABLE, metadataMatUOMList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaMaterialUOMReturnRecord,
				JCoListMetaData.EXPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("MATERIAL_UOM_EXPORTS");
		fmetaExport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaMaterialUOMReturnRecord,
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
				final JCoTable materialUOMTable = function.getTableParameterList().getTable("T_MAT_UOM");
				materialErrorTable = function.getTableParameterList().getTable("T_MAT_GLOBAL_ERR");
				final FDJcoServerResult result = new FDJcoServerResult();
				final int successCnt = 0;

				if(SapProperties.isMaterialUOMExportLogEnabled()){
					LOG.info("******************* Material UOM Data ************");
					LOG.info(materialUOMTable);
				}
				
				final Map<String, HashSet<ErpSalesUnitModel>> uomRecordMap = new HashMap<String, HashSet<ErpSalesUnitModel>>();
				final Map<String, ErpSalesUnitModel> unitPricingRecordMap = new HashMap<String, ErpSalesUnitModel>();

				if (materialUOMTable != null) {
					for (int i = 0; i < materialUOMTable.getNumRows(); i++) {
						materialUOMTable.setRow(i);

						final MaterialUOMParameter uomRecordParam = populateMaterialUOMRecord(materialUOMTable);
						if (!checkValidUOMRecord(uomRecordParam)) {
							continue;
						}

						ErpSalesUnitModel salesUnit = new ErpSalesUnitModel();

						salesUnit.setDenominator(uomRecordParam.getDenominator());
						salesUnit.setAlternativeUnit(uomRecordParam.getUnitCode());
						salesUnit.setDescription(uomRecordParam.getDescription());
						salesUnit.setNumerator(uomRecordParam.getNumerator());
						salesUnit.setBaseUnit(uomRecordParam.getBaseUnitCode());
						salesUnit.setDisplayInd(uomRecordParam.getDisplayIndicator());

						HashSet<ErpSalesUnitModel> units = null;
						if (!uomRecordMap.containsKey(uomRecordParam.getMaterialID())) {
							uomRecordMap.put(uomRecordParam.getMaterialID(), new HashSet<ErpSalesUnitModel>());
						}

						units = uomRecordMap.get(uomRecordParam.getMaterialID());
						if ("U1".equalsIgnoreCase(uomRecordParam.getUnitDimension())) {
							unitPricingRecordMap.put(uomRecordParam.getMaterialID(), salesUnit);
						} else {
							units.add(salesUnit);
						}

						if (unitPricingRecordMap.containsKey(uomRecordParam.getMaterialID())) {
							ErpSalesUnitModel unitPricingInfo = unitPricingRecordMap
									.get(uomRecordParam.getMaterialID());
							for (ErpSalesUnitModel erpSalesUnitModel : units) {
								erpSalesUnitModel.setUnitPriceNumerator(unitPricingInfo.getNumerator());
								erpSalesUnitModel.setUnitPriceDenominator(unitPricingInfo.getDenominator());
								erpSalesUnitModel.setUnitPriceUOM(unitPricingInfo.getAlternativeUnit());
								erpSalesUnitModel.setUnitPriceDescription(unitPricingInfo.getDescription());
							}
						}

						materialUOMTable.nextRow();
					}

					populateProductUOM(uomRecordMap, materialErrorTable, result, successCnt);

					// exportParamList.setValue("RETURN",
					// (FDJcoServerResult.OK_STATUS.equals(result.getStatus())
					// && materialUOMTable.getNumRows() == successCnt) ? "S" :
					// "W");
					// exportParamList.setValue("MESSAGE",
					// String.format("%s Product UOM(s) imported successfully! [ %s ]! [ %s ]",
					// successCnt, new Date()));
					exportParamList.setValue("T_MAT_GLOBAL_ERR", materialErrorTable);

					// email failure report
					emailFailureReport(result, "Product Sales UOM", null);
				}
			} catch (final Exception e) {
				LOG.error("Error importing product UOM(s): " + e);
				// exportParamList.setValue("RETURN", "E");
				// exportParamList.setValue("MESSAGE", e.toString().substring(0,
				// Math.min(225, e.toString().length())));
			}
		}

		/**
		 * Method to populate/update the product variants
		 * 
		 * @param salesUnitMap
		 * @param materialUOMErrorTable
		 * @param result
		 * @param successCnt
		 * @throws RemoteException
		 * @throws EJBException
		 */
		private void populateProductUOM(final Map<String, HashSet<ErpSalesUnitModel>> salesUnitMap,
				final JCoTable materialUOMErrorTable, final FDJcoServerResult result, int successCnt)
				throws EJBException, RemoteException, LoaderException {
			Context ctx = null;
			int batchNumber = 0;
			try {
				ctx = com.freshdirect.ErpServicesProperties.getInitialContext();

				SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");
				SAPLoaderSB sapLoader = home.create();

				// create a new batch
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
					batchNumber = FDECommerceService.getInstance().createBatch();
				}else{
					batchNumber = sapLoader.createBatch();
				}
				for (final Map.Entry<String, HashSet<ErpSalesUnitModel>> productUOMEntry : salesUnitMap.entrySet()) {
					final String matNo = productUOMEntry.getKey();
					HashSet<ErpSalesUnitModel> salesUnits = productUOMEntry.getValue();
					try {
						if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
							FDECommerceService.getInstance().loadSalesUnits(batchNumber, matNo, salesUnits);
						}else{
							sapLoader.loadSalesUnits(batchNumber, matNo, salesUnits);
						}
						successCnt = successCnt + productUOMEntry.getValue().size();

						LOG.info(String.format("%s product UOM(s) updated for material: " + matNo, productUOMEntry
								.getValue().size()));

					} catch (final Exception e) {
						LOG.error("Add / Updating product sales UOM(s) for material: " + productUOMEntry.getKey()
								+ " failed. Exception is ", e);

						final StringBuffer variantsBuffer = new StringBuffer();
						variantsBuffer.append(e.toString() + " to add UOM(s) [");
						for (final ErpSalesUnitModel salesUnit : productUOMEntry.getValue()) {
							variantsBuffer.append(salesUnit.getAlternativeUnit() + ",");
						}
						variantsBuffer.append("]");

						populateResponseRecord(result, matNo, variantsBuffer.toString());
					}
				}

				// mark the batch status
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
					FDECommerceService.getInstance().updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);
				}else{
					sapLoader.updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);
				}
			} catch (CreateException ce) {
				LOG.warn("Unable to create session bean", ce);
			} catch (NamingException ne) {
				LOG.warn("Failed to look up session bean home", ne);
			} finally {
				try {
					if (ctx != null)
						ctx.close();
				} catch (NamingException ne) {
					LOG.warn("Error closing naming context", ne);
				}
			}
		}

		/**
		 * @param materialUOMTable
		 */
		private MaterialUOMParameter populateMaterialUOMRecord(final JCoTable materialUOMTable) {
			final MaterialUOMParameter param = new MaterialUOMParameter();

			param.setMaterialID(FDSapHelperUtils.getString(materialUOMTable.getString("MATNR")));
			param.setUnitCode(FDSapHelperUtils.getString(materialUOMTable.getString("MEINH")));
			param.setDescription(FDSapHelperUtils.getString(materialUOMTable.getString("MSEHT")));

			final String numerator = materialUOMTable.getString("UMREZ");
			final String denominator = materialUOMTable.getString("UMREN");
			param.setNumerator(Integer.valueOf(numerator));
			param.setDenominator(Integer.valueOf(denominator));

			param.setBaseUnitCode(FDSapHelperUtils.getString(materialUOMTable.getString("MEINS")));
			param.setUnitDimension(FDSapHelperUtils.getString(materialUOMTable.getString("MEABM")));

			param.setDisplayIndicator(checkDisplayIndicator(param));

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got material global Record For Material No:" + param.getMaterialID() + "\t UnitCode:"
						+ param.getUnitCode() + "\t BaseUnitCode:" + param.getBaseUnitCode() + "\t Numerator:"
						+ param.getNumerator() + "\t Denominator:" + param.getDenominator() + "\t DisplayIndicator:"
						+ param.getDisplayIndicator() + "\t Description:" + param.getDescription());
			}

			return param;
		}

		/**
		 * @param param
		 * @return boolean
		 */
		private boolean checkValidUOMRecord(final MaterialUOMParameter param) {
			// Ignore the record for which the display indicator 'W1' is not
			// correct
			if (!"w1".equalsIgnoreCase(param.getUnitDimension()) || checkDisplayIndicator(param)) {
				return true;
			}

			return false;
		}

		/**
		 * @param param
		 * @return boolean
		 */
		private boolean checkDisplayIndicator(final MaterialUOMParameter param) {
			if (("EA".equalsIgnoreCase(param.getBaseUnitCode())) && ("LB".equalsIgnoreCase(param.getUnitCode()))
					&& ("w1".equalsIgnoreCase(param.getUnitDimension()))) {
				return true;
			}

			return false;
		}
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

}
