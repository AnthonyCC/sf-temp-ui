var FreshDirect = FreshDirect || {};

(function(fd) {

  /**
   * Sets a CSS class on the given element on trigger element's click event, unsets it on mouseout.
   *
   * @param {Element} el The element to set the class on
   * @param {Element} trigger The trigger element
   * @param {String} cssclass The CSS class to set
   */
  var ClickLeaveToggler = function (el, trigger, cssclass) {
    this.el = el;
    this.trigger = trigger;
    this.cssclass = cssclass || "active";

    YAHOO.util.Event.addListener(this.trigger, "click", this.setClass, null, this);
    YAHOO.util.Event.addListener(this.el, "mouseout", this.unSetClass, null, this);
  };

  ClickLeaveToggler.prototype.setClass = function (e) {
    if (!YAHOO.util.Dom.hasClass(this.el, this.cssclass)) {
      YAHOO.util.Dom.addClass(this.el, this.cssclass);
    } else {
      YAHOO.util.Dom.removeClass(this.el, this.cssclass);
    }
    YAHOO.util.Event.preventDefault(e);
  };

  ClickLeaveToggler.prototype.unSetClass = function (e) {
    var target = e.relatedTarget || e.toElement,
        to;
    if (target !== this.el && target !== this.trigger) {
      to = YAHOO.util.Dom.getAncestorByClassName(target, 'cltoggler');
      if (!to) {
        YAHOO.util.Dom.removeClass(this.el, this.cssclass);
      }
    }
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "ClickLeaveToggler", ClickLeaveToggler, fd);
})(FreshDirect);

// module initialization
(function () {
  var els = YAHOO.util.Dom.getElementsByClassName('cltoggler'), 
      trigger, cltoggler, i;

  for (i = 0; i < els.length; i++) {
    trigger = YAHOO.util.Dom.getElementsByClassName('cltrigger', null, els[i]).shift();
    if (trigger) {
      cltoggler = new FreshDirect.modules.common.ClickLeaveToggler(els[i], trigger);
    }
  }
})();

