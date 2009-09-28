<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/giftcard.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Purchase Gift Card</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<%
			String fdTemplateId = "";

			if (request.getParameter("gcTempId") !=null) {
				fdTemplateId = request.getParameter("gcTempId");
			}

			/*
			 *	Call include to gather giftcard info from CMS
			 */
			
			//set the options first
			String deptId = "GC_testDept";
			String catId = "GC_testCat";
			String prodName = "GC_testProd";
			
			String mediaRoot = "/media/editorial/giftcards/";
			String mediaStaticRoot = "/media_stat/images/giftcards/";
			String gcDisplayType = "2"; //for a carousel
			String gcDisplayContainer = "gcDisplayContainer"; //id of div to use for display
			String gcDisplayId = "gcDisplay"; //id of gc Display object
			String gcDisplayTemplateContainerId = "gcTempId";
		%>
		<%@ include file="/gift_card/purchase/includes/i_fetch_giftcard_info_from_cms.jspf" %>

		
		<table border="0" cellspacing="0" cellpadding="2" width="675">
			<tr>
				<td align="center">
					<img src="/media_stat/images/giftcards/landing/headline_giftcards.gif" width="518" height="29" alt="the Gift of Deliciously Free Food. Gift Cards." />
				</td>
			</tr>
			<tr>
				<td align="left">
					<fd:IncludeMedia name="/media/editorial/giftcards/media_includes/landing_header.html" />
				</td>
			</tr>
			<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
			</tr>
			<tr>
				<td align="center">
					<form name="giftcard_form" id="giftcard_form" method="get" action="add_giftcard.jsp">
						<input type="hidden" id="gcTempId" name="gcTempId" value="<%=fdTemplateId%>">
						<div id="gcDisplayContainer"><!-- --></div>
				</td>
			</tr>
			<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
			</tr>
			<tr>
				<td align="center"><a href="">
					<input type="image" src="/media_stat/images/giftcards/landing/shop_now_btn.gif" width="118" height="34" alt="Shop Now" name="gcLand_shopNow" id="gcLand_shopNow" border="0" onclick="$('giftcard_form').submit();return false;" />
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div class="card_TOC">Purchasing a Gift Card indicates agreement to our Gift Card<br />
						<a href="/gift_card/purchase/tac.jsp">Terms and Conditions</a>. Click for details. 
					</div>
				</td>
			</tr>
		</table>

		
	</tmpl:put>
</tmpl:insert>