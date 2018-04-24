<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page isErrorPage="true" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.webapp.util.AjaxErrorHandlingService' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%><%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.FDExceptionUtil"%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%!
	Logger LOGGER = LoggerFactory.getInstance("error.jsp");

	//Very dirty way of logging broken pipe and connnection reset without refactoring exception handling.
	public void logError(HttpServletRequest request, FDSessionUser user, String errorCode, String errorMessage){
		if (FDExceptionUtil.isTextContainsIgnoreCase(errorMessage, "JspException") && (FDExceptionUtil.isTextContainsIgnoreCase(errorMessage, "Broken pipe") || FDExceptionUtil.isTextContainsIgnoreCase(errorMessage,"Connection reset"))){
		    log500(request,user,"FDWEBERROR-04", errorMessage);
		}
		else{
		    log500(request,user,errorCode,errorMessage);
		}
	}

	public void log500(HttpServletRequest request, FDSessionUser user, String errorCode, String errorMessage){
		try {
			LOGGER.warn(errorCode + " for "
	    		+ " : User=" + (user != null ? (user.getIdentity() != null && user.getFDCustomer() != null ? user.getFDCustomer().getErpCustomerPK() : user.getPrimaryKey() ) : "NOUSER" )
	    		+ " : Cookie=" + CookieMonster.getCookie(request) 
	    		+ " : Request URL=" + request.getRequestURL().append("?"+request.getQueryString())
	    		+ " : Referer=" + request.getHeader("referer") 
	    		+ " : User-Agent=" + request.getHeader("User-Agent")
	    		+ " : ErrorMessage=" + errorMessage); 
		} catch (Exception exp) {
			LOGGER.warn("FDWEBERROR-03 error logging error " + exp.getMessage());
		}
	}
%>

<%
/****
 * Error page
 *
 * Do not use any feature here that could easily break and throw an other exception!
 * Do not include CSS/JS via jawr, do not try to read properties etc.
 */

try {
	
  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	
  if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
    // AJAX errors
    String message = AjaxErrorHandlingService.defaultService().prepareErrorMessageForAjaxCall(request, response);
    String primaryErrorMessage = AjaxErrorHandlingService.defaultService().getPrimaryErrorMessage(message);
    String secondaryErrorMessage = AjaxErrorHandlingService.defaultService().getSecondaryErrorMessage(message);
    
    logError(request, user, "FDWEBERROR-01", message);
%>
  {"error": {"primary": "<%= primaryErrorMessage%>", "secondary": "<%= secondaryErrorMessage%>"}}
<%
  } else {
    // standard JSP errors
    response.setStatus(500);
    logError(request, user, "FDWEBERROR-02", FDExceptionUtil.getRootCauseStackTrace(exception));
        
 %>
 
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <%-- <title>FreshDirect - Something went wrong...</title> --%>
   <fd:SEOMetaTag title="FreshDirect - Something went wrong..."/>
  <style>
    body {
      font-family: Verdana, Arial, sans-serif;
    }
    a {
      color: #360;
      text-decoration: none;
      font-size: 1.2em;
    }
    a:hover {
      text-decoration: underline;
    }
    #errorcode {
      color: #aaa;
      text-align: right;
    }
    #logo {
      margin-top: 250px;
    }
    .container {
      width: 970px;
      margin: auto;
      text-align: center;
    }
    .container h1 {
      font-weight: normal;
    }
    .container pre {
      border: 1px solid #888;
      background-color: #fffef0;
      padding: 10px;
      text-align: left;
      overflow: auto;
    }
    .container ul {
      display: inline-block;
      text-align: left;
      list-style-type: none;
    }
    .container ul li {
      margin: 10px;
    }
    .container ul li a {
      line-height: 29px;
      vertical-align: bottom;
    }
    .container ul li:before {
      content: '';
      width: 29px;
      height: 29px;
      margin-right: 10px;
      overflow: none;
      display: inline-block;
      vertical-align: bottom;
      background-image: url(/media_stat/errorpage/sprite.png);
    }
    .container ul li#refresh:before {
      background-position: 0 0;
    }
    .container ul li#continue:before {
      background-position: 0 -41px;
    }
    .container ul li#toprated:before {
      background-position: 0 -83px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div id="errorcode">
      Error code: 500
    </div>
    <div id="logo">
      <a href="/" title="back to the homepage"><img src="/media/images/navigation/global_nav/fd_logo_on.png" alt="broken truck" /></a>
    </div>
    <h1 id="errormessage">Sorry, we're experiencing an<br>internal server problem</h1>
    <ul class="links">
      <li id="refresh"><a href="#" onclick="window.location.reload();">Refresh the page</a></li>
      <li id="continue"><a href="/">Continue shopping</a></li>
      <li id="toprated"><a href="/browse.jsp?id=top_rated">View top rated items</a></li>
    </ul>
    <c:if test="${not empty cookie['developer']}">
      <a href="/" title="back to the homepage"><img src="/media_stat/images/something_went_wrong.png" alt="broken truck" /></a>
      <pre id="stacktrace">
  <% exception.printStackTrace(new java.io.PrintWriter(out)); %>
      </pre>
    </c:if>
  </div>
  <script>
    window.FreshDirect = window.FreshDirect || {};
    var fd = window.FreshDirect;
    var dataLayer = window.dataLayer || [];
    
    fd.gtm = fd.gtm || {};
    fd.gtm.key = '<%= FDStoreProperties.getGoogleTagManagerKey() %>';
    fd.gtm.auth = '<%= FDStoreProperties.getGoogleTagManagerAuthToken() %>';
    fd.gtm.preview = '<%= FDStoreProperties.getGoogleTagManagerPreviewId() %>';

    dataLayer.push({
      'config-ga-key': '<%= FDStoreProperties.getGoogleAnalyticsKey() %>',
      'config-ga-domain': '<%= FDStoreProperties.getGoogleAnlayticsDomain() %>'
    });

    dataLayer.push({
      event: 'error',
      eventCategory: 'Error',
      eventAction: '500 Error',
      eventLabel: '500 Error'
    });

    // load google tag manager
    (function(fd) {
      var loadGTM = function (w,d,s,l,i) {
        w[l]=w[l]||[];
        w[l].push({
            'gtm.start':new Date().getTime(),
            event:'gtm.js'
        });

        var f=d.getElementsByTagName(s)[0],
            j=d.createElement(s),
            dl=l!=='dataLayer'?'&l='+l:'';

        j.async=true;
        j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl+(fd.gtm.auth ? '&gtm_auth='+fd.gtm.auth : '')+(fd.gtm.preview ? '&gtm_preview='+fd.gtm.preview+'&gtm_cookies_win=x' : '');
        f.parentNode.insertBefore(j,f);
      };

      loadGTM(window, document, 'script', 'dataLayer', fd.gtm.key);

    }(FreshDirect));

  </script>

</body>
</html>
<%
  } // end of standard JSP error page
} catch (Exception fatalError) {
  JspLogger.GENERIC.error("FatalError in error page", fatalError);
%>
	<%=  String.valueOf(fatalError) %>
<%
}
%>
