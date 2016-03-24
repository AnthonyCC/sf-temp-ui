/* refactor these addToSo calls to make them all one */
function addToSoEvenBetter($clickedButton) {
	var that = $clickedButton;
	var ids = $jq(that).parent().find('.so-select').val().split(':');
	$jq.post('/api/standingOrderCartServlet',
		{
			data: JSON.stringify({
				actiontype: 'AddProductToSO',
				standingOrderId: ids[0],
				listId: ids[1],
				items: FreshDirect.components.AddToCart.atcFilter(
					FreshDirect.modules.common.productSerialize($jq('#evenBetterPopup .pdp-evenbetter-atc'))
				)
			})
		},
		function(data) {
			addToSoSuccessHandler(that, data);
		}
	).fail(function(data) {
		addToSoErrorHandler(data);
	});
}
function addToSoCustomize($clickedButton) {
	console.log('addToSoCustomize');
	var that = $clickedButton;
	var ids = $jq(that).parent().find('.so-select').val().split(':');
	$jq.post('/api/standingOrderCartServlet',
		{
			data: JSON.stringify({
				actiontype: 'AddProductToSO',
				standingOrderId: ids[0],
				listId: ids[1],
				items: FreshDirect.components.AddToCart.atcFilter(
					FreshDirect.modules.common.productSerialize($jq(that).closest('form[fdform="customize"]'))
				)
			})
		},
		function(data) {
			addToSoSuccessHandler(that, data);
		}
	).fail(function(data) {
		addToSoErrorHandler(data);
	});
}

$jq('button[data-component="addToSOButton"]').on('click', function() {
	console.log('addtoSo');
	var ids = $jq('.so-select').val().split(':');
	var that = this;
	$jq.post('/api/standingOrderCartServlet',
		{
			data: JSON.stringify({
				actiontype: 'AddProductToSO',
				standingOrderId: ids[0],
				listId: ids[1],
				items: FreshDirect.components.AddToCart.atcFilter(
					FreshDirect.modules.common.productSerialize($jq('.pdp-productconfig'))
				)
			})
		},
		function(data) {
			addToSoSuccessHandler(that, data);
		}
	).fail(function(data) {
		addToSoErrorHandler(data);
	});
});

function addToSoSuccessHandler($contextElem, data) {
	if (!data.success) {
		return addToSoErrorHandler(data);
	}
	
	var $soResultsCont = $jq($contextElem.closest('.so-container')).find('.so-results-content');

	$soResultsCont.find('.so-results-addedTo').html('Added to: <a href="/quickshop/standing_orders.jsp?soId='+data.id+'">'+data.name+'</a>');
	$soResultsCont.find('.so-results-items-total').html(data.productCount+', '+'<span class="total">$'+data.amount+'</span>');
	$soResultsCont.find('.so-results-changes-required').html(data.message);

	$soResultsCont.toggleClass('so-close');
	
	function soResultsClose() {
		$soResultsCont.addClass('so-close');
	}
}

function addToSoErrorHandler(data) {
	switch (data.error) {
		case 'Session Expired':
			//call session expired: refresh overlay functionality
			FreshDirect.common.dispatcher.signal('errorDialog', {message: '<div class="unauthorized">Session expired, please refresh!</div>'});
			break;
		default:
			if (data.hasOwnProperty('error') && data.hasOwnProperty('message')) {
				console.log('AddToSo Error', data.error, data.message);
			} else {
				console.log('AddToSo Error', data);
			}
	}
	
}

$jq('.so-toggler').on('click', function(e) {
	e.stopPropagation();
	$jq(this).parent('.so').toggleClass('so-close'); //toggle before setting
	var isOpen = !$jq(this).parent('.so').hasClass('so-close'); //has = closed, so send opposite bool

	$jq.ajax({
		url: '/api/manageStandingOrder',
		type: 'POST',
		data: "action=setOpenStatus&setOpenStatusTo="+isOpen,
		success: function(data) {
		},
		fail: function(data) {
			console.log('error: so-toggler', data);
		}
	});

	return false;
});

$jq('.so-test-added-toggler').on('click', function(e) {
	e.stopPropagation();
	$jq(this).closest('.so-container').find('.so-results-content').toggleClass('so-close');
	
	function soResultsClose() {
		$jq('.so-results-content').addClass('so-close');
	}
	window.setTimeout(soResultsClose, 3000);
	return false;
});

$jq('.pdp-evenbetter-soPreShow .cssbutton[data-component="showSOButton"]').on('click', function(e) {
	e.stopPropagation();
	$jq(this).closest('.pdp-evenbetter-soPreShow').toggleClass('pdp-evenbetter-soPreShow pdp-evenbetter-soShow');
	return false;
});


$jq('.cssbutton[data-component="createSOButton"]').on('click', function(e) {
	e.stopPropagation();
	window.location = "/quickshop/standing_orders.jsp";
	return false;
});

