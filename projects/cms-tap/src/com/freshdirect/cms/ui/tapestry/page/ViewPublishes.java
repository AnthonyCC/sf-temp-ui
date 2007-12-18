/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.ui.tapestry.page;

import java.util.List;

import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.framework.conf.FDRegistry;

public class ViewPublishes extends BasePage {

	private PublishServiceI service = (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);
	
	public List getPublishHistory() {
		return service.getPublishHistory();
	}
	
}