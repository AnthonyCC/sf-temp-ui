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

<% boolean isGuest = false; %>
<crm:GetCurrentAgent id="currentAgent">
	<%-- isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); --%> 
</crm:GetCurrentAgent>
    
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Gift Card : Generate New</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<%
			boolean hasCustomerCase = CrmSession.hasCustomerCase(session);
			String action=request.getParameter("tempAction");
			if(action==null) action="";
		%>
	
		<jsp:include page="/includes/giftcard_nav.jsp" />
 
		<crm:GiftCardController actionName='generateGiftCard' resultName="result">
		<%
			String gcId=(String)request.getAttribute("newGivexNum"); 
			String message=(String)request.getAttribute("waitMessage"); 
			String tmpSaleId=(String)request.getAttribute("saleId"); 
			 
			if(gcId==null) gcId="";
			if(message==null) message="";
			if(tmpSaleId==null) tmpSaleId="";
		%>

		<table class="gc_tableBody" cellspacing="0" cellpadding="0" width="100%" border="0">
			<tr>
				<th style="padding: 5px;">
					Note : This page takes more than 30 seconds to load, so be patient
				</th>
			</tr>
			<tr>
				<td align="center">
					<% if(action.trim().length()==0  || (gcId!=null && gcId.trim().length()>0)) { %>
						<table cellspacing="2" cellpadding="2" border="0" width="60%">
						<tbody>
							<tr>
								<td width="33%" align="right">Generate New GiftCard Number #: </td>
								<td width="33%">
									<input type="text" style="width: 200px;" class="input_text" value="<%=gcId%>" name="newGiftcard" disabled />
								</td>
								<td width="33%">
									<% if(gcId!=null && gcId.trim().length()>0){ %>
										<a href="/gift_card/bt_giftcard.jsp?toGCAccount=<%=gcId%>" style="font-weight: bold;">click to trasfer the balance to this card</a>
									<% }else{ %>
										&nbsp;
									<% } %>
								</td>
							</tr>
							<% if(gcId==null || gcId.trim().length()==0) { %>
							<tr>
								<td colspan="3" align="center">
									<br />
									<% if(hasCustomerCase){  %> 
										<form name="generateGC" id="generateGC" method="post">
											<input type="hidden" name="tempAction" value="generateGiftCard" />
											<input type="submit" value="Generate GiftCard Number" class="submit" />
										</form>
									<% }else{ %>
										<b>(Note : Case Required to generate new Gift Card)</b>&nbsp;
									<% } %>
								</td>
							</tr>
							<% } %>
						</tbody>
						</table>
					<% }else{ %>
						<table class="gc_tableBody" cellspacing="0" cellpadding="0" width="60%" border="0">
							<tr>
								<td>
									<font color="red"><%=message%></font>
								</td>
							</tr>
						</table>
						<fd:ErrorHandler result='<%=result%>' name='service_unavailable' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%=result%>' name='generate_error' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>

						<form name="generateGC2" id="generateGC2" method="get">
						<input type="hidden" name="tempAction" value="getGiftCard" />

							<table cellspacing="2" cellpadding="2" border="0" width="60%">
								<tbody>
									<% if(gcId==null || gcId.trim().length()==0) { %>
										<tr>				
											<td width="50%" align="right">Enter SaleId : </td>
											<td width="50%">
												<input type="text" style="width: 200px;" class="input_text" name="saleId" value="<%=tmpSaleId%>" />
											</td>
										</tr>	      
										<tr>
											<td colspan="2" align="center">
												<input type="submit" value="Get GiftCard Number" class="submit"/>
											</td>
										</tr>
									<% }else{ %>     
									<tr>				
										<td width="50%">New GiftCard Number #: </td>
										<td width="50%">
											<input type="text" style="width: 200px;" class="input_text" value="<%=gcId%>" name="newGiftcard" />
										</td>					        
									</tr>	
									<% } %>    
								</tbody>
							</table>

						</form>
					<% } %>
				</td>
			</tr>
		</table>
	</crm:GiftCardController>
 	</tmpl:put>
</tmpl:insert>