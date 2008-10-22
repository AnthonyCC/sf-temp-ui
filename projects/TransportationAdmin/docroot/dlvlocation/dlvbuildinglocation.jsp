<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.SecurityManager' %>
<%@ page import='com.freshdirect.transadmin.model.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%		
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");		
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>/ Location For Building  /</title>
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
    <script>
	    function addCustomRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
		
			var previousClass = null;
		    var table = document.getElementById(tableId);
		    
		    if(table != null) {
			    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
			    for (i = 0; i < rows.length; i++) {	    	
			        var cells = rows[i].getElementsByTagName("td");
			        
			        for (j = 1; j < cells.length; j++) {
			        	
			            cells[j].onmouseover = function () {
			            	previousClass = this.parentNode.className;
			            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
			            };
			        
			            cells[j].onmouseout = function () {
			              	this.parentNode.className = previousClass;
			            };
			        
			            if(checkCol == -1 || checkCol != j ) {
							if(!(needKeyPress && (j == (cells.length-1)))) {	            
						    	cells[j].onclick = function () {			    		
						      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
						      		self.opener.location.href = url+"?"+ paramName + "=" + cell.innerHTML;
						      		self.close();						      					      		
						    	};
						    }
				    	}    	
			        }
			    }
			}
		}
    </script>
	<table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr class="poptitle" >	
				<%
					List dataList = (List)request.getAttribute("dlvlocationlist");
					
					if(dataList != null && dataList.size() >0) {
						DlvLocation loc = (DlvLocation)dataList.get(0);
						StringBuffer strBuf = new StringBuffer();
						strBuf.append(loc.getBuilding().getSrubbedStreet()).append(", ").append(loc.getBuilding().getCity())
										.append(", ").append(loc.getBuilding().getState()).append(" ").append(loc.getBuilding().getZip());
						%> <td><%= strBuf.toString() %></td>
					<%} else { %>
						<td>&nbsp;</td>
					<%}					
				%>			
			</tr>
			<tr>
				<td class="navlist" colspan="3" bgcolor="c00cc3d">
				<table class="navtbl" border="0" width="100%">
					<tr>
						<td><form id="deliveryLocationForm" action="" method="post">	
				<ec:table items="dlvlocationlist"   action="${pageContext.request.contextPath}/dlvbuildinglocation.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
				    width="98%"  view="fd" form="deliveryLocationForm" autoIncludeParameters="true" rowsDisplayed="25"  >
				    
				        
				    <ec:row interceptor="obsoletemarker">				    				    	
				      <ec:column property="locationId" title="ID" alias="locId" width="10px"/>
				      <ec:column property="apartment" title="Apt" width="15px"/>				      				      
				      <ec:column alias="serviceTimeType" property="serviceTimeType.name" title="Service Time Type" width="25px"/>
				      
				    </ec:row>
				  </ec:table>
			 </form> 	
		</td>				
					</tr>
					</table>
				</td>
			</tr>
			
	</table>
	<script>
			addCustomRowHandlers('ec_table', 'rowMouseOver', 'editdlvlocation.do','id',0, 0, true);
		</script>
</body>
</html>

