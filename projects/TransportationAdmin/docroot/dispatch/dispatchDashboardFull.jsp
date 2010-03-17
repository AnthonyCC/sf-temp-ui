<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "false"); 
  pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Dispatch Sheet</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>	
	
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
                <tr>
                <td> 
                    <span style="font-size: 18px;font-weight: bold;">Dispatch</span>
                </td>                
                  <td> 
                    <span><input maxlength="10" size="10" name="dispDate" id="dispDate" value='<c:out value="${dispDate}"/>' /></span>
                     <span><a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a></span>
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
                      
                   function refreshRoute() 
                   {
                   		var param1 = document.getElementById("dispDate").value;
                        location.href = "<c:out value="${pageContext.request.contextPath}"/>/dispatchDashboard.do?dispDate="+param1;
                        
                    }
                    
                    function OpenPage(page) 
                   {
                   		var height=YAHOO.util.Dom.getDocumentHeight()-30;
                   		var width=YAHOO.util.Dom. getDocumentWidth();
                   		var props=document.forms["properties"];                   		
                  		var param1 = "";
                  		param1+="refreshtime="+props.refreshtime.value;
                  		param1+="&pagerefreshtime="+props.pagerefreshtime.value;
                  		param1+="&pagesize="+props.pagesize.value;
                   		window.open('<c:out value="${pageContext.request.contextPath}"/>/dispatchDashboardScreen.do?'+param1,'page'+page,'width='+width+',height='+height+',left=0,top=0,toolbar=no,location=no,resizable=yes,scrollbars=yes'); 
                    }
                    function OpenViewPage(mode) 
                    {
                        
                    		var height=YAHOO.util.Dom.getDocumentHeight()-30;
                    		var width=YAHOO.util.Dom. getDocumentWidth();
                    		var props=document.forms["properties"];                     		             		
	                   		var param1 = "";
	                   		param1+="refreshtime="+eval("props.refreshtime"+mode+".value");
	                   		param1+="&pagerefreshtime="+eval("props.pagerefreshtime"+mode+".value");
	                   		param1+="&pagesize="+eval("props.pagesize"+mode+".value");
	                   		param1+="&mode="+mode;
                    		window.open('<c:out value="${pageContext.request.contextPath}"/>/dispatchDashboardView.do?'+param1,'view'+mode,'width='+width+',height='+height+',left=0,top=0,toolbar=no,location=no,resizable=yes,scrollbars=yes'); 
                     }
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="dispDate" />
                </td>
 
                   <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:refreshRoute()" />
                  </td>  
                 <td>
                     <input type = "button" value="Configure DashBoard" onclick="javascript:showForm()" />
                  </td>
                 <td>
                     <input type = "button" value="&nbsp;Open DashBoard&nbsp;" onclick="javascript:OpenPage(1)" />
                  </td>
                 
                 <td>
                     <input type = "button" value="Ready View" onclick="javascript:OpenViewPage(1)" />
                  </td>
                  <td>
                     <input type = "button" value="Waiting View" onclick="javascript:OpenViewPage(2)" />
                  </td>
                  <td>
                     <input type = "button" value="N/R View" onclick="javascript:OpenViewPage(3)" />
                  </td>
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
         
      </div>
    
    <div align="center">
      <ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatchDashboard.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
                                         
             <ec:column alias="trnZoneRegion" property="regionZone" title="Region-Zone" />              
              
              <ec:column  alias="trnTimeslotslotName"  property="startTime" title="Start Time"/>
              
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>              
              <ec:column alias="trnTruckLocation" property="location" width="10"  title="Loc"/>
              <ec:column alias="trnTruckStops" property="noOfStops" width="10"  title="Stops"/>
               <ec:column alias="trnStatus" property="dispatchStatus"  title="Status"/>
              <ec:column property="drivers"  cell="dispatchResCell" title="Driver"  filterable="true" alias="drivers"/>
              <ec:column property="helpers"  cell="dispatchResCell" title="Helper"  filterable="true" alias="helpers"/>
              <ec:column property="runners"  cell="dispatchResCell" title="Runner"  filterable="true" alias="runners"/>
             <ec:column  alias="dispatchTime"  property="dispatchTimeEx" title="Dispatch Time"  cell="date" format="hh:mm aaa"/>
            </ec:row>
          </ec:table>
    </div>

    <%@ include file='dispatchSummaryEntry.jspf'%> 
  </tmpl:put>
</tmpl:insert>
