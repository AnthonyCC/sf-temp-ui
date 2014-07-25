package com.freshdirect.smartstore.external.certona;

import java.util.ArrayList;
import java.util.List;

public class CertonaUserContextHolder {

	private static ThreadLocal<CertonaUserContext> certonaUserContext = new ThreadLocal<CertonaUserContextHolder.CertonaUserContext>();
	
	//Hide actual user context object to prevent accidental access "behind ThreadLocal's back"...
	private static class CertonaUserContext {
		
		private String customerId;
		private String cohort;
		private String trackingId;
		private String sessionId;
		private String pageId;
		private List<String> excludeProductIds;
		private List<String> recommendedProductIds;
		
		public CertonaUserContext(String customerId, String cohort, String trackingId, String sessionId, List<String> excludeProductIds) {
			
			this.customerId = customerId;
			this.cohort = cohort;
			this.trackingId = trackingId;
			this.sessionId = sessionId;
			if (excludeProductIds == null) {
				this.excludeProductIds = new ArrayList<String>();
			} else{
				this.excludeProductIds = excludeProductIds;
			}
			this.recommendedProductIds = new ArrayList<String>();
			
		}

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
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

		public String getCohort() {
			return cohort;
		}

		public void setCohort(String cohort) {
			this.cohort = cohort;
		}
	}

	public static void createContextObject(String customerId, String cohort, String trackingId, String sessionId, List<String> excludeProductIds) {
		certonaUserContext.set(new CertonaUserContext(customerId, cohort, trackingId, sessionId, excludeProductIds));
	}
	
	public static String getCustomerId() {
		return getCertonaUserContext().getCustomerId();
	}

	public static void setCustomerId(String customerId) {
		getCertonaUserContext().setCustomerId(customerId);
	}

	public static String getCohort() {
		return getCertonaUserContext().getCohort();
	}

	public static void setCohort(String cohort) {
		getCertonaUserContext().setCohort(cohort);
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

	private static CertonaUserContext getCertonaUserContext() {
		if (certonaUserContext.get() == null) {
			certonaUserContext.set(new CertonaUserContext("", "", "", "", null));
			
		} 
		return certonaUserContext.get();
	}
	
}
