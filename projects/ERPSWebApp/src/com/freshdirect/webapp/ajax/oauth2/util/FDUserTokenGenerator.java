package com.freshdirect.webapp.ajax.oauth2.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.ValueGenerator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2InvalidCodeTokenException;

public class FDUserTokenGenerator implements ValueGenerator {
	private static final Logger LOGGER = LoggerFactory.getInstance(FDUserTokenGenerator.class);

	private final String algorithm = "AES/CBC/PKCS5Padding";

	private static SecretKeySpec secretKey;
	private static IvParameterSpec iv;

	private String tokenData;

	// Use this singleton instance for decoding
	public static FDUserTokenGenerator INSTANCE = new FDUserTokenGenerator();

	public static FDUserTokenGenerator getDefault() {
		return INSTANCE;
	}

	public static FDUserTokenGenerator newInstance(String tokenData) {
		FDUserTokenGenerator g = new FDUserTokenGenerator();
		g.setTokenData(tokenData);
		return g;
	}

	public FDUserTokenGenerator() {
		String secret = FDStoreProperties.get("fdstore.oauth2.token.encryption.secret", "0123456789ABCDEF");
		String initVectorStr = FDStoreProperties.get("fdstore.oauth2.token.encryption.initVector", "0987654321QWERTY");
		init(secret, initVectorStr);
	}

	public FDUserTokenGenerator(String secret, String initVectorStr) {
		init(secret, initVectorStr);
	}

	public void init(String secret, String initVectorStr) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

			byte[] key = secret.getBytes("UTF-8");
			key = sha1.digest(key);
			key = Arrays.copyOf(key, 16); // 128 (no 256 bits in Java6)
			secretKey = new SecretKeySpec(key, "AES");

			byte[] ivBytes = initVectorStr.getBytes("UTF-8");
			ivBytes = sha1.digest(ivBytes);
			ivBytes = Arrays.copyOf(ivBytes, 16);
			iv = new IvParameterSpec(ivBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setTokenData(String tokenData) {
		this.tokenData = tokenData;
	}

	@Override
	public String generateValue() throws OAuthSystemException {
		if (this.tokenData != null) {
			return generateValue(this.tokenData);
		} else {
			LOGGER.error("Error while encrypting [null]");
		}
		return null;
	}

	@Override
	public String generateValue(String token) throws OAuthSystemException {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return DatatypeConverter.printBase64Binary(cipher.doFinal(token.getBytes("UTF-8")));
		} catch (Exception e) {
			LOGGER.error("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public String decrypt(String strToDecrypt) throws OAuth2InvalidCodeTokenException {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

			byte[] obfusBytes = DatatypeConverter.parseBase64Binary(strToDecrypt);
			byte[] ptBytes = cipher.doFinal(obfusBytes);

			return new String(ptBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2InvalidCodeTokenException("Error in decrypting: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws OAuthSystemException {
		FDUserTokenGenerator gg = new FDUserTokenGenerator("LAqUbCJ" + "/z5Y5OlT1", "7xm+7+" + "YqSRMqYyV6");
		try {
			System.out.println(gg.decrypt(
					"LPvv1OmZJLutSZNzc2bHs08zP2plOG7QrWxDLqBEA2wn6n7ehyYFfiav5ZHfVzskzUHGmGZGzJgw3ZHdUS1kdBd5GHF10yO9Q5j1I19awz7ZknqRcURbTYUxb/zDpXffWfN98NsexSABBWeHB4lJktmdePqZaNxzh16kzRUyAUv/pWqtdEuC+BJKkaYjAp2ZQeoFFDXw2k09L+9TGzxwt6BI3IbYtaJBPE2Wpi0xFh+SzV6KtjTD33kg6Rr0oznWZT98aAaQFP+7rUKj0Ji22ipzvJ7ofKHyuwafZgr3EHaSRoy8nsgssu34SFRXVZB2o6orS/BMDDMGJcdZkerqPw=="));
		} catch (OAuth2InvalidCodeTokenException e) {
			System.out.println(e.getMessage());
		}
	}
}
