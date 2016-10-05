(function ($) {
	$(document).ready(function() {
		/* NAV */
			$("#nav-menu").mmenu({
				"extensions": [
					"pagedim-black",
					"border-full"
				],
				"offCanvas": {
					"zposition": "front"
				},
				"navbar": {
					"title": "<img src=\"/media/mobileweb/images/topbar-fd-logo.png\" alt=\"FreshDirect\" class=\"img-responsive\" />"
				},
				/*"navbars": [
					{
						"position": "bottom",
						"content": (FreshDirect && FreshDirect.locabar && FreshDirect.locabar.hasFdxServices) ? "<div class='navbar-cont'><a href='https://foodkick.freshdirect.com' class='locabar-tab locabar-tab-fdx-cont'><div class='locabar-tab-fdx'></div></a></div>" : ""
					}
				],*/
				"iconPanels": true,
				"screenReader": true,
				"setSelected": {
					"hover": true,
					"parent": true
				 }
			}, {
				// configuration
				offCanvas: {
					pageSelector: "#page-content"
				}
			}).show();
			
			/* remove navbars */
			$('.rem-navbar').parent('.mm-panel').addClass('mm-hasnavbar-rem');
			
			var API = $("#nav-menu").data( "mmenu" );
			
			$('#navbarShow').on('click', function() {
				API.open();
			});
			
			$('.signin>a').on('click', function(e) {
				e.stopPropagation();
				window.top.location = '/login/login.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
	
				API.close();
			});
			$('.createacc>a').on('click', function(e) {
				e.stopPropagation();
				window.top.location = '/social/signup_lite.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
	
	
				API.close();
			});
			$('.logout>a').on('click', function(e) {
				window.top.location = '/logout.jsp';
			});
			
		/* PDP */
			$jq('.pdp-ecoupon').find('.fdCoupon_data:first').prepend('<div class="fdCoupon_arrow fdCoupon_arrow_s fdCoupon_collapseArrow"></div>')
			$('.pdp-ecoupon').on('click', function(e) {
				e.stopPropagation();
				var $this = $(this);
				$this.find('.fdCoupon_collapseArrow').toggleClass('fdCoupon_arrow_n');
				$this.find('.fdCoupon_detContent').show();
				var $saveBtn = $this.find('.fdCoupon_detContent_saveButton');
				if ($this.hasClass('isClipped')) {
					$saveBtn.addClass('disabled');
				}
				$saveBtn.show();
				$(this).toggleClass('open');
				
			});
			$('.pdp-ecoupon .fdCoupon_detContent_saveButton').on('click', function(e) {
				e.stopPropagation();
				e.preventDefault();

				var couponId = $(e.currentTarget).closest('[data-component="ecoupon"]').data('ecouponid');
				if(couponId && fdCouponClip) {
					if (fdCouponClip(couponId)) {
						$(this).addClass('disabled');
					}
				}
			});
	});
}(jQuery));