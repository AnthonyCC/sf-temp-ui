/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
  var QSVersion = fd.utils.getActive("quickshop");

	var listheader = Object.create(fd.quickshop.common.listheader,{
		signal:{
			value:'listheader'
		},
		template:{
			value: QSVersion !== "2_0" ? quickshop.shopFromListHeaderQS22 : quickshop.shopFromListHeader
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
