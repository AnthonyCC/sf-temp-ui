package com.freshdirect.cms.ui.model.bulkload;

import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.UNKNOWN;

import java.io.Serializable;

public class GwtBulkLoadPreviewStatus implements Serializable {
	private static final long serialVersionUID = -2328656708716271864L;

	private BulkLoadPreviewState state;
	private String message;
	private Integer index;
	transient private Object node;
	transient private String attribute;

	public GwtBulkLoadPreviewStatus() {
		state = UNKNOWN;
		message = "";
	}

	public GwtBulkLoadPreviewStatus(Integer index) {
		this();
		this.index = index;
	}

	public GwtBulkLoadPreviewStatus(GwtBulkLoadPreviewStatus status) {
		this.state = status.state;
		this.message = status.message;
		this.node = status.node;
		this.index = status.index;
	}

	public BulkLoadPreviewState getState() {
		return state;
	}

	public void setState(BulkLoadPreviewState state) {
		this.state = state;
	}

	public String getMessage() {
		return (index != null ? "#" + index + ": " : "") + message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getNode() {
		return node;
	}

	public void setNode(Object node) {
		this.node = node;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public void removeIndex() {
		this.index = null;
	}

	public void setStateWithMessage(BulkLoadPreviewState state, String message) {
		setState(state);
		setMessage(message);
	}

	public void copyFrom(GwtBulkLoadPreviewStatus status) {
		node = status.node;
		setState(status.getState());
		setMessage(status.getMessage());
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{state=");
		buf.append(getState().name());
		buf.append(", message=");
		buf.append(getMessage());
		buf.append("}");
		return buf.toString();
	}
}