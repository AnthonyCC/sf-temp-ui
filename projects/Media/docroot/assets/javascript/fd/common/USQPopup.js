/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {

	var $=fd.libs.$;

  var USQPopup = Object.create({}, {
    container: {
      value: null,
      writable: true
    },
    opened:{
    	value:false,
    	writable:true
    },

    closeSelector: {
      value: '.USQ-close'
    },

    initPopup: {
      value: function () {

        var popupContent;
        if ( ! (this.container = $('#USQPopup')).length ) {

          popupContent = $('<div class="USQPopupContent"></div>').html(common.healthwarningpopup()).prepend('<a class="container-close USQ-close">Close</a>');
          this.container = $('<div id="USQPopup"></div>').hide().append(popupContent).appendTo($('body'));

          $(document).on('click', this.closeSelector, $.proxy(function(){
            this.close();
          },this));
        }
      }
    },

    isOpen: {
    	value:function(){
    		return this.opened;
    	}
    },
    
    isClosed: {
    	value:function(){
    		return !this.opened;
    	}
    },
    
    open: {
      value: function () {

        if (!this.container) {
          this.initPopup();
        }
        this.opened = true;
        this.container.show();
      }
    },

    close: {
      value: function () {

        if (this.container) {
          this.opened = false;
          this.container.hide();
        }
      }
    }

  });
  
  USQPopup.state=Bacon.mergeAll([$(document).asEventStream('click','#USQAcceptButton').map(true),
                                 $(document).asEventStream('click','#USQCancelButton').map(false)]).toProperty(FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1'));

  USQPopup.state.onValue(function(accepted){
	  if(accepted) {
		  fd.modules.common.utils.createCookie('freshdirect.healthwarning','1@'+FreshDirect.USQLegalWarning.getJSessionId());
	  }
  });
  
  fd.modules.common.utils.register("USQWarning", "Popup", USQPopup , fd);

}(FreshDirect));

