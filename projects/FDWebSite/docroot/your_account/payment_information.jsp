<%@page import="com.freshdirect.fdstore.ewallet.EnumEwalletType"%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='com.freshdirect.common.context.MasqueradeContext' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<c:set var="sitePage" scope="request" value="/your_account/payment_information.jsp" />
<c:set var="listPos" scope="request" value="SystemMessage,CategoryNote" />
<%

	//expanded page dimensions
	final int W_YA_PAYMENT_INFO_TOTAL = 970;

	FDUserI mobweb_user = (FDUserI)session.getAttribute(SessionName.USER);
	String template = "/common/template/dnav.jsp";
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, mobweb_user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	if (mobWeb) {
		/* uncomment the following template re-define when mobweb is ready for real */
		//template = "/common/template/mobileWeb.jsp"; //mobWeb template
		String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com" : request.getAttribute("sitePage").toString();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
		}
	}
%>
<% String actionName =  request.getParameter("actionName"); %>
<tmpl:insert template='<%=template %>'>
<%--    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Payment Options</tmpl:put> --%>
   <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Account - Payment Options" pageId="payment_info"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
    <div class="content<%= (mobWeb) ? " mm-page" : "" %>">
		<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>

		<%
		boolean hasCheck = false;
		boolean hasEBT = false;
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		Collection paymentMethods = null;
		FDIdentity identity = null;
		boolean isECheckRestricted = false;
		boolean isPayPalWalletConnected  = false;
		boolean isPayPalWalletEnabled  = true;
		MasqueradeContext masqueradeContext = user.getMasqueradeContext();
		List<ErpPaymentMethodI> paymentsPP = new ArrayList<ErpPaymentMethodI>();
		if(user!=null  && user.getIdentity()!=null) {
		    identity = user.getIdentity();
		    paymentMethods = FDCustomerManager.getPaymentMethods(identity);
		    if(paymentMethods != null && !paymentMethods.isEmpty()){
		    	user.refreshFdCustomer();
		    	List<ErpPaymentMethodI> paymentsNew = new ArrayList<ErpPaymentMethodI>();
				Iterator payItr = paymentMethods.iterator();
				while (payItr.hasNext()) {
			  		ErpPaymentMethodI  paymentM = (ErpPaymentMethodI) payItr.next();
			  		if (paymentM.geteWalletID() == null) {
			 			paymentsNew.add(paymentM);
			        }
			  		if(paymentM.geteWalletID() != null && paymentM.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){
			        	boolean isValid = FDCustomerManager.disconnectInvalidPPAccount(paymentM , identity);
			        	if(isValid){
			        		paymentsPP.add(paymentM);
			        		isPayPalWalletConnected = true;
			        	}
			        }
			 		if (EnumPaymentMethodType.ECHECK.equals(paymentM.getPaymentMethodType())) {
			            	hasCheck = true;
			        }else if (EnumPaymentMethodType.EBT.equals(paymentM.getPaymentMethodType())) {
			            	hasEBT = true;
			        }
				}
				paymentMethods = paymentsNew;
		    }
		    isECheckRestricted = FDCustomerManager.isECheckRestricted(identity);
		    // Check Whether PayPal EWallet status
		    isPayPalWalletEnabled = FDCustomerManager.getEwalletStatusByType(EnumEwalletType.PP.getName());
		}
		boolean isCheckEligible	= user.isCheckEligible();
		// MP Use Case #4
		/* ErpCustEWalletModel erpCustEWalletModel = FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(), "MP"); */

		%>

		<!-- error message handling here -->
		<table role="presentation" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%= W_YA_PAYMENT_INFO_TOTAL %>" class="text11">
					<span class="title18">Payment Options</span><br /><span class="space2pix"><br /></span>
					Update your payment information.<br />
					<%@ include file="/includes/debitSwitchNotice.jsp" %>
					<br />
					<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="errortext"><%=errorMsg%></span></fd:ErrorHandler>
				</td>
			</tr>
		</table>

		<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><br />
		<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><br />
		<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><br /><br />
		<!--  MP Use Case #4 -->
		<!-- <script type="text/javascript" src="https://sandbox.masterpass.com/lightbox/Switch/integration/MasterPass.client.js"></script> -->

		<script type="text/javascript" src="https://js.braintreegateway.com/js/braintree-2.21.0.min.js"></script>
		<div id='oas_CategoryNote' ad-fixed-size="true" ad-size-height="95" ad-size-width="774">
		  <SCRIPT LANGUAGE=JavaScript>
		  	<!--
		  	OAS_AD('CategoryNote');
		  	//-->
		  </SCRIPT>
		</div>
		<!--  PP Use Case  -->


		<fd:GetStandingOrderDependencyIds id="standingOrderDependencyIds" type="paymentMethod">

		     <input type="hidden" name="isPayPalDown" id= "isPayPalDown" value="false">

		<!--  MP Use Case #4 implemenation. Please use this component -->

		       		<fd:GetStandingOrderHelpInfo id="helpSoInfo">
		              <script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
		              <table role="presentation" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		              	<tr><td class="acc-payment-methods-title-header">ADD NEW PAYMENT METHOD</td></tr>
		              	<tr><td colspan="9"><img src="/media_stat/images/layout/999966.gif" alt="" width="970" height="1" border="0" vspace="3"></td></tr>
		              	<tr align="middle">
		       				<td>
		       				<div>
		              		<div id="PP_ERROR" class="wallet-error-msg" style="display:none">
		  						<img src='/media_stat/images/masterpass/mpwarning.png'> <span>Cannot Connect to PayPal at this time.</span>
		  					</div>
		              	</div>
		       				<a class="cssbutton green add-payment-methods-button" href="/your_account/add_creditcard.jsp">DEBIT OR CREDIT CARD</a>
		       					<div id="add-new-payment-method-checking-acc-disabled-master">
		       						<a id="add-new-payment-method-checking-acc-disabled" class="cssbutton green add-payment-methods-button disabled" href="#" onclick="return false;">CHECKING ACCOUNT</a>
		       						<div id="add-new-payment-method-checking-acc-disabled-indicator">Available after first order.</div>
		       					</div>
		       					<a id="add-new-payment-method-checking-acc" class="cssbutton green add-payment-methods-button" href="/your_account/add_checkacct.jsp" style="display: none;">CHECKING ACCOUNT</a>
		       					<% if (isPayPalWalletEnabled && masqueradeContext == null) {
		       						if (isPayPalWalletConnected ) { %>
		       			  				<!--  <input type="image" src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Connect With Paypal" id="PP_logo" disabled> -->
		       							<div id="PP_logo">
		       								<img src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Paypal Logo">
		       								<div class="wallet-connected-indicator"><img src="/media_stat/images/common/link-small.png" alt="linked account"> LINKED</div>
		       							</div>
		       							<input id="PP-connect" type="image" src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Connect With Paypal" style="display: none;">
		       							<input type="hidden" name="isPayPalWalletConnected" id= "isPayPalWalletConnected" value=<%=isPayPalWalletConnected%>
		       						<% } else { %>
		       						         <div id="PP_ERROR_WRAPPER">
			              						<!-- <div id="PP_ERROR" class="wallet-error-msg" style="display: none;">
						    						<img src='/media_stat/images/masterpass/mpwarning.png'> <span>Cannot Connect to PayPal at this time.</span>
						    					</div> -->
			              						<input id="PP-connect" type="image" src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Connect With Paypal">
		              						</div>
		              				<% } %>
		              			<% } %>
		       				</td>
		       			</tr>
		              </table>
						<%
							/* this display code seriously needs a re-write, one card per paypal row? */
							boolean hidePPDefaultSelectRadio = true;
							if ((request.getRequestURI().toLowerCase().indexOf("your_account/payment_information.jsp")>-1)) {
								hidePPDefaultSelectRadio = false;
							}
						%>
						<% if (isPayPalWalletEnabled) {%>
							<br />
							<table role="presentation" id="pp_wallet_cards" width="<%= W_YA_PAYMENT_INFO_TOTAL %>"  border="0" cellspacing="0" cellpadding="0">
								<% if (paymentsPP!=null && paymentsPP.size() > 0){
									for(ErpPaymentMethodI paymentM1 : paymentsPP) {
										if(paymentM1 != null && paymentM1.geteWalletID()!= null && paymentM1.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){
											String paymentM1PKId = ((ErpPaymentMethodModel)paymentM1).getPK().getId();
											 %>
											<tr>
												<td colspan="2" class="wallet-title-header">
													PayPal
												</td>
											</tr>
											<tr>
												<td colspan="2"><img src=/media_stat/images/layout/999966.gif alt="" width="970" height="1" border="0" vspace="3" /></td>
											</tr>
											<tr>
												<td width="20" class="chooser_radio">
												<% if ( FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user) && !hidePPDefaultSelectRadio) { %>
													<input
														type="<%=hidePPDefaultSelectRadio?"hidden":"checkbox"%>"
														id="paymentIsDefault_<%=paymentM1PKId%>"
														name="paymentDef"
														class="paymentDef"
														<%= ( ((paymentM1PKId).equals(user.getFDCustomer().getDefaultPaymentMethodPK())) ? " checked" : "" ) %>
														data-paymentid="<%= paymentM1PKId %>"
														data-isdefault="<%= ((paymentM1PKId).equals(user.getFDCustomer().getDefaultPaymentMethodPK())) %>"
														data-type="<%= paymentM1.getCardType().getDisplayName() %>"
														data-isdebit="<%= paymentM1.isDebitCard() %>"
														data-lastfour="XXXX"
													>
													<label for="paymentIsDefault_<%=paymentM1PKId%>"><span class="offscreen"></span></label>
												<% } %>
												</td>
												<td width="" class="text14 wallet-first-card">
													<img src="https://www.paypalobjects.com/webstatic/mktg/Logo/pp-logo-100px.png" alt="PayPal" /><br />
													<%=paymentM1.getName() %><br />
													<%= paymentM1.getEmailID() %><br />
												</td>
											</tr>
											<tr>
												<td width="20"></td>
												<td class="wallet-title-footer">
													<% if (masqueradeContext == null) { %>
														<button class="cssbutton green small disconnectWallet disconnect-cssbutton-green" id="deletePPWallet"><img src="/media_stat/images/common/delete-white-icon.png" alt="unlink" />UNLINK</button>
													<% } %>
												</td>
											</tr>
									<%
										}
									}
								} %>
							</table>
						<% } %>
		              <br />
		              <table role="presentation" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		              <% if (isCheckEligible && !isECheckRestricted) { %>
		              	 <script>
		               		$jq('#add-new-payment-method-checking-acc-disabled-master').css("display","none");
		               		$jq('#add-new-payment-method-checking-acc').css("display","inline-block");
		               	</script>
		                     <% if (hasCheck) { %>
		              <tr valign="top">
		                  <td><!-- <img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS"> --><span class="Container_Top_ChkAcctDetails">Checking Account Details</span> &nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')"><span class="Terms"> Terms of Use </span></a><br />
		                  <IMG src="/media_stat/images/layout/999966.gif" alt="" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" height="1" border="0" VSPACE="3"><br />
		                  </td>
		              </tr>
		              <tr valign="middle">
		                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another checking account: <a href="/your_account/add_checkacct.jsp" class="cssbutton green small">Add New Checking Account</a></td>
		                     </tr>
		                     <tr><td>
		                           <form name=checkacct_form method="post">
		                           <input type="hidden" name="actionName" value="">
		                           <input type="hidden" name="deletePaymentId" value="">
		                           <%@ include file="/includes/ckt_acct/checkacct_select.jspf" %>
		                           </form>
		                     </td>
		              </tr>
		              		<% } %>
		                    <%--  <% } else {%>
		                           <%@ include file="/includes/your_account/add_checkacct.jspf"%>
		                     <% } %> --%>
		                     <br /><br />
		              <% } %>
					</table>
					<br />
		              <table role="presentation" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		              <tr valign="top">
		                  <td><!-- <img src="/media_stat/images/navigation/credit_card_details.gif"
				WIDTH="152" HEIGHT="15" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<br /> -->
				<span vspace="0" border="0" class="Container_Top_YourAccCCDetails">Card Details</span><br />
				    <IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><br />
				    </td>
		              </tr>
		              <tr valign="middle">
		                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another card: <a class="cssbutton green small" href="/your_account/add_creditcard.jsp">ADD A NEW DEBIT OR CREDIT CARD</a>
		                     </td>
		                     </tr>
		                     <tr><td>
		                           <form name="creditcard_form" id="creditcard_form" method="post">
		                           	<input type="hidden" name="actionName" value="">
		                           	<input type="hidden" name="deletePaymentId" value="">
		                           	<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %>
		                           </form>
		                     </td></tr>
		              </table>
		              <br />
		              <% if(user.isEbtAccepted()|| hasEBT){ %>
		              <table role="presentation" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		              <tr valign="top">
		                  <td><img src="/media_stat/images/navigation/ebt_card_details.gif" WIDTH="119" HEIGHT="11" border="0" alt="EBT CARD DETAILS">&nbsp;&nbsp;&nbsp;<br />
		                  <IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><br />
		                  </td>
		              </tr>
		              <%if(user.isEbtAccepted()){ %>
		              <tr valign="middle">
		                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another EBT card: <a class="cssbutton green small" href="/your_account/add_ebt_card.jsp">ADD NEW EBT CARD</a>
		                     </tr>
		                     <% } %>
		                     <tr><td>
		                           <form name=ebtcard_form method="post">
		                           	<input type="hidden" name="actionName" value="">
		                           	<input type="hidden" name="deletePaymentId" value="">
		                           	<%@ include file="/includes/ckt_acct/i_ebtcard_select.jspf" %>
		                           </form>
		                     </td></tr>
		              </table>
		              <% } %>
		              <br />
		              <IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><br />
		              <FONT CLASS="space4pix"><br /><br /></FONT>
		              <TABLE role="presentation" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>">
		              <TR VALIGN="TOP">
		              <TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
                       CONTINUE SHOPPING
                      <BR>from <FONT CLASS="text11bold">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
		              </TR>
		              </TABLE>
		       </fd:GetStandingOrderHelpInfo>
		</fd:GetStandingOrderDependencyIds>
		<br />
		</fd:PaymentMethodController>
