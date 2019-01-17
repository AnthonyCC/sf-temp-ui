package com.freshdirect.webapp.ajax.oauth2.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;
import com.freshdirect.webapp.ajax.oauth2.util.FDUserTokenGenerator;

//import weblogic.auddi.util.Logger;

/**
 * This InMemoryDAO is a temporary solution to store all Tokens in memory. This
 * should be moved to persistent storage later.
 */
@Deprecated
public class OAuth2AccessTokenInMemoryDAO implements OAuth2AccessTokenDAO {

	private Set<String> storage;

	private static final OAuth2AccessTokenInMemoryDAO INSTANCE = new OAuth2AccessTokenInMemoryDAO();

	public OAuth2AccessTokenInMemoryDAO() {
		this.storage = new HashSet<String>();
	}

	public static OAuth2AccessTokenInMemoryDAO getDefault() {
		return INSTANCE;
	}

	@Override
	public void upsertToken(String accessToken) {
		if (accessToken == null) {
			return;
		}
		storage.add(accessToken);
	}

	@Override
	public OAuth2CodeAndTokenData fetchById(String accessToken) {
		if (accessToken == null) {
			return null;
		}

		if (!storage.contains(accessToken)) {
			return null;
		}

		try {
			FDUserTokenGenerator g = FDUserTokenGenerator.getDefault();
			String json = g.decrypt(accessToken);
			OAuth2CodeAndTokenData data = OAuth2CodeAndTokenData.fromJson(json);
			return data;
		} catch (Exception ex) {
		}
		return null;
	}

	@Override
	public void deleteToken(String accessTokenId) {
		if (accessTokenId == null) {
			return;
		}
		storage.remove(accessTokenId);
	}

	@Override
	public Collection<OAuth2CodeAndTokenData> fetchAllData() {
		Collection<OAuth2CodeAndTokenData> set = new TreeSet<OAuth2CodeAndTokenData>(new MyComparator());
		FDUserTokenGenerator g = FDUserTokenGenerator.getDefault();
		for (String tokenId : storage) {
			try {
				String json = g.decrypt(tokenId);
				OAuth2CodeAndTokenData data = OAuth2CodeAndTokenData.fromJson(json);
				set.add(data);
			} catch (Exception e) {
				//Logger.error(e.getMessage() + " : [ " + tokenId + " ]");
				continue;
			}
		}
		return set;
	}

	@Override
	public Collection<String> fetchAllId() {
		return storage;
	}

	@Override
	public void deleteAll() {
		storage.clear();
	}
}

class MyComparator implements Comparator<OAuth2CodeAndTokenData> {

	@Override
	public int compare(OAuth2CodeAndTokenData d1, OAuth2CodeAndTokenData d2) {
		return d1.getExpiresOn() > d2.getExpiresOn() ? 1 : -1;
	}

}