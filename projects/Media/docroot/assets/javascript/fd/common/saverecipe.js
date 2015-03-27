/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var saverecipepopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: 'save recipe to your lists'
    },
    customClass: {
      value: 'saverecipepopup'
    },
    helpTemplate: {
      value: 'common.listhelppopup'
    },
    helpHeader: {
      value: 'about shopping lists'
    },
    bodyTemplate: {
      value: common.saverecipepopup
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '[data-component="saveRecipeButton"]'
    },
    popupId: {
      value: 'saverecipepopup'
    },
    popupConfig: {
      value: {
        openonclick: true,
        halign: 'right'
      }
    },
    open: {
      value: function (e) {
        // update shopping list data
        this.DISPATCHER.signal('server',{
            url: '/api/shoppinglist'
        });

        this.render();

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
    saveToList: {
      value: function (changes) {
        var recipeName = $('#'+this.popupId).find('#saverecipe-name').val(),
  		validator = fd.modules.common.listValidator(recipeName,$.makeArray($('#addtolist-selectList option')).map(function(e){
  			return e.innerHTML;
  		}));
        	
        if (validator.ok) {

	        this.DISPATCHER.signal('server',{
	            url: '/api/shoppinglist',
	            method: 'POST',
	            data: {
	              data: JSON.stringify({
	                listName: recipeName,
	                recipeId: this.popup.$trigger.data('recipeid')
	              })
	            }
	        });
	      } else {
	    	  $('#'+this.popupId).find('.error').html(validator.toString());    	  
	      }
      }
    }
  });

  var recipesavelistener = Object.create(fd.common.signalTarget,{
    signal:{
      value:'responseItems'
    },
    callback: {
      value: function (data) {
        var $popupBody;
        $popupBody = $('#'+saverecipepopup.popupId).find(saverecipepopup.bodySelector);

        if (data && data[0]) {
        // display verification
          $popupBody.html(common.addtolistdone({responseItems: data}));
        }

      }
    }
  });

  recipesavelistener.listen();

  $(document).on('click', saverecipepopup.trigger, saverecipepopup.open.bind(saverecipepopup));
  $(document).on('click', '#' + saverecipepopup.popupId + ' .saverecipe-button-go', saverecipepopup.saveToList.bind(saverecipepopup));
  $(document).on('click', '#' + saverecipepopup.popupId + ' .addtolist-button-ok', saverecipepopup.close.bind(saverecipepopup));


  fd.modules.common.utils.register("modules.common", "saverecipepopup", saverecipepopup, fd); 

}(FreshDirect));

