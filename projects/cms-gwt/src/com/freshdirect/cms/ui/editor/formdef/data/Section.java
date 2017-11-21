package com.freshdirect.cms.ui.editor.formdef.data;

import java.text.MessageFormat;
import java.util.List;

/**
 * A section groups attribute fields together
 *
 * @author segabor
 *
 */
public class Section {
    public final String id; // can be null !!

    public final String title;

    public final List<Field> fields;

    public Section(String id, String title, List<Field> fields) {
        this.id = id;
        this.title = title;
        this.fields = fields;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[Section id=\"{0}\"; title=\"{1}\"; fields={2}]", id, title, fields);
    }
}
