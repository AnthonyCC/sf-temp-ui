package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;

public class ChangeHistoryPopUp extends Window {

    public ChangeHistoryPopUp(ChangeSetQueryResponse changeHistory, String label) {
        super();
        setHeading("Change History for : " + label);
        setLayout(new FitLayout());
        setModal(true);
        setMaximizable(true);
        setMinimizable(false);
        setClosable(true);
        setDraggable(true);
        setResizable(true);
        setSize(900, 600);

        add(new ChangeSetPanel(changeHistory));
    }
}
