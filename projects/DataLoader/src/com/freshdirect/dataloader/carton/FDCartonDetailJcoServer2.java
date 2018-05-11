package com.freshdirect.dataloader.carton;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.CartonDetailParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.ecomm.gateway.OrderResourceApiClient;
import com.freshdirect.ecomm.gateway.OrderResourceApiClientI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
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
 * This class represents the processing of carton details sent by SAP to Store front
 *
 * @author kkanuganti
 *
 */
public class FDCartonDetailJcoServer2 extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDCartonDetailJcoServer2.class.getName());

	private String serverName;

	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDCartonDetailJcoServer2(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("CartonDetailRepository");
		final JCoRecordMetaData metaCartonDetailList = JCo.createRecordMetaData("CARTONDETAILS");

		tableMetaDataList.add(new TableMetaData("BSTNK", JCoMetaData.TYPE_CHAR, 20, 0, "Web Order No"));
		tableMetaDataList.add(new TableMetaData("VBELN", JCoMetaData.TYPE_CHAR, 10, 0, "Sap Order No"));
		tableMetaDataList.add(new TableMetaData("CARTON", JCoMetaData.TYPE_CHAR, 20, 0, "Carton Number"));
		tableMetaDataList.add(new TableMetaData("CRT_TYP", JCoMetaData.TYPE_CHAR, 2, 0, "Carton Type"));
		tableMetaDataList.add(new TableMetaData("PARENT_LN", JCoMetaData.TYPE_NUM, 6, 0, "Parent Line Number"));
		tableMetaDataList.add(new TableMetaData("CHILD_LN", JCoMetaData.TYPE_NUM, 6, 0, "Child Line Number"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material Number"));
		tableMetaDataList.add(new TableMetaData("MAKTX", JCoMetaData.TYPE_CHAR, 40, 0, "Material Description"));
		tableMetaDataList.add(new TableMetaData("BARCODE", JCoMetaData.TYPE_CHAR, 20, 0, "Packet Barcode"));
		tableMetaDataList.add(new TableMetaData("ACT_QTY", JCoMetaData.TYPE_CHAR, 18, 0, "Actual Quantity"));
		tableMetaDataList.add(new TableMetaData("PACK_QTY", JCoMetaData.TYPE_CHAR, 18, 0, "Packaged Quantity"));
		tableMetaDataList.add(new TableMetaData("PACK_UOM", JCoMetaData.TYPE_CHAR, 3, 0, "Packed UOM"));
		tableMetaDataList.add(new TableMetaData("NTGEW", JCoMetaData.TYPE_CHAR, 16, 0, "Net Weight"));
		tableMetaDataList.add(new TableMetaData("GEWEI", JCoMetaData.TYPE_CHAR, 3, 0, "Weight Height"));
		tableMetaDataList.add(new TableMetaData("SHIP_STAT", JCoMetaData.TYPE_CHAR, 2, 0, "Shipping Status"));
		tableMetaDataList.add(new TableMetaData("SUB_MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Sub Material Number"));
		tableMetaDataList.add(new TableMetaData("WEBID", JCoMetaData.TYPE_CHAR, 18, 0, "Sku Code"));
		
		createTableRecord(metaCartonDetailList, tableMetaDataList);
		metaCartonDetailList.lock();
		repository.addRecordMetaDataToCache(metaCartonDetailList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("CARTON_IMPORTS");
		fmetaImport.add("CARTON", JCoMetaData.TYPE_TABLE, metaCartonDetailList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("CARTON_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null, fmetaImport, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	}

	@Override
	protected FDSapFunctionHandler getHandler()
	{

		return new FDConnectionHandler();
	}

	protected class FDConnectionHandler extends FDSapFunctionHandler implements JCoServerFunctionHandler
	{
		@Override
		public String getFunctionName()
		{
			return functionName;
		}

		public void handleRequest(final JCoServerContext serverCtx, final JCoFunction function)
		{
			final JCoParameterList exportParamList = function.getExportParameterList();
			try
			{
				final JCoTable cartonTable = function.getTableParameterList().getTable("CARTON");

				final Map<String, List<ErpCartonInfo>> orderTocartonMapping = new HashMap<String, List<ErpCartonInfo>>();
				
				if (cartonTable != null)
				{
					String currentOrderNo = StringUtils.EMPTY;
					String currentCartonNo = StringUtils.EMPTY;
					
					ErpCartonInfo cartonModel = null;
					for (int i = 0; i < cartonTable.getNumRows(); i++)
					{
						cartonTable.setRow(i);

						final CartonDetailParameter param = populateCartonEntryRecord(cartonTable);

						if (!currentOrderNo.equals(param.getOrderNo()))
						{
							orderTocartonMapping.put(param.getOrderNo(), new ArrayList<ErpCartonInfo>());
							currentOrderNo = param.getOrderNo();
						}

						if (!currentCartonNo.equals(param.getCartonNumber()))
						{
							cartonModel = new ErpCartonInfo(currentOrderNo, param.getSapOrderNo(), param.getCartonNumber(), param.getCartonType());
							if(null ==orderTocartonMapping.get(param.getOrderNo())){
								orderTocartonMapping.put(param.getOrderNo(),new ArrayList<ErpCartonInfo>());
							}
							orderTocartonMapping.get(param.getOrderNo()).add(cartonModel);
							currentCartonNo = param.getCartonNumber();
						}

						ErpCartonDetails cartonDetailModel = new ErpCartonDetails();
						
						cartonDetailModel.setCartonInfo(cartonModel);
						cartonDetailModel.setOrderLineNumber(param.getOrderLineNo());
						cartonDetailModel.setChildOrderLineNo(param.getChildOrderLineNo());
						cartonDetailModel.setMaterialDesc(param.getMaterial_description());
						cartonDetailModel.setActualQuantity(param.getActual_quantity());
						cartonDetailModel.setPacked_uom(param.getPacked_uom());
						cartonDetailModel.setShipping_status(param.getShipping_status());
						cartonDetailModel.setSub_material_number(param.getSub_material_number());
						
						cartonDetailModel.setMaterialNumber(param.getMaterialNumber());
						cartonDetailModel.setBarcode(param.getBarcode());
						cartonDetailModel.setOrdered_quantity(param.getOrdered_quantity());
						cartonDetailModel.setNetWeight(param.getNetWeight());
						cartonDetailModel.setWeightUnit(param.getSalesUnitCode());
						cartonDetailModel.setSkuCode(param.getSkuCode());
						
						cartonModel.getDetails().add(cartonDetailModel);

						cartonTable.nextRow();
					}
				}

				if (orderTocartonMapping.size() > 0)
				{
					if(LOG.isDebugEnabled())
					{
						LOG.debug("Storing carton content info for " + orderTocartonMapping.size() + " orders");
					}
					
					storeCartonInfo(orderTocartonMapping);
				
					exportParamList.setValue("RETURN", "S");
					exportParamList.setValue("MESSAGE", String.format("Carton details imported for %s orders successfully! [ %s ]",
							orderTocartonMapping.size(), new Date()));
				}
				else
				{
					exportParamList.setValue("RETURN", "W");
					exportParamList.setValue("MESSAGE", "No cartons received!");
				}

			}
			catch (final Exception e)
			{
				LOG.error("Error importing carton details: ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing carton details " + e.toString().substring(0, Math.min(226, e.toString().length())));
			}
		}

		/**
		 * This method updates the carton details for each sales order and persists the data
		 *
		 * @param orderTocartonMapping
		 * 
		 * @throws NamingException
		 * @throws EJBException
		 * @throws CreateException
		 * @throws FinderException
		 * @throws FDResourceException
		 * @throws RemoteException
		 */
		private void storeCartonInfo(Map<String, List<ErpCartonInfo>> orderTocartonMapping) 
				throws NamingException, EJBException, CreateException, FinderException, FDResourceException, RemoteException
		{
			Context ctx = null;
			String saleId = null;
		
			try
			{
				ctx = ErpServicesProperties.getInitialContext();
				ErpCustomerManagerHome mgr = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
				ErpCustomerManagerSB sb = mgr.create();
				OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
				
				for(Iterator<String> i = orderTocartonMapping.keySet().iterator(); i.hasNext(); )
				{ 
					saleId = i.next();	
					if(FDStoreProperties.isSF2_0_AndServiceEnabled("updateCartonInfo_Api")){
			    		service.updateCartonInfo(saleId, orderTocartonMapping.get(saleId));
			    	}else{
					sb.updateCartonInfo(saleId, orderTocartonMapping.get(saleId));
			    	}
				}
			} 
			catch(Exception ex) {
				throw new EJBException("Failed to store: " + saleId + "Msg: " + ex.toString());
			} 
			finally 
			{
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
					}
				}
			}
		}

		/**
		 * @param cartonTable
		 * @return CartonDetailParameter
		 */
		private CartonDetailParameter populateCartonEntryRecord(final JCoTable cartonTable)
		{
			final CartonDetailParameter param = new CartonDetailParameter();
			param.setOrderNo(FDSapHelperUtils.getString(cartonTable.getString("BSTNK")));
			param.setSapOrderNo(FDSapHelperUtils.getString(cartonTable.getString("VBELN")));
			param.setOrderLineNo(FDSapHelperUtils.getString(cartonTable.getString("PARENT_LN")));
			param.setChildOrderLineNo(FDSapHelperUtils.getString(cartonTable.getString("CHILD_LN")));
			param.setCartonNumber(FDSapHelperUtils.getString(cartonTable.getString("CARTON")));
			param.setMaterialNumber(FDSapHelperUtils.getString(cartonTable.getString("MATNR")));
			param.setMaterial_description(FDSapHelperUtils.getString(cartonTable.getString("MAKTX")));
			param.setBarcode(FDSapHelperUtils.getString(cartonTable.getString("BARCODE")));
			param.setOrdered_quantity(Double.parseDouble(cartonTable.getString("ACT_QTY")));
			param.setActual_quantity(Double.parseDouble(cartonTable.getString("PACK_QTY")));
			param.setPacked_uom(FDSapHelperUtils.getString(cartonTable.getString("PACK_UOM")));
			
			param.setNetWeight(Double.parseDouble(StringUtils.isNotBlank(cartonTable.getString("NTGEW")) ? cartonTable.getString("NTGEW") : "0.0"));
			param.setSalesUnitCode(FDSapHelperUtils.getString(cartonTable.getString("GEWEI")));
			param.setShipping_status(FDSapHelperUtils.getString(cartonTable.getString("SHIP_STAT")));
			param.setSub_material_number(FDSapHelperUtils.getString(cartonTable.getString("SUB_MATNR")));
			param.setSkuCode(FDSapHelperUtils.getString(cartonTable.getString("WEBID")));

			final String cType = cartonTable.getString("CRT_TYP").trim();

			String cartonType = ErpCartonInfo.CARTON_TYPE_REGULAR;

			if (cType.equals("FZ")){
				cartonType = ErpCartonInfo.CARTON_TYPE_FREEZER;
			}
			if (cType.equals("BR")){
				cartonType = ErpCartonInfo.CARTON_TYPE_BEER;
			}
			if (cType.equals("PL")){
				cartonType = ErpCartonInfo.CARTON_TYPE_PLATTER;
			}
			if (cType.equals("CS")){
				cartonType = ErpCartonInfo.CARTON_TYPE_CASEPICK;
			}
			if(cType.equals("RG")){
				cartonType = ErpCartonInfo.CARTON_TYPE_REGULAR;
			}
			param.setCartonType(cartonType);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Got Carton detail record for Order No:" + param.getOrderNo() + "\t SAP Order No:"
						+ param.getSapOrderNo() + "\t Carton No:" + param.getCartonNumber() + "\t Material No:"
						+ param.getMaterialNumber() + "\t Carton Type:" + param.getCartonType() + "\t Barcode:"
						+ param.getBarcode() + "\t PackedQuantity:" + param.getActual_quantity() + "\t NetWeight:"
						+ param.getNetWeight());
			}
			return param;
		}
	}

	@Override
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * @param serverName
	 *           the serverName to set
	 */
	public void setServerName(final String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * @return String
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

}
