
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebDispatchStatistics' %>
<%@ page import = 'java.util.*'%>
<%@ page import = 'java.util.Iterator'%>
<%@page import ='java.util.List'%>
<% 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
 %>

<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'>Dispatch Summary</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>
    <!-- <head><meta http-equiv='refresh' content='60;url='./dispatchSummary.do'></head> -->
    <script language="javascript">    
    </script>
   
    <div class="contentroot">               
    
	<table border="0">
		<tr>
			<td>
				<span style="font-size: 18px;font-weight: bold;">Today: <%= TransStringUtil.getFullMonthDate(new Date()) %></span>
			</td>			
		</tr>
		<tr>
		<td>
			<br/><br/>
		</td>
		</tr>
	</table>
	
	<table cellpadding="0" cellspacing="0" align="center" class="dispatchsummary">
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
								<td colspan="3">
									<div class="dispatchSummaryContent">
									   	 <%String  wpr =(String)request.getAttribute("unAvailableEmpList");%>									  
										 <%=wpr%>
									</div>
								</td>
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
								<td colspan="3">
									<div class="dispatchSummaryContent">
										<% String   routes =(String)request.getAttribute("unAvailableRoutes");%>
									    <%=routes%>
									</div>
								</td>
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
                	<td style="font-weight: bold; font-size:  12px; text-align: center;">Employees Punched but not Working</td>
                </tr>
                <tr>
					<td>
						<center>
						<table width= "200" cellpadding="0" cellspacing="0" border="0">  
							<tr>
								<td colspan="3">
									<div class="dispatchSummaryContent">
									    <% String webEmpInfo =(String)request.getAttribute("unassignedEmpList");%>
									     
	    								<%=webEmpInfo%>             
                                    </div>
								</td>
							</tr>							
						</table>
						</center>
					</td>
				</tr>
				</table>
			</td>
			<td>
                <table width="100%" align="right">
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
	                		<table style="font-weight: bold; text-size: 12px; text-align: center;">
	                			<tr>
	                				<td>
										<table cellpadding="0" cellspacing="0" border="0">  
											<tr>
												<td colspan="3">
													<div class="dispatchStatistices">
														
														 <% WebDispatchStatistics amStats=(WebDispatchStatistics)request.getAttribute("statistics"); %>
                                                          	Routes Planned: <b><%=amStats.getPlannedAmRoute()%></b> 
                                                           <br/>         
                                                            Routes Actual: <b><%=amStats.getDispatchAmRoute()%></b>
                                                           <br/>
                                                            Employees worked six consecutive days:<b><%=amStats.getEmpAmWorkedSixdays()%></b>
                                                           <br/>
                                                            Dispatch Team Changes: <b><%=amStats.getAMTeamChange()%></b>
                                                           <br/>
                                                            Dispatch Team Changes out of region:<b><%=amStats.getAMTeamChangeRegionOut()%></b>
                                                           <br/>
                                                            Fire Trucks/MOT: <b><%=amStats.getAMfireTruckorMOT()%> </b>
														   <br/>
													</div>
												</td>
											</tr>											
										</table>
									</td>
	                				<td>
										<table cellpadding="0" cellspacing="0" border="0">  
											<tr>
												<td colspan="3">
													<div class="dispatchStatistices">
														<% WebDispatchStatistics pmStats=(WebDispatchStatistics)request.getAttribute("statistics"); %>
                                                           Routes Planned: <b><%=pmStats.getPlannedPmRoute()%></b> 
                                                           <br/>         
                                                           Routes Actual: <b><%=pmStats.getDispatchPmRoute()%></b>
                                                           <br/>
                                                           Employees worked six consecutive days:<b><%=pmStats.getEmpPmWorkedSixdays()%></b>
                                                           <br/>
                                                           Dispatch Team Changes: <b><%=pmStats.getPMTeamChange()%></b>
                                                           <br/>
                                                           Dispatch Team Changes out of region:<b><%=pmStats.getPMTeamChangeRegionOut()%></b>
                                                           <br/>
                                                           Fire Trucks/MOT: <b><%=pmStats.getPMfireTruckorMOT()%></b>
                                                           <br/>
													</div>
												</td>
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
								<td colspan="3">									
									  <div class="dispatchSummaryContent" ">
                                           <% String htOUT= (String)request.getAttribute("handTruckInvList"); %>                                                   
                                           <%=htOUT%>                                                  	                                      													
									   </div>                                   									
								</td>
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
								<td colspan="3">
									<div class="dispatchReadyRoutes">
										<% String  dispatchreadyroutes =(String)request.getAttribute("readyRoutes");%>
										<%=dispatchreadyroutes%>	
									</div>
								</td>
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
