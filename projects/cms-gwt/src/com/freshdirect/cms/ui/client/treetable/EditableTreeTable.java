package com.freshdirect.cms.ui.client.treetable;

import com.extjs.gxt.ui.client.widget.treetable.TreeTable;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumnModel;

public class EditableTreeTable extends TreeTable {

	public EditableTreeTable() {
		super();
		super.setView(new EditableTreeTableView());
	}
	
	public EditableTreeTable(TreeTableColumnModel cm) {
		super(cm);
		super.setView(new EditableTreeTableView());
	}	
	
	protected EditableTreeTableView getEditableView() {
		return (EditableTreeTableView) view;
	}
}
