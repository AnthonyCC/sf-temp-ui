
<%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebDispatchStatistics' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.DispatchResourceInfo' %>
<%@ page import= 'com.freshdirect.transadmin.model.EmployeeInfo' %>
<%@ page import= 'com.freshdirect.transadmin.model.RouteInfo' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanResource' %>
<%@ page import= 'com.freshdirect.customer.ErpRouteMasterInfo'%>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebDispatchStatistics' %>
<%@ page import = 'java.util.*'%>
<%@ page import = 'java.util.Iterator'%>
 <%@page import ='java.util.List'%>
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
    <script language="javascript">                 
                                       
                  
                  </script>
    <div class="contentroot">               
    
	<table border="0">
		<tr>
			<td>
				<span style="font-size: 18px;font-weight: bold;">Today: <%= TransStringUtil.getFullMonthDate(new Date()) %></span>
			</td>
		</tr>
	</table>
	<c:if test="${not empty messages}">
		<table border = "1">
			<tr>
				<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
			</tr>
		</table>
	</c:if> 
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
                <table width="200">
                <tr>
                    
                      <td style="font-weight: bold; font-size:  12px; text-align: center;">Today's Schedule Problems<br/><br/></td>
                	
                </tr>
                <tr>
					<td>
						<center>
						<table width="200" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
								<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
							</tr>
							<tr>
								<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
									<div style="width: 200px; font-size: 12px;  height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
									   	 <%String  wpr =(String)request.getAttribute("unAvailableEmpList");%>									  
										 <%=wpr%>
									</div>
								</td>
							</tr>
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
								<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
							</tr>
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
			
			<td>
                <table width="200">
                <tr>
                	<td style="font-weight: bold; font-size:  12px; 12px; text-align: center;">Unassigned Routes<br/><br/></td>
                </tr>
                <tr>
					<td>
						<center>
						<table width="200" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
								<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
							</tr>
							<tr>
								<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
									<div style="width: 200px;font-size: 12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
									
								   	<% String   routes =(String)request.getAttribute("unAvailableRoutes");%>
									  
									   <%=routes%>
									
										</div>
								</td>
							</tr>
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
								<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
							</tr>
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
			
			<td>
                <table width="200">
                <tr>
                	<td style="font-weight: bold; font-size:  12px; text-align: center;">Employees Punched but Not Working</td>
                </tr>
                <tr>
					<td>
						<center>
						<table width= "200" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
								<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
							</tr>
							<tr>
								<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
									<div style="width: 200px; font-size:  12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
									
										     <% String webEmpInfo =(String)request.getAttribute("unassignedEmpList");%>
										      									      
	    										<%=webEmpInfo%>             
                                    </div>
								</td>
							</tr>
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
								<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
							</tr>
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
			<td>
                <table width="200" align="right">
                <tr>
                	<td>
                		<table width="100%" style="font-weight: bold; font-size:  12px; text-align: center;" cellpadding="0" cellspacing="0">
                			<tr><td colspan="2">Dispatch Metrics </td></tr>
                			<tr><td>AM Shift</td><td> PM Shift</td></tr>
                		</table>
					</td>
                </tr>
                <tr>
					<td>
						<center>
	                		<table width="100%" style="font-weight: bold; text-size: 12px; text-align: center;">
	                			<tr>
	                				<td>
										<table width="200" cellpadding="0" cellspacing="0" border="0">  
											<tr>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
												<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
											</tr>
											<tr>
												<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
													<div style="width: 200px;  font-size:  12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
														
														 <% WebDispatchStatistics amStats=(WebDispatchStatistics)request.getAttribute("statistics"); %>
                                                          	Routes Planned: <b><%=amStats.getPlannedAmRoute()%></b> 
                                                           <br/>         
                                                            Routes Actual: <b><%=amStats.getDispatchAmRoute()%></b>
                                                           <br/>
                                                            Employees worked six consecutive days:<b><%=amStats.getEmpAmWorkedSixdays()%></b>
                                                           <br/>
                                                            Dispatch Team Changes:<b><%=amStats.getAMTeamChange()%></b>
                                                           <br/>
                                                            Dispatch Team Changes out of region:
                                                           <br/>
                                                            Fire Trucks/MOT: <b><%=amStats.getAMfireTruckorMOT()%> </b>
														   <br/>
													</div>
												</td>
											</tr>
											<tr>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
												<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
											</tr>
										</table>
									</td>
	                				<td>
										<table width="200" cellpadding="0" cellspacing="0" border="0">  
											<tr>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
												<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
											</tr>
											<tr>
												<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
													<div style="width: 200px; font-size:  12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
														<% WebDispatchStatistics pmStats=(WebDispatchStatistics)request.getAttribute("statistics"); %>
                                                           Routes Planned: <b><%=pmStats.getPlannedPmRoute()%></b> 
                                                           <br/>         
                                                           Routes Actual: <b><%=pmStats.getDispatchPmRoute()%></b>
                                                           <br/>
                                                           Employees worked six consecutive days:<b><%=pmStats.getEmpPmWorkedSixdays()%></b>
                                                           <br/>
                                                           Dispatch Team Changes:<b><%=pmStats.getPMTeamChange()%></b>
                                                           <br/>
                                                           Dispatch Team Changes out of region:
                                                           <br/>
                                                           Fire Trucks/MOT: <b><%=pmStats.getPMfireTruckorMOT()%></b>
                                                           <br/>
													</div>
												</td>
											</tr>
											<tr>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
												<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
												<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
											</tr>
										</table>
									</td>
								</tr>
	                		</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="3">
                <table width="200">
                <tr>
                	<td style="font-weight: bold; font-size:  12px;text-align: center;">Handtruck Inventory</td>
                </tr>
                <tr>
					<td>
						<center>
						<table width="200" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
								<td height="7" width="200" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
							</tr>
							<tr>
								<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
									
									  <div style="width: 200px; font-size:  12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px; ">
                                           <% String htOUT= (String)request.getAttribute("handTruckInvList"); %>                                                   
                                           <%=htOUT%>                                                  	                                      													
									   </div>	                 
                                   									
								</td>
							</tr>
							<tr>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
								<td height="7" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="7"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
							</tr>
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
			<td>
                <table width="300" align="center">
                <tr>
                	<td style="font-weight: bold; font-size:  12px;text-align: center;">Top 10 Ready Routes</td>
                </tr>
                <tr>
					<td>
						<center>
						<table width="300" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td height="5"><img height="6" width="6" src="/media_stat/images/layout/top_left_curve.gif"></td>
								<td height="5" width="300" style="border-top: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="5"><img height="6" width="6" src="/media_stat/images/layout/top_right_curve.gif"></td>
							</tr>
							<tr>
								<td colspan="3" style="border-left: 1px solid rgb(153, 153, 102); border-right: 1px solid rgb(153, 153, 102);">
									<div style="width: 300px; font-size:  12px; height: 142px; overflow-y: scroll; border: 1px solid #383; margin: 5px;">
										<% String  dispatchreadyroutes =(String)request.getAttribute("readyRoutes");%>
										<%=dispatchreadyroutes%>	
									</div>
								</td>
							</tr>
							<tr>
								<td height="5"><img height="6" width="6" src="/media_stat/images/layout/bottom_left_curve.gif"></td>
								<td height="5" style="border-bottom: 1px solid rgb(153, 153, 102);"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"></td>
								<td height="5"><img height="6" width="6" src="/media_stat/images/layout/bottom_right_curve.gif"></td>
							</tr>
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>
    <form name="dispatchForm" action="./dispatchSummary.do" >
    
    </form> 
   
  </tmpl:put>
</tmpl:insert>
