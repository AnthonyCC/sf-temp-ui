/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;


	function dispatchMenu( name ) {
		DISPATCHER.signal( name, this[name] );
	};

	var menu = Object.create(fd.common.signalTarget,{
		signal:{
			value:'menu'
		},
		callback:{
			value:function(value){
				Object.keys( value ).forEach(dispatchMenu,value);
			}
		}
	});
	

	fd.modules.common.utils.register("quickshop.pastOrders", "menu", menu, fd);
}(FreshDirect));
