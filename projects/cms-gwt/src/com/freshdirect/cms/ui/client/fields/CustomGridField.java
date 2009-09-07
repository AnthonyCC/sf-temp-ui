package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
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
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.google.gwt.user.client.ui.Widget;

public class CustomGridField extends OneToManyRelationField implements SaveListenerField {

    public static class TextFieldRenderer implements GridCellRenderer<OneToManyModel> {
        String modelProperty;
        
        TextFieldRenderer (String modelProperty) {
            this.modelProperty = modelProperty;
        }
        
        @Override
        public Object render(final OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<OneToManyModel> store,
                Grid<OneToManyModel> grid) {
            final TextField<String> txt = new TextField<String>();
            txt.setValue((String)model.get(modelProperty));
            txt.addListener(Events.Blur, new Listener<BaseEvent>() {
               @Override
                public void handleEvent(BaseEvent be) {
                    model.set(modelProperty, txt.getValue());
                } 
            });
            return txt;
        }
    }

    
    CustomFieldDefinition customFields;
    
    public CustomGridField(String attributeKey, Set<String> allowedTypes, boolean navigable, CustomFieldDefinition customFields, boolean readonly ) {
        super(attributeKey, allowedTypes, navigable, createExtraColumns(customFields), readonly);
        this.customFields = customFields;
    }
    
    static List<GridCellRenderer<OneToManyModel>> createExtraColumns(CustomFieldDefinition customFields) {
        List<GridCellRenderer<OneToManyModel>> extraColumns = new ArrayList<GridCellRenderer<OneToManyModel>>();
        if (customFields.getGridColumns()!=null) {
            for (String col : customFields.getGridColumns()) {
                extraColumns.add(new CustomGridField.TextFieldRenderer(col));
            }
        }
        return extraColumns;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onSave() {
		for ( int rowIndex = 0; rowIndex < this.store.getCount(); rowIndex++ ) {
			OneToManyModel model = this.store.getAt( rowIndex );

			GwtContentNode newNode = new GwtContentNode( model.getKey() );
			
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
