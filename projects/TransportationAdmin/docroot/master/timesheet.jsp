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
	padding : 15px;
}
table.timesheetTable td {
	border-width: 2px;
	border-style: inset;
	border-color: gray;
	padding : 15px;
}

</style>
  
  
    <br/>     
    <div align="center">     
     
    <span class="screentitle">Employee Timesheet</span>
    <c:if test="${not empty messages}">
		<div class="screenmessages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if>
	<table border="0" align="center" cellspacing="10" cellpadding="5">
		<tr><td align="left">
    			Select Date:</td><td>&nbsp;&nbsp;</td>
                  	<td><input maxlength="40" name="selectedDate" id="selectedDate" value='<c:out value="${selectedDate}"/>' style="width:90px"/>
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
              			<td>
							<select id="zone" name="zone">
								<option value="">--Select Zone</option> 
					              <c:forEach var="zone" items="${zones}">
					              <c:out value="${zoneS}"/><c:out value="${zone.code}"/>
									  <c:choose>
											<c:when test="${zoneS == zone.code}" > 
											
											  <option selected value="<c:out value="${zone.code}"/>"><c:out value="${zone.code}"/></option>
											</c:when>
											<c:otherwise> 
											  <option value="<c:out value="${zone.code}"/>"><c:out value="${zone.code}"/></option>
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
 		 <td><c:out value="${employeeInfo.onBreak}"/><td id="signatureLink"><a id="signAnchor" href="javascript:viewsignature('<c:out value="${employeeInfo.badgeId}"/>');">
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
	
	function viewsignature(empId)
	{
		var selectedDate = document.getElementById('selectedDate').value;
		var zone = document.getElementById('zone').value;
		var elem = document.getElementById("signatureLink");
		var oImg=document.createElement("img");
		oImg.setAttribute('src', 'viewsignature.do?selectedDate='+selectedDate+'&zone='+zone+'&empId='+empId);
		oImg.setAttribute('alt', 'signature');
		elem.replaceChild(oImg, document.getElementById("signAnchor"));
		
	}
	
	
	 
	</script> 
	
  </tmpl:put>
</tmpl:insert>