package com.freshdirect.listadmin.ui.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.hibernate.Session;

import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.Template;
import com.freshdirect.listadmin.db.VirtualObject;
import com.freshdirect.listadmin.metadata.MetaDataUtils;

public class NewObject extends AppPage implements IExternalPage {
	private boolean createTemplate;
	private VirtualObject virtualObject;
	
	public void activateExternalPage(Object[] args, IRequestCycle cycle) {
		if(args != null && args.length != 0) {
			setObjectName((String) args[0]);
		}
	}
	
	public String getObjectName() {return getVirtualObject().getName();}
	public void setObjectName(String name) {
		if(name != null) {
			virtualObject = VirtualObject.getByName(name);
		}
	}
	

	public void saveObject(IRequestCycle cycle) {
		// We should check that the syntax is valid
		// (which can be done by seeing if Template.getParams throws
		// a parseException) and notify the user.
		Template t       = null;
			
		
		ListadminDao dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session      sess = dao.currentSession();
		dao.beginTransaction();
		sess.save(virtualObject);
		
		// As a convinience we allow the analyst to create
		// a query from the object (with no additional parameters)
		// automatically
		if(createTemplate) {
			t = new Template();
			t.setName(virtualObject.getName());
			sess.save(t);
			t.getObjects().add(virtualObject);
			sess.save(t);
		}
		
		dao.commitTransaction();
		
		MetaDataUtils.setup();
		
		if(createTemplate) {
			IPage page = cycle.getPage("ConfigureQuery");
			page.setProperty("templateId",t.getTemplateId());
			
			cycle.activate("ConfigureQuery");
		} else {
			IPage page = cycle.getPage("NewQuery");
			page.setProperty("tables",new String[] {virtualObject.getName()});
			cycle.activate("NewQuery");
		}
	}

	public boolean isCreateTemplate() {
		return createTemplate;
	}

	public void setCreateTemplate(boolean createTemplate) {
		this.createTemplate = createTemplate;
	}

	public VirtualObject getVirtualObject() {
		if(virtualObject == null) {
			virtualObject = new VirtualObject();
		}
		return virtualObject;
	}

	public void setVirtualObject(VirtualObject virtualObject) {
		this.virtualObject = virtualObject;
	}
}
