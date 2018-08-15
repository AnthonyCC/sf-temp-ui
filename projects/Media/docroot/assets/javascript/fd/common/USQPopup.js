/*global jQuery,common,Bacon*/
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

        	$.ajax({
        		url: '/media/editorial/site_pages/health_warning_overlay_2016.html',
        		context: this
        	}).done(function(data) {
        		popupContent = $('<div class="USQPopupContent"></div>').html(data).prepend('<a href="#" onclick="event.preventDefault();" class="container-close USQ-close">Close</a>');
        		if($('.mm-page').length){
        			popupContent.addClass("mobWeb-health-warning-overlay");
        		}
        		this.container = $('<div id="USQPopup"></div>').hide().append(popupContent).appendTo($('body'));
        		this.opened = true;
        		this.container.show();
        		changeSizeOfBack();
          		this.setTabIndexes();
        	});
          

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
    setTabIndexes: {
    	value: function() {
			var $el = $('#USQPopup'),
			lastEl = -1, $s, $f, $l;
			
			//clear existing tabindexes
			$('[tabindex]').each(function(i,e) {
				$(e).attr('tabindex', null);
			});
			
			//clear existing tabindexes
			$el.find('input, button, textarea, select, a').each(function(i,e) {
				$(e).attr('tabindex', null);
			});

			// set new tabindices for popup
			$s = $el.find('input, button, textarea, select, a').not('.USQ-close').not('[disabled]').not('[type="hidden"]').not('[nofocus]');
			$s.each(function (i, tiel) {
				var $tiel = $(tiel);
				if (i === 0) { $f = $tiel; }
				if (i === $s.length-1) { $l = $tiel; }

				lastEl = i+1;

				$tiel.attr('tabindex', lastEl);
			});
			//tabindex order for overlays and popup
			$el.find('.USQ-close').each(function(i,e) {
				if (!$(e).is(':hidden')) {
					$(e).attr('tabindex', lastEl+=1);
					$l = $(e);
				}
			});
			$f.on('keydown', function(e) {
				if (e.keyCode === jQuery.ui.keyCode.TAB && e.shiftKey) {
					$l.focus();
					return false;
				}
			});
			$l.on('keydown', function(e) {
				if (e.keyCode === jQuery.ui.keyCode.TAB & !e.shiftKey) {
					$f.focus();
					return false;
				}
			});
			
			//set focus
			$el.find('button').first().focus();
    	}
    },
    open: {
      value: function () {
        if (!this.container) {
        	this.initPopup();
        } else {
            this.opened = true;
            this.container.show();
            changeSizeOfBack();
        	this.setTabIndexes();
        }
        
        //hiding the background body scrollbar and adding margin with width of scrollbar, so the content will not move
        $("body").css("overflow","hidden");
        if(!$('.mm-page').length){
        	$("body").css("margin-right","17px");
        }
      }
    },

    check: {
      value: function () {
        if (!FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')) {
          this.open();
        }
      }
    },

    close: {
      value: function () {
        if (this.container) {
          this.opened = false;
          this.container.hide();
        }
      //returning body's properties back to normal
        $("body").css("overflow","auto");
        $("body").css("margin-right","0px");
      }
    }

  });
  
  //changes background size if the window is resized
  $(window).on('resize', function(){ changeSizeOfBack(); });
  
  //changes the background size of popup, so it will be bigger if the window is too small
  var changeSizeOfBackStyleAdded = false;
  function changeSizeOfBack(){
	  if (!changeSizeOfBackStyleAdded) {
		  $('head').append('<style>#USQPopup:before{height:100%;}</style>');
		  $('head').append('<style>#USQPopup:before{width:100%;}</style>');
		  changeSizeOfBackStyleAdded = true;
	  }
	  
	  $("#USQPopup.soShow").removeClass("soShow");
	  if(FreshDirect.hasOwnProperty("standingorder") && FreshDirect.standingorder.USQPopupOpen){
		  $("#USQPopup").addClass("soShow");
		  FreshDirect.standingorder.USQPopupOpen = false;
      }
	  
	  if ($("#USQPopup").height() < $("#USQPopup div.USQPopupContent").height()){
			var sumH = $("#USQPopup div.USQPopupContent").height() + 70;
			$('head').append('<style>#USQPopup:before{height:'+ sumH + 'px;}</style>');
		} 
	  if ($(window).width() < $("#USQPopup div.USQPopupContent").width()){
			var sumW = $("#USQPopup div.USQPopupContent").width();
			$('head').append('<style>#USQPopup:before{width:'+ sumW + 'px;}</style>');
		}
  }
  
  try {
    USQPopup.state=Bacon.mergeAll([$(document).asEventStream('click','#USQAcceptButton').map(true),
                                   $(document).asEventStream('click','#USQCancelButton').map(false)]).toProperty(FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1'));
  } catch (e) {
    USQPopup.state=$(document).asEventStream('click','#USQAcceptButton').map(true);
  }

  USQPopup.state.onValue(function(accepted){
    if(accepted) {
      fd.modules.common.utils.createCookie('freshdirect.healthwarning','1@'+FreshDirect.USQLegalWarning.getJSessionId());
    }
  });
  
  fd.modules.common.utils.register("USQWarning", "Popup", USQPopup , fd);

}(FreshDirect));

