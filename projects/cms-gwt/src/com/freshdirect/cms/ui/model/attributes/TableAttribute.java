package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsombor
 * 
 */
public class TableAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {
	private static final long serialVersionUID = 8700958168223826246L;

	public enum ColumnType {
        NORMAL, ATTRIB, KEY, CLASS, GROUPING
    }

    ContentNodeAttributeI[] columns;

    List<Serializable[]> values = new ArrayList<Serializable[]>();

    ColumnType[] types;

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

    @Override
    public String getType() {
        return "table";
    }

    @Override
    public Serializable getValue() {
        return (Serializable) values;
    }

    public List<Serializable[]> getRows() {
        return values;
    }

}
