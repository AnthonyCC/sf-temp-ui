<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeReverseRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RecurringRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RestrictionI"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>
<%@ page import='java.util.List' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>

	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">	
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Add Platter Restrictions</tmpl:put>
<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />
<%! DateFormat DLV_TIME_FORMATTER = new SimpleDateFormat("hh:mm a");
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); %>
    <% 
      String restrictionId=request.getParameter("restrictionId");
      Date startDt=null;
      if(null !=request.getParameter("startDate")){
    	  startDt =dateFormat.parse(request.getParameter("startDate")+" "+request.getParameter("platterStartTime"));
      }
      String startDate = 
			NVL.apply(request.getParameter("startDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
      String endDate = 
			NVL.apply(request.getParameter("endDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
			String reason = NVL.apply(request.getParameter("reason"), "all");
			String message = NVL.apply(request.getParameter("message"), "");
			String restrictedType = NVL.apply(request.getParameter("restrictedType"), "");
            String name=NVL.apply(request.getParameter("name"), "");
            String criterion=NVL.apply(request.getParameter("criterion"), "");
            String dayOfWeek=NVL.apply(request.getParameter("dayOfWeek"), "");
            String path=NVL.apply(request.getParameter("path"), "");
            
            
            final TreeMap timeMap=new TreeMap();
            timeMap.put(new Integer(0),"00:00 AM");    
            timeMap.put(new Integer(1),"01:00 AM");    
            timeMap.put(new Integer(2),"02:00 AM");    
            timeMap.put(new Integer(3),"03:00 AM");    
            timeMap.put(new Integer(4),"04:00 AM");    
            timeMap.put(new Integer(5),"05:00 AM");    
            timeMap.put(new Integer(6),"06:00 AM");    
            timeMap.put(new Integer(7),"07:00 AM");    
            timeMap.put(new Integer(8),"08:00 AM"); 
            timeMap.put(new Integer(9),"09:00 AM");
            timeMap.put(new Integer(10),"10:00 AM");    
            timeMap.put(new Integer(11),"11:00 AM");    
            timeMap.put(new Integer(12),"12:00 PM");    
            timeMap.put(new Integer(13),"01:00 PM");    
            timeMap.put(new Integer(14),"02:00 PM");    
            timeMap.put(new Integer(15),"03:00 PM");    
            timeMap.put(new Integer(16),"04:00 PM");    
            timeMap.put(new Integer(17),"05:00 PM");    
            timeMap.put(new Integer(18),"06:00 PM");    
            timeMap.put(new Integer(19),"07:00 PM");    
            timeMap.put(new Integer(20),"08:00 PM");    
            timeMap.put(new Integer(21),"09:00 PM");    
            timeMap.put(new Integer(22),"10:00 PM");    
            timeMap.put(new Integer(23),"11:00 PM");   
      
    %>
    <div class="home_search_module_content" style="height:91%;">
    <crm:AdminToolsController result="result">
    <%
    String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";
    %>
	<crm:GetRestrictedDlv id="restrictionList" restrictionId="<%= restrictionId %>">
<%
      if(restrictionList!=null && restrictionList.size()>0 && request.getParameter("actionName")==null){
        RestrictionI restriction=(RestrictionI)restrictionList.get(0);
        restrictionId=restriction.getId();
        restrictedType=restriction.getType().getName();
		name=restriction.getName();		
		reason=restriction.getReason().getName();
		message=restriction.getMessage();
		criterion=restriction.getCriterion().getName();
		path=restriction.getPath();
          if(restriction instanceof OneTimeReverseRestriction){
                OneTimeReverseRestriction otrRestriction=(OneTimeReverseRestriction)restriction;			
                startDate=CCFormatter.formatDateYear(otrRestriction.getDateRange().getStartDate());
                endDate=CCFormatter.formatDateYear(otrRestriction.getDateRange().getEndDate());			
          }else if(restriction instanceof OneTimeRestriction){
                OneTimeRestriction otRestriction=(OneTimeRestriction)restriction;			
                startDate=CCFormatter.formatDateYear(otRestriction.getDateRange().getStartDate());
                startDt=otRestriction.getDateRange().getStartDate();
                endDate=CCFormatter.formatDateYear(otRestriction.getDateRange().getEndDate());			
          }else if(restriction instanceof RecurringRestriction){
                RecurringRestriction rRestriction=(RecurringRestriction)restriction;
                dayOfWeek=""+rRestriction.getDayOfWeek();
                startDate=CCFormatter.formatDateYear(rRestriction.getTimeRange().getStartTime().getNormalDate());
                endDate=CCFormatter.formatDateYear(rRestriction.getTimeRange().getEndTime().getNormalDate());			
          }
      }
      
     
    	
    
	 


%>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b3> <font color="red"> <%=successMsg%> </font> <b3>

<br>
	<table width="90%" cellpadding="0" cellspacing="5" border="0" class="case_content_text">
	<tr><td colspan="8" align="left"><span><b>Add Platter Restrictions<br><br></b></span></td></tr>
<form name="restriction_detail" method="POST">        
<fd:ErrorHandler result='<%=result%>' name='startDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
	<tr>
		<!-- td class="case_content_field">Id</td-->
		<td><input type="hidden" name="restrictionId" value="<%=restrictionId%>">
            <input type="hidden" name="actionName" value="addPlatterRestriction">
             <input type="hidden" name="criterion" value="<%=EnumDlvRestrictionCriterion.PURCHASE.getName()%>">
        </td>				
	</tr>	
	<tr>
		<td class="case_content_field">TYPE</td>        
		<td>		
			<select name="restrictedType" style="width: 50px;">            
			
			<%  
				EnumDlvRestrictionType eType=EnumDlvRestrictionType.ONE_TIME_RESTRICTION;
			%>
				<option value="<%= eType.getName() %>" <%= eType.getName().equals(restrictedType) ? "SELECTED" : "" %>><%= eType.getName() %></option>
			
			</select>                     			
		</td>		
	</tr>
	<tr>
		<td class="case_content_field">NAME</td>
		<td>			            
            <input type="text" length="50" size="50" name="name" value="<%=name%>">        
			<fd:ErrorHandler result='<%=result%>' name='name' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
</tr>
	<tr><td colspan="4"></td></tr>
	<!-- tr valign="top">
		<td class="case_content_field">DAY_OF_WEEK</td>
		<td>			
             <input type="text" name="dayOfWeek" value="<%=dayOfWeek%>">
			<fd:ErrorHandler result='<%=result%>' name='dayOfWeek' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
		</td>
	</tr-->
	<tr>
		<td class="case_content_field">START DATE</td>
		<td>            
			<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">
			<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">                        
            <input type="text" name="newStartDate" id="newStartDate" size="10" value="<%=startDate%>" disabled="true" onchange="setDate1(this);"> &nbsp;<a href="#" id="trigger_startDate" style="font-size: 9px;">>></a>
            <fd:ErrorHandler result='<%=result%>' name='newStartDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
 		        <script language="javascript">
			    function setDate1(field){
			    document.getElementById("startDate").value=field.value;
			    document.getElementById("endDate").value=field.value;
			    }


			    Calendar.setup(
			    {
			    showsTime : false,
			    electric : false,
			    inputField : "newStartDate",
			    ifFormat : "%Y-%m-%d",
			    singleClick: true,
			    button : "trigger_startDate" 
			    }
			    );
			    
			    function clearAll(){
			    	 var d = new Date();
				    var date = d.getDate();
				    var month = d.getMonth()+1;
				    var year = d.getFullYear();
			    	var fd = year + "-" + month + "-" + date;                    
			    	document.getElementById("startDate").value = fd;
                  document.getElementById("newStartDate").value = fd;
			    	document.getElementById("reason").value = "";
			    	document.getElementById("message").value = "all";
			    	document.getElementById("restrictedType").value = "";
			    }
			    
			    			    
					//document.getElementById("actionName").value = actionName;
					//document.getElementById("searchcriteria").submit();	
					
				
			    	
			    

			    function openURL(inLocationURL) {

			    	popResize(inLocationURL, 400,400,'');

			    }

			</script>
		</td>		
	</tr>
	<!-- tr>
		<td class="case_content_field">END DATE</td>
		<td>			
			<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">                        
            <input type="text" name="newEndDate" id="newEndDate" size="10" value="<%=endDate%>" disabled="true" onchange="setDate2(this);"> &nbsp;<a href="#" id="trigger_endDate" style="font-size: 9px;">>></a>
            <fd:ErrorHandler result='<%=result%>' name='newEndDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
 		        <script language="javascript">
			    function setDate2(field){
			    document.getElementById("endDate").value=field.value;

			    }


			    Calendar.setup(
			    {
			    showsTime : false,
			    electric : false,
			    inputField : "newEndDate",
			    ifFormat : "%Y-%m-%d",
			    singleClick: true,
			    button : "trigger_endDate" 
			    }
			    );			    									                
			    
			    function openURL(inLocationURL) {

			    	popResize(inLocationURL, 400,400,'');

			    }

			</script>
			
		</td>		
	</tr-->
	<tr>
	<td class="case_content_field">Platter CutOff Time</td>
	<td><% int index=0; %>
	<select name="platterStartTime">                
               <%                  
                     java.util.Set keySet=timeMap.keySet();  
                     java.util.Iterator iterator=keySet.iterator();
                     while(iterator.hasNext()){
                       Integer key=(Integer)iterator.next();                    
                       String value=(String)timeMap.get(key);
                     
               %>
                <option value="<%=value%>" <%=null !=startDt && DLV_TIME_FORMATTER.format(startDt).equals(value)?"selected":"" %> > <%=value%></option>
               <%
                    }
                 
                %>                                               
               
                </select>
                </td>
    </tr>
   	<tr>
		<td class="case_content_field">REASON</td>
		<td>
        <select name="reason" style="width: 120px;">				
        	<%  
				EnumDlvRestrictionReason eReason1=EnumDlvRestrictionReason.PLATTER;//enumReason;
			%>
				<option value="<%= eReason1.getName() %>" <%= eReason1.getName().equals(reason) ? "SELECTED" : "" %>><%= eReason1.getName() %></option>
		
		</select>    <fd:ErrorHandler result='<%=result%>' name='reason' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
			
		</td>		
	</tr>
    <tr>
		<td class="case_content_field">MESSAGE</td>
		<td>			
			<input type="text" length="50" size="50" name="message" value="<%=message%>">        
            <fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>    
    <tr>
		<td class="case_content_field">MEDIA PATH</td>
		<td>			
			<input type="text" length="50" size="50" name="path" value="<%=path%>">        
            <fd:ErrorHandler result='<%=result%>' name='path' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>    
    <!-- tr>
		<td class="case_content_field">CRITERION</td>
		<td>			
        <select name="criterion" style="width: 120px;">				
            <%
			List enumCriterion=EnumDlvRestrictionCriterion.getEnumList();
			%>
			<logic:iterate collection="<%= enumCriterion %>" id="enumReason" type="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion">
			<%  
				EnumDlvRestrictionCriterion eReason=(EnumDlvRestrictionCriterion)enumReason;
			%>
				<option value="<%= eReason.getName() %>" <%= eReason.getName().equals(criterion) ? "SELECTED" : "" %>><%= eReason.getName() %></option>
			</logic:iterate>
			</select>                   			
            <fd:ErrorHandler result='<%=result%>' name='criterion' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
		</td>		
	</tr>   -->  
	<tr>
		<td></td>
		<td colspan="1">
			<input type="submit" value="SAVE RESTRICTION" class="submit">&nbsp;&nbsp;
			<INPUT TYPE="button" VALUE="BACK TO SEARCH" class="submit" onclick="javascript:location.href='/admintools/platter_restrictions.jsp';">
		</td>
		<td colspan="2"></td>
	</tr>
    
    
</form>
</table>
	
 </crm:GetRestrictedDlv>
 </crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>