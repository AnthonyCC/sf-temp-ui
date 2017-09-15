<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%
	String type = request.getParameter("type");
%>
<%
	if (null == type || type.isEmpty()) {
%>
		<potato:qsRecommender />
		<soy:render template="common.qsBottomTabbedCarousel" data="${qsBottomPotato}" />
<%
	} else if ("deals".equals(type)) {
%>
		<potato:recommender siteFeature="DEALS_QS" name="deals" maxItems="15"  cmEventSource="cc_tabbedRecommender" />
		<c:set target="${deals}" property="selected" value="deals" />
		<soy:render template="common.tabbedCarousel" data="${deals}" />
<%
	} else if ("cart".equals(type)) {
		%>
		<potato:viewCart/>
		<soy:render template="common.viewCartTabbedCarousel" data="${viewCartPotato}" />
		<%
	} else if("checkout".equals(type)) {
		%>
		<potato:viewCart name="checkout"/>
		<soy:render template="common.checkoutTabbedCarousel" data="${checkout}" />
		<%
	} else if( "ymal".equals(type)) {
		String currentNodeKey = request.getParameter("currentNodeKey");
	%>
		<potato:recommender siteFeature="YMAL" name="ymal" maxItems="25" currentNodeKey="<%=currentNodeKey%>" cmEventSource="CC_YMAL"  sendVariant="<%= true %>"/>
		<soy:render template="common.ymalCarousel" data="${ymal}" />
		
	<%
	}
%>