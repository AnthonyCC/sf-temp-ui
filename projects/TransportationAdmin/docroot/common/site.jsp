<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.SecurityManager' %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : <tmpl:get name='title'/> /</title>
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />   
  <link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
  <link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
  <script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
  <script src="js/action.js" language="javascript" type="text/javascript"></script>
    
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
    
    <script type="text/javascript" language="javascript" src="js/mootools.v1.11.js"></script>
    <script type="text/javascript" language="javascript" src="js/nogray_time_picker.js"></script>
    <script type="text/javascript" language="javascript" src="js/nogray_time_picker_min.js"></script>
    

</head>
 <body marginwidth="0" marginheight="0" border="0">
  <table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr class="apptitle" >
        <td width="40%"> 
          <img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban" />
        </td>
        <td width="50%">Transportation Department</td>
        <td width="10%">
          <table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr class="appusertitle">
              <td><img src="images/user.gif" border="0" alt="User:" /></td>
              <td><%=SecurityManager.getUserName(request)%></td>
            </tr>
            <tr class="appusertitle">
              <td><img src="images/role.gif" border="0" alt="Role:" /></td>
              <td><%=SecurityManager.getUserRole(request)%></td>
            </tr>
            <tr class="appusertitle">
              <td colspan="2" align="center"><a href="logout.jsp"><img src="images/logout.gif" border="0" alt="Logout" /></a></td>
            </tr>
          </table>          
        </td>
      </tr>
      <tr>
        <td class="navlist" colspan="3" bgcolor="c00cc3d">
        <table class="navtbl" border="0" width="100%">
          <tr>
            <td width="9%" align="center"><a href="index.jsp" >&nbsp;Home&nbsp;</a></td>
            <td width="9%" align="center"><a href="employee.do" >&nbsp;Employee&nbsp;</a></td>
            <td width="9%" align="center"><a href="adHocRoute.do" >&nbsp;Route&nbsp;</a></td>
            <td width="9%" align="center"><a href="area.do" >&nbsp;Area&nbsp;</a></td>
            <td width="9%" align="center"><a href="zonetype.do" >&nbsp;Zone Type&nbsp;</a></td>
            <td width="9%" align="center"><a href="zone.do" >&nbsp;Zone&nbsp;</a></td>
            <td width="9%" align="center"><a href="region.do" >&nbsp;Region&nbsp;</a></td>
            <td width="9%" align="center"><a href="truck.do" >&nbsp;Truck&nbsp;</a></td>
            <td width="9%" align="center"><a href="plan.do" >&nbsp;Planning&nbsp;</a></td>
            <td width="9%" align="center"><a href="assignment.do" >&nbsp;Assignment&nbsp;</a></td>
            <td width="9%" align="center"><a href="dispatch.do" >&nbsp;Dispatch&nbsp;</a></td>
            <td width="9%" align="center"><a href="dispatchSummary.do" >&nbsp;Dispatch Summary&nbsp;</a></td>
            <% if (SecurityManager.isUserAdmin(request)) {%>
              <td width="9%" align="center"><a href="welcomedlvlocation.do" >&nbsp;Routing&nbsp;</a></td>
            <% } %>
          </tr>
          </table>
        </td>
      </tr>
      
  </table>  
    <tmpl:get name='content'/>
  <br clear="all"/>
  <div class="separator"></div>
  <div class="footer"><jsp:include page='/common/copyright.jsp'/></div>
</body>
</html>