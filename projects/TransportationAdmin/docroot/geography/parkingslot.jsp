<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%		
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "true");		
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>/ Parking Slots for Location /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
    
</head>
 <body marginwidth="0" marginheight="0" border="0">
	<script>
		function doCompositeLink(compId1, url) {
          var param1 = document.getElementById(compId1).value;
          location.href = url+"?"+compId1+"="+ param1;
		}
	</script>
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="navlist">
				<table border="0" width="100%">
					<tr>	
						 <td style="float:left;text-align:center;font-size:11px;font-weight:bold">Parking Location
							 <select id="parlingloc" name="parlingloc" style="font-size:11px" >
				                  	<option value='<c:out value=""/>'>--Please Select</option>
							 		<c:forEach var="ploc" items="${parkingLocs}">
									<option value='<c:out value="${ploc.locationName}"/>'><c:out value="${ploc.locationDesc}"/></option>
					        		</c:forEach>
							</select>
							<input type="button" value="View" style="font-size:11px" onclick="javascript:doCompositeLink('parlingloc','parkingslot.do');" /> 
						 </td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td>
						<form id="parkingSlotForm" action="" method="post">	
							<ec:table tableId="ec_parkingslots" items="parkingLocSlots" action="${pageContext.request.contextPath}/parkingslot.do"
								imagePath="${pageContext.request.contextPath}/images/table/*.gif" title="&nbsp;"
								width="98%"  view="fd" form="parkingLocationForm" autoIncludeParameters="true">
							<ec:row interceptor="obsoletemarker">
							   <ec:column title=" " width="5px" filterable="false" sortable="false" cell="selectcol" property="slotNumber" />
							  <ec:column property="slotNumber" title="Slot Number" alias="slotNo" />
							  <ec:column property="barcodeStatus" title="Barcode" />
							  <ec:column property="pavedStatus" title="Paved" />
							  <ec:column property="slotDesc" title="Slot Description" />
							  <ec:column property="location.locationName" title="Parking Location"/>
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
	 	addRowHandlersFilterTest('ec_parkingslots_table', 'rowMouseOver', 'editparkingslot.do','slotNo',0, 0);
	</script>
</body>
</html>