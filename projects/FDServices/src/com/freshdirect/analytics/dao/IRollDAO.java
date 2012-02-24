package com.freshdirect.analytics.dao;

import java.util.List;

import com.freshdirect.analytics.model.RollData;

public interface IRollDAO {

	public List<RollData> getData(final String deliveryDate, final String zone);

	public List<RollData> getDataByZone(final String deliveryDate);

}