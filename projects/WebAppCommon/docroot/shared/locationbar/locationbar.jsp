<%@page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import='com.freshdirect.delivery.EnumDeliveryStatus' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel' %>
<%@ page import='java.util.List' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<fd:LocationHandler/>
<%
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
String selectedPickupId = (String)pageContext.getAttribute(LocationHandlerTag.SELECTED_PICKUP_DEPOT_ID_ATTR);
Boolean disabled = (Boolean)pageContext.getAttribute(LocationHandlerTag.DISABLED_ATTR);
%>

<tmpl:insert template="/shared/locationbar/locationbar_layout.jsp">

<tmpl:put name="zipcode"><span class="zipcode orange"><%= selectedAddress.getZipCode() %></span> </tmpl:put>
<% 
	if(user!=null && user.getLevel() != FDUserI.GUEST) {
		List<ErpAddressModel> allHomeAddresses = user.getAllHomeAddresses();
		List<ErpAddressModel> allCorporateAddresses = user.getAllCorporateAddresses();
		List<DlvLocationModel> allPickupDepots = (List<DlvLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);
		
		if( allHomeAddresses.size() + allCorporateAddresses.size() + allPickupDepots.size() > 1 && (disabled == null || !disabled)) {
			%><tmpl:put name="address"><select id="selectAddressList" name="selectAddressList"><%
				if(allHomeAddresses.size()>0){%>
					<optgroup label="Home Delivery">
						<logic:iterate id="homeAddress" collection="<%=allHomeAddresses%>" type="com.freshdirect.common.address.AddressModel">
							<option<%= selectedAddress.equals(homeAddress) ? " selected='selected'" : "" %> value="<%=homeAddress.getPK().getId()%>"><%=LocationHandlerTag.formatAddressText(homeAddress)%></option>
						</logic:iterate>
					</optgroup>
				<%}
				if(allCorporateAddresses.size()>0){%>
					<optgroup label="Office Delivery">
						<logic:iterate id="corporateAddress" collection="<%=allCorporateAddresses%>" type="com.freshdirect.common.address.AddressModel">
							<option<%= selectedAddress.equals(corporateAddress) ? " selected='selected'" : "" %> value="<%=corporateAddress.getPK().getId()%>"><%=LocationHandlerTag.formatAddressText(corporateAddress)%></option>
						</logic:iterate>
					</optgroup>
				<%}
				if(allPickupDepots.size()>0){%>
				<optgroup label="Pickup">
					<logic:iterate id="pickupDepot" collection="<%=allPickupDepots%>" type="com.freshdirect.delivery.depot.DlvLocationModel">
						<option<%= selectedPickupId!=null && selectedPickupId.equalsIgnoreCase(pickupDepot.getId()) ? " selected='selected'" : "" %> value="DEPOT_<%=pickupDepot.getId()%>"><%=LocationHandlerTag.formatAddressText(pickupDepot.getAddress())%></option>
					</logic:iterate>
				</optgroup>
				<%}
			%></select></tmpl:put><%
		} else { //only one address
			%><tmpl:put name="address"><span class="text"><%=LocationHandlerTag.formatAddressText(selectedAddress)%></span></tmpl:put><%	
		}

	} else { //non-recognized user
		String shortAddress = LocationHandlerTag.formatAddressShortText(selectedAddress);
		%><tmpl:put name="address"><span class="text"><%="".equals(shortAddress) ?  "" : "("+shortAddress+")" %></span> <span id="newzip"><input type="text" id="newziptext" class="placeholder" placeholder="change zip code" maxlength="5"><input type="image" src="/media_stat/images/locationbar/button_go.png" id="newzipgo"></span></tmpl:put><%		
	}
%>
<tmpl:put name="zipdisplay"><tmpl:get name="zipcode" /> <tmpl:get name="address" /></tmpl:put>

<tmpl:put name="cheftable"><%
	if(user!=null && user.isChefsTable() && !user.getChefsTableInduction().equals("0") && user.getChefsTableInduction().length() == 8) { 
		%><a href="/your_account/manage_account.jsp"><img src='<%= "/media_stat/images/navigation/global_nav/global_hdr_ct_"+user.getChefsTableInduction().substring(0,4)+".gif"%>' width="256" height="10" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0"></a><% 
	} else if (user!=null && user.isDlvPassActive()) {
		%><a href="/your_account/delivery_pass.jsp"><img src="/media_stat/images/navigation/global_nav/global_hdr_dp.gif" width="217" height="10" alt="CLICK HERE FOR DETAILS"  vspace="0" border="0"></a><% 
	} %> 
