<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

		<title>Gift Card Examples</title>
		

	<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/FD_GiftCards.js" type="text/javascript" language="javascript"></script>

	<link href="/assets/css/pc_ie.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/giftcards.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/modalbox.css" rel="stylesheet" type="text/css" />


 </head>

<body>

<%

	String deptId=FDStoreProperties.getGiftCardDeptId();
	String catId=FDStoreProperties.getGiftCardCatId();
	String prodId=FDStoreProperties.getGiftCardProdName();
	String fdTemplateId = request.getParameter("fdTemplateId");
	List gcList = null;
	ProductModel prodRef = null;
	boolean errorFound = false;
	String gcDisplayId = "gcDisplay"; //id of gc Display object
	String gcDisplayTemplateContainerId = "gcTemplateId";

	
	// is set (gcId is not used/invalid), this value is used.
		int gcInitialCard = 0;

	ContentNodeModel categoryRef = null;
	categoryRef = ContentFactory.getInstance().getContentNode(catId);
	System.out.println("catId "+catId);
	if (categoryRef!=null) {
		System.out.println("categoryRef "+categoryRef);
		prodRef = ContentFactory.getInstance().getProductByName(catId, prodId);
		if (prodRef!=null) {
			gcList = prodRef.getGiftcardType();
		}else{
			errorFound = true;
			%><div class="error title18">prodId: <%=prodId%> not found (required).</div><%
		}
	}else{
		errorFound = true;
		%><div class="error title18">catId: <%=catId%> not found (required).</div><%
	}
	System.out.println("errorFound 1 "+errorFound);
	String mediaRoot = FDStoreProperties.getMediaGiftCardTemplatePath();
	String mediaStaticRoot = "/media_stat/images/giftcards/";
	String gcId = request.getParameter("gcId");
%>
	<fd:Department id='department' departmentId='<%= deptId %>'/>

<%
	//ContentNodeModel currentFolder = department;
