<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<html>
	<head>
	<jwr:style src="/global.css"></jwr:style>
	<style>
		h1,h2,li { font-family: verdana;}
		h1,h2 { -webkit-column-span:all;column-span:all; }
		ol { margin:0; }
		li { margin-bottom:1em; }
		button { vertical-align: middle }
		section { -webkit-column-count:2; column-count:2; }
	</style>
	</head>
	<body>
	<h1>CSS Buttons</h1>
	<section>
		<h2>colors</h2>
		<ol>
			<li><button class="cssbutton green">green</button></li>
			<li><button class="cssbutton green transparent">green transparent</button></li>
			<li><button class="cssbutton orange">orange</button></li>
			<li><button class="cssbutton orange transparent">orange transparent</button></li>
			<li><button class="cssbutton red">red</button></li>
			<li><button class="cssbutton red nontransparent">red nontransparent</button></li>
			<li><button class="cssbutton purple">purple</button></li>
			<li><button class="cssbutton purple nontransparent">purple nontransparent</button></li>
			<li><button class="cssbutton blue">blue</button></li>
			<li><button class="cssbutton khaki">khaki</button></li>
			<li><button class="cssbutton white">white</button></li>
			<li><button class="cssbutton white transparent">white + transparent</button> - for icon use</li>
      <li><button class="cssbutton black">black</li>
      <li><button class="cssbutton grey">grey</li>
      <li><button class="cssbutton green" disabled>disabled (green)</li>
		</ol>
		<h2>sizes</h2>
		<ol>
			<li><button class="cssbutton orange small">small</button></li>
			<li><button class="cssbutton orange">normal</button></li>
			<li><button class="cssbutton orange medium">medium</button></li>
			<li><button class="cssbutton orange large">large</button></li>
		</ol>
	</section>
	<hr>
	<section>
		<h2>icons</h2>
		<ol>
			<li><button class="cssbutton orange icon-cart-before">cart - before</button><button class="cssbutton orange icon-cart-after">cart - after</button><button class="cssbutton orange icon-cart-before notext">cart - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-up-left-before">arrow up-left - before</button><button class="cssbutton blue icon-arrow-up-left-after">arrow up-left - after</button><button class="cssbutton blue icon-arrow-up-left-before notext">arrow up-left - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-up-before">arrow up - before</button><button class="cssbutton blue icon-arrow-up-after">arrow up - after</button><button class="cssbutton blue icon-arrow-up-before notext">arrow up - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-up-right-before">arrow up-right - before</button><button class="cssbutton blue icon-arrow-up-right-after">arrow up-right - after</button><button class="cssbutton blue icon-arrow-up-right-before notext">arrow up-right - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-right-before">arrow right - before</button><button class="cssbutton blue icon-arrow-right-after">arrow right - after</button><button class="cssbutton blue icon-arrow-right-before notext">arrow right - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-down-right-before">arrow down-right - before</button><button class="cssbutton blue icon-arrow-down-right-after">arrow down-right - after</button><button class="cssbutton blue icon-arrow-down-right-before notext">arrow down-right - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-down-before">arrow down - before</button><button class="cssbutton blue icon-arrow-down-after">arrow down - after</button><button class="cssbutton blue icon-arrow-down-before notext">arrow down - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-down-left-before">arrow down-left - before</button><button class="cssbutton blue icon-arrow-down-left-after">arrow down-left - after</button><button class="cssbutton blue icon-arrow-down-left-before notext">arrow down-left - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-left-before">arrow left - before</button><button class="cssbutton blue icon-arrow-left-after">arrow left - after</button><button class="cssbutton blue icon-arrow-left-before notext">arrow left - notext</button> - notext</li>
			<li><button class="cssbutton green icon-plus-before">plus - before</button><button class="cssbutton green icon-plus-after">plus - after</button><button class="cssbutton green icon-plus-before notext">plus - notext</button> - notext</li>
			<li><button class="cssbutton green icon-minus-before">minus - before</button><button class="cssbutton green icon-minus-after">minus - after</button><button class="cssbutton green icon-minus-before notext">minus - notext</button> - notext</li>
			<li><button class="cssbutton red icon-cancel-circle-before">cancel - before</button><button class="cssbutton red icon-cancel-circle-after">cancel - after</button><button class="cssbutton red icon-cancel-circle-before notext">cancel - notext</button> - notext</li>
			<li><button class="cssbutton red icon-remove-before">remove - before</button><button class="cssbutton red icon-remove-after">remove - after</button><button class="cssbutton red icon-remove-before notext">remove - notext</button> - notext</li>
			<li><button class="cssbutton khaki icon-warning-before">warning - before</button><button class="cssbutton khaki icon-warning-after">warning - after</button><button class="cssbutton khaki icon-warning-before notext">warning - notext</button> - notext</li>
			<li><button class="cssbutton khaki icon-info-before">info - before</button><button class="cssbutton khaki icon-info-after">info - after</button><button class="cssbutton khaki icon-info-before notext">info - notext</button> - notext</li>
			<li><button class="cssbutton khaki icon-info2-before">info2 - before</button><button class="cssbutton khaki icon-info2-after">info2 - after</button><button class="cssbutton khaki icon-info2-before notext">info2 - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-first-before">first - before</button><button class="cssbutton purple icon-first-after">first - after</button><button class="cssbutton purple icon-first-before notext">first - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-backward-before">backward - before</button><button class="cssbutton purple icon-backward-after">backward - after</button><button class="cssbutton purple icon-backward-before notext">backward - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-forward-before">forward - before</button><button class="cssbutton purple icon-forward-after">forward - after</button><button class="cssbutton purple icon-forward-before notext">forward - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-last-before">last - before</button><button class="cssbutton purple icon-last-after">last - after</button><button class="cssbutton purple icon-last-before notext">last - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-file-before">file - before</button><button class="cssbutton purple icon-file-after">file - after</button><button class="cssbutton purple icon-file-before notext">file - notext</button> - notext</li>
			<li><button class="cssbutton purple icon-file2-before">file2 - before</button><button class="cssbutton purple icon-file2-after">file2 - after</button><button class="cssbutton purple icon-file2-before notext">file2 - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-up2-before">arrow-up2 - before</button><button class="cssbutton blue icon-arrow-up2-after">arrow-up2 - after</button><button class="cssbutton blue icon-arrow-up2-before notext">arrow-up2 - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-left2-before">arrow-left2 - before</button><button class="cssbutton blue icon-arrow-left2-after">arrow-left2 - after</button><button class="cssbutton blue icon-arrow-left2-before notext">arrow-left2 - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-down2-before">arrow-down2 - before</button><button class="cssbutton blue icon-arrow-down2-after">arrow-down2 - after</button><button class="cssbutton blue icon-arrow-down2-before notext">arrow-down2 - notext</button> - notext</li>
			<li><button class="cssbutton blue icon-arrow-right2-before">arrow-right2 - before</button><button class="cssbutton blue icon-arrow-right2-after">arrow-right2 - after</button><button class="cssbutton blue icon-arrow-right2-before notext">arrow-right2 - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-reorder-icon-before">reorder-icon - before</button><button class="cssbutton blue icon-reorder-icon-after">reorder-icon - after</button><button class="cssbutton blue icon-reorder-icon-before notext">reorder-icon - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-magnifying-glass-before">magnifying-glass - before</button><button class="cssbutton blue icon-magnifying-glass-after">magnifying-glass - after</button><button class="cssbutton blue icon-magnifying-glass-before notext">magnifying-glass - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-grid-view-before">grid-view - before</button><button class="cssbutton blue icon-grid-view-after">grid-view - after</button><button class="cssbutton blue icon-grid-view-before notext">grid-view - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-list-view-before">list-view - before</button><button class="cssbutton blue icon-list-view-after">list-view - after</button><button class="cssbutton blue icon-list-view-before notext">list-view - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-trash-new-before">trash-new - before</button><button class="cssbutton blue icon-trash-new-after">trash-new - after</button><button class="cssbutton blue icon-trash-new-before notext">trash-new - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-cart-new-before">cart-new - before</button><button class="cssbutton blue icon-cart-new-after">cart-new - after</button><button class="cssbutton blue icon-cart-new-before notext">cart-new - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-list-black-before">list-black - before</button><button class="cssbutton blue icon-list-black-after">list-black - after</button><button class="cssbutton blue icon-list-black-before notext">list-black - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-list-white-before">list-white - before</button><button class="cssbutton blue icon-list-white-after">list-white - after</button><button class="cssbutton blue icon-list-white-before notext">list-white - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-cart_fast-before">cart_fast - before</button><button class="cssbutton blue icon-cart_fast-after">cart_fast - after</button><button class="cssbutton blue icon-cart_fast-before notext">cart_fast - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-trash-new2-before">trash-new2 - before</button><button class="cssbutton blue icon-trash-new2-after">trash-new2 - after</button><button class="cssbutton blue icon-trash-new2-before notext">trash-new2 - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-pencil-before">pencil - before</button><button class="cssbutton blue icon-pencil-after">pencil - after</button><button class="cssbutton blue icon-pencil-before notext">pencil - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-maps-before">maps - before</button><button class="cssbutton blue icon-maps-after">maps - after</button><button class="cssbutton blue icon-maps-before notext">maps - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-add-drawer-before">add drawer - before</button><button class="cssbutton blue icon-add-drawer-after">add drawer - after</button><button class="cssbutton blue icon-add-drawer-before notext">add drawer - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-clock-before">clock - before</button><button class="cssbutton blue icon-clock-after">clock - after</button><button class="cssbutton blue icon-clock-before notext">clock - notext</button> - notext</li>
      <li><button class="cssbutton blue icon-meal-details-before">meal details - before</button><button class="cssbutton blue icon-meal-details-after">meal details - after</button><button class="cssbutton blue icon-meal-details-before notext">meal details - notext</button> - notext</li>
      <li><button class="cssbutton orange icon-chevron-left-before">chevron left - before</button><button class="cssbutton orange icon-chevron-left-after">chevron left - after</button><button class="cssbutton orange icon-chevron-left-before notext">chevron left - notext</button> - notext</li>
      <li><button class="cssbutton orange icon-chevron-right-before">chevron right - before</button><button class="cssbutton orange icon-chevron-right-after">chevron right - after</button><button class="cssbutton orange icon-chevron-right-before notext">chevron right - notext</button> - notext</li>
      <li><button class="cssbutton orange icon-chevron-up-before">chevron up - before</button><button class="cssbutton orange icon-chevron-up-after">chevron up - after</button><button class="cssbutton orange icon-chevron-up-before notext">chevron up - notext</button> - notext</li>
      <li><button class="cssbutton orange icon-chevron-down-before">chevron down - before</button><button class="cssbutton orange icon-chevron-down-after">chevron down - after</button><button class="cssbutton orange icon-chevron-down-before notext">chevron down - notext</button> - notext</li>
		</ol>
	</section>
	</body>
</html>
