package com.freshdirect.cms.ui.model.bulkload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * We need to implement BaseModelData due to GXT data model requirements for Grid components. Yuck! :)
 * 
 * @author csongor
 */
public class GwtBulkLoadRow extends BaseModelData implements Serializable {
	private static final long serialVersionUID = 5583074716035217390L;

	private List<GwtBulkLoadCell> cells;
	transient private GwtBulkLoadPreviewStatus rowStatus;

	public GwtBulkLoadRow() {
		this(new ArrayList<GwtBulkLoadCell>(), new GwtBulkLoadPreviewStatus());
	}

	public GwtBulkLoadRow(List<GwtBulkLoadCell> cells, GwtBulkLoadPreviewStatus rowStatus) {
		super();
		this.cells = cells;
		this.rowStatus = rowStatus;
	}

	public List<GwtBulkLoadCell> getCells() {
		return cells;
	}

	public GwtBulkLoadPreviewStatus getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(GwtBulkLoadPreviewStatus rowStatus) {
		this.rowStatus = rowStatus;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String classSimpleName = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		buf.append(classSimpleName);
		buf.append(cells.toString());
		return buf.toString();
	}
}
