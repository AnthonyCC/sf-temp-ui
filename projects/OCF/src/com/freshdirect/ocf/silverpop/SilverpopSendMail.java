package com.freshdirect.ocf.silverpop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.oro.text.perl.Perl5Util;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.freshdirect.fdstore.FDRuntimeException;

//import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author jangela on Nov 8, 2004
 *
 * Invoke Send Mail via Silverpop API. 
 * Required parameters (2): MailingID and ListFileName
 * All emails addressed will be sent to despite errors.
 * 
 */
public class SilverpopSendMail extends SilverpopBase{

	public static String send(String mailingId, List emailAddresses) throws IOException {
		
		///////////////////////
	if (emailAddresses.size()>0) {	
		Document sendMailDoc = DocumentHelper.createDocument();
		Element body = sendMailDoc.addElement("Envelope").addElement("Body");

		for (int i = 0; i < emailAddresses.size(); i++) {
			Element sendMail = body.addElement("SendMailing");
			sendMail.addElement("MailingId").addText(mailingId);
			sendMail.addElement("RecipientEmail").addText(emailAddresses.get(i).toString());
		}

		Document doc = postXml(SERVICE.getUrl(), sendMailDoc, "0");

		//System.out.println(doc);

		List successNodes = doc.selectNodes("/Envelope/Body/RESULT/SUCCESS");
		List faultNodes = new ArrayList();
		int count = 0;
		int countSuccess = 0;
		int countFail = 0;
		for (Iterator i = successNodes.iterator(); i.hasNext(); count++) {
			Node node = (Node) i.next();
			boolean succ = Boolean.valueOf(node.getText()).booleanValue();
			System.out.println((count+1) +". "+ emailAddresses.get(count) + " -> " + succ);
			if (!succ) {
				String faultString = node.valueOf("../following-sibling::node()/FaultString");
				System.out.println(emailAddresses.get(count) + ": " + faultString);
				faultNodes.add(emailAddresses.get(count) + ": " + faultString);
				countFail++;
			} else {
				countSuccess++;
			}
			//System.out.println("----------");
		}
		
		if(!faultNodes.isEmpty()){
			StringBuffer message = new StringBuffer();
			for(Iterator i = faultNodes.iterator();i.hasNext();){
				message.append(i.next()).append("\n");
			}
			throw new FDRuntimeException(message.toString());
		}
	}
		return null;
	}

	public static void main(String args[]) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage sendMail: mailingId listFileName.csv");
			System.exit(1);
		}
		
		String mailingId = args[0];
		String fileName = args[1];

		send(mailingId, getEmailAddresses(fileName));

	}
	
	private static String parseLine(String line) {
		if (line == null) return null;
		String email = "";
		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		
		while(tokenizer.hasMoreTokens()) {
			//assume email is first
			String part = tokenizer.nextToken().trim();
			if (isValidEmailAddress(part)) {
				email = part;
				//System.out.println("email read:" + email);
				break;
			} else {
				System.out.println("discarding ("+ line +") - invalid email address ");
				return null;
			}
		}
		return email;
	}
    
    public static boolean isValidEmailAddress(String emailAddress) {
    	Perl5Util emailRegExp = new Perl5Util();
        String emailPattern = "/^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\\.)+[a-zA-Z]{2,}$/";
        return emailRegExp.match(emailPattern, emailAddress);
    }
    
    private static List getEmailAddresses(String fileName) throws IOException{
		/*ListBuilder builder = new ListBuilder();
		List emailAddresses = builder.parseEmailList(new File(fileName));
		*/
		List emailAddresses = new ArrayList();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		//
		// LOOP THROUGH EACH LINE IN THE FILE,
		// AND ADD EACH TOKEN (EMAIL ADDRESS) TO THE LIST
		//
		String line = in.readLine();
		while (line != null) {
			String trimmed = line.trim();
			if ("".equals(trimmed)) {
				line = in.readLine();
				continue;
			}
			String email = parseLine(line);
			if (email != null) {
				emailAddresses.add(email);
			}
			line = in.readLine();
		}
		
		return emailAddresses;
    }
}