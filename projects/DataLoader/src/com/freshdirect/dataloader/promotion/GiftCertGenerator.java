/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author rgayle
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.freshdirect.dataloader.promotion;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class GiftCertGenerator {
	public static void main (String[] args) {
		
	/*** Asjust the values below before generating  *****/
		int howMany = 1;				// how many gift certs to generate
		String gfCodePrefix = "TEST200";   //prefix of the redemption code (cs50 or cs100
		int dollarAmount = 100;          // dollar amount of the gift cert
		
		/** Run one of the following queries, on production, to get the Next starting  seq number for that 
		 * gift cert promo amount  and plug it into the promoCodeSeq var below.
			*** get next $100 gift cert promo *
			  select max(to_number(substr(code,11)))+1  from cust.promotion where code like 'CS_GC_100_%'
			 
			***** get Next $50 gift cert promo * 
			  select max(to_number(substr(code,10)))+1  from cust.promotion where code like 'CS_GC_50_%'
        !!Put value here----+
		                    V   */
		int promoCodeSeq = 1;         //starting sequence number for the promocode and name..need manually to query promo table to
										//determine this number
		
		/** REMEMBER TO CHANGE THE Name, Description, START and End DATE, IN THE SQL BELOW *****/
	/**** End of adjustment area ****/
		
		
		FileOutputStream fo=null;
		try {
			fo = new FileOutputStream("c:\\genGiftCert.sql");
			Random generator = new Random(System.currentTimeMillis());
			Set s = new HashSet();
			for ( int x = 1; x <= howMany; x++,promoCodeSeq++) {
				String  rndValue = Long.toString(Math.abs(generator.nextInt()),36);
				generateSql(rndValue,promoCodeSeq,gfCodePrefix,dollarAmount,fo);
				s.add("'"+gfCodePrefix+rndValue.toUpperCase()+"'");
			}
			fo.close();
			System.out.println(s);
		} catch (Exception e) {
			System.out.println("Error trying to create Sql Script File\n"+e.getMessage());
		}
	}
	
	private static void generateSql(String redemptionCode, int codeSeq,String prfx,int dollars,FileOutputStream fo){
		StringBuffer insertStatement = new StringBuffer();
		insertStatement.append("INSERT INTO CUST.PROMOTION (ID, CODE, NAME, DESCRIPTION, MAX_USAGE,"); 
		insertStatement.append("START_DATE, EXPIRATION_DATE, REDEMPTION_CODE, CAMPAIGN_CODE, \nMIN_SUBTOTAL,");
		insertStatement.append("MAX_AMOUNT, UNIQUE_USE, ACTIVE, MODIFY_DATE, MODIFIED_BY) \nVALUES(CUST.SYSTEM_SEQ.NEXTVAL,");
		insertStatement.append("'YH_GC_").append(dollars).append("_").append(codeSeq).append("',");
		insertStatement.append("'FD/YH Gift Cert: $").append(dollars).append(" ").append(codeSeq).append("',");	
		insertStatement.append("'FreshDirect/Yeshiva Hirsch gift certificate', \n1, TO_DATE('12/14/2005', 'MM/DD/YYYY'),TO_DATE('03/01/2006', 'MM/DD/YYYY'),");
		insertStatement.append("'").append(prfx).append(redemptionCode.toUpperCase()).append("',");
		insertStatement.append("'GIFT_CARD',0,").append(dollars).append(",'X'").append(",'X',");
		insertStatement.append("SYSDATE,").append("'System')").append(";\n\n");

		try {
			fo.write(insertStatement.toString().getBytes());
		} catch (Exception e) {
			System.out.println("Error trying to write Sql Script File\n"+e.getMessage());
		}
	}

}
