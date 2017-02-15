function centerLoginModal() {
	if ($jq('#login_cont_formContent').length) {
		$jq( '#login_cont_formContent' ).position({
			my: 'center',
			at: 'center',
			of: window
		});
	}
}

/* fix cart for now, until actual solution is found */
$jq(document).on('ready', function() {
	if ($jq('#popupcart').length) {
		$jq('#popupcart').appendTo('#locabar_popupcart');
		
		$jq( '#locabar_popupcart' ).position({
			my: 'right top',
			at: 'right bottom+8',
			of: '#locabar_popupcart_trigger'
		});
		
		//window.setTimeout(function() {
			/* add an additional align for timing */
			//$jq( '#locabar_popupcart' ).position({
			//	my: 'right top',
			//	at: 'right bottom-12',
			//	of: '#locabar_popupcart_trigger'
			//});
			//if ($jq( '#locabar_popupcart' ).css('top') === '10px' 
			//}, 1000);
		
		
	}
});

/* right align this one arrow */
if ($jq('#locabar_popupcart .ui-arrow.ui-top').length) {
	$jq( '#locabar_popupcart .ui-arrow.ui-top' ).position({
		my: 'right top',
		at: 'right-20 top-16',
		of: '#locabar_popupcart'
	});
}

/* align to triggers */

if ($jq('#locabar_addresses').length) {
	$jq( '#locabar_addresses' ).position({
		my: 'center top',
		at: 'center bottom+8',
		of: '#locabar_addresses_trigger'
	});
}

if ($jq('#locabar_user').length) {
	$jq( '#locabar_user' ).position({
		my: 'center top',
		at: 'center bottom+8',
		of: '#locabar_user_trigger'
	});
}

function showOverlay(zIndexVar) {
	var zIndex = zIndexVar || 0;
	if ($jq('#fs_overlay').length !== 0) {
		if (zIndex > 0) { $jq('#fs_overlay').css('zIndex', zIndex); }
		$jq('#fs_overlay').toggle();
	} else { //defaults to visible
		var ol = '';
			ol += '<div id="fs_overlay" class="ui-widget-overlay ui-front"';
			if (zIndex > 0) {
				ol += ' style="z-index: ' + zIndex + '";'; 
			}
			ol += '></div>';
		$jq('body').prepend(ol);
	}
	$jq('#fs_overlay').on('click', function() { $jq('#fs_overlay').hide(); });
}

$jq('.locabar-logout').on('click', function() {
	window.top.location = '/logout.jsp';
	return false;
});

/* customized rendering */
$jq.widget( "custom.iconselectmenu", $jq.ui.selectmenu, {
	_renderItem: function( ul, item ) {
		var li = $jq( "<li>", { text: item.label } );

		if ( item.disabled ) {
			li.addClass( "ui-state-disabled" );
		}

		$jq( "<span>", {
			style: item.element.attr( "data-style" ),
			"class": "ui-icon " + item.element.attr( "data-class" )
		}).appendTo( li );
	
		return li.appendTo( ul );
	}
});
$jq('#selectAddressList').iconselectmenu({
	appendTo: '#locabar_addresses_choices',
	position: {
		my: 'left top',
		at: 'left top',
		of: '#locabar_addresses_choices'
	},
	create: function(event, ui) {
		$jq('#selectAddressList').iconselectmenu('open');
		iconselectmenuSetEvents();
	},
	open: function(event, ui) {
		//remove document mousedown close listener (ONLY)
		var namespace = ($jq('#selectAddressList').iconselectmenu('instance').eventNamespace);
		$jq(document).off('mousedown'+namespace);
	},
	close: function(event, ui) {
		$jq('#selectAddressList').iconselectmenu('open');
	},
	select: function(event, ui) {
		var key = ui.item.value;
		
		$jq.ajax({
			url: '/api/locationhandler.jsp',
			data: {
				action: 'selectAddress',
				selectAddressList: key
			},
			success: function(data){
				var $refIcon = $jq(event.currentTarget).find('.address-icon:first');
				
				if (window.location.pathname === '/your_account/reserve_timeslot.jsp' && !$refIcon.hasClass('address-type-home')) {
					window.location = '/your_account/delivery_info_avail_slots.jsp';
				} else { //just reload the page
					window.location.reload();
				}
				
			},
			error: function(data){
				/* this needs fixing */
				console.log('address error', data.responseText);
			}
		});
	} 
}).iconselectmenu( "menuWidget" ).addClass( "ui-menu-icons customicons" );

