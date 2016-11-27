<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%request.setAttribute("listPos", "ProductNote,CategoryNote");%>
<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Pay using your banking account</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<% 
	String addCheckPage = "/your_account/add_checkacct.jsp";
	String fromPage = request.getParameter("from"); 
		if ("checkout".equalsIgnoreCase(fromPage)) {
			addCheckPage = "/checkout/step_3_checkacct_add.jsp";
		}
	%>
	<script>
	function linkTo(url){
		redirectUrl = "http://" + location.host + url;
		parent.opener.location = redirectUrl;
	}
	</script>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class="text12">
				<img src="/media_stat/images/headers/pay_with_banking_acct.gif" width="309" height="17" alt="PAY WITH BANKING ACCOUNT" vspace="5"><br />
				<strong>Paying from your banking account has great benefits!</strong><br />
				<ul>
					<li>No need to worry about credit card expiration</li>
					<li>Avoid the monthly interest on credit card debt</li>
				</ul>
				<strong>Did you know?</strong> While every order paid with a credit card costs FreshDirect a fee, the costs associated with banking accounts are much lower. As more customers switch to e-check payments, we save money and can pass the savings on to you.<br />
				<br />
					<SCRIPT LANGUAGE=JavaScript>
						<!--
						OAS_AD('CategoryNote');
						//-->
					</SCRIPT>

				<div align="center"><a href="javascript:linkTo('<%=addCheckPage%>'); window.reallyClose();" target="_parent"><img src="/media_stat/images/buttons/sign_up_now.gif" width="104" height="17" border="0" alt="SIGN UP NOW"></a></div>
				<br />
				<div align="center"><hr style="width: 75%;" /><br /></div>
				<div class="text11"><strong>Please note:</strong> To pay for your order from a banking account you must have received and paid for one order and there must be a valid credit card attached to your FreshDirect customer account. In the event that sufficient funds are not available in your banking account, FreshDirect will charge the full amount of the order to your credit card. There is a $25 fee for insufficient funds. FreshDirect cannot accept paper checks. FreshDirect reserves the right to withdraw the online check payment option from any account for any reason at any time. For details, see full terms of use in the <a href="javascript:popup('/registration/user_agreement.jsp?show=terms&backTo=/pay_by_check.jsp','large')">FreshDirect Customer Agreement</a>.</div>
				<br />
				<SCRIPT LANGUAGE=JavaScript>
					<!--
					OAS_AD('ProductNote');
					//-->
				</SCRIPT>
			</td>
		</tr>
	</table>
	<br />
	</tmpl:put>
</tmpl:insert>
