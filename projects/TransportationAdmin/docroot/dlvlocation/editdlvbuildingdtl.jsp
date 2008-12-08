<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Edit Delivery Location</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : Building Detail /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
</head>
 
 <body  marginwidth="0" marginheight="0" border="0" onLoad="javascript:my_init();">
 
 <script type="text/javascript">

function toggle(checked, field1, field2) {
    if(checked){
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=false;
	}
	else {
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=true;
	}
}

function toggle3(checked, field1, field2,field3) {
    if(checked){
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=document.getElementById(field3).disabled=false;
	}
	else {
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=document.getElementById(field3).disabled=true;
	}
}

function $() {
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++) {
        var element = arguments[i];
        if (typeof element == 'string')
            element = document.getElementById(element);
        if (arguments.length == 1)
            return element;
        elements.push(element);
    }
    return elements;
}

function do_refresh()
{
	   //alert("difficultToDeliver[0].checked="+document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[0].checked);
	   if(document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[0].checked==true){
	        //alert("if->");
	        $('difficultToDeliver').checked = true;
	        $('difficultReason').disabled=false;
 			$('extraTimeNeeded').disabled=false; 
	   }
	   else {
   	        //alert("else->");
   	        $('difficultToDeliver').checked = false;
			$('difficultReason').disabled=true;
 			$('extraTimeNeeded').disabled=true; 
	   }
	   //alert("doorman.checked="+document.getElementById("deliveryBuildingDtlForm").doorman.checked);
	   doormanf(document.getElementById("deliveryBuildingDtlForm").doorman.checked);
	   walkupf(document.getElementById("deliveryBuildingDtlForm").walkup.checked);
	   elevatorf(document.getElementById("deliveryBuildingDtlForm").elevator.checked);
	   svcEntf(document.getElementById("deliveryBuildingDtlForm").svcEnt.checked);
	   housef(document.getElementById("deliveryBuildingDtlForm").house.checked);
	   hoursoperationf();
	   svchoursoperationf();
	   
}

function my_init(){
//alert("isNew="+$('isNew').value);
//alert("difficultToDeliver[0].checked="+document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[0].checked);

	if($('isNew').value == "true"){  //new
	   //alert("New!");	   	   	
  	  	document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[1].checked=true;
 		$('difficultReason').disabled=true;
 		$('extraTimeNeeded').disabled=true; 
  	  	return;
	 }
	 //alert("Old!");
	 do_refresh();
}

function clearAll(){
//alert("Clear");

$('addrType').value="Residential";

$('companyName').value="";

$('doorman').checked=false;
$('walkup').checked=false;
$('elevator').checked=false;
$('svcEnt').checked=false;
$('house').checked=false;

$('includeMon').checked=false;
$('includeTue').checked=false;
$('includeWed').checked=false;
$('includeThu').checked=false;
$('includeFri').checked=false;
$('includeSat').checked=false;
$('includeSun').checked=false;

$('commentMon').value="";
$('commentTue').value="";
$('commentWed').value="";
$('commentThu').value="";
$('commentFri').value="";
$('commentSat').value="";
$('commentSun').value="";
$('commentTue').value="";


$('svcIncludeMon').checked=false;
$('svcIncludeTue').checked=false;
$('svcIncludeWed').checked=false;
$('svcIncludeThu').checked=false;
$('svcIncludeFri').checked=false;
$('svcIncludeSat').checked=false;
$('svcIncludeSun').checked=false;

$('svcIncludeMon').disabled=true;
$('svcIncludeTue').disabled=true;
$('svcIncludeWed').disabled=true;
$('svcIncludeThu').disabled=true;
$('svcIncludeFri').disabled=true;
$('svcIncludeSat').disabled=true;
$('svcIncludeSun').disabled=true;


$('svcHoursOpenMon').disabled=true;
$('svcHoursOpenTue').disabled=true;
$('svcHoursOpenWed').disabled=true;
$('svcHoursOpenThu').disabled=true;
$('svcHoursOpenFri').disabled=true;
$('svcHoursOpenSat').disabled=true;
$('svcHoursOpenSun').disabled=true;

$('svcHoursCloseMon').disabled=true;
$('svcHoursCloseTue').disabled=true;
$('svcHoursCloseWed').disabled=true;
$('svcHoursCloseThu').disabled=true;
$('svcHoursCloseFri').disabled=true;
$('svcHoursCloseSat').disabled=true;
$('svcHoursCloseSun').disabled=true;

$('svcCommentMon').value="";
$('svcCommentTue').value="";
$('svcCommentWed').value="";
$('svcCommentThu').value="";
$('svcCommentFri').value="";
$('svcCommentSat').value="";
$('svcCommentSun').value="";

$('svcCommentMon').disabled=true;
$('svcCommentTue').disabled=true;
$('svcCommentWed').disabled=true;
$('svcCommentThu').disabled=true;
$('svcCommentFri').disabled=true;
$('svcCommentSat').disabled=true;
$('svcCommentSun').disabled=true;


//alert('svcCommentTue');

$('svcScrubbedStreet').disabled=true;
$('svcValidate').disabled=true;
$('svcAddrLine2').disabled=true;
$('svcCity').disabled=true;
$('svcState').disabled=true;
$('svcZip').disabled=true;
//alert('svcZip');

$('svcScrubbedStreet').value="";
//$('svcValidate').checked=false;
$('svcAddrLine2').value="";
$('svcCity').value="";
$('svcState').value="";
$('svcZip').value="";

 $('difficultToDeliver').value=false;
 $('difficultReason').value="";
 $('extraTimeNeeded').value="";
 $('difficultReason').disabled=true;
 $('extraTimeNeeded').disabled=true; 
 


}

</script>


		<br/>	
			<form:form commandName = "deliveryBuildingDtlForm" method="post">
     		<form:hidden path="isNew"/>     
 			<form:hidden path="dlvBuildingDtlId"/>
			<form:hidden path="dlvBuildingId"/>
		<div align="center">

 
	<table width="60%" cellpadding="0" cellspacing="0" border="0">
 
				<tr>
					<td align="center" class="screentitle"><b>Edit Delivery Building Detail<b></td>
				</tr>
				<tr>
					<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
				</tr>
		</table>
		</div>
		
	<table width="60%" cellpadding="0" cellspacing="0" border="0">
 			
		<div align="left">
			<tr><BR><BR>										
					 
			<%@ include file='building.jspf'%> 
			<BR><BR>								 

			<%@ include file='hoursoperation.jspf'%> 
			<BR><BR>
						
			<%@ include file='difficultdeliver.jspf'%> 
			<BR><BR>
							
			<%@ include file='buildingtype.jspf'%> 
			<BR><BR>
			
			<%@ include file='svcentdetails.jspf'%> 
			<BR><BR>				
				       
			<%@ include file='svchoursoperation.jspf'%> 
			<BR><BR>										

			<%@ include file='misc.jspf'%> 
			<BR><BR>										
				
            <tr><td colspan="3">&nbsp;</td></tr>
				<tr>
				    <td colspan="3" align="left">
					   <input type = "submit" value="&nbsp;Save&nbsp;"  />
					</td>			
			    <td colspan="3" align="center">
					   <input type = "button" value="&nbsp;Clear&nbsp;" onClick="javascript:clearAll();" />
					</td>			
				</tr>
			</table>
			
			</form:form>
		 </div>
		
</body>
</html>
  </tmpl:put>
</tmpl:insert>
