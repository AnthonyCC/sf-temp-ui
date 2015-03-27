/*global quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
  var QSVersion = fd.utils.getActive("quickshop");

	var pager = Object.create(WIDGET,{
		signal:{
			value:'pager'
		},
		template:{
			value: QSVersion !== "2_0" ? quickshop.pagerQS22 : quickshop.pager
		},
		placeholder:{
			value:'.pagination'
		},
		serialize:{
			value:function(el){
				var $el = $(el);
				return {
					activePage : parseInt($el.data('activepage'),10),
					pageSize : parseInt($el.data('pagesize'),10),
					itemCount : parseInt($el.data('itemcount'),10)
        };
			}
		},
		handleClick:{
			value:function(clickEvent){
				var clicked = $(clickEvent.target),
					component = clicked.data('component'),
					parent = clicked.parents('[data-component="pager"]'),
					newPage, activePage, newPageSize = parseInt(parent.data('pagesize'),10);
				if(component === 'pager-prev') {
					activePage = parent.data('activepage')*1;
					newPage=Math.max(activePage-1,0);
				} else if(component === 'pager-next') {
					activePage = parent.data('activepage')*1;
					newPage=Math.min(activePage+1,Math.ceil(parent.data('itemcount')/parent.data('pagesize')));
				} else if(component === 'showall'){
					newPageSize=parseInt(clicked.data('showall'),10);
					newPage=0;
				} else {
					newPage = clicked.data('page');
				}
				if(newPage!==undefined) {
					var json = this.serialize(parent);
					json.activePage=newPage;
					json.pageSize = newPageSize;

					this.render(json);
					$(this.placeholder).first().trigger('page-change');
				}
			}
		}
	});

	pager.listen();
	$(document).on('click',pager.placeholder,pager.handleClick.bind(pager));

	fd.modules.common.utils.register("quickshop.common", "pager", pager, fd);
}(FreshDirect));
