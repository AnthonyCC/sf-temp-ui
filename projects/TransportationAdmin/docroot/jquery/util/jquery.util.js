(function($) {

	$.fn.serializeObject = function()
	{
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};

	
	$.fn.emptySelect = function() {
		 return this.each(function(){
			if (this.tagName=='SELECT') this.options.length = 0;
		 });
	}

	$.fn.loadSelect = function(optionsDataArray, asIntVar, includeSelect) {
		  	var asInt = asIntVar || false;
		  	var asIntSel = includeSelect || false;
	    	
		    return this.emptySelect().each(function(){
		      if (this.tagName=='SELECT') {
		    	
		        var selectElement = this;
		        if(asIntSel) {
		        	selectElement.add(new Option('--Please select', ""));
		        }
		        $.each(optionsDataArray,function(index,optionData){
		          var option = new Option(optionData.caption, optionData.value);
		          if ($.browser.msie) {
		            selectElement.add(option);
		          }
		          else {
		            selectElement.add(option,null);
		          }
		        });
		        
		        if(asInt) {
		        	$(this).html($("option", $(this)).sort(function(a, b) {
		           		return a - b;
			        }));	
		        } else {
			        // Sort all the options by text. 
			        $(this).html($("option", $(this)).sort(function(a, b) {
		           		return a.text == b.text ? 0 : a.text < b.text ? -1 : 1;
			        }));
		        }
		      }
		    });
	}

	 $.fn.formatDate = function(timeVal) {
				var timeInt = timeVal || 0;
				var time = '';
				var timeObj;
				if (timeInt > 0) {
					//offset already applied to timeVal, put it back
					//be sure to init the date object here, DLST will affect the calc.
					timeInt = timeInt + (new Date(timeInt).getTimezoneOffset() * 60 * 1000);
					timeObj = new Date(timeInt);
				} else {
					timeObj = new Date();
				}
				var mm = timeObj.getMonth() + 1;
				time += (mm < 10) ? '0'+mm : mm;
				
				time += '/';	
				
				var dd = timeObj.getDate();
				time += (dd < 10) ? '0'+dd : dd;
				
				time += '/';	
				
				var yyyy = timeObj.getFullYear();
					time += yyyy;
			return time;
	}
	 
	$.fn.clearForm = function() {
		  return this.each(function() {
		    var type = this.type, tag = this.tagName.toLowerCase();
		    if (tag == 'form')
		      return $(':input',this).clearForm();
		    if (type == 'text' || type == 'password' || tag == 'textarea')
		      this.value = '';
		    else if (type == 'checkbox' || type == 'radio')
		      this.checked = false;
		    else if (tag == 'select')
		      this.selectedIndex = -1;
		  });
	};
	


})(jQuery);

function sortDropDownListByText() {
	    // Loop for each select element on the page.
	    $("select").each(function() {
	         
	     // Keep track of the selected option.
	     var selectedValue = $(this).val();
	 
        // Sort all the options by text. I could easily sort these by val.
	        $(this).html($("option", $(this)).sort(function(a, b) {
           		return a.text == b.text ? 0 : a.text < b.text ? -1 : 1
	        }));
	       
	        
	        // Select one option.
	        $(this).val(selectedValue);
	    });
}