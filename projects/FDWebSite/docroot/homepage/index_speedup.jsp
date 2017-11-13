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
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.common.context.MasqueradeContext' %>
<%@ page import='javax.servlet.http.Cookie' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="fd-features" prefix="features" %>



<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />
<%

  FDUserI fdUser = (FDUserI)session.getAttribute(SessionName.USER);
  FDSessionUser sessionUser_speedup = (FDSessionUser)session.getAttribute(SessionName.USER);
  int validOrderCount = fdUser.getAdjustedValidOrderCount();
  boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, fdUser) && JspMethods.isMobile(request.getHeader("User-Agent"));
  boolean isHomepageReturningUser = validOrderCount > 0;
  String currentUserModuleContainerContentKey = FDStoreProperties.getHomepageRedesignCurrentUserContainerContentKey();
  String newUserModuleContainerContentKey = FDStoreProperties.getHomepageRedesignNewUserContainerContentKey();

  request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <fd:SEOMetaTag pageId="index" includeSiteSearchLink="true" title="Welcome to FreshDirect"></fd:SEOMetaTag>
    <fd:IncludeMedia name="/media/editorial/site_pages/javascript.html" />
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>

    <jwr:script src="/fdlibs_opt.js" useRandomParam="false" />
    <script type="text/javascript">

  		var FreshDirect = FreshDirect || {};
  		FreshDirect.USQLegalWarning = FreshDirect.USQLegalWarning || {};
  		FreshDirect.USQLegalWarning.sessionStore = '<%=session.getId()%>';

  		var $jq;
  		var jqInit = false;
  		function initJQuery() {
  			if (typeof(jQuery) == 'undefined') {
  				if (!jqInit) {
  					jqInit = true;
  				}
  				setTimeout("initJQuery()", 100);
  			} else {
  				$jq = jQuery.noConflict();
  			}
  		}
  		initJQuery();
  	</script>

    <jwr:style src="/newhomepage.css" media="all" />

    <%-- Keep the media include last, so it can always override any css auto-loaded --%>
    <fd:IncludeMedia name="/media/editorial/site_pages/stylesheet.html" />

  <fd:css href="/assets/css/homepage/homepage.css" media="all" />
</head>
  <body>
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
    <div id="content">
      <center class="text10">
      <!-- content lands here -->
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
               request.setAttribute("listPos", "SystemMessage,HPMob01,HPMob02");
            /* these use OAS pages like www.freshdirect.com/mobileweb/[PAGENAME] */

               if (FDStoreProperties.isAdServerEnabled()) {
              %><div id="oas_HPMob01" class="home-page-banner">
                  <script type="text/javascript">OAS_AD('HPMob01');</script>
                </div><%
              %><div id="oas_HPMob02" class="home-page-banner">
                  <script type="text/javascript">OAS_AD('HPMob02');</script>
                </div><%
              }

            List<CategoryModel> catModels = ContentFactory.getInstance().getStore().getiPhoneHomePagePicksLists();

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
                    <img src="<%= curCat.getTabletThumbnailImage().getPathWithPublishId() %>" alt="" />
                  <div class="home-page-banner-subtext-cont">
                    <div class="home-page-banner-subtext"><%= bannerText %></div>
                  </div>
                </div>
                </a>
            <% } %>
          </div>
        <% } else { %>

          <fd:GetSegmentMessage id='segmentMessage' user="<%=fdUser%>">

          <%
            boolean location2Media = false;
            if(null != segmentMessage && segmentMessage.isLocation2()) {
                    location2Media = true;
              }
               request.setAttribute("listPos", "SystemMessage,HPFull,HPMain,HPLeft01,HPLeft02,HPLeft03,HPRight01,HPRight02,HPRight03,HPFullTopBar,HPMainTopBar");
          %>

          <%
          boolean showAltHome = false;
          if (FDStoreProperties.IsHomePageMediaEnabled() && (!fdUser.isHomePageLetterVisited() ||
            (request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1)))
              showAltHome = true;

            //Coupons disabled warning msg
            if (!fdUser.isCouponsSystemAvailable() && !sessionUser_speedup.isCouponWarningAcknowledged() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
                  sessionUser_speedup.setCouponWarningAcknowledged(true);
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
            %><comp:homePageLetter user="<%= fdUser %>" />
          <%} else if (!showAltHome && location2Media) {
            %><comp:welcomeMessage user="<%= fdUser %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
          <%
          } else if (!showAltHome && !location2Media) {
            %><comp:welcomeMessage user="<%= fdUser %>" segmentMessage="<%= segmentMessage %>" isCosPage="<%=false%>"/>
              <comp:deliverySlotReserved user="<%= fdUser %>" />
          <%
          }
          %>

          <%
          if (location2Media) { %><comp:location2Media user="<%= fdUser %>" /><% }
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

                <%if (isHomepageReturningUser){ %>
                <potato:modulehandling name="welcomepagePotato" moduleContainerId="<%=currentUserModuleContainerContentKey%>" />
                <% }
                else {
                %>
                <potato:modulehandling name="welcomepagePotato" moduleContainerId="<%=newUserModuleContainerContentKey%>" />
              <%}%>
                <soy:render template="common.contentModules" data="${welcomepagePotato}" />

              </div>
            <%-- END MAIN CONTENT--%>

          </div>
          </fd:GetSegmentMessage>
        <% } %>

        <script type="text/javascript">
          var FreshDirect = window.FreshDirect || {};
          FreshDirect.homepage = true;
        </script>
      <!-- content ends above here-->
      </center>
    </div>

