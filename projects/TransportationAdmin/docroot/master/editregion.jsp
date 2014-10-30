<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Add/Edit Region";
%>
    <tmpl:put name='title' direct='true'> Geography : Zone Region : <%=pageTitle%></tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">                
      <form:form commandName = "regionForm" method="post">            
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle"><%=pageTitle%></td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
                 <c:if test="${!empty regionForm.code}">
              <c:set var="hasPK" value="true"/>
          </c:if>

          <tr>
            <td class="screencontent">
             <table class="forms1">         
                  <tr>
                    <td>Code</td>
                  <td>                                                                  
                    <form:input maxlength="16" size="30" path="code" readonly="${hasPK}" />                                    
                </td>
                <td>
                  &nbsp;<form:errors path="code" />
                </td>
               </tr>                            
                            
                <tr>
                  <td>Region Name</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="name" />
                </td>
                <td>
                  &nbsp;<form:errors path="name" />
                </td>
               </tr>
                            
                <tr>
                  <td>Description</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="description" />
                </td>
                <td>
                  &nbsp;<form:errors path="description" />
                </td>
               </tr>               
               <tr>
                  <td>Obsolete</td>
                  <td>
                  <form:checkbox path="obsolete" value="X"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="obsolete" />
                </td>
               </tr>
               <tr>
                  <td>Muni Meter</td>
                  <td>
                  <form:checkbox path="muniMeterEnabled" value="X"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="muniMeterEnabled" />
                </td>
               </tr>
               <tr>
                  <td>Dispatch validation</td>
                  <td>
                  <form:checkbox path="needsDispValidation" value="X"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="needsDispValidation" />
                </td>
               </tr>
               <tr>
					<td>Origin Facility</td>
					<td> 
						<form:select path="originFacility" >
							<form:option value="" label="--Please Select Origin Facility"/>
							<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
						</form:select> 
					</td>
					<td><form:errors path="originFacility" />&nbsp;</td>
				</tr>
                                           
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:doBack('region');" /> &nbsp;
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                </td>     
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     <form name="region" action="region.do" method="post">  </form>
  </tmpl:put>
</tmpl:insert>