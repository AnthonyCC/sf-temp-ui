package com.freshdirect.smartstore.external.certona;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CertonaUserContextHolder {

	private static ThreadLocal<CertonaUserContext> certonaUserContext = new ThreadLocal<CertonaUserContextHolder.CertonaUserContext>();
	
	//Hide actual user context object to prevent accidental access "behind ThreadLocal's back"...
	private static class CertonaUserContext {
		
		private String id;
		private String searchParam;
		/**
		 * Holds true value if the latest search operation was successful
		 */
		private boolean successfulSearch = false;
		private String trackingId;
		private String sessionId;
		private String pageId;
		private List<String> excludeProductIds;
		private List<String> recommendedProductIds;
		
		public CertonaUserContext(String id, String searchParam, String trackingId, String sessionId, List<String> excludeProductIds) {
			
			this.trackingId = trackingId;
			this.sessionId = sessionId;
			this.id = id;
			this.searchParam = searchParam;
			if (excludeProductIds == null) {
				this.excludeProductIds = new ArrayList<String>();
			} else{
				this.excludeProductIds = excludeProductIds;
			}
			this.recommendedProductIds = new ArrayList<String>();
			
		}


		public String getTrackingId() {
			return trackingId;
		}

		public void setTrackingId(String trackingId) {
			this.trackingId = trackingId;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public String getPageId() {
			return pageId;
		}

		public void setPageId(String pageId) {
			this.pageId = pageId;
		}

		public List<String> getExcludeProductIds() {
			return excludeProductIds;
		}

		public void setExcludeProductIds(List<String> excludeProductIds) {
			this.excludeProductIds = excludeProductIds;
		}

		public List<String> getRecommendedProductIds() {
			return recommendedProductIds;
		}

		public void setRecommendedProductIds(List<String> recommendedProductIds) {
			this.recommendedProductIds = recommendedProductIds;
		}


		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}


		public String getSearchParam() {
			return searchParam;
		}


		public void setSearchParam(String searchParam) {
			this.searchParam = searchParam;
		}

		public boolean isSuccessfulSearch() {
			return successfulSearch;
		}
		
		public void setSuccessfulSearch(boolean successfulSearch) {
			this.successfulSearch = successfulSearch;
		}
	}

	public static void initCertonaContextFromCookies(HttpServletRequest request) {
		String trackingId = "";
		String sessionId = "";
		Cookie cookies[] = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("RES_TRACKINGID".equals(cookie.getName())) {
					trackingId = cookie.getValue();
				} else if ("RES_SESSIONID".equals(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
		}
		CertonaUserContextHolder.createContextObject("", "", trackingId, sessionId, null);

	}
	
	public static void createContextObject(String id, String searchParam, String trackingId, String sessionId, List<String> excludeProductIds) {
		
		if (certonaUserContext.get() == null) {
			certonaUserContext.set(new CertonaUserContext(id, searchParam, trackingId, sessionId, excludeProductIds));
		} else {
			certonaUserContext.get().setTrackingId(trackingId);
			certonaUserContext.get().setSessionId(sessionId);
			certonaUserContext.get().setId(id);
			certonaUserContext.get().setSearchParam(searchParam);
			certonaUserContext.get().setExcludeProductIds(excludeProductIds);
		}
	}
	
	public static String getTrackingId() {
		return getCertonaUserContext().getTrackingId();
	}

	public static void setTrackingId(String trackingId) {
		getCertonaUserContext().setTrackingId(trackingId);
	}

	public static String getSessionId() {
		return getCertonaUserContext().getSessionId();
	}

	public static void setSessionId(String sessionId) {
		getCertonaUserContext().setSessionId(sessionId);
	}

	public static String getPageId() {
		return getCertonaUserContext().getPageId();
	}

	public static void setPageId(String pageId) {
		getCertonaUserContext().setPageId(pageId);
	}

	public static List<String> getExcludeProductIds() {
		return getCertonaUserContext().getExcludeProductIds();
	}

	public static void setExcludeProductIds(List<String> excludeProductIds) {
		getCertonaUserContext().setExcludeProductIds(excludeProductIds);
	}

	public static List<String> getRecommendedProductIds() {
		return getCertonaUserContext().getRecommendedProductIds();
	}

	public static void setRecommendedProductIds(List<String> recommendedProductIds) {
		getCertonaUserContext().setRecommendedProductIds(recommendedProductIds);
	}

	public static String getId() {
		return getCertonaUserContext().getId();
	}

	public static void setId(String id) {
		getCertonaUserContext().setId(id);
	}

	public static String getSearchParam() {
		return getCertonaUserContext().getSearchParam();
	}

	public static void setSearchParam(String searchParam) {
		getCertonaUserContext().setSearchParam(searchParam);
	}

	public static boolean isSuccessfulSearch() {
		return getCertonaUserContext().isSuccessfulSearch();
	}
	
	public static void setSuccessfulSearch(boolean flag) {
		getCertonaUserContext().setSuccessfulSearch(flag);
	}


	private static CertonaUserContext getCertonaUserContext() {
		if (certonaUserContext.get() == null) {
			certonaUserContext.set(new CertonaUserContext("", "", "", "", null));
			
		} 
		return certonaUserContext.get();
	}

	public static void invalidateCertonaUserContext() {
		certonaUserContext.remove();
	}
}
