<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.storeapi.fdstore.FDContentTypes' %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@ page import="com.freshdirect.storeapi.content.StoreModel"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.freshdirect.storeapi.application.CmsManager"%>
<%@ page import="com.freshdirect.cms.core.domain.ContentType"%>
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
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));

	request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");

	String pageTemplate = "/common/template/no_shell_optimized.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
%>

<potato:modulehandling name="welcomepagePotato" moduleContainerId="ModuleContainer:currentUserModuleContainer" />
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
					if ("".equals(curCat.getRedirectURL()) && !hasProduct(curCat)) {
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
			//if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() ||
			//	(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1)))
			//		showAltHome = true;

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

						}
			%>

      <style>
      .full-image-carousel[data-component="carousel"] .pager {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 5px;
      }

      .full-image-carousel[data-component="carousel"],
      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"],
      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul,
      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li {
        width: 100%;
        height: 100%;
        text-align: center;
        padding: 0;
        margin: 0;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a img {
        height: 100%;
        width: 100%;
        position: relative;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a .content {
        bottom: 15px;
        position: absolute;
        text-align: left;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a .content .flag {
        background-color: red;
        color: black;
        font-size: 18px;
        font-weight: bold;
        height: 24px;
        line-height: 24px;
        padding-left: 10px;
        padding-right: 25px;
        text-transform: uppercase;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a .content .description {
        background-color: rgba(255,255,255,.9);
        padding-bottom: 5px;
        width: 100%;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a .content .description .header{
        color: black;
        font-size: 25px;
        font-weight: bold;
        padding: 0 10px;
        text-transform: uppercase;
      }

      .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul li a .content .description .body{
        color: black;
        font-size: 17px;
        letter-spacing: 25;
        padding: 0 10px;
      }
      </style>

			<script type="text/javascript">
					var oasCarousel = [
						{
							imageLink: '/media/images/navigation/fd_raf_for_fb.gif',
							link: '/browse.jsp?id=fru',
							flag: 'Story1',
							header: 'test header 1',
							content: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
						},
						{
							imageLink: '/media/images/product/bakery/cake/cake_bday_strawmsse_z.jpg',
							link: '/browse.jsp?id=fru',
							flag: 'Story2',
							header: 'test header 2',
							content: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
						}
					];
			</script>

				<div class="image-carousel">
					<div data-component="carousel" class="first full-image-carousel">
						<div data-component="carousel-mask" class="transactional">
							<ul data-component="carousel-list" data-carousel-page="0">
							</ul>
						</div>
						<button data-component="carousel-prev" data-carousel-nav="prev">previous</button>
						<button data-component="carousel-next" data-carousel-nav="next">next</button>
            <ul class="pager">
              <li data-page="0" class="current">
                <span>1</span>
              </li>
            </ul>
					</div>
				</div>

			<script type="text/javascript">
        (function () {
					var $ = jQuery,
              carousel = $('.image-carousel .full-image-carousel[data-component="carousel"]'),
              currentPage = carousel.data('carousel-page') || 0,
              numberOfPages = oasCarousel.length-1;

					oasCarousel.forEach(function (e) {
						$('.image-carousel .full-image-carousel[data-component="carousel"] [data-component="carousel-mask"] ul[data-component="carousel-list"]').append(
							'<li class="portrait-item">' +
							'<a href=' + e.link + '>' +
							'<img src=' + e.imageLink + '>' +
							'<div class="content">' +
							'<span class="flag">' + e.flag + '</span>' +
							'<div class="description">' +
							'<h1 class="header">' + e.header +'</h1>' +
							'<div class="body">' + e.content +'</div></div></div></a></li>');
					});

          setInterval(function autoPlay() {
            FreshDirect.components.carousel.changePage(carousel, null, currentPage++);
            if(currentPage > numberOfPages) {
              currentPage = 0;
            }
          }, 5000);
			  }());
			</script>


      <style>
        .image-block a {
          text-decoration: none;
        }
        .image-block a img {
          height: 100%;
          width: 100%;
        }

        .image-block a .header {
          background-color: rgba(255,255,255,.9);
          bottom: 50px;
          color: black;
          font-size: 18px;
          height: 22px;
          line-height: 22px;
          margin: 0;
          padding: 0;
          padding-left: 5px;
          position: relative;
          text-align: left;
          text-transform: uppercase;
          width: 100%;
        }

        .image-block a .header:after {
          content: '\276F';
          position: relative;
          right: -5px;
        }
      </style>
			<script type="text/javascript">
					var oasBlock1 = {
							imageLink: '/media/images/navigation/fd_raf_for_fb.gif',
							link: '/browse.jsp?id=fru',
							header: 'test test test'
						};
			</script>

				<div class="image-block">
				</div>

			<script type="text/javascript">
				(function () {
					var $ = jQuery;
					$('.image-block').append(
						'<a href=' + oasBlock1.link + '>' +
						'<img src=' + oasBlock1.imageLink + '>' +
						'<h1 class="header">' + oasBlock1.header +'</h1>' +
						'</a>');
				}());
			</script>

        <soy:render template="common.contentModules" data="${welcomepagePotato}" />
						<div class="oas_home_bottom" id='oas_HPWideBottom'><script type="text/javascript">OAS_AD('HPWideBottom');</script></div>
					</div>
					<%-- Removed the learn more for marketing change. --%>
					<%-- <div id="bottom_link"><a href="/welcome.jsp"><img src="/media_stat/images/home/fd_logo_learn_more_back.jpg" alt="Learn More About Our Services"></a></div> --%>
				<%-- END MAIN CONTENT--%>

			</div>
			</fd:GetSegmentMessage>
		<% } %>

</tmpl:put>
</tmpl:insert>
<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.welcomepage = window.FreshDirect.welcomepage || {};

    window.FreshDirect.welcomepage.data = <fd:ToJSON object="${welcomepagePotato}" noHeaders="true"/>
    console.log(window.FreshDirect.welcomepage.data);
    </script>
    <jwr:script src="/fdlibs.js" useRandomParam="false" />
    <script>
      var $jq = FreshDirect.libs.$;
    </script>
    <jwr:script src="/fdmodules.js"  useRandomParam="false" />
    <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
  </body>
</html>
