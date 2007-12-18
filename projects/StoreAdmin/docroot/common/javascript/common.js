<!--

function swapImage(imgName,imgURL){
	if (imgURL.length > 0) {
    		document.images[imgName].src = imgURL;
    	}
}

function jumpTo(locationURL) {
	if (locationURL==null) return false;
	window.location=locationURL;
}

function toggleDisplay(id) {
	if (document.all){
   		if(document.all[id].style.display == 'none'){
       		document.all[id].style.display = '';
    	} else {
      	document.all[id].style.display = 'none';
    	}
  return false;

   } else if (document.getElementById) {
     	if(document.getElementById(id).style.display == 'none'){
       		document.getElementById(id).style.display = 'block';
     	} else {
       		document.getElementById(id).style.display = 'none';
     	}
  return false;
   }
}

var isIE;
if(document.all){isIE=true}
function preview(url) {
var forceRefresh = "&test=" + Math.round(Math.random()*10) + Math.round(Math.random()*10) + Math.round(Math.random()*10) + Math.round(Math.random()*10);
url += forceRefresh;
	if (window.previewWin) {
         	window.previewWin.location=url;
	} else {
	    specs = "width=765,height=655,menubar=yes,location=yes,status=yes,toolbar=yes,resizable=yes,scrollbars=yes";
    	previewWin =  window.open(url,"previewWin",specs);
    	if (previewWin.opener == null) previewWin.opener = self;
    	previewWin.focus();
	}

}
function pop(URL,w,h) {
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
	specs = "width=" + w + ",height=" + h + ",resizeable=yes,scrollbars=yes,top=0,left=180";
	newWin =  window.open(URL,"newWin",specs);
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}

function popup(URL,type) {
	if ("s" == type) {
		w = "600";
		h = "400";
	} else if ("l" == type) {
		w = "800";
		h = "500";
	}
	pop(URL,w,h);
}

function doConfirm(msg) {
         if( confirm('Are you sure you want to\n'+msg)) {
            return true;
        }
        return false;
}

function deleteThis(type,URL) {
	if (confirm("Are you sure you want to delete this " + type + "?")) {
		jumpTo(URL);
		return true;
		}
		jumpTo(URL);
		return false;
	}

//-->