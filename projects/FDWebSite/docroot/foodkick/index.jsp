<%@ include file="includes/fk_presenter_vars.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick</tmpl:put>
	<tmpl:put name='content'>
		<script type="text/javascript">
			cmCreatePageviewTag("HOMEPAGE: index.jsp","FDX_HOMEPAGE",null,null,"-_--_-HOMEPAGE: index.jsp-_--_--_--_-HOMEPAGE: index.jsp");
		</script>
		<a name="slideshow" id="slideshow" class="fk_anchor"></a>
		<section id="section_slideshow" class="main_section">
			<%=iosapp_button(FK_IOSAPP_DLINK, "Download the APP", "purple") %>
			<div id="carousel_1"></div>

			<button class="appstore_button" onclick="location.href='<%=FK_IOSAPP_DLINK %>'">
				<%--svg_obj("appstore.svg") --%>
				<img src="<%=SVG_SRC %>appstore.svg" />
			</button>
		</section> <!-- end of section 'section_1'  -->
		
		<a name="Get_Foodkick" id="Get_Foodkick" class="fk_anchor"></a>
		<section id="section_Get_Foodkick" class="main_section">
			<h1>Hey Brooklyn <span class="plus_span">+</span> LIC, the party starts now!</h1>
		
			<section>
				<article>
					<h2>Let&rsquo;s make this thing happen with</h2>
					<h1>30 days of <br/> FREE delivery.</h1>

					<%=iosapp_button(FK_IOSAPP_DLINK, "Download the APP", "purple") %>
				</article>
				
				<figure>
					<img src="<%=SVG_SRC %>section_3/arrow.svg" class="svg_arrow" />
				</figure>
				<article>
					<form action="/foodkick/ajax/fdx_zone.jsp" method="post" id="ziphandler">
						<p>We're expanding fast. Check your ZIP to see if we're in your hood.</p>

						<div class="field_holders">
							<!--
							<input type="text" name="zipcode" id="zipcode_zh" class="required zipcode" data-msg="invalid zipcode"
							placeholder="ZIP CODE" maxlength="5" pattern="\d{5}" min="00001" max="99999" title="USA Zipcode format, like '12345'"
							onkeyup="numbersOnly(this);" required autocomplete="off" />
							-->
							
							<input type="text" name="zipcode" id="zipcode_zh"
							placeholder="ZIP CODE" maxlength="5" pattern="\d{5}" min="00001" max="99999" title="USA Zipcode format, like '12345'"
							onkeyup="numbersOnly(this);" required autocomplete="off" />
						</div>
						
						<button id="submit_ziphandler" type="submit" autocomplete="off" class="button_disabled" disabled>Check</button>
					</form>
					
					<form action="/api/locationhandler.jsp" method="post" id="locationhandler" style="display:none">
						<!--  <p class="ucwords">GIVE US YOUR ZIP SO WE KNOW WHERE TO GO NEXT.</p>-->
						
						<p>
						We&rsquo;re not delivering to your hood yet, but we can send you an email once we&rsquo;re there.
						You can still download the app to check it out. 
						</p>

						<div class="field_holders">
							<input type="text" name="zipcode_lh" id="zipcode_lh" class="required zipcode" data-msg="invalid zipcode"
							placeholder="ZIP CODE" maxlength="5" pattern="\d{5}" min="00001" max="99999" title="USA Zipcode format, like '12345'"
							onkeyup="numbersOnly(this);" required autocomplete="off" />
						</div>
						
						<div class="field_holders">
							<input type="email" type="email" name="email_lh" id="email_lh" class="required custom_email" data-msg="invalid email"
							placeholder="EMAIL" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,24}$" required autocomplete="off" />
						</div>

						<button id="submit_locationhandler" type="submit" class="button_disabled" disabled>Submit</button>
						<br/>
						By hitting submit, you agree to receive FoodKick emails.
						
					</form>
					
					<div id="we_deliver_to_you" style="display:none">
						<h2>
							<img src="<%=IMAGES_DIR %>/section_3/smiley.png" /> <br/>
							You're in luck! <br/>
							We're in your hood. <br/>
							Download the app today.
						</h2>
						<br/>
						<a href="#Get_Foodkick" onClick="reset_zip_forms();">
							<img src="<%=IMAGES_DIR %>/section_3/recycle.png" />
						</a>
						<br/>
						Try another zip.
						<br/>
					</div>
					
					<div id="form_congratulations" style="display:none">
						<p class="ucwords">Thank you for your submission!</p>
						We will contact you when Foodkick DOES deliver to your area!
						
						<br/>
						<a href="#Get_Foodkick" onClick="reset_zip_forms();">
							<img src="<%=IMAGES_DIR %>/section_3/recycle.png" />
						</a>
						<br/>
						Try another zip.
						<br/>
					</div>
				</article>
			</section>
		</section>
		
		<a name="How_it_works" id="How_it_works" class="fk_anchor"></a>
		<section id="section_How_it_works" class="main_section">
			<h1>This is the part where we tell you how fast and easy it is.</h1>
			
			<article>
				<figure>
					<div><%--svg_obj("section_2/bowl_bottle_fruit.svg") --%> <img src="<%=SVG_SRC %>section_2/bowl_bottle_fruit.svg" /> </div>
					<figcaption>
						<h4>GET WHAT YOU NEED FOR TODAY OR TOMORROW</h4>
						like meals, fresh fixin&rsquo;s, booze, and even TP.
					</figcaption>
				</figure>
			</article>
			
			<article>
				<figure>
					<div><%--svg_obj("section_2/clock.svg") --%> <img src="<%=SVG_SRC %>section_2/clock.svg" /></div>
					<figcaption>
						<h4>PICK A DELIVERY TIME</h4>
						that works for you. We can be there in an hour.
					</figcaption>
				</figure>
			</article>

			<article>
				<figure>
					<div><%--svg_obj("section_2/check.svg") --%> <img src="<%=SVG_SRC %>section_2/check.svg" /></div>
					<figcaption>
						<h4>GO BACK TO DOING YOU</h4>
						We&rsquo;ll take care of the food so you have time for important things, like having emoji-only text convos.
					</figcaption>
				</figure>
			</article>
			
			<article class="last">
				<figure>
					<div><%--svg_obj("section_2/smiley.svg") --%> <img src="<%=SVG_SRC %>section_2/smiley.svg" style="top:7px;" /></div>
					<figcaption>
						<h4>ENJOY YOUR EATS</h4>
						Dig in, share with friends, or snap a pic.
					</figcaption>
				</figure>
			</article>	
			<%=iosapp_button(FK_IOSAPP_DLINK, "Download the APP", "purple") %>
		</section>
		
		<a name="Featured_Food" id="Featured_Food" class="fk_anchor"></a>
		<section id="section_Whats_Good" class="main_section">
			<div class="container">
				<div id="carousel_Whats_Good_foodkick" class="carousel_Whats_Good">
					<a href="https://dev5.freshdirect.com?ad=19984" target="_top">
						<img src="<%=CMS_IMAGES_DIR_LP%>/950_1.jpg" alt="Get what you need in an hour: Customizable meals and solutions.
						Farm-fresh produce, meat, fish, dairy.  Pantry staples and home essentials.
						The perfect food and booze pairing recommendations from our squad." border="0" />
					</a>
				</div>
			</div>
			
			<div class="container">
				<div id="carousel_Whats_Good_scenes" class="carousel_Whats_Good">
					<!-- <script>
					OAS_AD('FKMiddle2');
					</script> -->
					<a href="https://dev5.freshdirect.com?ad=19992" target="_top">
						<img src="<%=CMS_IMAGES_DIR_LP%>/950_2.jpg" alt="Meet Cocktail Courier. Crafted cocktails without stepping foot outside?
						Cocktail Courier sources recipies for FoodKick from renowned NYC mixologists. Fancy a Moscow Mule or Midnight Manhattan?
						We'll deliver the mixers, liquor, and recipies to your door so you can get cozy and boozy in 3-2-1." border="0" />
					</a>
				</div>
			</div>
		</section>
		
		<a name="FOODKICK" id="FOODKICK" class="fk_anchor"></a>
		<section id="section_FOODKICK" class="main_section">
			<figure>
				<p class="author"><img src="<%=IMAGES_DIR %>/section_5/author_icon/1.jpg" /> <span class="author_name">@THEBAKER</span></p>
				<img src="<%=IMAGES_DIR %>/section_5/main/1.jpg" />
				<figcaption>
					<p class="likes">31 likes</p>
					<span class="caption_text">Loving this view</span>
					<span class="hash">#myfoodkick</span>
					<span class="hash">#clintonhill</span>
					<span class="hash">#gingersnapseason</span>
				</figcaption>
			</figure>
			<figure>
				<p class="author"><img src="<%=IMAGES_DIR %>/section_5/author_icon/2.jpg" /> <span class="author_name">@MOSTESSHOSTESS</span></p>
				<img src="<%=IMAGES_DIR %>/section_5/main/2.jpg" />
				<figcaption>
					<p class="likes">2 likes</p>
					<span class="hash">#myfoodkick</span>
					<span class="hash">#dinnerparty</span>
				</figcaption>
			</figure>
			<figure>
				<p class="author"><img src="<%=IMAGES_DIR %>/section_5/author_icon/3.jpg" /> <span class="author_name">@JHOLT</span></p>
				<img src="<%=IMAGES_DIR %>/section_5/main/3.jpg" />
				<figcaption>
					<p class="likes">45 likes</p>
					<span class="caption_text">@rjacoby Look what you're missing!!</span>
					<span class="hash">#myfoodkick</span>
					<span class="hash">#betterlucknexttime</span>
				</figcaption>
			</figure>
			<figure>
				<p class="author"><img src="<%=IMAGES_DIR %>/section_5/author_icon/4.jpg" /> <span class="author_name">@BKFOODIE</span></p>
				<img src="<%=IMAGES_DIR %>/section_5/main/4.jpg" />
				<figcaption>
					<p class="likes">18 likes</p>
					<span class="caption_text">Nothing better than a homecooked meal with friends</span>
					<span class="hash">#myfoodkick</span>
				</figcaption>
			</figure>
			<figure>
				<p class="author"><img src="<%=IMAGES_DIR %>/section_5/author_icon/5.jpg" /> <span class="author_name">@GYROSUPERHERO</span></p>
				<img src="<%=IMAGES_DIR %>/section_5/main/5.jpg" />
				<figcaption>
					<p class="likes">6 likes</p>
					<span class="hash">#wheresthechampagne</span>
					<span class="hash">#ojmakesitbreakfast</span>
					<span class="hash">#myfoodkick</span>
				</figcaption>
			</figure>
		</section>
	</tmpl:put>
	<tmpl:put name='special_disclaimer'>
		*This Offer is for free delivery on qualifying orders for a thirty (30) day period. Offer applies to first-time customers only. Free delivery means <span>no delivery or service fees</span>. <span>Eligible orders</span>
