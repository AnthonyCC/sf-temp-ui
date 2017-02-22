package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
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
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleEditorialContainer;
import com.freshdirect.webapp.util.MediaUtils;

public class DatasourceService {

    private static DatasourceService INSTANCE = new DatasourceService();
    private static final Logger LOGGER = LoggerFactory.getInstance(DatasourceService.class);

    private static final String INDEX_CM_EVENT_SOURCE = "BROWSE";

    private static final String TOP_ITEMS_SITE_FEATURE = "TOP_ITEMS_QS";
    private static final String MOST_POPULAR_PRODUCTS_SITE_FEATURE = "FAVORITES";
    private static final String MODULE_GROUP_SOURCE_TYPE = "MODULE_GROUP";

    public static DatasourceService getDefaultService() {
        return INSTANCE;
    }

    private DatasourceService() {
    }

    public ModuleConfig loadModuleConfiguration(ContentNodeI module, FDUserI user) {
        return populateModuleConfig(module, user);
    }

    public ModuleConfig loadModuleGroupConfiguration(ContentNodeI moduleGroup, FDUserI user) {
        return populateModuleGroupConfig(moduleGroup);
    }

    public ModuleData loadModuleData(ContentNodeI module, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {
        return populateModuleData(module, user, session);
    }

    private ModuleConfig populateModuleGroupConfig(ContentNodeI moduleGroup) {
        ModuleConfig config = new ModuleConfig();

        String sourceType = MODULE_GROUP_SOURCE_TYPE;
        config.setCmEventSource(INDEX_CM_EVENT_SOURCE);

        // General Module Config Data
        config.setModuleGroupTitle(ContentNodeUtil.getStringAttribute(moduleGroup, "moduleGroupTitle"));
        config.setModuleGroupTitleTextBanner(ContentNodeUtil.getStringAttribute(moduleGroup, "moduleGroupTitleTextBanner"));
        config.setHideModuleGroupViewAllButton(ContentNodeUtil.getBooleanAttribute(moduleGroup, "hideViewAllButton"));

        config.setSourceType(sourceType);
        String viewAllUrl = ContentNodeUtil.getStringAttribute(moduleGroup, "viewAllButtonURL");

        if (viewAllUrl == null) {
            ContentKey viewAllSourceContentKey = (ContentKey) moduleGroup.getAttributeValue("viewAllSourceNode");
            if (viewAllSourceContentKey != null) {
                viewAllUrl = "/browse.jsp?id=" + viewAllSourceContentKey.getId();
            }
        }

        config.setModuleGroupViewAllButtonLink(viewAllUrl);

        return config;
    }

    private ModuleConfig populateModuleConfig(ContentNodeI module, FDUserI user) {
        ModuleConfig config = new ModuleConfig();
        String sourceType = ContentNodeUtil.getStringAttribute(module, "displayType");

        config.setCmEventSource(INDEX_CM_EVENT_SOURCE);
        config.setSourceType(sourceType);

        // General Module Config Data
        config.setModuleId(module.getKey().getId());
        config.setModuleTitle(ContentNodeUtil.getStringAttribute(module, "moduleTitle"));
        config.setModuleTitleTextBanner(ContentNodeUtil.getStringAttribute(module, "moduleTitleTextBanner"));
        config.setContentTitle(ContentNodeUtil.getStringAttribute(module, "contentTitle"));

        config.setHideViewAllButton(ContentNodeUtil.getBooleanAttribute(module, "hideViewAllButton"));
        config.setHideProductBadge(ContentNodeUtil.getBooleanAttribute(module, "hideProductBadge"));
        config.setHideProductName(ContentNodeUtil.getBooleanAttribute(module, "hideProductName"));
        config.setHideProductPrice(ContentNodeUtil.getBooleanAttribute(module, "hideProductPrice"));
        config.setUseViewAllPopup(ContentNodeUtil.getBooleanAttribute(module, "useViewAllPopup"));

        String viewAllUrl = ContentNodeUtil.getStringAttribute(module, "viewAllButtonLink");

        if (viewAllUrl == null) {
            ContentKey viewAllSourceContentKey = (ContentKey) module.getAttributeValue("sourceNode");

            if (viewAllSourceContentKey != null) {
                if (FDContentTypes.DEPARTMENT.equals(viewAllSourceContentKey.getType()) || FDContentTypes.CATEGORY.equals(viewAllSourceContentKey.getType())) {
                    viewAllUrl = "/browse.jsp?id=" + viewAllSourceContentKey.getId();
                }
                if (FDContentTypes.BRAND.equals(viewAllSourceContentKey.getType())) {
                    ContentNodeI brand = CmsManager.getInstance().getContentNode(viewAllSourceContentKey, ContentFactory.getInstance().getCurrentDraftContext());
                    viewAllUrl = "/srch.jsp?searchParams=" + ContentNodeUtil.getStringAttribute(brand, "FULL_NAME");
                }
            }
        }

        config.setViewAllButtonLink(viewAllUrl);

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
                headerGraphic = MediaUtils.generateImageFromImageContentKey(module.getAttributeValue("headerGraphic"));
            }

            if (heroGraphicContentKey != null) {
                heroGraphic = MediaUtils.generateImageFromImageContentKey(module.getAttributeValue("heroGraphic"));
            }

            if (editorialContentKey != null) {
                editorialContainer.setEditorialContent(MediaUtils.generateStringFromHTMLContentKey(editorialContentKey, user));
            }

            editorialContainer.setHeaderTitle(ContentNodeUtil.getStringAttribute(module, "headerTitle"));
            editorialContainer.setHeaderSubtitle(ContentNodeUtil.getStringAttribute(module, "heroSubtitle"));
            editorialContainer.setHeroTitle(ContentNodeUtil.getStringAttribute(module, "heroTitle"));
            editorialContainer.setHeroSubtitle(ContentNodeUtil.getStringAttribute(module, "headerSubtitle"));
            editorialContainer.setHeaderGraphic(headerGraphic.getPath());
            editorialContainer.setHeroGraphic(heroGraphic.getPath());

            config.setEditorialContainer(editorialContainer);
        }

