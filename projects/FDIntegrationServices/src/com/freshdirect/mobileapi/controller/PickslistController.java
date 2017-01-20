package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.controller.data.response.Idea.ideaFor;
import static com.freshdirect.mobileapi.controller.data.response.Idea.ThumbnailType.TabletThumbnail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CMSImageBannerModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ImageBanner;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.PicksListDetailResponse;
import com.freshdirect.mobileapi.controller.data.response.PicksListResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

public class PickslistController extends BaseController {

	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_ALL_IPHONE = "getAlliPhone";
	private static final String ACTION_GET_ALL_IPHONE_IMAGE_BANNERS = "getAlliPhoneImageBanners";
	private static final String ACTION_GET_DETAIL = "getDetail";

	@Override
	protected ModelAndView processRequest(final HttpServletRequest request,
			final HttpServletResponse response, final ModelAndView model, final String action,
			final SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		if (ACTION_GET_ALL.equals(action)) {
			return getAll(model, user);
		}else if (ACTION_GET_ALL_IPHONE.equals(action)) {
			return getAlliPhone(model, user);
		}else if (ACTION_GET_DETAIL.equals(action)) {
			return getDetail(request, model, user);
		}
		else if (ACTION_GET_ALL_IPHONE_IMAGE_BANNERS.equals(action)) {
			return getAlliPhoneImageBanners(model, user);
		}
		throw new UnsupportedOperationException();
	}

	private ModelAndView getDetail(final HttpServletRequest request,
			final ModelAndView model, final SessionUser user) throws JsonException {
		// TODO Auto-generated method stub
		final String pickslistId = request.getParameter("pickslistId");
		final ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(pickslistId);
		if (currentFolder instanceof CategoryModel) {
			final CategoryModel category = (CategoryModel) currentFolder;
			final List<ProductModel> productModels = category.getProducts();

			final PicksListDetailResponse response = new PicksListDetailResponse();
			response.setTitle(category.getFullName());
			if (category.getDescription() != null)
				response.setDescription(category.getDescription().getPath());
			response.setImage(new Image(category.getCategoryDetailImage()));
			final List<Product> products = new ArrayList<Product>(productModels.size());
			for (final ProductModel productModel : productModels) {
				try {
					products.add(new Product(com.freshdirect.mobileapi.model.Product.wrap(productModel)));
				} catch (final ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			response.setProducts(products);
			setResponseMessage(model, response, user);
		}
		return model;
	}

	private ModelAndView getAll(final ModelAndView model, final SessionUser user) throws JsonException {
		final StoreModel store = ContentFactory.getInstance().getStore();
		final List<Idea> picksList = new ArrayList<Idea>();
		for (final CategoryModel categoryModel : store.getTabletIdeasFeaturedPicksLists()) {
			picksList.add(ideaFor(categoryModel, Idea.ThumbnailType.TabletThumbnail));
		}
		final PicksListResponse response = new PicksListResponse();
		response.setPicksList(picksList);
		setResponseMessage(model, response, user);
		return model;
	}

	private ModelAndView getAlliPhone(final ModelAndView model, final SessionUser user) throws JsonException {
		final StoreModel store = ContentFactory.getInstance().getStore();
		final List<Idea> picksList = new ArrayList<Idea>();
		for (final CategoryModel categoryModel : store.getiPhoneHomePagePicksLists()) {
			picksList.add(ideaFor(categoryModel, Idea.ThumbnailType.TabletThumbnail));
		}
		final PicksListResponse response = new PicksListResponse();
		response.setPicksList(picksList);
		setResponseMessage(model, response, user);
		return model;
	}
	
	private ModelAndView getAlliPhoneImageBanners(final ModelAndView model, final SessionUser user) throws JsonException {
		final StoreModel store = ContentFactory.getInstance().getStore();
		List<CMSImageBannerModel> cmsImageBanners = new ArrayList<CMSImageBannerModel>();
        List<ImageBanner> imageBanners = store.getiPhoneHomePageImageBanners();
        if (imageBanners != null) {
            for (ImageBanner imageBanner : imageBanners) {
                CMSImageBannerModel cmsImageBanner = convertImageBanner(imageBanner);
                if (cmsImageBanner != null){
                    cmsImageBanners.add(cmsImageBanner);
                }
            }
        }
		final PicksListResponse response = new PicksListResponse();
		response.setiPhoneHomePageImageBanners(cmsImageBanners);
		setResponseMessage(model, response, user);
		return model;
	}
	
	@Override
	protected boolean validateUser() {
		return false;
	}

}
