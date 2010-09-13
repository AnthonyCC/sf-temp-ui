var id= "";
var css="";

function soon() { alert("Coming soon"); }

//preferred roll-over swap technique..
function swapImage(imgName,imgURL){
	if (imgURL.length>0) {
    		document.images[imgName].src = imgURL;
    	}
}

//roll-over swap with width check
function swapImage2(imgName,imgURL, w, h){
	var noW;

	(w) ? noW = true : noW = false;
	var w = w || document.images[imgName].width;
	var h = h || document.images[imgName].height;

	if (imgURL.length) {
		swapImage2sup(imgName, w, noW);
		document.images[imgName].src = imgURL;
   	}
}
//swapImage2 support function
function swapImage2sup( imgName, w, noW ){
	if ( w > 90 && noW )	{
		document.images[imgName].width = '90';
	} else {
		document.images[imgName].width = w;
	}
	return true;
}

//roll-over swap with width check + burst swap
function swapImageAndBurst( imgName, imgURL, w, h, hasBurst, burstName, burstURL ) {
	
	var noW;
	(w) ? noW = true : noW = false;
	
	var w = w || document.images[imgName].width;
	var h = h || document.images[imgName].height;

	if ( imgURL.length ) {
		swapImage2sup( imgName, w, noW );
		document.images[imgName].src = imgURL;
	}
	
	if ( hasBurst ) {
		document.images[burstName].src = burstURL;		
		document.images[burstName].style.display = "";
	} else {
		document.images[burstName].style.display="none";
	}
}


var isIE = !!(window.attachEvent && !window.opera);

var newWin='';
/* simple pop */
function pop(URL, h, w, name) {
    var name = name || 'popWin';
    newWin = window.open(URL, name, "height=" + h + ", width=" + w + ", resizable, scrollbars");
    newWin.focus();
    if (window.resizeTo) { newWin.resizeTo(w, h); }
} 

/* currently being used for help section...*/
function popold(URL,h,w) {

	if(isIE){
		if (window.newWin) { window.newWin.close(); }
	}else{
        //for Netscape	
		if(window.newWin){
			if(window.newWin.closed!=true){
			    window.newWin.close();
			}
		}
	}
	specs = "HEIGHT=" + h + ",WIDTH=" + w + ",resizable,scrollbars";
	newWin =  window.open(URL,"newWin",specs);
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}

function popResize(URL,h,w,name) {
	if(isIE){
		if (window.newWin) { window.newWin.close(); }
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
function popResizeHelp(URL,h,w,name) {
	
	specs = "HEIGHT=" + h + ",WIDTH=" + w + ",resizable,scrollbars,top=0,screenY=0";
	newWin =  window.open(URL,name,specs);
	
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}
function backtoWin(url) {
    parent.window.opener.location = url ;
    parent.window.opener.focus();
}

function backtoWinPop(url, size) {
    if (self.opener) {
          self.opener.focus();
          self.opener.popup(url, size, 'popInPop');
    }else{
          popup(url, size);
    }
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

function popup(URL, type, name) {
	var w = "375";
	var h = "335";
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
	pop(URL, h, w, name);
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


function updateYourCartPanel() {
	YAHOO.util.Connect.asyncRequest('GET', '/ajax/yc.jsp', {
		success: function(resp) {
			var container = $('your-cart-div');
			if (container) {
				container.innerHTML = resp.responseText;
			}
		}
	});
}

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
/*
 *	sort option
 */
	function sortByText(sortId) {
		var specId = sortId || '';
			
		//alphabetize
		var selectArr = new Array();

		if (specId!='') {
			selectArr[0] = $(specId); 
		}else{
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
			oArr.sort();
				

			// Remove all options from the select
			selectArr[i].options.length = 0;
			
			// Rebuild the select using our sorted array
			for (var j = 0; j < oArr.length; j++) {
				selectArr[i].options[j] = oArr[j].option;
			}
			selectArr[i].selectedIndex = 0;
		}

		return true;

	}
	/*
	 *	takes the select values from sortId, assumes they are days of the week,
	 *	and sorts them by day instead of alphabetically
	 */
	function sortByDayOfWeek(sortId) {
		var specId = sortId || '';

		var selectArr = new Array();

		//name to idx
		var weekdayIdx=new Array(14);
			weekdayIdx["SUNDAY"]   =0;
			weekdayIdx["SUN"]      =0;
			weekdayIdx["MONDAY"]   =1;
			weekdayIdx["MON"]      =1;
			weekdayIdx["TUESDAY"]  =2;
			weekdayIdx["TUE"]      =2;
			weekdayIdx["WEDNESDAY"]=3;
			weekdayIdx["WED"]      =3;
			weekdayIdx["THURSDAY"] =4;
			weekdayIdx["THU"]      =4;
			weekdayIdx["FRIDAY"]   =5;
			weekdayIdx["FRI"]      =5;
			weekdayIdx["SATURDAY"] =6;
			weekdayIdx["SAT"]      =6;

		if (specId!='') {
			selectArr[0] = $(specId); 
		}else{
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
							// look up the text of the option in name-to-index and return it
							return weekdayIdx[(this.option.text).toUpperCase()];
						}
					}
				}
			} 

			// Sort the array of options for this select 
			oArr.sort();

			// Remove all options from the select
			selectArr[i].options.length = 0;
			
			// Rebuild the select using our sorted array
			for (var j = 0; j < oArr.length; j++) {
				selectArr[i].options[j] = oArr[j].option;
			}
			selectArr[i].selectedIndex = 0;
		}

		return true;
	}
