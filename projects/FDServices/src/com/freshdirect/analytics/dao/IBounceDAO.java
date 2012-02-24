package com.freshdirect.analytics.dao;

import java.util.List;

import com.freshdirect.analytics.model.BounceData;

public interface IBounceDAO {

	public List<BounceData> getData(final String deliveryDate, final String zone);

	public List<BounceData> getDataByZone(final String deliveryDate);


}