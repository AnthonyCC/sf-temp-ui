/*global quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var WIDGET = fd.modules.common.widget;

	var filteringId=	['TIME_FRAME_30', 'TIME_FRAME_60', 'TIME_FRAME_90', 'TIME_FRAME_180', 'TIME_FRAME_LAST', 'ORDERS_BY_DATE', 'DEPT', 'GLUTEN_FREE', 'KOSHER', 'LOCAL', 'ORGANIC', 'ON_SALE'],
		filteringValue=	['TIME_FRAME_ALL', 'TIME_FRAME_ALL', 'TIME_FRAME_ALL', 'TIME_FRAME_ALL', 'TIME_FRAME_ALL', 'order_', 'all_dept', 'pref_glutenFree', 'pref_kosher', 'pref_local', 'pref_organic', 'pref_onSale'],
    multiSelect = ['ORDERS_BY_DATE'];


	function filterList(prev,current){
    if(current.selected && filteringId.indexOf(current.filter) > -1){
      if (multiSelect.indexOf(current.filter) === -1) {
        prev[current.filter]={name:current.name, filteringUrlValue: filteringValue[filteringId.indexOf(current.filter)]};
      } else {
        var prefix = filteringValue[filteringId.indexOf(current.filter)],
            fuv = prefix+current.filteringUrlValue;
        prev[prefix+current.filteringUrlValue] = {name: current.name, filteringUrlValue: fuv};
      }
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
          });
				});

        WIDGET.callback.call(this,{data:Object.keys(result).map(function (k) {
          return result[k];
        })});
			}
		}
	});

	breadcrumbs.listen();

	fd.modules.common.utils.register("quickshop.common", "breadcrumbs", breadcrumbs, fd);
}(FreshDirect));
