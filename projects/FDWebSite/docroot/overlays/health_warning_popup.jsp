<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%="/common/template/blank.jsp" %>'>
<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
<tmpl:put name='content' direct='true'>
	<soy:import packageName="common"/>
	<% String successPage = request.getParameter("successPage");
		String instant = request.getParameter("instant");
		String decorate = request.getParameter("decorate");
		String atc = request.getParameter("atc");
	    request.setAttribute("listPos", "SystemMessage,SideCartBottom");
	%>
	<fd:HealthWarningController successPage="<%=successPage%>" result="result">


	    <%

	    String onclickValue = "if(window.parent.FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')==false) {window.parent.FreshDirect.USQLegalWarning.setCookie('freshdirect.healthwarning','1')};";
 		onclickValue += decorate;
     	if (instant != null && !"".equals(instant)) {
     		if (!"true".equals(atc)) {
	        	onclickValue += instant+";";
     		}
     		onclickValue +="window.parent.FreshDirect.USQLegalWarning.blockSubmit(location.search,'"+atc+"');";
    	} else {
    		onclickValue += "window.parent.FreshDirect.USQLegalWarning.doNormalSubmit(location.search,'"+atc+"');";
    	}
     	onclickValue += "return false;";

		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put( "onclickValue", onclickValue );

		%>

		<soy:render template="common.healthwarningpopup" data="<%=dataMap%>" />


	</fd:HealthWarningController>
</tmpl:put>

</tmpl:insert>

