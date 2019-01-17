/*global quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
  var DATALISTWIDGET = fd.quickshop.datalistWidget;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var ORDERLIMIT = 15;

	function reduceValues(prev,current) {
		prev.push(current.value);
		return prev;
  }

	var orders = Object.create(WIDGET,{
		signal:{
			value:'orders'
		},
		template:{
			value: quickshop.ordersQS22
		},
		placeholder:{
			value:'#orders .orders-list-cont'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $('#orders'),
            result = $('input',el).serializeArray().reduce(reduceValues,[]);

				if(result.length === 1 && result[0]==='all') {
					result=[];
				}

				return {
					orderIdList:result
				};
			}
		},
    checkMaxOrder: {
      value: function () {
        var orderList = $('#orders input:checked');

        if (orderList.length >= ORDERLIMIT) {
          $(".orders,.orderlist").addClass("maxorder");
        } else {
          $(".orders,.orderlist").removeClass("maxorder");
        }

        return orderList.length;
      }
    },
		handleClick:{
			value:function(e){
        if (this.checkMaxOrder() > ORDERLIMIT) {
          e.stopPropagation();
          e.preventDefault();
        } else {
          $(document).trigger("orders-change");
        }
			}
		},
    allOrders: {
			value: function (e) {
        var $t = $(e.currentTarget),
            id = $t.attr('id'),
            checked = $t.prop('checked'),
            $inputs = $('.orders input');

        if (id === 'all_orders') {
          $inputs.attr('checked', !checked);
          $t.attr('checked', checked);
        } else {
          $('.orders #all_orders').attr('checked', false);
        }
			}
    },
		render:{
			value:function(data){
        var ordersHeader = $('.orders-header'),
            orderNum = data.firstMenuItems.length + 
                       data.pastOrdersGroupedByYear.reduce(function (p, c) { return p + c.orders.length; }, 0);

				$(this.placeholder).html(this.template(data));

				ordersHeader.attr('data-listsize',orderNum);
				ordersHeader.find('.counter').html("("+orderNum+")");
        this.checkMaxOrder();
			}
    },
    initModule: {
      value: function () {
        var orders = this;

        orders.listen();
        moreorderspopup.render();

        $(document).on('change', '.orders input', orders.allOrders.bind(orders));
        $(document).on('click', '.orders input,#moreorderspopup input', orders.handleClick.bind(orders));
        $(document).on('click', moreorderspopup.trigger, moreorderspopup.open.bind(moreorderspopup));
        $(document).on('click', '#' + moreorderspopup.popupId + ' .close', moreorderspopup.close.bind(moreorderspopup));
        $(document).on('change', '#' + moreorderspopup.popupId + ' input', moreorderspopup.inputChange.bind(moreorderspopup));
      }
		}
	});

  var moreorderspopup = Object.create(POPUPWIDGET, {
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'moreorderspopup'
    },
    hasClose: {
      value: true
    },
    $trigger: {
      value: null
    },
    bodySelector:{
        value:'.qs-popup-content'
    },
    trigger: {
      value: '.orders .seemore'
    },
    popupId: {
      value: 'moreorderspopup'
    },
    popupConfig: {
      value: {
        zIndex: 200,
        ghostZIndex: 210,
        hidecallback: fd.quickshop.common.itemList.scrollToTop
      }
    },
    inputChange: {
      value: function (e) {
        var $t = $(e.currentTarget),
            checked = $t.prop('checked'),
            $inputs = $('[id='+$t.attr('id')+']');

        if (orders.checkMaxOrder() >= ORDERLIMIT) {
          checked = false;
        }

        $inputs.attr('checked', checked);
        $('.qs-content').first().addClass('noscroll');
      }
    },
    close: {
      value: function () {
        if (this.popup) {
          this.popup.hide();
        }

        return false;
      }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget),
            bt = function () {
              return $t.siblings('.moreorders').html();
            };

        this.refreshBody(null, bt, this.headerContent);
        this.popup.show($t, null);
        this.popup.clicked = true;
      }
    }
  });

  var moreordersalign = function () {
    var triggerRect = $(moreorderspopup.trigger)[0].getBoundingClientRect(),
        qsContainerRect = $('.qs-container')[0].getBoundingClientRect(),
        elHeight = this.$el.height(),
        deltaH = triggerRect.bottom - qsContainerRect.top,
        top = elHeight > deltaH ? qsContainerRect.top + $(document).scrollTop() : triggerRect.bottom - elHeight + $(document).scrollTop(),
        arrowPos = triggerRect.top - top + $(document).scrollTop();

    this.$el.css({
      top: top,
      left: triggerRect.right
    });

    this.$el.find('span.bubblearrow').css({
      top: arrowPos
    });
  };

	var orders2_0 = Object.create(DATALISTWIDGET,{
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
        $(document).trigger("orders-change");
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
		},
    initModule: {
      value: function () {
        var orders = this;

        orders.listen();

        $(document).on('click','#orders button',orders.toggleOpen.bind(orders));
        $(document).on('change','#orders input[type="radio"]',orders.disableToggle.bind(orders));
        $(document).on('click', '.orders input', orders.handleClick.bind(orders));
      }
    }
	});

	fd.modules.common.utils.registerModule("quickshop.pastOrders", "orders", orders, fd, "quickshop", "2_2");
	fd.modules.common.utils.registerModule("quickshop.pastOrders", "orders", orders2_0, fd, "quickshop", "2_0");
  fd.modules.common.utils.register("popups.alignment", "moreordersalign", moreordersalign, fd);

  fd.utils.initModule('quickshop.pastOrders.orders', fd);
}(FreshDirect));
