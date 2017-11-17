package com.freshdirect.webapp.globalnav;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.GlobalNavigationModel;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.data.CMSModelToSoyDataConverter;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GlobalNavTag extends SimpleTagSupport {

    private static Logger LOGGER = LoggerFactory.getInstance(GlobalNavTag.class.getSimpleName());

    private String name = "globalnav";

    @Override
    public void doTag() throws JspException, IOException {

        PageContext ctx = (PageContext) getJspContext();
        FDSessionUser user = (FDSessionUser) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);
        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user)) {
            GlobalNavigationModel globalNavigationModel = null;
            try {
                // Current global nav decision logic... Might be changed in the future.
                globalNavigationModel = GlobalNavContextUtil.getGlobalNavigationModel(user);
                ctx.setAttribute(name, DataPotatoField.digGlobalNav(CMSModelToSoyDataConverter.createGlobalNavData(globalNavigationModel, user)));
            } catch (Exception e) {
            	e.printStackTrace();
                if (globalNavigationModel != null) {
                    LOGGER.error("Failed to load global navigation model " + globalNavigationModel.toString(), e);
                } else {
                    LOGGER.error("Missing global navigation model ", e);
                }

                throw new JspException(e);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
