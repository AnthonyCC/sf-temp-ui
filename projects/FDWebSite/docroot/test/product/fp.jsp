<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.storeapi.fdstore.*" %>
<%@ page import="com.freshdirect.cms.core.domain.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.pricing.*" %>
<%@ page import="com.freshdirect.fdstore.rollout.*"%>
<%@ page import="com.freshdirect.fdstore.customer.*"%>
<%@ page import="com.freshdirect.smartstore.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.ajax.*"%>
<%@ page import="com.freshdirect.webapp.ajax.filtering.*"%>
<%@ page import="com.freshdirect.webapp.ajax.browse.*"%>
<%@ page import="com.freshdirect.webapp.ajax.browse.data.*"%>
<%@ page import="com.freshdirect.webapp.ajax.product.*"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.*"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.*"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="unbxd" prefix='unbxd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<%
	int ratingBaseLine = 4
	final CmsFilteringNavigator nav = CmsFilteringNavigator.createInstance(request, user);
	nav.setPageTypeType(FilteringFlowType.BROWSE);
	
	List<List<ProductModel>> interestingProductGroups = null;
	String customerId = null;
	try {
		FDCustomerModel fdCustomerModel = user.getFDCustomer();
		if(fdCustomerModel != null) {
			customerId = fdCustomerModel.getErpCustomerPK();
		}
	} catch (Exception e) {
		//User no found
	}
	if(customerId != null) {
		interestingProductGroups = getYouLoveWeLoveProducts(customerId);
	}
	
	BrowseData browseData = new BrowseData();
	
	List<SectionData> sections = new ArrayList<SectionData>();
	browseData.getSections().setSections(sections);
	
	List<FilteringSortingItem<ProductModel>> items = new ArrayList<FilteringSortingItem<ProductModel>>();
	int sectionCount = 0;
	SectionData sectionData = null;
	ProductData productData = null;
	
	List<ContentKey> uniqueProducts = new ArrayList<ContentKey>();
	
	if(interestingProductGroups != null) {
		for(List<ProductModel> interestingProducts :  interestingProductGroups) {
			
			sectionData = new SectionData();
			if(sectionCount == 0) {			
				sectionData.setHeaderText(".......................Your Fav that might INTEREST U!............................");
			} else {
				sectionData.setHeaderText(".......................Our Fav that might INTEREST U!..............................");
			}
			sections.add(sectionData);
			sectionCount++;
			
			List<ProductData> productDatas = new ArrayList<ProductData>();
			sectionData.setProducts(productDatas);
			
			for(ProductModel product : interestingProducts) {
				if(!uniqueProducts.contains(product.getContentKey())) {
					product = ProductPricingFactory.getInstance().getPricingAdapter( product, user.getUserContext().getPricingContext());
					productData = new ProductData(product);
					try {
						ProductDetailPopulator.populateBrowseProductData(productData, user, nav, false);
						productDatas.add(productData);
					} catch(Exception e) {
						//System.out.println("Failed to populate product data for " + product==null ? "null": product.getContentName() + " (" + e.getMessage() + ")");
					}
					uniqueProducts.add(product.getContentKey());
				}
			}
		}
	}
			
	pageContext.setAttribute("browsePotato", DataPotatoField.digBrowse(browseData));
%>

<%!
	public static List<List<ProductModel>> getYouLoveWeLoveProducts(String customerId) {
		
		List<List<ProductModel>> result = new ArrayList<List<ProductModel>>();
		ProductModel productModel = null;
		
		List<ProductModel> youLove = new ArrayList<ProductModel>();
		Map<ContentKey,Float> customerPersonalizedProductScores = ScoreProvider.getInstance().getUserProductScores(customerId);	//2149848491
		for (Map.Entry<ContentKey,Float> entry : customerPersonalizedProductScores.entrySet()) {
			productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(entry.getKey());
			if(isYouLoveWeLoveProduct(productModel)) {
				youLove.add(productModel);
			}
		}
		result.add(youLove);
		
		List<ProductModel> weLove = new ArrayList<ProductModel>();
		Map<ContentKey, double[]> globalProductScores = ScoreProvider.getInstance().getGlobalScores();
				
		for (Map.Entry<ContentKey, double[]> entry : globalProductScores.entrySet()) {			
			double[] value = entry.getValue();
			
		    if(value[4] >= 10000) {
		    	productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(entry.getKey());
		    	if(isYouLoveWeLoveProduct(productModel)) {
					weLove.add(productModel);
				}
		    }
		}
		
		result.add(weLove);
		
		return result;
	}
	
	public static boolean isYouLoveWeLoveProduct(ProductModel productModel) {
		try {
			return (!productModel.isUnavailable() && 
						(productModel.isNew() || productModel.getPriceCalculator().getHighestDealPercentage() > 30 || productModel.isBackInStock() || productModel.getExpertWeight() >= 4));
		} catch (Exception e) {
			//System.out.println("isYouLoveWeLoveProduct..failed...."+productModel.getContentKey().getId());
			return false;
		}
	}
%>

<tmpl:insert template='/common/template/browse_noleftnav_template.jsp'>
  
   <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
    <soy:import packageName="srch" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all" />
    <jwr:style src="/browse.css" media="all" />
    <jwr:style src="/srch.css" media="all" />
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    	<div class="browse-sections transactional"><%-- this does the main prod grid --%>
	        <soy:render template="browse.content" data="${browsePotato.sections}" />
	    </div>
	    <script>
	      window.FreshDirect = window.FreshDirect || {};
	      window.FreshDirect.browse = window.FreshDirect.browse || {};
	      window.FreshDirect.globalnav = window.FreshDirect.globalnav || {};

	      window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
	      window.FreshDirect.globalnav.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
	      window.FreshDirect.coremetricsData = window.FreshDirect.browse.data.coremetrics;
	      window.FreshDirect.browse.data.searchParams = window.FreshDirect.browse.data.searchParams || {};

	      window.FreshDirect.browse.data.sortOptions = window.FreshDirect.browse.data.sortOptions || {};
	    </script>
  </tmpl:put>

  <tmpl:put name='extraJsModules'>
    <jwr:script src="/browse.js"  useRandomParam="false" />
    <jwr:script src="/srch.js"  useRandomParam="false" />
  </tmpl:put>
</tmpl:insert>
