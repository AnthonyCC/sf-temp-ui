package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmCaseConstants {
	
	private static List crmCaseMediaList=new ArrayList();
	
	private static List caseMoreThenOneIssueList=new ArrayList();
	
	private static List caseFirstContactForIssueList=new ArrayList();
	
	private static List caseResolvedOnFirstContactList=new ArrayList();
	
	private static List crmReasonForNotResolveList=new ArrayList();
	
	private static List crmResolutionSatisfactoryList=new ArrayList();
	
	private static List crmCustomerToneList=new ArrayList();
	
	static{
		
		crmCaseMediaList.add("Email");
		crmCaseMediaList.add("Phone");
		crmCaseMediaList.add("Chat");
		crmCaseMediaList.add("Letter");
		crmCaseMediaList.add("Other");
		
		caseMoreThenOneIssueList.add("Yes");
		caseMoreThenOneIssueList.add("No");
		
		caseFirstContactForIssueList.add("Yes");
		caseFirstContactForIssueList.add("No");
		
		caseResolvedOnFirstContactList.add("Yes");
		caseResolvedOnFirstContactList.add("No");
		
		crmReasonForNotResolveList.add("Delivery team unreachable");
		crmReasonForNotResolveList.add("Could not redeliver today");
		crmReasonForNotResolveList.add("Information unavailable");
		crmReasonForNotResolveList.add("Technology problem");
		crmReasonForNotResolveList.add("Requires supervisor escalation");
		crmReasonForNotResolveList.add("Other");
		
		crmResolutionSatisfactoryList.add("Yes");
		crmResolutionSatisfactoryList.add("No");
		
		crmCustomerToneList.add("WOW!");
		crmCustomerToneList.add("Happy");
		crmCustomerToneList.add("Neutral");
		crmCustomerToneList.add("Upset");
		crmCustomerToneList.add("Very Upset");
		crmCustomerToneList.add("Unsure");						
		
	}
	
	public static List getCrmCaseMedia()
	{
		return crmCaseMediaList;
	}

	
	public static List getCrmCaseReasonForNotResolve()
	{
		return crmReasonForNotResolveList;
	}
	
	public static List getCrmCaseCustomerTone()
	{
		return crmCustomerToneList;
	}
	
    public static List getCaseMoreThenOneIssueList(){
        return 	caseMoreThenOneIssueList;
    }
	
    public static List getCaseFirstContactForIssueList(){
        return 	caseFirstContactForIssueList;
    }

    public static List getCaseResolvedOnFirstContactList(){
        return caseResolvedOnFirstContactList;	
    }
    
    public static List getCrmResolutionSatisfactoryList(){
    	return crmResolutionSatisfactoryList;
    }   
}
