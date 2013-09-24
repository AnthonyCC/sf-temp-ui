/*global YAHOO*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  /**
   * Popup submenu
   *
   * @param {Element} el The popup element to show
   * @param {Element} trigger The popup submenu will show up on this element's mouseover events
   */
  var PopSubMenu = function (el, trigger) {
    this.el = el;
    this.trigger = trigger;
    this.overlay = YAHOO.util.Dom.get("popsubmenuoverlay");
    this.container = YAHOO.util.Dom.get("popsubmenuholder");
    this.delay = null;

    this.el.parentNode.removeChild(this.el);
    this.container.appendChild(this.el);

    this.showProxy = fd.modules.common.utils.proxy(this.show_, this);

    YAHOO.util.Event.addListener(this.trigger, "mouseover", this.show, null, this);
    YAHOO.util.Event.addListener(this.trigger, "mouseout", this.checkHide, null, this);
    YAHOO.util.Event.addListener(this.el, "mouseout", this.checkHide, null, this);
  };

  PopSubMenu.prototype.show = function (e) {
    var from = e.fromElement;
    // console.log(from);
    // console.log(YAHOO.util.Dom.getAncestorByClassName(from, 'popsubmenu'));
    if (from && (YAHOO.util.Dom.getAncestorByClassName(from, 'popsubmenu') || YAHOO.util.Dom.getAncestorByClassName(from, 'submenu'))) {
      this.show_();
    } else if (this.delay === null) {
      this.delay = setTimeout(this.showProxy, 100);
    }
  };

  PopSubMenu.prototype.show_ = function () {
    YAHOO.util.Dom.addClass(this.trigger, "hover");
    this.el.style.display = "block";
    this.overlay.style.display = "block";
    this.alignOverlay();
  };

  PopSubMenu.prototype.checkHide = function (e) {
    var target = e.relatedTarget || e.toElement,
        to;
    if (target !== this.el && target !== this.trigger) {
      to = YAHOO.util.Dom.getAncestorByClassName(target, 'popsubmenu');
      if (!to) {
        this.hide();
        if (this.delay !== null) {
          clearTimeout(this.delay);
          this.delay = null;
        }
      }
    }
  };

  PopSubMenu.prototype.hide = function () {
    YAHOO.util.Dom.removeClass(this.trigger, "hover");
    this.el.style.display = "none";
    this.overlay.style.display = "none";
  };

  PopSubMenu.prototype.alignOverlay = function () {
    var body = document.getElementsByTagName('body')[0],
        html = document.getElementsByTagName('html')[0],
        scrollHeight = html.scrollHeight || body.scrollHeight,
        height = scrollHeight - YAHOO.util.Dom.getXY(this.overlay)[1];
    this.overlay.style.height = height + "px";
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.header", "PopSubMenu", PopSubMenu, fd);
}(FreshDirect));

// module initialization
(function () {
  var submenu = YAHOO.util.Dom.get("submenu"),
      smlis = YAHOO.util.Dom.getElementsByClassName("menuitem", "li", submenu),
      i, smli, psmel, psm;

  for (i = 0; i < smlis.length; i++) {
    smli = smlis[i];
    psmel = YAHOO.util.Dom.getElementsByClassName("popsubmenu", "div", smli).shift();
    if (psmel) {
      psm = new FreshDirect.modules.header.PopSubMenu(psmel, smli);
    }
  }
}());