/* add events here, otherwise they'll be lost on refresh */
function iconselectmenuSetEvents() {
	$jq('#locabar_addresses_choices li.ui-menu-item').on('mouseenter mouseleave', function(e){
		var $makeResvButton = $jq('.locabar_addresses-reservation-make');
		var $viewTsButton = $jq('.locabar_addresses-reservation-view');
		
		var $makeResvCont = $jq('.locabar_addresses-reservation-make-cont');
		if (e.type === 'mouseenter') {
			var $refIcon = $jq(this).find('.address-icon:first');
			
			$jq('.locabar_addresses-reservation-make-notFor').data('resvPrevHtml', $jq('.locabar_addresses-reservation-make-notFor').html());
			$makeResvButton.data('resvDisabled', $makeResvButton.hasClass('disabled'));
			
			if ($refIcon.hasClass('address-type-home')) {
				$jq('.locabar_addresses-reservation-make-notFor').html('&nbsp;');
				
				$makeResvButton.removeClass('disabled');

				if ($makeResvButton.length > 0) {
					$viewTsButton.hide();
				}
				
				$makeResvCont.show();
			} else {
				if ($refIcon.hasClass('address-type-cos')) {
					$jq('.locabar_addresses-reservation-make-notFor').html('Not for Office Delivery');
				} else if ($refIcon.hasClass('address-type-pickup')) {
					$jq('.locabar_addresses-reservation-make-notFor').html('Not for Pickup Option');
				}
				$makeResvButton.addClass('disabled');
				$makeResvCont.hide();
				
				$viewTsButton.show();
			}
			
		} else if (e.type === 'mouseleave') {

			$jq('.locabar_addresses-reservation-make-notFor').html( $jq('.locabar_addresses-reservation-make-notFor').data('resvPrevHtml') );
			if ($makeResvButton.data('resvDisabled')) {
				$makeResvButton.addClass('disabled');
				$makeResvCont.hide();
				$viewTsButton.show();
			} else {
				$makeResvButton.removeClass('disabled');
				$makeResvCont.show();

				if ($makeResvButton.length > 0) {
					$viewTsButton.hide();
				}
			}
		}
	});
}


$jq('#locabar_addresses_choices').on('mouseleave', function(e){
	$jq('#selectAddressList').iconselectmenu('refresh');
	iconselectmenuSetEvents();
});

$jq('.locabar_addresses-reservation-make.disabled').on('click', function(e) {
	e.preventDefault();
	return false;
});

function showLoginDialog(successPage, useSocial) {

	if (useSocial) {
		var socialUrl = '/social/login.jsp';
		if (successPage && successPage !== '') {
			socialUrl += '?successPage='+successPage;
		}

		if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
			FreshDirect.components.ifrPopup.open({ url: socialUrl, height: 580, opacity: .5});
		}
	} else {
		if (successPage && successPage !== '') {
			if ($jq('#login_cont_formContentForm #successPage').length === 0) {
				$jq('#login_cont_formContentForm').append('<input type="hidden" id="successPage" name="successPage" value="'+successPage+'" />');
			}
		}


		showOverlay(1001);
		$jq('#login_cont_formContent').show();
		centerLoginModal();
		
		//set focus
		var f = $jq('#login_cont_formContent').find(':focusable');
		if (f.length > 0) {
			f[0].focus();
		}
	}
}

$jq('#locabar_messages_trigger').on('click', function() {
	$jq('#messages').messages('openMessages');
	$jq(this).hide();
	$jq("#messages").addClass("open");
	if($jq('#activatesoalert .so-activate-alert').length && $jq('#activatesoalert .so-activate-alert').length > 0){
		$jq('#activatesoalert').messages('openAlerts', ['activatesoalert']);
		
	}
	
});

$jq('#login_cont_formContent').on('hide', function() {
	$jq('#fs_overlay').hide();
});
$jq('#login_cont_formContent_close').on('click', function() {
	$jq('#login_cont_formContent').hide().trigger('hide');
});

$jq(window).on('resize', function(event) {
	centerLoginModal();
});