<!-- i_javascript_opt start-->
    <script>
      window.FreshDirect = window.FreshDirect || {};
      var fd = window.FreshDirect;

      fd.gtm = fd.gtm || {};
      fd.gtm.key = '<%= FDStoreProperties.getGoogleTagManagerKey() %>';
      fd.properties.isDFPEnabled = <%= FDStoreProperties.isDfpEnabled() ? true : false %>;
    </script>

    <%-- Google Tag Manager login/signup events --%>
    <%
    FDUserI gtm_user = (FDUserI)session.getAttribute(SessionName.USER);
    if (gtm_user != null) {
    %>
    <script>
      var dataLayer = window.dataLayer || [];

      dataLayer.push({
        'config-ga-key': '<%= FDStoreProperties.getGoogleAnalyticsKey() %>',
        'config-ga-domain': '<%= FDStoreProperties.getGoogleAnlayticsDomain() %>'
      });

      var gtm_userdata = {
        'user-customer-type': '<%= gtm_user.getSelectedAddress().getServiceType() %>',
        'user-ct-status': '<%= gtm_user.isChefsTable() %>',
        'user-del-county':'<%= gtm_user.getDefaultCounty() %>',
        'user-marketing-segment': '<%= gtm_user.getMarketingPromo() %>',
        'user-order-count': '<%= gtm_user.getAdjustedValidOrderCount() %>'
      };

      <%
      if (gtm_user.getDlvPassInfo() != null && gtm_user.getDlvPassInfo().getStatus() != null) {
      %>
      gtm_userdata['user-dp-status'] = '<%= gtm_user.getDlvPassInfo().getStatus().getDisplayName() %>';
      <%
      }
      %>

      <%
      if (gtm_user.getIdentity() != null && gtm_user.getIdentity().getErpCustomerPK() != null) {
      %>
      gtm_userdata['user-customer-id'] = '<%= gtm_user.getIdentity().getErpCustomerPK() %>';
      <%
      }
      %>

      dataLayer.push(gtm_userdata);
    </script>
    <% } %>

  <%@ include file="/common/template/includes/extol_tags.jspf" %>

    <%-- Feature version switcher --%>
    <features:potato />
    <%-- TODO !!! --%>
    <script type="text/javascript">
    (function () {
      window.FreshDirect = window.FreshDirect || {};
      var fd = window.FreshDirect;

      fd.features = fd.features || {};

      fd.features.active = <fd:ToJSON object="${featuresPotato.active}" noHeaders="true"/>

      fd.properties = fd.properties || {};
      fd.properties.isLightSignupEnabled = <%= FDStoreProperties.isLightSignupEnabled() ? "true" : "false" %>;
      fd.properties.isSocialLoginEnabled = <%= FDStoreProperties.isSocialLoginEnabled() ? "true" : "false" %>;

      <%
      FDSessionUser jsUser = (FDSessionUser)session.getAttribute(SessionName.USER);
      if (jsUser != null) {
        int sessionUserLevel = jsUser.getLevel();
        FDSessionUser sessionUser = (FDSessionUser) jsUser;
        boolean hideZipCheckPopup = (jsUser.getLevel() != FDSessionUser.GUEST || jsUser.isZipCheckPopupUsed() || sessionUser.isZipPopupSeenInSession());

        if (!hideZipCheckPopup){
            sessionUser.setZipPopupSeenInSession(true);
        }

        String zipCode = jsUser.getZipCode();
        String cohortName = jsUser.getCohortName();
        MasqueradeContext jsMasqueradeContext = jsUser.getMasqueradeContext();
        %>
      fd.user = {};
      fd.user.recognized = <%= sessionUserLevel == FDUserI.RECOGNIZED %>;
      fd.user.guest = <%= sessionUserLevel == FDUserI.GUEST %>;
      fd.user.isZipPopupUsed = <%= hideZipCheckPopup %>;
      fd.user.zipCode = '<%= zipCode %>';
      fd.user.cohortName = '<%= cohortName %>';
      fd.user.isCorporateUser = <%= sessionUser.isCorporateUser() %>;
        <% if (jsMasqueradeContext != null) {%>
          fd.user.masquerade = true;
        <% } %>
      <% } %>
    }());
    </script>
  	<jwr:script src="/fdlibs_opt.js" useRandomParam="false" />
    <% if (request.getAttribute("noyui") == null || !request.getAttribute("noyui").equals(true)) { %>
      <jwr:script src="/fdlibsyui.js"  useRandomParam="false" />
      <jwr:script src="/fdccl.js"  useRandomParam="false" />
    <% } %>



  	<script>
  	(function ($) {
  		$(function () {

  			$.smartbanner({daysHidden: 0, daysReminder: 0,author:'FreshDirect',button: 'VIEW'});
  			if(!$jq('#smartbanner.shown').is(':visible')) { $jq('#smartbanner').show(); }

  		});
  	})($jq);
  	</script>

  	<jwr:script src="/fdmisc.js" useRandomParam="false" />
  	<jwr:script src="/commonjavascript.js" useRandomParam="false" />

  	<%
  		FDUserI dpTcCheckUser = (FDUserI)session.getAttribute(SessionName.USER);
  		FDSessionUser dpTcCheckSessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);

  		if (dpTcCheckUser != null &&
  				(dpTcCheckUser.getLevel() == FDSessionUser.SIGNED_IN && Boolean.FALSE.equals(dpTcCheckSessionUser.hasSeenDpNewTc()) && dpTcCheckUser.isDpNewTcBlocking())
  			) {
  			%>
  				<script type="text/javascript">
  		    		$jq(document).ready(function() {
  			    		doOverlayWindow('/overlays/delivery_pass_tc.jsp?showButtons=true&count=true');
  		    		});
  		    	</script>
  	    	<%
  		}
  	%>

<!-- i_javascript_opt end-->

    <%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
  </body>
</html>