%>

	
<script type="text/javascript">
	function TEST() {
		TEST1();
		TEST2();
	}
	
	function TEST1() {
		
		var cardArr = new Array;
		var testArray = new Array;

		testArray = Array('testMyCar0','testMyCar1','testMyCar2','testMyCar3');
		//testArray = Array('testMyCar0','testMyCar1');

		//test various array lengths
			//carArr = Array('GC_001');
			//carArr = Array('GC_001','GC_002');
			//carArr = Array('GC_001','GC_002','GC_003');
		<%
			String test = "";
			if (gcList != null && gcList.size()>0) {
				System.out.println("gcList.size() "+gcList.size());
				for(int i=0;i<gcList.size();i++) {
					DomainValue gcType=(DomainValue)gcList.get(i);
					%>
					cardArr[<%=i%>]=Array(
										<%=i%>,						// fdCard.index
										'<%= gcType.getID() %>',	// fdCard.id
										URLDecode('<%= URLEncoder.encode(gcType.getLabel()) %>'),	// fdCard.displayName
										true						// fdCard.preLoad
									);
						<%
						//if this is the id we've passed, make it the initial card
						if ((gcType.getID()).equals(fdTemplateId)) {
							gcInitialCard = i;
						}%>
					if (window.console) { console.log(cardArr[<%=i%>]); }
					<%
				}
			}else{
				errorFound = true;
			}
		%>
		if (window.console) { console.log(cardArr); }

		for (var i=0; i < testArray.length; i++) {

			//test with id
				initGiftcardDisplay(testArray[i]);
				
			//media root
				window[testArray[i]].mediaRoot = '<%= mediaRoot %>';
				window[testArray[i]].mediaStaticRoot = '<%= mediaStaticRoot %>';

			//set suffixes
				window[testArray[i]].left_img_suffix = '/car_l.jpg';
				window[testArray[i]].center_img_suffix = '/car_c.jpg';
				window[testArray[i]].right_img_suffix = '/car_r.jpg';

			//send test card array to test carousel
				window[testArray[i]].addCardsArray(cardArr);
			//set container ID for the giftcard ID
				window[testArray[i]].gcId_containerId = "<%= gcDisplayTemplateContainerId%>"+"_0"+i;

			//set container  to hold display
				$('containerTest'+i).appendChild(window[testArray[i]].setDisplayObjType(i));

			//test manually changing selected card
				if (i==0) {
					window[testArray[i]].chooseInitialCard(1);
				}
				if (i==1) {
					window[testArray[i]].chooseInitialCard(1);
				}
				if (i==2) {
					window[testArray[i]].chooseInitialCard(1);
				}
				if (i==3) {
					//setting greater than available cards
					window[testArray[i]].chooseInitialCard(<%=(gcList != null && gcList.size()>0)?gcList.size()+10:0%>);
				}

				window[testArray[i]].updateDisplay();
			
		}


		//test with nopassed id
			//initCarousel();


			//carArr = Array(<%=test%>);
			//carArr = Array('GC_001','GC_002','GC_003','GC_004','GC_101','GC_102','GC_103','GC_104');



	}
	function TEST2() {
		var cardArr = new Array;
		var testArray = new Array;

		<%
			if (gcList != null && gcList.size()>0) {
				System.out.println("gcList.size() "+gcList.size());
					for(int i=0;i<gcList.size();i++) {
						DomainValue gcType=(DomainValue)gcList.get(i);
						%>
						cardArr[<%=i%>]=Array(
											<%=i%>,						// fdCard.index
											'<%= gcType.getID() %>',	// fdCard.id
											'<%= gcType.getLabel() %>',	// fdCard.displayName
											true						// fdCard.preLoad
										);		
						if (window.console) { console.log(cardArr[<%=i%>]); }
						<%
					}
			}
		%>
		
		initGiftcardDisplay('formTester');
			//media root
				window['formTester'].mediaRoot = '<%= mediaRoot %>';
				window['formTester'].mediaStaticRoot = '<%= mediaStaticRoot %>';

			//set suffixes
				window['formTester'].left_img_suffix = '/car_l.jpg';
				window['formTester'].center_img_suffix = '/car_c.jpg';
				window['formTester'].right_img_suffix = '/car_r.jpg';
			//send test card array to test carousel
				window['formTester'].addCardsArray(cardArr);
			//set container  to hold display
				$('formTesterContainer').appendChild(window['formTester'].setDisplayObjType(1));

			//set container ID for the giftcard ID (leave blank to test without)
				window['formTester'].gcId_containerId = 'gcTemplateId';

			window['formTester'].updateDisplay();
	}
	Event.observe(window, 'load', TEST);
	
</script>
<div style="font-family: monospace;">
<b>Department:</b> <%= department.getFullName() %><br />
<b>Category:</b> <%= (errorFound)?"":categoryRef.getFullName() %><br />
<b>Gift Card Types:</b> <%= (errorFound)?"":prodRef.getGiftcardType() %><br />
<b>Media Root:</b> <%= mediaRoot %><br />
<b>Media Static Root:</b> <%= mediaStaticRoot %><br />
</div>

<br /><br />
<div style="font-family: monospace;">
<%
	if (gcList != null && gcList.size()>0) {
		System.out.println("gcList.size() "+gcList.size());
		for(int i=0;i<gcList.size();i++){
			DomainValue gcType=(DomainValue)gcList.get(i);
			%>
			<div style="float: left;">
			<b>ID:</b> <%= gcType.getID() %><br />
			<b>Label:</b> <%= gcType.getLabel() %><br />
			<b>(Not) Value:</b> <%= gcType.getValue() %><br />
			<b>Value:</b> <%= gcType.getTheValue() %><br />
			<img src="<%= mediaRoot %><%= gcType.getID() %>/car_c.jpg" />
			</div>
			<%
		}
	}
%> 
</div>
<p style="clear: left;" />

<hr /><hr />


<div id="containerTest0" class="container"></div>
Gift Card alone (Choosing second card)
<hr />
<div id="containerTest1" class="container"></div>
	<div class="container">
		<div class="card_controls_footer">Enter the information at left and select one of our five dazzling designs above. When you click 'Add to Recipient List' we'll save your card choice for this giftee.</div>
	</div>
