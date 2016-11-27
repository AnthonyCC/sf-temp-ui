/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {

  var $=fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  var ModifyBRDPopup1 = Object.create(WIDGET, {
    signal: {
      value: 'pendingPopupData'
    },
    template: {
      value: common.modifyBRDPopup1
    },
    placeholder: {
      value: '#ModifyBRDContainer'
    },
    data: {
      value: {},
      writable: true
    },
    callback:{
      value:function( data ) {
        var $ph = $(this.placeholder);

        if ($ph.length === 0) {
          $('<div id="'+this.placeholder.substr(1)+'"></div>').appendTo(document.body);
        }

        this.render(data);
        this.data = data;

        // focus on open
        try {
          fd.common.transactionalPopup.close();
          $(this.placeholder).find('button, a[href], input').not(':hidden').first().focus();
        } catch (e) {}
      }
    },

    popupId: {
      value: '#MBRDPopup1'
    },
    close: {
      value: function () {
        $(this.popupId).hide();
      }
    },

    newOrderClick: {
      value: function () {
        var items = fd.modules.common.productSerialize(this.data.pendingItems);
        // .map(function(item){
        //   return {
        //     atcItemId: item.itemId,
        //     categoryId: item.catId,
        //     productId: item.productId,
        //     quantity: item.quantity.quantity,
        //     salesUnit: item.salesUnit.filter(function(su){return su.selected;})[0].id,
        //     skuCode: item.skuCode
        //   };
        // });

        fd.components.AddToCart.triggerATC(items,{newOrder:true}, $(this.placeholder), this.data.eventSource, (this.data.mobWeb) ? true : this.data.ignoreRedirect);

        this.close();
      }
    },
    modifyClick: {
      value: function () {
        this.close();
        DISPATCHER.signal('pendingPopupModifyData', this.data);
      }
    }

  });

  var ModifyBRDPopup2 = Object.create(WIDGET, {
    signal: {
      value: 'pendingPopupModifyData'
    },
    template: {
      value: common.modifyBRDPopup2
    },
    placeholder: {
      value: '#ModifyBRDContainer'
    },
    callback:{
      value:function( data ) {
        this.render(data);
        this.data = data;
        // focus on open
        try {
          fd.common.transactionalPopup.close();
          $(this.placeholder).find('button, a[href], input').not(':hidden').first().focus();
        } catch (e) {}
      }
    },

    popupId: {
      value: '#MBRDPopup2'
    },
    close: {
      value: function () {
        $(this.popupId).hide();
      }
    },
    orderInput: {
      value: '#MBRDOrder'
    },
    cartItemToggles: {
      value: null,
      writable: true
    },

    cancelClick: {
      value: function () {
        var items = fd.modules.common.productSerialize(this.data.pendingItems);

        fd.components.AddToCart.triggerATC(items,{newOrder:true}, $(this.placeholder), this.data.eventSource, (this.data.mobWeb) ? true : this.data.ignoreRedirect);
        this.close();
      }
    },
    modifyClick: {
      value: function (e) {
        var items = fd.modules.common.productSerialize(e.target);
        var orderId = $(this.orderInput).val();

        fd.components.AddToCart.triggerATC(items,{orderId:orderId}, $(this.placeholder), this.data.eventSource, (this.data.mobWeb) ? true : this.data.ignoreRedirect);
        this.close();
      }
    },

    selectAllClick: {
      value: function (e) {
        this.toggleCartItems(true);
      }
    },
    deSelectAllClick: {
      value: function (e) {
        this.toggleCartItems();
      }
    },
    toggleCartItems: {
      value: function (on) {
        if (!this.cartItemToggles) {
          this.cartItemToggles = $(this.popupId).find('[data-component="productToggle"]');
        }
        this.cartItemToggles.prop("checked", !!on);
      }
    },

  });

  ModifyBRDPopup1.listen();
  ModifyBRDPopup2.listen();

  // $(document).on('click', ModifyBRDPopup1.popupId + ' .MBRD-close', ModifyBRDPopup1.close.bind(ModifyBRDPopup1));
  // $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-close', ModifyBRDPopup2.close.bind(ModifyBRDPopup2));

  $(document).on('click', ModifyBRDPopup1.popupId + ' .MBRD-close', ModifyBRDPopup1.newOrderClick.bind(ModifyBRDPopup1)); // :(
  $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-close', ModifyBRDPopup2.cancelClick.bind(ModifyBRDPopup2));

  $(document).on('click', ModifyBRDPopup1.popupId + ' .MBRD-neworder', ModifyBRDPopup1.newOrderClick.bind(ModifyBRDPopup1));
  $(document).on('click', ModifyBRDPopup1.popupId + ' .MBRD-modify', ModifyBRDPopup1.modifyClick.bind(ModifyBRDPopup1));

  $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-cancel', ModifyBRDPopup2.cancelClick.bind(ModifyBRDPopup2));
  $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-modify', ModifyBRDPopup2.modifyClick.bind(ModifyBRDPopup2));

  $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-selectall', ModifyBRDPopup2.selectAllClick.bind(ModifyBRDPopup2));
  $(document).on('click', ModifyBRDPopup2.popupId + ' .MBRD-deselectall', ModifyBRDPopup2.deSelectAllClick.bind(ModifyBRDPopup2));


}(FreshDirect));

