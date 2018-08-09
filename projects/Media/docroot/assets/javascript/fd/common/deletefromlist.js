/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var deletefromlistpopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: 'deletefromlistpopup'
    },
    bodyTemplate: {
      value: common.deletefromlistpopup
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '[data-component=product] .deletefromlist'
    },
    popupId: {
      value: 'deletefromlistpopup'
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
        this.popup.show($t);
        this.popup.clicked = true;
      }
    },
    close: {
      value: function () {
        this.popup.hide();
        $('#'+this.popupId).find(this.bodySelector).html('');
      }
    },
    deleteFromList: {
      value: function (changes) {
    	
    /*APPDEV-4151 - Changes for displaying Spinning wheel : START*/ 
    	
  	  var listArea = $(this.popup.$trigger).closest('.itemlist-item').parent().parent(); 
	  var spin = listArea.append('<img style="position:fixed;left:50%;top:50%;" id="load" src="/media_stat/images/navigation/spinner.gif"/>'); 
	  listArea.css("opacity","0.2"); 
	  
	  var timeOutVar = setTimeout(function() { 
		  $('#load').hide();
		  listArea.css("opacity","1"); 
	  	  }, 2000); 
    
	  /*APPDEV-4151 - Changes for displaying Spinning wheel : END*/ 
    	
        var listId = this.popup.$trigger.attr('data-listid'),
            itemId = this.popup.$trigger.attr('data-itemid'),
            items = fd.components.AddToCart.atcFilter(fd.modules.common.productSerialize(this.popup.$trigger)),
            item = items && items[0],
            $itemEl;

        if (item) {
          item.lineId = itemId;
          item.deleteItem = true;
          if(listId) {
        	  item.listId = listId;
          }

          this.DISPATCHER.signal('server',{
              url: '/api/qs/shoppingLists/'+listId+'/'+itemId,
              data: { data: JSON.stringify(item) },
              method: 'PUT'
          });
        
          $itemEl = $(this.popup.$trigger).closest('.itemlist-item');

          if ($itemEl.length) {
            $itemEl.remove();
          }

          $(document).trigger('list-change');
        }

        this.close();
      }
    }
  });

  $(document).on('click', deletefromlistpopup.trigger, deletefromlistpopup.open.bind(deletefromlistpopup));
  $(document).on('click', '#' + deletefromlistpopup.popupId + ' .deletefromlist-button-delete', deletefromlistpopup.deleteFromList.bind(deletefromlistpopup));
  $(document).on('click', '#' + deletefromlistpopup.popupId + ' .deletefromlist-button-cancel', deletefromlistpopup.close.bind(deletefromlistpopup));

  fd.modules.common.utils.register("modules.common", "deletefromlistpopup", deletefromlistpopup, fd);

}(FreshDirect));

