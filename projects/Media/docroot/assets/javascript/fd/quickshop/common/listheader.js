/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var listheader = Object.create(WIDGET,{
		signal:{
			value:'listheader'
		},
		template:{
			value:function(){ return '' }
		},
		placeholder:{
			value:'#listheader'
		}
	});

	fd.modules.common.utils.register("quickshop.common", "listheader", listheader, fd);
}(FreshDirect));
