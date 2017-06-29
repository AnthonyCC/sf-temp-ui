package com.freshdirect.dataloader.material;

import static org.apache.commons.lang.BooleanUtils.toBooleanObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialGlobalParameter;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialVariantParameter;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialVariantPriceParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
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
 * This class will be used for populating the material global, variants, variant
 * price by salesarea via Jco-server registered to ERP system
 * 
 * Created by kkanuganti on 02/11/15.
 */
public class FDProductBaseJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDProductBaseJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDProductBaseJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDProductGlobalRepository");

		final JCoRecordMetaData metaGlobalMaterialList = JCo.createRecordMetaData("MAT_GLOBAL_LIST");
		createMaterialGlobalMetadata(repository, metaGlobalMaterialList);

		final JCoRecordMetaData metaMaterialVariantList = JCo.createRecordMetaData("MAT_VARIANT_LIST");
		createMaterialVariantMetadata(repository, metaMaterialVariantList);

		final JCoRecordMetaData metaMaterialVariantPriceList = JCo.createRecordMetaData("MAT_VARIANTPRICE_LIST");
		createMaterialVariantPriceMetadata(repository, metaMaterialVariantPriceList);

		/*
		 * final JCoListMetaData fmetaImport =
		 * JCo.createListMetaData("MATERIAL_GLOBAL_IMPORTS");
		 * fmetaImport.add("T_MATERIAL_GLOBAL", JCoMetaData.TYPE_TABLE,
		 * metaGlobalMaterialList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.add("T_MAT_VARCONFIG", JCoMetaData.TYPE_TABLE,
		 * metaMaterialVariantList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.add("T_MAT_VARPRICE", JCoMetaData.TYPE_TABLE,
		 * metaMaterialVariantPriceList , JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.lock();
		 */

		final JCoRecordMetaData metaGlobalMaterialReturnRecord = JCo
				.createRecordMetaData("MATERIAL_GLOBAL_RETURN_LIST");

		tableMetaDataList = new ArrayList<TableMetaData>();
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("ERROR", JCoMetaData.TYPE_CHAR, 220, "Material Error Message"));

		createTableRecord(metaGlobalMaterialReturnRecord, tableMetaDataList);
		metaGlobalMaterialReturnRecord.lock();
		repository.addRecordMetaDataToCache(metaGlobalMaterialReturnRecord);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("MATERIAL_GLOBAL_IMPORTS");
		fmetaImport.add("T_MATERIAL_GLOBAL", JCoMetaData.TYPE_TABLE, metaGlobalMaterialList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_VARCONFIG", JCoMetaData.TYPE_TABLE, metaMaterialVariantList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_VARPRICE", JCoMetaData.TYPE_TABLE, metaMaterialVariantPriceList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaGlobalMaterialReturnRecord,
				JCoListMetaData.EXPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("MATERIAL_GLOBAL_EXPORTS");
		fmetaExport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaGlobalMaterialReturnRecord,
				JCoListMetaData.EXPORT_PARAMETER);
		// fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null,
		// JCoListMetaData.EXPORT_PARAMETER, null, null);
		// fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 250, 0, 0, null,
		// null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null,
				fmetaImport, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	}

	private void createMaterialGlobalMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaGlobalMaterialList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material No."));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, 0, "Material Description"));
		tableMetaDataList.add(new TableMetaData("BISMT", JCoMetaData.TYPE_CHAR, 18, 0, "Sku Code"));
		tableMetaDataList.add(new TableMetaData("MEINS", JCoMetaData.TYPE_CHAR, 3, 0, "Base Unit"));
		tableMetaDataList.add(new TableMetaData("VRKME", JCoMetaData.TYPE_CHAR, 3, 0, "Sales Unit")); // N
		tableMetaDataList.add(new TableMetaData("MTART", JCoMetaData.TYPE_CHAR, 4, 0, "Material Type"));
		tableMetaDataList.add(new TableMetaData("MTBEZ", JCoMetaData.TYPE_CHAR, 25, 0, "Material Type Description"));
		tableMetaDataList.add(new TableMetaData("MATKL", JCoMetaData.TYPE_CHAR, 9, 0, "Material Group"));
		tableMetaDataList.add(new TableMetaData("KZKFG", JCoMetaData.TYPE_CHAR, 1, 0, "Configurable Item"));
		tableMetaDataList.add(new TableMetaData("MSTAV", JCoMetaData.TYPE_CHAR, 2, 0, "Cross Chain Sales Status")); // N
		tableMetaDataList.add(new TableMetaData("MSTDV", JCoMetaData.TYPE_CHAR, 8, 0, "Sales Status Date")); // N
		tableMetaDataList.add(new TableMetaData("LVORM", JCoMetaData.TYPE_CHAR, 1, 0, "Deletion flag indicator"));
		tableMetaDataList.add(new TableMetaData("EAN11", JCoMetaData.TYPE_CHAR, 18, 0, "UPC"));
		tableMetaDataList.add(new TableMetaData("TAXM1", JCoMetaData.TYPE_CHAR, 1, 0, "TaxID Indicator")); // (0
																											// -
																											// No
																											// tax,
																											// 1
																											// -
																											// Taxable)
		tableMetaDataList.add(new TableMetaData("WESCH", JCoMetaData.TYPE_CHAR, 3, 0, "Number of Days Fresh"));
		tableMetaDataList.add(new TableMetaData("FERTH", JCoMetaData.TYPE_CHAR, 18, 0, "Avalara Tax ID"));
		tableMetaDataList.add(new TableMetaData("VAREX", JCoMetaData.TYPE_CHAR, 1, 0, "Variant config indicator"));

		createTableRecord(metaGlobalMaterialList, tableMetaDataList);
		metaGlobalMaterialList.lock();
		repository.addRecordMetaDataToCache(metaGlobalMaterialList);
	}

	private void createMaterialVariantMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaMaterialVariantList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("PRFID", JCoMetaData.TYPE_CHAR, 30, "Configuration Profile Name"));
		tableMetaDataList.add(new TableMetaData("CLASS", JCoMetaData.TYPE_CHAR, 18, "Class number"));
		tableMetaDataList.add(new TableMetaData("ATNAM", JCoMetaData.TYPE_CHAR, 30, "Characteristic Name"));
		tableMetaDataList.add(new TableMetaData("ATWRT", JCoMetaData.TYPE_CHAR, 30, "Characteristic Value"));
		tableMetaDataList.add(new TableMetaData("ATSTD", JCoMetaData.TYPE_CHAR, 1, "Default value"));
		tableMetaDataList.add(new TableMetaData("MEABM", JCoMetaData.TYPE_CHAR, 3, "Unit of Dimension"));

		createTableRecord(metaMaterialVariantList, tableMetaDataList);
		metaMaterialVariantList.lock();
		repository.addRecordMetaDataToCache(metaMaterialVariantList);
	}

	private void createMaterialVariantPriceMetadata(final JCoCustomRepository repository,
			final JCoRecordMetaData metaMaterialVariantPriceList) {
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("PRFID", JCoMetaData.TYPE_CHAR, 30, "Configuration Profile Name"));
		tableMetaDataList.add(new TableMetaData("CLASS", JCoMetaData.TYPE_CHAR, 18, "Class number"));
		tableMetaDataList.add(new TableMetaData("ATNAM", JCoMetaData.TYPE_CHAR, 30, "Characteristic Name"));
		tableMetaDataList.add(new TableMetaData("ATWRT", JCoMetaData.TYPE_CHAR, 30, "Characteristic Value"));
		tableMetaDataList.add(new TableMetaData("ATWTB", JCoMetaData.TYPE_CHAR, 30, "Characteristic Description"));
		tableMetaDataList.add(new TableMetaData("KBETR", JCoMetaData.TYPE_CHAR, 11,
				"Rate (amount or percentage) where no scale exists"));
		tableMetaDataList.add(new TableMetaData("KMEIN", JCoMetaData.TYPE_CHAR, 3, "Condition unit"));
		tableMetaDataList.add(new TableMetaData("KSCHL", JCoMetaData.TYPE_CHAR, 4, "Condition type"));
		tableMetaDataList.add(new TableMetaData("KNUMH", JCoMetaData.TYPE_CHAR, 10, "Condition Record No."));
		tableMetaDataList.add(new TableMetaData("DATBI", JCoMetaData.TYPE_DATE, 8,
				"Validity end date of the condition record")); // N
		tableMetaDataList.add(new TableMetaData("DATAB", JCoMetaData.TYPE_DATE, 8,
				"Validity start date of the condition record")); // N

		createTableRecord(metaMaterialVariantPriceList, tableMetaDataList);
		metaMaterialVariantPriceList.lock();
		repository.addRecordMetaDataToCache(metaMaterialVariantPriceList);
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
			final FDJcoServerResult result = new FDJcoServerResult();
			final int successCnt = 0;
			try {
				// checks if any batch in loading status. If found, do not
				// process / create new batch
				checkBatchStatus();

				final JCoTable materialTable = function.getTableParameterList().getTable("T_MATERIAL_GLOBAL");
				final JCoTable variantConfigTable = function.getTableParameterList().getTable("T_MAT_VARCONFIG");
				final JCoTable variantPriceTable = function.getTableParameterList().getTable("T_MAT_VARPRICE");
				
				if(SapProperties.isMaterialGlobalExportLogEnabled()){
					LOG.info("******************* Material Global Data ************");
					LOG.info(materialTable);
					LOG.info("******************* Material Variant Config ************");
					LOG.info(variantConfigTable);
					LOG.info("******************* Material Variant Pricing ************");
					LOG.info(variantPriceTable);
				}

				materialErrorTable = function.getTableParameterList().getTable("T_MAT_GLOBAL_ERR");// function.getTableParameterList().getTable("T_MAT_GLOBAL_ERR");

				if (materialTable != null) {
					/* Material No -> ErpMaterialModel */
					Map<String, ErpMaterialModel> materialMap = new HashMap<String, ErpMaterialModel>();

					/* Material No -> ClassName -> Class */
					Map<String, Map<String, ErpClassModel>> materialClassMap = new HashMap<String, Map<String, ErpClassModel>>();

					/*
					 * Material No -> ErpCharacteristicValuePriceModel ->
					 * String, String
					 */
					Map<String, Map<ErpCharacteristicValuePriceModel, Map<String, String>>> variantPriceMap = new HashMap<String, Map<ErpCharacteristicValuePriceModel, Map<String, String>>>();

					// global material
					buildGlobalMaterial(result, materialTable, materialMap);

					// variant configuration
					builtMaterialClass(result, variantConfigTable, materialMap, materialClassMap);

					// variant price by salesarea
					buildMaterialVariantPrice(result, variantPriceTable, materialMap, materialClassMap, variantPriceMap);

					processMaterialData(result, materialMap, materialClassMap, variantPriceMap, successCnt);

					// exportParamList.setValue("RETURN",
					// (FDJcoServerResult.OK_STATUS.equals(result.getStatus())
					// && materialTable.getNumRows() == successCnt) ? "S" :
					// "W");
					// exportParamList.setValue("MESSAGE",
					// String.format("%s Global material details imported successfully! [ %s ]",
					// successCnt, new Date()));

					exportParamList.setValue("T_MAT_GLOBAL_ERR", materialErrorTable);
				}
			} catch (final Exception e) {
				LOG.error("Error importing global materils details: ", e);
				/*
				 * exportParamList.setValue("RETURN", "E");
				 * exportParamList.setValue("MESSAGE", e.toString().substring(0,
				 * Math.min(255, e.toString().length())));
				 */
			}
		}

		/**
		 * Method to load material, class, characteristic, characteristic value
		 * & characteristic value prices data exported by ERP to storefront
		 * 
		 * @param result
		 * @param materialMap
		 * @param materialClassMap
		 * @param variantPriceMap
		 * @param successCnt
		 * 
		 * @throws EJBException
		 * @throws RemoteException
		 * @throws LoaderException
		 */
		private void processMaterialData(FDJcoServerResult result, Map<String, ErpMaterialModel> materialMap,
				Map<String, Map<String, ErpClassModel>> materialClassMap,
				Map<String, Map<ErpCharacteristicValuePriceModel, Map<String, String>>> variantPriceMap, int successCnt)
				throws EJBException, RemoteException, LoaderException {

			int batchNumber = 0;
			Context ctx = null;
			try {
				ctx = com.freshdirect.ErpServicesProperties.getInitialContext();

				SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");
				SAPLoaderSB sapLoader = home.create();

				// 1. create batch version
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
					batchNumber = FDECommerceService.getInstance().createBatch();
				}else{
					batchNumber = sapLoader.createBatch();
				}

				LOG.info("####### Starting to load global material data, version:" + batchNumber + " ######");

				// 2. load material data
				for (Map.Entry<String, ErpMaterialModel> materialEntry : materialMap.entrySet()) {
					LOG.debug("Starting to load global material data for material No: " + materialEntry.getKey()
							+ ", version:" + batchNumber);

					String materialNo = materialEntry.getKey();
					try {
						if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
							FDECommerceService.getInstance().loadData(batchNumber, materialEntry.getValue(), materialClassMap.get(materialNo),
									variantPriceMap.get(materialNo));
						}else{
						sapLoader.loadData(batchNumber, materialEntry.getValue(), materialClassMap.get(materialNo),
								variantPriceMap.get(materialNo));
						}

						successCnt++;
					} catch (RemoteException e) {
						LOG.error("Failed to load data for material No [" + materialNo + "], version [" + batchNumber
								+ "] ", e);

						populateResponseRecord(result, materialNo, "Failed to load data for the material");
						continue;
					} catch (LoaderException e) {
						LOG.error("Failed to load data for material No [" + materialNo + "], version [" + batchNumber
								+ "] ", e);

						populateResponseRecord(result, materialNo, "Failed to load data for the material");
						continue;
					}

					LOG.debug("Loading global material data for material No: " + materialEntry.getKey() + ", version:"
							+ batchNumber + " is complete");
				}

				// 3. mark the batch status
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
					FDECommerceService.getInstance().updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);
				}else{
					sapLoader.updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);
				}
				// 4. email failure material report
				emailFailureReport(result, "Global material", Integer.toString(batchNumber));

				LOG.info("####### Loading global material data, version:" + batchNumber
						+ " completed successfully ######");
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
		 * @param result
		 * @param materialTable
		 * @param materialMap
		 */
		private void buildGlobalMaterial(final FDJcoServerResult result, final JCoTable materialTable,
				Map<String, ErpMaterialModel> materialMap) {
			for (int i = 0; i < materialTable.getNumRows(); i++) {
				materialTable.setRow(i);

				final MaterialGlobalParameter materialParam = populateMaterialGlobalRecord(materialTable);

				// material must have a SKU assigned
				if (materialParam.getSkuCode() == null || StringUtils.isEmpty(materialParam.getSkuCode())) {
					LOG.error("No SKU provided for the material No. { " + materialParam.getMaterialID() + " } ");

					populateResponseRecord(result, materialParam.getMaterialID(), "No SKU provided for the material");
					continue;
				}

				if (!materialMap.containsKey(materialParam.getMaterialID())) {
					materialMap.put(materialParam.getMaterialID(), new ErpMaterialModel());
				}

				ErpMaterialModel materialModel = materialMap.get(materialParam.getMaterialID());
				if (materialModel != null) {
					materialModel.setSapId(materialParam.getMaterialID());
					materialModel.setSkuCode(materialParam.getSkuCode());
					materialModel.setBaseUnit(materialParam.getUnitCode());
					materialModel.setDescription(materialParam.getMaterialDescription());
					materialModel.setUPC(materialParam.getUpc());
					materialModel.setDaysFresh(materialParam.getDaysFresh());
					materialModel.setMaterialType(materialParam.getMaterialType());
					materialModel.setTaxable(materialParam.getTaxable().booleanValue());
					materialModel.setTaxCode(materialParam.getTaxId());
					materialModel.setMaterialGroup(materialParam.getMaterialGroup());

					if (materialParam.getMaterialGroup() != null && !materialParam.getMaterialGroup().isEmpty()) {
						materialModel.setAlcoholicContent(FDSapHelperUtils.convertToAlcoholContentType(materialParam
								.getMaterialGroup()));
					}
				}

				materialTable.nextRow();
			}
		}

		/**
		 * @param variantConfigTable
		 * @param materialClassMap
		 */
		@SuppressWarnings("unchecked")
		private void builtMaterialClass(final FDJcoServerResult result, final JCoTable variantConfigTable,
				final Map<String, ErpMaterialModel> materialMap,
				Map<String, Map<String, ErpClassModel>> materialClassMap) {
			if (variantConfigTable != null) {
				for (int i = 0; i < variantConfigTable.getNumRows(); i++) {
					variantConfigTable.setRow(i);

					final MaterialVariantParameter variantParam = populateVariantRecord(variantConfigTable);

					// do not process material characstric price if global
					// material data is not exported or failed validation
					if (!materialMap.containsKey(variantParam.getMaterialID())) {
						continue;
					}

					ErpMaterialModel material = materialMap.get(variantParam.getMaterialID());
					if (material != null && "S1".equalsIgnoreCase(variantParam.getUnitOfDimension())) {
						material.setSalesUnitCharacteristic(variantParam.getCharacteristicName());
					}
					if (StringUtils.isEmpty(variantParam.getClassName())) {
						LOG.error("No class name was supplied for the characteristic ["
								+ variantParam.getCharacteristicName() + "], material No. { "
								+ variantParam.getMaterialID() + " } ");

						populateResponseRecord(
								result,
								variantParam.getMaterialID(),
								"No class name was supplied for the characteristic ["
										+ variantParam.getCharacteristicName() + "]");
						continue;
					}

					if (!materialClassMap.containsKey(variantParam.getMaterialID())) {
						materialClassMap.put(variantParam.getMaterialID(), new HashMap<String, ErpClassModel>());
					}

					if (!materialClassMap.get(variantParam.getMaterialID()).containsKey(variantParam.getClassName())) {
						materialClassMap.get(variantParam.getMaterialID()).put(variantParam.getClassName(),
								new ErpClassModel());
					}

					ErpClassModel materialClass = materialClassMap.get(variantParam.getMaterialID()).get(
							variantParam.getClassName());
					if (materialClass != null) {
						materialClass.setSapId(variantParam.getClassName());

						ErpCharacteristicModel charModel = new ErpCharacteristicModel();
						charModel.setName(variantParam.getCharacteristicName());

						// add the characteristic to the class
						if (!materialClass.hasCharacteristic(charModel)) {
							materialClass.addCharacteristic(charModel);
						}
						
						charModel = materialClass.getCharacteristic(variantParam.getCharacteristicName());
						if(null !=charModel && !charModel.isSalesUnit()){
							charModel.setSalesUnit("S1".equalsIgnoreCase(variantParam.getUnitOfDimension()));
						}

						List<ErpCharacteristicModel> characteristics = new ArrayList<ErpCharacteristicModel>(
								materialClass.getCharacteristics());
						ListIterator<ErpCharacteristicModel> characteristicsItr = characteristics.listIterator();

						int foundQtyChar = 0;
						while (characteristicsItr.hasNext()) {
							ErpCharacteristicModel erpCharacteristic = characteristicsItr.next();
							if (erpCharacteristic.getName().endsWith("_QTY")) {
								if (++foundQtyChar > 1) {
									LOG.warn("Duplicate quantity characteristics found for the Class '"
											+ materialClass.getSapId() + "'");
									// and set the modified list back on the
									// class it came from
								}
								characteristicsItr.remove();
								materialClass.setCharacteristics(characteristics);
								
								// set material quantityCharacteristic property
								material = materialMap.get(variantParam.getMaterialID());
								if (material != null) {
									material.setQuantityCharacteristic(erpCharacteristic.getName());
								}
							}
						}
					}
					variantConfigTable.nextRow();
				}
			}
		}

		/**
		 * @param result
		 * @param variantPriceTable
		 * @param materialClassMap
		 * @param variantPriceMap
		 */
		private void buildMaterialVariantPrice(final FDJcoServerResult result, final JCoTable variantPriceTable,
				final Map<String, ErpMaterialModel> materialMap,
				Map<String, Map<String, ErpClassModel>> materialClassMap,
				Map<String, Map<ErpCharacteristicValuePriceModel, Map<String, String>>> variantPriceMap) {
			if (variantPriceTable != null) {
				for (int i = 0; i < variantPriceTable.getNumRows(); i++) {
					variantPriceTable.setRow(i);
					final MaterialVariantPriceParameter variantPriceParam = populateVariantPriceRecord(variantPriceTable);

					// do not process material characstric price if global
					// material data is not exported or failed validation
					if (!materialMap.containsKey(variantPriceParam.getMaterialID())) {
						continue;
					}

					if (null == variantPriceParam.getCharacteristicValueDescription()
							|| "".equalsIgnoreCase(variantPriceParam.getCharacteristicValueDescription())) {
						LOG.error("CharacteristicValue Description(ATWTB) can't be empty for class name ["
								+ variantPriceParam.getClassName() + "], characteristic value ["
								+ variantPriceParam.getCharacteristicValueName() + "] ");

						populateResponseRecord(result, variantPriceParam.getMaterialID(),
								"CharacteristicValue Description(ATWTB) can't be empty for class name ["
										+ variantPriceParam.getClassName() + "], characteristic value ["
										+ variantPriceParam.getCharacteristicValueName() + "]");
						continue;
					}
					ErpClassModel classModel = findErpClass(materialClassMap, variantPriceParam.getClassName());
					if (classModel == null) {
						LOG.error("No matching class found for class name [" + variantPriceParam.getClassName()
								+ "], characteristic value [" + variantPriceParam.getCharacteristicValueName() + "] ");
						populateResponseRecord(result, variantPriceParam.getMaterialID(),
								"No matching class found with class name [" + variantPriceParam.getClassName()
										+ "], characteristic value [" + variantPriceParam.getCharacteristicValueName()
										+ "]");
						continue;
					}

					ErpCharacteristicModel characteristicModel = classModel.getCharacteristic(variantPriceParam
							.getCharacteristicName());
					if (characteristicModel == null) {
						LOG.error("No matching characteristic found for characteristic ["
								+ variantPriceParam.getCharacteristicName() + "], characteristic value ["
								+ variantPriceParam.getCharacteristicValueName() + "] ");
						populateResponseRecord(
								result,
								variantPriceParam.getMaterialID(),
								"No matching characteristic found with characteristic name ["
										+ variantPriceParam.getCharacteristicName() + "], characteristic value ["
										+ variantPriceParam.getCharacteristicValueName() + "]");
						continue;
					}

					ErpCharacteristicValueModel characteristicValueModel = new ErpCharacteristicValueModel();
					characteristicValueModel.setName(variantPriceParam.getCharacteristicValueName());
					characteristicValueModel.setDescription(variantPriceParam.getCharacteristicValueDescription());

					// add the characteristic value to the characteristic
					if (!characteristicModel.hasCharacteristicValue(characteristicValueModel)) {
						characteristicModel.addCharacteristicValue(characteristicValueModel);
					}

					// create the characteristic value price
					ErpCharacteristicValuePriceModel characValPrice = new ErpCharacteristicValuePriceModel();
					characValPrice.setConditionType(variantPriceParam.getConditionType());
					characValPrice.setPrice(variantPriceParam.getPrice());
					characValPrice.setSapId(variantPriceParam.getConditionRecordNumber());
					characValPrice.setPricingUnit(variantPriceParam.getPricingUnitCode());
					characValPrice.setSalesOrg(variantPriceParam.getSalesOrganizationId());
					characValPrice.setDistChannel(variantPriceParam.getDistributionChannelId());

					if (!variantPriceMap.containsKey(variantPriceParam.getMaterialID())) {
						variantPriceMap.put(variantPriceParam.getMaterialID(),
								new HashMap<ErpCharacteristicValuePriceModel, Map<String, String>>());
					}

					if (!variantPriceParam.getCharacteristicName().endsWith("_QTY") && !variantPriceMap.get(variantPriceParam.getMaterialID()).containsKey(characValPrice)
							/*&& characValPrice.getPrice() > 0*/) {
						variantPriceMap.get(variantPriceParam.getMaterialID()).put(characValPrice,
								new HashMap<String, String>());

						Map<String, String> charValPriceExtraInfo = variantPriceMap.get(
								variantPriceParam.getMaterialID()).get(characValPrice);

						if (StringUtils.isEmpty(variantPriceParam.getMaterialID())) {
							LOG.error("No material number was supplied for characteristic value price with class { "
									+ variantPriceParam.getClassName() + " } ");

							populateResponseRecord(result, variantPriceParam.getMaterialID(),
									"No material number was supplied for characteristic value price with class { "
											+ variantPriceParam.getClassName() + " }.");
							continue;
						}
						charValPriceExtraInfo.put(FDSapHelperUtils.MATERIAL_NUMBER, variantPriceParam.getMaterialID());

						if (StringUtils.isEmpty(variantPriceParam.getClassName())) {
							LOG.error("No class name was supplied for charcteristic value price for material No. { "
									+ variantPriceParam.getMaterialID() + " } ");

							populateResponseRecord(result, variantPriceParam.getMaterialID(),
									"No class name was supplied for charcteristic value price");
							continue;
						}
						charValPriceExtraInfo.put(FDSapHelperUtils.CLASS, variantPriceParam.getClassName());

						if (StringUtils.isEmpty(variantPriceParam.getCharacteristicName())) {
							LOG.error("No characteristic name was supplied for characteristic value price for material No. { "
									+ variantPriceParam.getMaterialID() + " } ");

							populateResponseRecord(result, variantPriceParam.getMaterialID(),
									"No characteristic name was supplied for characteristic value price");
							continue;
						}
						charValPriceExtraInfo.put(FDSapHelperUtils.CHARACTERISTIC_NAME,
								variantPriceParam.getCharacteristicName());

						if ((!variantPriceParam.getCharacteristicName().endsWith("_QTY"))
								&& StringUtils.isEmpty(variantPriceParam.getCharacteristicValueName())) {
							LOG.error("No characteristic value name was supplied for characteristic value price for material No. { "
									+ variantPriceParam.getMaterialID() + " } ");

							populateResponseRecord(result, variantPriceParam.getMaterialID(),
									"No characteristic value name was supplied for characteristic value price");
							continue;
						}
						charValPriceExtraInfo.put(FDSapHelperUtils.CHARACTERISTIC_VALUE,
								variantPriceParam.getCharacteristicValueName());
					}
					variantPriceTable.nextRow();
				}
			}
		}

		/**
		 * @param materialClassMap
		 * @param className
		 * @return ErpClassModel
		 */
		public ErpClassModel findErpClass(Map<String, Map<String, ErpClassModel>> materialClassMap, String className) {
			for (Map.Entry<String, Map<String, ErpClassModel>> materialClassMapEntry : materialClassMap.entrySet()) {
				Map<String, ErpClassModel> classMap = materialClassMapEntry.getValue();
				if (classMap.containsKey(className)) {
					return classMap.get(className);
				}
			}
			return null;
		}

		/**
		 * @param materialTable
		 */
		private MaterialGlobalParameter populateMaterialGlobalRecord(final JCoTable materialTable) {
			final MaterialGlobalParameter param = new MaterialGlobalParameter();

			param.setMaterialID(FDSapHelperUtils.getString(materialTable.getString("MATNR")));
			param.setMaterialDescription(FDSapHelperUtils.getString(materialTable.getString("MAKTX")));
			param.setTaxId(FDSapHelperUtils.getString(materialTable.getString("FERTH")));

			param.setSkuCode(FDSapHelperUtils.getString(materialTable.getString("BISMT")));

			param.setUnitCode(FDSapHelperUtils.getString(materialTable.getString("MEINS"))); // base
																								// unit

			param.setMaterialType(FDSapHelperUtils.getString(materialTable.getString("MTART")));
			param.setMaterialTypeDescription(FDSapHelperUtils.getString(materialTable.getString("MTBEZ")));
			param.setMaterialGroup(FDSapHelperUtils.getString(materialTable.getString("MATKL")));

			param.setConfigurableItem(toBooleanObject(StringUtils.lowerCase(materialTable.getString("KZKFG")), "x", "",
					null));

			param.setDeleted(toBooleanObject(StringUtils.lowerCase(materialTable.getString("LVORM")), "x", "", null));
			param.setUpc(FDSapHelperUtils.getString(materialTable.getString("EAN11")));
			param.setTaxable(toBooleanObject(StringUtils.lowerCase(materialTable.getString("TAXM1")), "1", "0", null));
			param.setDaysFresh(FDSapHelperUtils.getString(materialTable.getString("WESCH")));
			param.setVariantConfigExists(toBooleanObject(StringUtils.lowerCase(materialTable.getString("VAREX")), "x",
					"", null).booleanValue());

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got material base record for Material No:" + param.getMaterialID() + "\t SkuCode:"
						+ param.getSkuCode() + "\t BaseUnit:" + param.getUnitCode() + "\t Material Type:"
						+ param.getMaterialType() + "\t UPC:" + param.getUpc() + "\t MaterialGroup:"
						+ param.getMaterialGroup() + "\t Taxable:" + param.getTaxable().booleanValue() + "\t Deleted:"
						+ param.getDeleted().booleanValue() + "\t VariantConfigExists:" + param.isVariantConfigExists());
			}

			return param;
		}

		/**
		 * @param variantConfigTable
		 * @return MaterialVariantParameter
		 */
		private MaterialVariantParameter populateVariantRecord(final JCoTable variantConfigTable) {
			final MaterialVariantParameter param = new MaterialVariantParameter();

			param.setMaterialID(FDSapHelperUtils.getString(variantConfigTable.getString("MATNR")));

			param.setConfigProfileName(FDSapHelperUtils.getString(variantConfigTable.getString("PRFID")));
			param.setClassName(FDSapHelperUtils.getString(variantConfigTable.getString("CLASS")));
			param.setDeafaultValue(FDSapHelperUtils.getString(variantConfigTable.getString("ATSTD")));

			param.setCharacteristicName(FDSapHelperUtils.getString(variantConfigTable.getString("ATNAM")));
			param.setCharacteristicValueName(FDSapHelperUtils.getString(variantConfigTable.getString("ATWRT")));
			param.setUnitOfDimension(FDSapHelperUtils.getString(variantConfigTable.getString("MEABM")));

			return param;
		}

		/**
		 * @param materialVariantPriceTable
		 * @return MaterialVariantPriceParameter
		 */
		private MaterialVariantPriceParameter populateVariantPriceRecord(final JCoTable materialVariantPriceTable) {
			final MaterialVariantPriceParameter param = new MaterialVariantPriceParameter();

			param.setMaterialID(FDSapHelperUtils.getString(materialVariantPriceTable.getString("MATNR")));
			param.setSalesOrganizationId(FDSapHelperUtils.getString(materialVariantPriceTable.getString("VKORG")));
			param.setDistributionChannelId(FDSapHelperUtils.getString(materialVariantPriceTable.getString("VTWEG")));

			param.setConfigProfileName(FDSapHelperUtils.getString(materialVariantPriceTable.getString("PRFID")));
			param.setClassName(FDSapHelperUtils.getString(materialVariantPriceTable.getString("CLASS")));
			// param.setDeafaultValue(FDSapHelperUtils.getString(materialVariantPriceTable.getString("ATSTD")));

			param.setCharacteristicName(FDSapHelperUtils.getString(materialVariantPriceTable.getString("ATNAM")));
			param.setCharacteristicValueName(FDSapHelperUtils.getString(materialVariantPriceTable.getString("ATWRT")));
			param.setCharacteristicValueDescription(FDSapHelperUtils.getString(materialVariantPriceTable
					.getString("ATWTB")));
			String price = materialVariantPriceTable.getString("KBETR");
			if(null ==price || "".equals(price)){
				price="0";
			}
			param.setPrice(Double.parseDouble(price));
			param.setPricingUnitCode(FDSapHelperUtils.getString(materialVariantPriceTable.getString("KMEIN")));

			param.setConditionType(FDSapHelperUtils.getString(materialVariantPriceTable.getString("KSCHL")));
			param.setConditionRecordNumber(FDSapHelperUtils.getString(materialVariantPriceTable.getString("KNUMH")));
			param.setValidEndDate(materialVariantPriceTable.getDate("DATBI"));
			param.setValidStartDate(materialVariantPriceTable.getDate("DATAB"));

			return param;
		}
	}

	/**
	 * Method to check if any batch in loading status
	 * 
	 * @throws EJBException
	 * @throws RemoteException
	 * @throws LoaderException
	 */
	private void checkBatchStatus() throws EJBException, RemoteException, LoaderException {

		Context ctx = null;
		ErpMaterialBatchHistoryModel batchModel;
		try {
			ctx = com.freshdirect.ErpServicesProperties.getInitialContext();

			SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");
			SAPLoaderSB sapLoader = home.create();

			try {
				
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SAPLoaderSB)){
					batchModel = FDECommerceService.getInstance().getMaterialBatchInfo();
				}else{
					batchModel = sapLoader.getMaterialBatchInfo();
				}

				if (batchModel != null) {
					int timeSinceBatchCreated = getDiffInMinutes(new Date(), batchModel.getCreatedDate());
					if (timeSinceBatchCreated < ErpServicesProperties.getMaterialBatchLoaderStatusExpiry()
							&& EnumApprovalStatus.LOADING.equals(batchModel.getStatus())) {
						throw new LoaderException(String.format(
								"There is already a batch %s, created [ %s ] and is In-progress",
								batchModel.getVersion(), batchModel.getCreatedDate()));
					}
				}
			} catch (RemoteException e) {
				LOG.error("Failed to check for material batch status ", e);
			} catch (LoaderException e) {
				throw e;
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
	 * @param d1
	 * @param d2
	 * @return int
	 */
	public static int getDiffInMinutes(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) 60 * 1000)));
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}
}
