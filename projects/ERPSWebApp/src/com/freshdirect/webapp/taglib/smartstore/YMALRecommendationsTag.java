package com.freshdirect.webapp.taglib.smartstore;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.YmalSet;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * SmartStore YMAL Recommendations Tag
 * 
 * @author csongor
 * 
 */
public class YMALRecommendationsTag extends RecommendationsTag implements SessionName {
	private static final long serialVersionUID = 5976696010559642821L;
	
	private YmalSource source = null;
	
	public YMALRecommendationsTag() {
		super();
		itemCount = 6;
	}

    public void setSource(YmalSource source) {
		this.source = source;
	}

    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
        Recommendations recommendations = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        if (errorOccurred) {
            String recsCachedId = request.getParameter("recs_cached_id");
            if (recsCachedId != null && recsCachedId.equals(request.getSession().getAttribute("recs_cached_id"))) {
                recommendations = (Recommendations) request.getSession().getAttribute("recs_cached");
            }
        }

        // get recommendations by recommender
        if (recommendations == null) {
            recommendations = extractRecommendations();
            request.getSession().setAttribute("recs_cached_id", recommendations.getRequestId());
            request.getSession().setAttribute("recs_cached", recommendations);
        }

        setupRequest(request, recommendations);
        
        return recommendations;
    }

    /**
     * @param request
     * @param recommendations
     */
    private void setupRequest(HttpServletRequest request, Recommendations recommendations) {
        // setup the request with putting lots of result in to various attributes.
        List<ProductModel> relatedProducts = recommendations.getProducts();
        List<Recipe> relatedRecipes = source.getYmalRecipes();
        List<CategoryModel> relatedCategories = source.getYmalCategories();
        YmalSet ymal_aset = source.getActiveYmalSet();
        String ymalHeader = source.getYmalHeader();
        FDUserI ymal_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);        
        ProductModel ymal_product = source instanceof ProductModel ? (ProductModel) source : YmalUtil.getSelectedCartLine(ymal_user).lookupProduct();

        // -- AVAILABILITY CHECK --
        // remove unavailable recipes
        for (Iterator it = relatedRecipes.iterator(); it.hasNext();) {
                Recipe rec = (Recipe) it.next();
                if (!rec.isAvailable()) {
                        it.remove();
                }
        }

        // sort out hidden categories
        for (Iterator<CategoryModel> it = relatedCategories.iterator(); it.hasNext();) {
                CategoryModel c = (CategoryModel) it.next();
                if (c.isHidden()) {
                        it.remove();
                }
        }

        // Pass parameters to YMAL display box
        request.setAttribute("ymal_products", relatedProducts);
        request.setAttribute("ymal_categories", relatedCategories);
        request.setAttribute("ymal_recipes", relatedRecipes);
        request.setAttribute("ymal_aset", ymal_aset);
        request.setAttribute("ymal_product", ymal_product);
        
        ActionResult ymal_result = (ActionResult) request.getAttribute("actionResult");
        request.setAttribute("ymal_result", ymal_result);
        
        if (recommendations.getVariant() != null && recommendations.getVariant().getServiceConfig().getPresentationDescription() != null) {
            request.setAttribute("ymal_header", recommendations.getVariant().getServiceConfig().getPresentationDescription());
        } else {
            // Special case: Don't pass header if there are related products
            if (ymalHeader != null && source.getRelatedProducts().size() == 0) {
                request.setAttribute("ymal_header", ymalHeader);
            }
        }

        request.setAttribute("ymal_variant", recommendations.getVariant());
        request.setAttribute("recommendations", recommendations);
    }

    private Recommendations extractRecommendations() throws FDResourceException {
        Recommendations results;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        // setup an input
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        SessionInput inp = new SessionInput(user);
        initFromSession(inp);
        if (source != null) {
            inp.setYmalSource(source);
            if (source instanceof ProductModel) {
                inp.setCurrentNode(source);
            }
        } else {
            inp.setYmalSource(YmalUtil.resolveYmalSource(user, request));
        }

        if (inp.getCurrentNode() == null) {
            inp.setCurrentNode(YmalUtil.getSelectedCartLine(user).lookupProduct());
        }

        inp.setMaxRecommendations(itemCount);

        results = FDStoreRecommender.getInstance().getRecommendations(EnumSiteFeature.YMAL, user, inp);
        persistToSession(results);
        collectRequestId(request, results, user);
        return results;
    }

	public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
