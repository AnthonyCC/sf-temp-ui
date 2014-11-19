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
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.mobileapi.controller.data.response.FeaturedCategoriesResponse;
import com.freshdirect.mobileapi.controller.data.response.HomeGetAllResponse;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.SessionUser;

public class HomeController extends BaseController {

	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_FEATURED_CATEGORIES = "getFeaturedCategories";
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException {
		
		if (ACTION_GET_ALL.equals(action)) {
			return all(model, user);
		} else if (ACTION_GET_FEATURED_CATEGORIES.equals(action)) {
			return featuredCategories(model, user);
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
}
