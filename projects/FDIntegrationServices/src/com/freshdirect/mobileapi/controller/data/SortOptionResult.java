package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.catalog.model.SortOptionInfo;
import com.freshdirect.mobileapi.util.SortType;

public class SortOptionResult extends Message{
	
	private SortOptionInfo sortOptionInfo;

	public SortOptionInfo getSortOptionInfo() {
		return sortOptionInfo;
	}

	public void setSortOptionInfo(SortOptionInfo sortOptionInfo) {
		this.sortOptionInfo = sortOptionInfo;
	}
	
	
}
