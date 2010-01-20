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
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.ContentIdWindow;
import com.freshdirect.cms.ui.client.ContentTypeSelectorPopup;
import com.freshdirect.cms.ui.client.FixedGridDropTarget;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.NewGwtNodeCallback;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OneToManyRelationField extends MultiField<List<OneToManyModel>>
		implements HasCustomDefaultValue<List<OneToManyModel>> {

	protected AdapterField af;
	protected Grid<OneToManyModel> grid;
	protected ListStore<OneToManyModel> store;
	protected Set<String> allowedTypes;
	protected List<GridCellRenderer<OneToManyModel>> extraColumns;
	protected boolean navigable;

	final static int MAIN_LABEL_WIDTH = 415;

	private ToolBar theToolBar;

	private ToolButton addButton;
	private ToolButton createButton;
	private ToolButton copyButton;
	private ToolButton moveButton;
	private ToolButton deleteButton;
	private ToolButton sortButton;
	private CheckBox selectCheckbox;

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

	public OneToManyRelationField(String attrKey, Set<String> allowedTypes,
			boolean navigable,
			List<GridCellRenderer<OneToManyModel>> extraColumns,
			boolean readonly) {
		super();
		this.attributeKey = attrKey;
		this.allowedTypes = allowedTypes;
		this.navigable = navigable;
		this.extraColumns = extraColumns;

		initialize();

		setReadOnly(readonly);
	}

	protected Listener<BaseEvent> createButtonListener = new Listener<BaseEvent>() {
		@Override
		public void handleEvent(BaseEvent be) {
			if ( OneToManyRelationField.this.allowedTypes.size() == 1 ) {
				generateUniqueIdForType( OneToManyRelationField.this.allowedTypes.iterator().next() );
				return;
			}
			final ContentTypeSelectorPopup typeSelector = new ContentTypeSelectorPopup( OneToManyRelationField.this.allowedTypes );
			typeSelector.addListener( Events.Select, new Listener<BaseEvent>() {

				@Override
				public void handleEvent( BaseEvent be ) {
					String type = typeSelector.getSelectedType();
					generateUniqueIdForType( type );
				}
			} );
			typeSelector.show();
		}
	};

	protected Listener<BaseEvent> addButtonListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent be) {
			final ContentTreePopUp popup = ContentTreePopUp.getInstance(
					getAllowedTypes(), true);
			popup.setHeading(getFieldLabel());
			popup.addListener(Events.Select, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					addOneToManyModels(popup.getSelectedItems());
					// MainLayout.getMainTree().invalidate();
					// MainLayout.scrollHack();
				}
			});

			popup.show();
		}
	};

	protected Listener<BaseEvent> sortButtonListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent be) {

			store.sort("label", SortDir.ASC);
			grid.getView().refresh(false);
			store.setStoreSorter(null);

		}
	};

	protected Listener<BaseEvent> deleteButtonListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent be) {

			final List<OneToManyModel> selectedList = selection
					.getSelectedItems();

			String question;
			if (selectedList.size() == 0) {
				return;
			} else if (selectedList.size() == 1) {
				question = "Do you really want to delete '"
						+ selectedList.get(0).getLabel() + "' ?";
			} else {
				question = "Do you really want to delete "
						+ selectedList.size() + " items?";
			}
			MessageBox.confirm("Delete", question,
					new Listener<MessageBoxEvent>() {

						public void handleEvent(MessageBoxEvent we) {
							if (we.getButtonClicked().getText().equals("Yes")) {
								removeRelationships(selectedList);
							}
						}
					});
		}
	};

	protected Listener<BaseEvent> copyButtonListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent be) {

			final List<OneToManyModel> selectedList = selection
					.getSelectedItems();

			final ContentTreePopUp popup = ContentTreePopUp.getInstance(null,
					false);
			popup.setHeading("Copy " + selectedList.size() + " item(s) to :");

			popup.addListener(Events.Select, new Listener<BaseEvent>() {

				public void handleEvent(BaseEvent be) {
					ContentNodeModel targetNode = popup.getSelectedItem();
					addRelationshipsToNode(targetNode.getKey(), attributeKey,
							selectedList);
				}
			});
			popup.show();

		}
	};

	protected Listener<BaseEvent> moveButtonListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent be) {

			final List<OneToManyModel> selectedList = selection
					.getSelectedItems();

			final ContentTreePopUp popup = ContentTreePopUp.getInstance(null,
					false);
			popup.setHeading("Move " + selectedList.size() + " item(s) to :");

			popup.addListener(Events.Select, new Listener<BaseEvent>() {

				public void handleEvent(BaseEvent be) {
					ContentNodeModel targetNode = popup.getSelectedItem();
					addRelationshipsToNode(targetNode.getKey(), attributeKey,
							selectedList);
					removeRelationships(selectedList);
				}
			});
			popup.show();

		}
	};

	protected Listener<BaseEvent> selectListener = new Listener<BaseEvent>() {
		public void handleEvent(BaseEvent event) {
			if (selectCheckbox.getValue()) {
				selection.selectAll();
			} else {
				selection.deselectAll();
			}
		}
	};

	/**
	 * Drag and drop listener for the grid component.
	 * 
	 */
	protected class GridDNDListener<T extends ContentNodeModel> extends
			DNDListener {
		Grid<T> grid;

		FixedGridDropTarget dropTarget;

		protected Timer scrollUpTimer = new Timer() {
			@Override
			public void run() {
				boolean canScrollTop = grid.getBounds(false).y < 20;

				if (canScrollTop) {
					MainLayout.getInstance().setVScrollPosition(
							MainLayout.getInstance().getVScrollPosition() - 10);
				} else {
					cancel();
				}
			}
		};

		protected Timer scrollDownTimer = new Timer() {
			@Override
			public void run() {
				boolean canScrollBottom = grid.getBounds(false).height
						+ grid.getBounds(false).y > MainLayout.getInstance()
						.getBounds(false).height - 40;

				if (canScrollBottom) {
					MainLayout.getInstance().setVScrollPosition(
							MainLayout.getInstance().getVScrollPosition() + 10);
				} else {
					cancel();
				}
			}
		};

		public GridDNDListener(Grid<T> aGrid) {
			this.grid = aGrid;

			GridDragSource dragSource = new GridDragSource(this.grid);
			dragSource.setGroup(this.grid.getId() + "-ddgroup");

			dropTarget = new FixedGridDropTarget(this.grid);
			dropTarget.setAllowSelfAsSource(true);
			dropTarget.setFeedback(Feedback.INSERT);
			dropTarget.setGroup(aGrid.getId() + "-ddgroup");
		}

		@Override
		public void dragMove(DNDEvent e) {
			if (e.getClientY() < 80) {
				scrollUpTimer.scheduleRepeating(10);
			} else {
				scrollUpTimer.cancel();
			}

			if (e.getClientY() > (MainLayout.getInstance().getBounds(false).height - 80)) {
				scrollDownTimer.scheduleRepeating(10);
			} else {
				scrollDownTimer.cancel();
			}
		}

		@Override
		public void dragDrop(DNDEvent e) {
			scrollUpTimer.cancel();
			scrollDownTimer.cancel();
			grid.getView().refresh(true);
			fireEvent(Events.Change, new FieldEvent(OneToManyRelationField.this));
		}

		public void observe() {
			dropTarget.enable();
			dropTarget.addDNDListener(this);
		}

		public void forget() {
			dropTarget.removeDNDListener(this);
			dropTarget.disable();
		}
	}

	protected GridDNDListener<OneToManyModel> dndListener;

	protected void initialize() {

		ContentPanel cp = new ContentPanel();
		cp.addStyleName("one-to-many");
		cp.setHeaderVisible(false);

		theToolBar = new ToolBar();
		cp.setTopComponent(theToolBar);

		{
			// ==================================== CREATE ====================================
			if ( navigable && allowedTypes.size() > 0 ) {
				createButton = new ToolButton( "create-relation" );
				createButton.setToolTip( new ToolTipConfig( "CREATE", "Create new node..." ) );

				theToolBar.add( createButton );
			}

			// ==================================== ADD ====================================
			if ( !( this instanceof CustomGridField ) ) {
				addButton = new ToolButton( "add-relation" );
				addButton.setToolTip( new ToolTipConfig( "ADD", "Add a relation..." ) );
				theToolBar.add( addButton );
			}

			// ==================================== SORT ====================================
			sortButton = new ToolButton( "sort-button" );
			sortButton.setToolTip( new ToolTipConfig( "SORT", "Sort the relations alphabetically." ) );
			theToolBar.add( sortButton );

			theToolBar.add(new FillToolItem());

			// ==================================== DELETE ====================================
			deleteButton = new ToolButton( "delete-button" );
			deleteButton.setToolTip( new ToolTipConfig( "DELETE", "Delete selected relations." ) );
			theToolBar.add( deleteButton );

			// ==================================== COPY ====================================
			copyButton = new ToolButton( "copy-button" );
			copyButton.setToolTip( new ToolTipConfig( "COPY", "Copy selected relations to another node." ) );
			theToolBar.add( copyButton );

			// ==================================== MOVE ====================================
			moveButton = new ToolButton( "move-button" );
			moveButton.setToolTip( new ToolTipConfig( "MOVE", "Move selected relations to another node." ) );
			theToolBar.add( moveButton );

			// ==================================== SELECT CHECKBOX ====================================
			if ( !( this instanceof VariationMatrixField || this instanceof CustomGridField ) ) {
				selectCheckbox = new CheckBox();
				selectCheckbox.setToolTip( new ToolTipConfig( "Select all/none", "Select all/none relations." ) );
				theToolBar.add( selectCheckbox );
			}
		}

		store = new ListStore<OneToManyModel>();
		store.removeAll();

		List<ColumnConfig> config = setupExtraColumns();
		grid = new Grid<OneToManyModel>(store, new ColumnModel(config));

		grid.setSelectionModel(selection);
		grid.addPlugin(selection);

		grid.setAutoHeight(true);
		grid.setHideHeaders(true);
		grid.setStripeRows(true);
		grid.getView().setForceFit(true);
		grid.getView().setEmptyText("empty");

		grid.hide();

		{
			dndListener = new GridDNDListener<OneToManyModel>(grid);
		}

		cp.add(grid);

		af = new AdapterField(cp);
		af.setResizeWidget(true);

		/*
		 * if ( readonly ) { af.setReadOnly( true ); }
		 */

		add(af);
		setFireChangeEventOnSetValue(true);
	}

	protected List<ColumnConfig> setupExtraColumns() {
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		{
			ColumnConfig indexColumn = new ColumnConfig();
			indexColumn.setId("idx");
			indexColumn.setHeader("No.");
			indexColumn.setSortable(false);
			indexColumn.setMenuDisabled(true);
			indexColumn.setWidth(25);
			indexColumn.setStyle("vertical-align: middle;");
			indexColumn.setAlignment(HorizontalAlignment.RIGHT);
			indexColumn.setRenderer(Renderers.ROW_INDEX);
			config.add(indexColumn);
		}

		if (extraColumns == null || extraColumns.size() == 0) {
			ColumnConfig column = new ColumnConfig();
			column.setId("label");
			column.setHeader("label");
			column.setSortable(false);
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
				extraColumn.setHeader(renderer.toString());
				extraColumn.setSortable(false);
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
			config.add(selection.getColumn());
		}
		return config;
	}

	public Set<String> getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(Set<String> aTypes) {
		allowedTypes = aTypes;
	}

	@SuppressWarnings("unchecked")
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
				super.setValue(store.getModels());
				fireEvent(Events.Change, new FieldEvent(OneToManyRelationField.this));

			} else {
				super.setValue(values);
				fireEvent(Events.Change, new FieldEvent(OneToManyRelationField.this));
			}
		} else {
			final OneToManyRelationField tmp = this;
			addListener(Events.Render, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					tmp.setValue(values);
					tmp.originalValue = tmp.getValue();
					tmp.removeListener(Events.Render, this);
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

	private void addRelationshipsToNode(String targetNodeKey,
			final String targetAttributeKey,
			final List<OneToManyModel> relationships) {

		if (targetNodeKey == null || targetNodeKey.trim().length() == 0
				|| relationships == null || relationships.size() == 0
				|| targetAttributeKey == null
				|| targetAttributeKey.trim().length() == 0) {
			return;
		}

		GwtContentNode node = WorkingSet.get(targetNodeKey);
		if ( node != null ) {
			addRelationshipsToNodeHelper( node, targetAttributeKey, relationships );
		} else {
			CmsGwt.getContentService().loadNodeData( targetNodeKey,	new AsyncCallback<GwtNodeData>() {
		
				public void onFailure( Throwable caught ) {
					MessageBox.alert( "Error", "Cannot add relations to node because node loading failed.", null );
				}

				public void onSuccess( GwtNodeData nodeData ) {
					addRelationshipsToNodeHelper( nodeData.getNode(), targetAttributeKey, relationships );
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	private void addRelationshipsToNodeHelper(GwtContentNode node,
			String targetAttributeKey, List<OneToManyModel> relationships) {

		List<OneToManyModel> oldValue = (List<OneToManyModel>) node
				.getAttributeValue(targetAttributeKey);
		List<OneToManyModel> newValue;
		if (oldValue != null) {
			newValue = new ArrayList<OneToManyModel>(oldValue);
		} else {
			newValue = new ArrayList<OneToManyModel>();
		}

		for (OneToManyModel otmModel : relationships) {
			if (!newValue.contains(otmModel)) {
				newValue.add(otmModel);
			}
		}
		node.changeValue(targetAttributeKey, (Serializable) newValue);
		WorkingSet.add(node);
	}

	private void removeRelationships(final List<OneToManyModel> relationships) {

		if (relationships == null || relationships.size() == 0) {
			return;
		}

		for (OneToManyModel model : relationships) {
			store.remove(model);
		}

		if (store.getCount() == 0) {
			grid.hide();
		} else {
			grid.getView().refresh(false);
		}
		fireEvent(Events.Change, new FieldEvent(OneToManyRelationField.this));

	}

	void generateUniqueIdForType( final String type ) {
		final ContentServiceAsync contentService = (ContentServiceAsync)GWT.create( ContentService.class );
		contentService.generateUniqueId( type, new BaseCallback<String>() {

			@Override
			public void onSuccess( String result ) {
				final ContentIdWindow w = new ContentIdWindow( result, "ID of '" + type + "'" );
				w.addListener( Events.Select, new Listener<BaseEvent>() {

					@Override
					public void handleEvent( BaseEvent be ) {
						String id = w.getContentId();
						contentService.createNodeData( type, id, new NewGwtNodeCallback( OneToManyRelationField.this ) );
					}
				} );
				w.show();
			}
		} );
	}

	public void addOneToManyModel(String type, String key, String label,
			GwtNodeData newNodeData) {
		if (store.findModel("key", key) == null) {
			OneToManyModel model = createModel(type, key, label);
			model.setNewNodeData(newNodeData);
			store.add(model);
			grid.show();
			grid.getView().refresh(false);
			fireEvent(Events.Change, new FieldEvent(this));
		}
	}

	public void addOneToManyModels(List<? extends ContentNodeModel> list) {
		for (ContentNodeModel cmModel : list) {
			if (store.findModel("key", cmModel.getKey()) == null) {
				OneToManyModel otmModel = createModel(cmModel.getType(),
						cmModel.getKey(), cmModel.getLabel());
				store.add(otmModel);
			}
		}
		grid.show();
		grid.getView().refresh(false);
		fireEvent(Events.Change, new FieldEvent(this));
	}

	protected OneToManyModel createModel(String type, String key, String label) {
		return new OneToManyModel(type, key, label, store.getCount());
	}

	/**
	 * refresh the UI, this call is needed when new columns added or deleted
	 * from the extra column list.
	 */
	public void refreshColumns() {
		grid.reconfigure(store, new ColumnModel(setupExtraColumns()));
	}

	@Override
	public void disable() {
		super.disable();

		// theToolBar.setVisible(false);
		theToolBar.setEnabled(false);

		/**
		 * if ( addButton != null ) addButton.disable(); if ( createButton !=
		 * null ) createButton.disable(); if ( copyButton != null )
		 * copyButton.disable(); if ( moveButton != null ) moveButton.disable();
		 * if ( deleteButton != null ) deleteButton.disable(); if ( sortButton
		 * != null ) sortButton.disable(); if ( selectCheckbox != null )
		 * selectCheckbox.disable();
		 **/
	}

	@Override
	public void enable() {
		super.enable();

		theToolBar.setEnabled(true);
		// theToolBar.setVisible(true);

		if (addButton != null)
			addButton.enable();
		if (createButton != null)
			createButton.enable();
		if (copyButton != null)
			copyButton.enable();
		if (moveButton != null)
			moveButton.enable();
		if (deleteButton != null)
			deleteButton.enable();
		if (sortButton != null)
			sortButton.enable();
		if (selectCheckbox != null)
			selectCheckbox.enable();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		if (addButton != null) {
			addButton.setEnabled(!readOnly);

			if (readOnly) {
				addButton.removeListener(Events.OnClick, addButtonListener);
			} else {
				addButton.addListener(Events.OnClick, addButtonListener);
			}
		}

		if (createButton != null) {
			createButton.setEnabled(!readOnly);

			if ( readOnly ) {
				createButton.removeListener( Events.OnClick, createButtonListener );
			} else {
				createButton.addListener( Events.OnClick, createButtonListener );
			}
		}

		if (copyButton != null) {
			copyButton.setEnabled(!readOnly);

			if (readOnly) {
				copyButton.removeListener(Events.OnClick, copyButtonListener);
			} else {
				copyButton.addListener(Events.OnClick, copyButtonListener);
			}
		}

		if (moveButton != null) {
			moveButton.setEnabled(!readOnly);

			if (readOnly) {
				moveButton.removeListener(Events.OnClick, moveButtonListener);
			} else {
				moveButton.addListener(Events.OnClick, moveButtonListener);
			}
		}

		if (deleteButton != null) {
			deleteButton.setEnabled(!readOnly);

			if (readOnly) {
				deleteButton.removeListener(Events.OnClick,
						deleteButtonListener);
			} else {
				deleteButton.addListener(Events.OnClick, deleteButtonListener);
			}
		}

		if (sortButton != null) {
			sortButton.setEnabled(!readOnly);

			if (readOnly) {
				sortButton.removeListener(Events.OnClick, sortButtonListener);
			} else {
				sortButton.addListener(Events.OnClick, sortButtonListener);
			}
		}

		if (selectCheckbox != null) {
			selectCheckbox.setEnabled(!readOnly);
			if (readOnly) {
				selectCheckbox.removeListener(Events.OnClick, selectListener);
				selectCheckbox
						.removeListener(Events.OnKeyPress, selectListener);
			} else {
				selectCheckbox.addListener(Events.OnClick, selectListener);
				selectCheckbox.addListener(Events.OnKeyPress, selectListener);
			}
		}

		/** It is requested not to dim the grid itself - leave as is */
		// FIXME: grids not disabled allow to realign items which means change
		// grid.setEnabled(!readOnly);

		if (readOnly) {
			dndListener.forget();
		} else {
			dndListener.observe();
		}
	}

	@Override
	public void setWidth(int width) {
		ColumnModel cm = grid.getColumnModel();
		int columnWidth = width / (cm.getColumnCount() - 2);
		int idx;
		for (idx = 1; idx < cm.getColumnCount() - 2; idx++) {
			cm.getColumn(idx).setWidth(columnWidth);
		}
		af.setWidth(width);
		grid.setWidth(width);

		super.setWidth(width);
	}
	
	@Override
	public void reset() {
		setValue(originalValue);
	}

}
