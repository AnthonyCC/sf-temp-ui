<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US">
		<fd:css href="css/reset.css"/>
		<fd:css href="css/typography.css"/>
		<fd:css href="css/grid.css"/>

		<fd:css href="css/fdgeneral.css"/>
		<fd:css href="css/header.css"/>
		<fd:css href="css/footer.css"/>
		<fd:css href="css/content.css"/>

<!--[if IE]>
		<fd:css href="css/ie.css"/>
<![endif]-->

    <!-- YUI includes START --> 
    <fd:css href="/assets/yui-2.9.0/container/assets/container.css"/> 
    <fd:javascript src="/assets/yui-2.9.0/yahoo/yahoo.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/event/event.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/dom/dom.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/event-mouseenter/event-mouseenter.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/connection/connection.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/animation/animation.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/dragdrop/dragdrop.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/utilities/utilities.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/container/container.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/json/json.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/datasource/datasource.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/animation/animation.js"/> 
    <fd:javascript src="/assets/yui-2.9.0/yahoo-dom-event/yahoo-dom-event.js"/> 

    <script type="text/javascript"> 
      var $E  = YAHOO.util.Event;  
      var $D  = YAHOO.util.Dom;  
      //var $DH = YAHOO.ext.DomHelper;  
      var $C  = YAHOO.util.Connect;  
      var $X  = YAHOO.ext;  
      var $  = $D.get;  
    </script> 
    <!-- YUI includes END -->

    <script>
      var FreshDirect = FreshDirect || {};
      (function(fd) {
        var PopSubMenu = function (el, trigger) {
          this.el = el;
          this.trigger = trigger;
          this.overlay = YAHOO.util.Dom.get("popsubmenuoverlay");
          this.visible = YAHOO.util.Dom.getStyle(el, "display") === "block";
          YAHOO.util.Event.addListener(this.trigger, "click", this.triggerClicked, null, this);
        };

        PopSubMenu.prototype.triggerClicked = function (e) {
          if (this.visible) {
            this.hide();
          } else {
            this.show();
          }
        };

        PopSubMenu.prototype.show = function () {
          this.el.style.display = "block";
          this.overlay.style.display = "block";
          this.alignOverlay();
          this.visible = true;
        };

        PopSubMenu.prototype.hide = function () {
          this.el.style.display = "none";
          this.overlay.style.display = "none";
          this.visible = false;
        };

        PopSubMenu.prototype.alignOverlay = function () {
          var body = document.getElementsByTagName('body')[0],
              html = document.getElementsByTagName('html')[0],
              scrollHeight = html.scrollHeight || body.scrollHeight,
              height = scrollHeight - YAHOO.util.Dom.getXY(this.overlay)[1];
          this.overlay.style.height = height + "px";
        };

        fd.PopSubMenu = PopSubMenu;
      })(FreshDirect);

      YAHOO.util.Event.onDOMReady(function() {
        var psmEl = YAHOO.util.Dom.get("popsubmenu"),
            psmTrigger = YAHOO.util.Dom.get("submenu"),
            psm = new FreshDirect.PopSubMenu(psmEl, psmTrigger);
      });

    </script>
	</head>
  <body>
    <%@ include file="/test/grid_prototype/modules/header.jspf" %>

    <%-- content --%>
		<div id="content">
			<div class="container clearfix">
				<div class="column span-19">
					<div class="box">
					<p>Ut ultricies, metus quis gravida iaculis, magna nunc lobortis magna, a vehicula ante dolor eget mauris. Nulla tempus eleifend dui, quis mollis magna tincidunt ut. Pellentesque tellus dolor, ornare a blandit quis, congue non mi. Nullam justo nisl, ultricies sit amet pharetra sed, commodo vel quam. Aenean at arcu lacus. Nulla facilisi. Fusce bibendum vehicula bibendum. Donec gravida magna vitae nulla fringilla eu auctor lorem euismod. Aenean nec nulla vitae nibh vehicula consequat id id diam. Ut hendrerit, orci non consectetur molestie, ipsum orci cursus purus, at sollicitudin mauris tortor quis felis. Nunc sed eros urna, sit amet luctus nisl. Vestibulum at augue nibh, in tristique urna. Mauris pellentesque porta leo imperdiet commodo. Etiam in est sit amet nibh volutpat iaculis ac iaculis turpis.</p>
					<p>Mauris orci augue, dapibus vitae imperdiet vel, molestie nec ipsum. Aenean ut neque a felis eleifend tincidunt ut eu turpis. Mauris auctor dapibus ipsum non ultricies. Morbi ac enim vitae dui aliquam pellentesque. Fusce vulputate semper sagittis. Phasellus nec lacus eu lectus commodo mollis nec sed massa. Donec sagittis est quis mauris luctus quis tempus enim venenatis. Pellentesque ornare velit a ante sollicitudin sit amet consectetur sem ornare. Maecenas id enim tortor. Vivamus tempus, tortor vitae viverra molestie, quam ipsum rhoncus mauris, tincidunt commodo ante lorem et dolor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
					</div>
          <hr />
          <div class="column span-19-1">
            <%-- {{{ Lorem ipsum --%>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            <%-- }}} --%>
          </div>
          <div class="column span-19-2">
            <%-- {{{ Lorem ipsum --%>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            <%-- }}} --%>
          </div>
          <hr />
          <div class="column span-4">
            <%-- {{{ Lorem ipsum --%>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            <%-- }}} --%>
          </div>
          <div class="column span-15 last">
            <div class="column span-15-1">
            <%-- {{{ Lorem ipsum --%>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            <%-- }}} --%>
            </div>
            <div class="column span-15-2">
            <%-- {{{ Lorem ipsum --%>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            <%-- }}} --%>
            </div>
          </div>
				</div>
				<div class="column span-5 last center">
					<div id="cart">
						<div id="cart_header_wrapper"><div id="cart_header"><h5>Your Cart</h5><div class="cart_summary_wrapper"><div class="cart_summary">Subtotal: 99999</div></div></div></div>
						<div id="cart_body">
							<h5>Bakery</h5>
							<ul>
								<li>Espresso Mousse</li>
							</ul>
							<h5>Deli</h5>
							<ul>
								<li>Macaroni Salad</li>
							</ul>
							<h5>Fruit</h5>
							<ul>
								<li>Bananas</li>
							</ul>
							<h5>Grocery</h5>
							<ul>
								<li>Oven White</li>
								<li>Tea Biscuits</li>
							</ul>
							<h5>Heat & Eat</h5>
							<ul>
								<li>Mozzarella Pizza</li>
								<li>Blue Cheese Salad</li>
								<li>Spinach Salad</li>
							</ul>
							<h5>Vegetables & Herbs</h5>
							<ul>
								<li>Baby Spinach</li>
							</ul>
							<h5>Wine</h5>
							<ul>
								<li>Tintorera</li>
								<li>Cabernet Sauvignon</li>
								<li>Periquita</li>
							</ul>
						</div>
						<div id="cart_footer_wrapper"><div id="cart_footer"><div class="cart_summary_wrapper"><div class="cart_summary">Subtotal: 99999</div></div></div></div>
					</div>
				</div>
			</div>
		</div>
    <%-- end of content --%>

    <%@ include file="/test/grid_prototype/modules/footer.jspf" %>
  </body>
</html>
