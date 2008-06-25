/* GLOBALS */

//pre-set delay for submenu hiding (in milliseconds)
//this fixes IE 'flicker'
var delay_ms=10;
//hideID holds the id globally for checks
var hideID;
/*
	showing is a self-creating array of menu choices

	values are:
	 1 = menu showing
	 0 = menu to be hidden
	-1 = menu is hidden
*/
var showing = new Array();

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

function show_t(id) {
	//mark as a showing menu
	showing[id] = 1;

	//clear delay if the same menu is touched
	resetDelay(id);

	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}
	//fix height bug
	$(id+'_menu').style.height = 'auto';
	
	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

/* show popup for bottom */
function show_b(id) {
	//mark as a showing menu
	showing[id] = 1;

	//clear delay if the same menu is touched
	resetDelay(id);

	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth+'px';
	}

	//fix height bug
	$(id+'_menu').style.height = 'auto';
	//move to above menu *fixed*
	if ($(id+'_menu').style.top >= 0){
		$(id+'_menu').style.top = ($(id+'_menu').style.top-$(id+'_menu').offsetHeight+1)+'px';
	}
	//make visible
	$(id+'_menu').style.visibility = 'visible';
}

/* show popup for leftnav */
function show_lnav(id) {
	//mark as a showing menu
	showing[id] = 1;

	//clear delay if the same menu is touched
	resetDelay(id);

	//change to display block first for width check
	$(id+'_menu').style.display = 'block';
	
	//check widths
	if ($(id).offsetWidth > $(id+'_menu').offsetWidth)
	{
		$(id+'_menu').style.width = $(id).offsetWidth-10+'px';
	}
	
	//move left and next to item
	$(id+'_menu').style.left = $(id).offsetWidth+'px';
	
	//move up to item's top
	$(id+'_menu').style.top = '0px';
	
	//fix height bug
	$(id+'_menu').style.height = 'auto';
	//make visible
	$(id+'_menu').style.visibility = 'visible';

}

/* reset delay to hide menu, includes check for to-be-hidden menus */
function resetDelay(id) {
	if (window.hide_me) clearTimeout(hide_me);

	//double check menu status
	for (x in showing)
	{
		if (showing[x] == 0)
		{
			showing[x] = -1;
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

	for (x in showing)
	{
		if (showing[x] >= 0 )
		{
			showing[id] = -1;
			$(id+'_menu').style.visibility = 'hidden';
			$(id+'_menu').style.display = 'none';
		}
	}
	
}

/* more/less menus */
function toggleMore(id) {
	if ($(id+'_less').style.display != 'none')
	{
		$(id+'_more').style.display = 'block';
		$(id+'_more').style.visibility = 'visible';
		$(id+'_less').style.display = 'none';
		$(id+'_less').style.visibility = 'hidden';
	}else{
		$(id+'_more').style.display = 'none';
		$(id+'_more').style.visibility = 'hidden';
		$(id+'_less').style.display = 'block';
		$(id+'_less').style.visibility = 'visible';
	}
}