package com.freshdirect.cms.ui.client.action;

import com.freshdirect.cms.ui.client.contenteditor.CompareNodesUtil;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeData;

public class CompareNodesAction extends BasicAction<GwtNodeData> {
	
	private GwtNodeData currentNode;
	
	public CompareNodesAction(GwtNodeData currentNode) {
		final String message = "Loading ";
		this.currentNode = currentNode;
		startLoadProgress(message, message + " ... " );
	}

	@Override
	public void onSuccess(final GwtNodeData result) {
		final ManageStoreView ml = ManageStoreView.getInstance();
        java.util.HashSet<String> obj = CompareNodesUtil.compare( currentNode, result );
		
//		ml.changeToComparisonMode(cd2, obj);
		
        stopProgress( "Loaded successfully" );
    }
}
