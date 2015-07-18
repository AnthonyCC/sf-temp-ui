package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.controller.data.response.Idea.ideaFor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.util.PublishId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.CMSContentFactory;
import com.freshdirect.fdstore.content.CMSPageRequest;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.Buildver;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.Configuration;
import com.freshdirect.mobileapi.controller.data.response.FeaturedCategoriesResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeGetAllResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeResponse;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.WebPageResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.FeaturedCategory;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.BrowseUtil;

public class HomeController extends BaseController {

	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_FEATURED_CATEGORIES = "getFeaturedCategories";
	private static final String ACTION_GET_All_DETAILS = "getAllDetails";
	private static final String ACTION_GET_CMS_PAGE = "getPage";
	public static final Integer DEFAULT_PAGE = 1;
	public static final Integer DEFAULT_MAX = 998;	
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
		} else if (ACTION_GET_CMS_PAGE.equals(action)){
			return getCMSPage(model, user, request, response);
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

	@Override
	protected boolean validateUser() {
		return false;
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
		BrowseUtil browse = new BrowseUtil();
		
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
        	requestMessage.setPage(1);
        	requestMessage.setMax(DEFAULT_MAX);
        	//BrowseResult res = new BrowseResult();
        	BrowseResult res=browse.getCategories(requestMessage, user, request);
        	FeaturedCategory featured = new FeaturedCategory();
        	cat.setNoOfProducts(res.getProducts().size());
        	featured.setCategory(cat);
        	featured.setProducts(res.getProducts());
        	featuredCategories.add(featured);
		}
        response.setFeaturedCategories(featuredCategories);
        
        
        //-------------------------------------------- END ------------------------------------------------------------------  
        Configuration configuration = new Configuration();
        configuration.setAkamaiImageConvertorEnabled( FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.akamaiimageconvertor, user.getFDSessionUser().getUser()));
        configuration.setApiCodeVersion(Buildver.getInstance().getBuildver());
        configuration.setStoreVersion(PublishId.getInstance().getPublishId());
        response.setConfiguration(configuration);
		
		// Add all these to the response and send back the model.
        setResponseMessage(model, response, user);
		return model;
	}
	
	private ModelAndView getCMSPage(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		WebPageResponse pageResponse = new WebPageResponse();
		CMSPageRequest pageRequest = parseRequestObject(request, response, CMSPageRequest.class);
		if(pageRequest != null){
			if(pageRequest.getDate() == null){
				pageResponse.setPage(CMSContentFactory.getInstance().getCMSPageByName(pageRequest.getPageName()));
			} else {
				List<CMSWebPageModel> pages = CMSContentFactory.getInstance().getCMSPageByParameters(pageRequest);
				if(pages != null && !pages.isEmpty()){
					pageResponse.setPage(pages.get(0));
				}
			}
		}
		setResponseMessage(model,pageResponse,user);
		return model;
	}	
}