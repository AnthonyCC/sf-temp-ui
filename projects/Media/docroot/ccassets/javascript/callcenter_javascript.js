<!--    //--------- Common CallCenter JavaScript functions  ------------------
var lastZipEntry = "";  //used to track the zipcode entry.
var isIE;
if(document.all){isIE=true}


function zipCodeEntry(fieldObj,eventOBJ) {
	return true; //bypass check for now.
 /*****************************************************************
 ** check the characters being typed that they are only numeric.
 **   Also limit the number if characters to 5 characters.
 ******************************************************************/
 //if this is running in an IE browser, the event cannot be passed, so we must get it from the window object.
	if (window.event) eventOBJ = window.event;  //IE keeps the most current event in the window object..so 

	if (document.all) { // this will be true if in an IE browser
		key = eventOBJ.keyCode;
	} else {
		key = eventOBJ.which;
	}
	//cool.  we have the key code now..check it
	if (key == 8) return true;		//backspace key
	if (key == 13) return true;  //enter key
	if (fieldObj.value.length > 4) {
		return false;
	}

	if (key < 48 || key > 57) return false;

	lastZipEntry = fieldObj.value + String.fromCharCode(key);
	return true;

}
/*****************************************************************/


	
	// clears the values of all elements in a given form
	function clearFormFields(formObj) {
		for (var i = 0; i < formObj.elements.length; i++) {
			formObj.elements[i].value = "";
		}
	}
	
	// takes a boolean value form element (e.g. checkbox) and flips its value
	function flipBoolean(formEl) {
		if (formEl.value == "true") { formEl.value = "false"; }
		else { formEl.value = "true"; }
	}
	
	// navigates to a product details page preserving any config information
	function view_product(formObj, idx, qtyField, searchIdx) {
		if (formObj.catId[0] != null && formObj.catId[0].value != null) {
			var productId = formObj.productId[idx].value;
			var catId = formObj.catId[idx].value;
			var skuCode = formObj.skuCode[idx].value;
			var salesUnit = formObj.salesUnit[idx].value;
			var vcNames = formObj.vcNames[idx].value;
			var vcValues = formObj.vcValues[idx].value;
		} else {
			var productId = formObj.productId.value;
			var catId = formObj.catId.value;
			var skuCode = formObj.skuCode.value;
			var salesUnit = formObj.salesUnit.value;
			var vcNames = formObj.vcNames.value;
			var vcValues = formObj.vcValues.value;
		}
		var qty = qtyField.value;
		if (qty == '0') { qty = '1'; }
		
		theUrl = "/order/product.jsp?template=yes"
		theUrl += "&productId=" + productId;
		theUrl += "&catId=" + catId;
		theUrl += "&qty=" + qty;
		theUrl += "&skuCode=" + skuCode;
		theUrl += "&salesUnit=" + salesUnit;
		//if (vcNames != "" && vcValues != "") {
			theUrl += "&vcNames=" + vcNames;
			theUrl += "&vcValues=" + vcValues;
		//}
		theUrl += "&searchIndex=" + searchIdx;
			
		window.location = theUrl;
	}
	
	// links to a separate page that adds a preconfigured item to the customer's cart
	function addConfigToCart(formObj, idx, qtyField, theUrl, redirectUrl, searchIndex, minQty, maxQty) {
		if (formObj.catId[0] != null && formObj.catId[0].value != null) {
			var productId = formObj.productId[idx].value;
			var catId = formObj.catId[idx].value;
			var skuCode = formObj.skuCode[idx].value;
			var salesUnit = formObj.salesUnit[idx].value;
			var vcNames = formObj.vcNames[idx].value;
			var vcValues = formObj.vcValues[idx].value;
			var configDesc = formObj.configDesc[idx].value;
		} else {
			var productId = formObj.productId.value;
			var catId = formObj.catId.value;
			var skuCode = formObj.skuCode.value;
			var salesUnit = formObj.salesUnit.value;
			var vcNames = formObj.vcNames.value;
			var vcValues = formObj.vcValues.value;
			var configDesc = formObj.configDesc.value;
		}
		var qty = qtyField.value;
		if(qty < minQty) {
			alert("FreshDirect cannot deliver less than " + minQty + " of the selected item.");
		} else if(qty > maxQty) {
			alert("FreshDirect cannot deliver more than " + maxQty + " of the selected item.");
		} else {
			theUrl += "?productId=" + productId;
			theUrl += "&catId=" + catId;
			theUrl += "&quantity=" + qty;
			theUrl += "&skuCode=" + skuCode;
			theUrl += "&salesUnit=" + salesUnit;
			theUrl += "&vcNames=" + vcNames;
			theUrl += "&vcValues=" + vcValues;
			theUrl += "&description=" + configDesc;
			theUrl += "&redirectUrl=" + redirectUrl;
			if (redirectUrl.indexOf("searchIndex")==-1 && redirectUrl.length > 0) {
					if (redirectUrl.indexOf("?")==-1) {
						theUrl += "?searchIndex=" + searchIndex;
					} else {
						theUrl += "&searchIndex=" + searchIndex;
					}
			}
			window.location = theUrl;
		}
		
	}
	
	function confirmDeletePayment(cardId) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (doDelete == true) {
			setDeletePaymentId(document.delete_payment, cardId);
			document.delete_payment.submit();
		}
	}
	
	//sets the value of the hidden field named deletePaymentId
    function setDeletePaymentId(frmObj,payid) {
			if (frmObj["deletePaymentId"]!=null) {
       			frmObj.deletePaymentId.value=payid;			
			}
			return true;
    }
	
	function up() {}
	function down() {}
				

//CRM toggle display

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
	} else if ("print" == type) {
		w = "650";
		h = "700";
	}
	pop(URL,h,w);
}

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
	specs = "HEIGHT=" + h + ",WIDTH=" + w + ",scrollbars";
	//var newWin =  window.open(URL,"newWin",specs);
	newWin =  window.open(URL,"newWin",specs);
	if (newWin.opener == null) newWin.opener = self;
	newWin.focus();
}

	
//  End of Common CallCenter JavaScript Functions       -------->