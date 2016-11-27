<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>/ FreshDirect Transportation Admin : Geography : Building Info : Edit Delivery Building Details</tmpl:put>

  <tmpl:put name='content' direct='true'>
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
  	  // if(document.getElementById('extraTimeNeeded').value == "0")
		//  document.getElementById('extraTimeNeeded').value = "";   
		
	   if(document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[0].checked==true){
	        //alert("if->");
	        $('difficultToDeliver').checked = true;
	        $('difficultReason').disabled=false;		
 			//$('extraTimeNeeded').disabled=false; 
	   }
	   else {
   	        //alert("else->");
   	        $('difficultToDeliver').checked = false;
		$('difficultReason').disabled=true;		
 			//$('extraTimeNeeded').disabled=true; 
			//serviceTimeOverride
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
	//if($('isNew').value == "true"){  //new
	   //alert("New!");	   	   	
  	  	//return;
	 //}
	 //alert("Old!");
	 do_refresh();
}

function clearAll(){
//alert("Clear");
if($('isNew').value != "true"){ //clear only applicable for a new entry.
	return;
}
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

$('hoursOpenMon').disabled=true;
$('hoursOpenTue').disabled=true;
$('hoursOpenWed').disabled=true;
$('hoursOpenThu').disabled=true;
$('hoursOpenFri').disabled=true;
$('hoursOpenSat').disabled=true;
$('hoursOpenSun').disabled=true;
$('hoursOpenMon').value="00:00 AM";
$('hoursOpenTue').value="00:00 AM";
$('hoursOpenWed').value="00:00 AM";
$('hoursOpenThu').value="00:00 AM";
$('hoursOpenFri').value="00:00 AM";
$('hoursOpenSat').value="00:00 AM";
$('hoursOpenSun').value="00:00 AM";

$('hoursCloseMon').disabled=true;
$('hoursCloseTue').disabled=true;
$('hoursCloseWed').disabled=true;
$('hoursCloseThu').disabled=true;
$('hoursCloseFri').disabled=true;
$('hoursCloseSat').disabled=true;
$('hoursCloseSun').disabled=true;
$('hoursCloseMon').value="00:00 AM";
$('hoursCloseTue').value="00:00 AM";
$('hoursCloseWed').value="00:00 AM";
$('hoursCloseThu').value="00:00 AM";
$('hoursCloseFri').value="00:00 AM";
$('hoursCloseSat').value="00:00 AM";
$('hoursCloseSun').value="00:00 AM";

$('commentMon').value="";
$('commentTue').value="";
$('commentWed').value="";
$('commentThu').value="";
$('commentFri').value="";
$('commentSat').value="";
$('commentSun').value="";
$('commentTue').value="";


$('svcIncludeMon').disabled=true;
$('svcIncludeTue').disabled=true;
$('svcIncludeWed').disabled=true;
$('svcIncludeThu').disabled=true;
$('svcIncludeFri').disabled=true;
$('svcIncludeSat').disabled=true;
$('svcIncludeSun').disabled=true;
$('svcIncludeMon').checked=false;
$('svcIncludeTue').checked=false;
$('svcIncludeWed').checked=false;
$('svcIncludeThu').checked=false;
$('svcIncludeFri').checked=false;
$('svcIncludeSat').checked=false;
$('svcIncludeSun').checked=false;

$('svcHoursOpenMon').disabled=true;
$('svcHoursOpenTue').disabled=true;
$('svcHoursOpenWed').disabled=true;
$('svcHoursOpenThu').disabled=true;
$('svcHoursOpenFri').disabled=true;
$('svcHoursOpenSat').disabled=true;
$('svcHoursOpenSun').disabled=true;
$('svcHoursOpenMon').value="00:00 AM";
$('svcHoursOpenTue').value="00:00 AM";
$('svcHoursOpenWed').value="00:00 AM";
$('svcHoursOpenThu').value="00:00 AM";
$('svcHoursOpenFri').value="00:00 AM";
$('svcHoursOpenSat').value="00:00 AM";
$('svcHoursOpenSun').value="00:00 AM";

$('svcHoursCloseMon').disabled=true;
$('svcHoursCloseTue').disabled=true;
$('svcHoursCloseWed').disabled=true;
$('svcHoursCloseThu').disabled=true;
$('svcHoursCloseFri').disabled=true;
$('svcHoursCloseSat').disabled=true;
$('svcHoursCloseSun').disabled=true;
$('svcHoursCloseMon').value="00:00 AM";
$('svcHoursCloseTue').value="00:00 AM";
$('svcHoursCloseWed').value="00:00 AM";
$('svcHoursCloseThu').value="00:00 AM";
$('svcHoursCloseFri').value="00:00 AM";
$('svcHoursCloseSat').value="00:00 AM";
$('svcHoursCloseSun').value="00:00 AM";

$('svcCommentMon').disabled=true;
$('svcCommentTue').disabled=true;
$('svcCommentWed').disabled=true;
$('svcCommentThu').disabled=true;
$('svcCommentFri').disabled=true;
$('svcCommentSat').disabled=true;
$('svcCommentSun').disabled=true;
$('svcCommentMon').value="";
$('svcCommentTue').value="";
$('svcCommentWed').value="";
$('svcCommentThu').value="";
$('svcCommentFri').value="";
$('svcCommentSat').value="";
$('svcCommentSun').value="";

//alert('svcCommentTue');

$('svcScrubbedStreet').disabled=true;
$('svcValidate').disabled=true;
$('svcCity').disabled=true;
$('svcState').disabled=true;
$('svcZip').disabled=true;
//alert('svcZip');

$('svcScrubbedStreet').value="";
$('svcValidate').checked=false;
$('svcCity').value="";
$('svcState').value="";
$('svcZip').value="";


document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[0].checked=false;
document.getElementById("deliveryBuildingDtlForm").difficultToDeliver[1].checked=true;
 
for (i=0; i<document.getElementById("deliveryBuildingDtlForm").handTruckAllowed.length; i++){
	document.getElementById("deliveryBuildingDtlForm").handTruckAllowed[i].disabled=true;
	document.getElementById("deliveryBuildingDtlForm").handTruckAllowed[i].checked=false;
	
}
for (i=0; i<document.getElementById("deliveryBuildingDtlForm").other.length; i++){
	document.getElementById("deliveryBuildingDtlForm").other[i].disabled=true;
	document.getElementById("deliveryBuildingDtlForm").other[i].checked=false;
}
 
 $('difficultReason').value="";
 $('serviceTimeOverride').value="";
 $('additional').value="";
 //$('extraTimeNeeded').value="";
 $('difficultReason').disabled=true;
 //$('extraTimeNeeded').disabled=true; 
 $('dlvServiceTimeType').disabled=true;
 $('serviceTimeOverride').disabled=true;
 $('serviceTimeOperator').disabled=true;
 $('serviceTimeAdjustable').disabled=true;
 $('additional').disabled=true;
}


	function init_clock(){

		
		var tpMon1 = new TimePicker('timeMon1_picker', 'hoursOpenMon', 'timeMon1_toggler', {imagesPath:"images"});
		var tpTue1 = new TimePicker('timeTue1_picker', 'hoursOpenTue', 'timeTue1_toggler', {imagesPath:"images"});
		var tpWed1 = new TimePicker('timeWed1_picker', 'hoursOpenWed', 'timeWed1_toggler', {imagesPath:"images"});
		var tpThu1 = new TimePicker('timeThu1_picker', 'hoursOpenThu', 'timeThu1_toggler', {imagesPath:"images"});
		var tpFri1 = new TimePicker('timeFri1_picker', 'hoursOpenFri', 'timeFri1_toggler', {imagesPath:"images"});
		var tpSat1 = new TimePicker('timeSat1_picker', 'hoursOpenSat', 'timeSat1_toggler', {imagesPath:"images"});
		var tpSun1 = new TimePicker('timeSun1_picker', 'hoursOpenSun', 'timeSun1_toggler', {imagesPath:"images"});
	    var tpMon2 = new TimePicker('timeMon2_picker', 'hoursCloseMon', 'timeMon2_toggler', {imagesPath:"images"});
		var tpTue2 = new TimePicker('timeTue2_picker', 'hoursCloseTue', 'timeTue2_toggler', {imagesPath:"images"});
		var tpWed2 = new TimePicker('timeWed2_picker', 'hoursCloseWed', 'timeWed2_toggler', {imagesPath:"images"});
		var tpThu2 = new TimePicker('timeThu2_picker', 'hoursCloseThu', 'timeThu2_toggler', {imagesPath:"images"});
		var tpFri2 = new TimePicker('timeFri2_picker', 'hoursCloseFri', 'timeFri2_toggler', {imagesPath:"images"});
		var tpSat2 = new TimePicker('timeSat2_picker', 'hoursCloseSat', 'timeSat2_toggler', {imagesPath:"images"});
		var tpSun2 = new TimePicker('timeSun2_picker', 'hoursCloseSun', 'timeSun2_toggler', {imagesPath:"images"});
		var tpSvcMon1 = new TimePicker('timeSvcMon1_picker', 'svcHoursOpenMon', 'timeSvcMon1_toggler', {imagesPath:"images"});
		var tpSvcTue1 = new TimePicker('timeSvcTue1_picker', 'svcHoursOpenTue', 'timeSvcTue2_toggler', {imagesPath:"images"});
		var tpSvcWed1 = new TimePicker('timeSvcWed1_picker', 'svcHoursOpenWed', 'timeSvcWed2_toggler', {imagesPath:"images"});
		var tpSvcThu1 = new TimePicker('timeSvcThu1_picker', 'svcHoursOpenThu', 'timeSvcThu2_toggler', {imagesPath:"images"});
		var tpSvcThu1 = new TimePicker('timeSvcThu1_picker', 'svcHoursOpenThu', 'timeSvcThu2_toggler', {imagesPath:"images"});
		var tpSvcFri1 = new TimePicker('timeSvcFri1_picker', 'svcHoursOpenFri', 'timeSvcFri2_toggler', {imagesPath:"images"});
		var tpSvcSat1 = new TimePicker('timeSvcSat1_picker', 'svcHoursOpenSat', 'timeSvcSat2_toggler', {imagesPath:"images"});
		var tpSvcSun1 = new TimePicker('timeSvcSun1_picker', 'svcHoursOpenSun', 'timeSvcSun2_toggler', {imagesPath:"images"});

		var tpSvcMon2 = new TimePicker('timeSvcMon2_picker', 'svcHoursCloseMon', 'timeSvcMon2_toggler', {imagesPath:"images"});
		var tpSvcTue2 = new TimePicker('timeSvcTue2_picker', 'svcHoursCloseTue', 'timeSvcTue2_toggler', {imagesPath:"images"});
		var tpSvcWed2 = new TimePicker('timeSvcWed2_picker', 'svcHoursCloseWed', 'timeSvcWed2_toggler', {imagesPath:"images"});
		var tpSvcThu2 = new TimePicker('timeSvcThu2_picker', 'svcHoursCloseThu', 'timeSvcThu2_toggler', {imagesPath:"images"});
		var tpSvcThu2 = new TimePicker('timeSvcThu2_picker', 'svcHoursCloseThu', 'timeSvcThu2_toggler', {imagesPath:"images"});
		var tpSvcFri2 = new TimePicker('timeSvcFri2_picker', 'svcHoursCloseFri', 'timeSvcFri2_toggler', {imagesPath:"images"});
		var tpSvcSat2 = new TimePicker('timeSvcSat2_picker', 'svcHoursCloseSat', 'timeSvcSat2_toggler', {imagesPath:"images"});
		var tpSvcSun2 = new TimePicker('timeSvcSun2_picker', 'svcHoursCloseSun', 'timeSvcSun2_toggler', {imagesPath:"images"});

	}

