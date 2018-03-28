package com.freshdirect.cms.ui.client.fields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentManager;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.OneToManyModel;

public class PrimaryHomeSelectorField extends OneToManyRelationField {
	private String storeKey;
	
	// Store Key -> {set parent category keys}
	private Map<String,Set<String>> mapping;

	private GwtNodeContext contexts;

	private static final Set<String> ALLOWED_TYPES = new HashSet<String>();
	static {
		ALLOWED_TYPES.add("Category");
	}
	
	public PrimaryHomeSelectorField(GwtNodeContext contexts, Map<String,Set<String>> mapping, String storeKey, GwtNodePermission permission) {
		super("PRIMARY_HOME", ALLOWED_TYPES, false, permission, "Product");
		
		this.contexts = contexts;
		this.storeKey = storeKey;
		this.mapping = mapping;
	}

	@Override
	protected void initDNDListener() {
		// DO NOTHING
	}
	
	@Override
	protected void setupTopToolbar(ToolBar aToolBar) {
	    // LEAVE THIS METHOD EMPTY, DO NOT INVOKE SUPER!
	}

	@Override
	protected List<ColumnConfig> setupExtraColumns() {
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		{
			ColumnConfig column = new ColumnConfig();
			column.setId("label");
			column.setHeader("label");
			column.setSortable(false);
			column.setMenuDisabled(true);
			column.setWidth(MAIN_LABEL_WIDTH);
			// column.setRenderer(Renderers.GRID_LINK_RENDERER);
			column.setRenderer(new Renderers.LabelRenderer("label", "label"));
			config.add(column);
		}
		
		// [change] button
		{
			ColumnConfig cfg = new ColumnConfig();
			cfg.setId("change");
			cfg.setSortable(false);
			cfg.setFixed(true);
			cfg.setMenuDisabled(true);
			cfg.setWidth(80);
			cfg.setStyle("vertical-align: middle;");
			cfg.setAlignment(HorizontalAlignment.RIGHT);
            cfg.setRenderer(new GridCellRenderer<OneToManyModel>() {

                @Override
                public Object render(final OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, final ListStore<OneToManyModel> store,
                        Grid<OneToManyModel> grid) {

                    Button b = new Button("change", new SelectionListener<ButtonEvent>() {

                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            ContentTreePopUp popup = setupPopup(model);

                            popup.show();
                            popup.setSize(401, 601);
                            popup.setSize(650, 600);
                        }
                    });

                    b.setId("chg-btn" + model.getKey());

                    // adjust visual state
                    boolean flag = Boolean.valueOf((String) model.get("_enabled"));
                    b.setEnabled(flag);

                    return b;
                }
            });
	
			config.add(cfg);
		}

		return config;
	}



	/**
	 * Prepare a popup for Primary Home selection
	 */
	private ContentTreePopUp setupPopup(OneToManyModel model) {
		final ContentTreePopUp popup = ContentTreePopUp.getPrimaryHomePopup(getAllowedTypes());

		final NodeTree tree =  popup.getTreepanel();
		
		final TreeStore<TreeContentNodeModel> _treeStore = tree.getMainStore(); 

		_treeStore.removeAll();

		final String selKey = model.get("_storeKey");

		// setup tree popup with 'parent' categories just for the selected store

		for (String p : contexts.getPaths()) {
			if (p.contains(selKey)) {
				String[] _pathFragments = p.split("\\|");
				TreeContentNodeModel categoryNode = new TreeContentNodeModel("Category", contexts.getLabel(p),
						_pathFragments[_pathFragments.length - 2]);

                String cosContextOverride = contexts.getCosContext(p);
                if (!GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE.equals(cosContextOverride)) {
                    categoryNode.setIconOverride(contexts.getCosContext(p));
                }

				categoryNode.setHasChildren(false);
				_treeStore.insert(categoryNode, 0, false);
			}
		}
		
		tree.hideToolbar();
		popup.setHeading(getFieldLabel());
		popup.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
            public void handleEvent(BaseEvent be) {
				addOneToManyModels(popup.getSelectedItems());
			}
		});
		return popup;
	}

	/**
	 * Event hook invoked when context toolbar is changed
	 * 
	 * @param storeKey
	 */
	public void changeStoreKey(String storeKey) {
		this.storeKey = storeKey;

		adjustValues();
	}	


	/**
	 * Adjust list values according to changed store key
	 */
	protected void adjustValues() {
		if (this.storeKey == null) {
			// disable all ...
			for (OneToManyModel m : store.getModels()) {
				Component c = ComponentManager.get().get("chg-btn" + m.getKey());
				if (c instanceof Button) {
					((Button)c).setEnabled(false);
				}
			}
			return;
		}
		
		List<OneToManyModel> parentz = store.getModels();
		for (OneToManyModel m : parentz) {
			final String _pKey = m.getKey();
			for (String sKey : mapping.keySet()) {
				if (mapping.get(sKey).contains(_pKey)) {
					final boolean flag = sKey.equals(storeKey) && permission.getAllowedStores().contains( storeKey );
					m.set("_storeKey", sKey);
					m.set("_storeId", sKey.split(":")[1]);
					
					
                    for (String p : contexts.getPaths()) {
                        // Check if path ends with top category key
                        if (p.contains("|" + _pKey + "|")) {
                            m.set("label", contexts.getLabel(p));
                            break;
                        }
                    }
					
					m.set("_enabled", flag ? "true" : "false");

					// adjust visual state
					Component c = ComponentManager.get().get("chg-btn" + m.getKey());
					if (c instanceof Button) {
						((Button)c).setEnabled(flag);
					}
					break;
				}
			}
		}		
	}
	
	@Override
	protected boolean addSingleValueToStore(OneToManyModel value) {
		if (value == null) {
			return false;
		}
		
		final String sKey = findStoreKey(value.getKey());
		if (sKey == null) {
			CmsGwt.debug("Ooops, no store key found for selected category " + value.getKey());
			return false;
		}
		
		// cat key -> store key
		List<OneToManyModel> newVals = new ArrayList<OneToManyModel>();
		for (OneToManyModel m : store.getModels()) {
			final String key = m.getKey();
			String ssKey = findStoreKey(key);
			
			if (!sKey.equalsIgnoreCase(ssKey)) {
				newVals.add(m);
			} else {
				CmsGwt.debug("Removing parent cat " + key);
			}
		}
		newVals.add(value);
		
		setValue(newVals);
		
		return true;
	}

	@Override
	public void setValue(List<OneToManyModel> values) {
		super.setValue(values);
		
		adjustValues();
	}

	private String findStoreKey(String catKey) {
		for (String storeKey : mapping.keySet()) {
			for (String aCatKey : mapping.get(storeKey)) {
				if (aCatKey.equalsIgnoreCase(catKey)) {
					return storeKey;
				}
			}
		}
		return null;
	}
}
