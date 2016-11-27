/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;


	/* helper function for successHandler
	 * "this" means the data object
	 * @param name property name
	 */
	var _signalWidgets = function( name ) {
		DISPATCHER.signal( name, this[name] );
	};


	var menu = Object.create(fd.common.signalTarget,{
		signal:{
			value:'menu'
		},
		callback:{
			value:function( menuData ) {
				Object.keys( menuData ).forEach( _signalWidgets, menuData );
			}
		}
	});

	menu.listen();

	fd.modules.common.utils.register("quickshop.common", "menu", menu, fd);
}(FreshDirect));
