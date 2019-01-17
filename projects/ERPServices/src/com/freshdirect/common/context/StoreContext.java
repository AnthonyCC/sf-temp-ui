package com.freshdirect.common.context;

import java.io.Serializable;

import com.freshdirect.fdstore.EnumEStoreId;

public class StoreContext implements Serializable {

	private static final long serialVersionUID = -3817236918159902688L;
	private EnumEStoreId eStoreId;

	public static StoreContext createDefault(){
		StoreContext storeContext = new StoreContext();
		storeContext.setEStoreId(EnumEStoreId.FD); //TODO modify once FDX_CMS is merged!!!
		return storeContext;
	}
	
	public static StoreContext createStoreContext(EnumEStoreId eStoreId){
		StoreContext storeContext = new StoreContext();	
		if(null ==eStoreId){
			throw new RuntimeException("StoreId can't be null");
		}else{
			storeContext.setEStoreId(eStoreId);
		}
		return storeContext;
	}
	
	public EnumEStoreId getEStoreId() {
		return eStoreId;
	}
	public void setEStoreId(EnumEStoreId eStoreId) {
		this.eStoreId = eStoreId;
	}
	
}
