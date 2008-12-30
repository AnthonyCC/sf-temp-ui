<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "true"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

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
            <td align="center">

              <table border = "0" width="50%">
                <tr>
                  <td width="25%"> 
                                
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
                    
                    function refreshRoute() {
                        var hasConfirmed = confirm ("This action can overwrite existing Route/Truck Assignment. Are you sure you want to perform this operation?");
                        if(hasConfirmed) {
                           location.href = "<c:out value="${pageContext.request.contextPath}"/>/refreshRoute.do?dispDate=<%= dateRangeVal %>";
                        }
                    }
                    
                    function newDispatch() {
                         location.href= "<c:out value="${pageContext.request.contextPath}"/>/editdispatch.do?dispDate=<%= dateRangeVal %>";
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
                  &nbsp;<form:errors path="dispDate" />
                </td>
                <td width="10%"> 
                  <select id="zone" name="zone">
                      <option value="">Select Zone</option> 
                      <c:forEach var="zone" items="${zones}">                             
                          <c:choose>
                            <c:when test="${param.zone == zone.zoneCode}" > 
                              <option selected value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
                
                </td>
                <td align="center">OR</td>
                <td width="25%"> 
                    <select id="region" width="50" name="region">
                      <option value="">Select Region</option> 
                      <c:forEach var="region" items="${regions}">                             
                          <c:choose>
                            <c:when test="${param.region == region.code}" > 
                              <option selected value="<c:out value="${region.code}"/>"><c:out value="${region.name}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${region.code}"/>"><c:out value="${region.name}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
            
                  </td>
                   <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('dispDate','zone','region','dispatch.do')" />
                  </td>  
                  <td>
                     <input type = "button" value="&nbsp;New Dispatch&nbsp;" onclick="javascript:newDispatch()" />
                  </td> 
                  <td>
                     <input type = "button" value="&nbsp;Refresh Route&nbsp;" onclick="javascript:refreshRoute()" />
                  </td> 
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
       <script>
         function doCompositeLink(compId1,compId2, compId3, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          var param3 = document.getElementById(compId3).value;
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
        } 

      </script>      
      </div>
    
    <div align="center">
      <ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatch.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="dispatchobsoletemarker">
              <ec:column title=" "width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="dispatchId" />                           
              <ec:column alias="trnZonezoneNumber" width="12" property="zoneCode" title="Zone" />
              <ec:column alias="trnConfirm" width="5" cell="confirmcol" property="confirmed" title="C"  />
              <ec:column alias="trnZoneRegion" property="regionName" title="Region" />
              <ec:column alias="trnSupervisorname" property="supervisorName" title="Supervisor" />
              <ec:column  alias="trnTimeslotslotName"  property="startTime" title="Start Time"/>
              <ec:column  alias="trnTimeEndslotslotName" property="firstDeliveryTime" title="First Dlv."/>
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>
              <ec:column property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Driver" alias="001"/>
              <ec:column property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Helper" alias="002"/>
              <ec:column property="runners"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Runner" alias="003"/>
              <ec:column alias="trnStatus" property="status"  title="statusName"/>
            </ec:row>
          </ec:table>
    </div>
    <script>
      addMultiRowHandlers('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 0,'dispDate');
    </script>   
  </tmpl:put>
</tmpl:insert>
