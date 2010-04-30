<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
%>

<fd:CheckLoginStatus id="user" />

<% 
request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "CategoryNote");

if (user.getLevel() < FDUserI.RECOGNIZED) { %>
<jsp:forward page="/your_account/delivery_info_avail_slots_a.jsp" />
<% } else { %>
<jsp:forward page="/your_account/delivery_info_avail_slots_b.jsp" />
<% } %>