$jq('#sitemessage').on('alertOpen', ['sitemessage'], function(event) {
	$jq('#locabar_addresses').parent('.locabar_triggers').addClass('alertOpen');
	$jq('#locabar_addresses').closest('.locabar-section').addClass('alertOpen');
	$jq('#locabar_addresses').hide();
});
$jq('#sitemessage').on('alertClose', ['sitemessage'], function(event) {
	$jq('#locabar_addresses').parent('.locabar_triggers').removeClass('alertOpen');
	$jq('#locabar_addresses').closest('.locabar-section').removeClass('alertOpen');
	$jq('#locabar_addresses').show();
});

//for APPBUG-4773
$jq("#selectAddressList-menu").on('hover mouseover', function(e) {
	$jq(this).find(".locabar-check-text").parent("li").addClass("ui-state-focus");
});

/* keyboard navigation */
FreshDirect.locabar.lastFocusElemId = '';
$jq('.locabar_triggers').on('focus retClose', function(event) {
	var prevId = FreshDirect.locabar.lastFocusElemId;
	if (prevId !== '' && ( ($jq(this).attr('id') !== $jq(prevId).attr('id')) || (event.type == 'retClose') )) {
		$jq(prevId).removeClass('hover');
		$jq(prevId).find('[aria-hidden="false"]:first').attr('aria-hidden', true);
	} 
});
$jq('.locabar_triggers').on('mouseenter', function(event) {
	$jq(this).parent().addClass('mouse');
});
$jq('.locabar_triggers').on('mouseleave', function(event) {
	//if keyboard <-> mouse
	//$jq(this).parent().removeClass('mouse');
});

$jq('.locabar_triggers').on('keyup', function(event) {
	var $this = $jq(this);
	var curId = $this.attr('id');
	if (event.keyCode == 13) {
		if (curId == 'locabar_popupcart_trigger') { // send to cart instead...
			document.location = '/view_cart.jsp';
		} else if (curId == 'locabar_user_trigger' && !$this.data('signedin') && !$this.data('recog')) { //allow login
			$this.trigger('click');
		} else {
			if ($this.hasClass('hover')) { //allow manual closing
				$this.trigger('retClose');
			} else {
				$this.addClass('hover');
				$this.find('[aria-hidden="true"]:first').attr('aria-hidden', false);
				FreshDirect.locabar.lastFocusElemId = '#'+curId;
				
				//set focus
				var f = $this.find(':focusable');
				if (f.length > 0) {
					f[0].focus();
				}
			}
		}
	}
});
$jq('#modifyorderalert').on('alertOpen', ['modifyorderalert'], function(event) {
	$jq('#locabar_orders').parent('.locabar_triggers').addClass('alertOpen');
	$jq('#locabar_orders').closest('.locabar-section').addClass('alertOpen');
	$jq('#locabar_orders').hide();
});
$jq('#modifyorderalert').on('alertClose', ['modifyorderalert'], function(event) {
	$jq('#locabar_orders').parent('.locabar_triggers').removeClass('alertOpen');
	$jq('#locabar_orders').closest('.locabar-section').removeClass('alertOpen');
	$jq('#locabar_orders').show();
	$jq('.locabar-modify-order-section').show();
	$jq("#locabar_modify_order_trigger").focus();
	if($jq('.locabar-modify-order-section').css("display")==="none" && $jq("#locabar_user_trigger .changeBGClr").length ==0){
		$jq('.locabar-modify-order-section').show();
		$jq("#locabar_modify_order_trigger").focus();
	}
});
$jq(document).ready(function() {
	function messagesOpened() {
		$jq('#locabar-messages-open').parent('.locabar_triggers').addClass('alertOpen');
		$jq('#locabar-messages-open').closest('.locabar-section').addClass('alertOpen');
		if($jq("#newziptext").length && ($jq("#newziptext").val()=="" || $jq("#newziptext").val().length >=5)){
			$jq('#messages').removeClass("open");
		}
		
		
	} 
	if ($jq('#messages').hasClass('open')) { /* doc ready will miss the initial messagesOpen trigger */
		messagesOpened();

	}
	$jq('#messages').on('messagesOpen', function(event) {
		messagesOpened()
	});
//alert the user while entering wrong zip code or less then  digits
		$jq('#messages').on('messagesClose', function(event) {
				$jq('#locabar-messages-open').parent('.locabar_triggers').removeClass('alertOpen');
				$jq('#locabar-messages-open').closest('.locabar-section').removeClass('alertOpen');
				if ($jq('.messages-count').data('count') > 0) {
					$jq('#locabar_messages_trigger').show();
					$jq('#locabar_messages_trigger').focus();
				}
				

				
				if($jq('#locabar_messages_trigger').css("display")==="block"){
					$jq('#locabar_messages_trigger').show();
					$jq("#locabar_messages_trigger").focus();
				}
			}); 

	if ($jq('#locabar_addresses .locabar_addresses-anon-deliverable').length) {
		$jq('#locabar_addresses').addClass('anon-deliverable');
		$jq('#locabar_addresses .ui-arrow.ui-top').addClass('anon-deliverable');
	}
	if ($jq.fn.messages('isClosed')) {
		$jq('#locabar_messages_trigger').show();
		
		//$jq('.locabar-modify-order-section').css("display","block");
		//$jq("#locabar_modify_order_trigger").focus();
	} 
});

