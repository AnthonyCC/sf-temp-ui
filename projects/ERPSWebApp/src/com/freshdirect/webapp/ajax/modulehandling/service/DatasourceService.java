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
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.DatasourceType;
import com.freshdirect.webapp.ajax.modulehandling.ModuleSourceType;
import com.freshdirect.webapp.ajax.modulehandling.data.IconData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleEditorialContainer;
import com.freshdirect.webapp.ajax.product.CriteoProductsUtil;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.MediaUtils;

public class DatasourceService {

    private static final Logger LOGGER = LoggerFactory.getInstance(DatasourceService.class);
    private static final DatasourceService INSTANCE = new DatasourceService();

    private static final String INDEX_CM_EVENT_SOURCE = "BROWSE";
    private static final String TOP_ITEMS_SITE_FEATURE = "TOP_ITEMS_QS";
    private static final String MOST_POPULAR_PRODUCTS_SITE_FEATURE = "FAVORITES";
    private static final String MODULE_GROUP_SOURCE_TYPE = "MODULE_GROUP";

    public static DatasourceService getDefaultService() {
        return INSTANCE;
    }

    private DatasourceService() {
    }

    private List<ProductData> generateBrandFeaturedProducts(ContentNodeI module, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI brand = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
        return ModuleContentService.getDefaultService().loadBrandFeaturedProducts(brand, user);
    }

    private SectionDataCointainer generateBrowseProductsForViewAll(ContentNodeI module, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI category = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
        String categoryId=null;
        if(category!=null && category.getKey()!=null)
        	categoryId = category.getKey().getId();
        return ModuleContentService.getDefaultService().loadBrowseSectionDataContainer(categoryId, user);
    }

    private List<ProductData> generateBrowseProducts(ContentNodeI module, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        ContentNodeI category = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
        String categoryId=null;
        if(category!=null && category.getKey()!=null)
        	categoryId = category.getKey().getId();
        return ModuleContentService.getDefaultService().loadBrowseProducts(categoryId, user);
    }

