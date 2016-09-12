package com.spring.boot.oauth2.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.spring.boot.oauth2.service.OAuthService;

//访问令牌控制器
@Controller
public class AccessTokenController {
	@Autowired
	OAuthService oAuthService;
	
//accessToken?client_id=ci1&client_secret=cs1&grant_type=authorization_code&code=828beda907066d058584f37bcfd597b6
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/accessToken")
	public HttpEntity token(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		for (String string : keySet) {
			System.out.println("*"+string+"/"+parameterMap.get(string));
		}
		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

			// 检查提交的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("INVALID_CLIENT_DESCRIPTION").buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			// 检查客户端安全KEY是否正确
			if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription("INVALID_CLIENT_DESCRIPTION").buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!oAuthService.checkAuthCode(authCode)) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription("wrong authCode")
							.buildJSONMessage();
					return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
				}
			}

			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			oAuthService.addAccessToken(accessToken, oAuthService.getUsernameByAuthCode(authCode));

			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken).setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
					.buildJSONMessage();

			// 根据OAuthResponse生成ResponseEntity
			return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			// 构建错误响应
			OAuthResponse res = null;
			try {
				res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
						.buildJSONMessage();
			} catch (OAuthSystemException e1) {
				e1.printStackTrace();
			}
			return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
		return null;
	}

}
