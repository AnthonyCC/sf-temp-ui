/**
 * @author ekracoff
 * Created on Feb 4, 2005*/

package com.freshdirect.cms.ui.tapestry.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.framework.conf.FDRegistry;

public abstract class PublishHistory extends BasePage implements IExternalPage {

	private ChangeLogServiceI service = (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
	
	private PublishServiceI   pubService = (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);

	private Publish		      publish = null;
	
	private List				  changes = null;

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		setPublishId(params[0].toString());
	}
	
	public abstract void setPublishId(String publishId);

	public abstract String getPublishId();

	public List getChanges() {
		if (changes == null) {
			Publish  publish;
			Publish  prevPublish;
			List     sets;
			
			publish = getPublish();
			prevPublish = pubService.getPreviousPublish(publish);
			
			sets = service.getChangesBetween(prevPublish.getTimestamp(), publish.getTimestamp());
	
			changes = new ArrayList();
			for (Iterator i = sets.iterator(); i.hasNext();) {
				ChangeSet cn = (ChangeSet) i.next();
				changes.addAll(cn.getNodeChanges());
			}
		}
		
		return changes;
	}

	public Publish getPublish() {
		if (publish == null) {
			publish = pubService.getPublish(getPublishId());
		}
		
		return publish;
	}

	/**
	 *  Prepare to get detached from page serving, and get the object back
	 *  to the object pool. Free up local / cached variables here.
	 */
	public void detach() {
		publish = null;
		changes = null;
		
		super.detach();
	}
	
}