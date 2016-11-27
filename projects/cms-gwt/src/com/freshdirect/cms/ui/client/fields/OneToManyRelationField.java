package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
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
import com.freshdirect.cms.ui.client.NewKeySet;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtNodePermission.EvalResult;
import com.freshdirect.cms.ui.model.NavigableRelationInfo;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OneToManyRelationField extends MultiField<List<OneToManyModel>> implements HasCustomDefaultValue<List<OneToManyModel>> {

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

    private ToolButton moveUpButton;
    private ToolButton moveDownButton;

    private CheckBox selectCheckbox;

    private CheckBoxSelectionModel<OneToManyModel> selection = new CheckBoxSelectionModel<OneToManyModel>();

    protected String attributeKey;
    protected String parentType;

    protected GwtNodePermission permission;

    /**
     * empty constructor for the descendants
     */
    protected OneToManyRelationField() {
        super();
    }

    public OneToManyRelationField(String attrKey, Set<String> allowedTypes, boolean navigable, GwtNodePermission permission, String parentType) {
        this(attrKey, allowedTypes, navigable, new ArrayList<GridCellRenderer<OneToManyModel>>(0), permission, parentType);
    }

    public OneToManyRelationField(String attrKey, Set<String> allowedTypes, boolean navigable, List<GridCellRenderer<OneToManyModel>> extraColumns, GwtNodePermission permission,
            String parentType) {
        super();
        this.attributeKey = attrKey;
        this.allowedTypes = allowedTypes;
        this.navigable = navigable;
        this.extraColumns = extraColumns;
        this.parentType = parentType;

        this.permission = permission;

        initialize();

        setReadOnly(permission.isReadonly());
    }

    protected Listener<BaseEvent> createButtonListener = new Listener<BaseEvent>() {

        @Override
        public void handleEvent(BaseEvent be) {
            Set<String> contentTypes = OneToManyRelationField.this.permission.getTypesAllowedForCreate(OneToManyRelationField.this.allowedTypes);

            if (contentTypes.isEmpty()) {
                // ALERT
                MessageBox.alert("Operation Denied", "You're not allowed to create content node", null);
                return;
            } else if (contentTypes.size() == 1) {
                generateUniqueIdForType(contentTypes.iterator().next());
                return;
            }
            final ContentTypeSelectorPopup typeSelector = new ContentTypeSelectorPopup(contentTypes);
            typeSelector.addListener(Events.Select, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    String type = typeSelector.getSelectedType();
                    generateUniqueIdForType(type);
                }
            });
            typeSelector.show();
        }
    };

    protected Listener<BaseEvent> addButtonListener = new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent be) {
            final ContentTreePopUp popup = ContentTreePopUp.getInstance(getAllowedTypes(), true);
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

            final StoreSorter<OneToManyModel> caseInsensitiveLabelSorter = new StoreSorter<OneToManyModel>() {

                @Override
                public int compare(Store<OneToManyModel> store, OneToManyModel m1, OneToManyModel m2, String property) {
                    if ("label".equals(property)) {
                        String s1 = m1.get(property, "");
                        String s2 = m2.get(property, "");

                        return s1.compareToIgnoreCase(s2);
                    }

                    return super.compare(store, m1, m2, property);
                }
            };

            // apply custom sort
            store.setStoreSorter(caseInsensitiveLabelSorter);
            store.sort("label", SortDir.ASC);
            grid.getView().refresh(false);

            // now remove sorter allowing manual item realign
            store.setStoreSorter(null);
        }
    };

    protected Listener<BaseEvent> deleteButtonListener = new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent be) {

            final List<OneToManyModel> selectedList = selection.getSelectedItems();

            //
            EvalResult result = permission.evaluate(isReadOnly());
            if (selectedList.size() > 0 && result == EvalResult.STRICT) {
                Collection<String> types = permission.getTypesAllowedForDelete(allowedTypes);

                // handle edge cases
                if (types.isEmpty()) {
                    MessageBox.alert("Operation Denied", "Delete operation is not allowed", null);
                    return;
                } else /* if (types.size() < allowedTypes.size()) */{
                    // start looking for inappropriate items
                    for (final ContentNodeModel m : selectedList) {
                        if (!types.contains(m.getType())) {
                            MessageBox.alert("Operation Denied", "You are not allowed to remove content node with type " + m.getType(), null);
                            return;
                        }
                    }
                }
            }

            String question;
            if (selectedList.size() == 0) {
                return;
            } else if (selectedList.size() == 1) {
                question = "Do you really want to delete '" + selectedList.get(0).getLabel() + "' ?";
            } else {
                question = "Do you really want to delete " + selectedList.size() + " items?";
            }
            MessageBox.confirm("Delete", question, new Listener<MessageBoxEvent>() {

                public void handleEvent(MessageBoxEvent we) {
                    if (we.getButtonClicked().getText().equals("Yes")) {
                        removeRelationships(selectedList);
                    }
                }
            });
        }
    };

    protected Listener<BaseEvent> copyButtonListener = new CopyMoveListener(true);
    protected Listener<BaseEvent> moveButtonListener = new CopyMoveListener(false);

    protected Listener<BaseEvent> selectListener = new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent event) {
            if (selectCheckbox != null) {
                if (selectCheckbox.getValue()) {
                    selection.selectAll();
                } else {
                    selection.deselectAll();
                }
            }
        }
    };

    private final class CopyMoveListener implements Listener<BaseEvent> {

        boolean copy;

        public CopyMoveListener(boolean copy) {
            this.copy = copy;
        }

        public void handleEvent(BaseEvent be) {
            final List<OneToManyModel> selectedList = selection.getSelectedItems();

            String permissionRestrictedContentType = null;

            if (selectedList.size() == 0) {
                return;
            }

            Collection<String> types = permission.getTypesAllowedForDelete(allowedTypes);

            for (OneToManyModel model : selectedList) {
                if (!types.contains(model.getType())) {
                    permissionRestrictedContentType = model.getType();
                    break;
                }
            }

            // just one type, otherwise it will be confusing
            String moveType = selectedList.get(0).getType();
            for (Iterator<OneToManyModel> iter = selectedList.iterator(); iter.hasNext();) {
                OneToManyModel current = iter.next();
                if (!moveType.equals(current.getType())) {
                    iter.remove();
                }
            }
            if (permissionRestrictedContentType != null && !copy) {
                MessageBox.alert("Operation Denied", "The selected list contains the following content type " + permissionRestrictedContentType, null);
            } else {
                CmsGwt.getNavigableRelations(moveType, new BaseCallback<NavigableRelationInfo>() {

                    public void onSuccess(final NavigableRelationInfo result) {

                        final ContentTreePopUp popup = ContentTreePopUp.getInstance(result.getAllTargetTypes(parentType), false);

                        if (copy) {
                            popup.setHeading("Copy " + selectedList.size() + " item(s) to :");
                        } else {
                            popup.setHeading("Move " + selectedList.size() + " item(s) to :");

                        }

                        popup.addListener(Events.Select, new Listener<BaseEvent>() {

                            public void handleEvent(BaseEvent be) {
                                ContentNodeModel targetNode = popup.getSelectedItem();
                                String attrName;
                                if (parentType.equals(targetNode.getType())) {
                                    attrName = attributeKey;
                                } else {
                                    attrName = result.getNavigableAttributeName(targetNode.getType());
                                }
                                if (addRelationshipsToNode(targetNode.getKey(), attrName, selectedList)) {
                                    if (!copy) {
                                        removeRelationships(selectedList);
                                    }
                                }
                            }
                        });
                        popup.show();

                    };
                });
            }
        }
    }

    protected Listener<BaseEvent> moveUpButtonListener = new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent be) {
            final List<OneToManyModel> selectedList = selection.getSelectedItems();
            if (selectedList.size() == 0) {
                return;
            } else if (selectedList.size() > 1) {
                String message = "Please select one item to move. You have selected " + selectedList.size() + "!";
                MessageBox.alert("Move Up", message, null);
            } else {
                OneToManyModel model = selectedList.get(0);
                int position = store.indexOf(model);
                if (position > 0) {
                    position--;
                    store.remove(model);
                    store.insert(model, position);
                    grid.getView().refresh(false);
                    selection.setSelection(selectedList);
                }
            }
        }
    };

    protected Listener<BaseEvent> moveDownButtonListener = new Listener<BaseEvent>() {

        public void handleEvent(BaseEvent be) {
            final List<OneToManyModel> selectedList = selection.getSelectedItems();
            if (selectedList.size() == 0) {
                return;
            } else if (selectedList.size() > 1) {
                String message = "Please select one item to move. You have selected " + selectedList.size() + "!";
                MessageBox.alert("Move Down", message, null);
            } else {
                OneToManyModel model = selectedList.get(0);
                int position = store.indexOf(model);

                if (position < store.getCount() - 1) {
                    position++;
                    store.remove(model);
                    store.insert(model, position);
                    grid.getView().refresh(false);
                    selection.setSelection(selectedList);
                }
            }
        }
    };

    /**
     * Drag and drop listener for the grid component.
     * 
     */
    protected class GridDNDListener<T extends ContentNodeModel> extends DNDListener {

        Grid<T> grid;

        FixedGridDropTarget dropTarget;

        protected Timer scrollUpTimer = new Timer() {

            @Override
            public void run() {
                boolean canScrollTop = grid.getBounds(false).y < 20;

                if (canScrollTop) {
                    MainLayout.getInstance().setVScrollPosition(MainLayout.getInstance().getVScrollPosition() - 10);
                } else {
                    cancel();
                }
            }
        };

        protected Timer scrollDownTimer = new Timer() {

            @Override
            public void run() {
                boolean canScrollBottom = grid.getBounds(false).height + grid.getBounds(false).y > MainLayout.getInstance().getBounds(false).height - 40;

                if (canScrollBottom) {
                    MainLayout.getInstance().setVScrollPosition(MainLayout.getInstance().getVScrollPosition() + 10);
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

    protected void setupTopToolbar(ToolBar aToolBar) {
        // ==================================== CREATE ====================================
        if (navigable && allowedTypes.size() > 0) {
            createButton = new ToolButton("create-relation");
            createButton.setToolTip(new ToolTipConfig("CREATE", "Create new node..."));

            theToolBar.add(createButton);
        }

        // ==================================== ADD ====================================
        if (isAddRelationToolNeeded()) {
            addButton = new ToolButton("add-relation");
            addButton.setToolTip(new ToolTipConfig("ADD", "Add a relation..."));
            theToolBar.add(addButton);
        }

        // ==================================== SORT ====================================
        sortButton = new ToolButton("sort-button");
        sortButton.setToolTip(new ToolTipConfig("SORT", "Sort the relations alphabetically."));
        theToolBar.add(sortButton);

        theToolBar.add(new FillToolItem());

        // ==================================== MOVE UP ====================================
        moveUpButton = new ToolButton("moveup-button");
        moveUpButton.setToolTip(new ToolTipConfig("Move Up", "Move Up"));
        theToolBar.add(moveUpButton);

        // ==================================== MOVE DOWN ====================================
        moveDownButton = new ToolButton("movedown-button");
        moveDownButton.setToolTip(new ToolTipConfig("Move Down", "Move Down"));
        theToolBar.add(moveDownButton);

        // ==================================== DELETE ====================================
        deleteButton = new ToolButton("delete-button");
        deleteButton.setToolTip(new ToolTipConfig("DELETE", "Delete selected relations."));
        theToolBar.add(deleteButton);

        // ==================================== COPY ====================================
        copyButton = new ToolButton("copy-button");
        copyButton.setToolTip(new ToolTipConfig("COPY", "Copy selected relations to another node."));
        theToolBar.add(copyButton);

        // ==================================== MOVE ====================================
        moveButton = new ToolButton("move-button");
        moveButton.setToolTip(new ToolTipConfig("MOVE", "Move selected relations to another node."));
        theToolBar.add(moveButton);

        // ==================================== SELECT CHECKBOX ====================================
        if (isSelectAllToolNeeded()) {
            selectCheckbox = new CheckBox();
            selectCheckbox.setToolTip(new ToolTipConfig("Select all/none", "Select all/none relations."));
            theToolBar.add(selectCheckbox);
        }
    }

    protected void initialize() {

        ContentPanel cp = new ContentPanel();
        cp.addStyleName("one-to-many");
        cp.setHeaderVisible(false);

        theToolBar = new ToolBar();
        cp.setTopComponent(theToolBar);

        // ---
        setupTopToolbar(theToolBar);

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

        initDNDListener();

        cp.add(grid);

        af = new AdapterField(cp);
        af.setResizeWidget(true);

        /*
         * if ( readonly ) { af.setReadOnly( true ); }
         */

        add(af);
        setFireChangeEventOnSetValue(true);
    }

    protected void initDNDListener() {
        dndListener = new GridDNDListener<OneToManyModel>(grid);
    }

    /**
     * @return
     */
    protected boolean isAddRelationToolNeeded() {
        // return !( this instanceof CustomGridField );
        return true;
    }

    /**
     * @return
     */
    protected boolean isSelectAllToolNeeded() {
        // return !( this instanceof VariationMatrixField || this instanceof CustomGridField );
        return true;
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

        config.add(selection.getColumn());

        return config;
    }

    public Set<String> getAllowedTypes() {
        return allowedTypes;
    }

    public void setAllowedTypes(Set<String> aTypes) {
        allowedTypes = aTypes;
    }

    /**
     * Subclasses may define custom logic
     * 
     * @param value
     * @return
     */
    protected boolean addSingleValueToStore(OneToManyModel value) {
        store.add(value);
        return true;
    }

    @Override
    public void setValue(final List<OneToManyModel> values) {
        if (store.getCount() > 0) {
            store.removeAll();
        }
        if (values != null) {
            store.add(values);
        }
        super.setValue(values);

        if (rendered) {
            if (store.getCount() == 0) {
                grid.hide();
            } else {
                grid.show();
            }

        } else {
            addListener(Events.Render, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    if (store.getCount() == 0) {
                        grid.hide();
                    } else {
                        grid.show();
                    }
                }
            });
        }
        fireEvent(Events.Change, new FieldEvent(OneToManyRelationField.this));
    }

    @Override
    public List<OneToManyModel> getValue() {
        return store.getModels();
    }

    @Override
    public List<OneToManyModel> getDefaultValue() {
        return new ArrayList<OneToManyModel>(0);
    }

    private boolean addRelationshipsToNode(String targetNodeKey, final String targetAttributeKey, final List<OneToManyModel> relationships) {

        if (targetNodeKey == null || targetNodeKey.trim().length() == 0 || relationships == null || relationships.size() == 0 || targetAttributeKey == null
                || targetAttributeKey.trim().length() == 0) {
            return false;
        }

        GwtContentNode node = WorkingSet.get(targetNodeKey);
        if (node != null) {
            addRelationshipsToNodeHelper(node, targetAttributeKey, relationships);
        } else {
            CmsGwt.getContentService().loadNodeData(targetNodeKey, new AsyncCallback<GwtNodeData>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Cannot add relations to node because node loading failed.", null);
                }

                public void onSuccess(GwtNodeData nodeData) {
                    addRelationshipsToNodeHelper(nodeData.getNode(), targetAttributeKey, relationships);
                }
            });
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void addRelationshipsToNodeHelper(GwtContentNode node, String targetAttributeKey, List<OneToManyModel> relationships) {

        List<OneToManyModel> oldValue = (List<OneToManyModel>) node.getAttributeValue(targetAttributeKey);
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

    void generateUniqueIdForType(final String type) {
        final ContentServiceAsync contentService = (ContentServiceAsync) GWT.create(ContentService.class);
        contentService.generateUniqueId(type, new BaseCallback<String>() {

            @Override
            public void onSuccess(String result) {
                final ContentIdWindow w = new ContentIdWindow(result, "ID of '" + type + "'");
                w.addListener(Events.Select, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {
                        String id = w.getContentId();

                        // check if node with same key is waiting to be saved
                        final String cKey = type + ":" + id;
                        if (NewKeySet.contains(cKey)) {
                            MessageBox.alert("Creating node failed", "Content ID '" + id + "' is just allocated for a new node.", null);
                            return;
                        }
                        if (WorkingSet.containsNodeWithKey(cKey)) {
                            MessageBox.alert("Creating node failed", "Content node with the given ID '" + id + "' is just created.", null);
                            return;
                        }

                        contentService.createNodeData(type, id, new NewGwtNodeCallback(OneToManyRelationField.this));
                    }
                });
                w.show();
            }
        });
    }

    public void addOneToManyModel(String type, String key, String label, GwtNodeData newNodeData) {
        if (store.findModel("key", key) == null) {
            OneToManyModel model = createModel(type, key, label);
            model.setNewNodeData(newNodeData);
            addSingleValueToStore(model);
            grid.show();
            grid.getView().refresh(false);
            fireEvent(Events.Change, new FieldEvent(this));
        }
    }

    public void addOneToManyModels(List<? extends ContentNodeModel> list) {
        for (ContentNodeModel cmModel : list) {
            if (store.findModel("key", cmModel.getKey()) == null) {
                OneToManyModel otmModel = createModel(cmModel.getType(), cmModel.getKey(), cmModel.getLabel());
                addSingleValueToStore(otmModel);
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
     * refresh the UI, this call is needed when new columns added or deleted from the extra column list.
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
         * if ( addButton != null ) addButton.disable(); if ( createButton != null ) createButton.disable(); if ( copyButton != null ) copyButton.disable(); if ( moveButton != null
         * ) moveButton.disable(); if ( deleteButton != null ) deleteButton.disable(); if ( sortButton != null ) sortButton.disable(); if ( selectCheckbox != null )
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

        final EvalResult result = permission.evaluate(readOnly);
        final boolean isEnabled = result != EvalResult.READONLY;

        if (addButton != null) {
            addButton.setEnabled(isEnabled);

            if (result == EvalResult.READONLY) {
                addButton.removeListener(Events.OnClick, addButtonListener);
            } else {
                addButton.addListener(Events.OnClick, addButtonListener);
            }
        }

        if (createButton != null) {
            Set<String> t = permission.getTypesAllowedForCreate(this.allowedTypes);
            createButton.setEnabled(isEnabled && (!t.isEmpty()));

            if (result == EvalResult.READONLY) {
                createButton.removeListener(Events.OnClick, createButtonListener);
            } else {
                createButton.addListener(Events.OnClick, createButtonListener);
            }
        }

        if (copyButton != null) {
            copyButton.setEnabled(isEnabled);

            if (result == EvalResult.READONLY) {
                copyButton.removeListener(Events.OnClick, copyButtonListener);
            } else {
                copyButton.addListener(Events.OnClick, copyButtonListener);
            }
        }

        if (moveButton != null) {
            moveButton.setEnabled(isEnabled);

            if (result == EvalResult.READONLY) {
                moveButton.removeListener(Events.OnClick, moveButtonListener);
            } else {
                moveButton.addListener(Events.OnClick, moveButtonListener);
            }
        }

        if (deleteButton != null) {
            deleteButton.setEnabled(isEnabled);

            if (result == EvalResult.READONLY) {
                deleteButton.removeListener(Events.OnClick, deleteButtonListener);
            } else {
                deleteButton.addListener(Events.OnClick, deleteButtonListener);
            }
        }

        if (sortButton != null) {
            sortButton.setEnabled(isEnabled);

            if (result == EvalResult.READONLY) {
                sortButton.removeListener(Events.OnClick, sortButtonListener);
            } else {
                sortButton.addListener(Events.OnClick, sortButtonListener);
            }
        }

        if (moveUpButton != null) {
            moveUpButton.setEnabled(isEnabled);
            if (result == EvalResult.READONLY) {
                moveUpButton.removeListener(Events.OnClick, moveUpButtonListener);
            } else {
                moveUpButton.addListener(Events.OnClick, moveUpButtonListener);
            }
        }

        if (moveDownButton != null) {
            moveDownButton.setEnabled(isEnabled);
            if (result == EvalResult.READONLY) {
                moveDownButton.removeListener(Events.OnClick, moveDownButtonListener);
            } else {
                moveDownButton.addListener(Events.OnClick, moveDownButtonListener);
            }
        }

        if (selectCheckbox != null) {
            selectCheckbox.setEnabled(isEnabled);
            if (result == EvalResult.READONLY) {
                selectCheckbox.removeListener(Events.OnClick, selectListener);
                selectCheckbox.removeListener(Events.OnKeyPress, selectListener);
            } else {
                selectCheckbox.addListener(Events.OnClick, selectListener);
                selectCheckbox.addListener(Events.OnKeyPress, selectListener);
            }
        }

        /** It is requested not to dim the grid itself - leave as is */
        // FIXME: grids not disabled allow to realign items which means change
        // grid.setEnabled(!readOnly);

        if (dndListener != null) {
            if (result == EvalResult.READONLY) {
                dndListener.forget();
            } else {
                dndListener.observe();
            }
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

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        originalValue = getValue();
    }
}
