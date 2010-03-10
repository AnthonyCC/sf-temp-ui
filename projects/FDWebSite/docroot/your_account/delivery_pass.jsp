
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.deliverypass.EnumDPAutoRenewalType' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<script language="javascript">
    	function redirectToSignup() {
		
	    var form = document.forms['signup'];
	    form.elements['action'].value='signup';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}
    	
    	function flipAutoRenewalOFF() {
	    var form = document.forms['autoRenew'];
	    form.elements['action'].value='FLIP_AUTORENEW_OFF';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}
    	function flipAutoRenewalON() {
	    var form = document.forms['autoRenew'];
	    form.elements['action'].value='FLIP_AUTORENEW_ON';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}

</script>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - FreshDirect DeliveryPass</tmpl:put>
    <tmpl:put name='content' direct='true'>
    <fd:DlvPassSignupController result="result" callCenter="false">
	<fd:ErrorHandler result='<%=result%>' name='dlvpass_discontinued' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
    
        <fd:WebViewDeliveryPass id='viewContent'>
        	
		<table width="675" align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="2" class="text11">
					<font class="title18">FreshDirect DeliveryPass</font>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<%= viewContent.getHeaderInfo() %><br>
					<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
					<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
					<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>
					
				</td>
			</tr>
			<tr>
				<td colspan="2" class="text13">
					<font class="text13bold"><%= viewContent.getPassName() %></font>
					<font class="text13"><%= viewContent.getDetailInfo() %></font><br><br>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<%
					
					if (viewContent.getDescription() != null) { %>

						<FONT CLASS="space2pix"><BR></FONT>

						<fd:IncludeMedia name="<%= viewContent.getDescription().getPath() %>" />

						<br>

					<%} else { %>
						DeliveryPass offers you a new way to save on delivery fees. Place as many orders as you'd like while your pass is active, all for a low, one-time fee for delivery.
					<%}%>
				</td>
			</tr>
			<%
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			EnumDlvPassStatus status = user.getDeliveryPassStatus();
                  EnumDPAutoRenewalType arType=user.hasAutoRenewDP();
                  if(user.getDlvPassInfo().getAutoRenewUsablePassCount()==0)  {
				arType=EnumDPAutoRenewalType.NONE;
                  } 

			if(user.isEligibleForDeliveryPass() && (user.getUsableDeliveryPassCount()==FDStoreProperties.getMaxDlvPassPurchaseLimit()) &&(EnumDPAutoRenewalType.NONE.equals(arType))) { %>
			<form name="signup" method="POST">
			<input type="hidden" name="action" value="">
				<tr>
					<td colspan="2">
						<b>DeliveryPass Refills </b>&nbsp;You have <%=DeliveryPassUtil.getAsText(user.getUsableDeliveryPassCount()-1)%>  DeliveryPass refills on your account. A refill will go into effect automatically when your current pass runs out. You can keep up to two refills in your account at a time. 
						<br><br><br>
					</td>
				</tr>	
			</form>
			<%} else if (user.isEligibleForDeliveryPass() && (user.getUsableDeliveryPassCount()>1)&&(EnumDPAutoRenewalType.NONE.equals(arType)) ) {%>
			<form name="signup" method="POST">
			<input type="hidden" name="action" value="">
				<tr>
					<td colspan="2">
					<b>DeliveryPass Refills </b>&nbsp;You have <%=DeliveryPassUtil.getAsText(user.getUsableDeliveryPassCount()-1)%> DeliveryPass refill
  on your account. Now you can purchase a refill DeliveryPass which renews automatically! Simply 
						<A HREF="#" onClick="javascript:redirectToSignup()">purchase an additional FreshDirect DeliveryPass</A> and you'll never need to worry about running out.

						<br><br><br>
					</td>
				</tr>	
			</form>
			<%} else if (user.isEligibleForDeliveryPass() && (user.getUsableDeliveryPassCount()==1)&&(EnumDPAutoRenewalType.NONE.equals(arType))&& (user.getDlvPassInfo().getAutoRenewUsablePassCount()==0)) {%>
			<form name="signup" method="POST">
			<input type="hidden" name="action" value="">
				<tr>
					<td colspan="2">
						<b>DeliveryPass Refills </b>&nbsp;You do not have any DeliveryPass refills on your account. Now you can purchase a refill DeliveryPass which renews automatically! Simply
						<A HREF="#" onClick="javascript:redirectToSignup()">purchase an additional DeliveryPass</A> and you'll never need to worry about running out.

						<br><br><br>
					</td>
				</tr>	
			</form>
                  <%}else if (user.isEligibleForDeliveryPass() && (user.getUsableDeliveryPassCount()==0)&&(EnumDPAutoRenewalType.NONE.equals(arType))) {%>
			<form name="signup" method="POST">
			<input type="hidden" name="action" value="">
				<tr>
					<td colspan="2">
						<br>
						<A HREF="#" onClick="javascript:redirectToSignup()"><font class="text11bold">Click here</font></A> to sign up for DeliveryPass today!
					</td>
				</tr>	
			</form>
                  <%}%>

		      <% if(EnumDPAutoRenewalType.YES.equals(arType) || EnumDPAutoRenewalType.NO.equals(arType)) {%>
                        <% if(user.getUsableDeliveryPassCount()>1) {%>
				<tr>
					<td colspan="2">
						You have <%=DeliveryPassUtil.getAsText(user.getUsableDeliveryPassCount()-1)%> 
						<% if(user.getUsableDeliveryPassCount()>2) {%>

                                    	<b>DeliveryPass refills</b>
						<%} else {%>
	                                    <b>DeliveryPass refill</b>
						<%}%> on your account. A refill will go into effect when your current membership expires.<br><br><br>

						
					</td>
				</tr>
				<%}%>	
				<tr>
					<td colspan="2">

							
						

						<% if(EnumDPAutoRenewalType.YES.equals(arType)) {%>

							<% if(user.getDlvPassInfo().getAutoRenewUsablePassCount()>0) {%>
								<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><br>	

								<form name="autoRenew" method="POST">
								<% if(!DeliveryPassUtil.getAutoRenewalDate(user).equals("")) {%>
									<font class="text12bold">DeliveryPass Renewal</font>
									<font class="text12"> Your membership will be renewed automatically on the day your current membership expires.
									<A HREF="javascript:pop('/about/aboutRenewal.jsp?sku=<%=user.getDlvPassInfo().getAutoRenewDPType().getCode()%>&term=<%=user.getDlvPassInfo().getAutoRenewDPTerm()%>',400,560)">	
										Click here to learn more about renewals.
									</A>
									<br><br>Your DeliveryPass membership is set to renew on <%=DeliveryPassUtil.getAutoRenewalDate(user)%>.
								<%} %>

								
									<input type="hidden" name="action" value="">
									<A HREF="#" onClick="javascript:flipAutoRenewalOFF()"><font class="text12">Click here to turn renewal OFF.</A>
								</form>
								<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><br><br>	
                                          <%} else if (user.getUsableDeliveryPassCount()==0) {%>
								<form name="signup" method="POST">
									<input type="hidden" name="action" value="">
									<br><A HREF="#" onClick="javascript:redirectToSignup()"><font class="text11bold">Click here</font></A> to sign up for FreshDirect DeliveryPass today!
								</form>
							<% }%>
						<%} else {%>
							<% if(user.getDlvPassInfo().getAutoRenewUsablePassCount()>0) {%>
								<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3">
								<br><br>	


								<font class="text12bold">DeliveryPass Renewals</font>
								<font class="text12"> Your membership can be renewed automatically when your current DeliveryPass (or refill) expires.
								<A HREF="javascript:pop('/about/aboutRenewal.jsp?sku=<%=user.getDlvPassInfo().getAutoRenewDPType().getCode()%>&term=<%=user.getDlvPassInfo().getAutoRenewDPTerm()%>&price=<%=user.getDlvPassInfo().getAutoRenewPriceAsText()%>',400,560)">								Click here to learn more about renewals.
								</A><br><br>
								<form name="autoRenew" method="POST">
									<% if(!DeliveryPassUtil.getExpDate(user).equals("")) {%>
									<b>Your DeliveryPass membership will expire on <%=DeliveryPassUtil.getExpDate(user)%>.<b>
								<%}%>
								
									<input type="hidden" name="action" value="">
									<A HREF="#" onClick="javascript:flipAutoRenewalON()"><font class="text12bold">Click here to turn renewal ON.</A>
								</form>
								<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><br><br>	
                                          <%} else if (user.getUsableDeliveryPassCount()==0){%>
								<form name="signup" method="POST">
									<input type="hidden" name="action" value="">
									<br><A HREF="#" onClick="javascript:redirectToSignup()"><font class="text11bold">Click here</font></A> to sign up for FreshDirect DeliveryPass today!
								</form>
							<% }%>

						<%}%>



						</font>
						
						
					  

					</td>
				</tr>	
				<b>


			<%}%>
 
			<%
			if(viewContent.getUsageAndPurchaseInfo() != null) { %>
			<tr>
				<td colspan="2">
					<b><%= viewContent.getUsageAndPurchaseInfo() %><b><br><br><br>
				</td>
			</tr>	
			<%}%>
			
			<%
			if(viewContent.getId() != null) { %>
			<tr>
				<td colspan="2">

				<fd:GetOrdersByDeliveryPass deliveryPassId='<%= viewContent.getId() %>' id='orderHistoryInfo'>
					<% int rowCounter = 0; %>

					<TABLE WIDTH="500" BORDER="0" CELLPADDING="0" CELLSPACING="0" align="center">				
						<tr>
							<td class="text10bold" bgcolor="#DDDDDD" WIDTH="75">Order #</td>
							<td class="text10bold" bgcolor="#DDDDDD" WIDTH="135">&nbsp;&nbsp;&nbsp;&nbsp;Delivery Date</td>
					                <td class="text10bold" bgcolor="#DDDDDD" WIDTH="135">Delivery Type</td>
							<td class="text10bold" bgcolor="#DDDDDD" WIDTH="75" align="right">Order Total</td>
							<td bgcolor="#DDDDDD"><img src="/media_stat/images/layout/clear.gif" width="40" height="1" alt="" border="0"></td>
							<td class="text10bold" bgcolor="#DDDDDD" WIDTH="90">Order Status</td>
						</tr>
					<logic:iterate id="orderInfo" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
						<tr bgcolor="<%= (rowCounter++ % 2 == 0) ? "#FFFFFF" : "#EEEEEE" %>">
							<td><a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>"><%= orderInfo.getErpSalesId() %></a></td>
							<td class="text10">&nbsp;&nbsp;&nbsp;&nbsp;<%= CCFormatter.defaultFormatDate( orderInfo.getRequestedDate() ) %></td>
					                <td class="text10"><%= orderInfo.getDeliveryType().getName() %></td>
							<td class="text10" align=right><%= CCFormatter.formatCurrency( orderInfo.getTotal() ) %>&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td></td>
							<td><%= orderInfo.getOrderStatus().getDisplayName() %></td>
						</tr>	
					</logic:iterate>
					</TABLE ><br><br><br>
				</fd:GetOrdersByDeliveryPass>
					
				</td>
			</tr>	
			<%}%>
                  
			
 
			
			<tr>
				<td colspan="2">
				<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3">			
				</td>
			</tr>	
		</table>
        </fd:WebViewDeliveryPass>
</fd:DlvPassSignupController>        
	<table width="675" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" colspan="2">
				<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="20" BORDER="0"><BR>
<A HREF="javascript:pop('/shared/template/generic_popup.jsp?contentPath=/media/editorial/picks/deliverypass/dp_tc.html&windowSize=large&name=Delivery Pass Information',400,560)">
				<font class="text11bold">Click here to learn more about FreshDirect DeliveryPass.</font>
				</A>
			</td>
		</tr>				
	</table>

	<BR><BR><IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR><BR>
	<FONT CLASS="space4pix"></FONT>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
	<TR VALIGN="TOP">
	<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
	<TD WIDTH="640"  class="text11" ><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
	<BR>from <A HREF="/index.jsp"><b>Home Page</b></A><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
	</TR>
	</TABLE>
	<BR>
    </tmpl:put>
</tmpl:insert>

