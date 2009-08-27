package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;

public class ChangeHistoryPopUp extends Window {

    public ChangeHistoryPopUp(ChangeSetQueryResponse changeHistory, String label) {
        super();
        setHeading("Change History for : " + label);
        setLayout(new BorderLayout());
        setModal(true);
        setMaximizable(true);
        setMinimizable(false);
        setClosable(true);
        setDraggable(true);
        setResizable(true);
        setSize(900, 600);

        BorderLayoutData bd = new BorderLayoutData(LayoutRegion.CENTER);
        add(new ChangeSetPanel(changeHistory), bd);
        
        BorderLayoutData south = new BorderLayoutData(LayoutRegion.SOUTH);
        add(new PublishMessagesPanel(changeHistory), south);
        
    }
}
