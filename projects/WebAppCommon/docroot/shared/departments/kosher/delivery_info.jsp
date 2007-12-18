<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Kosher Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>

			<table width="98%">
				<tr>
					<td class="text11">
					<span class="title14"><b>About Our Kosher Delivery Slots</b></span><br>
					Because we custom-prepare our kosher meat and seafood for each individual order, these items cannot be produced or delivered during Shabbat as well as during certain holidays when work is not allowed. Items with this restriction are clearly marked throughout the site, and will be removed from your cart at checkout if you select delivery on a day when production is not available. 
					<br><br>
					Kosher production items are not available for delivery on Friday, Saturday, or Sunday morning as well as on the following days:
					<ul>
					<fd:GetDlvRestrictions id="kosherRestrictions" reason="<%=EnumDlvRestrictionReason.KOSHER%>">
						<logic:iterate collection="<%= kosherRestrictions %>" id="restriction" type="com.freshdirect.delivery.restriction.RestrictionI">
							<li><b><%=restriction.getName()%></b>, <%=restriction.getDisplayDate()%></li>
						</logic:iterate>
					</fd:GetDlvRestrictions> 
					</ul>
					</td>
				</tr>
			</table>
		
		</tmpl:put>
</tmpl:insert>
