function gotoWindow(locationURL) {
	if (locationURL==null) return false;
	window.location=locationURL;
}

function selectProduct(locationURL,qtyFldName) {
	if (locationURL==null) return false;
	if (qtyFldName!=null) {
		var qty = parseFloat(document.groceryForm[qtyFldName].value)
		if (isNaN(qty)) qty=0;
		if (qty>=1) {
			locationURL=locationURL+"&typedQuantity="+document.groceryForm[qtyFldName].value
		}
	}
	window.location=locationURL;
}

function chgQty(qtyFldName,delta,min,max) {
	var qty = parseFloat(document.groceryForm[qtyFldName].value)
	if (isNaN(qty)) qty=0;
	qty = qty + delta;

	if (qty <= 0 || (qty < min && delta < 0) ){ 
		qty=0;
	} else if(qty < min && delta >=0) {
		qty=min;
	} else if (qty > max) {
		qty=max;
	}

	if (qty<=0) {
		document.groceryForm[qtyFldName].value='';
	} else {
		document.groceryForm[qtyFldName].value = qty;
	}

	//only update the price if the bigProduct is displayed
	if (qtyFldName=="quantity_big") {
		pricing.setQuantity(qty);
		if (syncProdIdx!=null) {  //global var that will contain the "suffix" of the quantity field
			syncQty('big',syncProdIdx);
		}
	}
}

function updatePriceField() {
	document.groceryForm.PRICE.value = pricing.getPrice();
}

// displays/removes the red dot next to the product 
function changeImg(image,direction)
{
   if(document.images && image!=null)
	  if(direction == 'in')
	  {
		  image.src="/media_stat/images/layout/item_highlight_on.gif";
	  }
	  else
	  {
		 image.src="/media_stat/images/layout/dot_clear.gif";
	  }
}

function syncQty(fromSuffix,ToSuffix) {
	if (fromSuffix==null || ToSuffix==null) return;
	var toName = "quantity_"+ToSuffix;
	var fromName = "quantity_"+fromSuffix;
	var fromSkuName = "skuCode_"+fromSuffix;
	var qty = document.groceryForm[fromName].value;
	var qtyString = '';

	if (isNaN(qty)) qty=0;
	if (qty>=1) {
		qtyString = qty;
	}
	
	//only do the update if coming from the bigProduct or the from-item is the same as the bigProduct
	if (fromSuffix=="big"  || fromSuffix == syncProdIdx) {
		document.groceryForm[toName].value = qtyString;
		pricing.setQuantity(qty);
	}

}

