<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.data.*" %>
<%@ page import="org.apache.oltu.oauth2.common.message.OAuthResponse" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<% 
OAuth2Service authService = OAuth2Service.defaultService();
String action = request.getParameter("action");
String item = request.getParameter("item");
String token = request.getParameter("token");
String mapKey = request.getParameter("key");
if (token != null){
	
	token = token.replace(" ","+");
}
if(item != null){
	
}
if(action!= null && action.equals("clear")){
	if(mapKey.equals("all")){
		FDStoreProperties.VENDOR_MAP.clear();
	} else {
		FDStoreProperties.VENDOR_MAP.remove(mapKey);
	}
	
}
%>
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Test OAuth 2.0</title>
	<jwr:script src="/oauth.js" useRandomParam="false" />
	<style>
		.container{
			padding: 5px 0 0 20px;
		}
		.user-token-display {
		  overflow: hidden;
		  max-width: 500px;
		  text-overflow: ellipsis;
	      white-space: nowrap;
		}
		.inline-block {
			display: inline-block;
		}
		.label {
			width: 100px;
		}
		.label:after {
			content: ':';
		}
		.red {
			color: red;
		}
		h4 {
			display: inline-block;
    		padding-right: 20px;
		}
	</style>
	<script>
		$(document).on('click', '.remove-all', function (e) {
			document.location = '/test/oauth/client.jsp?action=clear&key=all';
		});
		$(document).on('click', '.remove', function (e) {
			document.location = '/test/oauth/client.jsp?action=clear&key=' + $(e.target).siblings('.phone').val();
		});
		$(document).on('click', '.add-item-to-cart', function (e) {
			$('.status').html("Adding item to user's shopping cart");
			var product = window.products.filter(function (p){ return p.id === $('.product-list').val()})[0];
			$.ajax({
		        type: "POST",
		        url: "/api/addtocart",
		        headers: {'Authorization': 'Bearer ' + $('.token').val()},
		        data: 'data=' + encodeURIComponent('{'+
					'"items": [{'+
						'"externalGroup": "Chat-to-Cart",'+
						'"externalSource": "Storepower",'+
						'"externalAgency": "STOREPOWER",'+
						'"skuCode": "' + product.skuCode + '",'+
						'"quantity": "1",'+
						'"salesUnit": "' + product.saleUnit[0].id + '"'+
					'}],'+
					'"eventSource": "FinalizingExternal"'+
				'}'),
		        success: function (dt, status, request) {
		        	if(!dt.atcResult || !dt.atcResult[0]) {
		        		 $('.status').html('Error occurs when adding item.' ).animate({
					         backgroundColor: "#fff8c4"
					       }, 1000 ).animate({backgroundColor: "transparent"});
		        	} else if(dt.atcResult[0].status=='SUCCESS'){
				        $('.status').html('Successfully added item.' ).animate({
				         backgroundColor: "#fff8c4"
				       }, 1000 ).animate({backgroundColor: "transparent"});
			      	} else {
			      		$('.status').html('Error: '  + dt.atcResult[0].message).animate({
				         backgroundColor: "#fff8c4"
				       }, 1000 ).animate({backgroundColor: "transparent"});
			      	}
		            
		
		        },
		        error: function (jqXHR, status) {
			        var iFrame = $('<iframe id="error" width="600"></iframe>');
					$('.result').html(iFrame);
					
					var iFrameDoc = iFrame[0].contentDocument || iFrame[0].contentWindow.document;
					iFrameDoc.write(jqXHR.responseText);
					iFrameDoc.close();
		        }
		    });
		});
		$(document).on('click', '.get-cart', function(e) {
			$('.status').html('Getting cart data').animate({
					         backgroundColor: "#fff8c4"
					       }, 1000 ).animate({backgroundColor: "transparent"});
			$.ajax({
		        type: "GET",
		        url: "/api/expresscheckout/cartdata",
		        headers: {'Authorization': 'Bearer ' + $('.token').val()},
		        success: function (dt, status, request) {
		            $(e.target).siblings('.result').html(JSON.stringify(dt));
					$('.status').html('Received cart data. # of cartlines in the first cart section: ' + (dt.cartSections && dt.cartSections.length && dt.cartSections[0] && dt.cartSections[0].cartLines && dt.cartSections[0].cartLines.length) || 0).animate({
					         backgroundColor: "#fff8c4"
					       }, 1000 ).animate({backgroundColor: "transparent"});
		        },
		        error: function (jqXHR, status) {
			        var iFrame = $('<iframe id="error" width="600"></iframe>');
					$(e.target).siblings('.result').html(iFrame);
					
					var iFrameDoc = iFrame[0].contentDocument || iFrame[0].contentWindow.document;
					iFrameDoc.write(jqXHR.responseText);
					iFrameDoc.close();
		        }
		    });    
		});
		$(document).on('click', '.go-cart', function(e) {
			window.location='/expressco/view_cart.jsp?fetch=true';
		});
		$(document).on('click', '.get-order-history', function(e){
			$(e.target).siblings('.user-order-history').html("Loading");
			 $.ajax({
		        type: "GET",
		        url: "/api/order?action=orderhistory",
		        headers: {'Authorization': 'Bearer ' + $(e.target).siblings('.user-token').val()},
		        success: function (dt, status, request) {
		            $(e.target).siblings('.user-order-history').html(JSON.stringify(dt));
		
		        },
		        error: function (jqXHR, status) {
			        var iFrame = $('<iframe id="error" width="600"></iframe>');
					$(e.target).siblings('.user-order-history').html(iFrame);
					
					var iFrameDoc = iFrame[0].contentDocument || iFrame[0].contentWindow.document;
					iFrameDoc.write(jqXHR.responseText);
					iFrameDoc.close();
		        }
		    });
			console.log(e);
		});
		
	</script>
	
