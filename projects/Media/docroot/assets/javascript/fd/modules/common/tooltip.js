/*global YAHOO */
var FreshDirect = FreshDirect || {};

(function (fd) {
  /**
   * Tooltip for links and such with HTML content.
   *
   * @param {Element} el The <input> element with the placeholder
   * @config {Object} Extra configuration
   * config:
   *    - content: HTML content for the tooltip
   *    - cssClass: CSS class for the tooltip
   *    - orientation: "top", "bottom"
   *    - offset: the gap between the element and the tooltip
   */
  var Tooltip = function (el, config) {
    this.el = el;
    this.config = fd.modules.common.utils.extend({}, Tooltip.DEFAULT_CONFIG, config);
    this.config.content = this.getContent();
    this.node = null;

    YAHOO.util.Event.addListener(this.el, "mouseover", this.show, null, this);
    YAHOO.util.Event.addListener(this.el, "mouseout", this.hide, null, this);
  };

  Tooltip.prototype.createNode = function () {
    if (!!this.node) {
      return this.node;
    }
    this.node = document.createElement("div");

    YAHOO.util.Dom.addClass(this.node, this.config.cssClass);
    YAHOO.util.Dom.addClass(this.node, "tooltip_" + this.config.orientation);

    YAHOO.util.Dom.setStyle(this.node, 'display', 'none');
    YAHOO.util.Dom.setStyle(this.node, 'position', 'absolute');
    YAHOO.util.Dom.setStyle(this.node, 'z-index', '1000');

    this.arrow = document.createElement("span");
    this.arrow.className = "arrow";
    this.node.appendChild(this.config.content);
    this.node.appendChild(this.arrow);
    document.body.appendChild(this.node);

    return this.node;
  };

  Tooltip.prototype.reposition = function () {
    var elregion = YAHOO.util.Dom.getRegion(this.el),
        myregion = YAHOO.util.Dom.getRegion(this.node),
        top, bottom, left, right;

    // Position based on orientation
    if (this.config.orientation === 'bottom') {
      top = (elregion.bottom + this.config.offset) + "px";
      left = (elregion.left + (elregion.width / 2) - (myregion.width / 2)) + "px";
    } else { // top
      top = 'auto';
      top = (elregion.top - this.config.offset - myregion.height) + "px";
      left = (elregion.left + (elregion.width / 2) - (myregion.width / 2)) + "px";
    }

    YAHOO.util.Dom.setStyle(this.node, 'top', top);
    YAHOO.util.Dom.setStyle(this.node, 'left', left);
  };

  Tooltip.prototype.show = function () {
    this.createNode();
    YAHOO.util.Dom.setStyle(this.node, 'display', 'block');
    this.reposition();
  };

  Tooltip.prototype.hide = function () {
    if (!!this.node) {
      YAHOO.util.Dom.setStyle(this.node, 'display', 'none');
    }
  };

  Tooltip.prototype.getContent = function () {
    var next = YAHOO.util.Dom.getNextSibling(this.el),
        title;
    if (typeof(this.config.content) === "string") {
      return document.createTextNode(this.config.content);
    } else if (YAHOO.util.Dom.hasClass(next, 'tooltipcontent')) {
      return this.el.parentElement.removeChild(next);
    } else if (!!(title = YAHOO.util.Dom.getAttribute(this.el, 'title'))) {
      YAHOO.util.Dom.setAttribute(this.el, 'title', "");
      return document.createTextNode(title);
    }
  };

  Tooltip.DEFAULT_CONFIG = {
    orientation: 'top',
    offset: 10,
    cssClass: 'tooltip'
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Tooltip", Tooltip, fd);
}(FreshDirect));

// module initialization
(function () {
  var els = YAHOO.util.Dom.getElementsByClassName('hastooltip'),
      tooltip, i;

  for (i = 0; i < els.length; i++) {
    tooltip = new FreshDirect.modules.common.Tooltip(els[i]);
  }
}());
