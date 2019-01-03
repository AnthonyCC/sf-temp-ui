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
				window.top.location = '/login/login.jsp?successPage=' + encodeURIComponent(window.location.pathname + window.location.search + window.location.hash);
	
				API.close();
				return false;
			});
			$('.createacc>a').on('click', function(e) {
				e.stopPropagation();
				window.top.location = '/social/signup_lite_mobile.jsp?successPage=' + encodeURIComponent(window.location.pathname + window.location.search + window.location.hash);
	
	
				API.close();
				return false;
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
			
			function couponPrep() {

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
			}
			couponPrep();
			if (fd && fd.common && fd.common.signalTarget) {
				Object.create(fd.common.signalTarget,{
					allowNull:{
						value:true
					},
					signal:{
						value:'items'
					},
					callback:{
						value:function(data) {
							couponPrep();
						}
					}
				}).listen();
			}
			
			
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
		var ua = window.navigator.userAgent;
		var iOS = !!ua.match(/iPad/i) || !!ua.match(/iPhone/i);
		var webkit = !!ua.match(/WebKit/i);
		var iOSSafari = iOS && webkit && !ua.match(/CriOS/i);
		/* anchor header */
		function co_alignHeader() {
			var cartHeaderEl = $('[data-ec-page] .mm-page #cartheader, [data-ec-page] .mm-page #cartheader_co');
			if (cartHeaderEl) {

				// if this is ios safari, add padding because of its navigation bar at the bottom
				if (iOSSafari) {
					// check to see if navigation bar is showing
					var navBarHeight = window.innerHeight - $(window).height();
					cartHeaderEl.css({'padding-bottom' : (navBarHeight > 0? 44 : 0)  + 'px'});
					
				} 
				cartHeaderEl.find('.estimated-total').css({
					"padding-top": (cartHeaderEl.find('.right').height() - cartHeaderEl.find('.estimated-total').height()) + "px"
				});
				
				cartHeaderEl.css({'top': window.innerHeight - cartHeaderEl.outerHeight() +'px' });
				$('footer').css({'padding-bottom': cartHeaderEl.outerHeight() + 'px'});
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
		
		/* show/hide search icon */
		$(document).on('keyup change', '#topSearchField', function(e) {
			if ($(e.target).val() !== '') {
				$('#search span.icon-cancel-circle-after').show();
			} else {
				$('#search span.icon-cancel-circle-after').hide();
			}
		});
		/* set icon to clear field */
		$('#search span.icon-cancel-circle-after').on('click', function(e) {
			$('#topSearchField').val('').trigger('change');
		});
		
		$('#open-chat-popup').on('click', function(e) {
			API.close();
			doOverlayDialogNew('/includes/chat_popup.jsp');
		});
	});
}(jQuery));
