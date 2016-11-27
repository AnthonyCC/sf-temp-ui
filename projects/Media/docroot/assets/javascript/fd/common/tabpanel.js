/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;


	var tabpanel = {
		selectTab:{
			value:function(tabPanelElement,tabName) {
				$('.selected[data-tabname]',tabPanelElement).removeClass('selected');
				$('[data-tabname="'+tabName+'"]',tabPanelElement).addClass('selected');	
				
			}
		},
		clickHandler:{
			value:function( e ) {
				var $clicked=$(e.target),
					tabPanelElement=e.currentTarget;
				
				if($clicked.data('component') === 'tabitem') {
					this.selectTab(tabPanelElement,$clicked.data('tabname'),$clicked);
				}
			}
		}
	};


	fd.modules.common.utils.register("common", "tabPanel", tabpanel, fd);
}(FreshDirect));
