package com.freshdirect.dataloader.groupscale;

import static org.apache.commons.lang.BooleanUtils.toBooleanObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPGrpInfoLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SapGrpInfoLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.GroupScalePriceParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.ejb.ErpGrpInfoHome;
import com.freshdirect.erp.ejb.ErpGrpInfoSB;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.SalesAreaInfo;
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
 * This class represents the processing of group scale details sent by SAP to
 * Store front
 * 
 * @author kkanuganti
 * 
 */
public class FDGroupScalePriceJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDGroupScalePriceJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDGroupScalePriceJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDGroupScalePriceRepository");

		final JCoRecordMetaData metaGroupScalePriceList = JCo.createRecordMetaData("GRPSCALELIST");

		tableMetaDataList.add(new TableMetaData("KUNNR", JCoMetaData.TYPE_CHAR, 10, 0, "Customer Number"));
		tableMetaDataList.add(new TableMetaData("ZMATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material Number"));
		tableMetaDataList.add(new TableMetaData("ZGROUP_ID", JCoMetaData.TYPE_CHAR, 10, 0, "Group ID Number"));
		tableMetaDataList.add(new TableMetaData("ZGROUP_SHORT_DES", JCoMetaData.TYPE_CHAR, 30, 0, "Short Description"));
		tableMetaDataList.add(new TableMetaData("ZGROUP_LONG_DESC", JCoMetaData.TYPE_CHAR, 50, 0, "Long Description"));
		tableMetaDataList.add(new TableMetaData("ZZONE_ID", JCoMetaData.TYPE_CHAR, 10, 0, "Zone ID"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_QTY", JCoMetaData.TYPE_CHAR, 5, 0, "Group Scale Quantity"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_UOM", JCoMetaData.TYPE_CHAR, 3, 0, "Group Price Quantity UOM"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_PRICE", JCoMetaData.TYPE_CHAR, 13, 0, "Group Price"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_SUOM", JCoMetaData.TYPE_CHAR, 3, 0,
				"Group Scale Price Sales UOM"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_EXP_IND", JCoMetaData.TYPE_CHAR, 1, 0, "Expiry Indicator"));
		tableMetaDataList.add(new TableMetaData("ZSGRP_QTY_FLAG", JCoMetaData.TYPE_CHAR, 1, 0, "Qty 1 allowed flag"));
		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, 0, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, 0, "Distribution Channel"));

		createTableRecord(metaGroupScalePriceList, tableMetaDataList);
		metaGroupScalePriceList.lock();
		repository.addRecordMetaDataToCache(metaGroupScalePriceList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("GROUPSCALE_PRICE_IMPORTS");
		fmetaImport.add("ZSD_BAPI_SGRP_PRICE_WEB", JCoMetaData.TYPE_TABLE, metaGroupScalePriceList,
				JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("GROUPSCALE_PRICE_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null,
				null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER,
				null, null);
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
			FDJcoServerResult result = new FDJcoServerResult();
			try {
				final JCoTable groupTable = function.getTableParameterList().getTable("ZSD_BAPI_SGRP_PRICE_WEB");

				if(SapProperties.isGroupScaleExportLogEnabled()){
					LOG.info("*********** Group Scale Export Data ************");
					LOG.info(groupTable);
				}
				final Map<String, Map<String, List<GroupScalePriceParameter>>> salesAreaGroupScaleRecordMap = new HashMap<String, Map<String, List<GroupScalePriceParameter>>>();
				for (int i = 0; i < groupTable.getNumRows(); i++) {
					groupTable.setRow(i);

					final GroupScalePriceParameter gsParamRecord = populateGroupScaleRecord(groupTable);
					if (!salesAreaGroupScaleRecordMap.containsKey(gsParamRecord.getSalesOrganizationId())) {
						salesAreaGroupScaleRecordMap.put(gsParamRecord.getSalesOrganizationId(), new HashMap<String, List<GroupScalePriceParameter>>());
					}

					Map<String, List<GroupScalePriceParameter>> groupScaleRecordMap = salesAreaGroupScaleRecordMap.get(gsParamRecord.getSalesOrganizationId());
					if (!groupScaleRecordMap.containsKey(gsParamRecord.getGroupId())) {
						groupScaleRecordMap.put(gsParamRecord.getGroupId(), new ArrayList<GroupScalePriceParameter>());
					}

					groupScaleRecordMap.get(gsParamRecord.getGroupId()).add(gsParamRecord);

					groupTable.nextRow();
				}
				
				for (Iterator<String> iterator = salesAreaGroupScaleRecordMap.keySet().iterator(); iterator.hasNext();) {
					Map<String, List<GroupScalePriceParameter>> groupScaleRecordMap = salesAreaGroupScaleRecordMap.get(iterator.next());
					validateScaleGroupRecords(groupScaleRecordMap, result);
					populateGroupScale(groupScaleRecordMap);
				}
//				validateScaleGroupRecords(groupScaleRecordMap, result);

//				populateGroupScale(groupScaleRecordMap);

				if (FDJcoServerResult.OK_STATUS.equals(result.getStatus())) {
					exportParamList.setValue("RETURN", "S");
					exportParamList.setValue("MESSAGE",
							String.format("Group scale details imported successfully [ %s ]", new Date()));
				}

				// email failure report
				emailFailureReport(result, "Group Scale", null);
			} catch (final Exception e) {
				LOG.error("Error importing group scale -> ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing group scale "
								+ e.toString().substring(0, Math.min(230, e.toString().length())));
			}

		}

		/**
		 * @param groupScaleRecordMap
		 * @throws FDSapException
		 */
		private void validateScaleGroupRecords(final Map<String, List<GroupScalePriceParameter>> groupScaleRecordMap,
				final FDJcoServerResult result) throws LoaderException {
			final Map<String, String> materialToGroupMapping = new HashMap<String, String>();
			final Map<String, List<String>> activeScaleGroupToMaterialMapping = new HashMap<String, List<String>>();

			for (final Map.Entry<String, List<GroupScalePriceParameter>> groupScaleRecordEntry : groupScaleRecordMap
					.entrySet()) {
				final String scaleGroupId = groupScaleRecordEntry.getKey();

				for (final GroupScalePriceParameter scaleGroupRecord : groupScaleRecordEntry.getValue()) {
					if(null == scaleGroupRecord.getGrpShortDesc() || "".equalsIgnoreCase(scaleGroupRecord.getGrpShortDesc())){
//						result.addError(scaleGroupRecord.getGroupId(),"value for 'ZGROUP_SHORT_DES' can't be empty.");
						throw new LoaderException(
								"[SAP_INPUT_VALIDATION]: Value for the field 'ZGROUP_SHORT_DES' can't be empty. Group Id:"
										+ scaleGroupRecord.getGroupId());
					}
					final Boolean isScaleGroupExpired = toBooleanObject(
							StringUtils.lowerCase(groupScaleRecordEntry.getValue().get(0).getGrpExpiryIndicator()),
							"x", "", null);

					// check for active scale group
					if (Boolean.FALSE.equals(isScaleGroupExpired)) {
						if (materialToGroupMapping.containsKey(scaleGroupRecord.getMaterialID())
								&& !materialToGroupMapping.get(scaleGroupRecord.getMaterialID()).equals(scaleGroupId)) {
							// [ERROR:Same material in more than one scale group
							// in the export]
							result.addError(scaleGroupRecord.getMaterialID(),
									"Appeared in more than one active group in the batch");
						} else {
							materialToGroupMapping.put(scaleGroupRecord.getMaterialID(), scaleGroupId);
						}
					}

					// check if material is part of other active scale group
					String existingScaleGroupId = checkIfMaterialAlreadyExistsInActiveGroup(scaleGroupId,
							scaleGroupRecord);
					if (existingScaleGroupId != null && existingScaleGroupId.length() > 0) {
						if (!activeScaleGroupToMaterialMapping.containsKey(existingScaleGroupId)) {
							activeScaleGroupToMaterialMapping.put(existingScaleGroupId, new ArrayList<String>());
						}
						activeScaleGroupToMaterialMapping.get(existingScaleGroupId).add(
								scaleGroupRecord.getMaterialID());
					}
				}
			}

			if (FDStoreProperties.isValidationGroupExportInputEnabled() && result.getError().getMessages().size() > 0) {
				throw new LoaderException(
						"[SAP_INPUT_VALIDATION]: Same material appears in more than one active scale group. Material No(s): "
								+ result.getError().getMessages().keySet().toString());
			}

			if (activeScaleGroupToMaterialMapping.size() > 0) {
				for (final Map.Entry<String, List<GroupScalePriceParameter>> groupScaleRecordEntry : groupScaleRecordMap
						.entrySet()) {
					if (activeScaleGroupToMaterialMapping.containsKey(groupScaleRecordEntry.getKey())) {
						final Boolean isScaleGroupExpired = toBooleanObject(
								StringUtils.lowerCase(groupScaleRecordEntry.getValue().get(0).getGrpExpiryIndicator()),
								"x", null, null);

						// remove the group from active group map if active
						// group is expired or if this group no more contains
						// the material being checked against.
						if (Boolean.TRUE.equals(isScaleGroupExpired)) {
							activeScaleGroupToMaterialMapping.remove(groupScaleRecordEntry.getKey());
						} else {
							for (final GroupScalePriceParameter scaleGroupRecord : groupScaleRecordEntry.getValue()) {
								if (activeScaleGroupToMaterialMapping.get(groupScaleRecordEntry.getKey()) != null
										&& !activeScaleGroupToMaterialMapping.get(groupScaleRecordEntry.getKey())
												.contains(scaleGroupRecord.getMaterialID())) {
									activeScaleGroupToMaterialMapping.remove(groupScaleRecordEntry.getKey());
								}
							}
						}
					}
				}
			}

			if (FDStoreProperties.isValidationGroupExportEnabled() && activeScaleGroupToMaterialMapping.size() > 0) {
				throw new LoaderException(
						"[STOREFRONT_STATE_VALIDATION]: Material(s) from this export already exists in an active group. Material No(s): "
								+ activeScaleGroupToMaterialMapping.toString());
			}

		}

		/**
		 * @param groupScaleRecordMap
		 * @throws Exception
		 */
		private void populateGroupScale(final Map<String, List<GroupScalePriceParameter>> groupScaleRecordMap)
				throws Exception {
			List<ErpGrpPriceModel> scaleGroups = new ArrayList<ErpGrpPriceModel>();
			try {
				
				for (final Map.Entry<String, List<GroupScalePriceParameter>> scaleGroupRecordEntry : groupScaleRecordMap
						.entrySet()) {
					final String scaleGroupId = scaleGroupRecordEntry.getKey();

					ErpGrpPriceModel scaleGroupModel = new ErpGrpPriceModel();
					scaleGroups.add(scaleGroupModel);

					scaleGroupModel.setGrpId(scaleGroupId);

					final Boolean isScaleGroupExpired = toBooleanObject(
							StringUtils.lowerCase(scaleGroupRecordEntry.getValue().get(0).getGrpExpiryIndicator()),
							"x", "", null);

					scaleGroupModel.setActive(!isScaleGroupExpired.booleanValue());
					scaleGroupModel.setShortDesc(scaleGroupRecordEntry.getValue().get(0).getGrpShortDesc());
					scaleGroupModel.setLongDesc(scaleGroupRecordEntry.getValue().get(0).getGrpLongDesc());

					scaleGroupModel.setMatList(new HashSet<String>());

					

					Set<ErpGrpPriceZoneModel> zonePriceRows = null;
					Set<ZoneInfo> zoneInfos = null;
					if (scaleGroupModel.getZoneModelList() != null && !scaleGroupModel.getZoneModelList().isEmpty()) {
						zonePriceRows = new HashSet<ErpGrpPriceZoneModel>(scaleGroupModel.getZoneModelList());
					} else {
						zonePriceRows = new HashSet<ErpGrpPriceZoneModel>();
					}
					zoneInfos = new HashSet<ZoneInfo>();
					List<GroupScalePriceParameter> groupScalePriceParams = scaleGroupRecordEntry.getValue();
					for (GroupScalePriceParameter groupScalePriceParameter : groupScalePriceParams) {
						ZoneInfo zoneInfo=	new ZoneInfo(groupScalePriceParameter.getPricingZoneId(), groupScalePriceParameter.getSalesOrganizationId(), groupScalePriceParameter.getDistributionChannelId());
						if(!zoneInfos.contains(zoneInfo)){
							zoneInfos.add(zoneInfo);
							ErpGrpPriceZoneModel scaleGroupPriceRow = new ErpGrpPriceZoneModel(zoneInfo, 
									groupScalePriceParameter.getGrpScaleQuantity(), groupScalePriceParameter.getPricingUnitCode(),
									groupScalePriceParameter.getPrice(), groupScalePriceParameter.getScaleUnitCode());
							zonePriceRows.add(scaleGroupPriceRow);
						}
					}
					

					scaleGroupModel.setZoneModelList(zonePriceRows);

					for (final GroupScalePriceParameter scaleGroupRecord : scaleGroupRecordEntry.getValue()) {
						if (!scaleGroupModel.getMatList().contains(scaleGroupRecord.getMaterialID())) {
							scaleGroupModel.getMatList().add(scaleGroupRecord.getMaterialID());
						}

						/*
						 * ErpGrpPriceZoneModel scaleGroupPriceRow = new
						 * ErpGrpPriceZoneModel( new
						 * ZoneInfo(scaleGroupRecord.getPricingZoneId(),
						 * scaleGroupRecord.getSalesOrganizationId(),
						 * scaleGroupRecord.getDistributionChannelId()),
						 * scaleGroupRecord.getGrpScaleQuantity(),
						 * scaleGroupRecord.getPricingUnitCode(),
						 * scaleGroupRecord.getPrice(),
						 * scaleGroupRecord.getScaleUnitCode() );
						 * 
						 * Set<ErpGrpPriceZoneModel> zonePriceRows = null; if
						 * (scaleGroupModel.getZoneModelList() != null &&
						 * !scaleGroupModel.getZoneModelList().isEmpty()) {
						 * zonePriceRows = new
						 * HashSet<ErpGrpPriceZoneModel>(scaleGroupModel
						 * .getZoneModelList()); } else { zonePriceRows = new
						 * HashSet<ErpGrpPriceZoneModel>(); }
						 * zonePriceRows.add(scaleGroupPriceRow);
						 * 
						 * scaleGroupModel.setZoneModelList(zonePriceRows);
						 */
					}
				}

				if (scaleGroups.size() > 0) {
					storeScaleGroups(scaleGroups);
				}

				LOG.info(String.format("Processing scale group successfull [%s] ", new Date()));
			} catch (final Exception e) {
				LOG.error("Processing scale group failed. Exception is ", e);
				throw new Exception(e);
			}
		}

		/**
		 * @param scaleGroups
		 * @throws EJBException
		 */
		private void storeScaleGroups(List<ErpGrpPriceModel> scaleGroups) throws EJBException {
			Context ctx = null;
			String saleId = null;
			try {
				ctx = ErpServicesProperties.getInitialContext();

				LOG.info(String.format("Storing scale group(s) [%s], [%s] ", scaleGroups.size(), new Date()));

				SAPGrpInfoLoaderHome mgr = (SAPGrpInfoLoaderHome) ctx.lookup("freshdirect.dataloader.SAPGrpInfoLoader");
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("sap.ejb.SapGrpInfoLoaderSB")){
					FDECommerceService.getInstance().loadGroupPriceData(scaleGroups);
				}else{
				SapGrpInfoLoaderSB sb = mgr.create();
				sb.loadData(scaleGroups);
				}

			} catch (Exception ex) {
				throw new EJBException("Failed to store: " + saleId + "Msg: " + ex.toString());
			} finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
					}
				}
			}
		}

		/**
		 * @param groupTable
		 * @return the group scale record
		 */
		private GroupScalePriceParameter populateGroupScaleRecord(final JCoTable groupTable) {
			final GroupScalePriceParameter param = new GroupScalePriceParameter();

			param.setMaterialID(FDSapHelperUtils.getString(groupTable.getString("ZMATNR")));

			param.setGroupId(FDSapHelperUtils.getString(groupTable.getString("ZGROUP_ID")));
			param.setGrpShortDesc(FDSapHelperUtils.getString(groupTable.getString("ZGROUP_SHORT_DES")));
			param.setGrpLongDesc(FDSapHelperUtils.getString(groupTable.getString("ZGROUP_LONG_DESC")));

			param.setPricingZoneId(FDSapHelperUtils.getString(groupTable.getString("ZZONE_ID")));

			param.setGrpScaleQuantity(FDSapHelperUtils.getInt(groupTable.getString("ZSGRP_QTY")));
			param.setPricingUnitCode(FDSapHelperUtils.getString(groupTable.getString("ZSGRP_UOM")));
			param.setPrice(FDSapHelperUtils.getDouble(groupTable.getString("ZSGRP_PRICE")));
			param.setScaleUnitCode(FDSapHelperUtils.getString(groupTable.getString("ZSGRP_SUOM")));
			param.setGrpExpiryIndicator(FDSapHelperUtils.getString(groupTable.getString("ZSGRP_EXP_IND")));

			param.setSalesOrganizationId(FDSapHelperUtils.getString(groupTable.getString("VKORG")));
			param.setDistributionChannelId(FDSapHelperUtils.getString(groupTable.getString("VTWEG")));

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got scale group record for Material No:" + param.getMaterialID() + "\t SalesOrg:"
						+ param.getSalesOrganizationId() + "\t DistChannel:" + param.getDistributionChannelId()
						+ "\t GroupId:" + param.getGroupId() + "\t SalesOrg:" + param.getSalesOrganizationId()
						+ "\t DistChannel:" + param.getDistributionChannelId() + "\t ZoneId:"
						+ param.getPricingZoneId() + "\t UnitCode:" + param.getPricingUnitCode() + "\t ScaleUnitCode:"
						+ param.getScaleUnitCode() + "\t Quantity:" + param.getGrpScaleQuantity() + "\t Active:"
						+ param.getGrpExpiryIndicator());
			}

			return param;
		}
	}

	/**
	 * Method to check if material is part of ACTIVE scale group
	 * 
	 * @param grpId
	 * @param matId
	 * @return
	 */
	private String checkIfMaterialAlreadyExistsInActiveGroup(String grpId, GroupScalePriceParameter scaleGrpRecord) {
		Context ctx = null;
		String existingGrpId = null;
		FDGroup group=null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpGrpInfoHome mgr = (ErpGrpInfoHome) ctx.lookup("freshdirect.erp.GrpInfoManager");
			ErpGrpInfoSB sb = mgr.create();
			Map<SalesAreaInfo, FDGroup> salesAreaGroup = null;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpGrpInfoSB")){
				salesAreaGroup = FDECommerceService.getInstance().getGroupIdentitiesForMaterial(scaleGrpRecord.getMaterialID());
			}else{
			salesAreaGroup=sb.getGroupIdentitiesForMaterial(scaleGrpRecord.getMaterialID());
			}
			SalesAreaInfo salesArea = new SalesAreaInfo(scaleGrpRecord.getSalesOrganizationId(), scaleGrpRecord.getDistributionChannelId());
			if(null != salesAreaGroup && salesAreaGroup.containsKey(salesArea)){
				group =salesAreaGroup.get(salesArea);
			}

			if (group != null && !group.getGroupId().equals(grpId)) {// and its
																		// does
																		// not
																		// match
																		// with
																		// current
																		// active
																		// group.
				existingGrpId = group.getGroupId();
			}
		} catch (Exception ex) {
			throw new EJBException("Failed to validate if material already exists in an active Group: " + grpId
					+ ", Material ID:" + scaleGrpRecord.getMaterialID() + " Exception Msg: " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
		return existingGrpId;
	}

	/**
	 * @param serverName
	 *            the serverName to set
	 */
	public void setServerName(final String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName
	 *            the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

}