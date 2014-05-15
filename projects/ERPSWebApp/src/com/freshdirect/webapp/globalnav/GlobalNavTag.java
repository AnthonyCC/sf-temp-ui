package com.freshdirect.webapp.globalnav;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
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
		if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.globalnav2014, user)) {
			String globalNavId = "";
	
			//Current global nav decision logic... Might be changed in the future.
			if (user == null || user.getPricingContext() == null || user.getPricingContext().getUserContext() == null || !user.getPricingContext().getUserContext().isAlcoholRestricted()) {
				globalNavId = "GlobalNavWithWine"; //with wine
			} else {
				globalNavId = "GlobalNavWithoutWine"; //w/o wine
			}
			
			GlobalNavigationModel globalNavigationModel = (GlobalNavigationModel)ContentFactory.getInstance().getContentNode("GlobalNavigation", globalNavId);
			
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
