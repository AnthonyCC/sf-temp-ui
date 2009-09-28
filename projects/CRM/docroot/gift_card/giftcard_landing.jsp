<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<% boolean isGuest = false; %>
	<crm:GetCurrentAgent id="currentAgent">
		<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %>
	</crm:GetCurrentAgent>
    
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Gift Card : Search</tmpl:put>
	<tmpl:put name='content' direct='true'>
	
	<%
		String gcId=request.getParameter("");
		String recEmail=request.getParameter("");
		if(gcId==null) gcId="";
		if(recEmail==null) recEmail="";
    %>
    
		<jsp:include page="/includes/giftcard_acct_nav.jsp" />
	 
		<crm:GiftCardController actionName='getSearchResults' resultName="result" successPage='/gift_card/giftcard_landing.jsp'>

			<script type="text/javascript">
			<!--
				function clearAll(){
					$("gcNumber").value = "";
					$("recEmail").value = "";
					$("certNumber").value = "";
				}
				function clearFields(e){
					e.each(function(e) { clearField(e); });
				}
				function clearField(e){
					$(e).value = "";
				}
			//-->
			</script>

			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td>
						<table cellspacing="2" cellpadding="2" border="0" width="100%" class="home_search_module_field">
						<form name="searchGC" id="searchGC">
						<tbody>
							<tr>				
								<td>Gift Card#:</td>
								<td><input type="text" style="width: 100px;" value="<%=gcId%>" name="gcNumber" id="gcNumber" /></td>
								<td>Recipient Email:</td>
								<td><input type="text" style="width: 200px;" value="<%=recEmail%>" name="recEmail" id="recEmail" /></td>
								<td><input type="submit" value="SEARCH GIFTCARD INFO" class="submit" /></td>
								<td><input type="button" value="CLEAR" class="submit" onclick="javascript:clearFields(Array('gcNumber','recEmail'));" /></td>
							</tr>
						</tbody>                 
						</form>
						</table>
					</td>
					<td>
						<table cellspacing="2" cellpadding="2" border="0" width="100%" class="home_search_module_field">
						<form name="order_search" method="POST" action="/main/order_search_results.jsp?search=quick">
						<tbody>
							<tr>
								<td align="bottom">Search GiftCard Orders :</td>					                    
								<td><input type="text" name="giftCardNumber" id="giftCardNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("giftCardNumber")) ? "" : request.getParameter("giftCardNumber") %>" /></td>
								<td><input type="submit" value="SEARCH GIFTCARD ORDERS" class="submit"></td>
								<td><input type="button" value="CLEAR" class="submit" onclick="javascript:clearField('giftCardNumber');" /></td>
							</tr>
						</tbody>                  
						</form>
						</table>
					</td>
				</tr>
			</table>

			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td>
						<div style="background: #fff; overflow: auto; width: 100%; height: 420px;">
							<%@ include file="/includes/gift_card/giftcards_list.jspf"%>
						</div>		
					</td>
				</tr>
			</table>



		</crm:GiftCardController>

	</tmpl:put>
</tmpl:insert>