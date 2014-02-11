/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	
	var $=fd.libs.$;
	
  /**
   * Popup content
   *
   * @param {jQuery} $el The popup element (cart) to show
   * @param {jQuery} $trigger The content will show up on this element's mouseover events
   * @param {Object} config Configuration object
   */
  var PopupContent = function ($el, $trigger, config) {
    this.$el = $el;
    this.config = $.extend({}, PopupContent.CONFIG, config);

    this.$alignTo = this.config.aligntoselector && $(this.config.aligntoselector) ||
                    this.config.alignTo || $trigger;

    this.$overlay = null;
    this.$ghost = null;
    this.$placeholder = null;
    this.delay = null;
    this.hidedelay = null;
    this.clicked = false;
    this.shown = false;
    this.disabled = this.config.disabled || false;
    this.config.placeholder = !!(this.config.placeholder && $trigger);
    if(config['align']){
    	this.config.align=config.align;
    }

    this.hideProxy = $.proxy(this.hide, this);

    // wait for domready event
    $($.proxy(function(){

      // TODO move the trigger element to the ghost, replace it with a placeholder

      // check overlay and ghost
      if (this.config.overlay) {
        this.$overlay = $('<div class="popupcontentoverlay"></div>').appendTo(document.body);

        if (this.config.zIndex) {
          this.$overlay.css('z-index', this.config.zIndex);
        }

        this.$overlay.on("mouseover", $.proxy(this.hideDelayed, this));
        this.$overlay.on("mouseout", $.proxy(this.clearHideDelay, this));
        this.$overlay.on("click", this.hideProxy);
      }
      if (this.config.placeholder) {
        this.$ghost = $('<div class="popupcontentghost"></div>').appendTo(document.body);
        this.$placeholder = $('<div class="popupcontentplaceholder"></div>').appendTo(document.body);

        if (this.config.zIndex) {
          this.$ghost.css('z-index', this.config.zIndex+1);
        }

      }

      // move $el to the end of body
      this.$el.appendTo(document.body);

      if (this.config.zIndex) {
        this.$el.css('z-index', this.config.zIndex+2);
      }

      if (this.config.placeholder) {
        this.$ghost.on("click", this.hideProxy);
      }

      this.bindTrigger($trigger);

      if (this.config.stayOnClick) {
        this.$el.on("mousedown", $.proxy(function () {
          if (this.shown && !this.clicked) {
            this.clicked = true;
            this.$el.addClass('clicked');
          }
        }, this));
      }

      if (this.config.closeHandle) {
        $(this.config.closeHandle).on('click', this.hideProxy);
      }


    $(window).on('resize',$.proxy(function(e){
      if(this.shown){
        this.hide();
        this.show();
      }
    },this));

  },this));

  };

  PopupContent.prototype.bindTrigger = function ($trigger) {
    if ($trigger) {
      this.$trigger = $trigger;
      this.alignTo = this.alignTo || $trigger;

      if (this.config.openonclick) {
        this.$trigger.on("click", $.proxy(this.show, this));
      } else {
        this.$trigger.on("mouseover", $.proxy(this.showDelayed, this));
        this.$trigger.on("mouseout", $.proxy(this.clearDelay, this));
      }
      if (!this.config.overlay && !this.config.openonclick) {
        this.$trigger.on("mouseout", $.proxy(this.hideDelayed, this));
        this.$el.on("mouseover", $.proxy(this.clearHideDelay, this));
        this.$el.on("mouseout", $.proxy(this.hideDelayed, this));
      }
    }
  };

  PopupContent.prototype.clearDelay = function (e) {
    clearTimeout(this.delay);
    this.delay = null;
  };

  PopupContent.prototype.clearHideDelay = function (e) {
    clearTimeout(this.hidedelay);
    this.hidedelay = null;
  };

  PopupContent.prototype.showDelayed = function (e) {
    if (this.delay === null) {
      this.delay = setTimeout($.proxy(this.show, this), this.config.delay);
    }
  };

  PopupContent.prototype.hideDelayed = function (e) {
    if (this.hidedelay === null && !this.clicked) {
      this.hidedelay = setTimeout($.proxy(this.hide, this), this.config.delay);
    }
  };

  PopupContent.prototype.show = function ($trigger, align) {
	var screenOffset, boundingRect;
    if ($trigger) {
      this.$trigger = $trigger;
      this.$alignTo = $trigger;
    }
    this.config.align=align;
    if (this.$trigger && !this.shown && !this.disabled) {
      this.shown = true;
      this.$trigger.addClass("hover");
      this.reposition();
      this.$el.css({display: "block"});
      this.$el.addClass('shown');
      if (this.config.placeholder) {
        this.$ghost.css({display: "block"});
      }
      if (this.config.overlay) {
        this.$overlay.css({display: "block"});
      }
      var el = this.$el;
      setTimeout(function(){
          boundingRect = el[0].getBoundingClientRect();
          
          screenOffset =  $(window).height() - (boundingRect.bottom);
    	  //console.log(screenOffset);
          if(screenOffset<0) {
        	  el.smoothScroll();
          }     	  
      },500);
      
      
    }
  };

  PopupContent.prototype.hide = function () {
    if (this.$trigger) {
      this.$trigger.removeClass("hover");
    }
    this.$el.css({display: "none"});
    this.$el.removeClass('shown');
    this.clicked = false;
    this.shown = false;
    this.$el.removeClass('clicked');
    if (this.config.placeholder) {
      this.$ghost.css({display: "none"});
      this.$alignTo.insertBefore(this.$placeholder);
      this.$placeholder.detach();
    }
    if (this.config.overlay) {
      this.$overlay.css({display: "none"});
    }
    this.clearHideDelay();
  };

  PopupContent.prototype.reposition = function () {
    var offset = this.$alignTo.offset(),
        width = this.$alignTo.outerWidth(),
        height = this.$alignTo.outerHeight(),
        fwidth = this.$alignTo.outerWidth(true),
        fheight = this.$alignTo.outerHeight(true),
        contentWidth = this.$el.outerWidth(),
        contentHeight = this.$el.outerHeight(),
        screenOffset;

    var align = this.config.align;

    if (this.config.placeholder) {
      this.$ghost.css({
        top: offset.top + 'px',
        left: offset.left + 'px',
        width: fwidth + 'px',
        height: fheight + 'px'
      });
      this.$placeholder.insertBefore(this.$alignTo);
      this.$placeholder.css({
        width: fwidth + 'px',
        height: fheight + 'px',
        float: this.$alignTo.css('float')
      });
      this.$alignTo.appendTo(this.$ghost);
    }
    
    if(align) {
    	var position={};
    	if(align[0]==='t') position.top = offset.top
    	else if(align[0]==='b') position.top = offset.top + height
    	else if(align[0]==='c') position.top = offset.top + height/2;
    	
    	if(align[1]==='l') position.left = offset.left
    	else if(align[1]==='r') position.left = offset.left + width
    	else if(align[1]==='c') position.left = offset.left + width/2;
    	
    	if(align[3]==='c') position.top = position.top - contentHeight/2
    	else if(align[3]==='b') position.top = position.top - contentHeight;
    	
    	if(align[4]==='c') position.left = position.left - contentWidth/2
    	else if(align[4]==='r') position.left = position.left - contentWidth;
    	    	
		this.$el.css({
    		top:position.top+'px',
    		left:position.left+'px',
    		right:'auto',
    		bottom:'auto'
    	});
    	
    } else if(align!==false) {
        if (this.config.valign === 'bottom') {
            this.$el.css({top: (offset.top + height) + 'px', bottom: 'auto'});
        } else {
            this.$el.css({top: offset.top + 'px', bottom: 'auto'});
        }

	if (this.config.halign === 'right') {
		    this.$el.css({left: (offset.left + width - contentWidth) + 'px', right: 'auto'});
	} else {
	    this.$el.css({left: offset.left + 'px', right: 'auto'});
	}
    }
    


  };

  PopupContent.CONFIG = {
    valign: 'bottom',
    halign: 'left',
    delay: 300,
    placeholder: true,
    overlay: true,
    stayOnClick: true
    // closehandle: '.close'
    // alignTo
    // aligntoselector
    // openonclick
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "PopupContent", PopupContent, fd);
}(FreshDirect));
