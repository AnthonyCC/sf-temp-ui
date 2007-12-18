/**
 * @author ekracoff
 * Created on Mar 16, 2005*/

package com.freshdirect.cms.ui.tapestry.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.framework.conf.FDRegistry;


public abstract class ProcessPublish extends BasePage implements IExternalPage {
	private PublishServiceI service = (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		String pubId = (String) params[0];
		Publish publish = service.getPublish(pubId);
		setPublish(publish);
		
		if (EnumPublishStatus.COMPLETE.equals(publish.getStatus())){
			cycle.activate("ViewPublishes");
		} else if(EnumPublishStatus.FAILED.equals(publish.getStatus())){
			setIsFailed(true);
		}
	}
	
	public abstract void setPublish(Publish publish);
	
	public abstract Publish getPublish();
	
	public abstract void setIsFailed(boolean isFailed);
	
	

}
