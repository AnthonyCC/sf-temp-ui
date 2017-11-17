package com.freshdirect.cms.ui.editor.formdef.data;

import java.text.MessageFormat;
import java.util.List;

/**
 * Mid-level object defining field {@link Section}s
 * in a {@link Page}
 *
 * @author segabor
 *
 */
public class Page {
    /**
     * Page ID, for keeping track of parsed objects
     */
    public final String id;

    /**
     * Page / Tab title
     */
    public final String title;

    /**
     * List of embedded sections
     */
    public final List<Section> sections;

    public Page(String id, String title, List<Section> sections) {
        this.id = id;
        this.title = title;
        this.sections = sections;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[Page: id=\"{0}\"; title=\"{1}\"; sections={2}]", id, title, sections);
    }
}
