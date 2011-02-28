<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Nutrition - Note</tmpl:put>
	<tmpl:put name='content' direct='true'>    
		<table width="315" cellpadding="0" cellspacing="0" border="0">
		<tr>
<%
String referer = "";
if(request.getHeader("Referer") != null){
	referer =request.getHeader("Referer");
}%>
			<td><%if(referer.indexOf("nutrition_info.jsp") > -1 || referer.indexOf("prod_desc_popup.jsp") > -1 || referer.indexOf("meal_item_detail.jsp") > -1 ){%><a href="javascript:history.back()"><< back </a><br><br><%}%>			
				<b>An Important Note About Product Information</b>
				<br><br>
				We do our best to present accurate ingredients, nutrition, kosher symbols, and other product information on our Web site. Unfortunately, since this information comes from many sources, we cannot guarantee that it is accurate or complete.<br>
				<br>
				If you have a specific dietary concern or a question about a product, please consult the product's label or contact the manufacturer directly. Please note that product packaging may change from time to time and may differ from the product image displayed on the Web site.<br>
				<br>
				If you think you have detected an error on our site, please <a href="mailto:<fd:GetServiceEmail />">contact us</a>.
				<br><br>
			</td>
		</tr>
		</table>
	</tmpl:put>
</tmpl:insert>
