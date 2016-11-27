<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='true' />
<potato:productExtra name="productExtra" productId='${param.productId}' categoryId='${param.catId}'/>
<html>
	<head>
		<meta charset="utf-8">
		<title>Product Extra Data Test Page</title>
		<style type="text/css">
			textarea.debug {
				width: 100%;
				height: 4em;
			}
			
			code.debug {
				white-space: pre-wrap;      /* CSS3 */   
				white-space: -moz-pre-wrap; /* Firefox */    
				white-space: -pre-wrap;     /* Opera <7 */   
				white-space: -o-pre-wrap;   /* Opera 7 */    
				word-wrap: break-word;      /* IE */
 			}
			
			table.example th {
				font-size: 12px;
			}
			
			.example-desc {
				background-color: #eff;
			}
			
			.example-value {
				text-align: center;
			}
		</style>
		<link rel="stylesheet" href="http://yandex.st/highlightjs/7.4/styles/default.min.css">
		<script src="http://yandex.st/highlightjs/7.4/highlight.min.js"></script>
	</head>
	<body>
		<div>User: <span>${ user.userId }</span></div>

		<textarea class="debug"><fd:ToJSON object="${productExtra}" noHeaders="true"/></textarea>

		<!-- Examples -->
		<table class="example">
			<tbody title="Product Description">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Allergens</td>
					<td class="example-value"><a href="?productId=gro_arnold_twlvgrn&catId=gro_baker_multi">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=gro_arnold_twlvgrn&catId=gro_baker_multi">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Product Description Note (Html)</td>
					<td class="example-value"><a href="?catId=grns&productId=grns_spin">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=grns&productId=grns_spin">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Product About (Html)</td>
					<td class="example-value"><a href="?catId=usq_red&productId=usq_spa_mati_elv_rio">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=usq_red&productId=usq_spa_mati_elv_rio">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Buying Guide</td>
					<td class="example-value"><a href="?catId=ptdol&productId=ptdol_ptdgrkoliv">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=ptdol&productId=ptdol_ptdgrkoliv">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Organic Claims</td>
					<td class="example-value"><a href="?productId=pr_danjou_or&catId=orgnat_fruit_apl">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=pr_danjou_or&catId=orgnat_fruit_apl">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Claims</td>
					<td class="example-value"><a href="?productId=pr_danjou_or&catId=orgnat_fruit_apl">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=pr_danjou_or&catId=orgnat_fruit_apl">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Kosher</td>
					<td class="example-value"><a href="?catId=orgnat_fruit_apl&productId=pr_danjou_or">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=orgnat_fruit_apl&productId=pr_danjou_or">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Heating Instructions</td>
					<td class="example-value"><a href="?productId=pr_danjou_or&catId=orgnat_fruit_apl">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=pr_danjou_or&catId=orgnat_fruit_apl">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Partially Frozen Seafood</td>
					<td class="example-value"><a href="?catId=scrb&productId=scrb_snwcrb_lgs">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=scrb&productId=scrb_snwcrb_lgs">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Partially Baked</td>
					<td class="example-value"><a href="?catId=whlmlt_whlmlt&productId=whlmlt_orgwhlwd">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=whlmlt_whlmlt&productId=whlmlt_orgwhlwd">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Season Text</td>
					<td class="example-value"><a href="?productId=pr_danjou_or&catId=orgnat_fruit_apl">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=pr_danjou_or&catId=orgnat_fruit_apl">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Cheese 101</td>
					<td class="example-value"><a href="?catId=ched&productId=usa_ched_xtra">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=ched&productId=usa_ched_xtra">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Brands</td>
					<td class="example-value"><a href="?productId=del_chs_ltswis_01&catId=swissmtn">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=del_chs_ltswis_01&catId=swissmtn">Product</a></td>
				</tr>
			</tbody>
			<tbody title="Nutrition">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Nutrition Info (new)</td>
					<td class="example-value"><a href="?catId=hba_drugstore_1&productId=hba_arborne_orig">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=hba_drugstore_1&productId=hba_arborne_orig">Product</a></td>
				</tr>
			</tbody>
			<tbody title="How To Cook It">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Doneness Guide</td>
					<td class="example-value"><a href="?catId=mtblk_bblk_bnlsribeye&productId=bblk_rib_bnls_opb">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=mtblk_bblk_bnlsribeye&productId=bblk_rib_bnls_opb">Product</a></td>
				</tr>

				<tr>
					<td class="example-desc">How To Cook It</td>
					<td class="example-value"><a href="?productId=brib_bnlsshrt&catId=brib">Test</a></td>
					<td class="example-value"><a href="/product.jsp?productId=brib_bnlsshrt&catId=brib">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Fresh Tips</td>
					<td class="example-value"><a href="?catId=mt_trky_brst&productId=ptrk_brstblsknls">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=mt_trky_brst&productId=ptrk_brstblsknls">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Ratings</td>
					<td class="example-value"><a href="?catId=bcbs&productId=vpak_bf_kbob">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=bcbs&productId=vpak_bf_kbob">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Usage Lists</td>
					<td class="example-value"><a href="?catId=fflt&productId=sslm_atl_8ozflt">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=fflt&productId=sslm_atl_8ozflt">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Storage Guide (Parent Category)</td>
					<td class="example-value"><a href="?catId=corn&productId=corn_yllw">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=corn&productId=corn_yllw">Product</a></td>
				</tr>
			</tbody>
			<tbody title="Ingredients">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Ingredients</td>
					<td class="example-value"><a href="?catId=dai_yogur_low&productId=dai_chbni_pna6oz">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=dai_yogur_low&productId=dai_chbni_pna6oz">Product</a></td>
				</tr>
			</tbody>
			<tbody title="Serving Suggestions">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Serving Suggestions</td>
					<td class="example-value"><a href="?catId=orgnat_veg_salad&productId=veg_earthbd_sldclm_6">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=orgnat_veg_salad&productId=veg_earthbd_sldclm_6">Product</a></td>
				</tr>
			</tbody>
			<tbody title="Origin">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Grade</td>
					<td class="example-value"><a href="?catId=bgril&productId=bstk_flet_dfat">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=bgril&productId=bstk_flet_dfat">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Source</td>
					<td class="example-value"><a href="?catId=pot_pot&productId=pot_red">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=pot_pot&productId=pot_red">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Frenching</td>
					<td class="example-value">TBD</td>
					<td class="example-value">TBD</td>
				</tr>
			</tbody>
			<tbody title="Wine">
				<tr>
					<th>Example</th>
					<th colspan="2">Links</th>
				</tr>
				<tr>
					<td class="example-desc">Wine (spirit)</td>
					<td class="example-value"><a href="?catId=usq_spirits_vodka&productId=usq_swe_abso_vod">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=usq_spirits_vodka&productId=usq_swe_abso_vod">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Wine (classification text)</td>
					<td class="example-value"><a href="?catId=usq_arg&productId=usq_arg_asti_mal_1500">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=usq_arg&productId=usq_arg_asti_mal_1500">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Wine (types and varietals)</td>
					<td class="example-value"><a href="?catId=usq_red&productId=usq_chi_alfa_res_mal">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=usq_red&productId=usq_chi_alfa_res_mal">Product</a></td>
				</tr>
				<tr>
					<td class="example-desc">Wine (ratings)</td>
					<td class="example-value"><a href="?catId=usq_spa&productId=usq_spa_bode_nid_cli">Test</a></td>
					<td class="example-value"><a href="/product.jsp?catId=usq_spa&productId=usq_spa_bode_nid_cli">Product</a></td>
				</tr>
			</tbody>
		</table>


		<!-- <script>hljs.initHighlightingOnLoad();</script> -->
	</body>
</html>