package com.freshdirect.cms.ui.client.action;

import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeData;


public class GetNodeAction extends BasicAction<GwtNodeData> {
	
	private String path;	
	
	public GetNodeAction( String nodeKey, String path ) {
		this.path = path;
		startLoadProgress( "Loading " + nodeKey, "Loading " + nodeKey + " ... " );
	}
	
	@Override
	public void onSuccess( final GwtNodeData currentNodeData ) {
		final ManageStoreView ms = ManageStoreView.getInstance();
				
        CmsGwt.log( "setting context path to : " + path );
		currentNodeData.setCurrentContext( path );
		ms.setCurrentNode( currentNodeData );
		ms.setupEditorLayout();

        stopProgress();
    }

}
