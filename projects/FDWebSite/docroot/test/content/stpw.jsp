<!DOCTYPE html>
<html lang="en-US" prefix="og: http://ogp.me/ns#">
   <head>
      <meta charset="UTF-8" />
      <title>FreshDirect | Standalone Transactable Product Widgets.</title>
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" crossorigin="anonymous">

	  <script src="//code.jquery.com/jquery-3.2.1.min.js"></script>

	  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/fancybox/3.2.5/jquery.fancybox.min.css" />
	  <script src="//cdnjs.cloudflare.com/ajax/libs/fancybox/3.2.5/jquery.fancybox.min.js"></script>

	   <style>
	   .fancybox-slide--iframe .fancybox-content {
			width  : 800px;
			height : 800px;
			max-width  : 80%;
			max-height : 80%;
			margin: 0;
		}

		img {
		  width:550px;
		  height:400px;
		}
	   </style>
   </head>
   <body>
      <div class="container-fluid">
			  <div class="row">
			  	<div class="col-3"></div>
			  	<div class="col-6">
			  		<h2>Standalone Transactable Product Widgets</h2>
			  	</div>
			  	<div class="col-3"></div>
			  </div>
      		  <div class="row">
				  <div class="col-4">
						<label>
							<h5>Applegate Sheet Pan Breakfast with Sweet Potatoes and Bacon</h5>
							<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=del_pid_3204349&catId=bcn_pckgd" href="javascript:;">
								<img src="https://blog.freshdirect.com/wp-content/uploads/2018/01/applegate-sheetpan-breakfast-680x680.jpg" alt="Applegate Sheet Pan Breakfast with Sweet Potatoes and Bacon" />
							</a>
						</label>
				  </div>
				  <div class="col-4">
			          		<label>
								<h5>Mac + Cheese Veggie Burger with Amy&#8217;s</h5>
								<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=fro_amys_macaroni_01&catId=fro_meals_past" href="javascript:;">
									<img src="https://blog.freshdirect.com/wp-content/uploads/2018/02/home-cooked-meal_blog.png" alt="Mac + Cheese Veggie Burger with Amy&#8217;s" />
								</a>
			          		</label>
			      </div>
				  <div class="col-4">
						<label>
							<h5>Tostitos Pizza Nachos</h5>
							<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=gro_tost_rest_sty_03&catId=gro_snack_chips_corn" href="javascript:;">
								<img src="https://blog.freshdirect.com/wp-content/uploads/2017/12/big-game-tostitos-pizza-nachos-1080x1080.jpg" alt="Tostitos Pizza Nachos" />
							</a>
						</label>
					</div>
			  </div>

      		  <div class="row">
			          <div class="col-4">
			          		<label>
			          			<h5>Butternut Squash Lasagna Recipe from Amy&#8217;s</h5>
			          			<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=spe_de_cecco_laslar&catId=gro_pasta_long" href="javascript:;">
			          				<img src="https://blog.freshdirect.com/wp-content/uploads/2018/02/made-with-organic_blog.png" alt="Butternut Squash Lasagna Recipe from Amy&#8217;s" />
			          			</a>
			          		</label>
			          </div>
			          <div class="col-4">
							<label>
								<h5>Flatbread Salad with Pesto, Asparagus, and Mushrooms</h5>
								<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=veg_pid_2301848&catId=cut_veg" href="javascript:;">
									<img src="https://blog.freshdirect.com/wp-content/uploads/2018/01/Flatbread.jpeg" alt="Applegate Thai-Style Chicken and Rice Soup" />
								</a>
							</label>
					  </div>
					  <div class="col-4">
							<label>
								<h5>Applegate Thai-Style Chicken and Rice Soup</h5>
								<a data-fancybox data-type="iframe" data-src="/test/content/product_detail.jsp?productId=dai_pid_2003435&catId=dai_packa_chic" href="javascript:;">
									<img src="https://blog.freshdirect.com/wp-content/uploads/2018/01/applegate-Thai-Chicken-and-Rice-Soup-680x680.jpg" alt="Applegate Thai-Style Chicken and Rice Soup" />
								</a>
							</label>
				  	  </div>			          
      		   </div>
         </div>
         <script>
         [].forEach.call(document.getElementsByClassName("iframe-lightbox-link"), function(el) {
		   el.lightbox = new IframeLightbox(el);
		  });
         </script>
   </body>
</html>

