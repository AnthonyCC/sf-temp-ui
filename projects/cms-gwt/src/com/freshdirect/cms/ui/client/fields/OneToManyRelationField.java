package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.ContentIdWindow;
import com.freshdirect.cms.ui.client.ContentTypeSelectorPopup;
import com.freshdirect.cms.ui.client.FixedGridDropTarget;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.NewGwtNodeCallback;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OneToManyRelationField extends MultiField<List<OneToManyModel>> implements HasCustomDefaultValue<List<OneToManyModel>> {
    

	protected Grid<OneToManyModel> grid;
	protected ListStore<OneToManyModel> store;
	protected Set<String> allowedTypes;
	protected List<GridCellRenderer<OneToManyModel>> extraColumns;
	protected boolean navigable;
	protected boolean readonly;

	final static int                       MAIN_LABEL_WIDTH = 415;
	
	private ToolButton	addButton;
	private ToolButton	createButton;
	private ToolButton	copyButton;
	private ToolButton	moveButton;
	private ToolButton	deleteButton;
	private ToolButton	sortButton;
	
	private CheckBoxSelectionModel<OneToManyModel> selection;
	
	private String attributeKey;
	
	/**
	 * empty constructor for descendants.
	 */
	protected OneToManyRelationField() {
	    
	}
	
	public OneToManyRelationField( String attrKey, Set<String> allowedTypes, boolean navigable, boolean readonly ) {
		this( attrKey, allowedTypes, navigable, new ArrayList<GridCellRenderer<OneToManyModel>>( 0 ), readonly );
	}

	public OneToManyRelationField( String attrKey, Set<String> allowedTypes, boolean navigable,
			List<GridCellRenderer<OneToManyModel>> extraColumns, boolean readonly ) {
		super();
		this.attributeKey = attrKey;
		this.allowedTypes = allowedTypes;
		this.navigable = navigable;
		this.extraColumns = extraColumns;
		this.readonly = readonly;
		initialize();
	}
	
	
	protected void initialize() {
		
		ContentPanel cp = new ContentPanel();		
		cp.setWidth(MAIN_LABEL_WIDTH + 50);
		cp.addStyleName("one-to-many");
		
		
		// ==================================== COPY ====================================
		copyButton = new ToolButton("copy-button");
		copyButton.setToolTip( new ToolTipConfig( "COPY", "Copy selected relations to another node." ) );		
		copyButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				
				final List<OneToManyModel> selectedList = selection.getSelectedItems();
				
				final ContentTreePopUp popup = ContentTreePopUp.getInstance( null, false );		
				popup.setHeading( "Copy " + selectedList.size() + " item(s) to :" );
				
				popup.addListener( Events.Select, new Listener<BaseEvent>() {
					
					public void handleEvent( BaseEvent be ) {
						ContentNodeModel targetNode = popup.getSelectedItem();
						addRelationshipsToNode( targetNode.getKey(), attributeKey, selectedList );
						MainLayout.setStatus( "" + selectedList.size() + " item(s) copied to " + targetNode.getLabel() );
					}					
				});		
				popup.show();

			}			
		});
		cp.getHeader().addTool( copyButton );
		
		
		if ( !readonly ) {
				
			// ==================================== MOVE ====================================
			moveButton = new ToolButton("move-button");
			moveButton.setToolTip( new ToolTipConfig( "MOVE", "Move selected relations to another node." ) );		
			moveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					
					final List<OneToManyModel> selectedList = selection.getSelectedItems();
					
					final ContentTreePopUp popup = ContentTreePopUp.getInstance( null, false );		
					popup.setHeading( "Move " + selectedList.size() + " item(s) to :" );
					
					popup.addListener( Events.Select, new Listener<BaseEvent>() {
						
						public void handleEvent( BaseEvent be ) {
							ContentNodeModel targetNode = popup.getSelectedItem();
							addRelationshipsToNode( targetNode.getKey(), attributeKey, selectedList );
							removeRelationships( selectedList );
							MainLayout.setStatus( "" + selectedList.size() + " item(s) moved to " + targetNode.getLabel() );
						}					
					});		
					popup.show();
	
				}			
			});
			cp.getHeader().addTool( moveButton );
	
			
			// ==================================== DELETE ====================================		
			deleteButton = new ToolButton("delete-button");
			deleteButton.setToolTip( new ToolTipConfig( "DELETE", "Delete selected relations." ) );		
			deleteButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
	
					final List<OneToManyModel> selectedList = selection.getSelectedItems();
	
					String question;
					if ( selectedList.size() == 0 ) {
						return;
					} else if ( selectedList.size() == 1 ) {
						question = "Do you really want to delete '" + selectedList.get( 0 ).getLabel() + "' ?";
					} else {
						question = "Do you really want to delete " + selectedList.size() + " items?";
					}
					MessageBox.confirm( "Delete", question, new Listener<MessageBoxEvent>() {
						
						public void handleEvent( MessageBoxEvent we ) {
							if ( we.getButtonClicked().getText().equals( "Yes" ) ) {
								removeRelationships( selectedList );
							}
						}
					} );
				}			
			});
			cp.getHeader().addTool( deleteButton );
			
			
			// ==================================== SORT ====================================		
			sortButton = new ToolButton("sort-button");
			sortButton.setToolTip( new ToolTipConfig( "SORT", "Sort the relations alphabetically." ) );		
			sortButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					store.sort( "label", SortDir.ASC );
					grid.getView().refresh( false );
					store.setStoreSorter(null);
				}
			});
			cp.getHeader().addTool( sortButton );
	
			
			
			// TODO separatortoolitem doesnt work in header, need to add separator with different method
			cp.getHeader().addTool( new SeparatorToolItem() );
			
			
			// ==================================== ADD ====================================
			addButton = new ToolButton("add-relation");		
			addButton.setToolTip( new ToolTipConfig( "ADD", "Add a relation..." ) );		
			addButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					final ContentTreePopUp popup = ContentTreePopUp.getInstance( getAllowedTypes(), true );		
					popup.setHeading(getFieldLabel());
					popup.addListener(Events.Select, new Listener<BaseEvent>() {
						public void handleEvent(BaseEvent be) {
							addOneToManyModels( popup.getSelectedItems() );
//							MainLayout.getMainTree().invalidate();
//					        MainLayout.scrollHack();
						}					
					});		
					
					popup.show();					
				}			
			});
			
			cp.getHeader().addTool(addButton);
			
			
			// ==================================== CREATE ====================================
			if (navigable && allowedTypes.size() > 0) {
			    createButton = new ToolButton("create-relation");
				createButton.setToolTip( new ToolTipConfig( "CREATE", "Create new node..." ) );
			    createButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			        @Override
			        public void handleEvent(BaseEvent be) {
			            if (OneToManyRelationField.this.allowedTypes.size() == 1) {
			                generateUniqueIdForType(OneToManyRelationField.this.allowedTypes.iterator().next());
			                return;
			            }
			            
			            final ContentTypeSelectorPopup typeSelector = new ContentTypeSelectorPopup(OneToManyRelationField.this.allowedTypes);
		                typeSelector.addListener(Events.Select, new Listener<BaseEvent>() {
		                    @Override
		                    public void handleEvent(BaseEvent be) {
		                        String type = typeSelector.getSelectedType();
		                        generateUniqueIdForType(type);
		                    } 
		                });
		                typeSelector.show();
			        }
			    });
			    cp.getHeader().addTool(createButton);
			}
			
		}
		
		
		store = new ListStore<OneToManyModel>();
		store.removeAll();

        List<ColumnConfig> config = setupExtraColumns();
		grid = new Grid<OneToManyModel>(store, new ColumnModel(config));
				
		grid.setSelectionModel( selection );
		grid.addPlugin( selection );

		grid.setWidth(MAIN_LABEL_WIDTH + 50);			
		grid.setAutoHeight(true);
		grid.setHideHeaders(true);
		grid.setStripeRows(true);
		grid.getView().setForceFit(true);
		grid.getView().setEmptyText("empty");		
	    
		grid.hide();		
		

		if ( !readonly ) {
			GridDragSource dragSource = new GridDragSource(grid);
			dragSource.setGroup(grid.getId() + "-ddgroup");
			FixedGridDropTarget dropTarget = new FixedGridDropTarget(grid);
			dropTarget.setAllowSelfAsSource(true);
			dropTarget.setFeedback(Feedback.INSERT);
			dropTarget.setGroup(grid.getId() + "-ddgroup");
			
			dropTarget.addDNDListener(new DNDListener() {
				@Override
				public void dragDrop(DNDEvent e) {				
					grid.getView().refresh(true);
			        fireEvent(AttributeChangeEvent.TYPE, new AttributeChangeEvent(OneToManyRelationField.this));
				}
			});
		}
			
		cp.add( grid );
				
		AdapterField f = new AdapterField( cp );
		f.setWidth( MAIN_LABEL_WIDTH + 70 );
		
		if ( readonly ) {
			f.setReadOnly( true );
		}
		
		add(f);
	}
	


    protected List<ColumnConfig> setupExtraColumns() {
        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        {
            ColumnConfig indexColumn = new ColumnConfig();  
                indexColumn.setId("idx");
                indexColumn.setMenuDisabled(true);
                indexColumn.setWidth(25);
                indexColumn.setStyle("vertical-align: middle;");
                indexColumn.setAlignment(HorizontalAlignment.RIGHT);
                indexColumn.setRenderer(Renderers.ROW_INDEX);
                config.add(indexColumn);
        }

        if ( extraColumns == null || extraColumns.size()==0 ) {
		    ColumnConfig column = new ColumnConfig();  
		    column.setId("label");
            column.setMenuDisabled(true);
            column.setWidth(MAIN_LABEL_WIDTH);
            column.setRenderer(Renderers.GRID_LINK_RENDERER);
            config.add(column);
		} else {
            int idx = 0;
            int size = MAIN_LABEL_WIDTH / (extraColumns.size());
            for (GridCellRenderer<OneToManyModel> renderer : extraColumns) {
                ColumnConfig extraColumn = new ColumnConfig();
                extraColumn.setId("extra_" + idx);
                extraColumn.setMenuDisabled(true);
                extraColumn.setWidth(size);
                extraColumn.setStyle("vertical-align: middle;");
                extraColumn.setRenderer(renderer);
                config.add(extraColumn);
                idx++;
            }
		}
        {
        	selection = new CheckBoxSelectionModel<OneToManyModel>();
			config.add( selection.getColumn() );              			
        }
        return config;
    }


	public Set<String> getAllowedTypes() {
		return allowedTypes;
	}
	
	public void setAllowedTypes(Set<String> aTypes) {
		allowedTypes = aTypes;
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public void setValue(final List<OneToManyModel> values) {
		// TODO FIXME bad solution, needs some checking
		if (rendered) {
		    if (values instanceof List) {
	    		if (store.getCount() > 0) {
	    			store.removeAll();
	    		}
	    		store.add(values);
	    		if (store.getCount() == 0) {
	    			grid.hide();
	    		} else {
	    			grid.show();
	    		}
		    } else {
		        super.setValue(values);
		    }
		} else {
			final OneToManyRelationField tmp = this;
			addListener( Events.Render, new Listener<BaseEvent>() {
				@Override
				public void handleEvent( BaseEvent be ) {
					tmp.setValue( values );
					tmp.removeListener( Events.Render, this );
				}
			});
		}
	}
	
	@Override
	public List<OneToManyModel> getValue() {
		return store.getModels();
	}
	
    @Override
    public List<OneToManyModel> getDefaultValue() {
        return new ArrayList<OneToManyModel>(0);
    }

	private void addRelationshipsToNode ( String targetNodeKey, final String targetAttributeKey, final List<OneToManyModel> relationships ) {
		
		if ( targetNodeKey == null || targetNodeKey.trim().length() == 0 || relationships == null || relationships.size() == 0 || targetAttributeKey == null || targetAttributeKey.trim().length() == 0 ) {
			return;
		}
		
		GwtContentNode node = WorkingSet.get( targetNodeKey );
		if ( node != null ) {
			addRelationshipsToNodeHelper( node, targetAttributeKey, relationships );			
		} else {			
			CmsGwt.getContentService().getNodeData( targetNodeKey, new AsyncCallback<GwtNodeData>() {		
		        public void onFailure( Throwable caught ) {
		            MessageBox.alert( "Error", "Cannot add relations to node because node loading failed.", null );
		            MainLayout.setStatus( "Adding relationships failed." );
		        }	
		        public void onSuccess( GwtNodeData nodeData ) {
		        	addRelationshipsToNodeHelper( nodeData.getNode(), targetAttributeKey, relationships ); 
		        }
			} );
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addRelationshipsToNodeHelper ( GwtContentNode node, String targetAttributeKey, List<OneToManyModel> relationships ) {
		
		List<OneToManyModel> oldValue = (List<OneToManyModel>)node.getAttributeValue( targetAttributeKey );  
		List<OneToManyModel> newValue;
		if ( oldValue != null ) {
			newValue = new ArrayList<OneToManyModel>( oldValue );
		} else {
			newValue = new ArrayList<OneToManyModel>();
		}

		// TODO check if it is already in the list? duplicate items are possible here!
		newValue.addAll( relationships );
		node.changeValue( targetAttributeKey, (Serializable)newValue );
		
		// TODO check if node really changed?  
    	WorkingSet.add( node );
		
	}
	
	private void removeRelationships( final List<OneToManyModel> relationships ) {

		if ( relationships == null || relationships.size() == 0 ) {
			return;
		}
		
		for ( OneToManyModel model : relationships ) {
			store.remove( model );
		}
		
		if ( store.getCount() == 0 ) {
			grid.hide();
		} else {
			grid.getView().refresh( false );
		}
		fireEvent( AttributeChangeEvent.TYPE, new AttributeChangeEvent( OneToManyRelationField.this ) );

	}


    void generateUniqueIdForType(final String type) {
        final ContentServiceAsync contentService = (ContentServiceAsync) GWT.create(ContentService.class);
        contentService.generateUniqueId(type, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                final ContentIdWindow w = new ContentIdWindow(result, "ID of '"+type+"'");
                w.addListener(Events.Select, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        String id = w.getContentId();
                        contentService.createNodeData(type, id, new NewGwtNodeCallback(OneToManyRelationField.this));
                    } 
                });
                w.show();
            } 
            @Override
            public void onFailure(Throwable caught) {
                
            }
        });
    }

    public void addOneToManyModel(String type, String key, String label) {
        OneToManyModel model = createModel(type, key, label);
        store.add(model);
        grid.show();
        grid.getView().refresh(false);
        fireEvent(AttributeChangeEvent.TYPE, new AttributeChangeEvent(this));       
//        NodeTree.removeItemFromOrphans( key );
    }

	public void addOneToManyModels( List<ContentNodeModel> list ) {
		for ( ContentNodeModel cmModel : list ) {
			OneToManyModel otmModel = createModel( cmModel.getType(), cmModel.getKey(), cmModel.getLabel() );
			store.add( otmModel );
//			NodeTree.removeItemFromOrphans( cmModel.getKey() );
		}
		grid.show();
		grid.getView().refresh( false );
		fireEvent( AttributeChangeEvent.TYPE, new AttributeChangeEvent( this ) );
	}

    protected OneToManyModel createModel(String type, String key, String label) {
        return new OneToManyModel(type, key, label, store.getCount());
    }

    /**
     * refresh the UI, this call is needed when new columns added or deleted from the extra column list.
     */
    public void refreshColumns() {
        grid.reconfigure(store, new ColumnModel(setupExtraColumns()));
    }

    @Override
    public void disable() {
    	super.disable();
    	if ( addButton != null )
    		addButton.disable();
    	if ( createButton != null )
    		createButton.disable();
    	if ( copyButton != null )
    		copyButton.disable();
    	if ( moveButton != null )
    		moveButton.disable();
    	if ( deleteButton != null )
    		deleteButton.disable();
    	if ( sortButton != null )
    		sortButton.disable();
    }
    @Override
    public void enable() {
    	super.enable();
    	if ( addButton != null )
    		addButton.enable();
    	if ( createButton != null )
    		createButton.enable();
    	if ( copyButton != null )
    		copyButton.enable();
    	if ( moveButton != null )
    		moveButton.enable();
    	if ( deleteButton != null )
    		deleteButton.enable();
    	if ( sortButton != null )
    		sortButton.enable();
    }

	@Override
	public void setReadOnly( boolean readOnly ) {
		super.setReadOnly( readOnly );
	}
}
