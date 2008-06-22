/* from the prototype lib*/
function $() {
	var elements = new Array();
	for (var i = 0; i < arguments.length; i++) {
		var element = arguments[i];
		if (typeof element == 'string')
			element = document.getElementById(element);
		if (arguments.length == 1)
			return element;
		elements.push(element);
	}
	return elements;
}


/* clear existing popup */
function hide(id) {
	$(id+'_menu').style.visibility = 'hidden';
	$(id+'_menu').style.display = 'none';
}

/* show popup for top */
function show_t(id) {
	
	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}
	
	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

/* show popup for bottom */
function show_b(id) {
	
	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}
	$('debug').innerHTML = $(id+'_menu').offsetHeight;
	//move to above menu
	$(id+'_menu').style.top = ($(id+'_menu').style.top-$(id+'_menu').offsetHeight)+'px';

	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

/* test functions */

/* show popup for top */
function show_t_test(id) {
	
	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}
	
	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

/* clear existing popup */
function hide_test(id) {
	$(id+'_menu').style.width = '0px';
	$(id+'_menu').style.height = '0px';
	$(id+'_menu').style.visibility = 'hidden';
}