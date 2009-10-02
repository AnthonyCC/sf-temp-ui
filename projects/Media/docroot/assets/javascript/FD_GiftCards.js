/*	FreshDirect Giftcard Javascript Library
 *
 *	Javascript library for display of Fresh Direct Giftcards.
 *
 *	--------------------------------------------------------------------------*/


/* Test : Debug Functions ----------------------------------------------------*/

var global_gcDebug = false;
var global_gcLog = false;
var lastEdit = '2009.10.01_10.51.30.AM';
var lastLog;

gcLog('Last Edit: '+lastEdit);

function formatCurrency(num) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
	num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
	cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + '$' + num + '.' + cents);
}

function maxLen(elem, len) {
	if (elem.value.length >= len) { elem.value = (elem.value).substring(0,len); }
}

/*	ranSTR( length as STRING, charset as STRING )
 *
 *	Returns: random STRING of length(LENGTH) using chars from CHARSET
 */
function ranSTR(length, charset) {
	var randomstring = '';
	if (length <= 0 || isNaN(length))
	{
		return randomstring;
	}
	for (var i=0; i < length; i++) {
		var rnum = Math.floor(Math.random() * charset.length);
		randomstring += charset.substring(rnum,rnum+1);
	}
	return randomstring;
}

function gcLog(logMsg) {
	lastLog = logMsg;
	var time = new Date();
	var timeNow = time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
	if ((global_gcDebug||global_gcLog) && window.console) {	console.log(timeNow+' Log: '+this.lastLog); }
}

function sI(l,u){var n=(Math.floor(Math.random()*u));if (n<l){n=sI(l,u);}return n;}function dM(){return Boolean(Math.floor(Math.random()*2));}
function QA(){var d=new Date();var ran=(Math.floor(Math.random()*1000));$('gcBuyerName').value="gcBuyerName "+ran;$('gcBuyerEmail').value='gcBuyerEmail'+ran+'@freshdirect.com';$('gcBuyerName').value="gcBuyerName "+ran;if(!$('quantity')){$('gcRecipientEmail').value='gcRecipientEmail'+ran+'@freshdirect.com';$('gcRecipientName').value="gcRecipientName "+ran;$('fldMessage').value="fldMessage "+ran;}else{$('quantity').value=sI(1,100);}$('fldAmount').selectedIndex=sI(2,$('fldAmount').length);$('gcDisplaySELECT_card_select').selectedIndex=sI(0,$('gcDisplaySELECT_card_select').length);window['gcDisplay'].selectCard();(dM())?$('deliveryMethodPdf').checked=true:$('deliveryMethodEmail').checked=true;}
function showDialogs(){$$('div.gcResendBox','div.gcResendBoxContent','div.gcResendCRMCancelBox','div.gcResendCRMCancelBoxContent','div.gcResendCRMBox','div.gcResendCRMBoxContent','div.gcCheckAddressBox','div.gcCheckAddressBoxContent','div.gcCheckBalanceBox','div.gcCheckBalanceBoxContent').each(function(x){x.style.display='block';x.style.visibility='visible';x.style.clear='both';x.style.float='none';x.style.height='auto';x.style.width='auto';});}

/*---------------------------------------------------- Test : Debug Functions */


