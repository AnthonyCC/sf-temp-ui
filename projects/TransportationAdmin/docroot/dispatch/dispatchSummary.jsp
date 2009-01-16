<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "false"); 
  pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>

  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Dispatch Summary</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td>

              <table border = "0">
              <input type="hidden" id="dispDate" name="dispDate" value="" />
                <tr>
                <td> 
                    <span style="font-size: 18px;font-weight: bold;">Today: <%= TransStringUtil.getFullMonthDate(new Date()) %></span>
                </td>

                     <script language="javascript">                 
                    function refreshRoute() {
                        var hasConfirmed = confirm ("This action can overwrite existing Route/Truck Assignment. Are you sure you want to perform this operation?");
                        if(hasConfirmed) {
                           location.href = "<c:out value="${pageContext.request.contextPath}"/>/refreshRouteSummary.do?dispDate=<%= dateRangeVal %>&summary=true";
                        }
                    }
                  </script>
 
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
      <ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatchSummary.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd">
           
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
              
            <ec:row interceptor="dispatchobsoletemarker">
              <ec:column alias="trnConfirm" width="5" cell="confirmcol" property="confirmedValue" title="C"  />            
              <ec:column  cell="tooltip" alias="zoneCode" property="zoneNameEx" title="Zone"/>
              <ec:column alias="trnZoneRegion" property="regionName" title="Region" />
              <ec:column  alias="trnTimeslotslotName"  property="startTime" title="Start Time"/>  
              <ec:column  alias="trnTimeEndslotslotName" property="firstDeliveryTime" title="First Dlv."/>
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>
              <ec:column alias="trnNoOfStops" property="noOfStops" width="10"  title="Stops"/>
              <ec:column alias="trnStatus" property="statusName"  title="Status"/>              
              <ec:column property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchSummaryResourceCell" title="Driver"  filterable="true" alias="drivers"/>
              <ec:column property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchSummaryResourceCell" title="Helper"  filterable="true" alias="helpers"/>
              <ec:column property="runners"  cell="com.freshdirect.transadmin.web.ui.FDDispatchSummaryResourceCell" title="Runner"  filterable="true" alias="runners"/>
              <ec:column alias="trnComments" filterable="false" property="comments"  title="Comments"/>              
            </ec:row>
          </ec:table>
    </div>
    
    <script>
      //addMultiRowHandlers('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 0,'dispDate');
    </script>   
  </tmpl:put>
</tmpl:insert>
