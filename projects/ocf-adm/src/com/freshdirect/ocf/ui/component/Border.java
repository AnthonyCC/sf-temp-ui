package com.freshdirect.ocf.ui.component;

import java.util.List;

import org.apache.tapestry.BaseComponent;

import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.impl.hibernate.CampaignDao;

public class Border extends BaseComponent {
	
	public List getCampaigns() {
		CampaignDao dao = OcfDaoFactory.getInstance().getCampaignDao();
		return dao.getAllCampaigns();
	}
	
}
