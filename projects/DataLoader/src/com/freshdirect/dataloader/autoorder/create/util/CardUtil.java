package com.freshdirect.dataloader.autoorder.create.util;

import com.freshdirect.common.customer.EnumCardType;


public class CardUtil {
    
    
	 private final static int INVALID			= -1;
	 private final static int VISA				= 0;
	 private final static int MASTERCARD			= 1;
	 private final static int AMERICAN_EXPRESS	= 2;
	 private final static int DISCOVER			= 3;
	    
    public static Card generateCards () {
    	 Card c = randomCardNumber ();
         if (validateCreditCardNumber (c.number, c.brand)) {
             System.out.println (c.brand + " : " + c.number);
         }
         return c;
    }
    
    private static Card randomCardNumber () {
        
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
    
    /**
     * this method takes a potential credit card number and checks against Luhn check on it
     * including the prefix check making sure that the it is from a one of correct providers
     *
     * @param String number to check
     * @return true if it is a valid credit card number
     */
    public static boolean validateCreditCardNumber(String number, String brand){
        if (validateCardBrand(number, brand) == INVALID){
            return false;
        }
        //now we know it is a number and brand is valid, next we'll check for mod10
        try {
            int checksum = 0;
            boolean timesTwo = false;
            
            for (int i = number.length()-1; i >= 0; i--) {
                int k = 0;
                if(timesTwo){
                    k = Integer.parseInt(number.substring(i, i+1)) * 2;
                    if (k > 9) {
                        k = k - 9;
                    }
                }else{
                    k = Integer.parseInt(number.substring(i, i+1));
                }
                checksum += k;
                timesTwo = !timesTwo;
            }
            
            return (checksum % 10) == 0;
            
        } catch (NumberFormatException ne) {
            return false;
        }
    }
    
    protected static int validateCardBrand(String number, String brand) {
        String digit2 = number.substring(0,2);
        
        // for VISA prefix is 4 and lenght must be 13 || 16
        if (number.startsWith("4") && EnumCardType.VISA.getFdName().equalsIgnoreCase(brand))  {
            if (number.length() == 13 || number.length() == 16){
                return VISA;
            }
            
            // for MASTERCARD prefix is between 51..55 and length must be 16
        } else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0 && EnumCardType.MC.getFdName().equalsIgnoreCase(brand)) {
            if (number.length() == 16){
                return MASTERCARD;
            }
            
            //for AMEX prefix is 34 || 37 and length must be 15
        } else if ((digit2.equals("34") || digit2.equals("37")) && EnumCardType.AMEX.getFdName().equalsIgnoreCase(brand)) {
            if (number.length() == 15){
                return AMERICAN_EXPRESS;
            }
            
            //for DISC prefix is 60 and length must be 16
        } else if(digit2.equals("60") && EnumCardType.DISC.getFdName().equalsIgnoreCase(brand)){
            if(number.length() == 16){
                return DISCOVER;
            }
        }
        
        return INVALID;
    }

}
