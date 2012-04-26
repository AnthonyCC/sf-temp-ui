package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.ErpProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;



public interface SAPProductPromotionLoaderSB extends EJBObject {

	public void loadProductPromotions(List<ErpProductPromotion> ppList,List<ErpProductPromotionInfo> ppInfoList,int batchVersion) throws LoaderException, RemoteException;
	public int getNextBatchNumber() throws LoaderException, RemoteException;
	public void createHistoryData(Timestamp timestamp,int batchNumber) throws LoaderException, RemoteException;
	public void updateHistoryData(int batchNumber,String status) throws LoaderException, RemoteException;
}
