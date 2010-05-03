<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.enums.WeekDay" %>
<%@ page import="com.freshdirect.crm.CrmClick2CallModel" %>
<%@ page import="com.freshdirect.crm.CrmClick2CallTimeModel" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>


<%@page import="com.freshdirect.webapp.taglib.crm.CrmClick2CallControllerTag"%><tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'> Click to Call</tmpl:put>

<tmpl:put name='content' direct='true'>
<script language="javascript">
var selectedList;
var availableList;
function createListObjects(){
    availableList = document.getElementById("availableZones");
    selectedList = document.getElementById("selectedZones");
}
function delAttribute(){
	availableList = document.getElementById("availableZones");
    selectedList = document.getElementById("selectedZones");
	   var selIndex = selectedList.selectedIndex;
	   var len = selectedList.length -1;
	   for(i=len; i>=0; i--){
		   if(true==selectedList.item(i).selected){
	        availableList.appendChild(selectedList.item(i));
		   }
	    }
	   //if(selIndex < 0)
	     // return;
	   //availableList.appendChild(
	     // selectedList.options.item(selIndex))
	   selectNone(selectedList,availableList);
	   //setSize(availableList,selectedList);
	   sortItems();
	}
	function addAttribute(){
		availableList = document.getElementById("availableZones");
	    selectedList = document.getElementById("selectedZones");
	   var addIndex = availableList.selectedIndex;
	   var len = availableList.length -1;
	  
	    for(i=len; i>=0; i--){
	    	
		    if(true==availableList.item(i).selected){
		    	 selectedList.appendChild(availableList.item(i));
		    }
	    }
	   //if(addIndex < 0)
	    //  return;
	   //selectedList.appendChild( 
	     // availableList.options.item(addIndex));
	   selectNone(selectedList,availableList);
	 //  setSize(selectedList,availableList);
	   sortItems();
}


	function setSize(list1,list2){
	    list1.size = getSize(list1);
	    list2.size = getSize(list2);
	}

	function selectNone(list1,list2){
	    //list1.selectedIndex = -1;
	    list2.selectedIndex = -1;
	    addIndex = -1;
	    //selIndex = -1;
	}

	function sortItems(){
		var selectedItems = new Array();
		var availableItems = new Array();
		var lenSelected = selectedList.length -1;
		var lenAvailable = availableList.length -1;

		for(i=0; i<lenSelected; i++){
			selectedItems[i]=selectedList.item(i).value;
	    }
		selectedItems.sort();
		for(i=0; i<lenSelected; i++){
			selectedList.item(i).value = selectedItems[i];
	    }

		
		for(j=0; j<lenAvailable; j++){
			availableItems[j]=availableList.item(j).value;
	    }	    
	    availableItems.sort();
	    for(j=0; j<lenAvailable; j++){
	    	availableList.item(j).value = availableItems[j];
	    }
	}


	function getSize(list){
	    /* Mozilla ignores whitespace, 
	       IE doesn't - count the elements 
	       in the list */
	    var len = list.childNodes.length;
	    var nsLen = 0;
	    //nodeType returns 1 for elements
	    for(i=0; i<len; i++){
	        if(list.childNodes.item(i).nodeType==1)
	            nsLen++;
	    }
	    if(nsLen<2)
	        return 2;
	    else
	        return nsLen;
	}

	function checkEligibleCustomers(){
		for (var i=0; i <document.frmClick2Call.eligibleCustType.length; i++)
		   {
			document.getElementById("segments").style.display="none";
		   if (document.frmClick2Call.eligibleCustType[i].checked)
		      {
		      var rad_val = document.frmClick2Call.eligibleCustType[i].value;
		      if("selected"==rad_val){
				document.getElementById("segments").style.display="block";
		      }
		      }
		   }
		
	}

