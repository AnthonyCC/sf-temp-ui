package com.freshdirect.cms.ui.editor.formdef.data;

import java.text.MessageFormat;
import java.util.List;

/**
 * Top level item of form definition. This object associates tabs / pages for the given content type.
 *
 * @author segabor
 *
 */
public class Editor {

    /**
     * Object ID, for tracking editor references
     */
    public final String id;

    /**
     * CMS Content type in String form
     */
    public final String contentType;

    /**
     * List of pages / tabs for the particular editor
     */
    public final List<Page> pages;

    public Editor(String id, String contentType, List<Page> pages) {
        this.id = id;
        this.contentType = contentType;
        this.pages = pages;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[Editor id=\"{0}\"; contentType=\"{1}\"; pages={2}]", id, contentType, pages);
    }
}
