var id= "";
var css="";

var $jq = $jq || window.jQuery || null;

window.originalClose = window.close;

// works in IE too
window.reallyClose = function (ev) {
  window.originalClose();

  try {
    if (window.parent) {
      window.parent.FreshDirect.components.ifrPopup.close();
    }
  } catch (e) {}

  try {
    if (window.FreshDirect) {
      window.FreshDirect.components.ifrPopup.close();
      window.FreshDirect.components.ajaxPopup.close();
    }
  } catch (e) {}

  ev = ev || window.event;
  if (ev && ev.stopPropagation) {
    ev.stopPropagation();
  }

  return false;
};

// works in other browsers
window.close = window.reallyClose;

try {
  document.addEventListener("DOMContentLoaded", function () {
    if (window.top &&
        window.top.FreshDirect &&
        window.top.FreshDirect.components &&
        window.top.FreshDirect.components.ifrPopup) {
      window.top.FreshDirect.components.ifrPopup.reposition(true);
    }
  });
} catch (e) {}

function soon() { alert("Coming soon"); }

//APPBUG-4163
function close_window_new_account(){
	/*
	 * go through with this only if it is called from within the new account iframe,
	 * AFTER the account has been made or if this is the grey upper right x visually nearby
	*/
	if(
	($jq("#social-login-green-button_begin-shopping").length > 0) ||
	($jq("#iframepopup-body_id").contents().find("#social-login-green-button_begin-shopping").length > 0)
	){
		window.top.location='/login/index.jsp';
		window.top['FreshDirect'].components.ifrPopup.close();
	}
}

//preferred roll-over swap technique..
function swapImage(imgName,imgURL){
	if (imgURL.length>0) {
    		document.images[imgName].src = imgURL;
    	}
}

