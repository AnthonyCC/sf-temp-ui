<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%request.setAttribute("listPos", "ProductNote,CategoryNote");%>
<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>

	<tmpl:insert template='/common/template/large_pop.jsp'>
		
		<tmpl:put name='title' direct='true'>FreshDirect - Pay by check online!</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<% 
		String addCheckPage = "/your_account/add_checkacct.jsp";
		String fromPage = request.getParameter("from"); 
			if ("checkout".equalsIgnoreCase(fromPage)) {
				addCheckPage = "/checkout/step_3_checkacct_add.jsp";
			}
		%>

		<table cellpadding="0" cellspacing="0" border="0">
		<script>
		function linkTo(url){
			redirectUrl = "http://" + location.host + url;
			parent.opener.location = redirectUrl;
		}
		</script>
			<tr>
				<td class="text12">
<img src="/media_stat/images/headers/pay_by_check.gif" width="112" height="15" alt="PAY BY CHECK ONLINE" vspace="5"><br>
Now FreshDirect's <b>online check-payment service</b> gives you another easy option for payment.
<br><br>
<b>As Easy as Paying by Credit Card</b><br>
It's simple to set up and use&mdash;just enter your account and routing number one time. When you check out, select your checking account as the payment method and we'll take it from there. The amount you owe will be automatically deducted from your account by your bank. Of course, all of your private information is <b>kept</b> <a href="javascript:popup('/help/faq_home_pop.jsp?page=security&backTo=/pay_by_check.jsp','large')">private</a>.
<br><br>
	<SCRIPT LANGUAGE=JavaScript>
		<!--
		OAS_AD('CategoryNote');
		//-->
	</SCRIPT>

<div align="center"><a href="javascript:linkTo('<%=addCheckPage%>'); window.close();" target="_parent"><img src="/media_stat/images/buttons/sign_up_now.gif" width="104" height="17" border="0" alt="SIGN UP NOW"></a></div>
<br>
Please note: To pay for your order from a checking account you must have received and paid for one order and there must be a valid credit card attached to your FreshDirect customer account. In the event that sufficient funds are not available in your checking account, FreshDirect will charge the full amount of the order to your credit card. There is a $25 fee for bounced checks. FreshDirect cannot accept paper checks. FreshDirect reserves the right to withdraw the online check payment option from any account for any reason at any time. For details, see full terms of use in the <a href="javascript:popup('/registration/user_agreement.jsp?show=terms&backTo=/pay_by_check.jsp','large')">FreshDirect Customer Agreement</a>.
<br><br>
	<SCRIPT LANGUAGE=JavaScript>
		<!--
		OAS_AD('ProductNote');
		//-->
	</SCRIPT>
</td>
			</tr>
		</table>
		<br>
		</tmpl:put>
	</tmpl:insert>
