<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>/ Edit Parking slot /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
</head>
 <body marginwidth="0" marginheight="0" border="0">

		<br/>	
		<div align="center">
			<form:form commandName = "parkingSlotForm" method="post">
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Edit Parking Slot</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
                        <table class="forms1">
							  <tr>
                               <td>Slot Number</td>
							    <td>
							  	 	<form:input maxlength="50" size="30" path="slotNumber" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="slotNumber" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Slot Description</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="slotDesc" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="slotDesc" />
							 	</td>
							 </tr>  
							 <tr>
								<td>Barcode status</td>
								<td> 
									<form:select path="barcodeStatus" >
										<form:option value="" label="--Please Select Location"/>
										<form:options items="${parkingSlotBarcodeStatus}" itemLabel="name" itemValue="name" />
									</form:select> 
								</td>
								<td><form:errors path="barcodeStatus" />&nbsp;</td>
							</tr>
							 <tr>
							    <td>Paved status</td>
							    <td> 								  
							  	 	<form:select path="pavedStatus" >
										<form:option value="" label="--Please Select Location"/>
										<form:options items="${parkingSlotPavedStatus}" itemLabel="name" itemValue="name" />
									</form:select> 
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="pavedStatus" />
							 	</td>
							 </tr> 
							  <tr>
							     <td>Parking Location</td>
							    <td> 
							  	 	<form:select path="location" >
										<form:option value="" label="--Please Select Location"/>
										<form:options items="${parkingLocs}" itemLabel="locationDesc" itemValue="locationName" />
									</form:select> 
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="location" />
							 	</td>
							 </tr>

                            <tr><td colspan="3">&nbsp;</td></tr>
							<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								   <input type = "button" value="&nbsp;Back&nbsp;" onclick="document.location='/TrnAdmin/parkingslot.do';" />
								</td>
							</tr>
							</table>
						</td>
					</tr>
				</table>
			</form:form>
		 </div>
</body>
<html>