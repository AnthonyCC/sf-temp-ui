<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<%  
	pageContext.setAttribute("HAS_COPYBUTTON", "true");  
	String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";
%>
<% 
	String pageTitle = "Planning";
%>

<tmpl:insert template='/common/sitelayout.jsp'>

	<tmpl:put name='title' direct='true'> Operations : <%=pageTitle%></tmpl:put>

  <tmpl:put name='content' direct='true'> 

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 

  <div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">						
						<div class="scrTitle" style="float:left;padding-top:3px"><%=pageTitle%></div>
						<div style="float:left;text-align:center;font-weight:bold">Week<br>
							<input maxlength="40" name="weekdate" id="weekdate" value='<c:out value="${weekDate}"/>' style="width:100px"/>
						 	<a href="#" id="trigger_planWDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</div>
						<div style="float:left;text-align:center;font-weight:bold">Date<br>&nbsp;
							<input maxlength="40" name="daterange" id="daterange" value='<c:out value="${dateRange}"/>' style="width:100px"/>
						 	<a href="#" id="trigger_planDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</div>&nbsp;
						<div style="float:left;text-align:center;font-weight:bold">Zone<br>&nbsp;
							<input maxlength="40" size="20" name="zone" id="zone" value="<%= zoneVal %>" style="width:100px" />
						</div>	&nbsp;					
						<div style="float:left;"><br>
	                   	  <span>&nbsp;<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','weekdate','zone','plan.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
						  <span><input id="view_button" type="image" alt="Dispatch" src="./images/icons/dispatch.gif" onclick="javascript:doAutoDispatch('weekdate','daterange','zone','autoDispatch.do');" onmousedown="this.src='./images/icons/dispatch_ON.gif'" /> 
							<a href="#"  onclick="javascript:doAutoDispatch('weekdate','daterange','zone','autoDispatch.do')" class="dispatch_link">Dispatch</a></span>
	                      <input type = "button" value=" U/A " onclick="javascript:doUnavailable('plan.do','weekdate','daterange','y')" />
	                      <input type = "button" value="Kronos Files" onclick="javascript:doKronos('plan.do','weekdate','daterange','y','1')" />                  
	                   </div>  
				</div>
			</div>
		</div> 
		

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

      <form id="planListForm" action="" method="post">  
        <ec:table items="planlist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/plan.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
            width="98%"  view="fd" form="planListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationplan.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Plan" />
              <ec:exportXls fileName="transportationplan.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationplan.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="planId" />             
              <ec:column cell="date" property="weekDate" sortable="true" title="Week"/>
              <ec:column cell="date" property="planDate" sortable="true" title="Date"/>
              <ec:column property="planDay" sortable="true" title="Day"/>
              <ec:column  cell="tooltip" alias="zoneCode" property="zoneNameEx" title="Zone"/>
              <ec:column alias="region" property="regionCode" title="Region"/>
              <ec:column property="supervisorEx"   title="Supervisor" cell="tooltip"  />
              <ec:column cell="date" format="hh:mm aaa" alias="startTime" property="startTimeEx" title="Start Time"/>
              <ec:column cell="date" format="hh:mm aaa" alias="firstDeliveryTime" property="firstDeliveryTimeEx" title="First Dlv Time"/>
              <ec:column cell="date" format="hh:mm aaa" alias="lastDeliveryTime" property="lastDeliveryTimeEx" title="Last Dlv Time"/>
              <ec:column  filterable="true" property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Driver" alias="drivers"/>
              <ec:column  filterable="true" property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Helper" alias="helpers"/>
              <ec:column  filterable="true" property="runners"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Runner" alias="runners"/>
              <ec:column property="sequence"  title="Rank"/>
              <ec:column property="open"  title="Open"/>
              <ec:column cell="date" format="hh:mm aaa" alias="cutOffTime" property="cutOffTimeEx" title="CutOff"/>
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>

      function doAutoDispatch(compId1,compId2,compId3, url) {
    	  	var param2 = document.getElementById(compId2).value;
    	  	if(param2==''){
		  		alert('Please enter the DATE to AUTO-DISPATCH for.');
			}else{
				var hasConfirmed = confirm ("You are about to perform AUTO-DISPATCH.  If DISPATCHES already exist for the "+param2+", ALL CHANGES WILL BE LOST.  Do you want to continue?")
				if (hasConfirmed) {				
					doCompositeLink(compId1,compId2,compId3, url);
				}
			} 
      }
      function doCompositeLink(compId1,compId2,compId3, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          var param3 = document.getElementById(compId3).value;
          
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
      } 

      addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
      function getFilterTestValue() {
             var filters = getFilterValue(document.getElementById("planListForm"),false);
             var param1 = document.getElementById("weekdate").value;
     		 var param2 = document.getElementById("daterange").value;
     		 var param3 = document.getElementById("zone").value;
             filters+="&weekdate="+param1;
             filters+="&daterange="+param2;
             filters+="&zone="+param3;                                                               
             return escape(filters);
       }
 
      function doDelete(tableId, url) 
      {    
		    var paramValues = getParamList(tableId, url);
		    if (paramValues != null) {
		    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
		    	if (hasConfirmed) 
				{
		    		var param1 = document.getElementById("weekdate").value;
		     		var param2 = document.getElementById("daterange").value;
		     		var param3 = document.getElementById("zone").value;
		            var filters="&weekdate="+param1+"&daterange="+param2+"&zone="+param3+"&"+getFilterValue(document.getElementById("planListForm"),false)
				  	location.href = url+"?id="+ paramValues+filters;
				} 
		    } else {
		    	alert('Please Select a Row!');
		    }
		}
		
	  function doUnavailable(url,id1,id2,param3)
	  {
	     var param1 = document.getElementById(id1).value;
	     var param2 = document.getElementById(id2).value;
         var id3 = "unavailable";
          
         javascript:pop(url+"?"+id1+"="+ param1+"&"+id2+"="+param2+"&"+id3+"="+param3, 400,600);
	  }

	  function doKronos(url,id1,id2,param3,param4)
	  {
	  	var param1 = document.getElementById(id1).value;
	  	var param2 = document.getElementById(id2).value;
        var id3 = "kronos"; 
        var id4 = "file";          
        location.href =url+"?"+id1+"="+ param1+"&"+id2+"="+param2+"&"+id3+"="+param3+"&"+id4+"="+param3  ;
	  }

	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "daterange",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_planDate" 
                }
               );
	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "weekdate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_planWDate" 
                }
               );

    </script>   
  </tmpl:put>
</tmpl:insert>