/*	DISPLAY
 *	---------------------------------*/

	/*	initGiftcardDisplay ( refId as STRING )
	 *
	 *	initiate a new Giftcard Display
	 *	
	 *	If refId is not passed, a random id is used instead in the form of:
	 *		'fdGCDisplay_#####'
	 *
	 *	Returns: TRUE
	 */
	function initGiftcardDisplay(refId){
		var myRefId;

		if (typeof(refId) != 'undefined') {
			myRefId = refId;
		}else{
			'fdGCDisplay_'+ranSTR(5,'0123456789');
		}

		window[myRefId] = new fdGiftcardDisplay(myRefId);

		return true;
	}

	/*	fdGiftcardDisplay ( refId as STRING )
	 *	
	 *	create a new Giftcard Display object
	 *
	 *	refId is REQUIRED
	 */
	function fdGiftcardDisplay(refId) {
		// debug / log vars
			this.debug = false;					// debug mode
			this.debug_alert = false;			// debug mode, use alert msgboxes
			this.debug_PreloadErrLog = false;	// log errors before load is done

			this.lastErr = '';			// last error message
			this.lastLog = '';			// last log message

		// refId
			this.refId = refId;			//STRING

		// media directories
			this.mediaRoot = '/media/editorial/giftcards/';
			this.mediaStaticRoot = '/media_stat/images/giftcards/';

		// an array of fdCard objects
			this.cards = new Array();

		// an array of the current card images displayed (l/c/r)
			this.display = new Array(-1,-1,-1);	// -1 for unintialized

		/*	Display Object type
		 *
		 *	the type of display to return pre-built (starting with index 1)
		 *		1 = Card Example with dropdown
		 *		2 = Card Example with Carousel
		 *		* = Card Example (alone)
		 */
			this.displayObjType = -1;	// -1 for unintialized

		// holds the displayObj until it's ready to be used
			this.displayObj;
		
		/* Personal Message length */
			this.personalMsgLen = 150;

		/* Left Image */
			// left image container's ID
				this.left_img_containerId = this.refId+'card_left_img';
			// left image's suffix
				this.left_img_suffix = '_l.jpg';

		/* Center Image */
			// center image container's ID
				this.center_img_containerId = this.refId+'card_center_img';
			// center image's suffix
				this.center_img_suffix = '_c.jpg';

		/* Right Image */
			// right image container's ID
				this.right_img_containerId = this.refId+'card_right_img';
			// right image's suffix
				this.right_img_suffix = '_r.jpg';

		// var to hold current selection
			this.curSelectedIndex = 0;
			this.selectBoxId = this.refId+'SELECT_card_select';
		// current card container's ID
			this.curCard_containerId = this.refId+'card_controls_curCard';

		/* Scriptaculous Effects */
			// check for scriptaculous and turn on effects
				this.useEffects = (typeof Effect=="object") ? true : false;
			//set the options for fade out-in Effect
			/*
			 *	this.eff_OpacityStart
			 *		The opacity the element starts at (FLOAT)
			 *	this.eff_OpacityMid
			 *		The opacity the element 'fades out' to (FLOAT)
			 *	this.eff_OpacityFinish
			 *		The opacity the element finishes at (FLOAT)
			 *	this.eff_OpacityDuration_FadeOut
			 *		How long it takes to fade out (FLOAT)
			 *	this.eff_OpacityDuration_FadeIn
			 *		How long it takes to fade out (FLOAT)
			 *	this.eff_OpacityDelay_FadeOut
			 *		How long to wait before fading out (FLOAT)
			 *	this.eff_OpacityDelay_FadeIn
			 *		How long to wait before fading in (FLOAT)
			 */
				this.eff_OpacityStart = 1;
				this.eff_OpacityMid = .25;
				this.eff_OpacityFinish = 1;
				this.eff_OpacityDuration_FadeOut = .3;
				this.eff_OpacityDuration_FadeIn = this.eff_OpacityDuration_FadeOut;
				this.eff_OpacityDelay_FadeOut = 0;
				this.eff_OpacityDelay_FadeIn = this.eff_OpacityDuration_FadeOut;


		// pre load images
			this.preLoadArr= new Array(); // uninitialized

		// keeps us from re-requesting images on every change
			this.loaded = 0;

		// form input element to keep updated with ID (leave blank to ignore)
			this.gcId_containerId = '';

		// this is the form this object belongs in (for display usage)
			this.partOfForm = 'giftcard_form';

		/*
		 *	addCardsArray as ARRAY
		 */
		this.addCardsArray = function (cardsArray) {
			this.log('addCardsArray called');
			
			
			for (var n = 0; n < cardsArray.length; n++) {
				this.cards[this.cards.length] = new fdCard(cardsArray[n]);
				if (!this.preLoadArr.length) { this.preLoadArr[0] = 'PRELOADER'; }

				var PL = this.preLoadArr.length-1;
					this.log('\tpreLoadArr.length INIT: '+PL);

				if (this.cards[this.cards.length-1].preLoad) {
					
					this.log('\t\tpreLoading card images: '+(n));

					PL++;
					this.preLoadArr[PL] = new Image();
					this.preLoadArr[PL].src = this.mediaRoot+this.cards[this.cards.length-1].id+this.left_img_suffix;
						this.log('\t\tpreLoadArr.length: '+PL);
						this.log('\t\tthis.preLoadArr['+PL+']: '+this.preLoadArr[PL]);
						this.log('\t\tthis.preLoadArr['+PL+'].src: '+this.preLoadArr[PL].src);
					PL++;
					this.preLoadArr[PL] = new Image();
					this.preLoadArr[PL].src = this.mediaRoot+this.cards[this.cards.length-1].id+this.center_img_suffix;
						this.log('\t\tpreLoadArr.length: '+PL);
						this.log('\t\tthis.preLoadArr['+PL+']: '+this.preLoadArr[PL]);
						this.log('\t\tthis.preLoadArr['+PL+'].src: '+this.preLoadArr[PL].src);
					PL++;
					this.preLoadArr[PL] = new Image();
					this.preLoadArr[PL].src = this.mediaRoot+this.cards[this.cards.length-1].id+this.right_img_suffix;
						this.log('\t\tpreLoadArr.length: '+PL);
						this.log('\t\tthis.preLoadArr['+PL+']: '+this.preLoadArr[PL]);
						this.log('\t\tthis.preLoadArr['+PL+'].src: '+this.preLoadArr[PL].src);

					PL--;

				}
			}

			this.log('addCardsArray pre-loads: '+(this.preLoadArr.length-1));

			this.checkDisplay();

			this.log('current cards: '+this.cards);
		}

		this.checkDisplay = function (initialCardIndex) {
			this.log('checkDisplay called');

			var initCardIndex=0;

			if (typeof(initialcardIndex) != 'undefined') {
				//initialCardIndex++;
				this.display[1] = -1;
				this.log('checkDisplay called with initialCardIndex '+initialCardIndex);
				this.log('checkDisplay called this.cards.length '+this.cards.length);
				if (initialCardIndex <= this.cards.length) {
					this.log('checkDisplay initialCardIndex <= this.cards.length true');
					initCardIndex = initialCardIndex
				}else{
					this.log('checkDisplay initialCardIndex <= this.cards.length false');
					initCardIndex = 0;
				}
				this.log('checkDisplay final initCardIndex '+initCardIndex);
			}else{
				initialcardIndex = 0;
			}
			

			if (this.display[1] == -1) {
				//uninitialized view
				if (this.cards.length >= 1)	{
					//we have at least one card to show
					this.display[1] = initCardIndex;

					this.log('initCardIndex: '+initCardIndex);
					if (this.cards.length > 1) {
						//we have two or more cards
						if (initCardIndex == this.cards.length-1) {
							this.log('\tinitCardIndex == this.cards.length');
							// init card == last card in array
							this.display[0] = initCardIndex-1;
							this.display[2] = 0;
						}else{
							this.log('\tinitCardIndex != this.cards.length');
							// check if init is 0
							if (initCardIndex == 0) {
								this.log('\t\tinitCardIndex: == 0 ('+initCardIndex+')');
								//first card
								this.display[0] = this.cards.length-1;
								this.display[2] = initCardIndex+1;
							}else{
								this.log('\t\tinitCardIndex: != 0 ('+initCardIndex+')');
								//somewhere in-between
								this.display[0] = initCardIndex-1;
								this.display[2] = initCardIndex+1;
							}
						}
					}else{
						//we have one card
						this.display[0] = 0;
						this.display[2] = 0;
					}
				}
			}

			this.updateDisplay();
		};

		this.chooseInitialCard = function(cardIndex) {
			this.selectCard(cardIndex);
			this.checkDisplay(cardIndex);
		}

		
		/* */
		this.rotate = function (direction) {
			this.log('rotate called');

			if (this.display[1] != -1 && this.cards.length > 1) {
				//this means we have items displayed
				//we only need to rotate is we have > 1 cards to show
				this.log('direction: '+direction);

				switch (direction)
				{
					case 'LEFT':
						this.display[0] = this.display[1];
						this.display[1] = this.display[2];
						if (this.display[2] == this.cards.length-1) {
							this.log('left display[2]: equals cards length');
							this.display[2] = 0;
						}else{
							this.log('left display[2]: does not equal cards length');
							this.display[2] = this.display[2]+1;
						}

						break;
					case 'RIGHT':
						this.display[2] = this.display[1];
						this.display[1] = this.display[0];
						if (this.display[0] == 0) {
							this.log('right display[0]: equals cards length');
							this.display[0] = this.cards.length-1;
						}else{
							this.log('right display[0]: does not equal cards length');
							this.display[0] = this.display[0]-1;
						}

						break;
				}

				this.log('checking effects');
				if (this.useEffects) {
					// effects in effect
					new Effect.Opacity(this.center_img_containerId, { 
						from: this.eff_OpacityStart,
						to: this.eff_OpacityMid,
						duration: this.eff_OpacityDuration_FadeOut,
						delay: this.eff_OpacityDelay_FadeOut
					});
					new Effect.Opacity(this.center_img_containerId, { 
						from: this.eff_OpacityMid,
						to: this.eff_OpacityFinish,
						duration: this.eff_OpacityDuration_FadeIn,
						delay: this.eff_OpacityDelay_FadeIn,
						refId: this.refId, //pass a ref to object
						afterSetup: 
							function (effect) { 
								window[effect.options.refId].updateDisplay();
							}
					});
				}else{
					// effects are not being used, call update
					this.updateDisplay();
				}
			}else{
				this.err('Cannot rotate ('+direction+'), no display item');
			}
		}

		/*
		 *	This function does the actual display update
		 */

		this.updateDisplay = function () {
			this.log('updateDisplay called');

			
			this.log('this.display[0] '+this.display[0]);
			this.log('this.display[1] '+this.display[1]);
			this.log('this.display[2] '+this.display[2]);

			if ($(this.left_img_containerId)) {
				$(this.left_img_containerId).src = this.mediaRoot+this.cards[this.display[0]].id+this.left_img_suffix;
				$(this.left_img_containerId).alt = this.cards[this.display[0]].displayName;
				this.log('updateDisplay: '+this.display[0]+' '+this.mediaRoot+this.cards[this.display[0]].id+this.left_img_suffix);
			}else{
				this.err('Cannot get left_img_containerId ('+this.left_img_containerId+')');
			}

			if ($(this.center_img_containerId)) {
				$(this.center_img_containerId).src = this.mediaRoot+this.cards[this.display[1]].id+this.center_img_suffix;
				$(this.center_img_containerId).alt = this.cards[this.display[1]].displayName;
				if ($(this.curCard_containerId)) {
					$(this.curCard_containerId).innerHTML = this.cards[this.display[1]].displayName; 
					this.log('updateDisplay innerHTML check ok: '+this.display[1]+' '+$(this.curCard_containerId).innerHTML);
				}else{
					this.err('updateDisplay innerHTML check ERR: '+this.curCard_containerId);
				}
				this.log('updateDisplay: '+this.display[1]+' '+this.mediaRoot+this.cards[this.display[1]].id+this.center_img_suffix);
			}else{
				this.err('Cannot get center_img_containerId ('+this.center_img_containerId+')');
			}

			if ($(this.right_img_containerId)) {
				$(this.right_img_containerId).src = this.mediaRoot+this.cards[this.display[2]].id+this.right_img_suffix;
				$(this.right_img_containerId).alt = this.cards[this.display[2]].displayName;
				this.log('updateDisplay: '+this.display[2]+' '+this.mediaRoot+this.cards[this.display[2]].id+this.right_img_suffix);
			}else{
				this.err('Cannot get right_img_containerId ('+this.right_img_containerId+')');
			}

			this.selectCard(); // call update for select box

		}

		this.selectCard = function (selIndex) {
			
			//make sure we skip out if not using a select box
			if (!$(this.selectBoxId)) { 

				//update gcId_containerId now if not a select box (otherwise wait till end of function)
				if (typeof(this.gcId_containerId) != 'undefined' && this.gcId_containerId != '' && $(this.gcId_containerId)) {
					$(this.gcId_containerId).value = this.cards[this.display[1]].id;
					this.log('gcId_containerId ('+this.gcId_containerId+') = ('+this.cards[this.display[1]].id+')');
				}else{
					this.err('Cannot set gcId_containerId ('+this.gcId_containerId+')');
					this.err('Cannot set gcId_containerId to value ('+this.cards[this.display[1]].id+')');
				}
				
				return false;
			}

			this.log('selectCard '+this.curSelectedIndex);
			var imgContainerId = this.refId+this.center_img_containerId;
			this.log('loaded? '+this.loaded);

			// see if we have a value passed (and it's a valid value)
			if (typeof(selIndex) != 'undefined' && selIndex <= this.cards.length) { $(this.selectBoxId).selectedIndex = selIndex; }

			

			// this fixes the loaded choice not matching the intended src
			if (($(this.selectBoxId).selectedIndex != this.curSelectedIndex) || this.loaded <= 1) {
				this.curSelectedIndex = $(this.selectBoxId).selectedIndex;
					this.log('selectCard inside IF '+this.curSelectedIndex);
				// change src to match selection
				$(imgContainerId).src = this.mediaRoot+$(this.selectBoxId)[this.curSelectedIndex].value+this.center_img_suffix;
				$(imgContainerId).alt = $(this.selectBoxId)[this.curSelectedIndex].innerHTML;

				if ( this.loaded <= 1 ) { this.loaded++; } // keeps us from re-requesting images on every change
			}

			//update gcId_containerId now if a select box
			if (typeof(this.gcId_containerId) != 'undefined' && this.gcId_containerId != '') {
				$(this.gcId_containerId).value = $(this.selectBoxId)[this.curSelectedIndex].value;
				this.log('2 gcId_containerId ('+this.gcId_containerId+') = ('+$(this.selectBoxId)[this.curSelectedIndex].value+')');
			}else{
				this.err('2 Cannot set gcId_containerId ('+this.gcId_containerId+')');
				this.err('2 Cannot set gcId_containerId to value ('+$(this.selectBoxId)[this.curSelectedIndex].value+')');
			}
			return true;
		}

		this.setDisplayObjType = function(type) {
			if (typeof(type) != 'undefined' && !isNaN(type)) {
				this.displayObjType = type; // if type = 0 (or not passed), this.displayObjType overrides
			}

			this.log('setDisplayObjType: Type = '+this.displayObjType);


			/* 
			 *	to allow direct appending, make sure this.displayObjType is always an object to be returned (it is by default)
			 */
			switch (this.displayObjType) {
				case 1: // Card Example with dropdown

					var optionArray = new Array;
					for (var i=this.cards.length-1; i>=0; i--) {
						optionArray[i] = Builder.node( 'option', { value: this.cards[i].id }, [ this.cards[i].displayName ]);
					}

					this.displayObj = Builder.node( 'div', { className: 'card_display' }, [
						Builder.node( 'div', { className: 'card_center' }, [
							Builder.node( 'img', { src: '', width: '332', height: '213', alt: 'c', id: this.refId+this.center_img_containerId } )
						]),
						Builder.node( 'div', { className: 'card_controls' }, [
							Builder.node( 'div', { className: 'card_controls_header' }, [
								Builder.node( 'img', { src: this.mediaStaticRoot+'purchase/choose_design.gif', width: '165', height: '16', alt: 'Choose Design', id: 'gcChooseDesign_img' } )
						
							]),
							Builder.node( 'div', { className: 'card_controls_select' }, [
								Builder.node( 'select', { id: this.selectBoxId, onChange: 'window[\''+this.refId+'\'].selectCard();', onKeyUp: 'window[\''+this.refId+'\'].selectCard();' }, [
									optionArray
								])
							])
						]),
						Builder.node( 'p', [ ] )
					]);

					break;
				case 2: // Card Example with Carousel
					this.displayObj = Builder.node( 'div', { className: 'card_display' }, [
						Builder.node( 'div', { className: 'card_left' }, [
							Builder.node( 'img', { src: '', width: '120', height: '213', alt: 'l', id: this.left_img_containerId } )
						]), 
						Builder.node( 'div', { className: 'card_center' }, [
							Builder.node( 'a', { href: '#', onClick: '$(\''+this.partOfForm+'\').submit();return false;' }, [
								Builder.node( 'img', { src: '', width: '332', height: '213', alt: 'c', id: this.center_img_containerId } )
							])
						]), 
						Builder.node( 'div', { className: 'card_right' }, [
							Builder.node( 'img', { src: '', width: '120', height: '213', alt: 'r', id: this.right_img_containerId } )
						]),

						Builder.node( 'div', { className: 'card_controls' }, [
							Builder.node('div', { className: 'card_controls_msg_type' }, [
								'Gift Card design:', Builder.node( 'span', { id: this.curCard_containerId } )
							]),

							Builder.node( 'a', { href: '#', onClick: 'window[\''+this.refId+'\'].rotate(\'LEFT\'); return false;', id: this.refId+'card_control_left' }, [
								Builder.node('img', { src: this.mediaStaticRoot+'landing/control_arrow_l.gif', width: '25', height: '29', alt: 'scroll left' } )
							]),

							Builder.node( 'a', { href: '#', onClick: 'window[\''+this.refId+'\'].rotate(\'RIGHT\'); return false;', id: this.refId+'card_control_right' }, [
								Builder.node('img', { src: this.mediaStaticRoot+'landing/control_arrow_r.gif', width: '25', height: '29', alt: 'scroll right' } )
							]),

							Builder.node( 'div', { className: 'card_controls_msg' }, [
								'Click arrows to scroll & preview card designs.'
							]),
						]),
						Builder.node( 'p', [ ] )
					]);

					break;
				default:
					this.displayObj = Builder.node( 'div', { className: 'card_display' }, [
						Builder.node( 'div', { className: 'card_center' }, [
							Builder.node( 'img', { src: '', width: '332', height: '213', alt: 'c', id: this.center_img_containerId } )
						])
					]);
			}


			return this.displayObj;

		}
		

		this.log = function (logMsg) {
			this.lastLog = logMsg;
			var time = new Date();
			var timeNow = time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
			if (this.debug) {
				if (window.console) {
					console.log(timeNow+' Log: '+this.lastLog);
				}
			}
		}

		this.err = function (errMsg) {
			this.lastErr = errMsg;
			if (this.debug) {
				if ( this.debug_PreloadErrLog && !this.loaded ) {
					if (window.console) {
						console.log('Err: '+this.lastErr);
					}
					if (this.debug_alert) { alert('Err: '+this.lastErr); }
				}
			}
		}

		this.log(this.refId + ' is using effects?: '+this.useEffects);

		/* controls values sent to trigger an email preview */
		this.emailPreview = function() {
			var titleString = ' ';

			if (this.debug) { titleString = 'debug: '+$('gcTemplateId').value; }
			var tempAmount = formatCurrency($('fldAltAmount').value);
			tempAmount=(tempAmount).replace(/\$/g, '');


			/* only trigger if we have a choice of preview type */
			if ( $('deliveryMethodEmail').checked || $('deliveryMethodPdf').checked) {
				Modalbox.show('/gift_card/postbacks/preview.jsp', {
					params: {
						isEmailPreview: $('deliveryMethodEmail').checked,
						isPdfPreview: $('deliveryMethodPdf').checked,
						gcId: $('gcTemplateId').value,
						gcAmount: tempAmount,
						gcRedempCode: 'xxxxx',
						gcFor: $('gcRecipientName').value,
						gcFrom: $('gcBuyerName').value,
						gcMessage: ($('fldMessage').value).slice(0, this.personalMsgLen)
					},
					method: 'post',
					title: titleString,
					loadingString: 'Loading Preview...',
					closeValue: '<img src="'+this.mediaRoot+this.cards[this.display[1]].id+'/close_preview.gif" />',
					closeString: 'Close Preview',
					overlayOpacity: .85,
					overlayClose: false,
					width: 700,
					height: 800,
					transitions: false,
					autoFocusing: false,
					centered:true,
					afterLoad: function() { window.scrollTo(0,0); },
					afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
				});
			}else{
				Modalbox.show('<div class="error" style="height: auto; width: auto; margin: 3px 10px;">Please choose a Delivery Method before attempting to preview.</div>', {
					title: ' ',
					loadingString: 'Loading Preview...',
					closeValue: '<img src="'+this.mediaStaticRoot+'/your_account/close.gif" height="11" width="50" alt="" border="0" />',
					closeString: 'Close Preview',
					overlayOpacity: .85,
					overlayClose: false,
					transitions: false,
					autoFocusing: false,
					height: 75,
					width: 250,
					centered: true,
					afterLoad: function() { window.scrollTo(0,0); },
					afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
				});
			}
		}
	}


	function fdCard(cardOptions) {
		this.index = cardOptions[0] || -1;
		this.id = cardOptions[1] || null;
		this.displayName = cardOptions[2] || null;
		this.preLoad = cardOptions[3] || false;
	}

