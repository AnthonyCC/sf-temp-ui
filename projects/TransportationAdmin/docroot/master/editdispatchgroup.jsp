<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Routing : Dispatch Group : Add/Edit Dispatch Group</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "dispatchGroupForm" method="post">
      <form:hidden path="dispatchGroupId"/>      
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Dispatch Group</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">                  
                <tr>
                  <td>Dispatch Group Name</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="name" />
                </td>
                <td>
                  &nbsp;<form:errors path="name" />
                </td>
               </tr>
               
               <tr>
                  <td>Dispatch Group Time</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="groupTime" onblur="this.value=time(this.value);"/>
                </td>
                <td>
                  &nbsp;<form:errors path="groupTime" />
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