function swapImageWithBurst(imgName,imgURL, isAlcoholic, burstId){
	if (imgURL.length>0) {
    		document.images[imgName].src = imgURL;
    		if (isAlcoholic=="true" && document.getElementById(burstId)) {
    			//document.getElementsByName(burstName)[0].style.background="url(\"/media_stat/images/layout/small_usq.png\") no-repeat scroll right bottom transparent";
    			document.getElementById(burstId).className = "burst-wine";
    		} else {
    			document.getElementById(burstId).className = "burst-wine.without-background";
			}
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
function swapImageAndBurst( imgName, imgURL, w, h, hasBurst, burstName, burstURL, bWidth, bHeight ) {
	
	var noW;
	(w) ? noW = true : noW = false;

	var w = w || document.images[imgName].width;
	var h = h || document.images[imgName].height;

  // check bounding box
  if (bWidth && w > bWidth) {
    h = bWidth / w * h;
    w = bWidth;
  }
  if (bHeight && h > bHeight) {
    w = bHeight / h * w;
    h = bHeight;
  }

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
var popInterval = null;
var popReturnInterval = null;
var newWin = null;

/* all pop functionality in one function */
function popWithInterval(urlVar, externalVar, hVar, wVar, nameVar, resizableVar, scrollbarsVar, closeFirstVar, retFuncVar) {
	var url = url || urlVar;
		if (!url) { return; }
	var external = externalVar || false;
	var h = hVar || 585;
	var w = wVar || 400;
	var resizable = resizableVar || true;
	var scrollbars = scrollbarsVar || true;
	var name = nameVar || 'popWin';
	var closeFirst = closeFirstVar || false;
	var retFunc = (typeof retFuncVar === 'function') ? retFuncVar : (typeof window[retFuncVar] === 'function') ? window[retFuncVar] : null;

	var params = 'height='+h+',width='+w;
		if (resizable) { params += ',resizable=1'; }
		if (scrollbars) { params += ',scrollbars=1'; }
	
	if (closeFirstVar && window.newWin && !window.newWin.closed) { window.newWin.close(); }

	if (external) {
		newWin = window.open('', name, params);
		newWin.open(url, name, params);
		return; //we can't do any more after opening
	} else {
		newWin = window.open(url, name, params);
	}
	
	popInterval = window.setInterval(function() {
		if (h < 100) { h = 585; }
		if (w < 100) { w = 400; }
		if (window.newWin && !window.newWin.closed) {
			if (window.newWin.opener == null) { window.newWin.opener = self; }
			if (window.newWin.resizeTo) {
				window.clearInterval(popInterval);
				window.newWin.resizeTo(w, h); /* resizeTo is WIDTH, HEIGHT, not HEIGHT, WIDTH */
			} else {
				window.clearInterval(popInterval);
			}
			window.newWin.focus();
		} else {
			window.clearInterval(popInterval);
		}
	}, 500);

	
	/* only if a function was passed in */
	if (retFunc != null) {
		popReturnInterval = setInterval(
			function() {
				if (window.newWin.closed) {
					clearInterval(popReturnInterval);
					retFunc();
				}
			}, 
		500);
	}
}

/* open popup to external URL */
function popExternal(URL, h, w, name) {
	popWithInterval(URL, true, h, w, name);
}

/* simple pop */
function pop(URL, h, w, name) {
	popWithInterval(URL, false, h, w, name);
} 

/* currently being used for help section...*/
function popold(URL,h,w) {
	popWithInterval(URL, false, h, w);
}

/* resize pop */
function popResize(URL,h,w,name) {
	popWithInterval(URL, false, h, w, name, true, true, true);
}

/* pop in help section */
function popResizeHelp(URL,h,w,name) {
	popWithInterval(URL, false, h, w, name);
}

/* open a popup and call a function when it's closed */
function popReturn(URL, h, w, name, retFuncVar) {
	popWithInterval(URL, false, h, w, name, true, true, false, retFuncVar);
}

/* set sizes based on a type string */
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
	
	popWithInterval(URL, false, h, w, name);
}

/* send a url back to the opener */
function backtoWin(url) {
	try {
		if (window.top.FreshDirect.components.ifrPopup.popup.shown) {
			window.top.location=url;
		} else {
			if (window.opener && !window.opener.closed){
				parent.window.opener.location = url ;
				parent.window.opener.focus();
				window.close();
			} else {
				window.location=url;
			}
		}
	} catch(e) {

		window.location=url;
	}
}

/* open a 2nd popup in the opener of the 1st popup */
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

function arrayContainsObject(a, obj){
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}

function deletePaymentMethod(context, paymentPKId, idsUsedBySo, helpSoInfo){
	
	var deletePaymentMethodFunction = function(){
		setDeletePaymentId(context.form,paymentPKId);
		setActionName(context.form,'deletePaymentMethod');
		context.form.submit();
	};
	
	if (arrayContainsObject(idsUsedBySo,paymentPKId)){
		helpSoInfo.entityLabel = "Payment Method ID";
		helpSoInfo.entityId = paymentPKId;
		var deletePaymentMethodSoInfo = {
			deleteFunction: deletePaymentMethodFunction,
			helpSoInfo: helpSoInfo
		};
		CCL.delete_payment_method_so(deletePaymentMethodSoInfo, context);
	
	} else {
		deletePaymentMethodFunction();
	}
	
	return false;
}

function deleteDeliveryAddress(context, addrPKId, idsUsedBySo, helpSoInfo){
	
	var deleteDeliveryAddressFunction = function(){
		setDlvAction(context.form,'deleteDeliveryAddress');
		setIdToDelete(context.form, addrPKId);
		context.form.submit();
	};
	
	
	if (arrayContainsObject(idsUsedBySo,addrPKId)){
		helpSoInfo.entityLabel = "Delivery Address ID";
		helpSoInfo.entityId = addrPKId;
		var deleteDeliveryAddressSoInfo = {
			deleteFunction: deleteDeliveryAddressFunction,
			helpSoInfo: helpSoInfo
		};
		CCL.delete_delivery_address_so(deleteDeliveryAddressSoInfo, context);
	
	} else {
		deleteDeliveryAddressFunction();
	}
	
	return false;
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

/* this functionality is in the popupcart.js!!! 
function updateYourCartPanel() {}
*/


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
 *		pass a second param as boolean to sort as Integers (false by default)
 *		
 *		Note: When using sort by Int, mixed (Str/Int) options will sort numbers first.
 *		      Sets containing Strings that LOOK like Ints (like "5ive") will treat those as Ints.
 *
 *		pass a third param as boolean to sort case-sensitive (false by default)
 *		pass a fourth param as int to reset selected index after sort (0 by default, -1 to not reset)
 */
	function sortByText(sortId, asIntVar, caseSensitiveVar, resetSelIndexVar) {
		var specId = sortId || '';
		var asInt = asIntVar || false;
		var caseSensitive = caseSensitiveVar || false;
		var resetSelIndex = resetSelIndexVar = typeof resetSelIndexVar !== 'undefined' ?  resetSelIndexVar : 0;
		
		if (!document.getElementById(specId) || document.getElementById(specId).tagName !== 'SELECT') {
			return;
		}
			
		//alphabetize
		var selectArr = [];

		if (specId!='') {
			selectArr[0] = document.getElementById(specId); 
		}else{
			selectArr = document.getElementsByTagName('select'); 
		}

		for (var i = 0; i < selectArr.length; i++) {
			var strArr = [];
			if (asInt) {
				var intArr = [];
			}
			var strArr = [];
			// Get the options for the select element 
			for (var j = 0; j < selectArr[i].options.length; j++) { 
				// Store this as an object that has an option object member, and a toString function (which will be used for sorting) 
				if (selectArr[i].options[j].text != "") { //ignore blanks
					if (asInt && !isNaN(parseInt(selectArr[i].options[j].text)) && !(selectArr[i].options[j].text).match(/[\D]/g)) {
						intArr[intArr.length] = { 
							option : selectArr[i].options[j], 
							toString : function() { 
								// Return the text of the option, not the value 
								return this.option.text;
							}
						}
					} else {
						strArr[strArr.length] = { 
							option : selectArr[i].options[j],
							toString : function() { 
								// Return the text of the option, not the value 
								return this.option.text;
							}
						}
					}
				}
			} 
			// Sort the array(s) of options
			if (asInt && intArr.length > 1) {
				intArr.sort(function(a,b){return a-b});
			}
			if (strArr.length > 1) {
				if (caseSensitive) {
					strArr.sort();
				} else {
					strArr.sort(function (a, b) {
						return a.toString().toLowerCase().localeCompare(b.toString().toLowerCase());
					});
				}
			}
				

			// Remove all options from the select
			selectArr[i].options.length = 0;
			
			// Rebuild the select using our sorted array
			var j = 0;
			var n = 0;
			if (asInt) {
				for (j = 0; j < intArr.length; j++) {
					selectArr[i].options[n] = intArr[j].option;
					n++;
				}
			}
			for (j = 0; j < strArr.length; j++) {
				selectArr[i].options[n] = strArr[j].option;
				n++;
			}

			if (resetSelIndex >= 0) {
				selectArr[i].selectedIndex = resetSelIndex;
			}
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

/* === listbox box helper ======================= */

/* generic select populate method - assumes jquery
	 *	pass in data to add
	 *		array of arrays: [[elemId, optText, optValue]]
	 *  optional selectedVar for selected items (defaults selectedIndex to 0)
	 *		array of arrays: [[elemId, defaultSelected]] - last passed, last set
	 */
	function populateLists(dataArrVar, defSelectedArrVar) {
		var dataArr = dataArrVar || []; //
		if (dataArr.length == 0) { return; }
		var selectedArr = defSelectedArrVar || [];
		var selectedIndex = 0; //default
		var listObjs = {};

		for (var i = 0; i < selectedArr.length; i++) {
			if (selectedArr[i].length != 2) { continue; }
			
			if (!listObjs.hasOwnProperty(selectedArr[i][0])) {
				listObjs[selectedArr[i][0]] = {};
			}
			listObjs[selectedArr[i][0]].name = selectedArr[i][0];
			listObjs[selectedArr[i][0]].defaultSelection = selectedArr[i][1];
		}

		for (var i = 0; i < dataArr.length; i++) {
			if (dataArr[i].length != 3) { continue; } //invalid
			
			var elemId = dataArr[i][0];
			var optText = dataArr[i][1];
			var optValue = dataArr[i][2];

			if ($jq('#'+elemId).length == 0) { continue; } //elem with elemId doesn't exist
			
			addOpt(elemId, optText, optValue);

			if (
				listObjs.hasOwnProperty(elemId) && listObjs[elemId].hasOwnProperty('defaultSelection') 
				&& listObjs[elemId].defaultSelection == optValue
			) {
				listObjs[elemId].selectedIndex = $jq('#'+elemId+' option').length-1;
			}
		}

		for (listObj in listObjs) {
			if (!listObjs[listObj].hasOwnProperty('selectedIndex')) {
				listObjs[listObj].selectedIndex = 0;
			}
			if (listObjs[listObj].selectedIndex < 0) { listObjs[listObj].selectedIndex = 0; }

			//set selected index
			$jq('#'+listObjs[listObj].name).prop('selectedIndex', listObjs[listObj].selectedIndex);
		}

		
	}

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

	

/**
 * Function to adjust iframe size.
 * Call from just before iframe body tag end.
 * 
 * @param frameId DOM ID of your iframe
 * @param offset Number to extend height (optional)
 */
function getFrameById(frameId) {
	var f = null;
	try {
		f = window.parent.document.getElementById(frameId);
		if (f == null || f == undefined) {
			f = window.top.document.getElementById(frameId);
		}
	} catch (e) {
		
	}
	return f;
}

function setFrameHeight(frameId, offset) {
	var f = getFrameById(frameId);
	if (f == null) { return; }

	f.style.height = null;

	var hgt = getFrameHeight(frameId);
	
	if (offset == undefined)
		offset = 0;
	
	f.style.height = (hgt+offset)+"px";
}

function setFrameHeightSL(frameId, hgt) {
	var f = getFrameById(frameId);
	if (f == null) { return; }

	f.style.height = (hgt)+"px";
}

function setFrameWidthSL(frameId, wth) {
	var f = getFrameById(frameId);
	if (f == null) { return; }

	f.style.width = (wth)+"px";
}

function getFrameHeight(frameId) {
	var f = getFrameById(frameId);
	if (f == null) { return; }
	
	var innerDoc = (f.contentDocument) ? f.contentDocument : f.contentWindow.document;

	return innerDoc.body.parentNode.scrollHeight;
}

function getFrameWidth(frameId) {
	var f = getFrameById(frameId);
	if (f == null) { return; }
	
	var innerDoc = (f.contentDocument) ? f.contentDocument : f.contentWindow.document;

	return innerDoc.body.parentNode.scrollWidth;
}
function setFrameSize(frameId, offsetX, offsetY) {
	var f = getFrameById(frameId);
	if (f == null) { return; }

	var wdh = getFrameWidth(frameId);
	var hgt = getFrameHeight(frameId);
	
	if (offsetX == undefined)
		offsetX = 0;

	if (offsetY == undefined)
		offsetY = 0;
	
	f.style.width = (wdh+offsetY)+"px";
	f.style.height = (hgt+offsetY)+"px";
}

/**
 * Extract query string from the form
 * 
 * @param theForm the content node of the form (HTMLFormElement)
 * @return the query string in param1=val1&param2=val2... format
 */
function extract_query_string(theForm) {
	var params = {};

	for (i = 0; i < theForm.elements.length; i++) {
		var elem = theForm.elements[i];
		switch (elem.tagName) {
		case "INPUT":
			switch (elem.type) {
			case "text":
			case "hidden":
				params[elem.name] = elem.value;
				break;
			case "checkbox":
				if (elem.checked) {
					params[elem.name] = elem.value;
				}
				break;
			case "radio":
				if (elem.checked) {
					params[elem.name] = elem.value;
				}
			}
			break;
		case "TEXTAREA":
			params[elem.name] = elem.value;
			break;
		case "SELECT":
			params[elem.name] = elem.options[elem.selectedIndex].value;
			break;
		}
	}

	var qs = "";
	var first = true;
	for (var key in params) {
		var value = params[key];
		if (first)
			first = false;
		else
			qs += '&';
		qs += encodeURIComponent(key) + '=' + encodeURIComponent(value);
	}

	return qs;
}



	/* change language of page */
	function changeLang(langVar) {
		var lang = langVar || 'english';
		var loc = document.location.toString();
		if (loc.indexOf('lang=') != -1) {
			loc = loc.replace(/lang=[^&]*/, 'lang='+lang);
		} else {
			if (loc.indexOf('&') != -1 || loc.indexOf('?') != -1) {
				loc = loc + '&lang='+lang;
			} else {
				loc = loc + '?lang='+lang;
			}
		}
		document.location = loc;
		return false;
	}


function curvyCornersHelper(elemId, settingsObj) {
                if (document.getElementById(elemId)) {
						try {
	                        var temp = new curvyCorners(settingsObj, document.getElementById(elemId)).applyCornersToAll();
						} catch(e) {
							//ignore
						}
                }
        }

var ccSettings = {
                tl: { radius: 6 },
                tr: { radius: 6 },
                bl: { radius: 6 },
                br: { radius: 6 },
                topColour: "#ffffff",
                bottomColour: "#ffffff",
                antiAlias: true,
                autoPad: true
        };		
		
function doOverlayWindow(olURL, titleVar) {
	var titleString = titleVar || '';
		
		var olURL = olURL || '';
                if (olURL == '') { return false; }
				var ieVer = detectIEVersion();
                
                Modalbox.show(olURL, {
                        loadingString: 'Loading Preview...',
                        closeString: 'Close Preview',
                        title: titleString,
                        width: 420,
                        overlayOpacity: .80,
                        transitions: false,
                        autoFocusing: false,
                        centered: true,
                        overlayClose: true,
                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
                        beforeLoad: function() {
							if (ieVer == 7) {
								$$('html')[0].setStyle({ overflow: 'hidden' });
							} else {
								$$('body')[0].setStyle({ overflowY: 'hidden' });
							}
						},
                        afterLoad: function() {
                                        $('MB_frame').style.border = '1px solid #CCCCCC';
										$('MB_content').style.overflowX = 'hidden';
                                        $('MB_header').style.border = '0px solid #CCCCCC';
                                        $('MB_header').style.display = 'block';
                                        window.scrollTo(0,0);
                                        $('MB_window').style.width = 'auto';
                                        $('MB_window').style.height = 'auto';
                                        $('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
                                        //$('MB_content').style.padding = '20px';

                                        ccSettings.topColour = "#ffffff";
                                        ccSettings.bottomColour = "#ffffff";
                                        curvyCornersHelper('MB_frame', ccSettings);					
							
                        },
                        afterHide: function() { 
							window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY);
								$$('html')[0].setStyle({ overflow: '' });
								$$('body')[0].setStyle({ overflowY: '' });
						}
                });		
	}

	function doOverlayWindowFormSubmit(olURL, formname) {		
		var olURL = olURL || '';
		if (olURL == '') { return false; }

		paramsvar = Form.serialize(formname);
		Modalbox.hide();
		Modalbox.show(olURL, {
			loadingString: 'Loading Preview...',
			title: ' ',
			width: 400,
			overlayOpacity: .80,
			centered: true,
            method: 'post',
            params: paramsvar,
			closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
			afterLoad: function() {
					$('MB_frame').style.border = '1px solid #CCCCCC';
					$('MB_content').style.overflowX = 'hidden';
					$('MB_header').style.border = '0px solid #CCCCCC';
					$('MB_header').style.display = 'block';
					window.scrollTo(0,0);
					$('MB_window').style.width = 'auto';
					$('MB_window').style.height = 'auto';
					$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
					//$('MB_content').style.padding = '20px';

					ccSettings.topColour = "#ffffff";
					ccSettings.bottomColour = "#ffffff";
					curvyCornersHelper('MB_frame', ccSettings);
			},
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		});
	}
	
	function detectIEVersion() {
			var rv = -1; // Return value assumes failure.
			if (navigator.appName == 'Microsoft Internet Explorer') {
				var ua = navigator.userAgent;
				var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
				if (re.exec(ua) != null)
					rv = parseFloat( RegExp.$1 );
			}
			return rv;
		}
	
	
	var fdCoremetrics = (function(){
		
		var iFrame = null;
		
		var createIFrame = function(){
			iFrame = document.createElement("iframe");
			iFrame.className = "coremetrics-iframe";
			document.body.appendChild(iFrame);
		}; 

		var initIFrame = function(){
			if(iFrame === null){
				createIFrame();
			};  
		}; 
		
		var trackAddToCartEvent = function(){
			initIFrame();
			iFrame.src = "/coremetrics/shop5.jsp";
		}; 

		return {
			trackAddToCartEvent: trackAddToCartEvent
		}

	})();
	
	/* setup dialog
	 * returns reference to dialog
	 */
	function setupOverlayDialog() {
		var overlayDialog = $jq('#uimodal-output');
		if (overlayDialog.length == 0) {
			overlayDialog = $jq('<div id="uimodal-output"></div>');
			$jq("body").append(overlayDialog);
		}
		overlayDialog.dialog({
			title: "", /* no title */
			modal: true,
			autoOpen: false,
			height: 'auto',
			width: 'auto',
			maxClientHeight: 1,
			maxClientWidth: 1,
			maxHeight: 1,
			maxWidth: 1,
			openHeight: -1,
			openWidth: -1,
			closeText: 'hide',
			position: { my: "center", at: "center", of: window },
			zIndex:10001,
			resizable: false,
			draggable: false,
			open: function() {
				$jq('body').css({ 'overflow': 'hidden' });
				if(FreshDirect.mobWeb){
					overlayDialog.dialog('option', 'maxClientHeight', 1);
					overlayDialog.dialog('option', 'maxClientWidth', 1);
				} else {
					overlayDialog.dialog('option', 'maxClientHeight', 0.85);
					overlayDialog.dialog('option', 'maxClientWidth', 0.95);
				}
				overlayDialog.dialog('option', 'maxHeight', Math.round(document.documentElement.clientHeight * overlayDialog.dialog('option', 'maxClientHeight')));
				overlayDialog.dialog('option', 'maxWidth',  Math.round(document.documentElement.clientWidth * overlayDialog.dialog('option', 'maxClientWidth')));

				if (overlayDialog.height() > overlayDialog.dialog('option', 'maxHeight')) { 
					setTimeout(function(){ overlayDialog.dialog('option', 'height', overlayDialog.dialog('option', 'maxHeight')); }, 100);
				} else {
					if ($jq('#uimodal-output').height() == 0) { //this is 0 in IE, which causes issues
						overlayDialog.dialog('option', 'openHeight', '500px');
					} else {
						overlayDialog.dialog('option', 'openHeight', $jq('#uimodal-output').height());
					}
				}


				if (overlayDialog.width() > overlayDialog.dialog('option', 'maxWidth')) {
					setTimeout(function(){ overlayDialog.dialog('option', 'width', overlayDialog.dialog('option', 'maxWidth')); }, 100);
				} else {
					if ($jq('#uimodal-output').width() == 0) { //this is 0 in IE, which causes issues
						overlayDialog.dialog('option', 'openWidth', '500px');
					} else {
						overlayDialog.dialog('option', 'openWidth', $jq('#uimodal-output').width());
					}
				}

				//allow off-click to close
				$jq('.ui-widget-overlay').bind('click', function(){ overlayDialog.dialog('close'); });


			},
			close: function () {
				$jq('body').css({ 'overflow': 'visible' });
			}
		});	
	
		return overlayDialog;
	}
	
	/* use dialog by url */
	function doOverlayDialog(olURL, olData) {
		var overlayDialog = setupOverlayDialog();
		
		if (olData != 'undefined' && (typeof olData).toLowerCase() == 'object') {
			//if data is passed in, POST it
			overlayDialog.load(olURL, olData, function() { overlayDialog.dialog('open'); });
		} else {
			overlayDialog.load(olURL, function() { overlayDialog.dialog('open'); });
		}

		return overlayDialog;
	}
	
	function doOverlayDialogNew(olURL) {
		$jq('#uimodal-output').off('dialogclose');
		$jq('#uimodal-output').dialog('close');
		var overlayDialog = setupOverlayDialog();
		overlayDialog.load(olURL, function() { overlayDialog.dialog('open'); setTimeout(function(){ dialogWindowRealignFunc(); }, 1); });
		$jq('.ui-dialog').addClass('overlay-dialog-new').css('z-index','1001');
		$jq('.ui-widget-overlay').css('z-index','1000');
		if(FreshDirect.mobWeb){ $jq('.ui-dialog').addClass('mm-page-overlay'); }
	}
	
	/* use dialog by css selector */
	function doOverlayDialogBySelector(olSelector) {
		var overlayDialog = setupOverlayDialog();
		
		overlayDialog.html($jq(olSelector).html());
		overlayDialog.dialog('open');
		
		return overlayDialog;
	}
	
	/* use dialog by html */
	function doOverlayDialogByHtml(olHtml) {
		var overlayDialog = setupOverlayDialog();
		
		overlayDialog.html(olHtml);
		overlayDialog.dialog('open');

		return overlayDialog;
	}
	
	function doOverlayDialogByHtmlNew(olHtml) {
		$jq('#uimodal-output').off('dialogclose');
		$jq('#uimodal-output').dialog('close');
		var overlayDialog = setupOverlayDialog();
		overlayDialog.html(olHtml);
		overlayDialog.dialog('open');
		$jq('.ui-dialog').addClass('overlay-dialog-new').css('z-index','1001');
		$jq('.ui-widget-overlay').css('z-index','1000');
		if(FreshDirect.mobWeb){ $jq('.ui-dialog').addClass('mm-page-overlay') }
	}

	/* use dialog by url */
	function doOverlayDialogWithSpinner(olURL, olData) {
		var overlayDialog = setupOverlayDialog();
		
		if (olData != 'undefined' && (typeof olData).toLowerCase() == 'object') {
			//if data is passed in, POST it
			overlayDialog.load(olURL, olData, function() { overlayDialog.dialog('open'); }).html('<img src="/media_stat/images/navigation/sidecart/spinner.gif" alt="loading..."/> ');
		} else {
			overlayDialog.load(olURL, function() { overlayDialog.dialog('open'); }).html('<img src="/media_stat/images/navigation/sidecart/spinner.gif" alt="loading..."/> ');
		}

		return overlayDialog;
	}

	function dialogWindowRealignFunc() {
		var overlayDialog = $jq('#uimodal-output');
		overlayDialog.dialog('option', 'position', overlayDialog.dialog('option', 'position'));
	}
	
	function dialogWindowResizeFunc() {
		var overlayDialog = $jq('#uimodal-output');
		var maxContextHeight = Math.round(document.documentElement.clientHeight * overlayDialog.dialog('option', 'maxClientHeight'));
		var maxContextWidth = Math.round(document.documentElement.clientWidth * overlayDialog.dialog('option', 'maxClientWidth'));
		
		if (overlayDialog.height() >= maxContextHeight) { 
			overlayDialog.dialog('option', 'height', maxContextHeight);
			if (overlayDialog.dialog('option', 'openHeight') == -1) { overlayDialog.dialog('option', 'openHeight', overlayDialog.height()); }
		} else {
			if (overlayDialog.dialog('option', 'openHeight') == -1) { overlayDialog.dialog('option', 'openHeight', overlayDialog.height()); }

			if (overlayDialog.dialog('option', 'openHeight') >= maxContextHeight) {
				overlayDialog.dialog('option', 'height', maxContextHeight);
			} else {
				overlayDialog.dialog('option', 'height', overlayDialog.dialog('option', 'openHeight'));
			}
		}
		if (overlayDialog.width() >= maxContextWidth) {
			overlayDialog.dialog('option', 'width', maxContextWidth);
			if (overlayDialog.dialog('option', 'openWidth') == -1) { overlayDialog.dialog('option', 'openWidth', overlayDialog.width()); }
		} else {
			if (overlayDialog.dialog('option', 'openWidth') == -1) { overlayDialog.dialog('option', 'openWidth', overlayDialog.width()); }
			
			if (overlayDialog.dialog('option', 'openHeight') >= maxContextWidth) {
				overlayDialog.dialog('option', 'width', maxContextWidth);
			} else {
				overlayDialog.dialog('option', 'width', overlayDialog.dialog('option', 'openWidth'));
			}
			
		}

		overlayDialog.dialog('option', 'position', overlayDialog.dialog('option', 'position'));
	}

	var dialogWindowResizeTimer;
	var dialogDocReady = false;
	if (window['$jq'] && $jq.ui) { //make sure jQuery is available, and ui (for dialog)
		$jq(document).ready(function () { dialogDocReady = true });
		$jq(window).resize(function() {
			clearTimeout(dialogWindowResizeTimer);
			if (dialogDocReady) { dialogWindowResizeTimer = setTimeout(dialogWindowResizeFunc, 100); }
		});
	}

function checkBatch() {
	if(document.getElementById("batch_promo").checked) {
		//the box is checked so display an alert box
		if(confirm("Do you want to Apply these changes to entire batch?"))
			return true;
		return false;
	}
}

	var clipPending = false;
	function fdCouponClip(couponId) {
		if (couponId && !clipPending) {
			clipPending = true;
			/* check all other matching check boxes */
			$jq('input[name="fdCoupon_'+couponId+'_cb"]').each(function (i, e){
				$jq(e).attr('checked', true);
				$jq(e).attr('disabled', true);
			});
			$jq.ajax({
				type: 'GET',
				url: window.overrideCouponEndpoint || '/api/cp_api.jsp',
				data: { action: 'clip', cpid: couponId },
				success: function(data) {
					/* make sure the tooltips are gone (IE) */
					$jq('.cDetToolTipClickToApply').each(function (i, e){
						$jq(e).hide();
					});
					window.parent['qbClippedSuccess']('[name="fdCoupon_'+couponId+'_cb"]');
					try {
						if (data && data.cartData) {
							FreshDirect.common.dispatcher.signal('cartData', data.cartData);
						}
					} catch (e) {}
				},
				error: function() {
					/* clear check box(es) */
					$jq('[name="fdCoupon_'+couponId+'_cb"]:checked').each(function (i, e){
						$jq(e).attr('checked', false);
						$jq(e).attr('disabled', false);
						$jq(e).removeClass('disabled');
					});
				},
				complete: function() {
					clipPending = false;
				}
			});
		}
	}
	function qbClippedSuccess(selectorStr) {
		/* check all other matching check boxes */
		$jq(selectorStr).each(function (i, e){
			if ($jq(e).parent('div').hasClass('fdCoupon_prodBox')) {
				$jq(e).addClass('isClipped');
			}
			$jq(e).addClass('disabled');
			$jq(e).prop('checked', true);
			$jq(e).prop('disabled', true);
		});
	}

/* add coupon tooltips */
if ($jq && $jq.ui) { /* requires jquery ui */
	$jq(function() {
		var currentlyPositionning;
		$jq.ui.position.ttFlipCustom = {
			left: function(position, data) {
			currentlyPositionning = data.elem;
				initPos = position.left;
				$jq.ui.position.flip.left(position, data);
				if (initPos != position.left) {
					currentlyPositionning.addClass('tooltipFlipH');
					currentlyPositionning.position({
						of: currentlyPositionning,
						my: 'left bottom',
						at: 'right top',
						offset: '100px'
					});
				}
			},
			top: function(position, data) {
				initPos = position.top;
				$jq.ui.position.flip.top(position, data);
				if (initPos != position.top) {
					data.elem.addClass('tooltipFlipV');
				}
			}
		};
		$jq( document ).tooltip({
			items: ".fdCoupon_det, .fdCoupon_cb",
			tooltipClass: "cDetToolTip",
			content: function() {
				var element = $jq( this );
				if ( $jq(element).is(":checked") ) { return null; }
				if (element.hasClass('fdCoupon_cb')) {
					$jq( document ).tooltip({ tooltipClass: "cDetToolTipClickToApply" });
					$jq( document ).tooltip({ 
						position: {
							my: "left-16 bottom-16",
							at: "center top",
							collision: 'ttFlipCustom',
							using: function( position, feedback ) {
								$jq( this ).css( position );
								$jq( '<div>' )
									.addClass( "arrow" )
									.addClass( feedback.vertical )
									.addClass( feedback.horizontal )
									.appendTo( this );
							}
						}
					});

					return "Click to apply";
				} else {
					$jq( document ).tooltip({ tooltipClass: "cDetToolTip" });
					$jq( document ).tooltip({ 
						position: {
							my: "left-40 bottom-30",
							at: "center top",
							collision: 'ttFlipCustom',
							using: function( position, feedback ) {
								$jq( this ).css( position );
								$jq( '<div>' )
									.addClass( "arrow" )
									.addClass( feedback.vertical )
									.addClass( feedback.horizontal )
									.appendTo( this );
							}
						}
					});

					return $jq('div[name="'+element.attr('name')+'Content"]:first').html();
				}
			}
			

		});
	});
}

/*	auto-fix featured products row.
 *		pass in selectors:
 *			row container
 *			inner box container
 *			each item in inner box to count against height
 */
	function fixGridFeatRowHeights(outerBox, innerBox, innerItems) {
		var totalH = 0;
		var tallest = 0;
		$jq(innerBox).each(function (index, elem) {
			totalH = 0;
			$jq($jq(elem).find(innerItems).children('div')).each(function (ii, ee) {
				totalH = totalH+$jq(ee).outerHeight(true);
			});
			if (totalH > tallest) {
				tallest = totalH;
			}
		}).each(function (index, elem) {
			
			if (index === 0) {
				$jq(outerBox).css({ 'height': (tallest+14)+'px' });
			}
			$jq($jq(elem).find(innerItems)).css({ 'height': tallest+'px' });
			$jq(elem).css({ 'height': (tallest+14)+'px' });
		});
	}
	
    /* clear coupon status msging on ATC */
    function clearCouponStatusATC(couponIdVar, couponIdRefElementId) {
           var couponId = couponIdVar;
           if (couponId == null) {
                  $jq('#'+couponIdRefElementId).children().each(function (i, e){
                        var tempName = $jq(e).attr('name');
                        if (tempName.indexOf('fdCoupon_') == 0) {
                               tempName = tempName.split('_');
                               if (tempName.length == 3) {
                                      couponId = tempName[1];
                                      return;
                               }
                        }
                  });
           }

           if (couponId == null) { return false; }

           $jq('span[name="fdCoupon_'+couponId+'_stat"]').each(function (i, e){
                  $jq(e).empty();
           });

           return true;
    }


	/* input type changer for jQuery < 1.9
	 * inputElem is the <input/> element
	 * type is the type you want to change it to
	 */
	function changeType(inputElem, type) {
		var elem = $jq(inputElem);
	    if(elem.prop('type') == type)
	        return elem; //That was easy.
	    try {
	        return elem.prop('type', type); //Stupid IE security will not allow this
	    } catch(e) {
	        //Try re-creating the element
	        //jQuery has no html() method for the element, so we have to put into a div first
	        var html = $jq("<div>").append(elem.clone()).html();
	        var regex = /type=(\")?([^\"\s]+)(\")?/; //matches type=text or type="text"
	        //If no match, we add the type attribute to the end; otherwise, we replace
	        var tmp = $jq(html.match(regex) == null ?
	            html.replace(">", ' type="' + type + '">') :
	            html.replace(regex, 'type="' + type + '"') );
	        //Copy data from old element
	        tmp.data('type', elem.data('type') );
	        var events = elem.data('events');
	        var cb = function(events) {
	            return function() {
	                //Bind all prior events
	                for(i in events) {
	                    var y = events[i];
	                    for(j in y)
	                        tmp.bind(i, y[j].handler);
	                }
	            }
	        }(events);
	        elem.replaceWith(tmp);
	        setTimeout(cb, 10); //Wait a bit to call function
	        return tmp;
	    }
	}
	
if ($jq) {
	/* parse value from reqParams in current window.location
	 * usage: var value = $jq.QueryString["PARAM_TO_GETVALUE_OF"];
	 */
	(function($) {
		$.QueryString = (function(a) {
			if (a == "") return {};
			var b = {};
			for (var i = 0; i < a.length; ++i)
			{
				var p=a[i].split('=');
				if (p.length != 2) continue;
				b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
			}
			return b;
		})(window.location.search.substr(1).split('&'))
	})($jq);
}

/* change uri param value (or update if existing), then go to new uri */
function changeUriParam(paramKey, paramVal) {
	if (paramKey) {
		
		function escapeRegExp(regexStr) {
		    return regexStr.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
		}
		
		var loc = document.location.toString();
		var regex = new RegExp(escapeRegExp(paramKey)+'=[^&]*', 'g');
		var keyVal = paramKey+'='+paramVal;
		
		if (loc.indexOf(paramKey+'=') != -1) {
			loc = loc.replace(regex, keyVal);
		} else {
			if (loc.indexOf('&') != -1 || loc.indexOf('?') != -1) {
				loc = loc + '&'+keyVal;
			} else {
				loc = loc + '?'+keyVal;
			}
		}
		document.location = loc;
	}
	
	return false;
}

/* throttle events helper */
function throttle(fn, threshold, scope) {
	threshold || (threshold = 250);
	var last, deferTimer;

	return function () {
		var context = scope || this;

		var now = +new Date,
		args = arguments;
		if (last && now < last + threshold) {
			// hold on to it
			clearTimeout(deferTimer);
			deferTimer = setTimeout(function () {
			last = now;
			fn.apply(context, args);
			}, threshold);
		} else {
			last = now;
			fn.apply(context, args);
		}
	};
}