/*	PREVIEW
 *	---------------------------------*/

	/* recip Preview */
	function recipPreview(id) {
		new Ajax.Request('/gift_card/postbacks/preview.jsp', {
			parameters: {
				isRecipPreview: true,
				recipId: id
			},
			onSuccess: function(transport) {
				previewShow(transport.responseText);
			}
		});
	}

	function previewShow(JSONstring) {
		var params = JSONstring.evalJSON(true);

		//we need some error checking and delivery type here

		if (params.status != "error") {

			var isEmailPreview = (params.gcDeliveryType == "E") ? true:false;
			var isPdfPreview = (params.gcDeliveryType == "P") ? true:false;
			var titleString = ' ';
			var tempAmount = formatCurrency(params.gcAmount);
			tempAmount=(tempAmount).replace(/\$/g, '');

			if (global_gcDebug) { titleString = 'debug: '+params.gcId; }
			var gcRedempCodeTemp = 'xxxxx';

			Modalbox.show('/gift_card/postbacks/preview.jsp', {
				params: {
					isEmailPreview: isEmailPreview, //delivery type here
					isPdfPreview: isPdfPreview, //delivery type here
					gcId: params.gcId,
					gcAmount: tempAmount,
					gcRedempCode: gcRedempCodeTemp,
					gcFor: params.gcFor,
					gcFrom: params.gcFrom,
					gcMessage: params.gcMessage
				},
				method: 'post',
				title: titleString,
				loadingString: 'Loading Preview...',
				closeValue: '<img src="'+params.gcMediaRoot+'/close_preview.gif" />',
				closeString: 'Close Preview',
				overlayOpacity: .85,
				overlayClose: false,
				width: 700,
				height: 800,
				transitions: false,
				autoFocusing: false,
				afterLoad: function() { window.scrollTo(0,0); },
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			})
		}
	}