    private List<ProductData> generateFeaturedRecommenderProducts(ContentNodeI module, FDUserI user) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        List<ProductData> products = new ArrayList<ProductData>();
        try {
            ContentNodeI department = CmsManager.getInstance().getContentNode((ContentKey) module.getAttributeValue("sourceNode"), currentDraftContext);
            String departmentId = department.getKey().getId();
            products = ModuleContentService.getDefaultService().loadFeaturedItems(user, departmentId);
        } catch (NullPointerException e) {
            LOGGER.error("Datasource sourceNode is not set for Module:" + module.getKey().getId());
        } catch (ClassCastException e) {
            LOGGER.error("Datasource sourceNode is not a department for Module:" + module.getKey().getId());
        }
        return products;
    }

    private List<IconData> loadIconData(ContentNodeI module) {
        List<IconData> icons = new ArrayList<IconData>();
        List<ContentKey> iconContentKeys = (List<ContentKey>) module.getAttributeValue("iconList");

        icons = processImageBannerList(iconContentKeys, icons);

        return icons;
    }

    private List<IconData> loadImageGridData(ContentNodeI module) {
        List<IconData> imageGridData = new ArrayList<IconData>();
        List<ContentKey> imageGridContentKeys = (List<ContentKey>) module.getAttributeValue("imageGrid");

        imageGridData = processImageBannerList(imageGridContentKeys, imageGridData);

        if (imageGridData.size() > 6) {
            imageGridData = imageGridData.subList(0, 6);
        }

        return imageGridData;
    }

    public ModuleConfig loadModuleConfiguration(ContentNodeI module, FDUserI user) {
        return populateModuleConfig(module, user);
    }

    public ModuleData loadModuleData(ContentNodeI module, FDUserI user, HttpSession session, boolean showAllProducts) throws FDResourceException, InvalidFilteringArgumentException {
        return populateModuleData(module, user, session, showAllProducts);
    }

    public ModuleConfig loadModuleGroupConfiguration(ContentNodeI moduleGroup, FDUserI user) {
        return populateModuleGroupConfig(moduleGroup);
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
        config.setContentTitleTextBanner(ContentNodeUtil.getStringAttribute(module, "contentTitleTextBanner"));

        config.setHideViewAllButton(ContentNodeUtil.getBooleanAttribute(module, "hideViewAllButton"));
        config.setHideProductBadge(ContentNodeUtil.getBooleanAttribute(module, "hideProductBadge"));
        config.setHideProductName(ContentNodeUtil.getBooleanAttribute(module, "hideProductName"));
        config.setHideProductPrice(ContentNodeUtil.getBooleanAttribute(module, "hideProductPrice"));
        config.setUseViewAllPopup(ContentNodeUtil.getBooleanAttribute(module, "useViewAllPopup"));
        config.setShowViewAllOverlayOnImages(ContentNodeUtil.getBooleanAttribute(module, "showViewAllOverlayOnImages"));

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

        ModuleSourceType moduleSourceType = ModuleSourceType.convertAttributeValueToModuleSourceType(sourceType);

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
            editorialContainer.setHeaderSubtitle(ContentNodeUtil.getStringAttribute(module, "headerSubtitle"));
            editorialContainer.setHeroTitle(ContentNodeUtil.getStringAttribute(module, "heroTitle"));
            editorialContainer.setHeroSubtitle(ContentNodeUtil.getStringAttribute(module, "heroSubtitle"));
            editorialContainer.setHeaderGraphic(headerGraphic.getPath());
            editorialContainer.setHeroGraphic(heroGraphic.getPath());

            config.setEditorialContainer(editorialContainer);
        }

        return config;

    }

    private ModuleData populateModuleData(ContentNodeI module, FDUserI user, HttpSession session, boolean showAllProducts) throws FDResourceException,
            InvalidFilteringArgumentException {
        ModuleData moduleData = new ModuleData();
        DatasourceType datasourceEnum = DatasourceType.convertAttributeValueToDatasourceType((String) module.getAttributeValue("productSourceType"));
        ModuleSourceType moduleSourceType = ModuleSourceType.convertAttributeValueToModuleSourceType(ContentNodeUtil.getStringAttribute(module, "displayType"));
        String productListCarouselLineMax = ContentNodeUtil.getStringAttribute(module, "productListRowMax");
        List<ProductData> products = new ArrayList<ProductData>();

        SectionDataCointainer sectionDataContainer = null;

        // LOAD PRODUCTS
        switch (datasourceEnum) {
            case MOST_POPULAR_PRODUCTS:
                products = ModuleContentService.getDefaultService().generateRecommendationProducts(session, user, MOST_POPULAR_PRODUCTS_SITE_FEATURE);
                //moduleData.setCriteoHomeProducts(Criteo.getHomeAdProduct(u\er));
                //moduleData.setAdProducts(Criteo.getHomeAdProduct(user));
                //moduleData.setAdPrdPageBeacon();
                break;
            case TOP_ITEMS:
                products = ModuleContentService.getDefaultService().generateRecommendationProducts(session, user, TOP_ITEMS_SITE_FEATURE);
                break;
            case PRES_PICKS:
                products = ModuleContentService.getDefaultService().loadPresidentPicksProducts(user);
                break;
            case GENERIC:
                switch (moduleSourceType) {
                    case IMAGEGRID_MODULE:
                        moduleData.setImageGridData(loadImageGridData(module));
                        break;
                    case ICON_CAROUSEL_MODULE:
                        moduleData.setIcons(loadIconData(module));
                        break;
                    case OPENHTML_MODULE:
                        moduleData.setOpenHTMLEditorial(ModuleContentService.getDefaultService().populateOpenHTML(module, user));
                        break;
                    default:
                        break;
                }
                break;
            case BROWSE:
                // Special view all for browse data source with product lists.
            	if (showAllProducts && ModuleSourceType.PRODUCT_LIST_MODULE.equals(moduleSourceType)) {
            		sectionDataContainer = generateBrowseProductsForViewAll(module, user);
            	}
            	else {
                    products = generateBrowseProducts(module, user);
                }
                break;
            case FEATURED_RECOMMENDER:
                products = generateFeaturedRecommenderProducts(module, user);
                break;
            case BRAND_FEATURED_PRODUCTS:
                products = generateBrandFeaturedProducts(module, user);
                break;
            case STAFF_PICKS:
                products = ModuleContentService.getDefaultService().loadStaffPicksProducts(user);
                break;
            case CRITEO:
            	CriteoProductsUtil.addFeatureProductsToHomePage(user, moduleData);
            default:
                break;
        }

        // LIMIT PRODUCTS
        if (!showAllProducts) {
            products = ModuleContentService.getDefaultService().limitProductList(products);
        }

        // LIMIT PRODUCTS ADDITIONALY IF PRODUCT LIST IS ENABLED
        if (!showAllProducts && ModuleSourceType.PRODUCT_LIST_MODULE.equals(moduleSourceType) && productListCarouselLineMax != null && products != null) {
            products = ModuleContentService.getDefaultService().setMaxProductLinesForProductList(products, Integer.parseInt(productListCarouselLineMax));
        }

        moduleData.setSectionDataContainer(sectionDataContainer);
        moduleData.setProducts(products);

        return moduleData;
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

    private List<IconData> processImageBannerList(List<ContentKey> contentKeys, List<IconData> moduleImages) {
        DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
        for (ContentKey contentKey : contentKeys) {
            ContentNodeI imageBanner = CmsManager.getInstance().getContentNode(contentKey, currentDraftContext);
            IconData imageBannerData = ModuleContentService.getDefaultService().populateIconData(imageBanner);
            moduleImages.add(imageBannerData);
        }
        return moduleImages;
    }

}
