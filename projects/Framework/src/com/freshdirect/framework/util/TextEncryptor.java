package com.freshdirect.framework.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.FixedStringSaltGenerator;
import org.jasypt.util.text.BasicTextEncryptor;

public class TextEncryptor {

    public static String encrypt(String key, String plainText){
          StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();  
          FixedStringSaltGenerator g1=new FixedStringSaltGenerator();
          g1.setSalt(key);        
          textEncryptor.setSaltGenerator(g1);
          textEncryptor.setPassword(key); 
          return textEncryptor.encrypt(plainText);
    }
    

    public static String decrypt(String key, String encryptedText){
          StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();        
          FixedStringSaltGenerator g1=new FixedStringSaltGenerator();
          g1.setSalt(key);        
          textEncryptor.setSaltGenerator(g1);
          textEncryptor.setPassword(key);
          return textEncryptor.decrypt(encryptedText);
    }
    public static String decryptOld(String key, String encryptedText){
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();        
        //FixedStringSaltGenerator g1=new FixedStringSaltGenerator();
        //g1.setSalt(key);        
        //textEncryptor.setSaltGenerator(g1);
        textEncryptor.setPassword(key);
        return textEncryptor.decrypt(encryptedText);
  }

    public static void main(String args[]){
          String givexNum = "30854159664100018007";
          //System.out.println("b"+new String(b));
          //System.out.println("Certificate num "+givexNum.substring(11, givexNum.length()-1));
          //String encText = encrypt("5f4dcc3b5aa765d61d8327deb882cf99", "60362881161451447");
          //System.out.println("Encrypted version : "+encText);       
          System.out.println("Decrypted version : "+decrypt("5f4dcc3b5aa765d61d8327deb882cf99", "IJ0YWnpE+2dPdAC349J9+9NM5CowFJQE"));
    }

}

