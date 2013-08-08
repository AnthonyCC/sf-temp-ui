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
    this.$trigger = $trigger;
    this.config = $.extend({}, PopupContent.CONFIG, config);

    this.$alignTo = this.config.alignTo || this.$trigger;

    this.$overlay = $("#popupcontentoverlay");
    this.$ghost = $("#popupcontentghost");
    this.$placeholder = $("#popupcontentplaceholder");
    this.delay = null;
    this.hidedelay = null;
    this.clicked = false;
    this.shown = false;
    this.disabled = this.config.disabled || false;

    // TODO move the trigger element to the ghost, replace it with a placeholder

    // check overlay and ghost
    if (!this.$overlay.length) {
      this.$overlay = $('<div id="popupcontentoverlay"></div>').appendTo(document.body);
    }
    if (!this.$ghost.length) {
      this.$ghost = $('<div id="popupcontentghost"></div>').appendTo(document.body);
    }
    if (!this.$placeholder.length) {
      this.$placeholder = $('<div id="popupcontentplaceholder"></div>').appendTo(document.body);
    }

    // move $el to the end of body
    this.$el.appendTo(document.body);

    this.hideProxy = $.proxy(this.hide, this);

    this.$trigger.on("mouseover", $.proxy(this.showDelayed, this));
    this.$trigger.on("click", $.proxy(this.showDelayed, this)); // ! might cause merge problems with quickshop branch !
    this.$trigger.on("mouseout", $.proxy(this.clearDelay, this));
    this.$overlay.on("mouseover", $.proxy(this.hideDelayed, this));
    this.$overlay.on("mouseout", $.proxy(this.clearHideDelay, this));
    this.$ghost.on("click", this.hideProxy);
    this.$overlay.on("click", this.hideProxy);

    if (this.config.stayOnClick) {
      this.$el.on("mousedown", $.proxy(function () { 
        if (this.shown && !this.clicked) {
          this.clicked = true; 
          this.$el.addClass('clicked');
        }
      }, this));
    }

    if (this.config.$closeHandle) {
      this.config.$closeHandle.on('click', this.hideProxy);
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
    e.preventDefault();
    if (this.delay === null) {
      this.delay = setTimeout($.proxy(this.show, this), this.config.delay);
    }
  };

  PopupContent.prototype.hideDelayed = function (e) {
    if (this.hidedelay === null && !this.clicked) {
      this.hidedelay = setTimeout($.proxy(this.hide, this), this.config.delay);
    }
  };

  PopupContent.prototype.show = function (e) {
    if (!this.shown && !this.disabled) {
      this.shown = true;
      this.$trigger.addClass("hover");
      this.reposition();
      this.$el.css({display: "block"});
      this.$ghost.css({display: "block"});
      this.$overlay.css({display: "block"});
    }
  };

  PopupContent.prototype.hide = function () {
    this.$trigger.removeClass("hover");
    this.$el.css({display: "none"});
    this.$ghost.css({display: "none"});
    this.$overlay.css({display: "none"});
    this.clicked = false;
    this.shown = false;
    this.$el.removeClass('clicked');
    this.$alignTo.insertBefore(this.$placeholder);
    this.$placeholder.detach();
    this.clearDelay();
  };

  PopupContent.prototype.reposition = function () {
    var offset = this.$alignTo.offset(),
        width = this.$alignTo.outerWidth(),
        height = this.$alignTo.outerHeight(),
        fwidth = this.$alignTo.outerWidth(true),
        fheight = this.$alignTo.outerHeight(true),
        contentWidth = this.$el.outerWidth();

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

    if (this.config.valign === 'bottom') {
      this.$el.css({top: (offset.top + height) + 'px', bottom: 'auto'});
    } else {
      this.$el.css({bottom: offset.top + 'px', top: 'auto'});
    }

    if (this.config.halign === 'right') {
      this.$el.css({left: (offset.left + width - contentWidth) + 'px', right: 'auto'});
    } else {
      this.$el.css({left: offset.left + 'px', right: 'auto'});
    }

  };

  PopupContent.CONFIG = {
    valign: 'bottom',
    halign: 'left',
    delay: 300
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "PopupContent", PopupContent, fd);
}(FreshDirect));
