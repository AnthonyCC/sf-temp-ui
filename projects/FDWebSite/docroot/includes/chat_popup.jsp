<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>

<style>
    .chat-popup{
        width: 400px;
        font-family: Verdana;
        text-align: center;
        padding: 40px 0;
    }
    .chat-cont{
        padding: 0 0 5px;
    }
    .chat-header{
        font-size: 16px;
        font-weight: bold;
        padding: 0 0 4px;
    }
    button.chat-button{
        font-size: 18px;
        font-family: Verdana;
        text-shadow: 1px 1px 0 #3f8045;
        width: 340px;
        height: 65px;
    }
    button.chat-button svg{
        vertical-align: middle;
    }
    .chat-hours{
        font-size: 14px;
        padding: 4px 0 0;
    }
    .chat-popup button.chat-message-button, 
    .chat-popup a.chat-phone-button{
        font-family: Verdana;
        font-size: 14px;
        width: 340px;
        height: 43px;
        margin: 24px 0 0;
    }
    .chat-popup button.chat-message-button svg, 
    .chat-popup button.chat-phone-button svg{
        vertical-align: middle;
    }
    .chat-help{
        padding: 30px 0 0;
    }
    .chat-help-text{
        font-size: 14px;
    }
    .chat-help-text a{
        color: #458d4e;
    }
</style>

<div class="chat-popup">
    <div class="chat-cont">
        <div class="chat-header">Chat With Our Service Team</div>
        <button class="chat-button cssbutton cssbutton-flat green nontransparent" onclick="$jq('#open_live_chat a').click();">
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="18" height="17" viewBox="0 0 18 17">
                <defs>
                    <path id="b" d="M13.24 4C15.314 4 17 5.719 17 7.833v4.334C17 14.28 15.314 16 13.24 16H9.9l-3.934 3.77a.869.869 0 0 1-.572.23.819.819 0 0 1-.316-.063.84.84 0 0 1-.501-.77V16H3.76C1.686 16 0 14.281 0 12.167V7.833C0 5.72 1.686 4 3.76 4h9.48zm2.125 8.167V7.833c0-1.198-.95-2.166-2.125-2.166H3.76c-1.175 0-2.125.968-2.125 2.166v4.334c0 1.198.95 2.166 2.125 2.166h2.452v2.98l3.034-2.98h3.994c1.175 0 2.125-.968 2.125-2.166z"/>
                    <filter id="a" width="111.8%" height="112.5%" x="-2.9%" y="-3.1%" filterUnits="objectBoundingBox">
                        <feOffset dx="1" dy="1" in="SourceAlpha" result="shadowOffsetOuter1"/>
                        <feColorMatrix in="shadowOffsetOuter1" values="0 0 0 0 0.247058824 0 0 0 0 0.501960784 0 0 0 0 0.270588235 0 0 0 1 0"/>
                    </filter>
                </defs>
                <g fill="none" fill-rule="evenodd" transform="translate(0 -4)">
                    <use fill="#000" filter="url(#a)" xlink:href="#b"/>
                    <use fill="#FFF" xlink:href="#b"/>
                </g>
            </svg>
            Open Chat
        </button>
        <div class="chat-hours">Hours: 6:30 AM to 12:30 AM</div>
    </div>
    <button class="chat-message-button cssbutton cssbutton-flat green transparent" onclick="doOverlayDialogNew('/includes/chat_message_popup.jsp')">
        <svg width="22" height="13" >
            <path fill="#458D4E" fill-rule="evenodd" d="M10.265 0L0 12.295 13.232 13l.59-3.378L22 8.04l-6.982-4.696-.711.28.125-.795L10.265 0zm.142 1.74l2.957 1.868-.066.472L4.42 8.908l5.986-7.168zm3.559 3.601l.977-.385 3.345 2.253-14.091 3.099 9.769-4.967zm-7.075 5.89l5.896-1.396-.254 1.56-5.642-.164z"/>
        </svg>
        Send a Message
    </button>
    <a class="chat-phone-button cssbutton cssbutton-flat green transparent" href="tel:+<%= user.getCustomerServiceContact() %>">
        <svg width="16" height="15">
            <path fill="#458D4E" fill-rule="evenodd" d="M4.124 15C2.392 15 1.078 13.217.592 11.55c-.234-.812-.033-1.264.737-1.599.544-.242 1.272-.577 1.908-.87.452-.201.88-.394 1.172-.527.21-.093.427-.185.678-.185.452 0 .753.302.896.444l.745.745a9.812 9.812 0 0 0 1.875-1.456 9.812 9.812 0 0 0 1.456-1.867l-.745-.753c-.56-.553-.519-.996-.268-1.566.134-.301.327-.72.536-1.172.293-.636.628-1.364.87-1.908C10.57.561 10.813 0 11.5 0c.184 0 .376.05.552.1 1.674.486 3.466 1.808 3.449 3.558-.017 1.74-1.381 4.846-3.943 7.408C9.004 13.619 5.9 14.983 4.15 15h-.026zm-2.251-3.834v.017c.36 1.222 1.297 2.478 2.251 2.478h.009c1.239-.009 4.06-1.122 6.487-3.541 2.419-2.42 3.532-5.248 3.549-6.479.008-.954-1.264-1.9-2.494-2.26h-.009c-.243.553-.577 1.28-.87 1.925-.21.444-.394.854-.528 1.155-.008.026-.016.042-.025.059 0 .008.009.017.017.025l1.431 1.431-.2.427c-.026.05-.662 1.356-1.942 2.645-1.29 1.29-2.595 1.917-2.646 1.942l-.426.21-1.44-1.44c-.008-.008-.008-.017-.017-.017-.017.009-.042.017-.067.025-.293.134-.703.327-1.155.528-.644.3-1.373.627-1.925.87z"/>
        </svg>
        <%= user.getCustomerServiceContact() %>
    </a>
    <div class="chat-help">
        <div class="chat-help-text">Want more help? <a href="/help/index.jsp?trk=gnav">Visit our Help Center</a></div>
    </div>
</div>



