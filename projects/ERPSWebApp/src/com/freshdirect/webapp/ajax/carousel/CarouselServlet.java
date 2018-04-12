package com.freshdirect.webapp.ajax.carousel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CarouselDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCrazyQuickshopRecommendationService;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.CheckoutCarouselService;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class CarouselServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 7146728571778380697L;
	private static final Logger LOGGER = LoggerFactory.getInstance(CarouselServlet.class);
    private static final String NEW_PRODUCTS_CAROUSEL_NAME = "New Products";

	@Override
	// For OAuth2 token endpoint, GET is disabled
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI u) throws HttpErrorResponse {

		String type = request.getParameter("type");
		HttpSession session = request.getSession();
		FDSessionUser sessionUser = (FDSessionUser) QuickShopHelper.getUserFromSession(session);

		if (type == null || type.isEmpty()) {
			ViewCartCarouselData carousels = getQuickShopCarousel(request, sessionUser);
			writeResponseData(response, carousels);

		} else if (type.equals("checkout")) {
			ViewCartCarouselData carousels = getCheckoutCarousel(request, sessionUser);
			writeResponseData(response, carousels);

		} else if (type.equals("cart")) {
			ViewCartCarouselData carousels = getViewCartCarousel(request, sessionUser);
			writeResponseData(response, carousels);

		} else if (type.equals("pres-picks")) {
			CarouselDataCointainer carouselData = getPresPickCarousel(sessionUser);
			writeResponseData(response, carouselData);

		} else if (type.equals("search")) {
			CarouselData carouselData = getSearchCarousel(request, sessionUser);
			writeResponseData(response, carouselData);

		} else if (type.equals("ymal") || type.equals("deals")) {
			Map<String, ?> dataMap = getProductCarousel(request, session, sessionUser);
			writeResponseData(response, dataMap);

		} else {
			LOGGER.error("unsupported carousel type " + type);
			writeResponseData(response, null);
		}
	}

	private Map<String, ?> getProductCarousel(HttpServletRequest request, HttpSession session,
			FDSessionUser sessionUser) {
		String currentNodeKey = request.getParameter("currentNodeKey");
		String siteFeature = request.getParameter("siteFeature");
		String cmEventSource = request.getParameter("cmEventSource");
		boolean sendVariant = request.getParameter("sendVariant") != null
				? Boolean.parseBoolean(request.getParameter("sendVariant"))
				: false;
		List<ProductModel> products = new ArrayList<ProductModel>();
		Recommendations results = null;
		try {

			int maxItems = request.getParameter("maxItems") != null ? Integer.parseInt(request.getParameter("maxItems"))
					: 0;
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();

			ContentNodeModel currentNode = null;
			if (currentNodeKey != null && currentNodeKey.length() != 0) {
				currentNode = ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(currentNodeKey));
			}

			results = recommender.getRecommendations(EnumSiteFeature.getEnum(siteFeature), sessionUser,
					ProductRecommenderUtil.createSessionInput(session, sessionUser, maxItems, currentNode, null));

			ProductRecommenderUtil.persistToSession(session, results);

			products = results.getAllProducts();

			if (products.size() > maxItems) {
				products = products.subList(0, maxItems);
			}

		} catch (FDResourceException e) {
			LOGGER.warn("Failed to get recommendations for siteFeature:" + siteFeature, e);
		}

		Map<String, ?> dataMap = null;

		if (sendVariant && results != null) {
			dataMap = DataPotatoField.digProductListFromModels(sessionUser, products, results.getVariant().getId());
		} else {
			dataMap = DataPotatoField.digProductListFromModels(sessionUser, products);
		}

		if (cmEventSource != null) {
			((Map<String, Object>) dataMap).put("cmEventSource", cmEventSource);
		}
		return dataMap;
	}

	private CarouselData getSearchCarousel(HttpServletRequest request, FDSessionUser sessionUser) {
		String productId = request.getParameter("productId");
		CarouselData carouselData = null;
		try {
			if (null != sessionUser) {
				Recommendations recommendations = ProductRecommenderUtil.getSearchPageRecommendations(sessionUser,
						productId);
				if (recommendations != null && recommendations.getAllProducts().size() > 0) {
					carouselData = CarouselService.defaultService().createCarouselData(null, "You Might Also Like",
							recommendations.getAllProducts(), sessionUser, "", recommendations.getVariant().getId());

				}
			}

		} catch (Exception e) {
			LOGGER.error("search recommendation failed", e);
		}
		return carouselData;
	}

	private CarouselDataCointainer getPresPickCarousel(FDSessionUser sessionUser) {
		CarouselDataCointainer carouselData = new CarouselDataCointainer();

        final boolean isNewProductsCarouselEnabled = FDStoreProperties.isFreshDealsPageNewProductsCarouselEnabled();
        if (isNewProductsCarouselEnabled) {
            carouselData.setCarousel1(CarouselService.defaultService().createNewProductsCarousel(sessionUser,
                    FDStoreProperties.isFreshDealsPageNewProductsCarouselRandomizeProductOrderEnabled()));
        }

		try {
            SessionInput si = new SessionInput(sessionUser);

            if (carouselData.getCarousel1() == null) {
                si.setCurrentNode(ContentFactory.getInstance().getContentNode("gro"));
                Recommendations groRecommendations = ProductRecommenderUtil.doRecommend(sessionUser, EnumSiteFeature.BRAND_NAME_DEALS, si);

                if (groRecommendations != null && groRecommendations.getAllProducts().size() > 0) {
                    carouselData.setCarousel1(CarouselService.defaultService().createCarouselData(null, groRecommendations.getVariant().getServiceConfig().get("prez_title_gro"),
                            groRecommendations.getAllProducts(), sessionUser, "", groRecommendations.getVariant().getId()));
                }

            }
            si.setCurrentNode(ContentFactory.getInstance().getContentNode("fro"));
			Recommendations froRecommendations = ProductRecommenderUtil.doRecommend(sessionUser,
					EnumSiteFeature.BRAND_NAME_DEALS, si);
			if (froRecommendations != null && froRecommendations.getAllProducts().size() > 0) {
				carouselData.setCarousel2(CarouselService.defaultService().createCarouselData(null,
						froRecommendations.getVariant().getServiceConfig().get("prez_title_fro"),
						froRecommendations.getAllProducts(), sessionUser, "", froRecommendations.getVariant().getId()));
			}
			si.setCurrentNode(ContentFactory.getInstance().getContentNode("dai"));
			Recommendations daiRecommendations = ProductRecommenderUtil.doRecommend(sessionUser,
					EnumSiteFeature.BRAND_NAME_DEALS, si);
			if (daiRecommendations != null && daiRecommendations.getAllProducts().size() > 0) {
				carouselData.setCarousel3(CarouselService.defaultService().createCarouselData(null,
						daiRecommendations.getVariant().getServiceConfig().get("prez_title_dai"),
						daiRecommendations.getAllProducts(), sessionUser, "", daiRecommendations.getVariant().getId()));
			}
		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		return carouselData;
	}

    private ViewCartCarouselData getViewCartCarousel(HttpServletRequest request, FDSessionUser sessionUser) {
		ViewCartCarouselData carousels = null;
		try {
			SessionInput input;
			input = ViewCartCarouselService.getDefaultService().createSessionInput(sessionUser, request);
			input.setError(request.getParameter("warning_message") != null);
			carousels = ViewCartCarouselService.getDefaultService().populateTabsRecommendationsAndCarousel(request,
					sessionUser, input);

		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		return carousels;
	}

	private ViewCartCarouselData getCheckoutCarousel(HttpServletRequest request, FDSessionUser sessionUser) {
		ViewCartCarouselData carousels = null;
		try {
			SessionInput input;

			input = QuickShopCarouselService.defaultService().createSessionInput(sessionUser, request);
			carousels = CheckoutCarouselService.getDefaultService().populateTabsRecommendationsAndCarousel(request,
					sessionUser, input);

		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		return carousels;
	}

	private ViewCartCarouselData getQuickShopCarousel(HttpServletRequest request, FDSessionUser sessionUser) {
		ViewCartCarouselData carousels = null;
		try {

			SessionInput input = QuickShopCarouselService.defaultService().createSessionInput(sessionUser, request);
			carousels = QuickShopCarouselService.defaultService().populateTabsRecommendations(request, sessionUser,
					input);
			carousels.getRecommendationTabs().add(0,
					new RecommendationTab(
							QuickShopCrazyQuickshopRecommendationService.defaultService()
									.getTheCrazyQuickshopTitle(null),
							QuickShopCrazyQuickshopRecommendationService.QUICKSHOP_VIRTUAL_SITE_FEATURE));

            final boolean isNewProductsCarouselEnabled = FDStoreProperties.isReorderPageNewProductsCarouselEnabled();
            if (isNewProductsCarouselEnabled) {
                RecommendationTab recommendationTab = new RecommendationTab(NEW_PRODUCTS_CAROUSEL_NAME, null);
                recommendationTab.setCarouselData(CarouselService.defaultService().createNewProductsCarousel(sessionUser,
                        FDStoreProperties.isReorderPageNewProductsCarouselRandomizeProductOrderEnabled()));
                carousels.getRecommendationTabs().add(0, recommendationTab);
            }

			boolean isFirstTab = true;
			for (RecommendationTab tab : carousels.getRecommendationTabs()) {
				tab.setSelected(isFirstTab);
				isFirstTab = false;
			}

		} catch (Exception e) {
			LOGGER.error("recommendation failed", e);
		}
		return carousels;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
	
	@Override 
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}
}