//delivery information change zip code button click event
$jq('.locabar_addresses-anon-deliverable-change-zip-toggle-btn').on('click', function(e) {
	e.preventDefault();
	$jq('.locabar_addresses-anon-deliverable-change-zip-toggle-target').show();
	$jq('.locabar_addresses-anon-deliverable-change-zip-toggle-target').css("display","block");
	$jq(".locabar_addresses-anon-deliverable-change-zip-toggle-target #newzip #newziptext").focus();
	$jq(this).hide();
	
		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibility","visible");	
		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","1");
	
	$jq('.locabar_addresses-anon-deliverable-change-zip-toggle-target').css("display","block");
	
	$jq("#newziptext").focus();
	if($jq("#newziptext").val()===""){
		$jq('#messages').removeClass("open");
		$jq("#locabar_messages_trigger").show();
		
	}
	
	//return false;
});



$jq('.locabar_addresses-anon-deliverable-add-address-btn').on('click', function() {
	window.location = '/registration/signup.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
});

$jq('#locabar_addresses_trigger').on('focus mouseover', function(event) {
	$jq('.locabar-addresses-section').css('background-color', '#4fa157');
});

$jq('#locabar_addresses_trigger').on('blur mouseleave', function(event) {
	$jq('.locabar-addresses-section').css('background-color', '#6AAA6D');
});

$jq('#locabar_user_trigger').on('focus mouseover', function(event) {
	$jq('.locabar-user-section').css('background-color', '#4fa157');
});

$jq('#locabar_user_trigger').on('blur mouseleave', function(event) {
	$jq('.locabar-user-section').css('background-color', '#6AAA6D');
});

$jq('#locabar_popupcart_trigger').on('focus mouseover', function(event) {
	$jq('.locabar-popupcart-section').css('background-color', '#4fa157');
});

$jq('#locabar_popupcart_trigger').on('blur mouseleave', function(event) {
	$jq('.locabar-popupcart-section').css('background-color', '#6AAA6D');
});

$jq('#locabar_modify_order_trigger').on('focus mouseover', function(event) {
	$jq('.locabar-modify-order-section').css('background-color', '#4fa157');
});

$jq('#locabar_modify_order_trigger').on('blur mouseleave', function(event) {
	//console.log('blur on locabar_triggers ');
	$jq('.locabar-modify-order-section').css('background-color', '#6AAA6D');
});


$jq('#locabar_messages_trigger').on('focus mouseover', function(event) {
	$jq('.locabar-messages-section').css('background-color', '#4fa157');
});

$jq('#locabar_messages_trigger').on('blur mouseleave', function(event) {
	$jq('.locabar-messages-section').css('background-color', '#6AAA6D');
});

$jq('.changeBGClr').on('focus mouseover', function(event) {
	$jq('.locabar-user-section').css('background-color', '#4fa157');
});

$jq('.changeBGClr').on('blur mouseleave', function(event) {
	$jq('.locabar-user-section').css('background-color', '#6AAA6D');
});