Gift Card with select (Choosing second card)
<hr />
<div id="containerTest2" class="container"></div>
	<div class="container">
		<div class="card_shopNow">
			<a href="#" onClick="alert('Show Now clicked.'); return false;" id="card_shopNow">
				<img src="<%=mediaStaticRoot%>landing/shop_now_btn.gif" width="118" height="34" alt="Shop Now" />
			</a>
		</div>

		<div class="card_TOC">
			<span>Purchasing a Gift Card inidcates agreement to our Gift Card <a href="href: '#" onClick="alert('TOC clicked'); return false;">Terms and Conditions</a>. Click for details.
			</span>
		</div>
	</div>
Gift Card with carousel (Choosing second card)
<hr />
<div id="containerTest3" class="container"></div>
Gift Card alone (Choosing a card outside of available [<%if (gcList != null && gcList.size()>0) { %><%= gcList.size()+10 %><%}%> of <%if (gcList != null && gcList.size()>0) { %><%=gcList.size()%><%}else{%>null<%}%>])
<hr />

<hr />
Gift Card test page
<div id="formTesterContainer" class="container"></div>
<div>
	<form action="pdf_generator.jsp" method="POST">
		emailPreview:	<input type="radio" id="deliveryMethodEmail" name="deliveryMethod" value="true" checked="true" /><br />
		pdfPreview:	<input type="radio" id="deliveryMethodPdf" name="deliveryMethod" /><br />
		gcID:			<input type="text" id="gcTemplateId" name="gcTemplateId" value="<%= gcId %>" style="width: 200px;" /><br />
		gcAmount:		<input type="text" id="fldAltAmount" name="fldAltAmount" value="$50.00" style="width: 100px;" /><br />
		gcRedempCode:	<input type="text" id="gcRedempCode" name="gcRedempCode" value="XXXXX123456" style="width: 100px;" /><br />
		gcFor:			<input type="text" id="gcRecipientName" name="gcRecipientName" value="PersonB" style="width: 200px;" /><br />
		gcFrom:		<input type="text" id="gcBuyerName" name="gcBuyerName" value="PersonA" style="width: 200px;" /><br />
		gcMessage:		<textarea type="text" id="fldMessage" name="fldMessage" onkeyup="charCounter();" style="width: 300px; height: 100px;">Test message</textarea>&nbsp;<span id="msgLength"></span><br />
		<input type="button" value="randomize" name="submit" onclick="gcTest(); return false;" />
		<input type="button" value="preview" name="submit" onclick="window['formTester'].emailPreview(); return false;" />
		<input type="submit" value="save pdf" name="submit" />
	</form>
</div>

<script type="text/javascript">
<!--

	function gcTest(){
		var d=new Date();
		var ran=(Math.floor(Math.random()*1000));
		$('gcBuyerName').value="gcBuyerName "+ran;
		$('gcRecipientName').value="gcRecipientName "+ran;
		$('fldMessage').value='Random';
		var m=sI(1,20);
		for (var i=0; i<m; i++) {
			$('fldMessage').value+=" Text"+ran;
			$('msgLength').innerHTML=($('fldMessage').value).length+' chars';
		}
		$('fldAltAmount').value=sI(1,5000);
		$('gcRedempCode').value=(dM())?'XXXXXX'+sI(100000,900000):sI(100000000000,999999999999);
		$('formTesterSELECT_card_select').selectedIndex=sI(0,$('formTesterSELECT_card_select').length);
		window['formTester'].selectCard();
		(dM())?$('deliveryMethodPdf').checked=true:$('deliveryMethodEmail').checked=true;
		charCounter();
	}
	function charCounter() {
		if (($('fldMessage').value).length<150){
			$('msgLength').innerHTML=(150-($('fldMessage').value).length)+' chars left';
		}else{
			$('msgLength').innerHTML='0 chars left ('+($('fldMessage').value).length+' chars)';
		}
	}
	charCounter();
//-->
</script>


<div id="error" style="clear: both;"></div>
