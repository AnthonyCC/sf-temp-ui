<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>


<fd:ProduceRatingCheck>
<%
	if ( "fru".equals(request.getParameter("deptId")) ||  "veg".equals(request.getParameter("deptId")) || "wgd".equals(request.getParameter("deptId"))
		) {
%>

<%
	//var defines
	int ppTarget = 6;
	int ppVeg = 0;
	int ppFru = 0;

	Collection vegCol = null;
	Collection fruCol = null;
%>


	<fd:GetPeakProduce deptId='veg' id='peakProduces'>
		<% vegCol = peakProduces; %>
	</fd:GetPeakProduce>
	
	<fd:GetPeakProduce deptId='fru' id='peakProduces'>
		<% fruCol = peakProduces; %>
	</fd:GetPeakProduce>

	<%
		//iterate over vegCol and remove dupes contained in fruCol
		Iterator iterator = vegCol.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (fruCol.contains(element)) {
				iterator.remove();
			}
		}
		//set sizes
		ppVeg = vegCol.size();
		ppFru = fruCol.size();

		//logic to determine products from each dept to show
		if (ppVeg > 3) {
			//veg = 3
			ppVeg = 3;
			if (ppFru > 3){
				//veg = 3, fru = 3
				ppVeg = 3;
				ppFru = 3;
				//done
			}else{
				//fru<3
				//veg = tar-fru
				ppVeg = ppTarget-ppFru;
			}
		}else{
			//fru = tar - veg
			ppFru = ppTarget-ppVeg;
		}
		%>
		<% if (ppFru > 0 || ppVeg > 0) { // min to require to display section %>
			<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />
			<fd:IncludeMedia name= "/media/editorial/whats_good/whats_good_GRN_above.html"/>
			<table cellpadding="0" cellspacing="0" border="0">
			<tr valign="bottom">
				<logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppFru) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
						<a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> alt="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0" /></a>
					</td>
				</logic:iterate>
				<logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppVeg) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
						<a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> alt="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0" /></a>
					</td>
				</logic:iterate>
			</tr>
			<tr valign="top">
				<logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppFru) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
						<% if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
							<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3" />
						<% } %>
						<br /><a href="<%=displayObj.getItemURL()%>&trk=dept" class="text11"><%=displayObj.getItemName()%></a>
						<%  if (displayObj.getPrice()!=null) { %>
							<br /><span class="price"><%=displayObj.getPrice()%></span>
						<%  } %>
					</td>
				</logic:iterate>
				<logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppVeg) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
						<% if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
							<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3" />
						<% } %>
						<br /><a href="<%=displayObj.getItemURL()%>&trk=dept" class="text11"><%=displayObj.getItemName()%></a>
						<%  if (displayObj.getPrice()!=null) { %>
							<br /><span class="price"><%=displayObj.getPrice()%></span>
						<%  } %>
					</td>
				</logic:iterate>
			</tr>
			</table>
		<% } %>
<% } %>
</fd:ProduceRatingCheck>