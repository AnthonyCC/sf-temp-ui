<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<!-- 

									<td colspan="2"><input type="button" value="Add Profile/Update Operator" class="submit" onclick="javascript:addProfile();"></td>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <title></title>
<script type="text/javascript">
    function testadd() {
            document.getElementById('containter').innerHTML = document.getElementById('containter').innerHTML + "<div>test row</div>";
    }
</script>
</head>
<body>
      <div id="containter">
            <div>test row</div>
      </div>
      <div onclick="testadd();">plus</div>
 </body>
</html>

 -->


<script language="Javascript">

  var rollingIndex = 0;

    function addProfile() {
                         alert("inside add profile");
                      	var dayOfWeek = document.getElementById('dayOfWeek');
							var startTime = document.getElementById('startTime');
							var endTime = document.getElementById('endTime');
                        var condition = document.getElementById('condition');                            					
                            
							//if(dayOfWeek != null && 
									//	dayOfWeek.value.length > 0 &&
									//		startTime != null	&& 
									//			startTime.value.length > 0) {																														
								addProfleRow(dayOfWeek.value,startTime.value,endTime.value,condition.value);	
							//}				
							//reArrangeCondition();		
					  }
					 					  
					  function addProfleRow(dayOfWeek, startTime, endTime, condition) {
                        alert("inside add profile row"+dayOfWeek);
					  		if( document.createElement && document.childNodes ) {
								var profileTableFld = document.getElementById('profileListTB').tBodies[0];																
								if(profileTableFld != null) {									
										var row = document.createElement('tr');
										rollingIndex++;
										var tmpID = 'attributeList['+rollingIndex+']';
										row.id = tmpID;
									    var td1 = document.createElement('td');
									    td1.appendChild(document.createTextNode(dayOfWeek));
									    var td2 = document.createElement('td');
									    td2.appendChild (document.createTextNode(startTime));
									    var td3 = document.createElement('td');
									    td3.appendChild (document.createTextNode(endTime));
									    var td4 = document.createElement('td');
                                    td4.appendChild(document.createTextNode(condition));    
                                   var td5 = document.createElement('td');
									    var tdDelete = document.createElement('a');
									    tdDelete.innerHTML = 'Delete';
									    tdDelete.href = "javascript:deleteProfile('"+tmpID+"')";
									    td5.appendChild (tdDelete);
									    row.appendChild(td1);
									    row.appendChild(td2);
									    row.appendChild(td3);
									    row.appendChild(td4);
                                    row.appendChild(td5);
									    profileTableFld.appendChild(row);
									    createHiddenInput(rollingIndex, dayOfWeek, startTime, endTime, condition);
								}								
							}
					 }
                     
                  
					function createHiddenInput(indexVal, dayOfWeek, startTime, endTime, condition) {
						
						var tmpId = 'attributeList['+indexVal+']'+'.';
						var newElementValue1 = document.createElement("input");
						newElementValue1.setAttribute("type", "hidden");
                     newElementValue1.setAttribute("name", tmpId+'dayOfWeek');
						newElementValue1.setAttribute("id", tmpId+'dayOfWeek');
						newElementValue1.setAttribute("value", dayOfWeek);
						
						var newElementValue2 = document.createElement("input");
						newElementValue2.setAttribute("type", "hidden");
						newElementValue2.setAttribute("name", tmpId+'startTime');
						newElementValue2.setAttribute("id", tmpId+'startTime');
						newElementValue2.setAttribute("value", startTime);
						
                     var newElementValue3 = document.createElement("input");
						newElementValue3.setAttribute("type", "hidden");
						newElementValue3.setAttribute("name", tmpId+'endTime');
						newElementValue3.setAttribute("id", tmpId+'endTime');
						newElementValue3.setAttribute("value", endTime);

                     var newElementValue4 = document.createElement("input");
						newElementValue4.setAttribute("type", "hidden");
						newElementValue4.setAttribute("name", tmpId+'condition');
						newElementValue4.setAttribute("id", tmpId+'condition');
						newElementValue4.setAttribute("value", condition);
						//document.getElementById('profileContainer').appendChild(newElementName);
						//document.getElementById('profileContainer').appendChild(newElementValue);	
						document.forms['geoRestrictionForm'].appendChild(newElementValue1);
						document.forms['geoRestrictionForm'].appendChild(newElementValue2);						
                     document.forms['geoRestrictionForm'].appendChild(newElementValue3);						
                     document.forms['geoRestrictionForm'].appendChild(newElementValue4);						
												
					}

					function deleteProfile(theCell) {
						var profileTableFld = document.getElementById('profileListTB');
						var profileTableRow = document.getElementById(theCell);
						
						if( document.createElement && document.childNodes 
								&& profileTableFld != null 
								&& profileTableRow != null) {
														
							var rowIndex = profileTableRow.rowIndex;
							var hiddenElement1 = document.getElementById(profileTableRow.id+'.dayOfWeek');
						    var hiddenElement2 = document.getElementById(profileTableRow.id+'.startTime');						    
                         var hiddenElement3 = document.getElementById(profileTableRow.id+'.endTime');						    
                         var hiddenElement4 = document.getElementById(profileTableRow.id+'.condition');						    
						    var parentElementNode = hiddenElement1.parentNode;
						   						   				    									    				
						    parentElementNode.removeChild(hiddenElement1);
						   	parentElementNode.removeChild(hiddenElement2);							
                            parentElementNode.removeChild(hiddenElement3);							
                            parentElementNode.removeChild(hiddenElement4);							
							profileTableFld.deleteRow(rowIndex);							
							//reArrangeCondition();						
						}
					}


             

