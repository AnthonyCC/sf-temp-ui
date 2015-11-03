<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="freshdirect" prefix="fd" %>



  <div class="social-login-spinner">
  	<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />  
  </div>
  
 <fd:CheckLoginStatus />
 <fd:ExternalAccountController />


  
<script language="javascript">
		window.opener.location='/';
</script>
  