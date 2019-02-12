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
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'> Click to Call</tmpl:put>

<tmpl:put name='content' direct='true'>
<style type="text/css">
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}
</style>
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
	   len = selectedList.length -1;
	   for(i=len; i>=0; i--){
		   selectedList.item(i).selected="selected";
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
	    len = selectedList.length -1;
		   for(i=len; i>=0; i--){
			   selectedList.item(i).selected="selected";
		   }
	   //if(addIndex < 0)
	    //  return;
	   //selectedList.appendChild( 
	     // availableList.options.item(addIndex));
	   selectNone(selectedList,availableList);
	 //  setSize(selectedList,availableList);
	   sortItems();
}

function selectZones(){
	var selectedList = document.getElementById("selectedZones");
	var len = selectedList.length -1;
	for(i=len; i>=0; i--){
		   selectedList.item(i).selected="selected";
	}
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

</tmpl:put>
</tmpl:insert>