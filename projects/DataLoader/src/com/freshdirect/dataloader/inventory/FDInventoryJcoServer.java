package com.freshdirect.dataloader.inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.ejb.ErpInventoryManagerHome;
import com.freshdirect.erp.ejb.ErpInventoryManagerSB;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
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
 * This class represents the processing of inventory / stock details sent by SAP
 * to Store front
 * 
 * @author kkanuganti
 * 
 */
public class FDInventoryJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDInventoryJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDInventoryJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("InventoryRepository");
		final JCoRecordMetaData metaMatList = JCo.createRecordMetaData("MATERIALINVENTORYLIST");

		tableMetaDataList.add(new TableMetaData("WERKS", JCoMetaData.TYPE_CHAR, 4, 0, "Plant ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material No."));
		tableMetaDataList.add(new TableMetaData("EDATU", JCoMetaData.TYPE_DATE, 8, 0, "Schedule Date"));
		tableMetaDataList.add(new TableMetaData("BMENG", JCoMetaData.TYPE_BCD, 7, 3, "Confirmed Qty")); // 13.3
																										// BCD
		tableMetaDataList.add(new TableMetaData("MEINS", JCoMetaData.TYPE_CHAR, 3, 0, "Sales / Base Units"));

		createTableRecord(metaMatList, tableMetaDataList);
		metaMatList.lock();
		repository.addRecordMetaDataToCache(metaMatList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("INVENTORY_IMPORTS");
		fmetaImport.add("MATERIALS", JCoMetaData.TYPE_TABLE, metaMatList, JCoListMetaData.IMPORT_PARAMETER);

		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("INVENTORY_EXPORTS");
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
			try {
				final JCoTable materialTable = function.getTableParameterList().getTable("MATERIALS");

				final Map<String, List<ErpInventoryEntryModel>> stockMap = new HashMap<String, List<ErpInventoryEntryModel>>();
				for (int i = 0; i < materialTable.getNumRows(); i++) {
					materialTable.setRow(i);

					final String plant = FDSapHelperUtils.getString(materialTable.getString("WERKS"));
					final String matNo = FDSapHelperUtils.getString(materialTable.getString("MATNR"));
					final Date startDate = materialTable.getDate("EDATU");
					final BigDecimal commitedQty = materialTable.getBigDecimal("BMENG");
					final String salesUnit = FDSapHelperUtils.getString(materialTable.getString("MEINS"));

					if (SapProperties.isInventoryExportLogEnabled()) {
						LOG.info("Got Inventory Record For Plant:" + plant + ", Material No:" + matNo
								+ ", Start Date:" + startDate + ", Committed Qty:" + commitedQty + ", Sales Unit:"
								+ salesUnit);
					}

					// do not process batch if any material fails as SAP resends
					// all inventory data for the batch
					if (plant != null) {
						if (!stockMap.containsKey(matNo)) {
							stockMap.put(matNo, new ArrayList<ErpInventoryEntryModel>());
						}
						/*
						 * if (!stockMap.get(matNo).containsKey(plant)) {
						 * stockMap.get(matNo).put(plant, new
						 * ArrayList<ErpInventoryEntryModel>()); }
						 * 
						 * stockMap.get(matNo).get(plant).add(new
						 * ErpInventoryEntryModel(startDate,
						 * commitedQty.doubleValue(), plant));
						 */
						stockMap.get(matNo)
								.add(new ErpInventoryEntryModel(startDate, commitedQty.doubleValue(), plant));
					} else {
						throw new LoaderException("Empty plant info for material" + "\t" + matNo + "\t" + startDate
								+ "\t" + commitedQty + "\t" + salesUnit);
					}

					materialTable.nextRow();
				}

				if (stockMap.size() > 0) {
					processStocklevels(stockMap);

					exportParamList.setValue("RETURN", "S");
					exportParamList.setValue("MESSAGE",
							String.format("Product inventory updated successfully! [ %s ]", new Date()));
				} else {
					exportParamList.setValue("RETURN", "W");
					exportParamList.setValue("MESSAGE", String.format(
							"No Product inventory to process. Please check the export data [ %s ]", new Date()));
				}
			} catch (final Exception e) {
				LOG.error("Error importing inventory: ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing inventory " + e.toString().substring(0, Math.min(230, e.toString().length())));
			}

		}
	}

	/**
	 * @param stockMap
	 * @param result
	 * @throws LoaderException
	 */
	private void processStocklevels(final Map<String, List<ErpInventoryEntryModel>> stockMap) throws LoaderException {
		Date currentDate = FDSapHelperUtils.CURRENT_DATE;
		try {
			List<ErpInventoryModel> stockEntries = new ArrayList<ErpInventoryModel>();

			for (final Map.Entry<String, List<ErpInventoryEntryModel>> materialEntry : stockMap.entrySet()) {
				final String matNo = materialEntry.getKey();
				/*
				 * for (final Map.Entry<String, List<ErpInventoryEntryModel>>
				 * materialWarehouseEntry : materialEntry.getValue().entrySet())
				 * { //final String warehouseCode =
				 * materialWarehouseEntry.getKey(); stockEntries.add(new
				 * ErpInventoryModel(matNo, currentDate,
				 * materialWarehouseEntry.getValue())); }
				 */
				stockEntries.add(new ErpInventoryModel(matNo, currentDate, materialEntry.getValue()));
			}

			if (stockEntries.size() > 0) {
				updateInventories(stockEntries);
			}
		} catch (final Exception e) {
			throw new LoaderException(e);
		}
	}

	/**
	 * Method to store stock entry(s) to the database
	 * 
	 * @param stockEntries
	 * @throws EJBException
	 */
	private void updateInventories(List<ErpInventoryModel> stockEntries) throws EJBException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpInventoryManagerHome mgr = (ErpInventoryManagerHome) ctx.lookup("freshdirect.erp.InventoryManager");
			ErpInventoryManagerSB sb = mgr.create();

			sb.updateInventories(stockEntries);
		} catch (Exception ex) {
			throw new EJBException(ex.toString());
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
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

}