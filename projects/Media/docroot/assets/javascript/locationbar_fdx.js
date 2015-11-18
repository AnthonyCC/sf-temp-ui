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

$jq('#locabar_user_trigger').on('click', function(e) {
	var $this = $jq(this);
	if (!$this.data('signedin') ) {
		if ($this.data('social')) {
			if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
				FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp', height: 580, opacity: .5});
			}
		} else {
			showOverlay(1001);
			$jq('#login_cont_formContent').show();
			centerLoginModal();
		}
	}
});

$jq("#locabar_alerts_trigger").on('click', function() {
	$jq('#messages').messages('open');
});

$jq('#login_cont_formContent').on('hide', function() {
	$jq('#fs_overlay').hide();
});

$jq(window).on('resize', function(event) {
	centerLoginModal();
});