</div>
</tmpl:put>

<tmpl:put name="extraJs">
	<script>
		var checkout;
		(function($) { /* isolate for $ use */
			$(document).ready(function() { /* this MUST be in doc ready (or after) */
				if ($('#isPayPalWalletConnected').val() !== 'true') {
					// Brain Tree setup-- Start
					brainTreeSetup($);
					// Brain Trees setup -- END
				}
			});

			$(document).on('click', '#PP-connect', function(event) {
				if (event.preventDefault) {
					event.preventDefault();
				} else {
					event.returnValue = false;
				}
				if (checkout != null) {
					checkout.paypal.initAuthFlow();
				}
				if ($('#isPayPalWalletConnected').val() === 'true') {
					$('#PP_ERROR').css("display", "inline-block");
				}else{
					$('#PP_ERROR').css("display", "none");
				}
			});

			$(document).on('click', '#deletePPWallet', function() {
				$.ajax({
					url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EPPDISC\",\"formdata\":{\"action\":\"PP_Disconnect_Wallet\",\"ewalletType\":\"PP\"}}",
					type: 'post',
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					success: function(result1) {
						/* $('#pp_wallet_cards').css("display","none"); */
						$('#pp_wallet_cards').empty();
						$('#PP_logo').css("display", "none");
						$('#PP-connect').css("display", "inline-block");
						$("#isPayPalWalletConnected").val("false");
						$('#PP_ERROR').css("display", "none");
						brainTreeSetup($);
						//reload page so default gets selected
						location.reload(true);
					}
				});
			});
		}(jQuery));

		function brainTreeSetup($){
			$.ajax({
				url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPSTART\",\"formdata\":{\"action\":\"PP_Connecting_Start\",\"ewalletType\":\"PP\"}}",
				type: 'post',
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(result) {
					if (result.submitForm.success) {
						$('#PP_ERROR').css("display", "none");
						$("#isPayPalDown").val("false");
						//var x = document.getElementById("PP_button");
						var deviceObj = "";
						if (window.hasOwnProperty('braintree')) {
							braintree.setup(result.submitForm.result.eWalletResponseData.token, "custom", {
								dataCollector: {
									paypal: true
								},
								onReady: function(integration) {
									// alert("integration.deviceData :"+integration.deviceData);
									checkout = integration;
									// checkout.paypal.initAuthFlow();
									deviceObj = JSON.parse(integration.deviceData);

								},
								// For saving the PayPal payment Method to FD
								onPaymentMethodReceived: function(payload) {

									$.ajax({
										url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPEND\",\"formdata\":{\"action\":\"PP_Connecting_End\",\"ewalletType\":" +
											"\"PP\",\"origin\":\"your_account\",\"paymentMethodNonce\":\"" + payload.nonce + "\",\"email\":\"" + payload.details.email + "\",\"firstName\":\"" + payload.details.firstName + "\"," +
											"\"lastName\":\"" + payload.details.lastName + "\" ,\"deviceId\":\"" + deviceObj.correlation_id + "\"}}",
										type: 'post',
										success: function(id, result) {
											location.reload(true);
										}
									});
								},
								paypal: {
									singleUse: false,
									headless: true
								}
							});
						}
					} else {
						$("#isPayPalDown").val("true");
					}
				}
			});
		}
	</script>
</tmpl:put>
<tmpl:put name="extraJsModules"><%-- use extraJsModules so the common modules load first --%>
	<jwr:script src="/youraccount.js" useRandomParam="false" />
</tmpl:put>

<tmpl:put name="extraCss" direct="true">
	<jwr:style src="/assets/css/your_account/paymentinfo.css" media="all" />
</tmpl:put>
<tmpl:put name="soytemplates"><soy:import packageName="youraccount"/></tmpl:put>
</tmpl:insert>
