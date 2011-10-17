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
    this.el.parentNode.insertBefore(this.placeholder, this.el);

    this.el.parentNode.removeChild(this.el);
    document.body.appendChild(this.el);

    YAHOO.util.Dom.addClass(this.el, "fixedtop");
  };

  PinnedHeader.prototype.unpin = function (e) {
    this.el.parentNode.removeChild(this.el);
    this.placeholder.parentNode.insertBefore(this.el, this.placeholder);
    this.placeholder.parentNode.removeChild(this.placeholder);
    this.placeholder = null;

    YAHOO.util.Dom.removeClass(this.el, "fixedtop");
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.header", "PinnedHeader", PinnedHeader, fd);
})(FreshDirect);

// module initialization
(function () {
  var searchbar = YAHOO.util.Dom.get("searchbar"),
      pinnedsearchbar = new FreshDirect.modules.header.PinnedHeader(searchbar);
})();


