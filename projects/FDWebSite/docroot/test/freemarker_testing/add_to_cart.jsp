<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en-US" xml:lang="en-US"> <!--<![endif]-->
<head>
	<%
		/* fake ddpp layout so stylesheets will auto add */
		request.setAttribute("layoutType", "200");
	%>
    <title>freemarker add to cart test page</title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<jwr:style src="/giftcards.css" media="all" />
	<jwr:style src="/timeslots.css" media="all" />
	<%@ include file="/shared/template/includes/ccl.jspf" %>
<%		
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script type="text/javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>

</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" 
      onload="<%= request.getAttribute("bodyOnLoad")%>" 
      onunload="<%= request.getAttribute("bodyOnUnload")%>" >
	<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" />
	<%
		Map params = new HashMap();
	
		Enumeration keys = request.getParameterNames();  
		while (keys.hasMoreElements() ) {  
			String key = (String)keys.nextElement(); 
	
			//To retrieve a single value  
			String value = request.getParameter(key); 
	
			params.put(key, value);
		
			// If the same key has multiple values (check boxes)  
			String[] valueArray = request.getParameterValues(key);  
			 
			for(int i = 0; i > valueArray.length; i++){
				params.put(key+valueArray[i], valueArray[i]);
			}  
		}
	
		String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		params.put("baseUrl", baseUrl);
		params.put("user", user);
	%>
	<fd:IncludeMedia name="/media/testing/add_to_cart.ftl" parameters="<%=params%>" withErrorReport="false" />
</body>
</html>