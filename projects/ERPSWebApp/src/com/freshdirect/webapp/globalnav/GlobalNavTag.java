package com.freshdirect.webapp.globalnav;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.GlobalNavigationModel;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.data.CMSModelToSoyDataConverter;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GlobalNavTag extends SimpleTagSupport {

	private String name = "globalnav";
	
	@Override
	public void doTag() throws JspException, IOException {

		PageContext ctx = (PageContext) getJspContext();
		FDSessionUser user = (FDSessionUser) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);
		if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user)) {
	
			//Current global nav decision logic... Might be changed in the future.
			GlobalNavigationModel globalNavigationModel = GlobalNavContextUtil.getGlobalNavigationModel(user);
			
			try {
				ctx.setAttribute(name, DataPotatoField.digGlobalNav(CMSModelToSoyDataConverter.createGlobalNavData(globalNavigationModel, user)));
			} catch (FDResourceException e) {
				throw new JspException();
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}
}
