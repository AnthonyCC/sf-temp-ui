package com.freshdirect.dataloader.material;

import static org.apache.commons.lang.BooleanUtils.toBooleanObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.SAPConstants;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer.TableMetaData;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialPlantParameter;
import com.freshdirect.dataloader.sap.jco.server.param.MaterialSalesAreaParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DayOfWeekSet;
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
 * This class will be used for populating the plant material attributes via
 * Jco-server registered to ERP system
 * 
 * Created by kkanuganti on 02/11/15.
 */
public class FDPlantProductJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDPlantProductJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDPlantProductJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {

		final JCoCustomRepository repository = JCo.createCustomRepository("FDProductPlantRepository");

		final JCoRecordMetaData metaPlantMaterialList = JCo.createRecordMetaData("MATPLANTLIST");

		tableMetaDataList.add(new TableMetaData("WERKS", JCoMetaData.TYPE_CHAR, 4, "Plant ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, "Material Description"));
		tableMetaDataList.add(new TableMetaData("ZZDEPT", JCoMetaData.TYPE_CHAR, 3, "Department"));
		tableMetaDataList.add(new TableMetaData("ZZDESC", JCoMetaData.TYPE_CHAR, 30, "Department Description"));
		// tableMetaDataList.add(new TableMetaData("VRKME",
		// JCoMetaData.TYPE_CHAR, 3, "Sales Unit"));
		tableMetaDataList.add(new TableMetaData("MTVFP", JCoMetaData.TYPE_CHAR, 2, "Availability check / ATP Rule"));
		tableMetaDataList
				.add(new TableMetaData("WEBAZ", JCoMetaData.TYPE_CHAR, 3, "Lead time / Planned delivery time"));
		tableMetaDataList.add(new TableMetaData("WZEIT", JCoMetaData.TYPE_CHAR, 3,
				"Total replenishment lead time (in workdays) / In-house production date"));
		tableMetaDataList.add(new TableMetaData("DZEIT", JCoMetaData.TYPE_NUM, 3, "Days In-house"));
		tableMetaDataList.add(new TableMetaData("ZZATP1", JCoMetaData.TYPE_CHAR, 2, "Monday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP2", JCoMetaData.TYPE_CHAR, 2, "Tuesday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP3", JCoMetaData.TYPE_CHAR, 2, "Wednesday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP4", JCoMetaData.TYPE_CHAR, 2, "Thursday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP5", JCoMetaData.TYPE_CHAR, 2, "Friday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP6", JCoMetaData.TYPE_CHAR, 2, "Saturday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZATP7", JCoMetaData.TYPE_CHAR, 2, "Sunday ATP Rule"));
		tableMetaDataList.add(new TableMetaData("ZZDTYPE", JCoMetaData.TYPE_CHAR, 1,
				"Department Type (Production / Non-Production / Other) / Kosher indicator"));
		tableMetaDataList.add(new TableMetaData("ZZIHIVI", JCoMetaData.TYPE_CHAR, 1,
				"Indicator: Highly Viscous / Special cut-off indicator"));
		tableMetaDataList.add(new TableMetaData("ZZAESZN", JCoMetaData.TYPE_CHAR, 6,
				"Document change number / Day specific indicator"));
		// tableMetaDataList.add(new TableMetaData("WESCH",
		// JCoMetaData.TYPE_CHAR, 3, 0, "Number of Days Fresh"));
		tableMetaDataList.add(new TableMetaData("RATING", JCoMetaData.TYPE_CHAR, 3, "SKU Rating"));
		tableMetaDataList.add(new TableMetaData("NORMT", JCoMetaData.TYPE_CHAR, 3, "Industry Standard Description"));
		tableMetaDataList.add(new TableMetaData("SEARK", JCoMetaData.TYPE_CHAR, 2, "FD RankNumber for Seafood Material"));
		tableMetaDataList.add(new TableMetaData("ZZHOO", JCoMetaData.TYPE_CHAR, 1, "Hide out of stock"));

		createTableRecord(metaPlantMaterialList, tableMetaDataList);
		metaPlantMaterialList.lock();
		repository.addRecordMetaDataToCache(metaPlantMaterialList);

		final JCoRecordMetaData metaSalesAreaMaterialList = JCo.createRecordMetaData("MAT_SALESAREA_LIST");
		tableMetaDataList = new ArrayList<TableMetaData>();

		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("VMSTA", JCoMetaData.TYPE_CHAR, 2,
				"Dist. Chain status / Material Status"));
		tableMetaDataList.add(new TableMetaData("VMSTB", JCoMetaData.TYPE_CHAR, 25, "Dist. Chain status Description"));
		tableMetaDataList.add(new TableMetaData("VMSTD", JCoMetaData.TYPE_CHAR, 8,
				"Date from availability Status valid"));
		tableMetaDataList.add(new TableMetaData("ZZDAYPART", JCoMetaData.TYPE_CHAR, 4,"Daypart Selling"));
		tableMetaDataList.add(new TableMetaData("PICKING_PLANT", JCoMetaData.TYPE_CHAR, 4,"Picking Plant")); 
		createTableRecord(metaSalesAreaMaterialList, tableMetaDataList);
		metaSalesAreaMaterialList.lock();
		repository.addRecordMetaDataToCache(metaSalesAreaMaterialList);

		/*
		 * final JCoListMetaData fmetaImport =
		 * JCo.createListMetaData("MATERIAL_PLANT_IMPORTS");
		 * fmetaImport.add("T_MATERIAL_PLANT", JCoMetaData.TYPE_TABLE,
		 * metaPlantMaterialList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.add("T_MAT_SALES_AREA", JCoMetaData.TYPE_TABLE,
		 * metaSalesAreaMaterialList, JCoListMetaData.IMPORT_PARAMETER);
		 * fmetaImport.lock();
		 */

		final JCoRecordMetaData metaPlantMaterialReturnRecord = JCo.createRecordMetaData("PLANT_MATERIAL_RETURN_LIST");

		tableMetaDataList = new ArrayList<TableMetaData>();
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, "Material No."));
		tableMetaDataList.add(new TableMetaData("ERROR", JCoMetaData.TYPE_CHAR, 220, "Error Message"));

		createTableRecord(metaPlantMaterialReturnRecord, tableMetaDataList);
		metaPlantMaterialReturnRecord.lock();
		repository.addRecordMetaDataToCache(metaPlantMaterialReturnRecord);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("MATERIAL_PLANT_IMPORTS");
		fmetaImport.add("T_MATERIAL_PLANT", JCoMetaData.TYPE_TABLE, metaPlantMaterialList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_SALES_AREA", JCoMetaData.TYPE_TABLE, metaSalesAreaMaterialList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaPlantMaterialReturnRecord,
				JCoListMetaData.EXPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("T_MAT_GLOBAL_ERR");
		fmetaExport.add("T_MAT_GLOBAL_ERR", JCoMetaData.TYPE_TABLE, metaPlantMaterialReturnRecord,
				JCoListMetaData.EXPORT_PARAMETER);
		// fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null,
		// JCoListMetaData.EXPORT_PARAMETER, null, null);
		// fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 220, 0, 0, null,
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
			final FDJcoServerResult result = new FDJcoServerResult();
			try {
				final JCoTable materialPlantTable = function.getTableParameterList().getTable("T_MATERIAL_PLANT");
				final JCoTable materialSalesAreaTable = function.getTableParameterList().getTable("T_MAT_SALES_AREA");
				final JCoTable materialErrorTable = function.getTableParameterList().getTable("T_MAT_GLOBAL_ERR");
				Map<String, List<ErpPlantMaterialModel>> materialPlantsMap = new HashMap<String, List<ErpPlantMaterialModel>>();
				Map<String, List<ErpMaterialSalesAreaModel>> materialSalesAreasMap = new HashMap<String, List<ErpMaterialSalesAreaModel>>();

				if(true/*SapProperties.isMaterialPlantExportLogEnabled()*/){
					LOG.info("******************* Material Plant Data ************");
					LOG.info(materialPlantTable);
					LOG.info("******************* Material Sales Area Data ************");
					LOG.info(materialSalesAreaTable);
				}
				// populate plant info
				for (int i = 0; i < materialPlantTable.getNumRows(); i++) {
					materialPlantTable.setRow(i);
					String materialNo = FDSapHelperUtils.getString(materialPlantTable.getString("MATNR"));
					List<ErpPlantMaterialModel> plants = materialPlantsMap.get(materialNo);
					if (null == plants) {
						plants = new ArrayList<ErpPlantMaterialModel>();
						materialPlantsMap.put(materialNo, plants);
					}

					ErpPlantMaterialModel param = populatePlantMaterialModel(materialPlantTable);
					plants.add(param);

					materialPlantTable.nextRow();
				}

				Set<String> errMaterials = new HashSet<String>();
				// populate salesarea info
				for (int i = 0; i < materialSalesAreaTable.getNumRows(); i++) {
					materialSalesAreaTable.setRow(i);
					String materialNo = FDSapHelperUtils.getString(materialSalesAreaTable.getString("MATNR"));
					List<ErpMaterialSalesAreaModel> salesAreas = materialSalesAreasMap.get(materialNo);
					if (null == salesAreas) {
						salesAreas = new ArrayList<ErpMaterialSalesAreaModel>();
						materialSalesAreasMap.put(materialNo, salesAreas);
					}
					ErpMaterialSalesAreaModel param = populateMaterialSalesAreaModel(materialSalesAreaTable);
					if(FDStoreProperties.isPickPlantIdReqForMatSalesOrgExport() && (null == param.getPickingPlantId() || param.getPickingPlantId().isEmpty())){
						populateErrorResponse(result, materialErrorTable, materialNo, "Picking plant id is missing for sales org:"+param.getSalesOrg());
						LOG.info("Picking plant id is missing for sales org:"+param.getSalesOrg()+" and material:"+materialNo);
						errMaterials.add(materialNo);
					}
					
					salesAreas.add(param);
					

					materialSalesAreaTable.nextRow();
				}
				
				//Don't persist any info of the material with errors.
				if(FDStoreProperties.isPickPlantIdReqForMatSalesOrgExport() && !errMaterials.isEmpty()){
					for (Iterator iterator = errMaterials.iterator(); iterator.hasNext();) {
						String materialNo = (String) iterator.next();
						materialSalesAreasMap.remove(materialNo);
						materialPlantsMap.remove(materialNo);
					}
				}

				// save plant and salesarea info
				loadPlantAndSalesAreas(materialPlantsMap, materialSalesAreasMap, result, materialErrorTable);
				exportParamList.setValue("T_MAT_GLOBAL_ERR", materialErrorTable);
			} catch (final Exception e) {
				LOG.error("Error importing material plant details: ", e);
			}
		}

	}

	private void populateErrorResponse(final FDJcoServerResult result, final JCoTable materialErrorTable,
			String materialNo, String error) {
		materialErrorTable.appendRow();
		materialErrorTable.setValue("MATNR", materialNo);
		materialErrorTable.setValue("ERROR", error);
		if (result != null) {
			result.addError(materialNo, error);
		}
	}

	/**
	 * @param materialPlantTable
	 * @return MaterialPlantParameter
	 */
	public ErpPlantMaterialModel populatePlantMaterialModel(final JCoTable materialPlantTable) {
		final ErpPlantMaterialModel param = new ErpPlantMaterialModel();
		String materialNum = materialPlantTable.getString("MATNR");
		param.setPlantId(FDSapHelperUtils.getString(materialPlantTable.getString("WERKS")));

//		param.setAtpRule(EnumATPRule.getEnum(materialPlantTable.getString("MTVFP"))); // atp rule
		param.setAtpRule(decodeATPRule(materialPlantTable.getString("MTVFP"))); // atp rule

		try {
			param.setLeadTime(Integer.parseInt(FDSapHelperUtils.getString(materialPlantTable.getString("WEBAZ"))));
		} catch (NumberFormatException e) {
			param.setLeadTime(0);
		}

		param.setKosherProduction("1".equals(StringUtils.lowerCase(materialPlantTable.getString("ZZDTYPE"))));
		param.setPlatter("1".equalsIgnoreCase(StringUtils.lowerCase(materialPlantTable.getString("ZZIHIVI"))));

//		param.setBlockedDays(DayOfWeekSet.decode(StringUtils.lowerCase(materialPlantTable.getString("ZZAESZN"))));
		DayOfWeekSet allowedDays = DayOfWeekSet.decode(StringUtils.lowerCase(materialPlantTable.getString("ZZAESZN")));
		if (!allowedDays.isEmpty()) {
			param.setBlockedDays(allowedDays.inverted());
		}

		param.setDays_in_house(FDSapHelperUtils.getString(materialPlantTable.getString("NORMT")));

		param.setRating(FDSapHelperUtils.getString(materialPlantTable.getString("RATING")));
		param.setSustainabilityRating(FDSapHelperUtils.getString(materialPlantTable.getString("SEARK")));
		param.setHideOutOfStock("X".equalsIgnoreCase(materialPlantTable.getString("ZZHOO")));

		if (LOG.isDebugEnabled()) {
			LOG.debug("Got material plant record for Plant:" + param.getPlantId() + "\t Material No:" + materialNum
					+ "\t MaterialGroup:" + param.getAtpRule() + "\t LeadTime:" + param.getLeadTime() + "\t Blocked:"
					+ param.getBlockedDays() + "\t DaysInhouse:" + param.getDays_in_house() + "\t ExpertRating:"
					+ param.getRating() + "\t SustainabilityRating:" + param.getSustainabilityRating()
					+ "\t Hide Out of Stock:" + param.isHideOutOfStock());
		}
		return param;
	}
	private EnumATPRule decodeATPRule(String token) {
    	if ("ZP".equals(token)) {
    		return EnumATPRule.SIMULATE;
    	}
    	if ("KP".equals(token) || "ZA".equals(token)) {
    		return EnumATPRule.JIT;
    	}
    	if ("ZC".equals(token)) {
    		return EnumATPRule.COMPONENT;
    	}
    	if("ZF".equals(token)) {
    		return EnumATPRule.MULTILEVEL_MATERIAL;
    	}
    	return EnumATPRule.MATERIAL;
    }

	public ErpMaterialSalesAreaModel populateMaterialSalesAreaModel(final JCoTable materialSalesAreaTable) {
		final ErpMaterialSalesAreaModel salesAreaModel = new ErpMaterialSalesAreaModel();
		String materialNum = materialSalesAreaTable.getString("MATNR");
		salesAreaModel.setSalesOrg(materialSalesAreaTable.getString("VKORG"));
		salesAreaModel.setDistChannel(materialSalesAreaTable.getString("VTWEG"));
		salesAreaModel.setUnavailabilityStatus(FDSapHelperUtils.getString(materialSalesAreaTable.getString("VMSTA")));
		salesAreaModel.setUnavailabilityReason(FDSapHelperUtils.getString(materialSalesAreaTable.getString("VMSTB")));
		// salesAreaModel.setUnavailabilityDate(materialSalesAreaTable.getDate("VMSTD"));
		salesAreaModel.setDayPartSelling(FDSapHelperUtils.getString(materialSalesAreaTable.getString("ZZDAYPART")));
		salesAreaModel.setPickingPlantId(FDSapHelperUtils.getString(materialSalesAreaTable.getString("PICKING_PLANT")));

		if ("33".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())
				|| "30".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())
				|| "35".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())) {
			salesAreaModel.setUnavailabilityReason("Discontinued by SAP");//salesAreaModel.getUnavailabilityReason());
			salesAreaModel.setUnavailabilityStatus("DISC");
			salesAreaModel.setUnavailabilityDate(new java.util.Date());
		} else if ("31".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())) {
			salesAreaModel.setUnavailabilityReason("Temporarily Unavailable");//salesAreaModel.getUnavailabilityReason());
			salesAreaModel.setUnavailabilityStatus("UNAV");
			salesAreaModel.setUnavailabilityDate(SAPConstants.THE_FUTURE);
		} else if ("32".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())) {
			salesAreaModel.setUnavailabilityReason("Out Of Season");//salesAreaModel.getUnavailabilityReason());
			salesAreaModel.setUnavailabilityStatus("SEAS");
			salesAreaModel.setUnavailabilityDate(SAPConstants.THE_FUTURE);
		} else if ("34".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())) {
			salesAreaModel.setUnavailabilityReason("Testing");//salesAreaModel.getUnavailabilityReason());
			salesAreaModel.setUnavailabilityStatus("TEST");
			salesAreaModel.setUnavailabilityDate(SAPConstants.THE_FUTURE);
		} else if ("40".equalsIgnoreCase(salesAreaModel.getUnavailabilityStatus())) {
			salesAreaModel.setUnavailabilityReason("Discontinued Soon");//salesAreaModel.getUnavailabilityReason());
			salesAreaModel.setUnavailabilityStatus("TBDS");
			salesAreaModel.setUnavailabilityDate(SAPConstants.THE_FUTURE);
		}
		else {
			salesAreaModel.setUnavailabilityReason("");
			salesAreaModel.setUnavailabilityStatus("");
		}
		salesAreaModel.setUnavailabilityDate(SAPConstants.THE_FUTURE);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Got material salesarea record for Material No:" + materialNum + "\t SalesArea:"
					+ salesAreaModel.getSalesOrg() + "\t Distribution Channel:" + salesAreaModel.getDistChannel()
					+ "\t Material Status:" + salesAreaModel.getUnavailabilityStatus() + "\t Material Status Desc:"
					+ salesAreaModel.getUnavailabilityReason() + "\t Material Availability Date:"
					+ salesAreaModel.getUnavailabilityDate());
		}
		return salesAreaModel;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	private void loadPlantAndSalesAreas(Map<String, List<ErpPlantMaterialModel>> materialPlantsMap,
			Map<String, List<ErpMaterialSalesAreaModel>> materialSalesAreaMap, final FDJcoServerResult result,
			final JCoTable materialErrorTable) throws EJBException, RemoteException, CreateException, LoaderException {
		Context ctx = null;
		int batchNumber = 0;
		try {
			ctx = com.freshdirect.ErpServicesProperties.getInitialContext();

			SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");
			SAPLoaderSB sapLoader = home.create();
			// create a new batch
			batchNumber = sapLoader.createBatch();
			for (Iterator<String> iterator = materialPlantsMap.keySet().iterator(); iterator.hasNext();) {
				String materialNo = iterator.next();
				List<ErpPlantMaterialModel> plantModels = materialPlantsMap.get(materialNo);
				List<ErpMaterialSalesAreaModel> salesAreas = materialSalesAreaMap.get(materialNo);

				try {
					sapLoader.loadMaterialPlantsAndSalesAreas(batchNumber, materialNo, plantModels, salesAreas);
				} catch (Exception e) {
					LOG.error("Saving Plant Records for material# " + materialNo + " failed. Exception is ", e);
					populateErrorResponse(result, materialErrorTable, materialNo, e.toString());
				}
				// mark the batch status
				sapLoader.updateBatchStatus(batchNumber, EnumApprovalStatus.NEW);

			}
		} catch (NamingException e) {
			LOG.warn("Failed to look up session bean home", e);
			e.printStackTrace();
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOG.warn("Error closing naming context", ne);
			}
		}

	}
}
