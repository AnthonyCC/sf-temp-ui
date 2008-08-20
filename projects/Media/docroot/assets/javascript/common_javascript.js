<!--    //--------- Common JavaScript functions  ------------------

var id= "";
var css="";

function soon() { alert("Coming soon"); }

//peffered rollover swap technique..
function swapImage(imgName,imgURL){
	if (imgURL.length>0) {
    		document.images[imgName].src = imgURL;
    	}
}

//rollover swap with width check
function swapImage2(imgName,imgURL, w, h){
	var noW;

	(w) ? noW = true : noW = false;
	var w = w || document.images[imgName].width;
	var h = h || document.images[imgName].height;

	if (imgURL.length) {
    		while( !swapImage2sup(imgName, w, noW) ){ document.images[imgName].src = 'clear.gif'; }
			document.images[imgName].src = imgURL;
    	}
}
//swapImage2 support function
function swapImage2sup(imgName, w, noW){
	if (w > 90 && noW)	{
		document.images[imgName].width = '90';
	}else{
		document.images[imgName].width = w;
	}
	return true;
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

function popResize(URL,h,w,name) {
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
	specs = "HEIGHT=" + h + ",WIDTH=" + w + ",resizable,scrollbars,top=0,screenY=0";
	newWin =  window.open(URL,name,specs);
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}

function backtoWin(url) {
    parent.window.opener.location = url ;
    parent.window.opener.focus();
}

//sets the value of hidden field named "actionName"
function setActionName(frmObj,actionText) {
    if (frmObj["actionName"]!=null) {
        frmObj["actionName"].value=actionText;
    }
    return true;
}

//sets the value of the hidden field named deletePaymentId
function setDeletePaymentId(frmObj,payid) {
    if (frmObj["deletePaymentId"]!=null) {
        frmObj.deletePaymentId.value=payid;			
    }
    return true;
}

function popup(URL,type) {
	if ("small" == type) {
		w = "375";
		h = "335";
	} else if ("large" == type) {
		w = "585";
		h = "400";	
	} else if ("large_long" == type) {
		w = "585";
		h = "600";
	} else if ("minimal" == type) {
		w = "585";
		h = "600";
	} else if ("print" == type) {
		w = "650";
		h = "700";
	}
	pop(URL,h,w);
}


function IsNumericNoDecimal(sText){
	   var numericChars = "0123456789";
	   var IsNumber=true;
	   var Char;


	   for (i = 0; i < sText.length && IsNumber == true; i++) 
		  { 
		  Char = sText.charAt(i); 
		  if (numericChars.indexOf(Char) == -1) 
			 {
			 IsNumber = false;
			 }
		  }
	   return IsNumber;

}
function expandcollapse (postid) { 

   whichpost = document.getElementById(postid); 
   if (whichpost.className=="postshown") { 
      whichpost.className="posthidden"; 
   } 
   else { 
      whichpost.className="postshown"; 
   } 
}

function changeColors(currentId, currentCss){
  if(id=="") id= currentId;
  if(css=="") css="current";
  document.getElementById(id).className=css;
  id=currentId;
  css=currentCss;
  document.getElementById(currentId).className="case_selected_header";
}

// -->
 
 
 /*****************************************************************/

/**
 * Support for document loaded event
 * @author segabor
 * 
 * This snippet is based on a part of Prototype source code
 * See: http://prototypejs.org
 */
(function() {
  /* Support for the DOMContentLoaded event is based on work by Dan Webb,
     Matthias Miller, Dean Edwards and John Resig. */

  var timer, fired = false;

  function fireContentLoadedEvent() {
    if (fired) return;
    if (timer) window.clearInterval(timer);
    /// document.fire("dom:loaded");
	if (typeof(document.onDocumentLoaded) == "function") {
		document.onDocumentLoaded();
	    fired = true;
	}

  }

  if (document.addEventListener) {
    if (navigator.userAgent.indexOf('AppleWebKit/') > -1) {
      timer = window.setInterval(function() {
        if (/loaded|complete/.test(document.readyState))
          fireContentLoadedEvent();
      }, 0);

      /// Event.observe(window, "load", fireContentLoadedEvent);
      if (window.addEventListener) {
        window.addEventListener("onload", fireContentLoadedEvent, false);
      } else {
        window.attachEvent("onload", fireContentLoadedEvent);
      }

    } else {
      document.addEventListener("DOMContentLoaded",
        fireContentLoadedEvent, false);
    }

  } else {
    document.write("<script id=__onDOMContentLoaded defer src=//:><\/script>");
    document.getElementById("__onDOMContentLoaded").onreadystatechange = function() {
      if (this.readyState == "complete") {
        this.onreadystatechange = null;
        fireContentLoadedEvent();
      }
    };
  }
})();

//  End of Common JavaScript Functions       -------->
