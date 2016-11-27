/**************************************************************************************************/
/***
/***	WORDPRESS RTE BIOGRAPHY PLUGIN JAVASCRIPT
/***	-----------------------------------------------------------------------
/***	Written by Matthew Praetzel. Copyright (c) 2008 Matthew Praetzel.
/***	-----------------------------------------------------------------------
/***	All Rights Reserved. Any use of these functions & scripts without written consent is prohibited.
/***
/**************************************************************************************************/

/*-----------------------
	Variables
-----------------------*/

//this is for compatability with cforms
var purl = '';

/*-----------------------
	Initialize
-----------------------*/
window.onload = function () {
	tinyMCE.settings['save_callback'] = function (e,c,body) {
		return c;
	}
	tinyMCE.execCommand('mceAddControl',false,'description');
}
