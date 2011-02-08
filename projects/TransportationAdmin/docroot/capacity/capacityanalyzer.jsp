<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>

<% 
	String pageTitle = "Capacity Analyzer";
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
		<style>
			.summaryTable th, .summaryTable td {
				cursor: pointer;
				cursor: hand;
			}
			.summaryTable th.first {
				border-right: 2px solid #000;
			}
			.summaryTable th.last {
				border-right: 2px solid #000;
			}
			.summaryTable th {
				border-bottom: 2px solid #000;
			}
			.summaryTable td.first {
				border: 1px solid #000;
				border-right: 2px solid #000;
				border-top: none;
				font-weight: bold;
			}
			.summaryTable td.last {
				border-right: 2px solid #000;
			}
			.summaryTable td {
				border-right: 1px dashed #000;
				text-align: center;
			}
			.summaryTable td.red {
				background-color: red;
			}
			.summaryTable td.yellow {
				background-color: yellow;
			}
			.redSummary {
				background-color: red;
				font-weight: bold;
				padding:2px;
				text-align:center;
				width:350px;
			}
			.yellowSummary {
				background-color: yellow;
				font-weight: bold;
				padding:2px;
				text-align:center;
				width:350px;
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
									  <option value='<c:out value="${deliveryGroup.groupId}"/>'><c:out value="${deliveryGroup.groupName}"/></option>
								</c:forEach>   
							  </select>
						</span>
						<span style="font-size: 11px;">&nbsp;Auto Refresh:
							<input type="checkbox" name="autorefresh" id="autorefresh" <%= ("true".equalsIgnoreCase(request.getParameter("autorefresh")) ? "checked=\"true\"" : "false") %>  />
						</span>
						&nbsp;&nbsp;&nbsp;
						<span>
							<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('selectedDate','cutOff',''group','autorefresh','capacityanalyzer.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /><br>
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




				</div>
			</div>
	</div>






<script>
      
       function doCompositeLink(compId1,compId2,compId3,compId4,url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
					  var param4 = document.getElementById(compId4).checked;
			          if(param1.length == 0 | param3.length == 0) {
			          		alert("Please select the required filter param (Date, Group)");
			          } else {
			          	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4;
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
		 
		 addTSRowHandlers('ec_table', 'rowMouseOver');

		 <% if(request.getParameter("cutOff") != null && request.getParameter("selectedDate") != null && request.getParameter("group") != null &&	"true".equalsIgnoreCase(request.getParameter("autorefresh"))) { %>
		      		doRefresh(<%= TransportationAdminProperties.getCapacityRefreshTime() %>);
		 <% } %>
		
		function doBoundary(doShow) {
			var table_capacity = document.getElementById("capacity_analyzer_table");
			var checkboxList_Address = table_capacity.getElementsByTagName("input");
            var checked = "";
            
            for (i = 0; i < checkboxList_Address.length; i++) {            
              if (checkboxList_Address[i].type=="checkbox" && !checkboxList_Address[i].disabled)  {
              	if(checkboxList_Address[i].checked) {
              		checked += checkboxList_Address[i].name+",";
              	}
              }
            }
            if(checked.length == 0) {
             	alert('Please Select a Row!');
            }
            else {
                if(doShow) {
            		showBoundary(checked.substring(0,checked.length-1));
                } else {
                	location.href = "gmapexport.do?code="+checked.substring(0,checked.length-1);	
                }
            }
		}

		function doClear() {
			var table_capacity = document.getElementById("capacity_analyzer_table");
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
