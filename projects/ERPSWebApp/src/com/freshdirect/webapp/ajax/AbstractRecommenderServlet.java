package com.freshdirect.webapp.ajax;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.smartstore.Impression;

/**
 * Abstract class that supplies recommendations for tabbed carousels
 * 
 * @author segabor
 *
 */
public abstract class AbstractRecommenderServlet extends BaseJsonServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		RecommendationRequestObject requestData = parseRequestData(request, RecommendationRequestObject.class, true);
		HttpSession session = request.getSession();
		String siteFeature = requestData.getFeature();
		EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
		Variant variant = VariantSelectorFactory.getSelector(enumSiteFeature).select(user, false);

		final String parentImpressionId = requestData.getParentImpressionId();
		final String impressionId = requestData.getImpressionId();
		final String parentVariantId = requestData.getParentVariantId();

		// track click-thru event
		if (impressionId != null) {
			Impression.tabClick(impressionId);
		}

		try {
			// output structure
			ViewCartCarouselData viewCartCarouselData = new ViewCartCarouselData();

			final String titleForVariant = ViewCartCarouselService.defaultService().getTitleForVariant(variant);
			RecommendationTab recommendationTab = new RecommendationTab(titleForVariant, enumSiteFeature.getName(), parentImpressionId, impressionId, parentVariantId);
			viewCartCarouselData.getRecommendationTabs().add(recommendationTab);

			// populate recommendation tab
			doRecommendation(request, (FDSessionUser)user, session, variant,
					parentImpressionId, parentVariantId, recommendationTab);

			if (recommendationTab.getCarouselData() == null) {
				// no recommendation happened
				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> recommenderResult = new HashMap<String, Object>();
				recommenderResult.put("siteFeature", siteFeature);
				recommenderResult.put("items", Collections.EMPTY_LIST);
				result.put("recommenderResult", recommenderResult);
				
				return;
			}
			
			// populate recommendations
			List<ProductData> recommendations = recommendationTab.getCarouselData().getProducts();
			if (recommendations.isEmpty()) {
				writeResponseData(response, "No recommendations found.");
			} else {
				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> recommenderResult = new HashMap<String, Object>();
				recommenderResult.put("siteFeature", siteFeature);
				recommenderResult.put("items", recommendations);
				result.put("recommenderResult", recommenderResult);

				writeResponseData(response, result);
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			returnHttpError(500, "Cannot collect recommendations. exception:" + e);
		}
	}


	/**
	 * Subclasses must override this method.
	 * 
	 * Do something here like this
	 * 		CarouselData carousel = <...>; // populate carousel data here!
	 *		recommendationTab.setCarouselData(carousel);
	 * 
	 * @param request
	 * @param user
	 * @param session
	 * @param variant
	 * @param parentImpressionId
	 * @param parentVariantId
	 * @param recommendationTab
	 * 
	 * @throws FDResourceException
	 * @throws InvalidContentKeyException
	 */
	protected abstract void doRecommendation(HttpServletRequest request,
			FDSessionUser user, HttpSession session, Variant variant,
			final String parentImpressionId, final String parentVariantId,
			RecommendationTab recommendationTab) throws FDResourceException,
			InvalidContentKeyException;
}
