package com.spring.boot.oauth2.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.entity.Client;
import com.spring.boot.oauth2.service.OAuthService;
import com.spring.boot.oauth2.service.Oauth2ClientService;

@Service
public class OAuthServiceImpl implements OAuthService{
	@Autowired
	Oauth2ClientService oauth2ClientService;
	
	private Map<String,String> cache = new HashMap<String, String>();

	@Override
	public void addAuthCode(String authCode, String username) {
		cache.put(authCode, username);
	}

	@Override
	public void addAccessToken(String accessToken, String username) {
		cache.put(accessToken, username);
	}

	@Override
	public boolean checkAuthCode(String authCode) {
		return cache.containsKey(authCode);
	}

	@Override
	public boolean checkAccessToken(String accessToken) {
		return cache.containsKey(accessToken);
	}

	@Override
	public String getUsernameByAuthCode(String authCode) {
		return cache.get(authCode);
	}

	@Override
	public String getUsernameByAccessToken(String accessToken) {
		return cache.get(accessToken);
	}

	@Override
	public long getExpireIn() {
		return 3600;
	}

	@Override
	public boolean checkClientId(String clientId) {
		Client findByClientId = oauth2ClientService.findByClientId(clientId);
		if(findByClientId!=null) return true;
		return false;
	}

	@Override
	public boolean checkClientSecret(String clientSecret) {
		Client findByClientId = oauth2ClientService.findByClientSecret(clientSecret);
		if(findByClientId!=null) return true;
		return false;
	}

}
