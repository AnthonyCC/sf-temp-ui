package com.freshdirect.cms.ui.client.views;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.draft.DraftChangeHistory;
import com.freshdirect.cms.ui.client.draft.LoaderMode;
import com.freshdirect.cms.ui.client.publish.PublishProgressListener;

public class DraftOverviewView extends LayoutContainer implements PublishProgressListener {

    private static DraftOverviewView instance = new DraftOverviewView();

    private Button showChangesButton;
    private Button validateButton;
    private Button mergeButton;
    private DraftChangeHistory draftChangeHistory;
    private LoaderMode lastMode = LoaderMode.ALL_CHANGES;

    public static DraftOverviewView getInstance() {
        return instance;
    }

    private DraftOverviewView() {
        super();
        setupLayout();
        MainLayout.getInstance().registerPublishProgressListener(this);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorders(false);

        LayoutContainer buttonBar = setupButtons();

        // -- list view --
        {
            draftChangeHistory = new DraftChangeHistory();

            LayoutContainer wrapper = new LayoutContainer();
            wrapper.add(draftChangeHistory);
            wrapper.setLayout(new FitLayout());
            wrapper.setScrollMode(Scroll.AUTO);
            add(wrapper, new BorderLayoutData(LayoutRegion.CENTER));
        }

        // -- footer bar, buttons
        {
            HtmlContainer wrapper = new HtmlContainer("<footer></footer>");
            wrapper.add(buttonBar, "footer");
            wrapper.addStyleName("pageHeader");
            BorderLayoutData footer = new BorderLayoutData(LayoutRegion.SOUTH);
            footer.setCollapsible(false);
            footer.setFloatable(false);
            footer.setSplit(false);
            footer.setSize(50);
            add(wrapper, footer);
        }

        this.addListener(Events.Show, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                draftChangeHistory.getLoader(LoaderMode.ALL_CHANGES).load();
            }
        });

        layout();
    }

    /**
     * Setup buttons
     */
    private LayoutContainer setupButtons() {

        showChangesButton = new Button("Show All Changes", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                initiateAsyncOperation(LoaderMode.ALL_CHANGES);
            }
        });

        validateButton = new Button("Validate", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                initiateAsyncOperation(LoaderMode.VALIDATION);
            }
        });

        mergeButton = new Button("Merge", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                initiateAsyncOperation(LoaderMode.MERGE);
            }
        });

        if (MainLayout.getInstance().isPublishInProgress()) {
            mergeButton.disable();
        }

        ActionBar actionBar = new ActionBar();

        actionBar.addButton(showChangesButton, new Margins(0, 10, 0, 10));
        actionBar.addButton(validateButton, new Margins(0, 10, 0, 10));
        actionBar.addButton(mergeButton, new Margins(0, 10, 0, 10));

        return actionBar;
    }

    public void startProgress() {
        String message = null;
        switch (lastMode) {
            case ALL_CHANGES:
                message = "Loading draft changes";
                break;
            case VALIDATION:
                message = "Validating draft " + CmsGwt.getCurrentUser().getDraftName();
                break;
            case MERGE:
                message = "Merging draft " + CmsGwt.getCurrentUser().getDraftName() + " into mainline";
                break;
            default:
                message = "Executing requested order";
                break;
        }

        MainLayout.getInstance().startProgress("Draft Changes View", message, "Please wait ...");
    }

    private void initiateAsyncOperation(LoaderMode mode) {
        lastMode = mode;
        startProgress();
        draftChangeHistory.getLoader(mode).load();
    }

    @Override
    public void onPublishStarted() {
        if (mergeButton != null) {
            mergeButton.disable();
        }
    }

    @Override
    public void onPublishFinished() {
        if (mergeButton != null) {
            mergeButton.enable();
        }
    }
}
