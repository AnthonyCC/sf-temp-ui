

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
	String pageId = (String)request.getAttribute("pageId");
	String pageTitle = "Plan Summary View"; 
	if(pageId != null && "scribsummary".equalsIgnoreCase(pageId)) {
		pageTitle = "Scrib Summary View"; 
	}
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
		</style>
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">						
						<span class="scrTitle"><%=pageTitle%></span>
						<span style="font-weight:bold">
							Selected Date:<input maxlength="40" name="selectedDate" id="selectedDate" value='<c:out value="${selectedDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_selectedDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</span>
						<span style="font-weight:bold">
							&nbsp;Base Date:<input maxlength="40" name="baseDate" id="baseDate" value='<c:out value="${baseDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_baseDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</span>						
						&nbsp;&nbsp;&nbsp;&nbsp;<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('selectedDate','baseDate','<%= pageId %>.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /><br>
						</span>  
				</div>
			</div>
		</div>
		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				<br/><br/>
	    <%
	    	Set<CustomTimeOfDay> columns = (Set<CustomTimeOfDay>)request.getAttribute("allWindows");
	    	Map<String, Map<CustomTimeOfDay, Integer>> selectedSummaryMapping = (Map<String, Map<CustomTimeOfDay, Integer>>)request.getAttribute("selectedSummaryMapping");
	    	Map<String, Map<CustomTimeOfDay, Integer>> baseSummaryMapping = (Map<String, Map<CustomTimeOfDay, Integer>>)request.getAttribute("baseSummaryMapping");
	    	
	    	if(columns != null) {
	    		%>
	    		<table class="summaryTable" cellspacing="0" cellpadding="0" border="0" width="98%">				    	
				    	<tr>
				    		<th class="first">Zone</th>
				    		<%		
				    			for(CustomTimeOfDay range : columns) {  %>	    				    							
									<th><%= range.getTimeString() %></th>
							<% 	}  %>
							<th class="last">Total</th>								
						</tr>
						<%
						 	Map<CustomTimeOfDay, Integer> timeRangeTotal = new TreeMap<CustomTimeOfDay, Integer>();
							for(Map.Entry<String, Map<CustomTimeOfDay, Integer>> selectedMapping : selectedSummaryMapping.entrySet()) {
						 		int zoneTotalCount = 0;
						 		%>
						 	<tr>
						 	<td class="first">&nbsp;&nbsp;<%= selectedMapping.getKey()%>&nbsp;&nbsp;</td>
							<% for(CustomTimeOfDay range : columns) { 
									int selectedCount = 0;
									int baseCount = 0;
									String subClass = "";
									if(selectedMapping.getValue() != null && selectedMapping.getValue().containsKey(range)) {
										selectedCount = selectedMapping.getValue().get(range);
									}
									if(baseSummaryMapping != null && baseSummaryMapping.containsKey(selectedMapping.getKey())
											&& baseSummaryMapping.get(selectedMapping.getKey()).containsKey(range)) {
										baseCount = baseSummaryMapping.get(selectedMapping.getKey()).get(range);
									}
									if((baseCount - selectedCount) > 0) {
										subClass = "red";
									} else if((baseCount - selectedCount) < 0) {
										subClass = "yellow";
									}
									zoneTotalCount = zoneTotalCount + selectedCount;
									if(!timeRangeTotal.containsKey(range)) {
										timeRangeTotal.put(range, 0);
									}
									timeRangeTotal.put(range, timeRangeTotal.get(range)+ selectedCount);
								%> 
									
									<td class="<%= subClass %>"><%= selectedCount %></td>
									<% 
								} %>
							<td class="last"><%= zoneTotalCount %></td>	
							</tr>	 		
						 	<% } %>
							<tr>
						 	<td class="first">&nbsp;&nbsp;Total&nbsp;&nbsp;</td>
						 	<%
						 	int timeRangeGrandTotal = 0;
						 	for(Map.Entry<CustomTimeOfDay, Integer> _trTotal : timeRangeTotal.entrySet()) {
						 		timeRangeGrandTotal = timeRangeGrandTotal + _trTotal.getValue();
						 	%>
						 		<td><%= _trTotal.getValue() %></td> 
						 	<% }
						 	%>
						 	<td class="last"><%= timeRangeGrandTotal %></td>
						 	</tr>
						
	    	     </table>
	    	
	     <% } %>	
 				</div>
 			</div>
 		</div>
 		
     <script>
      
      function doCompositeLink(compId1,compId2,url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
             
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
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
      
	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "baseDate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_baseDate" 
                }
               );

    </script>   
  </tmpl:put>
</tmpl:insert>