</script>

    <tmpl:put name='title' direct='true'>Add/Modify Geo Restriction</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "geoRestrictionForm" method="post">
      
      <input type="hidden" id="profileOperator" name="profileOperator" class="input" value="" %>/> 
      
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">
              Add/Modify Geo Restriction             
            </td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Name</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="name" />
                </td>
                <td>
                  &nbsp;<form:errors path="name" />
                </td>
               </tr>
               
               <tr>
                  <td>Restriction Boundary</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="boundaryCode" />
                </td>
                <td>
                  &nbsp;<form:errors path="boundaryCode" />
                </td>
               </tr>
               
               <tr>
                  <td>Comments</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="comments" />
                </td>
                <td>
                  &nbsp;<form:errors path="comments" />
                </td>
               </tr>
                                           
               <tr>
                  <td>Start Date</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="startDate" />
                </td>
                <td>
                  &nbsp;<form:errors path="startDate" />
                </td>
               </tr>
                                           
               <tr>
                  <td>End Date</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="endDate" />
                </td>
                <td>
                  &nbsp;<form:errors path="endDate" />
                </td>
               </tr>
               
               <tr>
                  <td>Message</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="message" />
                </td>
                <td>
                  &nbsp;<form:errors path="message" />
                </td>
               </tr>
           </table>    
               
           <br><br>    
               
           <table width="50%" cellpadding="0" cellspacing="0" border="0">    
               <tr>
                  <td><B>Restriction Details</B></td>
                 </tr>                 
                 <tr>
                        <td><c:forEach var="gDay" items="${geoRestrictionForm.geoRestrictionDays}"></td>
                        <td><input type="text" name="startTime1" value="<c:out value="${gDay.startTime}"/>"></td>
                        <td><c:out value="${gDay.endTime}"/></td>
                        <td><c:out value="${gDay.condition}"/></td>
                        <td><c:out value="${gDay.restrictionDaysId.dayOfWeek}"/></td>                               
                        </c:forEach>
                  </tr>
                
               </table>        
               
               <table width="50%" cellpadding="0" cellspacing="0" border="0">    
               <tr>
                   <td><input type="text" id="dayOfWeek" name="dayOfWeek" value=""></td>                               
                   <td><input type="text" id="startTime" name="startTime" value=""></td>
                   <td><input type="text" id="endTime" name="endTime" value=""></td>
                   <td><input type="text" id="condition" name="condition" value=""></td>                                      
                 </tr>                 
                 <tr>
                 <td colspan="3"><input type="button" value="Add Rest Operator" class="submit" onclick="javascript:addProfile();"></td>
                  </tr>
                
               </table>        



                    <table id="profileListTB" border="1" cellpadding="2" cellspacing="0" >
					<thead>
					<tr> 
                   <th>dayOfWeek</th>                               
                   <th>startTime</th>
                   <th>endTime</th>
                   <th>condition</th>                                      
                   <th>&nbsp;</th>                                      
					</tr>
					</thead>	
					<tbody>						
				     </tbody> 				
				</table>								
               
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>