// Adds onmouseover, onmouseout, and onclick handlers to each table row.  The onmouseover handler changes the row's class attribute to
// rowMouseOver.  The onmouseout handler changes it back.  The onclick function makes a request for the specified url, including the
// innerHTML of the specified column as a request parameter.
function addRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol) {
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
			      		//var selectBox = this.parentNode.getElementsByTagName("input")[0];			      		
			      		location.href = url+"?"+ paramName + "=" + cell.innerHtml;
			    	};
		    	}
	        }
	    }
	}
}


function editRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol) {
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
			      		//var selectBox = this.parentNode.getElementsByTagName("input")[0];			      		
			      		location.href = url+"?"+ paramName + "=" + cell.innerHtml;
			    	};
		    	}
	        }
	    }
	}
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


