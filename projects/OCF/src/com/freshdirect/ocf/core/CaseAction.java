/**
 * @author ekracoff
 * Created on May 3, 2005*/

package com.freshdirect.ocf.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.HashMessageFormat;
import com.freshdirect.ocf.core.OcfTableI.Column;
import com.freshdirect.ocf.core.OcfTableI.Row;


public class CaseAction extends Action implements ActionI{
	private String subjectCode;
	private String summary;
	private String note;
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getSubjectCode() {
		return subjectCode;
	}
	
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * New version which allows the fields to be MessageFormat strings.
	 * The old version has been renamed _OLD and appears below to allow
	 * for quick revertion if needed.
	 */
	public void execute(OcfTableI customers) {
		int custRow    = customers.getColumnPosByName("CUSTOMER_ID");
		int saleRow    = customers.getColumnPosByName("SALE_ID");
		Column cols[]  = customers.getColumns();
		String names[] = new String[cols.length];
		
		HashMessageFormat hmfSummary = new HashMessageFormat(summary);
		HashMessageFormat hmfNote = null;
		if(note != null) {
			hmfNote = new HashMessageFormat(note);
		} else {
			hmfNote = new HashMessageFormat("");
		}
		
		for(int i=0;i<cols.length;i++) {
			names[i] = cols[i].getName();
		}
		
		CrmCaseSubject subject = CrmCaseSubject.getEnum(subjectCode);
		
		for(Iterator i = customers.getRows().iterator(); i.hasNext();){
			Row row           = (Row)i.next();
			PrimaryKey custPk = new PrimaryKey((String) row.getValues()[custRow]);
			Map templateData  = new HashMap();
			Object vals[]     = row.getValues();
			
			for(int j=0;j<cols.length;j++) {
				templateData.put(names[j],vals[j]);
			}
			
			CrmSystemCaseInfo caseInfo = null;
			if(saleRow > -1){
				PrimaryKey salePk = new PrimaryKey((String) vals[saleRow]);
				caseInfo = new CrmSystemCaseInfo(custPk, salePk, subject, hmfSummary.format(templateData));
			} else {
				caseInfo = new CrmSystemCaseInfo(custPk, subject, hmfSummary.format(templateData));
			}
			
			if(note != null){
				caseInfo.setNote(hmfNote.format(templateData));
			}
			
			try {
				FDCustomerManager.createCase(caseInfo);
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
			
		}
	}

	
	public void execute_OLD(OcfTableI customers) {
		int custRow = customers.getColumnPosByName("CUSTOMER_ID");
		int saleRow = customers.getColumnPosByName("SALE_ID");
		
		CrmCaseSubject subject = CrmCaseSubject.getEnum(subjectCode);
		
		for(Iterator i = customers.getRows().iterator(); i.hasNext();){
			Row row = (Row)i.next();
			PrimaryKey custPk = new PrimaryKey((String) row.getValues()[custRow]);
			
			CrmSystemCaseInfo caseInfo = null;
			if(saleRow > -1){
				PrimaryKey salePk = new PrimaryKey((String) row.getValues()[saleRow]);
				caseInfo = new CrmSystemCaseInfo(custPk, salePk, subject, summary);
			} else {
				caseInfo = new CrmSystemCaseInfo(custPk, subject, summary);
			}
			
			if(note != null){
				caseInfo.setNote(note);
			}
			
			try {
				FDCustomerManager.createCase(caseInfo);
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
			
		}
	}

	public String toString() {
		return "Case action: subject=" + subjectCode + ", summary=" + summary;
	}
}
