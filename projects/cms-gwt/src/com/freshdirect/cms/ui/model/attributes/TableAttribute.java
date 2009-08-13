/**
 * 
 */
package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsombor
 * 
 */
public class TableAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {
    public enum ColumnType { 
        NORMAL, ATTRIB, KEY
    }

    ContentNodeAttributeI[] columns;

    List<Serializable[]> values = new ArrayList<Serializable[]>();

    ColumnType[] types;
    
    /**
     * 
     */
    public TableAttribute() {
    }

    public void setTypes(ColumnType[] types) {
        this.types = types;
    }
    
    public ColumnType[] getTypes() {
        return types;
    }
    
    public void setColumns(ContentNodeAttributeI[] columns) {
        this.columns = columns;
    }
    
    public ContentNodeAttributeI[] getColumns() {
        return columns;
    }

    public void addRow(Serializable[] row) {
        values.add(row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI#getType()
     */
    @Override
    public String getType() {
        return "table";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI#getValue()
     */
    @Override
    public Serializable getValue() {
        return (Serializable) values;
    }

    
    public List<Serializable[]> getRows() {
        return values;
    }
    
}
