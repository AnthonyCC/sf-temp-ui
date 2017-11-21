<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey'%>
<%@ page import='com.freshdirect.payment.gateway.*'%>
<%@ page import='com.freshdirect.payment.gateway.impl.*'%>
<%@ page import='com.freshdirect.payment.gateway.test.*'%>
<%@page import='java.text.*'%>
<%@page import='java.util.*'%>

<%
		String pId = NVL.apply(request.getParameter("pId"), "");
		String action=NVL.apply(request.getParameter("action"), "");
		String customerName=NVL.apply(request.getParameter("userProfile.customerName"), "");
		String addrLine1=NVL.apply(request.getParameter("userProfile.streetAddress1"), "");
		String addrLine2=NVL.apply(request.getParameter("userProfile.streetAddress2"), "");
		String city=NVL.apply(request.getParameter("userProfile.city"), "");
		String state=NVL.apply(request.getParameter("userProfile.state"), "");
		String zip=NVL.apply(request.getParameter("userProfile.zip"), "");//
		String bankAccountNum=NVL.apply(request.getParameter("userProfile.depositAccount"), "");
		String routingNum=NVL.apply(request.getParameter("userProfile.bankRoutingNumber"), "");
		String eCheckAccountType=NVL.apply(request.getParameter("userProfile.ecpAccountType"), "");
		String profileID=NVL.apply(request.getParameter("userProfile.profileID"), "");
		String ccAccountNum=NVL.apply(request.getParameter("userProfile.ccAccountNum1"), "")+
						NVL.apply(request.getParameter("userProfile.ccAccountNum2"), "")+
						NVL.apply(request.getParameter("userProfile.ccAccountNum3"), "")+
						NVL.apply(request.getParameter("userProfile.ccAccountNum4"), "");
		String month=NVL.apply(request.getParameter("userProfile.expDateMonth"), "");
		String year=NVL.apply(request.getParameter("userProfile.expDateYear"), "");
		String ccType=NVL.apply(request.getParameter("userProfile.ccType"), "");
		String cvv=NVL.apply(request.getParameter("userProfile.cvv"), "");
		String merchant=NVL.apply(request.getParameter("userProfile.merchant"), "");
		String amount=NVL.apply(request.getParameter("userProfile.amount"), "");
		String orderID=NVL.apply(request.getParameter("userProfile.orderID"), "");
		String txRefNum=NVL.apply(request.getParameter("userProfile.txRefNum"), "");
		String txRefIndex=NVL.apply(request.getParameter("userProfile.txRefIdx"), "0");
		System.out.println("action: "+action);
		