</head>
	<body>
		<div class="container">
			<% if(item != null) { %>
				<p class="status">Getting order history</p>
				<p class="match-item"></p>
				<p><select class="product-list"></select></p>
				<button class="add-item-to-cart">Add item to user's shopping cart</button>
				<button class="get-cart">Get user's shopping cart</button>
				<button class="go-cart">Go to shopping cart</button>
				<input type="hidden" class="token" value="<%=token %>"/>
				<input type="hidden" class="item-id" value="<%=item %>"/>
				<div class="result"></div>
				
			<%} else if (action!= null) {
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			%>
			<div><h3>Vendor Side</h3></div>
			<div><h4> User Tokens</h4><button class="remove-all">Remove all</button></div>
			<% if( FDStoreProperties.VENDOR_MAP.size() == 0) {%>
				<div>Empty</div>
			<% 
				}
			for (String key: FDStoreProperties.VENDOR_MAP.keySet()){

	            String tokenId = FDStoreProperties.VENDOR_MAP.get(key);
				OAuth2CodeAndTokenData oAuthtoken = authService.getCodeOrTokenData(tokenId);
			%>
			<p>
				<div>
					<div class="label inline-block">Phone</div>
					<div class="inline-block" ><%=key %></div>
				</div>
				<div>
					<div class="label inline-block">User Id</div>
					<div class="inline-block" ><%=oAuthtoken.getUserLoginId() %></div>
				</div>
				<div class="user-token-display">
					<div class="label inline-block">User Token</div>
					<div title="<%=tokenId %>" class="inline-block" ><%=tokenId %></div>
				</div>
				<div>
					<div class="label inline-block">Expiration</div>
					<div class="inline-block" data-expire-on="<%=oAuthtoken.getExpiresOn() %>"><%=df.format(oAuthtoken.getExpiresOn() * 1000)%></div>
				</div>
				<input type="hidden" class="user-token" value="<%=tokenId != null? tokenId : "" %>"/>
				<input type="hidden" class="phone" value="<%=key %>" />
				<button class="get-order-history">Get User Order History</button>
				<button class="remove">Remove</button>
				<br />
				<div class="user-order-history">
				</div>
			</p>
			<br />
			<%
			}
			} %>
			
		</div>
		<script>
			if( $('.item-id').val()) {
				$('.add-item-to-cart').hide();
				$('.product-list').hide();
				$('.match-item').hide();
				$.ajax({
			        type: "GET",
			        url: "/api/order?action=orderhistory",
			        headers: {'Authorization': 'Bearer ' + $('.token').val()},
			        success: function (dt, status, request) {
			        	$('.product-list').show();
			            //$('.result').html(JSON.stringify(dt));
						$('.add-item-to-cart').show();
						window.vendor = window.vendor ||{};
						window.vendor.itemId = dt && dt[0] && dt[0].productIds && dt[0].productIds[0];
						var productlist = [];
						window.products = [];
						var itemId = $('.item-id').val();
						itemId = itemId && itemId.toLowerCase();
						var matchProduct = null;
						dt.forEach(function (d){ 
							if(d.products && d.products.length) {
								d.products.forEach(function(p) {
									if (productlist.indexOf(p.id) == -1) {
										if( !matchProduct && itemId && p.title && p.title.toLowerCase().indexOf(itemId) !== -1) {
											matchProduct = p;
											$('.product-list').append('<option selected value="' + p.id + '">' + p.title + '</option>');
										} else {
											$('.product-list').append('<option value="' + p.id + '">' + p.title + '</option>');
										}
										productlist.push(p.id);
										window.products.push(p);
									}
								});
							}
						});
						
						if (matchProduct) {
							var matchProductTitle = matchProduct.title.toLowerCase();
							var itemIndex = matchProductTitle.indexOf(itemId);
							$('.match-item').html('Matched Item: <strong>'+
							 matchProduct.title.substring(0, itemIndex) +
							 '<span class="red">' +
							 matchProduct.title.substring(itemIndex, itemIndex + itemId.length) + 
							 '</span>' +
							 matchProduct.title.substring(itemIndex + itemId.length) +
							 '</strong>').show();
						} else {
							$('.match-item').html('<strong>No Match</strong>').show();
						}
						
						$('.status').html('Received order history').animate({
					         backgroundColor: "#fff8c4"
					       }, 1000 ).animate({backgroundColor: "transparent"});
			        },
			        error: function (jqXHR, status) {
				        var iFrame = $('<iframe id="error" width="600"></iframe>');
						$('.result').html(iFrame);
						
						var iFrameDoc = iFrame[0].contentDocument || iFrame[0].contentWindow.document;
						iFrameDoc.write(jqXHR.responseText);
						iFrameDoc.close();
			        }
			    });
			}
		</script>
	</body>
</html>
