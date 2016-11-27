
function convertTo24Hour(time) {
    	    var hours = parseInt(time.substr(0, 2));
    	    if(time.indexOf('AM') != -1 && hours == 12) {
    	        time = time.replace('12', '0');
    	    }
    	    if(time.indexOf('PM')  != -1 && hours < 12) {
    	        time = time.replace(hours, (hours + 12));
    	    }
    	    return time.replace(/(AM|PM)/, '');
}
     
function checkTime(_departtime, _arrivetime) {
    	
    	 var now = new Date();
    	 var departTime = new Date((now.getMonth() + 1) + '/' + (now.getDate()) + '/' + now.getFullYear() + " " + convertTo24Hour(_departtime));
    	
    	 var arrivalTime = new Date((now.getMonth() + 1) + '/' + (now.getDate()) + '/' + now.getFullYear() + " " + convertTo24Hour(_arrivetime));
    	 
    	 if(arrivalTime.getTime() > departTime.getTime()) {
    		 return true;
    	 }
    	 return false;
}