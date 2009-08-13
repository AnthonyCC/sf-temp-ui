<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Copy Scrib Data</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
    <br/> 
    <div align="center">
      <form name="copyscribform" action= "copyscrib.do" method="post" onSubmit="return validate()">
      
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Copy Scrib Data</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">  
                <tr>
                  <td>Source Week Of</td>
                  <td>
                  <input  type="text" maxlength="50" size="30" name="sourceDate" readonly/>
                  &nbsp;<a href="#" id="trigger_sourceDate" style="font-size: 9px;">
                        <img src="images/calendar.gif"  style="border:0"  alt=">>" />
                        </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "sourceDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,                                            
                        button : "trigger_sourceDate" 
                       }
                      );
                     </script> 
                </td>    
                
               </tr>
                        
                <tr>
                  <td>Source Day</td>
                  <td>
                    <select name="sDay">
                          <option value="All" >--All Days</option>
                       <option value="2" >Monday</option>
                       <option value="3" >Tuesday</option>
                       <option value="4" >Wednesday</option>
                       <option value="5" >Thursday</option>
                       <option value="6" >Friday</option>
                       <option value="7" >Saturday</option>
                       <option value="8" >Sunday</option>
                     </select>
                   </td>
               
               </tr>
               
               <tr>
                  <td>Destination Week Of</td>
                  <td>
                  <input type="text" maxlength="50" size="30" name="destinationDate" readonly/>
                  &nbsp;<a href="#" id="trigger_destinationDate" style="font-size: 9px;">
                        <img src="images/calendar.gif"  style="border:0"  alt=">>" />
                        </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "destinationDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,                                            
                        button : "trigger_destinationDate" 
                       }
                      );
                    </script>
                </td>   
                <td>
                  &nbsp;<form:errors path="destinationDate" />
                </td>
               </tr>
              
             
               
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                
                <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                </td> 
               
              </tr>
              </table>        
              <script language="javascript">                  
            function validate() 
            {
              var f=document.forms["copyscribform"];
              
              if(!(f.sourceDate.value.length>0)) { alert("Please Select Source of Week of"); return false;}
              if(!(f.destinationDate.value.length>0)) { alert("Please Select Destination of Week of"); return false;}
            }
        </script>
            </td>
          </tr>               
        </table>
      
     </form>
     </div>
     
  </tmpl:put>
</tmpl:insert>