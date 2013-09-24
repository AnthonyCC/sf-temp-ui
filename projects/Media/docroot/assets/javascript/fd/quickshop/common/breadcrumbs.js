/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var WIDGET = fd.modules.common.widget;
	
	var filteringUrlValue = {
			DEPT: 'all_dept',
			GLUTEN_FREE: 'pref_glutenFree',
			KOSHER: 'pref_kosher',
			LOCAL: 'pref_local',
			ON_SALE: 'pref_onSale',
			ORDERS_BY_DATE: 'all_orders',
			ORGANIC: 'pref_organic',
			TIME_FRAME_30: 'TIME_FRAME_ALL',
			TIME_FRAME_60: 'TIME_FRAME_ALL',
			TIME_FRAME_90: 'TIME_FRAME_ALL',
			TIME_FRAME_180: 'TIME_FRAME_ALL',
			TIME_FRAME_LAST:'TIME_FRAME_ALL' 
	};
	
	function filterList(prev,current){
		if(current.selected){
			prev.push(current);
		}
		return prev;
	}

	var breadcrumbs = Object.create(WIDGET,{
		signal:{
			value:'menu'
		},
		template:{
			value:quickshop.breadcrumbs
		},
		placeholder:{
			value:'#breadcrumbs'
		},
		callback:{
			value:function(data){
				var result=[];
				Object.keys(data).forEach(function(item){
					var menuItem = data[item];
					Object.keys(menuItem).forEach(function(filterName){
						menuItem[filterName].reduce(filterList,result);
					})
				});
			
				
				WIDGET.callback.call(this,{data:result.map(function(item){
					if(item.filter in filteringUrlValue) {
						return {
							filteringUrlValue:filteringUrlValue[item.filter],
							name:item.name
						}
					}
					return null;
				})});
			}
		}
	});

	breadcrumbs.listen();
	
	
	fd.modules.common.utils.register("quickshop.common", "breadcrumbs", breadcrumbs, fd);
}(FreshDirect));
