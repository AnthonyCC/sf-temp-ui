<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<%
String catId = request.getParameter("catId");
PopulatorUtil.getContentNode(catId);

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
String custFirstName = user.getFirstName();
int validOrderCount = user.getAdjustedValidOrderCount();

Map params = new HashMap();
params.put("baseUrl", "");
%>

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Seasonal Picks"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Seasonal Picks</tmpl:put> --%>
	<tmpl:put name='content' direct='true'>
	
	<fd:IncludeMedia name="/media/editorial/picks/seasonal/seas_picks.ftl" parameters="<%=params%>"/>
	
</tmpl:put>
</tmpl:insert>
