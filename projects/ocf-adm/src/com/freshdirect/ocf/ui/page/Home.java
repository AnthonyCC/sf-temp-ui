/**
 * @author ekracoff
 * Created on Jun 8, 2005*/

package com.freshdirect.ocf.ui.page;

import java.util.List;

import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.impl.hibernate.CampaignDao;


public class Home extends AppPage{
	
	public List getCampaigns(){
		CampaignDao dao = OcfDaoFactory.getInstance().getCampaignDao();
		return dao.getAllCampaigns();
	}

}
