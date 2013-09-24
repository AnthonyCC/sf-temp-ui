/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;

	var listheader = Object.create(fd.quickshop.common.listheader,{
		signal:{
			value:'listheader'
		},
		template:{
			value:quickshop.shopFromListHeader
		},
		placeholder:{
			value:'#listheader'
		}
	});

	var recipeheader = Object.create(fd.quickshop.common.listheader,{
		signal:{
			value:'listDetails'
		},
		template:{
			value:quickshop.recipeHeader
		},
		placeholder:{
			value:'#recipeheader'
		},
		callback:{
			value:function( value ) {
				this.render(value || {});
			}
		}
	});

	listheader.listen();
	recipeheader.listen();

	fd.modules.common.utils.register("quickshop.shopFromList", "listheader", listheader, fd);
	fd.modules.common.utils.register("quickshop.shopFromList", "recipeheader", recipeheader, fd);
}(FreshDirect));
