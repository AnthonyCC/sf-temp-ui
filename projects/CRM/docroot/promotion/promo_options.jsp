<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Promotion: Configurable Options</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Configurable&nbsp;Options&nbsp;</td>
					<td width="90%" class="gray10pt">
						<!--  -->
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion options, config filter --%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_config_filter.jspf" %>
		</div>
		<%-- Promotion options, config profile --%>
		<div class="padLR4">
			<%@ include file="/includes/promotions/i_promo_config_profile.jspf" %>
		</div>
	
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>