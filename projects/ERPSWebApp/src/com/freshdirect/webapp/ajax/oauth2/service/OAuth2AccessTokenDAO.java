package com.freshdirect.webapp.ajax.oauth2.service;

import java.util.Collection;

import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;

/**
 * TODO this DAO could be moved out of 'service' package later. This layer of
 * operation handles CRUD of access token.
 */
@Deprecated
public interface OAuth2AccessTokenDAO {

	/** create/update an access token */
	public void upsertToken(String accessToken);

	/** fetch access token by id */
	public OAuth2CodeAndTokenData fetchById(String accessTokenId);

	/** delete an access token */
	public void deleteToken(String accessTokenId);
	
	/** delete all tokens */
	public void deleteAll();

	/** get all token data */
	public Collection<OAuth2CodeAndTokenData> fetchAllData();

	/** get all token strings */
	public Collection<String> fetchAllId();
}
