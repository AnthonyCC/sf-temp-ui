package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.controller.ProducersController.getProducersIdeaList;
import static com.freshdirect.mobileapi.controller.data.response.Idea.ideaFor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.IdeasResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.storeapi.content.BannerModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.RecipeTagModel;
import com.freshdirect.storeapi.content.StoreModel;

public class IdeasController extends BaseController {

    private static final String ACTION_GET_HOME_CONTENT = "getHomeContent";

	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		if (ACTION_GET_HOME_CONTENT.equals(action)) {
	           StoreModel store = ContentFactory.getInstance().getStore();
	           IdeasResponse result = new IdeasResponse();

	           final BannerModel banner = store.getTabletIdeasBanner();
	           if (banner != null && banner.getImage() != null) {
		           result.setIdeasBanner(new Image(banner.getImage()));
	           }
	           result.setPickLists(pickLists(store));
	           result.setProducers(producers(store));
	           result.setRecipeLists(recipeLists(store));

	           setResponseMessage(model, result, user);
		}
        return model;
	}

	private List<Idea> recipeLists(StoreModel store) {
		ArrayList<Idea> ideas = new ArrayList<Idea>();
		for (ContentNodeModel contentNodeModel : store.getTabletIdeasRecipes()) {
            RecipeTagModel recipeTagModel = new RecipeTagModel(contentNodeModel.getContentKey());
			Idea idea = ideaFor(recipeTagModel, true);
			ideas.add(idea);
		}
		return ideas;
	}

	private List<Idea> producers(StoreModel store) {
		final List<Idea> producersIdeaList = getProducersIdeaList();
		List<Idea> ideas = new ArrayList<Idea>();
		for (Idea idea : producersIdeaList) {
			if (idea.getFeatureImage() != null)
				ideas.add(idea);
		}
		return ideas;
	}

	private List<Idea> pickLists(StoreModel store) {
		List<Idea> ideas = new ArrayList<Idea>();
		for (CategoryModel categoryModel : store.getTabletIdeasFeaturedPicksLists()) {
			Idea idea = ideaFor(categoryModel, Idea.ThumbnailType.Banner);
			ideas.add(idea);
		}
		return ideas;
	}

	@Override
	protected boolean validateUser() {
		return false; // guest browsing for Ideas
	}

}
