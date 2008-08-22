<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:ProduceRatingCheck>
<% if("fru".equals(request.getParameter("deptId")) ||"veg".equals(request.getParameter("deptId")))  {%>
	<fd:GetPeakProduce deptId='<%= request.getParameter("deptId") %>' id='peakProduces'> 
	<% if(peakProduces.size()>0) {%>
	<table><tr><td style="padding-bottom:3px;"><img src="/media_stat/images/layout/greatrightnow.gif" name="Great Right Now" width="535" height="15" border="0"></td></tr></table>
	<fd:IncludeMedia name= "/media/editorial/fruit/rating/fru_rate_head.html"/>
	<% ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(request.getParameter("deptId")); %>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				 %>
				 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
					<a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> ALT="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0"></a>
				 </td>
			</logic:iterate>
		</tr>
		<tr valign="top">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				 %>
				 <td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<%  if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
							<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3"><% } %><br><a href="<%=displayObj.getItemURL()%>&trk=dept" class="text11"><%=displayObj.getItemName()%></a>
					<%  if (displayObj.getPrice()!=null) { %>
							<br><span class="price"><%=displayObj.getPrice()%></span>
					<%  } %>
				   </td>
				</logic:iterate>
			</tr>
		</table>
	<%} %>
	</fd:GetPeakProduce>
<% }%>
</fd:ProduceRatingCheck>