must (a) exceed minimum purchase requirements before taxes & fees, (b) be within eligible <span>delivery areas</span>, and (c) have a delivery window greater than one (1) hour. Delivery is subject to
availability. No promotion code necessary and the Offer will automatically apply starting with your first purchase and will continue for 30 days. This is a limited time Offer. All standard customer
terms and conditions apply. FoodKick reserves the right to cancel or modify this Offer at any time. Offer is nontransferable. Void where prohibited. All right reserved, Fresh Direct, LLC.
	</tmpl:put>
	
	<tmpl:put name='special_js'>
		<!--  <script src="<%=ANGULAR_DIR %>/angular.min.js"></script>
		<script src="<%=ANGULAR_DIR %>/angular-animate.min.js"></script>
		<script src="<%=ANGULAR_DIR %>/angular-sanitize.min.js"></script>
		<script src="<%=BOOTSTRAP_DIR %>/js/ui-bootstrap-tpls-0.14.3.min.js"></script>

		<link href="<%=BOOTSTRAP_DIR %>/css/bootstrap.min.css" rel="stylesheet" />
		-->
	
		<script src="<%=JS_DIR %>/jquery.slides.min.js"></script>
		<script type="text/javascript">
			window.IMAGES_DIR = "<%=IMAGES_DIR%>";
			window.CMS_IMAGES_DIR_LP = "<%=CMS_IMAGES_DIR_LP%>";
		</script>
		<script src="/media/editorial/foodkick/lp_top_carousel.js"></script>
	</tmpl:put>
</tmpl:insert>