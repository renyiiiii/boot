package com.spring.boot.oauth2client.token;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Data;
@Data
public class OAuth2Token implements AuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String authCode;
	private String principal;

	public OAuth2Token(String authCode,String principal) {
		this.authCode = authCode;
		this.principal = principal;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public Object getCredentials() {
		return authCode;
	}

}
