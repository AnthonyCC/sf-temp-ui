<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>OAS Test page</title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		
		
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
		
		
		<script type="text/javascript" src="/assets/javascript/htmlParser/htmlParser.js"></script>
		<script type="text/javascript" src="/assets/javascript/postscribe.min.js"></script>
		<script type="text/javascript">
		// jQuery used as an example of delaying until load.
		function oas_loader(afterLoad) {
			var OAS_rn = '001234567890', OAS_rns = '1234567890';
			OAS_rn = new String (Math.random()); OAS_rns = OAS_rn.substring (2, 11);
			var oasTestParams = 'CosFreeDelTest=&baf=0&brand=&camp=fallcamp2&cart=veg&cohort=C1&county=KINGS&ct=1&cti=&cts=WeeklyTest&depot=&do=346&dp=MKT0072950&dpar=n&dpas=&dpcamp=&ecp=1&ecpoc=29&ecppromo=&enteredCampn=OasDefault%2FHPFeatureTop_SweepStakes%2COasDefault%2FSpecialTopbar_SweepStakes_Week1&expd=0&hv=1&id=kitchen&lotype=h&lozn=560&lu=2&mktpro=&mzid=0000100000&nod=2014-07-24&og=&oim=0&pt=&recipe=true&ref_prog_id=&ret=&state=NY&sub=1.99&sv=1988&szid=0000200201&test=false&tofurkey=&type=home&v=4&win=2&wnbk=&zid=0000200201&zip=11231&zonelevel=true';
			var oasTestSite = 'www.freshdirect.com';
			var oasTestId = 'kitchen';
			var oasTestSpots = 'SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2';
			var oasTestURL = 'https://devpromo.freshdirect.com';
			if (afterLoad) {
				//dynamic id and spots
				oasTestURL = $jq('#oasTestURL').val();
				oasTestSite = $jq('#oasTestSite').val();
				oasTestId = $jq('#oasTestId').val();
				oasTestSpots = $jq('#oasTestSpots').val();
				oasTestParams = $jq('#oasTestParams').val();
			}
			var oasTestScript = '<script src="'+oasTestURL+'/RealMedia/ads/adstream_mjx.ads/'+oasTestSite+'/'+oasTestId+'/1'+OAS_rns+'@'+oasTestSpots+'?'+oasTestParams+'"><\/script>';
			postscribe('#oas_contents', oasTestScript, 
					{
						done: function() {
							//empty pre-existing spots (it appends)
							$jq('#oas_contents').empty();
							$jq(oasTestSpots.split(',')).each(function(i,e) {
								var cur = e.trim();
								if (cur !== '') {
									$jq('#oas_contents').append('<div style="text-align: left;">'+cur+':</div><div id="oas_'+cur+'"></div>');
									postscribe('#oas_'+cur, '<script>OAS_RICH("'+cur+'")<\/script>');
								}
							});
						}
					});
			
		}
		
		$jq(function() {
			oas_loader();
		});
		</script>
		<style>
			.testOasCont td, .testOasCont th {
				font-size: 14px;
			}
			.testOasCont th {
				padding-right: 20px;
			}
			.testOasCont textarea {
				box-sizing: border-box;
			}
		</style>
</head>
	<body>
    <div id="content">
    <center>
      <!-- content lands here -->
		<div style="margin-bottom: 10px;" class="testOasCont">
			<table>
				<tr>
					<th rowspan="3" style="font-weight: bold; width: 150px;" align="center" valign="middle">
						<div style="padding: 10px 0;">Test After Page Has Loaded</div>
						<button id="oasAjaxtest">Test</button>
					</th>
					<td style="padding: 0 5px;">Server :</td>
					<td>
						<input id="oasTestURL" value="https://devpromo.freshdirect.com" style="text-align: center; font-weight: bold; width: 250px; font-size: 12px;" />
					</td>
					<td style="padding: 0 5px;"> Site :</td>
					<td>
						<input id="oasTestSite" value="www.freshdirect.com" style="text-align: center; font-weight: bold; width: 250px; font-size: 12px;" />
					</td>
					<td style="padding: 0 5px;">Page :</td>
					<td align="right">
						<input id="oasTestId" value="fro" style="font-size: 12px; text-align: center; font-weight: bold; width: 250px;" />
					</td>
				</tr>
				<tr>
					<td style="padding: 0 5px;">Position(s) :</td>
					<td colspan="5"><textarea id="oasTestSpots" style="font-weight: bold; font-size: 12px; width: 100%; height: 2em; word-wrap: break-word;">SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2</textarea></td>
				</tr>
				<tr>
					<td style="padding: 0 5px;">Params :</td>
					<td colspan="5"><textarea id="oasTestParams" style="font-weight: bold; font-size: 12px; width: 100%; height: 7em; word-wrap: break-word;">CosFreeDelTest=&baf=0&brand=&camp=fallcamp2&cart=veg&cohort=C1&county=KINGS&ct=1&cti=&cts=WeeklyTest&depot=&do=346&dp=MKT0072950&dpar=n&dpas=&dpcamp=&ecp=1&ecpoc=29&ecppromo=&enteredCampn=OasDefault%2FHPFeatureTop_SweepStakes%2COasDefault%2FSpecialTopbar_SweepStakes_Week1&expd=0&hv=1&id=kitchen&lotype=h&lozn=560&lu=2&mktpro=&mzid=0000100000&nod=2014-07-24&og=&oim=0&pt=&recipe=true&ref_prog_id=&ret=&state=NY&sub=1.99&sv=1988&szid=0000200201&test=false&tofurkey=&type=home&v=4&win=2&wnbk=&zid=0000200201&zip=11231&zonelevel=true</textarea></td>
				</tr>
			</table>
			
			
		</div>
		<hr />
		<div style="margin-bottom: 10px;">This is some other static content</div>
		<hr />
		
		<div id="oas_contents"></div>
		
		<hr />
		<div style="margin-top: 10px;">This is also some other static content</div>
		<hr />
		
		<script type="text/javascript">
			$jq('#oasAjaxtest').click(function(event) {
				oas_loader(true);
			});
			
			$jq('#oasAjaxtest').button();
		</script>


      <!-- content ends above here-->
      </center>
    </div>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	</body>
</html> 
