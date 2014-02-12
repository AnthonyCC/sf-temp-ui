/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
	var POPUPWIDGET = fd.modules.common.popupWidget;

	var listPopup = Object.create(POPUPWIDGET,{
	    signal:{
	        value:'listInfos'
	      },
	      update: {
	        value: function (data) {
	          this.DISPATCHER.signal('server', {
	            url: '/api/shoppinglist',
	            data: {
	              data:JSON.stringify(data || {})
	            }
	          });
	        }
	      },
	      headerContent: {
	        value: '',
	        writable:true
	      },
	      customClass: {
	        value: 'addtolistpopup'
	      },
	      helpTemplate: {
	        value: 'common.listhelppopup'
	      },
	      helpHeader: {
	        value: 'about shopping lists'
	      },
	      bodyTemplate: {
	        value: null,
	        writable:true
	      },
	      $trigger: {
	        value: null,
	        writable:true
	      },
	      trigger: {
	        value: '',
	        writable:true
	      },
	      popupId: {
	        value: '',
	        writable:true
	      },
	      popupConfig: {
	        value: {
	          openonclick: true,
	          halign: 'right'
	        }
	      },
	      open: {
	        value: function (e) {
	          this.DISPATCHER.signal('server',{
	              url: '/api/shoppinglist'
	          });

	          this.popup.show($(e.currentTarget));
	          this.popup.clicked = true;
	        }
	      },
	      close: {
	        value: function () {
	          this.popup.hide();
	          $('#'+this.popupId).find(this.bodySelector).html('');
	        }
	      },
	      validName:{
	    	value: function( name, lists ) {
	    		var trimmed = name.trim(),
	    			result = new String('');
	    		if(trimmed.length > 0){
	    			if(lists.indexOf(name)>-1) {
	    				result = new String( 'Oops! That name is taken!' );
	    				result.taken = true;
	    			} else {
	    				result.ok = true;
	    			}
	    		} else {
	    			result = new String('Name cannot be empty.');
	    			result.empty = true;
	    		}
	    		
	    		return result;
	    	}  
	      },
	      saveToList: {
	        value: function (changes) {
	          var listId = $('#'+this.popupId).find('select[name=selectList]').val();
	          $('#'+this.popupId).find('button.enabled').removeClass('enabled');
	          this.DISPATCHER.signal('server',{
	              url: '/api/shoppinglist',
	              method: 'POST',
	              data: {
	                data: JSON.stringify({
	                  listId: listId,
	                  items: fd.components.AddToCart.atcFilter(
	                  			fd.modules.common.productSerialize(this.popup.$trigger))
	                })
	              }
	          });
	        }
	      },
	      saveToNew: {
	        value: function (changes) {
	          var listName = $('#'+this.popupId).find('#addtolist-newList').val()
	          		validator = this.validName(listName,$.makeArray($('#addtolist-selectList option')).map(function(e){
	          			return e.innerHTML;
	          		}));

	          if (validator.ok) {
	              $('#'+this.popupId).find('button.enabled').removeClass('enabled');
	            this.DISPATCHER.signal('server',{
	                url: '/api/shoppinglist',
	                method: 'POST',
	                data: {
	                  data: JSON.stringify({
	                    listName: listName,
	                    items: fd.components.AddToCart.atcFilter(
	                  		  	fd.modules.common.productSerialize(this.popup.$trigger))
	                  })
	                }
	            });
	          } else {
	        	  $('#'+this.popupId).find('.error').html(validator.toString());
	          }
	        }
	      }
	    });
	
	
  var addtolistpopup = Object.create(listPopup,{
    headerContent: {
      value: 'add to list'
    },
    bodyTemplate: {
      value: common.addtolistpopup
    },
    $trigger:{
    	value:null
    },
    trigger: {
      value: '[data-component=product] button.addtolist'
    },
    popupId: {
      value: 'addtolistpopup'
    }
  });

  var createlistpopup = Object.create(listPopup,{
    headerContent: {
      value: 'create a new list'
    },
    bodyTemplate: {
      value: common.createlistpopup
    },
    $trigger:{
    	value:null
    },
    trigger: {
      value: '.qs-actions button.qs-addtolist'
    },
    popupId: {
      value: 'createlistpopup'
    }
  });

  var listaddlistener = Object.create(fd.common.signalTarget,{
    signal:{
      value:'responseItems'
    },
    callback: {
      value: function (data) {
        var $popupBody;
        if (data && data.length === 1) {
          $popupBody = $('#'+addtolistpopup.popupId).find(addtolistpopup.bodySelector);
        } else if (data) {
          $popupBody = $('#'+createlistpopup.popupId).find(createlistpopup.bodySelector);
        }

        if (data && data[0]) {
          // display verification
          $popupBody.html(common.addtolistdone({responseItems: data}));

          // close create list popup
          // createlistpopup.close();
        }

      }
    }
  });

  listaddlistener.listen();
  createlistpopup.listen();
  addtolistpopup.listen();
  addtolistpopup.update();

  $(document).on('click', addtolistpopup.trigger, addtolistpopup.open.bind(addtolistpopup));
  $(document).on('click', createlistpopup.trigger, createlistpopup.open.bind(createlistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-add.enabled', addtolistpopup.saveToList.bind(addtolistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-go.enabled', addtolistpopup.saveToNew.bind(addtolistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-ok', addtolistpopup.close.bind(addtolistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-add.enabled', createlistpopup.saveToList.bind(createlistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-go.enabled', createlistpopup.saveToNew.bind(createlistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-ok', createlistpopup.close.bind(createlistpopup));

  fd.modules.common.utils.register("modules.common", "addtolistpopup", addtolistpopup, fd);
  fd.modules.common.utils.register("modules.common", "listValidator", listPopup.validName, fd);
  fd.modules.common.utils.register("modules.common", "createlistpopup", createlistpopup, fd);

}(FreshDirect));

