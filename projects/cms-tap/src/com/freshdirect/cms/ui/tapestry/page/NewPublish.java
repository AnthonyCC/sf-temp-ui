/**
 * @author ekracoff
 * Created on Feb 9, 2005*/

package com.freshdirect.cms.ui.tapestry.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.ConcurrentPublishException;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.framework.conf.FDRegistry;

public abstract class NewPublish extends BasePage {
	private ChangeLogServiceI service = (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
	private PublishServiceI pubService = (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);

	public void submit(IRequestCycle cycle) {
		CmsVisit visit = (CmsVisit) cycle.getPage().getVisit();
		Date date = new Date();
		
		Publish publish = getPublish();
		publish.setTimestamp(date);
		publish.setUserId(visit.getUser().getName());
		publish.setStatus(EnumPublishStatus.PROGRESS);
		publish.setLastModified(date);
		
		try {
			String pubId = pubService.doPublish(publish);
			
			IEngineService service = cycle.getEngine().getService(Tapestry.EXTERNAL_SERVICE);
			ExternalServiceParameter params = new ExternalServiceParameter("ProcessPublish", new Object[] { pubId });
			throw new RedirectException(service.getLink(false, params).getURL());
			
		} catch (ConcurrentPublishException e) {
			setErrorMsg("Publish already in progress, please wait until it completes to re-publish");
		}
	}

	public abstract Publish getPublish();

	public abstract void setPublish(Publish publish);
	
	public abstract String getErrorMsg();

	public abstract void setErrorMsg(String message);
	
	public List getChanges() {
		Publish lastPublish = pubService.getMostRecentPublish();
		if (lastPublish == null) {
			return Collections.EMPTY_LIST;
		}
		List sets = service.getChangesBetween(lastPublish.getTimestamp(), new Date());

		List changes = new ArrayList();
		for (Iterator i = sets.iterator(); i.hasNext();) {
			ChangeSet cn = (ChangeSet) i.next();
			changes.addAll(cn.getNodeChanges());
		}
		return changes;
	}
}