/***
 * Contains basic SlickGrid formatters.
 * @module Formatters
 * @namespace Slick
 */

(function ($) {
  // register namespace
  $.extend(true, window, {
    "Slick": {
      "Formatters": {
        "PercentComplete": PercentCompleteFormatter,
        "PercentCompleteBar": PercentCompleteBarFormatter,
        "YesNo": YesNoFormatter,
        "Checkmark": CheckmarkFormatter,
		"Date": DateFormatter,
		"DateTime": DateTimeFormatter,
		"Time": TimeFormatter
      }
    }
  });

  function PercentCompleteFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "") {
      return "-";
    } else if (value < 50) {
      return "<span style='color:red;font-weight:bold;'>" + value + "%</span>";
    } else {
      return "<span style='color:green'>" + value + "%</span>";
    }
  }

  function PercentCompleteBarFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "") {
      return "";
    }

    var color;

    if (value < 30) {
      color = "red";
    } else if (value < 70) {
      color = "silver";
    } else {
      color = "green";
    }

    return "<span class='percent-complete-bar' style='background:" + color + ";width:" + value + "%'></span>";
  }

  function YesNoFormatter(row, cell, value, columnDef, dataContext) {
    return value ? "Yes" : "No";
  }

  function CheckmarkFormatter(row, cell, value, columnDef, dataContext) {
    return value ? "<img src='./images/tick.png'/>" : "";
  }
  
  function DateFormatter(row, cell, timeVal, columnDef, dataContext) {
 
	  	var timeInt = timeVal || 0;
		var time = '';
		if(timeVal != null) {
			if (timeInt > 0) {
				//offset already applied to timeVal, put it back
				//be sure to init the date object here, DLST will affect the calc.
				timeInt = timeInt + (new Date(timeInt).getTimezoneOffset() * 60 * 1000);
			}
			var timeObj = new Date(timeInt);
			
			var mm = timeObj.getMonth() + 1;
			time += (mm < 10) ? '0'+mm : mm;
			
			time += '/';	
			
			var dd = timeObj.getDate();
			time += (dd < 10) ? '0'+dd : dd;
			
			time += '/';	
			
			var yyyy = timeObj.getFullYear();
				time += yyyy;
		}	
		return time;
		 
  }
  
  function timeFormator(timeVal, includeSec) {
		
	var timeInt = timeVal || 0;
	var time = '';
	if(timeVal != null) {
		if (timeInt > 0) {
			//offset already applied to timeVal, put it back
			//be sure to init the date object here, DLST will affect the calc.
			timeInt = timeInt + (new Date(timeInt).getTimezoneOffset() * 60 * 1000);
		}
		var timeObj = new Date(timeInt);
		
		var yyyy = timeObj.getFullYear();
			time += yyyy;
		
		time += '-';	
		
		var mm = timeObj.getMonth() + 1();
			time += (mm < 10) ? '0'+mm : mm;
		
		time += '-';	
		
		var dd = date.getDate();
			time += (dd < 10) ? '0'+dd : dd;
				
		var hours = timeObj.getHours();
			time += (hours < 10) ? '0'+hours : hours;
		
		time += ':';
		
		var mins = timeObj.getMinutes();
			time += (mins < 10) ? '0'+mins : mins;
		
		time += ':';
		
		if(includeSec) {
			var secs = timeObj.getSeconds();
				time += (secs < 10) ? '0'+secs : secs;
		}
	}
	return time;
 }
  

	  function DateTimeFormatter(row, cell, value, columnDef, dataContext) {
		var date = '';
		if (value != 'undefined' && value != null) {
				var match;
				if (!(match = value.toString().match(/\d+/))) {
					return false;
				}
				date = new Date();
				date.setTime(match[0] - 0).toString();
			
			var mm = date.getMonth() + 1;
			mm = (mm < 10) ? '0' + mm : mm;
			var dd = date.getDate();
			dd = (dd < 10) ? '0' + dd : dd;
			var yyyy = date.getFullYear();
			
			date = mm + '/' + dd + '/' + yyyy + ' ' + formatAMPM(date);
			
		}
		return date;

	}
  
  function TimeFormatter(row, cell, value, columnDef, dataContext) {
		var date;
		if (value != 'undefined' && value != null) {
			var match;
			if (!(match = value.toString().match(/\d+/))) {
				return false;
			}
			date = new Date();
			date.setTime(match[0] - 0).toString();
			return formatAMPM(date);
		}
		return date;		 
  }
  
  function formatAMPM(date) {
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	  var ampm = hours >= 12 ? 'PM' : 'AM';
	  hours = hours % 12;
	  hours = hours ? hours : 12; // the hour '0' should be '12'
	  hours = hours < 10 ? '0'+hours : hours;
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var strTime = hours + ':' + minutes + ' ' + ampm;
	  return strTime;
 }
  
 function myFormatter(row, cell, value, columnDef, dataContext) {
	  return "<a href='#'>Click</a>";
 }
})(jQuery);