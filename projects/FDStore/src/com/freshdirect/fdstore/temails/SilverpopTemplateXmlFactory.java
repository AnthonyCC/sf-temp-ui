package com.freshdirect.fdstore.temails;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.temails.TEmailTemplateInfo;
import com.freshdirect.mail.EnumTranEmailType;


/*
 * this class is primarily responsible for creating the xml Data that is used by silverpop to work with a given
 * template to generate an email.
 * Will be called from TEmailContentFactory to get the content field (xml goes there)
 * 
 */
public class SilverpopTemplateXmlFactory {
	Random rn = new Random();
	
	
	
	public String generateEmailXml(TEmailTemplateInfo templateInfo, TEmailContextI context, Map parameterHashMap){
		int tranEmailType = templateInfo.getTransactionType().getNumericRepresentation();
		String xmlTemplateStr="";
		
		switch (tranEmailType){
		
		case EnumTranEmailType.CUST_SIGNUP_CONST:
			xmlTemplateStr=	buildCustRegistrationEmailXml(templateInfo, context, parameterHashMap);
		break;
		default:
			/*same as above, dont let that bother you.
			 * currently, we can only do one type of email in the silverpop world.
			 */
			xmlTemplateStr=	buildCustRegistrationEmailXml(templateInfo, context, parameterHashMap);

		}
		
		
		 return xmlTemplateStr;
	}

	private String  buildCustRegistrationEmailXml(TEmailTemplateInfo templateInfo, TEmailContextI context,
			Map parameterHashMap) {
		String xmlTemplateStr;
		String emailAddress = "";
		String fName="";
		String lName="";
		String customerId="";
		String templateID= templateInfo.getTemplateId();
		//String customerId =  (String) parameterHashMap.get(TEmailConstants.CUSTOMER_ID);
		FDCustomerInfo  customerInfo = 	 ( com.freshdirect.fdstore.customer.FDCustomerInfo) parameterHashMap.get(TEmailConstants.CUSTOMER_ID_INP_KEY);
			 if (null!= customerInfo){
				 emailAddress = customerInfo.getEmailAddress(); 
				 lName= customerInfo.getLastName();
				 fName= customerInfo.getFirstName();	 
			 }
			 
			 String cohortId =(String)  parameterHashMap.get(TEmailConstants.COHORT_ID);
			 
			FDOrderI fdOrder = context.getOrder();
			if(  fdOrder!=null){
			 	customerId=	fdOrder.getCustomerId();
			}
			else{
				customerId =  (String) parameterHashMap.get(TEmailConstants.CUSTOMER_ID);
			}

			xmlTemplateStr=	 generateRegistrationEmailXml( templateID, emailAddress,  fName,customerId, cohortId, Arrays.asList ( new String[] {"COHORT_ID"} ));
			return xmlTemplateStr;
	}
	//temp
	
	
/*	public String generateRegistrationEmailXml(String campaignCode, String emailAddress, String firstName, String customerId,  String cohortId){
		return  generateRegistrationEmailXml( campaignCode, emailAddress, firstName, customerId,   cohortId, null);
	}*/
	
	/**
	 *  Creates the xml to be used by silverpop for customer registration emails.
	 * @param campaignCode this is what is commonly referred to as the email templateId.
	 * @param emailAddress email address we are sending to
	 * @param firstName first name field
	 * @param customerId String, optional at this time
	 * @param cohortId String C1, C2....C21, C22
	 * @return String, a fully formed xml ready to be posted to silverpop/IBM.
	 */
	//I dont think its a very good idea to have a parameter list of String, String, String, String, String but I dont see any solution now.
	public String generateRegistrationEmailXml(String campaignCode, String emailAddress, String firstName, String customerId,  String cohortId, java.util.List<String> saveColums ){
		  try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			if (customerId==null || customerId.isEmpty()){
				customerId="BLANK";
			}

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("XTMAILING");
			doc.appendChild(rootElement);


			campaign(doc, rootElement,  campaignCode);
			
			
			transaction(doc, rootElement);
			
			showAll(doc, rootElement);

			if (saveColums!= null &&  ! saveColums.isEmpty()){
				saveColumns( doc, rootElement,saveColums );
			}

			Element recipient = recipient( doc,  rootElement, emailAddress);
			 
			Element personalization =  personalization( doc,  recipient,  "NAME_FIRST",firstName);
			
			 personalization( doc,  recipient,  "ACCOUNT_NUMBER",customerId);
			 personalization( doc,  recipient,  "COHORT_ID",cohortId);
			

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
		//	StreamResult result = new StreamResult(new File("C:/FreshDirectTrunk/writedocument.xml"));
		
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			//transformer.transform(source, result);
			
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String xmlOutputAsStr = writer.getBuffer().toString();
			//	String xmlOutputAsStr = writer.getBuffer().toString().replaceAll("\n|\r", "");
		//	System.out.println("document: "+xmlOutputAsStr);
			

		//	System.out.println("File saved!");
			return xmlOutputAsStr;

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		  return "";
		}
		
		
		
