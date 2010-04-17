package com.freshdirect.webapp.util.json;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade;
import com.metaparadigm.jsonrpc.JSONRPCBridge;

public class FDJSONRPCBridge extends JSONRPCBridge {

	private static final long serialVersionUID = 1L;

	public FDJSONRPCBridge() {
		super(true);
		registerFDSerializers();
		if (FDStoreProperties.isCclAjaxDebugJsonRpc()) {
			CustomerCreatedListAjaxFacade.setJsonRpcDebug(this, true);
		}
	}

	private void registerFDSerializers() {
		// Register custom serializers
		try {
			registerSerializer(FDCustomerCreatedListJSONSerializer.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			registerSerializer(EnumStandingOrderFrequencyJSONSerializer.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
