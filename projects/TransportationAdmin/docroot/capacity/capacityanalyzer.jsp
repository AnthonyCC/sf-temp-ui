<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>
<%@ page import= 'com.freshdirect.routing.constants.EnumRoutingServiceType' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>
<%@ page import= 'com.freshdirect.routing.model.*' %>

<% 
	String pageTitle = "Capacity Analyzer";
	String serviceType = request.getParameter("serviceType");
%>

<tmpl:insert template='/common/sitelayout.jsp'>

	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>
	<tmpl:put name='gmap-lib'>
		<%@ include file='/common/i_gmap.jspf'%>
	</tmpl:put>

  <tmpl:put name='title' direct='true'> Operations : <%=pageTitle%></tmpl:put>
  	
  <tmpl:put name='content' direct='true'> 

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if>

  <div class="contentroot">
		<style>
			.capacityAnalyzerTable th, .capacityAnalyzerTable td {
				cursor: pointer;
				cursor: hand;
			}
			.capacityAnalyzerTable th.first {
				border-right: 1px solid #333333;
			}
			.capacityAnalyzerTable th {
				border-bottom: 1px solid #333333;
			}
			.capacityAnalyzerTable td.first {
				border: 1px solid #000;
				border-right: 2px solid #333333;
				border-top: none;
				font-weight: bold;
			}
			.capacityAnalyzerTable td {
				border: 1px solid #CCCCCC;
				text-align: center;
				height:25px;

			}
			.capacityAnalyzerTable td.red {
				background-color: red;
			}
			.capacityAnalyzerTable td.green {
				background-color: green;
			}
		</style>
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle"><%=pageTitle%></span>
				</div>
			</div><div class="cont_row">&nbsp;</div>
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">
						
						<span style="font-weight:bold">
							Selected Date:<input maxlength="40" name="selectedDate" id="selectedDate" value='<c:out value="${selectedDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_selectedDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</span>
						<span style="font-weight:bold">
							&nbsp;
							<select id="cutOff" name="cutOff">
								<option value="">--All Cut Off</option> 
					              <c:forEach var="cutoff" items="${cutoffs}">
									  <c:choose>
											<c:when test="${cutOff == cutoff.cutOffId}" > 
											  <option selected value="<c:out value="${cutoff.cutOffId}"/>"><c:out value="${cutoff.name}"/></option>
											</c:when>
											<c:otherwise> 
											  <option value="<c:out value="${cutoff.cutOffId}"/>"><c:out value="${cutoff.name}"/></option>
											</c:otherwise> 
										</c:choose>
								</c:forEach>   
							</select>
						</span>
						 <span>
							  <select name='group' id='group'>
							   <option value="">--Please Select Group</option> 
								<c:forEach var="deliveryGroup" items="${deliveryGroups}">
									   <c:choose>
											<c:when test="${group == deliveryGroup.groupId}" > 
											  <option selected value="<c:out value="${deliveryGroup.groupId}"/>"><c:out value="${deliveryGroup.groupName}"/></option>
											</c:when>
											<c:otherwise> 
											  <option value="<c:out value="${deliveryGroup.groupId}"/>"><c:out value="${deliveryGroup.groupName}"/></option>
											</c:otherwise> 
										</c:choose>
								</c:forEach>   
							  </select>
						</span>
						<span>
							  <select name='serviceType' id='serviceType'>
									 <option value="">--Please Select Service Type</option>
									<option <c:choose> <c:when test="${serviceType == 'HOME'}" >selected </c:when> </c:choose> 
										value="<%= EnumRoutingServiceType.HOME.getName() %>"><%= EnumRoutingServiceType.HOME.getName() %></option>  
				                    <option <c:choose> <c:when test="${serviceType == 'CORPORATE'}" >selected </c:when> </c:choose> 
										value="<%= EnumRoutingServiceType.CORPORATE.getName() %>"><%= EnumRoutingServiceType.CORPORATE.getName() %></option>
							  </select>
						</span>
						<span style="font-size: 11px;">&nbsp;Auto Refresh:
							<input type="checkbox" name="autorefresh" id="autorefresh" <%= ("true".equalsIgnoreCase(request.getParameter("autorefresh")) ? "checked=\"true\"" : "false") %>  />
						</span>
						&nbsp;&nbsp;&nbsp;
						<span>
							<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('selectedDate','cutOff','group','serviceType','autorefresh','capacityanalyzer.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
							
							&nbsp;&nbsp;<input id="mapview_button" type="button" value="Map View" alt="Google Maps Viewer" onclick="javascript:doBoundary()"/>
							
							&nbsp;&nbsp;<input id="selectall_button" type="button"  value="Select All" alt="select" onclick="javascript:doSelect()" />

							&nbsp;&nbsp;<input id="clearall_button" type="button"  value="Clear All" alt="Clear" onclick="javascript:doClear()" /><br/>
						</span>
				</div>

			</div>
		</div>

</div><br/>
<div class="cont_row">&nbsp;
</div>
	
	<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				<br/><br/>
				 <%
					List<CapacityAnalyzerCommand> buildings = (List<CapacityAnalyzerCommand>)request.getAttribute("allBuildings");
					Set<TimeRange> allWindows = (Set<TimeRange>)request.getAttribute("allWindows");
					if(buildings!=null && buildings.size() > 0) {
	    		%>
	    		<form id="summaryForm" method="post" action="">		
	    		
	    		<table id="tbl_capacityAnalyzer" class="capacityAnalyzerTable" cellspacing="0" cellpadding="0" border="0" width="98%">
				    	<tr>
							<th class="first" width="40"></th>
				    		<th class="first" width="40">Zone</th>
							<th class="first" width="250">Address</th>
							<th class="first" width="65">SoldOut Windows</th>
				    		<%		
				    			for(TimeRange range : allWindows) {  %>
									<th><%= range.getTimeRangeString().replace("\n","<br/>") %></th>
							<% 	}  %>
						</tr>
						<%
							int i=0;
							for(CapacityAnalyzerCommand _command : buildings) {
						 %>
						 	<tr>
								<td class=""><input type="checkbox" <%= (_command.getZoneCode()!=null ?  " " : " disabled=\"disabled\"") %>  name="<%=_command.getZoneCode()%>"/></td>
								<td class=""><%= _command.getZoneCode()%></td>
								<td class="" name="address"><%= _command.getAddress()%></td>
								<td class=""><%= _command.getSoldOutWindow()%></td>
						
							 <%	
								for(Map.Entry<TimeRange, String> slotsMapping : _command.getTimeslots().entrySet()) {
	 							    String subClass = "";
									if("N".equals(slotsMapping.getValue())) {
										subClass = "red";
									} else if("Y".equals(slotsMapping.getValue()))  {
										subClass = "green";
									}
							 %>
									<td  class="<%= subClass %>"><%= slotsMapping.getValue()%></td>
								<% } %>
							</tr>
						 <% } %>
						
	    	     </table>
	    	
				 <% } %>
				</div>
			</div>
	</div>

<%@ include file='/common/i_gmapanalyzerviewer.jspf'%>
<script>
      
       function doCompositeLink(compId1,compId2,compId3,compId4,compId5,url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
					  var param4 = document.getElementById(compId4).value;
					  var param5 = document.getElementById(compId5).checked;
			          if(param1.length == 0 || param3.length == 0 || param4.length == 0) {
			          		alert("Please select the required filter param (Date, Group, serviceType)");
			          } else {
			          	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4+"&"+compId5+"="+param5;
			          }
        } 

	  Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "selectedDate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_selectedDate" 
                }
               );
		 
		 <% if(request.getParameter("cutOff") != null && request.getParameter("selectedDate") != null && request.getParameter("serviceType") != null && request.getParameter("group") != null &&	"true".equalsIgnoreCase(request.getParameter("autorefresh"))) { %>
		      		doRefresh(<%= TransportationAdminProperties.getCapacityRefreshTime() %>);
		 <% } %>
		
		function doBoundary() {
			var table_capacity = document.getElementById("tbl_capacityAnalyzer");
			var checkboxList_Address = table_capacity.getElementsByTagName("input");
			var addressList = document.getElementsByName('address');
            var checked = "";
            var result = "";
            for (i = 0; i < checkboxList_Address.length; i++) {            
              if (checkboxList_Address[i].type=="checkbox" && !checkboxList_Address[i].disabled)  {
              	if(checkboxList_Address[i].checked) {
              		checked += checkboxList_Address[i].name+",";
					result += addressList[i].innerHTML+"_";
              	}
              }
            }
            if(checked.length == 0) {
             	alert('Please Select a Row!');
            }
            else {
              	showBoundary(checked.substring(0,checked.length-1), result);
            }
		}

		function doSelect() {
			var table_capacity = document.getElementById("tbl_capacityAnalyzer");
			var checkboxList_Address = table_capacity.getElementsByTagName("input");
                        
            for (i = 0; i < checkboxList_Address.length; i++) {            
              if (checkboxList_Address[i].type=="checkbox" && !checkboxList_Address[i].disabled)  {
            	  checkboxList_Address[i].checked = true;
              }
            }
		}

		function doClear() {
			var table_capacity = document.getElementById("tbl_capacityAnalyzer");
			var checkboxList_Address = table_capacity.getElementsByTagName("input");
                        
            for (i = 0; i < checkboxList_Address.length; i++) {            
              if (checkboxList_Address[i].type=="checkbox" && !checkboxList_Address[i].disabled)  {
            	  checkboxList_Address[i].checked = false;
              }
            }
		}
		

     </script> 
  </tmpl:put>
</tmpl:insert>
