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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_PAYMENT_INFO_TOTAL = 970;
%>

<%request.setAttribute("listPos", "CategoryNote");%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<% String actionName =  request.getParameter("actionName"); %>
<tmpl:insert template='/common/template/dnav.jsp'>
   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Payment Options</tmpl:put>
   <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="payment_info"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
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
<TABLE WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
    <TR>
        <TD width="<%= W_YA_PAYMENT_INFO_TOTAL %>" class="text11"><font class="title18">Payment Options</font><br><span class="space2pix"><br></span>
Update your payment information.<br>
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a>
<br><fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
        </td>
    </tr>
</table>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>
<!--  MP Use Case #4 -->
<!-- <script type="text/javascript" src="https://sandbox.masterpass.com/lightbox/Switch/integration/MasterPass.client.js"></script> -->
<script src="https://code.jquery.com/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/js/braintree-2.21.0.min.js"></script> 
<SCRIPT LANGUAGE=JavaScript>
	<!--
	OAS_AD('CategoryNote');
	//-->
</SCRIPT> 
<!--  PP Use Case  -->
 <script type="text/javascript" language="Javascript">

$( document ).ready(function(){
    $('#PP-connect').click(function() {
              console.log("-- Connect With Paypal click handler  --");
              $.ajax({
            	  			url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPSTART\",\"formdata\":{\"action\":\"PP_Connecting_Start\",\"ewalletType\":\"PP\"}}",
                            type: 'post',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            success: function(result){ 
                            	//var x = document.getElementById("PP_button");
                            	var deviceObj = "";
                    	    	braintree.setup(result.submitForm.result.eWalletResponseData.token, "custom", {
                    	    		  dataCollector: {
                    	    			    paypal: true
                    	    			  },
                    	    		  onReady: function (integration) {
                    	    			 // alert("integration.deviceData :"+integration.deviceData);
                    	    		    checkout = integration;
                    	    		    checkout.paypal.initAuthFlow();
                    	    		    deviceObj = JSON.parse(integration.deviceData);
                    	    		    
                    	    		  },
                    	    		  onPaymentMethodReceived: function (payload) {

                    	    	        $.ajax({
                    	                      url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPEND\",\"formdata\":{\"action\":\"PP_Connecting_End\",\"ewalletType\":" +
                    	                      		"\"PP\",\"origin\":\"your_account\",\"paymentMethodNonce\":\""+payload.nonce+"\",\"email\":\""+payload.details.email+"\",\"firstName\":\""+payload.details.firstName+"\"," +
                    	                      				"\"lastName\":\""+payload.details.lastName+"\" ,\"deviceId\":\""+deviceObj.correlation_id+"\"}}",
                    	                      type: 'post',
                    	                      success: function(id, result){
                    	                    	  $('#PP_logo').css("display","inline-block");
                    	                    	  $('#PP-connect').css("display","none");
                    	                    	  $('#pp_wallet_cards').append("<tr><td class=wallet-title-header>PayPal<div class=disconnectWallet onclick=deletePPWallet()><img src=/media_stat/images/common/delete-white-icon.png><span id=deleteMPWallet class=disconnect-cssbutton-green>UNLINK</span></div></td></tr><tr><td colspan=9><img src=/media_stat/images/layout/999966.gif width=970 height=1 border=0 vspace=3></td></tr>");
                    	                    	  $('#pp_wallet_cards').append("<tr height=14></tr><tr><td width=280 class=wallet-first-card><font class=text12> <img src=https://www.paypalobjects.com/webstatic/mktg/Logo/pp-logo-100px.png><br><br>"+id.submitForm.result.eWalletResponseData.paymentMethod.name+"<br>"+id.submitForm.result.eWalletResponseData.paymentMethod.emailID+"</font><br></td></tr>");
                    	                      }
                    	    	        });
                    	    		    

                    	    		  },
                    	    		  paypal: {
                    	    		    singleUse: false,
                    	    		    headless: true
                    	    		  }
                    	    		  
                    	    		});
                    	    			
                            }
       });
    });
    
}); 
 
function deletePPWallet(){
         $.ajax({
                      url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EPPDISC\",\"formdata\":{\"action\":\"PP_Disconnect_Wallet\",\"ewalletType\":\"PP\"}}",
                      type: 'post',
                      contentType: "application/json; charset=utf-8",
                      dataType: "json",
                      success: function(result1){ 
                    	  /* $('#pp_wallet_cards').css("display","none"); */
                    	    $('#pp_wallet_cards').empty();
                    	    $('#PP_logo').css("display","none");
                    	    $('#PP-connect').css("display","inline-block");
                      } 
                });
	
}

</script>  

<fd:GetStandingOrderDependencyIds id="standingOrderDependencyIds" type="paymentMethod">
     
