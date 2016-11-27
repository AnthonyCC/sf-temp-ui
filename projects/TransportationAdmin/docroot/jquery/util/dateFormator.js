 //Date formattor plugin
 

//replace YYYY-MM-DD(Mysql default date format) in user specified custom date format
//usage example $('JQUERY_SELECTOR').dateFormator('M-ddd-yyyy');
//dependency jquery.js download from jquery.com
//dependency date.js download from http://code.google.com/p/datejs/downloads/detail?name=date.js&can=2&q=
//for allowed date format specifier see below
/*
 Format Description Example
 ------ --------------------------------------------------------------------------- -----------------------
 s The seconds of the minute between 0-59. "0" to "59"
 ss The seconds of the minute with leading zero if required. "00" to "59"

 m The minute of the hour between 0-59. "0" or "59"
 mm The minute of the hour with leading zero if required. "00" or "59"

 h The hour of the day between 1-12. "1" to "12"
 hh The hour of the day with leading zero if required. "01" to "12"

 H The hour of the day between 0-23. "0" to "23"
 HH The hour of the day with leading zero if required. "00" to "23"

 d The day of the month between 1 and 31. "1" to "31"
 dd The day of the month with leading zero if required. "01" to "31"
 ddd Abbreviated day name. Date.CultureInfo.abbreviatedDayNames. "Mon" to "Sun"
 dddd The full day name. Date.CultureInfo.dayNames. "Monday" to "Sunday"

 M The month of the year between 1-12. "1" to "12"
 MM The month of the year with leading zero if required. "01" to "12"
 MMM Abbreviated month name. Date.CultureInfo.abbreviatedMonthNames. "Jan" to "Dec"
 MMMM The full month name. Date.CultureInfo.monthNames. "January" to "December"

 yy The year as a two-digit number. "99" or "08"
 yyyy The full four digit year. "1999" or "2008"

 t Displays the first character of the A.M./P.M. designator. "A" or "P"
 $C.amDesignator or Date.CultureInfo.pmDesignator
 tt Displays the A.M./P.M. designator. "AM" or "PM"
 $C.amDesignator or Date.CultureInfo.pmDesignator

 S The ordinal suffix ("st, "nd", "rd" or "th") of the current day. "st, "nd", "rd" or "th"
 */

(function($) {

	// Attach this new method to jQuery
	$.fn.extend({

		// This is plugin
		dateFormator : function(dateformat) {

			// Iterate over the current set of matched elements
			return this.each(function() {
				var result = '';
				var newresult = '';
				var dt = '';
				// pattern to be match here
				var patt = /(\d{4}-\d{2}-\d{2})\b/gm; // replace match in
														// yyyy-MM-dd format
				var str = $(this).html();
				// newresult=str;
				result = str.match(patt);
				if (result != null) {
					if (result.length == 1) {
						dt = Date.parseExact(result, "yyyy-MM-dd");
						str = str.replace(eval('/' + result + '/gi'), dt
								.toString(dateformat));
					} else {
						for (i = 0; i < result.length; i++) {
							dt = Date.parseExact(result[i], "yyyy-MM-dd");
							// alert(result[i]+'----'+dt.print(dateformat));

							str = str.replace(eval('/' + result[i] + '/gi'), dt
									.toString(dateformat));
						}
					}
				}
				// alert(str);
				$(this).html(str);
			});
		}
	});

	// pass jQuery to the function,

})(jQuery);