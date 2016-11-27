<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Promotion Activity</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%
		String promoId = request.getParameter("promoId");
		%>
		<fd:GetPromotionNew id="promotion" promotionId="<%= promoId %>">
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header noBorder">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Promotion&nbsp;Activity&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_detail_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<%if(FDStoreProperties.isPromoPublishNodeMaster() && !promotion.isOnHold()){ %>
						<input type="button" value="EDIT PROMOTION" onclick="javascript:editPromotionSubmit('<%= promoId %>')" class="promo_btn_brn fleft noMargLR w125px" />
						<% } %>
						<input type="button" value="BACK TO DETAIL VIEW" onclick="javascript:promotionDetailSubmit('<%= promoId %>')" class="promo_btn_wht fright noMargLR w150px" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion activity, name --%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_activity_topAdditionals.jspf" %>
		</div>
		<%-- Promotion activity log --%>
		<%-- content row --%>
		<table width="100%" cellspacing="0" cellpadding="0" class="promo_table2">
		<tbody>
			<tr>
				<th width="16px" align="center">&nbsp;</th>
				<th width="150px">Date</th>
				<th width="150px">Time</th>
				<th width="750px">Action</th>
				<th width="150px">User</th>
				<th><img width="75" height="1" src="/media_stat/crm/images/clear.gif"></th>
				<th><img width="12" height="1" src="/media_stat/crm/images/clear.gif"></th>
			</tr>
		</tbody>
		</table>
		<div class="promo_page_content-view_content3 noBorder">
			<%@ include file="/includes/promotions/i_promo_activity_log.jspf" %>
		</div>
	</fd:GetPromotionNew>
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>