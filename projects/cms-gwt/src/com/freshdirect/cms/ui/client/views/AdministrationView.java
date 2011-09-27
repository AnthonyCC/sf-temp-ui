package com.freshdirect.cms.ui.client.views;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;
import com.freshdirect.cms.ui.service.AdminServiceAsync;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class AdministrationView extends LayoutContainer {

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

    AdminServiceAsync admin = GWT.create(AdminService.class);

    TextField<String> status;

    TextField<String> lastIndexResult;

    Timer refresh;    

    private static AdministrationView instance = new AdministrationView();
    private LayoutContainer detailPanel;
    private ActionBar actionBar;
    
    public static AdministrationView getInstance() {
    	return instance;
    }

    public AdministrationView() {
    	HtmlContainer headerMarkup = new HtmlContainer("<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\">" +
        		"<tbody><tr>" +
        		"<td valign=\"bottom\">" +
        		"<h1 class=\"view-title\">Administration</h1>" +
        		"</td>" +
        		"<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">" +
        		"<img width=\"75\" height=\"66\" src=\"img/banner_admin.gif\"/>" +
        		"</td>" +
        		"</tr>" +
        		"</tbody></table>");
    
    	add(headerMarkup);
    	
    	detailPanel = new LayoutContainer();    	        
        
               
        actionBar = new ActionBar();

        actionBar.addButton(new Button("Re-index Search", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                admin.rebuildIndexes(new AdminProcStatusCallback());
                refresh.scheduleRepeating(5000);
            }
        }), new Margins(0, 10, 0, 10));

        actionBar.addButton(new Button("Rebuild Wine Index", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                admin.rebuildWineIndexes(new AdminProcStatusCallback());
                refresh.scheduleRepeating(5000);
            }
        }), new Margins(0, 10, 0, 10));
        
        detailPanel.add(actionBar);
        add(detailPanel);
        detailPanel.add(getSearchIndexTab());
    }

    private LayoutContainer getSearchIndexTab() {
        LayoutContainer t = new LayoutContainer();        

        FormPanel form = new FormPanel();
        form.setHeaderVisible(false);
        form.setAutoHeight(true);        
        t.add(form, new MarginData(10));

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

        admin.getBuildIndexStatus(new AdminProcStatusCallback());
        form.setWidth(450);
        return t;
    }

    @Override
    protected void onHide() {
        refresh.cancel();
        super.onHide();
    }
}