/*	CHECK ADDRESS
 *	---------------------------------*/
	function checkAddrShow() {
		$('gcCheckAddressBoxMsg').innerHTML = 'Do we deliver to your recipient? If you know their address, enter it below and find out.';
			Modalbox.show($('gcCheckAddressBox'), {
				loadingString: 'Loading Window...',
				closeValue: '<img src="/media_stat/images/giftcards/your_account/close.gif" border="0" alt="" />',
				closeString: 'Close Window',
				title: '',
				overlayOpacity: .85,
				overlayClose: false,
				width: 425,
				transitions: false,
				autoFocusing: false,
				centered: true,
				afterLoad: function() { window.scrollTo(0,0); },
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			})

	}

	function checkAddress() {
		if ( $('stateNY').checked || $('stateNJ').checked) {
			var state;
			if($('stateNY').checked) {
				state = 'NY';
			} else  {
				state = 'NJ';        
			}
			new Ajax.Request('/gift_card/postbacks/checkAddress.jsp', {
				parameters: {
					address1: $('address1').value,
					address2 : $('address2').value,
					city : $('city').value,
					state: state,
					zipcode: $('zipcode').value
				},
				onSuccess: function(transport) {
					//display returned messages
					var params = transport.responseText.evalJSON(true);
					$('gcCheckAddressBoxMsg').innerHTML =  params.message;
					$('checkAddrBtnImg').src =  '/media_stat/images/giftcards/your_account/chk_another_addr_btn.gif';
					$('checkAddrBtnImg').width='145';
					$('checkAddrBtnImg').height='25';
				}
			});
		} else {
			$('gcCheckAddressBoxMsg').innerHTML =  '<b>Please enter the complete address.</b>';
			
		}
	}

