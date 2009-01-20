
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}

function time(time_string) {

		var ampm='a';
		var hour=-1;
		var minute=0;
		var temptime='';
		time_string = time_string.trim();
		var ampmPatEnd = (/AM|PM$/);
		if(time_string.length == 8 && time_string.match(ampmPatEnd)) {
			return time_string;
		}
	for (var n =0; n < time_string.length; n++) {

		var ampmPat = (/a|p|A|P/);
		if (time_string.charAt(n).match(ampmPat)) {
			ampm = time_string.charAt(n);
			break;
		}else{
			ampm = 'a';
		}
		
		var digPat = (/\d/);
		if (time_string.charAt(n).match(digPat)) {
			temptime += time_string.charAt(n);
		}
	}
	if (temptime.length > 0 && temptime.length <= 2) {
		hour = temptime;
		minute = 0;
	}else if (temptime.length == 3 || temptime.length == 4) {
		if (temptime.length == 3) {
			hour=time_string.charAt(0);
			minute=time_string.charAt(1);
			minute+=time_string.charAt(2);
		}else{
			hour=time_string.charAt(0);
			hour+=time_string.charAt(1);
			minute=time_string.charAt(2);
			minute+=time_string.charAt(3);
		}
	} else {
		return '';
	}

	if ((hour <= 12) && (minute <= 59)) {
		
		if (hour.toString().length == 1) {
			hour = '0'+hour;
		}
		
		if (minute.toString().length == 1) {
			minute = '0'+minute;
		}
		if (hour == '00' ) { ampm='a' }
		//if (hour == '12' ) { ampm='p' }
		temptime = hour + ':' + minute + ' ' + ampm.toUpperCase()+'M';
	}else{
		temptime = '';
	}



	return temptime

 }