/* === Add/Remove functionality between two listboxes ======================= */

/* SHORTCUT select all/none for check boxes. */
	function selectAllCB(parentId) { selectNCB(parentId, 0, true) }
	function selectNoneCB(parentId) { selectNCB(parentId, 0, false) }

/*	select n for checkboxes. pass in parent container id.
 *	pass in parent container id
 *	n as INT for number to select (0 == ALL)
 *	truefalse as BOOL for value of CBs
 *	pos as ["first"|"last"] to select from start or end.
 *		"" passed will (de)select ALL. (n is ignored here)
 *		skips elems with "clickAllExclude" in their className
 *	doOnChangeEvent as BOOL to execute onChange Event for elem
 *		checkboxes will not execute the onChange event when their value is
 *		changed via js. Passing true here will execute them.
 *
 *	returns the count of ALL checkboxes checked under parentId 
 *	(excluding excludes), or -1 on an error
 */
	function selectNCB(parentId, n, truefalse, pos, doOnChangeEvent) {
		var curCount = 0;
		var totalCount = 0;
		var parent = document.getElementById(parentId);
		if (parent == null) { return -1; }
		var children = parent.getElementsByTagName('input');
		if (n == 0) { pos = ""; }
		var doOnChangeEvent = doOnChangeEvent || false; //careful...

		if (pos == "last") {
			//LAST n
			for (var i=children.length-1;i>=0;i--) {
				if (children[i].type == "checkbox" && (children[i].className).indexOf("clickAllExclude") < 0) {
					if(curCount < n) {
						children[i].checked = truefalse;
						if (doOnChangeEvent) { onChangeEventNCB(children[i]); }
						curCount++;
					}
					if (children[i].checked) { totalCount++; }
				}
			}
		}else if (pos == "first"){
			//FIRST n
			for (var i=0;i<children.length;i++) {
				if (children[i].type == "checkbox" && (children[i].className).indexOf("clickAllExclude") < 0) {
					if(curCount < n) {
						children[i].checked = truefalse;
						if (doOnChangeEvent) { onChangeEventNCB(children[i]); }
						curCount++;
					}
					if (children[i].checked) { totalCount++; }
				}
			}
		}else{
			//ALL
			for (var i=0;i<children.length;i++) {
				if (children[i].type == "checkbox" && (children[i].className).indexOf("clickAllExclude") < 0) {
					children[i].checked = truefalse;
					if (doOnChangeEvent) { onChangeEventNCB(children[i]); }
					
					if (children[i].checked) { totalCount++; }
				}
			}
		}
		
		return totalCount;
	}

	function onChangeEventNCB(obj) {
		
		if (obj.getAttribute('onchange') == null) {
			return true;
		}else{
			obj.onchange();
		}

		return true;
	}


