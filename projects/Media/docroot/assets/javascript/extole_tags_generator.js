/*
Helper function for 'setAndAppendExtoleObject'.
Places finalized Extole tag content in HTML hierarchy.
REQUIRES: jQuery as '$jq'
*/
function appendExtoleTag(obj, tag_type, id_placeafter, section){
	var extoleTag = document.createElement('script');
	extoleTag.type = tag_type;
	extoleTag.text = JSON.stringify(obj);
	
	console.log("extoleTag.text = " + extoleTag.text);
	
	/*if there is an html id given in parameter 3, then place the extol tag after said element.
	Otherwise, it goes to the end of the body tag.
	(but it might be superceded by other elements placed by other active javascript).
	*/
	if(typeof id_placeafter !== undefined && ($jq("#"+id_placeafter).length > 0) ){
		$jq("#"+id_placeafter).after(extoleTag);
		
	}else{
		
		//document.getElementsByTagName('body')[0].appendChild(extoleTag);
		
		$jq(document).ready(function(){				
			$jq("body").append(extoleTag);
		});
	}
	
	console.log("Extole tag_type: " + tag_type + " placed, section = " + section);
}

/*
This function usually called from OAS to place the Extole tags on page. It relys on 'appendExtoleTag' to do final placement.
However, this function is also called locally if a new customer is detected via the Java session, but only for 'REGISTER'.
REQUIRES: globalExtoleVars(global object), populated with server jsp/java variables
*/
function setAndAppendExtoleObject(section, id_placeafter){
	//'globalExtoleVars' is required
	if( typeof(globalExtoleVars) != "object" ){
		return;
	}
	
	//customer has to be signed in to continue, whether as a new 'friend' or as an advocate
	if( globalExtoleVars.isSignedCustomer == false ){
		console.log("sorry, no extole for you");
		return;
	}
	
	//default condition to test down the line, default is whether this logged in customer has completed and fulfilled orders
	var test_condition = globalExtoleVars.validOrderCount;
	
	//default for the most common extol tag type, the other kind being 'extole/conversion'; this being used by register and purchase
	var tag_type = "extole/widget";
	
	//core object
	var obj = {};

	//params sub-object of 'obj'. Used for everything but "fd_hp_carousel".
	obj.params = {};
	obj.params.f = globalExtoleVars.custFirstName;
	obj.params.l = globalExtoleVars.custLastName;
	obj.params.e = globalExtoleVars.custEmail;

	//core conditions based on what section this is for
	switch(section){
		case "fd_hp_banner":
		case "fd_search_results":
		case "fd_category":
		case "fd_pres_picks":
		case "fd_order_confirm":
		case "fd_order_confirm_banner":
		case "fd_hp_carousel":
			obj.zone = section;
			
			if(section == "fd_hp_carousel"){
				delete obj.params;
			}
			
			break;
		case "REGISTER":
			if( window.page != "invite_signup.jsp" ){
				//hides the popups for registration
				$jq( "html" ).append( "<style>#cta1, #cta2, div[class^='extole_id'] {display:none;}</style>" );
			}
		case "PURCHASE":
			//both this and 'PURCHASE' extole tags have a 'type' property, instead of a 'zone' property.  (but 'PURCHASE' extole tag has an additional helper extole widget tag)
			obj.type = section;

			obj.params.partner_conversion_id = globalExtoleVars.custId;
			
			tag_type = "extole\/conversion";
			
			//only brand new signup customers may continue with this
			test_condition = globalExtoleVars.justSignedUp;
			
			if(section == "PURCHASE"){
				//more parts for the main extole tag here
				obj.params['tag:cart_value'] = globalExtoleVars.validOrderCount;
								
				//override to always true for PURCHASE section.  Do not worry, login verification executed above.
				test_condition = true;
				
				//'purchase' requires 2 Extole tags, this is the first of two.  executed/generated after this switch statement
				var obj_pre = {};
				obj_pre.zone = "fd_order_confirm";
				
				tag_type_pre = "extole/widget";
			}
			
			break;
		default:
			break;
	}
	
	console.log(obj);console.log("section = " + section);
	
	//final test as to whether an extole tag should be rendered.
	if( test_condition == false ){
		return;
	}
	
	/* Only for purchase section.
	Executed here because we do not want the extole tags that result from this conditional to run until after 'test_condition' is run AND tested as 'true'.
	*/
	if (!$jq.isEmptyObject(obj_pre)) { //append if set up
		//if there is an html tag with a specified id attribute for function parameter 2, then place this Extole tag right after it.  Otherwise, place it at the end of the body tag.
		if(typeof id_placeafter !== undefined){
			appendExtoleTag(obj_pre, tag_type_pre, id_placeafter, section);
		}else{
			appendExtoleTag(obj_pre, tag_type_pre, null, section);
		}
	}

	//Final or only extole tag (depending on 'section' type) for the specified section rendered.
	if (!$jq.isEmptyObject(obj)) { //append if set up
		if(typeof id_placeafter !== undefined){
			appendExtoleTag(obj, tag_type, id_placeafter, section);
		}else{
			appendExtoleTag(obj, tag_type, null, section);
		}
	}
}//end function 'setAndAppendExtoleObject'


/*
If a customer IS signed in AND this is a brand new customer.  Never is supposed to run at any other time.
Placed inside '(function() {' block and a setInterval checker for jQuery existence block because both functions above use jQuery (as '$jq').
*/
(function(){
	
	//usable for getting the filename of current page minus the directory
	var path = window.location.pathname;
	window.page = path.split("/").pop();
	
	var extoleInt = setInterval(function(){ 
		if( typeof($jq) == "function" ){
			clearInterval(extoleInt);
			
			//this is to prevent iframed popups (or any other iframed request) from having the RAF lower ad popup (APPBUG-4160)
			//also to prevent traditional popup windows from having the RAF lower ad (APPBUG-4237)
			if( top != self || (globalExtoleVars.isSignedCustomer != true && window.page != "invite_signup.jsp") || window.opener != null ){ // '||' aka 'OR' conditional, prevents this from being shown on right after customer signs up. (APPBUG-4123)
				//hides the popups
				$jq( "html" ).append( "<style>#cta1, #cta2, div[class^='extole_id'] {display:none;}</style>" );
			}
			
			$jq.ajax({
				url: "//tags.extole.com/553175139/core.js", /*core 3rd party library url*/
				dataType: "script",
				success: function(){
					if( ( typeof(globalExtoleVars) == "object" ) && (globalExtoleVars.isSignedCustomer != false)){
						if( (globalExtoleVars.justSignedUp != false) ){
							console.log("lets register you, new customer");
							
							setAndAppendExtoleObject("REGISTER");
						}
						//else{
						//setAndAppendExtoleObject("PURCHASE",null);
						//}
					}//done just seeing if the Extole object was set up with a signed customer
					
					//for duplicating that orange button in the popout, APPBUG-4079
					if( $jq("#cta2").length > -1 ){
						$jq('a[href$="brownie_points.jsp"]').each(function(){
							$jq(this).click(function(e){
								e.preventDefault();
								
								$jq( "#cta2" ).trigger( "click" );
							})
						})
					}
				}//end success function for the 3rd party call
			});//end initial call for 3rd party Extole library
		}
	}, 100);
})();