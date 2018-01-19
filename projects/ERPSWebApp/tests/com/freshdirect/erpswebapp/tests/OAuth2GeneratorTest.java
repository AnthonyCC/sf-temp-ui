package com.freshdirect.erpswebapp.tests;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.junit.Test;

import com.freshdirect.webapp.ajax.oauth2.data.OAuth2InvalidCodeTokenException;
import com.freshdirect.webapp.ajax.oauth2.util.FDUserTokenGenerator;

import junit.framework.TestCase;

public class OAuth2GeneratorTest extends TestCase {

	private String plainText = "FreshDirect is an online grocer that delivers to "
			+ "residences and offices in the New York City metropolitan area. "
			+ "It also offers next-day delivery to much of New York City and parts "
			+ "of Nassau and Westchester Counties, New York; Fairfield County, "
			+ "Connecticut; Hoboken, Newark, and Jersey City; Philadelphia, Pennsylvania; "
			+ "and Washington, DC. FreshDirect custom-prepares groceries and meals for "
			+ "its customers, a manufacturing practice called Just In Time that reduces "
			+ "waste[citation needed] and improves quality and freshness. The service is "
			+ "popular for its distribution of organic food and locally grown items, as "
			+ "well as items that consumers see in supermarkets daily. It also delivers "
			+ "numerous kosher foods and is recognized by the Marine Stewardship Council "
			+ "as a certified sustainable seafood vendor.";

	@Test
	public void testSingleton() {
		FDUserTokenGenerator singleton1 = FDUserTokenGenerator.getDefault();
		assertNotNull(singleton1);

		FDUserTokenGenerator singleton2 = FDUserTokenGenerator.getDefault();
		assertEquals(singleton1, singleton2);
	}

	@Test
	public void testEncryption() throws OAuthSystemException, OAuth2InvalidCodeTokenException {
		FDUserTokenGenerator singleton = FDUserTokenGenerator.getDefault();
		assertNotNull(singleton);

		singleton.setTokenData(plainText);
		String cipher = singleton.generateValue();

		assertNotNull(cipher);
		assertNotSame("Error: cipher has same value as plainText", cipher, plainText);

		String decrypted = singleton.decrypt(cipher);
		assertNotNull(decrypted);
		assertNotSame("Error: Decrypted mesage object cannot be same plaintext object.", decrypted, plainText);
		assertEquals("Error: Decrypted mesage has different(wrong) content from original plaintext.", decrypted,
				plainText);
	}
}
