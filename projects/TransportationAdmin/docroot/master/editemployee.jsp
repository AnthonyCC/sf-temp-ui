<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Employee</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "employeeForm" method="post">
			<form:hidden path="employeeId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Employee</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Kronos ID</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="employeeId" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="employeeId" />
							 	</td>
							 </tr>
							 <tr valign="top">
							    <td>First Name</td>
							    <td> 
								   <form:input maxlength="50" size="30" path="firstName" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="firstName" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Last Name</td>
							    <td> 
								  	 <form:input maxlength="50" size="30" path="lastName" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="lastName" />
							 	</td>
							 </tr>				                                                            
                             <tr>
							    <td>Role</td>
							    <td> 	
                              <spring:bind path="employeeForm.employeeRoleTypes">
                                   <SELECT name="employeeRoleTypes" multiple="multiple">     
                                      <OPTION value="">Please select roles</OPTION>          
                                      <c:forEach  var="role" items="${roleTypes}"  >
                                       <c:set var="avail" value="true"/>
                                         <c:forEach var="irole" items="${employeeForm.employeeRoleTypes}">         
                                        <c:if test="${irole.code == role.code}">
                                          <OPTION selected="<c:out value="${role.code}"/>" value="<c:out value="${role.code}"/>"><c:out value="${role.name}"/></OPTION>
                                          <c:remove var="avail"/>
                                        </c:if>       
                                        </c:forEach>
                                        <c:if test="${avail}">
                                          <OPTION value="<c:out value="${role.code}"/>"><c:out value="${role.name}"/></OPTION>          
                                        </c:if>  
                                      </c:forEach>
                                    </SELECT>
                                  </spring:bind>                
                               </td>
							 	<td>
							 		&nbsp;<form:errors path="empRole" />
							 	</td>
							 </tr>							 	  	
							<tr><td colspan="3">&nbsp;</td></tr>
							<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								</td>			
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
			
			</form:form>
		 </div>
		 
	</tmpl:put>
</tmpl:insert>
