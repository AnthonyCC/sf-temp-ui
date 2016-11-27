<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='java.io.*'%>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Admin Home</tmpl:put>

  <tmpl:put name='content' direct='true'>
  <%    
    Exception e = (Exception) request.getAttribute("exception");    
    if(e!=null){
  %>    
      <br/><br/>
          <div align="center">
            <table cellspacing="0" cellpadding="0" border="0" >
            <tr> <td class="errortxt">
              
                  Sorry, System is unable to process that request. Please click your browser's back button and try again.<br/>
                  If that does not work, contact system administrator. We apologize for any inconvenience.<br/><br/>
                  Reference: <%= e.getMessage() %>
            </td></tr>
            </table>
          </div>
      <p>
      <!--
      <% 
        StringWriter writer=new StringWriter(); 
        PrintWriter pw=new PrintWriter(writer);
        e.printStackTrace(pw);        
      %>
      <%=writer.toString()%>
      --> 
      </p>
    <% } %> 
  </tmpl:put>
</tmpl:insert>