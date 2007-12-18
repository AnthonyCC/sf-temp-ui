<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.EnumMonth"%>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<% String promotionPk = request.getParameter("promoId");
	session.removeAttribute(SessionName.EDIT_PROMOTION);%>
<crm:GetCurrentAgent id='currentAgent'>
<fd:GetPromotion id="promotion" promotionId="<%=promotionPk%>">
<%
  boolean forPrint = "print".equalsIgnoreCase(request.getParameter("for"));
  boolean isViewOnly = true;
  String tmpl = "/template/" + (forPrint ? "print" : "top_nav") + ".jsp";
  TreeMap zipRestrictionMap = promotion.getZipRestrictions();
  Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
%>
<fd:PromotionController promotion="<%=promotion%>" actionName="search_customer_restriction" result="result" zipRestrictionMap="<%=zipRestrictionMap%>">  
<tmpl:insert template='<%=tmpl%>'>
    <tmpl:put name='title' direct='true'>View Promotion</tmpl:put>
    <tmpl:put name='content' direct='true'>
    <form  name="viewPromotion" method="POST">
	<div style="background: #FFFFFF;">
	 <%if(!forPrint){%>
            <div class="sub_nav" style="text-align: left;overflow:auto;width=100%;">
            <table cellpadding="0" cellspacing="0" border="0" width="99%">
            <tr><td width="40%"><span class="sub_nav_title">View Promotion</span></td>
            <% if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) { %>
				<td width="20%" align="middle"><input type="button" value="EDIT PROMOTION" class="clear" onClick="javascript:window.location.href='/promotion/edit_promotion.jsp?promoId=<%=promotionPk%>';"></td>
			<% } %>
            <td align="right">(<a href="javascript:pop('/main/view_promotion.jsp?promoId=<%= promotionPk %>&for=print','680','800')">Print Version</a>)&nbsp;&nbsp;<a href="/main/available_promotions.jsp">View All Promotions &raquo;</a></td>
            </tr>
            </table>
            </div>
            <%@ include file="/includes/view_promotion_data.jspf" %>
            
            <% if(promotion.getAssignedCustomerSize() > 0) { %>
	            <table cellpadding="0" cellspacing="0" border="0" width="100%">
	            	<tr><td><span class="sub_nav_title">Search Customer Restrictions</span></td></tr>
	            	<tr>
	            		<td><img src="/media_stat/crm/images/clear.gif" width="450" height="0">Email ID:&nbsp;&nbsp;
	            			<input type="text" name="userId" style="width:200px"/>&nbsp;&nbsp;
	            			<input type="SUBMIT" name="search" class="submit" value="SEARCH"/>
	            		
	            		
	            	<%
	            		Object obj = request.getAttribute("IS_USER_ASSIGNED");
	            		if(obj != null) { 
	            			Boolean value = (Boolean)obj;
	            			String displayValue = value.booleanValue() ? "IN" : "NOT IN";
	            			
	            	%>
	            		&nbsp;&nbsp;<b>The customer is <span class="error"><%= displayValue %></span> the restriction list.</b>
	            	<% } %>	
	            		</td>
	            	</tr>	
	            </table>
	     <%}%>       
        <%}%>
        <%if(forPrint) {%>
            <%@ include file="/includes/view_promotion_data.jspf" %>
        <%}%>
	</div>
    </form>
</tmpl:put>
</tmpl:insert>
</fd:PromotionController>
</fd:GetPromotion>
</crm:GetCurrentAgent>