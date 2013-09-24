/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var DISPATCHER = fd.common.dispatcher;

	function setAdditionalInfos(v) {
		if(!v.itemId) {
			v.atcItemId="atcId-"+Date.now().toString(24)+'-'+Math.ceil(Math.random()*10000).toString(24);			
		}
		if(v.quantity) {
			v.quantity.mayempty = true;
		}
		return v;
	}

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var itemList = Object.create(WIDGET,{
		signal:{
			value:'items'
		},
		template:{
			value:quickshop.itemlist
		},
		placeholder:{
			value:'#productlist'
		},
		render:{
			value:function(data) {
				data.itemType = fd.quickshop.itemType || 'general';
				data.data=data.data.map(setAdditionalInfos);

				$('.qs-content').attr('data-items', data.data.length);

				WIDGET.render.call(this,data);

		        $('[data-component="product"]',$(this.placeholder)).each(function(index,element){
		        	fd.components.Subtotal.update(element);
		        });
			}
		}
	});

	itemList.listen();

	fd.modules.common.utils.register("quickshop.common", "itemList", itemList, fd);
}(FreshDirect));
