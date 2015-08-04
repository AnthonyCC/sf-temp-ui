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
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
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
        
        request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
%>

<tmpl:insert template='/common/template/no_shell_optimized.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="index" includeSiteSearchLink="true"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name="customCss">
		<fd:css href="/assets/css/homepage/homepage.css" media="all" />
	</tmpl:put>
	<tmpl:put name="customJsBottom">
		
	</tmpl:put>
	
	<tmpl:put name='content' direct='true'>

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
	if (!user.isCouponsSystemAvailable() && !sessionUser.isCouponWarningAcknowledged()) {
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
	  <comp:modifyOrderBar user="<%= user %>"  htmlId="index_table_ordModify_0" /><% 
} else if (!showAltHome && !location2Media) { 
	%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
	  <comp:deliverySlotReserved user="<%= user %>" />
	  <comp:modifyOrderBar user="<%= user %>" htmlId="index_table_ordModify_1" /><%
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
		   			<div id="most-popular" class="">
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
<!-- end of APPDEV-4287 -->
	
</div>
</fd:GetSegmentMessage>
</tmpl:put>
</tmpl:insert>
