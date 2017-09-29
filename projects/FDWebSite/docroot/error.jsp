<%@ page isErrorPage="true" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.webapp.util.AjaxErrorHandlingService' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%

/****
 * Error page
 *
 * Do not use any feature here that could easily break and throw an other exception!
 * Do not include CSS/JS via jawr, do not try to read properties etc.
 */

try {
  if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
    // AJAX errors
    String message = AjaxErrorHandlingService.defaultService().prepareErrorMessageForAjaxCall(request, response);
    String primaryErrorMessage = AjaxErrorHandlingService.defaultService().getPrimaryErrorMessage(message);
    String secondaryErrorMessage = AjaxErrorHandlingService.defaultService().getSecondaryErrorMessage(message);
  %>
  {"error": {"primary": "<%= primaryErrorMessage%>", "secondary": "<%= secondaryErrorMessage%>"}}
  <%
  } else {
    // standard JSP errors
    response.setStatus(500);

    if (exception != null) { 

      JspLogger.GENERIC.error("Got an error page", exception);
      %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>FreshDirect - Something went wrong...</title>
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
</body>
</html>
      <%
    }
  } // end of standard JSP error page
} catch (Exception fatalError) {
  JspLogger.GENERIC.error("FatalError in error page", fatalError);
  %>
	<%=  String.valueOf(fatalError) %>
<%
}
%>
