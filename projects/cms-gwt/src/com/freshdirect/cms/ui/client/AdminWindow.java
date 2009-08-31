package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;
import com.freshdirect.cms.ui.service.AdminServiceAsync;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class AdminWindow extends Window {

    private final class AdminProcStatusCallback extends BaseCallback<AdminProcStatus> {

        @Override
        public void onSuccess(AdminProcStatus stat) {
            String current = stat.getCurrent() != null ? stat.getCurrent() : "";
            
            if (stat.isRunning()) {
                status.setValue(current + " (" + (stat.getElapsedTime() / 1000) + " second elapsed)");
            } else {
                status.setValue(current);
            }
            lastIndexResult.setValue(stat.getLastReindexResult());
        }
    }

    private final static class FormUploadListener implements Listener<FormEvent> {
        @Override
        public void handleEvent(FormEvent be) {
            HtmlPopup hp = new HtmlPopup("Upload Result", be.getResultHtml());
            hp.show();
        }
    }

    AdminServiceAsync admin = GWT.create(AdminService.class);

    TextField<String> status;

    TextField<String> lastIndexResult;

    Button showButton;

    Timer refresh;

    public AdminWindow(Button showButton) {
        this.showButton = showButton;

        setHeading("Administration Tasks");
        setLayout(new FillLayout());
        setSize(500, 300);
        TabPanel tp = new TabPanel();

        tp.add(getRecipeLoaderTab());
        tp.add(getXLSBulkLoaderTab());
        tp.add(getSearchIndexTab());
        add(tp);

        addButton(new Button("Close", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                AdminWindow.this.hide(AdminWindow.this.showButton);
            }
        }));
    }

    private TabItem getSearchIndexTab() {
        TabItem t = new TabItem("Search Index");
        t.setLayout(new FillLayout());

        FormPanel form = new FormPanel();
        form.setHeaderVisible(false);
        
        t.add(form);

        status = new TextField<String>();
        status.setFieldLabel("Status");
        status.setReadOnly(true);
        form.add(status);

        lastIndexResult = new TextField<String>();
        lastIndexResult.setFieldLabel("Last Result");
        lastIndexResult.setReadOnly(true);
        form.add(lastIndexResult);

        refresh = new Timer() {
            @Override
            public void run() {
                admin.getBuildIndexStatus(new AdminProcStatusCallback());
            }
        };

        addButton(new Button("Validate Editors", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                admin.validateEditors(new AdminProcStatusCallback());
            }
        }));
        addButton(new Button("Re-index Search", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                admin.rebuildIndexes(new AdminProcStatusCallback());
                refresh.scheduleRepeating(5000);
            }
        }));

        admin.getBuildIndexStatus(new AdminProcStatusCallback());
        return t;
    }

    private TabItem getXLSBulkLoaderTab() {
        TabItem t = new TabItem("XLS Bulk Product Loader");
        t.setLayout(new FillLayout());

        final FormPanel form = new FormPanel();
        form.setAction(GWT.getModuleBaseURL() + "XlsUpload");
        form.setEncoding(FormPanel.Encoding.MULTIPART);
        form.setMethod(FormPanel.Method.POST);
        form.setHeaderVisible(false);
        FileUploadField f = new FileUploadField();
        f.setFieldLabel("Recipies");
        f.setAllowBlank(false);
        f.getMessages().setBrowseText("Select file...");
        f.addStyleName("cms-gwt-file-upload");
        f.setName("xlsFile");
        form.add(f);

        form.addButton(new Button("Upload", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (form.isValid()) {
                    form.submit();
                }
            }
        }));
        form.addListener(Events.Submit, new FormUploadListener());

        t.add(form);
        return t;
    }

    private TabItem getRecipeLoaderTab() {
        TabItem t = new TabItem("Recipe Loader");
        t.setLayout(new FillLayout());

        final FormPanel form = new FormPanel();
        form.setAction(GWT.getModuleBaseURL() + "RecipeUpload");
        form.setEncoding(FormPanel.Encoding.MULTIPART);
        form.setMethod(FormPanel.Method.POST);
        form.setHeaderVisible(false);
        FileUploadField f = new FileUploadField();
        f.setFieldLabel("Recipies");
        f.setAllowBlank(false);
        f.getMessages().setBrowseText("Select file...");
        f.addStyleName("cms-gwt-file-upload");
        f.setName("recipeFile");
        form.add(f);
        RadioGroup rg = new RadioGroup("type");
        rg.setFieldLabel("File Type");
        {
            Radio r = new Radio();
            r.setValueAttribute("1");
            r.setBoxLabel("Recipe");
            rg.add(r);
        }
        {
            Radio r = new Radio();
            r.setValueAttribute("2");
            r.setBoxLabel("Configured Product Group");
            rg.add(r);
        }
        form.add(rg);

        form.addButton(new Button("Upload", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (form.isValid()) {
                    form.submit();
                }
            }
        }));
        form.addListener(Events.Submit, new FormUploadListener());

        t.add(form);
        return t;
    }

    @Override
    protected void onHide() {
        refresh.cancel();
        super.onHide();
    }
}
