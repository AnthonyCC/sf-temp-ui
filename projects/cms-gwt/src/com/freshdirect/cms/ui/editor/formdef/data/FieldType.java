package com.freshdirect.cms.ui.editor.formdef.data;

/**
 * Enumeration denoting attribute field type in CMS Node Editor
 *
 * @author segabor
 *
 */
public enum FieldType {
    /**
     * Simple attribute field
     */
    CmsField,

    /**
     * Grid arranged field
     */
    CmsGridField,

    /**
     * Field having custom editor component
     */
    CmsCustomField,

    /**
     * Multi-column field
     */
    CmsMultiColumnField
}
