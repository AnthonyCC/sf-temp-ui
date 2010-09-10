function doSelect(tableId, source) {	
	var table = document.getElementById(tableId);
	var chkSelect = document.getElementById(source);
    var checkboxList = table.getElementsByTagName("input");    
    var paramValues = null;
    for (i = 0; i < checkboxList.length; i++) {
    	if (checkboxList[i].type=="checkbox" && checkboxList[i] != chkSelect) {
    		
    		checkboxList[i].checked = chkSelect.checked;
    	}
    }

}

function getParamList(tableId, url) {
	
	var table = document.getElementById(tableId);
    var checkboxList = table.getElementsByTagName("input");    
    var paramValues = null;
    for (i = 0; i < checkboxList.length; i++) {
    	if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
    		
    		if (paramValues != null) {
    			paramValues = paramValues+","+checkboxList[i].name;
    		} else {
    			paramValues = checkboxList[i].name;
    		}
    	}
    }
    return paramValues;
}

function doAddNew(tableId, url) {
  	location.href = url+"?filter="+getFilterTestValue();  	
}

function doDelete(tableId, url) {
    
    var paramValues = getParamList(tableId, url);
    if (paramValues != null) {
    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
		if (hasConfirmed) {
			location.href = url+"?id="+ paramValues;
		} 
    } else {
    	alert('Please Select a Row!');
    }
}

function getFilterTestValue()
{
	var filters = "";
 	return escape(filters);
}

function doGeocode(tableId, url) {
    var paramValues = getParamList(tableId, url);
    if (paramValues != null) {
    	var hasConfirmed = confirm ("Do you want to re-geocode the selected records?")
		if (hasConfirmed) {
		  	location.href = url+"?id="+ paramValues;
		} 
    } else {
    	alert('Please Select a Row!');
    }
}
  
function doUpdate(tableId, url) {
    var paramValues = getParamList(tableId, url);
    if (paramValues != null) {
    	var hasConfirmed = confirm ("Do you want to update the selected records?")
		if (hasConfirmed) {
		  	location.href = url+"?id="+ paramValues;
		} 
    } else {
    	alert('Please Select a Row!');
    }
} 

function doSend(tableId, url) {
    var paramValues = getParamList(tableId, url);
    if (paramValues != null) {
    	var hasConfirmed = confirm ("Do you want to send the selected records?")
		if (hasConfirmed) {
		  	location.href = url+"?id="+ paramValues;
		} 
    } else {
    	alert('Please Select a Row!');
    }
} 
   
  
  function doLink(compId, url) {
    var param = document.getElementById(compId).value;
	if (url.indexOf("?") == -1) {
		location.href = url+"?"+compId+"="+ param;
	}else{
		location.href = url+"&"+compId+"="+ param;
	}
  }
  
  function doRefresh(timeoutPeriod) {
	  setTimeout("location.reload(true);",timeoutPeriod);
  
  }