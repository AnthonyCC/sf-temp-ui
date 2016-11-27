package com.freshdirect.cms.ui.client.changehistory;

import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;

public class ChangesetView extends DetailPanel {

	private GwtChangeSet changeSet;
	private ActionBar actionBar;
	
	public ChangesetView() {
		
	}
	
	public void setupLayout() {
        LayoutContainer head = new LayoutContainer(new AnchorLayout());
		HtmlContainer htmlTitle = new HtmlContainer(getHeaderMarkup());
        actionBar = new ActionBar();
        head.add(htmlTitle,new AnchorData("100%"));
        head.add(actionBar,new AnchorData("100%"));
		setHeader(head);

		if ( changeSet != null ) {
			ChangeSetQueryResponse result = new ChangeSetQueryResponse(changeSet);
			ChangeHistory changes = new ChangeHistory( result );
			setBody( changes );
		}
        layout();		
	}
	

    protected String getHeaderMarkup() {
    	String title = "No changes";
    	if(changeSet != null) {
    		title = "Recent Changes ("+changeSet.getChangeSetId()+")";
    	}
    	
    	return "<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\">" +
		"<tbody><tr>" +
		"<td valign=\"bottom\">" +
		"<h1 class=\"view-title\">"+title+"</h1>" +
		"</td>" +
		"<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">" +
		"<img width=\"75\" height=\"66\" src=\"img/banner_admin.gif\"/>" +
		"</td>" +
		"</tr>" +
		"</tbody></table>" +
		"<div class=\"pageHeader\"><br/></div>";
    }    

	public GwtChangeSet getChangeSet() {
		return changeSet;
	}

	public void setChangeSet(GwtChangeSet changeSet) {
		this.changeSet = changeSet;
	}

}
