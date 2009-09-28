<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	String deptId="GC_testDept";
	String catId="GC_testCat";
	ContentNodeModel categoryRef = ContentFactory.getInstance().getContentNodeByName(catId);
	ProductModel prodRef = ContentFactory.getInstance().getProductByName(catId, "GC_testProd");
	List gcList = prodRef.getGiftcardType();
	String mediaRoot = "/media/editorial/giftcards/";
	String mediaStaticRoot = "/media_stat/images/giftcards/";
	String gcId = request.getParameter("gcId");
%>
	<fd:Department id='department' departmentId='<%= deptId %>'/>

<%
	//ContentNodeModel currentFolder = department;
%>


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

			//set container  to hold display
				$('containerTest'+i).appendChild(window[testArray[i]].setDisplayObjType(i));

			//test manually changing selected card
				if (i==0) {
					window[testArray[i]].chooseInitialCard(1);
					//setting greater than available cards
					//window[testArray[i]].chooseInitialCard(99);
				}
				if (i==1) {
					window[testArray[i]].chooseInitialCard(1);
					//setting greater than available cards
					//window[testArray[i]].chooseInitialCard(99);
				}
				if (i==2) {
					window[testArray[i]].chooseInitialCard(1);
					//setting greater than available cards
					//window[testArray[i]].chooseInitialCard(5);
				}
				if (i==3) {
					//setting greater than available cards
					window[testArray[i]].chooseInitialCard(99);
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
				window['formTester'].gcId_containerId = 'gcId';

			window['formTester'].updateDisplay();
	}
	Event.observe(window, 'load', TEST);
	
</script>
<div style="font-family: monospace;">
<b>Department:</b> <%= department.getFullName() %><br />
<b>Category:</b> <%= categoryRef.getFullName() %><br />
<b>Gift Card Types:</b> <%= prodRef.getGiftcardType() %><br />
<b>Media Root:</b> <%= mediaRoot %><br />
<b>Media Static Root:</b> <%= mediaStaticRoot %><br />
</div>

<br /><br />
<div style="font-family: monospace;">
<%
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
Gift Card alone (Choosing a card outside of available [99 of 5])
<hr />

<hr />
Gift Card test page
<div id="formTesterContainer" class="container"></div>
<div>
	<form action="postback.jsp" method="POST">
		emailPreview:	<input type="checkbox" id="isEmailPreview" name="isEmailPreview" value="true" checked="true" /><br />
		pdfPreview:	<input type="checkbox" id="isPdfPreview" name="isPdfPreview" /><br />
		gcID:			<input type="text" id="gcId" name="gcId" value="<%= gcId %>" /><br />
		gcAmount:		<input type="text" id="gcAmount" name="gcAmount" value="$50.00" /><br />
		gcRedempCode:	<input type="text" id="gcRedempCode" name="gcRedempCode" value="XXXXX123456" /><br />
		gcFor:			<input type="text" id="gcFor" name="gcFor" value="PersonB" /><br />
		gcFrom:		<input type="text" id="gcFrom" name="gcFrom" value="PersonA" /><br />
		gcMessage:		<textarea type="text" id="gcMessage" name="gcMessage">Test message</textarea><br />
		<input type="submit" value="test non-ajax" name="submit" />
		<input type="button" value="test ajax" name="submit" onclick="window['formTester'].emailPreview(); return false;" />
		<input type="button" value="test ajax recip add" name="submit" onclick="addRecip(); return false;" />
	</form>
</div>

<div style="border: 1px dashed #00c; width: 750px;"">
	The gray box below is the recipient list container. It starts empty and only creates the header if it's empty. &nbsp;&nbsp;&nbsp;
	<input type="submit" value="clear recip list" onclick="$('recipList').innerHTML='';$('recipTotal').innerHTML='';totalSoFar=0;" />
	<div id="recipList" style="border: 2px solid #ccc;"></div>
	<div id="recipTotal" class="recipTotal" style="text-align: right;"></div>
</div>


<br /><br />
<div id="testTarget" style=" width: 200px; border: 1px solid red; float: left;">
	1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ
</div>
<div id="testTarget" style="height: 100px; width: 100px; border: 1px solid red; overflow: scroll; float: left;">
	1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ
</div>
<div id="testTarget" style="height: 100px; width: 100px; border: 1px solid red; overflow: hidden; float: left;">
	1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ 1234567890 ABCDEFGHIJ KLMNOPQRST UVWXYZ
</div>

<div id="error" style="clear: both;"></div>