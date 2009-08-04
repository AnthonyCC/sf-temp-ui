// Adds onmouseover, onmouseout, and onclick handlers to each table row.  The onmouseover handler changes the row's class attribute to
// rowMouseOver.  The onmouseout handler changes it back.  The onclick function makes a request for the specified url, including the
// innerHTML of the specified column as a request parameter.
function addRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol) {
   addHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, false);
}

function addHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
	
	var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(checkCol == -1 || checkCol != j ) {
					if(!(needKeyPress && (j == (cells.length-1)))) {	            
				    	cells[j].onclick = function () {			    		
				      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
				      		var selectBox = this.parentNode.getElementsByTagName("input")[0];
				      		
				      		/*if(needKeyPress) {
					      		var param1 = this.parentNode.getElementsByTagName("td")[1];			      		
					      		var param2 = this.parentNode.getElementsByTagName("td")[5];
					      		var param3 = this.parentNode.getElementsByTagName("td")[7];	
					      		var param4 = this.parentNode.getElementsByTagName("td")[8];
					      		javascript:pop('common/geography.jsp?address='+param1.innerHTML+' '
					      							+param2.innerHTML+'&latitude='+param3.innerHTML+'&longitude='+param4.innerHTML, 600,500);			      		
					      	}*/
				      		location.href = url+"?"+ paramName + "=" + selectBox.name;			      		
				    	};
				    }
		    	}
		    	
		    		    	
	        }
	    }
	}
}

function addRowHandlersFilter(tableId, rowClassName, url, paramName, columnIndex, checkCol) {
   addHandlersFilter(tableId, rowClassName, url, paramName, columnIndex, checkCol, false);
}

function addHandlersFilter(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
	
	var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(checkCol == -1 || checkCol != j ) {
					if(!(needKeyPress && (j == (cells.length-1)))) {	            
				    	cells[j].onclick = function () {			    		
				      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
				      		var selectBox = this.parentNode.getElementsByTagName("input")[0];
				      		
				      		/*if(needKeyPress) {
					      		var param1 = this.parentNode.getElementsByTagName("td")[1];			      		
					      		var param2 = this.parentNode.getElementsByTagName("td")[5];
					      		var param3 = this.parentNode.getElementsByTagName("td")[7];	
					      		var param4 = this.parentNode.getElementsByTagName("td")[8];
					      		javascript:pop('common/geography.jsp?address='+param1.innerHTML+' '
					      							+param2.innerHTML+'&latitude='+param3.innerHTML+'&longitude='+param4.innerHTML, 600,500);			      		
					      	}*/
				      		location.href = url+"?"+ paramName + "=" + selectBox.name+"&filter="+getPlanFilterValue();			      		
				    	};
				    }
		    	}
		    	
		    		    	
	        }
	    }
	}
}

function getPlanFilterValue()
{
	var filters=getFilterValue(document.getElementById("planListForm"),false);
	filters+="&daterange="+document.getElementById("daterange").value;
	return escape(filters)
}
// Adds onmouseover, onmouseout, and onclick handlers to each table row.  The onmouseover handler changes the row's class attribute to
// rowMouseOver.  The onmouseout handler changes it back.  The onclick function makes a request for the specified url, including the
// innerHTML of the specified column as a request parameter.
function addMultiRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, requestParam) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(checkCol == -1 || checkCol != j) {
			    	cells[j].onclick = function () {
			      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
			      		var selectBox = this.parentNode.getElementsByTagName("input")[0];			      		
			      		location.href = url+"?"+ paramName + "=" + selectBox.name+"&"
			      			+document.getElementById(requestParam).name + "=" +document.getElementById(requestParam).value;
			    	};
		    	}
	        }
	    }
	}
}
function addMultiRowHandlersColumn(tableId, rowClassName, url, paramName, columnIndex, checkCol, requestParam) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(j>checkCol) {
			    	cells[j].onclick = function () {
			      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
			      		var selectBox = this.parentNode.getElementsByTagName("input")[0];			      		
			      		location.href = url+"?"+ paramName + "=" + selectBox.name+"&"
			      			+document.getElementById(requestParam).name + "=" +document.getElementById(requestParam).value;
			    	};
		    	}
	        }
	    }
	}
}

function addMultiRowHandlersColumnFilter(tableId, rowClassName, url, paramName, columnIndex, checkCol, requestParam) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(j>checkCol) {
			    	cells[j].onclick = function () 
			    	{
			      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
			      		var selectBox = this.parentNode.getElementsByTagName("input")[0];			      					      		
			      		location.href = url+"?"+ paramName + "=" + selectBox.name+"&"
			      		+document.getElementById(requestParam).name + "=" +document.getElementById(requestParam).value
			      		+"&filter="+getDispatchFilterValue();			      		
			    	};
		    	}
	        }
	    }
	}
}

function getDispatchFilterValue()
{
	var filters=getFilterValue(document.getElementById("ec"),false);
	filters+="&dispDate="+document.getElementById("dispDate").value;
	return escape(filters)
}

// Adds onmouseover, onmouseout, and onclick handlers to each table row.  The onmouseover handler changes the row's class attribute to
// rowMouseOver.  The onmouseout handler changes it back.  The onclick function makes a request for the specified url, including the
// innerHTML of the specified column as a request parameter.
function addCompositeRowHandlers(tableId, rowClassName, url, paramName1, columnIndex1, paramName2, columnIndex2) {
    var previousClass = null;
    var table = document.getElementById(tableId);
    
    if(table != null) {
	    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
	    for (i = 0; i < rows.length; i++) {	    	
	        var cells = rows[i].getElementsByTagName("td");
	        
	        for (j = 1; j < cells.length; j++) {
	        	
	            cells[j].onmouseover = function () {
	            	previousClass = this.parentNode.className;
	            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
	            };
	        
	            cells[j].onmouseout = function () {
	              	this.parentNode.className = previousClass;
	            };
	        
	            if(j != 0) {
			    	cells[j].onclick = function () {			      		
			      		var param1 = this.parentNode.getElementsByTagName("td")[columnIndex1];
			      		var param2 = this.parentNode.getElementsByTagName("td")[columnIndex2];			      				      		
			      		location.href = url+"?"+ paramName1 + "=" + param1.innerHTML+"&"
			      			+paramName2 + "=" +param2.innerHTML;
			      		
			    	};
		    	}
	        }
	    }
	}
}

 var isIE = !!(window.attachEvent && !window.opera);

/* currently being used for help section...*/
function pop(URL,h,w) {
    
	if(isIE){
		if (window.newWin) { window.newWin.close() }
	}else{
        //for Netscape	
		if(window.newWin){
			if(window.newWin.closed!=true){
			    window.newWin.close();
			}
		}
	}
	specs = "HEIGHT=" + h + ",WIDTH=" + w + ",resizable,scrollbars";
	//var newWin =  window.open(URL,"newWin",specs);
	newWin =  window.open(URL,"newWin",specs);
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}

function modalWin(URL,h,w) {
    
	if (window.showModalDialog) {
		specs = "dialogHeight=" + h + ";dialogWidth:" + w ;
		window.showModalDialog(URL,"modalWin", specs);
	} else {
		specs = "HEIGHT=" + h + ",WIDTH=" + w + ",toolbar=no,directories=no,status=no,continued from previous linemenubar=no,scrollbars=no,resizable=no ,modal=yes";
		window.open(URL,'newWin',specs);
	}
}


