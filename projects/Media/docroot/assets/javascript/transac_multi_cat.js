
function chgQty(idx,qtyFldName,delta,min,max) {

	var qty = parseFloat(document.transac_multi_cat[qtyFldName].value)
	if (isNaN(qty)) qty=0;
	if (qty<1) qty=0;
	qty = qty + delta;

	if (qty < min  && qty!=0) return;
	if (qty > max) return;

	if (qty<=0) {
		qty=0;
		document.transac_multi_cat[qtyFldName].value='';
	} else {
		document.transac_multi_cat[qtyFldName].value = qty;
	}
	
     var pricingObj = eval ("pricing"+idx);
	pricingObj.setQuantity(qty);
}

function updatePriceField() {
	document.transac_multi_cat.total.value = pricing.getPrice();
}

