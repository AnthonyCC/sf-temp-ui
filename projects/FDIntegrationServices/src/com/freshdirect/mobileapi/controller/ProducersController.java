package com.freshdirect.mobileapi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProducerModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.ProducerDetailResponse;
import com.freshdirect.mobileapi.controller.data.response.ProducerListResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.mobileapi.util.StringUtil;

public class ProducersController extends BaseController {

	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_DETAIL = "getDetail";
	
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		if (ACTION_GET_ALL.equals(action)) {
			return getAll(model, user);
		} else if (ACTION_GET_DETAIL.equals(action)) {
			return getDetail(request, model, user);
		}
		throw new UnsupportedOperationException();
	}

	private ModelAndView getDetail(HttpServletRequest request,
			ModelAndView model, SessionUser user) throws JsonException {
		String producerId = request.getParameter("producerId");
		if (StringUtil.isEmpty(producerId)) return model; 
		ProducerDetailResponse response = new ProducerDetailResponse();
		
		BrandModel brand = null;
		
		for (BrandModel brandModel : ContentFactory.getInstance().getStore().getTabletIdeasBrands()) {
			if (brandModel.getContentName().equals(producerId)) {
				brand = brandModel;
				break;
			}
		}
		
		if (brand == null) return model;
		response.setProducerTitle(brand.getFullName());

		
		// JIRA FD-iPadFDIP-651 -- Michael Cress
//*********************************************************************************************************
//		if ( brand.getTabletAboutTextLong() != null)
//			response.setProducerText(brand.getTabletAboutTextLong().getPath());
		if ( brand != null && brand.getTabletAboutTextLong() != null)
		{
			//old code
			//response.setProducerText(brand.getTabletAboutTextLong().getPath());
			
			//Read file from path
			StringBuilder path = new StringBuilder();
			StringBuffer responseStr = new StringBuffer();
			path.append(MobileApiProperties.getMediaPath());
			path.append( brand.getTabletAboutTextLong().getPath() );
			if( path != null ) 	{

				response.setProducerText( path.toString() );
			}
		}
//*********************************************************************************************************
		
		final com.freshdirect.fdstore.content.Image tabletHeader = brand.getTabletHeader();
		if (tabletHeader != null)
			response.setProducerBanner(new Image(tabletHeader));
		List<Image> producerImages = new ArrayList<Image>();
		for (com.freshdirect.fdstore.content.Image image : new ArrayList<com.freshdirect.fdstore.content.Image>(brand.getTabletImages())) {
			producerImages.add(new Image(image));
		}

		// temporary put all brand images into a response
        final com.freshdirect.fdstore.content.Image chefImage = brand.getChefImage();
        final com.freshdirect.fdstore.content.Image logoLarge = brand.getLogoLarge();
        final com.freshdirect.fdstore.content.Image logoMedium = brand.getLogoMedium();
        final com.freshdirect.fdstore.content.Image logoSmall = brand.getLogoSmall();
        final com.freshdirect.fdstore.content.Image sideNavImage = brand.getSideNavImage();
        final com.freshdirect.fdstore.content.Image tabletThumbnailImage = brand.getTabletThumbnailImage();
        Map<String, Image> otherImages = new LinkedHashMap<String, Image>();
        otherImages.put("chefImage", chefImage == null ? null : new Image(chefImage));
        otherImages.put("logoLarge", logoLarge == null ? null : new Image(logoLarge));
        otherImages.put("logoMedium", logoMedium == null ? null : new Image(logoMedium));
        otherImages.put("logoSmall", logoSmall == null ? null : new Image(logoSmall));
        otherImages.put("sideNavImage", sideNavImage == null ? null : new Image(sideNavImage));
        otherImages.put("tabletThumbnailImage", tabletThumbnailImage == null ? null : new Image(tabletThumbnailImage));

        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
        for (ContentKey key : contentKeysByType) {
            BannerModel banner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if (banner.getLink() != null && StringUtils.equals(banner.getLink().getContentName(), brand.getContentName())) {
            	otherImages.put("Banner" + banner.getContentName(), Image.wrap(banner.getImage()));
            }
        }
        
        response.setOtherImages(otherImages);

        response.setProducerImages(producerImages);
		setResponseMessage(model, response, user);
		return model;
	}

	
	private ModelAndView getAll(ModelAndView model, SessionUser user) throws JsonException {
		List<Idea> ideas = getProducersIdeaList();
		ProducerListResponse response = new ProducerListResponse();
		response.setProducers(ideas);
		setResponseMessage(model, response, user);
		return model;
	}

	// used by Ideas screen, too
	public static List<Idea> getProducersIdeaList() {
		final List<BrandModel> producers = getProducers();
		List<Idea> ideas = new ArrayList<Idea>(producers.size());
		for (BrandModel brand : producers) {
			Idea idea = new Idea();
			idea.setDestinationId(brand.getContentName());
			idea.setDestinationSection("producer");

			if (brand != null) {
				if (brand.getTabletThumbnailImage() != null)
					idea.setFeatureImage(new Image(brand.getTabletThumbnailImage()));
				if (brand.getTabletAboutTextShort() != null)
					idea.setFeatureText(loadFeatureText(brand));
			}
			ideas.add(idea);
		}
		return ideas;
	}

	private static String loadFeatureText(final BrandModel brand) {
		final String path = brand.getTabletAboutTextShort().getPath();
		HttpClient http = new HttpClient();
		GetMethod get = new GetMethod(MobileApiProperties.getMediaPath() + path);
		try {
			http.executeMethod(get);
			final String text = get.getResponseBodyAsString();
			return text;
		} catch (Throwable e) {
			return null;
		}
	}

	
	private static List<BrandModel> getProducers() {
//      Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCER);
		List<BrandModel> brands = ContentFactory.getInstance().getStore().getTabletIdeasBrands();
		List<BrandModel> result = new ArrayList<BrandModel>(brands.size());
		for (BrandModel brandModel : brands) {
			if (brandModel != null) {
				result.add(brandModel);
			}
		}
    	
    	Collections.sort(result, new Comparator<BrandModel>() {
			@Override
			public int compare(BrandModel o1, BrandModel o2) {
				return o1.getFullName().compareTo(o2.getFullName());
			}
    	});
    	
        return result;
    }
	
	@Override
	protected boolean validateUser() {
		return false;
	}

	
}
