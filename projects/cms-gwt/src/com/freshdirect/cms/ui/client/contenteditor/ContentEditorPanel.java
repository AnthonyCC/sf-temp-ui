package com.freshdirect.cms.ui.client.contenteditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.Anchor;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.action.SaveNodeAction;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.google.gwt.user.client.Window;

public class ContentEditorPanel extends DetailPanel {

	private GwtNodeData contentNode;
	private NodeTree treePanel;
	private ActionBar actionBar;
	private Anchor compareButton;
	private Anchor compareEndButton;
	private Container<?> editorComponent; // root editor component
	private CompareNodesUtil compareUtil;
	
	private class ContextToolBar extends LayoutContainer {
		
		public ContextToolBar() {
			setLayout(new HBoxLayout());
	        GwtNodeContext ctx = contentNode.getContexts();
	        
	        final List<String> contextPathsList = new ArrayList<String>(ctx.size());
	        final List<String> contextLabelsList = new ArrayList<String>(ctx.size());
	        Listener<BaseEvent> synchronizeButtonClick = null;

	        for (String path : ctx.getPaths()) {
	            contextPathsList.add(path);
	            contextLabelsList.add(ctx.getLabel(path));
	        }
	        
	        ToolButton synchronizeButton = new ToolButton("synchronize-button");
	        synchronizeButton.setHeight(16);
	        synchronizeButton.setWidth(16);
	        HBoxLayoutData buttonMargin = null;
	        
	        if( ctx.size() > 1 ) {
	        	
	            final SimpleComboBox<String> contextDropdown = new SimpleComboBox<String>();
	            
	            contextDropdown.setEmptyText("Select active context");
	            contextDropdown.setWidth(500);            
	            contextDropdown.setEditable(false);
	            contextDropdown.setForceSelection(true);
	        	contextDropdown.setTriggerAction(TriggerAction.ALL);
	            
	            contextDropdown.add( contextLabelsList );
	                        
	            contextDropdown.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
	                @Override
	                public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
	                    int selectedIndex = contextDropdown.getSelectedIndex();
	                    if (selectedIndex == -1) {
	                        return;
	                    }
	                    String path = contextPathsList.get(selectedIndex);
	                    contentNode.changeContext(path);
	                }
	            });
	            
	            add( contextDropdown );
	            
	            synchronizeButtonClick = new Listener<BaseEvent>() {
	                @Override
	                public void handleEvent(BaseEvent be) {
	                    int index = contextDropdown.getSelectedIndex();
	                    if (index == -1) {
	                        MessageBox.alert("Info", "First select a context, then push the synchronize button to navigate in the tree.", null);
	                    } else {
	                        String path = contextPathsList.get(index);
	                        treePanel.synchronize(path);
	                    }
	                };
	            };

	            // context ...
	            int index = -1;
	            String contextPath = treePanel.getSelectedPath();

	            if (contextPath != null)
	                index = contextPathsList.indexOf(contextPath);

	            if (index == -1) {
	                contextPath = contentNode.getDefaultContextPath();
	            }

	            if (contextPath != null) {
	                index = contextPathsList.indexOf(contextPath);
	            }
	            if (index != -1) {
	                contextDropdown.setValue(contextDropdown.getStore().getAt(index));
	            }
	        	