/*	CHECK BALANCE
 *	---------------------------------*/

	function checkBalanceShow() {
		Modalbox.show($('gcCheckBalanceBox'), {
				loadingString: 'Loading Window...',
				closeValue: '<img src="/media_stat/images/giftcards/your_account/close.gif" border="0" alt="" />',
				closeString: 'Close Window',
				title: '',
				overlayOpacity: .85,
				overlayClose: false,
				width: 250,
				transitions: false,
				autoFocusing: false,
				centered: true,
				afterLoad: function() { window.scrollTo(0,0); },
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			})

	}

	function checkBalance() {
		new Ajax.Request('/gift_card/postbacks/gc_balance.jsp', {
				parameters: {
					givexNum: $('gcCheckBalanceBox_gcNumb').value
				},
				onSuccess: function(transport) {
					//display returned messages
					var params = transport.responseText.evalJSON(true);
					$('gcCheckBalanceBoxMsg').innerHTML =  params.message;
				}
			});
	}


/*	RESEND
 *	---------------------------------*/

	/* recip resend fetch web */
	function recipResendFetch(saleId, certNum) {
		var titleString = '';

		new Ajax.Request('/gift_card/postbacks/resend.jsp', {
			parameters: {
				isResendFetch: true,
				saleId: saleId,
				certNum: certNum
			},
			onComplete: function(transport) {
				resendShow(transport.responseText);
			}
		});

	}

	function resendShow(JSONstring) {
			
		var params = JSONstring.evalJSON(true);		
		
		if (params.status != "error") {
			//stick values into overlay html		
			$('gcResendRecipName').value = params.gcRecipName;
			$('gcResendRecipEmail').value = params.gcRecipEmail;
			$('gcResendRecipAmount').innerHTML = params.gcAmount;
			$('gcResendRecipMsg').value = params.gcMessage;
			$('gcSaleId').value = params.gcSaleId;
			$('gcCertNum').value = params.gcCertNum;

			Modalbox.show($('gcResendBox'), {
				loadingString: 'Loading Preview...',
				title: '',
				overlayOpacity: .85,
				overlayClose: false,
				width: 425,
				transitions: false,
				autoFocusing: false,
				centered: true,
				afterLoad: function() { window.scrollTo(0,0); },
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			})

		}

	}
	/* recip resend email web */
	function recipResendEmail() {
		new Ajax.Request('/gift_card/postbacks/resend.jsp', {
			parameters: {
				isResendEmail: true,
				gcRecipEmail: $('gcResendRecipEmail').value,
				gcRecipName : $('gcResendRecipName').value,
				gcMessage : $('gcResendRecipMsg').value,
				gcSaleId: $('gcSaleId').value,
				gcCertNum: $('gcCertNum').value
			},
			onSuccess: function(transport) {
				//display returned messages
				var params = transport.responseText.evalJSON(true);
				$('gcResendErr').innerHTML =  params.returnMsg;
			}
		});
	}

	/* recip resend fetch CRM */
	function recipResendFetchCRM(saleId, certNum) {
		var titleString = '';

		gcLog('recipResendFetchCRM '+saleId+' '+certNum);

		new Ajax.Request('/gift_card/postbacks/resend.jsp', {
			parameters: {
				isResendFetch: true,
				saleId: saleId,
				certNum: certNum
			},
			onSuccess: function(transport) {
				resendShowCRM(transport.responseText);
			}
		});
	}
	function resendShowCRM(JSONstring) {
		var params = JSONstring.evalJSON(true);

		if (params.status != "error") {
			//stick values into overlay html
			$('gcResendRecipName_gcResendCRMBox').value = params.gcRecipName;
			$('gcResendRecipEmail_gcResendCRMBox').value = params.gcRecipEmail;
			$('gcResendRecipAmount_gcResendCRMBox').innerHTML = params.gcAmount;
			$('gcResendRecipMsg_gcResendCRMBox').value = params.gcMessage;
			$('gcSaleId_gcResendCRMBox').value = params.gcSaleId;
			$('gcCertNumDisplay_gcResendCRMBox').innerHTML = params.gcCertNum;
			$('gcCertNum_gcResendCRMBox').value = params.gcCertNum;

			Modalbox.show($('gcResendCRMBox'), {
				loadingString: 'Loading Preview...',
				title: '',
				overlayOpacity: .85,
				overlayClose: false,
				width: 300,
				transitions: false,
				autoFocusing: false,
				centered: true,
				afterLoad: function() { window.scrollTo(0,0); },
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			})
		}
	}
	/* recip resend email CRM */
	function recipResendEmailCRM() {
		
		new Ajax.Request('/gift_card/postbacks/resend.jsp', {
			parameters: {
				isResendEmail: true,
				gcIsPurchaser: $('gcIsPurchaser_gcResendCRMBox').checked,
				gcIsLastRecip: $('gcIsLastRecip_gcResendCRMBox').checked,
				gcRecipEmail: $('gcResendRecipEmail_gcResendCRMBox').value,
				gcRecipName : $('gcResendRecipName_gcResendCRMBox').value,
				gcMessage : $('gcResendRecipMsg_gcResendCRMBox').value,
				gcSaleId: $('gcSaleId_gcResendCRMBox').value,
				gcCertNum: $('gcCertNum_gcResendCRMBox').value
			},
			onSuccess: function(transport) {
				//display returned messages
				var params = transport.responseText.evalJSON(true);
				$('gcResendCRMBoxErr').innerHTML =  params.returnMsg;
			}
		});
	}


