/* requires jquery, jquery.mmenu */
var API;
(function ($) {
	/* experimental back/forward navigation fix */
	if(!!window.performance && window.performance.navigation.type === 2) {
		window.location.reload(true);
	}
	$(window).on('pageshow', function() { /* fix for back on ios safari */
		$('.cartheader__button').attr('disabled', null);
	});
	$(window).on('pagehide', function() { /* pageshow covers, but just in case */
		$('.cartheader__button').attr('disabled', null);
	});
	
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
					"title": '<img src="/media/mobileweb/images/topbar-fd-logo.png" alt="FreshDirect" class="navbar-brand img-responsive" />'
				},
				"navbars": [
					{
						"position": "bottom",
						"content": (FreshDirect && FreshDirect.locabar && FreshDirect.locabar.hasFdxServices) ? "<div class='navbar-cont'><a href='https://www.foodkick.com' class='locabar-tab locabar-tab-fdx-cont'><span class='offscreen'>Visit Foodkick Store</span><div class='locabar-tab-fdx'></div></a></div>" : ""
					}
				],
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
			
			API = $("#nav-menu").data( "mmenu" );
			
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
			$(".navbar-brand").on('click', function(e) {
				e.stopPropagation();
				e.preventDefault();
				window.location = "/";
				return false;
			});
			
			/*
			API.bind( "init", function() {
				console.log('API init event');
			});
			API.bind( "setSelected", function() {
				console.log('API setSelected event');
			});
			API.bind( "update", function() {
				console.log('API update event');
			});
			API.bind( "openPanel", function(e) {
				console.log('API openPanel event', e);
			});
			API.bind( "openingPanel", function() {
				console.log('API openingPanel event');
			});
			API.bind( "openedPanel", function() {
				console.log('API openedPanel event');
			});
			API.bind( "closeAllPanels", function() {
				console.log('API closeAllPanels event');
			});
			API.bind( "closePanel", function() {
				console.log('API closePanel event');
			});
			API.bind( "closingPanel", function() {
				console.log('API closingPanel event');
			});
			API.bind( "closedPanel", function() {
				console.log('API closedPanel event');
			});
			*/
			
			/*$('a[href="#mm-0"]').on('click', function() {
			});*/
		/* PDP */
			$jq('.pdp-ecoupon').find('.fdCoupon_data:first').prepend('<div class="fdCoupon_arrow fdCoupon_arrow_s fdCoupon_collapseArrow"></div>');
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
			$jq('.portrait-item-coupon').find('.fdCoupon_cont:first').prepend('<div class="fdCoupon_arrow fdCoupon_arrow_s fdCoupon_collapseArrow"></div>');
			$('.portrait-item-coupon').on('click', function(e) {
				e.stopPropagation();
				var $this = $(this);
				$this.find('.fdCoupon_collapseArrow').toggleClass('fdCoupon_arrow_n');
				$this.find('.fdCoupon_detContent').show();
				var $saveBtn = $this.find('.fdCoupon_detContent_saveButton');
				if ($this.find(".fdCoupon_cont").hasClass('isClipped')) {
					$saveBtn.addClass('disabled');
				}
				$saveBtn.show();
				$(this).toggleClass('open');
				
			});
			$('.portrait-item-coupon .fdCoupon_detContent_saveButton').on('click', function(e) {
				e.stopPropagation();
				e.preventDefault();

				var couponId = $(e.currentTarget).closest('[data-component="ecoupon"]').data('ecouponid');
				if(couponId && fdCouponClip) {
					if (fdCouponClip(couponId)) {						
						$(this).addClass('disabled');
					}
				}
			});
		$jq('.mm-page .portrait-item').each(function(i, e){
			if($jq(e).find(".atc-info").attr('data-amount') != 0){
				$jq(e).find(".addtocart").html($jq(e).find(".atc-info").attr('data-amount')).addClass("ATCHasItemsMobile");
			}
		});
		
		/* generic accords */

		$jq('.gen-accord-toggler').each(function(i,e) {
			if (!$jq(e).find('.gen-accord-toggler-arrow').length) {
				//auto add an arrow indicator
				$jq(e).append('<div class="gen-accord-toggler-arrow gen-accord-toggler-arrow_s"></div>');
			}
		});
		$jq('.gen-accord-toggler').on('click touch', function(e) {
            if ($jq(this).hasClass('open')){
                $jq(this).parent().find('.gen-accord-content').hide();
                $jq(this).attr('aria-expanded', false);
            }else {
                $jq(this).parent().find('.gen-accord-content').show();
                $jq(this).attr('aria-expanded', true);
            }
            $jq(this).find('.gen-accord-toggler-arrow').toggleClass('gen-accord-toggler-arrow_n');
            $jq(this).toggleClass('open');
        });
        $jq('.gen-accord-toggler').keydown(function(event){
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if(keycode == '13'){
               event.preventDefault();
                $jq(this).trigger("click");
            }
        });
		
		/* timeslots */
		/* use pre-init to set mobweb to true */
		window['fdTSDisplayPreInitializeFuncs'] = window['fdTSDisplayPreInitializeFuncs'] || [];
		window['fdTSDisplayPreInitializeFuncs'].push(function(argsObj) {
			argsObj.thisObj.opts.isMobWeb = true;
			
		});

		/* use init to set all days as expanded */
		window['fdTSDisplayInitializeFuncs'] = window['fdTSDisplayInitializeFuncs'] || [];
		window['fdTSDisplayInitializeFuncs'].push(function(argsObj) {
			for (var dayId in argsObj.thisObj.dayObjs) {
				argsObj.thisObj.setDayAsExpanded(dayId);
			}
			
		});
		
		/* hooklogic */
			/* fix click beacon */
			/* hooklogic click event */
			window['hlClickHandler'] = function () {
				$('[data-hooklogic-beacon-click]').find('a,button,.portrait-item-productimage_wrapper').each(function(i,e) {
					if (!$(e).data('hooklogic-beacon-click')) {
						/* exclusion elems */
						if (
							$(this).is('[data-component-extra="showSOButton"], .quantity_minus, .quantity_plus')
						) { return; 
						} else {
							$(e).data('hooklogic-beacon-click', 'true');
							$(e).on('click', function(event) {
								var $parent = $(e).closest('[data-hooklogic-beacon-click]');
								var url = $parent.data('hooklogic-beacon-click');
								if ($parent.find('img.hl-beacon-click').length === 0) { /* prevent multiple calls */
									$parent.append('<img class="hl-beacon-click" src="'+url+'&rand='+new Date().getTime()+'" style="display: none;" />');
								}
							});
						}
					}
				});
			}
			window['hlClickHandler'](); /* initial page load */
				
		/* prevent li empty links from doing anything except open their submenu */
		$('.noClickThrough').on('click', function(e) {
			return false;
		});
		
		/* CHECKOUT */
		/* anchor header */
		function co_alignHeader() {
			var cartHeaderEl = $('[data-ec-page] .mm-page #cartheader, [data-ec-page] .mm-page #cartheader_co');
			if (cartHeaderEl) {
				cartHeaderEl.find('.estimated-total').css({
					"padding-top": (cartHeaderEl.find('.right').height() - cartHeaderEl.find('.estimated-total').height()) + "px"
				});
				
				cartHeaderEl.css({'top': $(window).height() - cartHeaderEl.height()  +'px' });
				$('footer').css({'padding-bottom': cartHeaderEl.height()+'px'});
				/* floating footer fix */
				$('html,body').css({
				    '-webkit-overflow-scrolling' : 'touch',
				    'overflow': 'auto',
				    'height': '100%'
				});
			}
		
			if (!$jq('#smartbanner').parent().hasClass('.mobweb-topnav')) { //move banner into nav
				$jq('.mobweb-topnav').prepend($jq('#smartbanner'));
			}
			
			// adjust the content's top position if top nav exists 
			if ($('.mobweb-topnav').length && $('[data-ec-page] .mm-page #content').length) {
				$('[data-ec-page] .mm-page #content').css({'padding-top': $('.mobweb-topnav').outerHeight(true)+'px'});
			}
		}
		$('#smartbanner .sb-close').on('click', co_alignHeader); //resize on smartbanner close
		$('[data-ec-page] .mm-page .mobweb-topnav,[data-ec-page] .mm-page .checkout-top-nav').on('resize', co_alignHeader);
		$(window).on('resize', co_alignHeader);
		co_alignHeader();

		/* display modifying order message */
		window.displayModifyingOrderMobile = window.displayModifyingOrderMobile || function(time, dayOfWeek) {
			if (!time || !dayOfWeek)
				return;
			var msg = '<div id="location-modify-order-message" style="position: static;text-align: center;font-size: 14px;line-height: 16px;padding: 15px 0 15px 0;background-color: #f68139;text-shadow: 1px 1px 0 #c85c19;color: #ffffff;border-bottom: none;">'+
			'<div><strong class="modify-delivery-label">Modifying Delivery: </strong><div class="modify-delivery-time"><span>' + dayOfWeek + ' </span><span style="text-transform: uppercase;text-shadow: 1px 1px 0 rgba(200, 92, 25, 0.5);">' + time + '</span></div></div></div>';
			if ($('#location-modify-order-message').length !== 0){
				$('#location-modify-order-message').replaceWith(msg);
			} else if ($('.mobweb-topnav .navbar').length) {
				$(msg).insertBefore('.mobweb-topnav .navbar');
			}
			
		}
		$(document).trigger('displayModifyingOrderMobile-loaded').off('displayModifyingOrderMobile-loaded');
	});
}(jQuery));