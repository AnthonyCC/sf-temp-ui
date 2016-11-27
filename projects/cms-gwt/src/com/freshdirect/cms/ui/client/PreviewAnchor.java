package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;

public class PreviewAnchor extends Anchor {
	private String originalUrl;

	
	public PreviewAnchor( String text, String url ) {
		super(text, url);

		this.originalUrl = url;
		
		final PreviewAnchor that = this;

		addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				that.updateUrl();
			};
		});

		setHideMode(HideMode.DISPLAY);
	}


	public String getOriginalUrl() {
		return originalUrl;
	}
	
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}



	protected void updateUrl() {
		setUrl( originalUrl + "&preview=" + Math.random() );
	}
}
