/*
 * CCGenerator.java
 *
 * Created on August 6, 2002, 1:04 PM
 */

package com.freshdirect.customer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;


public class CCGenerator {
    
    /**
     * @param args the command line arguments
     */
    public static void main (String args[]) {
        
        CCGenerator ccg = new CCGenerator ();
        ccg.generateCards ();
        
        //System.out.println(CreditCardUtil.validateCreditCardNumber("4422653000000180", EnumCreditCardType.VISA.getFdName()));
        
    }
    
    
    /** Creates new CCGenerator */
    public CCGenerator () {
    }
    
    public void generateCards () {
        for (int i=0;i<1000;i++) {
            Card c = randomCardNumber ();
            if (PaymentMethodUtil.validateCreditCardNumber (c.number, c.brand)) {
                System.out.println (c.brand + " : " + c.number);
            }
        }
    }
    
    private Card randomCardNumber () {
        
        String cardNum;
        String brand;
        int rand = (int) (Math.random () * 4.0);
        switch (rand) {
            case 0:
                // for VISA prefix is 4 and lenght must be 13 || 16
                brand = EnumCardType.VISA.getFdName();
                cardNum = "4";
                while (cardNum.length () < 16) {
                    cardNum += Integer.toString ((int) (Math.random () * 10.0));
                }
                break;
                
            case 1:
                // for MASTERCARD prefix is between 51..55 and length must be 16
                brand = EnumCardType.MC.getFdName ();
                cardNum = Integer.toString (51 + (int) (Math.random () * 5.0));
                while (cardNum.length () < 16) {
                    cardNum += Integer.toString ((int) (Math.random () * 10.0));
                }
                break;
                
            case 2:
                //for AMEX prefix is 34 || 37 and length must be 15
                brand = EnumCardType.AMEX.getFdName ();
                cardNum = Integer.toString (34 + (int)(Math.round (Math.random ()) * 3));
                while (cardNum.length () < 15) {
                    cardNum += Integer.toString ((int) (Math.random () * 10.0));
                }
                break;
                
            default:
                //for DISC prefix is 60 and length must be 16
                brand = EnumCardType.DISC.getFdName ();
                cardNum = "60";
                while (cardNum.length () < 16) {
                    cardNum += Integer.toString ((int) (Math.random () * 10.0));
                }
        }
        
        return new Card (cardNum, brand);
        
    }
    
    
    private class Card {
        
        public Card (String num, String b) {
            this.number = num;
            this.brand = b;
        }
        
        public String number;
        public String brand;
        
    }
}
