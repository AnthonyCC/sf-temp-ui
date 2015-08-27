<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/jquery.jsonview.css?v=1mar2015" />
<script type="text/javascript" src="../js/jquery.min.js?v=1mar2015"></script>
<script type="text/javascript" src="../js/urlEncode.js?v=1mar2015"></script>
<script type="text/javascript" src="../js/jquery.cookie.js?v=1mar2015"></script>
<script type="text/javascript" src="../js/jquery.jsonview.js?v=1mar2015"></script>

<script language="Javascript">
$(document).ready(function(){
  var version = $.cookie("version");
  if (version) {
    $("#version").val(version);
  } else {
    $("#version").val("1");
  }
  
  // Setup the ajax indicator
	$("body").append('<div id="ajaxBusy"><p><img src="../images/loading.gif"></p></div>');
	$('#ajaxBusy').css({
		display:"none",
		margin:"0px",
		paddingLeft:"0px",
		paddingRight:"0px",
		paddingTop:"0px",
		paddingBottom:"0px",
		position:"absolute",
		right:"3px",
		top:"3px",
		width:"auto"
	});

	// Ajax activity indicator bound 
	// to ajax start/stop document events
	$(document).ajaxStart(function(){ 
		$('#ajaxBusy').show(); 
	}).ajaxStop(function(){ 
		$('#ajaxBusy').hide();
	});
  
  $("#url").keyup(function(event){
    if(event.keyCode == 13){
        doStuff();
    }
  });
  
  $("#payload").keyup(function(event){
    if(event.keyCode == 13){
        doStuff();
    }
  });
  
  	$("#result").JSONView({});
  
	$('#collapse-btn').on('click', function() {
	  $('#result').JSONView('collapse');
	});

	$('#expand-btn').on('click', function() {
	  $('#result').JSONView('expand');
	});

	$('#toggle-btn').on('click', function() {
	  $('#result').JSONView('toggle');
	});

	$('#toggle-level1-btn').on('click', function() {
	  $('#result').JSONView('toggle', 1);
	});

	$('#toggle-level2-btn').on('click', function() {
	  $('#result').JSONView('toggle', 2);
	});

});


