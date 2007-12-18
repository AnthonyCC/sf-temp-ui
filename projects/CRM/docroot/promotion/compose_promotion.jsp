<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.EnumMonth"%>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"%>

<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.ProfileAttributeName"%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>New Promotion</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<div class="sub_nav" style="text-align: left;">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">
<fd:GetPromotion id="promotion" promotionId="">
<%
  TreeMap zipRestrictionMap = promotion.getZipRestrictions();
  Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
%>
<fd:PromotionController promotion="<%=promotion%>" actionName="new_promotion" result="result" successPage="create_promotion.jsp" zipRestrictionMap="<%=zipRestrictionMap%>">
	<form  method="POST">
	<tr><td width="35%"><span class="sub_nav_title">New Promotion</span> &nbsp;&nbsp; <span class="note">* Required</span></td>
	<%if(result.isFailure()) { %>	
	<td nowrap align="left">&nbsp;&nbsp;<span class="error">* Promotion has errors</span>&nbsp;&nbsp;</td>
	<%} %>
	<td width="30%"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SUBMIT PROMOTION" class="submit"></td>
	<td width="20%"align="right"><a href="/main/available_promotions.jsp">View All Promotions &raquo;</a></td>
	</tr>
	</table>
	</div>
	<div style="background: #FFFFFF;">
		<%@ include file="/includes/promotion_fields.jspf" %>
	</div>
	</form>
</fd:PromotionController>
</fd:GetPromotion>
</tmpl:put>
</tmpl:insert>