        return config;

    }

    @SuppressWarnings("unchecked")
    private ModuleData populateModuleData(ContentNodeI module, FDUserI user, HttpSession session) throws FDResourceException, InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ModuleData moduleData = new ModuleData();
        DatasourceType datasourceEnum = convertAttributeValueToDatasourceType((String) module.getAttributeValue("productSourceType"));

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
                ModuleSourceType moduleSourceType = convertAttributeValueToModuleSourceType(ContentNodeUtil.getStringAttribute(module, "displayType"));
                switch (moduleSourceType) {
                    case IMAGEGRID_MODULE:
                        List<IconData> imageGridData = new ArrayList<IconData>();
                        List<ContentKey> imageGridContentKeys = (List<ContentKey>) module.getAttributeValue("imageGrid");

                        for (ContentKey contentKey : imageGridContentKeys) {
                            ContentNodeI imageBanner = CmsManager.getInstance().getContentNode(contentKey, currentDraftContext);
                            IconData imageData = ModuleContentService.getDefaultService().populateIconData(imageBanner);
                            imageGridData.add(imageData);
                        }

                        if (imageGridData.size() > 6) {
                            imageGridData = imageGridData.subList(0, 6);
                        }

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
                ContentNodeI category = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
                String categoryId = category.getKey().getId();
                moduleData.setProducts(ModuleContentService.getDefaultService().loadBrowseProducts(categoryId, user));

                break;
            case FEATURED_RECOMMENDER:
                try {
                    ContentNodeI department = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
                    String departmentId = department.getKey().getId();
                    moduleData.setProducts(ModuleContentService.getDefaultService().loadFeaturedItems(user, departmentId));
                } catch (NullPointerException e) {
                    LOGGER.error("Datasource sourceNode is not set for Module:" + module.getKey().getId());
                } catch (ClassCastException e) {
                    LOGGER.error("Datasource sourceNode is not a department for Module:" + module.getKey().getId());
                }
                break;

            case BRAND:
                ContentNodeI brand = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
                moduleData.setProducts(ModuleContentService.getDefaultService().loadBrandFeaturedProducts(brand, user));
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