function loadStuff() {
  var loaddata = $("#loaddata").val();
  if(loaddata == "Signup") {  	
  	$("#url").val("/saptest12@freshdirect.com/register/");
  	$("#payload").val('{ "firstName": "Sairam", "lastName":"Krishnasamy", "email":"iphonetest@freshdirect.com", "confirmEmail":"iphonetest@freshdirect.com", "password":"test", "securityQuestion":"newyork"}');
  }else if(loaddata == "SignupFDX") {  	
  	$("#url").val("/saptest12@freshdirect.com/registerfdx/");
  	$("#payload").val('{"email" : "fd_1@yahoo.com", "password" : "test", "securityQuestion" : "new york", "firstName" : "Sairam", "lastName" : "Krishnasamy", "serviceType" : "HOME", "companyName" : "ABC Inc.", "address1" : "64 Bevan Street", "apartment" : "64", "city" : "jersey city", "state" : "NJ", "zipCode" : "07306" , "mobile_number": "2035594465", "workPhone" : "2013345534" , "recieveSMSAlerts" : "true"}');	
  }else if(loaddata == "CheckByZip") {  	
  	$("#url").val("/saptest12@freshdirect.com/zipcheck/checkbyzip/");
  	$("#payload").val('{ "zipCode" : "11101", "serviceType" : "HOME" }');
  }else if(loaddata == "globalalerts") {  	
  	$("#url").val("/saptest12@freshdirect.com/globalalerts/");
  	$("#payload").val('{"page" : "1"}');
  }else if(loaddata == "CheckByAddress") {  	
  	$("#url").val("/saptest12@freshdirect.com/zipcheck/checkbyaddress/");
  	$("#payload").val('{ "zipCode" : "11101", "serviceType" : "HOME", "address1" : "", "apartment" : "", "city" : "", "state" : ""}');
  }else if(loaddata == "CheckByAddressEX") {  	
  	$("#url").val("/saptest12@freshdirect.com/zipcheck/checkbyaddressEX/");
  	$("#payload").val('{ "zipCode" : "07306", "serviceType" : "HOME", "address1" : "64 Bevan Street", "apartment" : "64", "city" : "Jersey City", "state" : "Nj"}');
  } else if(loaddata == "Login") {  	
  	$("#url").val("/saptest12@freshdirect.com/login/");
  	$("#payload").val('{ "username" : "bogus@freshdirect.com", "password" : "test" }');
  } else if (loaddata == "Session") {
    $("#url").val("/saptest12@freshdirect.com/session/check/");
    $("#payload").val('{"source" : "IFX"}');
  } else if(loaddata == "AddAnonymousAddress") {  	
  	$("#url").val("/saptest12@freshdirect.com/session/addanonymousaddress/");
  	$("#payload").val('{ "zipCode" : "11101", "serviceType" : "HOME", "address1" : "", "apartment" : "", "city" : "", "state" : ""}');
  } else if (loaddata == "AddPromo") {
  	$("#url").val("/saptest12@freshdirect.com/cart/promo/apply/TEST_P0002");
  	$("#payload").val('');
  }  else if (loaddata == "AddCode") {
  	$("#url").val("/saptest12@freshdirect.com/cart/code/apply/TEST_P0002");
  	$("#payload").val('');
  } else if (loaddata == "ClipCoupon") {
  	$("#url").val("/saptest12@freshdirect.com/cart/coupon/clip/53421");
  	$("#payload").val('');
  	
  } else if (loaddata == "RemovePromo") {
  	$("#url").val("/saptest12@freshdirect.com/cart/promo/remove/TEST_P0002");
  	$("#payload").val('');
  	
  } else if (loaddata == "AddItem-SimpleBeef") {
  	$("#url").val("/saptest12@freshdirect.com/cart/add/");
  	var postdata = '{'+
		'\n	"productConfiguration" : {'+
		'\n		"product" : { '+
		'\n			"id" : "bstk_rbeye_bnls", '+
		'\n			"categoryId" : "bgril", '+
		'\n			"sku" : {'+
		'\n				"code" : "MEA0004676" '+
		'\n			}'+
		'\n		},'+
		'\n		"quantity" : "2",'+
		'\n		"salesUnit" : "E02",'+
		'\n		"options" : { "C_MT_BF_MAR" : "N" , "C_MT_BF_PAK" : "ST" } '+
		'\n	}'+
		'\n}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "AddItem-SimpleWine") {
  	$("#url").val("/saptest12@freshdirect.com/cart/add/");
  	var postdata = '{'+
		'\n	"productConfiguration" : {'+
		'\n		"product" : { '+
		'\n			"id" : "usq_chi_alfa_merl", '+
		'\n			"categoryId" : "usq_red", '+
		'\n			"sku" : {'+
		'\n				"code" : "WIN0073196" '+
		'\n			}'+
		'\n		},'+
		'\n		"quantity" : "1"'+
		'\n	}'+
		'\n}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "UpdateItem-SimpleBeef") {
  	$("#url").val("/saptest12@freshdirect.com/cart/update/");
  	var postdata = '{'+
		'\n	"cartLineId" : "-580423507",'+
		'\n	"productConfiguration" : {'+
		'\n		"product" : { '+
		'\n			"id" : "bstk_rbeye_bnls", '+
		'\n			"categoryId" : "bgril", '+
		'\n			"sku" : {'+
		'\n				"code" : "MEA0004675" '+
		'\n			}'+
		'\n		},'+
		'\n		"quantity" : "1",'+
		'\n		"salesUnit" : "E04",'+
		'\n		"options" : { "C_MT_BF_MAR" : "N" , "C_MT_BF_PAK" : "VP" } '+
		'\n	}'+
		'\n}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "RemoveItem") {
  	$("#url").val("/saptest12@freshdirect.com/cart/remove/621581041");
  	$("#payload").val('');
  	
  } else if (loaddata == "ViewItem") {
  	$("#url").val("/saptest12@freshdirect.com/cart/viewitem/621581041");
  	$("#payload").val('');
  	
  } else if (loaddata == "RemoveAllItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/removeallitems/");
  	$("#payload").val('');
  	  	
  } else if (loaddata == "ViewItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/getdetail/");
  	$("#payload").val("");
  	
  } else if (loaddata == "SetTip") {
  	$("#url").val("/saptest12@freshdirect.com/cart/tip/set/1.5");
  	$("#payload").val('');
  	
  }  else if (loaddata == "InitCheckout") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/init/");
    $("#payload").val('');
  	
  } else if (loaddata == "CheckoutAuthenticate") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/authenticate/");
    $("#payload").val('{ "username" : "bogus@freshdirect.com", "password" : "test" }');
  	
  } else if (loaddata == "GetDeliveryAddresses") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/getall/");
  	$("#payload").val("");
  	
  } else if (loaddata == "SetDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/set/");
  	$("#payload").val('{ "id" : "2150625068", "type" : "RESIDENTIAL"  }');  	
  } else if (loaddata == "SetDeliveryAddressEx") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddress/set/");
  	$("#payload").val('{ "id" : "2150625068", "type" : "RESIDENTIAL"  }');
  	SetDeliveryAddressEx
  } else if (loaddata == "AcceptDeliveryPassTermsAndConditionsReturnTimeslots") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/dpacceptterms/");
  	$("#payload").val("");
  	
  } else if (loaddata == "ReserveDeliverySlot") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliverytimeslot/reserve/2150625068/");
  	$("#payload").val('');
  	
  } else if (loaddata == "ReserveDeliverySlotEx") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliverytimeslot/reserveex/2150625068/");
  	$("#payload").val('');
  	
  }else if (loaddata == "ATPErrorDetails") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/atp/error/");
  	$("#payload").val('');
  	
  } else if (loaddata == "ATPRemove") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/atp/removeitems/");
  	$("#payload").val('');
  	
  }  else if (loaddata == "SpecialRestrictionErrorDetails") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/specialrestriction/details/");
  	$("#payload").val('');
  	
  } else if (loaddata == "SpecialRestrictionRemoveItem") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/specialrestriction/removeitems/");
  	$("#payload").val('');
  	
  } else if (loaddata == "GetPaymentMethods") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/getall/");
  	$("#payload").val("");
  	  	
  } else if (loaddata == "SetPaymentMethods") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/set/");
  	$("#payload").val('{ "paymentMethodId" : "2148933362", "billingRef" : "" }');
  	
  } else if (loaddata == "SetPaymentMethodsEx") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/setex/");
  	$("#payload").val('{ "paymentMethodId" : "2148933362", "billingRef" : "" }');
  	
  } else if (loaddata == "AddPaymentMethod") {
  	$("#url").val("/saptest12@freshdirect.com/paymentmethod/add/");
  	$("#payload").val('{"cardExpMonth" : "", "cardExpYear" : "", "cardBrand" : "",  "accountNumber" : "123456700000", "abaRouteNumber" : "221982389", "bankName" : "AMC Bank", "accountNumberVerify" : "123456700000", "bankAccountType" : "C", "accountHolder" : "Sairam","billAddress1" : "2100 Rachel Terrace", "billAddress2" : "", "billApt" : "14", "billCity" : "Pinebrook", "billState" : "NJ", "billZipCode" : "07058", "paymentMethodType" : "EC", "csv":"","billingCtry":"US"}');
  	
  } else if (loaddata == "AddAndSetPaymentMethod") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/addandset/");
  	$("#payload").val('{ "billingRef": "", "cardExpMonth" : "06", "cardExpYear" : "2012", "cardBrand" : "VISA",  "accountNumber" : "4184798082857938", "abaRouteNumber" : "", "bankName" : "AMC Bank", "accountNumberVerify" : "", "bankAccountType" : "", "accountHolder" : "Sairam","billAddress1" : "2100 Rachel Terrace", "billAddress2" : "", "billApt" : "14", "billCity" : "Pinebrook", "billState" : "NJ", "billZipCode" : "07058", "paymentMethodType" : "CC", "csv":"","billingCtry":"US"}');
  	
  } else if (loaddata == "EditPaymentMethod") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/edit/");
  	$("#payload").val('{"paymentMethodId" : "", "cardExpMonth" : "", "cardExpYear" : "", "cardBrand" : "",  "accountNumber" : "123456700000", "abaRouteNumber" : "221982389", "bankName" : "AMC Bank", "accountNumberVerify" : "123456700000", "bankAccountType" : "C", "accountHolder" : "Sairam","billAddress1" : "2100 Rachel Terrace", "billAddress2" : "", "billApt" : "14", "billCity" : "Pinebrook", "billState" : "NJ", "billZipCode" : "07058", "paymentMethodType" : "EC", "csv":"","billingCtry":"US"}');
  	
  } else if (loaddata == "SavePaymentMethod") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/save/");
  	$("#payload").val('{"paymentMethodId" : "", "cardExpMonth" : "", "cardExpYear" : "", "cardBrand" : "",  "accountNumber" : "123456700000", "abaRouteNumber" : "221982389", "bankName" : "AMC Bank", "accountNumberVerify" : "123456700000", "bankAccountType" : "C", "accountHolder" : "Sairam","billAddress1" : "2100 Rachel Terrace", "billAddress2" : "", "billApt" : "14", "billCity" : "Pinebrook", "billState" : "NJ", "billZipCode" : "07058", "paymentMethodType" : "EC", "csv":"","billingCtry":"US"}');
  	  
  } else if (loaddata == "DeletePaymentMethod") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/delete/");
  	$("#payload").val('{ "paymentMethodId" : ""}');
  	
  } else if (loaddata == "AddDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/deliveryaddress/add/");
  	$("#payload").val('{"dlvfirstname" : "David","dlvlastname" : "Saad", "dlvhomephone" : "7189281226","dlvhomephoneext" : "","address1" : "23-30 Borden Ave","address2" : "","apartment" : "","city" : "Long Island","state" : "NY","zipcode" : "11101","country" : "US","deliveryInstructions" : "", "doorman":"false", "dlvServiceType" : "HOME"}');
  	
  } else if (loaddata == "SaveDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/deliveryaddress/save/");
  	$("#payload").val('{"dlvfirstname" : "David","dlvlastname" : "Saad", "dlvhomephone" : "7189281226","dlvhomephoneext" : "","address1" : "23-30 Borden Ave","address2" : "","apartment" : "","city" : "Long Island","state" : "NY","zipcode" : "11101","country" : "US","deliveryInstructions" : "", "doorman":"false", "dlvServiceType" : "HOME"}');
  	
  } else if (loaddata == "AddAndSetDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/addandset/");
  	$("#payload").val('{"dlvfirstname" : "David","dlvlastname" : "Saad", "dlvhomephone" : "7189281226","dlvhomephoneext" : "","address1" : "23-30 Borden Ave","address2" : "","apartment" : "","city" : "Long Island","state" : "NY","zipcode" : "11101","country" : "US","deliveryInstructions" : "","doorman":"false", "dlvServiceType" : "HOME"}');
  	
  } else if (loaddata == "updateUserAccount") {
  	$("#url").val("/user/account/update/");
  	$("#payload").val('{"oldUserName" : "bogus@freshdirect.com","newUserName" : "bogus1@freshdirect.com","oldPassword" : "test","newPassword" : "test1"}');
  	
  } else if (loaddata == "EditDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/deliveryaddress/edit/");
  	$("#payload").val('{"shipToAddressId" : "2148933356", "dlvfirstname" : "David","dlvlastname" : "Chance", "dlvcompanyname" : "ABC Company", "dlvhomephone" : "7189281226","dlvhomephoneext" : "","address1" : "2100 Rachel terrace","address2" : "","apartment" : "4","city" : "Pine brook","state" : "NJ","zipcode" : "07058","country" : "US","deliveryInstructions" : "","dlvServiceType" : "HOME"}');
  	
  } else if (loaddata == "CheckoutEditDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/edit/");
  	$("#payload").val('{"shipToAddressId" : "2148933356", "dlvfirstname" : "David","dlvlastname" : "Chance", "dlvcompanyname" : "ABC Company", "dlvhomephone" : "7189281226","dlvhomephoneext" : "","address1" : "2100 Rachel terrace","address2" : "","apartment" : "4","city" : "Pine brook","state" : "NJ","zipcode" : "07058","country" : "US","deliveryInstructions" : "","dlvServiceType" : "HOME"}');
  	
  } else if (loaddata == "DeleteDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/deliveryaddress/delete/");
  	$("#payload").val('{ "shipToAddressId" : ""}');
  	
  } else if (loaddata == "CheckoutDeleteDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/delete/");
  	$("#payload").val('{ "shipToAddressId" : ""}');
  	
  } else if (loaddata == "OrderDetail") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/orderdetail/");
  	$("#payload").val('');
  	
  } else if (loaddata == "ReviewOrderDetail") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/revieworderdetail/");
  	$("#payload").val('');
  	
  } else if (loaddata == "SubmitOrder") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/ordersubmit/");
  	$("#payload").val('');
  	
  } else if (loaddata == "SubmitOrderEx") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/submitorderex/");
  	$("#payload").val('');
  	
  } else if (loaddata == "GetSelectedDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/getselectedeliverydaddress/");
  	$("#payload").val('');
  	
  } else if (loaddata == "GetPaymentMethodVerifyStatus") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/getpmverifystatus/");
  	$("#payload").val('');
  	
  } else if (loaddata == "VerifyAge") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/verifyage/");
  	$("#payload").val('');
  	  	
  } else if (loaddata == "AcknowledgeHealthWarning") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/acknowledgehealthwarning/");
  	$("#payload").val('');
  	  	
  } else if (loaddata == "RemoveAlcohol") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/removefromcart/");
  	$("#payload").val('');
  	  	
  } else if (loaddata == "Search") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchEX") {
  	$("#url").val("/searchEX/");
	var postdata = '{"query": "coffee", "page" : "1"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchUPC") {
  	$("#url").val("/search/");
	var postdata = '{"upc": "689544080602", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchUPCEX") {
  	$("#url").val("/searchEX/");
	var postdata = '{"upc": "689544080602", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchSort") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "sortBy" : "name"}';
  	$("#payload").val(postdata);
  	
  }  else if (loaddata == "SearchSortEX") {
  	$("#url").val("/searchEX/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "sortBy" : "name"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchFilter") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "category" : "gro_coffe"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchFilterEX") {
  	$("#url").val("/searchEX/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "category" : "gro_coffe"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "ProductDetail") {
  	$("#url").val("/product/catid/grns/id/grns_grnkale");
  	$("#payload").val("");
  	
  } else if (loaddata == "ProductRecommended") {
  	$("#url").val("/product/Recommended/catid/grns/id/grns_grnkale");
  	$("#payload").val("");
  	
  } else if (loaddata == "ProductDetailMoreInfo") {
  	$("#url").val("/product/moreinfo/catid/grns/id/grns_grnkale");
  	$("#payload").val("");
  	
  } else if (loaddata == "GetExistingOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153089071/detail");
  	$("#payload").val('');
  	
  } else if (loaddata == "GetExistingOrders") {
  	$("#url").val("/saptest12@freshdirect.com/order/details/");
  	$("#payload").val('{"orders":["10270258529","10270259198","10270258535","10270258520","10270258541"]}');
  	
  } else if (loaddata == "CancelExistingOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153087389/cancel");
  	$("#payload").val('');
  	
  } else if (loaddata == "GetOrderHistory") {
  	$("#url").val("/saptest12@freshdirect.com/account/orders/");
  	$("#payload").val("");
  	
  } else if (loaddata == "AcceptDeliveryPassTermsAndConditions") {
  	$("#url").val("/saptest12@freshdirect.com/account/dpacceptterms/");
  	$("#payload").val("");
  	
  } else if(loaddata == "SetMobilePreferences") {  	
  	$("#url").val("/saptest12@freshdirect.com/account/setmobilepreferences/");
  	$("#payload").val('{"mobile_number" : "2035594465", "order_notices" : "Y","order_exceptions" : "Y", "offers" : "Y"}');
  } else if(loaddata == "SetEmailPreferences") {  	
  	$("#url").val("/saptest12@freshdirect.com/account/setemailpreferences/");
  	$("#payload").val('{ "email_subscribed" : "Y" }');
  } else if(loaddata == "GetEmailPreferences") {  	
  	$("#url").val("/saptest12@freshdirect.com/account/getemailpreferences/");
  	$("#payload").val('');
  } else if (loaddata == "ModifyOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153089071/modify");
  	$("#payload").val('');
  	
  }  else if (loaddata == "CheckModify") {
  	$("#url").val("/saptest12@freshdirect.com/order/checkmodify");
  	$("#payload").val('{"orders":["10270258529","10270259198","10270258535","10270258520","10270258541"]}');
  	
  } 
  
  else if (loaddata == "CancelOrderModify") {
  	$("#url").val("/saptest12@freshdirect.com/order/cancelmodify/");
  	$("#payload").val("");
  	
  } else if (loaddata == "Logout") {
  	$("#url").val("/saptest12@freshdirect.com/logout/");
  	$("#payload").val("");
  	
  } else if (loaddata == "TransactionSource") {
  	$("#url").val("/saptest12@freshdirect.com/transactionsource/");
  	var postdata = '{"source" : "IPW"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "ForgotPassword") {
  	$("#url").val("/saptest12@freshdirect.com/forgotpassword/");
  	$("#payload").val('{ "username" : "saptest12@freshdirect.com"}');
  	
  } else if (loaddata == "QuickShopOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/id/2153085854/quickshop/");
  	$("#payload").val("");
  	
  } else if (loaddata == "AddItem-MultipleBeef") {
  	$("#url").val("/saptest12@freshdirect.com/cart/addmultiple/");
  	var postdata = '{'+
		'\n	"productsConfiguration" : ['+
		'\n  { '+
		'\n		"product" : { '+
		'\n			"id" : "bstk_rbeye_bnls", '+
		'\n			"categoryId" : "bgril", '+
		'\n			"sku" : {'+
		'\n				"code" : "MEA0004676" '+
		'\n			}'+
		'\n		},'+
		'\n		"quantity" : "2",'+
		'\n		"salesUnit" : "E02",'+
		'\n		"options" : {"C_MT_BF_MAR" : "N" ,  "C_MT_BF_PAK" : "ST" } '+
		'\n	},'+
		'\n  { '+
		'\n		"product" : { '+
		'\n			"id" : "bstk_rbeye_bnls", '+
		'\n			"categoryId" : "bgril", '+
		'\n			"sku" : {'+
		'\n				"code" : "MEA0004675" '+
		'\n			}'+
		'\n		},'+
		'\n		"quantity" : "1",'+
		'\n		"salesUnit" : "E04",'+
		'\n		"options" : {"C_MT_BF_MAR" : "N" ,  "C_MT_BF_PAK" : "VP" } '+
		'\n	}'+
		'\n	]'+
		'\n}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "ShoppingLists") {
  	$("#url").val("/saptest12@freshdirect.com/shoppinglists/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "OrderHistoryQuickshop") {
  	$("#url").val("/saptest12@freshdirect.com/orders/quickshop/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "FdLists") {
  	$("#url").val("/saptest12@freshdirect.com/starterlists/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "QuickShopFdLists") {
  	$("#url").val("/saptest12@freshdirect.com/starterlist/id/list_basics/quickshop/");
  	$("#payload").val("");
  	
  } else if (loaddata == "QuickShopLists") {
  	$("#url").val("/saptest12@freshdirect.com/shoppinglist/id/2153098981/quickshop/");
  	$("#payload").val("");
  	
  } else if (loaddata == "QuickShopGetDeptsForEveryItemOrdered") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/filterdays/none/getdeptsforeveryitem/");
  	$("#payload").val("");
  	
  } else if (loaddata == "QuickShopEveryItemOrderedByDept") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/dept/id/gro/filterdays/none/sortby/name/geteveryitemfordept/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "QuickShopEveryItemEverOrdered") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/geteveryitemeverordered/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "QuickShopEveryItemEverOrderedEX") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/geteveryitemeverorderedEX/");
  	$("#payload").val('{"page" : "1"}');
  	
  } else if (loaddata == "RemoveMultipleItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/removemultipleitems/");
  	var postdata = '{'+
  	'  "ids" : ["-2093500227","545704818"]'+
	'\n}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "ProductGetPrice") {
  	$("#url").val("/product/getprice/catid/crt/id/crt_jumbo");
  	var postdata = '\n	{'+
	'\n	    "product" : {'+
	'\n	        "id" : "crt_jumbo",'+
	'\n	        "categoryId" : "crt",'+
	'\n	        "sku" : {'+
	'\n	            "code" : "VEG0067468" '+
	'\n	        }'+
	'\n	    },'+
	'\n	    "quantity" : "6",'+
	'\n	    "salesUnit" : {'+
	'\n	        "name" : "EA" '+
	'\n	    },'+
	'\n	    "options" : {}'+
	'\n	} ';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SSYouMightAlsoLike") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/youmightalsolike/");
  	$("#payload").val("");
  	
  } else if (loaddata == "ContactUsFormData") {
  	$("#url").val("/saptest12@freshdirect.com/contactus/get/");
  	$("#payload").val("");
  	
  } else if (loaddata == "ContactUsSubmit") {
  	$("#url").val("/saptest12@freshdirect.com/contactus/send/");
  	var postdata = '\n	{'+
	'\n	    "subject" : "1",'+
	'\n	    "orderId" : "5348377355",'+
	'\n	    "message" : "please ignore. this is a test"'+
	'\n	} ';
	$("#payload").val(postdata);
  	
  } else if (loaddata == "SSYourFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/yourfavorite/");
  	$("#payload").val("");
  	
  } else if (loaddata == "SSFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/favorite/");
  	$("#payload").val("");
  	
  } else if (loaddata == "SSFDFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/fdfavorite/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSBestDealCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/bestdealcarousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSCustomerFavoriteCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/customerfavouritecarousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSPeakProduceCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/peakproducecarousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSBestDealMeatCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/bestdealmeatcarousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/carousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "SSDepartmentCarousel") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/departmentcarousel/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "WGDCategories") {
  	$("#url").val("/product/whatsgood/categories/");
  	$("#payload").val("");
  	
  }else if (loaddata == "WGDCategoryProducts") {
  	$("#url").val("/product/whatsgood/category/xxx/");
  	$("#payload").val("");
  	
  }else if (loaddata == "WGDPresidentPicks") {
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#url").val("/product/presidentspick/");
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "WGDButcherBlock") {
  	$("#url").val("/product/butchersblock/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "WGDPeakProduce") {
  	$("#url").val("/product/peakproduce/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "WGDBrandNameDeals") {
  	$("#url").val("/product/brandnamedeals/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "AccountDeliveryInfo") {
  	$("#url").val("/saptest12@freshdirect.com/account/addresses/");
  	$("#payload").val("");
  	
  } else if (loaddata == "AccountDeliveryTimeslotsDefault") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots");
  	$("#payload").val("");
  	
  } else if (loaddata == "AccountDeliveryTimeslots") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/2148933356");
  	$("#payload").val("");
  	
  } else if (loaddata == "AccountCancelTimeslotsReservation") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/cancel");
    var postdata = '{ "addressId" : "2148933356","deliveryTimeslotId": "xxx"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "AccountReservedTimeslots") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/reserve");
    var postdata = '{ "addressId" : "2148933356", "deliveryTimeslotId": "xxx"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "SearchAutocomplete") {
  	$("#url").val("/search/autocomplete/c");
  	$("#payload").val("");
  	
  } else if (loaddata == "EmailCapture") {
  	$("#url").val("/emailcapture/test@test.com");
  	$("#payload").val("");
  	
  } else if (loaddata == "ConfiguredValues") {
  	$("#url").val("/configvalue/param1");
  	$("#payload").val("");
  	
  }  else if (loaddata == "Help") {
  	$("#url").val("/help/");
  	$("#payload").val("");
  	
  }  else if (loaddata == "HelpContactUs") {
  	$("#url").val("/help/contactUs");
  	$("#payload").val("");
  	
  }  else if (loaddata == "HelpLearnMorePromo") {
  	$("#url").val("/help/learnMorePromo");
  	$("#payload").val("");
  	
  }  else if (loaddata == "CustomerAgreement") {
  	$("#url").val("/help/termsOfUse");
  	$("#payload").val("");
  	
  }  else if (loaddata == "smstermsofuse") {
  	$("#url").val("/help/smstermsofuse");
  	$("#payload").val("");
  	
  }  else if (loaddata == "AlcoholRestrictions") {
  	$("#url").val("/help/alcoholrestrictions");
  	$("#payload").val("");
  	
  }  else if (loaddata == "AlcoholAgeVerification") {
  	$("#url").val("/help/alcoholageverification");
  	$("#payload").val("");
  	
  }  else if (loaddata == "BackupDeliveryAuthorization") {
  	$("#url").val("/help/backupdeliveryauthorization");
  	$("#payload").val("");   	
  
  } else if (loaddata == "DeliveryPassTermsAndConditions") {
  	$("#url").val("/help/deliveryPassTermsAndConditions");
  	$("#payload").val("");
  	
  } else if (loaddata == "Ping") {
  	$("#url").val("/saptest12@freshdirect.com/ping/");
  	$("#payload").val("");
  	
  }  else if (loaddata == "LoginRefresh") {
  	$("#url").val("/saptest12@freshdirect.com/login/refresh/");
  	$("#payload").val("");
  	
  } else if (loaddata == "BrowseDepartment") {
  	$("#url").val("/browse/departments/");
	var postdata = '{"page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  }  else if (loaddata == "BrowseCategory") {
  	$("#url").val("/browse/categories/");
	var postdata = '{"department": "cof", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "Browse2") {
  	$("#url").val("/browse2/");
	var postdata = '{"id": "mea"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (  loaddata == "globalNav") {
  	$("#url").val("/browse/navigation/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getAllProductsForCategory") {
  	$("#url").val("/browse/getproducts/"); 
  	var postdata = '{"id": "bfry"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
  } else if (  loaddata == "getAllProductsForCategoryEX") {
  	$("#url").val("/browse/getproductsEX/"); 
  	var postdata = '{"id": "fdx_test", "sortBy" : "PRICE"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
  } else if (  loaddata == "getCatalog") {
  	$("#url").val("/browse/getCatalogForAddress/"); 
  	var postdata='{ "zipCode" : "10036", "serviceType" : "HOME", "address1" : "44 W 44TH ST", "apartment" : "", "city" : "New York", "state" : "NY", "productCount":"10"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
 } else if (  loaddata == "getAllCatalogKeys") {
  	$("#url").val("/browse/getAllCatalogKeys/"); 
  	var postdata='';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
 } else if (  loaddata == "getCatalogForKey") {
  	$("#url").val("/browse/getCatalogForKey/"); 
  	var postdata='{ "key": "FDX-1000-0001-01-0000200501", "productCount":"10"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
 } else if (  loaddata == "getCatalogId") {
  	$("#url").val("/browse/getCatalogIdForAddress/"); 
  	var postdata='{ "zipCode" : "10036", "serviceType" : "HOME", "address1" : "44 W 44TH ST", "apartment" : "", "city" : "New York", "state" : "NY"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
 } else if (  loaddata == "getCatalogKey") {
  	$("#url").val("/browse/getCatalogKeyForAddress/"); 
  	var postdata='{ "zipCode" : "10036", "serviceType" : "HOME", "address1" : "44 W 44TH ST", "apartment" : "", "city" : "New York", "state" : "NY"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
 }else if (loaddata == "BrowseCategoryContent") {
  	$("#url").val("/browse/categorycontent/");
	var postdata = '{"category": "cof_espres", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "BrowseCategoryContentProductOnly") {
  	$("#url").val("/browse/categorycontentproductonly/");
	var postdata = '{"category": "cof_espres", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "BrowseGroupContents") {
  	$("#url").val("/browse/groupproducts/");
	var postdata = '{"groupId": "FD_PIZZA-1", "groupVersion" : "10460"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "BrowseCouponDepartment") {
  	$("#url").val("/coupon/browse/departments/");
	var postdata = '{"page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  }  else if (loaddata == "BrowseCouponCategory") {
  	$("#url").val("/coupon/browse/categories/");
	var postdata = '{"department": "gro", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "BrowseCouponCategoryContent") {
  	$("#url").val("/coupon/browse/categorycontent/");
	var postdata = '{"category": "hba_deo", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "BrowseCouponCategoryContentProductOnly") {
  	$("#url").val("/coupon/browse/categorycontentproductonly/");
	var postdata = '{"category": "picks_gltnfr_condi", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	
  }else if (  loaddata == "GetNewProducts") {
  	$("#url").val("/product/getnewproducts/"); 
  	$("#payload").val("");
  } else if (  loaddata == "getSortOptionsForCat") {
  	$("#url").val("/browse/sortoptionsforcategory/"); 
  	var postdata = '{"id": "fdx_test"}';
  	$("#payload").val(postdata);
  	$("#result").val("");  	
  	
  } else if (loaddata == "IvrEmail") {
  	$("#url").val("/ext/t001");
  	$("#payload").val("2202928245,applicationdevelopment@freshdirect.com,qa@freshdirect.com");
  	
  } else if (loaddata == "IvrCallLog") {
  	$("#url").val("/ext/t002/");
	var postdata = '1,9174068937,11540278667,10/04/2012 06:05:00,2,CallComplete,51,571-730-5796,delivery access';
  	$("#payload").val(postdata);
  	
  }else if (loaddata == "smsMessageRelay") {
  	$("#url").val("/ext/t003/");
	var postdata = '3472634065,45444,sprint,2014-08-11 22:12:44,external interface test';
  	$("#payload").val(postdata);
  	
  }else if (loaddata == "fdxsmsMessageRelay") {
  	$("#url").val("/ext/t004/");
	var postdata = '3472634065,45444,sprint,2014-08-11 22:12:44,external interface test';
  	$("#payload").val(postdata);
  	
  } else if (loaddata == "GetCountries") {
  	$("#url").val("/lookup/countries/");  
  	$("#payload").val("");	
  	
  }  else if (loaddata == "GetRegions") {
  	$("#url").val("/lookup/regions/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getDetail") {
  	$("#url").val("/recipe/getdetail/QLmRGXthw"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getAll") {
  	$("#url").val("/recipe/getall/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getTags") {
  	$("#url").val("/recipe/gettags/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "foodilySearch") {
  	$("#url").val("/recipe/search/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getAllHome") {
  	$("#url").val("/home/getall/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "featuredCategories") {
  	$("#url").val("/home/featuredcategories/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "getHomeAndCategories") {
  	$("#url").val("/home/all/"); 
  	$("#payload").val(""); 	
  	
  }  else if (  loaddata == "socialrecognize") {
  	$("#url").val("/social/recognize/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "socialmerge") {
  	$("#url").val("/social/merge/"); 
  	$("#payload").val(""); 	
  	
  } else if (  loaddata == "socialregister") {
  	$("#url").val("/social/register/"); 
  	$("#payload").val(""); 	
  	
  } else if ( loaddata == "getPage"){
  	$("#url").val("/home/getPage/");
  	var postData = '{"pageType": "Feed", "requestedDate" : "2015-07-16T12:12:00.000-04:00"}';
  	$("#payload").val(postData);
  }  else if (  loaddata == "sociallogin") {
  	$("#url").val("/social/login/"); 
  	$("#payload").val('{"userToken":"12345","provider":"google"}'); 	
  	
  } else if (  loaddata == "linkaccount") {
  	$("#url").val("/social/linkaccount/"); 
  	$("#payload").val('{"email" : "fd_1@yahoo.com", "password" : "test","existingToken":"123","newToken":"456","provider":"google"}'); 	
  	
  } else if (  loaddata == "registersocial") {
  	$("#url").val("/saptest12@freshdirect.com/registersocial/"); 
  	$("#payload").val('{"email" : "test978@gmail.com","securityQuestion" : "test", "firstName" : "Door3", "lastName" : "Dev", "serviceType" : "HOME", "address1" : "500 W 43 st", "apartment" : "8c", "city" : "New York", "state" : "NY", "zipCode" : "10036" , "mobile_number": "2035594466", "recieveSMSAlerts" : "true","userToken" : "123", "provider" : "test"}'); 	
  	
  }  else if (  loaddata == "unlinkaccount") {
  	$("#url").val("/social/unlinkaccount/"); 
  	$("#payload").val('{"email" : "test@yahoo.com", "userToken":"123"}'); 	
  	
  }  else if (  loaddata == "socialConnect") {
  	$("#url").val("/social/socialConnect/"); 
  	$("#payload").val('{"userToken" : "12345", "provider":"google"}'); 	
  	
  }
  $("#result").JSONView({});  	
  $("#payload").focus();
}

function doStuff() {
  var version = $("#version").val();
  $.cookie("version", version);
  var strURL = "/mobileapi/v/" + version + $("#url").val();
  var postData = "";
  var payload = $("#payload").val();
  var dType = "html";
  if($("#loaddata").val() == "IvrEmail") {
  	 var temp = payload.split(",");
  	 if(temp != null && temp.length > 0) {
  	 	postData = postData + "data=" + $.URLEncode(temp[0]);
  	 	if(temp.length > 1) {
  	 		postData = postData + "&cc=" + $.URLEncode(temp[1]);
  	 	}
  	 	if(temp.length > 2) {
  	 		postData = postData + "&bcc=" + $.URLEncode(temp[2]);
  	 	}
  	 } 
  } else if($("#loaddata").val() == "IvrCallLog") {
  	 var temp = payload.split(",");
  	 if(temp != null && temp.length > 0) {
  	 	postData = postData + "data=" + $.URLEncode(temp[0]);
  	 	if(temp.length > 1) {
  	 		postData = postData + "&callerId=" + $.URLEncode(temp[1]);
  	 	}
		if(temp.length > 2) {
  	 		postData = postData + "&orderNumber=" + $.URLEncode(temp[2]);
  	 	}
  	 	if(temp.length > 3) {
  	 		postData = postData + "&callStartTime=" + $.URLEncode(temp[3]);
  	 	}
		if(temp.length > 4) {
  	 		postData = postData + "&callDuration=" + $.URLEncode(temp[4]);
  	 	}
		if(temp.length > 5) {
  	 		postData = postData + "&callOutcome=" + $.URLEncode(temp[5]);
  	 	}
		if(temp.length > 6) {
  	 		postData = postData + "&talkTime=" + $.URLEncode(temp[6]);
  	 	}
		if(temp.length > 7) {
  	 		postData = postData + "&phoneNumber=" + $.URLEncode(temp[7]);
  	 	}
		if(temp.length > 8) {
  	 		postData = postData + "&menuOption=" + $.URLEncode(temp[8]);
  	 	}
  	 } 
  }else if($("#loaddata").val() == "smsMessageRelay"){
  		var temp = payload.split(",");
  	 if(temp != null && temp.length > 0) {
  	 	postData = postData + "sender=" + $.URLEncode(temp[0]);
  	 	if(temp.length > 1) {
  	 		postData = postData + "&code=" + $.URLEncode(temp[1]);
  	 	}
		if(temp.length > 2) {
  	 		postData = postData + "&carrier=" + $.URLEncode(temp[2]);
  	 	}
		if(temp.length > 3) {
  	 		postData = postData + "&received=" + $.URLEncode(temp[3]);
  	 	}
		if(temp.length > 4) {
  	 		postData = postData + "&text=" + $.URLEncode(temp[4]);
  	 	}
  	 } 
  	}else if($("#loaddata").val() == "fdxsmsMessageRelay"){
  		var temp = payload.split(",");
  	 if(temp != null && temp.length > 0) {
  	 	postData = postData + "sender=" + $.URLEncode(temp[0]);
  	 	if(temp.length > 1) {
  	 		postData = postData + "&code=" + $.URLEncode(temp[1]);
  	 	}
		if(temp.length > 2) {
  	 		postData = postData + "&carrier=" + $.URLEncode(temp[2]);
  	 	}
		if(temp.length > 3) {
  	 		postData = postData + "&received=" + $.URLEncode(temp[3]);
  	 	}
		if(temp.length > 4) {
  	 		postData = postData + "&text=" + $.URLEncode(temp[4]);
  	 	}
  	 }
		
  }   else {
	  postData = "data=" + $.URLEncode(payload);
  }
  
   $.ajax({
	      type: "POST",
	      url: strURL,
	      data: postData,
	      dataType: dType,
	      success: function(msg) {
	         $("#result").JSONView(msg, { collapsed: true, nl2br: true, recursive_collapser: true });
	      }
	});	
}	
</script>
</head>
<body>
  <p><b>URL:</b> <input id="url" name="url" type="text" size="65" /> <b>Ver:</b> <input id="version" name="version" type="text" size="2" />
  <b>- or -</b>
  <select name="loaddata" id="loaddata" onchange='JavaScript:loadStuff()'>
  <option value="">========== ZIP CHECK ==========</option>
  <option value="CheckByZip">CheckByZip</option>
  <option value="globalalerts">OAS Alerts</option>
  <option value="CheckByAddress">CheckByAddress</option>
  <option value="CheckByAddressEX">CheckByAddressEX</option>
  <option value="">========== SIGN UP ==========</option>
  <option value="Signup">Signup</option> 
  <option value="SignupFDX">Signup EX</option>  
  <option value="">========== LOGIN ==========</option>
  <option value="Login">Login</option>
  <option value="Logout">Logout</option>
  <option value="Ping">Ping</option>
  <option value="Session">Session</option>  
  <option value="AddAnonymousAddress">Add Anonymous Address</option>  
  <option value="LoginRefresh">Login Refresh</option>
  <option value="ForgotPassword">Forgot Password</option>
  <option value="TransactionSource">Transaction Source</option>
  <option value=""> ========== CART ========== </option>  
  <option value="AddPromo">CART - Apply Promo</option>
  <option value="AddCode">CART - Apply Code</option>
  <option value="RemovePromo">CART - Remove Promo</option>
  <option value="AddItem-SimpleBeef">CART - Add - Simple Beef</option>
  <option value="AddItem-SimpleWine">CART - Add - Simple Wine</option>
  <option value="AddItem-MultipleBeef">CART - Add Multiple Beefs</option>
  <option value="UpdateItem-SimpleBeef">CART - Update - Simple Beef</option>
  <option value="RemoveItem">CART - Remove</option>
  <option value="RemoveAllItems">CART - Remove All</option>
  <option value="RemoveMultipleItems">CART - Remove Multiple</option>
  <option value="ViewItems">CART - View Items</option>
  <option value="ViewItem">CART - View Cartline</option>
  <option value="ClipCoupon">CART - Clip Coupon</option>
  <option value="SetTip">CART - Set Tip</option>
  <option value=""> ========== CHECKOUT ========== </option>
  <option value="InitCheckout">CHECKOUT - Init</option>
  <option value="CheckoutAuthenticate">CHECKOUT - Auth</option>
  <option value="GetDeliveryAddresses">CHECKOUT - Get Delivery Addresses</option>  
  <option value="SetDeliveryAddress">CHECKOUT - Set Delivery Address</option>
  <option value="SetDeliveryAddressEx">CHECKOUT - Set Delivery Address Ex</option>
  <option value="AddAndSetDeliveryAddress">CHECKOUT - Add and Set Delivery Address</option>
  <option value="CheckoutEditDeliveryAddress">CHECKOUT - Edit Delivery Address</option>
  <option value="CheckoutDeleteDeliveryAddress">CHECKOUT - Delete Delivery Address</option>
  <option value="VerifyAge">CHECKOUT - Verify Age</option>
  <option value="RemoveAlcohol">CHECKOUT - Remove All Alcohol</option>
  <option value="ReserveDeliverySlot">CHECKOUT - Set Delivery Slot</option>
  <option value="ReserveDeliverySlotEx">CHECKOUT - Set Delivery Slot EX</option>
  <option value="ATPErrorDetails">CHECKOUT - Get ATP Error Details</option>
  <option value="ATPRemove">CHECKOUT - Remove all unavailable items</option>
   <option value="SpecialRestrictionErrorDetails">CHECKOUT - Get Special Restriction Error Details[EBT]</option>
  <option value="SpecialRestrictionRemoveItem">CHECKOUT - Remove all Special Restriction items[EBT]</option>
  <option value="GetPaymentMethods">CHECKOUT - Get Payment Methods</option>
  <option value="SetPaymentMethods">CHECKOUT - Set Payment Methods</option>
  <option value="SetPaymentMethodsEx">CHECKOUT - Set Payment Methods EX</option>
  <option value="AddAndSetPaymentMethod">CHECKOUT - Add and Set Payment Method</option>
  <option value="EditPaymentMethod">CHECKOUT - Edit Payment Method</option>
  <option value="SavePaymentMethod">CHECKOUT - Save Payment Method</option>
  <option value="DeletePaymentMethod">CHECKOUT - Delete Payment Method</option>
  <option value="OrderDetail">CHECKOUT - Order Detail</option>
  <option value="ReviewOrderDetail">CHECKOUT - Review Order Detail</option>
  <option value="SubmitOrder">CHECKOUT - Submit Order</option>
  <option value="SubmitOrderEx">CHECKOUT - Submit Order FDX</option>
  <option value="GetSelectedDeliveryAddress">CHECKOUT - Get Selected Delivery Address</option>
  <option value="GetPaymentMethodVerifyStatus">CHECKOUT - Get Payment Method Verificaton Status(CVV)</option>
  <option value="AcceptDeliveryPassTermsAndConditionsReturnTimeslots">CHECKOUT - Accept DeliveryPass TermsAndConditions</option>
  <option value=""> ========== SEARCH ========== </option>
  <option value="Search">SEARCH - Basic</option>
  <option value="SearchUPC">SEARCH - UPC barcode</option>
  <option value="SearchSort">SEARCH - Sort</option>
  <option value="SearchFilter">SEARCH - Filter</option>
  <option value="SearchEX">SEARCHEX - Basic</option>
  <option value="SearchUPCEX">SEARCHEX - UPC barcode</option>
  <option value="SearchSortEX">SEARCHEX - Sort</option>
  <option value="SearchFilterEX">SEARCHEX - Filter</option>
  <option value="SearchAutocomplete">SEARCH - Autocomplete</option>
  <option value=""> ========== BROWSE ========== </option>
  <option value="BrowseDepartment">BROWSE - DEPARTMENT</option>
  <option value="BrowseCategory">BROWSE - CATEGORY</option>
  <option value="BrowseCategoryContent">BROWSE - CATEGORYCONTENT</option>
  <option value="BrowseCategoryContentProductOnly">BROWSE - CATEGORYCONTENT(Product Only)</option>
  <option value="BrowseGroupContents">BROWSE - GROUP CONTENTS</option> 
  <option value="getAllProductsForCategory">BROWSE - ALL PRODUCTS FOR CATEGORY</option>
  <option value="getAllProductsForCategoryEX">BROWSE - ALL PRODUCTS FOR CATEGORY WITH SORT </option>
  <option value="getCatalog">BROWSE - GET CATALOG FOR ADDRESS</option>
  <option value="getAllCatalogKeys">BROWSE - GET ALL CATALOG KEYS</option>
  <option value="getCatalogId">BROWSE - GET CATALOG KEY FOR ADDRESS</option>
  <option value="getCatalogKey">BROWSE - GET CATALOG KEY FOR ADDRESS EX</option>
  <option value="getCatalogForKey">BROWSE - GET CATALOG FOR CATALOG KEY</option>
    <option value="globalNav">BROWSE - NAVIGATION</option>
  <option value="getSortOptionsForCat">BROWSE - GET SORT OPTIONS FOR CATEOGRY</option>
  <option value=""> ========== BROWSE COUPON ========== </option>
  <option value="BrowseCouponDepartment">BROWSE COUPON - DEPARTMENT</option>
  <option value="BrowseCouponCategory">BROWSE COUPON - CATEGORY</option>
  <option value="BrowseCouponCategoryContent">BROWSE COUPON - CATEGORYCONTENT</option>
  <option value="BrowseCouponCategoryContentProductOnly">BROWSE COUPON - CATEGORYCONTENT(Product Only)</option>
  
  <option value=""> ========== New Products ========== </option>
  <option value="GetNewProducts">NEW PRODUCTS - GET ALL NEW PRODUCTS</option>  
  
  <option value=""> ========== PRODUCT ========== </option>
  <option value="ProductDetail">PRODUCT - Product Detail</option>
  <option value="ProductRecommended">PRODUCT - Product Recommended</option>
  <option value="ProductDetailMoreInfo">PRODUCT - More Info</option>
  <option value="ProductGetPrice">PRODUCT - Pricing API</option>
  <option value="AcknowledgeHealthWarning">PRODUCT - Ack Health Warning</option>
  <option value=""> ========== ORDERS ========== </option>
  <option value="GetOrderHistory">ORDERS - Order History</option>
  <option value="GetExistingOrder">ORDERS - Existing Order Detail</option>
  <option value="GetExistingOrders">ORDERS - List Of Existing Orders Detail</option>
  <option value="CancelExistingOrder">ORDERS - Cancel Existing Order</option>
  <option value="ModifyOrder">ORDERS - Modify Order</option>
  <option value="CheckModify">ORDERS - Check Modify</option>
  
  <option value="CancelOrderModify">ORDERS - Cancel Order Modify</option>
  <option value=""> ========== QUICKSHOP LISTS/ORDERS ========== </option>
  <option value="OrderHistoryQuickshop">QUICKSHOP - Previous Orders</option>
  <option value="QuickShopOrder">QUICKSHOP - Shop from Order</option>
  <option value="ShoppingLists">QUICKSHOP - Shopping Lists</option>
  <option value="FdLists">QUICKSHOP - Fd Lists</option>
  <option value="QuickShopFdLists">QUICKSHOP - Shop from Fd Lists</option>
  <option value="QuickShopLists">QUICKSHOP - Shop from Shopping List</option>
  <option value="QuickShopGetDeptsForEveryItemOrdered">QUICKSHOP - Get Department for Everything You've Ever Ordered!</option>
  <option value="QuickShopEveryItemOrderedByDept">QUICKSHOP - Everything You've Ever Ordered By Dept !</option>
  <option value="QuickShopEveryItemEverOrdered">QUICKSHOP - Everything You've Ever Ordered!</option>
  <option value="QuickShopEveryItemEverOrderedEX">QUICKSHOP - Everything You've Ever Ordered! EX</option>	
  <option value=""> ========== WHATS GOOD ========== </option>
  <option value="WGDCategories">WHATS GOOD - Categories</option>
  <option value="WGDCategoryProducts">WHATS GOOD - Category Products</option>
  <option value=""> ========== SMARTSTORE ========== </option>
  <option value="SSYouMightAlsoLike">SMARTSTORE - YouMightAlsoLike</option>
  <option value="SSYourFavorite">SMARTSTORE - YourFavorite</option>
  <option value="SSFavorite">SMARTSTORE - Favorite (either FD or Yours)</option>
  <option value="SSFDFavorite">SMARTSTORE - FreshDirect Favorite</option>
 <!-- <option value="SSCustomerFavoriteCarousel">SMARTSTORE - CustomerFavorite Carousel</option>
  <option value="SSBestDealCarousel">SMARTSTORE - BestDeal Carousel</option>
  <option value="SSPeakProduceCarousel">SMARTSTORE - Peak Produce Carousel</option>
  <option value="SSBestDealMeatCarousel">SMARTSTORE - BestDeal Meat Carousel</option>  -->
  <option value="SSCarousel">SMARTSTORE - Carousel</option>  
  <option value="SSDepartmentCarousel">SMARTSTORE - Department Carousel</option>  
  <option value=""> ========== CONTACT US ========== </option>
  <option value="ContactUsFormData">CONTACT US - Init</option>
  <option value="ContactUsSubmit">CONTACT US - Submit</option>
  <option value=""> ========== ACCOUNT ========== </option>
  <option value="updateUserAccount">ACCOUNT - Update User Account-email and/or password</option>
  <option value="AccountDeliveryInfo">ACCOUNT - Get Addresses</option>
  <option value="AccountDeliveryInfo">ACCOUNT - Get AddressesEX</option>
  <option value="AccountDeliveryTimeslotsDefault">ACCOUNT - Get Delivery Timeslots DEFAULT</option>
  <option value="AccountDeliveryTimeslots">ACCOUNT - Get Delivery Timeslots</option>
  <option value="AccountCancelTimeslotsReservation">ACCOUNT - Cancel Timeslot Reservation</option>
  <option value="AccountReservedTimeslots">ACCOUNT - Reserve Delivery Timeslots</option>
  <option value="AddDeliveryAddress">ACCOUNT - Add Delivery Address</option>
  <option value="EditDeliveryAddress">ACCOUNT - Edit Delivery Address</option>
  <option value="SaveDeliveryAddress">ACCOUNT - Save Delivery Address</option>
  <option value="DeleteDeliveryAddress">ACCOUNT - Delete Delivery Address</option>  
  <option value="AddPaymentMethod">ACCOUNT - Add Payment Method</option>
  <option value="EditPaymentMethod">ACCOUNT - Edit Payment Method</option>
  <option value="DeletePaymentMethod">ACCOUNT - Delete Payment Method</option>
  <option value="AcceptDeliveryPassTermsAndConditions">ACCOUNT - Accept DeliveryPass TermsAndConditions</option>  
  <option value="SetMobilePreferences">ACCOUNT - Set Mobile Preferences</option>
  <option value="SetEmailPreferences">ACCOUNT - Set Email Preferences</option>
  <option value="GetEmailPreferences">ACCOUNT - Get Email Preferences</option>
  <option value=""> ========== MISC ========== </option>
  <option value="EmailCapture">Email Capture</option>
  <option value="ConfiguredValues">Configured Values</option>
  <option value=""> ========== HELP ========== </option>
  <option value="Help">Help</option>
  <option value="HelpContactUs">Help - Contact Us</option>
  <option value="HelpLearnMorePromo">Help - Learn More Promo</option>
  <option value="CustomerAgreement">Help - Customer Agreement</option>
  <option value="smstermsofuse">Help - SMS Terms of Use</option>
  <option value="AlcoholRestrictions">Help - Learn More About Alcohol Restrictions</option>
  <option value="AlcoholAgeVerification">Help - Alcohol Age Verification</option>    
  <option value="BackupDeliveryAuthorization">Help - Backup Delivery Authorization</option>
  <option value="DeliveryPassTermsAndConditions">Help - DeliveryPass TermsAndConditions</option>
  <option value=""> ========== External Interface ========== </option>
  <option value="IvrEmail">Send - IVR Delivery Email</option>
  <option value="IvrCallLog">Lookup - IVR Delivery Call Log</option>
  <option value="smsMessageRelay">Send - sms Alert</option>
  <option value="fdxsmsMessageRelay">Send - fdx sms Alert</option>
  
  <option value=""> ========== Lookup ========== </option>
  <option value="GetCountries">Lookup - Get Countries</option>
  <option value="GetRegions">Lookup - Get Regions</option>
  <option value=""> ========== Foodily ========== </option>
  <option value="getDetail">Foodily - Get Recipe Details</option>
  <option value="getTags">Foodily - Get Tags</option>
  <option value="getAll">Foodily - Get All Recipes</option>
  <option value="foodilySearch">Foodily - search</option>
  <option value=""> ========== Home ========== </option>
  <option value="getAllHome"> Home - Get All</option>
  <option value="featuredCategories"> Home - Featured Categories </option>
  <option value="getHomeAndCategories"> Home - Get Home And Categories </option>
  <option value="getPage"> Home - Get Page </option>
  <option value=""> ========== Social Login ========== </option>
  <option value="socialrecognize"> Social - Recognize</option>
  <option value="socialmerge"> Social - Merge </option>
  <option value="socialregister"> Social - Register </option>
  <option value="sociallogin"> Social - Login</option>
  <option value="linkaccount"> Social - Link Account </option>
  <option value="registersocial"> Social - Register </option>
  <option value="unlinkaccount"> Social - Unlink Account </option>
  <option value="socialConnect"> Social - Connect </option>
 
  </select>
  
  <input value="Go" type="button" onclick='javascript:doStuff()'>
  <p/><b>Payload:</b><br/>
  <textarea id="payload" name="payload" rows="5" cols="120"></textarea><p/>
  <p/><b>Result:</b>
  <button id="collapse-btn">Collapse</button>
  <button id="expand-btn">Expand</button>
  <button id="toggle-btn">Toggle</button>
  <button id="toggle-level1-btn">Toggle level1</button>
  <button id="toggle-level2-btn">Toggle level2</button>
  <div  style="border: solid 2px #ff0000;width: 1000px;height: 500px;overflow: scroll;" id="result"></div>
</body>
</html>