package com.freshdirect.webapp.ajax.filtering;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.CharEncoding;
import org.apache.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.util.RequestUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsFilteringNavigator {

    // navigation id (dept or cat id)
    private String id;

    // catId for special pages like presiden't picks
    private String catId;

    // paging
    private int pageSize;
    private int activePage = 1;

    // sorting
    private String sortBy;
    private boolean isOrderAscending;

    // filtering
    private Map<String, List<String>> requestFilterParams = new TreeMap<String, List<String>>();

    // show all product on the actual page
    private boolean all = false;

    // are we on a product page?
    private boolean pdp = false;

    // are we on a special layout page?
    private boolean specialPage = false;

    // use cmevent param in order to determine the action (navigation/filtering/paging/sorting)
    private String browseEvent;

    // if nutrition sort is selected this field stores the selected nutrition type
    private ErpNutritionType.Type erpNutritionTypeType;

    // search query param
    private String searchParams = null;

    // used in masquerade mode
    private String listSearchParams = null;

    private String activeTab = "product";

    private FilteringFlowType pageType = null;

    private String ppPreviewId = null;

    private String picksId = null;

    public String getPicksId() {
        return picksId;
    }

    public void setPicksId(String picksId) {
        this.picksId = picksId;
    }

    private int productHits = 0;
    private int recipeHits = 0;

    private Cookie[] requestCookies;
    
    private String referer;
    private String requestUrl;
    private boolean aggregateCategories;
    
    /**
     * Creates a CmsFilteringNavigator instance out of request parameter map.
     * 
     * @return CmsFilteringNavigator
     * @throws FDResourceException
     * @throws InvalidFilteringArgumentException
     */
    public static CmsFilteringNavigator createInstance(HttpServletRequest request, FDUserI fdUser) throws InvalidFilteringArgumentException, FDResourceException {

        try {
            request.setCharacterEncoding(CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException uee) {
            throw new FDResourceException(uee);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String[]> paramMap = request.getParameterMap();
        Set<String> paramNames = new TreeSet<String>(paramMap.keySet());
        CmsFilteringNavigator cmsFilteringNavigator = null;
        if (paramMap.get("data") != null && !"".equals(paramMap.get("data"))) {

            try {   
                cmsFilteringNavigator = JsonHelper.parseRequestData(request, CmsFilteringNavigator.class);
            } catch (JsonParseException e) {
                throw new InvalidFilteringArgumentException(e, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
            } catch (JsonMappingException e) {
                throw new InvalidFilteringArgumentException(e, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
            } catch (IOException e) {
                throw new FDResourceException(e);
            }

        } else {

            cmsFilteringNavigator = new CmsFilteringNavigator();
            for (String param : paramNames) {

                String[] paramValues = paramMap.get(param);
                for (String paramValue : paramValues) {

                    if ("pageSize".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setPageSize(Integer.parseInt(paramValue));
                    } else if ("all".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setAll(Boolean.parseBoolean(paramValue.toLowerCase()));
                    } else if ("activePage".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setActivePage(Integer.parseInt(paramValue));
                    } else if ("sortBy".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setSortBy(paramValue);
                    } else if ("orderAsc".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setOrderAscending(Boolean.parseBoolean(paramValue.toLowerCase()));
                    } else if ("id".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setId(paramValue);
                    } else if ("catId".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setId(paramValue);
                    } else if ("searchParams".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setSearchParams(paramValue);
                    } else if ("listSearchParams".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setListSearchParams(paramValue);
                    } else if ("ppPreviewId".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setPpPreviewId(paramValue);
                    } else if ("activeTab".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setActiveTab(paramValue.toLowerCase());
                    } else if ("picksId".equalsIgnoreCase(param)) {
                        cmsFilteringNavigator.setPicksId(paramValue);
                    } else if ("pageType".equalsIgnoreCase(param)) {
                        // Do nothing but exclude from 'filtering params'
                    } else { // No match for any other CmsFilteringNavigator property => must be a filtering domain
                        String filteringDomainId = param;
                        String[] filteringIds = paramValue.split("\\|\\|");
                        List<String> filteringIdList = new ArrayList<String>();
                        Collections.addAll(filteringIdList, filteringIds);
                        cmsFilteringNavigator.getRequestFilterParams().put(filteringDomainId, filteringIdList);
                    }
                }
            }
        }

        String id = cmsFilteringNavigator.getId();
        cmsFilteringNavigator.parseFilteringFlowType(request);
        int pageSpecificPageSize;
        if (cmsFilteringNavigator.getPageSize() != 0) {
            pageSpecificPageSize = cmsFilteringNavigator.getPageSize();
        } else if (cmsFilteringNavigator.pageType != null) {
            switch (cmsFilteringNavigator.pageType) {
                case NEWPRODUCTS:
                    pageSpecificPageSize = FDStoreProperties.getNewProductsPageSize();
                    break;
                case ECOUPON:
                    pageSpecificPageSize = FDStoreProperties.getEcouponPageSize();
                    break;
                case PRES_PICKS:
                    pageSpecificPageSize = FDStoreProperties.getPresPicksPageSize();
                    break;
                case STAFF_PICKS:
                    pageSpecificPageSize = FDStoreProperties.getStaffPicksPageSize();
                    break;
                case SEARCH:
                    pageSpecificPageSize = FDStoreProperties.getSearchPageSize();
                    break;
                default:
                    pageSpecificPageSize = FDStoreProperties.getBrowsePageSize();
                    break;
            }
        } else {
        	pageSpecificPageSize = FDStoreProperties.getBrowsePageSize();
        }
        
        pageSpecificPageSize = increasePageSizeToFillLayoutFully(request, fdUser, pageSpecificPageSize);
        cmsFilteringNavigator.setPageSize(pageSpecificPageSize);

        if ((id == null || id.equals(""))
                && (cmsFilteringNavigator.getPageType().equals(FilteringFlowType.BROWSE) || cmsFilteringNavigator.getPageType().equals(FilteringFlowType.PRES_PICKS) || cmsFilteringNavigator
                        .getPageType().equals(FilteringFlowType.STAFF_PICKS))) {
            throw new InvalidFilteringArgumentException("ID parameter is null", InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE);
        }
        cmsFilteringNavigator.setRequestCookies(request.getCookies());
        cmsFilteringNavigator.setReferer(request.getHeader(HttpHeaders.REFERER));
        cmsFilteringNavigator.setRequestUrl(RequestUtil.getFullRequestUrl(request));
        return cmsFilteringNavigator;
    }

    public static int increasePageSizeToFillLayoutFully(HttpServletRequest request, FDUserI user, int pageSpecificPageSize) {
        Map<String, String> activeFeatures = FeaturesService.defaultService().getActiveFeaturesMapped(request.getCookies(), user);
        String gridlayoutversion = activeFeatures.get("gridlayoutcolumn");
        if (gridlayoutversion != null) {
            int divider = Integer.parseInt(gridlayoutversion.substring(0, 1));
            int modulo = pageSpecificPageSize % divider;
            if (modulo != 0) {
                pageSpecificPageSize = pageSpecificPageSize + (divider - modulo);
            }
        }
        return pageSpecificPageSize;
    }

    public static boolean isDisabledPartialRolloutRedirector(HttpServletRequest request) {
        String value = request.getParameter("disablePartialRolloutRedirector");
        final boolean result;
        if (value != null) {
            result = Boolean.parseBoolean(value);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @return non URL encoded query string
     */
    public String assembleQueryString() {

        StringBuffer queryString = new StringBuffer();

        queryString.append("pageType=").append(pageType.toString().toLowerCase());
        queryString.append("&");
        switch (pageType) {
            case BROWSE:
                queryString.append("id=").append(id);
                queryString.append("&");
                break;
            case PRES_PICKS: /* BROWSE could be combined in here */
                queryString.append("id=").append(id);
                queryString.append("&");
                break;
            case STAFF_PICKS:
                queryString.append("id=").append(id);
                queryString.append("&");
                queryString.append("picksId=").append(picksId);
                queryString.append("&");
                break;
            case SEARCH:
                queryString.append("searchParams=").append(searchParams);
                if (listSearchParams != null) {
                    queryString.append("&").append("listSearchParams=").append(listSearchParams);
                }
                queryString.append("&");
                break;
            default:
                break;
        }
        if (!FilteringFlowType.PRES_PICKS.equals(pageType) || (FilteringFlowType.PRES_PICKS.equals(pageType) && FDStoreProperties.isPresidentPicksPagingEnabled())) {
            queryString.append("pageSize=").append(pageSize);
            queryString.append("&");
            queryString.append("all=").append(all);
            queryString.append("&");
            queryString.append("activePage=").append(activePage);
            queryString.append("&");
        }
        queryString.append("sortBy=").append(sortBy);
        queryString.append("&");
        queryString.append("orderAsc=").append(isOrderAscending);
        queryString.append("&");
        queryString.append("activeTab=").append(activeTab);
        queryString.append("&");

        if (ppPreviewId != null && ppPreviewId.length() > 0) {
            queryString.append("ppPreviewId=").append(ppPreviewId);
            queryString.append("&");
        }
        for (String filteringDomainId : requestFilterParams.keySet()) {

            StringBuffer filteringIds = new StringBuffer();
            for (String filteringId : requestFilterParams.get(filteringDomainId)) {
                filteringIds.append(filteringId).append("-");
            }
            filteringIds.deleteCharAt(filteringIds.length() - 1); // remove last unnecessary '-'

            queryString.append(filteringDomainId).append("=").append(filteringIds.toString());
            queryString.append("&");

        }
        queryString.deleteCharAt(queryString.length() - 1); // remove last unnecessary '&'

        return queryString.toString();
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isOrderAscending() {
        return isOrderAscending;
    }

    public void setOrderAscending(boolean isOrderAscending) {
        this.isOrderAscending = isOrderAscending;
    }

    public Map<String, List<String>> getRequestFilterParams() {
        return requestFilterParams;
    }

    public void setRequestFilterParams(Map<String, List<String>> requestFilterParams) {
        this.requestFilterParams = requestFilterParams;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public boolean isPdp() {
        return pdp;
    }

    public void setPdp(boolean pdp) {
        this.pdp = pdp;
    }

    public boolean isSpecialPage() {
        return specialPage;
    }

    public void setSpecialPage(boolean specialPage) {
        this.specialPage = specialPage;
    }

    public String getBrowseEvent() {
        return browseEvent;
    }

    public void setBrowseEvent(String browseEvent) {
        this.browseEvent = browseEvent;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public ErpNutritionType.Type getErpNutritionTypeType() {
        return erpNutritionTypeType;
    }

    public void setErpNutritionTypeType(ErpNutritionType.Type erpNutritionTypeType) {
        this.erpNutritionTypeType = erpNutritionTypeType;
    }

    public String getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(String searchParams) {
        this.searchParams = searchParams;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public FilteringFlowType getPageType() {
        return pageType;
    }

    public void setPageTypeType(FilteringFlowType pageType) {
        this.pageType = pageType;
    }

    public void parseFilteringFlowType(HttpServletRequest request) {
        String pageTypeReqParam = request.getParameter("pageType");
        if (pageTypeReqParam == null && pageType != null) {
            pageTypeReqParam = pageType.toString().toLowerCase();
        }
        if (FilteringFlowType.PRES_PICKS.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.PRES_PICKS;
        } else if (FilteringFlowType.STAFF_PICKS.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.STAFF_PICKS;
        } else if (id != null && !"".equals(id) || FilteringFlowType.BROWSE.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.BROWSE;
        } else if (FilteringFlowType.NEWPRODUCTS.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.NEWPRODUCTS;
        } else if (FilteringFlowType.ECOUPON.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.ECOUPON;
        } else if (request.getRequestURI().equals("/srch.jsp") || searchParams != null || FilteringFlowType.SEARCH.toString().equalsIgnoreCase(pageTypeReqParam)) {
            pageType = FilteringFlowType.SEARCH;
        }
    }

    public void setProductHits(int productHits) {
        this.productHits = productHits;
    }

    public int getProductHits() {
        return productHits;
    }

    public void setRecipeHits(int recipeHits) {
        this.recipeHits = recipeHits;
    }

    public int getRecipeHits() {
        return recipeHits;
    }

    public String getPpPreviewId() {
        return ppPreviewId;
    }

    public void setPpPreviewId(String ppPreviewId) {
        this.ppPreviewId = ppPreviewId;
    }

    public String getListSearchParams() {
        return listSearchParams;
    }

    public void setListSearchParams(String listSearchParams) {
        this.listSearchParams = listSearchParams;
    }

    public Cookie[] getRequestCookies() {
        return requestCookies;
    }

    public void setRequestCookies(Cookie[] requestCookies) {
        this.requestCookies = requestCookies;
    }
    
    public void setReferer(String referer) {
        this.referer = referer;
    }
    
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    
    public String getReferer() {
        return referer;
    }
    
    public String getRequestUrl() {
        return requestUrl;
    }

    public boolean isAggregateCategories() {
        return aggregateCategories;
    }

    public void setAggregateCategories(boolean aggregateCategories) {
        this.aggregateCategories = aggregateCategories;
    }
}
