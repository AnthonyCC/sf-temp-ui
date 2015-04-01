/*global jQuery*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
	

	var	CARTDATA_HEADER = 'header',
			CARTDATA_ERRORMSG = 'errorMessage',
			CARTDATA_TOTAL = 'subTotal',
			CARTDATA_MODIFY = 'modifyOrder',
			CARTDATA_COREMETRICS = 'coremetricsScript',
			CARTDATA_SECTIONS = 'cartSections',
			CARTDATA_CARTLINES = 'itemCount',
      HEADER_COUNTER = 'requestCounter',
      API_URL = '/api/cartdata';
	
	
	
  var isDisabled = $('.globalnav_top').hasClass('disableCart'),
      $trigger = $("#sidecartbuttons .buttons"),
  		$clickTrigger = $("#sidecartbuttons .cart"),
      $sidecartbuttons = $("#sidecartbuttons"),
      $cart = $("#popupcart"),
      $cartContent = $("#popupcart .content"),
      $checkoutButton = $("#popupcart .checkout"),
      orderLines = $("#sidecartbuttons .summary .nritems"),
      summary = $("#sidecartbuttons .totalprice, #popupcart .totalprice");

  var cartContext;

  var requestCounter = 0;

  var partials={
    quantity:'<div class="qtyinput small" data-component="quantitybox" data-min="{{qMin}}" data-max="{{qMax}}" data-step="{{qInc}}"><button class="quantity_minus" data-component="quantitybox.dec">-</button><input class="qty" type="text" value="{{quantity}}" data-component="quantitybox.value"><button class="quantity_plus" data-component="quantitybox.inc">+</button></div>',
    salesunit:'<option value="{{id}}" {{#selected}}selected="selected"{{/selected}}>{{name}}</option>',
    section:'<tr class="section"><th colspan="4"><div class="title">{{title}}</div></th></tr>{{#cartLines}}{{>cartline}}{{/cartLines}}',
    remove:'<div class="remove"><button class="remove">remove</button></div>',
    cartline:'<tr class="cartline" name="{{id}}"><td class="remove">{{>remove}}</td><td class="quantity"><div class="quantity">{{#qu}}{{>quantity}}{{/qu}}{{^qu}}<select>{{#su}}{{>salesunit}}{{/su}}</select>{{/qu}}</div></td><td><div class="item">{{{descr}}}{{#confDescr}} ({{confDescr}}){{/confDescr}}{{#newItem}}<small class="new">(new)</small>{{/newItem}}</div></td><td><div class="price">{{price}}</div></td>'
  };


  function updateCartLines(ol) {
    orderLines.html('<em>'+Math.ceil(ol)+'</em> ' + ((ol>1) ? 'items' : 'item'));
  };

  function updateTotal(total) {
    summary.html(total);
  };

  function updateSections(cartData) {
    var cartHtml=$.mustache("{{#cartSections}}{{>section}}{{/cartSections}}",cartData,partials);
    $cartContent.html(cartHtml);
    $('#popupcart tr.cartline select').each(function(opt){
      $(this).data('oldVal', $(this).val());
    });
  }
  
  function updateModifyNote(cartData) {
  	if(cartData[CARTDATA_MODIFY]) {
    	$cart.addClass('modifycart');  		
  	} else {
    	$cart.removeClass('modifycart');  		
  	}
  }

  function createRequestHeader(){
    var obj = {};
    obj[HEADER_COUNTER] = (++requestCounter);
    return obj;
  };

  function createAjaxRequest(data){
    return {  data:{ 
                change:JSON.stringify({  
                  header: createRequestHeader(), 
                  data:(data || {}) 
                })
              },
              url: API_URL,
              type:'POST',
              dataType:'json'
            };
  };

  var collectData=function(){
    var data={};

    $('#popupcart tr.cartline.modified').each(function(){
      var cartlineId=$(this).attr('name'),
          value=$('select',this).val();
      if(value) {
        data[cartlineId]={type:'csu',data:value}
      } else {
        value=$('[data-component="quantitybox"]',this).quantityBox('value');
        if(value !== undefined) {
          data[cartlineId]={type:'cqu',data:value}
        }
      }
    });

    $('#popupcart tr.cartline.removed').each(function(){
      var cartlineId=$(this).attr('name');
        data[cartlineId]={type:'rmv',data:null}
    });

    return data;
  };

  /* this shouldn't exist */
  var coremetricsEval = function(cartData){
    if(cartData === false ) return false;
    try {
	    if(cartData[CARTDATA_COREMETRICS]) {
	    	eval(cartData[CARTDATA_COREMETRICS]);
	    }
    } catch (e) {
		// Ignore any errors coming from coremetrics:
    	// if coremetrics fails we don't want the whole js code to die ...
    	// console.log( "coremetrics script has failed! " + e );
	}
  }

  var updateHtml = function(cartData) {
    if(cartData === false ) {
      $cart.addClass('empty');
      return false;
    }

    if( CARTDATA_CARTLINES in cartData ) {
      updateCartLines(cartData[CARTDATA_CARTLINES]);
    }

    if( CARTDATA_TOTAL in cartData ) {
      updateTotal(cartData[CARTDATA_TOTAL]);
    }

    if( CARTDATA_SECTIONS in cartData ) {
      updateSections(cartData);
    }

    if(CARTDATA_MODIFY in cartData) {
    	updateModifyNote(cartData);
    }

    if (cartData[CARTDATA_CARTLINES]===0) {
      $cart.addClass('empty');
    } else {
      $cart.removeClass('empty');
    }

    $cart.removeClass('loading');
    $checkoutButton.removeClass('loading');
  }

  var errorHandler = function(jqXHR) {
    $("#popupcart .body").html('<p class="error">Something went wrong. Please refresh the page to continue.</p>');
    return false;
  }

  var cartProperties = {
    update:{
      value: function(){
        $cart.trigger('popupcart-update');
      }
    },
    collectData:{
      value:collectData
    }
  };

  cartContext = Object.create(new fd.modules.common.PopupContent(
    $cart,
    $trigger,
    {
      alignTo: $sidecartbuttons,
      valign: 'bottom',
      halign: 'right',
      stayOnClick: true,
      closeHandle: '#popupcart .close',
      disabled: isDisabled,
      $clickTrigger: $clickTrigger
    }
  ), cartProperties);

  $(document).on('change','#popupcart tr.cartline',function(e){
    var $this = $(this),
        $select = $this.find('select');
    $this.addClass("modified");
    $this.addClass("modified-" + ($select.val() > $select.data('oldVal') ? "inc" : "dec"));
    $select.data("oldVal", $select.val());
  });

  $(document).on('quantity-change','#popupcart tr.cartline',function(e){
    $(this).addClass("modified");
  });
  $(document).on('quantity-change-inc','#popupcart tr.cartline',function(e){
    $(this).addClass("modified-inc");
  });
  $(document).on('quantity-change-dec','#popupcart tr.cartline',function(e){
    $(this).addClass("modified-dec");
  });

  $(document).on('click','#popupcart tr.cartline',function(e){
    if($(e.target).hasClass("remove")) {
      $(this).addClass("removed");
    }
  });

  var qcStream = $cart.asEventStream('quantity-change');
  var sucStream = $cart.asEventStream('change','select');
  var rmStream = $cart.asEventStream('click','.remove');

  var dataChange = qcStream.merge(sucStream).merge(rmStream);
  var bouncedDataChange = dataChange.debounce(1500);

  /* dirty state */
  var setDirty = dataChange.map(function(){ return true });
  var setClean = bouncedDataChange.map(function(){ return false } );
  var isDirty = setDirty.merge(setClean).toProperty(false);

  /* on change, send the data */
  var ajaxStream = bouncedDataChange.merge($cart.asEventStream('popupcart-update')).flatMapLatest(function(){
    var data = cartContext.collectData();
    var ajaxData = createAjaxRequest(data);
    var ajax = Bacon.fromPromise($.ajax(ajaxData)); 

    /* if there is a value, then eval the coremetrics scripts */
    ajax.onValue(coremetricsEval);

    /* show spinner */
    var state = ajax.map(false).toProperty(true);
    Bacon.later(500,'show').filter(state).onValue(function(v){
      if(v==='show') {
        if ($cartContent.is(':empty')) {
          $cart.addClass('loading');
        } else {
          $checkoutButton.addClass('loading');
        }
      }
    });

    /* returns a stream for flatMapLatest */
    return ajax;
  });

  /* handle errors */
  ajaxStream.onError(errorHandler);

  /* update the ui with on change of ajaxstream if there is no pending modifications 
   * if this update is last (from the flatMapLatest) but the form is dirty, then drop this value,
   * else update the form */
  ajaxStream.filter(isDirty.not()).onValue(updateHtml);
  
  
/* uncomment this for debug 

  isDirty.onValue(function(e){
    if(e) {
      $('.header',$cart).addClass('dirty');
    } else {
      $('.header',$cart).removeClass('dirty');
    }
  });
*/

  /* initial data request */
  cartContext.update();

  window.updateYourCartPanel = cartContext.update.bind(cartContext);

  fd.modules.common.utils.register("modules.header", "Cart", cartContext , fd);

}(FreshDirect));

