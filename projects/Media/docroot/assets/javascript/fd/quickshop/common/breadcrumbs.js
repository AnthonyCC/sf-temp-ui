/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var WIDGET = fd.modules.common.widget;
	
	var filteringId=	['TIME_FRAME_30' ,'TIME_FRAME_60' ,'TIME_FRAME_90' ,'TIME_FRAME_180','TIME_FRAME_LAST','ORDERS_BY_DATE','DEPT'    ,'GLUTEN_FREE'    ,'KOSHER'     ,'LOCAL'     ,'ORGANIC'     ,'ON_SALE'],
		filteringValue=	['TIME_FRAME_ALL','TIME_FRAME_ALL','TIME_FRAME_ALL','TIME_FRAME_ALL','TIME_FRAME_ALL' ,'all_orders'    ,'all_dept','pref_glutenFree','pref_kosher','pref_local','pref_organic','pref_onSale'];

	
	var filteringUrlValue = {
	};
	
	function filterList(prev,current){
		if(current.selected){
			prev[current.filter]={name:current.name};
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
				var result={};
				Object.keys(data).forEach(function(item){
					var menuItem = data[item];
					Object.keys(menuItem).forEach(function(filterName){
						menuItem[filterName].reduce(filterList,result);
					})
				});
			
				
				WIDGET.callback.call(this,{data:filteringId.map(function(item,ndx){
					var ret = result[item];
					if(ret) {
						ret.filteringUrlValue=filteringValue[ndx];
						return ret;
					}
					return null;
				})});
			}
		}
	});

	breadcrumbs.listen();
	
	
	fd.modules.common.utils.register("quickshop.common", "breadcrumbs", breadcrumbs, fd);
}(FreshDirect));
