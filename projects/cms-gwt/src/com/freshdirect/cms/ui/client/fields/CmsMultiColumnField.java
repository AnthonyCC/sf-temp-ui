package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.google.gwt.user.client.ui.Widget;

public class CmsMultiColumnField extends OneToManyRelationField implements SaveListenerField {

    public static class ColumnRenderer implements GridCellRenderer<OneToManyModel> {
        String columnName;
        
        ColumnRenderer(String columnName) {
            this.columnName = columnName;
        }
        
        @Override
        public Object render(final OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<OneToManyModel> store,
                Grid<OneToManyModel> grid) {
        	return model.get(columnName);
        }
        
        public String getColumnName() {
        	return columnName;
        }
        
        @Override
        public String toString() {
        	return getColumnName();
        }
    }
    
    CustomFieldDefinition customFields;
    
    public CmsMultiColumnField(String attributeKey, Set<String> allowedTypes, boolean navigable, CustomFieldDefinition customFields, GwtNodePermission permission, String parentType ) {
        super(attributeKey, allowedTypes, navigable, createExtraColumns(customFields), permission, parentType);
        this.customFields = customFields;
        grid.setHideHeaders( false );
    }
    
    static List<GridCellRenderer<OneToManyModel>> createExtraColumns(CustomFieldDefinition customFields) {
        List<GridCellRenderer<OneToManyModel>> extraColumns = new ArrayList<GridCellRenderer<OneToManyModel>>();
        if (customFields.getGridColumns()!=null) {
            for (String column : customFields.getGridColumns()) {
            	extraColumns.add(new CmsMultiColumnField.ColumnRenderer(column));
            }
        }
        extraColumns.add(Renderers.GRID_ICON_RENDERER);
        return extraColumns;
    }
    
    @Override
    protected boolean isAddRelationToolNeeded() {
        return true;
    }

    @Override
    protected boolean isSelectAllToolNeeded() {
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onSave() {
		for ( int rowIndex = 0; rowIndex < this.store.getCount(); rowIndex++ ) {
			OneToManyModel model = this.store.getAt( rowIndex );

			GwtContentNode newNode = WorkingSet.get( model.getKey() );			
			if ( newNode == null ) {
				newNode = new GwtContentNode( model.getKey() );
			}
			
			for ( int colId = 0; colId < customFields.getGridColumns().size(); colId++ ) {
				int colIndex = grid.getColumnModel().getIndexById( "extra_" + colId );
				
				Widget widget = grid.getView().getWidget( rowIndex, colIndex );
				if ( widget instanceof Field ) {
					Field<Serializable> field = (Field<Serializable>)widget;
					Serializable value = (Serializable)field.getValue();
					String fieldName = customFields.getGridColumns().get( colId );
					newNode.changeValue( fieldName, value );
				}
			}
			WorkingSet.add( newNode );
		}
	}    
}