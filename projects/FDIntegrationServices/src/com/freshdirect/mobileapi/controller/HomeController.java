package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.controller.data.response.Idea.ideaFor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.CMSPageRequest;
import com.freshdirect.fdstore.content.CMSPickListModel;
import com.freshdirect.fdstore.content.CMSSectionModel;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.FeaturedCategoriesResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeGetAllResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeResponse;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.PageMessageResponse;
import com.freshdirect.mobileapi.controller.data.response.WebPageResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.FeaturedCategory;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.wcms.CMSContentFactory;

public class HomeController extends BaseController {

    private static Logger LOGGER = LoggerFactory.getInstance(HomeController.class);
    
	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_FEATURED_CATEGORIES = "getFeaturedCategories";
	private static final String ACTION_GET_All_DETAILS = "getAllDetails";
	private static final String ACTION_GET_CMS_PAGE = "getPage";
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_MAX = 998;

	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException {
		
		if (ACTION_GET_ALL.equals(action)) {
			return all(model, user);
		} else if (ACTION_GET_FEATURED_CATEGORIES.equals(action)) {
			return featuredCategories(model, user);
		} else if(ACTION_GET_All_DETAILS.equals(action)){
			return getAllDetails(model, user, request);
        } else if (ACTION_GET_CMS_PAGE.equals(action)) {
            if (isCheckLoginStatusEnable(request)) {
                return getCMSPages(model, user, request, response);
            } else {
                return getCMSPage(model, user, request, response);
            }
        }
		throw new UnsupportedOperationException();
	}

	private ModelAndView featuredCategories(ModelAndView model, SessionUser user) throws JsonException {
		FeaturedCategoriesResponse result = new FeaturedCategoriesResponse();
        StoreModel store = ContentFactory.getInstance().getStore();
        List<Category> categories = new ArrayList<Category>();
        for (CategoryModel categoryModel : store.getTabletFeaturedCategories()) {
			categories.add(Category.wrap(categoryModel));
		}
        result.setFeaturedCategories(categories);
//        result.setHomescreenBanner(homescreenBanner);
		setResponseMessage(model, result, user);
		return model;
	}

	private ModelAndView all(ModelAndView model, SessionUser user) throws JsonException {
        HomeGetAllResponse response = new HomeGetAllResponse();
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
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
		
        setResponseMessage(model, response, user);
        return model;
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
	private ModelAndView getAllDetails(ModelAndView model, SessionUser user, HttpServletRequest request) throws JsonException, FDException{
		
		HomeResponse response = new HomeResponse();
		
		if (user == null) {
    		user = fakeUser(request.getSession());
    	}
		
		// get the carousel Items and Banner for Mobile home page.
		//----------------------------------------CarouselItems---------------------------------------------------------
		Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
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
		
		// Add all these to the response and send back the model.
        setResponseMessage(model, response, user);
		return model;
	}
	
	private ModelAndView getCMSPage(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		WebPageResponse pageResponse = new WebPageResponse();
		CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);
		
		if(!pageRequest.isPreview() && pageRequest.getPlantId() == null){
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
		}
		
		setMediaPath(pageResponse);
		setResponseMessage(model,pageResponse,user);
		return model;
	}
	
    private ModelAndView getCMSPages(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException, FDException {
        CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);

        if (!pageRequest.isPreview() && pageRequest.getPlantId() == null) {
            pageRequest.setPlantId(BrowseUtil.getPlantId(user));
        }

        PageMessageResponse pageResponse = new PageMessageResponse();
        populateHomePages(user, pageRequest, pageResponse, request);

        setResponseMessage(model, pageResponse, user);
        return model;
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
	
}