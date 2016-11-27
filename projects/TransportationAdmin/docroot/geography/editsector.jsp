<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'>Operations : Add/Edit Sector Zipcode</tmpl:put>
  <tmpl:put name='content' direct='true'>
	<div class="contentroot">
		<div class="cont_row">
		<br/>
		<div align="center">
      <form:form commandName = "neighbourhoodForm" method="post">
	  
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Sector Zipcode</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">
                <tr>
                  <td>Zip Code</td>
                  <td>                  
                    <form:input maxlength="5" size="30" path="zipcode" />
                </td>
                <td>
                  &nbsp;<form:errors path="zipcode" />
                </td>
               </tr>
               <tr>
                  <td>Sector</td>
                  <td> 
                  <form:select path="sector">
                    <form:option value="null" label="--Please Select Sector"/>
                    <form:options items="${sectors}" itemLabel="name" itemValue="name" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="sector" />
                </td>
               </tr>
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
				   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
                </td>
              </tr>
              </table>
            </td>
          </tr>
        </table>
      </form:form>
     </div>
    </div>
</div>
	<script>
		function back()
	    {
	      	var filters=unescape(getParameter("filter"));
	      	var params=filters.split("&");
	      	var neighbourhoodForm=document.forms["sector"];
	      	for(var i=0;i<params.length;i++)
	      	{
	      		var param=params[i].split("=");         				
	      		add_input(neighbourhoodForm,"hidden",param[0],param[1]);
	      	}     	      	
	      	neighbourhoodForm.submit();
	    }
	</script>
  </tmpl:put>
</tmpl:insert>
<form name="sector" action="sector.do" method="post">  </form>