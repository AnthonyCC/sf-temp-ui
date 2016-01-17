function centerLoginModal() {
	$jq( '#login_cont_formContent' ).position({
		my: 'center',
		at: 'center',
		of: window
	});
}
/* fix cart for now, until actual solution is found */
$jq(document).on('ready', function() {
	$jq('#popupcart').appendTo('#locabar_popupcart');
	
	$jq( '#locabar_popupcart' ).position({
		my: 'right top',
		at: 'right bottom+8',
		of: '#locabar_popupcart_trigger'
	});
});

/* right align this one arrow */
$jq( '#locabar_popupcart .ui-arrow.ui-top' ).position({
	my: 'right top',
	at: 'right-20 top-16',
	of: '#locabar_popupcart'
});

/* align to triggers */
$jq( '#locabar_addresses' ).position({
	my: 'center top',
	at: 'center bottom+8',
	of: '#locabar_addresses_trigger'
});
$jq( '#locabar_user' ).position({
	my: 'center top',
	at: 'center bottom+8',
	of: '#locabar_user_trigger'
});

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
				window.location.reload();
				//console.log('locationhandler success');
			},
			error: function(data){
				/* this needs fixing */
				console.log('address error', data.responseText);
			}
		});
	} 
}).iconselectmenu( "menuWidget" ).addClass( "ui-menu-icons customicons" );

$jq('#locabar_addresses_choices' ).on('mouseleave', function(){
	$jq('#selectAddressList').iconselectmenu('refresh');
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
	}
}

$jq('#locabar_user_trigger').on('click', function(event) {
	var $this = $jq(this);
	if (!($this.data('signedin') || $this.data('recog'))) { //not signed in, not recog
		showLoginDialog($jq.QueryString["successPage"], $this.data('social'));
	} else if (!$this.data('signedin') && $this.data('recog')) { //not signed in, recog
		$e = $jq(event.target);
		if (!$e.attr('target')) {
			event.preventDefault();
			showLoginDialog(($e.attr('href')).replace($e.attr('origin'), ''), $this.data('social'));
		}
	}
}); 

$jq('#locabar_messages_trigger').on('click', function() {
	$jq('#messages').messages('openMessages');
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

$jq('#location-alerts').on('alertOpen', function() {
	$jq('#locabar_addresses').hide();
});
$jq('#location-alerts').on('alertClose', function() {
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

$jq('.locabar_triggers').on('keydown', function(event) {	
	var curId = $jq(this).attr('id');
	if (event.keyCode == 13) {
		// send to cart instead...
		if (curId == 'locabar_popupcart_trigger') {
			document.location = '/view_cart.jsp';
		} else {
			if ($jq(this).hasClass('hover')) { //allow manual closing
				$jq(this).trigger('retClose');
			} else {
				$jq(this).addClass('hover');
				$jq(this).find('[aria-hidden="true"]:first').attr('aria-hidden', false);
				FreshDirect.locabar.lastFocusElemId = '#'+curId;
				
				//set focus
				var f = $jq(this).find(':focusable');
				if (f.length > 0) {
					f[0].focus();
				}
			}
		}
	}
});