package com.freshdirect.webapp.ajax.browse.data;

import java.util.List;

public class SortDropDownData extends SortOptionData {

	private static final long serialVersionUID = 1297951412695701774L;

	private List<SelectableData> options;

	public List<SelectableData> getOptions() {
		return options;
	}

	public void setOptions(List<SelectableData> options) {
		this.options = options;
	}

}
