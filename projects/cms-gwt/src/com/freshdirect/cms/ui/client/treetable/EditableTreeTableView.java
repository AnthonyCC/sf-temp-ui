package com.freshdirect.cms.ui.client.treetable;

import com.extjs.gxt.ui.client.widget.treetable.TreeTableItem;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableView;
import com.google.gwt.user.client.Element;

public class EditableTreeTableView extends TreeTableView {

	public EditableTreeTableView() {
		super();
	}
		
	protected void renderItemValue(EditableTreeItem item, int index, Object value) {
		super.renderItemValue((TreeTableItem) item, index, value);
	}
	
	protected int getCellIndex(Element elem) {
		return super.getCellIndex(elem);
	}
	
	protected Element getCell(EditableTreeItem item, int cell) {
		return item.el().select("td.my-treetbl-cell").getItem(cell);
	}
	
	protected Element getTextCellElement(EditableTreeItem item, int cell) {
		return super.getTextCellElement(item, cell);
	}
	
	protected void applyCellStyles(EditableTreeItem item) {
		super.applyCellStyles(item);
	}	
	
	protected void setCellStyle(EditableTreeItem item, int index, String style) {
	    super.setCellStyle(item, index, style);
	}
}
