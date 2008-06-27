/* GLOBALS */

//pre-set delay for submenu hiding (in milliseconds)
//this fixes IE 'flicker'
var delay_ms=100;
//hideID holds the id globally for checks
var hideID;
/*
	showing is a self-creating array of menu choices

	values are:
	 2 = menu parent clicked
	 1 = menu showing
	 0 = menu to be hidden
	-1 = menu is hidden
*/
var showing = new Array();
/*
	toggle mouseover/onclick ability
	0 = onmouseover
	1 = onclick
*/
var events = 1;

/* FUNCTIONS */

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

/* add onclick ability */
function click_c(id) {

	 //2 used for ref
	showing[id] = 2;

	//initiate a mouseover event
	$(id).onmouseover();
}

/*
	show function

	id {string} = id of element event happened on
	type {string} = [ t | b | lnav]
		t = top menu (show below)
		b = bottom menu (show above)
		lnav = left menu (show to right)
*/
function show(id, type) {

	//mark as a showing menu
	showing[id] = 1;

	//change to display block first for width check
	$(id+'_menu').style.display = 'block';

	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth) {
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}

	//fix height bug
	$(id+'_menu').style.height = 'auto';

	if (type == 'b') {
		//move to above menu *fixed*
		if ($(id+'_menu').style.top >= 0) {
			$(id+'_menu').style.top = ($(id+'_menu').style.top-$(id+'_menu').offsetHeight+1)+'px';
		}
	}

	if (type == 'lnav')	{
		//move left and next to item
		$(id+'_menu').style.left = $(id).offsetWidth+'px';
		
		//move up to item's top
		$(id+'_menu').style.top = '0px';
	}

	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

function show_t(id) {
	
	//events check
	if (events == 0) { showing[id] = 2; }

	//clear delay if the same menu is touched
	resetDelay(id);

	//check if menu should be visible
	if (showing[id] >= 1) {
		//call show function, pass id and type
		show(id, 't');
	}
}

/* show popup for bottom */
function show_b(id) {

	//events check
	if (events == 0) { showing[id] = 2; }
	
	//clear delay if the same menu is touched
	resetDelay(id);

	//check if menu should be visible
	if (showing[id] >= 1) {
		//call show function, pass id and type
		show(id, 'b');
	}

}

/* show popup for leftnav */
function show_lnav(id) {
	
	//events check
	if (events == 0) { showing[id] = 2; }
	
	//clear delay if the same menu is touched
	resetDelay(id);

	//check if menu should be visible
	if (showing[id] >= 1) {
		//call show function, pass id and type
		show(id, 'lnav');
	}

}

/* reset delay to hide menu, includes check for to-be-hidden menus */
function resetDelay(id) {

	if (window.hide_me) clearTimeout(hide_me);

	//double check menu status
for (x in showing)
	{
		if (showing[x] == 0 && x != id)
		{
		
			$(x+'_menu').style.visibility = 'hidden';
			$(x+'_menu').style.display = 'none';
		}
	}

}

/* set delay to hide menu */
function hide(id){
	hideID = id;
	showing[hideID] = 0;
	hide_me=setTimeout("hide_delay(hideID)",delay_ms);
}

/* clear menus marked for hiding */
function hide_delay(id) {

	for (x in showing) {
		if (showing[x] >= 0 ) {
			showing[id] = -1;
			$(id+'_menu').style.visibility = 'hidden';
			$(id+'_menu').style.display = 'none';
		}
	}
	
}
