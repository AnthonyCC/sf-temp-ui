package com.freshdirect.cms.ui.client.action;

import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeData;


public class GetNodeAction extends BasicAction<GwtNodeData> {
	public GetNodeAction(String nodeKey) {
		startLoadProgress("Loading " + nodeKey, "Loading " + nodeKey + " ... " );
	}
	
	@Override
	public void onSuccess(final GwtNodeData currentNodeData) {
		final ManageStoreView ms = ManageStoreView.getInstance();
				
		ms.setCurrentNode(currentNodeData);
		ms.setupEditorLayout();

        stopProgress( "Loaded successfully" );
    }

}
