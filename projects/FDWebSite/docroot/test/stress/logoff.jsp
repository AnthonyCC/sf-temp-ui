<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
// clear session
java.util.Enumeration e = session.getAttributeNames();
while (e.hasMoreElements()) {
    String name = (String)e.nextElement();
    session.removeAttribute(name);
}
// end session
session.invalidate();
// remove cookie
CookieMonster.clearCookie(response);


%>