//add some of the functionality to arrays
	Array.prototype.inArray = function (value) {
		var i;
		for (i=0; i < this.length; i++) {
			if (this[i] === value) {
				return true;
			}
		}
		return false;
	};

	
	Array.prototype.inArrayRE = function (RegExpObj) {
		var i;
		for (i=0; i < this.length; i++) {
			if (RegExpObj.test(this[i])) {
				return true;
			}
		}
		return false;
	};


	Array.prototype.clone = function () {
		var a = new Array();
		for (var property in this) {
			a[property] = typeof (this[property]) == 'object'
				? this[property].clone() 
				: this[property]
		}
		return a;
	}

	// http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Objects:Array:push
	// ----------------------------------------------------------------
	if(!Array.prototype.push) Array.prototype.push = function( o ) {
	// ----------------------------------------------------------------
		this[this.length ] = o
	}

	// Like String.substr()
	// http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Objects:String:substr
	// ----------------------------------------------------------------
	Array.prototype.subarr = function( iStart, iLength ) {
	// ----------------------------------------------------------------
		if(iStart >= this.length || (iLength != null && iLength <= 0)) return [];
		else if(iStart < 0) {
			if(Math.abs(iStart) > this.length) iStart = 0;
			else iStart = this.length + iStart;
		}
		if(iLength == null || iLength + iStart > this.length) iLength = this.length - iStart;

		var aReturn = new Array();
		for(var i=iStart; i<iStart + iLength; i++) {
			aReturn.push(this[i]);
		}
		return aReturn;
	}

	// Like String.substring()
	// http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Objects:String:substring
	// ----------------------------------------------------------------
	Array.prototype.subarray = function( iIndexA, iIndexB ) {
	// ----------------------------------------------------------------
		if(iIndexA < 0) iIndexA = 0;
		if(iIndexB == null || iIndexB > this.length) iIndexB = this.length;
		if(iIndexA == iIndexB) return [];
		var aReturn = new Array();
		for(var i=iIndexA; i<iIndexB; i++) {
			aReturn.push(this[i]);
		}
		return aReturn;
	}

	// http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Objects:Array:splice
	// ----------------------------------------------------------------
	if(!Array.prototype.splice) Array.prototype.splice = function(iStart, iLength) {
	// ----------------------------------------------------------------
		if(iLength < 0) iLength = 0;

		var aInsert = new Array();
		if(arguments.length > 2) {
			for(var i=2; i<arguments.length; i++) {
				aInsert.push(arguments[i]);
			}
		}

		var aHead = this.subarray(0, iStart);
		var aDelete = this.subarr(iStart, iLength);
		var aTail = this.subarray(iStart + iLength);

		var aNew = aHead.concat(aInsert, aTail);

		// Rebuild yourself
		this.length = 0;
		for(var i=0; i<aNew.length; i++) {
			this.push(aNew[i]);
		}

		return aDelete;
	}

	// https://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Global_Objects/Array/indexOf
	// ----------------------------------------------------------------
	if (!Array.prototype.indexOf) {
	// ----------------------------------------------------------------
		Array.prototype.indexOf = function(elt /*, from*/) {
			var len = this.length >>> 0;

			var from = Number(arguments[1]) || 0;
			from = (from < 0)
				? Math.ceil(from)
				: Math.floor(from);
			if (from < 0)
				from += len;

			for (; from < len; from++) {
				if (from in this && this[from] === elt)
					return from;
			}
			return -1;
		};
	}

/* CB toggle (checkboxes == checked == Cont. elem visibile) */
	function cbToggle(cbId) {
		var cbId = cbId || '';
		if (!$(cbId) || !$(cbId+'Cont')) { return false; }

		($(cbId).checked)
			?$(cbId+'Cont').style.display = 'block'
			:$(cbId+'Cont').style.display = 'none';
	}

/* clear all form elements under parent element (elemId) */
function clearElements(elemId){
	var object = new Array();
	object[0] = document.getElementById(elemId).getElementsByTagName('input');
	object[1] = document.getElementById(elemId).getElementsByTagName('textarea');
	object[2] = document.getElementById(elemId).getElementsByTagName('select');
	var type = null;
	for (var x=0; x<object.length; x++){
		for (var y=0; y<object[x].length; y++){
			type = object[x][y].type
			switch(type){
				case "text":
				case "textarea":
				case "password":
					object[x][y].value = "";
					break;
				case "radio":
				case "checkbox":
					object[x][y].checked = "";
					break;
				case "select-one":
					object[x][y].options[0].selected = true;
					break;
				case "select-multiple":
					for (z=0; z<object[x][y].options.length; z++){
						object[x][y].options[z].selected = false;
					}
					break;
			}
		}
	}
}

String.prototype.count=function(s1) { 
	return (this.length - this.replace(new RegExp(s1,"g"), '').length) / s1.length;
}

/* max length for text fields (like textarea) */
function maxLen(elem, len) {
	if (elem.value.length+elem.value.count('\n') >= len) { elem.value = (elem.value).substring(0,len-elem.value.count('\n')); }
}

/* basic cookie functions */
	function createCookie(name,value,days) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
	}

	function readCookie(name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;
	}

	function eraseCookie(name) {
		createCookie(name,"",-1);
	}

/* site access interstitial overlay */
	function sa_OL() {
		if (!window['sa_OL_loaded']) { //run only once
			window['sa_OL_loaded'] = true;
			/* pages that will not show OL */
			if (
				(document.location.toString()).indexOf('/site_access/') == -1
			) {
				var mediaURL = readCookie('zipCheck_OL');
				if (mediaURL != null && mediaURL != '') {
					//display modalbox
					Modalbox.show(mediaURL, {
						loadingString: 'Loading Preview...',
						closeValue: ' ',
						closeString: 'Close Preview',
						title: '',
						overlayOpacity: .85,
						overlayClose: true,
						//width: 320,
						transitions: false,
						autoFocusing: false,
						centered: true,
						afterLoad: function() {
							$('MB_overlay').style.backgroundColor = '#000';
							window.scrollTo(0,0);
							$('MB_window').style.width = 'auto';
							$('MB_window').style.height = 'auto';
							Modalbox.options.width = 'auto';
							Modalbox.options.height = 'auto';
							$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
						},
						afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
					});
				}
				/* shown, erase existing cookie */
				eraseCookie('zipCheck_OL');
			}
		}
	}

	//wait for page load
	if (window.Prototype && window.Modalbox) { document.observe("dom:loaded", function() { sa_OL(); }); }
