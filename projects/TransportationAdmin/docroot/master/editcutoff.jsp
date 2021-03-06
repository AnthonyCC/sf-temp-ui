<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Routing : Hand Off : Add/Edit Hand Off</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "cutOffForm" method="post">
      <form:hidden path="cutOffId"/>      
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Hand Off</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">                  
                <tr>
                  <td>Hand Off Name</td>
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
                  <td>Sequence</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="sequenceNo" />
                </td>
                <td>
                  &nbsp;<form:errors path="sequenceNo" />
                </td>
               </tr>
               
               <tr>
                  <td>Hand Off Time</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="cutOffTime" onblur="this.value=time(this.value);"/>
                </td>
                <td>
                  &nbsp;<form:errors path="cutOffTime" />
                </td>
               </tr>
               
               <tr>
                  <td>Shift</td>
                  <td>                  
                     <form:select path="shift">
						<form:option value="" label="--Please Select Shift"/>
						<form:option value="AM" label="AM"/>
						<form:option value="PM" label="PM"/>
					</form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="shift" />
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