%>
<%
PaymentMethod pm=null;
	//if (!"".equals(pId)) {
		Paymentech g=new Paymentech();
		Request _request=null;
		Response _response=null;
		if("getProfile".equals(action)) {
			_request=RequestFactory.getRequest(TransactionType.GET_PROFILE);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			cc.setBillingProfileID(pId);
			BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,cc);
			_request.setBillingInfo(billinginfo);
			_response=g.getProfile(_request);
		} if("deleteProfile".equals(action)) {
			_request=RequestFactory.getRequest(TransactionType.DELETE_PROFILE);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			cc.setBillingProfileID(pId);
			BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,cc);
			_request.setBillingInfo(billinginfo);
			_response=g.deleteProfile(_request);
		} else  if("authorize".equals(action)) {
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			if(!"".equals(profileID)) {
				_request=RequestFactory.getRequest(TransactionType.AUTHORIZE);
				cc.setBillingProfileID(profileID);
               // cc.setType(PaymentMethodType.CREDIT_CARD);
                BillingInfo billinginfo=null;
                if(Merchant.USQ.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.USQ,cc);
                else if(Merchant.FDX.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,cc);
                else 
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,cc);
                billinginfo.setTransactionID(orderID);
                billinginfo.setAmount(Double.parseDouble(amount));
                _request.setBillingInfo(billinginfo);
                _response=g.authorize(_request);
			}
				
		} else if("authorizeEC".equals(action)) {
			ECheck ec=PaymentMethodFactory.getECheck();
			if(!"".equals(profileID)) {
			_request=RequestFactory.getRequest(TransactionType.AUTHORIZE);
			ec.setBillingProfileID(profileID);
			BillingInfo billinginfo=null;
            if(Merchant.USQ.name().equals(merchant))
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.USQ,ec);
            else if(Merchant.FDX.name().equals(merchant))
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,ec);
            else 
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,ec);
          
			billinginfo.setTransactionID(orderID);
			billinginfo.setAmount(Double.parseDouble(amount));
			_request.setBillingInfo(billinginfo);
			_response=g.authorize(_request);
			}
		}else  if("cashback".equals(action)) {
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			if(!"".equals(profileID)) {
				_request=RequestFactory.getRequest(TransactionType.CASHBACK);
				cc.setBillingProfileID(profileID);
               // cc.setType(PaymentMethodType.CREDIT_CARD);
                BillingInfo billinginfo=null;
                if(Merchant.USQ.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.USQ,cc);
                else if(Merchant.FDX.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,cc);
                else 
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,cc);
                billinginfo.setTransactionID(orderID);
                billinginfo.setAmount(Double.parseDouble(amount));
                _request.setBillingInfo(billinginfo);
                _response=g.issueCashback(_request);
			}
				
		}
		else if("addEditProfileEC".equals(action)) {
			if(!"".equals(profileID)) 
				_request=RequestFactory.getRequest(TransactionType.UPDATE_PROFILE);
			
			else
				_request=RequestFactory.getRequest(TransactionType.ADD_PROFILE);
			 
			ECheck ec=PaymentMethodFactory.getECheck();
			if(!"".equals(profileID)) 
				ec.setBillingProfileID(profileID);
			ec.setRoutingNumber(routingNum);
			if("C".equals(eCheckAccountType))
				ec.setBankAccountType(BankAccountType.CHECKING);
			else 	
				ec.setBankAccountType(BankAccountType.SAVINGS);
                    // ec.setType(PaymentMethodType.ECHECK);
			ec.setAccountNumber(bankAccountNum);
			ec.setCustomerName(customerName);
			ec.setAddressLine1(addrLine1);
			ec.setAddressLine2(addrLine2);
			ec.setCity(city);
			ec.setState(state);
			ec.setZipCode(zip);
			ec.setCountry("US");
			BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,ec);
			_request.setBillingInfo(billinginfo);
			if(!"".equals(profileID))
				_response=g.updateProfile(_request);
			else
				_response=g.addProfile(_request);
			
		}else if("addEditProfileCC".equals(action)||"verifyCC".equals(action)) {
			String orderId="";
		       if("verifyCC".equals(action)) {
				Random randomGenerator = new Random();
				orderId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();
				_request=RequestFactory.getRequest(TransactionType.CC_VERIFY);
		       } else {
				if(!"".equals(profileID)) 
					_request=RequestFactory.getRequest(TransactionType.UPDATE_PROFILE);
				
				else
					_request=RequestFactory.getRequest(TransactionType.ADD_PROFILE);
			}
			 
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			if(!"".equals(profileID)) 
				cc.setBillingProfileID(profileID);
                    // cc.setType(PaymentMethodType.CREDIT_CARD);
			cc.setAccountNumber(ccAccountNum);
			cc.setCustomerName(customerName);
			cc.setAddressLine1(addrLine1);
			cc.setAddressLine2(addrLine2);
			cc.setCity(city);
			cc.setState(state);
			cc.setZipCode(zip);
			cc.setCountry("US");
			if(!"".equals(cvv)) {
				cc.setCVV(cvv);
			}
			SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
	             Date date = sf.parse(month.trim()+year.trim(), new ParsePosition(0));
		      Calendar expCal = new GregorianCalendar();
	             expCal.setTime(date);
	             expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
		      cc.setExpirationDate(expCal.getTime());
		      if("AMEX".equals(ccType))
				cc.setCreditCardType(CreditCardType.AMEX);
			else  if("VISA".equals(ccType))
				cc.setCreditCardType(CreditCardType.VISA);	
			else  if("DISCOVER".equals(ccType))
				cc.setCreditCardType(CreditCardType.DISCOVER);
			else  if("MASTER_CARD".equals(ccType))
				cc.setCreditCardType(CreditCardType.MASTERCARD);	
				
			BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,cc);
			_request.setBillingInfo(billinginfo);
			if("verifyCC".equals(action)) {
			        billinginfo.setTransactionID(orderId);
				_response=g.verify(_request);
			} else {
				if(!"".equals(profileID))
					_response=g.updateProfile(_request);
				else
					_response=g.addProfile(_request);
			}
			
		}else  if("capture".equals(action)) {
			
			
				_request=RequestFactory.getRequest(TransactionType.CAPTURE);
                BillingInfo billinginfo=null;
                if(Merchant.USQ.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.USQ,PaymentMethodFactory.getCreditCard());
                else if(Merchant.FDX.name().equals(merchant))
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,PaymentMethodFactory.getCreditCard());
                else 
                	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,PaymentMethodFactory.getCreditCard());
                billinginfo.setTransactionID(orderID);
                billinginfo.setAmount(Double.parseDouble(amount));
                billinginfo.setTransactionRef(txRefNum);
                _request.setBillingInfo(billinginfo);
                _response=g.capture(_request);
			
				
		}else  if("voidCapture".equals(action)||"reverseAuthorize".equals(action)) {
			
			if("voidCapture".equals(action))
				_request=RequestFactory.getRequest(TransactionType.VOID_CAPTURE);
			else 
				_request=RequestFactory.getRequest(TransactionType.REVERSE_AUTHORIZE);
            BillingInfo billinginfo=null;
            if(Merchant.USQ.name().equals(merchant))
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.USQ,PaymentMethodFactory.getCreditCard());
            else if(Merchant.FDX.name().equals(merchant))
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FDX,PaymentMethodFactory.getCreditCard());
            else 
            	billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,PaymentMethodFactory.getCreditCard());
            billinginfo.setTransactionID(orderID);
            billinginfo.setTransactionRefIndex(txRefIndex);
            billinginfo.setTransactionRef(txRefNum);
            billinginfo.setAmount(Double.parseDouble(amount));
            _request.setBillingInfo(billinginfo);
            if("voidCapture".equals(action))
               _response=g.voidCapture(_request);
            else 
            	_response=g.reverseAuthorize(_request);
			
	} else if("verifyEC".equals(action)) {
			_request=RequestFactory.getRequest(TransactionType.ACH_VERIFY);
			 Random randomGenerator = new Random();
			String orderId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();
			ECheck ec=PaymentMethodFactory.getECheck();
			if(!"".equals(profileID)) 
				ec.setBillingProfileID(profileID);
			ec.setRoutingNumber(routingNum);
			if("C".equals(eCheckAccountType))
				ec.setBankAccountType(BankAccountType.CHECKING);
			else 	
				ec.setBankAccountType(BankAccountType.SAVINGS);
                    //ec.setType(PaymentMethodType.ECHECK);
			ec.setAccountNumber(bankAccountNum);
			ec.setCustomerName(customerName);
			ec.setAddressLine1(addrLine1);
			ec.setAddressLine2(addrLine2);
			ec.setCity(city);
			ec.setState(state);
			ec.setZipCode(zip);
			ec.setCountry("US");
			BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,ec);
			billinginfo.setTransactionID(orderId);
			_request.setBillingInfo(billinginfo);
			_response=g.verify(_request);
			
		}
	      
	      out.println("<b>Transaction Type :</b>"+_request.getTransactionType()+"<br/>");
	      if(_response.isRequestProcessed()) {
			pm=_response.getBillingInfo()!=null?_response.getBillingInfo().getPaymentMethod():null;
			out.println("<b>Is Sucess :</b>"+_response.isSuccess()+"<br/>");
			
			out.println("<b>Status Message :</b>"+_response.getStatusMessage()+"<br/>");
			if(pm!=null) {
				out.println("<b>Profile ID :</b>"+pm!=null?pm.getBillingProfileID():""+"<br/>");
				out.println("<b>Customer Name :</b>"+pm!=null?pm.getCustomerName():""+"<br/>");
				out.println("<b>Address Line1 :</b>"+pm.getAddressLine1()+"<br/>");
				out.println("<b>Address Line2 :</b>"+pm.getAddressLine2()+"<br/>");
				out.println("<b>City :</b>"+pm.getCity()+"<br/>");
				out.println("<b>State :</b>"+pm.getState()+"<br/>");
				out.println("<b>Zip :</b>"+pm.getZipCode()+"<br/>");
				out.println("<b>Account Type  :</b>"+pm.getType()+"<br/><br/>");
				if (PaymentMethodType.ECHECK.equals(pm.getType())) {
				     ECheck ec=(ECheck)pm;
				     out.println("<b>ECheck Account Number  :</b>"+ec.getAccountNumber()+"<br/>");
				     out.println("<b>ECheck Routing Number  :</b>"+ec.getRoutingNumber()+"<br/>");
				     out.println("<b>ECheck AccountType  :</b>"+ec.getBankAccountType()+"<br/>");
				} else {
				     CreditCard cc=(CreditCard)pm;
				     out.println("<b>Account Number  :</b>"+cc.getAccountNumber()+"<br/>");
				     out.println("<b>Card Type:</b>"+cc.getCreditCardType()+"<br/>");
				}
			}
			if("verifyCC".equals(action)|| "authorize".equals(action)||"capture".equals(action)||
				"voidCapture".equals(action)|| "reverseAuthorize".equals(action)	
			   ){
				out.println("<b>Order ID :</b>"+_response.getBillingInfo().getTransactionID()+"<br/>");
				out.println("<b>Amount :</b>"+_response.getBillingInfo().getAmount()+" "+_response.getBillingInfo().getCurrency().name() +"<br/>");
				out.println("<b>Auth Code :</b>"+_response.getAuthCode()+"<br/>");
				out.println("<b>Is Approved:</b>"+_response.isApproved()+"<br/>");
				out.println("<b>Is Declined:</b>"+_response.isDeclined()+"<br/>");
				out.println("<b>Is CVV Match:</b>"+_response.isCVVMatch()+"<br/>");
				out.println("<b>Is AVS Match:</b>"+_response.isAVSMatch()+"<br/>");
				out.println("<b>Is Processing Error:</b>"+_response.isError()+"<br/>");
				out.println("<b>Tx Ref Num</b>"+_response.getBillingInfo().getTransactionRef()+"<br/>");
				out.println("<b>Tx Ref Index</b>"+_response.getBillingInfo().getTransactionRefIndex()+"<br/>");
				
			}
		} else {
			out.println("<b>FAILURE</b>"+"<br/>");
			out.println("<b>Status Code :</b>"+_response.getStatusCode()+"<br/>");
			out.println("<b>Status Message :</b>"+_response.getStatusMessage()+"<br/>");
		}
		out.println("<b>Request XML :</b><br/>");
			out.println(_response.getRawRequest().replace("<","&lt;").replace(">","&gt;")+"<br/>");
			out.println("<b>Response XML :</b><br/>");
			out.println(_response.getRawResponse().replace("<","&lt;").replace(">","&gt;")+"<br/>");
	//}
%>