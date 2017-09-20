package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.ModuleSourceType;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;

/**
 * Used to load a moduleContainer for a User.
 * 
 * @author dviktor
 *
 */
public final class ModuleHandlingService {

    private static final ModuleHandlingService INSTANCE = new ModuleHandlingService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleHandlingService.class);

    private ModuleHandlingService() {
    }

    public static ModuleHandlingService getDefaultService() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public ModuleContainerData loadModuleContainer(String moduleContainerId, FDUserI user, HttpSession session)
            throws FDResourceException, InvalidFilteringArgumentException, FDNotFoundException {

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

                                if (datas.containsKey(moduleContentKey.getId())) {
                                    moduleData = datas.get(moduleContentKey.getId());
                                } else {
                                    moduleData = loadModuleData(moduleContentKey, user, session, false);
                                }

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

                        if (datas.containsKey(moduleContainerChildContentKey.getId())) {
                            moduleData = datas.get(moduleContainerChildContentKey.getId());
                        } else {
                            moduleData = loadModuleData(moduleContainerChildContentKey, user, session, false);
                        }

                        if (moduleData != null) {
                            moduleConfig = loadModuleConfig(moduleContainerChildContentKey, user);
                        }

                        datas.put(moduleConfig.getModuleId(), moduleData);
                        configs.add(moduleConfig);
                    }
                }
            }
        } else {
            LOGGER.warn("ModuleContainer was not found in Content Management System with id: " + moduleContainerId);
        }

        result.setData(datas);
        result.setConfig(configs);

        result = filterDisplayableModules(result);
        decorateVirtualCategory(result, moduleContainerId.split(":")[1]);

        return result;
    }

    public ModuleContainerData loadModuleforViewAll(String moduleId, FDUserI user, HttpSession session)
            throws FDResourceException, InvalidFilteringArgumentException, FDNotFoundException {
        ModuleContainerData result = new ModuleContainerData();
        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();



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
            InvalidFilteringArgumentException, FDNotFoundException {
        LOGGER.info("Loading module with id: " + moduleContentKey.getId());
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI module = CmsManager.getInstance().getContentNode(moduleContentKey, currentDraftContext);
        return DatasourceService.getDefaultService().loadModuleData(module, user, session, showAllProducts);
    }

    /**
     * 
     * @param moduleContainer
     * 
     * @author dviktor
     * 
     *         This method removes any modules from the container that are product display modules and have less than 3 products
     * @return
     */
    private ModuleContainerData filterDisplayableModules(ModuleContainerData moduleContainer) {
        // TODO reverse iterate first on config then on data so instead of removing we can use adding to empty list.
        Map<String, ModuleData> moduleDatas = moduleContainer.getData();
        List<ModuleConfig> moduleConfigs = moduleContainer.getConfig();

        ModuleContainerData filteredModuleContainerData = new ModuleContainerData();
        Map<String, ModuleData> filteredModuleDatas = new HashMap<String, ModuleData>();
        List<ModuleConfig> filteredModuleConfigs = new ArrayList<ModuleConfig>();

        filteredModuleDatas.putAll(moduleDatas);
        filteredModuleConfigs.addAll(moduleConfigs);

        for (Entry<String, ModuleData> entry : moduleDatas.entrySet()) {
            String key = entry.getKey();
            ModuleData moduleData = entry.getValue();

            if (moduleData.getImageGridData() == null && moduleData.getOpenHTMLEditorial() == null && moduleData.getIcons() == null) {
                if (moduleData.getProducts().size() < 3 && moduleData.getAdProducts().size() == 0) {
                    for (ModuleConfig moduleConfig : moduleConfigs) {
                        if (moduleConfig.getModuleId() == key) {
                            filteredModuleConfigs.remove(moduleConfig);
                        }
                    }
                    filteredModuleDatas.remove(key);
                }
            }
        }

        filteredModuleContainerData.setData(filteredModuleDatas);
        filteredModuleContainerData.setConfig(filteredModuleConfigs);

        return filteredModuleContainerData;
    }

    /**
     * 
     * @param moduleContainer
     * 
     * @author dviktor
     * 
     *         Decorates all modules with their position and id as a virtual category. ModuleGroups won't be assigned a position.
     * @param moduleContainerId
     */
    private void decorateVirtualCategory(ModuleContainerData moduleContainer, String moduleContainerId) {
        List<ModuleConfig> moduleConfigs = moduleContainer.getConfig();
        int moduleVirtualCategoryPosition = 1;

        for (ModuleConfig moduleConfig : moduleConfigs) {
            if (moduleConfig.getSourceType() != "MODULE_GROUP") {
                moduleConfig.setModuleVirtualCategory(moduleContainerId + ":POSITION " + moduleVirtualCategoryPosition + ":" + moduleConfig.getModuleId());
                moduleVirtualCategoryPosition++;
            }
        }
    }

    public ModuleContainerData loadModuleContainerForImageBanners(String imageBannerContentKey, FDUserI user, String moduleVirtualCategory) throws FDResourceException,
            InvalidFilteringArgumentException, FDNotFoundException {
        ModuleContainerData result = new ModuleContainerData();
        List<ModuleConfig> configs = new ArrayList<ModuleConfig>();
        Map<String, ModuleData> datas = new HashMap<String, ModuleData>();
        ModuleData moduleData = new ModuleData();
        ModuleConfig moduleConfig = new ModuleConfig();
        
        String imageModuleId = "imageModuleId";

        LOGGER.info("Loading products for imageBanner: " + imageBannerContentKey);
        //Load Browse sectionDataContainer
        SectionDataCointainer sectionDataContainer = new SectionDataCointainer();

        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI imageBanner = CmsManager.getInstance().getContentNode(ContentKey.getContentKey(imageBannerContentKey), currentDraftContext);
        ContentKey targetContentKey = (ContentKey) imageBanner.getAttributeValue("Target");
        
        if (targetContentKey.getType() == FDContentTypes.CATEGORY ){
            sectionDataContainer = ModuleContentService.getDefaultService().loadBrowseSectionDataContainer(targetContentKey.getId(), user);
        }

        moduleData.setSectionDataContainer(sectionDataContainer);
        moduleConfig.setModuleVirtualCategory(moduleVirtualCategory);
        moduleConfig.setSourceType(ModuleSourceType.PRODUCT_LIST_MODULE.toString());
        moduleConfig.setModuleId(imageModuleId);
        moduleConfig.setContentTitle(ContentNodeUtil.getStringAttribute(imageBanner, "Description"));

        datas.put(imageModuleId, moduleData);
        configs.add(moduleConfig);
        
        result.setConfig(configs);
        result.setData(datas);

        return result;
    }

}
