package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.DatasourceType;
import com.freshdirect.webapp.ajax.modulehandling.ModuleSourceType;
import com.freshdirect.webapp.ajax.modulehandling.data.IconData;
import com.freshdirect.webapp.ajax.modulehandling.data.ImageGridData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleEditorialContainer;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class DatasourceService {

    private static DatasourceService INSTANCE = new DatasourceService();
    private static final Logger LOGGER = LoggerFactory.getInstance(DatasourceService.class);

    private static final int MAX_ITEMS = 12;;

    private static final String INDEX_CM_EVENT_SOURCE = "BROWSE";

    private static final String TOP_ITEMS_SITE_FEATURE = "TOP_ITEMS_QS";
    private static final String MOST_POPULAR_PRODUCTS_SITE_FEATURE = "FAVORITES";

    public static DatasourceService getDefaultService() {
        return INSTANCE;
    }

    private DatasourceService() {
    }

    public ModuleConfig loadModuleConfiguration(ContentNodeI moduleInstance, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI module = CmsManager.getInstance().getContentNode((ContentKey) moduleInstance.getAttributeValue("module"), currentDraftContext);
        return populateModuleConfig(module, moduleInstance.getKey().getId(), user);
    }

    public ModuleData loadModuleData(ContentNodeI moduleInstance, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI module = CmsManager.getInstance().getContentNode((ContentKey) moduleInstance.getAttributeValue("module"), currentDraftContext);
        ContentNodeI datasource = CmsManager.getInstance().getContentNode((ContentKey) moduleInstance.getAttributeValue("datasource"), currentDraftContext);

        return populateModuleData(datasource, module, user, session);
    }

    private ModuleConfig populateModuleConfig(ContentNodeI module, String moduleInstanceId, FDUserI user) {
        ModuleConfig config = new ModuleConfig();
        FDSessionUser sessionUser = (FDSessionUser) user;

        String sourceType = ContentNodeUtil.getStringAttribute(module, "sourceType");

        // General Module Config Data
        config.setContentTitle(ContentNodeUtil.getStringAttribute(module, "contentTitle"));
        config.setModuleInstanceId(moduleInstanceId);
        config.setModuleTitle(ContentNodeUtil.getStringAttribute(module, "moduleTitle"));
        config.setModuleTitleTextBanner(ContentNodeUtil.getStringAttribute(module, "moduleTitleTextBanner"));
        config.setViewAllButtonLink(ContentNodeUtil.getStringAttribute(module, "viewAllButtonLink"));
        config.setShowContentTitle(ContentNodeUtil.getBooleanAttribute(module, "showContentTitle"));
        config.setShowModuleTitle(ContentNodeUtil.getBooleanAttribute(module, "showModuleTitle"));
        config.setShowModuleTitleTextBanner(ContentNodeUtil.getBooleanAttribute(module, "showModuleTitleTextBanner"));
        config.setShowViewAllButton(ContentNodeUtil.getBooleanAttribute(module, "showViewAllButton"));
        config.setSourceType(sourceType);
        config.setHideProductBadge(ContentNodeUtil.getBooleanAttribute(module, "hideProductBadge"));
        config.setHideProductName(ContentNodeUtil.getBooleanAttribute(module, "hideProductName"));
        config.setHideProductPrice(ContentNodeUtil.getBooleanAttribute(module, "hideProductPrice"));
        config.setCmEventSource(INDEX_CM_EVENT_SOURCE);

        ModuleSourceType moduleSourceType = convertAttributeValueToModuleSourceType(sourceType);

        // Editoral Module Config Data
        if (moduleSourceType != null && ModuleSourceType.EDITORIAL_MODULE.equals(moduleSourceType)) {
            ModuleEditorialContainer editorialContainer = new ModuleEditorialContainer();

            Image headerGraphic = new Image();
            Image heroGraphic = new Image();

            ContentKey headerGraphicContentKey = (ContentKey) module.getAttributeValue("headerGraphic");
            ContentKey heroGraphicContentKey = (ContentKey) module.getAttributeValue("heroGraphic");
            ContentKey editorialContentKey = (ContentKey) module.getAttributeValue("editorialContent");

            if (headerGraphicContentKey != null) {
                headerGraphic = ModuleContentService.getDefaultService().generateImageFromImageContentKey(module.getAttributeValue("headerGraphic"));
            }

            if (heroGraphicContentKey != null) {
                heroGraphic = ModuleContentService.getDefaultService().generateImageFromImageContentKey(module.getAttributeValue("heroGraphic"));
            }

            if (editorialContentKey != null) {
                editorialContainer.setEditorialContent(ModuleContentService.getDefaultService().generateStringFromHTMLContentKey(editorialContentKey, sessionUser));
            }

            editorialContainer.setHeaderTitle(ContentNodeUtil.getStringAttribute(module, "headerTitle"));
            editorialContainer.setHeaderSubtitle(ContentNodeUtil.getStringAttribute(module, "heroSubtitle"));
            editorialContainer.setHeroTitle(ContentNodeUtil.getStringAttribute(module, "heroTitle"));
            editorialContainer.setHeroSubtitle(ContentNodeUtil.getStringAttribute(module, "headerSubtitle"));
            editorialContainer.setHeaderGraphic(headerGraphic.getPath());
            editorialContainer.setHeroGraphic(heroGraphic.getPath());
            config.setShowHeaderGraphic(true);
            config.setShowHeaderTitle(ContentNodeUtil.getBooleanAttribute(module, "showHeaderTitle"));
            config.setShowHeaderSubtitle(ContentNodeUtil.getBooleanAttribute(module, "showHeaderSubtitle"));
            config.setShowHeroTitle(ContentNodeUtil.getBooleanAttribute(module, "showHeroTitle"));
            config.setShowHeroSubtitle(ContentNodeUtil.getBooleanAttribute(module, "showHeroSubtitle"));
            config.setEditorialContainer(editorialContainer);
        }

        return config;

    }

    @SuppressWarnings("unchecked")
    private ModuleData populateModuleData(ContentNodeI datasource, ContentNodeI module, FDUserI user, HttpSession session) throws FDResourceException,
            InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ModuleData moduleData = new ModuleData();
        DatasourceType datasourceEnum = convertAttributeValueToDatasourceType((String) datasource.getAttributeValue("datasourceType"));
        switch (datasourceEnum) {
            case MOST_POPULAR_PRODUCTS:
                moduleData.setProducts(ModuleContentService.getDefaultService().generateRecommendationProducts(session, user, MOST_POPULAR_PRODUCTS_SITE_FEATURE));
                break;
            case TOP_ITEMS:
                moduleData.setProducts(ModuleContentService.getDefaultService().generateRecommendationProducts(session, user, TOP_ITEMS_SITE_FEATURE));
                break;
            case PRES_PICKS:
                moduleData.setProducts(ModuleContentService.getDefaultService().loadPresidentPicksProducts(user));
                break;
            case GENERIC:
                ModuleSourceType moduleSourceType = convertAttributeValueToModuleSourceType(ContentNodeUtil.getStringAttribute(module, "sourceType"));
                switch (moduleSourceType) {
                    case IMAGEGRID_MODULE:
                        ContentNodeI imageGrid = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("imageGrid"), currentDraftContext);
                        ImageGridData imageGridData = ModuleContentService.getDefaultService().populateImageGridData(imageGrid);
                        moduleData.setImageGridData(imageGridData);
                        break;
                    case ICON_CAROUSEL_MODULE:
                        List<IconData> icons = new ArrayList<IconData>();
                        List<ContentKey> iconContentKeys = (List<ContentKey>) module.getAttributeValue("iconList");

                        for (ContentKey contentKey : iconContentKeys) {
                            ContentNodeI icon = CmsManager.getInstance().getContentNode(contentKey, currentDraftContext);
                            IconData iconData = ModuleContentService.getDefaultService().populateIconData(icon);
                            icons.add(iconData);
                        }
                        moduleData.setIcons(icons);
                        break;
                    case OPENHTML_MODULE:
                        moduleData.setOpenHTMLEditorial(ModuleContentService.getDefaultService().populateOpenHTML(module, user));
                        break;
                    default:
                        break;
                }
                break;
            case BROWSE:
                ContentNodeI category = CmsManager.getInstance().getContentNode((ContentKey) datasource.getAttributeValue("sourceNode"), currentDraftContext);
                String categoryId = category.getKey().getId();
                moduleData.setProducts(ModuleContentService.getDefaultService().loadBrowseProducts(categoryId, user));

                break;
            case FEATURED_RECOMMENDER:
                try {
                    ContentNodeI department = CmsManager.getInstance().getContentNode((ContentKey) datasource.getAttributeValue("sourceNode"), currentDraftContext);
                    String departmentId = department.getKey().getId();
                    moduleData.setProducts(ModuleContentService.getDefaultService().loadFeaturedItems(user, departmentId));
                } catch (NullPointerException e) {
                    LOGGER.error("Datasource sourceNode is not set for Datasource:" + datasource.getKey().getId());
                } catch (ClassCastException e) {
                    LOGGER.error("Datasource sourceNode is not a department for Datasource:" + datasource.getKey().getId());
                }
                break;

            default:
                break;
        }
        return moduleData;
    }

    private DatasourceType convertAttributeValueToDatasourceType(String datasourceTypeAttributeValue) {
        return DatasourceType.forValue(datasourceTypeAttributeValue);
    }

    private ModuleSourceType convertAttributeValueToModuleSourceType(String moduleSourceTypeAttributeValue) {
        return ModuleSourceType.forValue(moduleSourceTypeAttributeValue);
    }

}
