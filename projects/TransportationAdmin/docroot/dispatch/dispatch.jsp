<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "true"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON_NEW", "true"); 
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
                      
                   function doDelete(tableId, url) {    
                     sendRequest(tableId, url, "Do you want to delete the selected records?");                       
                   }
                    
                    function doConfirm(tableId, url) {
                        sendRequestNew(tableId, url, "Do you want to confirm/deconfirm the selected records?");                      
                    }
                    
                    function refreshRoute() {
                        var hasConfirmed = confirm ("This action can overwrite existing Route/Truck Assignment. Are you sure you want to perform this operation?");
                        if(hasConfirmed) {
                           location.href = "<c:out value="${pageContext.request.contextPath}"/>/refreshRoute.do?dispDate=<%= dateRangeVal %>";
                        }
                    }
                    

                    function sendRequest(tableId, url, message, action) {
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");
                        var confirmedList = table.getElementsByTagName("input");
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked && !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                            
                            if (paramValues != null) {
                              paramValues = paramValues+","+checkboxList[i].name//+"$"+dateField;
                            } else {
                              paramValues = checkboxList[i].name//+"$"+dateField;
                            }
                          }
                        }
                        if (paramValues != null) {
                          var hasConfirmed = confirm (message);
                        if (hasConfirmed) {
                            location.href = url+"?id="+ paramValues+"&dispDate="+dateField+"&"+getFilterValue(document.getElementById("ec"),false);
                        } 
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
                    
                   function sendRequestNew(tableId, url, message, action) {
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");
                        var confirmedList = table.getElementsByTagName("input");
                        var dateField = document.getElementById("dispDate").value;    
                        var checked="";
                        var unchecked="";
                        for (i = 0; i < checkboxList.length; i++) 
                        {
                        
                          if (checkboxList[i].type=="checkbox" && !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")!=-1) 
                          {
                          	if(checkboxList[i].checked)
                          	{
                          		checked+=checkboxList[i].name+",";
                          	}                          	
                          }
                        }
                        checked=checked.substring(0,checked.length-1);                        
                        if(checked.length==0)
                        {
                         alert('Please Select a Row!');
                        }
                        else
                        {
                        	var newForm=document.forms["newSubmit"];
                        	newForm.action=url;
                        	newForm.id.value=checked;
                        	newForm.dispDate.value=dateField;
                        	setFilter(document.getElementById("ec"),newForm);                    	
                        	newForm.submit();
                        }
                    }                    
                    				
                    function directions(tableId, url, columnIndex) {
                      var table = document.getElementById(tableId);
                       var checkboxList = table.getElementsByTagName("input");
                        
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                            var routeId = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[columnIndex].innerHTML
                            if(routeId != null && routeId.length > 0) {
	                            if (paramValues != null) {
	                              paramValues = paramValues+","+routeId;
	                            } else {
	                              paramValues = routeId;
	                            }
	                         }
                          }
                        }
                        if (paramValues != null) {
                          pop(url+"?routeId="+ paramValues+"&rdate="+dateField,600,800);
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
              
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="dispDate" />
                </td>
                <td> 
                  <select id="zone" name="zone">
                      <option value="">Select Zone</option> 
                      <c:forEach var="zone" items="${zones}">                             
                          <c:choose>
                            <c:when test="${param.zone == zone.zoneCode}" > 
                              <option selected value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.displayName}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.displayName}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
                
                </td>
                <td align="center"><span style="font-size: 9px;">OR</span></td>
                <td> 
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
                     <input type = "button" value="View" onclick="javascript:doCompositeLink('dispDate','zone','region','dispatch.do')" />
                  </td>  
                  <td>
                     <input type = "button" value="Refresh Route" onclick="javascript:refreshRoute()" />
                  </td>
                  <td>
                     <input type = "button" value="Unassigned Routes" onclick="javascript:doUnassignedRoutes('dispDate')" />
                  </td> 
                  <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request)){%> 
                  <td>
                     <input type = "button" value="Activity Log" onclick="javascript:doActivityLog('dispDate')" />
                  </td>
                  <td>
                     <input type = "button" value="Reason Code" onclick="javascript:doReasonCode()" />
                  </td>
                  <%} %>
                  <td>
                    <a href="javascript:directions('ec_table','drivingdirection.do', 10)">
                  		<img src="./images/driving-directions.gif" width="114" height="25" border="0" alt="Driving Directions" title="Driving Directions" />
                  	</a>
                  <td>
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
        
        function doUnassignedRoutes(compId1) {
        	 var param1 = document.getElementById(compId1).value;
        	javascript:pop('unassignedroute.do?routeDate='+param1, 400,600);
        }        
		function doActivityLog(compId1) {
        	 var param1 = document.getElementById(compId1).value;
        	 showForm(param1);
        }
		function doReasonCode() 
		{
			document.getElementById("result").innerHTML="";
	       	var panel=init("panel-2","Override Reason Code");
	       	panel.render(document.body);
	        panel.show();
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
              <ec:column viewsAllowed="fd" title="Row" width="5px"  filterable="false" sortable="false" cell="selectcol"  property="dispatchId" /> 
              <ec:column viewsAllowed="fd" title="Nextel" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="phoneAssigned" />
              <ec:column viewsAllowed="fd" title="Keys" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="keysReady" />   
              <ec:column viewsAllowed="fd" title="Dispatched" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="dispatched" />   
              <ec:column viewsAllowed="fd" title="ChIn" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="checkedIn" />  
              <ec:column alias="trnStatus" property="dispatchStatus"  title="Status"/> 
              <ec:column alias="trnZoneRegion" property="regionZone" title="Region - Zone" />             
              <ec:column property="supervisorEx"   title="Sup" cell="tooltip"  />
              <ec:column  alias="trnTimeslotslotName" cell="date" format="hh:mm aaa" property="startTimeEx" title="Start Time"/> 
              <ec:column  alias="trnTimeEndslotslotName" cell="date" format="hh:mm aaa" property="firstDeliveryTimeEx" title="First Dlv."/>
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>
              <ec:column alias="trnTruckLocation" property="location" width="10"  title="Location"/>
              <ec:column property="drivers"  cell="dispatchResCell" title="Driver"  filterable="true" alias="drivers"/>
              <ec:column property="helpers"  cell="dispatchResCell" title="Helper"  filterable="true" alias="helpers"/>
              <ec:column property="runners"  cell="dispatchResCell" title="Runner"  filterable="true" alias="runners"/>
               <ec:column alias="trnTruckGpsNumber" property="extras" width="10"  title="Extras"/>
              <ec:column  alias="dispatchTime"  property="dispatchTimeEx" title="Dispatch Time"  cell="date" format="hh:mm aaa"/>
              <ec:column property="override"  title="Override Dispatch"/>
              
            </ec:row>
          </ec:table>
    </div>
    <script>
      addMultiRowHandlersColumnFilter('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 4,'dispDate');
    </script>
    <%@ include file='i_activityLog.jspf'%> 
     <%@ include file='i_ReasonCode.jspf'%> 
    <form name="newSubmit" action="dispatch.do" method="post">
    <input type=hidden name=id><input type=hidden name=dispDate>
    </form>
    <form name="edit"  method="get">   
    </form>
  </tmpl:put>
  
  
</tmpl:insert>
