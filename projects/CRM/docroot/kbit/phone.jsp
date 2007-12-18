<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<%--
	Require:
	isSummary boolean
	show string parameter
	section string parameter
--%>

<tmpl:insert template='/template/kbit.jsp'>

    <tmpl:put name='title' direct='true'>Help - CRM System</tmpl:put>
	
    <tmpl:put name='content' direct='true'>

<% 
String show = request.getParameter("show");
String section = request.getParameter("section");
 %>
<span class="article">
<% if ("script".equalsIgnoreCase(show)) { %>
<span class="title18"><b>Call Scripts</b></span><br><br>
<% } %>

<% if ("technique".equalsIgnoreCase(show)) { %>
<span class="title18"><b>Call Techniques</b></span><br><br>

There are many elements that make up an exceptional customer service call.  All of these elements should be incorporated into a pleasant telephone experience.  Our customers are our business and each and every one should be treated with respect and courtesy.  Customer Service Agents must gather and document all information resulting from a call to FreshDirect.  FreshDirect uses this information collected to take the necessary steps to guarantee 100% customer satisfaction.  As an Agent, you are the first person in resolving any issues with a customer.  By following procedures and guidelines, in conjunction with one's judgment, an associate is empowered to make every customer 100% satisfied.
<br><br>
It is true that you can leave a lasting impression within the first few seconds of a call.  Only you can control how you sound throughout the call.  Therefore, maintaining professionalism throughout the call not only makes you look good, but the company as well.  The best way of achieving this is to stay alert and interested in the caller.  Pay attention to their needs.  Answer questions quickly without using slang or company jargon.
<ul><li>
<b>Active Listening</b><br>Listening is an active skill, not a passive one.  Paying full attention to the customer is the key to active listening.  As a Customer Service Agent you need to show the customer that you are being attentive and that you understand what the they're saying.  You need to be patient while actively listening.  Do not interrupt the customer while he/she is speaking to you, but do give feedback to let them know that you're still listening.  Did you know that people can listen at a rate of 600 words per minute and only speak at an average of 150 words per minute?
<br><br></li>
<li><b>Adjust your Voice</b><br>Adjust your voice in the direction of your customer's voice: fast/slow, pauses/no pauses.  A powerful way to establish rapport is to adjust your voice slightly so you are speaking a little more like your customer.  Subtle is the key here.  Do not mimic; just make a small shift.
<br><br></li>
<li><b>Avoid Dead Air</b><br>There will be times that you'll be accessing information while the customer is on the line with you.  You need to avoid dead air during this time.  You can inform the customer of the steps you are performing to help them get this information.  You can also use this time to confirm information they have already given you (Keyspelling).
<br><br></li>
<li><b>Asking for Information</b><br>It is important to sound respectful when asking the customer for information.  Instead of "What's your address?" say "May I have your address please?"  This will sound more like you are asking their permission to get the information you need to help them.  It will also avoid sounding like you are giving them instructions or orders.  The key is to always sound polite and eager to help.
<br><br></li>
<li><b>Be Efficient</b><br>Take the time to learn and comprehend the processes needed to perform your job efficiently.  Familiarize yourself with all product knowledge and become comfortable using the product menu.  A well-informed Customer Service Agent promotes a professional yet friendly relationship with the customer.  Don't take shortcuts.  If you are unsure of any procedures ask your Supervisor for assistance.  
<br><br></li>
<li><b>Be Polite</b><br>Put yourself in the customer's place.  One quick way to understand a customer's position during a customer service issue is to pretend you are wearing the customer's shoes.  This could change your viewpoint and allow you to resolve the problem.
<br><br></li>
<li><b>Enunciate</b><br>Pronounce your words clearly and be sure to enunciate. If we pronounce the words we use correctly, the customer will not misinterpret the message.  If you speak clearly your customers will appreciate you and they will be able to understand what you are saying to them.  This will also avoid you having to repeat yourself to the customer.  It is against company policy to chew food or gum while on the telephone.  
<br><br></li>
<li><b>Focus</b><br>Focus on what the customer is telling you.  There is nothing more annoying to a customer than being asked a question right after they communicated that information to you.  If you feel that you do not adequately understand the nature of the call, reiterate the issue using different words and phrasing.  This will clarify the information and control the conversation to bring closure.  If the customer senses that you are truly listening to them, you will gain credibility and increase their trust in you and the company.  
<br><br></li>
<li><b>Fraternization</b><br>Fraternization with customers is strictly forbidden and is grounds for termination.
<br><br></li>
<li><b>Investigate</b><br>If you cannot find a product the customer is requesting, politely ask them if it is known by another name.  There are several different ways you can find information.  Avoid telling the customer "We don't have that."  Instead say, "Let me tell you what we DO have."
<br><br></li>
<li><b>Please and Thank You</b><br>Use "please" when you ask your customer for information.  Customers feel good when they hear this magic word.  Say "thank you" and mean it.  "Thank you" is a powerful phrase.  By simply using common pleasantries, the tone of your call can go from cold and uncaring to warm and friendly.  
<br><br></li>
<li><b>Sit up Straight</b><br>Sit up comfortably in an erect posture.  Your voice sounds best when it is coming from an erect but relaxed body.  Your voice reflects everything that is going on with you.  It is up to you to be at your best so your voice will be magnetic.
<br><br></li>
<li><b>Smile - Use a smile! :-)</b><br>Your customer can hear a smile in your voice, but keep it natural.  Between calls and working cases, think of pleasant things in your life.  The more you feel like smiling, the more effective you are in your job.  Let your smile and personality shine through on the phone and it will make the customer feel comfortable.
<br><br></li>
<li><b>Sound Enthusiastic</b><br>In business today, we all talk to machines.  Let your customers know they have a real, live person on the line.  Don't sound like a machine.  Emphasize key points.  Provide prompt, professional and pleasant customer relationships.  It is important to present yourself as knowledgeable and professional at all times.  Remember, you are the first telephonic contact at FreshDirect.  A Customer Service Agent who provides exceptional service will generate additional business!
<br><br></li>
<li><b>Use English</b><br>Do use straight English such as "yes", "all right" etc. instead of "yea", "yo", "gotchya", etc.  Being so relaxed that you fall into sloppy language may be offensive to some customers. 
<br><br></li>
<li><b>Use the Caller's Name</b><br>We all like to hear our own name used.  This adds a personal touch, but keep it natural.  We suggest using the customer's name, three times: during the introduction, the order or issue and in the closing.
<br><br></li>
<li><b>Voice Tone</b><br>Sound awake, alert and interested.  Your voice on the telephone is the first opportunity your customer has to form an opinion of you, FreshDirect and the service we provide.  
<br><br></li></ul>
<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>
					
					
<% if ("etiquette".equalsIgnoreCase(show)) { %>
<b><span class="title18">Phone Etiquette</span>
	<ol>
	<li><a href="#During" class="topic">During a Call</a></li>
	<li><a href="#Hold" class="topic">Hold Procedures</a></li>
	<li><a href="#Keyspell" class="topic">Keyspelling</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="During"></a>
	<span class="topic_header">During a Call</span><br>
	<ul>
	<li>
	<b>The use of "no" at the beginning of a response</b><br>
	This hinders communication because it implies rejection of the idea or person.  When a customer hears "no" at the beginning of a sentence, chances are, they will not be listening to the rest of that sentence.  Try your best to agree with some part of their idea.  
	<br><br></li>
	<li>
	<b>"It is against company policy, I can't do that."</b><br>
	Instead, tell the customer what you can do.  If you have a policy in place for the customer's protection let the customer know that.
	<br><br></li>
	<li>
	<b>"Hold on"</b><br>
	Especially if used at the moment you answer the telephone.  Nothing  can be more irritating to a customer.  If you must place the customer on hold, ask them if they would mind holding and then politely let them know how long they will be holding.  Always check back with the customer and thank them for holding. See <a href="#Hold">Hold Procedure</a>.
	<br><br></li>
	<li>
	<b>"You have to..."</b><br>
	This makes the customer angry because they do not like to be told what to do.  Instead, explain the procedures in place and leave the customer with the options.
	<br><br></li>
	<li>
	<b>"You should have..."</b><br>
	Placing the blame for a problem on a customer will make the customer defensive and will serve to make a bad situation worse.  
	<br><br></li>
	<li><b>Select your words carefully:</b><br>
	<b>A bad experience with one unsatisfied customer can have a negative result for the entire company.</b>
		<ul>
		<li><b><i>Use positive words.</i></b><br> 
		Positive words convey conviction and belief rather than doubt. Communicate a constructive, helpful attitude rather than suggest criticism, which puts the customer on the defensive.  Use positive phrases when speaking to the caller.  Always put a positive spin to an answer.  A great example of this is the phrase "I don't know."  While it is an honest answer, it is also the easiest answer.  Your responsibility, as a Customer Service Agent, is to always find out the answer.  A great way to put a positive spin on that phrase is "That's a great question!  May I place you on hold so that I can find the answer?"
		<br><br></li>
		<li><b><i>Use involving words.</i></b><br>
			Use words that relate to the solution to the customer's situation. Show that your customer's needs and interests are your main concern.<br><br></li>
		<li><b><i>Use affirming words.</i></b><br>
		Use words that make your explanations convincing by relating them through the experience of others.  Try to be objective rather than stating your opinion.<br><br></li>
		</ul><br><br>
	</li>
	</ul>
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Hold"></a>
	<span class="topic_header">Hold procedures</span><br><br>
	Proper hold techniques are an extremely important aspect of a call and yet are usually one of the largest areas in need of improvement.  Proper hold techniques can turn a good customer service call into an exceptional customer service call.  
	<ol>
	<li>Ask the customer for permission to place the call on hold and explain why it is necessary to do so.  Do not say "I need to put you on hold".  Say instead, "Sir/Ma'am, do you mind if I put you on hold while I find that information for you?"  This is not only showing the customer respect (because you are asking their permission), but it also confirms that it will benefit them if they hold for a moment.  
<br><br></li>
	<li>Wait for the customer to answer before you place them on hold.<br><br></li>
	<li>If the customer agrees to hold, you should thank the customer and use the hold button to place the customer on hold.<br>
		<table width="80%"  class="convo_table">
		<tr valign="top">
		<td class="field_column" width="40%">CUSTOMER SERVICE AGENT:</td>
		<td>"Sir/Ma'am, do you mind if I put you on hold while I find that information for you?"</td>
		</tr>
		<tr valign="top">
		<td class="field_column">Customer:</td>
		<td>"I don't mind holding".</td>
		</tr>
		<tr valign="top">
		<td class="field_column">CUSTOMER SERVICE AGENT:</td>
		<td>"Thank you. One moment please"</td>
		</tr>
		</table>
	<br></li>
	<li>Do not offer false information about the customer's hold time.  It is not possible for you to get any information for the customer in just a second.  Therefore, do not say "Do you mind if I put you on hold for a second?"  We all know that when a customer is put on hold, it will definitely be longer than a second before the Customer Service Agent returns on the line.  Instead say, "Do you mind holding for a moment?" or "I'll get back on the line just as soon as I can".   
<br><br></li>
	<li>Always check back with the customer to let them know your status.  Never keep the customer holding for a long period of time without reassurance from you.    "Thank you for your patience.  I am still trying to get that information for you.  Do you mind holding a moment longer?"  Again, wait for the customer's response.  
<br><br></li>
	<li>Always thank the customer for holding.  As before, this shows the customer respect.  It will make them feel that not only are we concerned with assisting them, but we are aware of their patience as well.
<br><br></li>
	<li>If the customer does not agree to hold, you should first acknowledge the customer's concern about holding.  You should then offer reassurance and continue on with the call in the same professional manner.  Keep in mind that the customer will now hear everything you say as well as your surroundings.  
<br>
	<table width="80%"  class="convo_table">
		<tr valign="top">
		<td class="field_column" width="40%">CUSTOMER SERVICE AGENT:</td>
		<td>"Sir/Ma'am do you mind holding for a moment while I get that information for you?"</td>
		</tr>
		<tr valign="top">
		<td class="field_column">Customer:</td>
		<td>"Yes I mind! Don't put me on hold.  I hate being put on hold!"</td>
		</tr>
		<tr valign="top">
		<td class="field_column">CUSTOMER SERVICE AGENT:</td>
		<td>"OK, I understand.  I will not put you on hold.  We can look for that information together."</td>
		</tr>
		</table><br></li>
	</ol>
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Keyspell"></a>
	<span class="topic_header">Keyspelling</span><br><br>
Keyspelling is verifying information by repeating the information given to you back to the customer. This includes all numbers and spelling back names and words given.  While this is helpful during order entry and customer service, it should be applied through the different calls that you take.  Keyspelling will allow you to retrieve the correct order.  This will enable you to help the caller in an efficient manner.  
<br><br><b>Note: Keyspelling is not repeating</b>, it is a confirmation of information.  Be sure that you do not sound as if you were not listening to the customer's information the first time, but instead that you are taking extra initiative to confirm this information with them.  Keyspelling should be used for names, addresses, phone numbers, email addresses, etc.*
<br><br>
<table width="80%" class="convo_table">
<tr valign="top">
<td class="field_column" width="40%">CUSTOMER SERVICE AGENT:</td>
<td>"May have your delivery address please?"</td>
</tr>
<tr>
<td  class="field_column" valign="top">Customer:</td>
<td>"My address is 100 Fulton Street."</td>
</tr>
<tr>
<td class="field_column" valign="top">CUSTOMER SERVICE AGENT:</td>
<td>"100 Fulton Street. That's F-U-L-T-O-N?"</td>
</tr>
<tr>
<td class="field_column" valign="top">Customer:</td>
<td>"That's correct."</td>
</tr>
<tr>
<td class="field_column" valign="top">CUSTOMER SERVICE AGENT:</td>
<td>" Thank you." </td>
</tr>
</table><br>
<b>*Under no circumstances should credit card information be read back to a customer. If a customer provides correct information to you it may only be confirmed or denied.</b>
<br><br>
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>


<% if ("system".equalsIgnoreCase(show)) { %>
<span class="title18"><b>ACD Phone System Procedures</b></span><br><br>
<table width="80%" class="convo_table">
<tr valign="top">
<td class="field_column" width="40%">Log In:</td>
<td>Press the In Call key<br>
Enter your personal log in ID<br>
Press #<br>
Press Not Ready key</td>
</tr>
<tr>
<td  class="field_column" valign="top">Answer a Call:</td>
<td>Press the In call key<br>
<b>*Note:</b> If you pass the In call key during a conversation, the call with disconnect</td>
</tr>
<tr>
<td class="field_column" valign="top">End a Call:</td>
<td>Press Not Ready</td>
</tr>
<tr>
<td class="field_column" valign="top">Temporarily Remove Yourself from the ACD while remaining logged in:</td>
<td>Press Not Ready key once</td>
</tr>
<tr>
<td class="field_column" valign="top">Return to Queue:</td>
<td>Press Not Ready key once</td>
</tr>
<tr>
<td class="field_column" valign="top">Log Off:</td>
<td>Press the Make Busy key twice</td>
</tr>
<tr>
<td class="field_column" valign="top">Leaving Your Desk:</td>
<td>During an active call: press the hold key<br>
To speak to a supervisor: press the not ready key<br>
When you return: press the not ready key</td>
</tr>
<tr>
<td class="field_column" valign="top">Break/Meal:</td>
<td>To start your break:<br>
		<span style="margin-left: 15px;">Press not ready key</span><br>
		<span style="margin-left: 15px;">Press code and enter the code#</span><br>
		<span style="margin-left: 15px;">Press code</span>
		<br><br>
		To end your break:<br>
		<span style="margin-left: 15px;">Press Not Ready key</span>
</td>
</tr>
<tr>
<td class="field_column" valign="top">Project:</td>
<td>
	To start your project:<br>
	<span style="margin-left: 15px;">Press Not Ready key</span><br>
	<span style="margin-left: 15px;">Press Code and enter the code #</span><br>
	<span style="margin-left: 15px;">Press Code</span><br>
	<span style="margin-left: 15px;">Press Make Busy Twice</span>
	<br><br>To end your project:
	<span style="margin-left: 15px;">Press Not Ready key</span>
</td>
</tr>
</table>
<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>


</span>

</tmpl:put>

</tmpl:insert>