package com.freshdirect.telargo.service.proxy;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetWithStateDto;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusAssetsFilter;

import com.freshdirect.telargo.proxy.stub.coreservices.CoreService;
import com.freshdirect.telargo.service.TelargoServiceLocator;
import com.freshdirect.telargo.service.exception.IIssue;
import com.freshdirect.telargo.service.exception.TelargoServiceException;
import com.freshdirect.routing.service.proxy.BaseServiceProxy;


public class TelargoServiceProxy extends BaseServiceProxy {
	
	public ArrayOfAssetWithStateDto getAssetsWithLastState() {
		try{
			return getService().getAssetsWithLastState(new CurrentStatusAssetsFilter());
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new TelargoServiceException(exp, IIssue.PROCESS_RETRIEVETSASSET_UNSUCCESSFUL);
		}
	}
	
	public CoreService getService() {
		try{
			return TelargoServiceLocator.getInstance().getTelargoCoreService();
		} catch (AxisFault exp) {
			exp.printStackTrace();
			throw new TelargoServiceException(exp, IIssue.LOCATE_TELARGOSERVICE_UNSUCCESSFUL);
		}
	}
}
