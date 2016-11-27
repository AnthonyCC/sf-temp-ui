function f(evt, klazz) {
	var trg = evt.getTarget();
	trg.setAttribute("class", klazz);

	//alert("trg: "+trg.id);

	var ident = trg.id.split("-")[1];

	var labelId = "l-" + ident;

	var label = trg.getOwnerDocument().getElementById(labelId);
	//alert("label: "+label);

	label.setAttribute("class", klazz + "-text");
		
}
