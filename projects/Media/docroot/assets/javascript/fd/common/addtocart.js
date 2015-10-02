/*global jQuery*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;

	function filterValue(val) {
		return !val.invalid && val['salesUnit'] && parseFloat(val['quantity'])>0;
	};
	
	function atcFilter(items) {
		var productList; 
		
		return items.map(function(item){
			delete item.DOMElement;
			delete item.required;
			delete item.isAlcoholic;
			return item;
		});
	}

	function atcHandler(event){
		
		var productList = atcFilter(event.atcList),
			request = { items:productList },
      eventSource = $(document.body).data('cmeventsource');
		
		$.extend(request,event.ATCMeta,event.cmData);
    
    if (fd.components && fd.components.atcInfo) {
      fd.components.atcInfo.setServerMessage(request.items);
    }

    if (!request.eventSource && eventSource) {
      request.eventSource = eventSource;
    }

    if (event.ignoreRedirect) {
      request.ignoreRedirect = true;
    }

    $('[data-component="ATCButton"]').addClass('ATCinProgress');
    
    //Close the popup after added product to the cart with delay on mobile
    if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
    	setTimeout(function(){
        fd.common.transactionalPopup.popup.hide();
      }, 1000);    	
    }
    
    
		fd.common.dispatcher.signal('server',{
			url:'/api/addtocart',
			data:{data:JSON.stringify(request)},
			method:'POST'			
		});			
	}
	
	function requiredValidator(items){
		var valid = true;
		
		items.forEach(function(item){
			var required = item.required;
			if(required.length) {
				Object.keys(item).forEach(function(data){
					if(required.indexOf(data) > -1 && (!item[data] || item[data]==='0') ) {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').addClass('missing-data');
						item.DOMElement.find('[data-productdata-name="'+data+'"]').parents('.errorcontainer').first().addClass('haserror');
						item.invalid = true;
						valid = false;
					} else {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').removeClass('missing-data');
						item.DOMElement.find('[data-productdata-name="'+data+'"]').parents('.errorcontainer').first().removeClass('haserror');
          }
				});
				Object.keys(item.configuration || {}).forEach(function(data){
					if(required.indexOf(data) > -1 && !item.configuration[data]) {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').addClass('missing-data');
						item.DOMElement.find('[data-productdata-name="'+data+'"]').parents('.errorcontainer').first().addClass('haserror');
						item.invalid = true;
						valid = false;
					} else {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').removeClass('missing-data');
						item.DOMElement.find('[data-productdata-name="'+data+'"]').parents('.errorcontainer').first().removeClass('haserror');
          }
				});
			}
		});
		
		return valid;
	}
	
	function usqValidator(items) {
		return items.some(function(item){
			return item.isAlcoholic === 'true'; 
		});		
	}
	
	
	var ATC_BUS = new Bacon.Bus();
	var BASIC_ATC = ATC_BUS.filter(function(event){ return requiredValidator(event.items)}).toProperty();
	
	Bacon.combineWith(function(value,usqState){
		value.usqState = usqState;
		return value;
	},BASIC_ATC.map(function(event){
		event.containsAlcoholic=usqValidator(event.items);
		return event;
	}),fd.USQWarning.Popup.state).flatMapLatest(function(value){
		if(value.containsAlcoholic && !value.usqState && fd.USQWarning.Popup.isClosed()){
			fd.USQWarning.Popup.open();
			return fd.USQWarning.Popup.state.changes().flatMapLatest(function(accepted){
				value.usqState = accepted;
				return Bacon.once(value);
			})
		} else {
			return Bacon.once(value);
		}
	}).map(function(value){
		var newItems = value.items;
		fd.USQWarning.Popup.close();
		if(value.containsAlcoholic && !value.usqState) {
			newItems = newItems.filter(function(item){
				return !item.isAlcoholic;
			});
		}
		value.items = newItems.filter(filterValue); 
		return value;
	}).filter(function(event){
		return event.items.length > 0;
	}).onValue(function(event){
		triggerATC(event.items,{},event.target,event.eventSource,event.ignoreRedirect);
	});

	
	function triggerATC(items,meta,triggerElement,eventSource,ignoreRedirect){
		$(triggerElement || document.body).trigger({
			type:'addToCart',
			atcList:items,
			ATCMeta:(meta || {}),
			valid:true,
			cmData:eventSource ? {eventSource: eventSource} : {},
      ignoreRedirect: !!ignoreRedirect
		});		
	}
	
	function addToCart(element, extraData) {
		var items = fd.modules.common.productSerialize(element, true);
    ATC_BUS.push($.extend({items: items}, extraData));
	}
	
	$(document).on('click','[data-component="ATCButton"]',function(e){
		var items = fd.modules.common.productSerialize(e.target, true),
        cartdata = fd.modules.common.getCartData(e.target),
        amount,
        $t = $(e.target);

    // if button is blocking and ATC in progress, then do nothing
    if ($t.hasClass('ATCinProgress') && $t.attr('data-atc-blocking') || $t.attr('data-atc-disable')) {
      return;
    }

		e.items = items;

    // set amount on button
    if (items.length) {
      amount = +items[0].quantity;
      
      if (cartdata.max && (cartdata.max < amount + (cartdata.incart || 0))) {
        amount = cartdata.max - (cartdata.incart || 0);
      }
      
      e.currentTarget.setAttribute('data-amount', amount);
    }

    var ignoreRedirect = e.currentTarget && e.currentTarget.getAttribute('data-ignoreredirect');
    ignoreRedirect = ignoreRedirect && ignoreRedirect === 'true';

    if(ignoreRedirect){
      e.ignoreRedirect = true;
    }

		ATC_BUS.push(e);
	});

	$(document).on('addToCart',atcHandler);

  function allConfirmProcess(data) {
    var $cont = $(data.container),
        $products = $cont.find('[data-component="product"]').filter(function (i, el) {
          return !$(el).hasClass('unavailable');
        }),
        sumsubtotal = 0;

    data.header = "Add " + $products.size() + " items to cart?";

    $products.each(function () {
      var subtotal = $(this).find('[data-component="product-controls"] [data-component="subtotal"] b, [data-component="product-controls"] [data-component="subtotal"] span').text();

      if (subtotal) {
        sumsubtotal += +(subtotal.substring(1));
      }
    });

    data.message = "Estimated Subtotal: $" + sumsubtotal.toFixed(2);

    return data;
  }
	
  fd.modules.common.utils.register("components", "AddToCart", {
    addToCart:addToCart,
    requiredValidator:requiredValidator,
    atcFilter:atcFilter,
    triggerATC:triggerATC,
    allConfirmProcess: allConfirmProcess
  } , fd);

}(FreshDirect));

