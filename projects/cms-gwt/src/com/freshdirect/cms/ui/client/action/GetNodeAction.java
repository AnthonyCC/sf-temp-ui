package com.freshdirect.cms.ui.client.action;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeData;


public class GetNodeAction extends BasicAction<GwtNodeData> {
	
	private String path;
	private String nodeKey;
	
	public GetNodeAction( String nodeKey, String path ) {
		this.path = path;
		this.nodeKey = nodeKey;
		startLoadProgress( "Loading " + nodeKey, "Loading " + nodeKey + " ... " );
	}
	
    @Override
    public void onSuccess(final GwtNodeData currentNodeData) {
        final ManageStoreView ms = ManageStoreView.getInstance();
        if (currentNodeData != null) {
            CmsGwt.log("setting context path to : " + path);
            currentNodeData.setCurrentContext(path);
            ms.setCurrentNode(currentNodeData);
            ms.setupEditorLayout();
            stopProgress();
        } else {
            stopProgress();
            MessageBox.alert("Node missing!", "The system unable to load the node with ID : " + nodeKey, null);
        }
    }

}
