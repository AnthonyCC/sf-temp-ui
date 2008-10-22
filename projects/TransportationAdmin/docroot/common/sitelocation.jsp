<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.SecurityManager' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
            <td width="10%" align="center"><a href="index.jsp" >&nbsp;Home&nbsp;</a></td>           
            <td width="10%" align="center"><a href="dlvbuilding.do" >&nbsp;Building&nbsp;</a></td>
            <td width="10%" align="center"><a href="dlvlocation.do" >&nbsp;Location&nbsp;</a></td>
            <td width="10%" align="center"><a href="cutoff.do" >&nbsp;Cut Off&nbsp;</a></td>
            <td width="10%" align="center"><a href="dlvservicetime.do" >&nbsp;Service Time&nbsp;</a></td>           
            <td width="10%" align="center"><a href="dlvservicetimetype.do" >&nbsp;Service Time Type&nbsp;</a></td>
            <td width="10%" align="center"><a href="dlvservicetimescenario.do" >&nbsp;Scenario&nbsp;</a></td>   
            <td width="10%" align="center"><a href="routinginput.do" >&nbsp;Routing In&nbsp;</a></td>
            <td width="10%" align="center"><a href="routingoutput.do" >&nbsp;Routing Out&nbsp;</a></td> 
            <td width="10%" align="center"><a href="routingmerge.do" >&nbsp;Routing Merge&nbsp;</a></td>        
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