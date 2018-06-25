<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.customer.EnumSaleType" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="userCOSuccess" guestAllowed="false" recognizedAllowed="false" />
<%  //--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/expressco/checkout/");
	request.setAttribute("listPos", "SystemMessage"); // TODO

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, userCOSuccess) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/expressco/includes/ec_template.jsp";
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		String oasSitePage = request.getAttribute("sitePage").toString();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}

	boolean isDp2018 = false; /* set further down where order context is available already */
	boolean isMod56 = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.modOrderConfirmPageRedesign, userCOSuccess);

	//based on step_4_receipt.jsp
	boolean _modifyOrderMode = false; 	
	String _ordNum = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	
	if(session.getAttribute("MODIFIED" + _ordNum) != null && session.getAttribute("MODIFIED" + _ordNum).equals(_ordNum)) {
		_modifyOrderMode = true;
	}
%>
<potato:singlePageCheckoutSuccess />
<tmpl:insert template='<%= pageTemplate %>'>
	<tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/expressco.js" useRandomParam="false" />

		<script>
			(function (fd) {
				if(fd.expressco && fd.expressco.data && fd.expressco.data.redirectUrl){
					fd.common.dispatcher.signal('redirectUrl', fd.expressco.data.redirectUrl);
				}
			}(FreshDirect));
		</script>
		<% if (isMod56) { %>
			<script type="text/javascript">
				$jq(document).ready(function() {
					/* init ordermodifystatus with orderId */
					FreshDirect.components.ordermodifystatus.init('${param['orderId']}');
					/* start polling in the background */
					FreshDirect.components.ordermodifystatus.startPolling();
				});


				$jq('.csPhoneNumber').each(function(i,e) {
					$jq(e).text( ($jq(e).text().match(/[0-9\-]{14}/)||[''])[0] );
					$jq(e).parent().show();
				});

				$jq('.mod56 .orderconfirmed-title').html((<%=_modifyOrderMode%> && '${param['soId']}' === '') ? 'Changes Saved' : 'Order Confirmed');

				$jq('.mod56 #cartcontent').on('cartData', function(e, data) {
					if ((data.modifyCartData.cutoffTime || '') !== '') {
						$jq('.modify-order-before-label').show();
						$jq('.modify-order-before-value').html(data.modifyCartData.cutoffTime);
					}
				});

				FreshDirect.GTMDATAS = {
					modify_order_btn: {
						SUCCESS : {'event': 'modify-click','eventCategory': 'modify','eventAction': 'enter modify mode','eventLabel': 'order confirmation modify'},
						ERROR: {'event': 'modify-click','eventCategory': 'modify','eventAction': 'error','eventLabel': 'order confirmation modify error'}
					},
					delivery_info_edit_btn: {
						SUCCESS : {'event': 'modify-click', 'eventCategory': 'modify', 'eventAction': 'enter modify mode', 'eventLabel': 'order confirmation modify edit delivery'},
						ERROR: {'event': 'modify-click','eventCategory': 'modify','eventAction': 'error','eventLabel': 'order confirmation modify edit delivery error'}
					},
					cart_details_edit_btn: {
						SUCCESS : {'event': 'modify-click', 'eventCategory': 'modify', 'eventAction': 'enter modify mode', 'eventLabel': 'order confirmation modify edit cart'},
						ERROR: {'event': 'modify-click','eventCategory': 'modify','eventAction': 'error','eventLabel': 'order confirmation modify edit cart error'}
					}
				};
				$jq('.mod56 [data-gtm-click]').on('click', function(e) {
					var $e = $jq(e.currentTarget);
					if ($e.attr('data-gtm-click-error') !== undefined) {
						e.preventDefault();
						dataLayer.push(fd.GTMDATAS[$e.attr('id')].ERROR || {});
						
						//show overlay
						FreshDirect.components.ordermodifystatus.open($e);

						return false;
					} else {
						dataLayer.push(fd.GTMDATAS[$e.attr('id')].SUCCESS || {});
					}
				});
				$jq('#modify_order_btn').on('click', function(e) {
					if ($jq(e.currentTarget).attr('data-gtm-click-error')===undefined) {
						window.location = '/your_account/modify_order.jsp?orderId=${param['orderId']}&action=modify';
					}
				});
				$jq('#delivery_info_edit_btn').on('click', function(e) {
					if ($jq(e.currentTarget).attr('data-gtm-click-error')===undefined) {
						window.location = '/your_account/modify_order.jsp?orderId=${param['orderId']}&action=modify&successPage=%2Fexpressco%2Fcheckout.jsp';
					}
				});
				$jq('#cart_details_edit_btn').on('click', function(e) {
					if ($jq(e.currentTarget).attr('data-gtm-click-error')===undefined) {
						window.location = '/your_account/modify_order.jsp?orderId=${param['orderId']}&action=modify&successPage=%2Fexpressco%2Fview_cart.jsp%23cartcontent';
					}
				});
			</script>
		<% } %>
	</tmpl:put>

	<tmpl:put name="globalnav">
		<% if (isMod56) { %><%-- MOD-56 redesign --%>
			<div class="container globalnav_top mod56">
				<%-- OAS --%>
				<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
				<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
				<div id="oas_SystemMessage">
		  			<script type="text/javascript">OAS_AD('SystemMessage');</script>
		  		</div>
				<div id="oas_DFPSystemMessage" ad-size-width='970' ad-size-height='80' class="container">
					<script type="text/javascript">OAS_AD('DFPSystemMessage');</script>
				</div>
				<div class="nav">
					<a class="logo" href="<%= FDURLUtil.getLandingPageUrl(userCOSuccess) %>"><img src="/media/layout/nav/globalnav/fdx/logo.png" alt="FreshDirect"></a>

					<div class="inline fl-right small-text csPhoneNumber-cont" style="display: none;">
						<strong>Need Help?</strong>&nbsp;<span class="csPhoneNumber"><fd:IncludeMedia name="<%= userCOSuccess.getCustomerServiceContactMediaPath() %>" /></span>
					</div>
					<br style="clear: both;">
				</div>
			</div>
		<% } else { %>
			<%@ include file="/common/template/includes/globalnav.jspf" %>
		<% } %>
	</tmpl:put>

	<tmpl:put name="bottomnav">
		<% if (isMod56) { %><%-- MOD-56 redesign --%>
			<div class="container mod56">
				<div class="footer">
					<div class="footer-links">
						<a href="/index.jsp?serviceType=HOME">Home</a>
						&nbsp;&nbsp;<span style="color: #999999"><b>|</b></span>
				&nbsp;&nbsp;<a href="/your_account/manage_account.jsp" fd-login-required>Your Account</a>
						
						&nbsp;&nbsp;<span style="color: #999999"><b>|</b></span>
						&nbsp;&nbsp;<a href="/help/index.jsp">Help/FAQ</a>
						&nbsp;&nbsp;<span style="color: #999999"><b>|</b></span>
						&nbsp;&nbsp;<a href="/help/index.jsp?trk=bnav">Contact Us</a>
					</div>
					<div class="footer-copyright">
						<%@ include file="/shared/template/includes/copyright.jspf" %><br />
					</div>
					<div class="footer-after-copyright">
						<fd:IncludeMedia name="/media/layout/nav/globalnav/footer/after_copyright_footer.ftl">
							<img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt="" /><br />
							<a href="/help/privacy_policy.jsp">Privacy Policy</a>
							&nbsp;<span style="color: #999999">|</span>
							&nbsp;<a href="/help/terms_of_service.jsp">Customer Agreement</a>
							&nbsp;<span style="color: #999999">|</span>
							&nbsp;<a href="/help/platform_agreement.jsp">Platform Terms of Use</a>
						</fd:IncludeMedia>
					</div>
				</div>
			</div>
		<% } else { %>
			<%@ include file="/common/template/includes/footer.jspf" %>
		<% } %>
	</tmpl:put>

	<tmpl:put name="ecpage">success</tmpl:put>

	<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag title="Success" pageId="ec_success"></fd:SEOMetaTag>
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<% if (_ordNum != null) { %>
			<fd:GetOrder id='order' saleId='<%=_ordNum%>'>
				<script type="text/javascript">
					var cmFired = false, cmRetry = 60, cmRetryDelay = 500; /* retry for 30 secs */
					function fireCm() {
						if (!cmFired && cmRetry>0) {
							try {
								if (FreshDirect.utils.isDeveloper()) {
									console.log('Trying CM calls');
								}
								<fd:CmShop9 order="<%=order%>"/>
								<fd:CmOrder order="<%=order%>"/>
								<fd:CmRegistration force="true"/>
								<fd:CmConversionEvent eventId="became_a_customer"/>
								<% if(_modifyOrderMode){ %>
									<fd:CmConversionEvent order="<%=order%>" orderModified="true"/>
								<% } %>
								cmFired = true;
							} catch (e) {
								if (FreshDirect.utils.isDeveloper()) {
									console.log(e);
								}
								cmRetry--;
								setTimeout("fireCm()", cmRetryDelay);
							}
						} else {
							cmRetry--;
							setTimeout("fireCm()", cmRetryDelay);
						}
					}
					fireCm();
				</script>
				<% 
					isDp2018 = (FDStoreProperties.isDlvPassStandAloneCheckoutEnabled() && order.getOrderType().equals(EnumSaleType.SUBSCRIPTION));
				%>
			</fd:GetOrder>
		<% } %>
		<% if (isDp2018) { %><%-- DP2018 redesign --%>
      <div class="dpn-content">
        <%@ include file="/includes/deliverypasssuccess.jsp" %>
      </div>
      <%-- we need this to report checkout success to GTM/GA --%>
      <div style="display:none;" id="cartcontent" data-ec-linetemplate="expressco.successlines" data-drawer-disabled data-ec-request-uri="/api/expresscheckout/cartdata?orderId=${param['orderId']}"></div>
		<% } else if (isMod56) { %><%-- MOD-56 redesign --%>
			<div id='successpage' class="mod56"><%-- class to allow css override --%>
				<div class="container">
					<%-- header, left col --%>
					<soy:render template="expressco.successpageMod56" data="${singlePageCheckoutSuccessPotato.successPageData}" />
					<%-- right col --%>
					<div class="rightCol">
						<soy:render template="expressco.standingorderInfoMod56" data="${singlePageCheckoutSuccessPotato.successPageData}" />

						<div class="section delivery-info">
							<div class="colHeader delivery-info-header"><h2>Delivery Info</h2><button id="delivery_info_edit_btn" class="cssbutton small green edit-btn" data-gtm-click data-gtm-click-error>Edit<span class="offscreen"> Delivery Info</span></button></div>
							
							<div class="subsection delivery-info-delivery-window">
								<div class="delivery-info-delivery-window-label">DELIVERY WINDOW</div>
								<div class="drawer__previewitem">
									<soy:render template="expressco.timeslot" data="${singlePageCheckoutSuccessPotato.timeslot}" />
								</div>
							</div>

							<div class="subsection delivery-info-address">
								<div class="delivery-info-address-label">ADDRESS</div>
								<div data-drawer-default-content="address">
									<soy:render template="expressco.addresspreview" data="${singlePageCheckoutSuccessPotato.address}" />
								</div>
							</div>
							
							<div class="subsection delivery-info-payment-method">
								<div class="delivery-info-payment-method-label">PAYMENT METHOD</div>
								<div data-drawer-default-content="payment">
									<soy:render template="expressco.paymentmethodpreviewNew" data="${singlePageCheckoutSuccessPotato.payment}" />
								</div>
							</div>
						</div>
						
						<div class="section cart-details">
							<%-- cart content --%>
							<div class="colHeader cart-details-header"><h2>Cart Details</h2><button id="cart_details_edit_btn" class="cssbutton small green edit-btn" data-gtm-click data-gtm-click-error>Edit<span class="offscreen"> Cart Details</span></button></div>
							<div id="cartcontent" data-ec-linetemplate="expressco.successlines" data-drawer-disabled data-ec-request-uri="/api/expresscheckout/cartdata?orderId=${param['orderId']}"></div>
						</div>
					</div>
					<% if (mobWeb) { %>
						<soy:render template="expressco.estTotalNoteAndContactContainerMod56" data="${singlePageCheckoutSuccessPotato.successPageData}" />
					<% } %>
				</div>
			</div>
		<% } else { %><%-- pre MOD-56 design --%>
			<div id='successpage'>
				<div class="container">
					<div id="order-summary">
						<soy:render template="expressco.successpage" data="${singlePageCheckoutSuccessPotato.successPageData}" />
					</div>

					<%-- drawer --%>
					<div id="ec-drawer" drawer-id="ec-drawer">
						<soy:render template="expressco.drawer" data="${singlePageCheckoutSuccessPotato.drawer}" />
					</div>
					<div data-drawer-content-prerender="ec-drawer">
						<div data-drawer-default-content="address">
							<soy:render template="expressco.addresspreview" data="${singlePageCheckoutSuccessPotato.address}" />
						</div>
						<div data-drawer-default-content="timeslot">
							<div class="drawer__previewitem">
								<soy:render template="expressco.timeslot" data="${singlePageCheckoutSuccessPotato.timeslot}" />
							</div>
						</div>
						<div data-drawer-default-content="payment">
							<soy:render template="expressco.paymentmethodpreviewNew" data="${singlePageCheckoutSuccessPotato.payment}" />
						</div>
					</div>

					<%-- cart content --%>
					<h2 class="inyourorder">In Your Order</h2>
					<div id="cartcontent" data-ec-linetemplate="expressco.successlines" data-drawer-disabled data-ec-request-uri="/api/expresscheckout/cartdata?orderId=${param['orderId']}"></div>
				</div>
			</div>
		<% } %>
		<script>
			// potato loading
			window.FreshDirect.expressco = {};
			window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutSuccessPotato}" noHeaders="true"/>
		</script>
	</tmpl:put>
</tmpl:insert>
