<!DOCTYPE html>
<html lang="en-US" prefix="og: http://ogp.me/ns#">
   <head>
      <meta charset="UTF-8" />
      <title>FreshDirect | Standalone Transactable Product Widgets.</title>
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" crossorigin="anonymous">
	  <link rel="stylesheet" href="//cdn.jsdelivr.net/gh/englishextra/iframe-lightbox@0.1.6/iframe-lightbox.min.css" crossorigin="anonymous">
	  
	  <script src="//cdn.jsdelivr.net/gh/englishextra/iframe-lightbox@0.1.6/iframe-lightbox.min.js"></script>
  	  <style>
  	  .iframe-lightbox iframe {
			display: block;
			width: 800px;
			height: 800px;
			border: 0;
			box-shadow: 0.267rem 0.267rem 0.267rem 0 rgba(3, 3, 3, 0.3);
			/*!
			 * in js: iframe onload="this.style.opacity=1;" style="opacity:0;border:none;" https://codepen.io/englishextra/pen/jmjayV
			 */
			/* opacity: 0; */
			-webkit-transition: opacity 0.2s ease;
			transition: opacity 0.2s ease;
		}
  	  
	  	img {
		  width:550px;
		  height:400px;
		}
	   </style>
  	  <script>
  	  		function createLayout(stpwData) {
  	  			var stpwContainer = document.getElementById("root");
  	  			for(var i = 0; i < stpwData.length; i++) {
	  	  			var stpwRow = document.createElement('div');
	  	  			stpwRow.className = 'row';
	  	  			stpwContainer.appendChild(stpwRow);
	  	  			
	  	  			for(var j = 0; j < stpwData[i].length; j++) {
		  	  			var stpwItem = stpwData[i][j];
		  	  			
			  	  		var stpwColumn = document.createElement('div');
			  	  		stpwColumn.className = 'col-4';
			  	  		stpwRow.appendChild(stpwColumn);
			  	  		
				  	  	var stpwColumLabel = document.createElement('label');
				  	  	stpwColumn.appendChild(stpwColumLabel);
				  	  	
				  	  	var stpwColumLabelHeader = document.createElement('h5');
				  	  	stpwColumLabelHeader.appendChild(document.createTextNode(stpwItem.title));
				  	  	stpwColumLabel.appendChild(stpwColumLabelHeader);
				  	  	
				  	  	var stpwColumnLabelAnchor = document.createElement('a');
				  	  	stpwColumnLabelAnchor.setAttribute('href','javascript:void(0);');
				  	  	stpwColumnLabelAnchor.setAttribute('class','iframe-lightbox-link');
				  	  	stpwColumnLabelAnchor.setAttribute('data-src', stpwItem.productUrl);
				  	  //	stpwColumnLabelAnchor.setAttribute('data-padding-bottom','25%');
				  	   	stpwColumLabel.appendChild(stpwColumnLabelAnchor);
				  	   	
				  	  	var stpwColumnLabelImage = document.createElement('img');
				  	  	stpwColumnLabelImage.setAttribute('src', stpwItem.image);
				  		stpwColumnLabelImage.setAttribute('alt', stpwItem.title);
				  		stpwColumnLabelAnchor.appendChild(stpwColumnLabelImage);
		  	    	}
  	  			}
  	  		}
  	  </script>
   </head>
   <body>
      <div class="container-fluid" id="root">
			  <div class="row">
				  	<div class="col-3"></div>
				  	<div class="col-6">
				  		<h2>Standalone Transactable Product Widgets</h2>
				  	</div>
				  	<div class="col-3"></div>
			  </div>				  
        </div>
      <script>
	        var data =[ 
	                 [
	                     { "title":"Applegate Sheet Pan Breakfast with Sweet Potatoes and Bacon", "productUrl":"/test/content/product_detail.jsp?productId=del_pid_3204349&catId=bcn_pckgd", "image":"https://blog.freshdirect.com/wp-content/uploads/2018/01/applegate-sheetpan-breakfast-680x680.jpg"},
	                     { "title":"Mac + Cheese Veggie Burger with Amys", "productUrl":"/test/content/product_detail.jsp?productId=fro_amys_macaroni_01&catId=fro_meals_past", "image":"https://blog.freshdirect.com/wp-content/uploads/2018/02/home-cooked-meal_blog.png"},
	                     { "title":"Tostitos Pizza Nachos", "productUrl":"/test/content/product_detail.jsp?productId=gro_tost_rest_sty_03&catId=gro_snack_chips_corn", "image":"https://blog.freshdirect.com/wp-content/uploads/2017/12/big-game-tostitos-pizza-nachos-1080x1080.jpg"}
	                 ],
	                 [
	                     { "title":"Butternut Squash Lasagna Recipe from Amys", "productUrl":"/test/content/product_detail.jsp?productId=spe_de_cecco_laslar&catId=gro_pasta_long", "image":"https://blog.freshdirect.com/wp-content/uploads/2018/02/made-with-organic_blog.png"},
	                     { "title":"Flatbread Salad with Pesto, Asparagus, and Mushrooms", "productUrl":"/test/content/product_detail.jsp?productId=veg_pid_2301848&catId=cut_veg", "image":"https://blog.freshdirect.com/wp-content/uploads/2018/01/Flatbread.jpeg"},
	                     { "title":"Applegate Thai-Style Chicken and Rice Soup", "productUrl":"/test/content/product_detail.jsp?productId=dai_pid_2003435&catId=dai_packa_chic", "image":"https://blog.freshdirect.com/wp-content/uploads/2018/01/applegate-Thai-Chicken-and-Rice-Soup-680x680.jpg"}
	                 ] 
	           ];    
	      	createLayout(data);
	      	
	      	[].forEach.call(document.getElementsByClassName("iframe-lightbox-link"), function(el) {
	      	  el.lightbox = new IframeLightbox(el);
	      	});
      </script>
   </body>
</html>

