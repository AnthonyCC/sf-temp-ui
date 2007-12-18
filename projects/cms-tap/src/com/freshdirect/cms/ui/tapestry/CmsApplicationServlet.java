package com.freshdirect.cms.ui.tapestry;

import javax.servlet.ServletConfig;

import org.apache.hivemind.Registry;
import org.apache.tapestry.ApplicationServlet;

import com.freshdirect.framework.conf.FDRegistry;

public class CmsApplicationServlet extends ApplicationServlet {

	protected Registry constructRegistry(ServletConfig config) {
		FDRegistry.addConfiguration("classpath:/com/freshdirect/cms/ui/tapestry/hivemodule.xml");
		return FDRegistry.getInstance();
	}

}
