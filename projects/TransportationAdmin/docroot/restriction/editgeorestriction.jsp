<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<script language="Javascript">

  var rollingIndex = 0;
  var isEdit=false;
  var currentRow;
  
    function addOrUpdateProfile(){
        if(isEdit){
            
        }else{
            addProfile();        
        }
        
    }    
    
    function modifyRestrictionLink(num,actionType){
         var oldval=document.getElementById('restrictionLinkStr').value; 
         console.log("oldval :"+oldval);         
         console.log("num :"+num);         
         console.log("actionType :"+actionType);         
         var newval;
         if(actionType=='append'){            
             if(num<10){
               num='0'+num;
             }
             newval=oldval+'$'+num;
             document.getElementById('restrictionLinkStr').value=newval;             
         }else{             
             if(num<10){
               num='0'+num;
             }
             newval=oldval.replace('$'+num,'');
             document.getElementById('restrictionLinkStr').value=newval;
         }
    
    }
    
    
    
        function validateUserFields(){
           if(
              document.getElementById('DayOfWeekList').selectedIndex==0
                 || document.getElementById('condition').value=='' 
                   || document.getElementById('startTime').value=='' 
                      || document.getElementById('endTime').value==''){
                      
                   alert("field dayOfWeek, condition , startTime, endTime cannot be empty");   
                   return false;
            } 
           
            return true;
        }
    
    
        function editProfile() {
                            
                     if(!validateUserFields()){ return false; }
                    
                  
	                  var profileTableFld = document.getElementById('profileListTB');
						//var profileTableRow = document.getElementById(currentRow);
						
						var allTrElements = profileTableFld.getElementById(currentRow);
                     console.log("currentRow :"+currentRow);   
                     allTrElements.getElementsByTagName("td")[0].innerHTML=document.getElementById('DayOfWeekList')[document.getElementById('DayOfWeekList').selectedIndex].value;   
                     allTrElements.getElementsByTagName("td")[1].innerHTML=document.getElementById('condition').value;   
                     allTrElements.getElementsByTagName("td")[2].innerHTML=document.getElementById('startTime').value;   
                     allTrElements.getElementsByTagName("td")[3].innerHTML=document.getElementById('endTime').value;   
                                         
                      document.getElementById(currentRow+'.dayOfWeek').value=document.getElementById('DayOfWeekList')[document.getElementById('DayOfWeekList').selectedIndex].value;
                      document.getElementById(currentRow+'.condition').value=document.getElementById('condition').value;
                      document.getElementById(currentRow+'.startTime').value=document.getElementById('startTime').value;
                      document.getElementById(currentRow+'.endTime').value=document.getElementById('endTime').value;
                                                                   
                      document.getElementById('DayOfWeekList').selectedIndex=0;                                                                                         
                      document.getElementById('condition').value='';
                      document.getElementById('startTime').value='';
                      document.getElementById('endTime').value='';                         
                      
                    
                   isEdit=false;                      
                       
		  }

  

    function addProfile() {
                         //alert("inside add profile");
                      
                        if(!validateUserFields()){ return false; }                      
                         
                      	var dayOfWeek = document.getElementById('DayOfWeekList')[document.getElementById('DayOfWeekList').selectedIndex];
	                     var condition = document.getElementById('condition');                            					
  						   var startTime = document.getElementById('startTime');
						   var endTime = document.getElementById('endTime');
                            
							//if(dayOfWeek != null && 
									//	dayOfWeek.value.length > 0 &&
									//		startTime != null	&& 
									//			startTime.value.length > 0) {		
                        console.log("addProfile");                                    
								addProfleRow(dayOfWeek.value,condition.value,startTime.value,endTime.value);	
                                
                      document.getElementById('DayOfWeekList').selectedIndex=0;                                                                                         
                      document.getElementById('condition').value='';
                      document.getElementById('startTime').value='';
                      document.getElementById('endTime').value='';          
							//}				
					  }
                      
                      
                      
                      
					 					  
					  function addProfleRow(dayOfWeek, condition, startTime, endTime) {
                        //alert("inside add profile row"+dayOfWeek);
					  		if( document.createElement && document.childNodes ) {
								var profileTableFld = document.getElementById('profileListTB').tBodies[0];																
								if(profileTableFld != null) {									
										var row = document.createElement('tr');
										rollingIndex++;
										var tmpID = 'attributeList['+rollingIndex+']';
										row.id = tmpID;
									    var td1 = document.createElement('td');
									    td1.appendChild(document.createTextNode(dayOfWeek.value));
									    var td2 = document.createElement('td');
                                        td2.appendChild(document.createTextNode(condition));    
 									    var td3 = document.createElement('td');
									    td3.appendChild (document.createTextNode(startTime));
									    var td4 = document.createElement('td');
									    td4.appendChild (document.createTextNode(endTime));
                                   var td5 = document.createElement('td');
									    var tdDelete = document.createElement('a');
									    tdDelete.innerHTML = 'Delete';
									    tdDelete.href = "javascript:deleteProfile('"+tmpID+"')";
                                    var tdEdit = document.createElement('a');     
                                   tdEdit.innerHTML = 'Edit';
									    tdEdit.href = "javascript:copyProfile('"+tmpID+"')";     
                                        
                                   var tdSpacer = document.createElement('span');
									    tdSpacer.innerHTML = ' / ';     
                                        
									   td5.appendChild (tdDelete);
                                   td5.appendChild (tdSpacer);
                                   td5.appendChild (tdEdit);                                        
									    row.appendChild(td1);
									    row.appendChild(td2);
									    row.appendChild(td3);
									    row.appendChild(td4);
                                    row.appendChild(td5);
									    profileTableFld.appendChild(row);
									    createHiddenInput(rollingIndex,dayOfWeek,condition, startTime, endTime);
                                   var size = document.getElementById('restrictionListSize'); 
                                   size.value=""+rollingIndex;  
                                   modifyRestrictionLink(rollingIndex,'append');                                   
								}								
							}
					 }
                     
                  
					function createHiddenInput(indexVal, dayOfWeek, condition, startTime, endTime ) {
						
						var tmpId = 'attributeList['+indexVal+']'+'.';
						var newElementValue1 = document.createElement("input");
						newElementValue1.setAttribute("type", "hidden");
                     newElementValue1.setAttribute("name", tmpId+'dayOfWeek');
						newElementValue1.setAttribute("id", tmpId+'dayOfWeek');
						newElementValue1.setAttribute("value", dayOfWeek);
						
                  		var newElementValue2 = document.createElement("input");
						newElementValue2.setAttribute("type", "hidden");
						newElementValue2.setAttribute("name", tmpId+'condition');
						newElementValue2.setAttribute("id", tmpId+'condition');
						newElementValue2.setAttribute("value", condition);

						var newElementValue3 = document.createElement("input");
						newElementValue3.setAttribute("type", "hidden");
						newElementValue3.setAttribute("name", tmpId+'startTime');
						newElementValue3.setAttribute("id", tmpId+'startTime');
						newElementValue3.setAttribute("value", startTime);
						
                     	var newElementValue4 = document.createElement("input");
						newElementValue4.setAttribute("type", "hidden");
						newElementValue4.setAttribute("name", tmpId+'endTime');
						newElementValue4.setAttribute("id", tmpId+'endTime');
						newElementValue4.setAttribute("value", endTime);

   						//document.getElementById('profileContainer').appendChild(newElementName);
						//document.getElementById('profileContainer').appendChild(newElementValue);	
						document.forms['geoRestrictionForm'].appendChild(newElementValue1);
						document.forms['geoRestrictionForm'].appendChild(newElementValue2);						
                     document.forms['geoRestrictionForm'].appendChild(newElementValue3);						
                     document.forms['geoRestrictionForm'].appendChild(newElementValue4);						
                     
                     alert("created");
                     
												
					}

					function deleteProfile(theCell) {
						var profileTableFld = document.getElementById('profileListTB');
						var profileTableRow = document.getElementById(theCell);
						
						if( document.createElement && document.childNodes 
								&& profileTableFld != null 
								&& profileTableRow != null) {
														
							var rowIndex = profileTableRow.rowIndex;
							var hiddenElement1 = document.getElementById(profileTableRow.id+'.dayOfWeek');
                            var hiddenElement2 = document.getElementById(profileTableRow.id+'.condition');						    
						    var hiddenElement3 = document.getElementById(profileTableRow.id+'.startTime');						    
                            var hiddenElement4 = document.getElementById(profileTableRow.id+'.endTime');						    
						    var parentElementNode = hiddenElement1.parentNode;
						   						   				    									    				
						    parentElementNode.removeChild(hiddenElement1);
						   	parentElementNode.removeChild(hiddenElement2);							
                            parentElementNode.removeChild(hiddenElement3);							
                            parentElementNode.removeChild(hiddenElement4);							
							profileTableFld.deleteRow(rowIndex);
                        modifyRestrictionLink(rowIndex,'delete');                                   							
                        rollingIndex=rollingIndex-1; 
                         var size = document.getElementById('restrictionListSize'); 
                         size.value=""+rollingIndex;  
                        
                        
						}
					}
                    
                    
                  function copyProfile(theCell) {                                    											                     
                  
                       
                         currentRow=theCell;
                         alert("document.getElementById(theCell+'.dayOfWeek').value :"+document.getElementById(theCell+'.dayOfWeek').value);
                         document.getElementById('DayOfWeekList').selectedIndex=document.getElementById(theCell+'.dayOfWeek').value;
                         document.getElementById('condition').value=document.getElementById(theCell+'.condition').value;
                         document.getElementById('startTime').value=document.getElementById(theCell+'.startTime').value;
                         document.getElementById('endTime').value=document.getElementById(theCell+'.endTime').value;
                         
                         isEdit=true;                        
					}  

             