			private  void retryOnFailure(Document doc, Element rootElement)
		{
			Element retry = doc.createElement("NO_RETRY_ON_FAILURE");
			retry.appendChild(doc.createTextNode("true"));
			rootElement.appendChild(retry);
		}
		private  void showAll(Document doc, Element rootElement) {
			Element show = doc.createElement("SHOW_ALL_SEND_DETAIL");
			show.appendChild(doc.createTextNode("true"));
			rootElement.appendChild(show);
		}

		private  void transaction(Document doc, Element rootElement) {
			Element transaction = doc.createElement("TRANSACTION_ID");
			transaction.appendChild(doc.createTextNode(randomStringNum()));
			rootElement.appendChild(transaction);
		}

		private  void campaign(Document doc, Element rootElement, String campaignCode) {
			Element campaign = doc.createElement("CAMPAIGN_ID");
			campaign.appendChild(doc.createTextNode(campaignCode));
			rootElement.appendChild(campaign);
		}
		
		private  Element personalization(Document doc, Element rootElement,  String tagName, String tagValue ) {
			Element personalization = doc.createElement("PERSONALIZATION");
			
			rootElement.appendChild(personalization);
			createPersonalizationPair(doc,personalization, tagName,tagValue );
			return personalization;
		}
		
		private  Element recipient(Document doc, Element rootElement, String recipientEmailAddress)
		{
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
		
private  void  createPersonalizationPair( Document doc,  Element anchorElement, String tagName, String tagValue ){
			
			Element tagNameTag = doc.createElement("TAG_NAME");
			
			tagNameTag.appendChild(doc.createTextNode(tagName));
		Element tagValueTag = doc.createElement("VALUE");
			
		tagValueTag.appendChild(doc.createTextNode(tagValue));
		
		anchorElement.appendChild(tagNameTag);
		anchorElement.appendChild(tagValueTag);
			
		}
/**
 * 
 * @param doc
 * @param rootElement
 * Please Note, this method is here namely to generate a warning on the silverpop side, there is no save columns 
 * in the template at the time this was written
 */
private  void saveColumns(Document doc, Element rootElement, java.util.List<String> fields) {
		Element save = doc.createElement("SAVE_COLUMNS");
		// save.appendChild(doc.createTextNode("true"));
		for (String field : fields) {
			Element column_name = doc.createElement("COLUMN_NAME");
			column_name.appendChild(doc.createTextNode(field));
			save.appendChild(column_name);
		}
	
	//Element column_name2 = doc.createElement("COLUMN_NAME");
	
//	Attr attr2 = doc.createAttribute("SAVE_TO_RESPONSE");
//	attr2.setValue("TRUE");
//	column_name2.setAttributeNode(attr2);
//	
//	
//	column_name2.appendChild(doc.createTextNode("ACCOUNT_NUMBER"));
	
	
	//save.appendChild(column_name1);
	//save.appendChild(column_name2);
	
	
	rootElement.appendChild(save);
}
		
	
public  String randomStringNum(){
	return( new Integer(  rn.nextInt(32760)).toString()   +   new Integer(  rn.nextInt(32760)).toString()) ;
}

}
