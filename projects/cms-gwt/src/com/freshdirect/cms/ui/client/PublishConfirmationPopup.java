package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;

public class PublishConfirmationPopup extends Window {

    TextArea textArea;
    private Button startButton;

    public PublishConfirmationPopup(ChangeSetQueryResponse result) {
        super();
        setHeading("Changes for this publish");
        setLayout(new RowLayout(Orientation.VERTICAL));
        setModal(true);
        setMaximizable(true);
        setMinimizable(false);
        setClosable(true);
        setDraggable(true);
        setResizable(true);
        setSize(900, 550);

        add(new ChangeSetPanel(result), new RowData(1, 0.7));

        ContentPanel panel = new ContentPanel(new FormLayout());
        textArea = new TextArea();
        textArea.setFieldLabel("Comment");
        textArea.setEmptyText("Enter comment");

        panel.add(textArea);
        startButton = new Button("Start Publish", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String comment = textArea.getValue();
                startButton.setEnabled(false);
                CmsGwt.getContentService().startPublish(comment, new BaseCallback<String> () {
                    
                    @Override
                    public void onSuccess(String publishId) {
                        PublishConfirmationPopup.this.hide(startButton);
                        new PublishStatusPopup(publishId).show();
                    }
                });
            }
        });

        panel.addButton(startButton);
        add(panel, new RowData(1, 0.3));
    }

}
