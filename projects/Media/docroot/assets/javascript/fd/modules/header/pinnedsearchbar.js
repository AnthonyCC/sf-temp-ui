var FreshDirect = FreshDirect || {};

(function(fd) {
  /**
   * Pinned element to the top of the browser window
   * 
   * @param {Element} el The pinned header element
   */
  var PinnedHeader = function (el) {
    this.el = el;
    this.placeholder = null;
    this.pincontainer = null;

    YAHOO.util.Event.addListener(window, 'scroll', this.check, null, this);
  };

  PinnedHeader.prototype.check = function (e) {
    var scrolltop = document.body.scrollTop || window.pageYOffset || document.documentElement.scrollTop,
        region;
    
    if (!!this.placeholder) {
      region = YAHOO.util.Dom.getRegion(this.placeholder);
      if (scrolltop < region.top) {
        this.unpin();
      }
    } else {
      region = YAHOO.util.Dom.getRegion(this.el);
      if (scrolltop > region.top) {
        this.pin();
      }
    }

  };

  PinnedHeader.prototype.pin = function (e) {
    var region = YAHOO.util.Dom.getRegion(this.el);

    // create placeholder
    this.placeholder = document.createElement('div');
    YAHOO.util.Dom.setStyle(this.placeholder, 'height', region.height+'px');
    YAHOO.util.Dom.setStyle(this.placeholder, 'width', region.width+'px');
    YAHOO.util.Dom.setStyle(this.placeholder, 'float', YAHOO.util.Dom.getStyle(this.el, 'float'));
    this.el.parentNode.insertBefore(this.placeholder, this.el);

    this.pincontainer = document.createElement('div');
    YAHOO.util.Dom.setStyle(this.pincontainer, 'width', region.width+'px');
    YAHOO.util.Dom.setStyle(this.pincontainer, 'height', region.height+'px');
    YAHOO.util.Dom.setStyle(this.pincontainer, 'left', region.left+'px');
    YAHOO.util.Dom.setStyle(this.pincontainer, 'top', '0');
    YAHOO.util.Dom.setStyle(this.pincontainer, 'position', 'fixed');

    this.el.parentNode.removeChild(this.el);
    document.body.appendChild(this.pincontainer);
    this.pincontainer.appendChild(this.el);

    YAHOO.util.Dom.addClass(this.el, "fixedtop");
  };

  PinnedHeader.prototype.unpin = function (e) {
    this.el.parentNode.removeChild(this.el);
    this.placeholder.parentNode.insertBefore(this.el, this.placeholder);
    this.placeholder.parentNode.removeChild(this.placeholder);
    document.body.removeChild(this.pincontainer);
    this.placeholder = null;
    this.pincontainer = null;

    YAHOO.util.Dom.removeClass(this.el, "fixedtop");
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.header", "PinnedHeader", PinnedHeader, fd);
})(FreshDirect);

// module initialization
(function () {
  var topcart = YAHOO.util.Dom.get("sidecartbuttons"),
      pinnedtopcart;

  if (window.FreshDirect.CONFIG && window.FreshDirect.CONFIG.topcartpinned) {
    pinnedtopcart = new FreshDirect.modules.header.PinnedHeader(topcart);
  }
})();


