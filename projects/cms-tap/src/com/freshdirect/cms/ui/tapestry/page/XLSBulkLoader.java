package com.freshdirect.cms.ui.tapestry.page;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.cms.fdstore.ProductBulkLoader;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.framework.util.StringUtil;

import java.util.LinkedList;


public abstract class XLSBulkLoader extends BasePage {
	
	public abstract IUploadFile getFile();
	
	public abstract void setSuccessList(List successList);
	
	public IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}
	
	
	public void processFile(IRequestCycle cycle) {
		IUploadFile file = getFile();
		
		if (file == null || file.getFileName().length() <= 0 || getDelegate().getHasErrors()) {
			getDelegate().setFormComponent(null);
			getDelegate().record("no file uploaded", null);
			return;
		}
				
		String	fileName = StringUtil.parseFilename(file.getFileName(),true);
		
		if (file.getFileName().length() <= 0 || getDelegate().getHasErrors()) {
			getDelegate().setFormComponent(null);
			getDelegate().record("no file uploaded", null);
			return;
		} else if (!fileName.endsWith(".xls")) {
			getDelegate().setFormComponent(null);
			getDelegate().record("Files with .xls (Excel) extension expected", null);
			return; 
		}
		
		List successes = new LinkedList();
		Map failures = new TreeMap();
		try {
			ProductBulkLoader.XLSBulkLoad(file.getStream(), ((CmsVisit)getVisit()).getUser().getName(), successes, failures);
		} catch (Exception e) {
			getDelegate().setFormComponent(null);
			getDelegate().record("Exception occured: " + e.toString(),null);
			e.printStackTrace();
		} finally {
			setSuccessList(successes);
			
			if (failures.size() > 0) {
			getDelegate().setFormComponent(null);
				for(Iterator i = failures.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry ei = (Map.Entry)i.next();
					getDelegate().record("Failed to insert " + ei.getKey() + " because " + ei.getValue() + '!', null);
				}
			}
		}
	}

}
