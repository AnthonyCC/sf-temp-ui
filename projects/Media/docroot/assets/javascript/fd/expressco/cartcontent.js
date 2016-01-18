/*global expressco, Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var requestCounter = 0;
  var focusedElementId;

  var cartcontent = Object.create(WIDGET,{
    signal:{
      value:'cartData'
    },
    template:{
      value: function(data){
        // need to change between templates based on data param
        var lineTemplate = $(this.placeholder).data('ec-linetemplate');
        var processFn = fd.modules.common.utils.discover(lineTemplate) || expressco.viewcartlines;

        this.updateTopCheckoutButton(data);

        return processFn(data);
      }
    },
    placeholder:{
      value:'#cartcontent'
    },
    updateTopCheckoutButton: {
      value: function (data) {
        var $placeholder = $('#checkoutbutton_top');

        if ($placeholder.size() && expressco.checkoutButton) {
          $placeholder.html(expressco.checkoutButton(data));
          $(document).trigger('checkoutFlowImmediate-change');
        }
      }
    },
    serialize:{
      value:function(){
        var data = {},
            changeCounter = 0;

        $(this.placeholder + ' [data-component="cartline"].modified').each(function(){
          var cartlineId = $(this).data('cartlineid'),
              changedComponent = $(this).data('changed'),
              componentType = {
                salesunit: 'csu',
                complaintreason: 'ccr',
                quantitybox: 'cqu'
              },
              changeType, changeValue, changedEl;

          if(changedComponent){
            changedEl = $('[data-component="' + changedComponent + '"]', this);
            changeType = componentType[changedComponent];

            if(changedComponent === 'quantitybox'){
              changeValue = changedEl.quantityBox('value');
            }
            else{
              changeValue = changedEl.val();
            }

            if(cartlineId && changeType && changeValue){
              data[cartlineId] = { type: changeType, data: changeValue };
              changeCounter++;
            }
          }
        });

        $(this.placeholder + ' [data-component="cartline"].deleted').each(function(){
          var cartlineId = $(this).data('cartlineid');
          data[cartlineId]={type:'rmv',data:null};
          changeCounter++;
        });

        return changeCounter ? data : null;
      }
    },
    onDeleteRecipe: {
      value: function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(e.target).closest('[data-recipe-section]').find('[data-component="cartline"]').addClass('deleted');
        $(cartcontent.placeholder).trigger('cartline-delete');
      }
    },
    onDeleteCartLine:{
      value:function(e){
        e.preventDefault();
        e.stopPropagation();
        $(e.target).closest('[data-component="cartline"]').addClass('deleted');
        $(cartcontent.placeholder).trigger('cartline-delete');
      }
    },
    onQuantityChange:{
      value:function(e){
        e.preventDefault();
        e.stopPropagation();
        $(e.target).closest('[data-component="cartline"]').addClass('modified').attr('data-changed', $(e.target).data('component'));
        $(cartcontent.placeholder).trigger('quantity-change');
      }
    },
    update: {
      value:function(e){
        if(e){
          e.preventDefault();
          e.stopPropagation();
        }
        $(cartcontent.placeholder).trigger('cartcontent-update');
      }
    },
    getRequestURI: {
      value:function(){
        return $(this.placeholder).data('ec-request-uri') || '/api/expresscheckout/cartdata';
      }
    },
    createRequestConfig : {
      value: function(userData){
        var reqType = userData && 'POST' || 'GET';

        return {  data:{
                    data:JSON.stringify({
                      header: { requestCounter: (++requestCounter) },
                      change: (userData || {})
                    })
                  },
                  url: this.getRequestURI(),
                  type: reqType,
                  dataType:'json'
                };
      }
    },
    watchChanges:{
      value: function(){
        var deleteStream = $(cartcontent.placeholder).asEventStream('cartline-delete');
        var updateStream = $(cartcontent.placeholder).asEventStream('cartcontent-update');
        var changeStream = $(cartcontent.placeholder).asEventStream('quantity-change').filter(function () {
          var $el = $('input[name="dontupdatecartlines"]:checked');

          return $el.size() === 0;
        });

        var dataStream = deleteStream.merge(updateStream).merge(changeStream);
        var bouncedDataStream = dataStream.debounce(500);

        var setDirty = dataStream.map(function(){ return true; });
        var setClean = bouncedDataStream.map(function(){ return false; } );

        /* an immediate user action should mark the form 'dirty', after some timing it should mark 'clean' */
        var isDirty = setDirty.merge(setClean).toProperty(false);

        var ajaxStream = bouncedDataStream.flatMapLatest(function(){
          var serializedData = cartcontent.serialize();
          var ajax = Bacon.fromPromise($.ajax(cartcontent.createRequestConfig(serializedData)));

          focusedElementId = document.activeElement && document.activeElement.id;

          return ajax;
        });

        ajaxStream.onError(function(){
          $(cartcontent.placeholder).
            html('<p class="error">Something went wrong. Please refresh the page to continue.</p>');

          return false;
        });

        /* if user makes the form dirty before the ajax response comes back then we should not update UI
           instead send the changed form again after some timing again */
        ajaxStream.filter(isDirty.not()).onValue(function(ajaxData){
          var $ph = $(cartcontent.placeholder);

          // check gogreen status
          if (!$ph.attr('gogreen-status')) {
            $ph.attr('gogreen-status', !!ajaxData.goGreen);
          }

          cartcontent.render(ajaxData);

          if (ajaxData.coremetrics) {
            fd.common.dispatcher.signal('coremetrics', ajaxData.coremetrics);
          }

          fd.common.dispatcher.signal('cartHeader', ajaxData);
          fd.common.dispatcher.signal('productSampleCarousel', ajaxData);

          if (focusedElementId) {
            try {
              document.getElementById(focusedElementId).focus();
            } catch (e) {}
          }

          fd.expressco.checkout.coFlowChecker.checkFlow();
          try {
            fd.common.transactionalPopup.close();
          } catch (e) {}
        });
      }
    },
    onEmptyCart: {
      value: function(e){
        e.preventDefault();
        e.stopPropagation();
        // adding class to all cartlines
        if(confirm('Are you sure that you want to delete all items from your cart?')){
          $(cartcontent.placeholder + ' [data-component="cartline"]').addClass('deleted');
          $(cartcontent.placeholder).trigger('cartline-delete');
        }
      }
    },
    onChangeETip: {
    	value: function(e){
    		e.preventDefault();
            e.stopPropagation();
            
            console.log("onTipSelectionChange >>>>>");            
			$('#tipApplied').hide();
        	$('#tipApply').show();
        	$('#tipAppliedTick').hide();
        	
        	
        	if($('#tipDropdown').val() == "Other Amount") {
        		console.log("Selected Other Amount");
        		$('#tipDropdown').hide();
        		$('#tipTextBox').show();
        	}
    	}
    },
    onTipEntered: {
    	value: function(e) {
    		e.preventDefault();
            e.stopPropagation();
            
            console.log("onTipEntered");
            
            $('#tipApply').show();
            $('#tipApplied').hide();
            
            var tip = $('#tipTextBox').val();
            var subTotalStr = $('#hiddenSubTotal').val();
            
            var subTotal = subTotalStr.substring(1);
            
            console.log("Sub Total : " + subTotal + " Tip : " + tip);
            
            var maximumTipAllowed = subTotal * 32/100;
            
            if(tip > maximumTipAllowed) {
            	console.log("Tip greater than maximum tip");
            	$('#tipApply').prop('disabled', true);
            	var roundedMaxTip = Math.round(maximumTipAllowed * 100)/100;
            	var innerHtml = $("<b>That's quite a tip, thank you!</b><br/><p>As of now, we cap all electronic tips at 32% of the subtotal, making the highest allowed tip to be $" + roundedMaxTip + " for this order.</p>")
            	$('#toolTipTextBox').html('').append(innerHtml);
            } else {
            	$('#toolTipTextBox').html('');            	
            	$('#tipApply').prop('disabled', false);
            }
    	}
    }
  });

  var cartHeader = Object.create(WIDGET,{
    signal:{
      value:'cartHeader'
    },
    template: {
      value:expressco.cartheader
    },
    placeholder:{
      value:'#cartheader'
    }
  });
  cartHeader.listen();

  var billingReferenceInfo = Object.create(WIDGET,{
    signal:{
      value:'billingReferenceInfo'
    },
    template: {
      value:expressco.billingReferenceInfo
    },
    placeholder:{
      value:'#billingReferenceInfoContainer'
    }
  });
  billingReferenceInfo.listen();

  var productSampleCarousel = Object.create(WIDGET,{
    signal:{
      value:'productSampleCarousel'
    },
    template: {
      value:expressco.productSampleCarouselWrapper
    },
    placeholder:{
      value:'#productsamplecarousel'
    },
    callback: {
      value: function(value){
        this.render(value);
        fd.components.carousel && fd.components.carousel.initialize();
      }
    }
  });
  productSampleCarousel.listen();

  var atcHandler = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'atcResult'
    },
    callback: {
      value: function (data) {
        cartcontent.update();
        // close delivery pass popup
        if (fd.expressco.deliverypasspopup) {
          fd.expressco.deliverypasspopup.close();
        }
      }
    }
  });

  var subtotalbox = Object.create(WIDGET, {
    signal: {
      value: 'subTotalBox'
    },
    template: {
      value: expressco.subTotalBox
    },
    placeholder: {
      value: '#subtotalbox1'
    }
  });

  cartcontent.listen();
  cartcontent.watchChanges();
  cartcontent.update();

  atcHandler.listen();

  subtotalbox.listen();

  $(document).on('click', cartcontent.placeholder + ' .cartline-delete',
    cartcontent.onDeleteCartLine.bind(cartcontent));

  $(document).on('click', cartcontent.placeholder + ' [data-component="removeRecipeButton"]',
    cartcontent.onDeleteRecipe.bind(cartcontent));

  $(document).on('quantity-change', cartcontent.placeholder + ' [data-component="cartline"]',
    cartcontent.onQuantityChange.bind(cartcontent));

  $(document).on('change', cartcontent.placeholder + ' [data-component="cartline"] select',
    cartcontent.onQuantityChange.bind(cartcontent));

  $(document).on('click', '#questions [data-component="emptycart"]',
    cartcontent.onEmptyCart.bind(cartcontent));

  $(document).on('click', cartcontent.placeholder + ' [data-component="updatecart"]',
    cartcontent.update.bind(cartcontent));

  $(document).on('click', '[data-component="update-cart"]', function () {
    $(cartcontent.placeholder).trigger('cartcontent-update');
  });
  
  $(document).on('change', cartcontent.placeholder + ' [data-component="changeETip"]', 
	cartcontent.onChangeETip.bind(cartcontent));
  
  $(document).on('input', cartcontent.placeholder + ' [data-component="tooltip"]', 
		  cartcontent.onTipEntered.bind(cartcontent));

  fd.modules.common.utils.register('expressco', 'cartcontent', cartcontent, fd);
}(FreshDirect));

