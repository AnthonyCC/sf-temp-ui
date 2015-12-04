<%@ page import="com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceCmd"%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='crm' prefix='crm'%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='content' direct='true'>
	
				<!--jQuery dependencies-->
				<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
				<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
				<!--ParamQuery Grid files-->
				
				<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/hot-sneaks/jquery-ui.css" />
				<link rel="stylesheet" href="/assets/javascript/paramquery-2.0.3/pqgrid.min.css" />
				<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/humanity/jquery-ui.css" />
				
				<script type="text/javascript" language="javascript" src="/assets/javascript/paramquery-2.0.3/pqgrid.min.js"></script>
				
				

				
				<crm:GetCurrentAgent id="currentAgent">
				
				
				
					<jsp:include page="/includes/crm_standing_orders_nav.jsp" />
					
					<table width="100%">
						<tr>
							<td colspan="3">
								<div class="promo_page_header_text">Run Standing Order Job</div>
							</td>
						</tr>
					</table>
							
					
				



				<%
					//application.setAttribute("IS_SO_JOB_RUNNING", "true"); 	
				
					String isSOJobRunning = (String) application.getAttribute("IS_SO_JOB_RUNNING");
					
					if( isSOJobRunning != null && isSOJobRunning.equalsIgnoreCase("true")){
						
				%>
				
					<h3>Job is currently running and may take up to 30 mins to complete - refresh page for updates.</h3>
					
					
				<% } else { %>						

				
							<br/><br/>
							
							<form method="post">							

							
									<table>
										<tr>
											<td>
												Enter order numbers:
											</td>
											<td>
												<input type="text" name="orders" value="" size="60"/>
											</td>				
										</tr>
										
										<tr>
											<td>												
											</td>
											<td>
												(order seperated by comma. For example, "15868528523,15742923439") 
											</td>				
										</tr>
										
										<tr>
											<td>												
											</td>
											<td>
												(<b><font color="red">If no orders specified, all SO orders will be placed.</font></b>)  
											</td>				
										</tr>		
										<tr>
											<td>
												Send report email:
											</td>
											<td>
												<select name="sendReportEmail">
													<option value="false">No</option>
													<option value="true">Yes</option>											  
												</select>
											</td>				
										</tr>							

									</table>											
								
									<br/><br/>
									
									<input type="submit" value="Run SO Job"/>
								
							</form>
							
							<br/><br/>
							
							
							
							<br/><br/>
							<hr>
							
							
							<%
								String ordersToBePlaced = "";
								boolean sendReportEmail = false;
								String sendEmailStr = "";
								
								if ("POST".equalsIgnoreCase(request.getMethod())){
									try {
										ordersToBePlaced = request.getParameter("orders");
										sendEmailStr = (String)request.getParameter("sendReportEmail");
										sendReportEmail = sendEmailStr.equals("true");
										
										//System.out.println("ordersToBePlaced = " + ordersToBePlaced);
										//System.out.println("sendReportEmail = " + sendReportEmail);
										//System.out.println("isSOJobRunning = " + isSOJobRunning);
										
										StandingOrdersServiceCmd soService = new StandingOrdersServiceCmd();
										soService.runManualJob( ordersToBePlaced, sendReportEmail, application );
																
									} catch (Exception e){								
									}						
								}
							%>
							
							
							<br/>
							
							<h3>Here is what you entered: </h3> <br/>
							
							Orders to be placed: <%=ordersToBePlaced%> <br/>
							Send Report Email: <%=sendReportEmail%> <br/>
						
					<%							
						}						
					%>					
					
				</crm:GetCurrentAgent>
				
	</tmpl:put>
</tmpl:insert>


































