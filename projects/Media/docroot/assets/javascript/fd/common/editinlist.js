/*global common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
  "use strict";

	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var editinlistpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: 'save changes to this item'
    },
    customClass: {
      value: 'editinlistpopup'
    },
    bodyTemplate: {
      value: common.editinlistpopup
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '[data-component=product].changed .editinlist'
    },
    popupId: {
      value: 'editinlistpopup'
    },
    popupConfig: {
      value: {
        openonclick: true,
        halign: 'right'
      }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget);

        this.render({ itemName: $t.attr('data-itemname') });
        // #APPDEV-3901 - don't show popup
        // this.popup.show($t);
        // this.popup.clicked = true;
        this.popup.$trigger = $t;
        this.editInList(e);
      }
    },
    close: {
      value: function () {
        this.popup.hide();
        $('#'+this.popupId).find(this.bodySelector).html('');
      }
    },
    saveChange:{
    	value:function(listId,itemId,changeItem){
    		if(listId && itemId && changeItem) {
    	          changeItem.lineId = itemId;

    	          this.DISPATCHER.signal('server',{
    	              url: '/api/qs/shoppingLists/'+listId+'/'+itemId,
    	              data: { data: JSON.stringify(changeItem) },
    	              method: 'PUT'
    	          });    			
    		}
    	}
    },
    editInList: {
      value: function (changes) {
        var listId = this.popup.$trigger.attr('data-listid'),
            itemId = this.popup.$trigger.attr('data-itemid'),
            items = fd.components.AddToCart.atcFilter(fd.modules.common.productSerialize(this.popup.$trigger)),
            item = items && items[0];

        if (item) {
        	this.saveChange(listId,itemId,item);
        }
        this.popup.$trigger.closest('[data-component="product"]').removeClass('changed');
        this.close();
      }
    }
  });

  $(document).on('click', editinlistpopup.trigger, editinlistpopup.open.bind(editinlistpopup));
  $(document).on('click', '#' + editinlistpopup.popupId + ' .editinlist-button-save', editinlistpopup.editInList.bind(editinlistpopup));

  fd.modules.common.utils.register("modules.common", "editinlistpopup", editinlistpopup, fd);

}(FreshDirect));
