/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DATALISTWIDGET = fd.quickshop.datalistWidget;

	function reduceValues(prev,current) {
		prev.filterIdList.push(current.value);
		return prev;
	};

	var preferences = Object.create(DATALISTWIDGET,{
		signal:{
			value:'PREFERENCES'
		},
		template:{
			value:quickshop.preferences
		},
		placeholder:{
			value:'#preferences'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $(this.placeholder);
				return $('input[type="checkbox"]',el).serializeArray()
							.reduce(reduceValues,{ filterIdList:[] });
			}
		},
		handleClick:{
			value:function(clickEvent){
			}
		}
	});

	preferences.listen();

	fd.modules.common.utils.register("quickshop.common", "preferences", preferences, fd);
}(FreshDirect));
