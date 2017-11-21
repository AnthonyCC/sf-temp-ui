package com.freshdirect.cms.ui.editor.formdef.data;

import java.text.MessageFormat;
import java.util.List;

public class Field {
    public final FieldType fieldType;

    public final String id;

    /**
     * CMS attribute name
     */
    public final String attribute;

    /**
     * Name of Custom field editor component
     * Mandatory for {@link FieldType#CmsCustomField}
     */
    public final String componentName;

    /**
     * List of attribute names
     * Used for {@link FieldType#CmsMultiColumnField} and {@link FieldType#CmsGridField}
     */
    public final List<String> columns;

    public Field(String id, FieldType fieldType, String attribute, String componentName, List<String> columns) {
        this.id = id;
        this.fieldType = fieldType;
        this.attribute = attribute;
        this.componentName = componentName;
        this.columns = columns;
    }

    @Override
    public String toString() {
        switch (fieldType) {
            case CmsField:
            default:
                return MessageFormat.format("[CmsField attribute=\"{0}\"]", attribute);
            case CmsGridField:
                return MessageFormat.format("[CmsGridField attribute=\"{0}\"; columns={1}]", attribute, columns);
            case CmsCustomField:
                return MessageFormat.format("[CmsCustomField attribute=\"{0}\"; component=\"{1}\"]", attribute, componentName);
            case CmsMultiColumnField:
                return MessageFormat.format("[CmsMultiColumnField attribute=\"{0}\"; columns={1}]", attribute, columns);
        }
    }
}
