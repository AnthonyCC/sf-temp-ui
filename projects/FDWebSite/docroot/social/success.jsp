<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <title>FreshDirect</title>
  <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
  <%
    request.getSession(false).removeAttribute(SessionName.LOGIN_SUCCESS);
    request.getSession(false).removeAttribute(SessionName.SOCIAL_LOGIN_SUCCESS);
  %>
</head>
<body>
  <div class="social-login-spinner">
    <img src="/media_stat/images/navigation/spinner.gif" class="fleft" />  
  </div>

  <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
  <script>
    var fd = window.FreshDirect,
        successPage;

    if (fd) {

      try {
        successPage = window.decodeURIComponent(fd.utils.getParameters().successPage);
      } catch (e) {}

      successPage = '/' + (successPage || '') + window.top.location.hash;

      console.log('Login succeeded, redirecting to: '+successPage);

      // give some time for GTM and the marketing trackers
      setTimeout(function () {
        window.top.location.assign(successPage);
      }, 500);
    }
  </script>
</body>
</html>
