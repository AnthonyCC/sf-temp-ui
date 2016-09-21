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
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %><% 

//expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;

// no YUI required for index.jsp
request.setAttribute("noyui", true);

%><fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />
<%-- fd:WelcomeExperience / --%><%

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));        
	
	request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
        
	String pageTemplate = "/common/template/no_shell_optimized.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
%>
<tmpl:insert template="<%=pageTemplate %>">
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="index" includeSiteSearchLink="true"></fd:SEOMetaTag>
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
	
	<tmpl:put name="nav_top"><%-- MOBILE --%>
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
	</tmpl:put>
	
	<tmpl:put name="nav_bottom"><%-- MOBILE --%>
		<%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<%!

		//APPDEV- 4368:: Need Indicator for Empty Picks List Begin
		public static boolean hasProduct(CategoryModel categoryModel){
			boolean hasProduct = false;
			
			if(!categoryModel.getSubcategories().isEmpty())
			{
				List<CategoryModel> subCategories = categoryModel.getSubcategories();
				for (CategoryModel m1 : subCategories) {
					boolean result = hasProduct(m1);
					if(result){
						return result; 
					}
				}
			}
			if(categoryModel.getProducts().size()>0)
			{
				return isProductAvailable(categoryModel.getProducts());
			}else
				return false;
		}
		
		public static boolean isProductAvailable(List<ProductModel> prodList){
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
			   	request.setAttribute("listPos", "SystemMessage,HPMob01,HPMob02");
				/* these use OAS pages like www.freshdirect.com/mobileweb/[PAGENAME] */
			   	
			   	if (FDStoreProperties.isAdServerEnabled()) {
					%><div id="OAS_HPMob01" class="home-page-banner">
			  			<script type="text/javascript">OAS_AD('HPMob01');</script>
			  		</div><% 
					%><div id="OAS_HPMob02" class="home-page-banner">
		  				<script type="text/javascript">OAS_AD('HPMob02');</script>
		  			</div><% 
			  	}
						   	
				List<CategoryModel> catModels = ContentFactory.getInstance().getStore().getiPhoneHomePagePicksLists();
				
				for (CategoryModel curCat: catModels) {
					//skip if cat has no prod(s)
					if (!hasProduct(curCat)) {
						continue;
					}
					
					 %>
					 
					<a href="/browse.jsp?id=<%= curCat %>">
				    <div class="home-page-banner">
					    <%
					    	boolean bannerFound = false;
					        Set<ContentKey> banners = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
					        for (ContentKey key : banners) {
					            BannerModel curBanner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
	
				            	if (curBanner != null && curBanner.getLink() != null && curBanner.getImage() != null && StringUtils.equals(curBanner.getLink().getContentName(), curCat.getContentName())) {
					      			%><img src="<%= curBanner.getImage().getPathWithPublishId() %>" alt=""><%
					      			bannerFound = true;
					      			break;
				            	}
	
					        }
					        if (!bannerFound) {
					        	%><img src="<%= curCat.getTabletThumbnailImage().getPathWithPublishId() %>" alt=""><%
					        }
						%>
				        <div class="helpDiv"></div>
						<span class="span-left"><%= curCat.getFullName() %></span>
						<span class="span-right"><%= curCat.getPrimaryText() %></span>
					</div>
				    </a>
				<% } %>
			</div>
			<div class="loginDialog">
				<div class="close-x"></div>				
				<iframe src="/login/login.jsp" id="signin_iframe" style="display: none;"></iframe>
				<iframe src="/social/signup_lite.jsp?successPage=undefined" id="createacc_iframe" style="display: none;"></iframe>
			</div>
		<% } else { %>
			
			<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">
			
			<%
				boolean location2Media = false;
				if(null != segmentMessage && segmentMessage.isLocation2()) {
			        	location2Media = true;
			    }
			   	request.setAttribute("listPos", "SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom,HPLeftBottom,HPMiddleBottom,HPRightBottom");
			%>
			
			<% 
			boolean showAltHome = false;
			if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || 
				(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) 
					showAltHome = true;
			
				//Coupons disabled warning msg
				if (!user.isCouponsSystemAvailable() && !sessionUser.isCouponWarningAcknowledged() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
			        sessionUser.setCouponWarningAcknowledged(true);
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
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
			<% 
			} else if (!showAltHome && !location2Media) { 
				%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
				  <comp:deliverySlotReserved user="<%= user %>" />
			<%
			}
				   	
			if (location2Media) { %><comp:location2Media user="<%= user %>" /><% } 
			%>
					<comp:OASFeature 
						top="HPFeatureTop"
						left="HPFeature"
						tab1="HPTab1"
						tab2="HPTab2"
						tab3="HPTab3"
						tab4="HPTab4"
						bottom="HPFeatureBottom"
						hpBottomLeft="HPLeftBottom"
						hpBottomMiddle="HPMiddleBottom"
						hpBottomRight="HPRightBottom"
					/>
			<%
				   		StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
				   		if (store != null) {
				   			ConfigurationContext confContext = new ConfigurationContext();
				   			confContext.setFDUser(user);
				   			ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
				   			String trkCode = "favorites";
				   			request.setAttribute("trk",trkCode);
				   			if (validOrderCount<=3){
			%>
					   			<div id="most-popular">
					   				<h2 class="homepage-categories-header">
					   					<span>most popular products</span>
					   				</h2>
					   				<potato:recommender siteFeature="FAVORITES" name="deals" maxItems="24" cmEventSource="BROWSE"  sendVariant="true" />
					   				<soy:render template="common.ymalCarousel" data="${deals}" />
					   			</div>
			<%
			          } else {
			          %>
			            <div id="top-items" class="">
			                <potato:recommender siteFeature="TOP_ITEMS_QS" name="topItems" maxItems="24" cmEventSource="BROWSE" sendVariant="true" />
			                <soy:render template="common.yourTopItemsCarousel" data="${topItems}" />
			            </div>
			          <%
			          }  
				   			Html edtMed = store.getEditorial();
							if ( edtMed != null ) { %>
								<fd:IncludeHtml html="<%= edtMed %>"/>
							<% } else {
								String categoryLinks = FDStoreProperties.getHPCategoryLinksFallback();
								if ( categoryLinks != null ) {
									%><fd:IncludeMedia name="<%= categoryLinks %>"></fd:IncludeMedia><%
								}
							}
						}
			%>
						<div class="oas_home_bottom"><script type="text/javascript">OAS_AD('HPWideBottom');</script></div>
					</div>
					<%-- Removed the learn more for marketing change. --%>
					<%-- <div id="bottom_link"><a href="/welcome.jsp"><img src="/media_stat/images/home/fd_logo_learn_more_back.jpg" alt="Learn More About Our Services"></a></div> --%>
				<%-- END MAIN CONTENT--%> 
			<!-- end of APPDEV-4287 -->
				
			</div>
			</fd:GetSegmentMessage>
		<% } %>
		
		
				
		<!-- Dstillery pixel swap -->	
			<!--commented as a part of APPDEV-4287 <script src="//action.media6degrees.com/orbserv/hbjs?pixId=26207&pcv=47" type="text/javascript" async></script>-->
		<!-- start of APPDEV-4287 -->	
		<script type="text/javascript" async>
			function asyncPixelWithTimeout() {
			var img = new Image(1, 1);
			img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26207&pcv=47';
			setTimeout(function ()
			{ if (!img.complete) img.src = ''; /*kill the request*/ }
			
			, 33);
			};
			
			asyncPixelWithTimeout();
		</script>
</tmpl:put>
</tmpl:insert>
