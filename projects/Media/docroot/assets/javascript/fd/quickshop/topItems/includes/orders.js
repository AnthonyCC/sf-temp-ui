/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DATALISTWIDGET = fd.quickshop.datalistWidget;

	function reduceValues(prev,current) {
		prev.push(current.value);
		return prev;
	};

	var orders = Object.create(DATALISTWIDGET,{
		signal:{
			value:'ORDERS BY DATE'
		},
		template:{
			value:quickshop.orders
		},
		placeholder:{
			value:'#orders .orders-list'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $('#orders'),
					result = $('input',el).serializeArray()
								.reduce(reduceValues,[]),
					listSize = $('[data-listsize]',el).data('listsize')*1;

				if(result.length === 1 && result[0]==='all') {
					result=[];
				}

				return {
					orderIdList:result
				};
			}
		},
		handleClick:{
			value:function(clickEvent){
			}
		},
		render:{
			value:function(data){
				var $orders = $('#orders'),
					isOpen = $orders.hasClass('open');

				$(this.placeholder).html(this.template(data));

		        if (!$('input:checked', $orders).is('#all_orders')) {
		        	$orders.addClass('locked')
		        		   .removeClass('close').addClass('open')
		        		   .find('.qs-menu-toggle').html('hide');
		        } else {
		        	$orders.removeClass('locked');
		        	if (isOpen) {
		        		   $orders.find('.qs-menu-toggle').html('hide');
		        	}
		        }

		        var ordersHeader = $('.orders-header');
				ordersHeader.attr('data-listsize',data.data.length);
				ordersHeader.find('.counter').html("("+data.data.length+")");
		        
			}
		},
		toggleOpen:{
			value:function(clickEvent){
				var $target = $(clickEvent.target),
					list = $('#orders'),
					isOpen = list.hasClass('open'),
					isLocked = list.hasClass('locked');

				if (!isLocked) {
					if(isOpen) {
						list.removeClass('open').addClass('close');
						$target.html('show');
					} else {
						list.removeClass('close').addClass('open');
						$target.html('hide');
					}
				}
			}
		},
		disableToggle:{
			value:function(changeEvent){
				var $target = $(changeEvent.target),
					list = $('#orders'),
					isAll = $target.is('#all_orders');

				if(isAll) {
					list.removeClass('locked');
				} else {
					list.addClass('locked').addClass('open');
					$('button',list).html('hide');
				}
			}
		}
	});

	$(document).on('click','#orders button',orders.toggleOpen.bind(orders));
	$(document).on('change','#orders input[type="radio"]',orders.disableToggle.bind(orders));
	orders.listen();

	fd.modules.common.utils.register("quickshop.topItems", "orders", orders, fd);
}(FreshDirect));
