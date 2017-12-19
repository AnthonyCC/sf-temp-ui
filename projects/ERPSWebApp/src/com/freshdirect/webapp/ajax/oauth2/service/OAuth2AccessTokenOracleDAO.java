package com.freshdirect.webapp.ajax.oauth2.service;

import java.util.Collection;

import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;

/** PLACEHOLDER - Database DAO */
@Deprecated
public class OAuth2AccessTokenOracleDAO implements OAuth2AccessTokenDAO {

	private static final OAuth2AccessTokenOracleDAO INSTANCE = new OAuth2AccessTokenOracleDAO();

	public OAuth2AccessTokenOracleDAO() {
		// TODO db init
	}

	public static OAuth2AccessTokenOracleDAO getDefault() {
		return INSTANCE;
	}

	@Override
	public void upsertToken(String accessToken) {
		// TODO Auto-generated method stub

	}

	@Override
	public OAuth2CodeAndTokenData fetchById(String accessTokenId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteToken(String accessTokenId) {
		// TODO Auto-generated method stub
	}

	@Override
	public Collection<OAuth2CodeAndTokenData> fetchAllData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> fetchAllId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
