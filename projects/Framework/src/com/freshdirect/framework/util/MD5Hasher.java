package com.freshdirect.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class MD5Hasher {
	
	private MD5Hasher(){}
	private static Category LOGGER = LoggerFactory.getInstance(MD5Hasher.class);
	public static String hash (String toHash) 
	{
		if (toHash==null) {
			throw new IllegalArgumentException("String to be hashed cannot be null");
		}
		try {
			MessageDigest md = MessageDigest.getInstance( "MD5" );
			byte[] result = md.digest( toHash.getBytes() );
			return getString(result);
		} catch (NoSuchAlgorithmException nsae) {
			LOGGER.error("Catching NoSuchAlgorithm in MD5Hasher and returning null.",nsae);
			return null;
		}
	}
	
	private static String getString( byte[] bytes )	 {
	   StringBuffer sb = new StringBuffer();
	   for( int i=0; i<bytes.length; i++ )
	   {
		 byte b = bytes[ i ];
		 sb.append(Integer.toHexString(( b & 0xff ) + 0x100) .substring( 1 ));
	   }
	   return sb.toString();
	 }
	
	public static void main(String[] args){
		System.out.println(hash("test"));
	}
}
