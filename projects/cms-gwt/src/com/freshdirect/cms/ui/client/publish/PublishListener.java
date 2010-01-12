package com.freshdirect.cms.ui.client.publish;

import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;

public interface PublishListener {

	public void onPublishClicked(String publishId);
	public void onPublishStarted(String publishId);
	public void onDetailRequest(ChangeSetQuery query);
	
}