<!--  MP Use Case #4 implemenation. Please use this component -->
      
       		<fd:GetStandingOrderHelpInfo id="helpSoInfo">
              <script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
              <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
              	<tr><td class="acc-payment-methods-title-header">ADD NEW PAYMENT METHOD</td></tr>
              	<tr><td colspan="9"><img src="/media_stat/images/layout/999966.gif" width="970" height="1" border="0" vspace="3"></td></tr>
              	<tr align="middle">
       				<td><a class="cssbutton green add-payment-methods-button" href="/your_account/add_creditcard.jsp">CREDIT CARD</a>
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
       							
       						<% } else { %>
              			<input id="PP-connect" type="image" src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Connect With Paypal">
              			<div id="PP_logo" style="display: none;">
       								<img src="/media_stat/images/paypal/My Account - PayPal logo.png" alt="Paypal Logo">
       								<div class="wallet-connected-indicator"><img src="/media_stat/images/common/link-small.png" alt="linked account"> LINKED</div>
       							</div>
              				<% } %>
              			<% } %>
       				</td>
       			</tr>
              </table>
              <% if (isPayPalWalletEnabled ) {%>
	              <br>
	              <table id="pp_wallet_cards" width="<%= W_YA_PAYMENT_INFO_TOTAL %>"  border="0" cellspacing="0" cellpadding="0">
	              
	               <%  if (paymentsPP!=null && paymentsPP.size() > 0){ 
	            	   for(ErpPaymentMethodI paymentM1 : paymentsPP) {
	            		   if(paymentM1 != null && paymentM1.geteWalletID()!= null && paymentM1.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){
	               %> 
	              
		              <tr><td class=wallet-title-header>PayPal
		              <% if (masqueradeContext == null) { %>
		              	<div class="disconnectWallet" onclick="deletePPWallet()"><img src=/media_stat/images/common/delete-white-icon.png><span id=deleteMPWallet class=disconnect-cssbutton-green>UNLINK</span></div>
		              <% } %>
		              </td></tr><tr><td colspan=9><img src=/media_stat/images/layout/999966.gif width=970 height=1 border=0 vspace=3></td></tr>
		              
		              <tr height=14></tr>
		              <tr><td width=280 class=wallet-first-card>
		              <font class=text12> <img src="https://www.paypalobjects.com/webstatic/mktg/Logo/pp-logo-100px.png">
		              <br><%=paymentM1.getName() %>
		              <br><%= paymentM1.getEmailID() %></font> 
		              <br></td>
		              </tr>              
	              
	               <%	
	               }
	               }
	               } %>
	              </table>
	            <% } %>  
              <br>
              <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
              <% if (isCheckEligible && !isECheckRestricted) { %>
              	 <script> 
               		$('#add-new-payment-method-checking-acc-disabled-master').css("display","none"); 
               		$('#add-new-payment-method-checking-acc').css("display","inline-block"); 
               	</script>  
                     <% if (hasCheck) { %>
              <tr valign="top">
                  <td><!-- <img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS"> --><span class="Container_Top_ChkAcctDetails">Checking Account Details</span> &nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')"><span class="Terms"> Terms of Use </span></a><br>
                  <IMG src="/media_stat/images/layout/999966.gif" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" height="1" border="0" VSPACE="3"><br>
                  </td>
              </tr>
              <tr valign="middle">
                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another checking account: <a href="/your_account/add_checkacct.jsp"><IMG src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" ALT="Add New Checking Account" border="0" ALIGN="absmiddle"></a></td>
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
                     <br><br>
              <% } %>
			</table>
			<br>
              <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top">
                  <td><!-- <img src="/media_stat/images/navigation/credit_card_details.gif"
		WIDTH="152" HEIGHT="15" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR> -->
		<span vspace="0" border="0" class="Container_Top_YourAccCCDetails">Credit Card Details</span><BR>
		    <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
		    </td>
              </tr>
              <tr valign="middle">
                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another credit card: <a href="/your_account/add_creditcard.jsp"><IMG src="/media_stat/images/buttons/add_new_credit_card.jpg" WIDTH="137" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
                     </td>
                     </tr>
                     <tr><td>
                           <form name=creditcard_form method="post">
                           <input type="hidden" name="actionName" value="">
                           <input type="hidden" name="deletePaymentId" value="">
                           <%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %>
                           </form>
                     </td></tr>
              </table>
              <br>
              <% if(user.isEbtAccepted()|| hasEBT){ %>
              <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top">
                  <td><img src="/media_stat/images/navigation/ebt_card_details.gif" WIDTH="119" HEIGHT="11" border="0" alt="EBT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR>
                  <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
                  </td>
              </tr>
              <%if(user.isEbtAccepted()){ %>
              <tr valign="middle">
                     <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another EBT card: <a href="/your_account/add_ebt_card.jsp"><IMG src="/media_stat/images/buttons/add_new_ebt_card.jpg" WIDTH="117" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
                     </td>
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
              <br>
              <IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
              <FONT CLASS="space4pix"><BR><BR></FONT>
              <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>">
              <TR VALIGN="TOP">
              <TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
              <TD WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL - 35 %>"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
              <BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
              </TR>
              </TABLE>
       </fd:GetStandingOrderHelpInfo>
</fd:GetStandingOrderDependencyIds>
<BR>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>
