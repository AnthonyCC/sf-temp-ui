package com.freshdirect.ocf.ui.page;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.Session;

import com.freshdirect.listadmin.db.Template;
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.impl.hibernate.ActionDao;


public class SelectTemplate extends AppPage implements IExternalPage {
	private Template template;
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {

	}
	
	public IPropertySelectionModel getTemplateSelectionModel() {
		Map actions = new HashMap();
		ActionDao dao = OcfDaoFactory.getInstance().getActionDao();
		Session sess = dao.currentSession();
		List list    = sess.createCriteria(Template.class).list();
		
		for(Iterator it=list.iterator();it.hasNext();) {
			Template templ = (Template) it.next();
			actions.put(templ.getName(),templ);
		}
		
		return new LabelPropertySelectionModel(actions, true);
	}
	
	public void chooseTemplate(IRequestCycle cycle) {
		IPage page = cycle.getPage("FillInTemplate");
		page.setProperty("template",template);
		
		cycle.activate("FillInTemplate");
	}
}