//press enter key to display message
$jq('#locabar_messages_trigger').keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13'){
		$jq('#messages').messages('openMessages');
		$jq('#messages').addClass('open');
		$jq(this).hide();
		$jq("#messages.hashandler.open a").focus();
		if($jq('#activatesoalert .so-activate-alert').length && $jq('#activatesoalert .so-activate-alert').length > 0){
			$jq('#activatesoalert').messages('openAlerts', ['activatesoalert']);			
		}
	}
});


//enter key to modify button order
$jq('#locabar_modify_order_trigger').keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13'){
		$jq("#locabar_modify_order_trigger #locabar_orders").css("opacity","1");
		$jq("#locabar_modify_order_trigger #locabar_orders").css("visibility","visible");
		$jq(".locabar-modify-order-dropdown-container-delails a").focus();
		//event.preventDefault();
	}
});

//enter key for orders

$jq('#locabar_addresses_trigger').keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13'){
		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","");
		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibility","");
		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").focus();
		$jq("#locabar_addresses").css("display","");
		
		//event.preventDefault();
	}
});
//reset the values of modify order section when we focus using mouse
$jq(".locabar-modify-order-section").hover(function(){
	$jq("#locabar_orders").css("opacity","");
	$jq("#locabar_orders").css("visibility","");
});

$jq("#locabar_addresses_trigger").hover(function(){
		if (!$jq('#nodeliver-form:visible').length) {
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","");
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibility","");
			$jq(this).removeClass("hover");
			$jq("#locabar_addresses").css("display","");
		}
	}); 


//check if the alert box is present or not during tab press...

$jq(".locabar-modify-order-dropdown-container-modify").keydown(function(e){
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			return true;
			
		} else {
			$jq("#locabar_modify_order_trigger #locabar_orders").css("opacity","0");
			$jq("#locabar_modify_order_trigger #locabar_orders").css("visibility","hidden");
			if($jq("#locabar_messages_trigger").css("display")=="none" || $jq(".locabar-messages-section").css("display")=="none" ){
				$jq("#locabar_addresses_trigger").focus();
			}else{
				$jq("#locabar_messages_trigger").focus();
			}
			
			
		}
		e.preventDefault();
	}
});
//text-box-focs
//$jq("#newziptext").focus();

$jq(".locabar-modify-order-dropdown-container-delails ").keydown(function(e){
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			$jq("#locabar_modify_order_trigger #locabar_orders").css("opacity","0");
			$jq("#locabar_modify_order_trigger #locabar_orders").css("visibility","hidden");
			$jq("#locabar_modify_order_trigger").focus();
			
		} else {
			return true;
		}
		e.preventDefault();
	}
});
//escape key

$jq(".locabar-modify-order-dropdown-container-delails,.locabar-modify-order-dropdown-container-modify").keyup(function(event){
	if(event.keyCode == 27){
		$jq("#locabar_modify_order_trigger #locabar_orders").css("opacity","0");
		$jq("#locabar_modify_order_trigger #locabar_orders").css("visibility","hidden");
		$jq("#locabar_modify_order_trigger").focus();
		event.preventDefault();
	}
});

//green add delivery button
$jq(".locabar_addresses-anon-deliverable-add-address-btn").keydown(function(e){
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			return true;
			
		} else {
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","0");
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibilty","hidden");
			$jq(".changeBGClr").focus();
			$jq(".locabar-user-section mouse").css('background-color', '#4fa157');
			$jq("#locabar_addresses_trigger").removeClass("hover");
			$jq("#locabar_addresses").hide();
		}
		e.preventDefault();
	}
});

//loginbutton
$jq(".changeBGClr").keydown(function(e){
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			$jq("#locabar_addresses_trigger").focus();
			
		} else {
			return true;
		}
		e.preventDefault();
	}
});

//hide the about fresh popup shift+tab


$jq(".locabar_addresses-change-zip-cont input").keydown(function(e){
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","1");
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibilty","auto");
		
			
		} else {
			return true;
		}
		e.preventDefault();
	}
});



//mpdify order alert