/*	CANCEL
 *	---------------------------------*/
	function sendCancellationFetch(saleId, certificateNum, givexNum){
		
		$('gcSaleId_gcResendCRMCancelBox').value = saleId;
		$('gcCertNumDisplay_gcResendCRMCancelBox').innerHTML = certificateNum;
		$('gcCertNum_gcResendCRMCancelBox').value = certificateNum;
		$('gcGivexNum_gcResendCRMCancelBox').value = givexNum;
		
		Modalbox.show($('gcResendCRMCancelBox'), {
			loadingString: 'Loading Preview...',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 300,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})
	}

	function sendCancellation(){
		
		if(!(document.getElementById('origRecpEmail_gcResendCRMCancelBox').checked) && !(document.getElementById('selfEmail_gcResendCRMCancelBox').checked) && !(document.getElementById('newRecipient_gcResendCRMCancelBox').checked)){
			$('gcResendCRMCancelBoxErr').innerHTML = '<span class="error">Select atleast one option to send cancellation email.</span>';
				
		}else if(document.getElementById('newRecipient_gcResendCRMCancelBox').checked && (null == $('newRecpEmail_gcResendCRMCancelBox').value || $('newRecpEmail_gcResendCRMCancelBox').value.trim() == '')){
			$('sendCancelErr_gcResendCRMCancelBox').innerHTML = '<span class="error">Enter email address for New recipient.</span>';
			
		}else{			
			new Ajax.Request('/gift_card/postbacks/resend.jsp', {
				parameters: {
					isSendCancellationEmail: true,
					gcSaleId: $('gcSaleId_gcResendCRMCancelBox').value,
					gcCertNum: $('gcCertNum_gcResendCRMCancelBox').value,
					gcGivexNum: $('gcGivexNum_gcResendCRMCancelBox').value,
					origRecp: $('origRecpEmail_gcResendCRMCancelBox').checked,
					self: $('selfEmail_gcResendCRMCancelBox').checked,
					newRecipient: $('newRecipient_gcResendCRMCancelBox').checked,
					newRecipientAddr: $('newRecpEmail_gcResendCRMCancelBox').value
				   
				},
				onSuccess: function(transport) {
					//display returned messages
					var params = transport.responseText.evalJSON(true);					
					
						$('gcResendCRMCancelBoxErr').innerHTML = params.returnMsg;
						$('opStatus_gcResendCRMCancelBox').value = params.opStatus;
					
					
				}
			});
		}
	}

	function sendCancellationSuccess(certificateNum, origRecpEmail, selfEmail, newRecpEmail){
		
		
		$('origRecpEmail_gcResendCRMCancelBox').innerHTML = origRecpEmail;
		$('gcCertNum_gcResendCRMCancelBox').innerHTML = certificateNum;     
		$('selfEmail_gcResendCRMCancelBox').innerHTML = selfEmail;
		$('newRecpEmail_gcResendCRMCancelBox').innerHTML = newRecpEmail;
		
		Modalbox.show($('gcResendCRMCancelBox'), {
			loadingString: 'Loading Preview...',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 300,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})
	}

