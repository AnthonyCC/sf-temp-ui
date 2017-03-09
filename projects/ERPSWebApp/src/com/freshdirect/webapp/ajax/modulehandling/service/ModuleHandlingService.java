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
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

/**
 * Used to load a moduleContainer for a User.
 * 
 * @author dviktor
 *
 */
public final class ModuleHandlingService {

    private static ModuleHandlingService INSTANCE = new ModuleHandlingService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleHandlingService.class);

    private ModuleHandlingService() {
    }

    public static ModuleHandlingService getDefaultService() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public ModuleContainerData loadModuleContainer(String moduleContainerId, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {

        ModuleContainerData result = new ModuleContainerData();
        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();

        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();

        ContentNodeI moduleContainer = CmsManager.getInstance().getContentNode(ContentKey.getContentKey(moduleContainerId), currentDraftContext);

        if (moduleContainer != null) {
            List<ContentKey> modulesAndGroups = (List<ContentKey>) moduleContainer.getAttributeValue("modulesAndGroups");

            if (modulesAndGroups != null) {
                for (ContentKey moduleContainerChildContentKey : modulesAndGroups) {
                    if (FDContentTypes.MODULE_GROUP.equals(moduleContainerChildContentKey.getType())) {

                        ModuleConfig moduleGroupConfig = new ModuleConfig();
                        moduleGroupConfig = loadModuleGroupConfig(moduleContainerChildContentKey, user);
                        configs.add(moduleGroupConfig);

                        ContentNodeI moduleGroup = CmsManager.getInstance().getContentNode(moduleContainerChildContentKey, currentDraftContext);
                        List<ContentKey> modules = (List<ContentKey>) moduleGroup.getAttributeValue("modules");

                        if (modules != null) {
                            for (ContentKey moduleContentKey : modules) {
                                ModuleData moduleData = new ModuleData();
                                ModuleConfig moduleConfig = new ModuleConfig();
                                moduleData = loadModuleData(moduleContentKey, user, session, false);

                                if (moduleData != null) {
                                    moduleConfig = loadModuleConfig(moduleContentKey, user);
                                }

                                datas.put(moduleConfig.getModuleId(), moduleData);
                                configs.add(moduleConfig);
                            }
                        }
                    } else if (FDContentTypes.MODULE.equals(moduleContainerChildContentKey.getType())) {
                        ModuleData moduleData = new ModuleData();
                        ModuleConfig moduleConfig = new ModuleConfig();
                        moduleData = loadModuleData(moduleContainerChildContentKey, user, session, false);

                        if (moduleData != null) {
                            moduleConfig = loadModuleConfig(moduleContainerChildContentKey, user);
                        }

                        datas.put(moduleConfig.getModuleId(), moduleData);
                        configs.add(moduleConfig);
                    }
                }
            }
        }

        result.setData(datas);
        result.setConfig(configs);

        return result;
    }

    public ModuleContainerData loadModuleforViewAll(String moduleId, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {
        ModuleContainerData result = new ModuleContainerData();
        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();

        // Checklogin status is not applicable for AJAX calls so we need this.
        ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDSessionUser) user).isEligibleForDDPP());

        ModuleData moduleData = new ModuleData();
        ModuleConfig moduleConfig = new ModuleConfig();
        moduleData = loadModuleData(ContentKey.getContentKey(moduleId), user, session, true);

        if (moduleData != null) {
            moduleConfig = loadModuleConfig(ContentKey.getContentKey(moduleId), user);
        }

        datas.put(moduleConfig.getModuleId(), moduleData);
        configs.add(moduleConfig);

        result.setConfig(configs);
        result.setData(datas);

        return result;
    }

    private ModuleConfig loadModuleConfig(ContentKey moduleContentKey, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI module = CmsManager.getInstance().getContentNode(moduleContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleConfiguration(module, user);
    }

    private ModuleConfig loadModuleGroupConfig(ContentKey moduleGroupContentKey, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI moduleGroup = CmsManager.getInstance().getContentNode(moduleGroupContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleGroupConfiguration(moduleGroup, user);
    }

    private ModuleData loadModuleData(ContentKey moduleContentKey, FDUserI user, HttpSession session, boolean showAllProducts) throws FDResourceException,
            InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI module = CmsManager.getInstance().getContentNode(moduleContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleData(module, user, session, showAllProducts);
    }

}