$jq(window).load(function(){
	
	//var IsModifyOrder=$jq("#modifyorderalert").length;
	if($jq("#modifyorderalert").hasClass("open")){
		$jq(".locabar-modify-order-section").hide();
	}
	
	if($jq("#locabar_user_trigger .changeBGClr").length >0){
		$jq('.locabar-modify-order-section').hide();
	}
	
	if($jq("#messages").hasClass("open")){
		$jq('#locabar_messages_trigger').hide();
	}
	
	$jq('#locabar_messages_trigger').blur();
	//cart lin - background change

  	$jq(document).on('focusin',"#popupcart .cartline .qty",function(e){
		var TABKEY = 9;			
		$jq(".cartline").css("background","#fff");
		$jq(this).parent().parent().parent().parent().css("background","#f1f1f1");
		e.preventDefault();
		//return true;*/
	});
  	
  	$jq(document).on("focusout","#locabar_addresses",function(){
  		//$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","0");
  		//$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibilty","hidden");
  	});
  	
  	$jq(".locabar_addresses-anon-deliverable-item-icon-truck").keydown(function(e){
  		var TABKEY = 9;
  		if (e.which == TABKEY) {
  			if (e.shiftKey) {
  				$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibilty","hidden");  
  				$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","0");
  				$jq("#locabar_addresses_trigger").removeClass("hover");
  				$jq(this).blur();
  				//$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable input").blur();
  				$jq("#locabar_addresses_trigger").focus();
  				$jq("#locabar_addresses").hide();
  				
  			} else {
  				return true;
  			}
  			e.preventDefault();
  		}
  	});
	$jq("#locabar_addresses_trigger #locabar_addresses input").keyup(function(e){
		if(e.keyCode == 27){
  				$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibilty","hidden");  
  				$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","0");
  				
  				$jq(this).blur();
  				//$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable input").blur();
  				$jq("#locabar_addresses_trigger").focus();
  				$jq("#locabar_addresses").hide();
  				
  			} 
  			e.preventDefault();
  		
  	});
  	
  	
  	$jq(".locabar-addresses-section").hover(function(){
  		if($jq("#location-alerts #sitemessage").css("display")=="block"){
  			$jq("#locabar_addresses").hide();
  		}
  		else{
  			$jq("#locabar_addresses").show();
  		}
  	});
  	
  	$jq(document).on('focusin',".locabar-addresses-section",function(e){
  		if($jq("#location-alerts #sitemessage").css("display")=="block"){
  			$jq("#locabar_addresses").hide();
  		}
		e.preventDefault();
		//return true;*/
	});
  	



  	$jq(".locabar-addresses-section").keypress(function(event){
  		var keycode = (event.keyCode ? event.keyCode : event.which);
  		if(keycode == '13'){
  			if($jq("#location-alerts #sitemessage").css("display")=="block"){
  	  			$jq("#locabar_addresses").hide();
  	  		}
  			event.preventDefault();
  		}
  	});
});


/*enter key to close the alert
$jq("#locabar-messages-close").keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13'){
		$jq('#locabar-messages-open').parent('.locabar_triggers').removeClass('alertOpen');
		$jq('#locabar-messages-open').closest('.locabar-section').removeClass('alertOpen');
		if($jq("#locabar_messages_trigger").css("display")=="none"){
				$jq("#locabar_messages_trigger").show();
				$jq("#locabar_messages_trigger").focus();
			}	
	}
});

*/

// alert box close

//locabar_messages_trigger
//escape to hide for 
//change button
//enter key for change zip code in delivery information
$jq(".locabar_addresses-anon-deliverable-change-zip-toggle-btn").keypress(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13'){
		$jq(this).hide();
		$jq(".locabar_addresses locabar_triggers_menu").css("visibility","visible");
		$jq(".locabar_addresses locabar_triggers_menu").css("opacity","1");
		
		$jq(".locabar_addresses-anon-deliverable-change-zip-toggle-target").css("display","");
		setTimeout(function(){
			$jq(".locabar_addresses-change-zip-cont input#newziptext").focus();
		},100);
		event.preventDefault();
	}
});
/*
//message alert
$jq(".locabar-messages-section").click(function(){
	//$jq(this).hide();
});

//close the message replace the id with system message
$jq("#messages .content").click(function(e){
	e.preventDefault();
	//jq("#messages").removeClass("open");
	//$jq(".locabar-messages-section").show();
});

//modify oder
$jq("#modifyorderalert .alert-closeHandler").click(function(){alert("ss")
	$("#locabar_modify_order_trigger").hide();
});*/