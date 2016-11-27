/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var sorter = Object.create(WIDGET,{
		signal:{
			value:'sorter'
		},
		template:{
			value:quickshop.sorter
		},
		placeholder:{
			value:'#sortBar'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $(this.placeholder);
				return {
					sortId:$('button.selected',el).data('sortid')
				};
			}
		},
		handleClick:{
			value:function(clickEvent){
				var clicked = $(clickEvent.currentTarget),
					parent = clicked.parents('[data-component="sorter"]');
				$('button.selected',parent).removeClass('selected');
				clicked.addClass('selected');

				parent.trigger('sorter-change');
			}
		}
	});

	sorter.listen();
	$(document).on('click',sorter.placeholder+' button',sorter.handleClick.bind(sorter));

	fd.modules.common.utils.register("quickshop.common", "sorter", sorter, fd);
}(FreshDirect));