</script>
<jsp:include page="/includes/admintools_nav.jsp" />
<crm:CrmClick2CallController id="availableDeliveryZones" actionName='<%="saveFaqs"%>' result="saveFaqResult" >
<form method='POST' name="frmClick2Call">
		<% CrmClick2CallModel click2CallModel = (CrmClick2CallModel)pageContext.getAttribute("click2CallInfo");
		boolean edit = false;		
		if(null !=pageContext.getAttribute("mode") && "EDIT".equalsIgnoreCase((String)pageContext.getAttribute("mode"))){
			edit = true;
		}
		%>
  		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="click2Call_status_header">
  			<tr><td colspan="8"  align="center" />&nbsp;</td></tr>
			<tr>
			<td  align="center" width="7%"><b>Status:&nbsp;</b></td>
			<td colspan="3"  align="left" width="30%">
			
			<% if(true==click2CallModel.isStatus()){ %>
			<input type="submit" name="saveStatus" value="Click 2 Call <%= (true==click2CallModel.isStatus()?"ON":"OFF") %>" class="click2Call_button_green"/>&nbsp;Click to turn OFF & Edit
			<% } else if(null !=pageContext.getAttribute("mode") && "EDIT".equalsIgnoreCase((String)pageContext.getAttribute("mode"))){ %>
			<input type="submit" name="saveStatus" value="Click 2 Call <%= (true==click2CallModel.isStatus()?"ON":"OFF") %>" disabled class="click2Call_button_grey"/>&nbsp;
			<% } else {%>
			<input type="submit" name="saveStatus" value="Click 2 Call <%= (true==click2CallModel.isStatus()?"ON":"OFF") %>" class="click2Call_button_red"/>&nbsp;
			<% } %>
			<% if(null !=pageContext.getAttribute("mode") && "EDIT".equalsIgnoreCase((String)pageContext.getAttribute("mode"))){ %>
			Save/Cancel to enable
			<% } %>
			</td>
			
			<td colspan="4" align="left" width="63%">
			<% if(null !=pageContext.getAttribute("mode") && "EDIT".equalsIgnoreCase((String)pageContext.getAttribute("mode"))){ %>
			<b><input type="submit" name="save" value="Save Changes" class="click2Call_button_orange"/></b>&nbsp;&nbsp;<b><input type="submit" name="cancel" value="Cancel Changes" class="click2Call_button_grey" onclick="clearCheckbox(this.form.faqId)"/></b>
			<% } else if(!click2CallModel.isStatus()) { %>
			<b><input type="submit" name="edit" value="Edit Settings" class="click2Call_button_orange"/></b>
			<% } %>
			</td></tr>
			<tr><td colspan="8"  align="center" />&nbsp;</td></tr>
		</table>
		
	<div id="result" class="list_content" style="height:70%;">
	<table align="center" width="95%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">     
       
    
		<tr>
		
			<td colspan="8" align="left" class="case_content_field"><%= pageContext.getAttribute("SUCCESS_MSG")%></td>
	
		</tr>  
		
	<tr><td colspan="8"><br/></td></tr>
	 <tr><td colspan="8" ><span class="click2call_bold">Display Day & Hours:</span><% if(true==edit){ %><span class="not_set"> *Required</span><% } %></td></tr>
	  <tr><td colspan="8" class="click2call_bold"><br/></td></tr>
	  <tr><td colspan="8"><table width="100%">
	 <tr>
	 	<td >&nbsp;</td>
	 	
	 	<% WeekDay weekNames[] = WeekDay.values();
	 	for(int i=1;i<7;i++){
	 		String wkName = weekNames[i].name();
	 	
	 	%>
	 	<% if(i%2==0){ %>
	 	<td align="center" style="background-color: #999999;"><%= wkName %></td>
	 	<% } else { %>
	 	<td align="center" style="background-color: #CCCCCC;"><%= wkName %></td>
	 	<% } %>
	 	<% } %>
	 	<% String wkName = weekNames[0].name();%>
	 	<td align="center" style="background-color: #CCCCCC;"><%= wkName %></td>
	 </tr>
	 <tr>
	 <% String[] timings = CrmClick2CallTimeModel.TIMINGS; %>
	 	<td align="center">Start</td>
	 	<% if(false==edit){ %>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[1].getStartTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[2].getStartTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[3].getStartTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[4].getStartTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[5].getStartTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[6].getStartTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[0].getStartTime())] %></td>
	 	<% } else { %>
	 	<td align="center" style="background-color: #CCCCCC;">
	 	<select name="monStart" id="monStart" style="width:75px;" >
	 	<% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[1].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="tueStart" id="tueStart" style="width:75px;" >
	 	   <% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[2].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="wedStart" id="wedStart" style="width:75px;" >
	 	<% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[3].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="thuStart" id="thuStart" style="width:75px;" >
	 	  <% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[4].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="friStart" id="friStart" style="width:75px;" >
	 	   <% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[5].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="satStart" id="satStart" style="width:75px;" >
	 	    <% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[6].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="sunStart" id="sunStart" style="width:75px;" >
	 	    <% for(int i =0; i<13; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[0].getStartTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
		</select></td>
		<% } %>
	 </tr>
	 <tr><td align="center" style="background-color: #000000;" colspan="8"></td></tr>
	 <tr>
	 	<td align="center">End</td>
	 	<% if( false==edit){ %>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[1].getEndTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[2].getEndTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[3].getEndTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[4].getEndTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[5].getEndTime())] %></td>
	 	<td align="center" style="background-color: #999999;"><%= timings[Integer.parseInt(click2CallModel.getDays()[6].getEndTime())] %></td>
	 	<td align="center" style="background-color: #CCCCCC;"><%= timings[Integer.parseInt(click2CallModel.getDays()[0].getEndTime())] %></td>
	 	<% }else{ %>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="monEnd" id="monEnd" style="width:75px;" >
	 	    <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[1].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="tueEnd" id="tueEnd" style="width:75px;" >
	 	    <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[2].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="wedEnd" id="wedEnd" style="width:75px;" >
	 	    <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[3].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="thuEnd" id="thuEnd" style="width:75px;" >
	 	    <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[4].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="friEnd" id="friEnd" style="width:75px;" >
	 	     <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[5].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #999999;"><select name="satEnd" id="satEnd" style="width:75px;" >
	 	     <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[6].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
	 	<td align="center" style="background-color: #CCCCCC;"><select name="sunEnd" id="sunEnd" style="width:75px;" >
	 	     <% for(int i =12; i<24; i++){ %>
	 	    <option value="<%= i %>" <%= i==Integer.parseInt(click2CallModel.getDays()[0].getEndTime()) ? "selected": " " %>><%= timings[i]%></option>
	 	<%} %>
	 	<option value="<%= 0 %>"><%= timings[0]%></option>
		</select></td>
		<% } %>
	 </tr>
	 <tr><td align="center" style="background-color: #000000;" colspan="8"></td>
	 	</tr>
	 <tr>
	 	<td align="center">Show</td>
	 	<% if( false==edit){ %>
	 	<td align="center" style="background-color: #CCCCCC;"> <%= click2CallModel.getDays()[1].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #999999;"> <%= click2CallModel.getDays()[2].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #CCCCCC;"> <%= click2CallModel.getDays()[3].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #999999;"> <%= click2CallModel.getDays()[4].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #CCCCCC;"> <%= click2CallModel.getDays()[5].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #999999;"> <%= click2CallModel.getDays()[6].isShow()?"Enabled":" " %></td>
	 	<td align="center" style="background-color: #CCCCCC;"> <%= click2CallModel.getDays()[0].isShow()?"Enabled":" " %></td>
	 	<% }else{ %>
	 	<td align="center" style="background-color: #CCCCCC;"><input type="checkbox" name="monShow" id="monShow" <%= click2CallModel.getDays()[1].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #999999;"><input type="checkbox" name="tueShow" id="tueShow" <%= click2CallModel.getDays()[2].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #CCCCCC;"><input type="checkbox" name="wedShow" id="wedShow" <%= click2CallModel.getDays()[3].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #999999;"><input type="checkbox" name="thuShow" id="thuShow" <%= click2CallModel.getDays()[4].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #CCCCCC;"><input type="checkbox" name="friShow" id="friShow" <%= click2CallModel.getDays()[5].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #999999;"><input type="checkbox" name="satShow" id="satShow" <%= click2CallModel.getDays()[6].isShow()?"checked":" " %>/></td>
	 	<td align="center" style="background-color: #CCCCCC;"><input type="checkbox" name="sunShow" id="sunShow" <%= click2CallModel.getDays()[0].isShow()?"checked":" " %>/></td>
	 	<% } %>
	 </tr></table></td></tr>
	 <tr><td colspan="8"><br/></td></tr>
	 <tr><td colspan="8">&nbsp;<br/></td></tr>
	 <tr><td align="center" style="background-color: #000000;" colspan="8"></td>
	 <tr><td colspan="8" ><span class="click2call_bold">Eligible Delivery Zone(s): </span><% if(true==edit){ %><span class="not_set">*Required</span><% } %><br/></td></tr>
	 <tr><td colspan="8">&nbsp;<br/></td></tr>
	 <% String[] selectedZones = click2CallModel.getDeliveryZones();
		    List selected = new ArrayList();
		    if(null != selectedZones && selectedZones.length >0){
		    	selected = Arrays.asList(selectedZones);
		    }
	 	%>
	 <% if(false==edit){			 
	 %><tr><td >&nbsp;</td><td colspan="7" align="left">
	 <logic:iterate id="zoneId" collection="<%= selected %>"  indexId="idx">
	 <%= zoneId %>
	 </logic:iterate>
	 </td></tr>
	 <% } else{ %>
	  <tr><td colspan="2" align="left"> Available </td><td colspan="2">&nbsp;</td><td colspan="2" align="left"> Selected </td></tr>
	 	 <tr><td colspan="2" align="left">
	 	  
	 <select name="availableZones" id="availableZones" size="10" multiple="multiple" style="width:150px;">
<logic:iterate id="zoneId" collection="<%= availableDeliveryZones %>"  indexId="idx">
<% if(!selected.contains(zoneId)){ %>
 <option value="<%= zoneId %>"><%= zoneId %></option>
 <% } %>
</logic:iterate>
    
</select></td>
<td align="left" colspan="2">
<input name="remove" type="button" value="<< Remove" class="click2Call_button_remove" onclick="delAttribute()"/><br/><br/>
<input name="add" type="button" value=" Add >>" class="click2Call_button_add" onclick="addAttribute()"/><br/>

</td>
<td colspan="2" align="left">
	 <select name="selectedZones" id="selectedZones" size="10" multiple="multiple" style="width:150px;">
	
	 <logic:iterate id="zoneId" collection="<%= selected %>"  indexId="idx">
 	<option value="<%= zoneId %>"><%= zoneId %></option>
	</logic:iterate>
	 </select></td>
<td align="left" colspan="2">&nbsp;</td>
</tr><% } %>
	 <tr><td colspan="8"><br/></td></tr>
	 <tr><td align="center" style="background-color: #000000;" colspan="8"></td>
	 <tr><td colspan="8" class="click2call_bold">Eligible Customer(s): <br/></td></tr>
	 <tr><td colspan="8">&nbsp;<br/></td></tr>
	 <% String eligibleCust = click2CallModel.getEligibleCustomers();
	    List eligibleList = new ArrayList();
	    if(null !=eligibleCust){
	 		String[] eligibleCustomers = eligibleCust.split(",");
	 		eligibleList = Arrays.asList(eligibleCustomers);	 	
	    }
	 %>
	 <% if(false==edit){ %>
	    <% if(eligibleList.contains("everyone")){ %>
	       <tr><td >&nbsp;</td><td colspan="7">Everyone</td></tr>
	    <% }else{ %>
	    	<tr><td >&nbsp;</td><td colspan="7"><b>Selected segments</b></td></tr>
	    	<tr><td colspan="8">&nbsp;<br/></td></tr>
	    	<% if(eligibleList.contains("ct_dp")||eligibleList.contains("ct_ndp")){ %>
	    	<tr><td colspan="2">&nbsp;</td><td colspan="6">Chef's Table</td></tr>
	    		<%if(eligibleList.contains("ct_dp")){ %>
	    		<tr><td colspan="2">&nbsp;</td><td colspan="6">Delivery Pass</td></tr>
	    	   	<% } if(eligibleList.contains("ct_ndp")){ %>
	    		<tr><td colspan="2">&nbsp;</td><td colspan="6">No Delivery Pass</td></tr>
	    		<% } %>
	    	<% } %>
	    	<% if(eligibleList.contains("nct_dp")||eligibleList.contains("nct_ndp")){ %>
	    	<tr><td colspan="2">&nbsp;</td><td colspan="6">Non Chef's Table</td></tr>
	    		<%if(eligibleList.contains("nct_dp")){ %>
	    		<tr><td colspan="2">&nbsp;</td><td colspan="6">Delivery Pass</td></tr>
	    	   	<% } if(eligibleList.contains("nct_ndp")){ %>
	    		<tr><td colspan="2">&nbsp;</td><td colspan="6">No Delivery Pass</td></tr>
	    		<% } %>
	    	<% } %>
	    <% } %>
	 <%} else { %>
	 <tr><td colspan="8"><input type="radio" name="eligibleCustType" id="eligibleCustType" value="everyone" <%= eligibleList.contains("everyone")?"checked":" " %> onclick="checkEligibleCustomers()"> Everyone<br/></input></td></tr>
	 <tr><td colspan="8"><input type="radio" name="eligibleCustType" id="eligibleCustType" value="selected" <%= !eligibleList.contains("everyone")?"checked":" " %> onclick="checkEligibleCustomers()">Selected Segments<br/></input></td></tr>
	 <tr><td colspan="8">
	 <%  if(!eligibleList.contains("everyone")){ %>
	 <div id="segments" >
	 
	 <table>
	<tr>
	 	<td colspan="1" ><input type="checkbox" name="chefsTable" id="chefsTable" <%= (eligibleList.contains("ct_dp")|| eligibleList.contains("ct_ndp"))?"checked":" " %>/>Chef's Table</td>
	 	<td colspan="6" ><input type="checkbox" name="nonChefsTable" id="nonChefsTable" <%= (eligibleList.contains("nct_dp")|| eligibleList.contains("nct_ndp"))?"checked":" " %>/>Non Chef's Table</td>
	 </tr>
	 <tr>
	 	<td colspan="1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="ct_dp" id="ct_dp" <%= eligibleList.contains("ct_dp")?"checked":" " %>/>Delivery Pass</td>
	 	<td colspan="6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="nct_dp" id="nct_dp" <%= eligibleList.contains("nct_dp")?"checked":" " %>/>Delivery Pass</td>
	 </tr> 
	 <tr>	 	
	 	<td colspan="1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="ct_ndp" id="ct_ndp" <%= eligibleList.contains("ct_ndp")?"checked":" " %>/>Non Delivery Pass</td>	
	 	<td colspan="6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="nct_ndp" id="nct_ndp" <%= eligibleList.contains("nct_ndp")?"checked":" " %>/>Non Delivery Pass</td>
	 </tr> 
	 </table>
	 </div>
	 <% } %>
	 </td></tr>
	 <% } %>
	 <tr><td colspan="8"><br/></td></tr>
	 <tr><td align="center" style="background-color: #000000;" colspan="8"></td></tr>
	 <tr><td colspan="8" class="click2call_bold">Check if next day timeslots are sold out:<br/></td></tr>
	 <% if(false==edit){ %>
	 <tr><td >&nbsp;</td><td colspan="7" align="left"><%= click2CallModel.isNextDayTimeSlot()?"Yes":"No" %><br/></td></tr>
	 
	 <% }else { %>
	 <tr><td colspan="8"><input type="radio" name="nextDayTimeSlot" id="nextDayTimeSlot" value="no" <%= click2CallModel.isNextDayTimeSlot()?"checked":" " %>>No<br/></input></td></tr>
	 <tr><td colspan="8"><input type="radio" name="nextDayTimeSlot" id="nextDayTimeSlot" value="yes" <%= !click2CallModel.isNextDayTimeSlot()?"checked":" " %>>Yes<br/></input></td></tr>
	 <% } %>
	 <tr><td colspan="8" >&nbsp;</td></tr>
	</table>
	
	
</div>

</form>
</crm:CrmClick2CallController>
</tmpl:put>
</tmpl:insert>