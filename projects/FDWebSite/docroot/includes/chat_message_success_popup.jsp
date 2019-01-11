<style>
	.contact-fd-success{
        width: 400px;
        font-family: Verdana;
        padding: 30px;
        box-sizing: border-box;
    }
    .contact-fd-success-svg{
		text-align: center;
    }
    .contact-fd-success-header{
    	font-size: 22px;
		font-weight: bold;
		text-align: center;
		color: #1b1a21;
		padding: 10px;
    }
    .contact-fd-success-text{
		font-size: 16px;
		font-weight: normal;
		padding: 16px 0 24px;
		color: #1b1a21;
    }
	.contact-fd-success button{
        font-size: 18px;
        font-family: Verdana;
        text-shadow: 1px 1px 0 #3f8045;
        width: 340px;
        height: 48px;
        margin: 0;
    }
</style>
<script>
	function closeMessage(){
		$jq("#uimodal-output").dialog("close");
	}
</script>

<div class="contact-fd-success">
	<div class="contact-fd-success-svg">
		<svg xmlns="http://www.w3.org/2000/svg" width="38" height="22" viewBox="0 0 38 22">
			<path fill="#1B1A21" fill-rule="evenodd" d="M17.9 0L.5 20.814l22.43 1.193 1-5.718 13.863-2.678-11.835-7.95-1.206.474.212-1.346L17.9.001zm.24 2.945l5.014 3.162-.113.8L7.994 15.08 18.141 2.945zm6.033 6.096l1.657-.651 5.671 3.812L7.614 17.45l16.56-8.408zM12.18 19.012l9.995-2.364-.43 2.642-9.565-.278z"/>
		</svg>
	</div>
	<div class="contact-fd-success-header">Message Sent!</div>
	<div class="contact-fd-success-text">We generally respond within 1 to 3 hours during our business day.</div>
	<button class="cssbutton cssbutton-flat green" type="button" onclick="closeMessage()">Ok</button>
</div>