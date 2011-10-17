/*global YAHOO*/
var FreshDirect = FreshDirect || {};

// TODO
//  - no notifications class (cyan)
(function (fd) {

  /**
   * Notification bar
   *
   * @param {Element} trigger Notification bar will be displayed/hidden on clicking this element
   * @param {Element} container The DOM element containing the notification bar
   * @param {Object} config configuration object
   * configuration object:
   *    - prev: trigger to show previous notification (def: "container a.prev:first")
   *    - next: trigger to show next notification (def: "container a.next:first")
   *    - eContainer: DOM element with the notifications (def: "container ul")
   *    - elements: DOM element array with the notifications (def: "container li")
   *    - nnClass: CSS class if there's no messages to show (def: "nonew")
   *    - anim: animate transitions (def: true)
   */
  var Notifications = function (trigger, container, config) {
    this.trigger = trigger;
    this.container = container;
    this.config = config;

    this.prevEl = this.config.prev || YAHOO.util.Dom.getElementsByClassName("prev", "a", this.container).shift();
    this.nextEl = this.config.next || YAHOO.util.Dom.getElementsByClassName("next", "a", this.container).shift();

    this.eContainer = this.config.eContainer || this.container.getElementsByTagName("ul");
    this.elements = this.config.elements || this.container.getElementsByTagName("li");
    this.nnClass = this.config.nnClass || "nonew";
    this.anim = typeof(this.config.anim) === "undefined" ? true : this.config.anim;
    this.height = parseInt(YAHOO.util.Dom.getStyle(this.container, 'height'), 10);
    this.elWidth = parseInt(YAHOO.util.Dom.getStyle(this.elements[0], "width"), 10);

    YAHOO.util.Dom.setStyle(this.eContainer, 'width', (this.elWidth * this.elements.length) + "px");

    this.position = 0;
    this.showEl(this.position);

    this.checkCount();

    YAHOO.util.Event.addListener(this.trigger, "click", this.toggle, null, this);
    YAHOO.util.Event.addListener(this.prevEl, "click", this.prev, null, this);
    YAHOO.util.Event.addListener(this.nextEl, "click", this.next, null, this);

    // if first notification is important then show it
    if (YAHOO.util.Dom.hasClass(this.elements[this.position], "important")) {
      this.show();
    }

  };

  Notifications.prototype.getHeight = function (pos) {
    return YAHOO.util.Dom.getRegion(this.elements[pos]).height;
  };

  Notifications.prototype.setHeight = function (height) {
    var anim;
    if (this.anim && height) {
      anim = new YAHOO.util.Anim(this.container, {
          height: { to: height }
        }, 0.2);
      anim.animate();
    } else {
      YAHOO.util.Dom.setStyle(this.container, 'height', height + 'px');
    }
  };

  Notifications.prototype.getNotificationCount = function () {
    var countEl = YAHOO.util.Dom.getElementsByClassName("notif_count", "span", this.trigger).shift(),
        count = countEl.textContent || countEl.innerText;

    return +count;
  };

  Notifications.prototype.checkCount = function () {
    var count = this.getNotificationCount();

    if (count === 0) {
      YAHOO.util.Dom.addClass(this.container, 'nonew');
      YAHOO.util.Dom.addClass(this.trigger.parentNode, 'nonew');
    }
  };

  Notifications.prototype.toggle = function (e) {
    var visible = YAHOO.util.Dom.getStyle(this.container, 'display') === 'block';

    if (visible) {
      this.hide();
    } else {
      this.show();
    }

    YAHOO.util.Event.preventDefault(e);
  };

  Notifications.prototype.show_ = function () {
    YAHOO.util.Dom.setStyle(this.container, 'display', 'block');
    YAHOO.util.Dom.addClass(this.trigger, 'active');
  };

  Notifications.prototype.hide_ = function () {
    YAHOO.util.Dom.setStyle(this.container, 'display', 'none');
    YAHOO.util.Dom.removeClass(this.trigger, 'active');
  };

  Notifications.prototype.show = function () {
    var anim, height;

    if (this.anim) {
      YAHOO.util.Dom.setStyle(this.container, 'height', '0');
      this.show_();
      height = this.getHeight(this.position);
      this.setHeight(height > this.height ? height : this.height);
    } else {
      this.show_();
    }
  };

  Notifications.prototype.hide = function () {
    var anim;

    if (this.anim) {
      anim = new YAHOO.util.Anim(this.container, {
        height: { to: 0 }
      }, 0.2);
      anim.onComplete.subscribe(this.hide_, null, this);
      anim.animate();
    } else {
      this.hide_();
    }

  };

  Notifications.prototype.prev = function (e) {
    if (this.position > 0) {
      this.showEl(--this.position);
    }

    YAHOO.util.Event.preventDefault(e);
  };

  Notifications.prototype.next = function (e) {
    if (this.position < this.elements.length - 1) {
      this.showEl(++this.position);
    }

    YAHOO.util.Event.preventDefault(e);
  };

  Notifications.prototype.showEl = function (pos) {
    var newmargin, anim;

    if (pos >= this.elements.length) {
      pos = this.elements.length - 1;
    }
    if (pos < 0) {
      pos = 0;
    }

    this.setHeight(this.getHeight(pos));

    newmargin = pos * this.elWidth * -1;
    if (this.anim) {
      anim = new YAHOO.util.Anim(this.eContainer, { marginLeft: { to: newmargin } }, 0.2);
      anim.animate();
    } else {
      YAHOO.util.Dom.setStyle(this.eContainer, "margin-left", newmargin + "px");
    }

    // check prev/next buttons
    if (pos === 0) {
      YAHOO.util.Dom.addClass(this.prevEl, 'inactive');
    } else {
      YAHOO.util.Dom.removeClass(this.prevEl, 'inactive');
    }

    if (pos === this.elements.length - 1) {
      YAHOO.util.Dom.addClass(this.nextEl, 'inactive');
    } else {
      YAHOO.util.Dom.removeClass(this.nextEl, 'inactive');
    }

  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.header", "Notifications", Notifications, fd);
}(FreshDirect));

// module initialization
(function () {
  var notifications = YAHOO.util.Dom.get("notifications"),
      trigger = YAHOO.util.Dom.get("notifications_trigger"),
      prev = YAHOO.util.Dom.get("notifications_prev"),
      next = YAHOO.util.Dom.get("notifications_next"),
      notif_widget;
  if (notifications) {
    notif_widget = new FreshDirect.modules.header.Notifications(trigger, notifications, { prev: prev, next: next });
    FreshDirect.modules.common.utils.register("widgets", "notification", notif_widget, FreshDirect);
  }
}());