	            buttonMargin = new HBoxLayoutData(3, 0, 0, 2);
	            
	        } else if ( ctx.size() > 0 ) {
	        	
	        	final LabelField contextField = new LabelField(); 
	        	
	        	contextField.setValue(contextLabelsList.get(0));
	        	contextField.setHeight(22);
	        	contextField.setReadOnly(true);
	        	
	        	
	            synchronizeButtonClick = new Listener<BaseEvent>() {
	                @Override
	                public void handleEvent(BaseEvent be) {
	                    String path = contextPathsList.get(0);
	                    treePanel.synchronize(path);
	                };
	            };
	            
	            add(contextField);
	            buttonMargin = new HBoxLayoutData(2, 0, 0, 2);
	        
	    	} else {	    	
		    	// TODO what to do if there are no contexts at all ??
	    		
	            final String contextPath = treePanel.getSelectedPath().replace( TreeContentNodeModel.pathSeparator, " " );

	            if ( contextPath != null ) {
	            	
	            	final LabelField contextField = new LabelField(); 
	            	
	            	contextField.setValue( contextPath );
					contextField.setHeight( 22 );
					contextField.setReadOnly( true );            	
	            	
	                synchronizeButtonClick = new Listener<BaseEvent>() {
	                    @Override
	                    public void handleEvent(BaseEvent be) {
	                        treePanel.synchronize( contextPath );
	                    };
	                };
	                
	                add(contextField);
	                buttonMargin = new HBoxLayoutData(2, 0, 0, 2);            	            	
	            }
	            
		    }
	        
	        // if all above options failed - we have nothing to display
	        if ( synchronizeButtonClick == null  ) {
	        	return;
	        }
	        
	        synchronizeButton.setToolTip( new ToolTipConfig("Synchronize", "Synchronize the node tree with the selected path.") );  
	        synchronizeButton.addListener(Events.OnClick, synchronizeButtonClick );

	        add(synchronizeButton, buttonMargin);			
		}
	}
	
	
	public ContentEditorPanel() {
		setBorders(false);
	}
	
	public void setupLayout() {
				
        LayoutContainer head = new LayoutContainer(new AnchorLayout());
        LayoutContainer body = new LayoutContainer(new FitLayout());

		HtmlContainer htmlTitle = new HtmlContainer(getHeaderMarkup());		
        htmlTitle.add(new ContextToolBar(), "#main-toolbar");
        
        actionBar = new ActionBar();
        actionBar.setText(contentNode.getNode().getKey());
        prepareButtons();

        head.add(htmlTitle,new AnchorData("100%"));
        head.add(actionBar,new AnchorData("100%"));
        editorComponent = ContentEditorFactory.getEditor(new GwtContextualizedNodeData(contentNode, treePanel.getSelectedPath()));
        
        body.add(editorComponent);

		((BorderLayout) ManageStoreView.getInstance().getLayout()).expand(LayoutRegion.WEST);
		setHeader(head);
		setBody(body);		
		layout();
	}
	
    protected String getHeaderMarkup() {
    	GwtContentNode node = contentNode.getNode();  
    	StringBuilder tmpl = new StringBuilder();
    	
    	tmpl.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" class=\"pageTitle\">");
    	tmpl.append("<tbody><tr><td class=\"context\"></td><td width=\"75\" valign=\"bottom\" align=\"right\" rowspan=\"2\" style=\"line-height: 0pt;\">");
    	tmpl.append("<img width=\"75\" height=\"66\" src=\"img/banners/" + node.getType() + ".gif\"/></td></tr><tr><td valign=\"bottom\" style=\"position:relative\"><div id=\"main-toolbar\" style=\"position:absolute;top:0px;left:0px;right:80px;height:40px\"></div>");
    	tmpl.append("<h1 title=\"" + node.getKey()+ "\">");
       	tmpl.append("<nobr><span id=\"fadeError\">" + node.getLabel() + "</span></nobr>");    	
    	tmpl.append("</h1>");
    	tmpl.append("</td></tr></tbody></table>");
    	return tmpl.toString();
    }    

    protected void prepareButtons() {
        /** SAVE & DISCARD */
        if (!contentNode.isReadonly()) {
            Button saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                	((BorderLayout) ManageStoreView.getInstance().getLayout()).expand(LayoutRegion.WEST);              	
                    saveAction();
                }
            });
            saveButton.setToolTip( new ToolTipConfig( "Save", "Saves the workset." ) );
            saveButton.addStyleName( "green-button" );
            saveButton.setWidth(60);
            actionBar.addButton( saveButton ); 
/*
 * 
 *             Discard button uncomment, if needed
 *             
            Button discardButton = new Button("Discard", new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    nodeSelected(currentNode.getNode().getKey(), null, false);
                }
            });
            discardButton.setToolTip( new ToolTipConfig( "Discard", "Discard the changes in this workset." ) );
            discardButton.addStyleName( "red-button" );
            contentToolBar.addButton( discardButton );
*/            
            
        }




        compareButton = new Anchor("Compare to ...", null );

        compareButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				
	            /* Restrict node selector to type of the current node */
        		Set<String> s = new HashSet<String>(1);
        		s.add(contentNode.getNode().getType());
        		
				final ContentTreePopUp popup = ContentTreePopUp.getInstance( s, false );		
				popup.setHeading("Select a node for comparison");
				popup.addListener(Events.Select, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						List<TreeContentNodeModel> sel = popup.getSelectedItems();
						
						TreeContentNodeModel selectedNode = sel.get(0);
						if (contentNode.getNode().getKey().equals( selectedNode.getKey() ) ) {
							MessageBox.alert("Compare failed", "Comparing to itself does not make much sense ..." , null);
							return;
						}
						compareUtil = new CompareNodesUtil( selectedNode.getKey(), popup.getTreepanel().getSelectedPath());						
					}
				});

				popup.show();					
			}			
		});
        
        compareEndButton = new Anchor("Cancel compare", null);
        compareEndButton.hide();
        actionBar.addLink(compareEndButton, new Margins(0,10,0,0) );

        actionBar.addLink( compareButton, new Margins(0,10,0,0) );

        Anchor viewHistoryButton = new Anchor("View History", null );
        viewHistoryButton.addListener( Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				viewHistory();	
			}
        });
        actionBar.addLink(viewHistoryButton, new Margins(0,10,0,0) );

		if ( contentNode.getPreviewUrl() != null ) {
			final String previewUrl = contentNode.getPreviewUrl();
			final Anchor previewAnchor = new Anchor( "Preview", previewUrl );
			previewAnchor.setNewWindow( true );
			previewAnchor.addListener( Events.OnClick, new Listener<BaseEvent>() {
				@Override
				public void handleEvent( BaseEvent be ) {
					previewAnchor.setUrl( previewUrl + "&preview=" + Math.random() );					
				}				
			});
			actionBar.addLink( previewAnchor, new Margins(0,10,0,0) );
		}
        
	}
    
    protected void viewHistory() {
        if (contentNode != null) {
        	String url = Window.Location.getHref().replace("cmsgwt.html", "viewhistory.html");
        	int idx = url.indexOf( '#' );
        	if ( idx != -1 ) {
        		url = url.substring( 0, Math.min( idx + 1, url.length() ) );
        		url += contentNode.getNode().getKey();
        	}
        	Window.open(url, "_blank", null);
        }    	
    }
        
    protected void saveAction() {
        CmsGwt.getContentService().save( WorkingSet.getWorkingSet(), new SaveNodeAction());
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
		return compareEndButton;
	}
	
	public Anchor getCompareButton() {
		return compareButton;
	}
	
	@Override
	protected void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		editorComponent = null;
		compareButton = null;
	}	
}
