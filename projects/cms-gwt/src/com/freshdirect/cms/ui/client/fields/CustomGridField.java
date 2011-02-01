package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.google.gwt.user.client.ui.Widget;

public class CustomGridField extends OneToManyRelationField implements SaveListenerField {

    public static class TextFieldRenderer implements GridCellRenderer<OneToManyModel> {
        String columnName;
        
        TextFieldRenderer (String modelProperty) {
            this.columnName = modelProperty;
        }
        
        @Override
        public Object render(final OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<OneToManyModel> store,
                Grid<OneToManyModel> grid) {
            final TextField<String> txt = new TextField<String>();
            txt.setValue((String)model.get(columnName));
            txt.addListener(Events.Blur, new Listener<BaseEvent>() {
               @Override
                public void handleEvent(BaseEvent be) {
                    model.set(columnName, txt.getValue());
                } 
            });
            return txt;
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
    
    public CustomGridField(String attributeKey, Set<String> allowedTypes, boolean navigable, CustomFieldDefinition customFields, boolean readonly, String parentType ) {
        super(attributeKey, allowedTypes, navigable, createExtraColumns(customFields), readonly, parentType);
        this.customFields = customFields;
        grid.setHideHeaders( false );
    }
    
    static List<GridCellRenderer<OneToManyModel>> createExtraColumns(CustomFieldDefinition customFields) {
        List<GridCellRenderer<OneToManyModel>> extraColumns = new ArrayList<GridCellRenderer<OneToManyModel>>();
        if (customFields.getGridColumns()!=null) {
            for (String colName : customFields.getGridColumns()) {
                extraColumns.add(new CustomGridField.TextFieldRenderer(colName));
            }
        }
        return extraColumns;
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
    
    @Override
    public void addOneToManyModel( String type, String key, String label, GwtNodeData newNodeData ) {
        if ( store.findModel( "key", key ) == null ) {        
	        OneToManyModel model = createModel(type, key, label);
	        model.setNewNodeData(newNodeData);
	        
			if ( customFields.getGridColumns() != null ) {
				for ( String col : customFields.getGridColumns() ) {
					model.set( col, newNodeData.getNode().getAttributeValue( col ) );
				}
			}	        
	        
	        store.add(model);
	        grid.show();
	        grid.getView().refresh(false);
	        fireEvent(Events.Change, new FieldEvent(this));       
        }
    }
    
    
    /**
     * @return
     */
    @Override
    protected boolean isAddRelationToolNeeded() {
        //return !( this instanceof CustomGridField );
        return false;
    }

    /**
     * @return
     */
    @Override
    protected boolean isSelectAllToolNeeded() {
        //return !( this instanceof VariationMatrixField || this instanceof CustomGridField );
        return false;
    }
    
}
