package com.freshdirect.cms.ui.client.publish;

import java.util.Date;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;

public class PublishStatusPopup extends Window {

    String publishId;

    TextField<String> statusField = new TextField<String>();
    TextField<String> startedField = new TextField<String>();
    TextField<String> elapsedTimeField = new TextField<String>();
    
    TextField<String> lastInfoField = new TextField<String> ();

    Timer timer;

    public PublishStatusPopup(String publishId) {
        super();
        this.publishId = publishId;
        setSize(350, 250);
        setHeading("Publish - " + publishId);
        setLayout(new FormLayout());

        statusField.setReadOnly(true);
        statusField.setFieldLabel("Status");
        statusField.setValue("---");
        add(statusField);

        startedField.setReadOnly(true);
        startedField.setFieldLabel("Started");
        startedField.setValue(DateTimeFormat.getMediumTimeFormat().format(new Date()));
        add(startedField);

        elapsedTimeField.setReadOnly(true);
        elapsedTimeField.setFieldLabel("Elapsed Time");
        elapsedTimeField.setValue("---");
        add(elapsedTimeField);
        
        lastInfoField.setReadOnly(true);
        lastInfoField.setFieldLabel("Step");
        lastInfoField.setValue("running");
        add(lastInfoField);
        
        timer = new Timer() {
            @Override
            public void run() {
                ChangeSetQuery query = new ChangeSetQuery().setPublishId(PublishStatusPopup.this.publishId);
                query.setPublishInfoQuery(true);
                CmsGwt.getContentService().getChangeSets(query, new BaseCallback<ChangeSetQueryResponse>() {

                    @Override
                    public void onSuccess(ChangeSetQueryResponse resp) {
                        refreshFields(resp);
                    }

                });
            }
        };
        addButton(new Button("Close", new SelectionListener<ButtonEvent> () {
            @Override
            public void componentSelected(ButtonEvent ce) {
                PublishStatusPopup.this.hide();
            }
        }));
        
        timer.scheduleRepeating(5000);
    }

    @Override
    protected void onHide() {
        timer.cancel();
        super.onHide();
    }

    void refreshFields(ChangeSetQueryResponse resp) {
        statusField.setValue(resp.getPublishStatus());

        startedField.setValue(DateTimeFormat.getMediumTimeFormat().format(resp.getPublishStart()));
        elapsedTimeField.setValue("" + (resp.getElapsedTime() / 1000) + " seconds");
        lastInfoField.setValue(resp.getLastInfo());
        
        if (!"PROGRESS".equals(resp.getPublishStatus())) {
            timer.cancel();
        }
    }
}
