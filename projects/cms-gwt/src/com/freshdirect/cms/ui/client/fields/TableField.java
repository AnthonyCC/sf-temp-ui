package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute.ColumnType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TableField extends MultiField implements ChangeTrackingField {

    static class SerializableArrayComparator implements Comparator<Serializable[]>  {
        int idx;
        boolean revert = false;
        
        public SerializableArrayComparator(int idx, boolean revert) {
            this.idx = idx;
            this.revert = revert;
        }

        @Override
        public final int compare(Serializable[] o1, Serializable[] o2) {
            int result = compareOne(o1[idx], o2[idx]);
            if (revert) {
                return -result;
            } else {
                return result;
            }
        }
        
        public int compareOne(Serializable o1, Serializable o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    }


    static class ContentNodeModelComparator extends SerializableArrayComparator  {

        public ContentNodeModelComparator(int idx, boolean revert) {
            super(idx, revert);
        }

        @Override
        public int compareOne(Serializable o1, Serializable o2) {
            ContentNodeModel m1 = (ContentNodeModel) o1;
            ContentNodeModel m2 = (ContentNodeModel) o2;
            return m1.getId().compareTo(m2.getId());
        }
    }

    private static final class TableRowLoader implements DataProxy<PagingLoadResult<? extends ModelData>> {
        TableAttribute attribute;
        String sortKey = null;
        List<Serializable[]> rows;
        
        public TableRowLoader(TableAttribute attribute) {
            this.attribute = attribute;
        }

        List<Serializable[]> getRows(PagingLoadConfig config) {
            String newSortOrder = config.getSortDir().name() +'-'+config.getSortField();

            if (sortKey!=null && sortKey.equals(newSortOrder)) {
                return rows;
            }
            rows = this.attribute.getRows();
            if (config.getSortField() != null && config.getSortDir() != SortDir.NONE) {
                int idx = Integer.parseInt(config.getSortField().substring("col_".length()));
                Comparator<Serializable[]> comparator = getComparator(rows, idx, config.getSortDir() == SortDir.DESC);
                if (comparator != null) {
                    Collections.sort(rows, comparator);
                }
            }
            sortKey = newSortOrder;
            return rows;
        }
        
        private Comparator<Serializable[]> getComparator(List<Serializable[]> datas, int idx, boolean reverse) {
            for (int i = 0; i < datas.size(); i++) {
                Serializable[] serializables = datas.get(i);
                Serializable value = serializables[idx];
                if (value != null) {
                    if (value instanceof Integer) {
                        return new SerializableArrayComparator(idx, reverse);
                    }
                    if (value instanceof String) {
                        return new SerializableArrayComparator(idx, reverse);
                    }
                    if (value instanceof ContentNodeModel) {
                        return new ContentNodeModelComparator(idx, reverse);
                    }
                }
            }
            return null;
        }

        @Override
        public void load(DataReader<PagingLoadResult<? extends ModelData>> reader, Object loadConfig,
                AsyncCallback<PagingLoadResult<? extends ModelData>> callback) {
        	
            final PagingLoadConfig config = (PagingLoadConfig) loadConfig;
            List<BaseModelData> result = new ArrayList<BaseModelData>(config.getLimit());
            
            List<Serializable[]> lines = getRows(config);
            ColumnType[] types = attribute.getTypes();
            for (int i = 0; i < config.getLimit() && config.getOffset() + i < lines.size(); i++) {
                Serializable[] serializables = lines.get(config.getOffset() + i);
                BaseModelData bmd = new BaseModelData();
                for (int j = 0; j < serializables.length; j++) {
                    bmd.set("col_" + j, serializables[j]);
                    if (types[j].equals(TableAttribute.ColumnType.CLASS)) {
                        bmd.set("class", serializables[j]);
                    }
                }
                result.add(bmd);
            }
            BasePagingLoadResult<BaseModelData> bplr = new BasePagingLoadResult<BaseModelData>(result, config.getOffset(), lines.size());
            callback.onSuccess(bplr);
        }
    }
    
    
    private static final class RowStyler extends GridViewConfig {
        @Override
        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
            String rowClass = model.get("class");
            return rowClass != null ? "x-selectable cms-table-field-" + rowClass + (rowIndex % 2 == 0 ? "-even" : "-odd") : "";
        }
    }   
    
    TableAttribute attribute;

    protected Grid<BaseModelData> grid;
    protected boolean renderAsLinks; 
    
    private final String nodeKey;
    private final String attributeKey;

    public TableField( TableAttribute attribute, String nodeKeyp, String attributeKeyp ) {
        this.attribute = attribute;
        this.nodeKey = nodeKeyp;
        this.attributeKey = attributeKeyp;
        renderAsLinks = true;

        BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(new TableRowLoader(attribute));
        loader.setRemoteSort(true);
        
        ColumnType[] types = attribute.getTypes();

        int groupingColumn = -1;
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ContentNodeAttributeI[] columnAttributes = attribute.getColumns();
        for (int i = 0; i < columnAttributes.length; i++) {
            ContentNodeAttributeI col = columnAttributes[i];
            if (ColumnType.GROUPING == types[i]) {
                groupingColumn = i;
            }
            ColumnConfig cc = new ColumnConfig("col_" + i, col.getLabel(), 150);            
            
            if (ColumnType.KEY == types[i]) {
                cc.setRenderer(new GridCellRenderer<BaseModelData>() {

					@Override
					public Object render(BaseModelData model, String property,
							ColumnData config, int rowIndex, int colIndex,
							ListStore<BaseModelData> store,
							Grid<BaseModelData> grid) {
						Object rmodel = model.get(property);
			            if (rmodel instanceof ContentNodeModel) {
			            	if (renderAsLinks) {
			            		return ((ContentNodeModel) rmodel).renderLinkComponent();
			            	}
			            	return ((ContentNodeModel) rmodel).getKey();
			            }
			            return rmodel != null ? rmodel.toString() : "<i>null</i>";			            
					}
					
				});
            } else if (ColumnType.CLASS == types[i]) {
                cc.setHidden(true);
                cc.setWidth(5);
            }
            
            columns.add(cc);
        }

        final PagingToolBar toolBar = new PagingToolBar(20);
        toolBar.bind(loader);

        
        ContentPanel cp = new ContentPanel();
        cp.setBottomComponent(toolBar);
        cp.setAutoHeight(true);
        cp.setWidth(670);
        cp.setHeaderVisible(false);
        
        Button showIdButton = new Button("Show IDs", new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				renderAsLinks = !renderAsLinks;
				grid.getView().refresh(false);
				if (renderAsLinks) {
					ce.getButton().setText("Show IDs");
				}
				else {
					ce.getButton().setText("Show links");
				}
			}
		});        
        
        Button exportCsvButton = new Button("Export to CSV");
        exportCsvButton.setToolTip( "Exports the contents of this tablefield to a CSV file." );
        exportCsvButton.addSelectionListener( new SelectionListener<ButtonEvent>() {
        	public void componentSelected(ButtonEvent ce) {
        		String url = GWT.getModuleBaseURL() + "CsvExport?nodeKey=" + nodeKey + "&attributeKey=" + attributeKey;
        		open( url );        		        		
        	}
            native void open( String url ) /*-{
				window.open( url );
          	}-*/;
		});

        
        ToolBar tb = new ToolBar();
        tb.add( showIdButton );
        tb.add( exportCsvButton );        
        cp.add(tb);
        
        
        ListStore<BaseModelData> store;
        GroupingView view = null;

        if (groupingColumn != -1) {
            view = new GroupingView();
            view.setShowGroupedColumn(false);
            view.setForceFit(true);

            GroupingStore<BaseModelData> groupStore = new GroupingStore<BaseModelData>(loader);
            groupStore.groupBy("col_" + groupingColumn);

            store = groupStore;

        } else {
            store = new ListStore<BaseModelData>(loader);
        }

        grid = new Grid<BaseModelData>(store, new ColumnModel(columns));
        if (view != null) {
            grid.setView(view);
        }
        grid.getView().setViewConfig(new RowStyler());
        grid.getView().setForceFit(false);


        grid.setStripeRows(true);
        grid.setWidth(660);
        grid.setHeight(600);
        grid.addStyleName("table-field");

        loader.load(0, 20);

        cp.add(grid);

        add(new AdapterField(cp));
    }
    
    @Override
    public Object getValue() {
        // return the original value, because no editing is possible with this field.
        return attribute.getValue();
    }

    @Override
    public boolean isFieldValueChanged() {
        return false;
    }
    
    @Override
    public Serializable getChangedValue() {
        return null;
    }

}
