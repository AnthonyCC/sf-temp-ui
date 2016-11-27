/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;

	var listheader = Object.create(fd.quickshop.common.listheader,{
		signal:{
			value:'listheader'
		},
		template:{
			value:quickshop.pastOrdersHeader
		},
		placeholder:{
			value:'#listheader'
		}
	});

	listheader.listen();

	fd.modules.common.utils.register("quickshop.pastOrders", "listheader", listheader, fd);
}(FreshDirect));
