<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.common.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Modify Timeslot Restriction</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
		<form:form commandName = "timeslotRestrictionForm" method="post">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
          </br>
          <tr>
            <td class="screentitle">
              Add/Modify Timeslot Restriction             
            </td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
    			<tr>
                   	<td>Day Of Week</td>
	                <td>               
	                	<form:select path="dayOfWeek">
							<form:option value="" label="--Please Select Day"/>
							<form:options items="${DayOfWeeks}" itemLabel="desc" itemValue="desc" />
											</form:select>   
	                </td>
	                <td>
	                  &nbsp;<form:errors path="dayOfWeek" />
	                </td>
               </tr>
               
               <tr>
                  <td>Zone Code</td>
                  <td>                  
                 <form:select path="zoneCode">
							<form:option value="" label="--Please Select Zone"/>
							<form:options items="${zones}" itemLabel="zoneCode" itemValue="zoneCode" />
											</form:select>   
                </td>
                <td>
                  &nbsp;<form:errors path="zoneCode" />
                </td>
               </tr>
               
              <tr>
                  <td>Condition</td>
                  <td>                  
                  <form:select path="condition">
							<form:options items="${conditions}" itemLabel="description" itemValue="name" />
											</form:select>   
                </td>
                <td>
                  &nbsp;<form:errors path="condition" />
                </td>
              </tr>     
                    
                    
                    
                                                                    
               <tr>
					<td><a id="timeStart_toggler">Start&nbsp;Time</a></td>
						<td>         
							<form:input maxlength="50" size="24" path="startTime" onblur="this.value=time(this.value);" /> 
						</td>
						<td><form:errors path="startTime" />&nbsp;</td>                 
					</tr>                  
               <tr>
					<td><a id="time_toggler">End&nbsp;Time</a></td>
						<td>         
							<form:input maxlength="50" size="24" path="endTime" onblur="this.value=time(this.value);" /> 
						</td>
						<td><form:errors path="endTime" />&nbsp;</td>                 
					</tr>  
					<tr>
					 <td>Active</td>
					 <td>
		  					<form:checkbox id="active" path="active"value="X" />
		  			</td>
		    		</tr>
					
						<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
								</td>
							</tr>               
               <tr>
			
           </table>  
           
           </div>
           
           <br><br>    
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     
     <script language="javascript">
     	function back()
	    {
		  	var planForm=document.forms["timeslotrestriction"];
		   	planForm.submit();
		}
     
     </script>
  </tmpl:put>
</tmpl:insert>
<form name="timeslotrestriction" action="timeslotrestriction.do" method="post">  </form>
