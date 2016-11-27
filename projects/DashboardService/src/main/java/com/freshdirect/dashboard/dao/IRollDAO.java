package com.freshdirect.dashboard.dao;

import java.util.List;

import com.freshdirect.dashboard.model.RollData;

public interface IRollDAO {

	public List<RollData> getRollByZone(final String deliveryDate, final String zone);

}