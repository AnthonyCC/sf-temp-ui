package com.freshdirect.cms;

import com.freshdirect.HibernateDbTestCaseSupport;
import com.freshdirect.cms.core.CmsDaoFactory;
import com.freshdirect.cms.core.CmsDaoFactoryTestHelper;
import com.freshdirect.cms.publish.PublishDao;

public abstract class CmsDaoTestCaseSupport extends HibernateDbTestCaseSupport {

	private PublishDao		publishDao;
	
	public CmsDaoTestCaseSupport(String name) {
		super(name);
	}

	protected PublishDao getPublishDao() {
		return publishDao;
	}
	
	public void setUp() throws Exception {
		super.setUp();

		CmsDaoFactoryTestHelper.setSessionFactory(getSessionFactory());
		publishDao = CmsDaoFactory.getInstance().getPublishDao();
				
		publishDao.beginTransaction();
	}
	
	public void tearDown() throws Exception {
		publishDao.commitTransaction();
		publishDao.closeSession();
		
		super.tearDown();
	}
		
}
