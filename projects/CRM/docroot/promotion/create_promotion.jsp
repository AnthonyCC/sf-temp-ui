<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.EnumMonth"%>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<% boolean forPrint = false;
	boolean isViewOnly = false; %>
<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Create Promotion</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<div class="sub_nav" style="text-align: left;">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">
<fd:GetPromotion id="promotion" promotionId="">
<fd:PromotionController promotion="<%=promotion%>" actionName="create_promotion" result="result" successPage="/main/available_promotions.jsp">
	<form method="POST">
	<fd:ErrorHandler result="<%= result %>" name="<%=ActionError.GENERIC%>" id='errorMsg'><tr><td colspan="3"><span class="error"><%=errorMsg%></span></td></tr></fd:ErrorHandler>
	<tr><td width="40%"><span class="sub_nav_title">Confirm New Promotion</span></td>
	<td width="30%"><input type="button" value="EDIT" class="clear" onClick="javascript:window.location.href='/promotion/compose_promotion.jsp';"><input type="submit" value="CREATE PROMOTION" class="submit"></td>
	<td width="30%"align="right"><a href="/main/available_promotions.jsp">View All Promotions &raquo;</a></td>
	</tr>
	</table>
	</div>
	<div style="background: #FFFFFF;">
		<%@ include file="/includes/view_promotion_data.jspf" %>
	</div>
	</form>
</fd:PromotionController>
</fd:GetPromotion>
</tmpl:put>
</tmpl:insert>
