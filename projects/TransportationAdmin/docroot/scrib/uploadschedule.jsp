<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.SecurityManager' %>
<%@ page import='com.freshdirect.transadmin.model.*' %>
<%@ page import='com.freshdirect.transadmin.constants.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%		
	String pageTitle = "Upload Schedule";
    EnumUploadSource source = EnumUploadSource.SCHEDULE;
	String processType = request.getParameter("processType");
	
	if(processType != null) {
		EnumUploadSource newSource = EnumUploadSource.getEnum(processType);
		if(newSource != null && newSource.equals(EnumUploadSource.SCRIB)) {
			source = newSource;
			pageTitle = "Upload Scrib";
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>/ <%= pageTitle %>  /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
    
</head>
 <body marginwidth="0" marginheight="0" border="0" style="background-color:#D7C8FF">

		<div align="center">      
      	<form method="post" enctype="multipart/form-data">       	
			<div>
	    	   <table width="100%" cellpadding="0" cellspacing="0" border="0" >
			      
			      <tr>
			          <td class="screentitle"><br/><%= pageTitle %><br/><br/></td>
			      </tr>         
			      <tr>
			          <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>            
			      </tr>		      	       
		         <tr>		            
		            <td class="screencontent">
		              <table class="forms1">          
		                <tr>
		                  <td>File Input:</td>
		                  <td>                  
		                    <input type="file" size="30" name="file"/>
		                    <spring:bind path="command.file">
		                    		<c:forEach items="${status.errorMessages}" var="error">
		                                  &nbsp;<span id="file"><c:out value="${error}"/></span>
		                            </c:forEach> 
		                    </spring:bind>
		                  </td>
		                  <td>&nbsp;</td>
		                   <spring:bind path="command.processType">
                            	<input type="HIDDEN" name="<c:out value="${status.expression}"/> value="<c:out value="${status.value}"/> />                            	
                            </spring:bind>
		                </tr>
					  </table>
					</td>		                
              	 </tr>
              	 <tr>
              	 	<td align="center"><br/>
                   		<input type = "submit" value="&nbsp;Upload&nbsp;" />
                	</td>
                </tr>     		         
		      </table>   
	     	 </div>      
		</form>
		</div>
	



</body>
</html>

