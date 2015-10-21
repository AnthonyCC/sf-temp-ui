<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>

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
if(user!=null  && user.getIdentity()!=null) {
    identity = user.getIdentity();
    paymentMethods = FDCustomerManager.getPaymentMethods(identity);
    
    List<ErpPaymentMethodI> paymentsNew = new ArrayList<ErpPaymentMethodI>();
	
	Iterator payItr = paymentMethods.iterator();
	while (payItr.hasNext()) {
  		ErpPaymentMethodI  paymentM = (ErpPaymentMethodI) payItr.next();
  		if (paymentM.geteWalletID() == null) {
 			paymentsNew.add(paymentM);
        }
 		if (EnumPaymentMethodType.ECHECK.equals(paymentM.getPaymentMethodType())) {
            	hasCheck = true;
        }else if (EnumPaymentMethodType.EBT.equals(paymentM.getPaymentMethodType())) {
            	hasEBT = true;
        }
	}
	
	paymentMethods = paymentsNew;
    isECheckRestricted = FDCustomerManager.isECheckRestricted(identity); 
	
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
<!-- <script src="https://code.jquery.com/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="https://sandbox.masterpass.com/lightbox/Switch/integration/MasterPass.client.js"></script> -->
<SCRIPT LANGUAGE=JavaScript>
	<!--
	OAS_AD('CategoryNote');
	//-->
</SCRIPT> 
<!--  MP Use Case #4 -->
<!-- <script type="text/javascript" language="Javascript">

$( document ).ready(function(){
    $('#MP-connect').click(function() {
              console.log("-- Connect With MasterPass click handler  --");
              $.ajax({
                            url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EMP\",\"formdata\":{\"action\":\"MP_Connect_Start\",\"ewalletType\":\"MP\"}}",
                            type: 'post',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            success: function(result1){ 
                     MasterPass.client.connect({       
                    	 "pairingRequestToken":result1.submitForm.result.eWalletResponseData.pairingToken,
                    	 "callbackUrl":result1.submitForm.result.eWalletResponseData.callbackUrl,
                    	 "merchantCheckoutId":result1.submitForm.result.eWalletResponseData.eWalletIdentifier,
                         "requestToken":result1.submitForm.result.eWalletResponseData.token,
                         "requestExpressCheckout":true,
                         "requestedDataTypes":["CARD"],
                         "allowedCardTypes":["master,amex,discover,visa"],
                         "requestPairing":true,
                         "version":"v6"
                         
                                  });
                            }
       });
    });
    
}); 

function deleteMPWallet(){
        $.ajax({
                      url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EMP\",\"formdata\":{\"action\":\"MP_Disconnect_Ewallet\",\"ewalletType\":\"MP\"}}",
                      type: 'post',
                      contentType: "application/json; charset=utf-8",
                      dataType: "json",
                      success: function(result1){ 
                             $('#mp_wallet_cards').css("display","none");
                             $('#MP_logo').css("display","none");
                             $('#MP-connect').css("display","inline-block");
                      }
 });
}


function loadWalletInfo(){
	console.log("I am in loadWalletInfo()....");
              $.ajax({
                            url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EMP\",\"formdata\":{\"action\":\"MP_All_PayMethod_In_Ewallet_MyAccount\",\"ewalletType\":\"MP\"}}",
                            type: 'post',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            success: function(result1){ 
                            	if(result1.submitForm.result.eWalletResponseData.paymentDatas.size() != 0){
                                 $('#mp_wallet_cards').append("<tr><td class=wallet-title-header>MasterPass<div class=disconnectWallet onclick=deleteMPWallet()><img src=/media_stat/images/common/delete-white-icon.png><span id=deleteMPWallet class=disconnect-cssbutton-green>DISCONNECT</span></div></td></tr><tr><td colspan=9><img src=/media_stat/images/layout/999966.gif width=970 height=1 border=0 vspace=3></td></tr>");
                                 $.each(result1.submitForm.result.eWalletResponseData.paymentDatas, function(i, item) {
								    console.log("-- Load wallet cards --");
								    if(i % 3 == 0 ){
								    	$('#mp_wallet_cards').append("<tr height=14></tr><tr><td width=280 class=wallet-first-card><font class=text12> <img src=https://www.mastercard.com/mc_us/wallet/img/en/US/mp_acc_046px_gif.gif><br>XXXX-"+item.accountNumber+" Exp. "+item.expiration+"<br><br><b>Name on card</b><br>"+item.nameOnCard+"</font><br></td></tr>");
								    } else{
								    	$('#mp_wallet_cards tr:last-child').append("<td width=20><img src=/media_stat/images/layout/clear.gif width=20 height=1></td><td width=1 bgcolor=#CCCCCC><td width=34><img src=/media_stat/images/layout/clear.gif width=14 height=1></td><img src=/media_stat/images/layout/cccccc.gif width=1 height=1></td><td width=280><font class=text12> <img src=https://www.mastercard.com/mc_us/wallet/img/en/US/mp_acc_046px_gif.gif><br>XXXX-"+item.accountNumber+" Exp. "+item.expiration+"<br><br><b>Name on card</b><br>"+item.nameOnCard+"</font><br></td>");
								    }
                                	
                               	});
                                 } else{
                                	 $('#MP_logo').css("display","none");
                                     $('#MP-connect').css("display","inline-block");
                                 }
                                 	 
                                   

                          }
       });
}
</script>  -->

<fd:GetStandingOrderDependencyIds id="standingOrderDependencyIds" type="paymentMethod">
	<fd:GetStandingOrderHelpInfo id="helpSoInfo">
		<script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
		<% if (isCheckEligible && !isECheckRestricted) { %>
		
			<% if (hasCheck) { %>
		<table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
		    <td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS">&nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')">Terms of Use</a><br>
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
		</table>
			<% } else {%>
				<%@ include file="/includes/your_account/add_checkacct.jspf"%> 
			<% } %>
			<br><br>
		<% } %>
		<table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
		    <td><img src="/media_stat/images/navigation/credit_card_details.gif"
		WIDTH="152" HEIGHT="15" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR>
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
      
<!--  MP Use Case #4 implemenation. Please use this component -->
      
<%--        		<fd:GetStandingOrderHelpInfo id="helpSoInfo"> --%>
<%--               <script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script> --%>
<%--               <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0"> --%>
<!--               	<tr><td class="acc-payment-methods-title-header">ADD NEW PAYMENT METHOD</td></tr> -->
<!--               	<tr><td colspan="9"><img src="/media_stat/images/layout/999966.gif" width="970" height="1" border="0" vspace="3"></td></tr> -->
<!--               	<tr align="middle"> -->
<!--        				<td><a class="cssbutton green add-payment-methods-button" href="/your_account/add_creditcard.jsp">CREDIT CARD</a> -->
<!--        					<div id="add-new-payment-method-checking-acc-disabled-master"> -->
<!--        						<a id="add-new-payment-method-checking-acc-disabled" class="cssbutton green add-payment-methods-button disabled" href="#" onclick="return false;">CHECKING ACCOUNT</a> -->
<!--        						<div id="add-new-payment-method-checking-acc-disabled-indicator">Available after first order.</div> -->
<!--        					</div> -->
<!--        					<a id="add-new-payment-method-checking-acc" class="cssbutton green add-payment-methods-button" href="/your_account/add_checkacct.jsp" style="display: none;">CHECKING ACCOUNT</a> -->
<%--        					<% if (erpCustEWalletModel != null && erpCustEWalletModel.getLongAccessToken() != null) { %> --%>
<!--        			<input type="image" src="https://www.mastercard.com/mc_us/wallet/img/mcpp_wllt_btn_chk_166x038px.png" alt="Connect With MasterPass" id="MP_logo" disabled> -->
<!--        							<div id="MP_logo"> -->
<!--        								<img src="https://www.mastercard.com/mc_us/wallet/img/en/US/mp_mc_acc_050px_gif.gif" alt="MasterPass Logo" onload="loadWalletInfo()"> -->
<!--        								<div class="wallet-connected-indicator"><img src="/media_stat/images/common/checked-circle.png" alt="checked circle"> CONNECTED</div> -->
<!--        							</div> -->
<!--        							<input id="MP-connect" type="image" src="https://www.mastercard.com/mc_us/wallet/img/en/US/mp_connect_with_button_034px.png" alt="Connect With MasterPass" style="display: none;"> -->
       							
<%--        						<% } else { %> --%>
<!--               			<input id="MP-connect" type="image" src="https://www.mastercard.com/mc_us/wallet/img/en/US/mp_connect_with_button_034px.png" alt="Connect With MasterPass"> -->
<%--               				<% } %> --%>
<!--        				</td> -->
<!--        			</tr> -->
<!--               </table><br> -->
<%--               <table id="mp_wallet_cards" width="<%= W_YA_PAYMENT_INFO_TOTAL %>"  border="0" cellspacing="0" cellpadding="0"></table><br> --%>
<%--               <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0"> --%>
<%--               <% if (isCheckEligible && !isECheckRestricted) { %> --%>
<!--               	<script>  -->
<!--               		$('#add-new-payment-method-checking-acc-disabled-master').css("display","none"); -->
<!--                		$('#add-new-payment-method-checking-acc').css("display","inline-block"); -->
<!--               	</script> -->
<%--                      <% if (hasCheck) { %> --%>
<!--               <tr valign="top"> -->
<!--                   <td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS">&nbsp;&nbsp;&nbsp;<a href="javascript:popup('/registration/checkacct_terms.jsp','large')">Terms of Use</a><br> -->
<%--                   <IMG src="/media_stat/images/layout/999966.gif" width="<%= W_YA_PAYMENT_INFO_TOTAL %>" height="1" border="0" VSPACE="3"><br> --%>
<!--                   </td> -->
<!--               </tr> -->
<!--               <tr valign="middle"> -->
<!--                      <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another checking account: <a href="/your_account/add_checkacct.jsp"><IMG src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" ALT="Add New Checking Account" border="0" ALIGN="absmiddle"></a></td> -->
<!--                      </tr> -->
<!--                      <tr><td> -->
<!--                            <form name=checkacct_form method="post"> -->
<!--                            <input type="hidden" name="actionName" value=""> -->
<!--                            <input type="hidden" name="deletePaymentId" value=""> -->
<%--                            <%@ include file="/includes/ckt_acct/checkacct_select.jspf" %> --%>
<!--                            </form> -->
<!--                      </td> -->
<!--               </tr> -->
<%--                      <% } else {%> --%>
<%--                            <%@ include file="/includes/your_account/add_checkacct.jspf"%>  --%>
<%--                      <% } %> --%>
<!--                      <br><br> -->
<%--               <% } %> --%>
<!-- 			</table>  -->
<%--               <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0"> --%>
<!--               <tr valign="top"> -->
<!--                   <td><img src="/media_stat/images/navigation/credit_card_details.gif" -->
<!--               WIDTH="152" HEIGHT="15" border="0" alt="CREDIT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR> -->
<%--                   <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR> --%>
<!--                   </td> -->
<!--               </tr> -->
<!--               <tr valign="middle"> -->
<!--                      <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another credit card: <a href="/your_account/add_creditcard.jsp"><IMG src="/media_stat/images/buttons/add_new_credit_card.jpg" WIDTH="137" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a> -->
<!--                      </td> -->
<!--                      </tr> -->
<!--                      <tr><td> -->
<!--                            <form name=creditcard_form method="post"> -->
<!--                            <input type="hidden" name="actionName" value=""> -->
<!--                            <input type="hidden" name="deletePaymentId" value=""> -->
<%--                            <%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %> --%>
<!--                            </form> -->
<!--                      </td></tr> -->
<!--               </table> -->
<!--               <br> -->
<%--               <% if(user.isEbtAccepted()|| hasEBT){ %> --%>
<%--               <table width="<%= W_YA_PAYMENT_INFO_TOTAL %>" border="0" cellspacing="0" cellpadding="0"> --%>
<!--               <tr valign="top"> -->
<!--                   <td><img src="/media_stat/images/navigation/ebt_card_details.gif" WIDTH="119" HEIGHT="11" border="0" alt="EBT CARD DETAILS">&nbsp;&nbsp;&nbsp;<BR> -->
<%--                   <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0" VSPACE="3"><BR> --%>
<!--                   </td> -->
<!--               </tr> -->
<%--               <%if(user.isEbtAccepted()){ %> --%>
<!--               <tr valign="middle"> -->
<!--                      <td class="text11" style="padding-top: 5px; padding-bottom: 10px;">If you need to enter another EBT card: <a href="/your_account/add_ebt_card.jsp"><IMG src="/media_stat/images/buttons/add_new_ebt_card.jpg" WIDTH="117" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a> -->
<!--                      </td> -->
<!--                      </tr> -->
<%--                      <% } %> --%>
<!--                      <tr><td> -->
<!--                            <form name=ebtcard_form method="post"> -->
<!--                            <input type="hidden" name="actionName" value=""> -->
<!--                            <input type="hidden" name="deletePaymentId" value=""> -->
<%--                            <%@ include file="/includes/ckt_acct/i_ebtcard_select.jspf" %> --%>
<!--                            </form> -->
<!--                      </td></tr> -->
<!--               </table> -->
<%--               <% } %> --%>
<!--               <br> -->
<%--               <IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>" HEIGHT="1" BORDER="0"><BR> --%>
<!--               <FONT CLASS="space4pix"><BR><BR></FONT> -->
<%--               <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL %>"> --%>
<!--               <TR VALIGN="TOP"> -->
<!--               <TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD> -->
<%--               <TD WIDTH="<%= W_YA_PAYMENT_INFO_TOTAL - 35 %>"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a> --%>
<!--               <BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD> -->
<!--               </TR> -->
<!--               </TABLE> -->
<%--        </fd:GetStandingOrderHelpInfo> --%>
</fd:GetStandingOrderDependencyIds>
<BR>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>
