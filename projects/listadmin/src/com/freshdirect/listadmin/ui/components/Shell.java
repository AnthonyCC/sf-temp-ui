package com.freshdirect.listadmin.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.RequestContext;
import org.hibernate.Session;

import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.db.Template;
import com.freshdirect.listadmin.metadata.MetaDataUtils;

public class Shell extends BaseComponent {
	private String userType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	protected void prepareForRender(IRequestCycle cycle) {
		super.prepareForRender(cycle);
		
		// the build script doesn't know anything about the javax.servlet.package,
		// so we can't use cookies.  Oh well.
		// userType = cycle.getRequestContext().getCookieValue("userType");
		userType = "analyst";
	}
	
	public Set getAvailableClasses() throws Exception {
		return MetaDataUtils.getAvailableClasses();
	}
	
	public List getAvailableTemplates() {
		List ret = new ArrayList();
		
		try {
			ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
			Session 			sess = dao.currentSession();
			ret = sess.createCriteria(Template.class).list();
			dao.closeSession();
		} catch (Exception r) {
			r.printStackTrace();
		}
		return ret;
	}
	
	public List getAvailableStoredQueries() {
		List ret = new ArrayList();
		
		try {
			ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
			Session 			sess = dao.currentSession();
			ret = sess.createCriteria(StoredQuery.class).list();
			dao.closeSession();
		} catch (Exception r) {
			r.printStackTrace();
		}
		return ret;
	}
	
	public void startNewQuery(IRequestCycle cycle) {
		RequestContext rc = cycle.getRequestContext();
		IPage page        = cycle.getPage("NewQuery");
		
		page.setProperty("tables",rc.getParameters("classes"));
		
		cycle.activate("NewQuery");
	}
}
