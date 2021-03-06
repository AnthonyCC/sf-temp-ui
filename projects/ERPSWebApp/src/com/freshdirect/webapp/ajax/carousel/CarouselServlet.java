package com.freshdirect.webapp.ajax.carousel;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.freshdirect.framework.util.NVL;
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

	@Override
	// For OAuth2 token endpoint, GET is disabled
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        String type = request.getParameter("type");
        CarouselType carouselType = CarouselType.fromString(type);

        HttpSession session = request.getSession();
        FDSessionUser sessionUser = (FDSessionUser) user;

        Object carousel = null;

        switch (carouselType) {
            case QUICKSHOP:
                carousel = getQuickShopCarousel(request, sessionUser);
                break;

            case CHECKOUT:
                carousel = getCheckoutCarousel(request, sessionUser);
                break;

            case CART:
                carousel = getViewCartCarousel(request, sessionUser);
                break;

            case PRESIDENT_PICKS: {
                Map<String, Object> carousels = new HashMap<String, Object>();
                carousels.put("carousels", getPresPickCarousel(sessionUser));
                carousel = carousels;
            }
                break;

            case SEARCH: {
                Map<String, Object> carousels = new HashMap<String, Object>();
                carousels.put("carousels", getSearchCarousel(request, sessionUser));
                carousel = carousels;
            }
                break;

            case YMAL:
                // Fall-through
            case DEALS:
                carousel = getProductCarousel(request, session, sessionUser);
                break;

            default:
                LOGGER.error("unsupported carousel type " + type);
                break;
        }

        writeResponseData(response, carousel);
    }

    private Map<String, ?> getProductCarousel(HttpServletRequest request, HttpSession session,
			FDSessionUser sessionUser) {
		String currentNodeKey = request.getParameter("currentNodeKey");
		String siteFeature = request.getParameter("siteFeature");
		String eventSource = request.getParameter("eventSource");
        String type = request.getParameter("type");
        Boolean sendVariant = Boolean.parseBoolean(NVL.apply(request.getParameter("sendVariant"), "false"));
        Integer maxItems = Integer.parseInt(NVL.apply(request.getParameter("maxItems"), "0"));

        List<ProductModel> products = new ArrayList<ProductModel>();
		Recommendations results = null;

        boolean isNewProductsCarouselLoaded = false;

        if ("deals".equals(type) && FDStoreProperties.isCartConfirmPageNewProductsCarouselEnabled()) {
            products = CarouselService.defaultService().collectNewProducts(FDStoreProperties.isCartConfirmPageNewProductsCarouselRandomizeProductOrderEnabled());
            if (products.size() >= FDStoreProperties.getMinimumItemsCountInCarousel()) {
                isNewProductsCarouselLoaded = true;
                siteFeature = CarouselService.NEW_PRODUCTS_CAROUSEL_VIRTUAL_SITE_FEATURE;
            }
        }

        if (!isNewProductsCarouselLoaded) {
            try {
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
        }

		Map<String, ?> dataMap = null;

		if (sendVariant && results != null) {
			dataMap = DataPotatoField.digProductListFromModels(sessionUser, products, results.getVariant().getId());
		} else {
			dataMap = DataPotatoField.digProductListFromModels(sessionUser, products);
		}

		if (eventSource != null) {
			((Map<String, Object>) dataMap).put("eventSource", eventSource);
		}

        ((Map<String, Object>) dataMap).put("isNewProductsCarouselLoaded", isNewProductsCarouselLoaded);
		return dataMap;
	}

    private CarouselDataCointainer getSearchCarousel(HttpServletRequest request, FDSessionUser sessionUser) {
        CarouselDataCointainer carouselData = new CarouselDataCointainer();
        String productId = request.getParameter("productId");

        try {
			if (null != sessionUser) {
				Recommendations recommendations = ProductRecommenderUtil.getSearchPageRecommendations(sessionUser,
						productId);
				if (recommendations != null && recommendations.getAllProducts().size() > 0) {
                    carouselData.setCarousel1(CarouselService.defaultService().createCarouselData(null, "You Might Also Like", recommendations.getAllProducts(), sessionUser, "",
                            recommendations.getVariant().getId()));

				}
			}

		} catch (Exception e) {
			LOGGER.error("search recommendation failed", e);
		}
		return carouselData;
	}

	private CarouselDataCointainer getPresPickCarousel(FDSessionUser sessionUser) {
		CarouselDataCointainer carouselData = new CarouselDataCointainer();

        if (FDStoreProperties.isFreshDealsPageNewProductsCarouselEnabled()) {
            List<ProductModel> newProducts = CarouselService.defaultService()
                    .collectNewProducts(FDStoreProperties.isFreshDealsPageNewProductsCarouselRandomizeProductOrderEnabled());
            carouselData.setCarousel1(CarouselService.defaultService().createCarouselDataWithMinProductLimit(null, CarouselService.NEW_PRODUCTS_CAROUSEL_NAME.toUpperCase(),
                    newProducts, sessionUser, null, null));
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
            carousels = QuickShopCarouselService.defaultService().populateTabsRecommendations(request, sessionUser, input);
            carousels.getRecommendationTabs().add(0, new RecommendationTab(QuickShopCrazyQuickshopRecommendationService.defaultService().getTheCrazyQuickshopTitle(null),
                    QuickShopCrazyQuickshopRecommendationService.QUICKSHOP_VIRTUAL_SITE_FEATURE));

            if (FDStoreProperties.isReorderPageNewProductsCarouselEnabled()) {
                List<ProductModel> newProducts = CarouselService.defaultService()
                        .collectNewProducts(FDStoreProperties.isReorderPageNewProductsCarouselRandomizeProductOrderEnabled());
                CarouselData carouselData = CarouselService.defaultService().createCarouselDataWithMinProductLimit(null, CarouselService.NEW_PRODUCTS_CAROUSEL_NAME,
                        newProducts, sessionUser, null, null);
                if (carouselData != null) {
                    RecommendationTab recommendationTab = new RecommendationTab(CarouselService.NEW_PRODUCTS_CAROUSEL_NAME,
                            CarouselService.NEW_PRODUCTS_CAROUSEL_VIRTUAL_SITE_FEATURE);
                    carousels.getRecommendationTabs().add(0, recommendationTab);
                }
            }

            boolean isFirstTab = true;
            for (RecommendationTab tab : carousels.getRecommendationTabs()) {
                tab.setSelected(isFirstTab);
                isFirstTab = false;
            }

        } catch (FDResourceException e) {
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