</tmpl:put>

<tmpl:put name="loginButton">
	<button class="loginButton" id="locabar_loginButton">login</button>
	<style>
		.ccc {
			color: #ccc;
		}
		.bold {
			folt-weight: bold;
		}
		.alignRight {
			text-align: right;
		}
		#login_cont {
			display: inline-block;
		}
		#login_cont_formContent {
			position: absolute;
			z-index: 10001;
			width: 200px;
			height: auto;
			background-color: #fff;
			padding: 15px 20px;
			box-shadow: 3px 3px 3px 0 #666, -3px 3px 6px #888;
			border: 1px solid #666;
			border-left: 1px solid #888;
			border-top: 1px solid #5B8710;
		    border-radius: 10px 0 10px 10px;
		}
		#login_cont_formContent_email, #login_cont_formContent_password {
			width: 100%;
		}
		#login_cont_formContent .errorMsg {
			border: 1px solid #f00;
			background-color: #FEE7ED;
			padding: 6px;
		}
		#login_cont_formContent .errorMsg .header {
			font-weight: bold;
			font-size: 12px;
		}
		#login_cont_formContentForm_signInCont {
			text-align: right;
		}
		#login_cont_formContent div {
			margin: 6px 0;
		}
		#locationbar .loginButtonTab {
			z-index: 10002;
    		background-image: url("/media_stat/images/locationbar/button_login_tab.png");
		}
		.loginButton {
			border: 0 none;
			cursor: pointer;
		}
	</style>
	<script>
		/* input type changer for jQuery < 1.9
		 * inputElem is the <input/> element
		 * type is the type you want to change it to
		 */
		function changeType(inputElem, type) {
			var elem = $jq(inputElem);
		    if(elem.prop('type') == type)
		        return elem; //That was easy.
		    try {
		        return elem.prop('type', type); //Stupid IE security will not allow this
		    } catch(e) {
		        //Try re-creating the element
		        //jQuery has no html() method for the element, so we have to put into a div first
		        var html = $jq("<div>").append(elem.clone()).html();
		        var regex = /type=(\")?([^\"\s]+)(\")?/; //matches type=text or type="text"
		        //If no match, we add the type attribute to the end; otherwise, we replace
		        var tmp = $jq(html.match(regex) == null ?
		            html.replace(">", ' type="' + type + '">') :
		            html.replace(regex, 'type="' + type + '"') );
		        //Copy data from old element
		        tmp.data('type', elem.data('type') );
		        var events = elem.data('events');
		        var cb = function(events) {
		            return function() {
		                //Bind all prior events
		                for(i in events) {
		                    var y = events[i];
		                    for(j in y)
		                        tmp.bind(i, y[j].handler);
		                }
		            }
		        }(events);
		        elem.replaceWith(tmp);
		        setTimeout(cb, 10); //Wait a bit to call function
		        return tmp;
		    }
		}
		 
		$jq(document).ready(function() {
            $jq(document).on('mouseup', function(event) {
            	if($jq(event.target).attr('id') != 'locabar_loginButton' && $jq(event.target).closest('#login_cont_formContent').length==0) {
                	$jq("#locabar_loginButton").removeClass("loginButtonTab");
                	$jq('#login_cont_formContent').hide();
            	}
            });
            function alignLoginDropbox() {
				//call this each time to ensure alignment
				var cssObj = $jq("#locabar_loginButton").offset();
				cssObj.top = cssObj.top + $jq("#locabar_loginButton").height() + 'px';
				cssObj.left = ((cssObj.left + $jq("#locabar_loginButton").outerWidth()) - $jq('#login_cont_formContent').outerWidth()) + 'px';
				$jq('#login_cont_formContent').css(cssObj);
            	
            }
    		$jq(window).resize(function() {
    			alignLoginDropbox();
    		});
            
            if ($jq('#login_cont_formContent').length == 0) {
            	var loginDropboxHtml = '';
            	loginDropboxHtml += '<div id="login_cont_formContent" style="display: none">';
            		loginDropboxHtml += '<form id="login_cont_formContentForm">';
            			loginDropboxHtml += '<div><input id="login_cont_formContent_email" name="userId" value="Username" data-deftext="Username" class="ccc" /></div>';
            			loginDropboxHtml += '<div><input id="login_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" type="text" /></div>';
            		loginDropboxHtml += '</form>';
            		loginDropboxHtml += '<div id="login_cont_formContentForm_signInCont"><button id="login_cont_formContentForm_signIn" class="imgButtonOrange">sign in</button></div>';
        			loginDropboxHtml += '<div class="errorMsg" style="display: none;">'
        				loginDropboxHtml += '<div class="header">Please re-enter your Email and Password.</div>'; 
        				loginDropboxHtml += 'The information you entered is incorrect. Please try again.';
        			loginDropboxHtml += '</div>';
            		loginDropboxHtml += '<div class="bold alignRight">Forgot your <a href="/login/forget_password.jsp">password</a>?</div>';
            	loginDropboxHtml += '</div>';
            	
				$jq('body').append(loginDropboxHtml);
				
			}

            $jq('#login_cont_formContentForm_signIn').click(function(event){
				event.preventDefault();
            	$jq('#login_cont_formContentForm').submit();
            });
            
            $jq('#login_cont_formContentForm').submit(function(event) {
				event.preventDefault();
				
            	var form = $jq(this);
            	if (!form.data('submitting')) {
    				$jq('#login_cont_formContent .errorMsg').hide();
    				
            		form.data('submitting', true);
            		var formData = {};
            		$jq(form.serializeArray()).each(function () { formData[this.name] = this.value; });
            		$jq.post('/api/login/', "data="+JSON.stringify(formData), function(data) {
            			if (data.success) {
            				console.log('success');
            			} else {
            				console.log('err');
            				$jq('#login_cont_formContent .errorMsg').show();
            			}
                		form.data('submitting', false);
            		});
            	}
            });
            
            $jq('#login_cont_formContent input').on('focus blur', function(event) {
            	var elem = $jq(this);
            	var defText = elem.data('deftext');
            	if (event.type == 'focus') {
            		if (elem.hasClass('ccc') && elem.val() == defText) {
            			elem.val('');
            			elem.toggleClass('ccc');
            		}
        			if (elem.attr('id') == 'login_cont_formContent_password') {
        				changeType(elem, 'password');
        			}
            	} else if (event.type == 'blur') {
            		if (elem.val() == '') {
            			elem.val(defText);
            			elem.toggleClass('ccc');
            			if (elem.attr('id') == 'login_cont_formContent_password') {
            				changeType(elem, 'text');
            			}
            		}
            	}
            });
            
			$jq('#locabar_loginButton').click(function(event) {
    			alignLoginDropbox();
				
	            //first
               	$jq("#locabar_loginButton").toggleClass("loginButtonTab");
               	$jq('#login_cont_formContent').toggle();
			});
		});
	</script>
</tmpl:put>
<tmpl:put name="logoutButton"><a href="/logout.jsp" class="logoutButton">logout</a></tmpl:put>
<tmpl:put name="signupButton"><% 
	if(FDStoreProperties.isLightSignupEnabled()) { 
		%><a href="#" class="signUpButton" onclick="doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/signup_lite.jsp\' width=\'480px\' height=\'590px\' frameborder=\'0\' ></iframe>', '<span class=\'text12\' style=\'color: #000; margin-left: -12px;\'><strong>Already have a password? <a href=\'/login/login.jsp\' onclick=\'window.top.location=this.href;return false;\' style=\'text-decoration:none;\'>Log in now</a></strong></span>')">signup</a><% 
	} else { 
		%><a href="/registration/signup.jsp" class="signUpButton">signup</a><% 
	} 
%></tmpl:put>
<%
	if (user!=null && user.getLevel() == FDUserI.SIGNED_IN) {
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="logoutButton" /></tmpl:put><%
	} else if (user!=null && user.getLevel() == FDUserI.RECOGNIZED) {	
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="loginButton" /></tmpl:put><%
	} else { 
		%><tmpl:put name="buttons"><label>New customer?</label><tmpl:get name="signupButton" /><tmpl:get name="loginButton" /></tmpl:put>
		<tmpl:put name="location_message"><jsp:include page="location_messages.jsp" /></tmpl:put><%
    }

	if(Boolean.TRUE == pageContext.getAttribute(LocationHandlerTag.SERVICE_TYPE_MODIFICATION_ENABLED)){
		if(user.isCorporateUser()){
			%><tmpl:put name="hoicon"><a class="home green" href="/index.jsp">Home delivery?</a></tmpl:put><%			
		}else {
			%><tmpl:put name="hoicon"><a class="office green" href="/department.jsp?deptId=COS">Office delivery?</a></tmpl:put><%			
		}
	}
%>

</tmpl:insert>
