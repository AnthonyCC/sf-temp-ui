package com.freshdirect.mail.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.cheetahmail.apiclient.APIClient;
import com.cheetahmail.apiclient.APIClientException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.mail.EnumEmailType;
import com.freshdirect.mail.EnumTranEmailType;
import com.freshdirect.mail.service.silverpop.OAuthAuthentication;
import com.freshdirect.mail.service.silverpop.OAuthenticationException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import com.silverpop.api.client.ApiCommand;
//import com.silverpop.api.client.XmlApiProperties;
//import com.silverpop.api.client.mailing.command.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SilverpopTranMailServiceImpl implements TranMailServiceI {

	private final static Logger LOGGER = LoggerFactory.getInstance(SilverpopTranMailServiceImpl.class);
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;

	private String IBM_ACCESS_TOKEN = null; // getIBMCampaignAccessToken()
	private OAuthAuthentication auth;

	public SilverpopTranMailServiceImpl() {
	}

	{
		auth = new OAuthAuthentication();
		try {
			IBM_ACCESS_TOKEN = auth.getIBMCampaignAccessToken();
		} catch (OAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String sendTranEmail(TEmailI emailInfo) throws TranEmailServiceException {

		LOGGER.debug("generating temail for : " + emailInfo.getRecipient());
		// System.out.println(this.getClass().getName() + " generating Temail
		// for : " + emailInfo.getRecipient());
		// String response="OK";
		// try {

		String xmlAsString = emailInfo.getEmailContent();

		SilverpopEmailCampaignXmlHttpsPoster silverPopPoster = new SilverpopEmailCampaignXmlHttpsPoster();
		// System.out.println("**********************this is the xml as string:
		// |||||||");
		// System.out.println(xmlAsString);
		// System.out.println("|||||||*****************this is the xml as
		// string: |||||||");
		// System.out.println(">>>> this is what is in the email content field:
		// >>>>>>>>");
		// System.out.println(emailInfo.getEmailContent());
		// System.out.println("<<<<< this is what is in the email content field:
		// <<<<<<<");
		SilverpopPostReturnValue retvalue = null;
		try {
			// String accessToken = auth.getIBMCampaignAccessToken();
			String accessToken = getIBMAccessTokenThruCache();
			retvalue = silverPopPoster.httpsPostWatsonEmailCampaignWithToken(xmlAsString, accessToken);
		} catch (OAuthenticationException e) {

			e.printStackTrace();
			throw new TranEmailServiceException(this.getClass().getSimpleName() + " Silverpop authorization Failure ",
					e);
		}

		// the above do something with
		// Map<String, String> errMap = null;// new HashMap<String, String>();
		SilverpopXmlResponseParser responseParser = new SilverpopXmlResponseParser();
		// System.out.println("im about to parse the response body I got from
		// silverpop\n\r");
		// System.out.println(retvalue.getPostReturnBody()+ " \n\r");

		// String silverpopResponseStr =
		// responseParser.processXmlErrors(retvalue.getPostReturnBody() );
		String methodRetStr = "";

		if (retvalue.getRetcode() != 200) {
			methodRetStr = "FAIL : " + retvalue.getPostReturnBody();
			LOGGER.error(String.format(" ERROR in silverpop post, ret was %s, message for  %s ", emailInfo,
					retvalue.getRetcode(), emailInfo.getId()));
		} else {
			methodRetStr = "OK : " + retvalue.getPostReturnBody();
		}

		return methodRetStr;
		// }

	}

	private String unpackMapIntoString(Map<String, String> inmap) {
		Set<Entry<String, String>> entrySet = inmap.entrySet();
		StringBuffer buf = new StringBuffer();

		for (Entry<String, String> entry : entrySet) {
			buf.append(entry.getKey() + " : " + entry.getValue() + ",");
		}
		return buf.toString();

	}

	public Map<String, String> parseWarningResponse(String xmlInput) {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		String[] errorsToLookFor = { "SUCCESS", "FaultCode", "FaultString", "errorid" };
		Map<String, String> errMap = new HashMap<String, String>();
		try {
			InputStream inputStream = new ByteArrayInputStream(xmlInput.getBytes("UTF-8"));
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);

			doc.getDocumentElement().normalize();

			Element element = doc.getDocumentElement();

			// get all child nodes
			NodeList nodes = element.getChildNodes();

			// print the text content of each child
			// for (int i = 0; i < nodes.getLength(); i++) {
			// System.out.println(i+ " node: "+ nodes.item(i).getNodeName()+ ":"
			// + nodes.item(i).getTextContent());
			// }

			for (String errorToLookFor : Arrays.asList(errorsToLookFor)) {
				String err = processDocForErrors(errorToLookFor, doc);
				if (!err.isEmpty()) {
					errMap.put(errorToLookFor, err);
				}
			}
			processDocForErrors("SUCCESS", doc);
			processDocForErrors("FaultCode", doc);
			processDocForErrors("FaultString", doc);
			processDocForErrors("errorid", doc);

			processDocForErrors("UNKNOWN", doc);
			// NodeList nList = doc.getElementsByTagName("SUCCESS");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("FaultCode");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("FaultString");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("errorid");
			// process (nList);

			// if ( nList!=null ){
			// System.out.println("---------------------------- nlist.length:
			// "+nList.getLength());
			// System.out.println(" the value of success is :"+
			// nList.item(0).getTextContent() +"|");
			// Node nNode = nList.item(0) ;
			// System.out.println("\nCurrent Element :" + nNode.getNodeName());
			//
			// }
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errMap;

	}

	/*
	 * this is the early response parser before I broke it out into its own
	 * class Im leaving it here during testing as a fallback when I get unusual
	 * behaviour Newer doesnt mean better. This thing works.
	 */
	@Deprecated
	public Map<String, String> parseErrorResponse(String xmlInput) {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		String[] errorsToLookFor = { "SUCCESS", "FaultCode", "FaultString", "errorid" };
		Map<String, String> errMap = new HashMap<String, String>();
		try {
			InputStream inputStream = new ByteArrayInputStream(xmlInput.getBytes("UTF-8"));
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);

			doc.getDocumentElement().normalize();

			Element element = doc.getDocumentElement();

			// get all child nodes
			NodeList nodes = element.getChildNodes();

			// print the text content of each child
			// for (int i = 0; i < nodes.getLength(); i++) {
			// System.out.println(i + " node: " + nodes.item(i).getNodeName() +
			// ":" + nodes.item(i).getTextContent());
			// }

			for (String errorToLookFor : Arrays.asList(errorsToLookFor)) {
				String err = processDocForErrors(errorToLookFor, doc);
				if (!err.isEmpty()) {
					errMap.put(errorToLookFor, err);
				}
			}
			processDocForErrors("SUCCESS", doc);
			processDocForErrors("FaultCode", doc);
			processDocForErrors("FaultString", doc);
			processDocForErrors("errorid", doc);

			processDocForErrors("UNKNOWN", doc);
			// NodeList nList = doc.getElementsByTagName("SUCCESS");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("FaultCode");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("FaultString");
			// process (nList);
			//
			// nList = doc.getElementsByTagName("errorid");
			// process (nList);

			// if ( nList!=null ){
			// System.out.println("---------------------------- nlist.length:
			// "+nList.getLength());
			// System.out.println(" the value of success is :"+
			// nList.item(0).getTextContent() +"|");
			// Node nNode = nList.item(0) ;
			// System.out.println("\nCurrent Element :" + nNode.getNodeName());
			//
			// }
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errMap;

	}

	private String processDocForErrors(String errorNodeName, Document doc) {
		NodeList nList = doc.getElementsByTagName(errorNodeName);
		return process(nList);
	};

	private String process(NodeList nList) {
		String retString = "";
		if (nList != null && nList.item(0) != null) {
			// System.out.println("---------------------------- nlist.length:
			// "+nList.getLength());
			// System.out.println(" the value of the node named: " +
			// nList.item(0).getNodeName() + " is :"
			// + nList.item(0).getTextContent() + "|");
			retString = nList.item(0).getTextContent();
			// System.out.println("\nCurrent Element :" + nNode.getNodeName());
		}
		return retString;

	}

	public boolean isTempErrorString(String string) {
		return ((string.startsWith("err:internal")) || (string.startsWith("err:server"))
		// err:upload is a special client-side error generated in
		// readResponse()
				|| (string.startsWith("err:upload")) || (string.startsWith("err:param:requested data not available"))
				|| (string.startsWith("500")));
	}

	public String getEmailTypeValue(String emailTypeStr) {

		EnumEmailType emailType = EnumEmailType.getEnum(emailTypeStr);
		if (EnumEmailType.HTML == emailType) {
			return "1";
		} else if (EnumEmailType.TEXT == emailType) {
			return "0";
		} else {
			return "2";
		}
	}

	// public Map fillContentParams(Map params, String contents) {
	//
	// String lines[] = contents.split("&&");
	// if (lines != null && lines.length > 0) {
	//
	// for (int i = 0; i < lines.length; i++) {
	//
	// String line = lines[i];
	// if (line == null || line.trim().length() == 0)
	// continue;
	// String value = line.substring(line.indexOf("=") + 1);
	// String key = line.substring(0, line.indexOf("="));
	// if (key.startsWith("ENCRYPT[")) {
	// key = key.substring(key.indexOf("[") + 1, key.length() - 1);
	// value = ErpGiftCardUtil.decryptGivexNum(value);
	//// System.out.println("encrypted key :" + key);
	//// System.out.println("encrypted value :" + value);
	// }
	// params.put(key, value);
	// }
	// }
	//
	// /*
	// * StringTokenizer tokens=new StringTokenizer(contents,"&&");
	// * while(tokens.hasMoreElements()){ String line=tokens.nextToken();
	// * if(line==null || line.trim().length()==0) continue; String
	// * value=line.substring(line.indexOf("=")+1); String
	// * key=line.substring(0, line.indexOf("="));
	// *
	// * if(key.startsWith("ENCRYPT[")){
	// * key=key.substring(key.indexOf("[")+1,key.length()-1);
	// * value=ErpGiftCardUtil.decryptGivexNum(value);
	// *
	// * System.out.println("encrypted key :"+key); System.out.println(
	// * "encrypted value :"+value);
	// *
	// * }
	// *
	// * params.put(key, value); }
	// */
	// return params;
	// }

	// generally a bad idea to have parameters string string string together
	// like this. if anybody ups parameters from 4 to 5, RETHINK THIS!
	public String generateEmailXml(String campaignCode, String emailAddress, String firstName, String customerId) {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			if (customerId == null || customerId.isEmpty()) {
				customerId = "BLANK";
			}

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("XTMAILING");
			doc.appendChild(rootElement);

			// staff elements
			// Element staff = doc.createElement("Staff");
			// rootElement.appendChild(staff);

			// set attribute to staff element
			// Attr attr = doc.createAttribute("id");
			// attr.setValue("1");
			// rootElement.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			campaign(doc, rootElement, campaignCode);

			transaction(doc, rootElement);

			showAll(doc, rootElement);

			// saveColumns( doc, rootElement);

			Element recipient = recipient(doc, rootElement, emailAddress);

			Element personalization = personalization(doc, recipient, "NAME_FIRST", "TSRIF_EMAN");

			personalization(doc, recipient, "ACCOUNT_NUMBER", customerId);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new
			// File("C:/FreshDirectTrunk/writedocument.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			// transformer.transform(source, result);

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String xmlOutputAsStr = writer.getBuffer().toString();
			// String xmlOutputAsStr =
			// writer.getBuffer().toString().replaceAll("\n|\r", "");
			// System.out.println("document: " + xmlOutputAsStr);

			// System.out.println("File saved!");
			return xmlOutputAsStr;

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return "";
	}

	private void retryOnFailure(Document doc, Element rootElement) {
		Element retry = doc.createElement("NO_RETRY_ON_FAILURE");
		retry.appendChild(doc.createTextNode("true"));
		rootElement.appendChild(retry);
	}

	private void showAll(Document doc, Element rootElement) {
		Element show = doc.createElement("SHOW_ALL_SEND_DETAIL");
		show.appendChild(doc.createTextNode("true"));
		rootElement.appendChild(show);
	}

	private void transaction(Document doc, Element rootElement) {
		Element transaction = doc.createElement("TRANSACTION_ID");
		transaction.appendChild(doc.createTextNode("test1234"));
		rootElement.appendChild(transaction);
	}

	private void campaign(Document doc, Element rootElement, String campaignCode) {
		Element campaign = doc.createElement("CAMPAIGN_ID");
		campaign.appendChild(doc.createTextNode(campaignCode));
		rootElement.appendChild(campaign);
	}

	private Element personalization(Document doc, Element rootElement, String tagName, String tagValue) {
		Element personalization = doc.createElement("PERSONALIZATION");

		rootElement.appendChild(personalization);
		createPersonalizationPair(doc, personalization, tagName, tagValue);
		return personalization;
	}

	private void saveColumns(Document doc, Element rootElement) {
		Element save = doc.createElement("SAVE_COLUMNS");
		// save.appendChild(doc.createTextNode("true"));

		Element column_name1 = doc.createElement("COLUMN_NAME");
		column_name1.appendChild(doc.createTextNode("FIRSTNAME"));

		Element column_name2 = doc.createElement("COLUMN_NAME");

		Attr attr2 = doc.createAttribute("SAVE_TO_RESPONSE");
		attr2.setValue("TRUE");
		column_name2.setAttributeNode(attr2);

		column_name2.appendChild(doc.createTextNode("ACCOUNT_NUMBER"));

		save.appendChild(column_name1);
		save.appendChild(column_name2);

		rootElement.appendChild(save);
	}

	private Element recipient(Document doc, Element rootElement, String recipientEmailAddress) {
		Element recipient = doc.createElement("RECIPIENT");
		Element email = doc.createElement("EMAIL");
		email.appendChild(doc.createTextNode(recipientEmailAddress));
		recipient.appendChild(email);

		Element bodyType = doc.createElement("BODY_TYPE");
		bodyType.appendChild(doc.createTextNode("html"));
		recipient.appendChild(bodyType);

		rootElement.appendChild(recipient);
		return recipient;
	}

	private void createPersonalizationPair(Document doc, Element anchorElement, String tagName, String tagValue) {

		Element tagNameTag = doc.createElement("TAG_NAME");

		tagNameTag.appendChild(doc.createTextNode(tagName));
		Element tagValueTag = doc.createElement("VALUE");

		tagValueTag.appendChild(doc.createTextNode(tagValue));

		anchorElement.appendChild(tagNameTag);
		anchorElement.appendChild(tagValueTag);

	}

	// String accessToken = auth.getIBMCampaignAccessToken();
	private String getIBMAccessTokenThruCache() throws OAuthenticationException {
		long t = System.currentTimeMillis();

		if ((IBM_ACCESS_TOKEN == null) || (t - lastRefresh) > REFRESH_PERIOD) {
			synchronized (SilverpopTranMailServiceImpl.class) {
				if ((IBM_ACCESS_TOKEN == null) || (t - lastRefresh) > REFRESH_PERIOD) {// double
																						// check

					IBM_ACCESS_TOKEN = auth.getIBMCampaignAccessToken();
					lastRefresh = t;
					// System.out.println(this.getClass().getName() + " getting
					// token creating it! " + IBM_ACCESS_TOKEN);
					LOGGER.info("regenerated token ");

				}
			}
		} else {
			// System.out
			// .println(this.getClass().getName() + " %%%%%%%%%%% getting token
			// thru cache! " + IBM_ACCESS_TOKEN);
		}
		return IBM_ACCESS_TOKEN;
	}

}
