/**
 * @author ekracoff
 * Created on Apr 22, 2005*/

package com.freshdirect.ocf.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.ocf.core.Campaign;


public class CampaignDao extends HibernateDaoSupport {
	
	/**
	 *  The hibernate dao factory used to create this DAO object.
	 */
	private HibernateDaoFactory daoFactory;
		
	/**
	 *  Constructor.
	 * 
	 *  @param daoFactory the hibernate dao factory used to create this dao object.
	 */
	public CampaignDao(HibernateDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
		
	/**
	 *  Return a hibernate session factory to use for accessing persistent storage.
	 * 
	 *  @return a hibernate session factory
	 *  @see ResourceUtil#getResource(String)
	 */
	protected SessionFactory getSessionFactory()
	{
		return daoFactory.getSessionFactory();
	}

	/**
	 * Return a HibernateDaoFactory object that is specific to the package.
	 * The main reason for this object is to provide a thread-local, package-specific
	 * hibernate session and transaction.
	 */
	protected HibernateDaoFactory getDaoFactory() {
		return daoFactory;
	}	

	public List getAllCampaigns() {
		return query("from Campaign");
	}
	
	public Campaign getCampaign(String campId) {
		return (Campaign) load(Campaign.class, campId);
	}
	
	public void deleteCampaign(String campId) {
		delete(campId);
	}
	
	public void saveCampaign(Campaign camp) {
		saveOrUpdate(camp);
	}
	
	
	

}
