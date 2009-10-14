package com.freshdirect.cms.ui.client.treetable;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeTableEvent;
import com.extjs.gxt.ui.client.widget.Editor;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.treetable.TreeTable;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableItem;
import com.google.gwt.user.client.Event;

public class EditableTreeItem extends TreeTableItem {

	private Editor[] editors;

	public EditableTreeItem(Object[] values) {
		super(values);
		this.values = values;
		initFields();
	}

	public EditableTreeItem(String text, Object[] values) {
		super(text, values);
		this.values = values;
		initFields();
	}
	
	public void initFields() {
		int count = values.length;
		editors = new Editor[count];
		
		for (int i = 0; i < count; i++) {
			final int index = i;			
			editors[i] = new Editor(new TextField<String>());
			editors[i].addStyleName("x-editor x-small-editor x-grid-editor");
			editors[i].addListener(Events.Complete, new Listener<EditorEvent> () {
				public void handleEvent(EditorEvent be) {					
					HtmlContainer f = (HtmlContainer) values[index];
					String oldValue = f.getElement().getInnerHTML();
					f.getElement().setInnerHTML((String) be.getValue());
					if (!oldValue.equals(f.getElement().getInnerHTML())) {
						setCellStyle(index, " x-grid3-dirty-cell");
					}
				}
			});
		}		
	}

		
	public EditableTreeTable getEditableTreeTable() {
		return (EditableTreeTable) treeTable;
	}

	@Override
	public void onComponentEvent(ComponentEvent ce) {		

		if (ui != null && ce.getType().getEventCode() != Event.ONDBLCLICK) {
			getUI().handleEvent((TreeTableEvent) ce);
		}
		
		switch (ce.getType().getEventCode()) {
		case Event.ONCLICK:
			onClick(ce);
			break;
		case Event.ONDBLCLICK:			
			onDoubleClick(ce);
			break;
		case Event.ONMOUSEOVER:
			onMouseOver(ce);
			break;
		}	
		
	}

	@Override
	public void setCellStyle(int index, String style) {
		if (cellStyles == null)
			cellStyles = new String[values.length];
		cellStyles[index] = "my-treetbl-cell x-grid3-col x-grid3-cell " + style;
		if (isRendered()) {
			getEditableTreeTable().getEditableView().setCellStyle(this, index, style);
		}
	}

	@Override
	public void setValue(int index, Object value) {

		EditableTreeTableColumn column = (EditableTreeTableColumn) treeTable.getColumnModel().getColumn(index);
		
		if (column.getEditable()) {
			HtmlContainer field = new HtmlContainer(String.valueOf(value));			
			values[index] = field;
		}
		else {
			values[index] = String.valueOf(value);
		}
				
		if (rendered) {
			getEditableTreeTable().getEditableView().renderItemValue(this, index, values[index]);
		}		
	}

	@Override
	protected String[] getRenderedValues() {
		String[] svalues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String) {
				svalues[i] = (String) values[i];
			}
			else {
				HtmlContainer c = (HtmlContainer) values[i];
				c.setStyleName("x-grid3-cell-inner");
				svalues[i] = c.getElement().getInnerHTML();
			}
		}
		return svalues;
	}

	@Override
	protected void onDoubleClick(ComponentEvent ce) {
		TreeTableEvent event = (TreeTableEvent) ce;
		EditableTreeTableColumn column = (EditableTreeTableColumn) treeTable.getColumn(event.getCellIndex());
		if (column.getEditable()) {
			HtmlContainer c = (HtmlContainer) values[event.getCellIndex()];
			String v = c.getElement().getInnerHTML();
			editors[event.getCellIndex()].startEdit(c.getElement(), v);
		}			
		treeTable.fireEvent(Events.CellDoubleClick, ce);
		treeTable.fireEvent(Events.RowDoubleClick, ce);
	}
	
	@Override
	protected void init(TreeTable treeTable) {
		this.tree = treeTable;
		this.treeTable = treeTable;
	}


	@Override
	protected void setTree(Tree tree) {
		super.setTree(tree);
		treeTable = (EditableTreeTable) tree;
	}

	@Override
	public void afterRender() {
		super.afterRender();		
		for (int i = 0; i < values.length; i++) {
			getEditableTreeTable().getEditableView().renderItemValue(this, i, values[i]);
		}
		cellsRendered = true;		
	}
}
