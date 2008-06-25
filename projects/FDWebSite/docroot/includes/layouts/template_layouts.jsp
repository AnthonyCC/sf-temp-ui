<%@ page import="java.text.SimpleDateFormat"
%><%@ page import='java.util.*'
%><%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*'
%><%@ page import='com.freshdirect.fdstore.attributes.Attribute'
%><%@ page import='com.freshdirect.fdstore.promotion.*'
%><%@ page import='java.net.URLEncoder'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><%@ page import='com.freshdirect.fdstore.*'
%><%@ page import="com.freshdirect.framework.webapp.*"
%><%@ page import='com.freshdirect.framework.util.*'
%><%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ taglib uri='oscache' prefix='oscache'
%><%
// STEP 1 - RETRIEVE THE CURRENT NODE
//
String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if (deptId != null) {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
    isDepartment = true;
} else {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


// STEP 2 - RENDER CUSTOM CONTENT
//
Map params = new HashMap();
params.put("baseUrl", "");

Attribute attrib = currentFolder.getAttribute("TEMPLATE_PATH");
if (attrib!=null) {
	String templatePath = (String)attrib.getValue();
    %><fd:IncludeMedia name='<%= templatePath %>' parameters="<%=params%>" /><%
}
%>