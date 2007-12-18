/*
 * $Workfile: Spammer.java$
 *
 * $Date: 1/23/03 7:45:20 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.dataloader.email;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.mail.FDInfoEmail;

public class Spammer {
    
    public Spammer() {
        super();
    }
 	
    public static void main(String[] args) {

		printTime("Start");
        
        Spammer spammer = new Spammer();
        
        //
        // schedule email to run in the future
        //
        /*
        Calendar sendTime = Calendar.getInstance();
        sendTime.set(Calendar.DAY_OF_MONTH, 1);
        sendTime.set(Calendar.MONTH, Calendar.JULY);
        sendTime.set(Calendar.YEAR, 2003);
        sendTime.set(Calendar.HOUR_OF_DAY, 2);
        sendTime.set(Calendar.MINUTE, 0);
        System.out.println("Waiting until " + sendTime.getTime() + " to send...");
        while (new Date().before(sendTime.getTime())) {
            try {
                Thread.sleep(30000); // 30 seconds
            } catch (InterruptedException ie) {
                printTime("Interrupted");
                ie.printStackTrace();
                return;
            }
        }
        */
        
        String template_html = "bulk/h_event_wine_cheese_2.xsl";
        String template_text = "bulk/x_event_wine_cheese_2.xsl";
        String subject = "Our second wine and cheese seminar!";

       spammer.spamMe(subject, template_html, template_text);
        
      	//spammer.spamFDPeople(subject, template_html, template_text);
		//spammer.sendEmailTo("c:/FDdev/emails/sample_emails.txt", subject, template_html, template_text);
		//spammer.sendEmailTo("c:/FDdev/emails/rollout/10004.txt", subject, template_html, template_text);
		spammer.sendEmailTo("c:/FDdev/emails/event/wine_cheese_2.txt", subject, template_html, template_text);
		//spammer.sendEmailTo("c:/FDdev/emails/new/organic_b2.txt", subject, template_html, template_text);

        printTime("End");
        
    }
    
    public void sendEmailTo(String fileName, String subject, String htmlStyleSheet, String textStyleSheet) {
        
        ListBuilder builder = new ListBuilder();
        List emailAddresses = builder.parseEmailList(new File(fileName));
        spamEmailList(emailAddresses, subject, htmlStyleSheet, textStyleSheet);
        
    }
    
    /**
	 * Sends copy of this email to selected FD employees.
	 */
	public void spamMe(String subject, String htmlStyleSheet, String textStyleSheet) {
		List fdList = new ArrayList();
		fdList.add(new MailInfo("jangela@freshdirect.com", "Jeannice", true));
		fdList.add(new MailInfo("jangela@freshdirect.com", "Jeannice", false));
        //fdList.add(new MailInfo("mrose@freshdirect.com", "Mike", true));
        //fdList.add(new MailInfo("mxr@mutsys.com", "Mike", false));
		//fdList.add(new MailInfo("RBogitch@freshdirect.com", "Ross", true));
		spamEmailList(fdList, subject, htmlStyleSheet, textStyleSheet);
	}
    
    public void spamFDPeople(String subject, String htmlStyleSheet, String textStyleSheet) {
		List fdList = new ArrayList();
		//fdList.add(new MailInfo("mtrachtenberg@freshdirect.com", "Marc", true));
		 //fdList.add(new MailInfo("mtrachtenberg@freshdirect.com", "Marc", false));
		 fdList.add(new MailInfo("akwok@freshdirect.com", "Alvin", true));
		 fdList.add(new MailInfo("akwok@freshdirect.com", "Alvin", false));
		 //fdList.add(new MailInfo("nbayless@freshdirect.com", "Neal", true));
		 //fdList.add(new MailInfo("nbayless@freshdirect.com", "Neal", false));
		 fdList.add(new MailInfo("mjacobs@freshdirect.com", "Marc", true));
		 fdList.add(new MailInfo("mjacobs@freshdirect.com", "Marc", false));
		 //fdList.add(new MailInfo("apurcell@freshdirect.com", "Andrew", true));
		 //fdList.add(new MailInfo("apurcell@freshdirect.com", "Andrew", false));
		 fdList.add(new MailInfo("lmatz@freshdirect.com", "Leitha", true));
		 fdList.add(new MailInfo("lmatz@freshdirect.com", "Leitha", false));
		//fdList.add(new MailInfo("jcollet@freshdirect.com", "Jen", true));
		 //fdList.add(new MailInfo("jcollet@freshdirect.com", "Jen", false));
		 //fdList.add(new MailInfo("dmcinerney@freshdirect.com", "Dave", true));
		 //fdList.add(new MailInfo("dmcinerney@freshdirect.com", "Dave", false));
		
			  //fdList.add(new MailInfo("kgirek@freshdirect.com", "Kamila", true));
			  //fdList.add(new MailInfo("kgirek@freshdirect.com", "Kamila", false));
		
			  //fdList.add(new MailInfo("jzhou@freshdirect.com", "June", true));
			  //fdList.add(new MailInfo("jzhou@freshdirect.com", "June", false));
			  
			 //fdList.add(new MailInfo("jboris@freshdirect.com", "John", true));
			  //fdList.add(new MailInfo("jboris@freshdirect.com", "John", false));
			 //fdList.add(new MailInfo("dsmith@freshdirect.com", "Dana", true));
			 // fdList.add(new MailInfo("dsmith@freshdirect.com", "Dana", false));
		
			  //fdList.add(new MailInfo("rbogitch@freshdirect.com", "Ross", true));
			  //fdList.add(new MailInfo("rbogitch@freshdirect.com", "Ross", false));
			  //fdList.add(new MailInfo("aharmer@freshdirect.com", "Amelia", true));
			  //fdList.add(new MailInfo("aharmer@freshdirect.com", "Amelia", false));
		
			  //fdList.add(new MailInfo("dgerridge@freshdirect.com", "Dave", true));
			  //fdList.add(new MailInfo("dgerridge@freshdirect.com", "Dave", false));
			  //fdList.add(new MailInfo("mrose@freshdirect.com", "Mike", true));
			  //fdList.add(new MailInfo("mrose@freshdirect.com", "Mike", false));
			  fdList.add(new MailInfo("jsheer@freshdirect.com", "Jerry", true));
			  fdList.add(new MailInfo("jsheer@freshdirect.com", "Jerry", false));
			  //fdList.add(new MailInfo("vszathmary@freshdirect.com", "Viktor", true));
			  //fdList.add(new MailInfo("vszathmary@freshdirect.com", "Viktor", false));
			  fdList.add(new MailInfo("jangela@freshdirect.com", "Jeannice", true));
			  fdList.add(new MailInfo("jangela@freshdirect.com", "Jeannice", false));

			  //fdList.add(new MailInfo("fdtest1@yahoo.com", "FDtest1", true));
			  //fdList.add(new MailInfo("fdtest1@yahoo.com", "FDtest1", false));
			  //fdList.add(new MailInfo("fdtest2@hotmail.com", "FDtest2", true));
			  ///fdList.add(new MailInfo("fdtest2@hotmail.com", "FDtest2", false));
			  //fdList.add(new MailInfo("fdtest3@excite.com", "FDtest3", true));
			  //fdList.add(new MailInfo("fdtest3@excite.com", "FDtest3", false));

		spamEmailList(fdList, subject, htmlStyleSheet, textStyleSheet);
	}
	
	private void spamPerson(MailInfo mi, String subject, String htmlStyleSheet, String textStyleSheet) {
        try {
			//
            // Populate email
            //
            FDCustomerInfo cInfo = new FDCustomerInfo(mi.getFirstName(),"");
            cInfo.setEmailAddress(mi.getEmail()); 
            cInfo.setHtmlEmail(mi.isHtmlEmail());
			//cInfo.setHtmlEmail(true);
            FDInfoEmail email = new FDInfoEmail(cInfo);
            
            //email.setFromAddress(new com.freshdirect.framework.mail.EmailAddress("FreshDirect Customer Service Team", "service@freshdirect.com"));
			email.setFromAddress(new com.freshdirect.framework.mail.EmailAddress("FreshDirect", "announcements@freshdirect.com"));
            email.setXslPath(htmlStyleSheet, textStyleSheet);
            email.setSubject(subject);
            
			//
			// Send email
			//
			FDCustomerManager.doEmail(email);
			
        } catch (com.freshdirect.fdstore.FDResourceException fdre) {
            fdre.printStackTrace();
        }   
	}


    
    public void pause(int minutes) {
            // pause a few minutes...
            System.out.println(new java.util.Date() + " pausing for " + minutes + " minutes between bursts...");
            try {
                Thread.sleep(1000 * 60 * minutes);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }
    
	private void spamEmailList(List emailAddresses, String subject, String htmlStyleSheet, String textStyleSheet) {
		if (emailAddresses != null) {
			System.out.println("Preparing to spam "+ emailAddresses.size() +" email addresses...");
            int count = 0;
			for (Iterator it = emailAddresses.iterator(); it.hasNext(); ) {
                MailInfo mi = (MailInfo) it.next();
                //System.out.println(mi);
				spamPerson(mi, subject, htmlStyleSheet, textStyleSheet);
                count++;
                if (count % 1000 == 0) {
                    pause(1);
                }
			}
		}
	}	

	/*
	 * Utility method
	 */
	private static void printTime(String milestone) {
		Calendar cal = Calendar.getInstance();
		java.util.Date current = cal.getTime();
		System.out.println(milestone + "Time: \t" + current.toString());
		System.out.println();
	}


}
