 /* === Add/Remove functionality between two listboxes ======================= */
  	  /*
       *	remove selected
       *
       *	if a second id is passed, then add the removed item back into it,
       *	otherwise discard.
       */
      	function remOpt(remFromId, addBackToId) {
      		var remFromId = remFromId || null;
      		var addBackToId = addBackToId || null;
      		var remFrom = document.getElementById(remFromId);
      		var addBackTo = document.getElementById(addBackToId);

      		if ((remFromId == null || '') || (remFrom == null)) { return false; }


      		for (var i = remFrom.length - 1; i>=0; i--) {
      			if (remFrom.options[i].selected) {
      				//check to see if we need to add back
      				if (addBackTo != null) {
      					addOpt(addBackTo.id, remFrom.options[i].text, remFrom.options[i].value);
      					remFrom.remove(i);
      				}else{
      					remFrom.remove(i);
      				}
      			}
      		}

      		return true;
      	}

      /*
       *	add option
       *
       *	if text/value is not passed, uses ''
       */
      	function addOpt(addToId, addText, addVal) {
      		var addToId = addToId || null;
      		var addTo = document.getElementById(addToId);
      		var addText = addText || '';
      		var addVal = addVal || '';

      		if ((addToId == null || '') || (addTo == null)) { return false; }

      		try {
      			addTo.add(new Option(addText, addVal), null); //doesn't work in IE
      		}catch(ex) {
      			addTo.add(new Option(addText, addVal)); //IE only
      		}

      		return true;
      	}


        function $(element) {
    	   if (arguments.length > 1) {
    	     for (var i = 0, elements = [], length = arguments.length; i < length; i++)
    	       elements.push($(arguments[i]));
    	     return elements;
    	   }
    	   element = document.getElementById(element);
    	   return element;
    	}
        
        function getValues(elemId) {
            var elem = $(elemId);
            var csv = '';
            for (var i = 0; i < elem.options.length; i++) {
                if (elem.options[i].value!='') {
                    csv += elem.options[i].value;
                    (i<elem.options.length-1) ? csv += ',' : csv +='';
                }
            }
            return csv;
        }
        
        /*
        *     sort option
        *            pass a second param as boolean to sort as Integers (false by default)
        *            
        *            Note: passing true to sort by numbers on mixed (String/Int) options, will NOT sort properly
        */
         function sortByText(sortId, asIntVar, selElement) {
                var specId = '#'+sortId || '';
                var asInt = asIntVar || false;
                var selElemId = selElement || '';
			
      			//alphabetize
      			var selectArr = new Array();

      			if (specId!='') {
      				selectArr[0] = $(specId);
      			} else {
      				selectArr = document.getElementsByTagName('select'); 
      			}

      			for (var i = 0; i < selectArr.length; i++) { 
      				var oArr = new Array();
      				// Get the options for the select element 
      				for (var j = 0; j < selectArr[i].options.length; j++) { 
      					// Store this as an object that has an option object member, and a toString function (which will be used for sorting) 
      					if (selectArr[i].options[j].text != "") { //ignore blanks
      						oArr[oArr.length] = { 
      							option : selectArr[i].options[j], 
      							toString : function() { 
      								// Return the text of the option, not the value 
      								return this.option.text; 
      							}
      						}
      					}
      				} 
                    // Sort the array of options for this select
                    if (asInt) {
                       oArr.sort(function(a,b){return a-b});
	                } else {
	                   oArr.sort();
	                }

      					

      				// Remove all options from the select
      				selectArr[i].options.length = 0;
      				
      				// Rebuild the select using our sorted array
      				for (var j = 0; j < oArr.length; j++) {
      					if(oArr[j].option.value == selElemId){
      						oArr[j].option.selected = true;
      					}
      					selectArr[i].options[j] = oArr[j].option;
      				}
      				if(selElemId === ''){
      					selectArr[i].selectedIndex = 0;
      				}
      			}

      			return true;
      	}
         
        	function remOpt1(remFromId, addBackToId) {
          		var remFromId = remFromId || null;
          		var addBackToId = addBackToId || null;
          		var remFrom = document.getElementById(remFromId);
          		var addBackTo = document.getElementById(addBackToId);

          		if ((remFromId == null || '') || (remFrom == null)) { return false; }
          		
          		var inputText = remFrom.value;
          		if (inputText.indexOf('-') > 0) {
          			var rangeVals = inputText.split('-');
              		if (rangeVals.length > 1) {
              		  for (var i = rangeVals[0]; i <= rangeVals[1]; i++) {
              			addOpt(addBackTo.id, i, i);          		   
              		  }
              		}
          		} else {
          			addOpt(addBackTo.id, remFrom.value, remFrom.value);  
          		}    		
          		remFrom.value = '';
          		return true;
          	}
        	
        	function remOpt2(remFromId) {
          		var remFromId = remFromId || null;          		
          		var remFrom = document.getElementById(remFromId);          		

          		if ((remFromId == null || '') || (remFrom == null)) { return false; }


          		for (var i = remFrom.length - 1; i>=0; i--) {
          			if (remFrom.options[i].selected) {
          				remFrom.remove(i);
          			}
          		}

          		return true;
          	}
