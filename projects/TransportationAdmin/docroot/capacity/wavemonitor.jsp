<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>

<%  
   pageContext.setAttribute("HAS_ADDBUTTON", "false");
   pageContext.setAttribute("HAS_DELETEBUTTON", "false");
   pageContext.setAttribute("IS_USERADMIN", ""+com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request));
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getNextDate();
 %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Wave Monitor</tmpl:put>
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
                    <span style="font-size: 18px;font-weight: bold;">Wave Monitor</span>
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
                      
                                   
                     var jsonrpcClient = new JSONRpcClient("capacityprovider.ax");

                     var currentUpdateSource;
                      
                    function doCompositeLink(compId1, compId2, compId3, url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
			          			          			          
			          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
        			} 
        			
        			function addTSRowHandlers(tableId, rowClassName) {
					    var previousClass = null;
					    var table = document.getElementById(tableId);
					    
					    if(table != null) {
						    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
						    for (i = 0; i < rows.length; i++) {	    	
						        var cells = rows[i].getElementsByTagName("td");
						        
						        for (j = 0; j < cells.length-2; j++) {
						        	
						            cells[j].onmouseover = function () {
						            	previousClass = this.parentNode.className;
						            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
						            };
						        
						            cells[j].onmouseout = function () {
						              	this.parentNode.className = previousClass;
						            };
						        
						            cells[j].onclick = function () {						            	 
						            	var tdLst = this.parentNode.getElementsByTagName("td");	
						            	var notificationMsg = tdLst[tdLst.length-1].innerHTML;
						            	if(notificationMsg != null && notificationMsg.indexOf('orders will be unassigned') > 0) {
						            		if (confirm ("You are about to mark wave instance for force option. Do you want to continue?")) {
								            	try {
							            			var result = jsonrpcClient.AsyncCapacityProvider.forceWaveInstance(tdLst[0].innerHTML);
							            			if(result == 0) {
							                       	 	alert('Unable to mark wave instance for force option!');
							                        } else {
								                        alert('Wave Instance marked with force option successfully');
								                        window.location.reload();
							                        } 
								            	} catch(e) {
								            		alert('Unable to mark wave instance for force option!');
								            	}
						            		}						            									            		 
						            	}									      		
								    };
						        }
						    }
						}
				}
                    
                 
                          
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>
                <td> 
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
                
                </td>
                <td> 
                  <select id="waveStatus" name="waveStatus"> 
                  	  <option value="">----All----</option>                     
                      <option <c:choose> <c:when test="${waveStatus == 'SYN'}" >selected </c:when> </c:choose> value="SYN">In Sync</option>  
                      <option <c:choose> <c:when test="${waveStatus == 'NYN'}" >selected </c:when> </c:choose> value="NYN">Not In Sync</option>
                   </select>
                
                </td>
                   
                <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('rDate','cutOff','waveStatus','wavemonitor.do')" />
                  </td>  
                  
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
           
      </div>
      <div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					  <form id="waveMonitorForm" action="" method="post"> 
	    				<ec:table items="waveinstances"   action="${pageContext.request.contextPath}/wavemonitor.do"
				            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
				            width="98%" form="waveMonitorForm" filterable="true" showPagination="true" rowsDisplayed="100" view="fd" >
				            
				            <ec:exportPdf fileName="waveinstances.pdf" tooltip="Export PDF" 
				                      headerTitle="Wave Monitor" />
				            <ec:exportXls fileName="waveinstances.xls" tooltip="Export PDF" />
				            <ec:exportCsv fileName="waveinstances.csv" tooltip="Export CSV" delimiter="|"/>
				                
				            <ec:row>                
				              <ec:column property="waveInstanceId"  title="ID"/> 
				              <ec:column property="routingWaveInstanceId"  title="REF ID"/>
				              <ec:column property="deliveryDate" title="Delivery" />             
				              <ec:column property="area.areaCode" title="Zone" />
				              <ec:column property="dispatchTime" title="Dispatch" />
				              <ec:column property="waveStartTime" title="Start" />
				              <ec:column property="preferredRunTimeInfo" title="Run Time" />
				              <ec:column property="maxRunTimeInfo" title="Max Time" />
				              <ec:column property="noOfResources" title="Resources" />
				              <ec:column property="cutOffTime" title="CutOff" />
				              <ec:column property="source" title="Source" />				              
				              <ec:column cell="waveinstancestatus" property="isInSync" title="Status"/>
				              <ec:column cell="positive" property="force" title="Force"/>
				              <ec:column property="notificationMessage" title="Notification" />				              				              																											  	                           
				            </ec:row>
				          </ec:table>
					</form> 
				</div>
			</div>
		</div>
	</div>	          
	   	  <script>
		      addTSRowHandlers('ec_table', 'rowMouseOver');
		   </script>   
  </tmpl:put>
  
</tmpl:insert>
