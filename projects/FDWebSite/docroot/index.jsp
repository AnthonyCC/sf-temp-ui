<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.cms.fdstore.FDContentTypes' %>
<%@ page import="com.freshdirect.cms.ContentKey"%>
<%@ page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.freshdirect.cms.application.CmsManager"%>
<%@ page import="com.freshdirect.cms.ContentType"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.freshdirect.webapp.util.RequestUtil"%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %><%

//expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;

// no YUI required for index.jsp
request.setAttribute("noyui", true);

%><fd:CheckLoginStatus pixelNames="TheSearchAgency" id="user" />
<%-- fd:WelcomeExperience / --%><%

  if (request.getParameter("serviceType") == null) {
	String serviceType = "HOME";
	if (user.isCorporateUser()){
      serviceType = "CORPORATE";
    }
	//The below redirect is not required, and causing performance issues due to additional redirect.
	//response.sendRedirect("/index.jsp?serviceType=" + serviceType);
  }

  boolean isCorpotateUser = user.isCorporateUser();
  boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));

	request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");

	String pageTemplate = "/common/template/no_shell_optimized.jsp"; //default
	if (mobWeb) {
		if ( "true".equalsIgnoreCase(request.getParameter("opt")) ) { //allow opt for live testing
			pageTemplate = "/common/template/mobileWeb_index_optimized.jsp"; //mobWeb template (20170913 batchley - only index should use optimized for now)
		} else {
			pageTemplate = "/common/template/mobileWeb.jsp"; //default
		}
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
%>
<tmpl:insert template="<%=pageTemplate %>">

	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="index" includeSiteSearchLink="true" title="Welcome to FreshDirect"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name="customCss">
		<fd:css href="/assets/css/homepage/homepage.css" media="all" />
	</tmpl:put>
	<tmpl:put name="extraCss"><%-- MOBILE --%>
	</tmpl:put>
	<tmpl:put name="customJsBottom">

	</tmpl:put>
	<tmpl:put name="extraJsFooter"><%-- MOBILE, end of body --%>
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
	<%!

		//APPDEV- 4368:: Need Indicator for Empty Picks List Begin
		public static boolean hasProduct_hprd(CategoryModel categoryModel){
			boolean hasProduct = false;

			if(!categoryModel.getSubcategories().isEmpty())
			{
				List<CategoryModel> subCategories = categoryModel.getSubcategories();
				for (CategoryModel m1 : subCategories) {
					boolean result = hasProduct_hprd(m1);
					if(result){
						return result;
					}
				}
			}
			if(categoryModel.getProducts().size()>0)
			{
				return isProductAvailable_hprd(categoryModel.getProducts());
			}else
				return false;
		}

		public static boolean isProductAvailable_hprd(List<ProductModel> prodList){
			boolean result = false;

			for(ProductModel model:prodList){
				if(!(model.isUnavailable() || model.isDiscontinued())){
					result = true;
					break;
				}
			}
			return result;
		}
	%>
	<%
		if (mobWeb) { %>
			<div id="mobilehomeMainDiv">
			<%
				//OAS setup
			   	request.setAttribute("listPos", "SystemMessage,HPMob01,HPMob02,HPMob03,HPMob04");
				/* these use OAS pages like www.freshdirect.com/mobileweb/[PAGENAME] */

			   	if (FDStoreProperties.isAdServerEnabled()) {
					%><div id="oas_HPMob01" class="oas-cnt home-page-banner">
			  			<script type="text/javascript">OAS_AD('HPMob01');</script>
			  		</div><%
					%><div id="oas_HPMob02" class="oas-cnt home-page-banner">
		  				<script type="text/javascript">OAS_AD('HPMob02');</script>
		  			</div><%
			  	}

				List<CategoryModel> catModels = ContentFactory.getInstance().getStore().getiPhoneHomePagePicksLists();
				int bannerIndex = 0;

				for (CategoryModel curCat: catModels) {
					String curCatLink = "/browse.jsp?id="+curCat;
					//skip if cat has no prod(s) and is not redirecting
					if ("".equals(curCat.getRedirectURL()) && !hasProduct_hprd(curCat)) {
						continue;
					}
					if ( curCat.getRedirectURL() != null && (curCat.getRedirectURL()).startsWith("foodkick://") ) {
						curCatLink = "https://www.foodkick.com";
					}
					String bannerText = curCat.getPrimaryText();
					if ("".equals(bannerText)) {
						bannerText = curCat.getFullName();
					}

					 %>

					<a href="<%= curCatLink %>">
					    <div class="home-page-banner">
						    <img data-src="<%= (curCat.getTabletThumbnailImage() != null) ? curCat.getTabletThumbnailImage().getPathWithPublishId() : "" %>" alt="" />
							<div class="home-page-banner-subtext-cont">
								<div class="home-page-banner-subtext"><%= bannerText %></div>
							</div>
						</div>
				    </a>
				    <% bannerIndex++; %>

				    <% if (bannerIndex == 5) { %>
						<%
						   	if (FDStoreProperties.isAdServerEnabled()) {
								%><div id="oas_HPMob03" class="oas-cnt home-page-banner">
						  			<script type="text/javascript">OAS_AD('HPMob03');</script>
						  		</div><%
						  	}
						%>
				    <% } %>
				<% } %>
				<% if (bannerIndex < 5) { /* display if there's not enough banners */ %>
					<%
					   	if (FDStoreProperties.isAdServerEnabled()) {
							%><div id="oas_HPMob03" class="oas-cnt home-page-banner">
					  			<script type="text/javascript">OAS_AD('HPMob03');</script>
					  		</div><%
					  	}
					%>
			    <% } %>
				<%
				   	if (FDStoreProperties.isAdServerEnabled()) {
						%><div id="oas_HPMob04" class="oas-cnt home-page-banner">
				  			<script type="text/javascript">OAS_AD('HPMob04');</script>
				  		</div><%
				  	}
				%>
			</div>
		<% } else { %>

			<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">

			<%
				boolean location2Media = false;
				if(null != segmentMessage && segmentMessage.isLocation2()) {
			        	location2Media = true;
			    }
			   	request.setAttribute("listPos", "SystemMessage,HPFull,HPMain,HPLeft01,HPLeft02,HPLeft03,HPRight01,HPRight02,HPRight03,HPFullTopBar,HPMainTopBar");
			%>

			<%
			boolean showAltHome = false;
			if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() ||
				(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1)))
					showAltHome = true;

				//Coupons disabled warning msg
				if (!user.isCouponsSystemAvailable() && !user.isCouponWarningAcknowledged() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
			        user.setCouponWarningAcknowledged(true);
			%>
			        <div style="display: none;" id="fdCoupon_indexAlert">
			                <div style="text-align: center;"><img src="/media/images/ecoupon/logo-purpler_old.png" alt="fdCoupon" height="85" width="222" /></div>
			                <div class="error_title"  style="text-align: center; margin: 20px 20px;"><%= (SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE).replace("unavailable.", "unavailable.<br />") %></div>
			                <div style="text-align: center; margin-bottom: 20px;"><img id="clickToContinue" style="cursor: pointer;" src="/media/images/ecoupon/click-here-to-continue_large.png" alt="Click Here To Continue" /></div>
			        </div>
			        <script type="text/javascript" language="javascript">
			                $jq(function() {
			                        var overlayDialog = doOverlayDialogBySelector('#fdCoupon_indexAlert');
			                        $jq('#clickToContinue').live('click', function(e) { e.preventDefault(); overlayDialog.dialog('close'); });
			                });
			        </script>
			<%
				}
			%>
			 	<div class="holder">
					<%-- MAIN CONTENT--%>
						<div class="content">

			<% if (showAltHome && !location2Media) {
				%><comp:homePageLetter user="<%= user %>" />
			<%} else if (!showAltHome && location2Media) {
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=isCorpotateUser%>"/>
			<%
			} else if (!showAltHome && !location2Media) {
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=isCorpotateUser%>"/>
			<%
			}
			%>

			<%
			if (location2Media) { %><comp:location2Media user="<%= user %>" /><% }
			%>
					<comp:OAShomepageredesign
            			full="HPFull"
            			main="HPMain"
						left1="HPLeft01"
						left2="HPLeft02"
						left3="HPLeft03"
						right1="HPRight01"
						right2="HPRight02"
						right3="HPRight03"
            			fullTopBar="HPFullTopBar"
            			mainTopBar="HPMainTopBar"
					/>

            <potato:modulehandling name="welcomepagePotato" />
            <soy:render template="common.contentModules" data="${welcomepagePotato}" />

					</div>
					<%-- Removed the learn more for marketing change. --%>
					<%-- <div id="bottom_link"><a href="/welcome.jsp"><img src="/media_stat/images/home/fd_logo_learn_more_back.jpg" alt="Learn More About Our Services"></a></div> --%>
				<%-- END MAIN CONTENT--%>

			</div>
			</fd:GetSegmentMessage>
		<% } %>

    <script>
      var FreshDirect = window.FreshDirect || {};
      FreshDirect.homepage = window.FreshDirect.homepage || {};
      FreshDirect.homepage.data = window.FreshDirect.homepage.data || {};
      FreshDirect.homepage.data.isHomepage = true;

      var homepageType = <%=isCorpotateUser%> ? 'corporate' : 'residental';

      var dataLayer = window.dataLayer || [];

      dataLayer.push({
        'is-new-homepage': 'true',
        'homepage-type': homepageType,
        'module-container-id': '${moduleContainerId}'
      });      
    </script>
    <% /* allow data to be output for debugging */
    if ( "true".equalsIgnoreCase(RequestUtil.getValueFromCookie(request, "developer")) ) {
    	%><script>FreshDirect.homepage.data = $jq.extend(FreshDirect.homepage.data,<fd:ToJSON object="${welcomepagePotato}" noHeaders="true"/>);</script><%
    } %>
</tmpl:put>
</tmpl:insert>
