<!DOCTYPE html>
<%@ page import="com.freshdirect.giftcard.ErpGiftCardUtil"%>
<%@ page import="com.freshdirect.ErpServicesProperties"%>

<%
    String result = null;
    String error = null;
    String action = (String) request.getParameter("action");
    String certificateNumber = null;
    if ("encrypt".equals(action)) {
        String givexNum = (String) request.getParameter("givexNum");
        try {
            result = ErpGiftCardUtil.encryptGivexNum(givexNum);
            certificateNumber = ErpGiftCardUtil.getCertificateNumber(givexNum);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    } else if ("decrypt".equals(action)) {
        String encryptedGivexNum = (String) request.getParameter("encryptedGivexNum");
        try {
            result = ErpGiftCardUtil.decryptGivexNum(encryptedGivexNum);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
    }
%>

<html>
<head>
<title>Givex Number Encrypt/Decrypt Page</title>
</head>
<body>
	<h2>GivexNum Encryption</h2>
	<form action="">
		<input name="givexNum" placeholder="Givex Num"> <input
			type="hidden" name="action" value="encrypt"> <input
			type="submit" value="Encrypt">
	</form>
	<h2>GivexNum Decryption</h2>
	<form action="">
		<input name="encryptedGivexNum" placeholder="Encrypted Givex Num">
		<input type="hidden" name="action" value="decrypt"> <input
			type="submit" value="Decrypt">
	</form>
	<p>
	<h2>Result</h2><%=result%>
	<h2 style="color: red"><%=error%></h2>
	</p>
	<p>
	<h2>Certificate number</h2>
	<%=certificateNumber%>
	</p>
	<h3>GivexNum Encryption Key</h3>
	<%=ErpServicesProperties.getGivexNumEncryptionKey()%>
</body>
</html>