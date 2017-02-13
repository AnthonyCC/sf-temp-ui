package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.data.WelcomePageData;

public class ModuleHandlingService {

    private static ModuleHandlingService INSTANCE = new ModuleHandlingService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleHandlingService.class);

    public static ModuleHandlingService getDefaultService() {
        return INSTANCE;
    }

    private ModuleHandlingService() {
    }

    @SuppressWarnings("unchecked")
    public WelcomePageData loadModuleContainer(String moduleContainerId, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {

        WelcomePageData result = new WelcomePageData();

        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();

        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();

        ContentNodeI moduleContainer = CmsManager.getInstance().getContentNode(ContentKey.getContentKey(moduleContainerId), currentDraftContext);

        if (moduleContainer != null) {
            List<ContentKey> moduleInstances = (List<ContentKey>) moduleContainer.getAttributeValue("moduleInstances");

            if (moduleInstances != null) {
                for (ContentKey moduleInstanceContentKey : moduleInstances) {
                    ModuleData moduleData = loadModuleInstanceData(moduleInstanceContentKey, user, session);
                    if (moduleData != null) {
                        ModuleConfig moduleConfig = loadModuleInstanceConfig(moduleInstanceContentKey, user);
                        configs.add(moduleConfig);
                        datas.put(moduleConfig.getModuleInstanceId(), moduleData);
                    }

                }
            }
        }
        result.setData(datas);
        result.setConfig(configs);

        return result;
    }

    public WelcomePageData loadModuleInstance(ContentKey moduleInstanceContentKey, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {

        WelcomePageData result = new WelcomePageData();

        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();

        ModuleData moduleData = loadModuleInstanceData(moduleInstanceContentKey, user, session);
        if (moduleData != null) {
            ModuleConfig moduleConfig = loadModuleInstanceConfig(moduleInstanceContentKey, user);
            configs.add(moduleConfig);
            datas.put(moduleConfig.getModuleInstanceId(), moduleData);
        }

        result.setData(datas);
        result.setConfig(configs);

        return result;
    }

    private ModuleConfig loadModuleInstanceConfig(ContentKey moduleInstanceContentKey, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI moduleInstance = CmsManager.getInstance().getContentNode(moduleInstanceContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleConfiguration(moduleInstance, user);
    }

    private ModuleData loadModuleInstanceData(ContentKey moduleInstanceContentKey, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI moduleInstance = CmsManager.getInstance().getContentNode(moduleInstanceContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleData(moduleInstance, user, session);
    }

}
