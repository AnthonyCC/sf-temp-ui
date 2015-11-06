(function(){
	var ss_obj = new Object();

	ss_obj.img_dir = '%%SERVER%%/RealMedia/ads/Creatives/OasDefault/Foodkick_main_carousel_slides';

	ss_obj.slides = [
		{"img_src":"/one.png", "caption":"<h1>Test Caption ONE</h1>"},
		{"img_src":"/two.png", "caption":"<h1>Test Caption TWO</h1>"},
		{"img_src":"/three.png", "caption":"<h1>Test Caption THREE</h1>"},
		{"img_src":"/four.png", "caption":"<h1>Test Caption FOUR</h1>"},
	]

	ss_obj.html_id = "carousel_1"; //must be existing html element with specified 'id' attribute within foodkick page

	ss_obj.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth; //not strictly required, there is default within function
	ss_obj.height = ss_obj.width / 1.4769; //not strictly required, there is default within function

	prepare_slideshow(ss_obj);
})();


	var ss_obj = new Object();

	ss_obj.img_dir = window.IMAGES_DIR+'/carousel_1';

	ss_obj.slides = [
		{"img_src":"/slide_001.jpg", "caption":"<h1>Great food for the good life, delivered fresh to your door today.</h1>"},
		{"img_src":"/slide_002.jpg", "caption":"<h1>Great food for the good life, delivered fresh to your door today.</h1>"},
		{"img_src":"/slide_003.jpg", "caption":"<h1>Great food for the good life, delivered fresh to your door today.</h1>"},
		{"img_src":"/slide_004.jpg", "caption":"<h1>Great food for the good life, delivered fresh to your door today.</h1>"},
	]

	ss_obj.html_id = "carousel_1"; //must be existing html element with specified 'id' attribute within foodkick page

	ss_obj.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth; //not strictly required, there is default within function
	ss_obj.height = ss_obj.width / 1.4769; //not strictly required, there is default within function

	prepare_slideshow(ss_obj);