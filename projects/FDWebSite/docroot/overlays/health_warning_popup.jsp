<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.io.IOException"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.util.MediaUtils"%>
<%@ page import="com.freshdirect.common.pricing.PricingContext"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="java.io.StringWriter"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="com.freshdirect.framework.template.TemplateException"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%!
	private static String fetchMedia(String mediaPath, FDUserI user, boolean quoted) throws IOException, TemplateException {
		if (mediaPath == null)
			return null;
	
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
		parameters.put("user", (FDUserI)user);
		parameters.put("sessionUser", (FDSessionUser)user);
		
		StringWriter out = new StringWriter();
				
		MediaUtils.render(mediaPath, out, parameters, false, 
				user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);
	
		String outString = out.toString();
		
		//fix media if needed
		outString = MediaUtils.fixMedia(outString);
		
		return quoted ? JSONObject.quote( outString ) : outString;
	}
%>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%="/common/template/blank.jsp" %>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Alcohol Information"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
	<soy:import packageName="common"/>
	<% 
		/* I'm not sure what these params could be, but it's not a good idea
			to take them and put them straight into the js like this...
		*/
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		String successPage = request.getParameter("successPage");
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
		dataMap.put( "mediaContent", fetchMedia("/media/editorial/site_pages/health_warning_overlay_2016.html", user, false));
		
		/* render js to page so onclick has a method to call */
	%>
	<script>
		function healthWarningOnClick(accept) {
			if (window.parent.FreshDirect.components.ifrPopup) {
				window.parent.FreshDirect.components.ifrPopup.close();
			}
			if (accept) {
				<%= onclickValue %>
			} else {
				window.parent.FreshDirect.USQLegalWarning.blockSubmit(location.search,false);
			}
		}
	</script>
	

	<soy:render template="common.healthwarningpopup" data="<%=dataMap%>" />


	</fd:HealthWarningController>
</tmpl:put>

</tmpl:insert>

