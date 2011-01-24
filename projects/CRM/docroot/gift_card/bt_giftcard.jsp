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


    
<tmpl:insert template='/template/top_nav.jsp'>
    <tmpl:put name='title' direct='true'>Gift Card : Balance Transfer</tmpl:put>
    <tmpl:put name='content' direct='true'>
	
    <%
		boolean hasCustomerCase = CrmSession.hasCustomerCase(session);
		String gcId=request.getParameter("");
		String recEmail=request.getParameter("");
		if(gcId==null) gcId="";
		if(recEmail==null) recEmail="";
    %>  

	<jsp:include page="/includes/giftcard_nav.jsp"/>
	
	<crm:GiftCardController actionName='btGiftCard' resultName="result">
		<%
			String fromGCAccount="";
			String toGCAccount="";
			String gcAmount="";
			if (request.getParameter(EnumUserInfoName.GC_ACCOUNT_FROM.getCode()) !=null) {
					fromGCAccount =  request.getParameter(EnumUserInfoName.GC_ACCOUNT_FROM.getCode());
			}
			if (request.getParameter(EnumUserInfoName.GC_ACCOUNT_TO.getCode()) !=null) {
					toGCAccount =  request.getParameter(EnumUserInfoName.GC_ACCOUNT_TO.getCode());
			}
			if (request.getParameter(EnumUserInfoName.GC_AMOUNT.getCode()) !=null) {
					gcAmount =  request.getParameter(EnumUserInfoName.GC_AMOUNT.getCode());
			}
		%>
	    
		<form name="transferGC" id="searchGC" method="post">
		
		<table cellspacing="2" cellpadding="2" border="0" width="100%" class="gc_tableBody">
		<tbody>
            <tr>
				<td colspan="4">
				    <fd:ErrorHandler result='<%=result%>' name='bt_success' id='errorMsg'>
                        <%@ include file="/includes/i_error_messages.jspf" %>
                    </fd:ErrorHandler>        
				</td>
			</tr>

			<tr>
				<td width="33%">Transfer From GiftCard #: </td>
				<td width="33%">
					<input type="text" size="21" maxlength="50" class="text11" name="<%= EnumUserInfoName.GC_ACCOUNT_FROM.getCode() %>" id="<%= EnumUserInfoName.GC_ACCOUNT_FROM.getCode() %>" required="true" value="<%= fromGCAccount %>" />
					<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.GC_ACCOUNT_FROM.getCode() %>' id='errorMsg'>
						<span class="text11rbold">&nbsp;<%=errorMsg%></span>
					</fd:ErrorHandler>
				</td>
				<td width="33%">&nbsp;</td>							        
			</tr>	
			<tr>
				<td width="33%">to Gift Card: </td>
				<td width="33%">			
					<input type="text" size="21" maxlength="50" class="text11" name="<%= EnumUserInfoName.GC_ACCOUNT_TO.getCode() %>" id="<%= EnumUserInfoName.GC_ACCOUNT_TO.getCode() %>" required="true" value="<%= toGCAccount %>" />
					<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.GC_ACCOUNT_TO.getCode() %>' id='errorMsg'>
						<span class="text11rbold">&nbsp;<%=errorMsg%></span>
					</fd:ErrorHandler>                
				</td>
				<td width="33%">&nbsp;</td>					        
			<!--    
			</tr>	
				<tr>
				<td width="33%">amount: </td>
				<td width="33%">    			
					<input type="text" size="21" maxlength="50" class="text11" name="<%= EnumUserInfoName.GC_AMOUNT.getCode() %>" id="<%= EnumUserInfoName.GC_AMOUNT.getCode() %>" required="true" value="<%= gcAmount %>" />
					<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.GC_AMOUNT.getCode() %>' id='errorMsg'>
						<span class="text11rbold">&nbsp;<%=errorMsg%></span>
					</fd:ErrorHandler>                

				</td>
				<td width="33%">
					&nbsp;
				</td>							        
			</tr>	
			-->
			<tr>
				<td width="33%">&nbsp;</td>	
				<td width="33%">
					<% if(hasCustomerCase){  %> 
						<input type="submit" value="TRANSFER AMOUNT" class="submit" />
					<% }else{ %>
						<b>(Note : Case Required to do a balance transfer)</b>&nbsp;
					<% } %>
				<td width="33%">&nbsp;</td>
			</tr>
		</tbody>
		</table>

	</crm:GiftCardController>

	</tmpl:put>
</tmpl:insert>
