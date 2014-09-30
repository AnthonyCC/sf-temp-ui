package com.freshdirect.webapp.ajax.filtering;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagInput;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.external.certona.CertonaUserContextHolder;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.CoremetricsPopulator;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams.Tab;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class CmsFilteringServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -3643980667721343751L;
	private static final Logger LOGGER = LoggerFactory.getInstance( CmsFilteringServlet.class );

	/**
	 * Processing json post data
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		
		try {
			final CmsFilteringNavigator navigator = parseRequestData(request, CmsFilteringNavigator.class);
			final CmsFilteringFlowResult result = new CmsFilteringFlow().doFlow(navigator, (FDSessionUser)user);

			adjustCertonaSearchStatus(navigator, result);
	
			writeResponseData(response, result);
		} catch (InvalidFilteringArgumentException e) {
			returnHttpError( 400, "JSON contains invalid arguments", e );	// 400 Bad Request
		}
	
	}

	public enum BrowseEvent {
		NOEVENT, PAGEVIEW, ELEMENT, PAGE, SORT;
	}

	/**
	 * Processing query from direct http call
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		
		try {
			CmsFilteringNavigator navigator = CmsFilteringNavigator.createInstance(request);
			ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || ((FDSessionUser)user).isEligibleForDDPP());
			CertonaUserContextHolder.initCertonaContextFromCookies(request);
			CertonaUserContextHolder.setId(navigator.getId());
			CertonaUserContextHolder.setSearchParam(navigator.getSearchParams());
			final CmsFilteringFlowResult flow = new CmsFilteringFlow().doFlow(navigator, (FDSessionUser)user);
			final Map<String, ?> payload = DataPotatoField.digBrowse(flow);

			// certona extension
			adjustCertonaSearchStatus(navigator, flow);


			if ( request.getParameterMap().keySet().contains("data") ) {
				final Map<String, Object> clientInput;
				try {

					//
					// CoreMetrics reporting section
					//
					
					clientInput = JsonHelper.parseRequestData(request, Map.class);
					
					final BrowseEvent cmEvent = clientInput.get("browseEvent") != null ? BrowseEvent
							.valueOf( ((String) clientInput.get("browseEvent")).toUpperCase())
							: BrowseEvent.NOEVENT;

					// handle event
					switch(cmEvent) {
					case PAGEVIEW:
						CoremetricsPopulator.appendPageViewTag( (Map<String, Object>) payload , PageViewTagInput.populateFromJSONInput(request.getRequestURI(), clientInput) );
						break;
					case ELEMENT:
						CoremetricsPopulator.appendFilterElementTag((Map<String, Object>) payload , (Map<String, Object>) clientInput.get("requestFilterParams"));
						break;
					case SORT:
						CoremetricsPopulator.appendSortElementTag((Map<String, Object>) payload, navigator.getSortBy());
						break;
					case PAGE:
					case NOEVENT:
					default:
						// do nothing
						break;
					}

				} catch (JsonParseException e) {
					returnHttpError( 400, "Cannot read client input", e );	// 400 Bad Request
				} catch (JsonMappingException e) {
					returnHttpError( 400, "Cannot read client input", e );	// 400 Bad Request
				} catch (IOException e) {
					returnHttpError( 400, "Cannot read client input", e );	// 400 Bad Request
				} catch (SkipTagException e) {
					returnHttpError( 400, "Cannot read client input", e );	// 400 Bad Request
				}				
			}
			
			writeResponseData(response, payload);
			
		} catch (FDResourceException e) {
			returnHttpError( 400, "Cannot read JSON", e );	// 400 Bad Request
		} catch (InvalidFilteringArgumentException e) {
			
			switch (e.getType()){
			case NODE_IS_RECIPE_DEPARTMENT:
			case SPECIAL_LAYOUT:
			case NODE_HAS_REDIRECT_URL:{
				Map<String, String> resp = new HashMap<String, String>();
				resp.put("redirectUrl", e.getRedirectUrl());
				writeResponseData(response, resp);
				break;
			}
			case TERMINATE:
				LOGGER.error(e.getMessage());
				break;
				
			default:
				returnHttpError( 400, "JSON contains invalid arguments", e );	// 400 Bad Request	
				break;
			}				
		}
	}

	@Override
	protected int getRequiredUserLevel() {		
		return FDUserI.GUEST;
	}

	/**
	 * Utility function that maintains certona context
	 * based on recent navigation flow result
	 * 
	 * @param navigator
	 * @param result
	 */
	public static void adjustCertonaSearchStatus(final CmsFilteringNavigator navigator, final CmsFilteringFlowResult result) {
		// certona extension
		if (navigator.getPageType().isSearchLike()) {
			// assume search operation was executed
			for (Tab t : result.getBrowseDataPrototype().getSearchParams().getTabs()) {
				// pick active tab, check the search result
				if (t.isActive()) {
					if (t.getHits() > 0) {
						CertonaUserContextHolder.setSuccessfulSearch(true);
						break;
					}
				}
			}
		}
	}
}
