<html>
<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>
<script type="text/javascript" src="../js/urlEncode.js"></script>
<script type="text/javascript" src="../js/jquery.cookie.js"></script>


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
  
  
});


function loadStuff() {
  var loaddata = $("#loaddata").val();
  if(loaddata == "Login") {  	
  	$("#url").val("/saptest12@freshdirect.com/login/");
  	$("#payload").val('{ "username" : "saptest12@freshdirect.com", "password" : "test" }');
  	$("#result").val("");
  } else if (loaddata == "AddPromo") {
  	$("#url").val("/saptest12@freshdirect.com/cart/promo/apply/TEST_P0002");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "RemovePromo") {
  	$("#url").val("/saptest12@freshdirect.com/cart/promo/remove/TEST_P0002");
  	$("#payload").val('');
  	$("#result").val("");
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
  	$("#result").val("");
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
  	$("#result").val("");
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
  	$("#result").val("");
  } else if (loaddata == "RemoveItem") {
  	$("#url").val("/saptest12@freshdirect.com/cart/remove/621581041");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "RemoveAllItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/removeallitems/");
  	$("#payload").val('');
  	$("#result").val("");  	
  } else if (loaddata == "ViewItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/getdetail/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "InitCheckout") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/init/");
    $("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "CheckoutAuthenticate") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/authenticate/");
    $("#payload").val('{ "username" : "saptest12@freshdirect.com", "password" : "test" }');
  	$("#result").val("");
  } else if (loaddata == "GetDeliveryAddresses") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/getall/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "SetDeliveryAddress") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliveryaddresses/set/");
  	$("#payload").val('{ "id" : "2150625068", "type" : "RESIDENTIAL" }');
  	$("#result").val("");
  } else if (loaddata == "ReserveDeliverySlot") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/deliverytimeslot/reserve/2150625068/");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "ATPErrorDetails") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/atp/error/");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "ATPRemove") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/atp/removeitems/");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "GetPaymentMethods") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/getall/");
  	$("#payload").val("");
  	$("#result").val("");  	
  } else if (loaddata == "SetPaymentMethods") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/paymentmethod/set/");
  	$("#payload").val('{ "paymentMethodId" : "2148933362", "billingRef" : "" }');
  	$("#result").val("");
  } else if (loaddata == "OrderDetail") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/orderdetail/");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "SubmitOrder") {
  	$("#url").val("/saptest12@freshdirect.com/checkout/ordersubmit/");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "VerifyAge") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/verifyage/");
  	$("#payload").val('');
  	$("#result").val("");  	
  } else if (loaddata == "AcknowledgeHealthWarning") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/acknowledgehealthwarning/");
  	$("#payload").val('');
  	$("#result").val("");  	
  } else if (loaddata == "RemoveAlcohol") {  	
  	$("#url").val("/saptest12@freshdirect.com/alcohol/removefromcart/");
  	$("#payload").val('');
  	$("#result").val("");  	
  } else if (loaddata == "Search") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "SearchUPC") {
  	$("#url").val("/search/");
	var postdata = '{"upc": "689544080602", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "SearchSort") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "sortBy" : "name"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "SearchFilter") {
  	$("#url").val("/search/");
	var postdata = '{"query": "coffee", "page" : "1", "max" : "25", "category" : "gro_coffe"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "ProductDetail") {
  	$("#url").val("/product/catid/grns/id/grns_grnkale");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ProductDetailMoreInfo") {
  	$("#url").val("/product/moreinfo/catid/grns/id/grns_grnkale");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "GetExistingOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153089071/detail");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "CancelExistingOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153087389/cancel");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "GetOrderHistory") {
  	$("#url").val("/saptest12@freshdirect.com/account/orders/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ModifyOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/2153089071/modify");
  	$("#payload").val('');
  	$("#result").val("");
  } else if (loaddata == "CancelOrderModify") {
  	$("#url").val("/saptest12@freshdirect.com/order/cancelmodify/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "Logout") {
  	$("#url").val("/saptest12@freshdirect.com/logout/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ForgotPassword") {
  	$("#url").val("/saptest12@freshdirect.com/forgotpassword/");
  	$("#payload").val('{ "username" : "saptest12@freshdirect.com"}');
  	$("#result").val("");
  } else if (loaddata == "QuickShopOrder") {
  	$("#url").val("/saptest12@freshdirect.com/order/id/2153085854/quickshop/");
  	$("#payload").val("");
  	$("#result").val("");
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
		'\n		"options" : { "C_MT_BF_PAK" : "ST" } '+
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
		'\n		"options" : { "C_MT_BF_PAK" : "VP" } '+
		'\n	}'+
		'\n	]'+
		'\n}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "ShoppingLists") {
  	$("#url").val("/saptest12@freshdirect.com/shoppinglists/");
  	$("#payload").val('{"page" : "1"}');
  	$("#result").val("");
  } else if (loaddata == "OrderHistoryQuickshop") {
  	$("#url").val("/saptest12@freshdirect.com/orders/quickshop/");
  	$("#payload").val('{"page" : "1"}');
  	$("#result").val("");
  } else if (loaddata == "QuickShopLists") {
  	$("#url").val("/saptest12@freshdirect.com/shoppinglist/id/2153098981/quickshop/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "QuickShopEveryItemOrderedDept") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/filterdays/none/getdeptsforeveryitem/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "QuickShopEveryItemOrdered") {
  	$("#url").val("/saptest12@freshdirect.com/quickshop/dept/id/gro/filterdays/none/sortby/name/geteveryitemfordept/");
  	$("#payload").val('{"page" : "1"}');
  	$("#result").val("");
  } else if (loaddata == "RemoveMultipleItems") {
  	$("#url").val("/saptest12@freshdirect.com/cart/removemultipleitems/");
  	var postdata = '{'+
  	'  "ids" : ["-2093500227","545704818"]'+
	'\n}';
  	$("#payload").val(postdata);
  	$("#result").val("");
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
  	$("#result").val("");
  } else if (loaddata == "SSYouMightAlsoLike") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/youmightalsolike/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ContactUsFormData") {
  	$("#url").val("/saptest12@freshdirect.com/contactus/get/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ContactUsSubmit") {
  	$("#url").val("/saptest12@freshdirect.com/contactus/send/");
  	var postdata = '\n	{'+
	'\n	    "subject" : "1",'+
	'\n	    "orderId" : "5348377355",'+
	'\n	    "message" : "please ignore. this is a test"'+
	'\n	} ';
	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "SSYourFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/yourfavorite/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "SSFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/favorite/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "SSFDFavorite") {
  	$("#url").val("/saptest12@freshdirect.com/smartstore/fdfavorite/");
  	$("#payload").val("");
  	$("#result").val("");
  }else if (loaddata == "WGDCategories") {
  	$("#url").val("/product/whatsgood/categories/");
  	$("#payload").val("");
  	$("#result").val("");
  }else if (loaddata == "WGDCategoryProducts") {
  	$("#url").val("/product/whatsgood/category/xxx/");
  	$("#payload").val("");
  	$("#result").val("");
  }else if (loaddata == "WGDPresidentPicks") {
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#url").val("/product/presidentspick/");
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "WGDButcherBlock") {
  	$("#url").val("/product/butchersblock/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "WGDPeakProduce") {
  	$("#url").val("/product/peakproduce/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "WGDBrandNameDeals") {
  	$("#url").val("/product/brandnamedeals/");
    var postdata = '{"query": "", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "AccountDeliveryInfo") {
  	$("#url").val("/saptest12@freshdirect.com/account/addresses/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "AccountDeliveryTimeslotsDefault") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "AccountDeliveryTimeslots") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/2148933356");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "AccountCancelTimeslotsReservation") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/cancel");
    var postdata = '{ "addressId" : "2148933356","deliveryTimeslotId": "xxx"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "AccountReservedTimeslots") {
  	$("#url").val("/saptest12@freshdirect.com/account/timeslots/reserve");
    var postdata = '{ "addressId" : "2148933356", "deliveryTimeslotId": "xxx"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  } else if (loaddata == "SearchAutocomplete") {
  	$("#url").val("/search/autocomplete/c");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "EmailCapture") {
  	$("#url").val("/emailcapture/test@test.com");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "ConfiguredValues") {
  	$("#url").val("/configvalue/param1");
  	$("#payload").val("");
  	$("#result").val("");
  }  else if (loaddata == "Help") {
  	$("#url").val("/help/");
  	$("#payload").val("");
  	$("#result").val("");
  }  else if (loaddata == "HelpContactUs") {
  	$("#url").val("/help/contactUs");
  	$("#payload").val("");
  	$("#result").val("");
  }  else if (loaddata == "HelpLearnMorePromo") {
  	$("#url").val("/help/learnMorePromo");
  	$("#payload").val("");
  	$("#result").val("");
  }  else if (loaddata == "Ping") {
  	$("#url").val("/saptest12@freshdirect.com/ping/");
  	$("#payload").val("");
  	$("#result").val("");
  }  else if (loaddata == "LoginRefresh") {
  	$("#url").val("/saptest12@freshdirect.com/login/refresh/");
  	$("#payload").val("");
  	$("#result").val("");
  } else if (loaddata == "BrowseDepartment") {
  	$("#url").val("/browse/departments/");
	var postdata = '{"page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  }  else if (loaddata == "BrowseCategory") {
  	$("#url").val("/browse/categories/");
	var postdata = '{"department": "cof", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  }   else if (loaddata == "BrowseCategoryContent") {
  	$("#url").val("/browse/categorycontent/");
	var postdata = '{"category": "cof_espres", "page" : "1", "max" : "25"}';
  	$("#payload").val(postdata);
  	$("#result").val("");
  }
}

function doStuff() {
  var version = $("#version").val();
  $.cookie("version", version);
  var strURL = "/mobileapi/v/" + version + $("#url").val();
  var payload = $("#payload").val();
  $.ajax({
      type: "POST",
      url: strURL,
      data: "data=" + $.URLEncode(payload),
      dataType: "html",
      success: function(msg){
         $("#result").val(msg);
      }
  });	
}	
</script>
</head>
<body>
<form name="f1">
  <p><b>URL:</b> <input id="url" name="url" type="text" size="65" /> <b>Ver:</b> <input id="version" name="version" type="text" size="2" />
  <b>- or -</b>
  <select name="loaddata" id="loaddata" onchange='JavaScript:loadStuff()'>
  <option value="">========== LOGIN ==========</option>
  <option value="Login">Login</option>
  <option value="Logout">Logout</option>
  <option value="Ping">Ping</option>
  <option value="LoginRefresh">Login Refresh</option>
  <option value="ForgotPassword">Forgot Password</option>
  <option value=""> ========== CART ========== </option>
  <option value="AddPromo">CART - Apply Promo</option>
  <option value="RemovePromo">CART - Remove Promo</option>
  <option value="AddItem-SimpleBeef">CART - Add - Simple Beef</option>
  <option value="AddItem-SimpleWine">CART - Add - Simple Wine</option>
  <option value="AddItem-MultipleBeef">CART - Add Multiple Beefs</option>
  <option value="UpdateItem-SimpleBeef">CART - Update - Simple Beef</option>
  <option value="RemoveItem">CART - Remove</option>
  <option value="RemoveAllItems">CART - Remove All</option>
  <option value="RemoveMultipleItems">CART - Remove Multiple</option>
  <option value="ViewItems">CART - View Items</option>
  <option value=""> ========== CHECKOUT ========== </option>
  <option value="InitCheckout">CHECKOUT - Init</option>
  <option value="CheckoutAuthenticate">CHECKOUT - Auth</option>
  <option value="GetDeliveryAddresses">CHECKOUT - Get Delivery Addresses</option>
  <option value="SetDeliveryAddress">CHECKOUT - Set Delivery Address</option>
  <option value="VerifyAge">CHECKOUT - Verify Age</option>
  <option value="RemoveAlcohol">CHECKOUT - Remove All Alcohol</option>
  <option value="ReserveDeliverySlot">CHECKOUT - Set Delivery Slot</option>
  <option value="ATPErrorDetails">CHECKOUT - Get ATP Error Details</option>
  <option value="ATPRemove">CHECKOUT - Remove all unavailable items</option>
  <option value="GetPaymentMethods">CHECKOUT - Get Payment Methods</option>
  <option value="SetPaymentMethods">CHECKOUT - Set Payment Methods</option>
  <option value="OrderDetail">CHECKOUT - Order Detail</option>
  <option value="SubmitOrder">CHECKOUT - Submit Order</option>
  <option value=""> ========== SEARCH ========== </option>
  <option value="Search">SEARCH - Basic</option>
  <option value="SearchUPC">SEARCH - UPC barcode</option>
  <option value="SearchSort">SEARCH - Sort</option>
  <option value="SearchFilter">SEARCH - Filter</option>
  <option value="SearchAutocomplete">SEARCH - Autocomplete</option>
  <option value=""> ========== BROWSE ========== </option>
  <option value="BrowseDepartment">BROWSE - DEPARTMENT</option>
  <option value="BrowseCategory">BROWSE - CATEGORY</option>
  <option value="BrowseCategoryContent">BROWSE - CATEGORYCONTENT</option>  
  <option value=""> ========== PRODUCT ========== </option>
  <option value="ProductDetail">PRODUCT - Product Detail</option>
  <option value="ProductDetailMoreInfo">PRODUCT - More Info</option>
  <option value="ProductGetPrice">PRODUCT - Pricing API</option>
  <option value="AcknowledgeHealthWarning">PRODUCT - Ack Health Warning</option>
  <option value=""> ========== ORDERS ========== </option>
  <option value="GetOrderHistory">ORDERS - Order History</option>
  <option value="GetExistingOrder">ORDERS - Existing Order Detail</option>
  <option value="CancelExistingOrder">ORDERS - Cancel Existing Order</option>
  <option value="ModifyOrder">ORDERS - Modify Order</option>
  <option value="CancelOrderModify">ORDERS - Cancel Order Modify</option>
  <option value=""> ========== QUICKSHOP LISTS/ORDERS ========== </option>
  <option value="OrderHistoryQuickshop">QUICKSHOP - Previous Orders</option>
  <option value="QuickShopOrder">QUICKSHOP - Shop from Order</option>
  <option value="ShoppingLists">QUICKSHOP - Shopping Lists</option>
  <option value="QuickShopLists">QUICKSHOP - Shop from Shopping List</option>
  <option value="QuickShopEveryItemOrderedDept">QUICKSHOP - Department Everything You've Ever Ordered!</option>
  <option value="QuickShopEveryItemOrdered">QUICKSHOP - Everything You've Ever Ordered!</option>
  <option value=""> ========== WHATS GOOD ========== </option>
  <option value="WGDCategories">WHATS GOOD - Categories</option>
  <option value="WGDCategoryProducts">WHATS GOOD - Category Products</option>
  <option value=""> ========== SMARTSTORE ========== </option>
  <option value="SSYouMightAlsoLike">SMARTSTORE - YouMightAlsoLike</option>
  <option value="SSYourFavorite">SMARTSTORE - YourFavorite</option>
  <option value="SSFavorite">SMARTSTORE - Favorite (either FD or Yours)</option>
  <option value="SSFDFavorite">SMARTSTORE - FreshDirect Favorite</option>
  <option value=""> ========== CONTACT US ========== </option>
  <option value="ContactUsFormData">CONTACT US - Init</option>
  <option value="ContactUsSubmit">CONTACT US - Submit</option>
  <option value=""> ========== ACCOUNT ========== </option>
  <option value="AccountDeliveryInfo">ACCOUNT - Get Addresses</option>
  <option value="AccountDeliveryTimeslotsDefault">ACCOUNT - Get Delivery Timeslots DEFAULT</option>
  <option value="AccountDeliveryTimeslots">ACCOUNT - Get Delivery Timeslots</option>
  <option value="AccountCancelTimeslotsReservation">ACCOUNT - Cancel Timeslot Reservation</option>
  <option value="AccountReservedTimeslots">ACCOUNT - Reserve Delivery Timeslots</option>
  <option value=""> ========== MISC ========== </option>
  <option value="EmailCapture">Email Capture</option>
  <option value="ConfiguredValues">Configured Values</option>
  <option value=""> ========== HELP ========== </option>
  <option value="Help">Help</option>
  <option value="HelpContactUs">Help - Contact Us</option>
  <option value="HelpLearnMorePromo">Help - Learn More Promo</option>
  </select>
  
  <input value="Go" type="button" onclick='JavaScript:doStuff()'></p>
  <p><b>Payload:</b><br />
  <textarea id="payload" name="payload" rows="5" cols="120"></textarea></p>
  <p><b>Result:</b><br />
  <textarea id="result" name="result" rows="20" cols="120"></textarea></p>
  <div id="result"></div>
</form>
</body>
</html>
