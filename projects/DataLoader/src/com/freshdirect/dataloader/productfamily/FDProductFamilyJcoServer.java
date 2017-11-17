package com.freshdirect.dataloader.productfamily;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPProductFamilyLoaderDAO;
import com.freshdirect.dataloader.sap.ejb.SAPProductFamilyLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPProductFamilyLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.sap.jco.server.param.ProductFamilyParameter;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;
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
 * 
 *
 * @author aravind
 */


public class FDProductFamilyJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDProductFamilyJcoServer.class.getName());
	String sapMsg = null;
	private String serverName;
	Context ctx = null;
	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDProductFamilyJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	public FDProductFamilyJcoServer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JCoRepository createRepository() {
		final JCoCustomRepository repository = JCo.createCustomRepository("FDProductFamilyRepository");
		final JCoRecordMetaData metaProductFamilyList = JCo.createRecordMetaData("PRODUCTFAMILYLIST");
		
		tableMetaDataList.add(new TableMetaData("GROUP_ID", JCoMetaData.TYPE_CHAR, 10, 0, "Group ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material"));
		tableMetaDataList.add(new TableMetaData("ACTION", JCoMetaData.TYPE_CHAR, 1, 0, "Action"));
		tableMetaDataList.add(new TableMetaData("DEL_GROUP", JCoMetaData.TYPE_CHAR, 1, 0, "Delete Group"));
		tableMetaDataList.add(new TableMetaData("TYPE", JCoMetaData.TYPE_CHAR, 1, 0, "Type"));
		tableMetaDataList.add(new TableMetaData("MESSAGE", JCoMetaData.TYPE_CHAR, 40, 0, "Message"));

		createTableRecord(metaProductFamilyList, tableMetaDataList);
		metaProductFamilyList.lock();
		repository.addRecordMetaDataToCache(metaProductFamilyList);
		
		final JCoListMetaData fmetaImport = JCo.createListMetaData("PRODUCT_FAMILY_IMPORTS");
		
		fmetaImport.add("GT_ITEM_GROUP", JCoMetaData.TYPE_TABLE, metaProductFamilyList,	JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();
		
		final JCoListMetaData fmetaExport = JCo.createListMetaData("PRODUCT_FAMILY_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();
		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null, fmetaImport, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	}
	
	@Override
	protected FDSapFunctionHandler getHandler() {
		// TODO Auto-generated method stub
		return new FDConnectionHandler();
	}
	protected class FDConnectionHandler extends FDSapFunctionHandler implements JCoServerFunctionHandler
	{
		@Override
		public String getFunctionName()
		{
			return functionName;
		}
		
		
		@Override
		public void handleRequest(final JCoServerContext serverCtx, final JCoFunction function)
		 {
			
			final JCoParameterList exportParamList = function.getExportParameterList();
			FDJcoServerResult result = new FDJcoServerResult();
			try
			{
				
				final JCoTable productfamilyTable = function.getTableParameterList().getTable("GT_ITEM_GROUP"); 
				final Map<String, List<ProductFamilyParameter>> productFamilyRecordMap = new HashMap<String, List<ProductFamilyParameter>>();
			
			
				for (int i = 0; i < productfamilyTable.getNumRows(); i++)
				{
					productfamilyTable.setRow(i);
					// populating values to ProductFamilyParameter bean. So that we can take the values to Storefornt
					// getting vlaues from sap and setting in bean.
					final ProductFamilyParameter gsParamRecord = populateProductFamilyRecord(productfamilyTable);
				    // if groupid present add values to map
					if (!productFamilyRecordMap.containsKey(gsParamRecord.getGroupId()))
					{
						productFamilyRecordMap.put(gsParamRecord.getGroupId(), new ArrayList<ProductFamilyParameter>());
					}
					productFamilyRecordMap.get(gsParamRecord.getGroupId()).add(gsParamRecord);
					productfamilyTable.nextRow();	
				}	
				// inserting all the datas to DB from sap.
				sapMsg = populateProductFamily(productFamilyRecordMap);				
				exportParamList.setValue("RETURN", "S"); // Type S
				exportParamList.setValue("MESSAGE", sapMsg); // for Message

			}
			catch (final Exception e)
			{
				LOG.error("Error importing processing Family Product -> ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",	"Update Failed");
			}
			
				
		}
		//JJ change it back to private
		public String validatMaterials(final Map<String, List<ProductFamilyParameter>> productFamilyRecordMap,
				final FDJcoServerResult result) throws LoaderException
		{
		   

			try{
				sapMsg = populateProductFamily(productFamilyRecordMap);							
				}
				catch(Exception ex){
					LOG.error("Error in validateMaterials ", ex);
				}
			
			if(sapMsg.equals("Successfully Updated"))
				udateCacheProdFly(productFamilyRecordMap);
			
			return sapMsg;
		}
		
	

		private void udateCacheProdFly(
				Map<String, List<ProductFamilyParameter>> productFamilyRecordMap) {
			List<String> familyIds = new ArrayList<String>();
			for (final Map.Entry<String, List<ProductFamilyParameter>> productFamilyRecordEntry : productFamilyRecordMap
					.entrySet())
			{
				familyIds.add(productFamilyRecordEntry.getKey());
			}
			
			String msg = updateCacheWithProdFly(familyIds);
			
		}


		private String updateCacheWithProdFly(List<String> familyIds) {

			Context ctx = null;
			String saleId = null;
			String msg = null;
			try
			{
				LOG.info(String.format("Storing  product family [%s], [%s] ", familyIds.size(), new Date()));
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("sap.ejb.SAPProductFamilyLoaderSB")){
					IECommerceService service = FDECommerceService.getInstance();
					Map<String,List<String>> familySkucodeMap = service.updateCacheWithProdFly(familyIds);
					if(familySkucodeMap != null && familySkucodeMap.size() > 0 ){
						for(String familyId: familySkucodeMap.keySet()){
							List<String> skuCodes = familySkucodeMap.get(familyId);
		                    EhCacheUtil.putListToCache(EhCacheUtil.FD_FAMILY_PRODUCT_CACHE_NAME,familyId, skuCodes);
		                   }
					}
				}else{
					ctx = ErpServicesProperties.getInitialContext();
					SAPProductFamilyLoaderHome mgr = (SAPProductFamilyLoaderHome) ctx.lookup("freshdirect.dataloader.SAPProductFamilyInfoLoader");
					SAPProductFamilyLoaderSB sb = mgr.create();
			        sb.updateCacheWithProdFly(familyIds);
				}
		        msg = "Success";
		       
			} catch(Exception ex) {
				//throw new EJBException("Failed to store: " + saleId + "Msg: " + ex.toString());
				msg ="Failed";
			} finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
					}
				}
			}
			
			 return msg;
		
		}



		private String deleteProductFamily(final Map<String, List<ProductFamilyParameter>> productFamilyRecordMap) throws Exception
		{
			String deletemsg = null;
			List<ErpProductFamilyModel> scaleGroups = new ArrayList<ErpProductFamilyModel>();
			try
			{
				for (final Map.Entry<String, List<ProductFamilyParameter>> productFamilyRecordEntry : productFamilyRecordMap
						.entrySet())
				{
					final String productGroupId = productFamilyRecordEntry.getKey();
					
					ErpProductFamilyModel productFamilyModel = new ErpProductFamilyModel();
					scaleGroups.add(productFamilyModel);
					
					productFamilyModel.setGrpId(productGroupId);					
					productFamilyModel.setMaterialNumber(productFamilyRecordEntry.getValue().get(0).getMaterialNumber());
					String action =(((productFamilyRecordEntry.getValue().get(0).getAction()) ==null) ? " " : productFamilyRecordEntry.getValue().get(0).getAction());
					productFamilyModel.setAction(action);
					String Deletegroup =(((productFamilyRecordEntry.getValue().get(0).getDeletegroup()) ==null) ? " " : productFamilyRecordEntry.getValue().get(0).getDeletegroup());
					productFamilyModel.setDeletegroup(Deletegroup);
					
					productFamilyModel.setMsgtype(productFamilyRecordEntry.getValue().get(0).getMsgtype());
					productFamilyModel.setMsgtext(productFamilyRecordEntry.getValue().get(0).getMsgtext());					
				
				}
				
					if(scaleGroups.size() > 0)
					{
						//deletemsg =deleteScaleGroups(scaleGroups);
					}
	
					LOG.info(String.format("Processing product family successfull [%s] ", new Date()));
			}
			catch (final Exception e)
			{	
				LOG.error("Processing product family failed. Exception is ", e);
				throw new Exception(e);
			}
			
			return deletemsg;
			
		}
		private String populateProductFamily(final Map<String, List<ProductFamilyParameter>> productFamilyRecordMap) throws Exception
		{
			String msg = null;
			List<ErpProductFamilyModel> scaleGroups = new ArrayList<ErpProductFamilyModel>();
			try
			{
				for (final Map.Entry<String, List<ProductFamilyParameter>> productFamilyRecordEntry : productFamilyRecordMap
						.entrySet())
				{
					final String productGroupId = productFamilyRecordEntry.getKey();
					
					for (int i = 0;i<productFamilyRecordEntry.getValue().size();i++){
					ErpProductFamilyModel productFamilyModel = new ErpProductFamilyModel();
					scaleGroups.add(productFamilyModel);
					
					productFamilyModel.setGrpId(productGroupId);					
					productFamilyModel.setMaterialNumber(productFamilyRecordEntry.getValue().get(i).getMaterialID());
					
					String action =(((productFamilyRecordEntry.getValue().get(i).getAction()) ==null) ? " " : productFamilyRecordEntry.getValue().get(i).getAction());
					productFamilyModel.setAction(action);
					String Deletegroup =(((productFamilyRecordEntry.getValue().get(i).getDeletegroup()) ==null) ? " " : productFamilyRecordEntry.getValue().get(i).getDeletegroup());
					productFamilyModel.setDeletegroup(Deletegroup);
					
					
					//productFamilyModel.setAction(productFamilyRecordEntry.getValue().get(i).getAction());
					//productFamilyModel.setDeletegroup(productFamilyRecordEntry.getValue().get(i).getDeletegroup());
					productFamilyModel.setMsgtype(productFamilyRecordEntry.getValue().get(i).getMsgtype());
					productFamilyModel.setMsgtext(productFamilyRecordEntry.getValue().get(i).getMsgtext());					
					}
				}
				
					if(scaleGroups.size() > 0)
					{
					  msg=storeFamilyGroups(scaleGroups);
					}
	
					LOG.info(String.format("Processing product family successfull [%s] ", new Date()));
			}
			catch (final Exception e)
			{	
				LOG.error("Processing product family failed. Exception is ", e);
				throw new Exception(e);
			}
			return msg;
		}
		
		// stroing the values in database 
		private String storeFamilyGroups(List<ErpProductFamilyModel> prodFamily) throws EJBException
		{
			Context ctx = null;
			String saleId = null;
			String msg = null;
			LOG.info(String.format("Storing  product family [%s], [%s] ", prodFamily.size(), new Date()));
			try
			{
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("sap.ejb.SAPProductFamilyLoaderSB")){
					IECommerceService service = FDECommerceService.getInstance();
					service.loadData(prodFamily);
					
				}else{
					ctx = ErpServicesProperties.getInitialContext();
					SAPProductFamilyLoaderHome mgr = (SAPProductFamilyLoaderHome) ctx.lookup("freshdirect.dataloader.SAPProductFamilyInfoLoader");
					SAPProductFamilyLoaderSB sb = mgr.create();
			        sb.loadData(prodFamily);
				}
		        msg = "Successfully Updated";
		       
			} catch(Exception ex) {
				
				msg ="Update Failed";
			} finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
					}
				}
			}
			
			 return msg;
		}
       
		

		private ProductFamilyParameter populateProductFamilyRecord(final JCoTable groupTable)
		{
			final ProductFamilyParameter param = new ProductFamilyParameter();

			param.setGroupId(FDSapHelperUtils.getString(groupTable.getString("GROUP_ID")));
			param.setMaterialID(FDSapHelperUtils.getString(groupTable.getString("MATNR")));
			param.setAction((((FDSapHelperUtils.getString(groupTable.getString("ACTION"))) ==null) ? " " : FDSapHelperUtils.getString(groupTable.getString("ACTION"))));
			param.setDeletegroup((((FDSapHelperUtils.getString(groupTable.getString("DEL_GROUP"))) ==null) ? " " : FDSapHelperUtils.getString(groupTable.getString("DEL_GROUP"))));
			param.setMsgtype(FDSapHelperUtils.getString(groupTable.getString("TYPE")));
			param.setMsgtext(FDSapHelperUtils.getString(groupTable.getString("MESSAGE")));	

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Got  group record for Material No:" + param.getMaterialID() + "\t GroupId:" + param.getGroupId()
						+ "\t actiom:" + param.getAction() + "\t DeleteGroup:" + param.getDeletegroup());
			}

			return param;
		}
		
	}
	public void setServerName(final String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName()
	{
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName)
	{
		this.functionName = functionName;
	}

}