package com.freshdirect.webapp.ajax.modulehandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.modulehandling.data.IconData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.data.WelcomePageData;

public class ModuleHandlingServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 5274282114835055937L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        WelcomePageData result = new WelcomePageData();
        ModuleData iconModuleData = new ModuleData();
        ModuleConfig iconCarouselModuleConfig = new ModuleConfig();
        List<IconData> icons = new ArrayList<IconData>();
        List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();
        HashMap<String, ModuleData> moduleDatas = new HashMap<String, ModuleData>();
        IconData icon = new IconData();

        iconCarouselModuleConfig.setContentTitle("CarouselTitle");
        iconCarouselModuleConfig.setModuleInstanceId("ic_vik");
        iconCarouselModuleConfig.setModuleTitle("ModuleTitle");
        iconCarouselModuleConfig.setModuleTitleTextBanner("ModuleTitleTextBanner");
        iconCarouselModuleConfig.setShowContentTitle(false);
        iconCarouselModuleConfig.setShowHeaderGraphic(false);
        iconCarouselModuleConfig.setShowHeaderSubtitle(false);
        iconCarouselModuleConfig.setShowHeaderTitle(false);
        iconCarouselModuleConfig.setShowHeroSubtitle(false);
        iconCarouselModuleConfig.setShowModuleTitleTextBanner(true);
        iconCarouselModuleConfig.setShowModuleTitle(true);
        iconCarouselModuleConfig.setShowViewAllButton(false);
        iconCarouselModuleConfig.setSourceType("ICON_CAROUSEL_MODULE");
        iconCarouselModuleConfig.setViewAllButtonLink("/browse.jsp?id=fru");

        moduleConfigs.add(iconCarouselModuleConfig);

        icon.setIconImage("/media/images/product/bakery/cake/bak_cupcake_fd_c.jpg");
        icon.setIconLink("/browse.jsp?id=fru");
        icon.setIconLinkText("Trendy Fruits");
        for (int i = 0; i < 12; i++) {
            icons.add(icon);
        }
        iconModuleData.setIcons(icons);
        moduleDatas.put(iconCarouselModuleConfig.getModuleInstanceId(), iconModuleData);

        result.setConfig(moduleConfigs);
        result.setData(moduleDatas);

        writeResponseData(response, result);
    }

}
