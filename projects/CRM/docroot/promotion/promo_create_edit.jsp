<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewModel"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel"%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Promotion Create Edit</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
		<%
		String userId = CrmSecurityManager.getUserName(request);
		FDPromotionNewModel promotion = null; String promoId="";
		// quick fix because of broken build, remove if this file is finished %>
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header noBorder">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Promotion&nbsp;Create&nbsp;Edit&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_detail_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<%-- --%>
						<input type="button" value="EDIT SECTION" onclick="" class="promo_btn_brn2 fleft noMargLR" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion details, name-usage --%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_detail_topAdditionals.jspf" %>
		</div>
		<%-- Promotion details, offer--%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_detail_offer.jspf" %>
		</div>
		<%-- Promotion details, customer requirements--%>
		<div class="padLR4">
		<%-- 	<%@ include file="/includes/promotions/i_promo_detail_custreq.jspf" %>--%>
		</div>
		<%-- Promotion details, cart requirements--%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_detail_cartreq.jspf" %>
		</div>
		<%-- Promotion details, delivery requirements--%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_detail_dlvreq.jspf" %>
		</div>
		<%-- Promotion details, payment requirements--%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_detail_payreq.jspf" %>
		</div>
	
	
	</tmpl:put>
</tmpl:insert>