</script>
 
 
		<br />	
			<form:form commandName = "deliveryBuildingDtlForm" method="post">
     		<form:hidden path="isNew"/>     
 			
			<form:hidden path="dlvBuildingDtlId"/>
		<div  class="buildingdtl_content" align="center">

	<table id="buildingdtl" cellpadding="0" cellspacing="10" border="0">
 			
				<tr>
					<td colspan="2" align="center" class="screentitle"><b>Edit Delivery Building Details<b></td>
				</tr>
				<tr>
					<td colspan="2" class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
				</tr>
			<tr><td colspan="2">									
					 
			<%@ include file='./buildingdtl/building.jspf'%> 
			<BR><BR>								 

			<%@ include file='./buildingdtl/hoursoperation.jspf'%> 
			<BR><BR>
						
			<%@ include file='./buildingdtl/difficultdeliver.jspf'%> 
			<BR><BR>
							
			<%@ include file='./buildingdtl/buildingtype.jspf'%> 
			<BR><BR>
			
			<%@ include file='./buildingdtl/svcentdetails.jspf'%> 
			<BR><BR>				
				       
			<%@ include file='./buildingdtl/svchoursoperation.jspf'%> 
			<BR><BR>										

			<%@ include file='./buildingdtl/misc.jspf'%> 
			<BR><BR>		
			</td></tr>
				
				<tr>
				    <td  align="right">
					   <input type = "submit" value="&nbsp;Save&nbsp;"  />
					</td>			
			    <td align="left">
					   <input type = "button" value="&nbsp;Clear&nbsp;" onClick="javascript:clearAll();" />
					</td>			
				</tr>
			</table>
			
			</form:form>
		
		 </div>
		 <script type="text/javascript">
		     my_init();
		 </script>
  </tmpl:put>
</tmpl:insert>