<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "true"); %>
<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Dispatch Sheet</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Dispatch Sheet</td>
          </tr>
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Select Date</td>
                  <td> 
                                
                    <input maxlength="10" size="10" name="dispDate"
                      id="dispDate" value="<c:out value="${dispDate}"/>" />
                    
                    &nbsp;<a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
                        <img src="images/calendar.gif"  style="border:0"  alt=">>" />
                        </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "dispDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_dispatchDate" 
                       }
                      );
                      
                      function doDelete(tableId, url) {                       
                        sendRequest(tableId, url, "Do you want to delete the selected records?");                       
                    }
                    
                    function doConfirm(tableId, url) {
                        sendRequest(tableId, url, "Do you want to confirm/deconfirm the selected records?");                      
                    }
                    
                    function sendRequest(tableId, url, message) {
                      
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
                            
                            if (paramValues != null) {
                              paramValues = paramValues+","+checkboxList[i].name+"$"+dateField;
                            } else {
                              paramValues = checkboxList[i].name+"$"+dateField;
                            }
                          }
                        }
                        if (paramValues != null) {
                          var hasConfirmed = confirm (message);
                        if (hasConfirmed) {
                            location.href = url+"?id="+ paramValues;
                        } 
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
              
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="planDate" />
                </td>            
                   <td colspan="3" align="center">
                     <input type = "button" value="&nbsp;Go&nbsp;" onclick="javascript:doLink('dispDate','dispatch.do')" />
                </td>     
                      
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
      
     </div>
    <div align="center">
      <ec:table items="dispatchlist"   action="${pageContext.request.contextPath}/dispatch.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="dispatchobsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="id.planId" />                           
              <ec:column alias="trnZonezoneNumber" width="10" property="trnZone.zoneNumber" title="Zone"/>
              <ec:column alias="trnTimeslotslotName" width="10"  property="trnTimeslot.slotName" title="Slot"/>
              <ec:column alias="trnRouterouteNumber" property="trnRoute.routeNumber" title="Route"/>
              <ec:column alias="trnSupervisorname" property="trnSupervisor.name" title="Supervisor"/>
              <ec:column alias="trnTrucktruckNumber" property="trnTruck.truckNumber" title="Truck"/>
              <ec:column alias="trnDrivername" property="trnDriver.name" title="Driver"/>
              <ec:column alias="trnPrimaryHelpername" property="trnPrimaryHelper.name" title="Helper1"/>
              <ec:column alias="trnSecondaryHelpername" property="trnSecondaryHelper.name" title="Helper2"/>
              <ec:column property="nextelId" title="Nextel"/>
              <ec:column property="statusDescription" title="Status"/>
              <ec:column property="comments" title="Comments"/>
              
            </ec:row>
          </ec:table>
    </div>
    <script>
      addMultiRowHandlers('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 0,'dispDate');
    </script>   
  </tmpl:put>
</tmpl:insert>