<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	String wineMediaPathTop = "/media/editorial/win_"+JspMethods.getWineAssociateId().toLowerCase()+"/expert-ratings-popup/top.html";
	String wineMediaPathBottom = "/media/editorial/win_"+JspMethods.getWineAssociateId().toLowerCase()+"/expert-ratings-popup/bottom.html";
	String wineMediaPathKey = "/media/editorial/win_"+JspMethods.getWineAssociateId().toLowerCase()+"/expert_ratings_key.html";
%>
<div class="bde">
<div style="position: relative; font-size: 0px; height: 0px;"><img src="/media_stat/images/wine/ratings_popup_close.gif"
	width="21" height="21" border="0" style="display: block; position: absolute; top: 0px; right: 0px;"
	onclick="return FreshDirect.Wine.hideExpertRatingPopup();"></div>
<fd:IncludeMedia name="<%= wineMediaPathTop %>"/>
<div class="wine-xprt-rating-key" style="width: 425px; text-align: center; margin: 0px auto;">
<fd:IncludeMedia name="<%= wineMediaPathKey %>"/>
</div>
<div>
<fd:IncludeMedia name="<%= wineMediaPathBottom %>"/>
</div>
</div>