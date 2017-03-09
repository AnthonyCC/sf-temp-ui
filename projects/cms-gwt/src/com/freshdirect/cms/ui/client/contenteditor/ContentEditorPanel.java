package com.freshdirect.cms.ui.client.contenteditor;

import java.util.HashSet;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.Anchor;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.PreviewAnchor;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.action.SaveNodeAction;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.client.publish.PublishProgressListener;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.google.gwt.user.client.Window;

public class ContentEditorPanel extends DetailPanel implements PublishProgressListener {

    private GwtNodeData contentNode;
    private NodeTree treePanel;
    private ActionBar actionBar;
    private Anchor compareLink;
    private Anchor compareCancelLink;
    private Button saveButton;
    private Container<?> editorComponent; // root editor component
    private CompareNodesUtil compareUtil;
    private PreviewAnchor previewLink;
    private ContextToolBar contextToolBar;

    public ContentEditorPanel() {
        setBorders(false);
        MainLayout.getInstance().registerPublishProgressListener(this);
    }

    public void setupLayout() {
        LayoutContainer head = new LayoutContainer(new AnchorLayout());
        LayoutContainer body = new LayoutContainer(new FitLayout());

        // setup context toolbar
        HtmlContainer htmlTitle = new HtmlContainer(getHeaderMarkup());
        contextToolBar = new ContextToolBar(contentNode, treePanel);
        htmlTitle.add(contextToolBar, "#main-toolbar");

        actionBar = new ActionBar();
        actionBar.setText(contentNode.getNode().getKey());
        prepareButtons();

        head.add(htmlTitle, new AnchorData("100%"));
        head.add(actionBar, new AnchorData("100%"));

        editorComponent = ContentEditorFactory.getEditor(contentNode);

        body.add(editorComponent);

        ((BorderLayout) ManageStoreView.getInstance().getLayout()).expand(LayoutRegion.WEST);
        setHeader(head);
        setBody(body);
        layout();
    }

