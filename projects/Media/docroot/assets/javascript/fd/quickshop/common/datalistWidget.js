/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var datalistWidget = Object.create(WIDGET,{
		signal:{
			value:'datalistWidget'
		},
		template:{
			value:quickshop.departments
		},
		placeholder:{
			value:'#sortBar'
		},
		callback:{
			value: function( value ){

				WIDGET.callback.call(this,{
					data:Object.keys(value).filter(function (key) {
            return value[key];
          }).map(function(prop){
						return value[prop];
					}).reduce(function( prev, current ){
						return prev.concat(current);
					},[])
				});
			}
		}
	});


	fd.modules.common.utils.register("quickshop", "datalistWidget", datalistWidget, fd);
}(FreshDirect));
