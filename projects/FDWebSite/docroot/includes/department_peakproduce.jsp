
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:ProduceRatingCheck>
<% if("fru".equals(request.getParameter("deptId")) ||"veg".equals(request.getParameter("deptId")))  {%>

<fd:GetPeakProduce deptId='<%= request.getParameter("deptId") %>' id='peakProduces'> 
<% if(peakProduces.size()>0) {%>
<BR><img src="/media_stat/images/layout/greatrightnow.gif" name="greatRightNow" border="0">
<Br>
<fd:IncludeMedia name= "/media/editorial/fruit/rating/fru_rate_head.html"/>
<table CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr valign="top">
<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
<td>
<% 
  ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(request.getParameter("deptId"));
  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
 %>
 <td valign="top" align="center" WIDTH=<%=adjustedImgWidth%>">
 <a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> ALT="<%=displayObj.getAltText()%>" hspace="0" border="0"></a>

    <%  if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>

            <br><font class="center">           

            <img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif"  name="rating">

            </font>

    <%  } %>

        <br>

        <a href="<%=displayObj.getItemURL()%>&trk=dept"><font class="text11"><%=displayObj.getItemName()%></font></a>

        

<%  if (displayObj.getPrice()!=null) { %>
        <br>
        <font class="price"><%=displayObj.getPrice()%></font>

<%  } %>

   
   </td>




</logic:iterate>
</tr></table>
    <% if("veg".equals(request.getParameter("deptId")))  {%>
        <br>
    <% }%>

<%} %>
</fd:GetPeakProduce>

<% }%>
</fd:ProduceRatingCheck>