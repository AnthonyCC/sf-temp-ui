<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<% boolean isGuest = false; %>

<crm:GetCurrentAgent id="currentAgent">
	<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %> 
</crm:GetCurrentAgent>

<% boolean hasCustomerCase = CrmSession.hasCustomerCase(session); %>

<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Gift Card : Added/Used</tmpl:put>
    <tmpl:put name='content' direct='true'>
	
		<jsp:include page="/includes/giftcard_nav.jsp" />

		<fd:RedemptionCodeController actionName="noaction" result="redemptionResult">

			<script language="javascript">

				var xmlhttp;
				var url_base="/gift_card/giftcard_usedorders.jsp";

				function showHint(str) {

					if (str.length==0) {
						document.getElementById("txtHint").innerHTML="";
						return;
					}

					xmlhttp=GetXmlHttpObject();

					if (xmlhttp==null) {
						alert ("Your browser does not support XMLHTTP!");
						return;
					}

					url=url_base+"?certNum="+str;

					xmlhttp.onreadystatechange=stateChanged;
					xmlhttp.open("GET",url,true);
					xmlhttp.send(null);
				}

				function showHint2(str) {

					if (str.length==0)
					  {
					  document.getElementById("txtHint2").innerHTML="";
					  return;
					  }
						xmlhttp=GetXmlHttpObject();
					if (xmlhttp==null)
					  {
					  alert ("Your browser does not support XMLHTTP!");
					  return;
					  }
					url=url_base+"?certNum="+str;
					xmlhttp.onreadystatechange=stateChanged2;
					xmlhttp.open("GET",url,true);
					xmlhttp.send(null);
				}


				function stateChanged()
				{
				if (xmlhttp.readyState==4)
				  {  
				  document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
				  }
				}


				function stateChanged2()
				{
				if (xmlhttp.readyState==4)
				  {  
				  document.getElementById("txtHint2").innerHTML=xmlhttp.responseText;
				  }
				}


				function GetXmlHttpObject()
				{
				if (window.XMLHttpRequest)
				  {
				  // code for IE7+, Firefox, Chrome, Opera, Safari
				  return new XMLHttpRequest();
				  }
				if (window.ActiveXObject)
				  {
				  // code for IE6, IE5
				  return new ActiveXObject("Microsoft.XMLHTTP");
				  }
				return null;
				}

			</script>


			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_tableBody">
				<tr>
					<td width="50%" class="gc_table_footer">
						<fd:GiftCardController actionName='applyGiftCard' result='result' successPage='/gift_card/giftcard_addused.jsp'>
							<fd:ErrorHandler result='<%=result%>' name='invalid_card' id='errorMsg'>
								<%@ include file="/includes/i_error_messages.jspf" %>   
							</fd:ErrorHandler>
							<fd:ErrorHandler result="<%=result%>" name="card_in_use" id="errorMsg">
								<%@ include file="/includes/i_error_messages.jspf" %>   
							</fd:ErrorHandler>
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1footer">
								<tr valign="middle">
									<td align="center">
										<% if(hasCustomerCase){ %> 
											<form method="post" style="padding:0px;margin:0px;">
												Add Gift Card, enter code:&nbsp;<input type="text"name="givexNum" />&nbsp;&nbsp;&nbsp;<input type="submit" name="gcApplyCode" id="gcApplyCode" value="ADD" class="button" />
											</form>
										<% }else{ %>
											Add Gift Card, enter code:&nbsp;<input type="text"name="givexNum" />&nbsp;&nbsp;&nbsp;<input type="submit" name="gcApplyCode" id="gcApplyCode" value="ADD" class="button disabled" DISABLED />
										<% } %>
									</td>
								</tr>
							</table>
						</fd:GiftCardController>
					</td>
					<td width="50%" class="gc_table_footer">
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table3footer">
						<form name="order_search" method="POST" action="/main/order_search_results.jsp?search=quick">
							<tr valign="middle">
								<td align="center">
									Search Order with Gift Card:&nbsp;<input type="text" style="width: 150px;" name="giftCardNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("giftCardNumber")) ? "" : request.getParameter("giftCardNumber") %>" />&nbsp;&nbsp;&nbsp;                                      
                                    <input type="submit" name="" value="SEARCH" class="button" />
								</td>
							</tr>
						</form>
						</table>
					</td>
				</tr>
			</table>
				
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_tableBody">
				<tr>
					<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
				<tr>
					<th colspan="2" class="gc_colHeader_i">Gift Cards added to account</th>
					<th colspan="2" class="gc_colHeader_i">Gift Cards used and removed from account</th>
				</tr>
				<tr>
					<td width="25%" valign="top">
						<fd:GetGiftCardReceived id='giftcards' showAll='true'>
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1_i">
							<tr class="gc_colHeader">
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td width="85">Certificate #</td>
								<td width="80">Balance</td>
                                <td  width="85">Status</td>                                
								<td>Options</td>
								<td>Purchased</td>
							</tr>
							<logic:iterate id="giftcard" collection="<%= giftcards %>" type="com.freshdirect.fdstore.giftcard.FDGiftCardModel" indexId="gcRemCounter1">
							<%
								Integer counterTmp = (Integer) pageContext.getAttribute("gcRemCounter1");
							%>
							<tr valign="top" <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td><a href="javascript:showHint('<%= giftcard.getCertificateNumber() %>')">  <%= giftcard.getCertificateNumber() %></a></td>
                                <td class="gc_balance"><% if(giftcard.isRedeemable()){ %>$<%= giftcard.getFormattedBalance() %>&nbsp;&nbsp;<% } %></td>
                                <td><% if(giftcard.isRedeemable() && giftcard.getBalance() > 0) {%>
                                    Active
                                <%
                                 } else { %>
                                    <%= giftcard.isRedeemable() ? "Redeemed" : "Cancelled" %>
                                    <%}%>
                                </td>                                
                                <td><% if(hasCustomerCase){ %> <a href="<%= request.getRequestURI() %>?action=deleteGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=true" class="rLink">remove</a><% } else { %> remove <% } %></td>
								<td><%= giftcard.getPurchaseSaleId() %></td>							
							</tr>
							</logic:iterate>
							<% if(!hasCustomerCase){ %>
								<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td></tr>
								<tr><td colspan="5"><b>Note: Case Required to remove a Gift Card.</b></td></tr>
							<% } %>
						</table>
						</fd:GetGiftCardReceived>  
					</td>
					<td width="25%" valign="top">
						<span id="txtHint" class="gc_table1_i">
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1_i">
								<tr class="gc_colHeader">
									<td>&nbsp;</td>
								</tr>
							</table>
						</span>
					</td>
					<td width="25%" valign="top">          
						<fd:GetDeletedGiftCard id='giftcards'>
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table2_i">
							<tr class="gc_colHeader">
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td width="85">Certificate #</td>
								<td width="80">Balance</td>						
								<td>Purchased</td>
							</tr>
							<logic:iterate id="giftcard" collection="<%= giftcards %>" type="com.freshdirect.giftcard.ErpGiftCardModel" indexId="gcDelCounter1">
							<%
								Integer counterTmp = (Integer) pageContext.getAttribute("gcDelCounter1");
							%>
							<tr valign="top" <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td><a href="javascript:showHint2('<%= giftcard.getCertificateNumber() %>')">  <%= giftcard.getCertificateNumber() %></a></td>
								<td>$<%= giftcard.getBalance() %></td>							
								<td><%= giftcard.getPurchaseSaleId() %></td>							
							</tr>
							</logic:iterate>
						</table>
						</fd:GetDeletedGiftCard>        
					</td>
					<td width="25%" valign="top">
						<span id="txtHint2" class="gc_table2_i">
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table2_i">
								<tr class="gc_colHeader">
									<td>&nbsp;</td>
								</tr>
							</table>
						</span>
					</td>
				</tr>
				<tr>
					<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
			</table>
		</fd:RedemptionCodeController>  
	</tmpl:put>
</tmpl:insert>