</script>

    <tmpl:put name='title' direct='true'>Add/Modify Geo Restriction</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "geoRestrictionForm" method="post">
      
      <input type="hidden" id="profileOperator" name="profileOperator" class="input" value="" %>/> 
      <input type="hidden" id="restrictionListSize" name="restrictionListSize" class="input" value="0" %>/> 
      <input type="hidden" id="restrictionId" name="restrictionId" class="input" value="<c:out value="${geoRestrictionForm.id}"/>"/>" %>/> 
      <input type="hidden" id="restrictionLinkStr" name="restrictionLinkStr" class="input" value=""/>" %>/> 
      
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
                  <spring:bind path="geoRestrictionForm.boundaryCode">                                    
                     <SELECT name="boundaryCode">
                        <c:forEach var="contentTypeVar" items="${restrictionBoundries}">       
                             <c:if test="${geoRestrictionForm.boundaryCode == contentTypeVar.code}">
                                <OPTION selected="<c:out value="${contentTypeVar.code}"/>" value="<c:out value="${contentTypeVar.code}"/>"><c:out value="${contentTypeVar.name}"/></OPTION>
                             </c:if>
                             <c:if test="${geoRestrictionForm.boundaryCode != contentTypeVar.code}">
                                 <OPTION value="<c:out value="${contentTypeVar.code}"/>"><c:out value="${contentTypeVar.name}"/></OPTION>                    
                             </c:if>                
                         </c:forEach>
                     </SELECT>
                </spring:bind>       
                    
                    
                </td>
                <td>
                  &nbsp;<form:errors path="boundaryCode" />
                </td>
               </tr>
               
               <tr>
               <td>Comments</td>
                  <td>                  
                    <form:textarea path="comments" rows="5" cols="45" />
                </td>
                <td>
                  &nbsp;<form:errors path="comments" />
                </td>
               </tr>
                                           
               <tr>
                  <td>Start Date</td>
                  <td>                                    
                  			<span><input maxlength="10" size="10" name="startDate" id="startDate" 
                            value="<c:out value="${geoRestrictionForm.startDateInString}"/>" /></span>
							<span><input id="trigger_startDate" type="image" alt="Calendar" 
                            src="./images/icons/calendar.gif" onmousedown="this.src='./images/icons/calendar_ON.gif'" 
                            onmouseout="this.src='./images/icons/calendar.gif';" /></span>							                                      
                </td>
                <td>
                  &nbsp;<form:errors path="startDate" />
                </td>
               </tr>
                                           
               <tr>
                  <td>End Date</td>
                  <td>                  
                 			<span><input maxlength="10" size="10" name="endDate" id="endDate" value="<c:out value="${geoRestrictionForm.endDateInString}"/>" /></span>
							<span><input id="trigger_endDate" type="image" alt="Calendar" src="./images/icons/calendar.gif" onmousedown="this.src='./images/icons/calendar_ON.gif'" onmouseout="this.src='./images/icons/calendar.gif';" /></span>							                                      
                </td>
                <td>
                  &nbsp;<form:errors path="endDate" />
                </td>
               </tr>
               
               <tr>
                  <td>Message</td>
                  <td>                  
                    <form:textarea path="message" rows="5" cols="45" />
                </td>
                 <td>
                  &nbsp;<form:errors path="message" />
                </td>
               </tr>
           </table>    
               
           <br><br>    
               <script language="javascript">
							 Calendar.setup(
										{
											showsTime : false,
											electric : false,
											inputField : "startDate",
											ifFormat : "%m/%d/%Y",
											singleClick: true,
											button : "trigger_startDate" 
										}
									);
                                    
                           Calendar.setup(
										{
											showsTime : false,
											electric : false,
											inputField : "endDate",
											ifFormat : "%m/%d/%Y",
											singleClick: true,
											button : "trigger_endDate" 
										}
									);

							</script>		
               
               <table width="50%" cellpadding="0" cellspacing="0" border="0">    
               <tr>
                  <td><B>Restriction Details</B></td>
                 </tr>    
               <tr>
                   <td>                   
                   <select name="DayOfWeekList" id="DayOfWeekList">                   
                   <option value="0">Select Day</option> 
                   <option value="1">Monday</option> 
                   <option value="2">tuey</option> 
                   <option value="3">wed</option> 
                   <option value="4">thur</option>                    
                   </select> 
                   
                   </td>                               
                   <td><input type="text" id="condition" name="condition" value=""></td>                                      
                   <td><input type="text" id="startTime" name="startTime" value=""></td>
                   <td><input type="text" id="endTime" name="endTime" value=""></td>
                 </tr>                 
                 <tr>
                 <td colspan="3"><input type="button" value="Add Rest Operator" class="submit" onclick="javascript:(isEdit)?editProfile():addProfile();"></td>
                  </tr>
                
               </table>        

                    <table id="profileListTB" border="1" cellpadding="2" cellspacing="0" >
					<thead>
					<tr> 
                   <th>dayOfWeek</th>                               
                   <th>condition</th>                                      
                   <th>startTime</th>
                   <th>endTime</th>
                   <th>&nbsp;</th>                                      
					</tr>
					</thead>	
					<tbody>		
                     <% int intRowIndex = 0;
                  	 StringBuffer strProfileHidBuf = new StringBuffer();
                   %>
                    <c:forEach var="gDay" items="${geoRestrictionForm.geoRestrictionDays}">
                    <%                      
                          intRowIndex++;
                          

                       %>
                            <input type="hidden" name="attributeList[<%=intRowIndex%>].dayOfWeek" id="attributeList[<%=intRowIndex%>].dayOfWeek" value="<c:out value="${gDay.restrictionDaysId.dayOfWeek}"/>" />
                            <input type="hidden" name="attributeList[<%=intRowIndex%>].condition" id="attributeList[<%=intRowIndex%>].condition" value="<c:out value="${gDay.condition}"/>" />
                            <input type="hidden" name="attributeList[<%=intRowIndex%>].startTime" id="attributeList[<%=intRowIndex%>].startTime" value="<c:out value="${gDay.startTime}"/>" />
                            <input type="hidden" name="attributeList[<%=intRowIndex%>].endTime" id="attributeList[<%=intRowIndex%>].endTime" value="<c:out value="${gDay.endTime}"/>" />
                            
                            <tr id='attributeList[<%=intRowIndex%>]'> 
				        	  <td><c:out value="${gDay.restrictionDaysId.dayOfWeek}"/></td>                               
                              <td><c:out value="${gDay.condition}"/></td>
                              <td><input type="text" name="startTime1" value="<c:out value="${gDay.startTime}"/>"></td>
                              <td><c:out value="${gDay.endTime}"/></td>
							  <td><a href="javascript:deleteProfile('attributeList[<%=intRowIndex %>]')">Delete</a>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="javascript:copyProfile('attributeList[<%=intRowIndex %>]')">edit</a></td> 
							</tr> 
                            <script>
                            if(<%=intRowIndex%><10)
                               document.getElementById('restrictionLinkStr').value=document.getElementById('restrictionLinkStr').value+'$'+'0'+<%=intRowIndex %>;    
                            else
                               document.getElementById('restrictionLinkStr').value=document.getElementById('restrictionLinkStr').value+'$'+<%=intRowIndex %>;    
                         </script>
                             
                    </c:forEach>
                    	<script>
                          rollingIndex=<%=intRowIndex%>;
                           document.getElementById('restrictionListSize').value=rollingIndex; 
                        </script>		
				     </tbody> 				
				</table>								
 				<tr>
				    <td  align="center">
					   <input type = "submit" value="&nbsp;Save&nbsp;"  />
					</td>			
				</tr>
               
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>