/*	STATUS CHECK
 *	---------------------------------*/


	/*
	 *	fetch status / balance of card
	 *		returns transport object
	 */
	function statusCheck(certNum, saleId) {
		if (certNum != '' && saleId != '') {
			new Ajax.Request('/gift_card/postbacks/statCheck.jsp', {
				parameters: {
					gcSaleId: saleId,
					gcCertNum: certNum
				},
				onComplete: function(transport) {
					return transport;
				}
			});
		}
	}


	var scObjArr = new Array();
	var statCheck;
	/*
	 *	pass in target id, certNum, saleId
	 */
	function statusCheckSB(targetId, certNum, saleId, type) {

		//create new scObj and push into holder array
		initScObj(targetId, certNum, saleId, type);

		//once a second is fast enough...
		statCheck = setInterval(scObjWatcher, 1000);
	}

	//helper functions to pass correct type val
	function statusCheckPHStatus(targetId, certNum, saleId){ statusCheckSB(targetId, certNum, saleId, 'status'); }
	function statusCheckPHEmail(targetId, certNum, saleId){ statusCheckSB(targetId, certNum, saleId, 'email'); }
	function statusCheckPHEmailCancel(targetId, certNum, saleId){ statusCheckSB(targetId, certNum, saleId, 'emailCancel'); }

	function initScObj(targetId, certNum, saleId, type){
		if(targetId != '' && certNum != '' && saleId != '' && type != '') {
			scObjArr.push(new scObj(targetId, certNum, saleId, type));
			return true;
		}
		return false;
	}

	function scObj(targetId, certNum, saleId, type) {
		var that=this;
		this.id = 'scObj_'+targetId+'_'+certNum+'_'+saleId;
		/* init || pending || done */
		this.status = 'init';
		this.targetId = targetId;
		this.targetId_origHTML = $(targetId).innerHTML;
		this.certNum = certNum;
		this.saleId = saleId;

		/* status || email || emailCancel */
		this.type = type;
		
		this.repText_status = 'Check Status';
		this.repText_email = 'Send Email';
		this.repText_emailCancel = 'Send Cancellation';
		this.repText_emailWEB = 'Send Email';

		this.repMsg = function(repWith) {
			if (this.type=='email') {
				if (repWith==''||typeof(repWith)=='undefined') {
					return this.repText_email;
				}else{
					return repWith;
				}
			}else if (this.type=='emailCancel') {
				if (repWith==''||typeof(repWith)=='undefined') {
					return this.repText_emailCancel;
				}else{
					return repWith;
				}
			}else if (this.type=='repText_emailWEB') {
				if (repWith==''||typeof(repWith)=='undefined') {
					return this.repText_emailWEB;
				}else{
					return repWith;
				}
			}else{
				if (repWith==''||typeof(repWith)=='undefined') {
					return this.repText_status;
				}else{
					return repWith;
				}
			}
		}

		this.typeStatusLogic = function(JSONstring) {
			var params = JSONstring.evalJSON(true);
			var stat = params.stat;
			/* tests */
				if(global_gcDebug) { stat = ranSTR(1,'NACUE'); }
				//stat = 'N';
				//stat = 'A';
				//stat = 'C';
				//stat = 'U';
				//stat = 'E';
					gcLog('debug status logic ');
					gcLog('baln '+params.baln);
					gcLog('stat '+stat);
					gcLog('code '+params.code);

			/* we should never get N in return, but jic */
			if (stat == 'N') { $(this.targetId).innerHTML = this.repMsg('No Status, retry'); }

			/* active */
			if (stat == 'A') { $(this.targetId+'cont').innerHTML = this.repMsg('Active : '+params.baln); }
			/* cancelled */
			if (stat == 'C') { 
				$(this.targetId+'cont').innerHTML = this.repMsg('Cancelled');
			}

			/* something unknown */
			if (stat == 'U') { $(this.targetId).innerHTML = this.repMsg('UNKNOWN, retry'); }
			/* an error occurred */
			if (stat == 'E') { $(this.targetId).innerHTML = this.repMsg('ERROR, retry'); }

			this.status = 'done';
		}
		
		this.typeEmailLogic = function(JSONstring) {
			var params = JSONstring.evalJSON(true);
			var stat = params.stat;
			/* tests */
				if(global_gcDebug) { stat = ranSTR(1,'NACUE'); }
				//stat = 'N';
				//stat = 'A';
				//stat = 'C';
				//stat = 'U';
				//stat = 'E';
					gcLog('debug email logic ');
					gcLog('baln '+params.baln);
					gcLog('stat '+stat);
					gcLog('code '+params.code);


			/* we should never get N in return, but jic */
			if (stat == 'N') { $(this.targetId).innerHTML = this.repMsg('No Status, retry'); }

			/* active */
			if (stat == 'A') {
				//here we should call resend
				if (this.type=='email') {
					recipResendFetchCRM(this.saleId, this.certNum);
					$(this.targetId).innerHTML = this.repMsg();
				}else if (this.type=='emailCancel') {
					$(this.targetId).innerHTML = this.repMsg('Card Active, retry');
				}else if (this.type=='emailWEB') {
					recipResendFetch(this.saleId, this.certNum);
					$(this.targetId).innerHTML = this.repMsg();
				}
			}
			/* cancelled */
			if (stat == 'C') {
				//here we should call cancel
				if (this.type=='emailCancel') {
					sendCancellationFetch(this.saleId, this.certNum);
					$(this.targetId).innerHTML = this.repMsg();
				}else if (this.type=='email') {
					$(this.targetId).innerHTML = this.repMsg('Card Cancelled, retry');
				}else if (this.type=='emailWEB') {
					//do something for cancel?
				}
			}

			/* something unknown */
			if (stat == 'U') { $(this.targetId).innerHTML = this.repMsg('UNKNOWN, retry'); }
			/* an error occurred */
			if (stat == 'E') { $(this.targetId).innerHTML = this.repMsg('ERROR, retry'); }

			this.status = 'done';
		}

		this.kickOff = function() {
			that.status = 'pending';

			new Ajax.Request('/gift_card/postbacks/statCheck.jsp', {
				parameters: {
					gcSaleId: this.saleId,
					gcCertNum: this.certNum
				},
				on500: function(){
					if (that.type == 'email' || that.type == 'emailCancel' || that.type == 'emailWEB') {
						that.typeEmailLogic($H({stat: 'E', baln: '-1', code: '-1'}).toJSON());
					}else{
						that.typeStatusLogic($H({stat: 'E', baln: '-1', code: '-1'}).toJSON());
					}
				},
				onFailure: function(){
					if (that.type == 'email' || that.type == 'emailCancel' || that.type == 'emailWEB') {
						that.typeEmailLogic($H({stat: 'E', baln: '-1', code: '-1'}).toJSON());
					}else{
						that.typeStatusLogic($H({stat: 'E', baln: '-1', code: '-1'}).toJSON());
					}
				},
				onSuccess: function(transport) {
					if (that.type == 'email' || that.type == 'emailCancel' || that.type == 'emailWEB') {
						that.typeEmailLogic(transport.responseText);
					}else{
						that.typeStatusLogic(transport.responseText);
					}
				}
			});
		}

	}

	function scObjWatcher() {
		scObjArr.each(function(arrObj){
			if (arrObj) {
				if (arrObj.status == 'done') {
					gcLog('removing '+arrObj.id);
					scObjArr.splice(scObjArr.indexOf(arrObj.id), 1);
					gcLog('scObjArr '+scObjArr.length);
				}else if (arrObj.status == 'init') {
					gcLog('checked '+arrObj.id+' : '+arrObj.status);
					//kick off stat check
					arrObj.kickOff();
				} else if (arrObj.status == 'pending') {
					($(arrObj.targetId).innerHTML == 'checking...')
						?$(arrObj.targetId).innerHTML = 'checking.'
						:($(arrObj.targetId).innerHTML == 'checking.')
							?$(arrObj.targetId).innerHTML = 'checking..'
							:$(arrObj.targetId).innerHTML = 'checking...';
				} else {
					gcLog('checked '+arrObj.id+' : '+arrObj.status);
				}
			}
		});
		if (scObjArr.length == 0) { clearInterval(statCheck); }
	}


/*	RH Contact Prefs
 *	---------------------------------*/

	function rhContactPrefsShow() {
		Modalbox.show($('rhContactPrefsBox'), {
			loadingString: 'Loading Preview...',
			closeValue: '<img src="/media_stat/images/donation/robinhood/close_x.gif" border="0" alt="" />',
			closeString: 'Close Preview',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 330,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})

	}
	/* recip resend email web */
	function rhContactPrefsSave() {
		var rhShareInfoVal = ($('rhOptOut').checked)?false:true;

		new Ajax.Request('/robin_hood/postbacks/save_contact_prefs.jsp', {
			parameters: {
				rhShareInfo: rhShareInfoVal
			},
			onSuccess: function(transport) {
				//display returned message
				$('rhContactPrefsBoxErr').innerHTML =  transport.responseText;
			}
		});
	}