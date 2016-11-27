/*global quickshop,jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

	var $ = fd.libs.$;
	var DATALISTWIDGET = fd.quickshop.datalistWidget;
  var QSVersion = fd.utils.getActive("quickshop");

	function reduceValues(prev,current) {
		prev.filterIdList.push(current.value);
		return prev;
	};

	var preferences = Object.create(DATALISTWIDGET,{
		signal:{
			value:'PREFERENCES'
		},
		template:{
			value: QSVersion !== "2_0" ? quickshop.preferencesQS22 : quickshop.preferences
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
		render: {
			value: function (data) {
				DATALISTWIDGET.render.call(this, data);
				
				if ($(this.placeholder + ' ul li').size() === 0) {
					$(this.placeholder).hide();
				} else {
					$(this.placeholder).show();
				}
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
