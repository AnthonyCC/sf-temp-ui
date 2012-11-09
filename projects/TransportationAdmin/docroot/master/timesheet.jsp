 	<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.util.TransportationAdminProperties' %>

<tmpl:insert template='/common/sitelayout.jsp'>

  <tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
  </tmpl:put>	

  <tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
  <tmpl:put name='title' direct='true'> Operations : Employee Timesheet </tmpl:put>

  <tmpl:put name='content' direct='true'>
  
  <style type="text/css">
table.timesheetTable {
	text-align:center; 
    margin-left:auto; 
    margin-right:auto; 
	border-width: 1px;
	border-spacing: 0px;
	border-style: solid;
	border-color: gray;
	border-collapse: collapse;
}
table.timesheetTable th {
	border-width: 2px;
	border-style: inset;
	border-color: gray;
	padding : 10px;
}
table.timesheetTable td {
	border-width: 2px;
	border-style: inset;
	border-color: gray;
	padding : 10px;
}

</style>
  
  
    <br/>     
    <div align="center">     
     
    <span class="screentitle">Employee Timesheet</span>
   
		<div class="screenmessages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	
	<table border="0" align="center" cellspacing="10" cellpadding="5">
       
		<tr><td align="left">
    			Select Date:</td><td>&nbsp;&nbsp;</td>
                  	<td align="left"><input maxlength="40" name="selectedDate" id="selectedDate" value='<c:out value="${selectedDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_selectedDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "selectedDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_selectedDate"
                       }
                      );
                       
                      </script>
           
               </td>
           </tr>
           <br/>
           <br/>
            <tr><td align="left">
              			Select Zone:</td><td>&nbsp;&nbsp;</td>
              			<td align="left">
							<select id="zone" name="zone">
								<option value="">--Select Zone</option> 
					              <c:forEach var="zone" items="${zones}">
					              <c:out value="${zoneS}"/><c:out value="${zone.zoneCode}"/>
									  <c:choose>
											<c:when test="${zoneS == zone.zoneCode}" > 
											
											  <option selected value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/> - <c:out value="${zone.name}"/></option>
											</c:when>
											<c:otherwise> 
											  <option value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/> - <c:out value="${zone.name}"/></option>
											</c:otherwise> 
										</c:choose>
								</c:forEach>   
							</select>
						</td>
				</tr>
                                                   
              <tr>
			    <td colspan="3" align="center">
				 	<input type = "submit" value="&nbsp;Search&nbsp;"  onclick="javascript:doCompositeLink('selectedDate','zone','timesheet.do');" />
				 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				 	<input type = "button" value="&nbsp;Clear&nbsp;" onclick="javascript:doClear();"  />
				 		
			    </td>   
			  </tr>
								
              </table>        
     
     </div>
     <br/>
 		<table class="timesheetTable">
		<tr><th>Badge ID<th>Clock In<th>Clock Out<th>Tip Amount<th>On Break<th>Signature
 		 <c:forEach var="employeeInfo" items="${employees}">
 		 <tr><td><c:out value="${employeeInfo.badgeId}"/><td><c:out value="${employeeInfo.clockIn}"/>
 		 <td><c:out value="${employeeInfo.clockOut}"/><td><c:out value="${employeeInfo.tipAmount}"/>
 		 <td><c:out value="${employeeInfo.onBreak}"/><td id="signatureLink"><a id="signAnchor" href="#" onclick ="javascript:viewsignature(event, '<c:out value="${employeeInfo.badgeId}"/>');">
 		 View Signature</a></td>
 		 </c:forEach>
 		</table>
	<script>
	function doClear()
	{
		document.getElementById("selectedDate").value = "";
		document.getElementById("zone").selectedIndex = 0;
	}
	function doCompositeLink(compId1,compId2,url) {
         var param1 = document.getElementById(compId1).value;
         var param2 = document.getElementById(compId2).value;
      
         if(param1.length == 0 || param2.length == 0 ) {
         		alert("Please select the required filter param (Date, Zone)");
         } else {
         	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
         }
	}	
	
	function viewsignature(event, empId)
	{
		var selectedDate = document.getElementById('selectedDate').value;
		var zone = document.getElementById('zone').value;
		
		var e=window.event? event : event
				if (e.pageX || e.pageY) 	{
					posx = e.pageX;
					posy = e.pageY;
				}
				else if (e.clientX || e.clientY) 	{
					posx = e.clientX + document.body.scrollLeft
						+ document.documentElement.scrollLeft;
					posy = e.clientY + document.body.scrollTop
						+ document.documentElement.scrollTop;
				}
		var w = window.open('','signature','left='+posx+'px,top='+posy+'px,height=210px,width=250px,resizable=no,location=no');
		w.document.writeln("<body bgcolor='#ffffff'>");
		w.document.writeln("<img width='200px' height='100px' src='" + 'viewsignature.do?selectedDate='+selectedDate+'&zone='+zone+'&empId='+empId + "'>");
		w.document.writeln("<\/body>");
		w.document.close();
	}
	
	
	 
	</script> 
	
  </tmpl:put>
</tmpl:insert>