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
	boolean isScribSummary = false;
	if(pageId != null && "scribsummary".equalsIgnoreCase(pageId)) {
		pageTitle = "Scrib Summary View";
		isScribSummary = true;
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
				<div style="width:500px;float:right;">
						<span class="redSummary">Base-selected (No.of Trucks)>0</span>
						<span class="yellowSummary">Base-selected  (No.of Trucks)<0</span>
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
	    		<form id="summaryForm" method="post" action="">		
	    		
	    		<table id="tbl_summaryTable" class="summaryTable" cellspacing="0" cellpadding="0" border="0" width="98%">				    	
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
						 	<td class="first"><%= selectedMapping.getKey()%></td>
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
						 	<td class="first">Total</td>
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
      var isScribSummary = <%= isScribSummary %>;
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
       onload = function() {
    	   var table = document.getElementById("tbl_summaryTable");
    	   var url = "scrib.do";
    	    
    	    if(table != null) {
    		    var rows = table.getElementsByTagName("tr");	 	       
    		    for (i = 0; i < rows.length; i++) {	    	
    		        var cells = rows[i].getElementsByTagName("td");
    		        if(cells == null || cells.length == 0) {
    		        	cells = rows[i].getElementsByTagName("th");
    		        }
    		        for (j = 0; j < cells.length; j++) {
    		        	cells[j].onclick = function () {    		        						      				      		
    		        		if(!((this.parentNode.rowIndex == 0 && this.cellIndex == 0)
    	    		        		|| (this.parentNode.rowIndex == ((table.rows.length)-1))
    	    		        			|| (this.cellIndex == ((table.rows[0].cells.length)-1)))) {
	    		        		var zoneCell = table.rows[this.parentNode.rowIndex].cells[0];
	    		        		var firstDlvCell = table.rows[0].cells[this.cellIndex];
	    		        		var _selectedDate = document.getElementById("selectedDate").value;
	    		        		if(isScribSummary) {
		    		        		if(this.cellIndex == 0) {
		    		        			processCellClick(escape("ec_f_zoneS="+ zoneCell.innerHTML	
				    		        										+ "&ec_f_scribDate=" + _selectedDate						        										
							        										+ "&ec_f_a=fa")
							        										, "scrib.do?daterange=" + _selectedDate);
		    		        		} else if(this.parentNode.rowIndex == 0) {
		    		        			processCellClick(escape("ec_f_firstDlvTime="+ firstDlvCell.innerHTML
				    		        								+ "&ec_f_scribDate=" + _selectedDate							        										
        															+"&ec_f_a=fa")
        															, "scrib.do?daterange=" + _selectedDate);
		    		        		} else {
	    		        				processCellClick(escape("ec_f_zoneS="+ zoneCell.innerHTML
	    	    		        										+"&ec_f_firstDlvTime="+ firstDlvCell.innerHTML
	    	    		        										+ "&ec_f_scribDate=" + _selectedDate
	    	    		        										+"&ec_f_a=fa")
	    	    		        										, "scrib.do?daterange=" + _selectedDate);
		    		        		}
	    		        		} else {
	    		        			if(this.cellIndex == 0) {
		    		        			processCellClick(escape("ec_f_zoneCode="+ zoneCell.innerHTML	
							        										+ "&ec_f_a=fa")
							        										, "plan.do?daterange=" + _selectedDate
							        											+"&weekdate="+_selectedDate);
		    		        		} else if(this.parentNode.rowIndex == 0) {
		    		        			processCellClick(escape("ec_f_firstDeliveryTime="+ firstDlvCell.innerHTML
	       															+"&ec_f_a=fa")
        															, "plan.do?daterange=" + _selectedDate
        																	+"&weekdate="+_selectedDate);
		    		        		} else {
	    		        				processCellClick(escape("ec_f_zoneCode="+ zoneCell.innerHTML
	    	    		        										+"&ec_f_firstDeliveryTime="+ firstDlvCell.innerHTML
	    	    		        										+"&ec_f_a=fa")
	    	    		        										, "plan.do?daterange=" + _selectedDate
	    	    		        														+"&weekdate="+_selectedDate);
		    		        		}
	    		        		}
    		        		}	      		
				    	};    			    		    	
    		        }
    		    }
    		}
       }

       function processCellClick(filterParam, formAction) {
    	   var filters = unescape(filterParam);	      	     	
	       var params = filters.split("&");
	       var summaryForm = document.forms["summaryForm"];
	       for(var i=0; i<params.length; i++) {
	      		var param = params[i].split("=");         				
	      		add_input(summaryForm, "hidden", param[0], param[1]);
	       } 
	       summaryForm.action = formAction;	      	
	       summaryForm.submit();
       }

    </script>   
  </tmpl:put>
</tmpl:insert>
