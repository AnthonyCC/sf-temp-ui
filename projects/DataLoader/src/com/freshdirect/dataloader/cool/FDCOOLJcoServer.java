package com.freshdirect.dataloader.cool;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.ecomm.gateway.CountryOfOriginService;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.StringUtil;
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
 * This class will be used for populating the country list by warehouse for each
 * product via Jco-server registered to ERP system
 * 
 * Created by kkanuganti on 01/19/15.
 */
public class FDCOOLJcoServer extends FdSapServer {
	private static final Logger LOG = Logger.getLogger(FDCOOLJcoServer.class.getName());

	private String serverName;

	private String functionName;

	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDCOOLJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("ProductCoolRepository");

		final JCoRecordMetaData coolMetaList = JCo.createRecordMetaData("COOLLIST");

		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material No."));
		tableMetaDataList.add(new TableMetaData("WERKS", JCoMetaData.TYPE_CHAR, 4, 0, "Plant ID"));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, 0, "Material Description"));
		tableMetaDataList.add(new TableMetaData("COUNTRY1", JCoMetaData.TYPE_CHAR, 15, 0, "COUNTRY1"));
		tableMetaDataList.add(new TableMetaData("COUNTRY2", JCoMetaData.TYPE_CHAR, 15, 0, "COUNTRY2"));
		tableMetaDataList.add(new TableMetaData("COUNTRY3", JCoMetaData.TYPE_CHAR, 15, 0, "COUNTRY3"));
		tableMetaDataList.add(new TableMetaData("COUNTRY4", JCoMetaData.TYPE_CHAR, 15, 0, "COUNTRY4"));
		tableMetaDataList.add(new TableMetaData("COUNTRY5", JCoMetaData.TYPE_CHAR, 15, 0, "COUNTRY5"));

		createTableRecord(coolMetaList, tableMetaDataList);
		coolMetaList.lock();
		repository.addRecordMetaDataToCache(coolMetaList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("COOL_IMPORTS");
		fmetaImport.add("COUNTRY", JCoMetaData.TYPE_TABLE, coolMetaList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("COOL_EXPORTS");
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
			final FDJcoServerResult result = new FDJcoServerResult();
			try {
				final JCoTable coolTable = function.getTableParameterList().getTable("COUNTRY");

				/* MatNo -> PlantId -> CountryList */
				final Map<String, Map<String, List<String>>> countryOfOriginMap = new HashMap<String, Map<String, List<String>>>();

				for (int i = 0; i < coolTable.getNumRows(); i++) {
					coolTable.setRow(i);

					final String plant = FDSapHelperUtils.getString(coolTable.getString("WERKS"));
					final String matNo = FDSapHelperUtils.getString(coolTable.getString("MATNR"));
					// final String matDesc =
					// FDSapHelperUtils.getString(coolTable.getString("MAKTX"));
					final String country1 = FDSapHelperUtils.getString(coolTable.getString("COUNTRY1"));
					final String country2 = FDSapHelperUtils.getString(coolTable.getString("COUNTRY2"));
					final String country3 = FDSapHelperUtils.getString(coolTable.getString("COUNTRY3"));
					final String country4 = FDSapHelperUtils.getString(coolTable.getString("COUNTRY4"));
					final String country5 = FDSapHelperUtils.getString(coolTable.getString("COUNTRY5"));

					if (LOG.isDebugEnabled()) {
						LOG.debug("Got Cool Record Plant: " + plant + "\t" + "MatNo: " + matNo + "\t" + "country1: "
								+ country1 + "\t" + "country2: " + country2 + "\t" + "country3: " + country3 + "\t"
								+ "country4: " + country4 + "\t" + "country5:" + country5);
					}

					if (!countryOfOriginMap.containsKey(matNo)) {
						countryOfOriginMap.put(matNo, new HashMap<String, List<String>>());
					}
					if (!countryOfOriginMap.get(matNo).containsKey(plant)) {
						countryOfOriginMap.get(matNo).put(plant, new ArrayList<String>());
					}

					List<String> countryInfo = getCountryInfo(country1, country2, country3, country4, country5);

					countryOfOriginMap.get(matNo).put(plant, countryInfo);

					coolTable.nextRow();
				}

				processProductCountryOfOriginInfo(countryOfOriginMap, result);

				exportParamList.setValue("RETURN", "S");
				exportParamList.setValue("MESSAGE",
						String.format("Product Cool info updated successfully! [ %s ]", new Date()));

			} catch (final Exception e) {
				LOG.error("Error importing country of origin: ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue(
						"MESSAGE",
						"Error importing  country of origin: "
								+ e.toString().substring(0, Math.min(225, e.toString().length())));
			}
		}
	}

	/**
	 * @param countryOfOriginMap
	 * @param result
	 * @throws LoaderException
	 */
	private void processProductCountryOfOriginInfo(final Map<String, Map<String, List<String>>> countryOfOriginMap,
			final FDJcoServerResult result) throws LoaderException {
		List<ErpCOOLInfo> erpCOOLInfoList = new ArrayList<ErpCOOLInfo>();
		try {
			for (final Map.Entry<String, Map<String, List<String>>> materialEntry : countryOfOriginMap.entrySet()) {
				final String matNo = materialEntry.getKey();

				for (final Map.Entry<String, List<String>> materialPlantEntry : materialEntry.getValue().entrySet()) {
					final String plantId = materialPlantEntry.getKey();

					if (LOG.isDebugEnabled()) {
						LOG.debug(String.format("%s cool entry(s) for material ID: %s, plant ID: %s",
								materialPlantEntry.getValue().size(), matNo, plantId));
					}

					ErpCOOLInfo coolInfoModel = new ErpCOOLInfo(matNo, null, materialPlantEntry.getValue(), plantId);
					erpCOOLInfoList.add(coolInfoModel);
				}
			}

			if (erpCOOLInfoList.size() > 0) {
					storeCOOLInfo(erpCOOLInfoList);
				
			}
		} catch (final Exception e) {
			throw new LoaderException("Saving cool info failed. No update will happen. Exception is " + e);
		}
	}

	/**
	 * @param erpCOOLInfoList
	 */
	private void storeCOOLInfo(List<ErpCOOLInfo> erpCOOLInfoList) throws NamingException, EJBException,
			CreateException, FinderException, FDResourceException, RemoteException {
		
		try {
			LogisticsServiceLocator.getInstance().getCommerceService().saveCountryOfOriginData(erpCOOLInfoList);
			
		} catch (Exception ex) {
			throw new EJBException(ex.toString());
		} 
	}

	/**
	 * @param country1
	 * @param country2
	 * @param country3
	 * @param country4
	 * @param country5
	 * 
	 * @return List<String>
	 */
	private List<String> getCountryInfo(String country1, String country2, String country3, String country4,
			String country5) {

		List<String> countryInfo = new ArrayList<String>(3);

		if (!StringUtil.isEmpty(country1)) {
			countryInfo.add(country1);
		}
		if (!StringUtil.isEmpty(country2) && !StringUtil.isEmpty(country1)) {
			countryInfo.add(country2);
		}
		if (!StringUtil.isEmpty(country3) && !StringUtil.isEmpty(country2) && !StringUtil.isEmpty(country1)) {
			countryInfo.add(country3);
		}
		if (!StringUtil.isEmpty(country4) && !StringUtil.isEmpty(country3) && !StringUtil.isEmpty(country2)
				&& !StringUtil.isEmpty(country1)) {
			countryInfo.add(country4);
		}
		if (!StringUtil.isEmpty(country5) && !StringUtil.isEmpty(country4) && !StringUtil.isEmpty(country3)
				&& !StringUtil.isEmpty(country2) && !StringUtil.isEmpty(country1)) {
			countryInfo.add(country5);
		}
		return countryInfo;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName() {
		return serverName;
	}
}
