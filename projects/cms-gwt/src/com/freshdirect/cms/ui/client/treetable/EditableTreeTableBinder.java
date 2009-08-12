package com.freshdirect.cms.ui.client.treetable;

import java.util.List;

import com.extjs.gxt.ui.client.binder.TreeTableBinder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;

public class EditableTreeTableBinder<M extends ModelData> extends
		TreeTableBinder<M> {

	public EditableTreeTableBinder(EditableTreeTable treeTable,
			TreeStore<M> store) {
		super(treeTable, store);
	}

	protected EditableTreeTable getEditableTreeTable() {
		return (EditableTreeTable) super.getTree();
	}
	
	protected EditableTreeItem createItem(M model) {
		int cols = treeTable.getColumnCount();
		EditableTreeItem item = new EditableTreeItem(new Object[cols]);
		item.init(getEditableTreeTable());
		setModel(item, model);

		String txt = getTextValue(model, displayProperty);
		if (txt == null && displayProperty != null) {
			txt = model.get(displayProperty);
		} else {
			txt = model.toString();
		}

		String icon = getIconValue(model, displayProperty);

		item.setIconStyle(icon);
		item.setText(txt);

		if (loader != null) {
			item.setLeaf(!loader.hasChildren(model));
		} else {
			item.setLeaf(!hasChildren(model));
		}

		updateItemValues(item);
		updateItemStyles(item);

		return item;
	}

	@Override
	protected void createAll() {
		TreeItem root = (TreeItem) treeTable.getRootItem();
		root.removeAll();

		List<M> list = store.getRootItems();

		for (M element : list) {
			EditableTreeItem item = null;
			if (store.isFiltered()) {
				if (isOrDecendantSelected(null, element)) {
					item = createItem(element);
				}
			} else {
				item = createItem(element);
			}
			treeTable.getRootItem().add(item);

			if (autoLoad && item != null) {
				item.setData("force", true);
				loadChildren(item, false);
			}
		}
	}

	private boolean isOrDecendantSelected(M parent, M model) {
		if (!isFiltered(parent, model)) {
			return true;
		}
		TreeItem item = (TreeItem) findItem(model);
		if (item != null) {
			for (TreeItem child : item.getItems()) {
				boolean result = isOrDecendantSelected(model, (M) child.getModel());
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateItemStyles(EditableTreeItem item) {
		M model = (M) item.getModel();
		int cols = treeTable.getColumnCount();
		for (int i = 0; i < cols; i++) {
			String id = getColumnId(i);
			String style = (styleProvider == null) ? null : styleProvider.getStringValue(model, id);
			item.setCellStyle(i, style == null ? "" : style);
		}
	}

	private void updateItemValues(EditableTreeItem item) {
		M model = (M) item.getModel();
		int cols = treeTable.getColumnCount();
		for (int j = 0; j < cols; j++) {
			String id = getColumnId(j);
			Object val = getTextValue(model, id);
			if (val == null)
				val = model.get(id);
			item.setValue(j, val);
		}
	}

}