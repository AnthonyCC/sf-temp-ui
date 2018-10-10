package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.controller.data.response.Idea.ideaFor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.request.ModuleContainerRequest;
import com.freshdirect.mobileapi.controller.data.response.FeaturedCategoriesResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeGetAllResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeResponse;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.ModuleContainerResponse;
import com.freshdirect.mobileapi.controller.data.response.PageMessageResponse;
import com.freshdirect.mobileapi.controller.data.response.WebPageResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.FeaturedCategory;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.CMSSectionProductCollectorService;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.BannerModel;
import com.freshdirect.storeapi.content.CMSPageRequest;
import com.freshdirect.storeapi.content.CMSPickListModel;
import com.freshdirect.storeapi.content.CMSSectionModel;
import com.freshdirect.storeapi.content.CMSWebPageModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.StoreModel;
import com.freshdirect.wcms.CMSContentFactory;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleConfig;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;

public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getInstance(HomeController.class);

    private static final String MODULE_CONTAINER_ERROR_MESSAGE = "Error occured when loading module container data of {0} id";
    private static final String MODULE_CONTAINER_WARNING_MESSAGE = "No module with {0} id is found";
	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_FEATURED_CATEGORIES = "getFeaturedCategories";
	private static final String ACTION_GET_All_DETAILS = "getAllDetails";
	private static final String ACTION_GET_CMS_PAGE = "getPage";
	private static final String ACTION_GET_CMS_PAGE_COMPONENT = "getPageComponent";
    private static final String ACTION_GET_MODULE = "getModule";
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_MAX = 998;

	@Override
    protected boolean validateUser() {
        return false;
    }
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException {
        Message message = null;
		if (ACTION_GET_ALL.equals(action)) {
            message = all(user);
		} else if (ACTION_GET_FEATURED_CATEGORIES.equals(action)) {
            message = featuredCategories(user);
		} else if(ACTION_GET_All_DETAILS.equals(action)){
            message = getAllDetails(user, request);
        } else if (ACTION_GET_CMS_PAGE.equals(action)) {
            if (isExtraResponseRequested(request)) {
                message = getCMSPages(user, request, response);
            } else {
                message = getCMSPage(user, request, response);
            }
        } else if (ACTION_GET_CMS_PAGE_COMPONENT.equals(action)) {
        	message = getCMSPageComponents(user, request, response);
        } else if (ACTION_GET_MODULE.equals(action)) {
            message = getModule(user, request, response);
        } else {
            throw new UnsupportedOperationException();
        }
        setResponseMessage(model, message, user);
        return model;
	}

    private FeaturedCategoriesResponse featuredCategories(SessionUser user) throws JsonException {
		FeaturedCategoriesResponse result = new FeaturedCategoriesResponse();
        StoreModel store = ContentFactory.getInstance().getStore();
        List<Category> categories = new ArrayList<Category>();
        for (CategoryModel categoryModel : store.getTabletFeaturedCategories()) {
			categories.add(Category.wrap(categoryModel));
		}
        result.setFeaturedCategories(categories);
//        result.setHomescreenBanner(homescreenBanner);
        return result;
	}

    private HomeGetAllResponse all(SessionUser user) throws JsonException {
        HomeGetAllResponse response = new HomeGetAllResponse();
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.Banner);
        List<Idea> carrouselItems = new ArrayList<Idea>();
        for (ContentKey key : contentKeysByType) {
            BannerModel banner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if ("homepage".equalsIgnoreCase(banner.getLocation())) {
            	Idea idea = ideaFor(banner);
            	carrouselItems.add(idea);
            }
        }
        response.setCarouselItems(carrouselItems);

        StoreModel store = ContentFactory.getInstance().getStore();
        final List<BannerModel> shopBanners = store.getTabletHomeScreenPopUpShopBanners();

		List<Idea> shops = new ArrayList<Idea>();
		if (shopBanners != null) {
			for (BannerModel shop : shopBanners) {
				if (shop.getContentKey() != null
						&& !shop.isHidden()) {
					Idea idea = ideaFor(shop);
					shops.add(idea);
				}
			}
		}
		response.setShops(shops);
        return response;
	}

	/*
	 * non-javadoc
	 * This method will consolidate getAll and getFeaturedCategories and browseCategories as well.
	 * 
	 * The method is devided into three domains:
	 * 1. to get all the carousel items and banner.
	 * 2. to get the featured categories 
	 * 3. to iterate over the list of categories and get the list of products.
	 * 
	 */
    private HomeResponse getAllDetails(SessionUser user, HttpServletRequest request) throws JsonException, FDException {
		
		HomeResponse response = new HomeResponse();
		
		if (user == null) {
    		user = fakeUser(request.getSession());
    	}
		
		// get the carousel Items and Banner for Mobile home page.
		//----------------------------------------CarouselItems---------------------------------------------------------
		Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.Banner);
        List<Idea> carrouselItems = new ArrayList<Idea>();
        for (ContentKey key : contentKeysByType) {
            BannerModel banner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if ("homepage".equalsIgnoreCase(banner.getLocation())) {
            	Idea idea = ideaFor(banner);
            	carrouselItems.add(idea);
            }
        }
        response.setCarouselItems(carrouselItems);
      //----------------------------------------------- END -----------------------------------------------------
        
      //-----------------------------------------------Banner----------------------------------------------------------
        StoreModel store = ContentFactory.getInstance().getStore();
        final List<BannerModel> shopBanners = store.getTabletHomeScreenPopUpShopBanners();

		List<Idea> shops = new ArrayList<Idea>();
		if (shopBanners != null) {
			for (BannerModel shop : shopBanners) {
				if (shop.getContentKey() != null
						&& !shop.isHidden()) {
					Idea idea = ideaFor(shop);
					shops.add(idea);
				}
			}
		}
		response.setShops(shops);
		//-------------------------------------------- END ---------------------------------------------------------  
		
		// get the featured categories to be presented with on home screen
		
		//-----------------------------------------------Featured categories-------------------------------------------
		List<FeaturedCategory> featuredCategories = new ArrayList<FeaturedCategory>();
        for (CategoryModel categoryModel : store.getTabletFeaturedCategories()) {
        	Category cat = Category.wrap(categoryModel);
        	BrowseQuery requestMessage=new BrowseQuery();
        	requestMessage.setCategory(cat.getId());
        	requestMessage.setPage(DEFAULT_PAGE);
        	requestMessage.setMax(DEFAULT_MAX);
        	BrowseResult res = BrowseUtil.getCategories(requestMessage, user, request);
        	FeaturedCategory featured = new FeaturedCategory();
        	cat.setNoOfProducts(res.getProducts().size());
        	featured.setCategory(cat);
        	featured.setProducts(res.getProducts());
        	featuredCategories.add(featured);
		}
        response.setFeaturedCategories(featuredCategories);
        
        
        //-------------------------------------------- END ------------------------------------------------------------------  
        response.setConfiguration(getConfiguration(user));
        return response;
	}
	
    private WebPageResponse getCMSPage(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		WebPageResponse pageResponse = new WebPageResponse();
		CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);
		
		if(pageRequest != null && !pageRequest.isPreview() && pageRequest.getPlantId() == null){
			String plantId= user.getFDSessionUser().getUserContext().getFulfillmentContext().getPlantId();
			pageRequest.setPlantId(plantId);
		}
		
		if(pageRequest != null){
			if(pageRequest.isPreview()){ 
				//Refresh the feed if it is for preview		
				CMSContentFactory cmsContentFactory = new CMSContentFactory();
				List<CMSWebPageModel> pages = cmsContentFactory.getCMSPageByParameters(pageRequest);
				if(pages != null && !pages.isEmpty()){
					pageResponse.setPage(pages.get(0));
				}			
			} else if(pageRequest.getRequestedDate() == null){
				//Refresh the feed if it has the date in the request
				pageResponse.setPage(CMSContentFactory.getInstance().getCMSPageByName(pageRequest.getPageType()));
			} else {
				//Get the feed from cache if it doesn't have request date / if it is not for preview
				List<CMSWebPageModel> pages = CMSContentFactory.getInstance().getCMSPageByParameters(pageRequest);
				if(pages != null && !pages.isEmpty()){
					pageResponse.setPage(pages.get(0));
				}
			}
            if (pageResponse != null && pageResponse.getPage() != null) {
                CMSSectionProductCollectorService.getDefaultService().addProductsToSection(user, pageResponse.getPage());
                pageRequest.limitSections(pageResponse.getPage());
            }
		}
		
		setMediaPath(pageResponse);
        return pageResponse;
	}
	
    private PageMessageResponse getCMSPages(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException, FDException {
        CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);

        if (!pageRequest.isPreview() && pageRequest.getPlantId() == null) {
            pageRequest.setPlantId(BrowseUtil.getPlantId(user));
        }

        PageMessageResponse pageResponse = new PageMessageResponse();
        populateHomePages(user, pageRequest, pageResponse, request);
        return pageResponse;
    }

    private PageMessageResponse getCMSPageComponents(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException, FDException {
        CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);

        if (!pageRequest.isPreview() && pageRequest.getPlantId() == null) {
            pageRequest.setPlantId(BrowseUtil.getPlantId(user));
        }

        PageMessageResponse pageResponse = new PageMessageResponse();
        populatePageComponents(user, pageRequest, pageResponse, request);
        return pageResponse;
    }

	private void setMediaPath(WebPageResponse pageResponse) {
		String mediaPath = MobileApiProperties.getMediaPath();
		String mediaPathFDStore = FDStoreProperties.getMediaPath();
		String mediaStr = "/media/";
		if (pageResponse != null && pageResponse.getPage() != null
				&& pageResponse.getPage().getSections() != null) {
			for (CMSSectionModel section : pageResponse.getPage().getSections()) {
				if (section.getImageBanner() != null
						&& section.getImageBanner().getImage() != null
						&& section.getImageBanner().getImage().getPath() != null) {
					
					if (section.getImageBanner().getImage().getPath()
							.contains(mediaPathFDStore)) {
						section.getImageBanner()
								.getImage()
								.setPath(
										section.getImageBanner()
												.getImage()
												.getPath()
												.substring(
														section.getImageBanner()
																.getImage()
																.getPath()
																.indexOf(
																		mediaStr),
														section.getImageBanner()
																.getImage()
																.getPath()
																.length()));
					}
					
					if(!section.getImageBanner()
							.getImage().getPath().contains(mediaPath)) {
					section.getImageBanner()
							.getImage()
							.setPath(
									mediaPath
											+ section.getImageBanner()
													.getImage().getPath());
					}
				}
				if (section != null && section.getPickList() != null) {
					for (CMSPickListModel picklist : section.getPickList()) {
						if (picklist != null
								&& picklist.getImage() != null
								&& picklist.getImage().getImage() != null
								&& picklist.getImage().getImage().getPath() != null) {
							
							if (picklist.getImage().getImage().getPath()
									.contains(mediaPathFDStore)) {
								section.getImageBanner()
										.getImage()
										.setPath(
												picklist.getImage()
														.getImage()
														.getPath()
														.substring(
																picklist.getImage()
																		.getImage()
																		.getPath()
																		.indexOf(
																				mediaStr),
																				picklist.getImage()
																		.getImage()
																		.getPath()
																		.length()));
							}
							
							if(!picklist.getImage()
									.getImage().getPath().contains(mediaPath)) {
							picklist.getImage()
									.getImage()
									.setPath(
											mediaPath
													+ picklist.getImage()
															.getImage()
															.getPath());
							}
						}
					}
				}
			}
		}
	}
	
    private Message getModule(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
        ModuleContainerResponse messageResponse = new ModuleContainerResponse();

        ModuleContainerRequest messageRequest = parseRequestObject(request, response, ModuleContainerRequest.class);
        String moduleContainerId = messageRequest.getModuleContainerId();

        try {
            ModuleContainerData moduleContainer = ModuleHandlingService.getDefaultService().loadModuleContainer(moduleContainerId, user.getFDSessionUser(), request.getSession());
            Map<String, ModuleData> data = moduleContainer.getData();
            List<ModuleConfig> config = moduleContainer.getConfig();
            messageResponse.setData(data);
            messageResponse.setConfig(config);
            if (data.isEmpty() && config.isEmpty()) {
                messageResponse.addWarningMessage(MessageFormat.format(MODULE_CONTAINER_WARNING_MESSAGE, moduleContainerId));
                messageResponse.setStatus(Message.STATUS_FAILED);
            } else {
                messageResponse.setStatus(Message.STATUS_SUCCESS);
            }
        } catch (InvalidFilteringArgumentException e) {
            String errorMessage = MessageFormat.format(MODULE_CONTAINER_ERROR_MESSAGE, moduleContainerId);
            messageResponse.setFailureMessage(errorMessage);
            LOGGER.error(errorMessage, e);
        } catch (FDResourceException e) {
            String errorMessage = MessageFormat.format(MODULE_CONTAINER_ERROR_MESSAGE, moduleContainerId);
            messageResponse.setFailureMessage(errorMessage);
            LOGGER.error(errorMessage, e);
        } catch (FDNotFoundException e) {
            String errorMessage = MessageFormat.format(MODULE_CONTAINER_ERROR_MESSAGE, moduleContainerId);
            messageResponse.setFailureMessage(errorMessage);
            LOGGER.error(errorMessage, e);
        }

        return messageResponse;
    }

}