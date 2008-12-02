<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<% System.out.println("yyyyyyyyyyyyyDDDDDDDDTBRDDDDDDDDDDDDDDDDDDDDDDDDDDD");%>




<!--  dfsdsdfskdmfnsdlfmsdfmsdl;fmsd.sdfmsdfsdfsdfsdf
sdfsdfsdfsdf

<body onload="my_init(); init_columns();display_columns()">

 -->
 
 <body onLoad="javascript:my_init();">
 
 <script type="text/javascript">

function toggle(checked, field1, field2) {
    if(checked){
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=false;
	}
	else {
      document.getElementById(field1).disabled=document.getElementById(field2).disabled=true;
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

function my_init(){
//alert("dlvBuildingDtlId="+dlvBuildingDtlId.value);
//alert("difficultToDeliver1="+difficultToDeliver1.value);
//alert("difficultToDeliver2="+difficultToDeliver2.value);

	if(dlvBuildingDtlId.value == ""){  //new
	   //alert("New!");
	   $('addrType').value="Residential";
	   $('difficultToDeliver1').checked=false;
	   $('difficultToDeliver2').checked=true;
	 }
	 else{							//old
	   //alert("Old!");
	   //alert("difficultToDeliver1.value="+difficultToDeliver1.value);
	   //alert("difficultToDeliver2.value="+difficultToDeliver2.value);
	   //alert("difficultToDeliver1.checked="+difficultToDeliver1.checked);
	   //alert("difficultToDeliver2.checked="+difficultToDeliver2.checked);
	   if(difficultToDeliver1.checked == true){
	        //alert("if->");
			$('difficultReason').readOnly=false;
 			$('extraTimeNeeded').readOnly=false; 
	   }
	   else {
   	        //alert("else->");
			$('difficultReason').readOnly=true;
 			$('extraTimeNeeded').readOnly=true; 
	   }
	 }  

}

function clearAll(){
//alert("Clear");

$('addrType').value="Residential";

$('companyName').value="";

$('doorman1').checked=false;
$('walkup1').checked=false;
$('elevator1').checked=false;
$('svcEnt1').checked=false;
$('house1').checked=false;

$('includeMon1').checked=false;
$('includeTue1').checked=false;
$('includeWed1').checked=false;
$('includeThu1').checked=false;
$('includeFri1').checked=false;
$('includeSat1').checked=false;
$('includeSun1').checked=false;

$('commentMon').value="";
$('commentTue').value="";
$('commentWed').value="";
$('commentThu').value="";
$('commentFri').value="";
$('commentSat').value="";
$('commentSun').value="";
$('commentTue').value="";


$('svcIncludeMon1').checked=false;
$('svcIncludeTue1').checked=false;
$('svcIncludeWed1').checked=false;
$('svcIncludeThu1').checked=false;
$('svcIncludeFri1').checked=false;
$('svcIncludeSat1').checked=false;
$('svcIncludeSun1').checked=false;

$('svcIncludeMon1').disabled=true;
$('svcIncludeTue1').disabled=true;
$('svcIncludeWed1').disabled=true;
$('svcIncludeThu1').disabled=true;
$('svcIncludeFri1').disabled=true;
$('svcIncludeSat1').disabled=true;
$('svcIncludeSun1').disabled=true;


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

$('svcScrubbedStreet').readOnly=true;
$('svcValidate1').disabled=true;
$('svcAddrLine2').readOnly=true;
$('svcCity').readOnly=true;
$('svcState').readOnly=true;
$('svcZip').readOnly=true;
//alert('svcZip');

$('svcScrubbedStreet').value="";
//$('svcValidate1').checked=false;
$('svcAddrLine2').value="";
$('svcCity').value="";
$('svcState').value="";
$('svcZip').value="";

 $('difficultToDeliver1').value=false;
 $('difficultToDeliver2').value=true;
 $('difficultReason').value="";
 $('extraTimeNeeded').value="";
 $('difficultReason').readOnly=true;
 $('extraTimeNeeded').readOnly=true; 
 


}

</script>

<tmpl:insert template='/common/sitelayout.jsp'>

<!--  dfsdsdfskdmfnsdlfmsdfmsdl;fmsd.sdfmsdfsdfsdfsdf
sdfsdfsdfsdf

 -->
 
    <tmpl:put name='title' direct='true'>Edit Delivery Building Detail</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
			<form:form commandName = "deliveryBuildingDtlForm" method="post">
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
		 
	</tmpl:put>
</tmpl:insert>
</body>
