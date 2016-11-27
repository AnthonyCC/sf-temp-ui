function doDelete(tableId, url) {
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
    if (paramValues != null) {
    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
		if (hasConfirmed) {
		  	location.href = url+"?id="+ paramValues;
		} 
    } else {
    	alert('Please Select a Row!');
    }
  } 
  
  function doLink(compId, url) {
    var param = document.getElementById(compId).value;
    location.href = url+"?"+compId+"="+ param;
  }      