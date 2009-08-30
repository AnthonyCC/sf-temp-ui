<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>

<%  
   pageContext.setAttribute("HAS_ADDBUTTON", "false");
   pageContext.setAttribute("HAS_DELETEBUTTON", "false");
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
 %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Early Warning View</tmpl:put>
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
                    <span style="font-size: 18px;font-weight: bold;">Early Warning</span>
                </td>                
                  <td> 
                    <span><input maxlength="10" size="10" name="rDate" id="rDate" value='<c:out value="${rDate}"/>' /></span>
                     <span><a href="#" id="trigger_rptDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a></span>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "rDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_rptDate" 
                       }
                      );
                      
                                   

                    function doCompositeLink(compId1,compId2,compId3, url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
			          			          
			          if(param1.length == 0 || param2.length == 0) {
			          		alert("Please select the required filter param (Date, Cut Off)");
			          } else {
			          	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
			          }
        			} 
        			
        			function addTSRowHandlers(tableId, rowClassName) {
					    var previousClass = null;
					    var table = document.getElementById(tableId);
					    
					    if(table != null) {
						    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
						    for (i = 0; i < rows.length; i++) {	    	
						        var cells = rows[i].getElementsByTagName("td");
						        
						        for (j = 0; j < cells.length; j++) {
						        	
						            cells[j].onmouseover = function () {
						            	previousClass = this.parentNode.className;
						            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
						            };
						        
						            cells[j].onmouseout = function () {
						              	this.parentNode.className = previousClass;
						            };
						        
						            cells[j].onclick = function () {  										      		
								      		showTimeslot('panel-'+this.parentNode.rowIndex
								      						, findPosX(this.parentNode)
								      						, findPosY(this.parentNode));									      		
								    };
						        }
						    }
						}
				}
                    
                 function showTimeslot(rowDiv, rowX, rowY) {
                 	var tsPanel = new YAHOO.widget.Panel(rowDiv, {       
					                          width: "500px",					                           
					                          close: true, 
					                          draggable: true, 
					                          zindex:4,
					                          modal: true,
					                          visible: false,
					                          xy: [rowX, rowY],
					                          effect:{effect:YAHOO.widget.ContainerEffect.SLIDE,duration:0.25}});
					tsPanel.render(document.body);
          			tsPanel.show();					                          
                 }                
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>
                <td> 
                  <select id="cutOff" name="cutOff">
                      <option value="">--Please Select Cut Off</option> 
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
                
                </td>
                
                <td> 
                  <select id="rType" name="rType">                      
                      <option value="T">Time</option>  
                      <option value="O">Order</option>
                   </select>
                
                </td>
                
                   <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('rDate','cutOff','rType','earlywarning.do')" />
                  </td>  
                  
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
           
      </div>
    <table>
    <tr>
    <td style="vertical-align: top;" width="45%">
	    
	      <ec:table items="earlywarnings_region"   action="${pageContext.request.contextPath}/earlywarning.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
	            width="98%"  filterable="false"  showPagination="false" showExports="false" showStatusBar="false" sortable="false" 
	            tableId="region_earlywarning" rowsDisplayed="100" view="fd" >
	            
	            
	            <ec:row interceptor="earlywarningmarker">                
	              <ec:column property="name"  title="Name"/>                           
	              <ec:column property="totalCapacity" title="Planned" />
	              <ec:column property="confirmedCapacity" title="Confirmed" />
	              <ec:column property="percentageConfirmed" title="% Confirmed" />
	              <ec:column property="allocatedCapacity" title="Allocated" />
				  <ec:column property="percentageAllocated" title="% Allocated" />				  	                           
	            </ec:row>
	          </ec:table>
	    
	  </td> 
	  <td width="55%">
	      <ec:table items="earlywarnings"   action="${pageContext.request.contextPath}/earlywarning.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
	            width="98%"  rowsDisplayed="100" view="fd" >
	            
	            <ec:exportPdf fileName="earlywarnings.pdf" tooltip="Export PDF" 
	                      headerTitle="Early Warning" />
	            <ec:exportXls fileName="earlywarnings.xls" tooltip="Export PDF" />
	            <ec:exportCsv fileName="earlywarnings.csv" tooltip="Export CSV" delimiter="|"/>
	                
	            <ec:row interceptor="earlywarningmarker">                
	              <ec:column property="name"  title="Name"/> 
	              <ec:column property="code" title="Zone" />             
	              <ec:column property="totalCapacity" title="Planned" />
	              <ec:column property="confirmedCapacity" title="Confirmed" />
	              <ec:column property="percentageConfirmed" title="% Confirmed" />
	              <ec:column property="allocatedCapacity" title="Allocated" />
				  <ec:column property="percentageAllocated" title="% Allocated" />				  	                           
	            </ec:row>
	          </ec:table>
	    </td>
	    </tr> 
    </table>
	    <div id="timeslot_container" style="display:none;">
	    <%
	    	List gridData = (List)request.getAttribute("earlywarnings");
	    	int _rowIndex = 3;	    	
	    	if(gridData != null) {
	    		
	    		Iterator<EarlyWarningCommand> _itr = gridData.iterator();
	    		EarlyWarningCommand _command = null;
	    		while(_itr.hasNext()) {
	    			_command = _itr.next();
	    			  %>
			    	<div id="panel-<%=_rowIndex %>">
			    	<div class="hd"><img src="images/icons/edit_ON.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;&nbsp;&nbsp;<%="Breakdown-"+_command.getName() %></div>
			    	<div class="bd">
			    	<div class="eXtremeTable" >
			    	<table id="timeslot_table-<%=_rowIndex %>"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="98%" >
				    	<thead>
						<tr>
							<td class="tableHeader" >Name</td>
							<td class="tableHeader" >Planned</td>
							<td class="tableHeader" >Confirmed</td>
							<td class="tableHeader" >% Confirmed</td>
					
							<td class="tableHeader" >Allocated</td>
							<td class="tableHeader" >% Allocated</td>
						</tr>
						</thead>
						<tbody class="tableBody" >
	    			<% List<EarlyWarningCommand> timeslotDetails =  _command.getTimeslotDetails();
	    			if(timeslotDetails != null) {
	    				Iterator<EarlyWarningCommand> _itrTimeslot = timeslotDetails.iterator();
	    				EarlyWarningCommand _commandTS = null;
	    				while(_itrTimeslot.hasNext()) {
	    					_commandTS = _itrTimeslot.next();%>
	    					<tr>
								<td><%=_commandTS.getName() %></td>						
								<td><%=_commandTS.getTotalCapacity() %></td>
								<td><%=_commandTS.getConfirmedCapacity() %></td>
								<td><%=_commandTS.getPercentageConfirmed() %></td>
								<td><%=_commandTS.getAllocatedCapacity()%></td>
								<td><%=_commandTS.getPercentageAllocated()%></td>
							</tr>
	    					
	    			   <%}
	    			} %>
	    			</tbody>
	    	</table>
	    	</div>
	    	</div>
	    	</div>
	    	<% _rowIndex++;}	    		
	    	} %>
	    	
	        </div>
	        <script>
		      addTSRowHandlers('ec_table', 'rowMouseOver');
		    </script>
	     </div>     
	  
  </tmpl:put>
  
  
</tmpl:insert>
