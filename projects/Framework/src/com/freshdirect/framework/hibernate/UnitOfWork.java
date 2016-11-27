package com.freshdirect.framework.hibernate;

public abstract class UnitOfWork {

	public final void execute() {
		HibernateDaoSupport	dao = getDaoSupport();
		
		dao.beginTransaction();
		boolean ok = false;
		try {
			this.perform();
			dao.currentSession().flush();
			ok = true;
		} finally {
			if (ok) {
				dao.commitTransaction();
			}
		}
	}

	/**
	 * Template method
	 */
	protected abstract void perform();

	/**
	 *  Return a DAO support object, which facilitates connection to 
	 *  a persistent storage.
	 * 
	 *  @return a DAO support object.
	 */
	protected abstract HibernateDaoSupport getDaoSupport();
}