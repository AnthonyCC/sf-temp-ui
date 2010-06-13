<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "Schedule Employees";	
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=T" class="<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=S" class="<% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Schedule</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=C" class="<% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Team</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>

	<div class="contentroot">
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						Schedule
					</span>
					<span style="vertical-align:middle;">Week Of :<input maxlength="10" size="10" name="scheduleDate" id="scheduleDate" value='<c:out value="${scheduleDate}"/>' />
                    <a href="#" id="trigger_scheduleDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "scheduleDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_scheduleDate" 
                       }
                      );
                      </script>
                      &nbsp;Status : 
										
					<select id="statusFilter" ><option value="a">TransApp Active</option>
													   <option value="i">TransApp Inactive</option>
													   <option value="ae">All Employees</option>
													   </select>
					</span>
					
					<span>
                     <input style="font-size:11px" type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doFilter()" />
                  </span> 
                  <span>
                     <input style="font-size:11px" type = "button" value="View Master" onclick="javascript:doMasterFilter()" />
                  </span>
                  <span>
                     <input style="font-size:11px" type = "button" value="Mass Edit" onclick="javascript:massEdit()" />
                  </span> 
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					<form id="employeeListForm" action="" method="post">
					
                      <input type="hidden" name="status" value="<%=request.getAttribute("status")%>"/>
           
						<ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do?empstatus=S"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
									<ec:exportPdf fileName="transportationSchedule.pdf" tooltip="Export PDF" 
									  headerTitle="" />
							  <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">            								
								<ec:column title=" " width="5px" 
										filtercell="selectcol" sortable="false" cell="selectcol"
										property="employeeId" />
								<ec:column property="status" title="Status"/>						
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID"/>   
								<ec:column  property="employeeRoleType" title="Role"/>                                  								
                                <ec:column property="mon" title="MON"/>
                                <ec:column property="tue" title="TUE"/>
                                <ec:column property="wed" title="WED"/>
                                <ec:column property="thu" title="THU"/>
                                <ec:column property="fri" title="FRI"/>
                                <ec:column property="sat" title="SAT"/>
                                <ec:column property="sun" title="SUN"/>
                                 
							</ec:row>
						</ec:table>
    
					</form>
				</div>
			</div>
		</div>
	</div>	
		<script>
		   function massEdit() {
			var table_schedule = document.getElementById("ec_table");
			var checked = "";  
			if(table_schedule.tBodies[0] != null) {						
	            var checkboxList_Schedule = table_schedule.tBodies[0].getElementsByTagName("input");
	           	         
	            for (i = 0; i < checkboxList_Schedule.length; i++) {            
	              if (checkboxList_Schedule[i].type == "checkbox")  {
	              	if(checkboxList_Schedule[i].checked) {
	              		checked += checkboxList_Schedule[i].name+",";
	              	}                          	
	              }
	            }
	        }
            
            if(checked.length == 0) {
             	alert('Please Select a Row!');
            }  else {
                location.href = "editschedule.do?id="+checked.substring(0,checked.length-1)+"&"+getFilterTestValue();
            }
		}
		   addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editschedule.do','id',0, 0);
		     function getFilterTestValue() {
		      	var filters = getFilterValue(document.getElementById("employeeListForm"),false);
		      	var param1 = document.getElementById("statusFilter").value;
		        var param2 = document.getElementById("scheduleDate").value;
		      	filters+="&status="+param1;
      			filters+="&scheduleDate="+param2;		      			      	  		      
		      	return escape(filters) + "&status="+param1+"&scheduleDate="+param2;
		      }
		     function doFilter() {
		          var param1 = document.getElementById("statusFilter").value;
		          var param2 = document.getElementById("scheduleDate").value;
		          location.href = "employee.do?empstatus=S&status="+param1+"&scheduleDate="+param2;    
		     }
		     function doMasterFilter() {
		          var param1 = document.getElementById("statusFilter").value;
		          var param2 = '01/01/1900';
		          location.href = "employee.do?empstatus=S&status="+param1+"&scheduleDate="+param2;    
		     }
		     document.getElementById("statusFilter").value='<%=request.getAttribute("status")%>'
		</script>	
  </tmpl:put>
</tmpl:insert>