    protected String getHeaderMarkup() {
        GwtContentNode node = contentNode.getNode();
        StringBuilder headerMarkup = new StringBuilder();
        headerMarkup.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" class=\"pageTitle\">");
        headerMarkup.append("<tbody><tr><td class=\"context\"></td><td width=\"75\" valign=\"bottom\" align=\"right\" rowspan=\"2\" style=\"line-height: 0pt;\">");
        headerMarkup.append("<img width=\"75\" height=\"66\" src=\"img/banners/" + node.getType()
                + ".gif\"/></td></tr><tr><td valign=\"bottom\" style=\"position:relative\"><div id=\"main-toolbar\" style=\"position:absolute;top:0px;left:0px;right:80px;height:40px\"></div>");
        headerMarkup.append("<h1 title=\"" + node.getKey() + "\">");
        headerMarkup.append("<nobr><span id=\"fadeError\">" + node.getLabel() + "</span></nobr>");
        headerMarkup.append("</h1>");
        headerMarkup.append("</td></tr></tbody></table>");
        return headerMarkup.toString();
    }

    protected void prepareButtons() {
        /** SAVE & DISCARD */
        if (contentNode.getPermission().isEditable()) {
            saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    ((BorderLayout) ManageStoreView.getInstance().getLayout()).expand(LayoutRegion.WEST);
                    saveAction();
                }
            });
            saveButton.setToolTip(new ToolTipConfig("Save", "Saves the workset."));
            saveButton.addStyleName("green-button");
            saveButton.setWidth(60);
            if (MainLayout.getInstance().isPublishInProgress() && !CmsGwt.getCurrentUser().isDraftActive()) {
                saveButton.disable();
            }
            actionBar.addButton(saveButton);
        }

        compareCancelLink = createCompareCancelLink();
        actionBar.addLink(compareCancelLink, new Margins(0, 10, 0, 0));

        compareLink = createCompareLink();
        actionBar.addLink(compareLink, new Margins(0, 10, 0, 0));

        if (!CmsGwt.getCurrentUser().isHasAccessToDraftBranches() || !CmsGwt.getCurrentUser().isDraftActive()) {
            Anchor viewHistoryLink = createViewHistoryLink();
            actionBar.addLink(viewHistoryLink, new Margins(0, 10, 0, 0));
        }

        previewLink = createPreviewLink();
        actionBar.addLink(previewLink, new Margins(0, 10, 0, 0));
    }

    private Anchor createCompareCancelLink() {
        final Anchor compareEndButton = new Anchor("Cancel compare", null);
        compareEndButton.hide();
        return compareEndButton;
    }

    private Anchor createCompareLink() {
        final Anchor compareLink = new Anchor("Compare to ...", null);

        compareLink.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                /* Restrict node selector to type of the current node */
                Set<String> s = new HashSet<String>(1);
                s.add(contentNode.getNode().getType());

                final ContentTreePopUp popup = ContentTreePopUp.getInstance(s, false);
                popup.setHeading("Select a node for comparison");
                popup.addListener(Events.Select, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {
                        TreeContentNodeModel selectedNode = popup.getSelectedItem();
                        if (contentNode.getNode().getKey().equals(selectedNode.getKey())) {
                            MessageBox.alert("Compare failed", "Comparing to itself does not make much sense ...", null);
                            return;
                        }
                        compareUtil = new CompareNodesUtil(selectedNode.getKey(), popup.getTreepanel().getSelectedPath());
                    }
                });

                popup.show();
            }
        });
        return compareLink;
    }

    private Anchor createViewHistoryLink() {
        final Anchor viewHistoryLink = new Anchor("View History", null);
        viewHistoryLink.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                viewHistory();
            }
        });
        return viewHistoryLink;
    }

    private PreviewAnchor createPreviewLink() {
        final String previewUrl = "about:blank";
        final PreviewAnchor previewLink = new PreviewAnchor("Preview", previewUrl);
        previewLink.setNewWindow(true);
        previewLink.hide();
        return previewLink;
    }

    public void updatePreviewLink() {
        if (contentNode != null) {
            final String url = contentNode.getPreviewUrl();
            if (url != null) {
                previewLink.setOriginalUrl(contentNode.getPreviewUrl());
                previewLink.show();
            } else {
                previewLink.hide();
            }
        } else {
            previewLink.hide();
        }
    }

    protected void viewHistory() {
        if (contentNode != null) {
            String url = Window.Location.getHref().replace("cmsgwt.html", "viewhistory.html");
            int idx = url.indexOf('#');
            if (idx != -1) {
                url = url.substring(0, Math.min(idx + 1, url.length()));
                url += contentNode.getNode().getKey();
            }
            Window.open(url, "_blank", null);
        }
    }

    protected void saveAction() {
        CmsGwt.getContentService().save(WorkingSet.getWorkingSet(), new SaveNodeAction());

        // save context
        contextToolBar.saveContext();
    }

    public GwtNodeData getContentNode() {
        return contentNode;
    }

    public void setContentNode(GwtNodeData contentNode) {
        this.contentNode = contentNode;
    }

    public NodeTree getTreePanel() {
        return treePanel;
    }

    public void setTreePanel(NodeTree treePanel) {
        this.treePanel = treePanel;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public Container<?> getEditorComponent() {
        return editorComponent;
    }

    public CompareNodesUtil getCompareUtil() {
        return compareUtil;
    }

    public Anchor getCompareEndButton() {
        return compareCancelLink;
    }

    public Anchor getCompareButton() {
        return compareLink;
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        editorComponent = null;
        compareLink = null;
        MainLayout.getInstance().removePublishProgressListener(this);
    }

    @Override
    public void onPublishStarted() {
        if (saveButton != null && !CmsGwt.getCurrentUser().isDraftActive()) {
            saveButton.disable();
        }
    }

    @Override
    public void onPublishFinished() {
        if (saveButton != null) {
            saveButton.enable();
        }
    }
}
