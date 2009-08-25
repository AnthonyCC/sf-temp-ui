package com.freshdirect.cms.ui.client.treetable;

import com.extjs.gxt.ui.client.widget.Editor;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumn;

public class EditableTreeTableColumn extends TreeTableColumn {

	private boolean editable;
	private Editor editor;
	
	public EditableTreeTableColumn(String id, float width) {
		super(id, width);
		editable = false;
	}
	
	public EditableTreeTableColumn(String string, String string2, int i) {
		super(string, string2, i);
		editable = false;
	}

	public void setEditable(boolean isEditable) {
		editable = isEditable;
	}
	
	public void setEditor(Editor e) {
		editor = e;
		editor.addStyleName("x-editor x-small-editor x-grid-editor");
	}
	
	public boolean getEditable() {
		return editable;
	}
	
	public Editor getEditor() {
		return editor;
	}

}
