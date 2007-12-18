package com.freshdirect.listadmin.ui.page;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.db.Template;

public class ConfigureQuery extends AppPage implements IExternalPage {
	private Template template       = null;
	private StoredQuery storedQuery = null;
	
	public void activateExternalPage(Object[] args, IRequestCycle cycle) {
		if(args != null && args.length != 0 && args[0] != null) {
			setTemplateId((String) args[0]);
		}
	}
	
	/* Only needed if returning XML, which we currently aren't
	public IMarkupWriter getResponseWriter(OutputStream out) {
		return new HTMLWriter("text/xml","UTF-8",out);
	}
	*/

	public IPropertySelectionModel getDatasourceSelectionModel() {
		Map actions = new HashMap();
		
		try {
			for(Iterator it=JndiWrapper.getDatasources().iterator();it.hasNext();) {
				String dsName = it.next().toString();
				actions.put(dsName,dsName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new LabelPropertySelectionModel(actions, true);
	}		
	
	public String getTemplateId() {return template.getTemplateId();}
	public void setTemplateId(String templateId) {
		template = Template.getById(templateId);
	}
	
	public StoredQuery getStoredQuery() {
		if(storedQuery == null) {
			storedQuery = new StoredQuery();
			storedQuery.setTemplate(template);
		}
		return storedQuery;
	}

	public void setStoredQuery(StoredQuery storedQuery) {
		this.storedQuery = storedQuery;
	}

	public void saveQuery(IRequestCycle cycle) throws ParseException {
		storedQuery.save();
		
		IPage page = cycle.getPage("RunQuery");
		page.setProperty("query",storedQuery);
		cycle.activate("RunQuery");
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
}

